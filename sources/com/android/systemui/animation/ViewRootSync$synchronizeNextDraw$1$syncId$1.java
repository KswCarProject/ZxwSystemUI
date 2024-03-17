package com.android.systemui.animation;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/* compiled from: ViewRootSync.kt */
public final class ViewRootSync$synchronizeNextDraw$1$syncId$1 implements Runnable {
    public final /* synthetic */ Function0<Unit> $then;

    public ViewRootSync$synchronizeNextDraw$1$syncId$1(Function0<Unit> function0) {
        this.$then = function0;
    }

    public final void run() {
        this.$then.invoke();
    }
}
