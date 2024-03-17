package kotlin.random;

import java.util.Random;
import org.jetbrains.annotations.NotNull;

/* compiled from: PlatformRandom.kt */
public final class FallbackThreadLocalRandom extends AbstractPlatformRandom {
    @NotNull
    public final FallbackThreadLocalRandom$implStorage$1 implStorage = new FallbackThreadLocalRandom$implStorage$1();

    @NotNull
    public Random getImpl() {
        return (Random) this.implStorage.get();
    }
}
