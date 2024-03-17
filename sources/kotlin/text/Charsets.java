package kotlin.text;

import java.nio.charset.Charset;
import org.jetbrains.annotations.NotNull;

/* compiled from: Charsets.kt */
public final class Charsets {
    @NotNull
    public static final Charsets INSTANCE = new Charsets();
    @NotNull
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    @NotNull
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    @NotNull
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    @NotNull
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    @NotNull
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    @NotNull
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
