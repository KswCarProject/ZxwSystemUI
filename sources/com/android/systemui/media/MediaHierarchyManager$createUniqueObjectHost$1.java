package com.android.systemui.media;

import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.util.animation.UniqueObjectHostView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManager$createUniqueObjectHost$1 implements View.OnAttachStateChangeListener {
    public final /* synthetic */ UniqueObjectHostView $viewHost;
    public final /* synthetic */ MediaHierarchyManager this$0;

    public void onViewDetachedFromWindow(@Nullable View view) {
    }

    public MediaHierarchyManager$createUniqueObjectHost$1(MediaHierarchyManager mediaHierarchyManager, UniqueObjectHostView uniqueObjectHostView) {
        this.this$0 = mediaHierarchyManager;
        this.$viewHost = uniqueObjectHostView;
    }

    public void onViewAttachedToWindow(@Nullable View view) {
        if (this.this$0.rootOverlay == null) {
            this.this$0.rootView = this.$viewHost.getViewRootImpl().getView();
            MediaHierarchyManager mediaHierarchyManager = this.this$0;
            View access$getRootView$p = mediaHierarchyManager.rootView;
            Intrinsics.checkNotNull(access$getRootView$p);
            ViewOverlay overlay = access$getRootView$p.getOverlay();
            if (overlay != null) {
                mediaHierarchyManager.rootOverlay = (ViewGroupOverlay) overlay;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroupOverlay");
            }
        }
        this.$viewHost.removeOnAttachStateChangeListener(this);
    }
}
