package com.android.systemui.statusbar.phone.fragment;

import android.content.Intent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CollapsedStatusBarFragment$$ExternalSyntheticLambda4 implements View.OnLongClickListener {
    public final boolean onLongClick(View view) {
        return view.getContext().sendBroadcast(new Intent("SYSTEM_ACTION_SLITSCREEN"));
    }
}
