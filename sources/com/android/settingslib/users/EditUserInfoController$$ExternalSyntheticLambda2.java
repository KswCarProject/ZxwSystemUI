package com.android.settingslib.users;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EditUserInfoController f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda2(EditUserInfoController editUserInfoController, Runnable runnable) {
        this.f$0 = editUserInfoController;
        this.f$1 = runnable;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$buildDialog$2(this.f$1, dialogInterface, i);
    }
}
