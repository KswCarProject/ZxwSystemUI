package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.graphics.PointF;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WiredChargingRippleController.kt */
public final class WiredChargingRippleController {
    @NotNull
    public final BatteryController batteryController;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final Context context;
    public int debounceLevel;
    @Nullable
    public Long lastTriggerTime;
    public float normalizedPortPosX;
    public float normalizedPortPosY;
    @Nullable
    public Boolean pluggedIn;
    public final boolean rippleEnabled;
    @NotNull
    public ChargingRippleView rippleView;
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final WindowManager.LayoutParams windowLayoutParams;
    @NotNull
    public final WindowManager windowManager;

    @VisibleForTesting
    public static /* synthetic */ void getRippleView$annotations() {
    }

    public WiredChargingRippleController(@NotNull CommandRegistry commandRegistry, @NotNull BatteryController batteryController2, @NotNull ConfigurationController configurationController2, @NotNull FeatureFlags featureFlags, @NotNull Context context2, @NotNull WindowManager windowManager2, @NotNull SystemClock systemClock2, @NotNull UiEventLogger uiEventLogger2) {
        this.batteryController = batteryController2;
        this.configurationController = configurationController2;
        this.context = context2;
        this.windowManager = windowManager2;
        this.systemClock = systemClock2;
        this.uiEventLogger = uiEventLogger2;
        this.rippleEnabled = featureFlags.isEnabled(Flags.CHARGING_RIPPLE) && !SystemProperties.getBoolean("persist.debug.suppress-charging-ripple", false);
        this.normalizedPortPosX = context2.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_x);
        this.normalizedPortPosY = context2.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_y);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.format = -3;
        layoutParams.type = 2006;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Wired Charging Animation");
        layoutParams.flags = 24;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
        this.rippleView = new ChargingRippleView(context2, (AttributeSet) null);
        this.pluggedIn = Boolean.valueOf(batteryController2.isPluggedIn());
        commandRegistry.registerCommand("charging-ripple", new Function0<Command>(this) {
            public final /* synthetic */ WiredChargingRippleController this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return new ChargingRippleCommand();
            }
        });
        updateRippleColor();
    }

    @NotNull
    public final ChargingRippleView getRippleView() {
        return this.rippleView;
    }

    public final void registerCallbacks() {
        this.batteryController.addCallback(new WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1(this));
        this.configurationController.addCallback(new WiredChargingRippleController$registerCallbacks$configurationChangedListener$1(this));
    }

    public final void startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        Long l = this.lastTriggerTime;
        if (l != null) {
            Intrinsics.checkNotNull(l);
            if (((double) (elapsedRealtime - l.longValue())) <= ((double) 2000) * Math.pow(2.0d, (double) this.debounceLevel)) {
                this.debounceLevel = Math.min(3, this.debounceLevel + 1);
                this.lastTriggerTime = Long.valueOf(elapsedRealtime);
            }
        }
        startRipple();
        this.debounceLevel = 0;
        this.lastTriggerTime = Long.valueOf(elapsedRealtime);
    }

    public final void startRipple() {
        if (!this.rippleView.getRippleInProgress() && this.rippleView.getParent() == null) {
            this.windowLayoutParams.packageName = this.context.getOpPackageName();
            this.rippleView.addOnAttachStateChangeListener(new WiredChargingRippleController$startRipple$1(this));
            this.windowManager.addView(this.rippleView, this.windowLayoutParams);
            this.uiEventLogger.log(WiredChargingRippleEvent.CHARGING_RIPPLE_PLAYED);
        }
    }

    public final void layoutRipple() {
        PointF pointF;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.context.getDisplay().getRealMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        this.rippleView.setRadius((float) Integer.max(i, i2));
        ChargingRippleView chargingRippleView = this.rippleView;
        int exactRotation = RotationUtils.getExactRotation(this.context);
        if (exactRotation == 1) {
            pointF = new PointF(((float) i) * this.normalizedPortPosY, ((float) i2) * (((float) 1) - this.normalizedPortPosX));
        } else if (exactRotation == 2) {
            float f = (float) 1;
            pointF = new PointF(((float) i) * (f - this.normalizedPortPosX), ((float) i2) * (f - this.normalizedPortPosY));
        } else if (exactRotation != 3) {
            pointF = new PointF(((float) i) * this.normalizedPortPosX, ((float) i2) * this.normalizedPortPosY);
        } else {
            pointF = new PointF(((float) i) * (((float) 1) - this.normalizedPortPosY), ((float) i2) * this.normalizedPortPosX);
        }
        chargingRippleView.setOrigin(pointF);
    }

    public final void updateRippleColor() {
        this.rippleView.setColor(Utils.getColorAttr(this.context, 16843829).getDefaultColor());
    }

    /* compiled from: WiredChargingRippleController.kt */
    public final class ChargingRippleCommand implements Command {
        public ChargingRippleCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            WiredChargingRippleController.this.startRipple();
        }
    }

    /* compiled from: WiredChargingRippleController.kt */
    public enum WiredChargingRippleEvent implements UiEventLogger.UiEventEnum {
        CHARGING_RIPPLE_PLAYED(829);
        
        private final int _id;

        /* access modifiers changed from: public */
        WiredChargingRippleEvent(int i) {
            this._id = i;
        }

        public int getId() {
            return this._id;
        }
    }
}
