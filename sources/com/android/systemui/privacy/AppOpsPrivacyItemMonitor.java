package com.android.systemui.privacy;

import android.content.pm.UserInfo;
import android.util.IndentingPrintWriter;
import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.util.ArrayList;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor implements PrivacyItemMonitor {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final int[] OPS;
    @NotNull
    public static final int[] OPS_LOCATION;
    @NotNull
    public static final int[] OPS_MIC_CAMERA;
    @NotNull
    public static final int[] USER_INDEPENDENT_OPS = {R$styleable.Constraint_layout_goneMarginRight, 100};
    @NotNull
    public final AppOpsPrivacyItemMonitor$appOpsCallback$1 appOpsCallback;
    @NotNull
    public final AppOpsController appOpsController;
    @NotNull
    public final DelayableExecutor bgExecutor;
    @GuardedBy({"lock"})
    @Nullable
    public PrivacyItemMonitor.Callback callback;
    @NotNull
    public final AppOpsPrivacyItemMonitor$configCallback$1 configCallback;
    @GuardedBy({"lock"})
    public boolean listening;
    @GuardedBy({"lock"})
    public boolean locationAvailable;
    @NotNull
    public final Object lock = new Object();
    @NotNull
    public final PrivacyLogger logger;
    @GuardedBy({"lock"})
    public boolean micCameraAvailable;
    @NotNull
    public final PrivacyConfig privacyConfig;
    @NotNull
    public final UserTracker userTracker;
    @NotNull
    public final UserTracker.Callback userTrackerCallback;

    @VisibleForTesting
    public static /* synthetic */ void getUserTrackerCallback$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public AppOpsPrivacyItemMonitor(@NotNull AppOpsController appOpsController2, @NotNull UserTracker userTracker2, @NotNull PrivacyConfig privacyConfig2, @NotNull DelayableExecutor delayableExecutor, @NotNull PrivacyLogger privacyLogger) {
        this.appOpsController = appOpsController2;
        this.userTracker = userTracker2;
        this.privacyConfig = privacyConfig2;
        this.bgExecutor = delayableExecutor;
        this.logger = privacyLogger;
        this.micCameraAvailable = privacyConfig2.getMicCameraAvailable();
        this.locationAvailable = privacyConfig2.getLocationAvailable();
        this.appOpsCallback = new AppOpsPrivacyItemMonitor$appOpsCallback$1(this);
        this.userTrackerCallback = new AppOpsPrivacyItemMonitor$userTrackerCallback$1(this);
        AppOpsPrivacyItemMonitor$configCallback$1 appOpsPrivacyItemMonitor$configCallback$1 = new AppOpsPrivacyItemMonitor$configCallback$1(this);
        this.configCallback = appOpsPrivacyItemMonitor$configCallback$1;
        privacyConfig2.addCallback((PrivacyConfig.Callback) appOpsPrivacyItemMonitor$configCallback$1);
    }

    @VisibleForTesting
    /* compiled from: AppOpsPrivacyItemMonitor.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final int[] getOPS_MIC_CAMERA() {
            return AppOpsPrivacyItemMonitor.OPS_MIC_CAMERA;
        }

        @NotNull
        public final int[] getOPS_LOCATION() {
            return AppOpsPrivacyItemMonitor.OPS_LOCATION;
        }

        @NotNull
        public final int[] getUSER_INDEPENDENT_OPS() {
            return AppOpsPrivacyItemMonitor.USER_INDEPENDENT_OPS;
        }
    }

    static {
        int[] iArr = {26, R$styleable.Constraint_layout_goneMarginRight, 27, 100, androidx.appcompat.R$styleable.AppCompatTheme_windowFixedHeightMajor};
        OPS_MIC_CAMERA = iArr;
        int[] iArr2 = {0, 1};
        OPS_LOCATION = iArr2;
        OPS = ArraysKt___ArraysJvmKt.plus(iArr, iArr2);
    }

    public void startListening(@NotNull PrivacyItemMonitor.Callback callback2) {
        synchronized (this.lock) {
            this.callback = callback2;
            setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    public void stopListening() {
        synchronized (this.lock) {
            this.callback = null;
            setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    @GuardedBy({"lock"})
    public final void setListeningStateLocked() {
        boolean z = this.callback != null && (this.micCameraAvailable || this.locationAvailable);
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.appOpsController.addCallback(OPS, this.appOpsCallback);
                this.userTracker.addCallback(this.userTrackerCallback, this.bgExecutor);
                onCurrentProfilesChanged();
                return;
            }
            this.appOpsController.removeCallback(OPS, this.appOpsCallback);
            this.userTracker.removeCallback(this.userTrackerCallback);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x001b A[SYNTHETIC] */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.android.systemui.privacy.PrivacyItem> getActivePrivacyItems() {
        /*
            r11 = this;
            com.android.systemui.appops.AppOpsController r0 = r11.appOpsController
            r1 = 1
            java.util.List r0 = r0.getActiveAppOps(r1)
            com.android.systemui.settings.UserTracker r2 = r11.userTracker
            java.util.List r2 = r2.getUserProfiles()
            java.lang.Object r3 = r11.lock
            monitor-enter(r3)
            java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ all -> 0x0098 }
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ all -> 0x0098 }
            r4.<init>()     // Catch:{ all -> 0x0098 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0098 }
        L_0x001b:
            boolean r5 = r0.hasNext()     // Catch:{ all -> 0x0098 }
            if (r5 == 0) goto L_0x0072
            java.lang.Object r5 = r0.next()     // Catch:{ all -> 0x0098 }
            r6 = r5
            com.android.systemui.appops.AppOpItem r6 = (com.android.systemui.appops.AppOpItem) r6     // Catch:{ all -> 0x0098 }
            r7 = r2
            java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ all -> 0x0098 }
            boolean r8 = r7 instanceof java.util.Collection     // Catch:{ all -> 0x0098 }
            r9 = 0
            if (r8 == 0) goto L_0x003b
            r8 = r7
            java.util.Collection r8 = (java.util.Collection) r8     // Catch:{ all -> 0x0098 }
            boolean r8 = r8.isEmpty()     // Catch:{ all -> 0x0098 }
            if (r8 == 0) goto L_0x003b
        L_0x0039:
            r7 = r9
            goto L_0x005d
        L_0x003b:
            java.util.Iterator r7 = r7.iterator()     // Catch:{ all -> 0x0098 }
        L_0x003f:
            boolean r8 = r7.hasNext()     // Catch:{ all -> 0x0098 }
            if (r8 == 0) goto L_0x0039
            java.lang.Object r8 = r7.next()     // Catch:{ all -> 0x0098 }
            android.content.pm.UserInfo r8 = (android.content.pm.UserInfo) r8     // Catch:{ all -> 0x0098 }
            int r8 = r8.id     // Catch:{ all -> 0x0098 }
            int r10 = r6.getUid()     // Catch:{ all -> 0x0098 }
            int r10 = android.os.UserHandle.getUserId(r10)     // Catch:{ all -> 0x0098 }
            if (r8 != r10) goto L_0x0059
            r8 = r1
            goto L_0x005a
        L_0x0059:
            r8 = r9
        L_0x005a:
            if (r8 == 0) goto L_0x003f
            r7 = r1
        L_0x005d:
            if (r7 != 0) goto L_0x006b
            int[] r7 = USER_INDEPENDENT_OPS     // Catch:{ all -> 0x0098 }
            int r6 = r6.getCode()     // Catch:{ all -> 0x0098 }
            boolean r6 = kotlin.collections.ArraysKt___ArraysKt.contains((int[]) r7, (int) r6)     // Catch:{ all -> 0x0098 }
            if (r6 == 0) goto L_0x006c
        L_0x006b:
            r9 = r1
        L_0x006c:
            if (r9 == 0) goto L_0x001b
            r4.add(r5)     // Catch:{ all -> 0x0098 }
            goto L_0x001b
        L_0x0072:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0098 }
            r0.<init>()     // Catch:{ all -> 0x0098 }
            java.util.Iterator r1 = r4.iterator()     // Catch:{ all -> 0x0098 }
        L_0x007b:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0092
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0098 }
            com.android.systemui.appops.AppOpItem r2 = (com.android.systemui.appops.AppOpItem) r2     // Catch:{ all -> 0x0098 }
            com.android.systemui.privacy.PrivacyItem r2 = r11.toPrivacyItemLocked(r2)     // Catch:{ all -> 0x0098 }
            if (r2 != 0) goto L_0x008e
            goto L_0x007b
        L_0x008e:
            r0.add(r2)     // Catch:{ all -> 0x0098 }
            goto L_0x007b
        L_0x0092:
            monitor-exit(r3)
            java.util.List r11 = kotlin.collections.CollectionsKt___CollectionsKt.distinct(r0)
            return r11
        L_0x0098:
            r11 = move-exception
            monitor-exit(r3)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.AppOpsPrivacyItemMonitor.getActivePrivacyItems():java.util.List");
    }

    @GuardedBy({"lock"})
    public final boolean privacyItemForAppOpEnabledLocked(int i) {
        if (ArraysKt___ArraysKt.contains(OPS_LOCATION, i)) {
            return this.locationAvailable;
        }
        if (ArraysKt___ArraysKt.contains(OPS_MIC_CAMERA, i)) {
            return this.micCameraAvailable;
        }
        return false;
    }

    @GuardedBy({"lock"})
    public final PrivacyItem toPrivacyItemLocked(AppOpItem appOpItem) {
        PrivacyType privacyType;
        if (!privacyItemForAppOpEnabledLocked(appOpItem.getCode())) {
            return null;
        }
        int code = appOpItem.getCode();
        if (code == 0 || code == 1) {
            privacyType = PrivacyType.TYPE_LOCATION;
        } else {
            if (code != 26) {
                if (!(code == 27 || code == 100)) {
                    if (code != 101) {
                        if (code != 120) {
                            return null;
                        }
                    }
                }
                privacyType = PrivacyType.TYPE_MICROPHONE;
            }
            privacyType = PrivacyType.TYPE_CAMERA;
        }
        return new PrivacyItem(privacyType, new PrivacyApplication(appOpItem.getPackageName(), appOpItem.getUid()), appOpItem.getTimeStartedElapsed(), appOpItem.isDisabled());
    }

    public final void onCurrentProfilesChanged() {
        Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10));
        for (UserInfo userInfo : userProfiles) {
            arrayList.add(Integer.valueOf(userInfo.id));
        }
        this.logger.logCurrentProfilesChanged(arrayList);
        dispatchOnPrivacyItemsChanged();
    }

    public final void dispatchOnPrivacyItemsChanged() {
        PrivacyItemMonitor.Callback callback2;
        synchronized (this.lock) {
            callback2 = this.callback;
        }
        if (callback2 != null) {
            this.bgExecutor.execute(new AppOpsPrivacyItemMonitor$dispatchOnPrivacyItemsChanged$1(callback2));
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("AppOpsPrivacyItemMonitor:");
        asIndenting.increaseIndent();
        try {
            synchronized (this.lock) {
                asIndenting.println(Intrinsics.stringPlus("Listening: ", Boolean.valueOf(this.listening)));
                asIndenting.println(Intrinsics.stringPlus("micCameraAvailable: ", Boolean.valueOf(this.micCameraAvailable)));
                asIndenting.println(Intrinsics.stringPlus("locationAvailable: ", Boolean.valueOf(this.locationAvailable)));
                asIndenting.println(Intrinsics.stringPlus("Callback: ", this.callback));
                Unit unit = Unit.INSTANCE;
            }
            Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo userInfo : userProfiles) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            asIndenting.println(Intrinsics.stringPlus("Current user ids: ", arrayList));
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }
}
