package com.android.systemui.media;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$listening$1 implements Runnable {
    public final /* synthetic */ boolean $value;
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$listening$1(SeekBarViewModel seekBarViewModel, boolean z) {
        this.this$0 = seekBarViewModel;
        this.$value = z;
    }

    public final void run() {
        boolean access$getListening$p = this.this$0.listening;
        boolean z = this.$value;
        if (access$getListening$p != z) {
            this.this$0.listening = z;
            this.this$0.checkIfPollingNeeded();
        }
    }
}
