package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.TransformState;

public class HybridNotificationView extends AlphaOptimizedLinearLayout implements TransformableView, NotificationFadeAware {
    public TextView mTextView;
    public TextView mTitleView;
    public final ViewTransformationHelper mTransformationHelper;

    public void setNotificationFaded(boolean z) {
    }

    public HybridNotificationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public HybridNotificationView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTransformationHelper = new ViewTransformationHelper();
    }

    public TextView getTitleView() {
        return this.mTitleView;
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(R$id.notification_title);
        this.mTextView = (TextView) findViewById(R$id.notification_text);
        this.mTransformationHelper.setCustomTransformation(new ViewTransformationHelper.CustomTransformation() {
            public boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeOut((View) HybridNotificationView.this.mTextView, f);
                if (currentState != null) {
                    transformState.transformViewVerticalTo(currentState, f);
                    currentState.recycle();
                }
                return true;
            }

            public boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeIn((View) HybridNotificationView.this.mTextView, f, true);
                if (currentState != null) {
                    transformState.transformViewVerticalFrom(currentState, f);
                    currentState.recycle();
                }
                return true;
            }
        }, 2);
        this.mTransformationHelper.addTransformedView(1, this.mTitleView);
        this.mTransformationHelper.addTransformedView(2, this.mTextView);
    }

    public void bind(CharSequence charSequence, CharSequence charSequence2, View view) {
        this.mTitleView.setText(charSequence);
        this.mTitleView.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
        if (TextUtils.isEmpty(charSequence2)) {
            this.mTextView.setVisibility(8);
            this.mTextView.setText((CharSequence) null);
        } else {
            this.mTextView.setVisibility(0);
            this.mTextView.setText(charSequence2.toString());
        }
        requestLayout();
    }

    public TransformState getCurrentState(int i) {
        return this.mTransformationHelper.getCurrentState(i);
    }

    public void transformTo(TransformableView transformableView, Runnable runnable) {
        this.mTransformationHelper.transformTo(transformableView, runnable);
    }

    public void transformTo(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformTo(transformableView, f);
    }

    public void transformFrom(TransformableView transformableView) {
        this.mTransformationHelper.transformFrom(transformableView);
    }

    public void transformFrom(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformFrom(transformableView, f);
    }

    public void setVisible(boolean z) {
        setVisibility(z ? 0 : 4);
        this.mTransformationHelper.setVisible(z);
    }
}
