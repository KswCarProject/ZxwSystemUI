package com.android.systemui.statusbar.connectivity;

import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessPointController.kt */
public interface AccessPointController {

    /* compiled from: AccessPointController.kt */
    public interface AccessPointCallback {
        void onAccessPointsChanged(@NotNull List<WifiEntry> list);
    }

    void addAccessPointCallback(@NotNull AccessPointCallback accessPointCallback);

    boolean canConfigMobileData();

    boolean canConfigWifi();

    @Nullable
    MergedCarrierEntry getMergedCarrierEntry();

    void removeAccessPointCallback(@NotNull AccessPointCallback accessPointCallback);

    void scanForAccessPoints();
}
