package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LowPriorityInflationHelper_Factory implements Factory<LowPriorityInflationHelper> {
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<RowContentBindStage> rowContentBindStageProvider;

    public LowPriorityInflationHelper_Factory(Provider<NotificationGroupManagerLegacy> provider, Provider<RowContentBindStage> provider2, Provider<NotifPipelineFlags> provider3) {
        this.groupManagerProvider = provider;
        this.rowContentBindStageProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
    }

    public LowPriorityInflationHelper get() {
        return newInstance(this.groupManagerProvider.get(), this.rowContentBindStageProvider.get(), this.notifPipelineFlagsProvider.get());
    }

    public static LowPriorityInflationHelper_Factory create(Provider<NotificationGroupManagerLegacy> provider, Provider<RowContentBindStage> provider2, Provider<NotifPipelineFlags> provider3) {
        return new LowPriorityInflationHelper_Factory(provider, provider2, provider3);
    }

    public static LowPriorityInflationHelper newInstance(NotificationGroupManagerLegacy notificationGroupManagerLegacy, RowContentBindStage rowContentBindStage, NotifPipelineFlags notifPipelineFlags) {
        return new LowPriorityInflationHelper(notificationGroupManagerLegacy, rowContentBindStage, notifPipelineFlags);
    }
}
