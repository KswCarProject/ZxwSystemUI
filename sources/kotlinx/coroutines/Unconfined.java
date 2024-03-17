package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;

/* compiled from: Unconfined.kt */
public final class Unconfined extends CoroutineDispatcher {
    @NotNull
    public static final Unconfined INSTANCE = new Unconfined();

    public boolean isDispatchNeeded(@NotNull CoroutineContext coroutineContext) {
        return false;
    }

    @NotNull
    public String toString() {
        return "Dispatchers.Unconfined";
    }

    public void dispatch(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        YieldContext yieldContext = (YieldContext) coroutineContext.get(YieldContext.Key);
        if (yieldContext != null) {
            yieldContext.dispatcherWasUnconfined = true;
            return;
        }
        throw new UnsupportedOperationException("Dispatchers.Unconfined.dispatch function can only be used by the yield function. If you wrap Unconfined dispatcher in your code, make sure you properly delegate isDispatchNeeded and dispatch calls.");
    }
}
