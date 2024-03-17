package com.android.systemui.privacy;

import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor implements PrivacyItemMonitor {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Handler bgHandler;
    @GuardedBy({"lock"})
    @Nullable
    public PrivacyItemMonitor.Callback callback;
    @GuardedBy({"lock"})
    public boolean listening;
    @NotNull
    public final Object lock = new Object();
    @NotNull
    public final PrivacyLogger logger;
    @GuardedBy({"lock"})
    public boolean mediaProjectionAvailable;
    @NotNull
    public final MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1 mediaProjectionCallback;
    @NotNull
    public final MediaProjectionManager mediaProjectionManager;
    @NotNull
    public final MediaProjectionPrivacyItemMonitor$optionsCallback$1 optionsCallback;
    @NotNull
    public final PackageManager packageManager;
    @NotNull
    public final PrivacyConfig privacyConfig;
    @GuardedBy({"lock"})
    @NotNull
    public final List<PrivacyItem> privacyItems;
    @NotNull
    public final SystemClock systemClock;

    public MediaProjectionPrivacyItemMonitor(@NotNull MediaProjectionManager mediaProjectionManager2, @NotNull PackageManager packageManager2, @NotNull PrivacyConfig privacyConfig2, @NotNull Handler handler, @NotNull SystemClock systemClock2, @NotNull PrivacyLogger privacyLogger) {
        this.mediaProjectionManager = mediaProjectionManager2;
        this.packageManager = packageManager2;
        this.privacyConfig = privacyConfig2;
        this.bgHandler = handler;
        this.systemClock = systemClock2;
        this.logger = privacyLogger;
        this.mediaProjectionAvailable = privacyConfig2.getMediaProjectionAvailable();
        this.privacyItems = new ArrayList();
        MediaProjectionPrivacyItemMonitor$optionsCallback$1 mediaProjectionPrivacyItemMonitor$optionsCallback$1 = new MediaProjectionPrivacyItemMonitor$optionsCallback$1(this);
        this.optionsCallback = mediaProjectionPrivacyItemMonitor$optionsCallback$1;
        this.mediaProjectionCallback = new MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1(this);
        privacyConfig2.addCallback((PrivacyConfig.Callback) mediaProjectionPrivacyItemMonitor$optionsCallback$1);
        setListeningStateLocked();
    }

    /* compiled from: MediaProjectionPrivacyItemMonitor.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void startListening(@NotNull PrivacyItemMonitor.Callback callback2) {
        synchronized (this.lock) {
            this.callback = callback2;
            Unit unit = Unit.INSTANCE;
        }
    }

    public void stopListening() {
        synchronized (this.lock) {
            this.callback = null;
            Unit unit = Unit.INSTANCE;
        }
    }

    @GuardedBy({"lock"})
    public final void onMediaProjectionStartedLocked(MediaProjectionInfo mediaProjectionInfo) {
        PrivacyItem makePrivacyItem = makePrivacyItem(mediaProjectionInfo);
        this.privacyItems.add(makePrivacyItem);
        logItemActive(makePrivacyItem, true);
    }

    @GuardedBy({"lock"})
    public final void onMediaProjectionStoppedLocked(MediaProjectionInfo mediaProjectionInfo) {
        PrivacyItem makePrivacyItem = makePrivacyItem(mediaProjectionInfo);
        List<PrivacyItem> list = this.privacyItems;
        Iterator<PrivacyItem> it = list.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (Intrinsics.areEqual((Object) it.next().getApplication(), (Object) makePrivacyItem.getApplication())) {
                break;
            } else {
                i++;
            }
        }
        list.remove(i);
        logItemActive(makePrivacyItem, false);
    }

    public final PrivacyItem makePrivacyItem(MediaProjectionInfo mediaProjectionInfo) {
        return new PrivacyItem(PrivacyType.TYPE_MEDIA_PROJECTION, new PrivacyApplication(mediaProjectionInfo.getPackageName(), this.packageManager.getPackageUidAsUser(mediaProjectionInfo.getPackageName(), mediaProjectionInfo.getUserHandle().getIdentifier())), this.systemClock.elapsedRealtime(), false, 8, (DefaultConstructorMarker) null);
    }

    public final void logItemActive(PrivacyItem privacyItem, boolean z) {
        this.logger.logUpdatedItemFromMediaProjection(privacyItem.getApplication().getUid(), privacyItem.getApplication().getPackageName(), z);
    }

    @GuardedBy({"lock"})
    public final void setListeningStateLocked() {
        boolean z = this.mediaProjectionAvailable;
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.mediaProjectionManager.addCallback(this.mediaProjectionCallback, this.bgHandler);
                MediaProjectionInfo activeProjectionInfo = this.mediaProjectionManager.getActiveProjectionInfo();
                if (activeProjectionInfo != null) {
                    onMediaProjectionStartedLocked(activeProjectionInfo);
                    dispatchOnPrivacyItemsChanged();
                    return;
                }
                return;
            }
            this.mediaProjectionManager.removeCallback(this.mediaProjectionCallback);
            for (PrivacyItem logItemActive : this.privacyItems) {
                logItemActive(logItemActive, false);
            }
            this.privacyItems.clear();
            dispatchOnPrivacyItemsChanged();
        }
    }

    @NotNull
    public List<PrivacyItem> getActivePrivacyItems() {
        List<PrivacyItem> list;
        synchronized (this.lock) {
            list = CollectionsKt___CollectionsKt.toList(this.privacyItems);
        }
        return list;
    }

    public final void dispatchOnPrivacyItemsChanged() {
        PrivacyItemMonitor.Callback callback2;
        synchronized (this.lock) {
            callback2 = this.callback;
        }
        if (callback2 != null) {
            this.bgHandler.post(new MediaProjectionPrivacyItemMonitor$dispatchOnPrivacyItemsChanged$1(callback2));
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("MediaProjectionPrivacyItemMonitor:");
        asIndenting.increaseIndent();
        try {
            synchronized (this.lock) {
                asIndenting.println(Intrinsics.stringPlus("Listening: ", Boolean.valueOf(this.listening)));
                asIndenting.println(Intrinsics.stringPlus("mediaProjectionAvailable: ", Boolean.valueOf(this.mediaProjectionAvailable)));
                asIndenting.println(Intrinsics.stringPlus("Callback: ", this.callback));
                asIndenting.println(Intrinsics.stringPlus("Privacy Items: ", this.privacyItems));
                Unit unit = Unit.INSTANCE;
            }
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }
}
