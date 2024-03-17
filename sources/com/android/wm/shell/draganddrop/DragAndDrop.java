package com.android.wm.shell.draganddrop;

import android.content.res.Configuration;

public interface DragAndDrop {
    void onConfigChanged(Configuration configuration);

    void onThemeChanged();
}
