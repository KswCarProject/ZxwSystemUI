package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.backup.BackupHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuxiliaryPersistenceWrapper.kt */
public final class AuxiliaryPersistenceWrapper {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public List<StructureInfo> favorites;
    @NotNull
    public ControlsFavoritePersistenceWrapper persistenceWrapper;

    @VisibleForTesting
    public AuxiliaryPersistenceWrapper(@NotNull ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper) {
        this.persistenceWrapper = controlsFavoritePersistenceWrapper;
        this.favorites = CollectionsKt__CollectionsKt.emptyList();
        initialize();
    }

    public AuxiliaryPersistenceWrapper(@NotNull File file, @NotNull Executor executor) {
        this(new ControlsFavoritePersistenceWrapper(file, executor, (BackupManager) null, 4, (DefaultConstructorMarker) null));
    }

    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public final List<StructureInfo> getFavorites() {
        return this.favorites;
    }

    public final void changeFile(@NotNull File file) {
        this.persistenceWrapper.changeFileAndBackupManager(file, (BackupManager) null);
        initialize();
    }

    public final void initialize() {
        List<StructureInfo> list;
        if (this.persistenceWrapper.getFileExists()) {
            list = this.persistenceWrapper.readFavorites();
        } else {
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        this.favorites = list;
    }

    @NotNull
    public final List<StructureInfo> getCachedFavoritesAndRemoveFor(@NotNull ComponentName componentName) {
        if (!this.persistenceWrapper.getFileExists()) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object next : this.favorites) {
            if (Intrinsics.areEqual((Object) ((StructureInfo) next).getComponentName(), (Object) componentName)) {
                arrayList.add(next);
            } else {
                arrayList2.add(next);
            }
        }
        Pair pair = new Pair(arrayList, arrayList2);
        List<StructureInfo> list = (List) pair.component1();
        List<StructureInfo> list2 = (List) pair.component2();
        this.favorites = list2;
        if (!getFavorites().isEmpty()) {
            this.persistenceWrapper.storeFavorites(list2);
        } else {
            this.persistenceWrapper.deleteFile();
        }
        return list;
    }

    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    public static final class DeletionJobService extends JobService {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        public static final int DELETE_FILE_JOB_ID = 1000;
        public static final long WEEK_IN_MILLIS = TimeUnit.DAYS.toMillis(7);

        public boolean onStopJob(@Nullable JobParameters jobParameters) {
            return true;
        }

        /* compiled from: AuxiliaryPersistenceWrapper.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            @VisibleForTesting
            public static /* synthetic */ void getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
            }

            public Companion() {
            }

            public final int getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
                return DeletionJobService.DELETE_FILE_JOB_ID;
            }

            @NotNull
            public final JobInfo getJobForContext(@NotNull Context context) {
                return new JobInfo.Builder(getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core() + context.getUserId(), new ComponentName(context, DeletionJobService.class)).setMinimumLatency(DeletionJobService.WEEK_IN_MILLIS).setPersisted(true).build();
            }
        }

        @VisibleForTesting
        public final void attachContext(@NotNull Context context) {
            attachBaseContext(context);
        }

        public boolean onStartJob(@NotNull JobParameters jobParameters) {
            synchronized (BackupHelper.Companion.getControlsDataLock()) {
                getBaseContext().deleteFile("aux_controls_favorites.xml");
            }
            return false;
        }
    }
}
