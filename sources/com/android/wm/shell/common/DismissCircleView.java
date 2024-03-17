package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.wm.shell.R;

public class DismissCircleView extends FrameLayout {
    public final ImageView mIconView;

    public DismissCircleView(Context context) {
        super(context);
        ImageView imageView = new ImageView(getContext());
        this.mIconView = imageView;
        Resources resources = getResources();
        setBackground(resources.getDrawable(R.drawable.dismiss_circle_background));
        imageView.setImageDrawable(resources.getDrawable(R.drawable.pip_ic_close_white));
        addView(imageView);
        setViewSizes();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setViewSizes();
    }

    public final void setViewSizes() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dismiss_target_x_size);
        this.mIconView.setLayoutParams(new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 17));
    }
}
