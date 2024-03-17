package com.android.systemui.fragments;

import android.app.Fragment;
import android.app.FragmentController;
import android.app.FragmentHostCallback;
import android.app.FragmentManager;
import android.app.FragmentManagerNonConfig;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Trace;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Dependency;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.util.leak.LeakDetector;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentHostManager {
    public final InterestingConfigChanges mConfigChanges;
    public final Context mContext;
    public FragmentController mFragments;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public FragmentManager.FragmentLifecycleCallbacks mLifecycleCallbacks;
    public final HashMap<String, ArrayList<FragmentListener>> mListeners = new HashMap<>();
    public final FragmentService mManager;
    public final ExtensionFragmentManager mPlugins;
    public final View mRootView;

    public interface FragmentListener {
        void onFragmentViewCreated(String str, Fragment fragment);

        void onFragmentViewDestroyed(String str, Fragment fragment) {
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public FragmentHostManager(FragmentService fragmentService, View view) {
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges(-1073741820);
        this.mConfigChanges = interestingConfigChanges;
        this.mPlugins = new ExtensionFragmentManager();
        Context context = view.getContext();
        this.mContext = context;
        this.mManager = fragmentService;
        this.mRootView = view;
        interestingConfigChanges.applyNewConfig(context.getResources());
        createFragmentHost((Parcelable) null);
    }

    public final void createFragmentHost(Parcelable parcelable) {
        FragmentController createController = FragmentController.createController(new HostCallbacks());
        this.mFragments = createController;
        createController.attachHost((Fragment) null);
        this.mLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
            public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
                FragmentHostManager.this.onFragmentViewCreated(fragment);
            }

            public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
                FragmentHostManager.this.onFragmentViewDestroyed(fragment);
            }

            public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
                ((LeakDetector) Dependency.get(LeakDetector.class)).trackGarbage(fragment);
            }
        };
        this.mFragments.getFragmentManager().registerFragmentLifecycleCallbacks(this.mLifecycleCallbacks, true);
        if (parcelable != null) {
            this.mFragments.restoreAllState(parcelable, (FragmentManagerNonConfig) null);
        }
        this.mFragments.dispatchCreate();
        this.mFragments.dispatchStart();
        this.mFragments.dispatchResume();
    }

    public final Parcelable destroyFragmentHost() {
        this.mFragments.dispatchPause();
        Parcelable saveAllState = this.mFragments.saveAllState();
        this.mFragments.dispatchStop();
        this.mFragments.dispatchDestroy();
        this.mFragments.getFragmentManager().unregisterFragmentLifecycleCallbacks(this.mLifecycleCallbacks);
        return saveAllState;
    }

    public FragmentHostManager addTagListener(String str, FragmentListener fragmentListener) {
        ArrayList arrayList = this.mListeners.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mListeners.put(str, arrayList);
        }
        arrayList.add(fragmentListener);
        Fragment findFragmentByTag = getFragmentManager().findFragmentByTag(str);
        if (!(findFragmentByTag == null || findFragmentByTag.getView() == null)) {
            fragmentListener.onFragmentViewCreated(str, findFragmentByTag);
        }
        return this;
    }

    public void removeTagListener(String str, FragmentListener fragmentListener) {
        ArrayList arrayList = this.mListeners.get(str);
        if (arrayList != null && arrayList.remove(fragmentListener) && arrayList.size() == 0) {
            this.mListeners.remove(str);
        }
    }

    public final void onFragmentViewCreated(Fragment fragment) {
        String tag = fragment.getTag();
        ArrayList arrayList = this.mListeners.get(tag);
        if (arrayList != null) {
            arrayList.forEach(new FragmentHostManager$$ExternalSyntheticLambda1(tag, fragment));
        }
    }

    public final void onFragmentViewDestroyed(Fragment fragment) {
        String tag = fragment.getTag();
        ArrayList arrayList = this.mListeners.get(tag);
        if (arrayList != null) {
            arrayList.forEach(new FragmentHostManager$$ExternalSyntheticLambda0(tag, fragment));
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            reloadFragments();
        } else {
            this.mFragments.dispatchConfigurationChanged(configuration);
        }
    }

    public final <T extends View> T findViewById(int i) {
        return this.mRootView.findViewById(i);
    }

    public FragmentManager getFragmentManager() {
        return this.mFragments.getFragmentManager();
    }

    public ExtensionFragmentManager getExtensionManager() {
        return this.mPlugins;
    }

    public void destroy() {
        this.mFragments.dispatchDestroy();
    }

    public <T> T create(Class<T> cls) {
        return this.mPlugins.instantiate(this.mContext, cls.getName(), (Bundle) null);
    }

    public static FragmentHostManager get(View view) {
        try {
            return ((FragmentService) Dependency.get(FragmentService.class)).getFragmentHostManager(view);
        } catch (ClassCastException e) {
            throw e;
        }
    }

    public void reloadFragments() {
        Trace.beginSection("FrargmentHostManager#reloadFragments");
        createFragmentHost(destroyFragmentHost());
        Trace.endSection();
    }

    public class HostCallbacks extends FragmentHostCallback<FragmentHostManager> {
        public void onAttachFragment(Fragment fragment) {
        }

        public int onGetWindowAnimations() {
            return 0;
        }

        public boolean onHasView() {
            return true;
        }

        public boolean onHasWindowAnimations() {
            return false;
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return true;
        }

        public boolean onUseFragmentManagerInflaterFactory() {
            return true;
        }

        public HostCallbacks() {
            super(FragmentHostManager.this.mContext, FragmentHostManager.this.mHandler, 0);
        }

        public FragmentHostManager onGetHost() {
            return FragmentHostManager.this;
        }

        public void onDump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            FragmentHostManager.this.dump(str, fileDescriptor, printWriter, strArr);
        }

        public Fragment instantiate(Context context, String str, Bundle bundle) {
            return FragmentHostManager.this.mPlugins.instantiate(context, str, bundle);
        }

        public LayoutInflater onGetLayoutInflater() {
            return LayoutInflater.from(FragmentHostManager.this.mContext);
        }

        public <T extends View> T onFindViewById(int i) {
            return FragmentHostManager.this.findViewById(i);
        }
    }

    public class ExtensionFragmentManager {
        public final ArrayMap<String, Context> mExtensionLookup = new ArrayMap<>();

        public ExtensionFragmentManager() {
        }

        public void setCurrentExtension(int i, String str, String str2, String str3, Context context) {
            if (str2 != null) {
                this.mExtensionLookup.remove(str2);
            }
            this.mExtensionLookup.put(str3, context);
            FragmentHostManager.this.getFragmentManager().beginTransaction().replace(i, instantiate(context, str3, (Bundle) null), str).commit();
            FragmentHostManager.this.reloadFragments();
        }

        public Fragment instantiate(Context context, String str, Bundle bundle) {
            Context context2 = this.mExtensionLookup.get(str);
            if (context2 == null) {
                return instantiateWithInjections(context, str, bundle);
            }
            Fragment instantiateWithInjections = instantiateWithInjections(context2, str, bundle);
            if (instantiateWithInjections instanceof Plugin) {
                ((Plugin) instantiateWithInjections).onCreate(FragmentHostManager.this.mContext, context2);
            }
            return instantiateWithInjections;
        }

        public final Fragment instantiateWithInjections(Context context, String str, Bundle bundle) {
            FragmentService.FragmentInstantiationInfo fragmentInstantiationInfo = FragmentHostManager.this.mManager.getInjectionMap().get(str);
            if (fragmentInstantiationInfo == null) {
                return Fragment.instantiate(context, str, bundle);
            }
            try {
                Fragment fragment = (Fragment) fragmentInstantiationInfo.mMethod.invoke(fragmentInstantiationInfo.mDaggerComponent, new Object[0]);
                if (bundle != null) {
                    bundle.setClassLoader(fragment.getClass().getClassLoader());
                    fragment.setArguments(bundle);
                }
                return fragment;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new Fragment.InstantiationException("Unable to instantiate " + str, e);
            }
        }
    }
}
