package okio;

import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

/* renamed from: okio.-Platform  reason: invalid class name */
/* compiled from: -Platform.kt */
public final class Platform {
    @NotNull
    public static final String toUtf8String(@NotNull byte[] bArr) {
        return new String(bArr, Charsets.UTF_8);
    }

    @NotNull
    public static final byte[] asUtf8ToByteArray(@NotNull String str) {
        return str.getBytes(Charsets.UTF_8);
    }
}
