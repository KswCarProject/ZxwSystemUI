package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ShadeViewDiffer.kt */
public final class ShadeNode {
    @NotNull
    public final NodeController controller;
    @Nullable
    public ShadeNode parent;

    public ShadeNode(@NotNull NodeController nodeController) {
        this.controller = nodeController;
    }

    @NotNull
    public final NodeController getController() {
        return this.controller;
    }

    @NotNull
    public final View getView() {
        return this.controller.getView();
    }

    @Nullable
    public final ShadeNode getParent() {
        return this.parent;
    }

    public final void setParent(@Nullable ShadeNode shadeNode) {
        this.parent = shadeNode;
    }

    @NotNull
    public final String getLabel() {
        return this.controller.getNodeLabel();
    }

    @Nullable
    public final View getChildAt(int i) {
        return this.controller.getChildAt(i);
    }

    public final int getChildCount() {
        return this.controller.getChildCount();
    }

    public final void addChildAt(@NotNull ShadeNode shadeNode, int i) {
        this.controller.addChildAt(shadeNode.controller, i);
        shadeNode.controller.onViewAdded();
    }

    public final void moveChildTo(@NotNull ShadeNode shadeNode, int i) {
        this.controller.moveChildTo(shadeNode.controller, i);
        shadeNode.controller.onViewMoved();
    }

    public final void removeChild(@NotNull ShadeNode shadeNode, boolean z) {
        this.controller.removeChild(shadeNode.controller, z);
        shadeNode.controller.onViewRemoved();
    }
}
