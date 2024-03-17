package com.android.systemui;

import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

public final class FontSizeUtils {
    public static void updateFontSize(View view, int i, int i2) {
        updateFontSize((TextView) view.findViewById(i), i2);
    }

    public static void updateFontSize(TextView textView, int i) {
        if (textView != null) {
            textView.setTextSize(0, (float) textView.getResources().getDimensionPixelSize(i));
        }
    }

    public static void updateFontSizeFromStyle(TextView textView, int i) {
        TypedArray obtainStyledAttributes = textView.getContext().obtainStyledAttributes(i, new int[]{16842901});
        textView.setTextSize(0, (float) obtainStyledAttributes.getDimensionPixelSize(0, (int) textView.getTextSize()));
        obtainStyledAttributes.recycle();
    }
}
