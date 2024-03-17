package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import com.android.internal.policy.SystemBarUtils;
import com.android.settingslib.Utils;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.LargeScreenUtils;
import java.util.List;

public class QuickStatusBarHeader extends FrameLayout {
    public TouchAnimator mAlphaAnimator;
    public BatteryMeterView mBatteryRemainingIcon;
    public ViewGroup mClockContainer;
    public VariableDateView mClockDateView;
    public View mClockIconsSeparator;
    public Clock mClockView;
    public boolean mConfigShowBatteryEstimate;
    public View mContainer;
    public int mCutOutPaddingLeft;
    public int mCutOutPaddingRight;
    public View mDateContainer;
    public Space mDatePrivacySeparator;
    public View mDatePrivacyView;
    public View mDateView;
    public boolean mExpanded;
    public boolean mHasCenterCutout;
    public QuickQSPanel mHeaderQsPanel;
    public StatusIconContainer mIconContainer;
    public TouchAnimator mIconsAlphaAnimator;
    public TouchAnimator mIconsAlphaAnimatorFixed;
    public StatusBarContentInsetsProvider mInsetsProvider;
    public boolean mIsSingleCarrier;
    public float mKeyguardExpansionFraction;
    public View mPrivacyChip;
    public View mPrivacyContainer;
    public View mQSCarriers;
    public QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public boolean mQsDisabled;
    public View mRightLayout;
    public int mRoundedCornerPadding = 0;
    public List<String> mRssiIgnoredSlots = List.of();
    public boolean mShowClockIconsSeparator;
    public View mStatusIconsView;
    public int mTextColorPrimary = 0;
    public StatusBarIconController.TintedIconManager mTintedIconManager;
    public int mTopViewMeasureHeight;
    public TouchAnimator mTranslationAnimator;
    public boolean mUseCombinedQSHeader;
    public int mWaterfallTopInset;

    public QuickStatusBarHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getOffsetTranslation() {
        return this.mTopViewMeasureHeight;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderQsPanel = (QuickQSPanel) findViewById(R$id.quick_qs_panel);
        this.mDatePrivacyView = findViewById(R$id.quick_status_bar_date_privacy);
        this.mStatusIconsView = findViewById(R$id.quick_qs_status_icons);
        this.mQSCarriers = findViewById(R$id.carrier_group);
        this.mContainer = findViewById(R$id.qs_container);
        this.mIconContainer = (StatusIconContainer) findViewById(R$id.statusIcons);
        this.mPrivacyChip = findViewById(R$id.privacy_chip);
        this.mDateView = findViewById(R$id.date);
        this.mClockDateView = (VariableDateView) findViewById(R$id.date_clock);
        this.mClockIconsSeparator = findViewById(R$id.separator);
        this.mRightLayout = findViewById(R$id.rightLayout);
        this.mDateContainer = findViewById(R$id.date_container);
        this.mPrivacyContainer = findViewById(R$id.privacy_container);
        this.mClockContainer = (ViewGroup) findViewById(R$id.clock_container);
        this.mClockView = (Clock) findViewById(R$id.clock);
        this.mDatePrivacySeparator = (Space) findViewById(R$id.space);
        BatteryMeterView batteryMeterView = (BatteryMeterView) findViewById(R$id.batteryRemainingIcon);
        this.mBatteryRemainingIcon = batteryMeterView;
        batteryMeterView.setVisibility(8);
        this.mIconContainer.setVisibility(8);
        updateResources();
        setDatePrivacyContainersWidth(this.mContext.getResources().getConfiguration().orientation == 2);
        this.mBatteryRemainingIcon.setPercentShowMode(3);
        this.mIconsAlphaAnimatorFixed = new TouchAnimator.Builder().addFloat(this.mIconContainer, "alpha", 0.0f, 1.0f).addFloat(this.mBatteryRemainingIcon, "alpha", 0.0f, 1.0f).build();
    }

