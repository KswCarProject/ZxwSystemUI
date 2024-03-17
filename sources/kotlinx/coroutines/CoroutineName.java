package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CoroutineName.kt */
public final class CoroutineName extends AbstractCoroutineContextElement {
    @NotNull
    public static final Key Key = new Key((DefaultConstructorMarker) null);
    @NotNull
    public final String name;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CoroutineName) && Intrinsics.areEqual((Object) this.name, (Object) ((CoroutineName) obj).name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    /* compiled from: CoroutineName.kt */
    public static final class Key implements CoroutineContext.Key<CoroutineName> {
        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Key() {
        }
    }

    @NotNull
    public String toString() {
        return "CoroutineName(" + this.name + ')';
    }
}
