package okio;

import org.jetbrains.annotations.NotNull;

/* compiled from: Okio.kt */
public final /* synthetic */ class Okio__OkioKt {
    @NotNull
    public static final BufferedSource buffer(@NotNull Source source) {
        return new RealBufferedSource(source);
    }
}
