package kotlin.internal.jdk8;

import kotlin.internal.jdk7.JDK7PlatformImplementations;
import kotlin.random.Random;
import kotlin.random.jdk8.PlatformThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

/* compiled from: JDK8PlatformImplementations.kt */
public class JDK8PlatformImplementations extends JDK7PlatformImplementations {
    @NotNull
    public Random defaultPlatformRandom() {
        return new PlatformThreadLocalRandom();
    }
}
