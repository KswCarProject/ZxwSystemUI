package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DisplayCutout;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.FrameLayout;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.widget.floatingtoolbar.FloatingToolbar;
import com.android.systemui.R$id;
import com.android.systemui.R$styleable;

public class NotificationShadeWindowView extends FrameLayout {
    public Window mFakeWindow = new Window(this.mContext) {
        public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        }

        public void alwaysReadCloseOnTouchAttr() {
        }

        public void clearContentView() {
        }

        public void closeAllPanels() {
        }

        public void closePanel(int i) {
        }

        public View getCurrentFocus() {
            return null;
        }

        public WindowInsetsController getInsetsController() {
            return null;
        }

        public LayoutInflater getLayoutInflater() {
            return null;
        }

        public int getNavigationBarColor() {
            return 0;
        }

        public int getStatusBarColor() {
            return 0;
        }

        public int getVolumeControlStream() {
            return 0;
        }

        public void invalidatePanelMenu(int i) {
        }

        public boolean isFloating() {
            return false;
        }

        public boolean isShortcutKey(int i, KeyEvent keyEvent) {
            return false;
        }

        public void onActive() {
        }

        public void onConfigurationChanged(Configuration configuration) {
        }

        public void onMultiWindowModeChanged() {
        }

        public void onPictureInPictureModeChanged(boolean z) {
        }

        public void openPanel(int i, KeyEvent keyEvent) {
        }

        public View peekDecorView() {
            return null;
        }

        public boolean performContextMenuIdentifierAction(int i, int i2) {
            return false;
        }

        public boolean performPanelIdentifierAction(int i, int i2, int i3) {
            return false;
        }

        public boolean performPanelShortcut(int i, int i2, KeyEvent keyEvent, int i3) {
            return false;
        }

        public void reportActivityRelaunched() {
        }

        public void restoreHierarchyState(Bundle bundle) {
        }

        public Bundle saveHierarchyState() {
            return null;
        }

        public void setBackgroundDrawable(Drawable drawable) {
        }

        public void setChildDrawable(int i, Drawable drawable) {
        }

        public void setChildInt(int i, int i2) {
        }

        public void setContentView(int i) {
        }

        public void setContentView(View view) {
        }

