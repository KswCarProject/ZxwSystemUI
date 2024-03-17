package com.android.systemui.media.dialog;

import android.content.DialogInterface;
import android.widget.EditText;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputBroadcastDialog$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MediaOutputBroadcastDialog f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ EditText f$2;

    public /* synthetic */ MediaOutputBroadcastDialog$$ExternalSyntheticLambda4(MediaOutputBroadcastDialog mediaOutputBroadcastDialog, boolean z, EditText editText) {
        this.f$0 = mediaOutputBroadcastDialog;
        this.f$1 = z;
        this.f$2 = editText;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$launchBroadcastUpdatedDialog$4(this.f$1, this.f$2, dialogInterface, i);
    }
}
