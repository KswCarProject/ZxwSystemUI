package com.android.wm.shell.common;

import android.graphics.Rect;
import com.android.wm.shell.common.FloatingContentCoordinator;
import kotlin.Lazy;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: FloatingContentCoordinator.kt */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ Rect $allowedBounds;
    public final /* synthetic */ Lazy<Rect> $newContentBoundsBelow$delegate;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(Rect rect, Lazy<Rect> lazy) {
        super(0);
        this.$allowedBounds = rect;
        this.$newContentBoundsBelow$delegate = lazy;
    }

    @NotNull
    public final Boolean invoke() {
        return Boolean.valueOf(this.$allowedBounds.contains(FloatingContentCoordinator.Companion.m4180findAreaForContentVertically$lambda3(this.$newContentBoundsBelow$delegate)));
    }
}
