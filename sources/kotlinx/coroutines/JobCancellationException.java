package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Exceptions.kt */
public final class JobCancellationException extends CancellationException implements CopyableThrowable<JobCancellationException> {
    @NotNull
    public final Job job;

    public JobCancellationException(@NotNull String str, @Nullable Throwable th, @NotNull Job job2) {
        super(str);
        this.job = job2;
        if (th != null) {
            initCause(th);
        }
    }

    @NotNull
    public Throwable fillInStackTrace() {
        if (DebugKt.getDEBUG()) {
            return super.fillInStackTrace();
        }
        setStackTrace(new StackTraceElement[0]);
        return this;
    }

    @Nullable
    public JobCancellationException createCopy() {
        if (!DebugKt.getDEBUG()) {
            return null;
        }
        String message = getMessage();
        Intrinsics.checkNotNull(message);
        return new JobCancellationException(message, this, this.job);
    }

    @NotNull
    public String toString() {
        return super.toString() + "; job=" + this.job;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (obj instanceof JobCancellationException) {
                JobCancellationException jobCancellationException = (JobCancellationException) obj;
                if (!Intrinsics.areEqual((Object) jobCancellationException.getMessage(), (Object) getMessage()) || !Intrinsics.areEqual((Object) jobCancellationException.job, (Object) this.job) || !Intrinsics.areEqual((Object) jobCancellationException.getCause(), (Object) getCause())) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String message = getMessage();
        Intrinsics.checkNotNull(message);
        int hashCode = ((message.hashCode() * 31) + this.job.hashCode()) * 31;
        Throwable cause = getCause();
        return hashCode + (cause == null ? 0 : cause.hashCode());
    }
}
