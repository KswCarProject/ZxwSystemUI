package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatefulControlSubscriber.kt */
public final class StatefulControlSubscriber extends IControlsSubscriber.Stub {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final DelayableExecutor bgExecutor;
    @NotNull
    public final ControlsController controller;
    @NotNull
    public final ControlsProviderLifecycleManager provider;
    public final long requestLimit;
    @Nullable
    public IControlsSubscription subscription;
    public boolean subscriptionOpen;

    public StatefulControlSubscriber(@NotNull ControlsController controlsController, @NotNull ControlsProviderLifecycleManager controlsProviderLifecycleManager, @NotNull DelayableExecutor delayableExecutor, long j) {
        this.controller = controlsController;
        this.provider = controlsProviderLifecycleManager;
        this.bgExecutor = delayableExecutor;
        this.requestLimit = j;
    }

    /* compiled from: StatefulControlSubscriber.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void run(IBinder iBinder, Function0<Unit> function0) {
        if (Intrinsics.areEqual((Object) this.provider.getToken(), (Object) iBinder)) {
            this.bgExecutor.execute(new StatefulControlSubscriber$run$1(function0));
        }
    }

    public void onSubscribe(@NotNull IBinder iBinder, @NotNull IControlsSubscription iControlsSubscription) {
        run(iBinder, new StatefulControlSubscriber$onSubscribe$1(this, iControlsSubscription));
    }

    public void onNext(@NotNull IBinder iBinder, @NotNull Control control) {
        run(iBinder, new StatefulControlSubscriber$onNext$1(this, iBinder, control));
    }

    public void onError(@NotNull IBinder iBinder, @NotNull String str) {
        run(iBinder, new StatefulControlSubscriber$onError$1(this, str));
    }

    public void onComplete(@NotNull IBinder iBinder) {
        run(iBinder, new StatefulControlSubscriber$onComplete$1(this));
    }

    public final void cancel() {
        if (this.subscriptionOpen) {
            this.bgExecutor.execute(new StatefulControlSubscriber$cancel$1(this));
        }
    }
}
