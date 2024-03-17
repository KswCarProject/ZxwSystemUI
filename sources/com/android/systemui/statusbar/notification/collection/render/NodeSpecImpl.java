package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NodeController.kt */
public final class NodeSpecImpl implements NodeSpec {
    @NotNull
    public final List<NodeSpec> children = new ArrayList();
    @NotNull
    public final NodeController controller;
    @Nullable
    public final NodeSpec parent;

    public NodeSpecImpl(@Nullable NodeSpec nodeSpec, @NotNull NodeController nodeController) {
        this.parent = nodeSpec;
        this.controller = nodeController;
    }

    @Nullable
    public NodeSpec getParent() {
        return this.parent;
    }

    @NotNull
    public NodeController getController() {
        return this.controller;
    }

    @NotNull
    public List<NodeSpec> getChildren() {
        return this.children;
    }
}
