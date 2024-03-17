package kotlin.io;

import java.io.File;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
public final class ExceptionsKt {
    public static final String constructMessage(File file, File file2, String str) {
        StringBuilder sb = new StringBuilder(file.toString());
        if (file2 != null) {
            sb.append(Intrinsics.stringPlus(" -> ", file2));
        }
        if (str != null) {
            sb.append(Intrinsics.stringPlus(": ", str));
        }
        return sb.toString();
    }
}
