package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import android.service.controls.actions.ControlAction;

/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createConfirmationDialog$builder$1$1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ ControlAction $lastAction;

    public ChallengeDialogs$createConfirmationDialog$builder$1$1(ControlViewHolder controlViewHolder, ControlAction controlAction) {
        this.$cvh = controlViewHolder;
        this.$lastAction = controlAction;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.$cvh.action(ChallengeDialogs.INSTANCE.addChallengeValue(this.$lastAction, "true"));
        dialogInterface.dismiss();
    }
}
