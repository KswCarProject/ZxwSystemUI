package com.android.systemui.volume;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import java.util.Objects;

public class SegmentedButtons extends LinearLayout {
    public static final int LABEL_RES_KEY = R$id.label;
    public static final Typeface MEDIUM = Typeface.create("sans-serif-medium", 0);
    public static final Typeface REGULAR = Typeface.create("sans-serif", 0);
    public final View.OnClickListener mClick = new View.OnClickListener() {
        public void onClick(View view) {
            SegmentedButtons.this.setSelectedValue(view.getTag(), true);
        }
    };
    public final ConfigurableTexts mConfigurableTexts;
    public final Context mContext;
    public final LayoutInflater mInflater;
    public Object mSelectedValue;

    public final void fireOnSelected(boolean z) {
    }

    public SegmentedButtons(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        setOrientation(0);
        this.mConfigurableTexts = new ConfigurableTexts(context);
    }

    public void setSelectedValue(Object obj, boolean z) {
        if (!Objects.equals(obj, this.mSelectedValue)) {
            this.mSelectedValue = obj;
            for (int i = 0; i < getChildCount(); i++) {
                TextView textView = (TextView) getChildAt(i);
                boolean equals = Objects.equals(this.mSelectedValue, textView.getTag());
                textView.setSelected(equals);
                setSelectedStyle(textView, equals);
            }
            fireOnSelected(z);
        }
    }

    public void setSelectedStyle(TextView textView, boolean z) {
        textView.setTypeface(z ? MEDIUM : REGULAR);
    }
}
