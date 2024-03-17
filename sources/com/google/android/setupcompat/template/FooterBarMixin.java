package com.google.android.setupcompat.template;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.R$id;
import com.google.android.setupcompat.R$layout;
import com.google.android.setupcompat.R$style;
import com.google.android.setupcompat.R$styleable;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterButton;
import java.util.concurrent.atomic.AtomicInteger;

public class FooterBarMixin implements Mixin {
    public static final AtomicInteger nextGeneratedId = new AtomicInteger(1);
    public final boolean applyDynamicColor;
    public final boolean applyPartnerResources;
    public LinearLayout buttonContainer;
    public final Context context;
    public int defaultPadding;
    public int footerBarPaddingBottom;
    public int footerBarPaddingEnd;
    public int footerBarPaddingStart;
    public int footerBarPaddingTop;
    public final int footerBarPrimaryBackgroundColor;
    public final int footerBarSecondaryBackgroundColor;
    public final ViewStub footerStub;
    public boolean isSecondaryButtonInPrimaryStyle = false;
    public final FooterBarMixinMetrics metrics;
    public FooterButton primaryButton;
    public int primaryButtonId;
    public FooterButtonPartnerConfig primaryButtonPartnerConfigForTesting;
    public boolean removeFooterBarWhenEmpty = true;
    public FooterButton secondaryButton;
    public int secondaryButtonId;
    public FooterButtonPartnerConfig secondaryButtonPartnerConfigForTesting;
    public final boolean useFullDynamicColor;

    public final FooterButton.OnButtonEventListener createButtonEventListener(final int i) {
        return new FooterButton.OnButtonEventListener() {
            public void onEnabledChanged(boolean z) {
                Button button;
                PartnerConfig partnerConfig;
                PartnerConfig partnerConfig2;
                LinearLayout linearLayout = FooterBarMixin.this.buttonContainer;
                if (linearLayout != null && (button = (Button) linearLayout.findViewById(i)) != null) {
                    button.setEnabled(z);
                    FooterBarMixin footerBarMixin = FooterBarMixin.this;
                    if (footerBarMixin.applyPartnerResources && !footerBarMixin.applyDynamicColor) {
                        if (i == footerBarMixin.primaryButtonId || FooterBarMixin.this.isSecondaryButtonInPrimaryStyle) {
                            partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR;
                        } else {
                            partnerConfig = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR;
                        }
                        if (i == FooterBarMixin.this.primaryButtonId || FooterBarMixin.this.isSecondaryButtonInPrimaryStyle) {
                            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR;
                        } else {
                            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_DISABLED_TEXT_COLOR;
                        }
                        footerBarMixin.updateButtonTextColorWithStates(button, partnerConfig, partnerConfig2);
                    }
                }
            }
        };
    }

