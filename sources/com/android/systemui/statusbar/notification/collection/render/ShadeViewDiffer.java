package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewDiffer.kt */
public final class ShadeViewDiffer {
    @NotNull
    public final ShadeViewDifferLogger logger;
    @NotNull
    public final Map<NodeController, ShadeNode> nodes;
    @NotNull
    public final ShadeNode rootNode;

    public final void applySpec(@NotNull NodeSpec nodeSpec) {
        Trace.beginSection("ShadeViewDiffer.applySpec");
        try {
            Map<NodeController, NodeSpec> treeToMap = treeToMap(nodeSpec);
            if (Intrinsics.areEqual((Object) nodeSpec.getController(), (Object) this.rootNode.getController())) {
                detachChildren(this.rootNode, treeToMap);
                attachChildren(this.rootNode, treeToMap);
                Unit unit = Unit.INSTANCE;
                return;
            }
            throw new IllegalArgumentException("Tree root " + nodeSpec.getController().getNodeLabel() + " does not match own root at " + this.rootNode.getLabel());
        } finally {
            Trace.endSection();
        }
    }

    public ShadeViewDiffer(@NotNull NodeController nodeController, @NotNull ShadeViewDifferLogger shadeViewDifferLogger) {
        this.logger = shadeViewDifferLogger;
        ShadeNode shadeNode = new ShadeNode(nodeController);
        this.rootNode = shadeNode;
        this.nodes = MapsKt__MapsKt.mutableMapOf(TuplesKt.to(nodeController, shadeNode));
    }

