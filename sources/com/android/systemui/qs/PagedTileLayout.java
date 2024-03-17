package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PagedTileLayout extends ViewPager implements QSPanel.QSTileLayout {
    public static final Interpolator SCROLL_CUBIC = new PagedTileLayout$$ExternalSyntheticLambda0();
    public final PagerAdapter mAdapter;
    public AnimatorSet mBounceAnimatorSet;
    public boolean mDistributeTiles = false;
    public int mExcessHeight;
    public int mLastExcessHeight;
    public float mLastExpansion;
    public int mLastMaxHeight = -1;
    public int mLayoutDirection;
    public int mLayoutOrientation;
    public boolean mListening;
    public int mMaxColumns = 100;
    public int mMinRows = 1;
    public final ViewPager.OnPageChangeListener mOnPageChangeListener;
    public PageIndicator mPageIndicator;
    public float mPageIndicatorPosition;
    public PageListener mPageListener;
    public int mPageToRestore = -1;
    public final ArrayList<TileLayout> mPages = new ArrayList<>();
    public Scroller mScroller;
    public final ArrayList<QSPanelControllerBase.TileRecord> mTiles = new ArrayList<>();
    public final UiEventLogger mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();

    public interface PageListener {
        void onPageChanged(boolean z, int i);
    }

    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public PagedTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        AnonymousClass2 r0 = new ViewPager.SimpleOnPageChangeListener() {
            public int mCurrentScrollState = 0;
            public boolean mIsScrollJankTraceBegin = false;

            public void onPageSelected(int i) {
                PagedTileLayout.this.updateSelected();
                if (PagedTileLayout.this.mPageIndicator != null && PagedTileLayout.this.mPageListener != null) {
                    boolean z = true;
                    if (PagedTileLayout.this.isLayoutRtl()) {
                        i = (PagedTileLayout.this.mPages.size() - 1) - i;
                    }
                    PageListener r2 = PagedTileLayout.this.mPageListener;
                    if (i != 0) {
                        z = false;
                    }
                    r2.onPageChanged(z, i);
                }
            }

            public void onPageScrolled(int i, float f, int i2) {
                boolean z = true;
                if (!this.mIsScrollJankTraceBegin && this.mCurrentScrollState == 1) {
                    InteractionJankMonitor.getInstance().begin(PagedTileLayout.this, 6);
                    this.mIsScrollJankTraceBegin = true;
                }
                if (PagedTileLayout.this.mPageIndicator != null) {
                    PagedTileLayout.this.mPageIndicatorPosition = ((float) i) + f;
                    PagedTileLayout.this.mPageIndicator.setLocation(PagedTileLayout.this.mPageIndicatorPosition);
                    if (PagedTileLayout.this.mPageListener != null) {
                        if (PagedTileLayout.this.isLayoutRtl()) {
                            i = (PagedTileLayout.this.mPages.size() - 1) - i;
                        }
                        PageListener r4 = PagedTileLayout.this.mPageListener;
                        if (!(i2 == 0 && i == 0)) {
                            z = false;
                        }
                        if (i2 != 0) {
                            i = -1;
                        }
                        r4.onPageChanged(z, i);
                    }
                }
            }

            public void onPageScrollStateChanged(int i) {
                if (i != this.mCurrentScrollState && i == 0) {
                    InteractionJankMonitor.getInstance().end(6);
                    this.mIsScrollJankTraceBegin = false;
                }
                this.mCurrentScrollState = i;
            }
        };
        this.mOnPageChangeListener = r0;
        AnonymousClass3 r1 = new PagerAdapter() {
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
                viewGroup.removeView((View) obj);
                PagedTileLayout.this.updateListening();
            }

            public Object instantiateItem(ViewGroup viewGroup, int i) {
                if (PagedTileLayout.this.isLayoutRtl()) {
                    i = (PagedTileLayout.this.mPages.size() - 1) - i;
                }
                ViewGroup viewGroup2 = (ViewGroup) PagedTileLayout.this.mPages.get(i);
                if (viewGroup2.getParent() != null) {
                    viewGroup.removeView(viewGroup2);
                }
                viewGroup.addView(viewGroup2);
                PagedTileLayout.this.updateListening();
                return viewGroup2;
            }

            public int getCount() {
                return PagedTileLayout.this.mPages.size();
            }
        };
        this.mAdapter = r1;
        this.mScroller = new Scroller(context, SCROLL_CUBIC);
        setAdapter(r1);
        setOnPageChangeListener(r0);
        setCurrentItem(0, false);
        this.mLayoutOrientation = getResources().getConfiguration().orientation;
        this.mLayoutDirection = getLayoutDirection();
    }

    public void setPageMargin(int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int i2 = -i;
        marginLayoutParams.setMarginStart(i2);
        marginLayoutParams.setMarginEnd(i2);
        setLayoutParams(marginLayoutParams);
        int size = this.mPages.size();
        for (int i3 = 0; i3 < size; i3++) {
            View view = this.mPages.get(i3);
            view.setPadding(i, view.getPaddingTop(), i, view.getPaddingBottom());
        }
    }

    public void saveInstanceState(Bundle bundle) {
        int i = this.mPageToRestore;
        if (i == -1) {
            i = getCurrentPageNumber();
        }
        bundle.putInt("current_page", i);
    }

    public void restoreInstanceState(Bundle bundle) {
        this.mPageToRestore = bundle.getInt("current_page", -1);
    }

    public int getTilesHeight() {
        TileLayout tileLayout = this.mPages.get(0);
        if (tileLayout == null) {
            return 0;
        }
        return tileLayout.getTilesHeight();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            View view = this.mPages.get(i);
            if (view.getParent() == null) {
                view.dispatchConfigurationChanged(configuration);
            }
        }
        int i2 = this.mLayoutOrientation;
        int i3 = configuration.orientation;
        if (i2 != i3) {
            this.mLayoutOrientation = i3;
            this.mDistributeTiles = true;
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    public void onRtlPropertiesChanged(int i) {
        boolean z = true;
        if (this.mLayoutDirection != 1) {
            z = false;
        }
        int pageNumberForDirection = getPageNumberForDirection(z);
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            this.mLayoutDirection = i;
            setAdapter(this.mAdapter);
            setCurrentItem(pageNumberForDirection, false);
        }
    }

    public void setCurrentItem(int i, boolean z) {
        if (isLayoutRtl()) {
            i = (this.mPages.size() - 1) - i;
        }
        super.setCurrentItem(i, z);
    }

    public final int getCurrentPageNumber() {
        return getPageNumberForDirection(isLayoutRtl());
    }

    public final int getPageNumberForDirection(boolean z) {
        int currentItem = getCurrentItem();
        return z ? (this.mPages.size() - 1) - currentItem : currentItem;
    }

    public final void logVisibleTiles(TileLayout tileLayout) {
        for (int i = 0; i < tileLayout.mRecords.size(); i++) {
            QSTile qSTile = tileLayout.mRecords.get(i).tile;
            this.mUiEventLogger.logWithInstanceId(QSEvent.QS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
        }
    }

    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            updateListening();
        }
    }

    public void setSquishinessFraction(float f) {
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            this.mPages.get(i).setSquishinessFraction(f);
        }
    }

    public final void updateListening() {
        Iterator<TileLayout> it = this.mPages.iterator();
        while (it.hasNext()) {
            TileLayout next = it.next();
            next.setListening(next.getParent() != null && this.mListening);
        }
    }

    public void fakeDragBy(float f) {
        try {
            super.fakeDragBy(f);
            postInvalidateOnAnimation();
        } catch (NullPointerException e) {
            Log.e("PagedTileLayout", "FakeDragBy called before begin", e);
            post(new PagedTileLayout$$ExternalSyntheticLambda1(this, this.mPages.size() - 1));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fakeDragBy$1(int i) {
        setCurrentItem(i, true);
        AnimatorSet animatorSet = this.mBounceAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
        setOffscreenPageLimit(1);
    }

    public void endFakeDrag() {
        try {
            super.endFakeDrag();
        } catch (NullPointerException e) {
            Log.e("PagedTileLayout", "endFakeDrag called without velocityTracker", e);
        }
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (!isFakeDragging()) {
                beginFakeDrag();
            }
            fakeDragBy((float) (getScrollX() - this.mScroller.getCurrX()));
        } else if (isFakeDragging()) {
            endFakeDrag();
            AnimatorSet animatorSet = this.mBounceAnimatorSet;
            if (animatorSet != null) {
                animatorSet.start();
            }
            setOffscreenPageLimit(1);
        }
        super.computeScroll();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPages.add(createTileLayout());
        this.mAdapter.notifyDataSetChanged();
    }

    public final TileLayout createTileLayout() {
        TileLayout tileLayout = (TileLayout) LayoutInflater.from(getContext()).inflate(R$layout.qs_paged_page, this, false);
        tileLayout.setMinRows(this.mMinRows);
        tileLayout.setMaxColumns(this.mMaxColumns);
        return tileLayout;
    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.mPageIndicator = pageIndicator;
        pageIndicator.setNumPages(this.mPages.size());
        this.mPageIndicator.setLocation(this.mPageIndicatorPosition);
    }

    public void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mTiles.add(tileRecord);
        this.mDistributeTiles = true;
        requestLayout();
    }

    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        if (this.mTiles.remove(tileRecord)) {
            this.mDistributeTiles = true;
            requestLayout();
        }
    }

    public void setExpansion(float f, float f2) {
        this.mLastExpansion = f;
        updateSelected();
    }

    public final void updateSelected() {
        float f = this.mLastExpansion;
        if (f <= 0.0f || f >= 1.0f) {
            boolean z = f == 1.0f;
            setImportantForAccessibility(4);
            int currentPageNumber = getCurrentPageNumber();
            int i = 0;
            while (i < this.mPages.size()) {
                TileLayout tileLayout = this.mPages.get(i);
                tileLayout.setSelected(i == currentPageNumber ? z : false);
                if (tileLayout.isSelected()) {
                    logVisibleTiles(tileLayout);
                }
                i++;
            }
            setImportantForAccessibility(0);
        }
    }

    public void setPageListener(PageListener pageListener) {
        this.mPageListener = pageListener;
    }

    public List<String> getSpecsForPage(int i) {
        ArrayList arrayList = new ArrayList();
        if (i < 0) {
            return arrayList;
        }
        int maxTiles = this.mPages.get(0).maxTiles();
        int i2 = i * maxTiles;
        int i3 = (i + 1) * maxTiles;
        while (i2 < i3 && i2 < this.mTiles.size()) {
            arrayList.add(this.mTiles.get(i2).tile.getTileSpec());
            i2++;
        }
        return arrayList;
    }

    public final void distributeTiles() {
        emptyAndInflateOrRemovePages();
        int maxTiles = this.mPages.get(0).maxTiles();
        int size = this.mTiles.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            QSPanelControllerBase.TileRecord tileRecord = this.mTiles.get(i2);
            if (this.mPages.get(i).mRecords.size() == maxTiles) {
                i++;
            }
            this.mPages.get(i).addTile(tileRecord);
        }
    }

    public final void emptyAndInflateOrRemovePages() {
        int numPages = getNumPages();
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            this.mPages.get(i).removeAllViews();
        }
        if (size != numPages) {
            while (this.mPages.size() < numPages) {
                this.mPages.add(createTileLayout());
            }
            while (this.mPages.size() > numPages) {
                ArrayList<TileLayout> arrayList = this.mPages;
                arrayList.remove(arrayList.size() - 1);
            }
            this.mPageIndicator.setNumPages(this.mPages.size());
            setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
            int i2 = this.mPageToRestore;
            if (i2 != -1) {
                setCurrentItem(i2, false);
                this.mPageToRestore = -1;
            }
        }
    }

    public boolean updateResources() {
        boolean z = false;
        for (int i = 0; i < this.mPages.size(); i++) {
            z |= this.mPages.get(i).updateResources();
        }
        if (z) {
            this.mDistributeTiles = true;
            requestLayout();
        }
        return z;
    }

    public boolean setMinRows(int i) {
        this.mMinRows = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMinRows(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    public boolean setMaxColumns(int i) {
        this.mMaxColumns = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMaxColumns(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    public void setExcessHeight(int i) {
        this.mExcessHeight = i;
    }

    public void onMeasure(int i, int i2) {
        int size = this.mTiles.size();
        if (!(!this.mDistributeTiles && this.mLastMaxHeight == View.MeasureSpec.getSize(i2) && this.mLastExcessHeight == this.mExcessHeight)) {
            int size2 = View.MeasureSpec.getSize(i2);
            this.mLastMaxHeight = size2;
            int i3 = this.mExcessHeight;
            this.mLastExcessHeight = i3;
            if (this.mPages.get(0).updateMaxRows(size2 - i3, size) || this.mDistributeTiles) {
                this.mDistributeTiles = false;
                distributeTiles();
            }
            int i4 = this.mPages.get(0).mRows;
            for (int i5 = 0; i5 < this.mPages.size(); i5++) {
                this.mPages.get(i5).mRows = i4;
            }
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            int measuredHeight = getChildAt(i7).getMeasuredHeight();
            if (measuredHeight > i6) {
                i6 = measuredHeight;
            }
        }
        if (this.mPages.get(0).getParent() == null) {
            this.mPages.get(0).measure(i, i2);
            int measuredHeight2 = this.mPages.get(0).getMeasuredHeight();
            if (measuredHeight2 > i6) {
                i6 = measuredHeight2;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), i6 + getPaddingBottom());
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mPages.get(0).getParent() == null) {
            this.mPages.get(0).layout(i, i2, i3, i4);
        }
    }

    public int getColumnCount() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        return this.mPages.get(0).mColumns;
    }

    public int getNumPages() {
        int size = this.mTiles.size();
        int max = Math.max(size / this.mPages.get(0).maxTiles(), 1);
        return size > this.mPages.get(0).maxTiles() * max ? max + 1 : max;
    }

    public int getNumVisibleTiles() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        return this.mPages.get(getCurrentPageNumber()).mRecords.size();
    }

    public int getNumTilesFirstPage() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        return this.mPages.get(0).mRecords.size();
    }

    public void startTileReveal(Set<String> set, final Runnable runnable) {
        if (!set.isEmpty() && this.mPages.size() >= 2 && getScrollX() == 0 && beginFakeDrag()) {
            int size = this.mPages.size() - 1;
            ArrayList arrayList = new ArrayList();
            Iterator<QSPanelControllerBase.TileRecord> it = this.mPages.get(size).mRecords.iterator();
            while (it.hasNext()) {
                QSPanelControllerBase.TileRecord next = it.next();
                if (set.contains(next.tile.getTileSpec())) {
                    arrayList.add(setupBounceAnimator(next.tileView, arrayList.size()));
                }
            }
            if (arrayList.isEmpty()) {
                endFakeDrag();
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            this.mBounceAnimatorSet = animatorSet;
            animatorSet.playTogether(arrayList);
            this.mBounceAnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    PagedTileLayout.this.mBounceAnimatorSet = null;
                    runnable.run();
                }
            });
            setOffscreenPageLimit(size);
            int width = getWidth() * size;
            Scroller scroller = this.mScroller;
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            if (isLayoutRtl()) {
                width = -width;
            }
            scroller.startScroll(scrollX, scrollY, width, 0, 750);
            postInvalidateOnAnimation();
        }
    }

    public final int sanitizePageAction(int i) {
        int id = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT.getId();
        int id2 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT.getId();
        if (i == id || i == id2) {
            return !isLayoutRtl() ? i == id ? 8192 : 4096 : i == id ? 4096 : 8192;
        }
        return i;
    }

    public boolean performAccessibilityAction(int i, Bundle bundle) {
        int sanitizePageAction = sanitizePageAction(i);
        boolean performAccessibilityAction = super.performAccessibilityAction(sanitizePageAction, bundle);
        if (performAccessibilityAction && (sanitizePageAction == 8192 || sanitizePageAction == 4096)) {
            requestAccessibilityFocus();
        }
        return performAccessibilityAction;
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        if (getCurrentItem() != 0) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT);
        }
        if (getCurrentItem() != this.mPages.size() - 1) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT);
        }
    }

    public static Animator setupBounceAnimator(View view, int i) {
        view.setAlpha(0.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0f})});
        ofPropertyValuesHolder.setDuration(450);
        ofPropertyValuesHolder.setStartDelay((long) (i * 85));
        ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator(1.3f));
        return ofPropertyValuesHolder;
    }
}
