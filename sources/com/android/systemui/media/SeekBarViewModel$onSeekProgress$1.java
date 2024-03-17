package com.android.systemui.media;

import com.android.systemui.media.SeekBarViewModel;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$onSeekProgress$1 implements Runnable {
    public final /* synthetic */ long $position;
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$onSeekProgress$1(SeekBarViewModel seekBarViewModel, long j) {
        this.this$0 = seekBarViewModel;
        this.$position = j;
    }

    public final void run() {
        if (this.this$0.scrubbing) {
            SeekBarViewModel seekBarViewModel = this.this$0;
            seekBarViewModel.set_data(SeekBarViewModel.Progress.copy$default(seekBarViewModel._data, false, false, false, false, Integer.valueOf((int) this.$position), 0, 47, (Object) null));
            return;
        }
        this.this$0.onSeek(this.$position);
    }
}
