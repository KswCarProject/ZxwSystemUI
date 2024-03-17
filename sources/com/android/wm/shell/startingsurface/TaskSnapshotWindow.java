package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ContextImpl;
import android.app.WindowConfiguration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InsetsFlags;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowLayout;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.window.ClientWindowFrames;
import android.window.StartingWindowInfo;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.DecorView;
import com.android.internal.view.BaseIWindow;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.lang.ref.WeakReference;

public class TaskSnapshotWindow {
    public final int mActivityType;
    public final Paint mBackgroundPaint;
    public final Runnable mClearWindowHandler;
    public final Rect mFrame = new Rect();
    public boolean mHasDrawn;
    public final boolean mHasImeSurface;
    public final int mOrientationOnCreation;
    public final Runnable mScheduledRunnable;
    public final IWindowSession mSession;
    public boolean mSizeMismatch;
    public TaskSnapshot mSnapshot;
    public final Matrix mSnapshotMatrix;
    public final ShellExecutor mSplashScreenExecutor;
    public final int mStatusBarColor;
    public final SurfaceControl mSurfaceControl;
    public final SystemBarBackgroundPainter mSystemBarBackgroundPainter;
    public final Rect mSystemBarInsets = new Rect();
    public final Rect mTaskBounds;
    public final CharSequence mTitle;
    public final RectF mTmpDstFrame = new RectF();
    public final float[] mTmpFloat9;
    public final RectF mTmpSnapshotSize = new RectF();
    public final SurfaceControl.Transaction mTransaction;
    public final Window mWindow;

