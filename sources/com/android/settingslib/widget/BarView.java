package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarView extends LinearLayout {
    public TextView mBarSummary;
    public TextView mBarTitle;
    public View mBarView;
    public ImageView mIcon;

    public BarView(Context context) {
        super(context);
        init();
    }

    public BarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
        int color = context.obtainStyledAttributes(new int[]{16843829}).getColor(0, 0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SettingsBarView);
        int color2 = obtainStyledAttributes.getColor(R$styleable.SettingsBarView_barColor, color);
        obtainStyledAttributes.recycle();
        this.mBarView.setBackgroundColor(color2);
    }

    public CharSequence getTitle() {
        return this.mBarTitle.getText();
    }

    public CharSequence getSummary() {
        return this.mBarSummary.getText();
    }

    public final void init() {
        LayoutInflater.from(getContext()).inflate(R$layout.settings_bar_view, this);
        setOrientation(1);
        setGravity(81);
        this.mBarView = findViewById(R$id.bar_view);
        this.mIcon = (ImageView) findViewById(R$id.icon_view);
        this.mBarTitle = (TextView) findViewById(R$id.bar_title);
        this.mBarSummary = (TextView) findViewById(R$id.bar_summary);
    }
}