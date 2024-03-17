package com.android.systemui.controls.ui;

import android.service.controls.templates.TemperatureControlTemplate;
import android.view.View;

/* compiled from: TemperatureControlBehavior.kt */
public final class TemperatureControlBehavior$bind$1 implements View.OnClickListener {
    public final /* synthetic */ TemperatureControlTemplate $template;
    public final /* synthetic */ TemperatureControlBehavior this$0;

    public TemperatureControlBehavior$bind$1(TemperatureControlBehavior temperatureControlBehavior, TemperatureControlTemplate temperatureControlTemplate) {
        this.this$0 = temperatureControlBehavior;
        this.$template = temperatureControlTemplate;
    }

    public final void onClick(View view) {
        this.this$0.getCvh().getControlActionCoordinator().touch(this.this$0.getCvh(), this.$template.getTemplateId(), this.this$0.getControl());
    }
}
