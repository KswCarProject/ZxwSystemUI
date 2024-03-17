package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.service.controls.actions.ControlAction;
import android.widget.EditText;
import com.android.systemui.R$id;

/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createPinDialog$2$1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ ControlAction $lastAction;

    public ChallengeDialogs$createPinDialog$2$1(ControlViewHolder controlViewHolder, ControlAction controlAction) {
        this.$cvh = controlViewHolder;
        this.$lastAction = controlAction;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            int i2 = R$id.controls_pin_input;
            dialog.requireViewById(i2);
            this.$cvh.action(ChallengeDialogs.INSTANCE.addChallengeValue(this.$lastAction, ((EditText) dialog.requireViewById(i2)).getText().toString()));
            dialogInterface.dismiss();
        }
    }
}
