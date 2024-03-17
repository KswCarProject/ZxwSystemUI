package com.android.wm.shell.onehanded;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
import java.io.PrintWriter;

public class OneHandedSurfaceTransactionHelper {
    public final float mCornerRadius;
    public final float mCornerRadiusAdjustment;
    public final boolean mEnableCornerRadius;

    public interface SurfaceControlTransactionFactory {
        SurfaceControl.Transaction getTransaction();
    }

    public OneHandedSurfaceTransactionHelper(Context context) {
        Resources resources = context.getResources();
        float dimension = resources.getDimension(17105513);
        this.mCornerRadiusAdjustment = dimension;
        this.mCornerRadius = resources.getDimension(17105512) - dimension;
        this.mEnableCornerRadius = resources.getBoolean(R.bool.config_one_handed_enable_round_corner);
    }

    public OneHandedSurfaceTransactionHelper translate(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f) {
        transaction.setPosition(surfaceControl, 0.0f, f);
        return this;
    }

    public OneHandedSurfaceTransactionHelper crop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height());
        return this;
    }

    public OneHandedSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        if (this.mEnableCornerRadius) {
            transaction.setCornerRadius(surfaceControl, this.mCornerRadius);
        }
        return this;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedSurfaceTransactionHelperstates: ");
        printWriter.print("  mEnableCornerRadius=");
        printWriter.println(this.mEnableCornerRadius);
        printWriter.print("  mCornerRadiusAdjustment=");
        printWriter.println(this.mCornerRadiusAdjustment);
        printWriter.print("  mCornerRadius=");
        printWriter.println(this.mCornerRadius);
    }
}
