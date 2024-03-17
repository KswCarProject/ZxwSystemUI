package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.TriggerEventListener;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.CoreStartable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController extends CoreStartable {
    @NotNull
    public final AsyncSensorManager asyncSensorManager;
    public boolean bouncerVisible;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    public boolean isListening;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final KeyguardLiftController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new KeyguardLiftController$keyguardUpdateMonitorCallback$1(this);
    @NotNull
    public final TriggerEventListener listener = new KeyguardLiftController$listener$1(this);
    public final Sensor pickupSensor;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final KeyguardLiftController$statusBarStateListener$1 statusBarStateListener = new KeyguardLiftController$statusBarStateListener$1(this);

    public KeyguardLiftController(@NotNull Context context2, @NotNull StatusBarStateController statusBarStateController2, @NotNull AsyncSensorManager asyncSensorManager2, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull DumpManager dumpManager2) {
        super(context2);
        this.context = context2;
        this.statusBarStateController = statusBarStateController2;
        this.asyncSensorManager = asyncSensorManager2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.dumpManager = dumpManager2;
        this.pickupSensor = asyncSensorManager2.getDefaultSensor(25);
    }

    public void start() {
        if (this.context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            init();
        }
    }

    public final void init() {
        this.dumpManager.registerDumpable(KeyguardLiftController.class.getName(), this);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        updateListeningState();
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("KeyguardLiftController:");
        printWriter.println(Intrinsics.stringPlus("  pickupSensor: ", this.pickupSensor));
        printWriter.println(Intrinsics.stringPlus("  isListening: ", Boolean.valueOf(this.isListening)));
        printWriter.println(Intrinsics.stringPlus("  bouncerVisible: ", Boolean.valueOf(this.bouncerVisible)));
    }

    public final void updateListeningState() {
        if (this.pickupSensor != null) {
            boolean z = true;
            boolean z2 = this.keyguardUpdateMonitor.isKeyguardVisible() && !this.statusBarStateController.isDozing();
            boolean isFaceAuthEnabledForUser = this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(KeyguardUpdateMonitor.getCurrentUser());
            if ((!z2 && !this.bouncerVisible) || !isFaceAuthEnabledForUser) {
                z = false;
            }
            if (z != this.isListening) {
                this.isListening = z;
                if (z) {
                    this.asyncSensorManager.requestTriggerSensor(this.listener, this.pickupSensor);
                } else {
                    this.asyncSensorManager.cancelTriggerSensor(this.listener, this.pickupSensor);
                }
            }
        }
    }
}
