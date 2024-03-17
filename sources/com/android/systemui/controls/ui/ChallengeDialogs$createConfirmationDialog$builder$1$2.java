package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createConfirmationDialog$builder$1$2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ Function0<Unit> $onCancel;

    public ChallengeDialogs$createConfirmationDialog$builder$1$2(Function0<Unit> function0) {
        this.$onCancel = function0;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.$onCancel.invoke();
        dialogInterface.cancel();
    }
}
