package com.android.systemui.recents;

import android.content.ContentResolver;
import android.provider.Settings;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Recents$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ContentResolver f$0;

    public /* synthetic */ Recents$$ExternalSyntheticLambda0(ContentResolver contentResolver) {
        this.f$0 = contentResolver;
    }

    public final void run() {
        Settings.System.putInt(this.f$0, "zxw.Launcher3.SplitSceen", 0);
    }
}
