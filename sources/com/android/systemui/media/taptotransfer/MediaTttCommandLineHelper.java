package com.android.systemui.media.taptotransfer;

import android.app.StatusBarManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import com.android.systemui.media.taptotransfer.receiver.ChipStateReceiver;
import com.android.systemui.media.taptotransfer.sender.ChipStateSender;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttCommandLineHelper.kt */
public final class MediaTttCommandLineHelper {
    @NotNull
    public final Context context;
    @NotNull
    public final Executor mainExecutor;

    public MediaTttCommandLineHelper(@NotNull CommandRegistry commandRegistry, @NotNull Context context2, @NotNull Executor executor) {
        this.context = context2;
        this.mainExecutor = executor;
        commandRegistry.registerCommand("media-ttt-chip-sender", new Function0<Command>(this) {
            public final /* synthetic */ MediaTttCommandLineHelper this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return new SenderCommand();
            }
        });
        commandRegistry.registerCommand("media-ttt-chip-receiver", new Function0<Command>(this) {
            public final /* synthetic */ MediaTttCommandLineHelper this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return new ReceiverCommand();
            }
        });
    }

    /* compiled from: MediaTttCommandLineHelper.kt */
    public final class SenderCommand implements Command {
        public final boolean isSucceededState(int i) {
            return i == 4 || i == 5;
        }

        public SenderCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            boolean z = true;
            String str = list.get(1);
            try {
                Integer valueOf = Integer.valueOf(ChipStateSender.Companion.getSenderStateIdFromName(str));
                Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
                if (systemService != null) {
                    StatusBarManager statusBarManager = (StatusBarManager) systemService;
                    MediaRoute2Info.Builder addFeature = new MediaRoute2Info.Builder("id", list.get(0)).addFeature("feature");
                    if (list.size() >= 3 && Intrinsics.areEqual((Object) list.get(2), (Object) "useAppIcon=false")) {
                        z = false;
                    }
                    if (z) {
                        addFeature.setPackageName(ThemeOverlayApplier.SYSUI_PACKAGE);
                    }
                    statusBarManager.updateMediaTapToTransferSenderDisplay(valueOf.intValue(), addFeature.build(), getUndoExecutor(valueOf.intValue()), getUndoCallback(valueOf.intValue()));
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.app.StatusBarManager");
            } catch (IllegalArgumentException unused) {
                printWriter.println(Intrinsics.stringPlus("Invalid command name ", str));
            }
        }

        public final Executor getUndoExecutor(int i) {
            if (isSucceededState(i)) {
                return MediaTttCommandLineHelper.this.mainExecutor;
            }
            return null;
        }

        public final Runnable getUndoCallback(int i) {
            if (isSucceededState(i)) {
                return new MediaTttCommandLineHelper$SenderCommand$getUndoCallback$1(i);
            }
            return null;
        }
    }

    /* compiled from: MediaTttCommandLineHelper.kt */
    public final class ReceiverCommand implements Command {
        public ReceiverCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            boolean z = false;
            String str = list.get(0);
            try {
                Integer valueOf = Integer.valueOf(ChipStateReceiver.Companion.getReceiverStateIdFromName(str));
                Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
                if (systemService != null) {
                    StatusBarManager statusBarManager = (StatusBarManager) systemService;
                    MediaRoute2Info.Builder addFeature = new MediaRoute2Info.Builder("id", "Test Name").addFeature("feature");
                    if (list.size() < 2 || !Intrinsics.areEqual((Object) list.get(1), (Object) "useAppIcon=false")) {
                        z = true;
                    }
                    if (z) {
                        addFeature.setPackageName(ThemeOverlayApplier.SYSUI_PACKAGE);
                    }
                    statusBarManager.updateMediaTapToTransferReceiverDisplay(valueOf.intValue(), addFeature.build(), (Icon) null, (CharSequence) null);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.app.StatusBarManager");
            } catch (IllegalArgumentException unused) {
                printWriter.println(Intrinsics.stringPlus("Invalid command name ", str));
            }
        }
    }
}
