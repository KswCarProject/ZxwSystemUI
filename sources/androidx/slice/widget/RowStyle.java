package androidx.slice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$styleable;

public class RowStyle {
    public int mActionDividerHeight = -1;
    public int mBottomDividerEndPadding = -1;
    public int mBottomDividerStartPadding = -1;
    public int mContentEndPadding = -1;
    public int mContentStartPadding = -1;
    public boolean mDisableRecyclerViewItemAnimator = false;
    public int mEndItemEndPadding = -1;
    public int mEndItemStartPadding = -1;
    public int mIconSize = -1;
    public int mImageSize;
    public int mProgressBarEndPadding = -1;
    public int mProgressBarInlineWidth = -1;
    public int mProgressBarStartPadding = -1;
    public int mSeekBarInlineWidth = -1;
    public final SliceStyle mSliceStyle;
    public int mSubContentEndPadding = -1;
    public int mSubContentStartPadding = -1;
    public Integer mSubtitleColor;
    public int mTextActionPadding = -1;
    public Integer mTintColor;
    public Integer mTitleColor;
    public int mTitleEndPadding = -1;
    public int mTitleItemEndPadding = -1;
    public int mTitleItemStartPadding = -1;
    public int mTitleStartPadding = -1;

    public RowStyle(Context context, SliceStyle sliceStyle) {
        this.mSliceStyle = sliceStyle;
        this.mImageSize = context.getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
    }

    public RowStyle(Context context, int i, SliceStyle sliceStyle) {
        this.mSliceStyle = sliceStyle;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(i, R$styleable.RowStyle);
        try {
            this.mTitleItemStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleItemStartPadding, -1.0f);
            this.mTitleItemEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleItemEndPadding, -1.0f);
            this.mContentStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_contentStartPadding, -1.0f);
            this.mContentEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_contentEndPadding, -1.0f);
            this.mTitleStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleStartPadding, -1.0f);
            this.mTitleEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_titleEndPadding, -1.0f);
            this.mSubContentStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_subContentStartPadding, -1.0f);
            this.mSubContentEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_subContentEndPadding, -1.0f);
            this.mEndItemStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_endItemStartPadding, -1.0f);
            this.mEndItemEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_endItemEndPadding, -1.0f);
            this.mBottomDividerStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_bottomDividerStartPadding, -1.0f);
            this.mBottomDividerEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_bottomDividerEndPadding, -1.0f);
            this.mActionDividerHeight = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_actionDividerHeight, -1.0f);
            this.mSeekBarInlineWidth = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_seekBarInlineWidth, -1.0f);
            this.mProgressBarInlineWidth = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarInlineWidth, -1.0f);
            this.mProgressBarStartPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarStartPadding, -1.0f);
            this.mProgressBarEndPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_progressBarEndPadding, -1.0f);
            this.mTextActionPadding = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_textActionPadding, 10.0f);
            this.mIconSize = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_iconSize, -1.0f);
            this.mDisableRecyclerViewItemAnimator = obtainStyledAttributes.getBoolean(R$styleable.RowStyle_disableRecyclerViewItemAnimator, false);
            this.mImageSize = (int) obtainStyledAttributes.getDimension(R$styleable.RowStyle_imageSize, (float) context.getResources().getDimensionPixelSize(R$dimen.abc_slice_small_image_size));
            this.mTintColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_tintColor);
            this.mTitleColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_titleColor);
            this.mSubtitleColor = getOptionalColor(obtainStyledAttributes, R$styleable.RowStyle_subtitleColor);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public int getTitleItemStartPadding() {
        return this.mTitleItemStartPadding;
    }

    public int getTitleItemEndPadding() {
        return this.mTitleItemEndPadding;
    }

    public int getContentStartPadding() {
        return this.mContentStartPadding;
    }

    public int getContentEndPadding() {
        return this.mContentEndPadding;
    }

    public int getTitleStartPadding() {
        return this.mTitleStartPadding;
    }

    public int getTitleEndPadding() {
        return this.mTitleEndPadding;
    }

    public int getSubContentStartPadding() {
        return this.mSubContentStartPadding;
    }

    public int getSubContentEndPadding() {
        return this.mSubContentEndPadding;
    }

    public int getEndItemStartPadding() {
        return this.mEndItemStartPadding;
    }

    public int getEndItemEndPadding() {
        return this.mEndItemEndPadding;
    }

    public int getBottomDividerStartPadding() {
        return this.mBottomDividerStartPadding;
    }

    public int getBottomDividerEndPadding() {
        return this.mBottomDividerEndPadding;
    }

    public int getActionDividerHeight() {
        return this.mActionDividerHeight;
    }

    public int getSeekBarInlineWidth() {
        return this.mSeekBarInlineWidth;
    }

    public int getProgressBarInlineWidth() {
        return this.mProgressBarInlineWidth;
    }

    public int getProgressBarStartPadding() {
        return this.mProgressBarStartPadding;
    }

    public int getProgressBarEndPadding() {
        return this.mProgressBarEndPadding;
    }

    public int getTextActionPadding() {
        return this.mTextActionPadding;
    }

    public int getIconSize() {
        return this.mIconSize;
    }

    public boolean getDisableRecyclerViewItemAnimator() {
        return this.mDisableRecyclerViewItemAnimator;
    }

    public int getImageSize() {
        return this.mImageSize;
    }

    public int getTintColor() {
        Integer num = this.mTintColor;
        return num != null ? num.intValue() : this.mSliceStyle.getTintColor();
    }

    public int getTitleColor() {
        Integer num = this.mTitleColor;
        return num != null ? num.intValue() : this.mSliceStyle.getTitleColor();
    }

    public int getSubtitleColor() {
        Integer num = this.mSubtitleColor;
        return num != null ? num.intValue() : this.mSliceStyle.getSubtitleColor();
    }

    public static Integer getOptionalColor(TypedArray typedArray, int i) {
        if (typedArray.hasValue(i)) {
            return Integer.valueOf(typedArray.getColor(i, 0));
        }
        return null;
    }
}
