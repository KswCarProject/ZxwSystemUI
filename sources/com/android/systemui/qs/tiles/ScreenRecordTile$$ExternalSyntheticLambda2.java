package com.android.systemui.qs.tiles;

import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.screenrecord.ScreenRecordDialog;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenRecordTile$$ExternalSyntheticLambda2 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ ScreenRecordTile f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ScreenRecordDialog f$2;
    public final /* synthetic */ View f$3;

    public /* synthetic */ ScreenRecordTile$$ExternalSyntheticLambda2(ScreenRecordTile screenRecordTile, boolean z, ScreenRecordDialog screenRecordDialog, View view) {
        this.f$0 = screenRecordTile;
        this.f$1 = z;
        this.f$2 = screenRecordDialog;
        this.f$3 = view;
    }

    public final boolean onDismiss() {
        return this.f$0.lambda$showPrompt$2(this.f$1, this.f$2, this.f$3);
    }
}
