package com.android.systemui.media;

import android.media.session.MediaController;
import com.android.systemui.media.SeekBarViewModel;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$clearController$1 implements Runnable {
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$clearController$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    public final void run() {
        this.this$0.setController((MediaController) null);
        this.this$0.playbackState = null;
        Runnable access$getCancel$p = this.this$0.cancel;
        if (access$getCancel$p != null) {
            access$getCancel$p.run();
        }
        this.this$0.cancel = null;
        SeekBarViewModel seekBarViewModel = this.this$0;
        seekBarViewModel.set_data(SeekBarViewModel.Progress.copy$default(seekBarViewModel._data, false, false, false, false, (Integer) null, 0, 62, (Object) null));
    }
}
