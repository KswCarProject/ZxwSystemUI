package com.android.systemui.qs;

import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AutoAddTracker.kt */
public final class AutoAddTracker implements UserAwareController, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final IntentFilter FILTER = new IntentFilter("android.os.action.SETTING_RESTORED");
    @NotNull
    public final ArraySet<String> autoAdded = new ArraySet<>();
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final AutoAddTracker$contentObserver$1 contentObserver;
    @NotNull
    public final DumpManager dumpManager;
    @Nullable
    public final Handler mainHandler;
    @NotNull
    public final QSHost qsHost;
    @NotNull
    public final AutoAddTracker$restoreReceiver$1 restoreReceiver;
    @Nullable
    public Set<String> restoredTiles;
    @NotNull
    public final SecureSettings secureSettings;
    public int userId;

    public AutoAddTracker(@NotNull SecureSettings secureSettings2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull QSHost qSHost, @NotNull DumpManager dumpManager2, @Nullable Handler handler, @NotNull Executor executor, int i) {
        this.secureSettings = secureSettings2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.qsHost = qSHost;
        this.dumpManager = dumpManager2;
        this.mainHandler = handler;
        this.backgroundExecutor = executor;
        this.userId = i;
        this.contentObserver = new AutoAddTracker$contentObserver$1(this, handler);
        this.restoreReceiver = new AutoAddTracker$restoreReceiver$1(this);
    }

    /* compiled from: AutoAddTracker.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void processRestoreIntent(android.content.Intent r11) {
        /*
            r10 = this;
            java.lang.String r0 = "setting_name"
            java.lang.String r0 = r11.getStringExtra(r0)
            java.lang.String r1 = "sysui_qs_tiles"
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r1)
            r2 = 0
            if (r1 == 0) goto L_0x004c
            java.lang.String r0 = "new_value"
            java.lang.String r3 = r11.getStringExtra(r0)
            if (r3 != 0) goto L_0x001a
            goto L_0x0031
        L_0x001a:
            java.lang.String r11 = ","
            java.lang.String[] r4 = new java.lang.String[]{r11}
            r5 = 0
            r6 = 0
            r7 = 6
            r8 = 0
            java.util.List r11 = kotlin.text.StringsKt__StringsKt.split$default(r3, r4, r5, r6, r7, r8)
            if (r11 != 0) goto L_0x002b
            goto L_0x0031
        L_0x002b:
            java.lang.Iterable r11 = (java.lang.Iterable) r11
            java.util.Set r2 = kotlin.collections.CollectionsKt___CollectionsKt.toSet(r11)
        L_0x0031:
            if (r2 != 0) goto L_0x0048
            java.lang.String r11 = "AutoAddTracker"
            java.lang.String r0 = "Null restored tiles for user "
            int r1 = r10.userId
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r1)
            android.util.Log.w(r11, r0)
            java.util.Set r2 = kotlin.collections.SetsKt__SetsKt.emptySet()
        L_0x0048:
            r10.restoredTiles = r2
            goto L_0x00ff
        L_0x004c:
            java.lang.String r1 = "qs_auto_tiles"
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r1)
            if (r0 == 0) goto L_0x00ff
            java.util.Set<java.lang.String> r0 = r10.restoredTiles
            if (r0 != 0) goto L_0x005a
            goto L_0x00e8
        L_0x005a:
            java.lang.String r1 = "new_value"
            java.lang.String r3 = r11.getStringExtra(r1)
            if (r3 != 0) goto L_0x0064
            r1 = r2
            goto L_0x0072
        L_0x0064:
            java.lang.String r1 = ","
            java.lang.String[] r4 = new java.lang.String[]{r1}
            r5 = 0
            r6 = 0
            r7 = 6
            r8 = 0
            java.util.List r1 = kotlin.text.StringsKt__StringsKt.split$default(r3, r4, r5, r6, r7, r8)
        L_0x0072:
            if (r1 != 0) goto L_0x0078
            java.util.List r1 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
        L_0x0078:
            java.lang.String r3 = "previous_value"
            java.lang.String r4 = r11.getStringExtra(r3)
            if (r4 != 0) goto L_0x0081
            goto L_0x008f
        L_0x0081:
            java.lang.String r11 = ","
            java.lang.String[] r5 = new java.lang.String[]{r11}
            r6 = 0
            r7 = 0
            r8 = 6
            r9 = 0
            java.util.List r2 = kotlin.text.StringsKt__StringsKt.split$default(r4, r5, r6, r7, r8, r9)
        L_0x008f:
            if (r2 != 0) goto L_0x0095
            java.util.List r2 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
        L_0x0095:
            r11 = r1
            java.lang.Iterable r11 = (java.lang.Iterable) r11
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.Iterator r11 = r11.iterator()
        L_0x00a1:
            boolean r4 = r11.hasNext()
            if (r4 == 0) goto L_0x00ba
            java.lang.Object r4 = r11.next()
            r5 = r4
            java.lang.String r5 = (java.lang.String) r5
            boolean r5 = r0.contains(r5)
            r5 = r5 ^ 1
            if (r5 == 0) goto L_0x00a1
            r3.add(r4)
            goto L_0x00a1
        L_0x00ba:
            boolean r11 = r3.isEmpty()
            r11 = r11 ^ 1
            if (r11 == 0) goto L_0x00c7
            com.android.systemui.qs.QSHost r11 = r10.qsHost
            r11.removeTiles(r3)
        L_0x00c7:
            android.util.ArraySet<java.lang.String> r11 = r10.autoAdded
            monitor-enter(r11)
            android.util.ArraySet<java.lang.String> r0 = r10.autoAdded     // Catch:{ all -> 0x00fc }
            r0.clear()     // Catch:{ all -> 0x00fc }
            android.util.ArraySet<java.lang.String> r0 = r10.autoAdded     // Catch:{ all -> 0x00fc }
            java.util.Collection r1 = (java.util.Collection) r1     // Catch:{ all -> 0x00fc }
            java.lang.Iterable r2 = (java.lang.Iterable) r2     // Catch:{ all -> 0x00fc }
            java.util.List r1 = kotlin.collections.CollectionsKt___CollectionsKt.plus(r1, r2)     // Catch:{ all -> 0x00fc }
            java.util.Collection r1 = (java.util.Collection) r1     // Catch:{ all -> 0x00fc }
            r0.addAll(r1)     // Catch:{ all -> 0x00fc }
            java.lang.String r0 = r10.getTilesFromListLocked()     // Catch:{ all -> 0x00fc }
            monitor-exit(r11)
            r10.saveTiles(r0)
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
        L_0x00e8:
            if (r2 != 0) goto L_0x00ff
            java.lang.String r11 = "AutoAddTracker"
            java.lang.String r0 = "qs_auto_tiles restored before sysui_qs_tiles for user "
            int r10 = r10.userId
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            java.lang.String r10 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r10)
            android.util.Log.w(r11, r10)
            goto L_0x00ff
        L_0x00fc:
            r10 = move-exception
            monitor-exit(r11)
            throw r10
        L_0x00ff:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.AutoAddTracker.processRestoreIntent(android.content.Intent):void");
    }

    public final void initialize() {
        this.dumpManager.registerDumpable("AutoAddTracker", this);
        loadTiles();
        SecureSettings secureSettings2 = this.secureSettings;
        secureSettings2.registerContentObserverForUser(secureSettings2.getUriFor("qs_auto_tiles"), (ContentObserver) this.contentObserver, -1);
        registerBroadcastReceiver();
    }

    public final void registerBroadcastReceiver() {
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.restoreReceiver, FILTER, this.backgroundExecutor, UserHandle.of(this.userId), 0, (String) null, 48, (Object) null);
    }

    public final void unregisterBroadcastReceiver() {
        this.broadcastDispatcher.unregisterReceiver(this.restoreReceiver);
    }

    public void changeUser(@NotNull UserHandle userHandle) {
        if (userHandle.getIdentifier() != this.userId) {
            unregisterBroadcastReceiver();
            this.userId = userHandle.getIdentifier();
            this.restoredTiles = null;
            loadTiles();
            registerBroadcastReceiver();
        }
    }

    public final boolean isAdded(@NotNull String str) {
        boolean contains;
        synchronized (this.autoAdded) {
            contains = this.autoAdded.contains(str);
        }
        return contains;
    }

    public final void setTileAdded(@NotNull String str) {
        String tilesFromListLocked;
        synchronized (this.autoAdded) {
            tilesFromListLocked = this.autoAdded.add(str) ? getTilesFromListLocked() : null;
        }
        if (tilesFromListLocked != null) {
            saveTiles(tilesFromListLocked);
        }
    }

    public final void setTileRemoved(@NotNull String str) {
        String tilesFromListLocked;
        synchronized (this.autoAdded) {
            tilesFromListLocked = this.autoAdded.remove(str) ? getTilesFromListLocked() : null;
        }
        if (tilesFromListLocked != null) {
            saveTiles(tilesFromListLocked);
        }
    }

    public final String getTilesFromListLocked() {
        return TextUtils.join(",", this.autoAdded);
    }

    public final void saveTiles(String str) {
        this.secureSettings.putStringForUser("qs_auto_tiles", str, (String) null, false, this.userId, true);
    }

    public final void loadTiles() {
        synchronized (this.autoAdded) {
            this.autoAdded.clear();
            this.autoAdded.addAll(getAdded());
        }
    }

    public final Collection<String> getAdded() {
        Collection<String> collection;
        String stringForUser = this.secureSettings.getStringForUser("qs_auto_tiles", this.userId);
        if (stringForUser == null) {
            collection = null;
        } else {
            collection = StringsKt__StringsKt.split$default(stringForUser, new String[]{","}, false, 0, 6, (Object) null);
        }
        if (collection == null) {
            collection = SetsKt__SetsKt.emptySet();
        }
        return collection;
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Current user: ", Integer.valueOf(this.userId)));
        printWriter.println(Intrinsics.stringPlus("Added tiles: ", this.autoAdded));
    }

    /* compiled from: AutoAddTracker.kt */
    public static final class Builder {
        @NotNull
        public final BroadcastDispatcher broadcastDispatcher;
        @NotNull
        public final DumpManager dumpManager;
        @NotNull
        public final Executor executor;
        @NotNull
        public final Handler handler;
        @NotNull
        public final QSHost qsHost;
        @NotNull
        public final SecureSettings secureSettings;
        public int userId;

        public Builder(@NotNull SecureSettings secureSettings2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull QSHost qSHost, @NotNull DumpManager dumpManager2, @NotNull Handler handler2, @NotNull Executor executor2) {
            this.secureSettings = secureSettings2;
            this.broadcastDispatcher = broadcastDispatcher2;
            this.qsHost = qSHost;
            this.dumpManager = dumpManager2;
            this.handler = handler2;
            this.executor = executor2;
        }

        @NotNull
        public final Builder setUserId(int i) {
            this.userId = i;
            return this;
        }

        @NotNull
        public final AutoAddTracker build() {
            return new AutoAddTracker(this.secureSettings, this.broadcastDispatcher, this.qsHost, this.dumpManager, this.handler, this.executor, this.userId);
        }
    }
}
