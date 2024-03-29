package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;

public class ExpandableIndicator extends ImageView {
    public boolean mExpanded;
    public boolean mIsDefaultDirection = true;

    public ExpandableIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        updateIndicatorDrawable();
        setContentDescription(getContentDescription(this.mExpanded));
    }

    public void setExpanded(boolean z) {
        if (z != this.mExpanded) {
            this.mExpanded = z;
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getContext().getDrawable(getDrawableResourceId(!z)).getConstantState().newDrawable();
            setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.forceAnimationOnUI();
            animatedVectorDrawable.start();
            setContentDescription(getContentDescription(z));
        }
    }

    public final int getDrawableResourceId(boolean z) {
        if (this.mIsDefaultDirection) {
            if (z) {
                return R$drawable.ic_volume_collapse_animation;
            }
            return R$drawable.ic_volume_expand_animation;
        } else if (z) {
            return R$drawable.ic_volume_expand_animation;
        } else {
            return R$drawable.ic_volume_collapse_animation;
        }
    }

    public final String getContentDescription(boolean z) {
        if (z) {
            return this.mContext.getString(R$string.accessibility_quick_settings_collapse);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_expand);
    }

    public final void updateIndicatorDrawable() {
        setImageResource(getDrawableResourceId(this.mExpanded));
    }
}
