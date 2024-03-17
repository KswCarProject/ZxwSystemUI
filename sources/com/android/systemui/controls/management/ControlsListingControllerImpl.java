package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.applications.ServiceListing;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsListingControllerImpl.kt */
public final class ControlsListingControllerImpl implements ControlsListingController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public Set<ComponentName> availableComponents;
    @NotNull
    public List<? extends ServiceInfo> availableServices;
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final Set<ControlsListingController.ControlsListingCallback> callbacks;
    @NotNull
    public final Context context;
    public int currentUserId;
    @NotNull
    public ServiceListing serviceListing;
    @NotNull
    public final Function1<Context, ServiceListing> serviceListingBuilder;
    @NotNull
    public final ServiceListing.Callback serviceListingCallback;
    @NotNull
    public AtomicInteger userChangeInProgress;

    @VisibleForTesting
    public ControlsListingControllerImpl(@NotNull Context context2, @NotNull Executor executor, @NotNull Function1<? super Context, ? extends ServiceListing> function1, @NotNull UserTracker userTracker) {
        this.context = context2;
        this.backgroundExecutor = executor;
        this.serviceListingBuilder = function1;
        this.serviceListing = (ServiceListing) function1.invoke(context2);
        this.callbacks = new LinkedHashSet();
        this.availableComponents = SetsKt__SetsKt.emptySet();
        this.availableServices = CollectionsKt__CollectionsKt.emptyList();
        this.userChangeInProgress = new AtomicInteger(0);
        this.currentUserId = userTracker.getUserId();
        ControlsListingControllerImpl$serviceListingCallback$1 controlsListingControllerImpl$serviceListingCallback$1 = new ControlsListingControllerImpl$serviceListingCallback$1(this);
        this.serviceListingCallback = controlsListingControllerImpl$serviceListingCallback$1;
        Log.d("ControlsListingControllerImpl", "Initializing");
        this.serviceListing.addCallback(controlsListingControllerImpl$serviceListingCallback$1);
        this.serviceListing.setListening(true);
        this.serviceListing.reload();
    }

    public ControlsListingControllerImpl(@NotNull Context context2, @NotNull Executor executor, @NotNull UserTracker userTracker) {
        this(context2, executor, AnonymousClass1.INSTANCE, userTracker);
    }

    /* compiled from: ControlsListingControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public int getCurrentUserId() {
        return this.currentUserId;
    }

    public void changeUser(@NotNull UserHandle userHandle) {
        this.userChangeInProgress.incrementAndGet();
        this.serviceListing.setListening(false);
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$changeUser$1(this, userHandle));
    }

    public void addCallback(@NotNull ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$addCallback$1(this, controlsListingCallback));
    }

    public void removeCallback(@NotNull ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.backgroundExecutor.execute(new ControlsListingControllerImpl$removeCallback$1(this, controlsListingCallback));
    }

    @NotNull
    public List<ControlsServiceInfo> getCurrentServices() {
        Iterable<ServiceInfo> iterable = this.availableServices;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (ServiceInfo controlsServiceInfo : iterable) {
            arrayList.add(new ControlsServiceInfo(this.context, controlsServiceInfo));
        }
        return arrayList;
    }

    @Nullable
    public CharSequence getAppLabel(@NotNull ComponentName componentName) {
        Object obj;
        Iterator it = getCurrentServices().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual((Object) ((ControlsServiceInfo) obj).componentName, (Object) componentName)) {
                break;
            }
        }
        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) obj;
        if (controlsServiceInfo == null) {
            return null;
        }
        return controlsServiceInfo.loadLabel();
    }
}
