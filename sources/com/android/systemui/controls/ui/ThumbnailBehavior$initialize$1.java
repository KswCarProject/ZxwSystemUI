package com.android.systemui.controls.ui;

import android.view.View;

/* compiled from: ThumbnailBehavior.kt */
public final class ThumbnailBehavior$initialize$1 implements View.OnClickListener {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ ThumbnailBehavior this$0;

    public ThumbnailBehavior$initialize$1(ControlViewHolder controlViewHolder, ThumbnailBehavior thumbnailBehavior) {
        this.$cvh = controlViewHolder;
        this.this$0 = thumbnailBehavior;
    }

    public final void onClick(View view) {
        this.$cvh.getControlActionCoordinator().touch(this.$cvh, this.this$0.getTemplate().getTemplateId(), this.this$0.getControl());
    }
}
