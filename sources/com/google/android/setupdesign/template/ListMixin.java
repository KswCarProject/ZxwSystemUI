package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemAdapter;
import com.google.android.setupdesign.items.ItemGroup;
import com.google.android.setupdesign.items.ItemInflater;
import com.google.android.setupdesign.util.DrawableLayoutDirectionHelper;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class ListMixin implements Mixin {
    public Drawable defaultDivider;
    public Drawable divider;
    public int dividerInsetEnd;
    public int dividerInsetStart;
    public ListView listView;
    public final TemplateLayout templateLayout;

    public ListMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        Context context = templateLayout2.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudListMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.SudListMixin_android_entries, 0);
        if (resourceId != 0) {
            setAdapter(new ItemAdapter((ItemGroup) new ItemInflater(context).inflate(resourceId)));
        }
        if (isDividerShown(context, obtainStyledAttributes.getBoolean(R$styleable.SudListMixin_sudDividerShown, true))) {
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudListMixin_sudDividerInset, -1);
            if (dimensionPixelSize != -1) {
                setDividerInset(dimensionPixelSize);
            } else {
                int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudListMixin_sudDividerInsetStart, 0);
                int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudListMixin_sudDividerInsetEnd, 0);
                if (PartnerStyleHelper.shouldApplyPartnerResource((View) templateLayout2)) {
                    PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig = PartnerConfig.CONFIG_LAYOUT_MARGIN_START;
                    dimensionPixelSize2 = partnerConfigHelper.isPartnerConfigAvailable(partnerConfig) ? (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig) : dimensionPixelSize2;
                    PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_LAYOUT_MARGIN_END;
                    if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                        dimensionPixelSize3 = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig2);
                    }
                }
                setDividerInsets(dimensionPixelSize2, dimensionPixelSize3);
            }
        } else {
            getListView().setDivider((Drawable) null);
        }
        obtainStyledAttributes.recycle();
    }

    public final boolean isDividerShown(Context context, boolean z) {
        if (!PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            return z;
        }
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN;
        return partnerConfigHelper.isPartnerConfigAvailable(partnerConfig) ? PartnerConfigHelper.get(context).getBoolean(context, partnerConfig, true) : z;
    }

    public ListView getListView() {
        return getListViewInternal();
    }

    public final ListView getListViewInternal() {
        if (this.listView == null) {
            View findManagedViewById = this.templateLayout.findManagedViewById(16908298);
            if (findManagedViewById instanceof ListView) {
                this.listView = (ListView) findManagedViewById;
            }
        }
        return this.listView;
    }

    public void onLayout() {
        if (this.divider == null) {
            updateDivider();
        }
    }

    public void setAdapter(ListAdapter listAdapter) {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal != null) {
            listViewInternal.setAdapter(listAdapter);
        }
    }

    @Deprecated
    public void setDividerInset(int i) {
        setDividerInsets(i, 0);
    }

    public void setDividerInsets(int i, int i2) {
        this.dividerInsetStart = i;
        this.dividerInsetEnd = i2;
        updateDivider();
    }

    public final void updateDivider() {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal != null && this.templateLayout.isLayoutDirectionResolved()) {
            if (this.defaultDivider == null) {
                this.defaultDivider = listViewInternal.getDivider();
            }
            Drawable drawable = this.defaultDivider;
            if (drawable != null) {
                InsetDrawable createRelativeInsetDrawable = DrawableLayoutDirectionHelper.createRelativeInsetDrawable(drawable, this.dividerInsetStart, 0, this.dividerInsetEnd, 0, (View) this.templateLayout);
                this.divider = createRelativeInsetDrawable;
                listViewInternal.setDivider(createRelativeInsetDrawable);
            }
        }
    }
}
