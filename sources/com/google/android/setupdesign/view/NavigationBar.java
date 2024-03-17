package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.setupdesign.R$attr;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.R$layout;
import com.google.android.setupdesign.R$style;

public class NavigationBar extends LinearLayout implements View.OnClickListener {
    public Button backButton;
    public Button moreButton;
    public Button nextButton;

    public void onClick(View view) {
    }

    public static int getNavbarTheme(Context context) {
        boolean z = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{R$attr.sudNavBarTheme, 16842800, 16842801});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId == 0) {
            float[] fArr = new float[3];
            float[] fArr2 = new float[3];
            Color.colorToHSV(obtainStyledAttributes.getColor(1, 0), fArr);
            Color.colorToHSV(obtainStyledAttributes.getColor(2, 0), fArr2);
            if (fArr[2] > fArr2[2]) {
                z = true;
            }
            resourceId = z ? R$style.SudNavBarThemeDark : R$style.SudNavBarThemeLight;
        }
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Context getThemedContext(Context context) {
        return new ContextThemeWrapper(context, getNavbarTheme(context));
    }

    public NavigationBar(Context context) {
        super(getThemedContext(context));
        init();
    }

    public NavigationBar(Context context, AttributeSet attributeSet) {
        super(getThemedContext(context), attributeSet);
        init();
    }

    @TargetApi(11)
    public NavigationBar(Context context, AttributeSet attributeSet, int i) {
        super(getThemedContext(context), attributeSet, i);
        init();
    }

    public final void init() {
        if (!isInEditMode()) {
            View.inflate(getContext(), R$layout.sud_navbar_view, this);
            this.nextButton = (Button) findViewById(R$id.sud_navbar_next);
            this.backButton = (Button) findViewById(R$id.sud_navbar_back);
            this.moreButton = (Button) findViewById(R$id.sud_navbar_more);
        }
    }
}
