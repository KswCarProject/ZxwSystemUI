package com.android.wm.shell.pip.tv;

import android.media.session.MediaSession;
import com.android.wm.shell.pip.PipMediaController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipNotificationController$$ExternalSyntheticLambda1 implements PipMediaController.TokenListener {
    public final /* synthetic */ TvPipNotificationController f$0;

    public /* synthetic */ TvPipNotificationController$$ExternalSyntheticLambda1(TvPipNotificationController tvPipNotificationController) {
        this.f$0 = tvPipNotificationController;
    }

    public final void onMediaSessionTokenChanged(MediaSession.Token token) {
        this.f$0.onMediaSessionTokenChanged(token);
    }
}
