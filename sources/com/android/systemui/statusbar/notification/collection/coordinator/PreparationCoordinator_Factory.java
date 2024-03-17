package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PreparationCoordinator_Factory implements Factory<PreparationCoordinator> {
    public final Provider<NotifUiAdjustmentProvider> adjustmentProvider;
    public final Provider<BindEventManagerImpl> bindEventManagerProvider;
    public final Provider<NotifInflationErrorManager> errorManagerProvider;
    public final Provider<PreparationCoordinatorLogger> loggerProvider;
    public final Provider<NotifInflater> notifInflaterProvider;
    public final Provider<IStatusBarService> serviceProvider;
    public final Provider<NotifViewBarn> viewBarnProvider;

    public PreparationCoordinator_Factory(Provider<PreparationCoordinatorLogger> provider, Provider<NotifInflater> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifViewBarn> provider4, Provider<NotifUiAdjustmentProvider> provider5, Provider<IStatusBarService> provider6, Provider<BindEventManagerImpl> provider7) {
        this.loggerProvider = provider;
        this.notifInflaterProvider = provider2;
        this.errorManagerProvider = provider3;
        this.viewBarnProvider = provider4;
        this.adjustmentProvider = provider5;
        this.serviceProvider = provider6;
        this.bindEventManagerProvider = provider7;
    }

    public PreparationCoordinator get() {
        return newInstance(this.loggerProvider.get(), this.notifInflaterProvider.get(), this.errorManagerProvider.get(), this.viewBarnProvider.get(), this.adjustmentProvider.get(), this.serviceProvider.get(), this.bindEventManagerProvider.get());
    }

    public static PreparationCoordinator_Factory create(Provider<PreparationCoordinatorLogger> provider, Provider<NotifInflater> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifViewBarn> provider4, Provider<NotifUiAdjustmentProvider> provider5, Provider<IStatusBarService> provider6, Provider<BindEventManagerImpl> provider7) {
        return new PreparationCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static PreparationCoordinator newInstance(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, NotifUiAdjustmentProvider notifUiAdjustmentProvider, IStatusBarService iStatusBarService, BindEventManagerImpl bindEventManagerImpl) {
        return new PreparationCoordinator(preparationCoordinatorLogger, notifInflater, notifInflationErrorManager, notifViewBarn, notifUiAdjustmentProvider, iStatusBarService, bindEventManagerImpl);
    }
}
