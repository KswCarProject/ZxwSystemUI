package kotlin.random;

import java.util.Random;
import org.jetbrains.annotations.NotNull;

/* compiled from: PlatformRandom.kt */
public final class FallbackThreadLocalRandom$implStorage$1 extends ThreadLocal<Random> {
    @NotNull
    public Random initialValue() {
        return new Random();
    }
}
