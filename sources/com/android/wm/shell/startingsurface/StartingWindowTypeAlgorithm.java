package com.android.wm.shell.startingsurface;

import android.window.StartingWindowInfo;

public interface StartingWindowTypeAlgorithm {
    @StartingWindowInfo.StartingWindowType
    int getSuggestedWindowType(StartingWindowInfo startingWindowInfo);
}
