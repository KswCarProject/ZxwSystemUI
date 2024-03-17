package com.android.wm.shell.bubbles;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.View;

public interface BubbleViewProvider {
    Bitmap getAppBadge();

    Bitmap getBubbleIcon();

    int getDotColor();

    Path getDotPath();

    BubbleExpandedView getExpandedView();

    View getIconView();

    String getKey();

    int getTaskId();

    void setTaskViewVisibility(boolean z);

    boolean showDot();
}
