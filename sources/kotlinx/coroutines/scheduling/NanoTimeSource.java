package kotlinx.coroutines.scheduling;

import org.jetbrains.annotations.NotNull;

/* compiled from: Tasks.kt */
public final class NanoTimeSource extends SchedulerTimeSource {
    @NotNull
    public static final NanoTimeSource INSTANCE = new NanoTimeSource();

    public long nanoTime() {
        return System.nanoTime();
    }
}
