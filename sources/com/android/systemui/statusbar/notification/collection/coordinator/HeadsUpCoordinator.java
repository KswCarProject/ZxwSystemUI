package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArrayMap;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator implements Coordinator {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback mEndLifetimeExtension;
    @NotNull
    public final ArrayMap<String, Long> mEntriesBindingUntil = new ArrayMap<>();
    @NotNull
    public final DelayableExecutor mExecutor;
    @NotNull
    public final HeadsUpManager mHeadsUpManager;
    @NotNull
    public final HeadsUpViewBinder mHeadsUpViewBinder;
    @NotNull
    public final NodeController mIncomingHeaderController;
    @NotNull
    public final HeadsUpCoordinator$mLifetimeExtender$1 mLifetimeExtender = new HeadsUpCoordinator$mLifetimeExtender$1(this);
    @NotNull
    public final HeadsUpCoordinatorLogger mLogger;
    @NotNull
    public final HeadsUpCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new HeadsUpCoordinator$mNotifCollectionListener$1(this);
    public NotifPipeline mNotifPipeline;
    @NotNull
    public final HeadsUpCoordinator$mNotifPromoter$1 mNotifPromoter = new HeadsUpCoordinator$mNotifPromoter$1(this);
    @NotNull
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    @NotNull
    public final ArrayMap<NotificationEntry, Runnable> mNotifsExtendingLifetime = new ArrayMap<>();
    public long mNow = -1;
    @NotNull
    public final HeadsUpCoordinator$mOnHeadsUpChangedListener$1 mOnHeadsUpChangedListener = new HeadsUpCoordinator$mOnHeadsUpChangedListener$1(this);
    @NotNull
    public final LinkedHashMap<String, PostedEntry> mPostedEntries = new LinkedHashMap<>();
    @NotNull
    public final NotificationRemoteInputManager mRemoteInputManager;
    @NotNull
    public final SystemClock mSystemClock;
    @NotNull
    public final NotifSectioner sectioner = new HeadsUpCoordinator$sectioner$1(this);

    public HeadsUpCoordinator(@NotNull HeadsUpCoordinatorLogger headsUpCoordinatorLogger, @NotNull SystemClock systemClock, @NotNull HeadsUpManager headsUpManager, @NotNull HeadsUpViewBinder headsUpViewBinder, @NotNull NotificationInterruptStateProvider notificationInterruptStateProvider, @NotNull NotificationRemoteInputManager notificationRemoteInputManager, @NotNull NodeController nodeController, @NotNull DelayableExecutor delayableExecutor) {
        this.mLogger = headsUpCoordinatorLogger;
        this.mSystemClock = systemClock;
        this.mHeadsUpManager = headsUpManager;
        this.mHeadsUpViewBinder = headsUpViewBinder;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mIncomingHeaderController = nodeController;
        this.mExecutor = delayableExecutor;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        this.mHeadsUpManager.addListener(this.mOnHeadsUpChangedListener);
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeTransformGroupsListener(new HeadsUpCoordinator$attach$1(this));
        notifPipeline.addOnBeforeFinalizeFilterListener(new HeadsUpCoordinator$attach$2(this));
        notifPipeline.addPromoter(this.mNotifPromoter);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    public final void onHeadsUpViewBound(NotificationEntry notificationEntry) {
        this.mHeadsUpManager.showNotification(notificationEntry);
        this.mEntriesBindingUntil.remove(notificationEntry.getKey());
    }

    public final void onBeforeTransformGroups(@NotNull List<? extends ListEntry> list) {
        this.mNow = this.mSystemClock.currentTimeMillis();
        if (!this.mPostedEntries.isEmpty()) {
            Object unused = HeadsUpCoordinatorKt.modifyHuns(this.mHeadsUpManager, new HeadsUpCoordinator$onBeforeTransformGroups$1(this));
        }
    }

    public final void onBeforeFinalizeFilter(@NotNull List<? extends ListEntry> list) {
        Object unused = HeadsUpCoordinatorKt.modifyHuns(this.mHeadsUpManager, new HeadsUpCoordinator$onBeforeFinalizeFilter$1(this, list));
    }

    public final NotificationEntry findAlertOverride(List<PostedEntry> list, Function1<? super String, ? extends GroupLocation> function1) {
        PostedEntry postedEntry = (PostedEntry) SequencesKt___SequencesKt.firstOrNull(SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list), HeadsUpCoordinator$findAlertOverride$1.INSTANCE), new HeadsUpCoordinator$findAlertOverride$$inlined$sortedBy$1()));
        if (postedEntry == null) {
            return null;
        }
        NotificationEntry entry = postedEntry.getEntry();
        boolean z = true;
        if (!(function1.invoke(entry.getKey()) == GroupLocation.Isolated && entry.getSbn().getNotification().getGroupAlertBehavior() == 1)) {
            z = false;
        }
        if (z) {
            return entry;
        }
        return null;
    }

    public final NotificationEntry findBestTransferChild(List<NotificationEntry> list, Function1<? super String, ? extends GroupLocation> function1) {
        return (NotificationEntry) SequencesKt___SequencesKt.firstOrNull(SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.filter(SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list), HeadsUpCoordinator$findBestTransferChild$1.INSTANCE), new HeadsUpCoordinator$findBestTransferChild$2(function1)), ComparisonsKt__ComparisonsKt.compareBy(new HeadsUpCoordinator$findBestTransferChild$3(this), HeadsUpCoordinator$findBestTransferChild$4.INSTANCE)));
    }

    public final Map<String, GroupLocation> getGroupLocationsByKey(List<? extends ListEntry> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (ListEntry listEntry : list) {
            if (listEntry instanceof NotificationEntry) {
                linkedHashMap.put(((NotificationEntry) listEntry).getKey(), GroupLocation.Isolated);
            } else if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    linkedHashMap.put(summary.getKey(), GroupLocation.Summary);
                }
                for (NotificationEntry key : groupEntry.getChildren()) {
                    linkedHashMap.put(key.getKey(), GroupLocation.Child);
                }
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("unhandled type ", listEntry).toString());
            }
        }
        return linkedHashMap;
    }

    public final void handlePostedEntry(PostedEntry postedEntry, HunMutator hunMutator, String str) {
        this.mLogger.logPostedEntryWillEvaluate(postedEntry, str);
        if (postedEntry.getWasAdded()) {
            if (postedEntry.getShouldHeadsUpEver()) {
                bindForAsyncHeadsUp(postedEntry);
            }
        } else if (postedEntry.isHeadsUpAlready()) {
            if (postedEntry.getShouldHeadsUpEver()) {
                if (postedEntry.isAlerting()) {
                    hunMutator.updateNotification(postedEntry.getKey(), postedEntry.getShouldHeadsUpAgain());
                }
            } else if (postedEntry.isAlerting()) {
                hunMutator.removeNotification(postedEntry.getKey(), false);
            } else {
                cancelHeadsUpBind(postedEntry.getEntry());
            }
        } else if (postedEntry.getShouldHeadsUpEver() && postedEntry.getShouldHeadsUpAgain()) {
            bindForAsyncHeadsUp(postedEntry);
        }
    }

    public final void cancelHeadsUpBind(NotificationEntry notificationEntry) {
        this.mEntriesBindingUntil.remove(notificationEntry.getKey());
        this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
    }

    public final void bindForAsyncHeadsUp(PostedEntry postedEntry) {
        this.mEntriesBindingUntil.put(postedEntry.getKey(), Long.valueOf(this.mNow + 1000));
        this.mHeadsUpViewBinder.bindHeadsUpView(postedEntry.getEntry(), new HeadsUpCoordinator$bindForAsyncHeadsUp$1(this));
    }

    public final boolean shouldHunAgain(NotificationEntry notificationEntry) {
        return !notificationEntry.hasInterrupted() || (notificationEntry.getSbn().getNotification().flags & 8) == 0;
    }

    @NotNull
    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public final boolean isSticky(NotificationEntry notificationEntry) {
        return this.mHeadsUpManager.isSticky(notificationEntry.getKey());
    }

    public final boolean isEntryBinding(ListEntry listEntry) {
        Long l = this.mEntriesBindingUntil.get(listEntry.getKey());
        return l != null && l.longValue() >= this.mNow;
    }

    public final boolean isAttemptingToShowHun(ListEntry listEntry) {
        return this.mHeadsUpManager.isAlerting(listEntry.getKey()) || isEntryBinding(listEntry);
    }

    public final boolean isGoingToShowHunNoRetract(ListEntry listEntry) {
        PostedEntry postedEntry = this.mPostedEntries.get(listEntry.getKey());
        Boolean valueOf = postedEntry == null ? null : Boolean.valueOf(postedEntry.getCalculateShouldBeHeadsUpNoRetract());
        return valueOf == null ? isAttemptingToShowHun(listEntry) : valueOf.booleanValue();
    }

    public final boolean isGoingToShowHunStrict(ListEntry listEntry) {
        PostedEntry postedEntry = this.mPostedEntries.get(listEntry.getKey());
        Boolean valueOf = postedEntry == null ? null : Boolean.valueOf(postedEntry.getCalculateShouldBeHeadsUpStrict());
        return valueOf == null ? isAttemptingToShowHun(listEntry) : valueOf.booleanValue();
    }

    public final void endNotifLifetimeExtensionIfExtended(NotificationEntry notificationEntry) {
        if (this.mNotifsExtendingLifetime.containsKey(notificationEntry)) {
            Runnable remove = this.mNotifsExtendingLifetime.remove(notificationEntry);
            if (remove != null) {
                remove.run();
            }
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mEndLifetimeExtension;
            if (onEndLifetimeExtensionCallback != null) {
                onEndLifetimeExtensionCallback.onEndLifetimeExtension(this.mLifetimeExtender, notificationEntry);
            }
        }
    }

    /* compiled from: HeadsUpCoordinator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* compiled from: HeadsUpCoordinator.kt */
    public static final class PostedEntry {
        @NotNull
        public final NotificationEntry entry;
        public boolean isAlerting;
        public boolean isBinding;
        @NotNull
        public final String key;
        public boolean shouldHeadsUpAgain;
        public boolean shouldHeadsUpEver;
        public final boolean wasAdded;
        public boolean wasUpdated;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PostedEntry)) {
                return false;
            }
            PostedEntry postedEntry = (PostedEntry) obj;
            return Intrinsics.areEqual((Object) this.entry, (Object) postedEntry.entry) && this.wasAdded == postedEntry.wasAdded && this.wasUpdated == postedEntry.wasUpdated && this.shouldHeadsUpEver == postedEntry.shouldHeadsUpEver && this.shouldHeadsUpAgain == postedEntry.shouldHeadsUpAgain && this.isAlerting == postedEntry.isAlerting && this.isBinding == postedEntry.isBinding;
        }

        public int hashCode() {
            int hashCode = this.entry.hashCode() * 31;
            boolean z = this.wasAdded;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (hashCode + (z ? 1 : 0)) * 31;
            boolean z3 = this.wasUpdated;
            if (z3) {
                z3 = true;
            }
            int i2 = (i + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.shouldHeadsUpEver;
            if (z4) {
                z4 = true;
            }
            int i3 = (i2 + (z4 ? 1 : 0)) * 31;
            boolean z5 = this.shouldHeadsUpAgain;
            if (z5) {
                z5 = true;
            }
            int i4 = (i3 + (z5 ? 1 : 0)) * 31;
            boolean z6 = this.isAlerting;
            if (z6) {
                z6 = true;
            }
            int i5 = (i4 + (z6 ? 1 : 0)) * 31;
            boolean z7 = this.isBinding;
            if (!z7) {
                z2 = z7;
            }
            return i5 + (z2 ? 1 : 0);
        }

        @NotNull
        public String toString() {
            return "PostedEntry(entry=" + this.entry + ", wasAdded=" + this.wasAdded + ", wasUpdated=" + this.wasUpdated + ", shouldHeadsUpEver=" + this.shouldHeadsUpEver + ", shouldHeadsUpAgain=" + this.shouldHeadsUpAgain + ", isAlerting=" + this.isAlerting + ", isBinding=" + this.isBinding + ')';
        }

        public PostedEntry(@NotNull NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            this.entry = notificationEntry;
            this.wasAdded = z;
            this.wasUpdated = z2;
            this.shouldHeadsUpEver = z3;
            this.shouldHeadsUpAgain = z4;
            this.isAlerting = z5;
            this.isBinding = z6;
            this.key = notificationEntry.getKey();
        }

        @NotNull
        public final NotificationEntry getEntry() {
            return this.entry;
        }

        public final boolean getWasAdded() {
            return this.wasAdded;
        }

        public final void setWasUpdated(boolean z) {
            this.wasUpdated = z;
        }

        public final boolean getShouldHeadsUpEver() {
            return this.shouldHeadsUpEver;
        }

        public final void setShouldHeadsUpEver(boolean z) {
            this.shouldHeadsUpEver = z;
        }

        public final boolean getShouldHeadsUpAgain() {
            return this.shouldHeadsUpAgain;
        }

        public final void setShouldHeadsUpAgain(boolean z) {
            this.shouldHeadsUpAgain = z;
        }

        public final boolean isAlerting() {
            return this.isAlerting;
        }

        public final void setAlerting(boolean z) {
            this.isAlerting = z;
        }

        public final boolean isBinding() {
            return this.isBinding;
        }

        public final void setBinding(boolean z) {
            this.isBinding = z;
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        public final boolean isHeadsUpAlready() {
            return this.isAlerting || this.isBinding;
        }

        public final boolean getCalculateShouldBeHeadsUpStrict() {
            return this.shouldHeadsUpEver && (this.wasAdded || this.shouldHeadsUpAgain || isHeadsUpAlready());
        }

        public final boolean getCalculateShouldBeHeadsUpNoRetract() {
            return isHeadsUpAlready() || (this.shouldHeadsUpEver && (this.wasAdded || this.shouldHeadsUpAgain));
        }
    }
}
