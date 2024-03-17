package kotlin.random.jdk8;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import kotlin.random.AbstractPlatformRandom;
import org.jetbrains.annotations.NotNull;

/* compiled from: PlatformThreadLocalRandom.kt */
public final class PlatformThreadLocalRandom extends AbstractPlatformRandom {
    @NotNull
    public Random getImpl() {
        return ThreadLocalRandom.current();
    }
}
