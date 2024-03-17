package com.android.wm.shell.common;

import android.view.SurfaceControl;
import android.view.SurfaceSession;

public class SurfaceUtils {
    public static SurfaceControl makeDimLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, String str, SurfaceSession surfaceSession) {
        SurfaceControl makeColorLayer = makeColorLayer(surfaceControl, str, surfaceSession);
        transaction.setLayer(makeColorLayer, Integer.MAX_VALUE).setColor(makeColorLayer, new float[]{0.0f, 0.0f, 0.0f});
        return makeColorLayer;
    }

    public static SurfaceControl makeColorLayer(SurfaceControl surfaceControl, String str, SurfaceSession surfaceSession) {
        return new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setColorLayer().setName(str).setCallsite("SurfaceUtils.makeColorLayer").build();
    }
}
