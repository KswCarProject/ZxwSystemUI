package com.android.wm.shell.pip.tv;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.ArraySet;
import android.util.Size;
import android.view.Gravity;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.Set;

public class TvPipBoundsAlgorithm extends PipBoundsAlgorithm {
    public static final String TAG = "TvPipBoundsAlgorithm";
    public int mFixedExpandedHeightInPx;
    public int mFixedExpandedWidthInPx;
    public final TvPipKeepClearAlgorithm mKeepClearAlgorithm = new TvPipKeepClearAlgorithm();
    public final TvPipBoundsState mTvPipBoundsState;

    public TvPipBoundsAlgorithm(Context context, TvPipBoundsState tvPipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        super(context, tvPipBoundsState, pipSnapAlgorithm);
        this.mTvPipBoundsState = tvPipBoundsState;
        reloadResources(context);
    }

    private void reloadResources(Context context) {
        Resources resources = context.getResources();
        this.mFixedExpandedHeightInPx = resources.getDimensionPixelSize(17105093);
        this.mFixedExpandedWidthInPx = resources.getDimensionPixelSize(17105094);
        this.mKeepClearAlgorithm.setPipAreaPadding(resources.getDimensionPixelSize(R.dimen.pip_keep_clear_area_padding));
        this.mKeepClearAlgorithm.setMaxRestrictedDistanceFraction((double) resources.getFraction(R.fraction.config_pipMaxRestrictedMoveDistance, 1, 1));
    }

    public void onConfigurationChanged(Context context) {
        super.onConfigurationChanged(context);
        reloadResources(context);
    }

    public Rect getEntryDestinationBounds() {
        updateExpandedPipSize();
        boolean z = this.mTvPipBoundsState.isTvExpandedPipSupported() && this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio() != 0.0f && !this.mTvPipBoundsState.isTvPipManuallyCollapsed();
        if (z) {
            updateGravityOnExpandToggled(0, true);
        }
        this.mTvPipBoundsState.setTvPipExpanded(z);
        return adjustBoundsForTemporaryDecor(getTvPipPlacement().getBounds());
    }

    public Rect getAdjustedDestinationBounds(Rect rect, float f) {
        return adjustBoundsForTemporaryDecor(getTvPipPlacement().getBounds());
    }

    public Rect adjustBoundsForTemporaryDecor(Rect rect) {
        Rect rect2 = new Rect(rect);
        Insets pipMenuTemporaryDecorInsets = this.mTvPipBoundsState.getPipMenuTemporaryDecorInsets();
        Insets subtract = Insets.subtract(Insets.NONE, pipMenuTemporaryDecorInsets);
        rect2.inset(pipMenuTemporaryDecorInsets);
        Gravity.apply(this.mTvPipBoundsState.getTvPipGravity(), rect2.width(), rect2.height(), rect, rect2);
        rect2.inset(subtract);
        return rect2;
    }

    public TvPipKeepClearAlgorithm.Placement getTvPipPlacement() {
        Size pipSize = getPipSize();
        Rect displayBounds = this.mTvPipBoundsState.getDisplayBounds();
        Size size = new Size(displayBounds.width(), displayBounds.height());
        Rect rect = new Rect();
        getInsetBounds(rect);
        Set<Rect> restrictedKeepClearAreas = this.mTvPipBoundsState.getRestrictedKeepClearAreas();
        Set<Rect> unrestrictedKeepClearAreas = this.mTvPipBoundsState.getUnrestrictedKeepClearAreas();
        if (this.mTvPipBoundsState.isImeShowing()) {
            Rect rect2 = new Rect(0, rect.bottom - this.mTvPipBoundsState.getImeHeight(), rect.right, rect.bottom);
            ArraySet arraySet = new ArraySet(unrestrictedKeepClearAreas);
            arraySet.add(rect2);
            unrestrictedKeepClearAreas = arraySet;
        }
        this.mKeepClearAlgorithm.setGravity(this.mTvPipBoundsState.getTvPipGravity());
        this.mKeepClearAlgorithm.setScreenSize(size);
        this.mKeepClearAlgorithm.setMovementBounds(rect);
        this.mKeepClearAlgorithm.setStashOffset(this.mTvPipBoundsState.getStashOffset());
        this.mKeepClearAlgorithm.setPipPermanentDecorInsets(this.mTvPipBoundsState.getPipMenuPermanentDecorInsets());
        return this.mKeepClearAlgorithm.calculatePipPosition(pipSize, restrictedKeepClearAreas, unrestrictedKeepClearAreas);
    }

