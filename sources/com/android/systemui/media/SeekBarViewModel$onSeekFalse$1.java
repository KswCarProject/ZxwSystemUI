package com.android.systemui.media;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$onSeekFalse$1 implements Runnable {
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$onSeekFalse$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    public final void run() {
        if (this.this$0.scrubbing) {
            this.this$0.isFalseSeek = true;
        }
    }
}
