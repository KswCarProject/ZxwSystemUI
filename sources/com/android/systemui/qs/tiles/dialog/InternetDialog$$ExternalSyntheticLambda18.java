package com.android.systemui.qs.tiles.dialog;

import com.android.wifitrackerlib.WifiEntry;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ InternetDialog f$0;
    public final /* synthetic */ WifiEntry f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda18(InternetDialog internetDialog, WifiEntry wifiEntry, List list, boolean z, boolean z2) {
        this.f$0 = internetDialog;
        this.f$1 = wifiEntry;
        this.f$2 = list;
        this.f$3 = z;
        this.f$4 = z2;
    }

    public final void run() {
        this.f$0.lambda$onAccessPointsChanged$23(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