    public int updateGravityOnExpandToggled(int i, boolean z) {
        int i2 = 0;
        if (!this.mTvPipBoundsState.isTvExpandedPipSupported()) {
            return 0;
        }
        if (z && this.mTvPipBoundsState.getTvFixedPipOrientation() == 0) {
            float desiredTvExpandedAspectRatio = this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio();
            if (desiredTvExpandedAspectRatio == 0.0f) {
                return 0;
            }
            if (desiredTvExpandedAspectRatio < 1.0f) {
                this.mTvPipBoundsState.setTvFixedPipOrientation(1);
            } else {
                this.mTvPipBoundsState.setTvFixedPipOrientation(2);
            }
        }
        int tvPipGravity = this.mTvPipBoundsState.getTvPipGravity();
        if (z) {
            i2 = this.mTvPipBoundsState.getTvPipGravity();
            i = this.mTvPipBoundsState.getTvFixedPipOrientation() == 2 ? (tvPipGravity & 112) | 1 : (tvPipGravity & 7) | 16;
        } else if (i == 0) {
            i = this.mTvPipBoundsState.getTvFixedPipOrientation() == 2 ? (tvPipGravity & 112) | 5 : (tvPipGravity & 7) | 80;
        }
        this.mTvPipBoundsState.setTvPipGravity(i);
        return i2;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0059, code lost:
        r3 = r3 | r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean updateGravity(int r5) {
        /*
            r4 = this;
            com.android.wm.shell.pip.tv.TvPipBoundsState r0 = r4.mTvPipBoundsState
            boolean r0 = r0.isTvPipExpanded()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0026
            com.android.wm.shell.pip.tv.TvPipBoundsState r0 = r4.mTvPipBoundsState
            int r0 = r0.getTvFixedPipOrientation()
            if (r0 != r2) goto L_0x001a
            r3 = 19
            if (r5 == r3) goto L_0x0025
            r3 = 20
            if (r5 == r3) goto L_0x0025
        L_0x001a:
            r3 = 2
            if (r0 != r3) goto L_0x0026
            r0 = 22
            if (r5 == r0) goto L_0x0025
            r0 = 21
            if (r5 != r0) goto L_0x0026
        L_0x0025:
            return r1
        L_0x0026:
            com.android.wm.shell.pip.tv.TvPipBoundsState r0 = r4.mTvPipBoundsState
            int r0 = r0.getTvPipGravity()
            switch(r5) {
                case 19: goto L_0x0038;
                case 20: goto L_0x0035;
                case 21: goto L_0x0033;
                case 22: goto L_0x0031;
                default: goto L_0x002f;
            }
        L_0x002f:
            r3 = r0
            goto L_0x003a
        L_0x0031:
            r3 = 5
            goto L_0x003a
        L_0x0033:
            r3 = 3
            goto L_0x003a
        L_0x0035:
            r3 = 80
            goto L_0x003a
        L_0x0038:
            r3 = 48
        L_0x003a:
            switch(r5) {
                case 19: goto L_0x004c;
                case 20: goto L_0x004c;
                case 21: goto L_0x003e;
                case 22: goto L_0x003e;
                default: goto L_0x003d;
            }
        L_0x003d:
            goto L_0x005a
        L_0x003e:
            com.android.wm.shell.pip.tv.TvPipBoundsState r5 = r4.mTvPipBoundsState
            boolean r5 = r5.isTvPipExpanded()
            if (r5 == 0) goto L_0x0049
            r3 = r3 | 16
            goto L_0x005a
        L_0x0049:
            r5 = r0 & 112(0x70, float:1.57E-43)
            goto L_0x0059
        L_0x004c:
            com.android.wm.shell.pip.tv.TvPipBoundsState r5 = r4.mTvPipBoundsState
            boolean r5 = r5.isTvPipExpanded()
            if (r5 == 0) goto L_0x0057
            r3 = r3 | 1
            goto L_0x005a
        L_0x0057:
            r5 = r0 & 7
        L_0x0059:
            r3 = r3 | r5
        L_0x005a:
            if (r3 == r0) goto L_0x0062
            com.android.wm.shell.pip.tv.TvPipBoundsState r4 = r4.mTvPipBoundsState
            r4.setTvPipGravity(r3)
            return r2
        L_0x0062:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm.updateGravity(int):boolean");
    }

    public final Size getPipSize() {
        if (this.mTvPipBoundsState.isTvExpandedPipSupported() && this.mTvPipBoundsState.isTvPipExpanded() && this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio() != 0.0f) {
            return this.mTvPipBoundsState.getTvExpandedSize();
        }
        Rect normalBounds = getNormalBounds();
        return new Size(normalBounds.width(), normalBounds.height());
    }

    public void updateExpandedPipSize() {
        Size size;
        Size size2;
        DisplayLayout displayLayout = this.mTvPipBoundsState.getDisplayLayout();
        float desiredTvExpandedAspectRatio = this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio();
        Insets pipMenuPermanentDecorInsets = this.mTvPipBoundsState.getPipMenuPermanentDecorInsets();
        if (desiredTvExpandedAspectRatio != 0.0f) {
            if (desiredTvExpandedAspectRatio < 1.0f) {
                if (this.mTvPipBoundsState.getTvFixedPipOrientation() == 2) {
                    size = this.mTvPipBoundsState.getTvExpandedSize();
                } else {
                    int height = ((displayLayout.height() - (this.mScreenEdgeInsets.y * 2)) - pipMenuPermanentDecorInsets.top) - pipMenuPermanentDecorInsets.bottom;
                    float f = ((float) this.mFixedExpandedWidthInPx) / desiredTvExpandedAspectRatio;
                    if (((float) height) > f) {
                        size = new Size(this.mFixedExpandedWidthInPx, (int) f);
                    } else {
                        size2 = new Size(this.mFixedExpandedWidthInPx, height);
                    }
                }
                this.mTvPipBoundsState.setTvExpandedSize(size);
            }
            if (this.mTvPipBoundsState.getTvFixedPipOrientation() == 1) {
                size = this.mTvPipBoundsState.getTvExpandedSize();
            } else {
                int width = ((displayLayout.width() - (this.mScreenEdgeInsets.x * 2)) - pipMenuPermanentDecorInsets.left) - pipMenuPermanentDecorInsets.right;
                float f2 = ((float) this.mFixedExpandedHeightInPx) * desiredTvExpandedAspectRatio;
                if (((float) width) > f2) {
                    size = new Size((int) f2, this.mFixedExpandedHeightInPx);
                } else {
                    size2 = new Size(width, this.mFixedExpandedHeightInPx);
                }
            }
            this.mTvPipBoundsState.setTvExpandedSize(size);
            size = size2;
            this.mTvPipBoundsState.setTvExpandedSize(size);
        } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            String valueOf = String.valueOf(TAG);
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -323100848, 0, (String) null, valueOf);
        }
    }
}
