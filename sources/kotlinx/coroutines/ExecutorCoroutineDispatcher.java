package kotlinx.coroutines;

import java.io.Closeable;
import kotlin.coroutines.AbstractCoroutineContextKey;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Executors.kt */
public abstract class ExecutorCoroutineDispatcher extends CoroutineDispatcher implements Closeable {
    @NotNull
    public static final Key Key = new Key((DefaultConstructorMarker) null);

    /* compiled from: Executors.kt */
    public static final class Key extends AbstractCoroutineContextKey<CoroutineDispatcher, ExecutorCoroutineDispatcher> {
        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Key() {
            super(CoroutineDispatcher.Key, AnonymousClass1.INSTANCE);
        }
    }
}
