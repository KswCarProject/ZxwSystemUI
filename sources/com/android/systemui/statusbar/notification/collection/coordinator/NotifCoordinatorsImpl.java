package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCoordinators.kt */
public final class NotifCoordinatorsImpl implements NotifCoordinators {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final List<Coordinator> mCoordinators;
    @NotNull
    public final List<NotifSectioner> mOrderedSections;

    public NotifCoordinatorsImpl(@NotNull DumpManager dumpManager, @NotNull NotifPipelineFlags notifPipelineFlags, @NotNull DataStoreCoordinator dataStoreCoordinator, @NotNull HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator, @NotNull HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, @NotNull KeyguardCoordinator keyguardCoordinator, @NotNull RankingCoordinator rankingCoordinator, @NotNull AppOpsCoordinator appOpsCoordinator, @NotNull DeviceProvisionedCoordinator deviceProvisionedCoordinator, @NotNull BubbleCoordinator bubbleCoordinator, @NotNull HeadsUpCoordinator headsUpCoordinator, @NotNull GutsCoordinator gutsCoordinator, @NotNull ConversationCoordinator conversationCoordinator, @NotNull DebugModeCoordinator debugModeCoordinator, @NotNull GroupCountCoordinator groupCountCoordinator, @NotNull MediaCoordinator mediaCoordinator, @NotNull PreparationCoordinator preparationCoordinator, @NotNull RemoteInputCoordinator remoteInputCoordinator, @NotNull RowAppearanceCoordinator rowAppearanceCoordinator, @NotNull StackCoordinator stackCoordinator, @NotNull ShadeEventCoordinator shadeEventCoordinator, @NotNull SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, @NotNull ViewConfigCoordinator viewConfigCoordinator, @NotNull VisualStabilityCoordinator visualStabilityCoordinator, @NotNull SensitiveContentCoordinator sensitiveContentCoordinator) {
        ArrayList arrayList = new ArrayList();
        this.mCoordinators = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOrderedSections = arrayList2;
        DumpManager dumpManager2 = dumpManager;
        dumpManager.registerDumpable("NotifCoordinators", this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            DataStoreCoordinator dataStoreCoordinator2 = dataStoreCoordinator;
            arrayList.add(dataStoreCoordinator);
        }
        HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator2 = hideLocallyDismissedNotifsCoordinator;
        arrayList.add(hideLocallyDismissedNotifsCoordinator);
        HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator2 = hideNotifsForOtherUsersCoordinator;
        arrayList.add(hideNotifsForOtherUsersCoordinator);
        KeyguardCoordinator keyguardCoordinator2 = keyguardCoordinator;
        arrayList.add(keyguardCoordinator);
        RankingCoordinator rankingCoordinator2 = rankingCoordinator;
        arrayList.add(rankingCoordinator);
        AppOpsCoordinator appOpsCoordinator2 = appOpsCoordinator;
        arrayList.add(appOpsCoordinator);
        arrayList.add(deviceProvisionedCoordinator);
        arrayList.add(bubbleCoordinator);
        arrayList.add(debugModeCoordinator);
        arrayList.add(conversationCoordinator);
        arrayList.add(groupCountCoordinator);
        arrayList.add(mediaCoordinator);
        arrayList.add(rowAppearanceCoordinator);
        arrayList.add(stackCoordinator);
        arrayList.add(shadeEventCoordinator);
        arrayList.add(viewConfigCoordinator);
        arrayList.add(visualStabilityCoordinator);
        arrayList.add(sensitiveContentCoordinator);
        if (notifPipelineFlags.isSmartspaceDedupingEnabled()) {
            arrayList.add(smartspaceDedupingCoordinator);
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList.add(headsUpCoordinator);
            arrayList.add(gutsCoordinator);
            arrayList.add(preparationCoordinator);
            arrayList.add(remoteInputCoordinator);
        } else {
            HeadsUpCoordinator headsUpCoordinator2 = headsUpCoordinator;
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList2.add(headsUpCoordinator.getSectioner());
        }
        arrayList2.add(appOpsCoordinator.getSectioner());
        arrayList2.add(conversationCoordinator.getSectioner());
        arrayList2.add(rankingCoordinator.getAlertingSectioner());
        arrayList2.add(rankingCoordinator.getSilentSectioner());
        arrayList2.add(rankingCoordinator.getMinimizedSectioner());
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        for (Coordinator attach : this.mCoordinators) {
            attach.attach(notifPipeline);
        }
        notifPipeline.setSections(this.mOrderedSections);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println();
        printWriter.println("NotifCoordinators:");
        for (Coordinator coordinator : this.mCoordinators) {
            printWriter.println(Intrinsics.stringPlus("\t", coordinator.getClass()));
        }
        for (NotifSectioner name : this.mOrderedSections) {
            printWriter.println(Intrinsics.stringPlus("\t", name.getName()));
        }
    }

    /* compiled from: NotifCoordinators.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
