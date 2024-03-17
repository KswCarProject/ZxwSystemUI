package com.android.wm.shell.pip.tv;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm;
import java.util.Objects;
import java.util.function.Supplier;

public class TvPipBoundsController {
    @VisibleForTesting
    public static final long POSITION_DEBOUNCE_TIMEOUT_MILLIS = 300;
    public final Runnable mApplyPendingPlacementRunnable = new TvPipBoundsController$$ExternalSyntheticLambda1(this);
    public final Supplier<Long> mClock;
    public final Context mContext;
    public Rect mCurrentPlacementBounds;
    public PipBoundsListener mListener;
    public final Handler mMainHandler;
    public TvPipKeepClearAlgorithm.Placement mPendingPlacement;
    public int mPendingPlacementAnimationDuration;
    public boolean mPendingStash;
    public Rect mPipTargetBounds;
    public int mResizeAnimationDuration;
    public int mStashDurationMs;
    public final TvPipBoundsAlgorithm mTvPipBoundsAlgorithm;
    public final TvPipBoundsState mTvPipBoundsState;
    public Runnable mUnstashRunnable;

    public interface PipBoundsListener {
        void onPipTargetBoundsChange(Rect rect, int i);
    }

    public TvPipBoundsController(Context context, Supplier<Long> supplier, Handler handler, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm) {
        this.mContext = context;
        this.mClock = supplier;
        this.mMainHandler = handler;
        this.mTvPipBoundsState = tvPipBoundsState;
        this.mTvPipBoundsAlgorithm = tvPipBoundsAlgorithm;
        loadConfigurations();
    }

    public final void loadConfigurations() {
        Resources resources = this.mContext.getResources();
        this.mResizeAnimationDuration = resources.getInteger(R.integer.config_pipResizeAnimationDuration);
        this.mStashDurationMs = resources.getInteger(R.integer.config_pipStashDuration);
    }

    public void setListener(PipBoundsListener pipBoundsListener) {
        this.mListener = pipBoundsListener;
    }

    @VisibleForTesting
    public void recalculatePipBounds(boolean z, boolean z2, int i, boolean z3) {
        int i2;
        TvPipKeepClearAlgorithm.Placement tvPipPlacement = this.mTvPipBoundsAlgorithm.getTvPipPlacement();
        if (z2) {
            i2 = 0;
        } else {
            i2 = tvPipPlacement.getStashType();
        }
        this.mTvPipBoundsState.setStashed(i2);
        if (z) {
            cancelScheduledPlacement();
            applyPlacementBounds(tvPipPlacement.getAnchorBounds(), i);
        } else if (z2) {
            cancelScheduledPlacement();
            applyPlacementBounds(tvPipPlacement.getUnstashedBounds(), i);
        } else if (z3) {
            cancelScheduledPlacement();
            applyPlacementBounds(tvPipPlacement.getBounds(), i);
            scheduleUnstashIfNeeded(tvPipPlacement);
        } else {
            applyPlacementBounds(this.mCurrentPlacementBounds, i);
            schedulePinnedStackPlacement(tvPipPlacement, i);
        }
    }

    public final void schedulePinnedStackPlacement(TvPipKeepClearAlgorithm.Placement placement, int i) {
        TvPipKeepClearAlgorithm.Placement placement2 = this.mPendingPlacement;
        boolean z = true;
        if (placement2 == null || !Objects.equals(placement2.getBounds(), placement.getBounds())) {
            if (placement.getStashType() == 0 || (!this.mPendingStash && !placement.getTriggerStash())) {
                z = false;
            }
            this.mPendingStash = z;
            this.mMainHandler.removeCallbacks(this.mApplyPendingPlacementRunnable);
            this.mPendingPlacement = placement;
            this.mPendingPlacementAnimationDuration = i;
            this.mMainHandler.postAtTime(this.mApplyPendingPlacementRunnable, this.mClock.get().longValue() + 300);
            return;
        }
        if (!this.mPendingStash && !placement.getTriggerStash()) {
            z = false;
        }
        this.mPendingStash = z;
    }

    public final void scheduleUnstashIfNeeded(TvPipKeepClearAlgorithm.Placement placement) {
        Runnable runnable = this.mUnstashRunnable;
        if (runnable != null) {
            this.mMainHandler.removeCallbacks(runnable);
            this.mUnstashRunnable = null;
        }
        if (placement.getUnstashDestinationBounds() != null) {
            TvPipBoundsController$$ExternalSyntheticLambda0 tvPipBoundsController$$ExternalSyntheticLambda0 = new TvPipBoundsController$$ExternalSyntheticLambda0(this, placement);
            this.mUnstashRunnable = tvPipBoundsController$$ExternalSyntheticLambda0;
            this.mMainHandler.postAtTime(tvPipBoundsController$$ExternalSyntheticLambda0, this.mClock.get().longValue() + ((long) this.mStashDurationMs));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUnstashIfNeeded$0(TvPipKeepClearAlgorithm.Placement placement) {
        applyPlacementBounds(placement.getUnstashDestinationBounds(), this.mResizeAnimationDuration);
        this.mUnstashRunnable = null;
    }

    public final void applyPendingPlacement() {
        TvPipKeepClearAlgorithm.Placement placement = this.mPendingPlacement;
        if (placement != null) {
            if (this.mPendingStash) {
                this.mPendingStash = false;
                scheduleUnstashIfNeeded(placement);
            }
            if (this.mUnstashRunnable != null) {
                applyPlacementBounds(this.mPendingPlacement.getBounds(), this.mPendingPlacementAnimationDuration);
            } else {
                applyPlacementBounds(this.mPendingPlacement.getUnstashedBounds(), this.mPendingPlacementAnimationDuration);
            }
        }
        this.mPendingPlacement = null;
    }

    public void onPipDismissed() {
        this.mCurrentPlacementBounds = null;
        this.mPipTargetBounds = null;
        cancelScheduledPlacement();
    }

    public final void cancelScheduledPlacement() {
        this.mMainHandler.removeCallbacks(this.mApplyPendingPlacementRunnable);
        this.mPendingPlacement = null;
        Runnable runnable = this.mUnstashRunnable;
        if (runnable != null) {
            this.mMainHandler.removeCallbacks(runnable);
            this.mUnstashRunnable = null;
        }
    }

    public final void applyPlacementBounds(Rect rect, int i) {
        if (rect != null) {
            this.mCurrentPlacementBounds = rect;
            movePipTo(this.mTvPipBoundsAlgorithm.adjustBoundsForTemporaryDecor(rect), i);
        }
    }

    public final void movePipTo(Rect rect, int i) {
        if (!Objects.equals(this.mPipTargetBounds, rect)) {
            this.mPipTargetBounds = rect;
            PipBoundsListener pipBoundsListener = this.mListener;
            if (pipBoundsListener != null) {
                pipBoundsListener.onPipTargetBoundsChange(rect, i);
            }
        }
    }
}
