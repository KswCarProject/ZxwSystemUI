package kotlinx.coroutines.scheduling;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: Dispatcher.kt */
public final class DefaultScheduler extends ExperimentalCoroutineDispatcher {
    @NotNull
    public static final DefaultScheduler INSTANCE;
    @NotNull
    public static final CoroutineDispatcher IO;

    @NotNull
    public String toString() {
        return "Dispatchers.Default";
    }

    public DefaultScheduler() {
        super(0, 0, (String) null, 7, (DefaultConstructorMarker) null);
    }

    static {
        DefaultScheduler defaultScheduler = new DefaultScheduler();
        INSTANCE = defaultScheduler;
        IO = new LimitingDispatcher(defaultScheduler, SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.io.parallelism", RangesKt___RangesKt.coerceAtLeast(64, SystemPropsKt.getAVAILABLE_PROCESSORS()), 0, 0, 12, (Object) null), "Dispatchers.IO", 1);
    }

    @NotNull
    public final CoroutineDispatcher getIO() {
        return IO;
    }

    public void close() {
        throw new UnsupportedOperationException("Dispatchers.Default cannot be closed");
    }
}
