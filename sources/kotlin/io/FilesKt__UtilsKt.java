package kotlin.io;

import java.io.File;

/* compiled from: Utils.kt */
public class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return copyTo(file, file2, z, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005b, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r6, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x005f, code lost:
        throw r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0062, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0063, code lost:
        kotlin.io.CloseableKt.closeFinally(r8, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0066, code lost:
        throw r7;
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.io.File copyTo(@org.jetbrains.annotations.NotNull java.io.File r6, @org.jetbrains.annotations.NotNull java.io.File r7, boolean r8, int r9) {
        /*
            boolean r0 = r6.exists()
            if (r0 == 0) goto L_0x0067
            boolean r0 = r7.exists()
            if (r0 == 0) goto L_0x0025
            if (r8 == 0) goto L_0x001d
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0015
            goto L_0x0025
        L_0x0015:
            kotlin.io.FileAlreadyExistsException r8 = new kotlin.io.FileAlreadyExistsException
            java.lang.String r9 = "Tried to overwrite the destination, but failed to delete it."
            r8.<init>(r6, r7, r9)
            throw r8
        L_0x001d:
            kotlin.io.FileAlreadyExistsException r8 = new kotlin.io.FileAlreadyExistsException
            java.lang.String r9 = "The destination file already exists."
            r8.<init>(r6, r7, r9)
            throw r8
        L_0x0025:
            boolean r8 = r6.isDirectory()
            if (r8 == 0) goto L_0x003a
            boolean r8 = r7.mkdirs()
            if (r8 == 0) goto L_0x0032
            goto L_0x0058
        L_0x0032:
            kotlin.io.FileSystemException r8 = new kotlin.io.FileSystemException
            java.lang.String r9 = "Failed to create target directory."
            r8.<init>(r6, r7, r9)
            throw r8
        L_0x003a:
            java.io.File r8 = r7.getParentFile()
            if (r8 != 0) goto L_0x0041
            goto L_0x0044
        L_0x0041:
            r8.mkdirs()
        L_0x0044:
            java.io.FileInputStream r8 = new java.io.FileInputStream
            r8.<init>(r6)
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ all -> 0x0060 }
            r6.<init>(r7)     // Catch:{ all -> 0x0060 }
            kotlin.io.ByteStreamsKt.copyTo(r8, r6, r9)     // Catch:{ all -> 0x0059 }
            r9 = 0
            kotlin.io.CloseableKt.closeFinally(r6, r9)     // Catch:{ all -> 0x0060 }
            kotlin.io.CloseableKt.closeFinally(r8, r9)
        L_0x0058:
            return r7
        L_0x0059:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x005b }
        L_0x005b:
            r9 = move-exception
            kotlin.io.CloseableKt.closeFinally(r6, r7)     // Catch:{ all -> 0x0060 }
            throw r9     // Catch:{ all -> 0x0060 }
        L_0x0060:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0062 }
        L_0x0062:
            r7 = move-exception
            kotlin.io.CloseableKt.closeFinally(r8, r6)
            throw r7
        L_0x0067:
            kotlin.io.NoSuchFileException r7 = new kotlin.io.NoSuchFileException
            r2 = 0
            r4 = 2
            r5 = 0
            java.lang.String r3 = "The source file doesn't exist."
            r0 = r7
            r1 = r6
            r0.<init>(r1, r2, r3, r4, r5)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__UtilsKt.copyTo(java.io.File, java.io.File, boolean, int):java.io.File");
    }
}
