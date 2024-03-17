package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda27 implements View.OnClickListener {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ ImageButton f$1;
    public final /* synthetic */ Runnable f$2;
    public final /* synthetic */ Drawable f$3;
    public final /* synthetic */ Drawable f$4;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda27(MediaControlPanel mediaControlPanel, ImageButton imageButton, Runnable runnable, Drawable drawable, Drawable drawable2) {
        this.f$0 = mediaControlPanel;
        this.f$1 = imageButton;
        this.f$2 = runnable;
        this.f$3 = drawable;
        this.f$4 = drawable2;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindButtonCommon$13(this.f$1, this.f$2, this.f$3, this.f$4, view);
    }
}
