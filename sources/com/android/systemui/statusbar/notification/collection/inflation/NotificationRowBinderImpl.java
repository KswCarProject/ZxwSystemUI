package com.android.systemui.statusbar.notification.collection.inflation;

import android.content.Context;
import android.view.ViewGroup;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.util.Objects;
import javax.inject.Provider;

public class NotificationRowBinderImpl implements NotificationRowBinder {
    public BindRowCallback mBindRowCallback;
    public final Context mContext;
    public final ExpandableNotificationRowComponent.Builder mExpandableNotificationRowComponentBuilder;
    public final IconManager mIconManager;
    public NotificationListContainer mListContainer;
    public final LowPriorityInflationHelper mLowPriorityInflationHelper;
    public final NotificationMessagingUtil mMessagingUtil;
    public final NotifBindPipeline mNotifBindPipeline;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public NotificationClicker mNotificationClicker;
    public final NotificationLockscreenUserManager mNotificationLockscreenUserManager;
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    public NotificationPresenter mPresenter;
    public final RowContentBindStage mRowContentBindStage;
    public final Provider<RowInflaterTask> mRowInflaterTaskProvider;

    public interface BindRowCallback {
        void onBindRow(ExpandableNotificationRow expandableNotificationRow);
    }

    public NotificationRowBinderImpl(Context context, NotificationMessagingUtil notificationMessagingUtil, NotificationRemoteInputManager notificationRemoteInputManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage, Provider<RowInflaterTask> provider, ExpandableNotificationRowComponent.Builder builder, IconManager iconManager, LowPriorityInflationHelper lowPriorityInflationHelper, NotifPipelineFlags notifPipelineFlags) {
        this.mContext = context;
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
        this.mMessagingUtil = notificationMessagingUtil;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mNotificationLockscreenUserManager = notificationLockscreenUserManager;
        this.mRowInflaterTaskProvider = provider;
        this.mExpandableNotificationRowComponentBuilder = builder;
        this.mIconManager = iconManager;
        this.mLowPriorityInflationHelper = lowPriorityInflationHelper;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, BindRowCallback bindRowCallback) {
        this.mPresenter = notificationPresenter;
        this.mListContainer = notificationListContainer;
        this.mBindRowCallback = bindRowCallback;
        this.mIconManager.attach();
    }

    public void setNotificationClicker(NotificationClicker notificationClicker) {
        this.mNotificationClicker = notificationClicker;
    }

    public void inflateViews(NotificationEntry notificationEntry, NotifInflater.Params params, NotificationRowContentBinder.InflationCallback inflationCallback) throws InflationException {
        if (params == null) {
            this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        }
        ViewGroup viewParentForNotification = this.mListContainer.getViewParentForNotification(notificationEntry);
        if (notificationEntry.rowExists()) {
            this.mIconManager.updateIcons(notificationEntry);
            ExpandableNotificationRow row = notificationEntry.getRow();
            row.reset();
            updateRow(notificationEntry, row);
            inflateContentViews(notificationEntry, params, row, inflationCallback);
            return;
        }
        this.mIconManager.createIcons(notificationEntry);
        this.mRowInflaterTaskProvider.get().inflate(this.mContext, viewParentForNotification, notificationEntry, new NotificationRowBinderImpl$$ExternalSyntheticLambda1(this, notificationEntry, params, inflationCallback));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateViews$0(NotificationEntry notificationEntry, NotifInflater.Params params, NotificationRowContentBinder.InflationCallback inflationCallback, ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRowController expandableNotificationRowController = this.mExpandableNotificationRowComponentBuilder.expandableNotificationRow(expandableNotificationRow).notificationEntry(notificationEntry).onExpandClickListener(this.mPresenter).listContainer(this.mListContainer).build().getExpandableNotificationRowController();
        expandableNotificationRowController.init(notificationEntry);
        notificationEntry.setRowController(expandableNotificationRowController);
        bindRow(notificationEntry, expandableNotificationRow);
        updateRow(notificationEntry, expandableNotificationRow);
        inflateContentViews(notificationEntry, params, expandableNotificationRow, inflationCallback);
    }

    public final void bindRow(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        this.mListContainer.bindRow(expandableNotificationRow);
        this.mNotificationRemoteInputManager.bindRow(expandableNotificationRow);
        expandableNotificationRow.setOnActivatedListener(this.mPresenter);
        notificationEntry.setRow(expandableNotificationRow);
        this.mNotifBindPipeline.manageRow(notificationEntry, expandableNotificationRow);
        this.mBindRowCallback.onBindRow(expandableNotificationRow);
    }

    public void onNotificationRankingUpdated(NotificationEntry notificationEntry, Integer num, NotificationUiAdjustment notificationUiAdjustment, NotificationUiAdjustment notificationUiAdjustment2, NotificationRowContentBinder.InflationCallback inflationCallback) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        if (NotificationUiAdjustment.needReinflate(notificationUiAdjustment, notificationUiAdjustment2)) {
            if (notificationEntry.rowExists()) {
                ExpandableNotificationRow row = notificationEntry.getRow();
                row.reset();
                updateRow(notificationEntry, row);
                inflateContentViews(notificationEntry, (NotifInflater.Params) null, row, inflationCallback);
            }
        } else if (num != null && notificationEntry.getImportance() != num.intValue() && notificationEntry.rowExists()) {
            notificationEntry.getRow().onNotificationRankingUpdated();
        }
    }

    public final void updateRow(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        int i = notificationEntry.targetSdk;
        expandableNotificationRow.setLegacy(i >= 9 && i < 21);
        NotificationClicker notificationClicker = this.mNotificationClicker;
        Objects.requireNonNull(notificationClicker);
        NotificationClicker notificationClicker2 = notificationClicker;
        notificationClicker.register(expandableNotificationRow, notificationEntry.getSbn());
    }

    public final void inflateContentViews(NotificationEntry notificationEntry, NotifInflater.Params params, ExpandableNotificationRow expandableNotificationRow, NotificationRowContentBinder.InflationCallback inflationCallback) {
        boolean z;
        boolean isImportantMessaging = this.mMessagingUtil.isImportantMessaging(notificationEntry.getSbn(), notificationEntry.getImportance());
        if (params != null) {
            z = params.isLowPriority();
        } else {
            this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
            z = this.mLowPriorityInflationHelper.shouldUseLowPriorityView(notificationEntry);
        }
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry);
        rowContentBindParams.setUseIncreasedCollapsedHeight(isImportantMessaging);
        rowContentBindParams.setUseLowPriority(z);
        if (this.mNotificationLockscreenUserManager.needsRedaction(notificationEntry)) {
            rowContentBindParams.requireContentViews(8);
        } else {
            rowContentBindParams.markContentViewsFreeable(8);
        }
        rowContentBindParams.rebindAllContentViews();
        this.mRowContentBindStage.requestRebind(notificationEntry, new NotificationRowBinderImpl$$ExternalSyntheticLambda0(expandableNotificationRow, isImportantMessaging, z, inflationCallback));
    }

    public static /* synthetic */ void lambda$inflateContentViews$1(ExpandableNotificationRow expandableNotificationRow, boolean z, boolean z2, NotificationRowContentBinder.InflationCallback inflationCallback, NotificationEntry notificationEntry) {
        expandableNotificationRow.setUsesIncreasedCollapsedHeight(z);
        expandableNotificationRow.setIsLowPriority(z2);
        if (inflationCallback != null) {
            inflationCallback.onAsyncInflationFinished(notificationEntry);
        }
    }
}
