package com.android.systemui.privacy;

import android.content.pm.UserInfo;
import android.os.UserHandle;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.privacy.AppOpsPrivacyItemMonitor;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$appOpsCallback$1 implements AppOpsController.Callback {
    public final /* synthetic */ AppOpsPrivacyItemMonitor this$0;

    public AppOpsPrivacyItemMonitor$appOpsCallback$1(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor) {
        this.this$0 = appOpsPrivacyItemMonitor;
    }

    public void onActiveStateChanged(int i, int i2, @NotNull String str, boolean z) {
        boolean z2;
        Object access$getLock$p = this.this$0.lock;
        AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            AppOpsPrivacyItemMonitor.Companion companion = AppOpsPrivacyItemMonitor.Companion;
            if (ArraysKt___ArraysKt.contains(companion.getOPS_MIC_CAMERA(), i) && !appOpsPrivacyItemMonitor.micCameraAvailable) {
                return;
            }
            if (!ArraysKt___ArraysKt.contains(companion.getOPS_LOCATION(), i) || appOpsPrivacyItemMonitor.locationAvailable) {
                Iterable userProfiles = appOpsPrivacyItemMonitor.userTracker.getUserProfiles();
                boolean z3 = true;
                if (!(userProfiles instanceof Collection) || !((Collection) userProfiles).isEmpty()) {
                    Iterator it = userProfiles.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (((UserInfo) it.next()).id == UserHandle.getUserId(i2)) {
                            z2 = true;
                            continue;
                        } else {
                            z2 = false;
                            continue;
                        }
                        if (z2) {
                            break;
                        }
                    }
                    if (z3 || ArraysKt___ArraysKt.contains(AppOpsPrivacyItemMonitor.Companion.getUSER_INDEPENDENT_OPS(), i)) {
                        appOpsPrivacyItemMonitor.logger.logUpdatedItemFromAppOps(i, i2, str, z);
                        appOpsPrivacyItemMonitor.dispatchOnPrivacyItemsChanged();
                    }
                    Unit unit = Unit.INSTANCE;
                }
                z3 = false;
                appOpsPrivacyItemMonitor.logger.logUpdatedItemFromAppOps(i, i2, str, z);
                appOpsPrivacyItemMonitor.dispatchOnPrivacyItemsChanged();
                Unit unit2 = Unit.INSTANCE;
            }
        }
    }
}
