package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import com.android.systemui.monet.ColorScheme;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda25 implements Runnable {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ MediaData f$10;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ Drawable f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ int f$8;
    public final /* synthetic */ ColorScheme f$9;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda25(MediaControlPanel mediaControlPanel, int i, String str, int i2, boolean z, boolean z2, Drawable drawable, int i3, int i4, ColorScheme colorScheme, MediaData mediaData) {
        this.f$0 = mediaControlPanel;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = i2;
        this.f$4 = z;
        this.f$5 = z2;
        this.f$6 = drawable;
        this.f$7 = i3;
        this.f$8 = i4;
        this.f$9 = colorScheme;
        this.f$10 = mediaData;
    }

    public final void run() {
        this.f$0.lambda$bindArtworkAndColors$10(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10);
    }
}
