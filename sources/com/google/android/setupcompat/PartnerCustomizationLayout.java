package com.google.android.setupcompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import com.google.android.setupcompat.internal.LifecycleFragment;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.SetupCompatServiceInvoker;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.SetupMetricsLogger;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupcompat.template.SystemNavBarMixin;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupcompat.util.Logger;
import com.google.android.setupcompat.util.WizardManagerHelper;

public class PartnerCustomizationLayout extends TemplateLayout {
    public static final Logger LOG = new Logger("PartnerCustomizationLayout");
    public Activity activity;
    public boolean useDynamicColor;
    public boolean useFullDynamicColorAttr;
    public boolean usePartnerResourceAttr;
    public final ViewTreeObserver.OnWindowFocusChangeListener windowFocusChangeListener;

    public boolean enablePartnerResourceLoading() {
        return true;
    }

    public PartnerCustomizationLayout(Context context) {
        this(context, 0, 0);
    }

    public PartnerCustomizationLayout(Context context, int i) {
        this(context, i, 0);
    }

    public PartnerCustomizationLayout(Context context, int i, int i2) {
        super(context, i, i2);
        this.windowFocusChangeListener = new PartnerCustomizationLayout$$ExternalSyntheticLambda0(this);
        init((AttributeSet) null, R$attr.sucLayoutTheme);
    }

    public PartnerCustomizationLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.windowFocusChangeListener = new PartnerCustomizationLayout$$ExternalSyntheticLambda0(this);
        init(attributeSet, R$attr.sucLayoutTheme);
    }

    @TargetApi(11)
    public PartnerCustomizationLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.windowFocusChangeListener = new PartnerCustomizationLayout$$ExternalSyntheticLambda0(this);
        init(attributeSet, i);
    }

    public final void init(AttributeSet attributeSet, int i) {
        Class<SystemNavBarMixin> cls = SystemNavBarMixin.class;
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SucPartnerCustomizationLayout, i, 0);
            boolean z = obtainStyledAttributes.getBoolean(R$styleable.SucPartnerCustomizationLayout_sucLayoutFullscreen, true);
            obtainStyledAttributes.recycle();
            if (z) {
                setSystemUiVisibility(1024);
            }
            registerMixin(StatusBarMixin.class, new StatusBarMixin(this, this.activity.getWindow(), attributeSet, i));
            registerMixin(cls, new SystemNavBarMixin(this, this.activity.getWindow()));
            registerMixin(FooterBarMixin.class, new FooterBarMixin(this, attributeSet, i));
            ((SystemNavBarMixin) getMixin(cls)).applyPartnerCustomizations(attributeSet, i);
            this.activity.getWindow().addFlags(Integer.MIN_VALUE);
            this.activity.getWindow().clearFlags(67108864);
            this.activity.getWindow().clearFlags(134217728);
        }
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.partner_customization_layout;
        }
        return inflateTemplate(layoutInflater, 0, i);
    }

    public void onBeforeTemplateInflated(AttributeSet attributeSet, int i) {
        boolean z = true;
        this.usePartnerResourceAttr = true;
        Activity lookupActivityFromContext = lookupActivityFromContext(getContext());
        this.activity = lookupActivityFromContext;
        boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(lookupActivityFromContext.getIntent());
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SucPartnerCustomizationLayout, i, 0);
        int i2 = R$styleable.SucPartnerCustomizationLayout_sucUsePartnerResource;
        if (!obtainStyledAttributes.hasValue(i2)) {
            LOG.e("Attribute sucUsePartnerResource not found in " + this.activity.getComponentName());
        }
        if (!isAnySetupWizard && !obtainStyledAttributes.getBoolean(i2, true)) {
            z = false;
        }
        this.usePartnerResourceAttr = z;
        int i3 = R$styleable.SucPartnerCustomizationLayout_sucFullDynamicColor;
        this.useDynamicColor = obtainStyledAttributes.hasValue(i3);
        this.useFullDynamicColorAttr = obtainStyledAttributes.getBoolean(i3, false);
        obtainStyledAttributes.recycle();
        LOG.atDebug("activity=" + this.activity.getClass().getSimpleName() + " isSetupFlow=" + isAnySetupWizard + " enablePartnerResourceLoading=" + enablePartnerResourceLoading() + " usePartnerResourceAttr=" + this.usePartnerResourceAttr + " useDynamicColor=" + this.useDynamicColor + " useFullDynamicColorAttr=" + this.useFullDynamicColorAttr);
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = R$id.suc_layout_content;
        }
        return super.findContainer(i);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LifecycleFragment.attachNow(this.activity);
        if (WizardManagerHelper.isAnySetupWizard(this.activity.getIntent())) {
            getViewTreeObserver().addOnWindowFocusChangeListener(this.windowFocusChangeListener);
        }
        ((FooterBarMixin) getMixin(FooterBarMixin.class)).onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        PersistableBundle persistableBundle;
        PersistableBundle persistableBundle2;
        super.onDetachedFromWindow();
        if (WizardManagerHelper.isAnySetupWizard(this.activity.getIntent())) {
            FooterBarMixin footerBarMixin = (FooterBarMixin) getMixin(FooterBarMixin.class);
            footerBarMixin.onDetachedFromWindow();
            FooterButton primaryButton = footerBarMixin.getPrimaryButton();
            FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
            if (primaryButton != null) {
                persistableBundle = primaryButton.getMetrics("PrimaryFooterButton");
            } else {
                persistableBundle = PersistableBundle.EMPTY;
            }
            if (secondaryButton != null) {
                persistableBundle2 = secondaryButton.getMetrics("SecondaryFooterButton");
            } else {
                persistableBundle2 = PersistableBundle.EMPTY;
            }
            SetupMetricsLogger.logCustomEvent(getContext(), CustomEvent.create(MetricKey.get("SetupCompatMetrics", this.activity), PersistableBundles.mergeBundles(footerBarMixin.getLoggingMetrics(), persistableBundle, persistableBundle2)));
        }
        getViewTreeObserver().removeOnWindowFocusChangeListener(this.windowFocusChangeListener);
    }

    public static Activity lookupActivityFromContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return lookupActivityFromContext(((ContextWrapper) context).getBaseContext());
        }
        throw new IllegalArgumentException("Cannot find instance of Activity in parent tree");
    }

    public boolean shouldApplyPartnerResource() {
        if (enablePartnerResourceLoading() && this.usePartnerResourceAttr && PartnerConfigHelper.get(getContext()).isAvailable()) {
            return true;
        }
        return false;
    }

    public boolean shouldApplyDynamicColor() {
        if (this.useDynamicColor && BuildCompatUtils.isAtLeastS() && PartnerConfigHelper.get(getContext()).isAvailable()) {
            return true;
        }
        return false;
    }

    public boolean useFullDynamicColor() {
        return shouldApplyDynamicColor() && this.useFullDynamicColorAttr;
    }

    public final void onFocusChanged(boolean z) {
        SetupCompatServiceInvoker.get(getContext()).onFocusStatusChanged(FocusChangedMetricHelper.getScreenName(this.activity), FocusChangedMetricHelper.getExtraBundle(this.activity, this, z));
    }
}
