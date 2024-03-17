package com.android.keyguard;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.RowContent;
import androidx.slice.widget.SliceContent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.wakelock.KeepAwakeAnimationListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyguardSliceView extends LinearLayout {
    public Runnable mContentChangeListener;
    public float mDarkAmount = 0.0f;
    public boolean mHasHeader;
    public int mIconSize;
    public int mIconSizeWithHeader;
    public final LayoutTransition mLayoutTransition;
    public View.OnClickListener mOnClickListener;
    public Row mRow;
    public int mTextColor;
    @VisibleForTesting
    public TextView mTitle;

    public KeyguardSliceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context.getResources();
        LayoutTransition layoutTransition = new LayoutTransition();
        this.mLayoutTransition = layoutTransition;
        layoutTransition.setStagger(0, 275);
        layoutTransition.setDuration(2, 550);
        layoutTransition.setDuration(3, 275);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
        layoutTransition.setInterpolator(2, Interpolators.FAST_OUT_SLOW_IN);
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimateParentHierarchy(false);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R$id.title);
        this.mRow = (Row) findViewById(R$id.row);
        this.mTextColor = Utils.getColorAttrDefaultColor(this.mContext, R$attr.wallpaperTextColor);
        this.mIconSize = (int) this.mContext.getResources().getDimension(R$dimen.widget_icon_size);
        this.mIconSizeWithHeader = (int) this.mContext.getResources().getDimension(R$dimen.header_icon_size);
        this.mTitle.setBreakStrategy(2);
    }

    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        setLayoutTransition(z ? this.mLayoutTransition : null);
    }

    public void hideSlice() {
        this.mTitle.setVisibility(8);
        this.mRow.setVisibility(8);
        this.mHasHeader = false;
        Runnable runnable = this.mContentChangeListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    public Map<View, PendingIntent> showSlice(RowContent rowContent, List<SliceContent> list) {
        CharSequence charSequence;
        Drawable drawable;
        Trace.beginSection("KeyguardSliceView#showSlice");
        int i = 0;
        this.mHasHeader = rowContent != null;
        HashMap hashMap = new HashMap();
        int i2 = 8;
        if (!this.mHasHeader) {
            this.mTitle.setVisibility(8);
        } else {
            this.mTitle.setVisibility(0);
            SliceItem titleItem = rowContent.getTitleItem();
            this.mTitle.setText(titleItem != null ? titleItem.getText() : null);
            if (!(rowContent.getPrimaryAction() == null || rowContent.getPrimaryAction().getAction() == null)) {
                hashMap.put(this.mTitle, rowContent.getPrimaryAction().getAction());
            }
        }
        int size = list.size();
        int textColor = getTextColor();
        Row row = this.mRow;
        if (size > 0) {
            i2 = 0;
        }
        row.setVisibility(i2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRow.getLayoutParams();
        layoutParams.gravity = 8388611;
        this.mRow.setLayoutParams(layoutParams);
        for (int i3 = this.mHasHeader; i3 < size; i3++) {
            RowContent rowContent2 = (RowContent) list.get(i3);
            SliceItem sliceItem = rowContent2.getSliceItem();
            Uri uri = sliceItem.getSlice().getUri();
            KeyguardSliceTextView keyguardSliceTextView = (KeyguardSliceTextView) this.mRow.findViewWithTag(uri);
            if (keyguardSliceTextView == null) {
                keyguardSliceTextView = new KeyguardSliceTextView(this.mContext);
                keyguardSliceTextView.setTextColor(textColor);
                keyguardSliceTextView.setTag(uri);
                this.mRow.addView(keyguardSliceTextView, i3 - (this.mHasHeader ? 1 : 0));
            }
            PendingIntent action = rowContent2.getPrimaryAction() != null ? rowContent2.getPrimaryAction().getAction() : null;
            hashMap.put(keyguardSliceTextView, action);
            SliceItem titleItem2 = rowContent2.getTitleItem();
            if (titleItem2 == null) {
                charSequence = null;
            } else {
                charSequence = titleItem2.getText();
            }
            keyguardSliceTextView.setText(charSequence);
            keyguardSliceTextView.setContentDescription(rowContent2.getContentDescription());
            SliceItem find = SliceQuery.find(sliceItem.getSlice(), "image");
            if (find != null) {
                int i4 = this.mHasHeader ? this.mIconSizeWithHeader : this.mIconSize;
                drawable = find.getIcon().loadDrawable(this.mContext);
                if (drawable != null) {
                    if (drawable instanceof InsetDrawable) {
                        drawable = ((InsetDrawable) drawable).getDrawable();
                    }
                    drawable.setBounds(0, 0, Math.max((int) ((((float) drawable.getIntrinsicWidth()) / ((float) drawable.getIntrinsicHeight())) * ((float) i4)), 1), i4);
                }
            } else {
                drawable = null;
            }
            keyguardSliceTextView.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            keyguardSliceTextView.setOnClickListener(this.mOnClickListener);
            keyguardSliceTextView.setClickable(action != null);
        }
        while (i < this.mRow.getChildCount()) {
            View childAt = this.mRow.getChildAt(i);
            if (!hashMap.containsKey(childAt)) {
                this.mRow.removeView(childAt);
                i--;
            }
            i++;
        }
        Runnable runnable = this.mContentChangeListener;
        if (runnable != null) {
            runnable.run();
        }
        Trace.endSection();
        return hashMap;
    }

    public void setDarkAmount(float f) {
        this.mDarkAmount = f;
        this.mRow.setDarkAmount(f);
        updateTextColors();
    }

    public final void updateTextColors() {
        int textColor = getTextColor();
        this.mTitle.setTextColor(textColor);
        int childCount = this.mRow.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(textColor);
            }
        }
    }

    @VisibleForTesting
    public int getTextColor() {
        return ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
    }

    @VisibleForTesting
    public void setTextColor(int i) {
        this.mTextColor = i;
        updateTextColors();
    }

    public void onDensityOrFontScaleChanged() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.widget_icon_size);
        this.mIconSizeWithHeader = (int) this.mContext.getResources().getDimension(R$dimen.header_icon_size);
        for (int i = 0; i < this.mRow.getChildCount(); i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof KeyguardSliceTextView) {
                ((KeyguardSliceTextView) childAt).onDensityOrFontScaleChanged();
            }
        }
    }

    public void onOverlayChanged() {
        for (int i = 0; i < this.mRow.getChildCount(); i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof KeyguardSliceTextView) {
                ((KeyguardSliceTextView) childAt).onOverlayChanged();
            }
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        this.mTitle.setOnClickListener(onClickListener);
    }

    public static class Row extends LinearLayout {
        public float mDarkAmount;
        public final Animation.AnimationListener mKeepAwakeListener;
        public Set<KeyguardSliceTextView> mKeyguardSliceTextViewSet;
        public LayoutTransition mLayoutTransition;

        public boolean hasOverlappingRendering() {
            return false;
        }

        public Row(Context context) {
            this(context, (AttributeSet) null);
        }

        public Row(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public Row(Context context, AttributeSet attributeSet, int i) {
            this(context, attributeSet, i, 0);
        }

        public Row(Context context, AttributeSet attributeSet, int i, int i2) {
            super(context, attributeSet, i, i2);
            this.mKeyguardSliceTextViewSet = new HashSet();
            this.mKeepAwakeListener = new KeepAwakeAnimationListener(this.mContext);
        }

        public void onFinishInflate() {
            LayoutTransition layoutTransition = new LayoutTransition();
            this.mLayoutTransition = layoutTransition;
            layoutTransition.setDuration(550);
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object) null, new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("left", new int[]{0, 1}), PropertyValuesHolder.ofInt("right", new int[]{0, 1})});
            this.mLayoutTransition.setAnimator(0, ofPropertyValuesHolder);
            this.mLayoutTransition.setAnimator(1, ofPropertyValuesHolder);
            LayoutTransition layoutTransition2 = this.mLayoutTransition;
            Interpolator interpolator = Interpolators.ACCELERATE_DECELERATE;
            layoutTransition2.setInterpolator(0, interpolator);
            this.mLayoutTransition.setInterpolator(1, interpolator);
            this.mLayoutTransition.setStartDelay(0, 550);
            this.mLayoutTransition.setStartDelay(1, 550);
            this.mLayoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, "alpha", new float[]{0.0f, 1.0f}));
            this.mLayoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", new float[]{1.0f, 0.0f});
            this.mLayoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
            this.mLayoutTransition.setDuration(3, 137);
            this.mLayoutTransition.setAnimator(3, ofFloat);
            this.mLayoutTransition.setAnimateParentHierarchy(false);
        }

        public void onVisibilityAggregated(boolean z) {
            super.onVisibilityAggregated(z);
            setLayoutTransition(z ? this.mLayoutTransition : null);
        }

        public void onMeasure(int i, int i2) {
            View.MeasureSpec.getSize(i);
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof KeyguardSliceTextView) {
                    ((KeyguardSliceTextView) childAt).setMaxWidth(Integer.MAX_VALUE);
                }
            }
            super.onMeasure(i, i2);
        }

        public void setDarkAmount(float f) {
            Animation.AnimationListener animationListener;
            boolean z = true;
            boolean z2 = f != 0.0f;
            if (this.mDarkAmount == 0.0f) {
                z = false;
            }
            if (z2 != z) {
                this.mDarkAmount = f;
                if (z2) {
                    animationListener = null;
                } else {
                    animationListener = this.mKeepAwakeListener;
                }
                setLayoutAnimationListener(animationListener);
            }
        }

        public void addView(View view, int i) {
            super.addView(view, i);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.add((KeyguardSliceTextView) view);
            }
        }

        public void removeView(View view) {
            super.removeView(view);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.remove((KeyguardSliceTextView) view);
            }
        }
    }

    @VisibleForTesting
    public static class KeyguardSliceTextView extends TextView {
        public static int sStyleId = R$style.TextAppearance_Keyguard_Secondary;

        public KeyguardSliceTextView(Context context) {
            super(context, (AttributeSet) null, 0, sStyleId);
            onDensityOrFontScaleChanged();
            setEllipsize(TextUtils.TruncateAt.END);
        }

        public void onDensityOrFontScaleChanged() {
            updatePadding();
        }

        public void onOverlayChanged() {
            setTextAppearance(sStyleId);
        }

        public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
            super.setText(charSequence, bufferType);
            updatePadding();
        }

        public final void updatePadding() {
            boolean z = !TextUtils.isEmpty(getText());
            int dimension = ((int) getContext().getResources().getDimension(R$dimen.widget_horizontal_padding)) / 2;
            setPadding(0, dimension, 0, z ? dimension : 0);
            setCompoundDrawablePadding((int) this.mContext.getResources().getDimension(R$dimen.widget_icon_padding));
        }

        public void setTextColor(int i) {
            super.setTextColor(i);
            updateDrawableColors();
        }

        public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
            updateDrawableColors();
            updatePadding();
        }

        public final void updateDrawableColors() {
            int currentTextColor = getCurrentTextColor();
            for (Drawable drawable : getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setTint(currentTextColor);
                }
            }
        }
    }
}
