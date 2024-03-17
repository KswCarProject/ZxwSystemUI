package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import org.jetbrains.annotations.Nullable;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$callback$1 extends MediaController.Callback {
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$callback$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    public void onPlaybackStateChanged(@Nullable PlaybackState playbackState) {
        this.this$0.playbackState = playbackState;
        if (this.this$0.playbackState != null) {
            Integer num = 0;
            if (!num.equals(this.this$0.playbackState)) {
                this.this$0.checkIfPollingNeeded();
                return;
            }
        }
        this.this$0.clearController();
    }

    public void onSessionDestroyed() {
        this.this$0.clearController();
    }
}
