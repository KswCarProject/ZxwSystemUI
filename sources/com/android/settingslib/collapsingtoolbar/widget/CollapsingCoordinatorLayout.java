package com.android.settingslib.collapsingtoolbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.android.settingslib.collapsingtoolbar.R$id;
import com.android.settingslib.collapsingtoolbar.R$layout;
import com.android.settingslib.collapsingtoolbar.R$styleable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class CollapsingCoordinatorLayout extends CoordinatorLayout {
    public AppBarLayout mAppBarLayout;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public boolean mIsMatchParentHeight;
    public CharSequence mToolbarTitle;

    public CollapsingCoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CollapsingCoordinatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsMatchParentHeight = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CollapsingCoordinatorLayout);
            this.mToolbarTitle = obtainStyledAttributes.getText(R$styleable.CollapsingCoordinatorLayout_collapsing_toolbar_title);
            this.mIsMatchParentHeight = obtainStyledAttributes.getBoolean(R$styleable.CollapsingCoordinatorLayout_content_frame_height_match_parent, false);
            obtainStyledAttributes.recycle();
        }
        init();
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        int id = view.getId();
        int i2 = R$id.content_frame;
        if (id == i2 && this.mIsMatchParentHeight) {
            layoutParams.height = -1;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(i2);
        if (viewGroup == null || !isContentFrameChild(view.getId())) {
            super.addView(view, i, layoutParams);
        } else {
            viewGroup.addView(view, i, layoutParams);
        }
    }

    public final boolean isContentFrameChild(int i) {
        return (i == R$id.app_bar || i == R$id.content_frame) ? false : true;
    }

    public final void init() {
        ViewGroup.inflate(getContext(), R$layout.collapsing_toolbar_content_layout, this);
        this.mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R$id.collapsing_toolbar);
        this.mAppBarLayout = (AppBarLayout) findViewById(R$id.app_bar);
        CollapsingToolbarLayout collapsingToolbarLayout = this.mCollapsingToolbarLayout;
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setLineSpacingMultiplier(1.1f);
            this.mCollapsingToolbarLayout.setHyphenationFrequency(3);
            if (!TextUtils.isEmpty(this.mToolbarTitle)) {
                this.mCollapsingToolbarLayout.setTitle(this.mToolbarTitle);
            }
        }
        disableCollapsingToolbarLayoutScrollingBehavior();
    }

    public final void disableCollapsingToolbarLayoutScrollingBehavior() {
        AppBarLayout appBarLayout = this.mAppBarLayout;
        if (appBarLayout != null) {
            AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                public boolean canDrag(AppBarLayout appBarLayout) {
                    return false;
                }
            });
            ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(behavior);
        }
    }
}
