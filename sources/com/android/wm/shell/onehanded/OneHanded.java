package com.android.wm.shell.onehanded;

import android.content.res.Configuration;
import android.os.SystemProperties;

public interface OneHanded {
    public static final boolean sIsSupportOneHandedMode = SystemProperties.getBoolean("ro.support_one_handed_mode", false);

    IOneHanded createExternalInterface() {
        return null;
    }

    void onConfigChanged(Configuration configuration);

    void onKeyguardVisibilityChanged(boolean z);

    void onUserSwitch(int i);

    void registerEventCallback(OneHandedEventCallback oneHandedEventCallback);

    void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback);

    void setLockedDisabled(boolean z, boolean z2);

    void stopOneHanded();

    void stopOneHanded(int i);
}
