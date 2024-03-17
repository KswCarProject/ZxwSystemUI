package com.android.systemui.statusbar;

import com.android.systemui.statusbar.DisableFlagsLogger;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: DisableFlagsLogger.kt */
public final class DisableFlagsLoggerKt {
    @NotNull
    public static final List<DisableFlagsLogger.DisableFlag> defaultDisable1FlagsList = CollectionsKt__CollectionsKt.listOf(new DisableFlagsLogger.DisableFlag(65536, 'E', 'e'), new DisableFlagsLogger.DisableFlag(131072, 'N', 'n'), new DisableFlagsLogger.DisableFlag(262144, 'A', 'a'), new DisableFlagsLogger.DisableFlag(1048576, 'I', 'i'), new DisableFlagsLogger.DisableFlag(2097152, 'H', 'h'), new DisableFlagsLogger.DisableFlag(4194304, 'B', 'b'), new DisableFlagsLogger.DisableFlag(8388608, 'C', 'c'), new DisableFlagsLogger.DisableFlag(16777216, 'R', 'r'), new DisableFlagsLogger.DisableFlag(33554432, 'S', 's'), new DisableFlagsLogger.DisableFlag(67108864, 'O', 'o'));
    @NotNull
    public static final List<DisableFlagsLogger.DisableFlag> defaultDisable2FlagsList = CollectionsKt__CollectionsKt.listOf(new DisableFlagsLogger.DisableFlag(1, 'Q', 'q'), new DisableFlagsLogger.DisableFlag(2, 'I', 'i'), new DisableFlagsLogger.DisableFlag(4, 'N', 'n'), new DisableFlagsLogger.DisableFlag(8, 'G', 'g'), new DisableFlagsLogger.DisableFlag(16, 'R', 'r'));
}
