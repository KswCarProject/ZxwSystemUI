package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaContainerController.kt */
public final class MediaContainerController implements NodeController {
    @NotNull
    public final LayoutInflater layoutInflater;
    @Nullable
    public MediaContainerView mediaContainerView;
    @NotNull
    public final String nodeLabel = "MediaContainer";

    public MediaContainerController(@NotNull LayoutInflater layoutInflater2) {
        this.layoutInflater = layoutInflater2;
    }

    public void addChildAt(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.addChildAt(this, nodeController, i);
    }

    @Nullable
    public View getChildAt(int i) {
        return NodeController.DefaultImpls.getChildAt(this, i);
    }

    public int getChildCount() {
        return NodeController.DefaultImpls.getChildCount(this);
    }

    public void moveChildTo(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.moveChildTo(this, nodeController, i);
    }

    public void onViewAdded() {
        NodeController.DefaultImpls.onViewAdded(this);
    }

    public void onViewMoved() {
        NodeController.DefaultImpls.onViewMoved(this);
    }

    public void onViewRemoved() {
        NodeController.DefaultImpls.onViewRemoved(this);
    }

    public void removeChild(@NotNull NodeController nodeController, boolean z) {
        NodeController.DefaultImpls.removeChild(this, nodeController, z);
    }

    @NotNull
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    @Nullable
    public final MediaContainerView getMediaContainerView() {
        return this.mediaContainerView;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0022  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void reinflateView(@org.jetbrains.annotations.NotNull android.view.ViewGroup r6) {
        /*
            r5 = this;
            com.android.systemui.statusbar.notification.stack.MediaContainerView r0 = r5.mediaContainerView
            r1 = -1
            if (r0 != 0) goto L_0x0007
        L_0x0005:
            r2 = r1
            goto L_0x0017
        L_0x0007:
            r0.removeFromTransientContainer()
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r6) goto L_0x0005
            int r2 = r6.indexOfChild(r0)
            r6.removeView(r0)
        L_0x0017:
            android.view.LayoutInflater r0 = r5.layoutInflater
            int r3 = com.android.systemui.R$layout.keyguard_media_container
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r6, r4)
            if (r0 == 0) goto L_0x002c
            com.android.systemui.statusbar.notification.stack.MediaContainerView r0 = (com.android.systemui.statusbar.notification.stack.MediaContainerView) r0
            if (r2 == r1) goto L_0x0029
            r6.addView(r0, r2)
        L_0x0029:
            r5.mediaContainerView = r0
            return
        L_0x002c:
            java.lang.NullPointerException r5 = new java.lang.NullPointerException
            java.lang.String r6 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.MediaContainerView"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.MediaContainerController.reinflateView(android.view.ViewGroup):void");
    }

    @NotNull
    public View getView() {
        MediaContainerView mediaContainerView2 = this.mediaContainerView;
        Intrinsics.checkNotNull(mediaContainerView2);
        return mediaContainerView2;
    }
}
