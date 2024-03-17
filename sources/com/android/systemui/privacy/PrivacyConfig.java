package com.android.systemui.privacy;

import android.provider.DeviceConfig;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyConfig.kt */
public final class PrivacyConfig implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final List<WeakReference<Callback>> callbacks = new ArrayList();
    @NotNull
    public final DeviceConfigProxy deviceConfigProxy;
    @NotNull
    public final DeviceConfig.OnPropertiesChangedListener devicePropertiesChangedListener;
    public boolean locationAvailable = isLocationEnabled();
    public boolean mediaProjectionAvailable = isMediaProjectionEnabled();
    public boolean micCameraAvailable = isMicCameraEnabled();
    @NotNull
    public final DelayableExecutor uiExecutor;

    /* compiled from: PrivacyConfig.kt */
    public interface Callback {
        void onFlagLocationChanged(boolean z) {
        }

        void onFlagMediaProjectionChanged(boolean z) {
        }

        void onFlagMicCameraChanged(boolean z) {
        }
    }

    public final boolean isMicCameraEnabled() {
        return false;
    }

    public PrivacyConfig(@NotNull DelayableExecutor delayableExecutor, @NotNull DeviceConfigProxy deviceConfigProxy2, @NotNull DumpManager dumpManager) {
        this.uiExecutor = delayableExecutor;
        this.deviceConfigProxy = deviceConfigProxy2;
        PrivacyConfig$devicePropertiesChangedListener$1 privacyConfig$devicePropertiesChangedListener$1 = new PrivacyConfig$devicePropertiesChangedListener$1(this);
        this.devicePropertiesChangedListener = privacyConfig$devicePropertiesChangedListener$1;
        dumpManager.registerDumpable("PrivacyConfig", this);
        deviceConfigProxy2.addOnPropertiesChangedListener("privacy", delayableExecutor, privacyConfig$devicePropertiesChangedListener$1);
    }

    @VisibleForTesting
    /* compiled from: PrivacyConfig.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean getMicCameraAvailable() {
        return this.micCameraAvailable;
    }

    public final boolean getLocationAvailable() {
        return this.locationAvailable;
    }

    public final boolean getMediaProjectionAvailable() {
        return this.mediaProjectionAvailable;
    }

    public final boolean isLocationEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "location_indicators_enabled", false);
    }

    public final boolean isMediaProjectionEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "media_projection_indicators_enabled", true);
    }

    public final void addCallback(@NotNull Callback callback) {
        addCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    public final void addCallback(WeakReference<Callback> weakReference) {
        this.uiExecutor.execute(new PrivacyConfig$addCallback$1(this, weakReference));
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("PrivacyConfig state:");
        asIndenting.increaseIndent();
        try {
            asIndenting.println(Intrinsics.stringPlus("micCameraAvailable: ", Boolean.valueOf(getMicCameraAvailable())));
            asIndenting.println(Intrinsics.stringPlus("locationAvailable: ", Boolean.valueOf(getLocationAvailable())));
            asIndenting.println(Intrinsics.stringPlus("mediaProjectionAvailable: ", Boolean.valueOf(getMediaProjectionAvailable())));
            asIndenting.println("Callbacks:");
            asIndenting.increaseIndent();
            for (WeakReference weakReference : this.callbacks) {
                Callback callback = (Callback) weakReference.get();
                if (callback != null) {
                    asIndenting.println(callback);
                }
            }
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            throw th;
        } finally {
            asIndenting.decreaseIndent();
        }
    }
}
