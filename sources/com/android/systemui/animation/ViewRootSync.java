package com.android.systemui.animation;

import android.view.View;
import android.window.SurfaceSyncer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ViewRootSync.kt */
public final class ViewRootSync {
    @NotNull
    public static final ViewRootSync INSTANCE = new ViewRootSync();
    @Nullable
    public static SurfaceSyncer surfaceSyncer;

    public final void synchronizeNextDraw(@NotNull View view, @NotNull View view2, @NotNull Function0<Unit> function0) {
        if (!view.isAttachedToWindow() || view.getViewRootImpl() == null || !view2.isAttachedToWindow() || view2.getViewRootImpl() == null || Intrinsics.areEqual((Object) view.getViewRootImpl(), (Object) view2.getViewRootImpl())) {
            function0.invoke();
            return;
        }
        SurfaceSyncer surfaceSyncer2 = new SurfaceSyncer();
        int i = surfaceSyncer2.setupSync(new ViewRootSync$synchronizeNextDraw$1$syncId$1(function0));
        surfaceSyncer2.addToSync(i, view);
        surfaceSyncer2.addToSync(i, view2);
        surfaceSyncer2.markSyncReady(i);
        surfaceSyncer = surfaceSyncer2;
    }
}
