package com.android.systemui.media.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class MediaOutputSeekbar extends SeekBar {
    public static int scaleVolumeToProgress(int i) {
        return i * 1000;
    }

    public MediaOutputSeekbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setMin(0);
    }

    public static int scaleProgressToVolume(int i) {
        return i / 1000;
    }

    public int getVolume() {
        return getProgress() / 1000;
    }

    public void setVolume(int i) {
        setProgress(i * 1000, true);
    }

    public void setMaxVolume(int i) {
        setMax(i * 1000);
    }

    public void resetVolume() {
        setProgress(getMin());
    }
}
