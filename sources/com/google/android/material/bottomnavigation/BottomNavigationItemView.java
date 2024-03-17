package com.google.android.material.bottomnavigation;

import android.content.Context;
import com.google.android.material.R$dimen;
import com.google.android.material.R$layout;
import com.google.android.material.navigation.NavigationBarItemView;

public class BottomNavigationItemView extends NavigationBarItemView {
    public BottomNavigationItemView(Context context) {
        super(context);
    }

    public int getItemLayoutResId() {
        return R$layout.design_bottom_navigation_item;
    }

    public int getItemDefaultMarginResId() {
        return R$dimen.design_bottom_navigation_margin;
    }
}
