package com.android.wm.shell.compatui.letterboxedu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.wm.shell.R;

class LetterboxEduDialogActionLayout extends FrameLayout {
    public LetterboxEduDialogActionLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.LetterboxEduDialogActionLayout, i, i2);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.LetterboxEduDialogActionLayout_icon, 0);
        String string = obtainStyledAttributes.getString(R.styleable.LetterboxEduDialogActionLayout_text);
        obtainStyledAttributes.recycle();
        View inflate = FrameLayout.inflate(getContext(), R.layout.letterbox_education_dialog_action_layout, this);
        ((ImageView) inflate.findViewById(R.id.letterbox_education_dialog_action_icon)).setImageResource(resourceId);
        ((TextView) inflate.findViewById(R.id.letterbox_education_dialog_action_text)).setText(string);
    }
}
