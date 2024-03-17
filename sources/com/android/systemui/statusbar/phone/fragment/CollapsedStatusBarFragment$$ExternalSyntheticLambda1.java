package com.android.systemui.statusbar.phone.fragment;

import android.content.Intent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CollapsedStatusBarFragment$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final void onClick(View view) {
        view.getContext().sendBroadcast(new Intent("SYSTEM_ACTION_HOME"));
    }
}
