package com.android.systemui.controls.ui;

import android.service.controls.templates.StatelessTemplate;
import android.view.View;
import com.android.systemui.util.concurrency.DelayableExecutor;

/* compiled from: TouchBehavior.kt */
public final class TouchBehavior$initialize$1 implements View.OnClickListener {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ TouchBehavior this$0;

    public TouchBehavior$initialize$1(ControlViewHolder controlViewHolder, TouchBehavior touchBehavior) {
        this.$cvh = controlViewHolder;
        this.this$0 = touchBehavior;
    }

    public final void onClick(View view) {
        this.$cvh.getControlActionCoordinator().touch(this.$cvh, this.this$0.getTemplate().getTemplateId(), this.this$0.getControl());
        if (this.this$0.getTemplate() instanceof StatelessTemplate) {
            this.this$0.statelessTouch = true;
            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(this.$cvh, this.this$0.getEnabled(), this.this$0.lastColorOffset, false, 4, (Object) null);
            DelayableExecutor uiExecutor = this.$cvh.getUiExecutor();
            final TouchBehavior touchBehavior = this.this$0;
            final ControlViewHolder controlViewHolder = this.$cvh;
            uiExecutor.executeDelayed(new Runnable() {
                public final void run() {
                    touchBehavior.statelessTouch = false;
                    ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(controlViewHolder, touchBehavior.getEnabled(), touchBehavior.lastColorOffset, false, 4, (Object) null);
                }
            }, 3000);
        }
    }
}
