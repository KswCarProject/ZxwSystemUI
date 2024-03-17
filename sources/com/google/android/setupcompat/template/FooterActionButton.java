package com.google.android.setupcompat.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class FooterActionButton extends Button {
    public FooterButton footerButton;
    public boolean isPrimaryButtonStyle = false;

    public FooterActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setFooterButton(FooterButton footerButton2) {
        this.footerButton = footerButton2;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        FooterButton footerButton2;
        View.OnClickListener onClickListenerWhenDisabled;
        if (motionEvent.getAction() == 0 && (footerButton2 = this.footerButton) != null && !footerButton2.isEnabled() && this.footerButton.getVisibility() == 0 && (onClickListenerWhenDisabled = this.footerButton.getOnClickListenerWhenDisabled()) != null) {
            onClickListenerWhenDisabled.onClick(this);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setPrimaryButtonStyle(boolean z) {
        this.isPrimaryButtonStyle = z;
    }

    public boolean isPrimaryButtonStyle() {
        return this.isPrimaryButtonStyle;
    }
}
