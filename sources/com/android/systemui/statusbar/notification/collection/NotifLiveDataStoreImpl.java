package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import com.android.systemui.util.Assert;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifLiveDataStoreImpl.kt */
public final class NotifLiveDataStoreImpl implements NotifLiveDataStore {
    @NotNull
    public final NotifLiveData<Integer> activeNotifCount;
    @NotNull
    public final NotifLiveDataImpl<Integer> activeNotifCountPrivate;
    @NotNull
    public final NotifLiveData<List<NotificationEntry>> activeNotifList;
    @NotNull
    public final NotifLiveDataImpl<List<NotificationEntry>> activeNotifListPrivate;
    @NotNull
    public final NotifLiveData<Boolean> hasActiveNotifs;
    @NotNull
    public final NotifLiveDataImpl<Boolean> hasActiveNotifsPrivate;
    @NotNull
    public final Executor mainExecutor;

    public final void setActiveNotifList(@NotNull List<NotificationEntry> list) {
        Trace.beginSection("NotifLiveDataStore.setActiveNotifList");
        try {
            Assert.isMainThread();
            List unmodifiableList = Collections.unmodifiableList(CollectionsKt___CollectionsKt.toList(list));
            Function0[] function0Arr = new Function0[3];
            boolean z = false;
            function0Arr[0] = this.activeNotifListPrivate.setValueAndProvideDispatcher(unmodifiableList);
            function0Arr[1] = this.activeNotifCountPrivate.setValueAndProvideDispatcher(Integer.valueOf(unmodifiableList.size()));
            NotifLiveDataImpl<Boolean> notifLiveDataImpl = this.hasActiveNotifsPrivate;
            if (!unmodifiableList.isEmpty()) {
                z = true;
            }
            function0Arr[2] = notifLiveDataImpl.setValueAndProvideDispatcher(Boolean.valueOf(z));
            for (Function0 invoke : CollectionsKt__CollectionsKt.listOf(function0Arr)) {
                invoke.invoke();
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public NotifLiveDataStoreImpl(@NotNull Executor executor) {
        this.mainExecutor = executor;
        NotifLiveDataImpl<Boolean> notifLiveDataImpl = new NotifLiveDataImpl<>("hasActiveNotifs", Boolean.FALSE, executor);
        this.hasActiveNotifsPrivate = notifLiveDataImpl;
        NotifLiveDataImpl<Integer> notifLiveDataImpl2 = new NotifLiveDataImpl<>("activeNotifCount", 0, executor);
        this.activeNotifCountPrivate = notifLiveDataImpl2;
        NotifLiveDataImpl<List<NotificationEntry>> notifLiveDataImpl3 = new NotifLiveDataImpl<>("activeNotifList", CollectionsKt__CollectionsKt.emptyList(), executor);
        this.activeNotifListPrivate = notifLiveDataImpl3;
        this.hasActiveNotifs = notifLiveDataImpl;
        this.activeNotifCount = notifLiveDataImpl2;
        this.activeNotifList = notifLiveDataImpl3;
    }

    @NotNull
    public NotifLiveData<Boolean> getHasActiveNotifs() {
        return this.hasActiveNotifs;
    }

    @NotNull
    public NotifLiveData<Integer> getActiveNotifCount() {
        return this.activeNotifCount;
    }

    @NotNull
    public NotifLiveData<List<NotificationEntry>> getActiveNotifList() {
        return this.activeNotifList;
    }
}
