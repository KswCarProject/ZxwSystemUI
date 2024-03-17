package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import java.util.List;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ShadeEventCoordinator.kt */
public final class ShadeEventCoordinator implements Coordinator, NotifShadeEventSource {
    public boolean mEntryRemoved;
    public boolean mEntryRemovedByUser;
    @NotNull
    public final ShadeEventCoordinatorLogger mLogger;
    @NotNull
    public final Executor mMainExecutor;
    @NotNull
    public final ShadeEventCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new ShadeEventCoordinator$mNotifCollectionListener$1(this);
    @Nullable
    public Runnable mNotifRemovedByUserCallback;
    @Nullable
    public Runnable mShadeEmptiedCallback;

    public ShadeEventCoordinator(@NotNull Executor executor, @NotNull ShadeEventCoordinatorLogger shadeEventCoordinatorLogger) {
        this.mMainExecutor = executor;
        this.mLogger = shadeEventCoordinatorLogger;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeRenderListListener(new ShadeEventCoordinator$attach$1(this));
    }

    public void setNotifRemovedByUserCallback(@NotNull Runnable runnable) {
        if (this.mNotifRemovedByUserCallback == null) {
            this.mNotifRemovedByUserCallback = runnable;
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set".toString());
    }

    public void setShadeEmptiedCallback(@NotNull Runnable runnable) {
        if (this.mShadeEmptiedCallback == null) {
            this.mShadeEmptiedCallback = runnable;
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set".toString());
    }

    public final void onBeforeRenderList(List<? extends ListEntry> list) {
        if (this.mEntryRemoved && list.isEmpty()) {
            this.mLogger.logShadeEmptied();
            Runnable runnable = this.mShadeEmptiedCallback;
            if (runnable != null) {
                this.mMainExecutor.execute(runnable);
            }
        }
        if (this.mEntryRemoved && this.mEntryRemovedByUser) {
            this.mLogger.logNotifRemovedByUser();
            Runnable runnable2 = this.mNotifRemovedByUserCallback;
            if (runnable2 != null) {
                this.mMainExecutor.execute(runnable2);
            }
        }
        this.mEntryRemoved = false;
        this.mEntryRemovedByUser = false;
    }
}
