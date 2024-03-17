package com.android.systemui.qs;

import android.app.IActivityManager;
import android.app.IForegroundServiceObserver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController extends IForegroundServiceObserver.Stub implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String LOG_TAG = FgsManagerController.class.getSimpleName();
    @NotNull
    public final IActivityManager activityManager;
    @NotNull
    public final AppListAdapter appListAdapter = new AppListAdapter();
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    public boolean changesSinceDialog;
    @NotNull
    public final Context context;
    @NotNull
    public Set<Integer> currentProfileIds = new LinkedHashSet();
    @NotNull
    public final DeviceConfigProxy deviceConfigProxy;
    @Nullable
    public SystemUIDialog dialog;
    @NotNull
    public final DialogLaunchAnimator dialogLaunchAnimator;
    @NotNull
    public final DumpManager dumpManager;
    public boolean initialized;
    public boolean isAvailable;
    public int lastNumberOfVisiblePackages;
    @NotNull
    public final Object lock = new Object();
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final Set<OnDialogDismissedListener> onDialogDismissedListeners = new LinkedHashSet();
    @NotNull
    public final Set<OnNumberOfPackagesChangedListener> onNumberOfPackagesChangedListeners = new LinkedHashSet();
    @NotNull
    public final PackageManager packageManager;
    @NotNull
    public ArrayMap<UserPackage, RunningApp> runningApps = new ArrayMap<>();
    @NotNull
    public final Map<UserPackage, StartTimeAndTokens> runningServiceTokens = new LinkedHashMap();
    public boolean showFooterDot;
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final UserTracker userTracker;
    @NotNull
    public final FgsManagerController$userTrackerCallback$1 userTrackerCallback = new FgsManagerController$userTrackerCallback$1(this);

    /* compiled from: FgsManagerController.kt */
    public interface OnDialogDismissedListener {
        void onDialogDismissed();
    }

    /* compiled from: FgsManagerController.kt */
    public interface OnNumberOfPackagesChangedListener {
        void onNumberOfPackagesChanged(int i);
    }

    /* compiled from: FgsManagerController.kt */
    public enum UIControl {
        NORMAL,
        HIDE_BUTTON,
        HIDE_ENTRY
    }

    public FgsManagerController(@NotNull Context context2, @NotNull Executor executor, @NotNull Executor executor2, @NotNull SystemClock systemClock2, @NotNull IActivityManager iActivityManager, @NotNull PackageManager packageManager2, @NotNull UserTracker userTracker2, @NotNull DeviceConfigProxy deviceConfigProxy2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull DumpManager dumpManager2) {
        this.context = context2;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        this.systemClock = systemClock2;
        this.activityManager = iActivityManager;
        this.packageManager = packageManager2;
        this.userTracker = userTracker2;
        this.deviceConfigProxy = deviceConfigProxy2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.dumpManager = dumpManager2;
    }

    /* compiled from: FgsManagerController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final boolean getChangesSinceDialog() {
        return this.changesSinceDialog;
    }

    public final boolean isAvailable() {
        return this.isAvailable;
    }

    public final boolean getShowFooterDot() {
        return this.showFooterDot;
    }

    public final boolean getInitialized() {
        return this.initialized;
    }

    public final void setInitialized(boolean z) {
        this.initialized = z;
    }

    public final void init() {
        synchronized (this.lock) {
            if (!getInitialized()) {
                try {
                    this.activityManager.registerForegroundServiceObserver(this);
                } catch (RemoteException e) {
                    e.rethrowFromSystemServer();
                }
                this.userTracker.addCallback(this.userTrackerCallback, this.backgroundExecutor);
                Set<Integer> set = this.currentProfileIds;
                Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
                ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10));
                for (UserInfo userInfo : userProfiles) {
                    arrayList.add(Integer.valueOf(userInfo.id));
                }
                set.addAll(arrayList);
                this.deviceConfigProxy.addOnPropertiesChangedListener("systemui", this.backgroundExecutor, new FgsManagerController$init$1$2(this));
                this.isAvailable = this.deviceConfigProxy.getBoolean("systemui", "task_manager_enabled", true);
                this.showFooterDot = this.deviceConfigProxy.getBoolean("systemui", "task_manager_show_footer_dot", false);
                this.dumpManager.registerDumpable(this);
                BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, new FgsManagerController$init$1$3(this), new IntentFilter("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER"), this.mainExecutor, (UserHandle) null, 4, (String) null, 40, (Object) null);
                setInitialized(true);
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0037, code lost:
        if (r4.isEmpty() == true) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onForegroundStateChanged(@org.jetbrains.annotations.NotNull android.os.IBinder r3, @org.jetbrains.annotations.NotNull java.lang.String r4, int r5, boolean r6) {
        /*
            r2 = this;
            java.lang.Object r0 = r2.lock
            monitor-enter(r0)
            com.android.systemui.qs.FgsManagerController$UserPackage r1 = new com.android.systemui.qs.FgsManagerController$UserPackage     // Catch:{ all -> 0x004a }
            r1.<init>(r5, r4)     // Catch:{ all -> 0x004a }
            if (r6 == 0) goto L_0x0022
            java.util.Map<com.android.systemui.qs.FgsManagerController$UserPackage, com.android.systemui.qs.FgsManagerController$StartTimeAndTokens> r4 = r2.runningServiceTokens     // Catch:{ all -> 0x004a }
            java.lang.Object r5 = r4.get(r1)     // Catch:{ all -> 0x004a }
            if (r5 != 0) goto L_0x001c
            com.android.systemui.qs.FgsManagerController$StartTimeAndTokens r5 = new com.android.systemui.qs.FgsManagerController$StartTimeAndTokens     // Catch:{ all -> 0x004a }
            com.android.systemui.util.time.SystemClock r6 = r2.systemClock     // Catch:{ all -> 0x004a }
            r5.<init>(r6)     // Catch:{ all -> 0x004a }
            r4.put(r1, r5)     // Catch:{ all -> 0x004a }
        L_0x001c:
            com.android.systemui.qs.FgsManagerController$StartTimeAndTokens r5 = (com.android.systemui.qs.FgsManagerController.StartTimeAndTokens) r5     // Catch:{ all -> 0x004a }
            r5.addToken(r3)     // Catch:{ all -> 0x004a }
            goto L_0x0040
        L_0x0022:
            java.util.Map<com.android.systemui.qs.FgsManagerController$UserPackage, com.android.systemui.qs.FgsManagerController$StartTimeAndTokens> r4 = r2.runningServiceTokens     // Catch:{ all -> 0x004a }
            java.lang.Object r4 = r4.get(r1)     // Catch:{ all -> 0x004a }
            com.android.systemui.qs.FgsManagerController$StartTimeAndTokens r4 = (com.android.systemui.qs.FgsManagerController.StartTimeAndTokens) r4     // Catch:{ all -> 0x004a }
            r5 = 1
            r6 = 0
            if (r4 != 0) goto L_0x0030
        L_0x002e:
            r5 = r6
            goto L_0x0039
        L_0x0030:
            r4.removeToken(r3)     // Catch:{ all -> 0x004a }
            boolean r3 = r4.isEmpty()     // Catch:{ all -> 0x004a }
            if (r3 != r5) goto L_0x002e
        L_0x0039:
            if (r5 == 0) goto L_0x0040
            java.util.Map<com.android.systemui.qs.FgsManagerController$UserPackage, com.android.systemui.qs.FgsManagerController$StartTimeAndTokens> r3 = r2.runningServiceTokens     // Catch:{ all -> 0x004a }
            r3.remove(r1)     // Catch:{ all -> 0x004a }
        L_0x0040:
            r2.updateNumberOfVisibleRunningPackagesLocked()     // Catch:{ all -> 0x004a }
            r2.updateAppItemsLocked()     // Catch:{ all -> 0x004a }
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x004a }
            monitor-exit(r0)
            return
        L_0x004a:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.FgsManagerController.onForegroundStateChanged(android.os.IBinder, java.lang.String, int, boolean):void");
    }

    @NotNull
    public final Set<OnNumberOfPackagesChangedListener> getOnNumberOfPackagesChangedListeners() {
        return this.onNumberOfPackagesChangedListeners;
    }

    @NotNull
    public final Set<OnDialogDismissedListener> getOnDialogDismissedListeners() {
        return this.onDialogDismissedListeners;
    }

    public final void addOnNumberOfPackagesChangedListener(@NotNull OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener) {
        synchronized (this.lock) {
            getOnNumberOfPackagesChangedListeners().add(onNumberOfPackagesChangedListener);
        }
    }

    public final void removeOnNumberOfPackagesChangedListener(@NotNull OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener) {
        synchronized (this.lock) {
            getOnNumberOfPackagesChangedListeners().remove(onNumberOfPackagesChangedListener);
        }
    }

    public final void addOnDialogDismissedListener(@NotNull OnDialogDismissedListener onDialogDismissedListener) {
        synchronized (this.lock) {
            getOnDialogDismissedListeners().add(onDialogDismissedListener);
        }
    }

    public final void removeOnDialogDismissedListener(@NotNull OnDialogDismissedListener onDialogDismissedListener) {
        synchronized (this.lock) {
            getOnDialogDismissedListeners().remove(onDialogDismissedListener);
        }
    }

    public final int getNumRunningPackages() {
        int numVisiblePackagesLocked;
        synchronized (this.lock) {
            numVisiblePackagesLocked = getNumVisiblePackagesLocked();
        }
        return numVisiblePackagesLocked;
    }

    public final int getNumVisiblePackagesLocked() {
        Iterable<UserPackage> keySet = this.runningServiceTokens.keySet();
        if ((keySet instanceof Collection) && ((Collection) keySet).isEmpty()) {
            return 0;
        }
        int i = 0;
        for (UserPackage userPackage : keySet) {
            if ((userPackage.getUiControl() != UIControl.HIDE_ENTRY && this.currentProfileIds.contains(Integer.valueOf(userPackage.getUserId()))) && (i = i + 1) < 0) {
                CollectionsKt__CollectionsKt.throwCountOverflow();
            }
        }
        return i;
    }

    public final void updateNumberOfVisibleRunningPackagesLocked() {
        int numVisiblePackagesLocked = getNumVisiblePackagesLocked();
        if (numVisiblePackagesLocked != this.lastNumberOfVisiblePackages) {
            this.lastNumberOfVisiblePackages = numVisiblePackagesLocked;
            this.changesSinceDialog = true;
            for (OnNumberOfPackagesChangedListener fgsManagerController$updateNumberOfVisibleRunningPackagesLocked$1$1 : this.onNumberOfPackagesChangedListeners) {
                this.backgroundExecutor.execute(new FgsManagerController$updateNumberOfVisibleRunningPackagesLocked$1$1(fgsManagerController$updateNumberOfVisibleRunningPackagesLocked$1$1, numVisiblePackagesLocked));
            }
        }
    }

    public final boolean shouldUpdateFooterVisibility() {
        return this.dialog == null;
    }

    public final void showDialog(@Nullable View view) {
        synchronized (this.lock) {
            if (this.dialog == null) {
                for (UserPackage updateUiControl : this.runningServiceTokens.keySet()) {
                    updateUiControl.updateUiControl();
                }
                SystemUIDialog systemUIDialog = new SystemUIDialog(this.context);
                systemUIDialog.setTitle(R$string.fgs_manager_dialog_title);
                systemUIDialog.setMessage(R$string.fgs_manager_dialog_message);
                Context context2 = systemUIDialog.getContext();
                RecyclerView recyclerView = new RecyclerView(context2);
                recyclerView.setLayoutManager(new LinearLayoutManager(context2));
                recyclerView.setAdapter(this.appListAdapter);
                systemUIDialog.setView(recyclerView, 0, context2.getResources().getDimensionPixelSize(R$dimen.fgs_manager_list_top_spacing), 0, 0);
                this.dialog = systemUIDialog;
                systemUIDialog.setOnDismissListener(new FgsManagerController$showDialog$1$2(this));
                this.mainExecutor.execute(new FgsManagerController$showDialog$1$3(view, systemUIDialog, this));
                this.backgroundExecutor.execute(new FgsManagerController$showDialog$1$4(this));
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void updateAppItemsLocked() {
        if (this.dialog == null) {
            this.runningApps.clear();
            return;
        }
        ArrayList<UserPackage> arrayList = new ArrayList<>();
        Iterator it = this.runningServiceTokens.keySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            UserPackage userPackage = (UserPackage) next;
            if (this.currentProfileIds.contains(Integer.valueOf(userPackage.getUserId())) && userPackage.getUiControl() != UIControl.HIDE_ENTRY) {
                RunningApp runningApp = this.runningApps.get(userPackage);
                if (!(runningApp != null && runningApp.getStopped())) {
                    z = true;
                }
            }
            if (z) {
                arrayList.add(next);
            }
        }
        ArrayList<UserPackage> arrayList2 = new ArrayList<>();
        for (Object next2 : this.runningApps.keySet()) {
            if (!this.runningServiceTokens.containsKey((UserPackage) next2)) {
                arrayList2.add(next2);
            }
        }
        for (UserPackage userPackage2 : arrayList) {
            ApplicationInfo applicationInfoAsUser = this.packageManager.getApplicationInfoAsUser(userPackage2.getPackageName(), 0, userPackage2.getUserId());
            ArrayMap<UserPackage, RunningApp> arrayMap = this.runningApps;
            int userId = userPackage2.getUserId();
            String packageName = userPackage2.getPackageName();
            StartTimeAndTokens startTimeAndTokens = this.runningServiceTokens.get(userPackage2);
            Intrinsics.checkNotNull(startTimeAndTokens);
            long startTime = startTimeAndTokens.getStartTime();
            UIControl uiControl = userPackage2.getUiControl();
            CharSequence applicationLabel = this.packageManager.getApplicationLabel(applicationInfoAsUser);
            PackageManager packageManager2 = this.packageManager;
            arrayMap.put(userPackage2, new RunningApp(userId, packageName, startTime, uiControl, applicationLabel, packageManager2.getUserBadgedIcon(packageManager2.getApplicationIcon(applicationInfoAsUser), UserHandle.of(userPackage2.getUserId()))));
            String packageName2 = userPackage2.getPackageName();
            int userId2 = userPackage2.getUserId();
            RunningApp runningApp2 = this.runningApps.get(userPackage2);
            Intrinsics.checkNotNull(runningApp2);
            logEvent(false, packageName2, userId2, runningApp2.getTimeStarted());
        }
        for (UserPackage userPackage3 : arrayList2) {
            RunningApp runningApp3 = this.runningApps.get(userPackage3);
            Intrinsics.checkNotNull(runningApp3);
            RunningApp runningApp4 = runningApp3;
            RunningApp copy$default = RunningApp.copy$default(runningApp4, 0, (String) null, 0, (UIControl) null, 15, (Object) null);
            copy$default.setStopped(true);
            copy$default.setAppLabel(runningApp4.getAppLabel());
            copy$default.setIcon(runningApp4.getIcon());
            this.runningApps.put(userPackage3, copy$default);
        }
        this.mainExecutor.execute(new FgsManagerController$updateAppItemsLocked$3(this));
    }

    public final void stopPackage(int i, String str, long j) {
        logEvent(true, str, i, j);
        this.activityManager.stopAppForUser(str, i);
    }

    public final void logEvent(boolean z, String str, int i, long j) {
        this.backgroundExecutor.execute(new FgsManagerController$logEvent$1(this, str, i, z ? 2 : 1, this.systemClock.elapsedRealtime(), j));
    }

    /* compiled from: FgsManagerController.kt */
    public final class AppListAdapter extends RecyclerView.Adapter<AppItemViewHolder> {
        @NotNull
        public List<RunningApp> data = CollectionsKt__CollectionsKt.emptyList();
        @NotNull
        public final Object lock = new Object();

        public AppListAdapter() {
        }

        @NotNull
        public AppItemViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
            return new AppItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.fgs_manager_app_item, viewGroup, false));
        }

        public void onBindViewHolder(@NotNull AppItemViewHolder appItemViewHolder, int i) {
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            synchronized (this.lock) {
                ref$ObjectRef.element = this.data.get(i);
                Unit unit = Unit.INSTANCE;
            }
            FgsManagerController fgsManagerController = FgsManagerController.this;
            appItemViewHolder.getIconView().setImageDrawable(((RunningApp) ref$ObjectRef.element).getIcon());
            appItemViewHolder.getAppLabelView().setText(((RunningApp) ref$ObjectRef.element).getAppLabel());
            appItemViewHolder.getDurationView().setText(DateUtils.formatDuration(Math.max(fgsManagerController.systemClock.elapsedRealtime() - ((RunningApp) ref$ObjectRef.element).getTimeStarted(), 60000), 20));
            appItemViewHolder.getStopButton().setOnClickListener(new FgsManagerController$AppListAdapter$onBindViewHolder$2$1(appItemViewHolder, fgsManagerController, ref$ObjectRef));
            if (((RunningApp) ref$ObjectRef.element).getUiControl() == UIControl.HIDE_BUTTON) {
                appItemViewHolder.getStopButton().setVisibility(4);
            }
            if (((RunningApp) ref$ObjectRef.element).getStopped()) {
                appItemViewHolder.getStopButton().setEnabled(false);
                appItemViewHolder.getStopButton().setText(R$string.fgs_manager_app_item_stop_button_stopped_label);
                appItemViewHolder.getDurationView().setVisibility(8);
                return;
            }
            appItemViewHolder.getStopButton().setEnabled(true);
            appItemViewHolder.getStopButton().setText(R$string.fgs_manager_app_item_stop_button_label);
            appItemViewHolder.getDurationView().setVisibility(0);
        }

        public int getItemCount() {
            return this.data.size();
        }

        public final void setData(@NotNull List<RunningApp> list) {
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = this.data;
            this.data = list;
            DiffUtil.calculateDiff(new FgsManagerController$AppListAdapter$setData$1(ref$ObjectRef, list)).dispatchUpdatesTo((RecyclerView.Adapter) this);
        }
    }

    /* compiled from: FgsManagerController.kt */
    public final class UserPackage {
        public int backgroundRestrictionExemptionReason = -1;
        @NotNull
        public final String packageName;
        @NotNull
        public UIControl uiControl = UIControl.NORMAL;
        public boolean uiControlInitialized;
        @NotNull
        public final Lazy uid$delegate;
        public final int userId;

        public UserPackage(int i, @NotNull String str) {
            this.userId = i;
            this.packageName = str;
            this.uid$delegate = LazyKt__LazyJVMKt.lazy(new FgsManagerController$UserPackage$uid$2(FgsManagerController.this, this));
        }

        public final int getUserId() {
            return this.userId;
        }

        @NotNull
        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUid() {
            return ((Number) this.uid$delegate.getValue()).intValue();
        }

        public final int getBackgroundRestrictionExemptionReason() {
            return this.backgroundRestrictionExemptionReason;
        }

        @NotNull
        public final UIControl getUiControl() {
            if (!this.uiControlInitialized) {
                updateUiControl();
            }
            return this.uiControl;
        }

        public final void updateUiControl() {
            UIControl uIControl;
            int backgroundRestrictionExemptionReason2 = FgsManagerController.this.activityManager.getBackgroundRestrictionExemptionReason(getUid());
            this.backgroundRestrictionExemptionReason = backgroundRestrictionExemptionReason2;
            if (!(backgroundRestrictionExemptionReason2 == 10 || backgroundRestrictionExemptionReason2 == 11)) {
                if (backgroundRestrictionExemptionReason2 != 51 && backgroundRestrictionExemptionReason2 != 63) {
                    if (!(backgroundRestrictionExemptionReason2 == 300 || backgroundRestrictionExemptionReason2 == 318 || backgroundRestrictionExemptionReason2 == 320 || backgroundRestrictionExemptionReason2 == 55 || backgroundRestrictionExemptionReason2 == 56)) {
                        switch (backgroundRestrictionExemptionReason2) {
                            case 322:
                            case 323:
                            case 324:
                                break;
                            default:
                                uIControl = UIControl.NORMAL;
                                break;
                        }
                    }
                } else {
                    uIControl = UIControl.HIDE_ENTRY;
                    this.uiControl = uIControl;
                    this.uiControlInitialized = true;
                }
            }
            uIControl = UIControl.HIDE_BUTTON;
            this.uiControl = uIControl;
            this.uiControlInitialized = true;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof UserPackage)) {
                return false;
            }
            UserPackage userPackage = (UserPackage) obj;
            if (!Intrinsics.areEqual((Object) userPackage.packageName, (Object) this.packageName) || userPackage.userId != this.userId) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.userId), this.packageName});
        }

        public final void dump(@NotNull PrintWriter printWriter) {
            printWriter.println("UserPackage: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println(Intrinsics.stringPlus("userId=", Integer.valueOf(getUserId())));
            printWriter.println(Intrinsics.stringPlus("packageName=", getPackageName()));
            printWriter.println("uiControl=" + getUiControl() + " (reason=" + getBackgroundRestrictionExemptionReason() + ')');
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println("]");
        }
    }

    /* compiled from: FgsManagerController.kt */
    public static final class StartTimeAndTokens {
        public final long startTime;
        @NotNull
        public final SystemClock systemClock;
        @NotNull
        public final Set<IBinder> tokens = new LinkedHashSet();

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof StartTimeAndTokens) && Intrinsics.areEqual((Object) this.systemClock, (Object) ((StartTimeAndTokens) obj).systemClock);
        }

        public int hashCode() {
            return this.systemClock.hashCode();
        }

        @NotNull
        public String toString() {
            return "StartTimeAndTokens(systemClock=" + this.systemClock + ')';
        }

        public StartTimeAndTokens(@NotNull SystemClock systemClock2) {
            this.systemClock = systemClock2;
            this.startTime = systemClock2.elapsedRealtime();
        }

        @NotNull
        public final SystemClock getSystemClock() {
            return this.systemClock;
        }

        public final long getStartTime() {
            return this.startTime;
        }

        @NotNull
        public final Set<IBinder> getTokens() {
            return this.tokens;
        }

        public final void addToken(@NotNull IBinder iBinder) {
            this.tokens.add(iBinder);
        }

        public final void removeToken(@NotNull IBinder iBinder) {
            this.tokens.remove(iBinder);
        }

        public final boolean isEmpty() {
            return this.tokens.isEmpty();
        }

        public final void dump(@NotNull PrintWriter printWriter) {
            printWriter.println("StartTimeAndTokens: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println("startTime=" + getStartTime() + " (time running = " + (getSystemClock().elapsedRealtime() - getStartTime()) + "ms)");
            printWriter.println("tokens: [");
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            for (IBinder valueOf : getTokens()) {
                printWriter.println(String.valueOf(valueOf));
            }
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println("]");
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println("]");
        }
    }

    /* compiled from: FgsManagerController.kt */
    public static final class AppItemViewHolder extends RecyclerView.ViewHolder {
        @NotNull
        public final TextView appLabelView;
        @NotNull
        public final TextView durationView;
        @NotNull
        public final ImageView iconView;
        @NotNull
        public final Button stopButton;

        public AppItemViewHolder(@NotNull View view) {
            super(view);
            this.appLabelView = (TextView) view.requireViewById(R$id.fgs_manager_app_item_label);
            this.durationView = (TextView) view.requireViewById(R$id.fgs_manager_app_item_duration);
            this.iconView = (ImageView) view.requireViewById(R$id.fgs_manager_app_item_icon);
            this.stopButton = (Button) view.requireViewById(R$id.fgs_manager_app_item_stop_button);
        }

        @NotNull
        public final TextView getAppLabelView() {
            return this.appLabelView;
        }

        @NotNull
        public final TextView getDurationView() {
            return this.durationView;
        }

        @NotNull
        public final ImageView getIconView() {
            return this.iconView;
        }

        @NotNull
        public final Button getStopButton() {
            return this.stopButton;
        }
    }

    /* compiled from: FgsManagerController.kt */
    public static final class RunningApp {
        @NotNull
        public CharSequence appLabel;
        @Nullable
        public Drawable icon;
        @NotNull
        public final String packageName;
        public boolean stopped;
        public final long timeStarted;
        @NotNull
        public final UIControl uiControl;
        public final int userId;

        public static /* synthetic */ RunningApp copy$default(RunningApp runningApp, int i, String str, long j, UIControl uIControl, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = runningApp.userId;
            }
            if ((i2 & 2) != 0) {
                str = runningApp.packageName;
            }
            String str2 = str;
            if ((i2 & 4) != 0) {
                j = runningApp.timeStarted;
            }
            long j2 = j;
            if ((i2 & 8) != 0) {
                uIControl = runningApp.uiControl;
            }
            return runningApp.copy(i, str2, j2, uIControl);
        }

        @NotNull
        public final RunningApp copy(int i, @NotNull String str, long j, @NotNull UIControl uIControl) {
            return new RunningApp(i, str, j, uIControl);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RunningApp)) {
                return false;
            }
            RunningApp runningApp = (RunningApp) obj;
            return this.userId == runningApp.userId && Intrinsics.areEqual((Object) this.packageName, (Object) runningApp.packageName) && this.timeStarted == runningApp.timeStarted && this.uiControl == runningApp.uiControl;
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.userId) * 31) + this.packageName.hashCode()) * 31) + Long.hashCode(this.timeStarted)) * 31) + this.uiControl.hashCode();
        }

        @NotNull
        public String toString() {
            return "RunningApp(userId=" + this.userId + ", packageName=" + this.packageName + ", timeStarted=" + this.timeStarted + ", uiControl=" + this.uiControl + ')';
        }

        public RunningApp(int i, @NotNull String str, long j, @NotNull UIControl uIControl) {
            this.userId = i;
            this.packageName = str;
            this.timeStarted = j;
            this.uiControl = uIControl;
            this.appLabel = "";
        }

        public final int getUserId() {
            return this.userId;
        }

        @NotNull
        public final String getPackageName() {
            return this.packageName;
        }

        public final long getTimeStarted() {
            return this.timeStarted;
        }

        @NotNull
        public final UIControl getUiControl() {
            return this.uiControl;
        }

        public RunningApp(int i, @NotNull String str, long j, @NotNull UIControl uIControl, @NotNull CharSequence charSequence, @NotNull Drawable drawable) {
            this(i, str, j, uIControl);
            this.appLabel = charSequence;
            this.icon = drawable;
        }

        @NotNull
        public final CharSequence getAppLabel() {
            return this.appLabel;
        }

        public final void setAppLabel(@NotNull CharSequence charSequence) {
            this.appLabel = charSequence;
        }

        @Nullable
        public final Drawable getIcon() {
            return this.icon;
        }

        public final void setIcon(@Nullable Drawable drawable) {
            this.icon = drawable;
        }

        public final boolean getStopped() {
            return this.stopped;
        }

        public final void setStopped(boolean z) {
            this.stopped = z;
        }

        public final void dump(@NotNull PrintWriter printWriter, @NotNull SystemClock systemClock) {
            printWriter.println("RunningApp: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println(Intrinsics.stringPlus("userId=", Integer.valueOf(getUserId())));
            printWriter.println(Intrinsics.stringPlus("packageName=", getPackageName()));
            printWriter.println("timeStarted=" + getTimeStarted() + " (time since start = " + (systemClock.elapsedRealtime() - getTimeStarted()) + "ms)");
            printWriter.println(Intrinsics.stringPlus("uiControl=", getUiControl()));
            printWriter.println(Intrinsics.stringPlus("appLabel=", getAppLabel()));
            printWriter.println(Intrinsics.stringPlus("icon=", getIcon()));
            printWriter.println(Intrinsics.stringPlus("stopped=", Boolean.valueOf(getStopped())));
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println("]");
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        synchronized (this.lock) {
            indentingPrintWriter.println(Intrinsics.stringPlus("current user profiles = ", this.currentProfileIds));
            indentingPrintWriter.println(Intrinsics.stringPlus("changesSinceDialog=", Boolean.valueOf(getChangesSinceDialog())));
            indentingPrintWriter.println("Running service tokens: [");
            indentingPrintWriter.increaseIndent();
            for (Map.Entry next : this.runningServiceTokens.entrySet()) {
                indentingPrintWriter.println("{");
                indentingPrintWriter.increaseIndent();
                ((UserPackage) next.getKey()).dump(indentingPrintWriter);
                ((StartTimeAndTokens) next.getValue()).dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
            indentingPrintWriter.println("Loaded package UI info: [");
            indentingPrintWriter.increaseIndent();
            for (Map.Entry next2 : this.runningApps.entrySet()) {
                indentingPrintWriter.println("{");
                indentingPrintWriter.increaseIndent();
                ((UserPackage) next2.getKey()).dump(indentingPrintWriter);
                ((RunningApp) next2.getValue()).dump(indentingPrintWriter, this.systemClock);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
            Unit unit = Unit.INSTANCE;
        }
    }
}