    public FooterBarMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        FooterBarMixinMetrics footerBarMixinMetrics = new FooterBarMixinMetrics();
        this.metrics = footerBarMixinMetrics;
        Context context2 = templateLayout.getContext();
        this.context = context2;
        this.footerStub = (ViewStub) templateLayout.findManagedViewById(R$id.suc_layout_footer);
        boolean z = templateLayout instanceof PartnerCustomizationLayout;
        this.applyPartnerResources = z && ((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource();
        this.applyDynamicColor = z && ((PartnerCustomizationLayout) templateLayout).shouldApplyDynamicColor();
        this.useFullDynamicColor = z && ((PartnerCustomizationLayout) templateLayout).useFullDynamicColor();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.SucFooterBarMixin, i, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SucFooterBarMixin_sucFooterBarPaddingVertical, 0);
        this.defaultPadding = dimensionPixelSize;
        this.footerBarPaddingTop = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SucFooterBarMixin_sucFooterBarPaddingTop, dimensionPixelSize);
        this.footerBarPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SucFooterBarMixin_sucFooterBarPaddingBottom, this.defaultPadding);
        this.footerBarPaddingStart = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SucFooterBarMixin_sucFooterBarPaddingStart, 0);
        this.footerBarPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SucFooterBarMixin_sucFooterBarPaddingEnd, 0);
        this.footerBarPrimaryBackgroundColor = obtainStyledAttributes.getColor(R$styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterBackground, 0);
        this.footerBarSecondaryBackgroundColor = obtainStyledAttributes.getColor(R$styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterBackground, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.SucFooterBarMixin_sucFooterBarPrimaryFooterButton, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(R$styleable.SucFooterBarMixin_sucFooterBarSecondaryFooterButton, 0);
        obtainStyledAttributes.recycle();
        FooterButtonInflater footerButtonInflater = new FooterButtonInflater(context2);
        if (resourceId2 != 0) {
            setSecondaryButton(footerButtonInflater.inflate(resourceId2));
            footerBarMixinMetrics.logPrimaryButtonInitialStateVisibility(true, true);
        }
        if (resourceId != 0) {
            setPrimaryButton(footerButtonInflater.inflate(resourceId));
            footerBarMixinMetrics.logSecondaryButtonInitialStateVisibility(true, true);
        }
        FooterButtonStyleUtils.clearSavedDefaultTextColor();
    }

    public boolean isFooterButtonAlignedEnd() {
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(this.context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_BUTTON_ALIGNED_END;
        if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
            return PartnerConfigHelper.get(this.context).getBoolean(this.context, partnerConfig, false);
        }
        return false;
    }

    public boolean isFooterButtonsEvenlyWeighted() {
        if (!this.isSecondaryButtonInPrimaryStyle) {
            return false;
        }
        PartnerConfigHelper.get(this.context);
        return PartnerConfigHelper.isNeutralButtonStyleEnabled(this.context);
    }

    public final View addSpace() {
        LinearLayout ensureFooterInflated = ensureFooterInflated();
        View view = new View(this.context);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
        view.setVisibility(4);
        ensureFooterInflated.addView(view);
        return view;
    }

    public final LinearLayout ensureFooterInflated() {
        if (this.buttonContainer == null) {
            if (this.footerStub != null) {
                LinearLayout linearLayout = (LinearLayout) inflateFooter(R$layout.suc_footer_button_bar);
                this.buttonContainer = linearLayout;
                onFooterBarInflated(linearLayout);
                onFooterBarApplyPartnerResource(this.buttonContainer);
            } else {
                throw new IllegalStateException("Footer stub is not found in this template");
            }
        }
        return this.buttonContainer;
    }

    public void onFooterBarInflated(LinearLayout linearLayout) {
        if (linearLayout != null) {
            linearLayout.setId(View.generateViewId());
            updateFooterBarPadding(linearLayout, this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
            if (isFooterButtonAlignedEnd()) {
                linearLayout.setGravity(8388629);
            }
        }
    }

    public void onFooterBarApplyPartnerResource(LinearLayout linearLayout) {
        int dimension;
        if (linearLayout != null && this.applyPartnerResources) {
            if (!this.useFullDynamicColor) {
                linearLayout.setBackgroundColor(PartnerConfigHelper.get(this.context).getColor(this.context, PartnerConfig.CONFIG_FOOTER_BAR_BG_COLOR));
            }
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(this.context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_TOP;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                this.footerBarPaddingTop = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, partnerConfig);
            }
            PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(this.context);
            PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_BOTTOM;
            if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                this.footerBarPaddingBottom = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, partnerConfig2);
            }
            PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(this.context);
            PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_FOOTER_BAR_PADDING_START;
            if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig3)) {
                this.footerBarPaddingStart = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, partnerConfig3);
            }
            PartnerConfigHelper partnerConfigHelper4 = PartnerConfigHelper.get(this.context);
            PartnerConfig partnerConfig4 = PartnerConfig.CONFIG_FOOTER_BAR_PADDING_END;
            if (partnerConfigHelper4.isPartnerConfigAvailable(partnerConfig4)) {
                this.footerBarPaddingEnd = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, partnerConfig4);
            }
            updateFooterBarPadding(linearLayout, this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
            PartnerConfigHelper partnerConfigHelper5 = PartnerConfigHelper.get(this.context);
            PartnerConfig partnerConfig5 = PartnerConfig.CONFIG_FOOTER_BAR_MIN_HEIGHT;
            if (partnerConfigHelper5.isPartnerConfigAvailable(partnerConfig5) && (dimension = (int) PartnerConfigHelper.get(this.context).getDimension(this.context, partnerConfig5)) > 0) {
                linearLayout.setMinimumHeight(dimension);
            }
        }
    }

    @SuppressLint({"InflateParams"})
    public FooterActionButton createThemedButton(Context context2, int i) {
        return (FooterActionButton) LayoutInflater.from(new ContextThemeWrapper(context2, i)).inflate(R$layout.suc_button, (ViewGroup) null, false);
    }

    public void setPrimaryButton(FooterButton footerButton) {
        Preconditions.ensureOnMainThread("setPrimaryButton");
        ensureFooterInflated();
        FooterButtonPartnerConfig.Builder builder = new FooterButtonPartnerConfig.Builder(footerButton);
        int i = R$style.SucPartnerCustomizationButton_Primary;
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        FooterButtonPartnerConfig build = builder.setPartnerTheme(getPartnerTheme(footerButton, i, partnerConfig)).setButtonBackgroundConfig(partnerConfig).setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA).setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR).setButtonDisableTextColorConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR).setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType())).setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS).setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA).setTextColorConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR).setMarginStartConfig(PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_MARGIN_START).setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE).setButtonMinHeight(PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT).setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY).setTextStyleConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE).build();
        FooterActionButton inflateButton = inflateButton(footerButton, build);
        this.primaryButtonId = inflateButton.getId();
        inflateButton.setPrimaryButtonStyle(true);
        this.primaryButton = footerButton;
        this.primaryButtonPartnerConfigForTesting = build;
        onFooterButtonInflated(inflateButton, this.footerBarPrimaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, build);
        repopulateButtons();
    }

    public FooterButton getPrimaryButton() {
        return this.primaryButton;
    }

    public Button getPrimaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.primaryButtonId);
    }

    public boolean isPrimaryButtonVisible() {
        return getPrimaryButtonView() != null && getPrimaryButtonView().getVisibility() == 0;
    }

    public void setSecondaryButton(FooterButton footerButton) {
        setSecondaryButton(footerButton, false);
    }

    public void setSecondaryButton(FooterButton footerButton, boolean z) {
        int i;
        PartnerConfig partnerConfig;
        PartnerConfig partnerConfig2;
        PartnerConfig partnerConfig3;
        PartnerConfig partnerConfig4;
        Preconditions.ensureOnMainThread("setSecondaryButton");
        this.isSecondaryButtonInPrimaryStyle = z;
        ensureFooterInflated();
        FooterButtonPartnerConfig.Builder builder = new FooterButtonPartnerConfig.Builder(footerButton);
        if (z) {
            i = R$style.SucPartnerCustomizationButton_Primary;
        } else {
            i = R$style.SucPartnerCustomizationButton_Secondary;
        }
        if (z) {
            partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        } else {
            partnerConfig = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR;
        }
        FooterButtonPartnerConfig.Builder partnerTheme = builder.setPartnerTheme(getPartnerTheme(footerButton, i, partnerConfig));
        if (z) {
            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        } else {
            partnerConfig2 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR;
        }
        FooterButtonPartnerConfig.Builder buttonDisableBackgroundConfig = partnerTheme.setButtonBackgroundConfig(partnerConfig2).setButtonDisableAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA).setButtonDisableBackgroundConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR);
        if (z) {
            partnerConfig3 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR;
        } else {
            partnerConfig3 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_DISABLED_TEXT_COLOR;
        }
        FooterButtonPartnerConfig.Builder buttonRippleColorAlphaConfig = buttonDisableBackgroundConfig.setButtonDisableTextColorConfig(partnerConfig3).setButtonIconConfig(getDrawablePartnerConfig(footerButton.getButtonType())).setButtonRadiusConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS).setButtonRippleColorAlphaConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA);
        if (z) {
            partnerConfig4 = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR;
        } else {
            partnerConfig4 = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR;
        }
        FooterButtonPartnerConfig build = buttonRippleColorAlphaConfig.setTextColorConfig(partnerConfig4).setMarginStartConfig(PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_MARGIN_START).setTextSizeConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE).setButtonMinHeight(PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT).setTextTypeFaceConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY).setTextStyleConfig(PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE).build();
        FooterActionButton inflateButton = inflateButton(footerButton, build);
        this.secondaryButtonId = inflateButton.getId();
        inflateButton.setPrimaryButtonStyle(z);
        this.secondaryButton = footerButton;
        this.secondaryButtonPartnerConfigForTesting = build;
        onFooterButtonInflated(inflateButton, this.footerBarSecondaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, build);
        repopulateButtons();
    }

    public void repopulateButtons() {
        LinearLayout ensureFooterInflated = ensureFooterInflated();
        Button primaryButtonView = getPrimaryButtonView();
        Button secondaryButtonView = getSecondaryButtonView();
        ensureFooterInflated.removeAllViews();
        boolean isFooterButtonsEvenlyWeighted = isFooterButtonsEvenlyWeighted();
        if ((this.context.getResources().getConfiguration().orientation == 2) && isFooterButtonsEvenlyWeighted && isFooterButtonAlignedEnd()) {
            addSpace();
        }
        if (secondaryButtonView != null) {
            if (this.isSecondaryButtonInPrimaryStyle) {
                updateFooterBarPadding(ensureFooterInflated, ensureFooterInflated.getPaddingRight(), ensureFooterInflated.getPaddingTop(), ensureFooterInflated.getPaddingRight(), ensureFooterInflated.getPaddingBottom());
            }
            ensureFooterInflated.addView(secondaryButtonView);
        }
        if (!isFooterButtonAlignedEnd()) {
            addSpace();
        }
        if (primaryButtonView != null) {
            ensureFooterInflated.addView(primaryButtonView);
        }
        setEvenlyWeightedButtons(primaryButtonView, secondaryButtonView, isFooterButtonsEvenlyWeighted);
    }

    public final void setEvenlyWeightedButtons(Button button, Button button2, boolean z) {
        LinearLayout.LayoutParams layoutParams;
        LinearLayout.LayoutParams layoutParams2;
        if (button == null || button2 == null || !z) {
            if (!(button == null || (layoutParams2 = (LinearLayout.LayoutParams) button.getLayoutParams()) == null)) {
                layoutParams2.width = -2;
                layoutParams2.weight = 0.0f;
                button.setLayoutParams(layoutParams2);
            }
            if (button2 != null && (layoutParams = (LinearLayout.LayoutParams) button2.getLayoutParams()) != null) {
                layoutParams.width = -2;
                layoutParams.weight = 0.0f;
                button2.setLayoutParams(layoutParams);
                return;
            }
            return;
        }
        button.measure(0, 0);
        int measuredWidth = button.getMeasuredWidth();
        button2.measure(0, 0);
        int max = Math.max(measuredWidth, button2.getMeasuredWidth());
        button.getLayoutParams().width = max;
        button2.getLayoutParams().width = max;
    }

    public void onFooterButtonInflated(Button button, int i) {
        if (i != 0) {
            FooterButtonStyleUtils.updateButtonBackground(button, i);
        }
        this.buttonContainer.addView(button);
        autoSetButtonBarVisibility();
    }

    public final int getPartnerTheme(FooterButton footerButton, int i, PartnerConfig partnerConfig) {
        int theme = footerButton.getTheme();
        if (footerButton.getTheme() != 0 && !this.applyPartnerResources) {
            i = theme;
        }
        if (!this.applyPartnerResources) {
            return i;
        }
        if (PartnerConfigHelper.get(this.context).getColor(this.context, partnerConfig) == 0) {
            return R$style.SucPartnerCustomizationButton_Secondary;
        }
        return R$style.SucPartnerCustomizationButton_Primary;
    }

    public LinearLayout getButtonContainer() {
        return this.buttonContainer;
    }

    public FooterButton getSecondaryButton() {
        return this.secondaryButton;
    }

    public final void autoSetButtonBarVisibility() {
        Button primaryButtonView = getPrimaryButtonView();
        Button secondaryButtonView = getSecondaryButtonView();
        boolean z = true;
        int i = 0;
        boolean z2 = primaryButtonView != null && primaryButtonView.getVisibility() == 0;
        if (secondaryButtonView == null || secondaryButtonView.getVisibility() != 0) {
            z = false;
        }
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            if (!z2 && !z) {
                i = this.removeFooterBarWhenEmpty ? 8 : 4;
            }
            linearLayout.setVisibility(i);
        }
    }

    public int getVisibility() {
        return this.buttonContainer.getVisibility();
    }

    public Button getSecondaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.secondaryButtonId);
    }

    public boolean isSecondaryButtonVisible() {
        return getSecondaryButtonView() != null && getSecondaryButtonView().getVisibility() == 0;
    }

    public final FooterActionButton inflateButton(FooterButton footerButton, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        FooterActionButton createThemedButton = createThemedButton(this.context, footerButtonPartnerConfig.getPartnerTheme());
        createThemedButton.setId(View.generateViewId());
        createThemedButton.setText(footerButton.getText());
        createThemedButton.setOnClickListener(footerButton);
        createThemedButton.setVisibility(footerButton.getVisibility());
        createThemedButton.setEnabled(footerButton.isEnabled());
        createThemedButton.setFooterButton(footerButton);
        footerButton.setOnButtonEventListener(createButtonEventListener(createThemedButton.getId()));
        return createThemedButton;
    }

    @TargetApi(29)
    public final void onFooterButtonApplyPartnerResource(Button button, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        if (this.applyPartnerResources) {
            FooterButtonStyleUtils.applyButtonPartnerResources(this.context, button, this.applyDynamicColor, button.getId() == this.primaryButtonId, footerButtonPartnerConfig);
            if (!this.applyDynamicColor) {
                updateButtonTextColorWithStates(button, footerButtonPartnerConfig.getButtonTextColorConfig(), footerButtonPartnerConfig.getButtonDisableTextColorConfig());
            }
        }
    }

    public final void updateButtonTextColorWithStates(Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2) {
        if (button.isEnabled()) {
            FooterButtonStyleUtils.updateButtonTextEnabledColorWithPartnerConfig(this.context, button, partnerConfig);
        } else {
            FooterButtonStyleUtils.updateButtonTextDisabledColorWithPartnerConfig(this.context, button, partnerConfig2);
        }
    }

    public static PartnerConfig getDrawablePartnerConfig(int i) {
        switch (i) {
            case 1:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_ADD_ANOTHER;
            case 2:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CANCEL;
            case 3:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CLEAR;
            case 4:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_DONE;
            case 5:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_NEXT;
            case 6:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_OPT_IN;
            case 7:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_SKIP;
            case 8:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_STOP;
            default:
                return null;
        }
    }

    public View inflateFooter(int i) {
        this.footerStub.setLayoutInflater(LayoutInflater.from(new ContextThemeWrapper(this.context, R$style.SucPartnerCustomizationButtonBar_Stackable)));
        this.footerStub.setLayoutResource(i);
        return this.footerStub.inflate();
    }

    public final void updateFooterBarPadding(LinearLayout linearLayout, int i, int i2, int i3, int i4) {
        if (linearLayout != null) {
            linearLayout.setPadding(i, i2, i3, i4);
        }
    }

    public int getPaddingTop() {
        LinearLayout linearLayout = this.buttonContainer;
        return linearLayout != null ? linearLayout.getPaddingTop() : this.footerStub.getPaddingTop();
    }

    public int getPaddingBottom() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            return linearLayout.getPaddingBottom();
        }
        return this.footerStub.getPaddingBottom();
    }

    public void onAttachedToWindow() {
        this.metrics.logPrimaryButtonInitialStateVisibility(isPrimaryButtonVisible(), false);
        this.metrics.logSecondaryButtonInitialStateVisibility(isSecondaryButtonVisible(), false);
    }

    public void onDetachedFromWindow() {
        this.metrics.updateButtonVisibility(isPrimaryButtonVisible(), isSecondaryButtonVisible());
    }

    @TargetApi(29)
    public PersistableBundle getLoggingMetrics() {
        return this.metrics.getMetrics();
    }
}
