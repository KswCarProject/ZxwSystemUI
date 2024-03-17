package com.android.systemui.media;

/* compiled from: SeekBarViewModel.kt */
public /* synthetic */ class SeekBarViewModel$checkIfPollingNeeded$1 implements Runnable {
    public final /* synthetic */ SeekBarViewModel $tmp0;

    public SeekBarViewModel$checkIfPollingNeeded$1(SeekBarViewModel seekBarViewModel) {
        this.$tmp0 = seekBarViewModel;
    }

    public final void run() {
        this.$tmp0.checkPlaybackPosition();
    }
}
