package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;

public class SectionHeaderView extends StackScrollerDecorView {
    public ImageView mClearAllButton;
    public ViewGroup mContents;
    public View.OnClickListener mLabelClickListener = null;
    public Integer mLabelTextId;
    public TextView mLabelView;
    public View.OnClickListener mOnClearClickListener = null;

    public View findSecondaryView() {
        return null;
    }

    public boolean isTransparent() {
        return true;
    }

    public boolean needsClippingToShelf() {
        return true;
    }

    public SectionHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        this.mContents = (ViewGroup) requireViewById(R$id.content);
        bindContents();
        super.onFinishInflate();
        setVisible(true, false);
    }

    public final void bindContents() {
        this.mLabelView = (TextView) requireViewById(R$id.header_label);
        ImageView imageView = (ImageView) requireViewById(R$id.btn_clear_all);
        this.mClearAllButton = imageView;
        View.OnClickListener onClickListener = this.mOnClearClickListener;
        if (onClickListener != null) {
            imageView.setOnClickListener(onClickListener);
        }
        View.OnClickListener onClickListener2 = this.mLabelClickListener;
        if (onClickListener2 != null) {
            this.mLabelView.setOnClickListener(onClickListener2);
        }
        Integer num = this.mLabelTextId;
        if (num != null) {
            this.mLabelView.setText(num.intValue());
        }
    }

    public View findContentView() {
        return this.mContents;
    }

    public void setClearSectionButtonEnabled(boolean z) {
        this.mClearAllButton.setVisibility(z ? 0 : 8);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setOnHeaderClickListener(View.OnClickListener onClickListener) {
        this.mLabelClickListener = onClickListener;
        this.mLabelView.setOnClickListener(onClickListener);
    }

    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        this.mLabelView.setAlpha(f);
        this.mLabelView.setTranslationY(f2);
        this.mClearAllButton.setAlpha(f);
        this.mClearAllButton.setTranslationY(f2);
    }

    public void setOnClearAllClickListener(View.OnClickListener onClickListener) {
        this.mOnClearClickListener = onClickListener;
        this.mClearAllButton.setOnClickListener(onClickListener);
    }

    public void setHeaderText(int i) {
        this.mLabelTextId = Integer.valueOf(i);
        this.mLabelView.setText(i);
    }

    public void setForegroundColor(int i) {
        this.mLabelView.setTextColor(i);
        this.mClearAllButton.setImageTintList(ColorStateList.valueOf(i));
    }
}
