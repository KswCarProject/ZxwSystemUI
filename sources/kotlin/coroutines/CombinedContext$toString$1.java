package kotlin.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: CoroutineContextImpl.kt */
public final class CombinedContext$toString$1 extends Lambda implements Function2<String, CoroutineContext.Element, String> {
    public static final CombinedContext$toString$1 INSTANCE = new CombinedContext$toString$1();

    public CombinedContext$toString$1() {
        super(2);
    }

    @NotNull
    public final String invoke(@NotNull String str, @NotNull CoroutineContext.Element element) {
        if (str.length() == 0) {
            return element.toString();
        }
        return str + ", " + element;
    }
}
