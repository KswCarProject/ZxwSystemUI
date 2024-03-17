package com.android.systemui.statusbar.notification.collection.init;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifPipelineInitializer_Factory implements Factory<NotifPipelineInitializer> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GroupCoalescer> groupCoalescerProvider;
    public final Provider<ShadeListBuilder> listBuilderProvider;
    public final Provider<NotifCollection> notifCollectionProvider;
    public final Provider<NotifCoordinators> notifCoordinatorsProvider;
    public final Provider<NotifInflaterImpl> notifInflaterProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> pipelineWrapperProvider;
    public final Provider<RenderStageManager> renderStageManagerProvider;
    public final Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;

    public NotifPipelineInitializer_Factory(Provider<NotifPipeline> provider, Provider<GroupCoalescer> provider2, Provider<NotifCollection> provider3, Provider<ShadeListBuilder> provider4, Provider<RenderStageManager> provider5, Provider<NotifCoordinators> provider6, Provider<NotifInflaterImpl> provider7, Provider<DumpManager> provider8, Provider<ShadeViewManagerFactory> provider9, Provider<NotifPipelineFlags> provider10) {
        this.pipelineWrapperProvider = provider;
        this.groupCoalescerProvider = provider2;
        this.notifCollectionProvider = provider3;
        this.listBuilderProvider = provider4;
        this.renderStageManagerProvider = provider5;
        this.notifCoordinatorsProvider = provider6;
        this.notifInflaterProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.shadeViewManagerFactoryProvider = provider9;
        this.notifPipelineFlagsProvider = provider10;
    }

    public NotifPipelineInitializer get() {
        return newInstance(this.pipelineWrapperProvider.get(), this.groupCoalescerProvider.get(), this.notifCollectionProvider.get(), this.listBuilderProvider.get(), this.renderStageManagerProvider.get(), this.notifCoordinatorsProvider.get(), this.notifInflaterProvider.get(), this.dumpManagerProvider.get(), this.shadeViewManagerFactoryProvider.get(), this.notifPipelineFlagsProvider.get());
    }

    public static NotifPipelineInitializer_Factory create(Provider<NotifPipeline> provider, Provider<GroupCoalescer> provider2, Provider<NotifCollection> provider3, Provider<ShadeListBuilder> provider4, Provider<RenderStageManager> provider5, Provider<NotifCoordinators> provider6, Provider<NotifInflaterImpl> provider7, Provider<DumpManager> provider8, Provider<ShadeViewManagerFactory> provider9, Provider<NotifPipelineFlags> provider10) {
        return new NotifPipelineInitializer_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static NotifPipelineInitializer newInstance(NotifPipeline notifPipeline, GroupCoalescer groupCoalescer, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, RenderStageManager renderStageManager, NotifCoordinators notifCoordinators, NotifInflaterImpl notifInflaterImpl, DumpManager dumpManager, ShadeViewManagerFactory shadeViewManagerFactory, NotifPipelineFlags notifPipelineFlags) {
        return new NotifPipelineInitializer(notifPipeline, groupCoalescer, notifCollection, shadeListBuilder, renderStageManager, notifCoordinators, notifInflaterImpl, dumpManager, shadeViewManagerFactory, notifPipelineFlags);
    }
}
