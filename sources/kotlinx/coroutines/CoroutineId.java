package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CoroutineContext.kt */
public final class CoroutineId extends AbstractCoroutineContextElement implements ThreadContextElement<String> {
    @NotNull
    public static final Key Key = new Key((DefaultConstructorMarker) null);
    public final long id;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CoroutineId) && this.id == ((CoroutineId) obj).id;
    }

    public int hashCode() {
        return Long.hashCode(this.id);
    }

    public final long getId() {
        return this.id;
    }

    /* compiled from: CoroutineContext.kt */
    public static final class Key implements CoroutineContext.Key<CoroutineId> {
        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Key() {
        }
    }

    public CoroutineId(long j) {
        super(Key);
        this.id = j;
    }

    @NotNull
    public String toString() {
        return "CoroutineId(" + this.id + ')';
    }

    @NotNull
    public String updateThreadContext(@NotNull CoroutineContext coroutineContext) {
        String name;
        CoroutineName coroutineName = (CoroutineName) coroutineContext.get(CoroutineName.Key);
        String str = "coroutine";
        if (!(coroutineName == null || (name = coroutineName.getName()) == null)) {
            str = name;
        }
        Thread currentThread = Thread.currentThread();
        String name2 = currentThread.getName();
        int lastIndexOf$default = StringsKt__StringsKt.lastIndexOf$default((CharSequence) name2, " @", 0, false, 6, (Object) null);
        if (lastIndexOf$default < 0) {
            lastIndexOf$default = name2.length();
        }
        StringBuilder sb = new StringBuilder(str.length() + lastIndexOf$default + 10);
        sb.append(name2.substring(0, lastIndexOf$default));
        sb.append(" @");
        sb.append(str);
        sb.append('#');
        sb.append(getId());
        currentThread.setName(sb.toString());
        return name2;
    }

    public void restoreThreadContext(@NotNull CoroutineContext coroutineContext, @NotNull String str) {
        Thread.currentThread().setName(str);
    }
}
