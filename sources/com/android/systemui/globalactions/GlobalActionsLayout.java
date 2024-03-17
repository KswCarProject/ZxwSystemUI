package com.android.systemui.globalactions;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.HardwareBgDrawable;
import com.android.systemui.MultiListLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$id;
import com.android.systemui.util.leak.RotationUtils;
import java.util.Locale;

public abstract class GlobalActionsLayout extends MultiListLayout {
    public boolean mBackgroundsSet;

    public abstract boolean shouldReverseListItems();

    public GlobalActionsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void setBackgrounds() {
        HardwareBgDrawable backgroundDrawable;
        ViewGroup listView = getListView();
        HardwareBgDrawable backgroundDrawable2 = getBackgroundDrawable(getResources().getColor(R$color.global_actions_grid_background, (Resources.Theme) null));
        if (backgroundDrawable2 != null) {
            listView.setBackground(backgroundDrawable2);
        }
        if (getSeparatedView() != null && (backgroundDrawable = getBackgroundDrawable(getResources().getColor(R$color.global_actions_separated_background, (Resources.Theme) null))) != null) {
            getSeparatedView().setBackground(backgroundDrawable);
        }
    }

    public HardwareBgDrawable getBackgroundDrawable(int i) {
        HardwareBgDrawable hardwareBgDrawable = new HardwareBgDrawable(true, true, getContext());
        hardwareBgDrawable.setTint(i);
        return hardwareBgDrawable;
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (getListView() != null && !this.mBackgroundsSet) {
            setBackgrounds();
            this.mBackgroundsSet = true;
        }
    }

    public void addToListView(View view, boolean z) {
        if (z) {
            getListView().addView(view, 0);
        } else {
            getListView().addView(view);
        }
    }

    public void addToSeparatedView(View view, boolean z) {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView == null) {
            addToListView(view, z);
        } else if (z) {
            separatedView.addView(view, 0);
        } else {
            separatedView.addView(view);
        }
    }

    @VisibleForTesting
    public int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }

    @VisibleForTesting
    public int getCurrentRotation() {
        return RotationUtils.getRotation(this.mContext);
    }

    public void onUpdateList() {
        View view;
        super.onUpdateList();
        ViewGroup separatedView = getSeparatedView();
        ViewGroup listView = getListView();
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            boolean shouldBeSeparated = this.mAdapter.shouldBeSeparated(i);
            if (shouldBeSeparated) {
                view = this.mAdapter.getView(i, (View) null, separatedView);
            } else {
                view = this.mAdapter.getView(i, (View) null, listView);
            }
            if (shouldBeSeparated) {
                addToSeparatedView(view, false);
            } else {
                addToListView(view, shouldReverseListItems());
            }
        }
    }

    public ViewGroup getSeparatedView() {
        return (ViewGroup) findViewById(R$id.separated_button);
    }

    public ViewGroup getListView() {
        return (ViewGroup) findViewById(16908298);
    }

    public View getWrapper() {
        return getChildAt(0);
    }
}
