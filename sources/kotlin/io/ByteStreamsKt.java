package kotlin.io;

import java.io.InputStream;
import java.io.OutputStream;
import org.jetbrains.annotations.NotNull;

/* compiled from: IOStreams.kt */
public final class ByteStreamsKt {
    public static final long copyTo(@NotNull InputStream inputStream, @NotNull OutputStream outputStream, int i) {
        byte[] bArr = new byte[i];
        int read = inputStream.read(bArr);
        long j = 0;
        while (read >= 0) {
            outputStream.write(bArr, 0, read);
            j += (long) read;
            read = inputStream.read(bArr);
        }
        return j;
    }
}
