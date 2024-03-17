package kotlin.internal.jdk7;

import kotlin.internal.PlatformImplementations;
import org.jetbrains.annotations.NotNull;

/* compiled from: JDK7PlatformImplementations.kt */
public class JDK7PlatformImplementations extends PlatformImplementations {
    public void addSuppressed(@NotNull Throwable th, @NotNull Throwable th2) {
        th.addSuppressed(th2);
    }
}
