package com.android.systemui.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class FragmentService implements Dumpable {
    public ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            for (FragmentHostState sendConfigurationChange : FragmentService.this.mHosts.values()) {
                sendConfigurationChange.sendConfigurationChange(configuration);
            }
        }
    };
    public final Handler mHandler = new Handler();
    public final ArrayMap<View, FragmentHostState> mHosts = new ArrayMap<>();
    public final ArrayMap<String, FragmentInstantiationInfo> mInjectionMap = new ArrayMap<>();

    public interface FragmentCreator {

        public interface Factory {
            FragmentCreator build();
        }

        QSFragment createQSFragment();
    }

    public FragmentService(FragmentCreator.Factory factory, ConfigurationController configurationController, DumpManager dumpManager) {
        addFragmentInstantiationProvider(factory.build());
        configurationController.addCallback(this.mConfigurationListener);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public ArrayMap<String, FragmentInstantiationInfo> getInjectionMap() {
        return this.mInjectionMap;
    }

    public void addFragmentInstantiationProvider(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (Fragment.class.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & 1) != 0) {
                String name = method.getReturnType().getName();
                if (this.mInjectionMap.containsKey(name)) {
                    Log.w("FragmentService", "Fragment " + name + " is already provided by different Dagger component; Not adding method");
                } else {
                    this.mInjectionMap.put(name, new FragmentInstantiationInfo(method, obj));
                }
            }
        }
    }

    public FragmentHostManager getFragmentHostManager(View view) {
        View rootView = view.getRootView();
        FragmentHostState fragmentHostState = this.mHosts.get(rootView);
        if (fragmentHostState == null) {
            fragmentHostState = new FragmentHostState(rootView);
            this.mHosts.put(rootView, fragmentHostState);
        }
        return fragmentHostState.getFragmentHostManager();
    }

    public void destroyAll() {
        for (FragmentHostState r0 : this.mHosts.values()) {
            r0.mFragmentHostManager.destroy();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Dumping fragments:");
        for (FragmentHostState r0 : this.mHosts.values()) {
            r0.mFragmentHostManager.getFragmentManager().dump("  ", (FileDescriptor) null, printWriter, strArr);
        }
    }

    public class FragmentHostState {
        public FragmentHostManager mFragmentHostManager;
        public final View mView;

        public FragmentHostState(View view) {
            this.mView = view;
            this.mFragmentHostManager = new FragmentHostManager(FragmentService.this, view);
        }

        public void sendConfigurationChange(Configuration configuration) {
            FragmentService.this.mHandler.post(new FragmentService$FragmentHostState$$ExternalSyntheticLambda0(this, configuration));
        }

        public FragmentHostManager getFragmentHostManager() {
            return this.mFragmentHostManager;
        }

        /* renamed from: handleSendConfigurationChange */
        public final void lambda$sendConfigurationChange$0(Configuration configuration) {
            this.mFragmentHostManager.onConfigurationChanged(configuration);
        }
    }

    public static class FragmentInstantiationInfo {
        public final Object mDaggerComponent;
        public final Method mMethod;

        public FragmentInstantiationInfo(Method method, Object obj) {
            this.mMethod = method;
            this.mDaggerComponent = obj;
        }
    }
}
