package com.android.wm.shell.startingsurface.tv;

import android.window.StartingWindowInfo;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;

public class TvStartingWindowTypeAlgorithm implements StartingWindowTypeAlgorithm {
    public int getSuggestedWindowType(StartingWindowInfo startingWindowInfo) {
        return 3;
    }
}
