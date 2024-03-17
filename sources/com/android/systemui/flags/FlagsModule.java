package com.android.systemui.flags;

import android.content.Context;
import android.os.Handler;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: FlagsModule.kt */
public abstract class FlagsModule {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @NotNull
    public static final FlagManager provideFlagManager(@NotNull Context context, @NotNull Handler handler) {
        return Companion.provideFlagManager(context, handler);
    }

    @NotNull
    public static final Map<Integer, Flag<?>> providesAllFlags() {
        return Companion.providesAllFlags();
    }

    /* compiled from: FlagsModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final FlagManager provideFlagManager(@NotNull Context context, @NotNull Handler handler) {
            return new FlagManager(context, handler);
        }

        @NotNull
        public final Map<Integer, Flag<?>> providesAllFlags() {
            return Flags.collectFlags();
        }
    }
}
