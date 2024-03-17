package com.google.android.setupdesign;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.template.IconMixin;
import com.google.android.setupdesign.template.IllustrationProgressMixin;
import com.google.android.setupdesign.template.ProgressBarMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.template.ScrollViewScrollHandlingDelegate;
import com.google.android.setupdesign.util.DescriptionStyler;
import com.google.android.setupdesign.util.LayoutStyler;

public class GlifLayout extends PartnerCustomizationLayout {
    public boolean applyPartnerHeavyThemeResource;
    public ColorStateList backgroundBaseColor;
    public boolean backgroundPatterned;
    public ColorStateList primaryColor;

    public GlifLayout(Context context) {
        this(context, 0, 0);
    }

    public GlifLayout(Context context, int i) {
        this(context, i, 0);
    }

    public GlifLayout(Context context, int i, int i2) {
        super(context, i, i2);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init((AttributeSet) null, R$attr.sudLayoutTheme);
    }

    public GlifLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, R$attr.sudLayoutTheme);
    }

    @TargetApi(11)
    public GlifLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, i);
    }

    private void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SudGlifLayout, i, 0);
            this.applyPartnerHeavyThemeResource = shouldApplyPartnerResource() && obtainStyledAttributes.getBoolean(R$styleable.SudGlifLayout_sudUsePartnerHeavyTheme, false);
            registerMixin(HeaderMixin.class, new HeaderMixin(this, attributeSet, i));
            registerMixin(DescriptionMixin.class, new DescriptionMixin(this, attributeSet, i));
            registerMixin(IconMixin.class, new IconMixin(this, attributeSet, i));
            registerMixin(ProgressBarMixin.class, new ProgressBarMixin(this, attributeSet, i));
            registerMixin(IllustrationProgressMixin.class, new IllustrationProgressMixin(this));
            RequireScrollMixin requireScrollMixin = new RequireScrollMixin(this);
            registerMixin(RequireScrollMixin.class, requireScrollMixin);
            ScrollView scrollView = getScrollView();
            if (scrollView != null) {
                requireScrollMixin.setScrollHandlingDelegate(new ScrollViewScrollHandlingDelegate(requireScrollMixin, scrollView));
            }
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R$styleable.SudGlifLayout_sudColorPrimary);
            if (colorStateList != null) {
                setPrimaryColor(colorStateList);
            }
            if (shouldApplyPartnerHeavyThemeResource()) {
                updateContentBackgroundColorWithPartnerConfig();
            }
            View findManagedViewById = findManagedViewById(R$id.sud_layout_content);
            if (findManagedViewById != null) {
                if (shouldApplyPartnerResource()) {
                    LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById);
                }
                if (!(this instanceof GlifPreferenceLayout)) {
                    tryApplyPartnerCustomizationContentPaddingTopStyle(findManagedViewById);
                }
            }
            updateLandscapeMiddleHorizontalSpacing();
            setBackgroundBaseColor(obtainStyledAttributes.getColorStateList(R$styleable.SudGlifLayout_sudBackgroundBaseColor));
            setBackgroundPatterned(obtainStyledAttributes.getBoolean(R$styleable.SudGlifLayout_sudBackgroundPatterned, true));
            int resourceId = obtainStyledAttributes.getResourceId(R$styleable.SudGlifLayout_sudStickyHeader, 0);
            if (resourceId != 0) {
                inflateStickyHeader(resourceId);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        ((IconMixin) getMixin(IconMixin.class)).tryApplyPartnerCustomizationStyle();
        ((HeaderMixin) getMixin(HeaderMixin.class)).tryApplyPartnerCustomizationStyle();
        ((DescriptionMixin) getMixin(DescriptionMixin.class)).tryApplyPartnerCustomizationStyle();
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).tryApplyPartnerCustomizationStyle();
        tryApplyPartnerCustomizationStyleToShortDescription();
    }

    public final void tryApplyPartnerCustomizationStyleToShortDescription() {
        TextView textView = (TextView) findManagedViewById(R$id.sud_layout_description);
        if (textView == null) {
            return;
        }
        if (this.applyPartnerHeavyThemeResource) {
            DescriptionStyler.applyPartnerCustomizationHeavyStyle(textView);
        } else if (shouldApplyPartnerResource()) {
            DescriptionStyler.applyPartnerCustomizationLightStyle(textView);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00d3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateLandscapeMiddleHorizontalSpacing() {
        /*
            r8 = this;
            android.content.res.Resources r0 = r8.getResources()
            int r1 = com.google.android.setupdesign.R$dimen.sud_glif_land_middle_horizontal_spacing
            int r0 = r0.getDimensionPixelSize(r1)
            boolean r1 = r8.shouldApplyPartnerResource()
            if (r1 == 0) goto L_0x0031
            android.content.Context r1 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r1 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r1)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r2 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAND_MIDDLE_HORIZONTAL_SPACING
            boolean r1 = r1.isPartnerConfigAvailable(r2)
            if (r1 == 0) goto L_0x0031
            android.content.Context r0 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r0 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            android.content.Context r1 = r8.getContext()
            float r0 = r0.getDimension(r1, r2)
            int r0 = (int) r0
        L_0x0031:
            int r1 = com.google.android.setupdesign.R$id.sud_landscape_header_area
            android.view.View r1 = r8.findManagedViewById(r1)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x008b
            boolean r4 = r8.shouldApplyPartnerResource()
            if (r4 == 0) goto L_0x0063
            android.content.Context r4 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r4 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r4)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r5 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAYOUT_MARGIN_END
            boolean r4 = r4.isPartnerConfigAvailable(r5)
            if (r4 == 0) goto L_0x0063
            android.content.Context r4 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r4 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r4)
            android.content.Context r6 = r8.getContext()
            float r4 = r4.getDimension(r6, r5)
            int r4 = (int) r4
            goto L_0x0079
        L_0x0063:
            android.content.Context r4 = r8.getContext()
            int[] r5 = new int[r2]
            int r6 = com.google.android.setupdesign.R$attr.sudMarginEnd
            r5[r3] = r6
            android.content.res.TypedArray r4 = r4.obtainStyledAttributes(r5)
            int r5 = r4.getDimensionPixelSize(r3, r3)
            r4.recycle()
            r4 = r5
        L_0x0079:
            int r5 = r0 / 2
            int r5 = r5 - r4
            int r4 = r1.getPaddingStart()
            int r6 = r1.getPaddingTop()
            int r7 = r1.getPaddingBottom()
            r1.setPadding(r4, r6, r5, r7)
        L_0x008b:
            int r4 = com.google.android.setupdesign.R$id.sud_landscape_content_area
            android.view.View r4 = r8.findManagedViewById(r4)
            if (r4 == 0) goto L_0x00e6
            boolean r5 = r8.shouldApplyPartnerResource()
            if (r5 == 0) goto L_0x00bb
            android.content.Context r5 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r5 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r5)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r6 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAYOUT_MARGIN_START
            boolean r5 = r5.isPartnerConfigAvailable(r6)
            if (r5 == 0) goto L_0x00bb
            android.content.Context r2 = r8.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r2 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r2)
            android.content.Context r8 = r8.getContext()
            float r8 = r2.getDimension(r8, r6)
            int r8 = (int) r8
            goto L_0x00d1
        L_0x00bb:
            android.content.Context r8 = r8.getContext()
            int[] r2 = new int[r2]
            int r5 = com.google.android.setupdesign.R$attr.sudMarginStart
            r2[r3] = r5
            android.content.res.TypedArray r8 = r8.obtainStyledAttributes(r2)
            int r2 = r8.getDimensionPixelSize(r3, r3)
            r8.recycle()
            r8 = r2
        L_0x00d1:
            if (r1 == 0) goto L_0x00d7
            int r0 = r0 / 2
            int r3 = r0 - r8
        L_0x00d7:
            int r8 = r4.getPaddingTop()
            int r0 = r4.getPaddingEnd()
            int r1 = r4.getPaddingBottom()
            r4.setPadding(r3, r8, r0, r1)
        L_0x00e6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.GlifLayout.updateLandscapeMiddleHorizontalSpacing():void");
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.sud_glif_template;
        }
        return inflateTemplate(layoutInflater, R$style.SudThemeGlif_Light, i);
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = R$id.sud_layout_content;
        }
        return super.findContainer(i);
    }

    public View inflateStickyHeader(int i) {
        ViewStub viewStub = (ViewStub) findManagedViewById(R$id.sud_layout_sticky_header);
        viewStub.setLayoutResource(i);
        return viewStub.inflate();
    }

    public ScrollView getScrollView() {
        View findManagedViewById = findManagedViewById(R$id.sud_scroll_view);
        if (findManagedViewById instanceof ScrollView) {
            return (ScrollView) findManagedViewById;
        }
        return null;
    }

    public void setPrimaryColor(ColorStateList colorStateList) {
        this.primaryColor = colorStateList;
        updateBackground();
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).setColor(colorStateList);
    }

    public void setBackgroundBaseColor(ColorStateList colorStateList) {
        this.backgroundBaseColor = colorStateList;
        updateBackground();
    }

    public void setBackgroundPatterned(boolean z) {
        this.backgroundPatterned = z;
        updateBackground();
    }

    public final void updateBackground() {
        Drawable drawable;
        if (findManagedViewById(R$id.suc_layout_status) != null) {
            int i = 0;
            ColorStateList colorStateList = this.backgroundBaseColor;
            if (colorStateList != null) {
                i = colorStateList.getDefaultColor();
            } else {
                ColorStateList colorStateList2 = this.primaryColor;
                if (colorStateList2 != null) {
                    i = colorStateList2.getDefaultColor();
                }
            }
            if (this.backgroundPatterned) {
                drawable = new GlifPatternDrawable(i);
            } else {
                drawable = new ColorDrawable(i);
            }
            ((StatusBarMixin) getMixin(StatusBarMixin.class)).setStatusBarBackground(drawable);
        }
    }

    public boolean shouldApplyPartnerHeavyThemeResource() {
        return this.applyPartnerHeavyThemeResource || (shouldApplyPartnerResource() && PartnerConfigHelper.shouldApplyExtendedPartnerConfig(getContext()));
    }

    public final void updateContentBackgroundColorWithPartnerConfig() {
        if (!useFullDynamicColor()) {
            getRootView().setBackgroundColor(PartnerConfigHelper.get(getContext()).getColor(getContext(), PartnerConfig.CONFIG_LAYOUT_BACKGROUND_COLOR));
        }
    }

    @TargetApi(17)
    public void tryApplyPartnerCustomizationContentPaddingTopStyle(View view) {
        int dimension;
        Context context = view.getContext();
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_CONTENT_PADDING_TOP;
        boolean isPartnerConfigAvailable = partnerConfigHelper.isPartnerConfigAvailable(partnerConfig);
        if (shouldApplyPartnerResource() && isPartnerConfigAvailable && (dimension = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig)) != view.getPaddingTop()) {
            view.setPadding(view.getPaddingStart(), dimension, view.getPaddingEnd(), view.getPaddingBottom());
        }
    }
}
