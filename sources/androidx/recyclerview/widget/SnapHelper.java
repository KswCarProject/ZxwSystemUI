package androidx.recyclerview.widget;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SnapHelper extends RecyclerView.OnFlingListener {
    public Scroller mGravityScroller;
    public RecyclerView mRecyclerView;
    public final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        public boolean mScrolled = false;

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i == 0 && this.mScrolled) {
                this.mScrolled = false;
                SnapHelper.this.snapToTargetExistingView();
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (i != 0 || i2 != 0) {
                this.mScrolled = true;
            }
        }
    };

    public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View view);

    public abstract RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager);

    public abstract View findSnapView(RecyclerView.LayoutManager layoutManager);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int i, int i2);

    public boolean onFling(int i, int i2) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null || this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
        if ((Math.abs(i2) > minFlingVelocity || Math.abs(i) > minFlingVelocity) && snapFromFling(layoutManager, i, i2)) {
            return true;
        }
        return false;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (recyclerView != null) {
                setupCallbacks();
                this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
                snapToTargetExistingView();
            }
        }
    }

    public final void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() == null) {
            this.mRecyclerView.addOnScrollListener(this.mScrollListener);
            this.mRecyclerView.setOnFlingListener(this);
            return;
        }
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    public final void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener((RecyclerView.OnFlingListener) null);
    }

    public final boolean snapFromFling(RecyclerView.LayoutManager layoutManager, int i, int i2) {
        RecyclerView.SmoothScroller createScroller;
        int findTargetSnapPosition;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (createScroller = createScroller(layoutManager)) == null || (findTargetSnapPosition = findTargetSnapPosition(layoutManager, i, i2)) == -1) {
            return false;
        }
        createScroller.setTargetPosition(findTargetSnapPosition);
        layoutManager.startSmoothScroll(createScroller);
        return true;
    }

    public void snapToTargetExistingView() {
        RecyclerView.LayoutManager layoutManager;
        View findSnapView;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null && (layoutManager = recyclerView.getLayoutManager()) != null && (findSnapView = findSnapView(layoutManager)) != null) {
            int[] calculateDistanceToFinalSnap = calculateDistanceToFinalSnap(layoutManager, findSnapView);
            int i = calculateDistanceToFinalSnap[0];
            if (i != 0 || calculateDistanceToFinalSnap[1] != 0) {
                this.mRecyclerView.smoothScrollBy(i, calculateDistanceToFinalSnap[1]);
            }
        }
    }
}
