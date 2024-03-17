package com.android.systemui.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.people.widget.PeopleBackupHelper;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BackupHelper.kt */
public class BackupHelper extends BackupAgentHelper {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Object controlsDataLock = new Object();

    /* compiled from: BackupHelper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final Object getControlsDataLock() {
            return BackupHelper.controlsDataLock;
        }
    }

    public void onCreate(@NotNull UserHandle userHandle, int i) {
        super.onCreate();
        addHelper("systemui.files_no_overwrite", new NoOverwriteFileBackupHelper(controlsDataLock, this, MapsKt__MapsJVMKt.mapOf(TuplesKt.to("controls_favorites.xml", BackupHelperKt.getPPControlsFile(this)))));
        if (userHandle.isSystem()) {
            Object[] array = PeopleBackupHelper.getFilesToBackup().toArray(new String[0]);
            if (array != null) {
                addHelper("systemui.people.shared_preferences", new PeopleBackupHelper(this, userHandle, (String[]) array));
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
    }

    public void onRestoreFinished() {
        super.onRestoreFinished();
        Intent intent = new Intent("com.android.systemui.backup.RESTORE_FINISHED");
        intent.setPackage(getPackageName());
        intent.putExtra("android.intent.extra.USER_ID", getUserId());
        intent.setFlags(1073741824);
        sendBroadcastAsUser(intent, UserHandle.SYSTEM, "com.android.systemui.permission.SELF");
    }

    /* compiled from: BackupHelper.kt */
    public static final class NoOverwriteFileBackupHelper extends FileBackupHelper {
        @NotNull
        public final Context context;
        @NotNull
        public final Map<String, Function0<Unit>> fileNamesAndPostProcess;
        @NotNull
        public final Object lock;

        @NotNull
        public final Map<String, Function0<Unit>> getFileNamesAndPostProcess() {
            return this.fileNamesAndPostProcess;
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoOverwriteFileBackupHelper(@org.jetbrains.annotations.NotNull java.lang.Object r3, @org.jetbrains.annotations.NotNull android.content.Context r4, @org.jetbrains.annotations.NotNull java.util.Map<java.lang.String, ? extends kotlin.jvm.functions.Function0<kotlin.Unit>> r5) {
            /*
                r2 = this;
                java.util.Set r0 = r5.keySet()
                java.util.Collection r0 = (java.util.Collection) r0
                r1 = 0
                java.lang.String[] r1 = new java.lang.String[r1]
                java.lang.Object[] r0 = r0.toArray(r1)
                if (r0 == 0) goto L_0x0022
                java.lang.String[] r0 = (java.lang.String[]) r0
                int r1 = r0.length
                java.lang.Object[] r0 = java.util.Arrays.copyOf(r0, r1)
                java.lang.String[] r0 = (java.lang.String[]) r0
                r2.<init>(r4, r0)
                r2.lock = r3
                r2.context = r4
                r2.fileNamesAndPostProcess = r5
                return
            L_0x0022:
                java.lang.NullPointerException r2 = new java.lang.NullPointerException
                java.lang.String r3 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
                r2.<init>(r3)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.backup.BackupHelper.NoOverwriteFileBackupHelper.<init>(java.lang.Object, android.content.Context, java.util.Map):void");
        }

        public void restoreEntity(@NotNull BackupDataInputStream backupDataInputStream) {
            if (Environment.buildPath(this.context.getFilesDir(), new String[]{backupDataInputStream.getKey()}).exists()) {
                Log.w("BackupHelper", "File " + backupDataInputStream.getKey() + " already exists. Skipping restore.");
                return;
            }
            synchronized (this.lock) {
                super.restoreEntity(backupDataInputStream);
                Function0 function0 = getFileNamesAndPostProcess().get(backupDataInputStream.getKey());
                if (function0 != null) {
                    function0.invoke();
                    Unit unit = Unit.INSTANCE;
                }
            }
        }

        public void performBackup(@Nullable ParcelFileDescriptor parcelFileDescriptor, @Nullable BackupDataOutput backupDataOutput, @Nullable ParcelFileDescriptor parcelFileDescriptor2) {
            synchronized (this.lock) {
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
