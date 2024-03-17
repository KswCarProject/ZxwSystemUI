package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifPipeline.kt */
public final class NotifPipeline implements CommonNotifCollection {
    public final boolean isNewPipelineEnabled;
    @NotNull
    public final NotifCollection mNotifCollection;
    @NotNull
    public final RenderStageManager mRenderStageManager;
    @NotNull
    public final ShadeListBuilder mShadeListBuilder;

    public NotifPipeline(@NotNull NotifPipelineFlags notifPipelineFlags, @NotNull NotifCollection notifCollection, @NotNull ShadeListBuilder shadeListBuilder, @NotNull RenderStageManager renderStageManager) {
        this.mNotifCollection = notifCollection;
        this.mShadeListBuilder = shadeListBuilder;
        this.mRenderStageManager = renderStageManager;
        this.isNewPipelineEnabled = notifPipelineFlags.isNewPipelineEnabled();
    }

    @NotNull
    public Collection<NotificationEntry> getAllNotifs() {
        return this.mNotifCollection.getAllNotifs();
    }

    public void addCollectionListener(@NotNull NotifCollectionListener notifCollectionListener) {
        this.mNotifCollection.addCollectionListener(notifCollectionListener);
    }

    @Nullable
    public NotificationEntry getEntry(@NotNull String str) {
        return this.mNotifCollection.getEntry(str);
    }

    public final boolean isNewPipelineEnabled() {
        return this.isNewPipelineEnabled;
    }

    public final void addNotificationLifetimeExtender(@NotNull NotifLifetimeExtender notifLifetimeExtender) {
        this.mNotifCollection.addNotificationLifetimeExtender(notifLifetimeExtender);
    }

    public final void addNotificationDismissInterceptor(@NotNull NotifDismissInterceptor notifDismissInterceptor) {
        this.mNotifCollection.addNotificationDismissInterceptor(notifDismissInterceptor);
    }

    public final void addPreGroupFilter(@NotNull NotifFilter notifFilter) {
        this.mShadeListBuilder.addPreGroupFilter(notifFilter);
    }

    public final void addOnBeforeTransformGroupsListener(@NotNull OnBeforeTransformGroupsListener onBeforeTransformGroupsListener) {
        this.mShadeListBuilder.addOnBeforeTransformGroupsListener(onBeforeTransformGroupsListener);
    }

    public final void addPromoter(@NotNull NotifPromoter notifPromoter) {
        this.mShadeListBuilder.addPromoter(notifPromoter);
    }

    public final void setSections(@NotNull List<? extends NotifSectioner> list) {
        this.mShadeListBuilder.setSectioners(list);
    }

    public final void setVisualStabilityManager(@NotNull NotifStabilityManager notifStabilityManager) {
        this.mShadeListBuilder.setNotifStabilityManager(notifStabilityManager);
    }

    public final void addOnBeforeFinalizeFilterListener(@NotNull OnBeforeFinalizeFilterListener onBeforeFinalizeFilterListener) {
        this.mShadeListBuilder.addOnBeforeFinalizeFilterListener(onBeforeFinalizeFilterListener);
    }

    public final void addFinalizeFilter(@NotNull NotifFilter notifFilter) {
        this.mShadeListBuilder.addFinalizeFilter(notifFilter);
    }

    public final void addOnBeforeRenderListListener(@NotNull OnBeforeRenderListListener onBeforeRenderListListener) {
        this.mShadeListBuilder.addOnBeforeRenderListListener(onBeforeRenderListListener);
    }

    public final void addPreRenderInvalidator(@NotNull Invalidator invalidator) {
        this.mShadeListBuilder.addPreRenderInvalidator(invalidator);
    }

    public final void addOnAfterRenderListListener(@NotNull OnAfterRenderListListener onAfterRenderListListener) {
        this.mRenderStageManager.addOnAfterRenderListListener(onAfterRenderListListener);
    }

    public final void addOnAfterRenderGroupListener(@NotNull OnAfterRenderGroupListener onAfterRenderGroupListener) {
        this.mRenderStageManager.addOnAfterRenderGroupListener(onAfterRenderGroupListener);
    }

    public final void addOnAfterRenderEntryListener(@NotNull OnAfterRenderEntryListener onAfterRenderEntryListener) {
        this.mRenderStageManager.addOnAfterRenderEntryListener(onAfterRenderEntryListener);
    }

    @NotNull
    public final InternalNotifUpdater getInternalNotifUpdater(@Nullable String str) {
        return this.mNotifCollection.getInternalNotifUpdater(str);
    }
}
