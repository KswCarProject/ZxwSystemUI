package kotlinx.coroutines;

import kotlinx.coroutines.scheduling.DefaultScheduler;
import org.jetbrains.annotations.NotNull;

/* compiled from: Dispatchers.kt */
public final class Dispatchers {
    @NotNull
    public static final CoroutineDispatcher Default = CoroutineContextKt.createDefaultDispatcher();
    @NotNull
    public static final Dispatchers INSTANCE = new Dispatchers();
    @NotNull
    public static final CoroutineDispatcher IO = DefaultScheduler.INSTANCE.getIO();
    @NotNull
    public static final CoroutineDispatcher Unconfined = Unconfined.INSTANCE;

    @NotNull
    public static final CoroutineDispatcher getDefault() {
        return Default;
    }

    @NotNull
    public static final CoroutineDispatcher getIO() {
        return IO;
    }
}
