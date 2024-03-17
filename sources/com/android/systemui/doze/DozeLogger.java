package com.android.systemui.doze;

import android.view.Display;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger {
    @NotNull
    public final LogBuffer buffer;

    public DozeLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logPickupWakeup(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logPickupWakeup$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logPulseStart(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseStart$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPulseFinish() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseFinish$2.INSTANCE));
    }

    public final void logNotificationPulse() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logNotificationPulse$2.INSTANCE));
    }

    public final void logDozing(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozing$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logDozingChanged(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozingChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logPowerSaveChanged(boolean z, @NotNull DozeMachine.State state) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPowerSaveChanged$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logAlwaysOnSuppressedChange(boolean z, @NotNull DozeMachine.State state) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logAlwaysOnSuppressedChange$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logFling(boolean z, boolean z2, boolean z3, boolean z4) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logFling$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setBool3(z3);
        obtain.setBool4(z4);
        logBuffer.commit(obtain);
    }

    public final void logEmergencyCall() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logEmergencyCall$2.INSTANCE));
    }

    public final void logKeyguardBouncerChanged(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logKeyguardBouncerChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logScreenOn(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logScreenOn$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logScreenOff(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logScreenOff$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logMissedTick(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.ERROR, DozeLogger$logMissedTick$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTimeTickScheduled(long j, long j2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logTimeTickScheduled$2.INSTANCE);
        obtain.setLong1(j);
        obtain.setLong2(j2);
        logBuffer.commit(obtain);
    }

    public final void logKeyguardVisibilityChange(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logKeyguardVisibilityChange$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logDozeStateChanged(@NotNull DozeMachine.State state) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozeStateChanged$2.INSTANCE);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logStateChangedSent(@NotNull DozeMachine.State state) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logStateChangedSent$2.INSTANCE);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logDisplayStateDelayedByUdfps(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDisplayStateDelayedByUdfps$2.INSTANCE);
        obtain.setStr1(Display.stateToString(i));
        logBuffer.commit(obtain);
    }

    public final void logDisplayStateChanged(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDisplayStateChanged$2.INSTANCE);
        obtain.setStr1(Display.stateToString(i));
        logBuffer.commit(obtain);
    }

    public final void logWakeDisplay(boolean z, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logWakeDisplay$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logProximityResult(boolean z, long j, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logProximityResult$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setLong1(j);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPostureChanged(int i, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPostureChanged$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseDropped(boolean z, @NotNull DozeMachine.State state, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseDropped$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }

    public final void logSensorEventDropped(int i, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logSensorEventDropped$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseDropped(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseDropped$4.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseTouchDisabledByProx(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logPulseTouchDisabledByProx$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logSensorTriggered(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logSensorTriggered$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logAlwaysOnSuppressed(@NotNull DozeMachine.State state, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logAlwaysOnSuppressed$2.INSTANCE);
        obtain.setStr1(state.name());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logImmediatelyEndDoze(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logImmediatelyEndDoze$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDozeScreenBrightness(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozeScreenBrightness$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logSetAodDimmingScrim(long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logSetAodDimmingScrim$2.INSTANCE);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }
}
