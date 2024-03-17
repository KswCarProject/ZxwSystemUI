package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$layout;
import com.google.android.setupdesign.R$styleable;

public class SwitchItem extends Item implements CompoundButton.OnCheckedChangeListener {
    public boolean checked = false;

    public SwitchItem() {
    }

    public SwitchItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudSwitchItem);
        this.checked = obtainStyledAttributes.getBoolean(R$styleable.SudSwitchItem_android_checked, false);
        obtainStyledAttributes.recycle();
    }

    public int getDefaultLayoutResource() {
        return R$layout.sud_items_switch;
    }

    public void onBindView(View view) {
        super.onBindView(view);
        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R$id.sud_items_switch);
        switchCompat.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        switchCompat.setChecked(this.checked);
        switchCompat.setOnCheckedChangeListener(this);
        switchCompat.setEnabled(isEnabled());
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.checked = z;
    }
}
