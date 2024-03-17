package com.android.systemui.dreams;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.RemoteException;
import android.service.dreams.IDreamManager;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$bool;

public class DreamOverlayRegistrant extends CoreStartable {
    public static final boolean DEBUG = Log.isLoggable("DreamOverlayRegistrant", 3);
    public boolean mCurrentRegisteredState;
    public final IDreamManager mDreamManager;
    public final ComponentName mOverlayServiceComponent;
    public final BroadcastReceiver mReceiver;
    public final Resources mResources;

    public final void registerOverlayService() {
        String str;
        PackageManager packageManager = this.mContext.getPackageManager();
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent);
        boolean z = false;
        if (componentEnabledSetting != 3) {
            int i = this.mResources.getBoolean(R$bool.config_dreamOverlayServiceEnabled) ? 1 : 2;
            if (i != componentEnabledSetting) {
                packageManager.setComponentEnabledSetting(this.mOverlayServiceComponent, i, 0);
            }
        }
        if (packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent) == 1) {
            z = true;
        }
        if (this.mCurrentRegisteredState != z) {
            this.mCurrentRegisteredState = z;
            try {
                if (DEBUG) {
                    if (z) {
                        str = "registering dream overlay service:" + this.mOverlayServiceComponent;
                    } else {
                        str = "clearing dream overlay service";
                    }
                    Log.d("DreamOverlayRegistrant", str);
                }
                this.mDreamManager.registerDreamOverlayService(this.mCurrentRegisteredState ? this.mOverlayServiceComponent : null);
            } catch (RemoteException e) {
                Log.e("DreamOverlayRegistrant", "could not register dream overlay service:" + e);
            }
        }
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(this.mOverlayServiceComponent.getPackageName(), 0);
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        registerOverlayService();
    }
}
