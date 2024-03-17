package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$layout;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.ItemStyler;
import com.google.android.setupdesign.util.LayoutStyler;

public class Item extends AbstractItem {
    public CharSequence contentDescription;
    public boolean enabled;
    public Drawable icon;
    public int iconGravity;
    public int iconTint;
    public int layoutRes;
    public CharSequence summary;
    public CharSequence title;
    public boolean visible;

    public Item() {
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        this.layoutRes = getDefaultLayoutResource();
    }

    public Item(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudItem);
        this.enabled = obtainStyledAttributes.getBoolean(R$styleable.SudItem_android_enabled, true);
        this.icon = obtainStyledAttributes.getDrawable(R$styleable.SudItem_android_icon);
        this.title = obtainStyledAttributes.getText(R$styleable.SudItem_android_title);
        this.summary = obtainStyledAttributes.getText(R$styleable.SudItem_android_summary);
        this.contentDescription = obtainStyledAttributes.getText(R$styleable.SudItem_android_contentDescription);
        this.layoutRes = obtainStyledAttributes.getResourceId(R$styleable.SudItem_android_layout, getDefaultLayoutResource());
        this.visible = obtainStyledAttributes.getBoolean(R$styleable.SudItem_android_visible, true);
        this.iconTint = obtainStyledAttributes.getColor(R$styleable.SudItem_sudIconTint, 0);
        this.iconGravity = obtainStyledAttributes.getInt(R$styleable.SudItem_sudIconGravity, 16);
        obtainStyledAttributes.recycle();
    }

    public int getDefaultLayoutResource() {
        return R$layout.sud_items_default;
    }

    public int getCount() {
        return isVisible() ? 1 : 0;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIconGravity(int i) {
        this.iconGravity = i;
    }

    public int getLayoutResource() {
        return this.layoutRes;
    }

    public CharSequence getSummary() {
        return this.summary;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public CharSequence getContentDescription() {
        return this.contentDescription;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public final boolean hasSummary(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }

    public int getViewId() {
        return getId();
    }

    public void onBindView(View view) {
        ((TextView) view.findViewById(R$id.sud_items_title)).setText(getTitle());
        TextView textView = (TextView) view.findViewById(R$id.sud_items_summary);
        CharSequence summary2 = getSummary();
        if (hasSummary(summary2)) {
            textView.setText(summary2);
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        view.setContentDescription(getContentDescription());
        View findViewById = view.findViewById(R$id.sud_items_icon_container);
        Drawable icon2 = getIcon();
        if (icon2 != null) {
            ImageView imageView = (ImageView) view.findViewById(R$id.sud_items_icon);
            imageView.setImageDrawable((Drawable) null);
            onMergeIconStateAndLevels(imageView, icon2);
            imageView.setImageDrawable(icon2);
            int i = this.iconTint;
            if (i != 0) {
                imageView.setColorFilter(i);
            } else {
                imageView.clearColorFilter();
            }
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).gravity = this.iconGravity;
            }
            findViewById.setVisibility(0);
        } else {
            findViewById.setVisibility(8);
        }
        view.setId(getViewId());
        if (!(this instanceof ExpandableSwitchItem) && view.getId() != R$id.sud_layout_header) {
            LayoutStyler.applyPartnerCustomizationLayoutPaddingStyle(view);
        }
        ItemStyler.applyPartnerCustomizationItemStyle(view);
    }

    public void onMergeIconStateAndLevels(ImageView imageView, Drawable drawable) {
        imageView.setImageState(drawable.getState(), false);
        imageView.setImageLevel(drawable.getLevel());
    }
}
