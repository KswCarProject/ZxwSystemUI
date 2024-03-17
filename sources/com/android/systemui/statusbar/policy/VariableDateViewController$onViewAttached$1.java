package com.android.systemui.statusbar.policy;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* compiled from: VariableDateViewController.kt */
public /* synthetic */ class VariableDateViewController$onViewAttached$1 extends FunctionReferenceImpl implements Function0<Unit> {
    public VariableDateViewController$onViewAttached$1(Object obj) {
        super(0, obj, VariableDateViewController.class, "updateClock", "updateClock()V", 0);
    }

    public final void invoke() {
        ((VariableDateViewController) this.receiver).updateClock();
    }
}
