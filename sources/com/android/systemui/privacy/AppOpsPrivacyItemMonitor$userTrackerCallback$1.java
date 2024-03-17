package com.android.systemui.privacy;

import android.content.Context;
import android.content.pm.UserInfo;
import com.android.systemui.settings.UserTracker;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$userTrackerCallback$1 implements UserTracker.Callback {
    public final /* synthetic */ AppOpsPrivacyItemMonitor this$0;

    public AppOpsPrivacyItemMonitor$userTrackerCallback$1(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor) {
        this.this$0 = appOpsPrivacyItemMonitor;
    }

    public void onUserChanged(int i, @NotNull Context context) {
        this.this$0.onCurrentProfilesChanged();
    }

    public void onProfilesChanged(@NotNull List<? extends UserInfo> list) {
        this.this$0.onCurrentProfilesChanged();
    }
}