    public void onAttach(StatusBarIconController.TintedIconManager tintedIconManager, QSExpansionPathInterpolator qSExpansionPathInterpolator, List<String> list, StatusBarContentInsetsProvider statusBarContentInsetsProvider, boolean z) {
        this.mUseCombinedQSHeader = z;
        this.mTintedIconManager = tintedIconManager;
        this.mRssiIgnoredSlots = list;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(getContext(), 16842806));
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        updateAnimators();
    }

    public void setIsSingleCarrier(boolean z) {
        this.mIsSingleCarrier = z;
        updateAlphaAnimator();
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mDatePrivacyView.getMeasuredHeight() != this.mTopViewMeasureHeight) {
            this.mTopViewMeasureHeight = this.mDatePrivacyView.getMeasuredHeight();
            post(new QuickStatusBarHeader$$ExternalSyntheticLambda0(this));
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        setDatePrivacyContainersWidth(configuration.orientation == 2);
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateResources();
    }

    public final void setDatePrivacyContainersWidth(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDateContainer.getLayoutParams();
        int i = -2;
        layoutParams.width = z ? -2 : 0;
        float f = 0.0f;
        layoutParams.weight = z ? 0.0f : 1.0f;
        this.mDateContainer.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mPrivacyContainer.getLayoutParams();
        if (!z) {
            i = 0;
        }
        layoutParams2.width = i;
        if (!z) {
            f = 1.0f;
        }
        layoutParams2.weight = f;
        this.mPrivacyContainer.setLayoutParams(layoutParams2);
    }

    public final void updateBatteryMode() {
        if (!this.mConfigShowBatteryEstimate || this.mHasCenterCutout) {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        }
    }

    public void updateResources() {
        Resources resources = this.mContext.getResources();
        boolean shouldUseLargeScreenShadeHeader = LargeScreenUtils.shouldUseLargeScreenShadeHeader(resources);
        int i = 0;
        boolean z = shouldUseLargeScreenShadeHeader || this.mUseCombinedQSHeader || this.mQsDisabled;
        this.mStatusIconsView.setVisibility(z ? 8 : 0);
        View view = this.mDatePrivacyView;
        if (z) {
            i = 8;
        }
        view.setVisibility(i);
        this.mConfigShowBatteryEstimate = resources.getBoolean(R$bool.config_showBatteryEstimateQSBH);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
        int quickQsOffsetHeight = SystemBarUtils.getQuickQsOffsetHeight(this.mContext);
        this.mDatePrivacyView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mDatePrivacyView.getMinimumHeight());
        View view2 = this.mDatePrivacyView;
        view2.setLayoutParams(view2.getLayoutParams());
        this.mStatusIconsView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mStatusIconsView.getMinimumHeight());
        View view3 = this.mStatusIconsView;
        view3.setLayoutParams(view3.getLayoutParams());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.mQsDisabled) {
            layoutParams.height = this.mStatusIconsView.getLayoutParams().height;
        } else {
            layoutParams.height = -2;
        }
        setLayoutParams(layoutParams);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
        if (colorAttrDefaultColor != this.mTextColorPrimary) {
            int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(this.mContext, 16842808);
            this.mTextColorPrimary = colorAttrDefaultColor;
            this.mClockView.setTextColor(colorAttrDefaultColor);
            StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
            if (tintedIconManager != null) {
                tintedIconManager.setTint(colorAttrDefaultColor);
            }
            BatteryMeterView batteryMeterView = this.mBatteryRemainingIcon;
            int i2 = this.mTextColorPrimary;
            batteryMeterView.updateColors(i2, colorAttrDefaultColor2, i2);
        }
        this.mClockView.setTextSize(18.0f);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeaderQsPanel.getLayoutParams();
        if (shouldUseLargeScreenShadeHeader || !this.mUseCombinedQSHeader) {
            quickQsOffsetHeight = this.mContext.getResources().getDimensionPixelSize(R$dimen.qqs_layout_margin_top);
        }
        marginLayoutParams.topMargin = quickQsOffsetHeight;
        this.mHeaderQsPanel.setLayoutParams(marginLayoutParams);
        updateBatteryMode();
        updateHeadersPadding();
        updateAnimators();
        updateClockDatePadding();
    }

    public final void updateClockDatePadding() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_left_clock_starting_padding);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_left_clock_end_padding);
        Clock clock = this.mClockView;
        clock.setPaddingRelative(dimensionPixelSize, clock.getPaddingTop(), dimensionPixelSize2, this.mClockView.getPaddingBottom());
        this.mClockView.setVisibility(4);
        VariableDateView variableDateView = this.mClockDateView;
        if (variableDateView != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) variableDateView.getLayoutParams();
            marginLayoutParams.setMarginStart(dimensionPixelSize2);
            this.mClockDateView.setLayoutParams(marginLayoutParams);
        }
    }

    public final void updateAnimators() {
        Interpolator interpolator = null;
        if (this.mUseCombinedQSHeader) {
            this.mTranslationAnimator = null;
            return;
        }
        updateAlphaAnimator();
        int i = this.mTopViewMeasureHeight;
        TouchAnimator.Builder addFloat = new TouchAnimator.Builder().addFloat(this.mContainer, "translationY", 0.0f, (float) i);
        QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
        if (qSExpansionPathInterpolator != null) {
            interpolator = qSExpansionPathInterpolator.getYInterpolator();
        }
        this.mTranslationAnimator = addFloat.setInterpolator(interpolator).build();
    }

    public final void updateAlphaAnimator() {
        TouchAnimator.Builder builder;
        if (this.mUseCombinedQSHeader) {
            this.mAlphaAnimator = null;
            return;
        }
        if (this.mClockDateView != null) {
            builder = new TouchAnimator.Builder().addFloat(this.mDateView, "alpha", 0.0f, 0.0f, 1.0f).addFloat(this.mClockDateView, "alpha", 1.0f, 0.0f, 0.0f).addFloat(this.mQSCarriers, "alpha", 0.0f, 1.0f).setListener(new TouchAnimator.ListenerAdapter() {
                public void onAnimationAtEnd() {
                    super.onAnimationAtEnd();
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(8);
                }

                public void onAnimationStarted() {
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                    QuickStatusBarHeader.this.mClockDateView.setFreezeSwitching(true);
                    QuickStatusBarHeader.this.setSeparatorVisibility(false);
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                }

                public void onAnimationAtStart() {
                    super.onAnimationAtStart();
                    QuickStatusBarHeader.this.mClockDateView.setFreezeSwitching(false);
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                    QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                    quickStatusBarHeader.setSeparatorVisibility(quickStatusBarHeader.mShowClockIconsSeparator);
                    QuickStatusBarHeader.this.mIconContainer.removeIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                }
            });
        } else {
            builder = new TouchAnimator.Builder().addFloat(this.mDateView, "alpha", 0.0f, 0.0f, 1.0f).addFloat(this.mQSCarriers, "alpha", 0.0f, 1.0f).setListener(new TouchAnimator.ListenerAdapter() {
                public void onAnimationAtEnd() {
                    super.onAnimationAtEnd();
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                }

                public void onAnimationStarted() {
                    QuickStatusBarHeader.this.setSeparatorVisibility(false);
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                }

                public void onAnimationAtStart() {
                    super.onAnimationAtStart();
                    QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                    quickStatusBarHeader.setSeparatorVisibility(quickStatusBarHeader.mShowClockIconsSeparator);
                    QuickStatusBarHeader.this.mIconContainer.removeIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                }
            });
        }
        this.mAlphaAnimator = builder.build();
    }

    public void setChipVisibility(boolean z) {
        if (z) {
            TouchAnimator touchAnimator = this.mIconsAlphaAnimatorFixed;
            this.mIconsAlphaAnimator = touchAnimator;
            touchAnimator.setPosition(this.mKeyguardExpansionFraction);
            return;
        }
        this.mIconsAlphaAnimator = null;
        this.mIconContainer.setAlpha(1.0f);
        this.mBatteryRemainingIcon.setAlpha(1.0f);
    }

    public void setExpanded(boolean z, QuickQSPanelController quickQSPanelController) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            quickQSPanelController.setExpanded(z);
            updateEverything();
        }
    }

    public void setExpansion(boolean z, float f, float f2) {
        if (z) {
            f = 1.0f;
        }
        TouchAnimator touchAnimator = this.mAlphaAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
        TouchAnimator touchAnimator2 = this.mTranslationAnimator;
        if (touchAnimator2 != null) {
            touchAnimator2.setPosition(f);
        }
        TouchAnimator touchAnimator3 = this.mIconsAlphaAnimator;
        if (touchAnimator3 != null) {
            touchAnimator3.setPosition(f);
        }
        if (z) {
            setTranslationY(f2);
        } else {
            setTranslationY(0.0f);
        }
        this.mKeyguardExpansionFraction = f;
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        int i3 = 0;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.mQsDisabled) {
            this.mQsDisabled = z2;
            this.mHeaderQsPanel.setDisabledByPolicy(z2);
            View view = this.mStatusIconsView;
            if (this.mQsDisabled) {
                i3 = 8;
            }
            view.setVisibility(i3);
            updateResources();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        boolean currentRotationHasCornerCutout = this.mInsetsProvider.currentRotationHasCornerCutout();
        int i = 0;
        this.mDatePrivacyView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        this.mStatusIconsView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDatePrivacySeparator.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mClockIconsSeparator.getLayoutParams();
        if (displayCutout != null) {
            Rect boundingRectTop = displayCutout.getBoundingRectTop();
            if (boundingRectTop.isEmpty() || currentRotationHasCornerCutout) {
                layoutParams.width = 0;
                this.mDatePrivacySeparator.setVisibility(8);
                layoutParams2.width = 0;
                setSeparatorVisibility(false);
                this.mShowClockIconsSeparator = false;
                this.mHasCenterCutout = false;
            } else {
                layoutParams.width = boundingRectTop.width();
                this.mDatePrivacySeparator.setVisibility(0);
                layoutParams2.width = boundingRectTop.width();
                this.mShowClockIconsSeparator = true;
                setSeparatorVisibility(this.mKeyguardExpansionFraction == 0.0f);
                this.mHasCenterCutout = true;
            }
        }
        this.mDatePrivacySeparator.setLayoutParams(layoutParams);
        this.mClockIconsSeparator.setLayoutParams(layoutParams2);
        this.mCutOutPaddingLeft = ((Integer) statusBarContentInsetsForCurrentRotation.first).intValue();
        this.mCutOutPaddingRight = ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue();
        if (displayCutout != null) {
            i = displayCutout.getWaterfallInsets().top;
        }
        this.mWaterfallTopInset = i;
        updateBatteryMode();
        updateHeadersPadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    public final void setSeparatorVisibility(boolean z) {
        int i = 8;
        int i2 = 0;
        if (this.mClockIconsSeparator.getVisibility() != (z ? 0 : 8)) {
            this.mClockIconsSeparator.setVisibility(z ? 0 : 8);
            View view = this.mQSCarriers;
            if (!z) {
                i = 0;
            }
            view.setVisibility(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mClockContainer.getLayoutParams();
            layoutParams.width = z ? 0 : -2;
            float f = 1.0f;
            layoutParams.weight = z ? 1.0f : 0.0f;
            this.mClockContainer.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mRightLayout.getLayoutParams();
            if (!z) {
                i2 = -2;
            }
            layoutParams2.width = i2;
            if (!z) {
                f = 0.0f;
            }
            layoutParams2.weight = f;
            this.mRightLayout.setLayoutParams(layoutParams2);
        }
    }

    public final void updateHeadersPadding() {
        setContentMargins(this.mDatePrivacyView, 0, 0);
        setContentMargins(this.mStatusIconsView, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        int i = layoutParams.leftMargin;
        int i2 = layoutParams.rightMargin;
        int i3 = this.mCutOutPaddingLeft;
        int max = i3 > 0 ? Math.max(Math.max(i3, this.mRoundedCornerPadding) - i, 0) : 0;
        int i4 = this.mCutOutPaddingRight;
        int max2 = i4 > 0 ? Math.max(Math.max(i4, this.mRoundedCornerPadding) - i2, 0) : 0;
        this.mDatePrivacyView.setPadding(max, this.mWaterfallTopInset, max2, 0);
        this.mStatusIconsView.setPadding(max, this.mWaterfallTopInset, max2, 0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEverything$0() {
        setClickable(!this.mExpanded);
    }

    public void updateEverything() {
        post(new QuickStatusBarHeader$$ExternalSyntheticLambda1(this));
    }

    public final void setContentMargins(View view, int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(i);
        marginLayoutParams.setMarginEnd(i2);
        view.setLayoutParams(marginLayoutParams);
    }

    public void setExpandedScrollAmount(int i) {
        this.mStatusIconsView.setScrollY(i);
        this.mDatePrivacyView.setScrollY(i);
    }
}
