package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UserAvatarView;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;

public class KeyguardQsUserSwitchController extends ViewController<FrameLayout> {
    public static final AnimationProperties ANIMATION_PROPERTIES = new AnimationProperties().setDuration(360);
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public UserSwitcherController.BaseUserAdapter mAdapter;
    public int mBarState;
    public final ConfigurationController mConfigurationController;
    public ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            if (KeyguardQsUserSwitchController.this.mIsKeyguardShowing) {
                KeyguardQsUserSwitchController.this.updateView();
            }
        }
    };
    public final Context mContext;
    public UserSwitcherController.UserRecord mCurrentUser;
    public final DataSetObserver mDataSetObserver = new DataSetObserver() {
        public void onChanged() {
            if (KeyguardQsUserSwitchController.this.updateCurrentUser() || (KeyguardQsUserSwitchController.this.mIsKeyguardShowing && KeyguardQsUserSwitchController.this.mUserAvatarView.isEmpty())) {
                KeyguardQsUserSwitchController.this.updateView();
            }
        }
    };
    public final FalsingManager mFalsingManager;
    public boolean mIsKeyguardShowing;
    public final KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() {
        public void onUnlockedChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }

        public void onKeyguardShowingChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }

        public void onKeyguardFadingAwayChanged() {
            KeyguardQsUserSwitchController.this.updateKeyguardShowing(false);
        }
    };
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    public Resources mResources;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onStateChanged(int i) {
            boolean goingToFullShade = KeyguardQsUserSwitchController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = KeyguardQsUserSwitchController.this.mKeyguardStateController.isKeyguardFadingAway();
            int r2 = KeyguardQsUserSwitchController.this.mBarState;
            KeyguardQsUserSwitchController.this.mBarState = i;
            KeyguardQsUserSwitchController.this.setKeyguardQsUserSwitchVisibility(i, isKeyguardFadingAway, goingToFullShade, r2);
        }
    };
    public final UiEventLogger mUiEventLogger;
    @VisibleForTesting
    public UserAvatarView mUserAvatarView;
    public View mUserAvatarViewWithBackground;
    public final UserSwitchDialogController mUserSwitchDialogController;
    public final UserSwitcherController mUserSwitcherController;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardQsUserSwitchController(FrameLayout frameLayout, Context context, Resources resources, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController, UserSwitchDialogController userSwitchDialogController, UiEventLogger uiEventLogger) {
        super(frameLayout);
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "New KeyguardQsUserSwitchController");
        }
        this.mContext = context;
        this.mResources = resources;
        this.mUserSwitcherController = userSwitcherController;
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        this.mKeyguardStateController = keyguardStateController2;
        this.mFalsingManager = falsingManager;
        this.mConfigurationController = configurationController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(this.mView, keyguardStateController2, dozeParameters, screenOffAnimationController, false);
        this.mUserSwitchDialogController = userSwitchDialogController;
        this.mUiEventLogger = uiEventLogger;
    }

    public void onInit() {
        super.onInit();
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "onInit");
        }
        this.mUserAvatarView = (UserAvatarView) ((FrameLayout) this.mView).findViewById(R$id.kg_multi_user_avatar);
        this.mUserAvatarViewWithBackground = ((FrameLayout) this.mView).findViewById(R$id.kg_multi_user_avatar_with_background);
        this.mAdapter = new UserSwitcherController.BaseUserAdapter(this.mUserSwitcherController) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }
        };
        this.mUserAvatarView.setOnClickListener(new KeyguardQsUserSwitchController$$ExternalSyntheticLambda0(this));
        this.mUserAvatarView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, KeyguardQsUserSwitchController.this.mContext.getString(R$string.accessibility_quick_settings_choose_user_action)));
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onInit$0(View view) {
        if (!this.mFalsingManager.isFalseTap(1) && !isListAnimating()) {
            this.mUiEventLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_SWITCH_USER_TAP);
            this.mUserSwitchDialogController.showDialog(this.mUserAvatarViewWithBackground);
        }
    }

    public void onViewAttached() {
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "onViewAttached");
        }
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        this.mDataSetObserver.onChanged();
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        updateCurrentUser();
        updateKeyguardShowing(true);
    }

    public void onViewDetached() {
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "onViewDetached");
        }
        this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
    }

    public final void clearAvatar() {
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "clearAvatar");
        }
        this.mUserAvatarView.setAvatar((Bitmap) null);
    }

    @VisibleForTesting
    public void updateKeyguardShowing(boolean z) {
        boolean z2 = this.mIsKeyguardShowing;
        boolean z3 = this.mKeyguardStateController.isShowing() || this.mKeyguardStateController.isKeyguardGoingAway();
        this.mIsKeyguardShowing = z3;
        if (z2 != z3 || z) {
            if (DEBUG) {
                Log.d("KeyguardQsUserSwitchController", "updateKeyguardShowing: mIsKeyguardShowing=" + this.mIsKeyguardShowing + " forceViewUpdate=" + z);
            }
            if (this.mIsKeyguardShowing) {
                updateView();
            } else {
                clearAvatar();
            }
        }
    }

    public final boolean updateCurrentUser() {
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        this.mCurrentUser = null;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            UserSwitcherController.UserRecord item = this.mAdapter.getItem(i);
            if (item.isCurrent) {
                this.mCurrentUser = item;
                return !item.equals(userRecord);
            }
        }
        if (this.mCurrentUser != null || userRecord == null) {
            return false;
        }
        return true;
    }

    public final String getContentDescription() {
        UserInfo userInfo;
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        if (userRecord == null || (userInfo = userRecord.info) == null || TextUtils.isEmpty(userInfo.name)) {
            return this.mContext.getString(R$string.accessibility_multi_user_switch_switcher);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_user, new Object[]{this.mCurrentUser.info.name});
    }

    public final void updateView() {
        if (DEBUG) {
            Log.d("KeyguardQsUserSwitchController", "updateView");
        }
        this.mUserAvatarView.setContentDescription(getContentDescription());
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        this.mUserAvatarView.setDrawableWithBadge(getCurrentUserIcon().mutate(), userRecord != null ? userRecord.resolveId() : -10000);
    }

    public Drawable getCurrentUserIcon() {
        Drawable drawable;
        Drawable drawable2;
        UserSwitcherController.UserRecord userRecord = this.mCurrentUser;
        if (userRecord == null || userRecord.picture == null) {
            if (userRecord == null || !userRecord.isGuest) {
                drawable2 = this.mContext.getDrawable(R$drawable.ic_avatar_user);
            } else {
                drawable2 = this.mContext.getDrawable(R$drawable.ic_avatar_guest_user);
            }
            drawable = drawable2;
            drawable.setTint(this.mResources.getColor(R$color.kg_user_switcher_avatar_icon_color, this.mContext.getTheme()));
        } else {
            drawable = new CircleFramedDrawable(this.mCurrentUser.picture, (int) this.mResources.getDimension(R$dimen.kg_framed_avatar_size));
        }
        return new LayerDrawable(new Drawable[]{this.mContext.getDrawable(R$drawable.user_avatar_bg), drawable});
    }

    public int getUserIconHeight() {
        return this.mUserAvatarView.getHeight();
    }

    public void setKeyguardQsUserSwitchVisibility(int i, boolean z, boolean z2, int i2) {
        this.mKeyguardVisibilityHelper.setViewVisibility(i, z, z2, i2);
    }

    public void updatePosition(int i, int i2, boolean z) {
        AnimationProperties animationProperties = ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((FrameLayout) this.mView, AnimatableProperty.Y, (float) i2, animationProperties, z);
        PropertyAnimator.setProperty((FrameLayout) this.mView, AnimatableProperty.TRANSLATION_X, (float) (-Math.abs(i)), animationProperties, z);
    }

    public void setAlpha(float f) {
        if (!this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            ((FrameLayout) this.mView).setAlpha(f);
        }
    }

    public final boolean isListAnimating() {
        return this.mKeyguardVisibilityHelper.isVisibilityAnimating();
    }
}
