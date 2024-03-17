package com.android.systemui.statusbar.notification.collection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: TargetSdkResolver.kt */
public final class TargetSdkResolver {
    @NotNull
    public final String TAG = "TargetSdkResolver";
    @NotNull
    public final Context context;

    public TargetSdkResolver(@NotNull Context context2) {
        this.context = context2;
    }

    public final void initialize(@NotNull CommonNotifCollection commonNotifCollection) {
        commonNotifCollection.addCollectionListener(new TargetSdkResolver$initialize$1(this));
    }

    public final int resolveNotificationSdk(StatusBarNotification statusBarNotification) {
        try {
            return CentralSurfaces.getPackageManagerForUser(this.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.TAG, Intrinsics.stringPlus("Failed looking up ApplicationInfo for ", statusBarNotification.getPackageName()), e);
            return 0;
        }
    }
}
