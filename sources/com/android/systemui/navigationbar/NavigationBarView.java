package com.android.systemui.navigationbar;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda6;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.navigationbar.buttons.ContextualButtonGroup;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.NearestTouchFrame;
import com.android.systemui.navigationbar.buttons.RotationContextButton;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.recents.Recents;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.pip.Pip;
import com.szchoiceway.view.NaviBarView;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class NavigationBarView extends FrameLayout {
    public AutoHideController mAutoHideController;
    public KeyButtonDrawable mBackIcon;
    public NavigationBarTransitions mBarTransitions;
    public final SparseArray<ButtonDispatcher> mButtonDispatchers;
    public Configuration mConfiguration;
    public final ContextualButtonGroup mContextualButtonGroup;
    public int mCurrentRotation = -1;
    public View mCurrentView = null;
    public int mDarkIconColor;
    public final DeadZone mDeadZone;
    public int mDisabledFlags = 0;
    public KeyButtonDrawable mDockedIcon;
    public final Consumer<Boolean> mDockedListener;
    public boolean mDockedStackExists;
    public EdgeBackGestureHandler mEdgeBackGestureHandler;
    public FloatingRotationButton mFloatingRotationButton;
    public KeyButtonDrawable mHomeDefaultIcon;
    public View mHorizontal;
    public final boolean mImeCanRenderGesturalNavButtons;
    public boolean mImeDrawsImeNavBar;
    public boolean mInCarMode = false;
    public boolean mIsVertical;
    public boolean mLayoutTransitionsEnabled = true;
    public Context mLightContext;
    public int mLightIconColor;
    public boolean mLongClickableAccessibilityButton;
    public int mNavBarMode;
    public int mNavigationIconHints = 0;
    public NavigationBarInflaterView mNavigationInflaterView;
    public OnVerticalChangedListener mOnVerticalChangedListener;
    public boolean mOverviewProxyEnabled;
    public NotificationPanelViewController mPanelView;
    public final Consumer<Rect> mPipListener;
    public final View.AccessibilityDelegate mQuickStepAccessibilityDelegate;
    public KeyButtonDrawable mRecentIcon;
    public Optional<Recents> mRecentsOptional;
    public RotationButtonController mRotationButtonController;
    public final RotationButton.RotationButtonUpdatesCallback mRotationButtonListener;
    public RotationContextButton mRotationContextButton;
    public boolean mScreenOn = true;
    public ScreenPinningNotify mScreenPinningNotify;
    public boolean mShowSwipeUpUi;
    public Configuration mTmpLastConfiguration;
    public Gefingerpoken mTouchHandler;
    public final NavTransitionListener mTransitionListener = new NavTransitionListener();
    public UpdateActiveTouchRegionsCallback mUpdateActiveTouchRegionsCallback;
    public boolean mUseCarModeUi = false;
    public View mVertical;
    public boolean mWakeAndUnlocking;
    public NaviBarView mZxwNavBarView;

    public interface OnVerticalChangedListener {
        void onVerticalChanged(boolean z);
    }

    public interface UpdateActiveTouchRegionsCallback {
        void update();
    }

    public static String visibilityToString(int i) {
        return i != 4 ? i != 8 ? "VISIBLE" : "GONE" : "INVISIBLE";
    }

    public class NavTransitionListener implements LayoutTransition.TransitionListener {
        public boolean mBackTransitioning;
        public long mDuration;
        public boolean mHomeAppearing;
        public TimeInterpolator mInterpolator;
        public long mStartDelay;

        public NavTransitionListener() {
        }

        public void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == R$id.back) {
                this.mBackTransitioning = true;
            } else if (view.getId() == R$id.home && i == 2) {
                this.mHomeAppearing = true;
                this.mStartDelay = layoutTransition.getStartDelay(i);
                this.mDuration = layoutTransition.getDuration(i);
                this.mInterpolator = layoutTransition.getInterpolator(i);
            }
        }

        public void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == R$id.back) {
                this.mBackTransitioning = false;
            } else if (view.getId() == R$id.home && i == 2) {
                this.mHomeAppearing = false;
            }
        }

        public void onBackAltCleared() {
            ButtonDispatcher backButton = NavigationBarView.this.getBackButton();
            if (!this.mBackTransitioning && backButton.getVisibility() == 0 && this.mHomeAppearing && NavigationBarView.this.getHomeButton().getAlpha() == 0.0f) {
                NavigationBarView.this.getBackButton().setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(backButton, "alpha", new float[]{0.0f, 1.0f});
                ofFloat.setStartDelay(this.mStartDelay);
                ofFloat.setDuration(this.mDuration);
                ofFloat.setInterpolator(this.mInterpolator);
                ofFloat.start();
            }
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NavigationBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Context context2 = context;
        SparseArray<ButtonDispatcher> sparseArray = new SparseArray<>();
        this.mButtonDispatchers = sparseArray;
        this.mRecentsOptional = Optional.empty();
        this.mImeCanRenderGesturalNavButtons = InputMethodService.canImeRenderGesturalNavButtons();
        this.mQuickStepAccessibilityDelegate = new View.AccessibilityDelegate() {
            public AccessibilityNodeInfo.AccessibilityAction mToggleOverviewAction;

            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (this.mToggleOverviewAction == null) {
                    this.mToggleOverviewAction = new AccessibilityNodeInfo.AccessibilityAction(R$id.action_toggle_overview, NavigationBarView.this.getContext().getString(R$string.quick_step_accessibility_toggle_overview));
                }
                accessibilityNodeInfo.addAction(this.mToggleOverviewAction);
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i != R$id.action_toggle_overview) {
                    return super.performAccessibilityAction(view, i, bundle);
                }
                NavigationBarView.this.mRecentsOptional.ifPresent(new SystemActions$$ExternalSyntheticLambda6());
                return true;
            }
        };
        this.mRotationButtonListener = new RotationButton.RotationButtonUpdatesCallback() {
            public void onVisibilityChanged(boolean z) {
                if (z && NavigationBarView.this.mAutoHideController != null) {
                    NavigationBarView.this.mAutoHideController.touchAutoHide();
                }
                NavigationBarView.this.notifyActiveTouchRegions();
            }

            public void onPositionChanged() {
                NavigationBarView.this.notifyActiveTouchRegions();
            }
        };
        this.mDockedListener = new NavigationBarView$$ExternalSyntheticLambda0(this);
        this.mPipListener = new NavigationBarView$$ExternalSyntheticLambda1(this);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context2, Utils.getThemeAttr(context2, R$attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context2, Utils.getThemeAttr(context2, R$attr.lightIconTheme));
        this.mLightContext = contextThemeWrapper2;
        int i = R$attr.singleToneColor;
        this.mLightIconColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, i);
        this.mDarkIconColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        this.mIsVertical = false;
        this.mLongClickableAccessibilityButton = false;
        this.mZxwNavBarView = (NaviBarView) findViewById(R$id.fragmentPage);
        int i2 = R$id.menu_container;
        ContextualButtonGroup contextualButtonGroup = new ContextualButtonGroup(i2);
        this.mContextualButtonGroup = contextualButtonGroup;
        int i3 = R$id.ime_switcher;
        ContextualButton contextualButton = new ContextualButton(i3, this.mLightContext, R$drawable.ic_ime_switcher_default);
        int i4 = R$id.accessibility_button;
        ContextualButton contextualButton2 = new ContextualButton(i4, this.mLightContext, R$drawable.ic_sysbar_accessibility_button);
        contextualButtonGroup.addButton(contextualButton);
        contextualButtonGroup.addButton(contextualButton2);
        int i5 = R$id.rotate_suggestion;
        Context context3 = this.mLightContext;
        int i6 = R$drawable.ic_sysbar_rotate_button_ccw_start_0;
        this.mRotationContextButton = new RotationContextButton(i5, context3, i6);
        this.mFloatingRotationButton = new FloatingRotationButton(this.mContext, R$string.accessibility_rotate_button, R$layout.rotate_suggestion, i5, R$dimen.floating_rotation_button_min_margin, R$dimen.rounded_corner_content_padding, R$dimen.floating_rotation_button_taskbar_left_margin, R$dimen.floating_rotation_button_taskbar_bottom_margin, R$dimen.floating_rotation_button_diameter, R$dimen.key_button_ripple_max_width);
        Context context4 = this.mLightContext;
        int i7 = this.mLightIconColor;
        int i8 = this.mDarkIconColor;
        this.mRotationButtonController = new RotationButtonController(context4, i7, i8, i6, R$drawable.ic_sysbar_rotate_button_ccw_start_90, R$drawable.ic_sysbar_rotate_button_cw_start_0, R$drawable.ic_sysbar_rotate_button_cw_start_90, new NavigationBarView$$ExternalSyntheticLambda2(this));
        this.mConfiguration = new Configuration();
        this.mTmpLastConfiguration = new Configuration();
        this.mConfiguration.updateFrom(context.getResources().getConfiguration());
        this.mScreenPinningNotify = new ScreenPinningNotify(this.mContext);
        int i9 = R$id.back;
        sparseArray.put(i9, new ButtonDispatcher(i9));
        int i10 = R$id.home;
        sparseArray.put(i10, new ButtonDispatcher(i10));
        int i11 = R$id.home_handle;
        sparseArray.put(i11, new ButtonDispatcher(i11));
        int i12 = R$id.recent_apps;
        sparseArray.put(i12, new ButtonDispatcher(i12));
        sparseArray.put(i3, contextualButton);
        sparseArray.put(i4, contextualButton2);
        sparseArray.put(i2, contextualButtonGroup);
        this.mDeadZone = new DeadZone(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Integer lambda$new$0() {
        return Integer.valueOf(this.mCurrentRotation);
    }

    public void setEdgeBackGestureHandler(EdgeBackGestureHandler edgeBackGestureHandler) {
        this.mEdgeBackGestureHandler = edgeBackGestureHandler;
    }

    public void setBarTransitions(NavigationBarTransitions navigationBarTransitions) {
        this.mBarTransitions = navigationBarTransitions;
    }

    public void setAutoHideController(AutoHideController autoHideController) {
        this.mAutoHideController = autoHideController;
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mBarTransitions.getLightTransitionsController();
    }

    public void setComponents(Optional<Recents> optional) {
        this.mRecentsOptional = optional;
    }

    public void setComponents(NotificationPanelViewController notificationPanelViewController) {
        this.mPanelView = notificationPanelViewController;
        updatePanelSystemUiStateFlags();
    }

    public void setOnVerticalChangedListener(OnVerticalChangedListener onVerticalChangedListener) {
        this.mOnVerticalChangedListener = onVerticalChangedListener;
        notifyVerticalChangedListener(this.mIsVertical);
    }

    public void setTouchHandler(Gefingerpoken gefingerpoken) {
        this.mTouchHandler = gefingerpoken;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mTouchHandler.onInterceptTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mTouchHandler.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void abortCurrentGesture() {
        getHomeButton().abortCurrentGesture();
    }

    public View getCurrentView() {
        return this.mCurrentView;
    }

    public void forEachView(Consumer<View> consumer) {
        View view = this.mVertical;
        if (view != null) {
            consumer.accept(view);
        }
        View view2 = this.mHorizontal;
        if (view2 != null) {
            consumer.accept(view2);
        }
    }

    public RotationButtonController getRotationButtonController() {
        return this.mRotationButtonController;
    }

    public FloatingRotationButton getFloatingRotationButton() {
        return this.mFloatingRotationButton;
    }

    public ButtonDispatcher getRecentsButton() {
        return this.mButtonDispatchers.get(R$id.recent_apps);
    }

    public ButtonDispatcher getBackButton() {
        return this.mButtonDispatchers.get(R$id.back);
    }

    public ButtonDispatcher getHomeButton() {
        return this.mButtonDispatchers.get(R$id.home);
    }

    public ButtonDispatcher getImeSwitchButton() {
        return this.mButtonDispatchers.get(R$id.ime_switcher);
    }

    public ButtonDispatcher getAccessibilityButton() {
        return this.mButtonDispatchers.get(R$id.accessibility_button);
    }

    public RotationContextButton getRotateSuggestionButton() {
        return (RotationContextButton) this.mButtonDispatchers.get(R$id.rotate_suggestion);
    }

    public ButtonDispatcher getHomeHandle() {
        return this.mButtonDispatchers.get(R$id.home_handle);
    }

    public SparseArray<ButtonDispatcher> getButtonDispatchers() {
        return this.mButtonDispatchers;
    }

    public boolean isRecentsButtonVisible() {
        return getRecentsButton().getVisibility() == 0;
    }

    public boolean isOverviewEnabled() {
        return (this.mDisabledFlags & 16777216) == 0;
    }

    public final boolean isQuickStepSwipeUpEnabled() {
        return this.mShowSwipeUpUi && isOverviewEnabled();
    }

    public final void reloadNavIcons() {
        updateIcons(Configuration.EMPTY);
    }

    public final void updateIcons(Configuration configuration) {
        int i = configuration.orientation;
        Configuration configuration2 = this.mConfiguration;
        boolean z = true;
        boolean z2 = i != configuration2.orientation;
        boolean z3 = configuration.densityDpi != configuration2.densityDpi;
        if (configuration.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
            z = false;
        }
        if (z2 || z3) {
            this.mDockedIcon = getDrawable(R$drawable.ic_sysbar_docked);
            this.mHomeDefaultIcon = getHomeDrawable();
        }
        if (z3 || z) {
            this.mRecentIcon = getDrawable(R$drawable.ic_sysbar_recent);
            this.mContextualButtonGroup.updateIcons(this.mLightIconColor, this.mDarkIconColor);
        }
        if (z2 || z3 || z) {
            this.mBackIcon = getBackDrawable();
        }
    }

    public void updateRotationButton() {
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            ContextualButtonGroup contextualButtonGroup = this.mContextualButtonGroup;
            int i = R$id.rotate_suggestion;
            contextualButtonGroup.removeButton(i);
            this.mButtonDispatchers.remove(i);
            this.mRotationButtonController.setRotationButton(this.mFloatingRotationButton, this.mRotationButtonListener);
        } else {
            ContextualButtonGroup contextualButtonGroup2 = this.mContextualButtonGroup;
            int i2 = R$id.rotate_suggestion;
            if (contextualButtonGroup2.getContextButton(i2) == null) {
                this.mContextualButtonGroup.addButton(this.mRotationContextButton);
                this.mButtonDispatchers.put(i2, this.mRotationContextButton);
                this.mRotationButtonController.setRotationButton(this.mRotationContextButton, this.mRotationButtonListener);
            }
        }
        this.mNavigationInflaterView.setButtonDispatchers(this.mButtonDispatchers);
    }

    public KeyButtonDrawable getBackDrawable() {
        KeyButtonDrawable drawable = getDrawable(getBackDrawableRes());
        orientBackButton(drawable);
        return drawable;
    }

    public int getBackDrawableRes() {
        return chooseNavigationIconDrawableRes(R$drawable.ic_sysbar_back, R$drawable.ic_sysbar_back_quick_step);
    }

    public KeyButtonDrawable getHomeDrawable() {
        KeyButtonDrawable keyButtonDrawable;
        if (this.mShowSwipeUpUi) {
            keyButtonDrawable = getDrawable(R$drawable.ic_sysbar_home_quick_step);
        } else {
            keyButtonDrawable = getDrawable(R$drawable.ic_sysbar_home);
        }
        orientHomeButton(keyButtonDrawable);
        return keyButtonDrawable;
    }

    public final void orientBackButton(KeyButtonDrawable keyButtonDrawable) {
        float f;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        boolean z2 = this.mConfiguration.getLayoutDirection() == 1;
        float f2 = 0.0f;
        if (z) {
            f = (float) (z2 ? 90 : -90);
        } else {
            f = 0.0f;
        }
        if (keyButtonDrawable.getRotation() != f) {
            if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                keyButtonDrawable.setRotation(f);
                return;
            }
            if (!this.mShowSwipeUpUi && !this.mIsVertical && z) {
                f2 = -getResources().getDimension(R$dimen.navbar_back_button_ime_offset);
            }
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(keyButtonDrawable, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_ROTATE, new float[]{f}), PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_TRANSLATE_Y, new float[]{f2})});
            ofPropertyValuesHolder.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofPropertyValuesHolder.setDuration(200);
            ofPropertyValuesHolder.start();
        }
    }

    public final void orientHomeButton(KeyButtonDrawable keyButtonDrawable) {
        keyButtonDrawable.setRotation(this.mIsVertical ? 90.0f : 0.0f);
    }

    public final int chooseNavigationIconDrawableRes(int i, int i2) {
        return this.mShowSwipeUpUi ? i2 : i;
    }

    public final KeyButtonDrawable getDrawable(int i) {
        return KeyButtonDrawable.create(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, i, true, (Color) null);
    }

    public void onScreenStateChanged(boolean z) {
        this.mScreenOn = z;
    }

    public void setWindowVisible(boolean z) {
        this.mRotationButtonController.onNavigationBarWindowVisibilityChange(z);
    }

    public void setBehavior(int i) {
        this.mRotationButtonController.onBehaviorChanged(0, i);
    }

    public void setLayoutDirection(int i) {
        reloadNavIcons();
        super.setLayoutDirection(i);
    }

    public void setNavigationIconHints(int i) {
        if (i != this.mNavigationIconHints) {
            this.mNavigationIconHints = i;
            updateNavButtonIcons();
        }
    }

    public void onImeVisibilityChanged(boolean z) {
        if (!z) {
            this.mTransitionListener.onBackAltCleared();
        }
        this.mRotationButtonController.getRotationButton().setCanShowRotationButton(!z);
    }

    public void setDisabledFlags(int i, SysUiState sysUiState) {
        if (this.mDisabledFlags != i) {
            boolean isOverviewEnabled = isOverviewEnabled();
            this.mDisabledFlags = i;
            if (!isOverviewEnabled && isOverviewEnabled()) {
                reloadNavIcons();
            }
            updateNavButtonIcons();
            updateSlippery();
            updateDisabledSystemUiStateFlags(sysUiState);
        }
    }

    public void updateNavButtonIcons() {
        LayoutTransition layoutTransition;
        int i = 0;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        KeyButtonDrawable keyButtonDrawable = this.mBackIcon;
        orientBackButton(keyButtonDrawable);
        KeyButtonDrawable keyButtonDrawable2 = this.mHomeDefaultIcon;
        if (!this.mUseCarModeUi) {
            orientHomeButton(keyButtonDrawable2);
        }
        getHomeButton().setImageDrawable(keyButtonDrawable2);
        getBackButton().setImageDrawable(keyButtonDrawable);
        updateRecentsIcon();
        this.mContextualButtonGroup.setButtonVisibility(R$id.ime_switcher, !((this.mNavigationIconHints & 4) == 0 || isImeRenderingNavButtons()));
        this.mBarTransitions.reapplyDarkIntensity();
        boolean z2 = QuickStepContract.isGesturalMode(this.mNavBarMode) || (this.mDisabledFlags & 2097152) != 0;
        boolean isRecentsButtonDisabled = isRecentsButtonDisabled();
        boolean z3 = isRecentsButtonDisabled && (2097152 & this.mDisabledFlags) != 0;
        boolean z4 = (!z && (this.mEdgeBackGestureHandler.isHandlingGestures() || (this.mDisabledFlags & 4194304) != 0)) || isImeRenderingNavButtons();
        boolean isScreenPinningActive = ActivityManagerWrapper.getInstance().isScreenPinningActive();
        if (this.mOverviewProxyEnabled) {
            isRecentsButtonDisabled |= true ^ QuickStepContract.isLegacyMode(this.mNavBarMode);
            if (isScreenPinningActive && !QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                z4 = false;
                z2 = false;
            }
        } else if (isScreenPinningActive) {
            z4 = false;
            isRecentsButtonDisabled = false;
        }
        ViewGroup viewGroup = (ViewGroup) getCurrentView().findViewById(R$id.nav_buttons);
        if (!(viewGroup == null || (layoutTransition = viewGroup.getLayoutTransition()) == null || layoutTransition.getTransitionListeners().contains(this.mTransitionListener))) {
            layoutTransition.addTransitionListener(this.mTransitionListener);
        }
        NaviBarView naviBarView = this.mZxwNavBarView;
        if (naviBarView == null || !naviBarView.isCustomerRes()) {
            getBackButton().setVisibility(z4 ? 4 : 0);
            getHomeButton().setVisibility(z2 ? 4 : 0);
            getRecentsButton().setVisibility(isRecentsButtonDisabled ? 4 : 0);
            ButtonDispatcher homeHandle = getHomeHandle();
            if (z3) {
                i = 4;
            }
            homeHandle.setVisibility(i);
        } else {
            getBackButton().setVisibility(4);
            getHomeButton().setVisibility(4);
            getRecentsButton().setVisibility(4);
            getHomeHandle().setVisibility(4);
        }
        notifyActiveTouchRegions();
    }

    public boolean isImeRenderingNavButtons() {
        return this.mImeDrawsImeNavBar && this.mImeCanRenderGesturalNavButtons && (this.mNavigationIconHints & 2) != 0;
    }

    @VisibleForTesting
    public boolean isRecentsButtonDisabled() {
        return this.mUseCarModeUi || !isOverviewEnabled() || getContext().getDisplayId() != 0;
    }

    public final Display getContextDisplay() {
        return getContext().getDisplay();
    }

    public void setLayoutTransitionsEnabled(boolean z) {
        this.mLayoutTransitionsEnabled = z;
        updateLayoutTransitionsEnabled();
    }

    public void setWakeAndUnlocking(boolean z) {
        setUseFadingAnimations(z);
        this.mWakeAndUnlocking = z;
        updateLayoutTransitionsEnabled();
    }

    public final void updateLayoutTransitionsEnabled() {
        boolean z = !this.mWakeAndUnlocking && this.mLayoutTransitionsEnabled;
        LayoutTransition layoutTransition = ((ViewGroup) getCurrentView().findViewById(R$id.nav_buttons)).getLayoutTransition();
        if (layoutTransition == null) {
            return;
        }
        if (z) {
            layoutTransition.enableTransitionType(2);
            layoutTransition.enableTransitionType(3);
            layoutTransition.enableTransitionType(0);
            layoutTransition.enableTransitionType(1);
            return;
        }
        layoutTransition.disableTransitionType(2);
        layoutTransition.disableTransitionType(3);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
    }

    public final void setUseFadingAnimations(boolean z) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) ((ViewGroup) getParent()).getLayoutParams();
        if (layoutParams != null) {
            boolean z2 = layoutParams.windowAnimations != 0;
            if (!z2 && z) {
                layoutParams.windowAnimations = R$style.Animation_NavigationBarFadeIn;
            } else if (z2 && !z) {
                layoutParams.windowAnimations = 0;
            } else {
                return;
            }
            ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout((View) getParent(), layoutParams);
        }
    }

    public void onStatusBarPanelStateChanged() {
        updateSlippery();
    }

    public void updateDisabledSystemUiStateFlags(SysUiState sysUiState) {
        int displayId = this.mContext.getDisplayId();
        boolean z = true;
        SysUiState flag = sysUiState.setFlag(1, ActivityManagerWrapper.getInstance().isScreenPinningActive()).setFlag(128, (this.mDisabledFlags & 16777216) != 0).setFlag(256, (this.mDisabledFlags & 2097152) != 0);
        if ((this.mDisabledFlags & 33554432) == 0) {
            z = false;
        }
        flag.setFlag(1024, z).commitUpdate(displayId);
    }

    public final void updatePanelSystemUiStateFlags() {
        NotificationPanelViewController notificationPanelViewController = this.mPanelView;
        if (notificationPanelViewController != null) {
            notificationPanelViewController.updateSystemUiStateFlags();
        }
    }

    public void onOverviewProxyConnectionChange(boolean z) {
        this.mOverviewProxyEnabled = z;
    }

    public void setShouldShowSwipeUpUi(boolean z) {
        this.mShowSwipeUpUi = z;
        updateStates();
    }

    public void updateStates() {
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.onLikelyDefaultLayoutChange();
        }
        updateSlippery();
        reloadNavIcons();
        updateNavButtonIcons();
        WindowManagerWrapper.getInstance().setNavBarVirtualKeyHapticFeedbackEnabled(!this.mShowSwipeUpUi);
        getHomeButton().setAccessibilityDelegate(this.mShowSwipeUpUi ? this.mQuickStepAccessibilityDelegate : null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r1.mPanelView;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateSlippery() {
        /*
            r1 = this;
            boolean r0 = r1.isQuickStepSwipeUpEnabled()
            if (r0 == 0) goto L_0x001b
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r1.mPanelView
            if (r0 == 0) goto L_0x0019
            boolean r0 = r0.isFullyExpanded()
            if (r0 == 0) goto L_0x0019
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r1.mPanelView
            boolean r0 = r0.isCollapsing()
            if (r0 != 0) goto L_0x0019
            goto L_0x001b
        L_0x0019:
            r0 = 0
            goto L_0x001c
        L_0x001b:
            r0 = 1
        L_0x001c:
            r1.setSlippery(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBarView.updateSlippery():void");
    }

    public void setSlippery(boolean z) {
        setWindowFlag(536870912, z);
    }

    public final void setWindowFlag(int i, boolean z) {
        WindowManager.LayoutParams layoutParams;
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null && (layoutParams = (WindowManager.LayoutParams) viewGroup.getLayoutParams()) != null) {
            int i2 = layoutParams.flags;
            if (z != ((i2 & i) != 0)) {
                if (z) {
                    layoutParams.flags = i | i2;
                } else {
                    layoutParams.flags = (~i) & i2;
                }
                ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout(viewGroup, layoutParams);
            }
        }
    }

    public void setNavBarMode(int i, boolean z) {
        this.mNavBarMode = i;
        this.mImeDrawsImeNavBar = z;
        this.mBarTransitions.onNavigationModeChanged(i);
        this.mEdgeBackGestureHandler.onNavigationModeChanged(this.mNavBarMode);
        updateRotationButton();
    }

    public void setAccessibilityButtonState(boolean z, boolean z2) {
        this.mLongClickableAccessibilityButton = z2;
        getAccessibilityButton().setLongClickable(z2);
        this.mContextualButtonGroup.setButtonVisibility(R$id.accessibility_button, z);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        NavigationBarInflaterView navigationBarInflaterView = (NavigationBarInflaterView) findViewById(R$id.navigation_inflater);
        this.mNavigationInflaterView = navigationBarInflaterView;
        navigationBarInflaterView.setButtonDispatchers(this.mButtonDispatchers);
        updateOrientationViews();
        reloadNavIcons();
    }

    public void onDraw(Canvas canvas) {
        this.mDeadZone.onDraw(canvas);
        super.onDraw(canvas);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        notifyActiveTouchRegions();
    }

    public void notifyActiveTouchRegions() {
        UpdateActiveTouchRegionsCallback updateActiveTouchRegionsCallback = this.mUpdateActiveTouchRegionsCallback;
        if (updateActiveTouchRegionsCallback != null) {
            updateActiveTouchRegionsCallback.update();
        }
    }

    public void setUpdateActiveTouchRegionsCallback(UpdateActiveTouchRegionsCallback updateActiveTouchRegionsCallback) {
        this.mUpdateActiveTouchRegionsCallback = updateActiveTouchRegionsCallback;
        notifyActiveTouchRegions();
    }

    public Map<View, Rect> getButtonTouchRegionCache() {
        FrameLayout frameLayout;
        if (this.mIsVertical) {
            frameLayout = this.mNavigationInflaterView.mVertical;
        } else {
            frameLayout = this.mNavigationInflaterView.mHorizontal;
        }
        return ((NearestTouchFrame) frameLayout.findViewById(R$id.nav_buttons)).getFullTouchableChildRegions();
    }

    public final void updateOrientationViews() {
        this.mHorizontal = findViewById(R$id.horizontal);
        this.mVertical = findViewById(R$id.vertical);
        updateCurrentView();
    }

    public boolean needsReorient(int i) {
        return this.mCurrentRotation != i;
    }

    public final void updateCurrentRotation() {
        int displayRotation = this.mConfiguration.windowConfiguration.getDisplayRotation();
        if (this.mCurrentRotation != displayRotation) {
            this.mCurrentRotation = displayRotation;
            NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
            boolean z = true;
            if (displayRotation != 1) {
                z = false;
            }
            navigationBarInflaterView.setAlternativeOrder(z);
            this.mDeadZone.onConfigurationChanged(this.mCurrentRotation);
        }
    }

    public final void updateCurrentView() {
        resetViews();
        View view = this.mIsVertical ? this.mVertical : this.mHorizontal;
        this.mCurrentView = view;
        view.setVisibility(0);
        this.mNavigationInflaterView.setVertical(this.mIsVertical);
        this.mNavigationInflaterView.updateButtonDispatchersCurrentView();
        updateLayoutTransitionsEnabled();
        updateCurrentRotation();
    }

    public final void resetViews() {
        this.mHorizontal.setVisibility(8);
        this.mVertical.setVisibility(8);
    }

    public final void updateRecentsIcon() {
        this.mDockedIcon.setRotation((!this.mDockedStackExists || !this.mIsVertical) ? 0.0f : 90.0f);
        getRecentsButton().setImageDrawable(this.mDockedStackExists ? this.mDockedIcon : this.mRecentIcon);
        this.mBarTransitions.reapplyDarkIntensity();
    }

    public void showPinningEnterExitToast(boolean z) {
        if (z) {
            this.mScreenPinningNotify.showPinningStartToast();
        } else {
            this.mScreenPinningNotify.showPinningExitToast();
        }
    }

    public void showPinningEscapeToast() {
        this.mScreenPinningNotify.showEscapeToast(this.mNavBarMode == 2, isRecentsButtonVisible());
    }

    public void reorient() {
        updateCurrentView();
        ((NavigationBarFrame) getRootView()).setDeadZone(this.mDeadZone);
        this.mBarTransitions.init();
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
        }
        updateNavButtonIcons();
        getHomeButton().setVertical(this.mIsVertical);
    }

    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        boolean z = size > 0 && size2 > size && !QuickStepContract.isGesturalMode(this.mNavBarMode);
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
            reorient();
            notifyVerticalChangedListener(z);
        }
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            if (this.mIsVertical) {
                i3 = getResources().getDimensionPixelSize(17105364);
            } else {
                i3 = getResources().getDimensionPixelSize(17105362);
            }
            this.mBarTransitions.setBackgroundFrame(new Rect(0, getResources().getDimensionPixelSize(17105358) - i3, size, size2));
        } else {
            this.mBarTransitions.setBackgroundFrame((Rect) null);
        }
        super.onMeasure(i, i2);
    }

    public int getNavBarHeight() {
        if (this.mIsVertical) {
            return getResources().getDimensionPixelSize(17105364);
        }
        return getResources().getDimensionPixelSize(17105362);
    }

    public final void notifyVerticalChangedListener(boolean z) {
        OnVerticalChangedListener onVerticalChangedListener = this.mOnVerticalChangedListener;
        if (onVerticalChangedListener != null) {
            onVerticalChangedListener.onVerticalChanged(z);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTmpLastConfiguration.updateFrom(this.mConfiguration);
        this.mFloatingRotationButton.onConfigurationChanged(this.mConfiguration.updateFrom(configuration));
        boolean updateCarMode = updateCarMode();
        updateIcons(this.mTmpLastConfiguration);
        updateRecentsIcon();
        updateCurrentRotation();
        this.mEdgeBackGestureHandler.onConfigurationChanged(this.mConfiguration);
        if (!updateCarMode) {
            Configuration configuration2 = this.mTmpLastConfiguration;
            if (configuration2.densityDpi == this.mConfiguration.densityDpi && configuration2.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
                return;
            }
        }
        updateNavButtonIcons();
    }

    public final boolean updateCarMode() {
        Configuration configuration = this.mConfiguration;
        if (configuration != null) {
            boolean z = (configuration.uiMode & 15) == 3;
            if (z != this.mInCarMode) {
                this.mInCarMode = z;
                this.mUseCarModeUi = false;
            }
        }
        return false;
    }

    public final String getResourceName(int i) {
        if (i == 0) {
            return "(null)";
        }
        try {
            return getContext().getResources().getResourceName(i);
        } catch (Resources.NotFoundException unused) {
            return "(unknown)";
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mEdgeBackGestureHandler.onNavBarAttached();
        requestApplyInsets();
        reorient();
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        if (rotationButtonController != null) {
            rotationButtonController.registerListeners();
        }
        updateNavButtonIcons();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
            this.mButtonDispatchers.valueAt(i).onDestroy();
        }
        if (this.mRotationButtonController != null) {
            this.mFloatingRotationButton.hide();
            this.mRotationButtonController.unregisterListeners();
        }
        this.mEdgeBackGestureHandler.onNavBarDetached();
    }

    public void dump(PrintWriter printWriter) {
        Rect rect = new Rect();
        Point point = new Point();
        getContextDisplay().getRealSize(point);
        printWriter.println("NavigationBarView:");
        printWriter.println(String.format("      this: " + CentralSurfaces.viewInfo(this) + " " + visibilityToString(getVisibility()), new Object[0]));
        getWindowVisibleDisplayFrame(rect);
        boolean z = rect.right > point.x || rect.bottom > point.y;
        StringBuilder sb = new StringBuilder();
        sb.append("      window: ");
        sb.append(rect.toShortString());
        sb.append(" ");
        sb.append(visibilityToString(getWindowVisibility()));
        sb.append(z ? " OFFSCREEN!" : "");
        printWriter.println(sb.toString());
        printWriter.println(String.format("      mCurrentView: id=%s (%dx%d) %s %f", new Object[]{getResourceName(getCurrentView().getId()), Integer.valueOf(getCurrentView().getWidth()), Integer.valueOf(getCurrentView().getHeight()), visibilityToString(getCurrentView().getVisibility()), Float.valueOf(getCurrentView().getAlpha())}));
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(this.mDisabledFlags);
        objArr[1] = this.mIsVertical ? "true" : "false";
        objArr[2] = Float.valueOf(getLightTransitionsController().getCurrentDarkIntensity());
        printWriter.println(String.format("      disabled=0x%08x vertical=%s darkIntensity=%.2f", objArr));
        printWriter.println("    mScreenOn: " + this.mScreenOn);
        dumpButton(printWriter, "back", getBackButton());
        dumpButton(printWriter, "home", getHomeButton());
        dumpButton(printWriter, "handle", getHomeHandle());
        dumpButton(printWriter, "rcnt", getRecentsButton());
        dumpButton(printWriter, "rota", getRotateSuggestionButton());
        dumpButton(printWriter, "a11y", getAccessibilityButton());
        dumpButton(printWriter, "ime", getImeSwitchButton());
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.dump(printWriter);
        }
        this.mBarTransitions.dump(printWriter);
        this.mContextualButtonGroup.dump(printWriter);
        this.mEdgeBackGestureHandler.dump(printWriter);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        setPadding(systemWindowInsetLeft, windowInsets.getSystemWindowInsetTop(), systemWindowInsetRight, windowInsets.getSystemWindowInsetBottom());
        this.mEdgeBackGestureHandler.setInsets(systemWindowInsetLeft, systemWindowInsetRight);
        boolean z = !QuickStepContract.isGesturalMode(this.mNavBarMode) || windowInsets.getSystemWindowInsetBottom() == 0;
        setClipChildren(z);
        setClipToPadding(z);
        return super.onApplyWindowInsets(windowInsets);
    }

    public void addPipExclusionBoundsChangeListener(Pip pip) {
        pip.addPipExclusionBoundsChangeListener(this.mPipListener);
    }

    public void removePipExclusionBoundsChangeListener(Pip pip) {
        pip.removePipExclusionBoundsChangeListener(this.mPipListener);
    }

    public void registerBackAnimation(BackAnimation backAnimation) {
        this.mEdgeBackGestureHandler.setBackAnimation(backAnimation);
    }

    public static void dumpButton(PrintWriter printWriter, String str, ButtonDispatcher buttonDispatcher) {
        printWriter.print("      " + str + ": ");
        if (buttonDispatcher == null) {
            printWriter.print("null");
        } else {
            printWriter.print(visibilityToString(buttonDispatcher.getVisibility()) + " alpha=" + buttonDispatcher.getAlpha());
        }
        printWriter.println();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Boolean bool) {
        post(new NavigationBarView$$ExternalSyntheticLambda3(this, bool));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Boolean bool) {
        this.mDockedStackExists = bool.booleanValue();
        updateRecentsIcon();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(Rect rect) {
        post(new NavigationBarView$$ExternalSyntheticLambda4(this, rect));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(Rect rect) {
        this.mEdgeBackGestureHandler.setPipStashExclusionBounds(rect);
    }
}
