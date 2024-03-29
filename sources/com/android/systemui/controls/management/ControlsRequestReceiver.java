package com.android.systemui.controls.management;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.service.controls.Control;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsRequestReceiver.kt */
public final class ControlsRequestReceiver extends BroadcastReceiver {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* compiled from: ControlsRequestReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final boolean isPackageInForeground(@NotNull Context context, @NotNull String str) {
            int i;
            try {
                int packageUid = context.getPackageManager().getPackageUid(str, 0);
                ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                if (activityManager == null) {
                    i = 1000;
                } else {
                    i = activityManager.getUidImportance(packageUid);
                }
                if (i == 100) {
                    return true;
                }
                Log.w("ControlsRequestReceiver", "Uid " + packageUid + " not in foreground");
                return false;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("ControlsRequestReceiver", "Package " + str + " not found");
                return false;
            }
        }
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        String str;
        if (context.getPackageManager().hasSystemFeature("android.software.controls")) {
            try {
                ComponentName componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME");
                try {
                    Control parcelableExtra = intent.getParcelableExtra("android.service.controls.extra.CONTROL");
                    if (componentName == null) {
                        str = null;
                    } else {
                        str = componentName.getPackageName();
                    }
                    if (str != null && Companion.isPackageInForeground(context, str)) {
                        Intent intent2 = new Intent(context, ControlsRequestDialog.class);
                        intent2.putExtra("android.intent.extra.COMPONENT_NAME", componentName);
                        intent2.putExtra("android.service.controls.extra.CONTROL", parcelableExtra);
                        intent2.addFlags(268566528);
                        intent2.putExtra("android.intent.extra.USER_ID", context.getUserId());
                        context.startActivityAsUser(intent2, UserHandle.SYSTEM);
                    }
                } catch (ClassCastException e) {
                    Log.e("ControlsRequestReceiver", "Malformed intent extra Control", e);
                }
            } catch (ClassCastException e2) {
                Log.e("ControlsRequestReceiver", "Malformed intent extra ComponentName", e2);
            }
        }
    }
}
