package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: VariableDateView.kt */
public final class VariableDateView extends TextView {
    public boolean freezeSwitching;
    @NotNull
    public final String longerPattern;
    @Nullable
    public OnMeasureListener onMeasureListener;
    @NotNull
    public final String shorterPattern;

    /* compiled from: VariableDateView.kt */
    public interface OnMeasureListener {
        void onMeasureAction(int i);
    }

    public VariableDateView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.VariableDateView, 0, 0);
        String string = obtainStyledAttributes.getString(R$styleable.VariableDateView_longDatePattern);
        this.longerPattern = string == null ? context.getString(R$string.system_ui_date_pattern) : string;
        String string2 = obtainStyledAttributes.getString(R$styleable.VariableDateView_shortDatePattern);
        this.shorterPattern = string2 == null ? context.getString(R$string.abbrev_month_day_no_year) : string2;
        obtainStyledAttributes.recycle();
    }

    @NotNull
    public final String getLongerPattern() {
        return this.longerPattern;
    }

    @NotNull
    public final String getShorterPattern() {
        return this.shorterPattern;
    }

    public final boolean getFreezeSwitching() {
        return this.freezeSwitching;
    }

    public final void setFreezeSwitching(boolean z) {
        this.freezeSwitching = z;
    }

    public final void onAttach(@Nullable OnMeasureListener onMeasureListener2) {
        this.onMeasureListener = onMeasureListener2;
    }

    public void onMeasure(int i, int i2) {
        OnMeasureListener onMeasureListener2;
        int size = (View.MeasureSpec.getSize(i) - getPaddingStart()) - getPaddingEnd();
        if (!(View.MeasureSpec.getMode(i) == 0 || this.freezeSwitching || (onMeasureListener2 = this.onMeasureListener) == null)) {
            onMeasureListener2.onMeasureAction(size);
        }
        super.onMeasure(i, i2);
    }

    public final float getDesiredWidthForText(@NotNull CharSequence charSequence) {
        return StaticLayout.getDesiredWidth(charSequence, getPaint());
    }
}
