package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsProviderLifecycleManager.kt */
public final class ControlsProviderLifecycleManager implements IBinder.DeathRecipient {
    public static final int BIND_FLAGS = 67109121;
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final String TAG = ControlsProviderLifecycleManager.class.getSimpleName();
    @NotNull
    public final IControlsActionCallback.Stub actionCallbackService;
    public int bindTryCount;
    @NotNull
    public final ComponentName componentName;
    @NotNull
    public final Context context;
    @NotNull
    public final DelayableExecutor executor;
    @NotNull
    public final Intent intent;
    @Nullable
    public Runnable onLoadCanceller;
    @GuardedBy({"queuedServiceMethods"})
    @NotNull
    public final Set<ServiceMethod> queuedServiceMethods = new ArraySet();
    public boolean requiresBound;
    @NotNull
    public final ControlsProviderLifecycleManager$serviceConnection$1 serviceConnection;
    @NotNull
    public final IBinder token = new Binder();
    @NotNull
    public final UserHandle user;
    @Nullable
    public ServiceWrapper wrapper;

    public ControlsProviderLifecycleManager(@NotNull Context context2, @NotNull DelayableExecutor delayableExecutor, @NotNull IControlsActionCallback.Stub stub, @NotNull UserHandle userHandle, @NotNull ComponentName componentName2) {
        this.context = context2;
        this.executor = delayableExecutor;
        this.actionCallbackService = stub;
        this.user = userHandle;
        this.componentName = componentName2;
        Intent intent2 = new Intent();
        intent2.setComponent(getComponentName());
        Bundle bundle = new Bundle();
        bundle.putBinder("CALLBACK_TOKEN", getToken());
        Unit unit = Unit.INSTANCE;
        intent2.putExtra("CALLBACK_BUNDLE", bundle);
        this.intent = intent2;
        this.serviceConnection = new ControlsProviderLifecycleManager$serviceConnection$1(this);
    }

    @NotNull
    public final UserHandle getUser() {
        return this.user;
    }

    @NotNull
    public final ComponentName getComponentName() {
        return this.componentName;
    }

    @NotNull
    public final IBinder getToken() {
        return this.token;
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void bindService(boolean z) {
        this.executor.execute(new ControlsProviderLifecycleManager$bindService$1(this, z));
    }

    public final void handlePendingServiceMethods() {
        ArraySet<ServiceMethod> arraySet;
        synchronized (this.queuedServiceMethods) {
            arraySet = new ArraySet<>(this.queuedServiceMethods);
            this.queuedServiceMethods.clear();
        }
        for (ServiceMethod run : arraySet) {
            run.run();
        }
    }

    public void binderDied() {
        if (this.wrapper != null) {
            this.wrapper = null;
            if (this.requiresBound) {
                Log.d(this.TAG, "binderDied");
            }
        }
    }

    public final void queueServiceMethod(ServiceMethod serviceMethod) {
        synchronized (this.queuedServiceMethods) {
            this.queuedServiceMethods.add(serviceMethod);
        }
    }

    public final void invokeOrQueue(ServiceMethod serviceMethod) {
        Unit unit;
        if (this.wrapper == null) {
            unit = null;
        } else {
            serviceMethod.run();
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            queueServiceMethod(serviceMethod);
            bindService(true);
        }
    }

    public final void maybeBindAndLoad(@NotNull IControlsSubscriber.Stub stub) {
        this.onLoadCanceller = this.executor.executeDelayed(new ControlsProviderLifecycleManager$maybeBindAndLoad$1(this, stub), 20, TimeUnit.SECONDS);
        invokeOrQueue(new Load(stub));
    }

    public final void maybeBindAndLoadSuggested(@NotNull IControlsSubscriber.Stub stub) {
        this.onLoadCanceller = this.executor.executeDelayed(new ControlsProviderLifecycleManager$maybeBindAndLoadSuggested$1(this, stub), 20, TimeUnit.SECONDS);
        invokeOrQueue(new Suggest(stub));
    }

    public final void cancelLoadTimeout() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
    }

