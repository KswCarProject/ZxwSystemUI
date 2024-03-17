package com.android.systemui.tuner;

import android.view.View;
import com.android.systemui.tuner.TunerService;

public class TunablePadding implements TunerService.Tunable {
    public final int mDefaultSize;
    public final float mDensity;
    public final int mFlags;
    public final View mView;

    public void onTuningChanged(String str, String str2) {
        int i = this.mDefaultSize;
        if (str2 != null) {
            try {
                i = (int) (((float) Integer.parseInt(str2)) * this.mDensity);
            } catch (NumberFormatException unused) {
            }
        }
        int i2 = 2;
        int i3 = this.mView.isLayoutRtl() ? 2 : 1;
        if (this.mView.isLayoutRtl()) {
            i2 = 1;
        }
        this.mView.setPadding(getPadding(i, i3), getPadding(i, 4), getPadding(i, i2), getPadding(i, 8));
    }

    public final int getPadding(int i, int i2) {
        if ((this.mFlags & i2) != 0) {
            return i;
        }
        return 0;
    }

    public static class TunablePaddingService {
        public final TunerService mTunerService;

        public TunablePaddingService(TunerService tunerService) {
            this.mTunerService = tunerService;
        }
    }
}
