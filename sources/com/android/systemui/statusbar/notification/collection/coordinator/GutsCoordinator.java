package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import java.io.PrintWriter;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator implements Coordinator, Dumpable {
    @NotNull
    public final GutsCoordinatorLogger logger;
    @NotNull
    public final NotifGutsViewListener mGutsListener;
    @NotNull
    public final NotifLifetimeExtender mLifetimeExtender;
    @NotNull
    public final NotifGutsViewManager notifGutsViewManager;
    @NotNull
    public final ArraySet<String> notifsExtendingLifetime = new ArraySet<>();
    @NotNull
    public final ArraySet<String> notifsWithOpenGuts = new ArraySet<>();
    @Nullable
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback;

    public GutsCoordinator(@NotNull NotifGutsViewManager notifGutsViewManager2, @NotNull GutsCoordinatorLogger gutsCoordinatorLogger, @NotNull DumpManager dumpManager) {
        this.notifGutsViewManager = notifGutsViewManager2;
        this.logger = gutsCoordinatorLogger;
        dumpManager.registerDumpable("GutsCoordinator", this);
        this.mLifetimeExtender = new GutsCoordinator$mLifetimeExtender$1(this);
        this.mGutsListener = new GutsCoordinator$mGutsListener$1(this);
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        this.notifGutsViewManager.setGutsListener(this.mGutsListener);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("  notifsWithOpenGuts: ", Integer.valueOf(this.notifsWithOpenGuts.size())));
        Iterator<String> it = this.notifsWithOpenGuts.iterator();
        while (it.hasNext()) {
            printWriter.println(Intrinsics.stringPlus("   * ", it.next()));
        }
        printWriter.println(Intrinsics.stringPlus("  notifsExtendingLifetime: ", Integer.valueOf(this.notifsExtendingLifetime.size())));
        Iterator<String> it2 = this.notifsExtendingLifetime.iterator();
        while (it2.hasNext()) {
            printWriter.println(Intrinsics.stringPlus("   * ", it2.next()));
        }
        printWriter.println(Intrinsics.stringPlus("  onEndLifetimeExtensionCallback: ", this.onEndLifetimeExtensionCallback));
    }

    public final boolean isCurrentlyShowingGuts(ListEntry listEntry) {
        return this.notifsWithOpenGuts.contains(listEntry.getKey());
    }

    public final void closeGutsAndEndLifetimeExtension(NotificationEntry notificationEntry) {
        NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback2;
        this.notifsWithOpenGuts.remove(notificationEntry.getKey());
        if (this.notifsExtendingLifetime.remove(notificationEntry.getKey()) && (onEndLifetimeExtensionCallback2 = this.onEndLifetimeExtensionCallback) != null) {
            onEndLifetimeExtensionCallback2.onEndLifetimeExtension(this.mLifetimeExtender, notificationEntry);
        }
    }
}