        public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        }

        public void setDecorCaptionShade(int i) {
        }

        public void setFeatureDrawable(int i, Drawable drawable) {
        }

        public void setFeatureDrawableAlpha(int i, int i2) {
        }

        public void setFeatureDrawableResource(int i, int i2) {
        }

        public void setFeatureDrawableUri(int i, Uri uri) {
        }

        public void setFeatureInt(int i, int i2) {
        }

        public void setNavigationBarColor(int i) {
        }

        public void setResizingCaptionDrawable(Drawable drawable) {
        }

        public void setStatusBarColor(int i) {
        }

        public void setTitle(CharSequence charSequence) {
        }

        public void setTitleColor(int i) {
        }

        public void setVolumeControlStream(int i) {
        }

        public boolean superDispatchGenericMotionEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
            return false;
        }

        public boolean superDispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return false;
        }

        public boolean superDispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean superDispatchTrackballEvent(MotionEvent motionEvent) {
            return false;
        }

        public void takeInputQueue(InputQueue.Callback callback) {
        }

        public void takeKeyEvents(boolean z) {
        }

        public void takeSurface(SurfaceHolder.Callback2 callback2) {
        }

        public void togglePanel(int i, KeyEvent keyEvent) {
        }

        public View getDecorView() {
            return NotificationShadeWindowView.this;
        }
    };
    public ActionMode mFloatingActionMode;
    public View mFloatingActionModeOriginatingView;
    public FloatingToolbar mFloatingToolbar;
    public ViewTreeObserver.OnPreDrawListener mFloatingToolbarPreDrawListener;
    public InteractionEventHandler mInteractionEventHandler;
    public int mLeftInset = 0;
    public int mRightInset = 0;

    public interface InteractionEventHandler {
        void didIntercept(MotionEvent motionEvent);

        void didNotHandleTouchEvent(MotionEvent motionEvent);

        boolean dispatchKeyEvent(KeyEvent keyEvent);

        boolean dispatchKeyEventPreIme(KeyEvent keyEvent);

        void dispatchTouchEventComplete();

        Boolean handleDispatchTouchEvent(MotionEvent motionEvent);

        boolean handleTouchEvent(MotionEvent motionEvent);

        boolean interceptMediaKey(KeyEvent keyEvent);

        boolean shouldInterceptTouchEvent(MotionEvent motionEvent);
    }

    public NotificationShadeWindowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setMotionEventSplittingEnabled(false);
    }

    public NotificationPanelView getNotificationPanelView() {
        return (NotificationPanelView) findViewById(R$id.notification_panel);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        boolean z = true;
        if (getFitsSystemWindows()) {
            if (insetsIgnoringVisibility.top == getPaddingTop() && insetsIgnoringVisibility.bottom == getPaddingBottom()) {
                z = false;
            }
            if (z) {
                setPadding(0, 0, 0, 0);
            }
        } else {
            if (getPaddingLeft() == 0 && getPaddingRight() == 0 && getPaddingTop() == 0 && getPaddingBottom() == 0) {
                z = false;
            }
            if (z) {
                setPadding(0, 0, 0, 0);
            }
        }
        this.mLeftInset = 0;
        this.mRightInset = 0;
        DisplayCutout displayCutout = getRootWindowInsets().getDisplayCutout();
        if (displayCutout != null) {
            this.mLeftInset = displayCutout.getSafeInsetLeft();
            this.mRightInset = displayCutout.getSafeInsetRight();
        }
        this.mLeftInset = Math.max(insetsIgnoringVisibility.left, this.mLeftInset);
        this.mRightInset = Math.max(insetsIgnoringVisibility.right, this.mRightInset);
        applyMargins();
        return windowInsets;
    }

    public final void applyMargins() {
        int i;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getLayoutParams() instanceof LayoutParams) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (!layoutParams.ignoreRightInset && !(layoutParams.rightMargin == (i = this.mRightInset) && layoutParams.leftMargin == this.mLeftInset)) {
                    layoutParams.rightMargin = i;
                    layoutParams.leftMargin = this.mLeftInset;
                    childAt.requestLayout();
                }
            }
        }
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setWillNotDraw(true);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.mInteractionEventHandler.interceptMediaKey(keyEvent) && !super.dispatchKeyEvent(keyEvent)) {
            return this.mInteractionEventHandler.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        return this.mInteractionEventHandler.dispatchKeyEventPreIme(keyEvent);
    }

    public void setInteractionEventHandler(InteractionEventHandler interactionEventHandler) {
        this.mInteractionEventHandler = interactionEventHandler;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        Boolean handleDispatchTouchEvent = this.mInteractionEventHandler.handleDispatchTouchEvent(motionEvent);
        Boolean valueOf = Boolean.valueOf(handleDispatchTouchEvent != null ? handleDispatchTouchEvent.booleanValue() : super.dispatchTouchEvent(motionEvent));
        this.mInteractionEventHandler.dispatchTouchEventComplete();
        return valueOf.booleanValue();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean shouldInterceptTouchEvent = this.mInteractionEventHandler.shouldInterceptTouchEvent(motionEvent);
        if (!shouldInterceptTouchEvent) {
            shouldInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        }
        if (shouldInterceptTouchEvent) {
            this.mInteractionEventHandler.didIntercept(motionEvent);
        }
        return shouldInterceptTouchEvent;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean handleTouchEvent = this.mInteractionEventHandler.handleTouchEvent(motionEvent);
        if (!handleTouchEvent) {
            handleTouchEvent = super.onTouchEvent(motionEvent);
        }
        if (!handleTouchEvent) {
            this.mInteractionEventHandler.didNotHandleTouchEvent(motionEvent);
        }
        return handleTouchEvent;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public class LayoutParams extends FrameLayout.LayoutParams {
        public boolean ignoreRightInset;

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.StatusBarWindowView_Layout);
            this.ignoreRightInset = obtainStyledAttributes.getBoolean(R$styleable.StatusBarWindowView_Layout_ignoreRightInset, false);
            obtainStyledAttributes.recycle();
        }
    }

    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i) {
        if (i == 1) {
            return startActionMode(view, callback, i);
        }
        return super.startActionModeForChild(view, callback, i);
    }

    public final ActionMode createFloatingActionMode(View view, ActionMode.Callback2 callback2) {
        ActionMode actionMode = this.mFloatingActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        cleanupFloatingActionModeViews();
        this.mFloatingToolbar = new FloatingToolbar(this.mFakeWindow);
        final FloatingActionMode floatingActionMode = new FloatingActionMode(this.mContext, callback2, view, this.mFloatingToolbar);
        this.mFloatingActionModeOriginatingView = view;
        this.mFloatingToolbarPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                floatingActionMode.updateViewLocationInWindow();
                return true;
            }
        };
        return floatingActionMode;
    }

    public final void setHandledFloatingActionMode(ActionMode actionMode) {
        this.mFloatingActionMode = actionMode;
        actionMode.invalidate();
        this.mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
    }

    public final void cleanupFloatingActionModeViews() {
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        View view = this.mFloatingActionModeOriginatingView;
        if (view != null) {
            if (this.mFloatingToolbarPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
                this.mFloatingToolbarPreDrawListener = null;
            }
            this.mFloatingActionModeOriginatingView = null;
        }
    }

    public final ActionMode startActionMode(View view, ActionMode.Callback callback, int i) {
        ActionModeCallback2Wrapper actionModeCallback2Wrapper = new ActionModeCallback2Wrapper(callback);
        ActionMode createFloatingActionMode = createFloatingActionMode(view, actionModeCallback2Wrapper);
        if (createFloatingActionMode == null || !actionModeCallback2Wrapper.onCreateActionMode(createFloatingActionMode, createFloatingActionMode.getMenu())) {
            return null;
        }
        setHandledFloatingActionMode(createFloatingActionMode);
        return createFloatingActionMode;
    }

    public class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
        public final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            NotificationShadeWindowView.this.requestFitSystemWindows();
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (actionMode == NotificationShadeWindowView.this.mFloatingActionMode) {
                NotificationShadeWindowView.this.cleanupFloatingActionModeViews();
                NotificationShadeWindowView.this.mFloatingActionMode = null;
            }
            NotificationShadeWindowView.this.requestFitSystemWindows();
        }

        public void onGetContentRect(ActionMode actionMode, View view, Rect rect) {
            ActionMode.Callback callback = this.mWrapped;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(actionMode, view, rect);
            } else {
                super.onGetContentRect(actionMode, view, rect);
            }
        }
    }
}
