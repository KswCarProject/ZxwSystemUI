package com.android.systemui.media.muteawait;

import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaMuteAwaitConnectionCli.kt */
public final class MediaMuteAwaitConnectionCli {
    @NotNull
    public final Context context;

    public MediaMuteAwaitConnectionCli(@NotNull CommandRegistry commandRegistry, @NotNull Context context2) {
        this.context = context2;
        commandRegistry.registerCommand("media-mute-await", new Function0<Command>(this) {
            public final /* synthetic */ MediaMuteAwaitConnectionCli this$0;

            {
                this.this$0 = r1;
            }

            @NotNull
            public final Command invoke() {
                return new MuteAwaitCommand();
            }
        });
    }

    /* compiled from: MediaMuteAwaitConnectionCli.kt */
    public final class MuteAwaitCommand implements Command {
        public MuteAwaitCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(2, Integer.parseInt(list.get(0)), "address", list.get(1), CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList());
            String str = list.get(2);
            Object systemService = MediaMuteAwaitConnectionCli.this.context.getSystemService("audio");
            if (systemService != null) {
                AudioManager audioManager = (AudioManager) systemService;
                if (Intrinsics.areEqual((Object) str, (Object) "start")) {
                    audioManager.muteAwaitConnection(new int[]{1}, audioDeviceAttributes, 5, MediaMuteAwaitConnectionCliKt.TIMEOUT_UNITS);
                } else if (Intrinsics.areEqual((Object) str, (Object) "cancel")) {
                    audioManager.cancelMuteAwaitConnection(audioDeviceAttributes);
                } else {
                    printWriter.println(Intrinsics.stringPlus("Must specify `start` or `cancel`; was ", str));
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
            }
        }
    }
}
