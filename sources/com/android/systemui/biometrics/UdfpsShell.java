package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayController;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsShell.kt */
public final class UdfpsShell implements Command {
    @Nullable
    public IUdfpsOverlayController udfpsOverlayController;

    public UdfpsShell(@NotNull CommandRegistry commandRegistry) {
        commandRegistry.registerCommand("udfps", new Function0<Command>(this) {
            public final /* synthetic */ UdfpsShell this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return this.this$0;
            }
        });
    }

    public final void setUdfpsOverlayController(@Nullable IUdfpsOverlayController iUdfpsOverlayController) {
        this.udfpsOverlayController = iUdfpsOverlayController;
    }

    public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
        if (list.size() == 1 && Intrinsics.areEqual((Object) list.get(0), (Object) "hide")) {
            hideOverlay();
        } else if (list.size() != 2 || !Intrinsics.areEqual((Object) list.get(0), (Object) "show")) {
            invalidCommand(printWriter);
        } else {
            showOverlay(getEnrollmentReason(list.get(1)));
        }
    }

    public void help(@NotNull PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar udfps <cmd>");
        printWriter.println("Supported commands:");
        printWriter.println("  - show <reason>");
        printWriter.println("    -> supported reasons: [enroll-find-sensor, enroll-enrolling, auth-bp, auth-keyguard, auth-other, auth-settings]");
        printWriter.println("    -> reason otherwise defaults to unknown");
        printWriter.println("  - hide");
    }

    public final void invalidCommand(PrintWriter printWriter) {
        printWriter.println("invalid command");
        help(printWriter);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004a A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int getEnrollmentReason(java.lang.String r1) {
        /*
            r0 = this;
            int r0 = r1.hashCode()
            switch(r0) {
                case -945543637: goto L_0x003f;
                case -943067225: goto L_0x0034;
                case -646572397: goto L_0x0029;
                case -19448152: goto L_0x001e;
                case 244570389: goto L_0x0013;
                case 902271659: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x004a
        L_0x0008:
            java.lang.String r0 = "auth-other"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0011
            goto L_0x004a
        L_0x0011:
            r0 = 5
            goto L_0x004b
        L_0x0013:
            java.lang.String r0 = "enroll-enrolling"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x001c
            goto L_0x004a
        L_0x001c:
            r0 = 2
            goto L_0x004b
        L_0x001e:
            java.lang.String r0 = "auth-settings"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0027
            goto L_0x004a
        L_0x0027:
            r0 = 6
            goto L_0x004b
        L_0x0029:
            java.lang.String r0 = "auth-bp"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0032
            goto L_0x004a
        L_0x0032:
            r0 = 3
            goto L_0x004b
        L_0x0034:
            java.lang.String r0 = "enroll-find-sensor"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x003d
            goto L_0x004a
        L_0x003d:
            r0 = 1
            goto L_0x004b
        L_0x003f:
            java.lang.String r0 = "auth-keyguard"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0048
            goto L_0x004a
        L_0x0048:
            r0 = 4
            goto L_0x004b
        L_0x004a:
            r0 = 0
        L_0x004b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsShell.getEnrollmentReason(java.lang.String):int");
    }

    public final void showOverlay(int i) {
        IUdfpsOverlayController iUdfpsOverlayController = this.udfpsOverlayController;
        if (iUdfpsOverlayController != null) {
            iUdfpsOverlayController.showUdfpsOverlay(2, 0, i, new UdfpsShell$showOverlay$1());
        }
    }

    public final void hideOverlay() {
        IUdfpsOverlayController iUdfpsOverlayController = this.udfpsOverlayController;
        if (iUdfpsOverlayController != null) {
            iUdfpsOverlayController.hideUdfpsOverlay(0);
        }
    }
}
