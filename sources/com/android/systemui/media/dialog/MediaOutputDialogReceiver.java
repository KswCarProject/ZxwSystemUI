package com.android.systemui.media.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaOutputDialogReceiver.kt */
public final class MediaOutputDialogReceiver extends BroadcastReceiver {
    @NotNull
    public final MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory;
    @NotNull
    public final MediaOutputDialogFactory mediaOutputDialogFactory;

    public MediaOutputDialogReceiver(@NotNull MediaOutputDialogFactory mediaOutputDialogFactory2, @NotNull MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory2) {
        this.mediaOutputDialogFactory = mediaOutputDialogFactory2;
        this.mediaOutputBroadcastDialogFactory = mediaOutputBroadcastDialogFactory2;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if (TextUtils.equals("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG", intent.getAction())) {
            String stringExtra = intent.getStringExtra("package_name");
            if (!TextUtils.isEmpty(stringExtra)) {
                MediaOutputDialogFactory mediaOutputDialogFactory2 = this.mediaOutputDialogFactory;
                Intrinsics.checkNotNull(stringExtra);
                MediaOutputDialogFactory.create$default(mediaOutputDialogFactory2, stringExtra, false, (View) null, 4, (Object) null);
            } else if (MediaOutputDialogReceiverKt.DEBUG) {
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output dialog. Package name is empty.");
            }
        } else if (TextUtils.equals("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_BROADCAST_DIALOG", intent.getAction())) {
            String stringExtra2 = intent.getStringExtra("package_name");
            if (!TextUtils.isEmpty(stringExtra2)) {
                MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory2 = this.mediaOutputBroadcastDialogFactory;
                Intrinsics.checkNotNull(stringExtra2);
                MediaOutputBroadcastDialogFactory.create$default(mediaOutputBroadcastDialogFactory2, stringExtra2, false, (View) null, 4, (Object) null);
            } else if (MediaOutputDialogReceiverKt.DEBUG) {
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output broadcast dialog. Package name is empty.");
            }
        }
    }
}
