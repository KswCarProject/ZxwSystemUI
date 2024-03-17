package com.android.wm.shell.pip;

import android.media.session.MediaSession;
import com.android.wm.shell.pip.PipMediaController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ MediaSession.Token f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda3(MediaSession.Token token) {
        this.f$0 = token;
    }

    public final void accept(Object obj) {
        ((PipMediaController.TokenListener) obj).onMediaSessionTokenChanged(this.f$0);
    }
}
