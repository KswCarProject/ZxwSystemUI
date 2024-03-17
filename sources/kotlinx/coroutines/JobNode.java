package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public abstract class JobNode extends CompletionHandlerBase implements DisposableHandle, Incomplete {
    public JobSupport job;

    @Nullable
    public NodeList getList() {
        return null;
    }

    public boolean isActive() {
        return true;
    }

    @NotNull
    public final JobSupport getJob() {
        JobSupport jobSupport = this.job;
        if (jobSupport != null) {
            return jobSupport;
        }
        return null;
    }

    public final void setJob(@NotNull JobSupport jobSupport) {
        this.job = jobSupport;
    }

    public void dispose() {
        getJob().removeNode$external__kotlinx_coroutines__android_common__kotlinx_coroutines(this);
    }

    @NotNull
    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this) + "[job@" + DebugStringsKt.getHexAddress(getJob()) + ']';
    }
}
