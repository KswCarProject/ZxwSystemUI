package com.android.systemui.navigationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.content.pm.PackageManager;
import android.content.res.ApkAssets;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class NavigationModeController implements Dumpable {
    public static final String TAG = "NavigationModeController";
    public final Context mContext;
    public Context mCurrentUserContext;
    public final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedCallback;
    public ArrayList<ModeChangedListener> mListeners = new ArrayList<>();
    public final IOverlayManager mOverlayManager;
    public BroadcastReceiver mReceiver;
    public final Executor mUiBgExecutor;

    public interface ModeChangedListener {
        void onNavigationModeChanged(int i);
    }

    public NavigationModeController(Context context, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, Executor executor, DumpManager dumpManager) {
        AnonymousClass1 r0 = new DeviceProvisionedController.DeviceProvisionedListener() {
            public void onUserSwitched() {
                String r0 = NavigationModeController.TAG;
                Log.d(r0, "onUserSwitched: " + ActivityManagerWrapper.getInstance().getCurrentUserId());
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        };
        this.mDeviceProvisionedCallback = r0;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.d(NavigationModeController.TAG, "ACTION_OVERLAY_CHANGED");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        };
        this.mContext = context;
        this.mCurrentUserContext = context;
        this.mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
        this.mUiBgExecutor = executor;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        deviceProvisionedController.addCallback(r0);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.OVERLAY_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(ThemeOverlayApplier.ANDROID_PACKAGE, 0);
        context.registerReceiverAsUser(this.mReceiver, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                Log.d(NavigationModeController.TAG, "onOverlayChanged");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        });
        updateCurrentInteractionMode(false);
    }

    public void updateCurrentInteractionMode(boolean z) {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        int currentInteractionMode = getCurrentInteractionMode(currentUserContext);
        this.mUiBgExecutor.execute(new NavigationModeController$$ExternalSyntheticLambda0(this, currentInteractionMode));
        String str = TAG;
        Log.d(str, "updateCurrentInteractionMode: mode=" + currentInteractionMode);
        dumpAssetPaths(this.mCurrentUserContext);
        if (z) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onNavigationModeChanged(currentInteractionMode);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentInteractionMode$0(int i) {
        Settings.Secure.putString(this.mCurrentUserContext.getContentResolver(), "navigation_mode", String.valueOf(i));
    }

    public int addListener(ModeChangedListener modeChangedListener) {
        this.mListeners.add(modeChangedListener);
        return getCurrentInteractionMode(this.mCurrentUserContext);
    }

    public void removeListener(ModeChangedListener modeChangedListener) {
        this.mListeners.remove(modeChangedListener);
    }

    public boolean getImeDrawsImeNavBar() {
        return this.mCurrentUserContext.getResources().getBoolean(17891684);
    }

    public final int getCurrentInteractionMode(Context context) {
        int integer = context.getResources().getInteger(17694881);
        String str = TAG;
        Log.d(str, "getCurrentInteractionMode: mode=" + integer + " contextUser=" + context.getUserId());
        return integer;
    }

    public Context getCurrentUserContext() {
        int currentUserId = ActivityManagerWrapper.getInstance().getCurrentUserId();
        String str = TAG;
        Log.d(str, "getCurrentUserContext: contextUser=" + this.mContext.getUserId() + " currentUser=" + currentUserId);
        if (this.mContext.getUserId() == currentUserId) {
            return this.mContext;
        }
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(currentUserId));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to create package context", e);
            return null;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        String str;
        printWriter.println("NavigationModeController:");
        printWriter.println("  mode=" + getCurrentInteractionMode(this.mCurrentUserContext));
        try {
            str = String.join(", ", this.mOverlayManager.getDefaultOverlayPackages());
        } catch (RemoteException unused) {
            str = "failed_to_fetch";
        }
        printWriter.println("  defaultOverlays=" + str);
        dumpAssetPaths(this.mCurrentUserContext);
    }

    public final void dumpAssetPaths(Context context) {
        String str = TAG;
        Log.d(str, "  contextUser=" + this.mCurrentUserContext.getUserId());
        Log.d(str, "  assetPaths=");
        for (ApkAssets apkAssets : context.getResources().getAssets().getApkAssets()) {
            Log.d(TAG, "    " + apkAssets.getDebugName());
        }
    }
}
