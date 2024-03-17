package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.safetycenter.SafetyCenterManager;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Objects;

public class SafetyController implements CallbackController<Listener> {
    public static final IntentFilter PKG_CHANGE_INTENT_FILTER;
    public final Handler mBgHandler;
    public final Context mContext;
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    public final PackageManager mPackageManager;
    @VisibleForTesting
    public final BroadcastReceiver mPermControllerChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getData() != null ? intent.getData().getSchemeSpecificPart() : null, SafetyController.this.mPackageManager.getPermissionControllerPackageName())) {
                boolean r2 = SafetyController.this.mSafetyCenterEnabled;
                SafetyController safetyController = SafetyController.this;
                safetyController.mSafetyCenterEnabled = safetyController.mSafetyCenterManager.isSafetyCenterEnabled();
                if (r2 != SafetyController.this.mSafetyCenterEnabled) {
                    SafetyController.this.mBgHandler.post(new SafetyController$1$$ExternalSyntheticLambda0(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            SafetyController.this.handleSafetyCenterEnableChange();
        }
    };
    public boolean mSafetyCenterEnabled;
    public final SafetyCenterManager mSafetyCenterManager;

    public interface Listener {
        void onSafetyCenterEnableChanged(boolean z);
    }

    static {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_CHANGED");
        PKG_CHANGE_INTENT_FILTER = intentFilter;
        intentFilter.addDataScheme("package");
    }

    public SafetyController(Context context, PackageManager packageManager, SafetyCenterManager safetyCenterManager, Handler handler) {
        this.mContext = context;
        this.mSafetyCenterManager = safetyCenterManager;
        this.mPackageManager = packageManager;
        this.mBgHandler = handler;
        this.mSafetyCenterEnabled = safetyCenterManager.isSafetyCenterEnabled();
    }

    public boolean isSafetyCenterEnabled() {
        return this.mSafetyCenterEnabled;
    }

    public void addCallback(Listener listener) {
        synchronized (this.mListeners) {
            this.mListeners.add(listener);
            if (this.mListeners.size() == 1) {
                this.mContext.registerReceiver(this.mPermControllerChangeReceiver, PKG_CHANGE_INTENT_FILTER);
                this.mBgHandler.post(new SafetyController$$ExternalSyntheticLambda0(this, listener));
            } else {
                listener.onSafetyCenterEnableChanged(isSafetyCenterEnabled());
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addCallback$0(Listener listener) {
        this.mSafetyCenterEnabled = this.mSafetyCenterManager.isSafetyCenterEnabled();
        listener.onSafetyCenterEnableChanged(isSafetyCenterEnabled());
    }

    public void removeCallback(Listener listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
            if (this.mListeners.isEmpty()) {
                this.mContext.unregisterReceiver(this.mPermControllerChangeReceiver);
            }
        }
    }

    public final void handleSafetyCenterEnableChange() {
        synchronized (this.mListeners) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onSafetyCenterEnableChanged(this.mSafetyCenterEnabled);
            }
        }
    }
}
