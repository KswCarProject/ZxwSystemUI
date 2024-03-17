package com.android.systemui.statusbar.policy;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceProvisionedControllerImpl.kt */
public class DeviceProvisionedControllerImpl implements DeviceProvisionedController, DeviceProvisionedController.DeviceProvisionedListener, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final HandlerExecutor backgroundExecutor;
    @NotNull
    public final Handler backgroundHandler;
    @NotNull
    public final AtomicBoolean deviceProvisioned = new AtomicBoolean(false);
    public final Uri deviceProvisionedUri;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final GlobalSettings globalSettings;
    @NotNull
    public final AtomicBoolean initted;
    @NotNull
    public final ArraySet<DeviceProvisionedController.DeviceProvisionedListener> listeners;
    @NotNull
    public final Object lock;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final DeviceProvisionedControllerImpl$observer$1 observer;
    @NotNull
    public final SecureSettings secureSettings;
    @NotNull
    public final DeviceProvisionedControllerImpl$userChangedCallback$1 userChangedCallback;
    @NotNull
    public final SparseBooleanArray userSetupComplete;
    public final Uri userSetupUri;
    @NotNull
    public final UserTracker userTracker;

    public DeviceProvisionedControllerImpl(@NotNull SecureSettings secureSettings2, @NotNull GlobalSettings globalSettings2, @NotNull UserTracker userTracker2, @NotNull DumpManager dumpManager2, @NotNull Handler handler, @NotNull Executor executor) {
        this.secureSettings = secureSettings2;
        this.globalSettings = globalSettings2;
        this.userTracker = userTracker2;
        this.dumpManager = dumpManager2;
        this.backgroundHandler = handler;
        this.mainExecutor = executor;
        this.deviceProvisionedUri = globalSettings2.getUriFor("device_provisioned");
        this.userSetupUri = secureSettings2.getUriFor("user_setup_complete");
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        this.userSetupComplete = sparseBooleanArray;
        this.listeners = new ArraySet<>();
        this.lock = new Object();
        this.backgroundExecutor = new HandlerExecutor(handler);
        this.initted = new AtomicBoolean(false);
        this.observer = new DeviceProvisionedControllerImpl$observer$1(this, handler);
        this.userChangedCallback = new DeviceProvisionedControllerImpl$userChangedCallback$1(this);
        sparseBooleanArray.put(getCurrentUser(), false);
    }

    /* compiled from: DeviceProvisionedControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final int get_currentUser() {
        return this.userTracker.getUserId();
    }

    public int getCurrentUser() {
        return get_currentUser();
    }

    public void init() {
        if (this.initted.compareAndSet(false, true)) {
            this.dumpManager.registerDumpable(this);
            updateValues$default(this, false, 0, 3, (Object) null);
            this.userTracker.addCallback(this.userChangedCallback, this.backgroundExecutor);
            this.globalSettings.registerContentObserver(this.deviceProvisionedUri, (ContentObserver) this.observer);
            this.secureSettings.registerContentObserverForUser(this.userSetupUri, (ContentObserver) this.observer, -1);
        }
    }

    public static /* synthetic */ void updateValues$default(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl, boolean z, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 1) != 0) {
                z = true;
            }
            if ((i2 & 2) != 0) {
                i = -1;
            }
            deviceProvisionedControllerImpl.updateValues(z, i);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: updateValues");
    }

    public final void updateValues(boolean z, int i) {
        boolean z2 = true;
        if (z) {
            this.deviceProvisioned.set(this.globalSettings.getInt("device_provisioned", 0) != 0);
        }
        synchronized (this.lock) {
            if (i == -1) {
                try {
                    int size = this.userSetupComplete.size();
                    int i2 = 0;
                    while (i2 < size) {
                        int i3 = i2 + 1;
                        int keyAt = this.userSetupComplete.keyAt(i2);
                        this.userSetupComplete.put(keyAt, this.secureSettings.getIntForUser("user_setup_complete", 0, keyAt) != 0);
                        i2 = i3;
                    }
                } finally {
                }
            } else if (i != -2) {
                if (this.secureSettings.getIntForUser("user_setup_complete", 0, i) == 0) {
                    z2 = false;
                }
                this.userSetupComplete.put(i, z2);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void addCallback(@NotNull DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        synchronized (this.lock) {
            this.listeners.add(deviceProvisionedListener);
        }
    }

    public void removeCallback(@NotNull DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        synchronized (this.lock) {
            this.listeners.remove(deviceProvisionedListener);
        }
    }

    public boolean isDeviceProvisioned() {
        return this.deviceProvisioned.get();
    }

    public boolean isUserSetup(int i) {
        int indexOfKey;
        synchronized (this.lock) {
            indexOfKey = this.userSetupComplete.indexOfKey(i);
        }
        boolean z = false;
        if (indexOfKey < 0) {
            if (this.secureSettings.getIntForUser("user_setup_complete", 0, i) != 0) {
                z = true;
            }
            synchronized (this.lock) {
                this.userSetupComplete.put(i, z);
                Unit unit = Unit.INSTANCE;
            }
        } else {
            synchronized (this.lock) {
                z = this.userSetupComplete.get(i, false);
            }
        }
        return z;
    }

    public boolean isCurrentUserSetup() {
        return isUserSetup(getCurrentUser());
    }

    public void onDeviceProvisionedChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1.INSTANCE);
    }

    public void onUserSetupChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSetupChanged$1.INSTANCE);
    }

    public void onUserSwitched() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSwitched$1.INSTANCE);
    }

    public final void dispatchChange(@NotNull Function1<? super DeviceProvisionedController.DeviceProvisionedListener, Unit> function1) {
        ArrayList arrayList;
        synchronized (this.lock) {
            arrayList = new ArrayList(this.listeners);
        }
        this.mainExecutor.execute(new DeviceProvisionedControllerImpl$dispatchChange$1(arrayList, function1));
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Device provisioned: ", Boolean.valueOf(this.deviceProvisioned.get())));
        synchronized (this.lock) {
            printWriter.println(Intrinsics.stringPlus("User setup complete: ", this.userSetupComplete));
            printWriter.println(Intrinsics.stringPlus("Listeners: ", this.listeners));
            Unit unit = Unit.INSTANCE;
        }
    }
}
