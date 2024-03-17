package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Timeout.kt */
public final class TimeoutCancellationException extends CancellationException implements CopyableThrowable<TimeoutCancellationException> {
    @Nullable
    public final Job coroutine;

    public TimeoutCancellationException(@NotNull String str, @Nullable Job job) {
        super(str);
        this.coroutine = job;
    }

    public TimeoutCancellationException(@NotNull String str) {
        this(str, (Job) null);
    }

    @Nullable
    public TimeoutCancellationException createCopy() {
        String message = getMessage();
        if (message == null) {
            message = "";
        }
        TimeoutCancellationException timeoutCancellationException = new TimeoutCancellationException(message, this.coroutine);
        timeoutCancellationException.initCause(this);
        return timeoutCancellationException;
    }
}
