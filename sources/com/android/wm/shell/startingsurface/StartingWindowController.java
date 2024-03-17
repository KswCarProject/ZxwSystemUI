package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.content.Context;
import android.os.IBinder;
import android.os.Trace;
import android.util.SparseIntArray;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.TriConsumer;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.IStartingWindow;
import com.android.wm.shell.startingsurface.StartingSurface;

public class StartingWindowController implements RemoteCallable<StartingWindowController> {
    public final Context mContext;
    public final StartingSurfaceImpl mImpl = new StartingSurfaceImpl();
    public final ShellExecutor mSplashScreenExecutor;
    public final StartingSurfaceDrawer mStartingSurfaceDrawer;
    public final StartingWindowTypeAlgorithm mStartingWindowTypeAlgorithm;
    @GuardedBy({"mTaskBackgroundColors"})
    public final SparseIntArray mTaskBackgroundColors = new SparseIntArray();
    public TriConsumer<Integer, Integer, Integer> mTaskLaunchingCallback;

    public static boolean isSplashScreenType(@StartingWindowInfo.StartingWindowType int i) {
        return i == 1 || i == 3 || i == 4;
    }

    public StartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        this.mStartingSurfaceDrawer = new StartingSurfaceDrawer(context, shellExecutor, iconProvider, transactionPool);
        this.mStartingWindowTypeAlgorithm = startingWindowTypeAlgorithm;
        this.mSplashScreenExecutor = shellExecutor;
    }

    public StartingSurface asStartingSurface() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mSplashScreenExecutor;
    }

    public void setStartingWindowListener(TriConsumer<Integer, Integer, Integer> triConsumer) {
        this.mTaskLaunchingCallback = triConsumer;
    }

    public void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda0(this, startingWindowInfo, iBinder));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addStartingWindow$0(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        Trace.traceBegin(32, "addStartingWindow");
        int suggestedWindowType = this.mStartingWindowTypeAlgorithm.getSuggestedWindowType(startingWindowInfo);
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo.taskInfo;
        if (isSplashScreenType(suggestedWindowType)) {
            this.mStartingSurfaceDrawer.addSplashScreenStartingWindow(startingWindowInfo, iBinder, suggestedWindowType);
        } else if (suggestedWindowType == 2) {
            this.mStartingSurfaceDrawer.makeTaskSnapshotWindow(startingWindowInfo, iBinder, startingWindowInfo.taskSnapshot);
        }
        if (suggestedWindowType != 0) {
            int i = runningTaskInfo.taskId;
            int startingWindowBackgroundColorForTask = this.mStartingSurfaceDrawer.getStartingWindowBackgroundColorForTask(i);
            if (startingWindowBackgroundColorForTask != 0) {
                synchronized (this.mTaskBackgroundColors) {
                    this.mTaskBackgroundColors.append(i, startingWindowBackgroundColorForTask);
                }
            }
            if (this.mTaskLaunchingCallback != null && isSplashScreenType(suggestedWindowType)) {
                this.mTaskLaunchingCallback.accept(Integer.valueOf(i), Integer.valueOf(suggestedWindowType), Integer.valueOf(startingWindowBackgroundColorForTask));
            }
        }
        Trace.traceEnd(32);
    }

    public void copySplashScreenView(int i) {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda6(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$1(int i) {
        this.mStartingSurfaceDrawer.copySplashScreenView(i);
    }

    public void onAppSplashScreenViewRemoved(int i) {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda2(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAppSplashScreenViewRemoved$2(int i) {
        this.mStartingSurfaceDrawer.onAppSplashScreenViewRemoved(i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onImeDrawnOnTask$3(int i) {
        this.mStartingSurfaceDrawer.onImeDrawnOnTask(i);
    }

    public void onImeDrawnOnTask(int i) {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda5(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$4(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        this.mStartingSurfaceDrawer.removeStartingWindow(startingWindowRemovalInfo);
    }

    public void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda3(this, startingWindowRemovalInfo));
        this.mSplashScreenExecutor.executeDelayed(new StartingWindowController$$ExternalSyntheticLambda4(this, startingWindowRemovalInfo), 5000);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$5(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        synchronized (this.mTaskBackgroundColors) {
            this.mTaskBackgroundColors.delete(startingWindowRemovalInfo.taskId);
        }
    }

    public void clearAllWindows() {
        this.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$clearAllWindows$6() {
        this.mStartingSurfaceDrawer.clearAllWindows();
        synchronized (this.mTaskBackgroundColors) {
            this.mTaskBackgroundColors.clear();
        }
    }

    public class StartingSurfaceImpl implements StartingSurface {
        public IStartingWindowImpl mIStartingWindow;

        public StartingSurfaceImpl() {
        }

        public IStartingWindowImpl createExternalInterface() {
            IStartingWindowImpl iStartingWindowImpl = this.mIStartingWindow;
            if (iStartingWindowImpl != null) {
                iStartingWindowImpl.invalidate();
            }
            IStartingWindowImpl iStartingWindowImpl2 = new IStartingWindowImpl(StartingWindowController.this);
            this.mIStartingWindow = iStartingWindowImpl2;
            return iStartingWindowImpl2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
            if (r3 == 0) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return com.android.wm.shell.startingsurface.SplashscreenContentDrawer.getSystemBGColor();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            return r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0022, code lost:
            r3 = com.android.wm.shell.startingsurface.StartingWindowController.m4584$$Nest$fgetmStartingSurfaceDrawer(r3.this$0).estimateTaskBackgroundColor(r4);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int getBackgroundColor(android.app.TaskInfo r4) {
            /*
                r3 = this;
                com.android.wm.shell.startingsurface.StartingWindowController r0 = com.android.wm.shell.startingsurface.StartingWindowController.this
                android.util.SparseIntArray r0 = r0.mTaskBackgroundColors
                monitor-enter(r0)
                com.android.wm.shell.startingsurface.StartingWindowController r1 = com.android.wm.shell.startingsurface.StartingWindowController.this     // Catch:{ all -> 0x0034 }
                android.util.SparseIntArray r1 = r1.mTaskBackgroundColors     // Catch:{ all -> 0x0034 }
                int r2 = r4.taskId     // Catch:{ all -> 0x0034 }
                int r1 = r1.indexOfKey(r2)     // Catch:{ all -> 0x0034 }
                if (r1 < 0) goto L_0x0021
                com.android.wm.shell.startingsurface.StartingWindowController r3 = com.android.wm.shell.startingsurface.StartingWindowController.this     // Catch:{ all -> 0x0034 }
                android.util.SparseIntArray r3 = r3.mTaskBackgroundColors     // Catch:{ all -> 0x0034 }
                int r3 = r3.valueAt(r1)     // Catch:{ all -> 0x0034 }
                monitor-exit(r0)     // Catch:{ all -> 0x0034 }
                return r3
            L_0x0021:
                monitor-exit(r0)     // Catch:{ all -> 0x0034 }
                com.android.wm.shell.startingsurface.StartingWindowController r3 = com.android.wm.shell.startingsurface.StartingWindowController.this
                com.android.wm.shell.startingsurface.StartingSurfaceDrawer r3 = r3.mStartingSurfaceDrawer
                int r3 = r3.estimateTaskBackgroundColor(r4)
                if (r3 == 0) goto L_0x002f
                goto L_0x0033
            L_0x002f:
                int r3 = com.android.wm.shell.startingsurface.SplashscreenContentDrawer.getSystemBGColor()
            L_0x0033:
                return r3
            L_0x0034:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0034 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.StartingWindowController.StartingSurfaceImpl.getBackgroundColor(android.app.TaskInfo):int");
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setSysuiProxy$0(StartingSurface.SysuiProxy sysuiProxy) {
            StartingWindowController.this.mStartingSurfaceDrawer.setSysuiProxy(sysuiProxy);
        }

        public void setSysuiProxy(StartingSurface.SysuiProxy sysuiProxy) {
            StartingWindowController.this.mSplashScreenExecutor.execute(new StartingWindowController$StartingSurfaceImpl$$ExternalSyntheticLambda0(this, sysuiProxy));
        }
    }

    public static class IStartingWindowImpl extends IStartingWindow.Stub {
        public StartingWindowController mController;
        public SingleInstanceRemoteListener<StartingWindowController, IStartingWindowListener> mListener;
        public final TriConsumer<Integer, Integer, Integer> mStartingWindowListener = new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1(this);

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(Integer num, Integer num2, Integer num3) {
            this.mListener.call(new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda4(num, num2, num3));
        }

        public IStartingWindowImpl(StartingWindowController startingWindowController) {
            this.mController = startingWindowController;
            this.mListener = new SingleInstanceRemoteListener<>(startingWindowController, new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda2(this), new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda3());
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(StartingWindowController startingWindowController) {
            startingWindowController.setStartingWindowListener(this.mStartingWindowListener);
        }

        public void invalidate() {
            this.mController = null;
        }

        public void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setStartingWindowListener", new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0(this, iStartingWindowListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setStartingWindowListener$4(IStartingWindowListener iStartingWindowListener, StartingWindowController startingWindowController) {
            if (iStartingWindowListener != null) {
                this.mListener.register(iStartingWindowListener);
            } else {
                this.mListener.unregister();
            }
        }
    }
}
