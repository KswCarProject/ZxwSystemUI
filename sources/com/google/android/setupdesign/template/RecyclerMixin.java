package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.DividerItemDecoration;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.R$attr;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemHierarchy;
import com.google.android.setupdesign.items.ItemInflater;
import com.google.android.setupdesign.items.RecyclerItemAdapter;
import com.google.android.setupdesign.util.DrawableLayoutDirectionHelper;
import com.google.android.setupdesign.util.PartnerStyleHelper;
import com.google.android.setupdesign.view.HeaderRecyclerView;

public class RecyclerMixin implements Mixin {
    public Drawable defaultDivider;
    public Drawable divider;
    public DividerItemDecoration dividerDecoration;
    public int dividerInsetEnd;
    public int dividerInsetStart;
    public View header;
    public boolean isDividerDisplay = true;
    public final RecyclerView recyclerView;
    public final TemplateLayout templateLayout;

    public RecyclerMixin(TemplateLayout templateLayout2, RecyclerView recyclerView2) {
        this.templateLayout = templateLayout2;
        this.dividerDecoration = new DividerItemDecoration(templateLayout2.getContext());
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(templateLayout2.getContext()));
        if (recyclerView2 instanceof HeaderRecyclerView) {
            this.header = ((HeaderRecyclerView) recyclerView2).getHeader();
        }
        boolean isShowItemsDivider = isShowItemsDivider(templateLayout2.getContext());
        this.isDividerDisplay = isShowItemsDivider;
        if (isShowItemsDivider) {
            recyclerView2.addItemDecoration(this.dividerDecoration);
        }
    }

    public final boolean isShowItemsDivider(Context context) {
        TypedValue typedValue = new TypedValue();
        boolean z = true;
        context.getTheme().resolveAttribute(R$attr.sudDividerShown, typedValue, true);
        if (typedValue.data == 0) {
            z = false;
        }
        if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(this.recyclerView.getContext());
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                return PartnerConfigHelper.get(this.recyclerView.getContext()).getBoolean(this.recyclerView.getContext(), partnerConfig, z);
            }
        }
        return z;
    }

    public void parseAttributes(AttributeSet attributeSet, int i) {
        boolean z;
        boolean z2;
        Context context = this.templateLayout.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudRecyclerMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.SudRecyclerMixin_android_entries, 0);
        if (resourceId != 0) {
            ItemHierarchy itemHierarchy = (ItemHierarchy) new ItemInflater(context).inflate(resourceId);
            TemplateLayout templateLayout2 = this.templateLayout;
            if (templateLayout2 instanceof GlifLayout) {
                z2 = ((GlifLayout) templateLayout2).shouldApplyPartnerHeavyThemeResource();
                z = ((GlifLayout) this.templateLayout).useFullDynamicColor();
            } else {
                z2 = false;
                z = false;
            }
            RecyclerItemAdapter recyclerItemAdapter = new RecyclerItemAdapter(itemHierarchy, z2, z);
            recyclerItemAdapter.setHasStableIds(obtainStyledAttributes.getBoolean(R$styleable.SudRecyclerMixin_sudHasStableIds, false));
            setAdapter(recyclerItemAdapter);
        }
        if (!this.isDividerDisplay) {
            obtainStyledAttributes.recycle();
            return;
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudRecyclerMixin_sudDividerInset, -1);
        if (dimensionPixelSize != -1) {
            setDividerInset(dimensionPixelSize);
        } else {
            int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudRecyclerMixin_sudDividerInsetStart, 0);
            int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudRecyclerMixin_sudDividerInsetEnd, 0);
            if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
                PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig = PartnerConfig.CONFIG_LAYOUT_MARGIN_START;
                if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                    dimensionPixelSize2 = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
                }
                PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_LAYOUT_MARGIN_END;
                if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                    dimensionPixelSize3 = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig2);
                }
            }
            setDividerInsets(dimensionPixelSize2, dimensionPixelSize3);
        }
        obtainStyledAttributes.recycle();
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public View getHeader() {
        return this.header;
    }

    public void onLayout() {
        if (this.divider == null) {
            updateDivider();
        }
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        this.recyclerView.setAdapter(adapter);
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
        if (this.templateLayout.isLayoutDirectionResolved()) {
            if (this.defaultDivider == null) {
                this.defaultDivider = this.dividerDecoration.getDivider();
            }
            InsetDrawable createRelativeInsetDrawable = DrawableLayoutDirectionHelper.createRelativeInsetDrawable(this.defaultDivider, this.dividerInsetStart, 0, this.dividerInsetEnd, 0, (View) this.templateLayout);
            this.divider = createRelativeInsetDrawable;
            this.dividerDecoration.setDivider(createRelativeInsetDrawable);
        }
    }
}
