package com.android.wm.shell.unfold;

import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;

public class UnfoldBackgroundController {
    public final float[] mBackgroundColor;
    public SurfaceControl mBackgroundLayer;
    public final RootTaskDisplayAreaOrganizer mRootTaskDisplayAreaOrganizer;

    public UnfoldBackgroundController(Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer) {
        this.mRootTaskDisplayAreaOrganizer = rootTaskDisplayAreaOrganizer;
        this.mBackgroundColor = getBackgroundColor(context);
    }

    public void ensureBackground(SurfaceControl.Transaction transaction) {
        if (this.mBackgroundLayer == null) {
            SurfaceControl.Builder colorLayer = new SurfaceControl.Builder().setName("app-unfold-background").setCallsite("AppUnfoldTransitionController").setColorLayer();
            this.mRootTaskDisplayAreaOrganizer.attachToDisplayArea(0, colorLayer);
            SurfaceControl build = colorLayer.build();
            this.mBackgroundLayer = build;
            transaction.setColor(build, this.mBackgroundColor).show(this.mBackgroundLayer).setLayer(this.mBackgroundLayer, -1);
        }
    }

    public void removeBackground(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mBackgroundLayer;
        if (surfaceControl != null) {
            if (surfaceControl.isValid()) {
                transaction.remove(this.mBackgroundLayer);
            }
            this.mBackgroundLayer = null;
        }
    }

    public final float[] getBackgroundColor(Context context) {
        int color = context.getResources().getColor(R.color.unfold_transition_background);
        return new float[]{((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f};
    }
}
