package com.android.systemui.biometrics;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsHapticsSimulator.kt */
public final class UdfpsHapticsSimulator implements Command {
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    public final AudioAttributes sonificationEffects = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    @Nullable
    public UdfpsController udfpsController;
    @NotNull
    public final VibratorHelper vibrator;

    public UdfpsHapticsSimulator(@NotNull CommandRegistry commandRegistry, @NotNull VibratorHelper vibratorHelper, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2) {
        this.vibrator = vibratorHelper;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        commandRegistry.registerCommand("udfps-haptic", new Function0<Command>(this) {
            public final /* synthetic */ UdfpsHapticsSimulator this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return this.this$0;
            }
        });
    }

    public final void setUdfpsController(@Nullable UdfpsController udfpsController2) {
        this.udfpsController = udfpsController2;
    }

    public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
        if (list.isEmpty()) {
            invalidCommand(printWriter);
            return;
        }
        String str = list.get(0);
        int hashCode = str.hashCode();
        if (hashCode != -1867169789) {
            if (hashCode != 96784904) {
                if (hashCode == 109757538 && str.equals("start")) {
                    UdfpsController udfpsController2 = this.udfpsController;
                    if (udfpsController2 != null) {
                        udfpsController2.playStartHaptic();
                        return;
                    }
                    return;
                }
            } else if (str.equals("error")) {
                this.vibrator.vibrate(VibrationEffect.get(1), this.sonificationEffects);
                return;
            }
        } else if (str.equals("success")) {
            this.vibrator.vibrate(VibrationEffect.get(0), this.sonificationEffects);
            return;
        }
        invalidCommand(printWriter);
    }

    public void help(@NotNull PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar udfps-haptic <haptic>");
        printWriter.println("Available commands:");
        printWriter.println("  start");
        printWriter.println("  success, always plays CLICK haptic");
        printWriter.println("  error, always plays DOUBLE_CLICK haptic");
    }

    public final void invalidCommand(@NotNull PrintWriter printWriter) {
        printWriter.println("invalid command");
        help(printWriter);
    }
}
