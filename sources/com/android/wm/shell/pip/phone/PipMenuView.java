package com.android.wm.shell.pip.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PipMenuView extends FrameLayout {
    public AccessibilityManager mAccessibilityManager;
    public final List<RemoteAction> mActions = new ArrayList();
    public LinearLayout mActionsGroup;
    public boolean mAllowMenuTimeout = true;
    public boolean mAllowTouches = true;
    public Drawable mBackgroundDrawable;
    public int mBetweenActionPaddingLand;
    public RemoteAction mCloseAction;
    public final PhonePipMenuController mController;
    public boolean mDidLastShowMenuResize;
    public View mDismissButton;
    public int mDismissFadeOutDurationMs;
    public View mEnterSplitButton;
    public boolean mFocusedTaskAllowSplitScreen;
    public final Runnable mHideMenuRunnable = new PipMenuView$$ExternalSyntheticLambda0(this);
    public ShellExecutor mMainExecutor;
    public Handler mMainHandler;
    public ValueAnimator.AnimatorUpdateListener mMenuBgUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            PipMenuView.this.mBackgroundDrawable.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 0.3f * 255.0f));
        }
    };
    public View mMenuContainer;
    public AnimatorSet mMenuContainerAnimator;
    public int mMenuState;
    public final int mPipForceCloseDelay;
    public PipMenuIconsAlgorithm mPipMenuIconsAlgorithm;
    public final PipUiEventLogger mPipUiEventLogger;
    public View mSettingsButton;
    public final Optional<SplitScreenController> mSplitScreenControllerOptional;
    public View mTopEndContainer;
    public View mViewRoot;

    public static /* synthetic */ boolean lambda$updateActionViews$5(View view, MotionEvent motionEvent) {
        return true;
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public PipMenuView(Context context, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor, Handler handler, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger) {
        super(context, (AttributeSet) null, 0);
        this.mContext = context;
        this.mController = phonePipMenuController;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mSplitScreenControllerOptional = optional;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        FrameLayout.inflate(context, R.layout.pip_menu, this);
        this.mPipForceCloseDelay = context.getResources().getInteger(R.integer.config_pipForceCloseDelay);
        Drawable drawable = this.mContext.getDrawable(R.drawable.pip_menu_background);
        this.mBackgroundDrawable = drawable;
        drawable.setAlpha(0);
        View findViewById = findViewById(R.id.background);
        this.mViewRoot = findViewById;
        findViewById.setBackground(this.mBackgroundDrawable);
        View findViewById2 = findViewById(R.id.menu_container);
        this.mMenuContainer = findViewById2;
        findViewById2.setAlpha(0.0f);
        this.mTopEndContainer = findViewById(R.id.top_end_container);
        View findViewById3 = findViewById(R.id.settings);
        this.mSettingsButton = findViewById3;
        findViewById3.setAlpha(0.0f);
        this.mSettingsButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda1(this));
        View findViewById4 = findViewById(R.id.dismiss);
        this.mDismissButton = findViewById4;
        findViewById4.setAlpha(0.0f);
        this.mDismissButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda2(this));
        findViewById(R.id.expand_button).setOnClickListener(new PipMenuView$$ExternalSyntheticLambda3(this));
        View findViewById5 = findViewById(R.id.enter_split);
        this.mEnterSplitButton = findViewById5;
        findViewById5.setAlpha(0.0f);
        this.mEnterSplitButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda4(this));
        int i = R.id.resize_handle;
        findViewById(i).setAlpha(0.0f);
        this.mActionsGroup = (LinearLayout) findViewById(R.id.actions_group);
        this.mBetweenActionPaddingLand = getResources().getDimensionPixelSize(R.dimen.pip_between_action_padding_land);
        PipMenuIconsAlgorithm pipMenuIconsAlgorithm = new PipMenuIconsAlgorithm(this.mContext);
        this.mPipMenuIconsAlgorithm = pipMenuIconsAlgorithm;
        pipMenuIconsAlgorithm.bindViews((ViewGroup) this.mViewRoot, (ViewGroup) this.mTopEndContainer, findViewById(i), this.mEnterSplitButton, this.mSettingsButton, this.mDismissButton);
        this.mDismissFadeOutDurationMs = context.getResources().getInteger(R.integer.config_pipExitAnimationDuration);
        initAccessibility();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (view.getAlpha() != 0.0f) {
            showSettings();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        dismissPip();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        if (this.mMenuContainer.getAlpha() != 0.0f) {
            expandPip();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        if (this.mEnterSplitButton.getAlpha() != 0.0f) {
            enterSplit();
        }
    }

    public final void initAccessibility() {
        setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, PipMenuView.this.getResources().getString(R.string.pip_menu_title)));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i == 16 && PipMenuView.this.mMenuState != 1) {
                    PipMenuView.this.mController.showMenu();
                }
                return super.performAccessibilityAction(view, i, bundle);
            }
        });
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 111) {
            return super.onKeyUp(i, keyEvent);
        }
        hideMenu();
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mAllowTouches) {
            return false;
        }
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    public void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z = false;
        if ((this.mSplitScreenControllerOptional.isPresent() && this.mSplitScreenControllerOptional.get().isTaskInSplitScreen(runningTaskInfo.taskId)) || (runningTaskInfo.getWindowingMode() == 1 && runningTaskInfo.supportsSplitScreenMultiWindow && runningTaskInfo.topActivityType != 2)) {
            z = true;
        }
        this.mFocusedTaskAllowSplitScreen = z;
    }

    public void showMenu(int i, Rect rect, boolean z, boolean z2, boolean z3, boolean z4) {
        final int i2 = i;
        final boolean z5 = z;
        boolean z6 = z2;
        this.mAllowMenuTimeout = z5;
        this.mDidLastShowMenuResize = z6;
        boolean z7 = this.mContext.getResources().getBoolean(R.bool.config_pipEnableEnterSplitButton);
        int i3 = this.mMenuState;
        if (i3 != i2) {
            this.mAllowTouches = !(z6 && (i3 == 1 || i2 == 1));
            cancelDelayedHide();
            AnimatorSet animatorSet = this.mMenuContainerAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            View view = this.mMenuContainer;
            float f = 1.0f;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{view.getAlpha(), 1.0f});
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            View view2 = this.mSettingsButton;
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.ALPHA, new float[]{view2.getAlpha(), 1.0f});
            View view3 = this.mDismissButton;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view3, View.ALPHA, new float[]{view3.getAlpha(), 1.0f});
            View view4 = this.mEnterSplitButton;
            Property property = View.ALPHA;
            float[] fArr = new float[2];
            fArr[0] = view4.getAlpha();
            if (!z7 || !this.mFocusedTaskAllowSplitScreen) {
                f = 0.0f;
            }
            fArr[1] = f;
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view4, property, fArr);
            if (i2 == 1) {
                this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4});
            } else {
                this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat4});
            }
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_IN);
            this.mMenuContainerAnimator.setDuration(125);
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    PipMenuView.this.mAllowTouches = true;
                    PipMenuView.this.notifyMenuStateChangeFinish(i2);
                    if (z5) {
                        PipMenuView.this.repostDelayedHide(3500);
                    }
                }

                public void onAnimationCancel(Animator animator) {
                    PipMenuView.this.mAllowTouches = true;
                }
            });
            if (z3) {
                notifyMenuStateChangeStart(i2, z6, new PipMenuView$$ExternalSyntheticLambda9(this));
            } else {
                notifyMenuStateChangeStart(i2, z6, (Runnable) null);
                setVisibility(0);
                this.mMenuContainerAnimator.start();
            }
            updateActionViews(i, rect);
        } else if (z5) {
            repostDelayedHide(2000);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showMenu$4() {
        AnimatorSet animatorSet = this.mMenuContainerAnimator;
        if (animatorSet != null) {
            animatorSet.setStartDelay(30);
            setVisibility(0);
            this.mMenuContainerAnimator.start();
        }
    }

    public void fadeOutMenu() {
        this.mMenuContainer.setAlpha(0.0f);
        this.mSettingsButton.setAlpha(0.0f);
        this.mDismissButton.setAlpha(0.0f);
        this.mEnterSplitButton.setAlpha(0.0f);
    }

    public void pokeMenu() {
        cancelDelayedHide();
    }

    public void updateMenuLayout(Rect rect) {
        this.mPipMenuIconsAlgorithm.onBoundsChanged(rect);
    }

    public void hideMenu() {
        hideMenu((Runnable) null);
    }

    public void hideMenu(Runnable runnable) {
        hideMenu(runnable, true, this.mDidLastShowMenuResize, 1);
    }

    public void hideMenu(boolean z, int i) {
        hideMenu((Runnable) null, true, z, i);
    }

    public void hideMenu(final Runnable runnable, final boolean z, boolean z2, int i) {
        if (this.mMenuState != 0) {
            cancelDelayedHide();
            if (z) {
                notifyMenuStateChangeStart(0, z2, (Runnable) null);
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            View view = this.mMenuContainer;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{view.getAlpha(), 0.0f});
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            View view2 = this.mSettingsButton;
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.ALPHA, new float[]{view2.getAlpha(), 0.0f});
            View view3 = this.mDismissButton;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view3, View.ALPHA, new float[]{view3.getAlpha(), 0.0f});
            View view4 = this.mEnterSplitButton;
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view4, View.ALPHA, new float[]{view4.getAlpha(), 0.0f});
            this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4});
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_OUT);
            this.mMenuContainerAnimator.setDuration(getFadeOutDuration(i));
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    PipMenuView.this.setVisibility(8);
                    if (z) {
                        PipMenuView.this.notifyMenuStateChangeFinish(0);
                    }
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            this.mMenuContainerAnimator.start();
        }
    }

    public Size getEstimatedMinMenuSize() {
        return new Size(Math.max(2, this.mActions.size()) * getResources().getDimensionPixelSize(R.dimen.pip_action_size), getResources().getDimensionPixelSize(R.dimen.pip_expand_action_size) + getResources().getDimensionPixelSize(R.dimen.pip_action_padding) + getResources().getDimensionPixelSize(R.dimen.pip_expand_container_edge_margin));
    }

    public void setActions(Rect rect, List<RemoteAction> list, RemoteAction remoteAction) {
        this.mActions.clear();
        if (list != null && !list.isEmpty()) {
            this.mActions.addAll(list);
        }
        this.mCloseAction = remoteAction;
        int i = this.mMenuState;
        if (i == 1) {
            updateActionViews(i, rect);
        }
    }

    public final void updateActionViews(int i, Rect rect) {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.expand_container);
        ViewGroup viewGroup2 = (ViewGroup) findViewById(R.id.actions_container);
        viewGroup2.setOnTouchListener(new PipMenuView$$ExternalSyntheticLambda5());
        viewGroup.setVisibility(i == 1 ? 0 : 4);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
        if (this.mActions.isEmpty() || i == 0) {
            viewGroup2.setVisibility(4);
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        } else {
            viewGroup2.setVisibility(0);
            if (this.mActionsGroup != null) {
                LayoutInflater from = LayoutInflater.from(this.mContext);
                while (this.mActionsGroup.getChildCount() < this.mActions.size()) {
                    this.mActionsGroup.addView((PipMenuActionView) from.inflate(R.layout.pip_menu_action, this.mActionsGroup, false));
                }
                int i2 = 0;
                while (true) {
                    int i3 = 8;
                    if (i2 >= this.mActionsGroup.getChildCount()) {
                        break;
                    }
                    View childAt = this.mActionsGroup.getChildAt(i2);
                    if (i2 < this.mActions.size()) {
                        i3 = 0;
                    }
                    childAt.setVisibility(i3);
                    i2++;
                }
                boolean z = rect != null && rect.width() > rect.height();
                int i4 = 0;
                while (i4 < this.mActions.size()) {
                    RemoteAction remoteAction = this.mActions.get(i4);
                    PipMenuActionView pipMenuActionView = (PipMenuActionView) this.mActionsGroup.getChildAt(i4);
                    RemoteAction remoteAction2 = this.mCloseAction;
                    boolean z2 = remoteAction2 != null && Objects.equals(remoteAction2.getActionIntent(), remoteAction.getActionIntent());
                    remoteAction.getIcon().loadDrawableAsync(this.mContext, new PipMenuView$$ExternalSyntheticLambda6(pipMenuActionView), this.mMainHandler);
                    pipMenuActionView.setCustomCloseBackgroundVisibility(z2 ? 0 : 8);
                    pipMenuActionView.setContentDescription(remoteAction.getContentDescription());
                    if (remoteAction.isEnabled()) {
                        pipMenuActionView.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda7(this, remoteAction, z2));
                    }
                    pipMenuActionView.setEnabled(remoteAction.isEnabled());
                    pipMenuActionView.setAlpha(remoteAction.isEnabled() ? 1.0f : 0.54f);
                    ((LinearLayout.LayoutParams) pipMenuActionView.getLayoutParams()).leftMargin = (!z || i4 <= 0) ? 0 : this.mBetweenActionPaddingLand;
                    i4++;
                }
            }
            layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.pip_action_padding);
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.pip_expand_container_edge_margin);
        }
        viewGroup.requestLayout();
    }

    public static /* synthetic */ void lambda$updateActionViews$6(PipMenuActionView pipMenuActionView, Drawable drawable) {
        if (drawable != null) {
            drawable.setTint(-1);
            pipMenuActionView.setImageDrawable(drawable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateActionViews$7(RemoteAction remoteAction, boolean z, View view) {
        onActionViewClicked(remoteAction.getActionIntent(), z);
    }

    public final void notifyMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        this.mController.onMenuStateChangeStart(i, z, runnable);
    }

    public final void notifyMenuStateChangeFinish(int i) {
        this.mMenuState = i;
        this.mController.onMenuStateChangeFinish(i);
    }

    public final void expandPip() {
        PhonePipMenuController phonePipMenuController = this.mController;
        Objects.requireNonNull(phonePipMenuController);
        hideMenu(new PipMenuView$$ExternalSyntheticLambda10(phonePipMenuController), false, true, 1);
        this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN);
    }

    public final void dismissPip() {
        if (this.mMenuState != 0) {
            this.mController.onPipDismiss();
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_TAP_TO_REMOVE);
        }
    }

    public final void onActionViewClicked(PendingIntent pendingIntent, boolean z) {
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(e);
                ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1636860907, 0, (String) null, "PipMenuView", valueOf);
            }
        }
        if (z) {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_CUSTOM_CLOSE);
            this.mAllowTouches = false;
            this.mMainExecutor.executeDelayed(new PipMenuView$$ExternalSyntheticLambda11(this), (long) this.mPipForceCloseDelay);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onActionViewClicked$8() {
        hideMenu();
        this.mController.onPipDismiss();
        this.mAllowTouches = true;
    }

    public final void enterSplit() {
        PhonePipMenuController phonePipMenuController = this.mController;
        Objects.requireNonNull(phonePipMenuController);
        hideMenu(new PipMenuView$$ExternalSyntheticLambda8(phonePipMenuController), false, true, 1);
    }

    public final void showSettings() {
        Pair<ComponentName, Integer> topPipActivity = PipUtils.getTopPipActivity(this.mContext);
        if (topPipActivity.first != null) {
            Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", ((ComponentName) topPipActivity.first).getPackageName(), (String) null));
            intent.setFlags(268468224);
            this.mContext.startActivityAsUser(intent, UserHandle.of(((Integer) topPipActivity.second).intValue()));
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_SETTINGS);
        }
    }

    public final void cancelDelayedHide() {
        this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
    }

    public final void repostDelayedHide(int i) {
        int recommendedTimeoutMillis = this.mAccessibilityManager.getRecommendedTimeoutMillis(i, 5);
        this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
        this.mMainExecutor.executeDelayed(this.mHideMenuRunnable, (long) recommendedTimeoutMillis);
    }

    public final long getFadeOutDuration(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 125;
        }
        if (i == 2) {
            return (long) this.mDismissFadeOutDurationMs;
        }
        throw new IllegalStateException("Invalid animation type " + i);
    }
}