    public static TaskSnapshotWindow create(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot, ShellExecutor shellExecutor, Runnable runnable) {
        StartingWindowInfo startingWindowInfo2 = startingWindowInfo;
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo2.taskInfo;
        int i = runningTaskInfo.taskId;
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1037658567, 1, (String) null, Long.valueOf((long) i));
        }
        WindowManager.LayoutParams layoutParams = startingWindowInfo2.topOpaqueWindowLayoutParams;
        WindowManager.LayoutParams layoutParams2 = startingWindowInfo2.mainWindowLayoutParams;
        InsetsState insetsState = startingWindowInfo2.topOpaqueWindowInsetsState;
        if (layoutParams == null || layoutParams2 == null || insetsState == null) {
            Slog.w("ShellStartingWindow", "unable to create taskSnapshot surface for task: " + i);
            return null;
        }
        WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
        int i2 = layoutParams.insetsFlags.appearance;
        int i3 = layoutParams.flags;
        int i4 = layoutParams.privateFlags;
        layoutParams3.packageName = layoutParams2.packageName;
        layoutParams3.windowAnimations = layoutParams2.windowAnimations;
        layoutParams3.dimAmount = layoutParams2.dimAmount;
        layoutParams3.type = 3;
        layoutParams3.format = taskSnapshot.getHardwareBuffer().getFormat();
        layoutParams3.flags = (-830922809 & i3) | 8 | 16;
        layoutParams3.privateFlags = (131072 & i4) | 536870912 | 33554432;
        layoutParams3.token = iBinder;
        layoutParams3.width = -1;
        layoutParams3.height = -1;
        InsetsFlags insetsFlags = layoutParams3.insetsFlags;
        insetsFlags.appearance = i2;
        insetsFlags.behavior = layoutParams.insetsFlags.behavior;
        layoutParams3.layoutInDisplayCutoutMode = layoutParams.layoutInDisplayCutoutMode;
        layoutParams3.setFitInsetsTypes(layoutParams.getFitInsetsTypes());
        layoutParams3.setFitInsetsSides(layoutParams.getFitInsetsSides());
        layoutParams3.setFitInsetsIgnoringVisibility(layoutParams.isFitInsetsIgnoringVisibility());
        layoutParams3.setTitle(String.format("SnapshotStartingWindow for taskId=%s", new Object[]{Integer.valueOf(i)}));
        Point taskSize = taskSnapshot.getTaskSize();
        Rect rect = new Rect(0, 0, taskSize.x, taskSize.y);
        int orientation = taskSnapshot.getOrientation();
        int i5 = runningTaskInfo.topActivityType;
        int i6 = runningTaskInfo.displayId;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        SurfaceControl surfaceControl = new SurfaceControl();
        ClientWindowFrames clientWindowFrames = new ClientWindowFrames();
        WindowLayout windowLayout = new WindowLayout();
        Rect rect2 = new Rect();
        InsetsSourceControl[] insetsSourceControlArr = new InsetsSourceControl[0];
        MergedConfiguration mergedConfiguration = new MergedConfiguration();
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription == null) {
            taskDescription = new ActivityManager.TaskDescription();
            taskDescription.setBackgroundColor(-1);
        }
        Rect rect3 = rect2;
        ClientWindowFrames clientWindowFrames2 = clientWindowFrames;
        int i7 = i3;
        CharSequence title = layoutParams3.getTitle();
        int i8 = i2;
        ActivityManager.TaskDescription taskDescription2 = taskDescription;
        WindowManager.LayoutParams layoutParams4 = layoutParams3;
        String str = "ShellStartingWindow";
        InsetsState insetsState2 = insetsState;
        TaskSnapshotWindow taskSnapshotWindow = new TaskSnapshotWindow(surfaceControl, taskSnapshot, title, taskDescription2, i8, i7, i4, rect, orientation, i5, insetsState2, runnable, shellExecutor);
        Window window = taskSnapshotWindow.mWindow;
        InsetsState insetsState3 = new InsetsState();
        InputChannel inputChannel = new InputChannel();
        try {
            Trace.traceBegin(32, "TaskSnapshot#addToDisplay");
            int addToDisplay = windowSession.addToDisplay(window, layoutParams4, 8, i6, startingWindowInfo2.requestedVisibilities, inputChannel, insetsState3, insetsSourceControlArr);
            Trace.traceEnd(32);
            if (addToDisplay < 0) {
                Slog.w(str, "Failed to add snapshot starting window res=" + addToDisplay);
                return null;
            }
        } catch (RemoteException unused) {
            taskSnapshotWindow.clearWindowSynced();
        }
        window.setOuter(taskSnapshotWindow);
        try {
            Trace.traceBegin(32, "TaskSnapshot#relayout");
            if (ViewRootImpl.LOCAL_LAYOUT) {
                if (!surfaceControl.isValid()) {
                    windowSession.updateVisibility(window, layoutParams4, 0, mergedConfiguration, surfaceControl, insetsState3, insetsSourceControlArr);
                }
                Rect rect4 = rect3;
                insetsState3.getDisplayCutoutSafe(rect4);
                WindowConfiguration windowConfiguration = mergedConfiguration.getMergedConfiguration().windowConfiguration;
                windowLayout.computeFrames(layoutParams4, insetsState3, rect4, windowConfiguration.getBounds(), windowConfiguration.getWindowingMode(), -1, -1, startingWindowInfo2.requestedVisibilities, (Rect) null, 1.0f, clientWindowFrames2);
                windowSession.updateLayout(window, layoutParams4, 0, clientWindowFrames2, -1, -1);
            } else {
                windowSession.relayout(window, layoutParams4, -1, -1, 0, 0, clientWindowFrames2, mergedConfiguration, surfaceControl, insetsState3, insetsSourceControlArr, new Bundle());
            }
            Trace.traceEnd(32);
        } catch (RemoteException unused2) {
            taskSnapshotWindow.clearWindowSynced();
        }
        ClientWindowFrames clientWindowFrames3 = clientWindowFrames2;
        taskSnapshotWindow.setFrames(clientWindowFrames3.frame, getSystemBarInsets(clientWindowFrames3.frame, insetsState2));
        taskSnapshotWindow.drawSnapshot();
        return taskSnapshotWindow;
    }

    public TaskSnapshotWindow(SurfaceControl surfaceControl, TaskSnapshot taskSnapshot, CharSequence charSequence, ActivityManager.TaskDescription taskDescription, int i, int i2, int i3, Rect rect, int i4, int i5, InsetsState insetsState, Runnable runnable, ShellExecutor shellExecutor) {
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        this.mSnapshotMatrix = new Matrix();
        this.mTmpFloat9 = new float[9];
        this.mScheduledRunnable = new TaskSnapshotWindow$$ExternalSyntheticLambda0(this);
        this.mSplashScreenExecutor = shellExecutor;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        this.mSession = windowSession;
        Window window = new Window();
        this.mWindow = window;
        window.setSession(windowSession);
        this.mSurfaceControl = surfaceControl;
        this.mSnapshot = taskSnapshot;
        this.mTitle = charSequence;
        int backgroundColor = taskDescription.getBackgroundColor();
        paint.setColor(backgroundColor == 0 ? -1 : backgroundColor);
        this.mTaskBounds = rect;
        this.mSystemBarBackgroundPainter = new SystemBarBackgroundPainter(i2, i3, i, taskDescription, 1.0f, insetsState);
        this.mStatusBarColor = taskDescription.getStatusBarColor();
        this.mOrientationOnCreation = i4;
        this.mActivityType = i5;
        this.mTransaction = new SurfaceControl.Transaction();
        this.mClearWindowHandler = runnable;
        this.mHasImeSurface = taskSnapshot.hasImeSurface();
    }

    public int getBackgroundColor() {
        return this.mBackgroundPaint.getColor();
    }

    public boolean hasImeSurface() {
        return this.mHasImeSurface;
    }

    public void scheduleRemove(boolean z) {
        if (this.mActivityType == 2) {
            removeImmediately();
            return;
        }
        this.mSplashScreenExecutor.removeCallbacks(this.mScheduledRunnable);
        long j = (!this.mHasImeSurface || !z) ? 100 : 600;
        this.mSplashScreenExecutor.executeDelayed(this.mScheduledRunnable, j);
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 89657544, 1, (String) null, Long.valueOf(j));
        }
    }

    public void removeImmediately() {
        this.mSplashScreenExecutor.removeCallbacks(this.mScheduledRunnable);
        try {
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                boolean z = this.mHasDrawn;
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1218213214, 3, (String) null, Boolean.valueOf(z));
            }
            this.mSession.remove(this.mWindow);
        } catch (RemoteException unused) {
        }
    }

    public void setFrames(Rect rect, Rect rect2) {
        this.mFrame.set(rect);
        this.mSystemBarInsets.set(rect2);
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        this.mSizeMismatch = (this.mFrame.width() == hardwareBuffer.getWidth() && this.mFrame.height() == hardwareBuffer.getHeight()) ? false : true;
        this.mSystemBarBackgroundPainter.setInsets(rect2);
    }

    public static Rect getSystemBarInsets(Rect rect, InsetsState insetsState) {
        return insetsState.calculateInsets(rect, WindowInsets.Type.systemBars(), false).toRect();
    }

    public final void drawSnapshot() {
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            boolean z = this.mSizeMismatch;
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1663777552, 3, (String) null, Boolean.valueOf(z));
        }
        if (this.mSizeMismatch) {
            drawSizeMismatchSnapshot();
        } else {
            drawSizeMatchSnapshot();
        }
        this.mHasDrawn = true;
        reportDrawn();
        if (this.mSnapshot.getHardwareBuffer() != null) {
            this.mSnapshot.getHardwareBuffer().close();
        }
        this.mSnapshot = null;
        this.mSurfaceControl.release();
    }

    public final void drawSizeMatchSnapshot() {
        this.mTransaction.setBuffer(this.mSurfaceControl, this.mSnapshot.getHardwareBuffer()).setColorSpace(this.mSurfaceControl, this.mSnapshot.getColorSpace()).apply();
    }

    public final void drawSizeMismatchSnapshot() {
        Rect rect;
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        SurfaceSession surfaceSession = new SurfaceSession();
        boolean z = Math.abs((((float) hardwareBuffer.getWidth()) / ((float) hardwareBuffer.getHeight())) - (((float) this.mFrame.width()) / ((float) this.mFrame.height()))) > 0.01f;
        SurfaceControl.Builder builder = new SurfaceControl.Builder(surfaceSession);
        SurfaceControl build = builder.setName(this.mTitle + " - task-snapshot-surface").setBLASTLayer().setFormat(hardwareBuffer.getFormat()).setParent(this.mSurfaceControl).setCallsite("TaskSnapshotWindow.drawSizeMismatchSnapshot").build();
        this.mTransaction.show(build);
        if (z) {
            Rect calculateSnapshotCrop = calculateSnapshotCrop();
            rect = calculateSnapshotFrame(calculateSnapshotCrop);
            this.mTransaction.setWindowCrop(build, calculateSnapshotCrop);
            this.mTransaction.setPosition(build, (float) rect.left, (float) rect.top);
            this.mTmpSnapshotSize.set(calculateSnapshotCrop);
            this.mTmpDstFrame.set(rect);
        } else {
            rect = null;
            this.mTmpSnapshotSize.set(0.0f, 0.0f, (float) hardwareBuffer.getWidth(), (float) hardwareBuffer.getHeight());
            this.mTmpDstFrame.set(this.mFrame);
            this.mTmpDstFrame.offsetTo(0.0f, 0.0f);
        }
        this.mSnapshotMatrix.setRectToRect(this.mTmpSnapshotSize, this.mTmpDstFrame, Matrix.ScaleToFit.FILL);
        this.mTransaction.setMatrix(build, this.mSnapshotMatrix, this.mTmpFloat9);
        this.mTransaction.setColorSpace(build, this.mSnapshot.getColorSpace());
        this.mTransaction.setBuffer(build, this.mSnapshot.getHardwareBuffer());
        if (z) {
            GraphicBuffer create = GraphicBuffer.create(this.mFrame.width(), this.mFrame.height(), 1, 2336);
            Canvas lockCanvas = create.lockCanvas();
            drawBackgroundAndBars(lockCanvas, rect);
            create.unlockCanvasAndPost(lockCanvas);
            this.mTransaction.setBuffer(this.mSurfaceControl, HardwareBuffer.createFromGraphicBuffer(create));
        }
        this.mTransaction.apply();
        build.release();
    }

    public Rect calculateSnapshotCrop() {
        Rect rect = new Rect();
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        int i = 0;
        rect.set(0, 0, hardwareBuffer.getWidth(), hardwareBuffer.getHeight());
        Rect contentInsets = this.mSnapshot.getContentInsets();
        float width = ((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x);
        float height = ((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y);
        boolean z = this.mTaskBounds.top == 0 && this.mFrame.top == 0;
        int i2 = (int) (((float) contentInsets.left) * width);
        if (!z) {
            i = (int) (((float) contentInsets.top) * height);
        }
        rect.inset(i2, i, (int) (((float) contentInsets.right) * width), (int) (((float) contentInsets.bottom) * height));
        return rect;
    }

    public Rect calculateSnapshotFrame(Rect rect) {
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        Rect rect2 = new Rect(0, 0, (int) ((((float) rect.width()) / (((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x))) + 0.5f), (int) ((((float) rect.height()) / (((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y))) + 0.5f));
        rect2.offset(this.mSystemBarInsets.left, 0);
        return rect2;
    }

    public void drawBackgroundAndBars(Canvas canvas, Rect rect) {
        int i;
        Rect rect2 = rect;
        int statusBarColorViewHeight = this.mSystemBarBackgroundPainter.getStatusBarColorViewHeight();
        boolean z = true;
        boolean z2 = canvas.getWidth() > rect2.right;
        if (canvas.getHeight() <= rect2.bottom) {
            z = false;
        }
        if (z2) {
            float f = (float) rect2.right;
            float f2 = Color.alpha(this.mStatusBarColor) == 255 ? (float) statusBarColorViewHeight : 0.0f;
            float width = (float) canvas.getWidth();
            if (z) {
                i = rect2.bottom;
            } else {
                i = canvas.getHeight();
            }
            canvas.drawRect(f, f2, width, (float) i, this.mBackgroundPaint);
        }
        if (z) {
            canvas.drawRect(0.0f, (float) rect2.bottom, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mBackgroundPaint);
        }
        this.mSystemBarBackgroundPainter.drawDecors(canvas, rect2);
    }

    public final void clearWindowSynced() {
        this.mSplashScreenExecutor.executeDelayed(this.mClearWindowHandler, 0);
    }

    public final void reportDrawn() {
        try {
            this.mSession.finishDrawing(this.mWindow, (SurfaceControl.Transaction) null, Integer.MAX_VALUE);
        } catch (RemoteException unused) {
            clearWindowSynced();
        }
    }

    public static class Window extends BaseIWindow {
        public WeakReference<TaskSnapshotWindow> mOuter;

        public void setOuter(TaskSnapshotWindow taskSnapshotWindow) {
            this.mOuter = new WeakReference<>(taskSnapshotWindow);
        }

        public void resized(ClientWindowFrames clientWindowFrames, boolean z, MergedConfiguration mergedConfiguration, InsetsState insetsState, boolean z2, boolean z3, int i, int i2, int i3) {
            TaskSnapshotWindow taskSnapshotWindow = (TaskSnapshotWindow) this.mOuter.get();
            if (taskSnapshotWindow != null) {
                taskSnapshotWindow.mSplashScreenExecutor.execute(new TaskSnapshotWindow$Window$$ExternalSyntheticLambda0(mergedConfiguration, taskSnapshotWindow, z));
            }
        }

        public static /* synthetic */ void lambda$resized$0(MergedConfiguration mergedConfiguration, TaskSnapshotWindow taskSnapshotWindow, boolean z) {
            if (mergedConfiguration != null && taskSnapshotWindow.mOrientationOnCreation != mergedConfiguration.getMergedConfiguration().orientation) {
                taskSnapshotWindow.clearWindowSynced();
            } else if (z && taskSnapshotWindow.mHasDrawn) {
                taskSnapshotWindow.reportDrawn();
            }
        }
    }

    public static class SystemBarBackgroundPainter {
        public final InsetsState mInsetsState;
        public final int mNavigationBarColor;
        public final Paint mNavigationBarPaint;
        public final float mScale;
        public final int mStatusBarColor;
        public final Paint mStatusBarPaint;
        public final Rect mSystemBarInsets = new Rect();
        public final int mWindowFlags;
        public final int mWindowPrivateFlags;

        public SystemBarBackgroundPainter(int i, int i2, int i3, ActivityManager.TaskDescription taskDescription, float f, InsetsState insetsState) {
            Paint paint = new Paint();
            this.mStatusBarPaint = paint;
            Paint paint2 = new Paint();
            this.mNavigationBarPaint = paint2;
            this.mWindowFlags = i;
            this.mWindowPrivateFlags = i2;
            this.mScale = f;
            ContextImpl systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
            int color = systemUiContext.getColor(17171094);
            int calculateBarColor = DecorView.calculateBarColor(i, 67108864, color, taskDescription.getStatusBarColor(), i3, 8, taskDescription.getEnsureStatusBarContrastWhenTransparent());
            this.mStatusBarColor = calculateBarColor;
            int calculateBarColor2 = DecorView.calculateBarColor(i, 134217728, color, taskDescription.getNavigationBarColor(), i3, 16, taskDescription.getEnsureNavigationBarContrastWhenTransparent() && systemUiContext.getResources().getBoolean(17891711));
            this.mNavigationBarColor = calculateBarColor2;
            paint.setColor(calculateBarColor);
            paint2.setColor(calculateBarColor2);
            this.mInsetsState = insetsState;
        }

        public void setInsets(Rect rect) {
            this.mSystemBarInsets.set(rect);
        }

        public int getStatusBarColorViewHeight() {
            if (DecorView.STATUS_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mStatusBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0)) {
                return (int) (((float) this.mSystemBarInsets.top) * this.mScale);
            }
            return 0;
        }

        public final boolean isNavigationBarColorViewVisible() {
            return DecorView.NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mNavigationBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0);
        }

        public void drawDecors(Canvas canvas, Rect rect) {
            drawStatusBarBackground(canvas, rect, getStatusBarColorViewHeight());
            drawNavigationBarBackground(canvas);
        }

        public void drawStatusBarBackground(Canvas canvas, Rect rect, int i) {
            if (i > 0 && Color.alpha(this.mStatusBarColor) != 0) {
                if (rect == null || canvas.getWidth() > rect.right) {
                    canvas.drawRect((float) (rect != null ? rect.right : 0), 0.0f, (float) (canvas.getWidth() - ((int) (((float) this.mSystemBarInsets.right) * this.mScale))), (float) i, this.mStatusBarPaint);
                }
            }
        }

        @VisibleForTesting
        public void drawNavigationBarBackground(Canvas canvas) {
            Rect rect = new Rect();
            DecorView.getNavigationBarRect(canvas.getWidth(), canvas.getHeight(), this.mSystemBarInsets, rect, this.mScale);
            if (isNavigationBarColorViewVisible() && Color.alpha(this.mNavigationBarColor) != 0 && !rect.isEmpty()) {
                canvas.drawRect(rect, this.mNavigationBarPaint);
            }
        }
    }
}
