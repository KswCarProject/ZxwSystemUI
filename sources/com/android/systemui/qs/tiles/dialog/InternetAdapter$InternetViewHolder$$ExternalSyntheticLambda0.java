package com.android.systemui.qs.tiles.dialog;

import android.view.View;
import com.android.systemui.qs.tiles.dialog.InternetAdapter;
import com.android.wifitrackerlib.WifiEntry;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ InternetAdapter.InternetViewHolder f$0;
    public final /* synthetic */ WifiEntry f$1;

    public /* synthetic */ InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda0(InternetAdapter.InternetViewHolder internetViewHolder, WifiEntry wifiEntry) {
        this.f$0 = internetViewHolder;
        this.f$1 = wifiEntry;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBind$0(this.f$1, view);
    }
}
