package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.Context;
import com.android.systemui.R$bool;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.render.NotifRowController;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RowAppearanceCoordinator.kt */
public final class RowAppearanceCoordinator implements Coordinator {
    @Nullable
    public NotificationEntry entryToExpand;
    public final boolean mAlwaysExpandNonGroupedNotification;
    @NotNull
    public AssistantFeedbackController mAssistantFeedbackController;
    @NotNull
    public SectionClassifier mSectionClassifier;

    public RowAppearanceCoordinator(@NotNull Context context, @NotNull AssistantFeedbackController assistantFeedbackController, @NotNull SectionClassifier sectionClassifier) {
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mSectionClassifier = sectionClassifier;
        this.mAlwaysExpandNonGroupedNotification = context.getResources().getBoolean(R$bool.config_alwaysExpandNonGroupedNotifications);
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addOnBeforeRenderListListener(new RowAppearanceCoordinator$attach$1(this));
        notifPipeline.addOnAfterRenderEntryListener(new RowAppearanceCoordinator$attach$2(this));
    }

    public final void onBeforeRenderList(List<? extends ListEntry> list) {
        NotificationEntry representativeEntry;
        ListEntry listEntry = (ListEntry) CollectionsKt___CollectionsKt.firstOrNull(list);
        NotificationEntry notificationEntry = null;
        if (!(listEntry == null || (representativeEntry = listEntry.getRepresentativeEntry()) == null)) {
            SectionClassifier sectionClassifier = this.mSectionClassifier;
            NotifSection section = representativeEntry.getSection();
            Intrinsics.checkNotNull(section);
            if (!sectionClassifier.isMinimizedSection(section)) {
                notificationEntry = representativeEntry;
            }
        }
        this.entryToExpand = notificationEntry;
    }

    public final void onAfterRenderEntry(NotificationEntry notificationEntry, NotifRowController notifRowController) {
        notifRowController.setSystemExpanded(this.mAlwaysExpandNonGroupedNotification || Intrinsics.areEqual((Object) notificationEntry, (Object) this.entryToExpand));
        notifRowController.setFeedbackIcon(this.mAssistantFeedbackController.getFeedbackIcon(notificationEntry));
        notifRowController.setLastAudiblyAlertedMs(notificationEntry.getLastAudiblyAlertedMs());
    }
}
