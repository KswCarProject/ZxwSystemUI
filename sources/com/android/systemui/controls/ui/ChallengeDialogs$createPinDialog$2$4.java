package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.systemui.R$id;

/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createPinDialog$2$4 implements DialogInterface.OnShowListener {
    public final /* synthetic */ int $instructions;
    public final /* synthetic */ ChallengeDialogs$createPinDialog$1 $this_apply;
    public final /* synthetic */ boolean $useAlphaNumeric;

    public ChallengeDialogs$createPinDialog$2$4(ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1, int i, boolean z) {
        this.$this_apply = challengeDialogs$createPinDialog$1;
        this.$instructions = i;
        this.$useAlphaNumeric = z;
    }

    public final void onShow(DialogInterface dialogInterface) {
        final EditText editText = (EditText) this.$this_apply.requireViewById(R$id.controls_pin_input);
        editText.setHint(this.$instructions);
        ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1 = this.$this_apply;
        int i = R$id.controls_pin_use_alpha;
        final CheckBox checkBox = (CheckBox) challengeDialogs$createPinDialog$1.requireViewById(i);
        checkBox.setChecked(this.$useAlphaNumeric);
        ChallengeDialogs.INSTANCE.setInputType(editText, checkBox.isChecked());
        ((CheckBox) this.$this_apply.requireViewById(i)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ChallengeDialogs.INSTANCE.setInputType(editText, checkBox.isChecked());
            }
        });
        editText.requestFocus();
    }
}
