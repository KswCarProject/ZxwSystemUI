package com.android.wm.shell.common;

import android.graphics.Rect;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;

/* compiled from: FloatingContentCoordinator.kt */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2 extends Lambda implements Function0<Rect> {
    public final /* synthetic */ Rect $contentRect;
    public final /* synthetic */ Rect $newlyOverlappingRect;
    public final /* synthetic */ Ref$ObjectRef<List<Rect>> $rectsToAvoidBelow;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2(Rect rect, Ref$ObjectRef<List<Rect>> ref$ObjectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidBelow = ref$ObjectRef;
        this.$newlyOverlappingRect = rect2;
    }

    @NotNull
    public final Rect invoke() {
        return FloatingContentCoordinator.Companion.findAreaForContentAboveOrBelow(this.$contentRect, CollectionsKt___CollectionsKt.plus((Collection) this.$rectsToAvoidBelow.element, this.$newlyOverlappingRect), false);
    }
}
