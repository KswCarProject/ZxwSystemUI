package com.android.systemui.qs;

import android.util.Log;
import android.view.MotionEvent;

/* compiled from: FooterActionsView.kt */
public final class FooterActionsViewKt {
    public static final boolean VERBOSE = Log.isLoggable("FooterActionsView", 2);

    public static final String getString(MotionEvent motionEvent) {
        return '(' + motionEvent.getId() + "): (" + motionEvent.getX() + ',' + motionEvent.getY() + ')';
    }
}
