package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createPinDialog$1 extends AlertDialog {
    public ChallengeDialogs$createPinDialog$1(Context context) {
        super(context, 16974545);
    }

    public void dismiss() {
        View decorView;
        InputMethodManager inputMethodManager;
        Window window = getWindow();
        if (!(window == null || (decorView = window.getDecorView()) == null || (inputMethodManager = (InputMethodManager) decorView.getContext().getSystemService(InputMethodManager.class)) == null)) {
            inputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
        }
        super.dismiss();
    }
}
