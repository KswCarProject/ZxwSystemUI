package com.android.systemui.backup;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Environment;
import com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper;
import java.io.File;
import kotlin.Unit;
import kotlin.io.FilesKt__UtilsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: BackupHelper.kt */
public final class BackupHelperKt$getPPControlsFile$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Context $context;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BackupHelperKt$getPPControlsFile$1(Context context) {
        super(0);
        this.$context = context;
    }

    public final void invoke() {
        File filesDir = this.$context.getFilesDir();
        File buildPath = Environment.buildPath(filesDir, new String[]{"controls_favorites.xml"});
        if (buildPath.exists()) {
            FilesKt__UtilsKt.copyTo$default(buildPath, Environment.buildPath(filesDir, new String[]{"aux_controls_favorites.xml"}), false, 0, 6, (Object) null);
            JobScheduler jobScheduler = (JobScheduler) this.$context.getSystemService(JobScheduler.class);
            if (jobScheduler != null) {
                jobScheduler.schedule(AuxiliaryPersistenceWrapper.DeletionJobService.Companion.getJobForContext(this.$context));
            }
        }
    }
}
