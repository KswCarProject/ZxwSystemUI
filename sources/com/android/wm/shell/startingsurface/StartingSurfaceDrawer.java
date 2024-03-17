package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.TaskInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.Display;
import android.view.SurfaceControlViewHost;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.widget.FrameLayout;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskSnapshot;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ContrastColorUtil;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.startingsurface.StartingSurface;
import java.util.function.Supplier;

public class StartingSurfaceDrawer {
    public final SparseArray<SurfaceControlViewHost> mAnimatedSplashScreenSurfaceHosts = new SparseArray<>(1);
    public Choreographer mChoreographer;
    public final Context mContext;
    public final DisplayManager mDisplayManager;
    public final ShellExecutor mSplashScreenExecutor;
    @VisibleForTesting
    public final SplashscreenContentDrawer mSplashscreenContentDrawer;
    @VisibleForTesting
    public final SparseArray<StartingWindowRecord> mStartingWindowRecords = new SparseArray<>();
    public StartingSurface.SysuiProxy mSysuiProxy;
    public final StartingWindowRemovalInfo mTmpRemovalInfo = new StartingWindowRemovalInfo();
    public final WindowManagerGlobal mWindowManagerGlobal;

    public StartingSurfaceDrawer(Context context, ShellExecutor shellExecutor, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayManager = displayManager;
        this.mSplashScreenExecutor = shellExecutor;
        this.mSplashscreenContentDrawer = new SplashscreenContentDrawer(context, iconProvider, transactionPool);
        shellExecutor.execute(new StartingSurfaceDrawer$$ExternalSyntheticLambda5(this));
        this.mWindowManagerGlobal = WindowManagerGlobal.getInstance();
        displayManager.getDisplay(0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = Choreographer.getInstance();
    }

    public final Display getDisplay(int i) {
        return this.mDisplayManager.getDisplay(i);
    }

    public int getSplashScreenTheme(int i, ActivityInfo activityInfo) {
        if (i != 0) {
            return i;
        }
        if (activityInfo.getThemeResource() != 0) {
            return activityInfo.getThemeResource();
        }
        return 16974563;
    }

    public void setSysuiProxy(StartingSurface.SysuiProxy sysuiProxy) {
        this.mSysuiProxy = sysuiProxy;
    }

    public void addSplashScreenStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder, @StartingWindowInfo.StartingWindowType int i) {
        int i2;
        int i3;
        StartingWindowInfo startingWindowInfo2 = startingWindowInfo;
        int i4 = i;
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo2.taskInfo;
        ActivityInfo activityInfo = startingWindowInfo2.targetActivityInfo;
        if (activityInfo == null) {
            activityInfo = runningTaskInfo.topActivityInfo;
        }
        if (activityInfo != null && activityInfo.packageName != null) {
            int i5 = runningTaskInfo.displayId;
            int i6 = runningTaskInfo.taskId;
            int splashScreenTheme = getSplashScreenTheme(startingWindowInfo2.splashScreenThemeResId, activityInfo);
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -1859822367, 80, (String) null, String.valueOf(activityInfo.packageName), String.valueOf(Integer.toHexString(splashScreenTheme)), Long.valueOf((long) i6), Long.valueOf((long) i4));
            }
            Display display = getDisplay(i5);
            if (display != null) {
                Context createDisplayContext = i5 == 0 ? this.mContext : this.mContext.createDisplayContext(display);
                if (createDisplayContext != null) {
                    if (splashScreenTheme != createDisplayContext.getThemeResId()) {
                        try {
                            createDisplayContext = createDisplayContext.createPackageContextAsUser(activityInfo.packageName, 4, UserHandle.of(runningTaskInfo.userId));
                            createDisplayContext.setTheme(splashScreenTheme);
                        } catch (PackageManager.NameNotFoundException e) {
                            Slog.w("ShellStartingWindow", "Failed creating package context with package name " + activityInfo.packageName + " for user " + runningTaskInfo.userId, e);
                            return;
                        }
                    }
                    Configuration configuration = runningTaskInfo.getConfiguration();
                    if (configuration.diffPublicOnly(createDisplayContext.getResources().getConfiguration()) != 0) {
                        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                            i2 = 1;
                            i3 = 0;
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 301074142, 0, (String) null, String.valueOf(configuration));
                        } else {
                            i3 = 0;
                            i2 = 1;
                        }
                        Context createConfigurationContext = createDisplayContext.createConfigurationContext(configuration);
                        createConfigurationContext.setTheme(splashScreenTheme);
                        TypedArray obtainStyledAttributes = createConfigurationContext.obtainStyledAttributes(R.styleable.Window);
                        int resourceId = obtainStyledAttributes.getResourceId(i2, i3);
                        if (resourceId != 0) {
                            try {
                                if (createConfigurationContext.getDrawable(resourceId) != null) {
                                    if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1726817767, 0, (String) null, String.valueOf(configuration));
                                    }
                                    createDisplayContext = createConfigurationContext;
                                }
                            } catch (Resources.NotFoundException e2) {
                                Slog.w("ShellStartingWindow", "failed creating starting window for overrideConfig at taskId: " + i6, e2);
                                return;
                            }
                        }
                        obtainStyledAttributes.recycle();
                    }
                    Context context = createDisplayContext;
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(3);
                    layoutParams.setFitInsetsSides(0);
                    layoutParams.setFitInsetsTypes(0);
                    layoutParams.format = -3;
                    int i7 = 16843008;
                    TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(R.styleable.Window);
                    if (obtainStyledAttributes2.getBoolean(14, false)) {
                        i7 = 17891584;
                    }
                    if (i4 != 4 || obtainStyledAttributes2.getBoolean(33, false)) {
                        i7 |= Integer.MIN_VALUE;
                    }
                    layoutParams.layoutInDisplayCutoutMode = obtainStyledAttributes2.getInt(50, layoutParams.layoutInDisplayCutoutMode);
                    layoutParams.windowAnimations = obtainStyledAttributes2.getResourceId(8, 0);
                    obtainStyledAttributes2.recycle();
                    StartingWindowInfo startingWindowInfo3 = startingWindowInfo;
                    if (i5 == 0 && startingWindowInfo3.isKeyguardOccluded) {
                        i7 |= 524288;
                    }
                    layoutParams.flags = 131096 | i7;
                    layoutParams.token = iBinder;
                    layoutParams.packageName = activityInfo.packageName;
                    layoutParams.privateFlags |= 16;
                    if (!context.getResources().getCompatibilityInfo().supportsScreen()) {
                        layoutParams.privateFlags |= 128;
                    }
                    layoutParams.setTitle("Splash Screen " + activityInfo.packageName);
                    SplashScreenViewSupplier splashScreenViewSupplier = new SplashScreenViewSupplier();
                    FrameLayout frameLayout = new FrameLayout(this.mSplashscreenContentDrawer.createViewContextWrapper(context));
                    frameLayout.setPadding(0, 0, 0, 0);
                    frameLayout.setFitsSystemWindows(false);
                    StartingSurfaceDrawer$$ExternalSyntheticLambda0 startingSurfaceDrawer$$ExternalSyntheticLambda0 = r1;
                    FrameLayout frameLayout2 = frameLayout;
                    StartingSurfaceDrawer$$ExternalSyntheticLambda0 startingSurfaceDrawer$$ExternalSyntheticLambda02 = new StartingSurfaceDrawer$$ExternalSyntheticLambda0(this, splashScreenViewSupplier, i6, iBinder, frameLayout);
                    StartingSurface.SysuiProxy sysuiProxy = this.mSysuiProxy;
                    if (sysuiProxy != null) {
                        sysuiProxy.requestTopUi(true, "ShellStartingWindow");
                    }
                    this.mSplashscreenContentDrawer.createContentView(context, i, startingWindowInfo, new StartingSurfaceDrawer$$ExternalSyntheticLambda1(splashScreenViewSupplier), new StartingSurfaceDrawer$$ExternalSyntheticLambda2(splashScreenViewSupplier));
                    try {
                        if (addWindow(i6, iBinder, frameLayout2, display, layoutParams, i)) {
                            this.mChoreographer.postCallback(2, startingSurfaceDrawer$$ExternalSyntheticLambda0, (Object) null);
                            StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i6);
                            startingWindowRecord.parseAppSystemBarColor(context);
                            final SplashScreenView splashScreenView = splashScreenViewSupplier.get();
                            if (i4 != 4) {
                                splashScreenView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                                    public void onViewDetachedFromWindow(View view) {
                                    }

                                    public void onViewAttachedToWindow(View view) {
                                        splashScreenView.getWindowInsetsController().setSystemBarsAppearance(ContrastColorUtil.isColorLight(splashScreenView.getInitBackgroundColor()) ? 24 : 0, 24);
                                    }
                                });
                            }
                            startingWindowRecord.mBGColor = splashScreenView.getInitBackgroundColor();
                            return;
                        }
                        SplashScreenView splashScreenView2 = splashScreenViewSupplier.get();
                        if (splashScreenView2.getSurfaceHost() != null) {
                            SplashScreenView.releaseIconHost(splashScreenView2.getSurfaceHost());
                        }
                    } catch (RuntimeException e3) {
                        Slog.w("ShellStartingWindow", "failed creating starting window at taskId: " + i6, e3);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addSplashScreenStartingWindow$1(SplashScreenViewSupplier splashScreenViewSupplier, int i, IBinder iBinder, FrameLayout frameLayout) {
        Trace.traceBegin(32, "addSplashScreenView");
        SplashScreenView splashScreenView = splashScreenViewSupplier.get();
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && iBinder == startingWindowRecord.mAppToken) {
            if (splashScreenView != null) {
                try {
                    frameLayout.addView(splashScreenView);
                } catch (RuntimeException e) {
                    Slog.w("ShellStartingWindow", "failed set content view to starting window at taskId: " + i, e);
                    splashScreenView = null;
                }
            }
            startingWindowRecord.setSplashScreenView(splashScreenView);
        }
        Trace.traceEnd(32);
    }

    public int getStartingWindowBackgroundColorForTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord == null) {
            return 0;
        }
        return startingWindowRecord.mBGColor;
    }

    public static class SplashScreenViewSupplier implements Supplier<SplashScreenView> {
        public boolean mIsViewSet;
        public Runnable mUiThreadInitTask;
        public SplashScreenView mView;

        public SplashScreenViewSupplier() {
        }

        public void setView(SplashScreenView splashScreenView) {
            synchronized (this) {
                this.mView = splashScreenView;
                this.mIsViewSet = true;
                notify();
            }
        }

        public void setUiThreadInitTask(Runnable runnable) {
            synchronized (this) {
                this.mUiThreadInitTask = runnable;
            }
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
        /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:16:0x0001, LOOP_START, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.window.SplashScreenView get() {
            /*
                r1 = this;
                monitor-enter(r1)
            L_0x0001:
                boolean r0 = r1.mIsViewSet     // Catch:{ all -> 0x0017 }
                if (r0 != 0) goto L_0x0009
                r1.wait()     // Catch:{ InterruptedException -> 0x0001 }
                goto L_0x0001
            L_0x0009:
                java.lang.Runnable r0 = r1.mUiThreadInitTask     // Catch:{ all -> 0x0017 }
                if (r0 == 0) goto L_0x0013
                r0.run()     // Catch:{ all -> 0x0017 }
                r0 = 0
                r1.mUiThreadInitTask = r0     // Catch:{ all -> 0x0017 }
            L_0x0013:
                android.window.SplashScreenView r0 = r1.mView     // Catch:{ all -> 0x0017 }
                monitor-exit(r1)     // Catch:{ all -> 0x0017 }
                return r0
            L_0x0017:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0017 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.StartingSurfaceDrawer.SplashScreenViewSupplier.get():android.window.SplashScreenView");
        }
    }

    public int estimateTaskBackgroundColor(TaskInfo taskInfo) {
        ActivityInfo activityInfo = taskInfo.topActivityInfo;
        if (activityInfo == null) {
            return 0;
        }
        String str = activityInfo.packageName;
        int i = taskInfo.userId;
        try {
            Context createPackageContextAsUser = this.mContext.createPackageContextAsUser(str, 4, UserHandle.of(i));
            try {
                String splashScreenTheme = ActivityThread.getPackageManager().getSplashScreenTheme(str, i);
                int splashScreenTheme2 = getSplashScreenTheme(splashScreenTheme != null ? createPackageContextAsUser.getResources().getIdentifier(splashScreenTheme, (String) null, (String) null) : 0, activityInfo);
                if (splashScreenTheme2 != createPackageContextAsUser.getThemeResId()) {
                    createPackageContextAsUser.setTheme(splashScreenTheme2);
                }
                return this.mSplashscreenContentDrawer.estimateTaskBackgroundColor(createPackageContextAsUser);
            } catch (RemoteException | RuntimeException e) {
                Slog.w("ShellStartingWindow", "failed get starting window background color at taskId: " + taskInfo.taskId, e);
                return 0;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            Slog.w("ShellStartingWindow", "Failed creating package context with package name " + str + " for user " + taskInfo.userId, e2);
            return 0;
        }
    }

    public void makeTaskSnapshotWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot) {
        int i = startingWindowInfo.taskInfo.taskId;
        lambda$makeTaskSnapshotWindow$2(i);
        TaskSnapshotWindow create = TaskSnapshotWindow.create(startingWindowInfo, iBinder, taskSnapshot, this.mSplashScreenExecutor, new StartingSurfaceDrawer$$ExternalSyntheticLambda6(this, i));
        if (create != null) {
            this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, (View) null, create, 2));
        }
    }

    public void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            long j = (long) startingWindowRemovalInfo.taskId;
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -958966913, 1, (String) null, Long.valueOf(j));
        }
        removeWindowSynced(startingWindowRemovalInfo, false);
    }

    public void clearAllWindows() {
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -976436888, 0, (String) null, (Object[]) null);
        }
        int size = this.mStartingWindowRecords.size();
        int[] iArr = new int[size];
        int i = size - 1;
        for (int i2 = i; i2 >= 0; i2--) {
            iArr[i2] = this.mStartingWindowRecords.keyAt(i2);
        }
        while (i >= 0) {
            lambda$makeTaskSnapshotWindow$2(iArr[i]);
            i--;
        }
    }

    public void copySplashScreenView(int i) {
        SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable;
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        SplashScreenView r0 = startingWindowRecord != null ? startingWindowRecord.mContentView : null;
        if (r0 == null || !r0.isCopyable()) {
            splashScreenViewParcelable = null;
        } else {
            splashScreenViewParcelable = new SplashScreenView.SplashScreenViewParcelable(r0);
            splashScreenViewParcelable.setClientCallback(new RemoteCallback(new StartingSurfaceDrawer$$ExternalSyntheticLambda4(this, i)));
            r0.onCopied();
            this.mAnimatedSplashScreenSurfaceHosts.append(i, r0.getSurfaceHost());
        }
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 807939633, 13, (String) null, Long.valueOf((long) i), Boolean.valueOf(splashScreenViewParcelable != null));
        }
        ActivityTaskManager.getInstance().onSplashScreenViewCopyFinished(i, splashScreenViewParcelable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$4(int i, Bundle bundle) {
        this.mSplashScreenExecutor.execute(new StartingSurfaceDrawer$$ExternalSyntheticLambda7(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$3(int i) {
        onAppSplashScreenViewRemoved(i, false);
    }

    public void onAppSplashScreenViewRemoved(int i) {
        onAppSplashScreenViewRemoved(i, true);
    }

    public final void onAppSplashScreenViewRemoved(int i, boolean z) {
        SurfaceControlViewHost surfaceControlViewHost = this.mAnimatedSplashScreenSurfaceHosts.get(i);
        if (surfaceControlViewHost != null) {
            this.mAnimatedSplashScreenSurfaceHosts.remove(i);
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -1158385819, 4, (String) null, z ? "Server cleaned up" : "App removed", Long.valueOf((long) i));
            }
            SplashScreenView.releaseIconHost(surfaceControlViewHost);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0060, code lost:
        if (r19.getParent() != null) goto L_0x0063;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addWindow(int r17, android.os.IBinder r18, android.view.View r19, android.view.Display r20, android.view.WindowManager.LayoutParams r21, @android.window.StartingWindowInfo.StartingWindowType int r22) {
        /*
            r16 = this;
            r1 = r16
            r2 = r18
            r9 = r19
            java.lang.String r10 = "view not successfully added to wm, removing view"
            java.lang.String r11 = "ShellStartingWindow"
            android.content.Context r0 = r19.getContext()
            r12 = 0
            r13 = 32
            r15 = 1
            java.lang.String r3 = "addRootView"
            android.os.Trace.traceBegin(r13, r3)     // Catch:{ BadTokenException -> 0x003d }
            android.view.WindowManagerGlobal r3 = r1.mWindowManagerGlobal     // Catch:{ BadTokenException -> 0x003d }
            r7 = 0
            int r8 = r0.getUserId()     // Catch:{ BadTokenException -> 0x003d }
            r4 = r19
            r5 = r21
            r6 = r20
            r3.addView(r4, r5, r6, r7, r8)     // Catch:{ BadTokenException -> 0x003d }
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r0 = r19.getParent()
            if (r0 != 0) goto L_0x0039
        L_0x0030:
            android.util.Slog.w(r11, r10)
            android.view.WindowManagerGlobal r0 = r1.mWindowManagerGlobal
            r0.removeView(r9, r15)
            goto L_0x0063
        L_0x0039:
            r12 = r15
            goto L_0x0063
        L_0x003b:
            r0 = move-exception
            goto L_0x0070
        L_0x003d:
            r0 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x003b }
            r3.<init>()     // Catch:{ all -> 0x003b }
            r3.append(r2)     // Catch:{ all -> 0x003b }
            java.lang.String r4 = " already running, starting window not displayed. "
            r3.append(r4)     // Catch:{ all -> 0x003b }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x003b }
            r3.append(r0)     // Catch:{ all -> 0x003b }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x003b }
            android.util.Slog.w(r11, r0)     // Catch:{ all -> 0x003b }
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r0 = r19.getParent()
            if (r0 != 0) goto L_0x0063
            goto L_0x0030
        L_0x0063:
            if (r12 == 0) goto L_0x006f
            r16.lambda$makeTaskSnapshotWindow$2(r17)
            r3 = r17
            r4 = r22
            r1.saveSplashScreenRecord(r2, r3, r9, r4)
        L_0x006f:
            return r12
        L_0x0070:
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r2 = r19.getParent()
            if (r2 != 0) goto L_0x0081
            android.util.Slog.w(r11, r10)
            android.view.WindowManagerGlobal r1 = r1.mWindowManagerGlobal
            r1.removeView(r9, r15)
        L_0x0081:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.StartingSurfaceDrawer.addWindow(int, android.os.IBinder, android.view.View, android.view.Display, android.view.WindowManager$LayoutParams, int):boolean");
    }

    @VisibleForTesting
    public void saveSplashScreenRecord(IBinder iBinder, int i, View view, @StartingWindowInfo.StartingWindowType int i2) {
        this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, view, (TaskSnapshotWindow) null, i2));
    }

    /* renamed from: removeWindowNoAnimate */
    public final void lambda$makeTaskSnapshotWindow$2(int i) {
        StartingWindowRemovalInfo startingWindowRemovalInfo = this.mTmpRemovalInfo;
        startingWindowRemovalInfo.taskId = i;
        removeWindowSynced(startingWindowRemovalInfo, true);
    }

    public void onImeDrawnOnTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && startingWindowRecord.mTaskSnapshotWindow != null && startingWindowRecord.mTaskSnapshotWindow.hasImeSurface()) {
            lambda$makeTaskSnapshotWindow$2(i);
        }
    }

    public void removeWindowSynced(StartingWindowRemovalInfo startingWindowRemovalInfo, boolean z) {
        int i = startingWindowRemovalInfo.taskId;
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null) {
            if (startingWindowRecord.mDecorView != null) {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 2081268676, 1, (String) null, Long.valueOf((long) i));
                }
                if (startingWindowRecord.mContentView != null) {
                    startingWindowRecord.clearSystemBarColor();
                    if (z || startingWindowRecord.mSuggestType == 4) {
                        removeWindowInner(startingWindowRecord.mDecorView, false);
                    } else if (startingWindowRemovalInfo.playRevealAnimation) {
                        this.mSplashscreenContentDrawer.applyExitAnimation(startingWindowRecord.mContentView, startingWindowRemovalInfo.windowAnimationLeash, startingWindowRemovalInfo.mainFrame, new StartingSurfaceDrawer$$ExternalSyntheticLambda3(this, startingWindowRecord), startingWindowRecord.mCreateTime);
                    } else {
                        removeWindowInner(startingWindowRecord.mDecorView, true);
                    }
                } else {
                    Slog.e("ShellStartingWindow", "Found empty splash screen, remove!");
                    removeWindowInner(startingWindowRecord.mDecorView, false);
                }
            }
            if (startingWindowRecord.mTaskSnapshotWindow != null) {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -1612451307, 1, (String) null, Long.valueOf((long) i));
                }
                if (z) {
                    startingWindowRecord.mTaskSnapshotWindow.removeImmediately();
                } else {
                    startingWindowRecord.mTaskSnapshotWindow.scheduleRemove(startingWindowRemovalInfo.deferRemoveForIme);
                }
            }
            this.mStartingWindowRecords.remove(i);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeWindowSynced$5(StartingWindowRecord startingWindowRecord) {
        removeWindowInner(startingWindowRecord.mDecorView, true);
    }

    public final void removeWindowInner(View view, boolean z) {
        StartingSurface.SysuiProxy sysuiProxy = this.mSysuiProxy;
        if (sysuiProxy != null) {
            sysuiProxy.requestTopUi(false, "ShellStartingWindow");
        }
        if (z) {
            view.setVisibility(8);
        }
        this.mWindowManagerGlobal.removeView(view, false);
    }

    public static class StartingWindowRecord {
        public final IBinder mAppToken;
        public int mBGColor;
        public SplashScreenView mContentView;
        public final long mCreateTime;
        public final View mDecorView;
        public boolean mDrawsSystemBarBackgrounds;
        public boolean mSetSplashScreen;
        @StartingWindowInfo.StartingWindowType
        public int mSuggestType;
        public int mSystemBarAppearance;
        public final TaskSnapshotWindow mTaskSnapshotWindow;

        public StartingWindowRecord(IBinder iBinder, View view, TaskSnapshotWindow taskSnapshotWindow, @StartingWindowInfo.StartingWindowType int i) {
            this.mAppToken = iBinder;
            this.mDecorView = view;
            this.mTaskSnapshotWindow = taskSnapshotWindow;
            if (taskSnapshotWindow != null) {
                this.mBGColor = taskSnapshotWindow.getBackgroundColor();
            }
            this.mSuggestType = i;
            this.mCreateTime = SystemClock.uptimeMillis();
        }

        public final void setSplashScreenView(SplashScreenView splashScreenView) {
            if (!this.mSetSplashScreen) {
                this.mContentView = splashScreenView;
                this.mSetSplashScreen = true;
            }
        }

        public final void parseAppSystemBarColor(Context context) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Window);
            this.mDrawsSystemBarBackgrounds = obtainStyledAttributes.getBoolean(33, false);
            if (obtainStyledAttributes.getBoolean(45, false)) {
                this.mSystemBarAppearance |= 8;
            }
            if (obtainStyledAttributes.getBoolean(48, false)) {
                this.mSystemBarAppearance |= 16;
            }
            obtainStyledAttributes.recycle();
        }

        public final void clearSystemBarColor() {
            View view = this.mDecorView;
            if (view != null) {
                if (view.getLayoutParams() instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mDecorView.getLayoutParams();
                    if (this.mDrawsSystemBarBackgrounds) {
                        layoutParams.flags |= Integer.MIN_VALUE;
                    } else {
                        layoutParams.flags &= Integer.MAX_VALUE;
                    }
                    this.mDecorView.setLayoutParams(layoutParams);
                }
                this.mDecorView.getWindowInsetsController().setSystemBarsAppearance(this.mSystemBarAppearance, 24);
            }
        }
    }
}
