package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.R$attr;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.util.Utils;
import java.io.PrintWriter;
import java.util.ArrayList;

public class KeyguardStatusBarView extends RelativeLayout {
    public boolean mBatteryCharging;
    public BatteryMeterView mBatteryView;
    public TextView mCarrierLabel;
    public final Rect mClipRect = new Rect(0, 0, 0, 0);
    public int mCutoutSideNudge = 0;
    public View mCutoutSpace;
    public DisplayCutout mDisplayCutout;
    public final ArrayList<Rect> mEmptyTintRect = new ArrayList<>();
    public boolean mIsPrivacyDotEnabled;
    public boolean mIsUserSwitcherEnabled;
    public boolean mKeyguardUserAvatarEnabled;
    public boolean mKeyguardUserSwitcherEnabled;
    public int mLayoutState = 0;
    public int mMinDotWidth;
    public ImageView mMultiUserAvatar;
    public Pair<Integer, Integer> mPadding = new Pair<>(0, 0);
    public int mRoundedCornerPadding = 0;
    public boolean mShowPercentAvailable;
    public int mStatusBarPaddingEnd;
    public ViewGroup mStatusIconArea;
    public StatusIconContainer mStatusIconContainer;
    public View mSystemIconsContainer;
    public int mSystemIconsSwitcherHiddenExpandedMargin;
    public int mTopClipping;
    public ViewGroup mUserSwitcherContainer;

