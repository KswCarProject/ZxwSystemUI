package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import androidx.lifecycle.Observer;
import com.android.systemui.util.ListenerSet;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifLiveDataStoreImpl.kt */
public final class NotifLiveDataImpl<T> implements NotifLiveData<T> {
    @NotNull
    public final ListenerSet<Observer<T>> asyncObservers = new ListenerSet<>();
    @NotNull
    public final AtomicReference<T> atomicValue;
    @Nullable
    public T lastAsyncValue;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final String name;
    @NotNull
    public final ListenerSet<Observer<T>> syncObservers = new ListenerSet<>();

    public NotifLiveDataImpl(@NotNull String str, T t, @NotNull Executor executor) {
        this.name = str;
        this.mainExecutor = executor;
        this.atomicValue = new AtomicReference<>(t);
    }

    public final void dispatchToAsyncObservers() {
        T t = this.atomicValue.get();
        if (!Intrinsics.areEqual((Object) this.lastAsyncValue, (Object) t)) {
            this.lastAsyncValue = t;
            Trace.beginSection("NotifLiveData(" + this.name + ").dispatchToAsyncObservers");
            try {
                for (Observer onChanged : this.asyncObservers) {
                    onChanged.onChanged(t);
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
    }

    public T getValue() {
        return this.atomicValue.get();
    }

    @NotNull
    public final Function0<Unit> setValueAndProvideDispatcher(T t) {
        return !Intrinsics.areEqual((Object) this.atomicValue.getAndSet(t), (Object) t) ? new NotifLiveDataImpl$setValueAndProvideDispatcher$1(this, t) : NotifLiveDataImpl$setValueAndProvideDispatcher$2.INSTANCE;
    }

    public void addSyncObserver(@NotNull Observer<T> observer) {
        this.syncObservers.addIfAbsent(observer);
    }

    public void removeObserver(@NotNull Observer<T> observer) {
        this.syncObservers.remove(observer);
        this.asyncObservers.remove(observer);
    }
}
