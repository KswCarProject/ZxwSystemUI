package com.android.systemui.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import org.jetbrains.annotations.NotNull;

/* compiled from: DemoModeAvailabilityTracker.kt */
public abstract class DemoModeAvailabilityTracker {
    @NotNull
    public final DemoModeAvailabilityTracker$allowedObserver$1 allowedObserver = new DemoModeAvailabilityTracker$allowedObserver$1(this, new Handler(Looper.getMainLooper()));
    @NotNull
    public final Context context;
    public boolean isDemoModeAvailable = checkIsDemoModeAllowed();
    public boolean isInDemoMode = checkIsDemoModeOn();
    @NotNull
    public final DemoModeAvailabilityTracker$onObserver$1 onObserver = new DemoModeAvailabilityTracker$onObserver$1(this, new Handler(Looper.getMainLooper()));

    public abstract void onDemoModeAvailabilityChanged();

    public abstract void onDemoModeFinished();

    public abstract void onDemoModeStarted();

    public DemoModeAvailabilityTracker(@NotNull Context context2) {
        this.context = context2;
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final void setInDemoMode(boolean z) {
        this.isInDemoMode = z;
    }

    public final boolean isDemoModeAvailable() {
        return this.isDemoModeAvailable;
    }

    public final void setDemoModeAvailable(boolean z) {
        this.isDemoModeAvailable = z;
    }

    public final void startTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_demo_allowed"), false, this.allowedObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_tuner_demo_on"), false, this.onObserver);
    }

    public final void stopTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.unregisterContentObserver(this.allowedObserver);
        contentResolver.unregisterContentObserver(this.onObserver);
    }

    public final boolean checkIsDemoModeAllowed() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_demo_allowed", 0) != 0;
    }

    public final boolean checkIsDemoModeOn() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0;
    }
}
