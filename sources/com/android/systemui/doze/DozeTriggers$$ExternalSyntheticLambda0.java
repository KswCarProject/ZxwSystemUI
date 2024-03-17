package com.android.systemui.doze;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ DozeTriggers f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ boolean f$6;
    public final /* synthetic */ boolean f$7;
    public final /* synthetic */ float[] f$8;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda0(DozeTriggers dozeTriggers, int i, boolean z, boolean z2, float f, float f2, boolean z3, boolean z4, float[] fArr) {
        this.f$0 = dozeTriggers;
        this.f$1 = i;
        this.f$2 = z;
        this.f$3 = z2;
        this.f$4 = f;
        this.f$5 = f2;
        this.f$6 = z3;
        this.f$7 = z4;
        this.f$8 = fArr;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onSensor$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, (Boolean) obj);
    }
}
