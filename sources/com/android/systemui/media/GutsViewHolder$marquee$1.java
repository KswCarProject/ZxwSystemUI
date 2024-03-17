package com.android.systemui.media;

/* compiled from: GutsViewHolder.kt */
public final class GutsViewHolder$marquee$1 implements Runnable {
    public final /* synthetic */ boolean $start;
    public final /* synthetic */ GutsViewHolder this$0;

    public GutsViewHolder$marquee$1(GutsViewHolder gutsViewHolder, boolean z) {
        this.this$0 = gutsViewHolder;
        this.$start = z;
    }

    public final void run() {
        this.this$0.getGutsText().setSelected(this.$start);
    }
}
