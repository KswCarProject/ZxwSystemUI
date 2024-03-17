package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RenderStageManager.kt */
public final class RenderStageManager {
    @NotNull
    public final List<OnAfterRenderEntryListener> onAfterRenderEntryListeners = new ArrayList();
    @NotNull
    public final List<OnAfterRenderGroupListener> onAfterRenderGroupListeners = new ArrayList();
    @NotNull
    public final List<OnAfterRenderListListener> onAfterRenderListListeners = new ArrayList();
    @Nullable
    public NotifViewRenderer viewRenderer;

    public final void dispatchOnAfterRenderEntries(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderEntries");
        try {
            if (!this.onAfterRenderEntryListeners.isEmpty()) {
                for (ListEntry listEntry : list) {
                    if (listEntry instanceof NotificationEntry) {
                        NotificationEntry notificationEntry = (NotificationEntry) listEntry;
                        NotifRowController rowController = notifViewRenderer.getRowController(notificationEntry);
                        for (OnAfterRenderEntryListener onAfterRenderEntry : this.onAfterRenderEntryListeners) {
                            onAfterRenderEntry.onAfterRenderEntry(notificationEntry, rowController);
                        }
                    } else if (listEntry instanceof GroupEntry) {
                        GroupEntry groupEntry = (GroupEntry) listEntry;
                        NotificationEntry summary = groupEntry.getSummary();
                        if (summary != null) {
                            NotifRowController rowController2 = notifViewRenderer.getRowController(summary);
                            for (OnAfterRenderEntryListener onAfterRenderEntry2 : this.onAfterRenderEntryListeners) {
                                onAfterRenderEntry2.onAfterRenderEntry(summary, rowController2);
                            }
                            for (NotificationEntry notificationEntry2 : ((GroupEntry) listEntry).getChildren()) {
                                NotifRowController rowController3 = notifViewRenderer.getRowController(notificationEntry2);
                                for (OnAfterRenderEntryListener onAfterRenderEntry3 : this.onAfterRenderEntryListeners) {
                                    onAfterRenderEntry3.onAfterRenderEntry(notificationEntry2, rowController3);
                                }
                            }
                        } else {
                            throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
                        }
                    } else {
                        throw new IllegalStateException(Intrinsics.stringPlus("Unhandled entry: ", listEntry).toString());
                    }
                }
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void dispatchOnAfterRenderGroups(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderGroups");
        try {
            if (!this.onAfterRenderGroupListeners.isEmpty()) {
                for (GroupEntry groupEntry : SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list), RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1.INSTANCE)) {
                    NotifGroupController groupController = notifViewRenderer.getGroupController(groupEntry);
                    for (OnAfterRenderGroupListener onAfterRenderGroup : this.onAfterRenderGroupListeners) {
                        onAfterRenderGroup.onAfterRenderGroup(groupEntry, groupController);
                    }
                }
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void dispatchOnAfterRenderList(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderList");
        try {
            NotifStackController stackController = notifViewRenderer.getStackController();
            for (OnAfterRenderListListener onAfterRenderList : this.onAfterRenderListListeners) {
                onAfterRenderList.onAfterRenderList(list, stackController);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public final void onRenderList(List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.onRenderList");
        try {
            NotifViewRenderer notifViewRenderer = this.viewRenderer;
            if (notifViewRenderer != null) {
                notifViewRenderer.onRenderList(list);
                dispatchOnAfterRenderList(notifViewRenderer, list);
                dispatchOnAfterRenderGroups(notifViewRenderer, list);
                dispatchOnAfterRenderEntries(notifViewRenderer, list);
                notifViewRenderer.onDispatchComplete();
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void attach(@NotNull ShadeListBuilder shadeListBuilder) {
        shadeListBuilder.setOnRenderListListener(new RenderStageManager$attach$1(this));
    }

    public final void setViewRenderer(@NotNull NotifViewRenderer notifViewRenderer) {
        this.viewRenderer = notifViewRenderer;
    }

    public final void addOnAfterRenderListListener(@NotNull OnAfterRenderListListener onAfterRenderListListener) {
        this.onAfterRenderListListeners.add(onAfterRenderListListener);
    }

    public final void addOnAfterRenderGroupListener(@NotNull OnAfterRenderGroupListener onAfterRenderGroupListener) {
        this.onAfterRenderGroupListeners.add(onAfterRenderGroupListener);
    }

    public final void addOnAfterRenderEntryListener(@NotNull OnAfterRenderEntryListener onAfterRenderEntryListener) {
        this.onAfterRenderEntryListeners.add(onAfterRenderEntryListener);
    }
}
