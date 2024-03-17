package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugModeFilterProvider.kt */
public final class DebugModeFilterProvider$registerInvalidationListener$1 extends Lambda implements Function0<Command> {
    public final /* synthetic */ DebugModeFilterProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DebugModeFilterProvider$registerInvalidationListener$1(DebugModeFilterProvider debugModeFilterProvider) {
        super(0);
        this.this$0 = debugModeFilterProvider;
    }

    @NotNull
    public final Command invoke() {
        return new DebugModeFilterProvider.NotifFilterCommand();
    }
}
