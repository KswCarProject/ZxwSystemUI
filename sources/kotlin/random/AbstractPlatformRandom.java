package kotlin.random;

import java.util.Random;
import org.jetbrains.annotations.NotNull;

/* compiled from: PlatformRandom.kt */
public abstract class AbstractPlatformRandom extends Random {
    @NotNull
    public abstract Random getImpl();

    public int nextInt() {
        return getImpl().nextInt();
    }
}
