package okio;

import java.io.InputStream;
import java.util.logging.Logger;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: JvmOkio.kt */
public final /* synthetic */ class Okio__JvmOkioKt {
    public static final Logger logger = Logger.getLogger("okio.Okio");

    @NotNull
    public static final Source source(@NotNull InputStream inputStream) {
        return new InputStreamSource(inputStream, new Timeout());
    }

    public static final boolean isAndroidGetsocknameError(@NotNull AssertionError assertionError) {
        if (assertionError.getCause() == null) {
            return false;
        }
        String message = assertionError.getMessage();
        return message == null ? false : StringsKt__StringsKt.contains$default(message, "getsockname failed", false, 2, (Object) null);
    }
}