    public final void detachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        Iterable values = this.nodes.values();
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(values, 10)), 16));
        for (Object next : values) {
            linkedHashMap.put(((ShadeNode) next).getView(), next);
        }
        detachChildren$detachRecursively(linkedHashMap, this, shadeNode, map);
    }

    public static final void detachChildren$detachRecursively(Map<View, ShadeNode> map, ShadeViewDiffer shadeViewDiffer, ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map2) {
        NodeSpec nodeSpec = (NodeSpec) map2.get(shadeNode.getController());
        int childCount = shadeNode.getChildCount() - 1;
        if (childCount >= 0) {
            while (true) {
                int i = childCount - 1;
                ShadeNode shadeNode2 = map.get(shadeNode.getChildAt(childCount));
                if (shadeNode2 != null) {
                    shadeViewDiffer.maybeDetachChild(shadeNode, nodeSpec, shadeNode2, (NodeSpec) map2.get(shadeNode2.getController()));
                    if (shadeNode2.getController().getChildCount() > 0) {
                        detachChildren$detachRecursively(map, shadeViewDiffer, shadeNode2, map2);
                    }
                }
                if (i >= 0) {
                    childCount = i;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000b, code lost:
        r4 = r20.getParent();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void maybeDetachChild(com.android.systemui.statusbar.notification.collection.render.ShadeNode r17, com.android.systemui.statusbar.notification.collection.render.NodeSpec r18, com.android.systemui.statusbar.notification.collection.render.ShadeNode r19, com.android.systemui.statusbar.notification.collection.render.NodeSpec r20) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r19
            r3 = 0
            if (r20 != 0) goto L_0x000b
        L_0x0009:
            r4 = r3
            goto L_0x0016
        L_0x000b:
            com.android.systemui.statusbar.notification.collection.render.NodeSpec r4 = r20.getParent()
            if (r4 != 0) goto L_0x0012
            goto L_0x0009
        L_0x0012:
            com.android.systemui.statusbar.notification.collection.render.ShadeNode r4 = r0.getNode(r4)
        L_0x0016:
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r1)
            if (r5 != 0) goto L_0x0053
            r5 = 0
            r6 = 1
            if (r4 != 0) goto L_0x0022
            r7 = r6
            goto L_0x0023
        L_0x0022:
            r7 = r5
        L_0x0023:
            if (r7 == 0) goto L_0x002e
            java.util.Map<com.android.systemui.statusbar.notification.collection.render.NodeController, com.android.systemui.statusbar.notification.collection.render.ShadeNode> r8 = r0.nodes
            com.android.systemui.statusbar.notification.collection.render.NodeController r9 = r19.getController()
            r8.remove(r9)
        L_0x002e:
            com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger r10 = r0.logger
            java.lang.String r11 = r19.getLabel()
            r12 = r7 ^ 1
            if (r18 != 0) goto L_0x003a
            r13 = r6
            goto L_0x003b
        L_0x003a:
            r13 = r5
        L_0x003b:
            java.lang.String r14 = r17.getLabel()
            if (r4 != 0) goto L_0x0043
            r15 = r3
            goto L_0x0048
        L_0x0043:
            java.lang.String r0 = r4.getLabel()
            r15 = r0
        L_0x0048:
            r10.logDetachingChild(r11, r12, r13, r14, r15)
            r0 = r7 ^ 1
            r1.removeChild(r2, r0)
            r2.setParent(r3)
        L_0x0053:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.ShadeViewDiffer.maybeDetachChild(com.android.systemui.statusbar.notification.collection.render.ShadeNode, com.android.systemui.statusbar.notification.collection.render.NodeSpec, com.android.systemui.statusbar.notification.collection.render.ShadeNode, com.android.systemui.statusbar.notification.collection.render.NodeSpec):void");
    }

    public final void attachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        Object obj = map.get(shadeNode.getController());
        if (obj != null) {
            int i = 0;
            for (NodeSpec next : ((NodeSpec) obj).getChildren()) {
                int i2 = i + 1;
                View childAt = shadeNode.getChildAt(i);
                ShadeNode node = getNode(next);
                if (!Intrinsics.areEqual((Object) node.getView(), (Object) childAt)) {
                    ShadeNode parent = node.getParent();
                    if (parent == null) {
                        this.logger.logAttachingChild(node.getLabel(), shadeNode.getLabel());
                        shadeNode.addChildAt(node, i);
                        node.setParent(shadeNode);
                    } else if (Intrinsics.areEqual((Object) parent, (Object) shadeNode)) {
                        this.logger.logMovingChild(node.getLabel(), shadeNode.getLabel(), i);
                        shadeNode.moveChildTo(node, i);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Child ");
                        sb.append(node.getLabel());
                        sb.append(" should have parent ");
                        sb.append(shadeNode.getLabel());
                        sb.append(" but is actually ");
                        ShadeNode parent2 = node.getParent();
                        sb.append(parent2 == null ? null : parent2.getLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                }
                if (!next.getChildren().isEmpty()) {
                    attachChildren(node, map);
                }
                i = i2;
            }
            return;
        }
        throw new IllegalStateException("Required value was null.".toString());
    }

    public final ShadeNode getNode(NodeSpec nodeSpec) {
        ShadeNode shadeNode = this.nodes.get(nodeSpec.getController());
        if (shadeNode != null) {
            return shadeNode;
        }
        ShadeNode shadeNode2 = new ShadeNode(nodeSpec.getController());
        this.nodes.put(shadeNode2.getController(), shadeNode2);
        return shadeNode2;
    }

    public final Map<NodeController, NodeSpec> treeToMap(NodeSpec nodeSpec) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            registerNodes(nodeSpec, linkedHashMap);
            return linkedHashMap;
        } catch (DuplicateNodeException e) {
            this.logger.logDuplicateNodeInTree(nodeSpec, e);
            throw e;
        }
    }

    public final void registerNodes(NodeSpec nodeSpec, Map<NodeController, NodeSpec> map) {
        if (!map.containsKey(nodeSpec.getController())) {
            map.put(nodeSpec.getController(), nodeSpec);
            if (!nodeSpec.getChildren().isEmpty()) {
                for (NodeSpec registerNodes : nodeSpec.getChildren()) {
                    registerNodes(registerNodes, map);
                }
                return;
            }
            return;
        }
        throw new DuplicateNodeException("Node " + nodeSpec.getController().getNodeLabel() + " appears more than once");
    }
}