    public final void maybeBindAndSubscribe(@NotNull List<String> list, @NotNull IControlsSubscriber iControlsSubscriber) {
        invokeOrQueue(new Subscribe(list, iControlsSubscriber));
    }

    public final void maybeBindAndSendAction(@NotNull String str, @NotNull ControlAction controlAction) {
        invokeOrQueue(new Action(str, controlAction));
    }

    public final void startSubscription(@NotNull IControlsSubscription iControlsSubscription, long j) {
        Log.d(this.TAG, Intrinsics.stringPlus("startSubscription: ", iControlsSubscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.request(iControlsSubscription, j);
        }
    }

    public final void cancelSubscription(@NotNull IControlsSubscription iControlsSubscription) {
        Log.d(this.TAG, Intrinsics.stringPlus("cancelSubscription: ", iControlsSubscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.cancel(iControlsSubscription);
        }
    }

    public final void unbindService() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
        bindService(false);
    }

    @NotNull
    public String toString() {
        return "ControlsProviderLifecycleManager(" + Intrinsics.stringPlus("component=", getComponentName()) + Intrinsics.stringPlus(", user=", getUser()) + ")";
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public abstract class ServiceMethod {
        public abstract boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();

        public ServiceMethod() {
        }

        public final void run() {
            if (!callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                ControlsProviderLifecycleManager.this.queueServiceMethod(this);
                ControlsProviderLifecycleManager.this.binderDied();
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Load extends ServiceMethod {
        @NotNull
        public final IControlsSubscriber.Stub subscriber;

        public Load(@NotNull IControlsSubscriber.Stub stub) {
            super();
            this.subscriber = stub;
        }

        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("load ", ControlsProviderLifecycleManager.this.getComponentName()));
            ServiceWrapper access$getWrapper$p = ControlsProviderLifecycleManager.this.wrapper;
            if (access$getWrapper$p == null) {
                return false;
            }
            return access$getWrapper$p.load(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Suggest extends ServiceMethod {
        @NotNull
        public final IControlsSubscriber.Stub subscriber;

        public Suggest(@NotNull IControlsSubscriber.Stub stub) {
            super();
            this.subscriber = stub;
        }

        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("suggest ", ControlsProviderLifecycleManager.this.getComponentName()));
            ServiceWrapper access$getWrapper$p = ControlsProviderLifecycleManager.this.wrapper;
            if (access$getWrapper$p == null) {
                return false;
            }
            return access$getWrapper$p.loadSuggested(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Subscribe extends ServiceMethod {
        @NotNull
        public final List<String> list;
        @NotNull
        public final IControlsSubscriber subscriber;

        public Subscribe(@NotNull List<String> list2, @NotNull IControlsSubscriber iControlsSubscriber) {
            super();
            this.list = list2;
            this.subscriber = iControlsSubscriber;
        }

        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String access$getTAG$p = ControlsProviderLifecycleManager.this.TAG;
            Log.d(access$getTAG$p, "subscribe " + ControlsProviderLifecycleManager.this.getComponentName() + " - " + this.list);
            ServiceWrapper access$getWrapper$p = ControlsProviderLifecycleManager.this.wrapper;
            if (access$getWrapper$p == null) {
                return false;
            }
            return access$getWrapper$p.subscribe(this.list, this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    public final class Action extends ServiceMethod {
        @NotNull
        public final ControlAction action;
        @NotNull
        public final String id;

        public Action(@NotNull String str, @NotNull ControlAction controlAction) {
            super();
            this.id = str;
            this.action = controlAction;
        }

        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String access$getTAG$p = ControlsProviderLifecycleManager.this.TAG;
            Log.d(access$getTAG$p, "onAction " + ControlsProviderLifecycleManager.this.getComponentName() + " - " + this.id);
            ServiceWrapper access$getWrapper$p = ControlsProviderLifecycleManager.this.wrapper;
            if (access$getWrapper$p == null) {
                return false;
            }
            return access$getWrapper$p.action(this.id, this.action, ControlsProviderLifecycleManager.this.actionCallbackService);
        }
    }
}
