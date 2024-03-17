package com.android.systemui.controls.management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.qs.PageIndicator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ManagementPageIndicator.kt */
public final class ManagementPageIndicator extends PageIndicator {
    @NotNull
    public Function1<? super Integer, Unit> visibilityListener = ManagementPageIndicator$visibilityListener$1.INSTANCE;

    public ManagementPageIndicator(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setLocation(float f) {
        if (getLayoutDirection() == 1) {
            super.setLocation(((float) (getChildCount() - 1)) - f);
        } else {
            super.setLocation(f);
        }
    }

    public final void setVisibilityListener(@NotNull Function1<? super Integer, Unit> function1) {
        this.visibilityListener = function1;
    }

    public void onVisibilityChanged(@NotNull View view, int i) {
        super.onVisibilityChanged(view, i);
        if (Intrinsics.areEqual((Object) view, (Object) this)) {
            this.visibilityListener.invoke(Integer.valueOf(i));
        }
    }
}
