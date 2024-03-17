package com.android.systemui.media;

import android.app.PendingIntent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda9 implements View.OnClickListener {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ PendingIntent f$1;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda9(MediaControlPanel mediaControlPanel, PendingIntent pendingIntent) {
        this.f$0 = mediaControlPanel;
        this.f$1 = pendingIntent;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindPlayer$4(this.f$1, view);
    }
}
