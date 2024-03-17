package com.android.wm.shell.pip.phone;

import android.app.ActivityManager;
import android.app.RemoteAction;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Size;
import android.view.IWindow;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhonePipMenuController implements PipMenuController {
    public List<RemoteAction> mAppActions;
    public SyncRtSurfaceTransactionApplier mApplier;
    public RemoteAction mCloseAction;
    public final Context mContext;
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public PipMediaController.ActionListener mMediaActionListener = new PipMediaController.ActionListener() {
        public void onMediaActionsChanged(List<RemoteAction> list) {
            PhonePipMenuController.this.mMediaActions = new ArrayList(list);
            PhonePipMenuController.this.updateMenuActions();
        }
    };
    public List<RemoteAction> mMediaActions;
    public final PipMediaController mMediaController;
    public int mMenuState;
    public final Matrix mMoveTransform = new Matrix();
    public final PipBoundsState mPipBoundsState;
    public PipMenuView mPipMenuView;
    public final PipUiEventLogger mPipUiEventLogger;
    public final Optional<SplitScreenController> mSplitScreenController;
    public final SystemWindows mSystemWindows;
    public final RectF mTmpDestinationRectF = new RectF();
    public final Rect mTmpSourceBounds = new Rect();
    public final RectF mTmpSourceRectF = new RectF();

    public interface Listener {
        void onEnterSplit();

        void onPipDismiss();

        void onPipExpand();

        void onPipMenuStateChangeFinish(int i);

        void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable);

        void onPipShowMenu();
    }

    public PhonePipMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        this.mContext = context;
        this.mPipBoundsState = pipBoundsState;
        this.mMediaController = pipMediaController;
        this.mSystemWindows = systemWindows;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mSplitScreenController = optional;
        this.mPipUiEventLogger = pipUiEventLogger;
    }

    public boolean isMenuVisible() {
        return (this.mPipMenuView == null || this.mMenuState == 0) ? false : true;
    }

    public void attach(SurfaceControl surfaceControl) {
        attachPipMenuView();
    }

    public void detach() {
        hideMenu();
        detachPipMenuView();
    }

    public void attachPipMenuView() {
        if (this.mPipMenuView != null) {
            detachPipMenuView();
        }
        PipMenuView pipMenuView = new PipMenuView(this.mContext, this, this.mMainExecutor, this.mMainHandler, this.mSplitScreenController, this.mPipUiEventLogger);
        this.mPipMenuView = pipMenuView;
        this.mSystemWindows.addView(pipMenuView, getPipMenuLayoutParams("PipMenuView", 0, 0), 0, 1);
        setShellRootAccessibilityWindow();
        updateMenuActions();
    }

    public final void detachPipMenuView() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null) {
            this.mApplier = null;
            this.mSystemWindows.removeView(pipMenuView);
            this.mPipMenuView = null;
        }
    }

    public void updateMenuBounds(Rect rect) {
        this.mSystemWindows.updateViewLayout(this.mPipMenuView, getPipMenuLayoutParams("PipMenuView", rect.width(), rect.height()));
        updateMenuLayout(rect);
    }

    public void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null) {
            pipMenuView.onFocusTaskChanged(runningTaskInfo);
        }
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSystemWindows.getViewSurface(this.mPipMenuView);
    }

    public void addListener(Listener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public Size getEstimatedMinMenuSize() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null) {
            return null;
        }
        return pipMenuView.getEstimatedMinMenuSize();
    }

    public void showMenu() {
        this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda3());
    }

    public void showMenuWithPossibleDelay(int i, Rect rect, boolean z, boolean z2, boolean z3) {
        if (z2) {
            fadeOutMenu();
        }
        showMenuInternal(i, rect, z, z2, z2, z3);
    }

    public void showMenu(int i, Rect rect, boolean z, boolean z2, boolean z3) {
        showMenuInternal(i, rect, z, z2, false, z3);
    }

    public final void showMenuInternal(int i, Rect rect, boolean z, boolean z2, boolean z3, boolean z4) {
        if (maybeCreateSyncApplier()) {
            movePipMenu((SurfaceControl) null, (SurfaceControl.Transaction) null, rect);
            updateMenuBounds(rect);
            this.mPipMenuView.showMenu(i, rect, z, z2, z3, z4);
        }
    }

    public void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            if (surfaceControl == null || transaction == null) {
                this.mTmpSourceBounds.set(0, 0, rect.width(), rect.height());
            } else {
                this.mPipMenuView.getBoundsOnScreen(this.mTmpSourceBounds);
            }
            this.mTmpSourceRectF.set(this.mTmpSourceBounds);
            this.mTmpDestinationRectF.set(rect);
            this.mMoveTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl()).withMatrix(this.mMoveTransform).build();
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
                return;
            }
            SyncRtSurfaceTransactionApplier.SurfaceParams build2 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build();
            this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, build2});
        }
    }

    public void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl()).withWindowCrop(rect).build();
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
                return;
            }
            SyncRtSurfaceTransactionApplier.SurfaceParams build2 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build();
            this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, build2});
        }
    }

    public final boolean maybeCreateSyncApplier() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null || pipMenuView.getViewRootImpl() == null) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -101893684, 0, (String) null, "PhonePipMenuController");
            }
            return false;
        }
        if (this.mApplier == null) {
            this.mApplier = new SyncRtSurfaceTransactionApplier(this.mPipMenuView);
        }
        if (this.mApplier != null) {
            return true;
        }
        return false;
    }

    public void pokeMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.pokeMenu();
        }
    }

    public final void fadeOutMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.fadeOutMenu();
        }
    }

    public void hideMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.hideMenu();
        }
    }

    public void hideMenu(int i, boolean z) {
        if (isMenuVisible()) {
            this.mPipMenuView.hideMenu(z, i);
        }
    }

    public void hideMenu(Runnable runnable, Runnable runnable2) {
        if (isMenuVisible()) {
            if (runnable != null) {
                runnable.run();
            }
            this.mPipMenuView.hideMenu(runnable2);
        }
    }

    public void setAppActions(List<RemoteAction> list, RemoteAction remoteAction) {
        this.mAppActions = list;
        this.mCloseAction = remoteAction;
        updateMenuActions();
    }

    public void onPipExpand() {
        this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda4());
    }

    public void onPipDismiss() {
        this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda1());
    }

    public void onEnterSplit() {
        this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda5());
    }

    public final List<RemoteAction> resolveMenuActions() {
        if (isValidActions(this.mAppActions)) {
            return this.mAppActions;
        }
        return this.mMediaActions;
    }

    public final void updateMenuActions() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null) {
            pipMenuView.setActions(this.mPipBoundsState.getBounds(), resolveMenuActions(), this.mCloseAction);
        }
    }

    public static boolean isValidActions(List<?> list) {
        return list != null && list.size() > 0;
    }

    public void onMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        if (i != this.mMenuState) {
            this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda0(i, z, runnable));
            if (i == 1) {
                this.mMediaController.addActionListener(this.mMediaActionListener);
            } else {
                this.mMediaController.removeActionListener(this.mMediaActionListener);
            }
            try {
                WindowManagerGlobal.getWindowSession().grantEmbeddedWindowFocus((IWindow) null, this.mSystemWindows.getFocusGrantToken(this.mPipMenuView), i != 0);
            } catch (RemoteException e) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1614285461, 0, (String) null, "PhonePipMenuController", String.valueOf(e));
                }
            }
        }
    }

    public void onMenuStateChangeFinish(int i) {
        if (i != this.mMenuState) {
            this.mListeners.forEach(new PhonePipMenuController$$ExternalSyntheticLambda2(i));
        }
        this.mMenuState = i;
        setShellRootAccessibilityWindow();
    }

    public final void setShellRootAccessibilityWindow() {
        if (this.mMenuState != 0) {
            this.mSystemWindows.setShellRootAccessibilityWindow(0, 1, this.mPipMenuView);
        } else {
            this.mSystemWindows.setShellRootAccessibilityWindow(0, 1, (View) null);
        }
    }

    public void handlePointerEvent(MotionEvent motionEvent) {
        if (this.mPipMenuView != null) {
            if (motionEvent.isTouchEvent()) {
                this.mPipMenuView.dispatchTouchEvent(motionEvent);
            } else {
                this.mPipMenuView.dispatchGenericMotionEvent(motionEvent);
            }
        }
    }

    public void updateMenuLayout(Rect rect) {
        if (isMenuVisible()) {
            this.mPipMenuView.updateMenuLayout(rect);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PhonePipMenuController");
        printWriter.println(str2 + "mMenuState=" + this.mMenuState);
        printWriter.println(str2 + "mPipMenuView=" + this.mPipMenuView);
        printWriter.println(str2 + "mListeners=" + this.mListeners.size());
    }
}
