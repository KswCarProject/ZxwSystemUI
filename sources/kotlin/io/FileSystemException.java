package kotlin.io;

import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Exceptions.kt */
public class FileSystemException extends IOException {
    @NotNull
    private final File file;
    @Nullable
    private final File other;
    @Nullable
    private final String reason;

    public FileSystemException(@NotNull File file2, @Nullable File file3, @Nullable String str) {
        super(ExceptionsKt.constructMessage(file2, file3, str));
        this.file = file2;
        this.other = file3;
        this.reason = str;
    }
}
