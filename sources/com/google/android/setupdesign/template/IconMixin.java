package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.HeaderAreaStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class IconMixin implements Mixin {
    public final Context context;
    public final int originalHeight;
    public final ImageView.ScaleType originalScaleType;
    public final TemplateLayout templateLayout;

    public IconMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        Context context2 = templateLayout2.getContext();
        this.context = context2;
        ImageView view = getView();
        if (view != null) {
            this.originalHeight = view.getLayoutParams().height;
            this.originalScaleType = view.getScaleType();
        } else {
            this.originalHeight = 0;
            this.originalScaleType = null;
        }
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.SudIconMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.SudIconMixin_android_icon, 0);
        if (resourceId != 0) {
            setIcon(resourceId);
        }
        setUpscaleIcon(obtainStyledAttributes.getBoolean(R$styleable.SudIconMixin_sudUpscaleIcon, false));
        int color = obtainStyledAttributes.getColor(R$styleable.SudIconMixin_sudIconTint, 0);
        if (color != 0) {
            setIconTint(color);
        }
        obtainStyledAttributes.recycle();
    }

    public void tryApplyPartnerCustomizationStyle() {
        if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            HeaderAreaStyler.applyPartnerCustomizationIconStyle(getView(), getContainerView());
        }
    }

    public void setIcon(int i) {
        ImageView view = getView();
        if (view != null) {
            view.setImageResource(i);
            view.setVisibility(i != 0 ? 0 : 8);
            setIconContainerVisibility(view.getVisibility());
        }
    }

    public void setUpscaleIcon(boolean z) {
        ImageView view = getView();
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int maxHeight = view.getMaxHeight();
            if (!z) {
                maxHeight = this.originalHeight;
            }
            layoutParams.height = maxHeight;
            view.setLayoutParams(layoutParams);
            view.setScaleType(z ? ImageView.ScaleType.FIT_CENTER : this.originalScaleType);
        }
    }

    public void setIconTint(int i) {
        ImageView view = getView();
        if (view != null) {
            view.setColorFilter(i);
        }
    }

    public ImageView getView() {
        return (ImageView) this.templateLayout.findManagedViewById(R$id.sud_layout_icon);
    }

    public FrameLayout getContainerView() {
        return (FrameLayout) this.templateLayout.findManagedViewById(R$id.sud_layout_icon_container);
    }

    public final void setIconContainerVisibility(int i) {
        if (getContainerView() != null) {
            getContainerView().setVisibility(i);
        }
    }
}