    public final int calculateMargin(int i, int i2) {
        if (i2 >= i) {
            return 0;
        }
        return i - i2;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public KeyguardStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSystemIconsContainer = findViewById(R$id.system_icons_container);
        this.mMultiUserAvatar = (ImageView) findViewById(R$id.multi_user_avatar);
        this.mCarrierLabel = (TextView) findViewById(R$id.keyguard_carrier_text);
        this.mBatteryView = (BatteryMeterView) this.mSystemIconsContainer.findViewById(R$id.battery);
        this.mCutoutSpace = findViewById(R$id.cutout_space_view);
        this.mStatusIconArea = (ViewGroup) findViewById(R$id.status_icon_area);
        this.mStatusIconContainer = (StatusIconContainer) findViewById(R$id.statusIcons);
        this.mUserSwitcherContainer = (ViewGroup) findViewById(R$id.user_switcher_container);
        this.mIsPrivacyDotEnabled = this.mContext.getResources().getBoolean(R$bool.config_enablePrivacyDot);
        loadDimens();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        loadDimens();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mMultiUserAvatar.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.multi_user_avatar_keyguard_size);
        marginLayoutParams.height = dimensionPixelSize;
        marginLayoutParams.width = dimensionPixelSize;
        this.mMultiUserAvatar.setLayoutParams(marginLayoutParams);
        updateSystemIconsLayoutParams();
        ViewGroup viewGroup = this.mStatusIconArea;
        viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), getResources().getDimensionPixelSize(R$dimen.status_bar_padding_top), this.mStatusIconArea.getPaddingEnd(), this.mStatusIconArea.getPaddingBottom());
        StatusIconContainer statusIconContainer = this.mStatusIconContainer;
        statusIconContainer.setPaddingRelative(statusIconContainer.getPaddingStart(), this.mStatusIconContainer.getPaddingTop(), getResources().getDimensionPixelSize(R$dimen.signal_cluster_battery_padding), this.mStatusIconContainer.getPaddingBottom());
        this.mCarrierLabel.setTextSize(0, (float) getResources().getDimensionPixelSize(17105582));
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mCarrierLabel.getLayoutParams();
        marginLayoutParams2.setMarginStart(calculateMargin(getResources().getDimensionPixelSize(R$dimen.keyguard_carrier_text_margin), ((Integer) this.mPadding.first).intValue()));
        this.mCarrierLabel.setLayoutParams(marginLayoutParams2);
        updateKeyguardStatusBarHeight();
    }

    public void setUserSwitcherEnabled(boolean z) {
        this.mIsUserSwitcherEnabled = z;
    }

    public final void updateKeyguardStatusBarHeight() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.height = Utils.getStatusBarHeaderHeightKeyguard(this.mContext);
        setLayoutParams(marginLayoutParams);
    }

    public void loadDimens() {
        Resources resources = getResources();
        this.mSystemIconsSwitcherHiddenExpandedMargin = resources.getDimensionPixelSize(R$dimen.system_icons_switcher_hidden_expanded_margin);
        this.mStatusBarPaddingEnd = resources.getDimensionPixelSize(R$dimen.status_bar_padding_end);
        this.mMinDotWidth = resources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_min_padding);
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(R$dimen.display_cutout_margin_consumption);
        this.mShowPercentAvailable = getContext().getResources().getBoolean(17891384);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
    }

    public final void updateVisibilities() {
        if (!this.mKeyguardUserAvatarEnabled) {
            ViewParent parent = this.mMultiUserAvatar.getParent();
            ViewGroup viewGroup = this.mStatusIconArea;
            if (parent == viewGroup) {
                viewGroup.removeView(this.mMultiUserAvatar);
            } else if (this.mMultiUserAvatar.getParent() != null) {
                getOverlay().remove(this.mMultiUserAvatar);
            }
        } else {
            boolean z = false;
            if (this.mMultiUserAvatar.getParent() == this.mStatusIconArea || this.mKeyguardUserSwitcherEnabled) {
                ViewParent parent2 = this.mMultiUserAvatar.getParent();
                ViewGroup viewGroup2 = this.mStatusIconArea;
                if (parent2 == viewGroup2 && this.mKeyguardUserSwitcherEnabled) {
                    viewGroup2.removeView(this.mMultiUserAvatar);
                }
            } else {
                if (this.mMultiUserAvatar.getParent() != null) {
                    getOverlay().remove(this.mMultiUserAvatar);
                }
                this.mStatusIconArea.addView(this.mMultiUserAvatar, 0);
            }
            if (!this.mKeyguardUserSwitcherEnabled) {
                if (this.mIsUserSwitcherEnabled) {
                    this.mMultiUserAvatar.setVisibility(0);
                } else {
                    this.mMultiUserAvatar.setVisibility(8);
                }
            }
            BatteryMeterView batteryMeterView = this.mBatteryView;
            if (this.mBatteryCharging && this.mShowPercentAvailable) {
                z = true;
            }
            batteryMeterView.setForceShowPercent(z);
        }
    }

    public final void updateSystemIconsLayoutParams() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams();
        int i = this.mStatusBarPaddingEnd;
        if (this.mKeyguardUserSwitcherEnabled) {
            i = this.mSystemIconsSwitcherHiddenExpandedMargin;
        }
        if (i != layoutParams.getMarginEnd()) {
            layoutParams.setMarginEnd(i);
            this.mSystemIconsContainer.setLayoutParams(layoutParams);
        }
    }

    public WindowInsets updateWindowInsets(WindowInsets windowInsets, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        this.mLayoutState = 0;
        if (updateLayoutConsideringCutout(statusBarContentInsetsProvider)) {
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public final boolean updateLayoutConsideringCutout(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        updateKeyguardStatusBarHeight();
        updatePadding(statusBarContentInsetsProvider);
        if (this.mDisplayCutout == null || statusBarContentInsetsProvider.currentRotationHasCornerCutout()) {
            return updateLayoutParamsNoCutout();
        }
        return updateLayoutParamsForCutout();
    }

    public final void updatePadding(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        this.mPadding = statusBarContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        setPadding((!isLayoutRtl() || !this.mIsPrivacyDotEnabled) ? ((Integer) this.mPadding.first).intValue() : Math.max(this.mMinDotWidth, ((Integer) this.mPadding.first).intValue()), i, (isLayoutRtl() || !this.mIsPrivacyDotEnabled) ? ((Integer) this.mPadding.second).intValue() : Math.max(this.mMinDotWidth, ((Integer) this.mPadding.second).intValue()), 0);
    }

    public final boolean updateLayoutParamsNoCutout() {
        if (this.mLayoutState == 2) {
            return false;
        }
        this.mLayoutState = 2;
        View view = this.mCutoutSpace;
        if (view != null) {
            view.setVisibility(8);
        }
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, R$id.status_icon_area);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams.removeRule(1);
        layoutParams.width = -2;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(R$dimen.system_icons_super_container_margin_start));
        return true;
    }

    public final boolean updateLayoutParamsForCutout() {
        if (this.mLayoutState == 1) {
            return false;
        }
        this.mLayoutState = 1;
        if (this.mCutoutSpace == null) {
            updateLayoutParamsNoCutout();
        }
        Rect rect = new Rect();
        ScreenDecorations.DisplayCutoutView.boundsFromDirection(this.mDisplayCutout, 48, rect);
        this.mCutoutSpace.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
        int i = rect.left;
        int i2 = this.mCutoutSideNudge;
        rect.left = i + i2;
        rect.right -= i2;
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        layoutParams.addRule(13);
        int i3 = R$id.cutout_space_view;
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, i3);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams2.addRule(1, i3);
        layoutParams2.width = -1;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(0);
        return true;
    }

    public void onUserInfoChanged(Drawable drawable) {
        this.mMultiUserAvatar.setImageDrawable(drawable);
    }

    public void onBatteryLevelChanged(boolean z) {
        if (this.mBatteryCharging != z) {
            this.mBatteryCharging = z;
            updateVisibilities();
        }
    }

    public void setKeyguardUserSwitcherEnabled(boolean z) {
        this.mKeyguardUserSwitcherEnabled = z;
    }

    public void setKeyguardUserAvatarEnabled(boolean z) {
        this.mKeyguardUserAvatarEnabled = z;
        updateVisibilities();
    }

    public boolean isKeyguardUserAvatarEnabled() {
        return this.mKeyguardUserAvatarEnabled;
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            this.mSystemIconsContainer.animate().cancel();
            this.mSystemIconsContainer.setTranslationX(0.0f);
            this.mMultiUserAvatar.animate().cancel();
            this.mMultiUserAvatar.setAlpha(1.0f);
            return;
        }
        updateVisibilities();
        updateSystemIconsLayoutParams();
    }

    public void onThemeChanged(StatusBarIconController.TintedIconManager tintedIconManager) {
        this.mBatteryView.setColorsFromContext(this.mContext);
        updateIconsAndTextColors(tintedIconManager);
    }

    public void onOverlayChanged() {
        int themeAttr = com.android.settingslib.Utils.getThemeAttr(this.mContext, 16842818);
        this.mCarrierLabel.setTextAppearance(themeAttr);
        this.mBatteryView.updatePercentView();
        TextView textView = (TextView) this.mUserSwitcherContainer.findViewById(R$id.current_user_name);
        if (textView != null) {
            textView.setTextAppearance(themeAttr);
        }
    }

    public final void updateIconsAndTextColors(StatusBarIconController.TintedIconManager tintedIconManager) {
        int i;
        int colorAttrDefaultColor = com.android.settingslib.Utils.getColorAttrDefaultColor(this.mContext, R$attr.wallpaperTextColor);
        Context context = this.mContext;
        if (((double) Color.luminance(colorAttrDefaultColor)) < 0.5d) {
            i = R$color.dark_mode_icon_color_single_tone;
        } else {
            i = R$color.light_mode_icon_color_single_tone;
        }
        int colorStateListDefaultColor = com.android.settingslib.Utils.getColorStateListDefaultColor(context, i);
        float f = colorAttrDefaultColor == -1 ? 0.0f : 1.0f;
        this.mCarrierLabel.setTextColor(colorStateListDefaultColor);
        TextView textView = (TextView) this.mUserSwitcherContainer.findViewById(R$id.current_user_name);
        if (textView != null) {
            textView.setTextColor(com.android.settingslib.Utils.getColorStateListDefaultColor(this.mContext, R$color.light_mode_icon_color_single_tone));
        }
        if (tintedIconManager != null) {
            tintedIconManager.setTint(colorStateListDefaultColor);
        }
        applyDarkness(R$id.battery, this.mEmptyTintRect, f, colorStateListDefaultColor);
        applyDarkness(R$id.clock, this.mEmptyTintRect, f, colorStateListDefaultColor);
    }

    public final void applyDarkness(int i, ArrayList<Rect> arrayList, float f, int i2) {
        View findViewById = findViewById(i);
        if (findViewById instanceof DarkIconDispatcher.DarkReceiver) {
            ((DarkIconDispatcher.DarkReceiver) findViewById).onDarkChanged(arrayList, f, i2);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusBarView:");
        printWriter.println("  mBatteryCharging: " + this.mBatteryCharging);
        printWriter.println("  mLayoutState: " + this.mLayoutState);
        printWriter.println("  mKeyguardUserSwitcherEnabled: " + this.mKeyguardUserSwitcherEnabled);
        BatteryMeterView batteryMeterView = this.mBatteryView;
        if (batteryMeterView != null) {
            batteryMeterView.dump(printWriter, strArr);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateClipping();
    }

    public void setTopClipping(int i) {
        if (i != this.mTopClipping) {
            this.mTopClipping = i;
            updateClipping();
        }
    }

    public final void updateClipping() {
        this.mClipRect.set(0, this.mTopClipping, getWidth(), getHeight());
        setClipBounds(this.mClipRect);
    }
}
