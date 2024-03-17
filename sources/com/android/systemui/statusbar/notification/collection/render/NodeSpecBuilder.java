package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: NodeSpecBuilder.kt */
public final class NodeSpecBuilder {
    @NotNull
    public Set<NotifSection> lastSections = SetsKt__SetsKt.emptySet();
    @NotNull
    public final NodeSpecBuilderLogger logger;
    @NotNull
    public final MediaContainerController mediaContainerController;
    @NotNull
    public final SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider;
    @NotNull
    public final NotificationSectionsFeatureManager sectionsFeatureManager;
    @NotNull
    public final NotifViewBarn viewBarn;

    @NotNull
    public final NodeSpec buildNodeSpec(@NotNull NodeController nodeController, @NotNull List<? extends ListEntry> list) {
        Trace.beginSection("NodeSpecBuilder.buildNodeSpec");
        try {
            NodeSpecImpl nodeSpecImpl = new NodeSpecImpl((NodeSpec) null, nodeController);
            if (this.sectionsFeatureManager.isMediaControlsEnabled()) {
                nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, this.mediaContainerController));
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            boolean sectionHeadersVisible = this.sectionHeaderVisibilityProvider.getSectionHeadersVisible();
            ArrayList arrayList = new ArrayList();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            NotifSection notifSection = null;
            for (ListEntry listEntry : list) {
                NotifSection section = listEntry.getSection();
                Intrinsics.checkNotNull(section);
                if (!linkedHashSet.contains(section)) {
                    if (!Intrinsics.areEqual((Object) section, (Object) notifSection)) {
                        if (!Intrinsics.areEqual((Object) section.getHeaderController(), (Object) notifSection == null ? null : notifSection.getHeaderController()) && sectionHeadersVisible) {
                            NodeController headerController = section.getHeaderController();
                            if (headerController != null) {
                                nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController));
                                linkedHashMap.put(section, headerController);
                            }
                        }
                        linkedHashSet.add(notifSection);
                        arrayList.add(section);
                        notifSection = section;
                    }
                    nodeSpecImpl.getChildren().add(buildNotifNode(nodeSpecImpl, listEntry));
                    linkedHashMap2.put(section, Integer.valueOf(((Number) linkedHashMap2.getOrDefault(section, 0)).intValue() + 1));
                } else {
                    throw new RuntimeException("Section " + section.getLabel() + " has been duplicated");
                }
            }
            this.logger.logBuildNodeSpec(this.lastSections, linkedHashMap, linkedHashMap2, arrayList);
            this.lastSections = linkedHashMap2.keySet();
            return nodeSpecImpl;
        } finally {
            Trace.endSection();
        }
    }

    public NodeSpecBuilder(@NotNull MediaContainerController mediaContainerController2, @NotNull NotificationSectionsFeatureManager notificationSectionsFeatureManager, @NotNull SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider2, @NotNull NotifViewBarn notifViewBarn, @NotNull NodeSpecBuilderLogger nodeSpecBuilderLogger) {
        this.mediaContainerController = mediaContainerController2;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.sectionHeaderVisibilityProvider = sectionHeaderVisibilityProvider2;
        this.viewBarn = notifViewBarn;
        this.logger = nodeSpecBuilderLogger;
    }

    public final NodeSpec buildNotifNode(NodeSpec nodeSpec, ListEntry listEntry) {
        if (listEntry instanceof NotificationEntry) {
            return new NodeSpecImpl(nodeSpec, this.viewBarn.requireNodeController(listEntry));
        }
        if (listEntry instanceof GroupEntry) {
            NotifViewBarn notifViewBarn = this.viewBarn;
            GroupEntry groupEntry = (GroupEntry) listEntry;
            NotificationEntry summary = groupEntry.getSummary();
            if (summary != null) {
                NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(nodeSpec, notifViewBarn.requireNodeController(summary));
                for (NotificationEntry buildNotifNode : groupEntry.getChildren()) {
                    nodeSpecImpl.getChildren().add(buildNotifNode(nodeSpecImpl, buildNotifNode));
                }
                return nodeSpecImpl;
            }
            throw new IllegalStateException("Required value was null.".toString());
        }
        throw new RuntimeException(Intrinsics.stringPlus("Unexpected entry: ", listEntry));
    }
}
