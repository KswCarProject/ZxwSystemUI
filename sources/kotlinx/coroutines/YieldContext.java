package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Unconfined.kt */
public final class YieldContext extends AbstractCoroutineContextElement {
    @NotNull
    public static final Key Key = new Key((DefaultConstructorMarker) null);
    public boolean dispatcherWasUnconfined;

    /* compiled from: Unconfined.kt */
    public static final class Key implements CoroutineContext.Key<YieldContext> {
        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Key() {
        }
    }

    public YieldContext() {
        super(Key);
    }
}
