package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifCoordinatorsImpl_Factory implements Factory<NotifCoordinatorsImpl> {
    public final Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
    public final Provider<BubbleCoordinator> bubbleCoordinatorProvider;
    public final Provider<ConversationCoordinator> conversationCoordinatorProvider;
    public final Provider<DataStoreCoordinator> dataStoreCoordinatorProvider;
    public final Provider<DebugModeCoordinator> debugModeCoordinatorProvider;
    public final Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GroupCountCoordinator> groupCountCoordinatorProvider;
    public final Provider<GutsCoordinator> gutsCoordinatorProvider;
    public final Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
    public final Provider<HideLocallyDismissedNotifsCoordinator> hideLocallyDismissedNotifsCoordinatorProvider;
    public final Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
    public final Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
    public final Provider<MediaCoordinator> mediaCoordinatorProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<PreparationCoordinator> preparationCoordinatorProvider;
    public final Provider<RankingCoordinator> rankingCoordinatorProvider;
    public final Provider<RemoteInputCoordinator> remoteInputCoordinatorProvider;
    public final Provider<RowAppearanceCoordinator> rowAppearanceCoordinatorProvider;
    public final Provider<SensitiveContentCoordinator> sensitiveContentCoordinatorProvider;
    public final Provider<ShadeEventCoordinator> shadeEventCoordinatorProvider;
    public final Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
    public final Provider<StackCoordinator> stackCoordinatorProvider;
    public final Provider<ViewConfigCoordinator> viewConfigCoordinatorProvider;
    public final Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;

    public NotifCoordinatorsImpl_Factory(Provider<DumpManager> provider, Provider<NotifPipelineFlags> provider2, Provider<DataStoreCoordinator> provider3, Provider<HideLocallyDismissedNotifsCoordinator> provider4, Provider<HideNotifsForOtherUsersCoordinator> provider5, Provider<KeyguardCoordinator> provider6, Provider<RankingCoordinator> provider7, Provider<AppOpsCoordinator> provider8, Provider<DeviceProvisionedCoordinator> provider9, Provider<BubbleCoordinator> provider10, Provider<HeadsUpCoordinator> provider11, Provider<GutsCoordinator> provider12, Provider<ConversationCoordinator> provider13, Provider<DebugModeCoordinator> provider14, Provider<GroupCountCoordinator> provider15, Provider<MediaCoordinator> provider16, Provider<PreparationCoordinator> provider17, Provider<RemoteInputCoordinator> provider18, Provider<RowAppearanceCoordinator> provider19, Provider<StackCoordinator> provider20, Provider<ShadeEventCoordinator> provider21, Provider<SmartspaceDedupingCoordinator> provider22, Provider<ViewConfigCoordinator> provider23, Provider<VisualStabilityCoordinator> provider24, Provider<SensitiveContentCoordinator> provider25) {
        this.dumpManagerProvider = provider;
        this.notifPipelineFlagsProvider = provider2;
        this.dataStoreCoordinatorProvider = provider3;
        this.hideLocallyDismissedNotifsCoordinatorProvider = provider4;
        this.hideNotifsForOtherUsersCoordinatorProvider = provider5;
        this.keyguardCoordinatorProvider = provider6;
        this.rankingCoordinatorProvider = provider7;
        this.appOpsCoordinatorProvider = provider8;
        this.deviceProvisionedCoordinatorProvider = provider9;
        this.bubbleCoordinatorProvider = provider10;
        this.headsUpCoordinatorProvider = provider11;
        this.gutsCoordinatorProvider = provider12;
        this.conversationCoordinatorProvider = provider13;
        this.debugModeCoordinatorProvider = provider14;
        this.groupCountCoordinatorProvider = provider15;
        this.mediaCoordinatorProvider = provider16;
        this.preparationCoordinatorProvider = provider17;
        this.remoteInputCoordinatorProvider = provider18;
        this.rowAppearanceCoordinatorProvider = provider19;
        this.stackCoordinatorProvider = provider20;
        this.shadeEventCoordinatorProvider = provider21;
        this.smartspaceDedupingCoordinatorProvider = provider22;
        this.viewConfigCoordinatorProvider = provider23;
        this.visualStabilityCoordinatorProvider = provider24;
        this.sensitiveContentCoordinatorProvider = provider25;
    }

    public NotifCoordinatorsImpl get() {
        return newInstance(this.dumpManagerProvider.get(), this.notifPipelineFlagsProvider.get(), this.dataStoreCoordinatorProvider.get(), this.hideLocallyDismissedNotifsCoordinatorProvider.get(), this.hideNotifsForOtherUsersCoordinatorProvider.get(), this.keyguardCoordinatorProvider.get(), this.rankingCoordinatorProvider.get(), this.appOpsCoordinatorProvider.get(), this.deviceProvisionedCoordinatorProvider.get(), this.bubbleCoordinatorProvider.get(), this.headsUpCoordinatorProvider.get(), this.gutsCoordinatorProvider.get(), this.conversationCoordinatorProvider.get(), this.debugModeCoordinatorProvider.get(), this.groupCountCoordinatorProvider.get(), this.mediaCoordinatorProvider.get(), this.preparationCoordinatorProvider.get(), this.remoteInputCoordinatorProvider.get(), this.rowAppearanceCoordinatorProvider.get(), this.stackCoordinatorProvider.get(), this.shadeEventCoordinatorProvider.get(), this.smartspaceDedupingCoordinatorProvider.get(), this.viewConfigCoordinatorProvider.get(), this.visualStabilityCoordinatorProvider.get(), this.sensitiveContentCoordinatorProvider.get());
    }

    public static NotifCoordinatorsImpl_Factory create(Provider<DumpManager> provider, Provider<NotifPipelineFlags> provider2, Provider<DataStoreCoordinator> provider3, Provider<HideLocallyDismissedNotifsCoordinator> provider4, Provider<HideNotifsForOtherUsersCoordinator> provider5, Provider<KeyguardCoordinator> provider6, Provider<RankingCoordinator> provider7, Provider<AppOpsCoordinator> provider8, Provider<DeviceProvisionedCoordinator> provider9, Provider<BubbleCoordinator> provider10, Provider<HeadsUpCoordinator> provider11, Provider<GutsCoordinator> provider12, Provider<ConversationCoordinator> provider13, Provider<DebugModeCoordinator> provider14, Provider<GroupCountCoordinator> provider15, Provider<MediaCoordinator> provider16, Provider<PreparationCoordinator> provider17, Provider<RemoteInputCoordinator> provider18, Provider<RowAppearanceCoordinator> provider19, Provider<StackCoordinator> provider20, Provider<ShadeEventCoordinator> provider21, Provider<SmartspaceDedupingCoordinator> provider22, Provider<ViewConfigCoordinator> provider23, Provider<VisualStabilityCoordinator> provider24, Provider<SensitiveContentCoordinator> provider25) {
        return new NotifCoordinatorsImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25);
    }

    public static NotifCoordinatorsImpl newInstance(DumpManager dumpManager, NotifPipelineFlags notifPipelineFlags, DataStoreCoordinator dataStoreCoordinator, HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator, HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, KeyguardCoordinator keyguardCoordinator, RankingCoordinator rankingCoordinator, AppOpsCoordinator appOpsCoordinator, DeviceProvisionedCoordinator deviceProvisionedCoordinator, BubbleCoordinator bubbleCoordinator, HeadsUpCoordinator headsUpCoordinator, GutsCoordinator gutsCoordinator, ConversationCoordinator conversationCoordinator, DebugModeCoordinator debugModeCoordinator, GroupCountCoordinator groupCountCoordinator, MediaCoordinator mediaCoordinator, PreparationCoordinator preparationCoordinator, RemoteInputCoordinator remoteInputCoordinator, RowAppearanceCoordinator rowAppearanceCoordinator, StackCoordinator stackCoordinator, ShadeEventCoordinator shadeEventCoordinator, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, ViewConfigCoordinator viewConfigCoordinator, VisualStabilityCoordinator visualStabilityCoordinator, SensitiveContentCoordinator sensitiveContentCoordinator) {
        return new NotifCoordinatorsImpl(dumpManager, notifPipelineFlags, dataStoreCoordinator, hideLocallyDismissedNotifsCoordinator, hideNotifsForOtherUsersCoordinator, keyguardCoordinator, rankingCoordinator, appOpsCoordinator, deviceProvisionedCoordinator, bubbleCoordinator, headsUpCoordinator, gutsCoordinator, conversationCoordinator, debugModeCoordinator, groupCountCoordinator, mediaCoordinator, preparationCoordinator, remoteInputCoordinator, rowAppearanceCoordinator, stackCoordinator, shadeEventCoordinator, smartspaceDedupingCoordinator, viewConfigCoordinator, visualStabilityCoordinator, sensitiveContentCoordinator);
    }
}
