package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import androidx.lifecycle.Observer;
import com.android.systemui.util.ListenerSetKt;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: NotifLiveDataStoreImpl.kt */
public final class NotifLiveDataImpl$setValueAndProvideDispatcher$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ T $value;
    public final /* synthetic */ NotifLiveDataImpl<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotifLiveDataImpl$setValueAndProvideDispatcher$1(NotifLiveDataImpl<T> notifLiveDataImpl, T t) {
        super(0);
        this.this$0 = notifLiveDataImpl;
        this.$value = t;
    }

    public final void invoke() {
        if (ListenerSetKt.isNotEmpty(this.this$0.syncObservers)) {
            NotifLiveDataImpl<T> notifLiveDataImpl = this.this$0;
            T t = this.$value;
            Trace.beginSection("NotifLiveData(" + this.this$0.name + ").dispatchToSyncObservers");
            try {
                for (Observer onChanged : notifLiveDataImpl.syncObservers) {
                    onChanged.onChanged(t);
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
        if (ListenerSetKt.isNotEmpty(this.this$0.asyncObservers)) {
            Executor access$getMainExecutor$p = this.this$0.mainExecutor;
            final NotifLiveDataImpl<T> notifLiveDataImpl2 = this.this$0;
            access$getMainExecutor$p.execute(new Runnable() {
                public final void run() {
                    notifLiveDataImpl2.dispatchToAsyncObservers();
                }
            });
        }
    }
}
