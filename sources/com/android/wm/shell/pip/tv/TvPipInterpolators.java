package com.android.wm.shell.pip.tv;

import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

public class TvPipInterpolators {
    public static final Interpolator BROWSE = new PathInterpolator(0.18f, 1.0f, 0.22f, 1.0f);
    public static final Interpolator ENTER = new PathInterpolator(0.12f, 1.0f, 0.4f, 1.0f);
    public static final Interpolator EXIT = new PathInterpolator(0.4f, 1.0f, 0.12f, 1.0f);
    public static final Interpolator STANDARD = new PathInterpolator(0.2f, 0.1f, 0.0f, 1.0f);
}
