package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.wm.shell.R;

public class PipMenuActionView extends FrameLayout {
    public View mCustomCloseBackground;
    public ImageView mImageView;

    public PipMenuActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(R.id.image);
        this.mCustomCloseBackground = findViewById(R.id.custom_close_bg);
    }

    public void setImageDrawable(Drawable drawable) {
        this.mImageView.setImageDrawable(drawable);
    }

    public void setCustomCloseBackgroundVisibility(int i) {
        this.mCustomCloseBackground.setVisibility(i);
    }
}
