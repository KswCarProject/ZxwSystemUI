package androidx.viewpager2.widget;

import androidx.recyclerview.widget.RecyclerView;

public final class FakeDrag {
    public final RecyclerView mRecyclerView;
    public final ScrollEventAdapter mScrollEventAdapter;
    public final ViewPager2 mViewPager;

    public FakeDrag(ViewPager2 viewPager2, ScrollEventAdapter scrollEventAdapter, RecyclerView recyclerView) {
        this.mViewPager = viewPager2;
        this.mScrollEventAdapter = scrollEventAdapter;
        this.mRecyclerView = recyclerView;
    }

    public boolean isFakeDragging() {
        return this.mScrollEventAdapter.isFakeDragging();
    }
}
