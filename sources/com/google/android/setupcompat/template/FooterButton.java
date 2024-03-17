package com.google.android.setupcompat.template;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.setupcompat.R$styleable;
import com.google.android.setupcompat.logging.CustomEvent;
import java.util.Locale;

public final class FooterButton implements View.OnClickListener {
    public OnButtonEventListener buttonListener;
    public final int buttonType;
    public int clickCount;
    public int direction;
    public boolean enabled;
    public Locale locale;
    public View.OnClickListener onClickListener;
    public View.OnClickListener onClickListenerWhenDisabled;
    public CharSequence text;
    public int theme;
    public int visibility;

    public interface OnButtonEventListener {
        void onEnabledChanged(boolean z);
    }

    public FooterButton(Context context, AttributeSet attributeSet) {
        this.enabled = true;
        this.visibility = 0;
        this.clickCount = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SucFooterButton);
        this.text = obtainStyledAttributes.getString(R$styleable.SucFooterButton_android_text);
        this.onClickListener = null;
        this.buttonType = getButtonTypeValue(obtainStyledAttributes.getInt(R$styleable.SucFooterButton_sucButtonType, 0));
        this.theme = obtainStyledAttributes.getResourceId(R$styleable.SucFooterButton_android_theme, 0);
        obtainStyledAttributes.recycle();
    }

    public FooterButton(CharSequence charSequence, View.OnClickListener onClickListener2, int i, int i2, Locale locale2, int i3) {
        this.enabled = true;
        this.visibility = 0;
        this.clickCount = 0;
        this.text = charSequence;
        this.onClickListener = onClickListener2;
        this.buttonType = i;
        this.theme = i2;
        this.locale = locale2;
        this.direction = i3;
    }

    public CharSequence getText() {
        return this.text;
    }

    public View.OnClickListener getOnClickListenerWhenDisabled() {
        return this.onClickListenerWhenDisabled;
    }

    public int getButtonType() {
        return this.buttonType;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
        OnButtonEventListener onButtonEventListener = this.buttonListener;
        if (onButtonEventListener != null) {
            onButtonEventListener.onEnabledChanged(z);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getVisibility() {
        return this.visibility;
    }

    public void setOnButtonEventListener(OnButtonEventListener onButtonEventListener) {
        if (onButtonEventListener != null) {
            this.buttonListener = onButtonEventListener;
            return;
        }
        throw new NullPointerException("Event listener of footer button may not be null.");
    }

    public void onClick(View view) {
        View.OnClickListener onClickListener2 = this.onClickListener;
        if (onClickListener2 != null) {
            this.clickCount++;
            onClickListener2.onClick(view);
        }
    }

    public final int getButtonTypeValue(int i) {
        if (i >= 0 && i <= 8) {
            return i;
        }
        throw new IllegalArgumentException("Not a ButtonType");
    }

    public final String getButtonTypeName() {
        switch (this.buttonType) {
            case 1:
                return "ADD_ANOTHER";
            case 2:
                return "CANCEL";
            case 3:
                return "CLEAR";
            case 4:
                return "DONE";
            case 5:
                return "NEXT";
            case 6:
                return "OPT_IN";
            case 7:
                return "SKIP";
            case 8:
                return "STOP";
            default:
                return "OTHER";
        }
    }

    @TargetApi(29)
    public PersistableBundle getMetrics(String str) {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(str + "_text", CustomEvent.trimsStringOverMaxLength(getText().toString()));
        persistableBundle.putString(str + "_type", getButtonTypeName());
        persistableBundle.putInt(str + "_onClickCount", this.clickCount);
        return persistableBundle;
    }

    public static class Builder {
        public int buttonType = 0;
        public final Context context;
        public int direction = -1;
        public Locale locale = null;
        public View.OnClickListener onClickListener = null;
        public String text = "";
        public int theme = 0;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setText(String str) {
            this.text = str;
            return this;
        }

        public Builder setListener(View.OnClickListener onClickListener2) {
            this.onClickListener = onClickListener2;
            return this;
        }

        public FooterButton build() {
            return new FooterButton(this.text, this.onClickListener, this.buttonType, this.theme, this.locale, this.direction);
        }
    }
}
