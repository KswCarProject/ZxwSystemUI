package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@VisibleForTesting
/* compiled from: ControlsBindingControllerImpl.kt */
public class ControlsBindingControllerImpl implements ControlsBindingController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final ControlsBindingControllerImpl$Companion$emptyCallback$1 emptyCallback = new ControlsBindingControllerImpl$Companion$emptyCallback$1();
    @NotNull
    public final ControlsBindingControllerImpl$actionCallbackService$1 actionCallbackService = new ControlsBindingControllerImpl$actionCallbackService$1(this);
    @NotNull
    public final DelayableExecutor backgroundExecutor;
    @NotNull
    public final Context context;
    @Nullable
    public ControlsProviderLifecycleManager currentProvider;
    @NotNull
    public UserHandle currentUser;
    @NotNull
    public final Lazy<ControlsController> lazyController;
    @Nullable
    public LoadSubscriber loadSubscriber;
    @Nullable
    public StatefulControlSubscriber statefulControlSubscriber;

    public ControlsBindingControllerImpl(@NotNull Context context2, @NotNull DelayableExecutor delayableExecutor, @NotNull Lazy<ControlsController> lazy, @NotNull UserTracker userTracker) {
        this.context = context2;
        this.backgroundExecutor = delayableExecutor;
        this.lazyController = lazy;
        this.currentUser = userTracker.getUserHandle();
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    @VisibleForTesting
    public ControlsProviderLifecycleManager createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull ComponentName componentName) {
        return new ControlsProviderLifecycleManager(this.context, this.backgroundExecutor, this.actionCallbackService, this.currentUser, componentName);
    }

    public final ControlsProviderLifecycleManager retrieveLifecycleManager(ComponentName componentName) {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            if (!Intrinsics.areEqual((Object) controlsProviderLifecycleManager == null ? null : controlsProviderLifecycleManager.getComponentName(), (Object) componentName)) {
                unbind();
            }
        }
        ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.currentProvider;
        if (controlsProviderLifecycleManager2 == null) {
            controlsProviderLifecycleManager2 = createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName);
        }
        this.currentProvider = controlsProviderLifecycleManager2;
        return controlsProviderLifecycleManager2;
    }

    @NotNull
    public Runnable bindAndLoad(@NotNull ComponentName componentName, @NotNull ControlsBindingController.LoadCallback loadCallback) {
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        LoadSubscriber loadSubscriber3 = new LoadSubscriber(loadCallback, 100000);
        this.loadSubscriber = loadSubscriber3;
        retrieveLifecycleManager(componentName).maybeBindAndLoad(loadSubscriber3);
        return loadSubscriber3.loadCancel();
    }

    public void bindAndLoadSuggested(@NotNull ComponentName componentName, @NotNull ControlsBindingController.LoadCallback loadCallback) {
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        LoadSubscriber loadSubscriber3 = new LoadSubscriber(loadCallback, 36);
        this.loadSubscriber = loadSubscriber3;
        retrieveLifecycleManager(componentName).maybeBindAndLoadSuggested(loadSubscriber3);
    }

    public void subscribe(@NotNull StructureInfo structureInfo) {
        unsubscribe();
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(structureInfo.getComponentName());
        StatefulControlSubscriber statefulControlSubscriber2 = new StatefulControlSubscriber(this.lazyController.get(), retrieveLifecycleManager, this.backgroundExecutor, 100000);
        this.statefulControlSubscriber = statefulControlSubscriber2;
        Iterable<ControlInfo> controls = structureInfo.getControls();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10));
        for (ControlInfo controlId : controls) {
            arrayList.add(controlId.getControlId());
        }
        retrieveLifecycleManager.maybeBindAndSubscribe(arrayList, statefulControlSubscriber2);
    }

    public void unsubscribe() {
        StatefulControlSubscriber statefulControlSubscriber2 = this.statefulControlSubscriber;
        if (statefulControlSubscriber2 != null) {
            statefulControlSubscriber2.cancel();
        }
        this.statefulControlSubscriber = null;
    }

    public void action(@NotNull ComponentName componentName, @NotNull ControlInfo controlInfo, @NotNull ControlAction controlAction) {
        if (this.statefulControlSubscriber == null) {
            Log.w("ControlsBindingControllerImpl", "No actions can occur outside of an active subscription. Ignoring.");
        } else {
            retrieveLifecycleManager(componentName).maybeBindAndSendAction(controlInfo.getControlId(), controlAction);
        }
    }

    public void changeUser(@NotNull UserHandle userHandle) {
        if (!Intrinsics.areEqual((Object) userHandle, (Object) this.currentUser)) {
            unbind();
            this.currentUser = userHandle;
        }
    }

    public final void unbind() {
        unsubscribe();
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        this.loadSubscriber = null;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            controlsProviderLifecycleManager.unbindService();
        }
        this.currentProvider = null;
    }

    public void onComponentRemoved(@NotNull ComponentName componentName) {
        this.backgroundExecutor.execute(new ControlsBindingControllerImpl$onComponentRemoved$1(this, componentName));
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder("  ControlsBindingController:\n");
        sb.append("    currentUser=" + this.currentUser + 10);
        sb.append(Intrinsics.stringPlus("    StatefulControlSubscriber=", this.statefulControlSubscriber));
        sb.append("    Providers=" + this.currentProvider + 10);
        return sb.toString();
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public abstract class CallbackRunnable implements Runnable {
        @Nullable
        public final ControlsProviderLifecycleManager provider;
        @NotNull
        public final IBinder token;

        public abstract void doRun();

        public CallbackRunnable(@NotNull IBinder iBinder) {
            this.token = iBinder;
            this.provider = ControlsBindingControllerImpl.this.currentProvider;
        }

        @Nullable
        public final ControlsProviderLifecycleManager getProvider() {
            return this.provider;
        }

        public void run() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager == null) {
                Log.e("ControlsBindingControllerImpl", "No current provider set");
            } else if (!Intrinsics.areEqual((Object) controlsProviderLifecycleManager.getUser(), (Object) ControlsBindingControllerImpl.this.currentUser)) {
                Log.e("ControlsBindingControllerImpl", "User " + this.provider.getUser() + " is not current user");
            } else if (!Intrinsics.areEqual((Object) this.token, (Object) this.provider.getToken())) {
                Log.e("ControlsBindingControllerImpl", "Provider for token:" + this.token + " does not exist anymore");
            } else {
                doRun();
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class OnLoadRunnable extends CallbackRunnable {
        @NotNull
        public final ControlsBindingController.LoadCallback callback;
        @NotNull
        public final List<Control> list;

        public OnLoadRunnable(@NotNull IBinder iBinder, @NotNull List<Control> list2, @NotNull ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.list = list2;
            this.callback = loadCallback;
        }

        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Complete and loading controls");
            this.callback.accept(this.list);
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class OnCancelAndLoadRunnable extends CallbackRunnable {
        @NotNull
        public final ControlsBindingController.LoadCallback callback;
        @NotNull
        public final List<Control> list;
        @NotNull
        public final IControlsSubscription subscription;

        public OnCancelAndLoadRunnable(@NotNull IBinder iBinder, @NotNull List<Control> list2, @NotNull IControlsSubscription iControlsSubscription, @NotNull ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.list = list2;
            this.subscription = iControlsSubscription;
            this.callback = loadCallback;
        }

        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Canceling and loading controls");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.cancelSubscription(this.subscription);
            }
            this.callback.accept(this.list);
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class OnSubscribeRunnable extends CallbackRunnable {
        public final long requestLimit;
        @NotNull
        public final IControlsSubscription subscription;

        public OnSubscribeRunnable(@NotNull IBinder iBinder, @NotNull IControlsSubscription iControlsSubscription, long j) {
            super(iBinder);
            this.subscription = iControlsSubscription;
            this.requestLimit = j;
        }

        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Starting subscription");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.startSubscription(this.subscription, this.requestLimit);
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class OnActionResponseRunnable extends CallbackRunnable {
        @NotNull
        public final String controlId;
        public final int response;

        public OnActionResponseRunnable(@NotNull IBinder iBinder, @NotNull String str, int i) {
            super(iBinder);
            this.controlId = str;
            this.response = i;
        }

        @NotNull
        public final String getControlId() {
            return this.controlId;
        }

        public final int getResponse() {
            return this.response;
        }

        public void doRun() {
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                ((ControlsController) ControlsBindingControllerImpl.this.lazyController.get()).onActionResponse(provider.getComponentName(), getControlId(), getResponse());
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class OnLoadErrorRunnable extends CallbackRunnable {
        @NotNull
        public final ControlsBindingController.LoadCallback callback;
        @NotNull
        public final String error;

        public OnLoadErrorRunnable(@NotNull IBinder iBinder, @NotNull String str, @NotNull ControlsBindingController.LoadCallback loadCallback) {
            super(iBinder);
            this.error = str;
            this.callback = loadCallback;
        }

        @NotNull
        public final String getError() {
            return this.error;
        }

        public void doRun() {
            this.callback.error(this.error);
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                Log.e("ControlsBindingControllerImpl", "onError receive from '" + provider.getComponentName() + "': " + getError());
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    public final class LoadSubscriber extends IControlsSubscriber.Stub {
        @Nullable
        public Function0<Unit> _loadCancelInternal;
        @NotNull
        public ControlsBindingController.LoadCallback callback;
        @NotNull
        public AtomicBoolean isTerminated = new AtomicBoolean(false);
        @NotNull
        public final ArrayList<Control> loadedControls = new ArrayList<>();
        public final long requestLimit;
        public IControlsSubscription subscription;

        public LoadSubscriber(@NotNull ControlsBindingController.LoadCallback loadCallback, long j) {
            this.callback = loadCallback;
            this.requestLimit = j;
        }

        @NotNull
        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public final long getRequestLimit() {
            return this.requestLimit;
        }

        @NotNull
        public final ArrayList<Control> getLoadedControls() {
            return this.loadedControls;
        }

        @NotNull
        public final Runnable loadCancel() {
            return new ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1(this);
        }

        public void onSubscribe(@NotNull IBinder iBinder, @NotNull IControlsSubscription iControlsSubscription) {
            this.subscription = iControlsSubscription;
            this._loadCancelInternal = new ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(ControlsBindingControllerImpl.this, this);
            ControlsBindingControllerImpl.this.backgroundExecutor.execute(new OnSubscribeRunnable(iBinder, iControlsSubscription, this.requestLimit));
        }

        public void onNext(@NotNull IBinder iBinder, @NotNull Control control) {
            ControlsBindingControllerImpl.this.backgroundExecutor.execute(new ControlsBindingControllerImpl$LoadSubscriber$onNext$1(this, control, ControlsBindingControllerImpl.this, iBinder));
        }

        public void onError(@NotNull IBinder iBinder, @NotNull String str) {
            maybeTerminateAndRun(new OnLoadErrorRunnable(iBinder, str, this.callback));
        }

        public void onComplete(@NotNull IBinder iBinder) {
            maybeTerminateAndRun(new OnLoadRunnable(iBinder, this.loadedControls, this.callback));
        }

        public final void maybeTerminateAndRun(Runnable runnable) {
            if (!this.isTerminated.get()) {
                this._loadCancelInternal = ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$1.INSTANCE;
                this.callback = ControlsBindingControllerImpl.emptyCallback;
                ControlsProviderLifecycleManager access$getCurrentProvider$p = ControlsBindingControllerImpl.this.currentProvider;
                if (access$getCurrentProvider$p != null) {
                    access$getCurrentProvider$p.cancelLoadTimeout();
                }
                ControlsBindingControllerImpl.this.backgroundExecutor.execute(new ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2(this, runnable));
            }
        }
    }
}
