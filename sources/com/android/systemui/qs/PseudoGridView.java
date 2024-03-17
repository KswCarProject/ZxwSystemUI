package com.android.systemui.qs;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.systemui.R$styleable;
import java.lang.ref.WeakReference;

public class PseudoGridView extends ViewGroup {
    public int mFixedChildWidth = -1;
    public int mHorizontalSpacing;
    public int mNumColumns = 3;
    public int mVerticalSpacing;

    public PseudoGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PseudoGridView);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R$styleable.PseudoGridView_numColumns) {
                this.mNumColumns = obtainStyledAttributes.getInt(index, 3);
            } else if (index == R$styleable.PseudoGridView_verticalSpacing) {
                this.mVerticalSpacing = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R$styleable.PseudoGridView_horizontalSpacing) {
                this.mHorizontalSpacing = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R$styleable.PseudoGridView_fixedChildWidth) {
                this.mFixedChildWidth = obtainStyledAttributes.getDimensionPixelSize(index, -1);
            }
        }
        obtainStyledAttributes.recycle();
    }

    public void onMeasure(int i, int i2) {
        if (View.MeasureSpec.getMode(i) != 0) {
            int size = View.MeasureSpec.getSize(i);
            int i3 = this.mFixedChildWidth;
            int i4 = this.mNumColumns;
            int i5 = this.mHorizontalSpacing;
            int i6 = (i3 * i4) + ((i4 - 1) * i5);
            if (i3 == -1 || i6 > size) {
                i3 = (size - ((i4 - 1) * i5)) / i4;
            } else {
                size = (i3 * i4) + (i5 * (i4 - 1));
            }
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
            int childCount = getChildCount();
            int i7 = this.mNumColumns;
            int i8 = ((childCount + i7) - 1) / i7;
            int i9 = 0;
            for (int i10 = 0; i10 < i8; i10++) {
                int i11 = this.mNumColumns;
                int i12 = i10 * i11;
                int min = Math.min(i11 + i12, childCount);
                int i13 = 0;
                for (int i14 = i12; i14 < min; i14++) {
                    View childAt = getChildAt(i14);
                    childAt.measure(makeMeasureSpec, 0);
                    i13 = Math.max(i13, childAt.getMeasuredHeight());
                }
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i13, 1073741824);
                while (i12 < min) {
                    View childAt2 = getChildAt(i12);
                    if (childAt2.getMeasuredHeight() != i13) {
                        childAt2.measure(makeMeasureSpec, makeMeasureSpec2);
                    }
                    i12++;
                }
                i9 += i13;
                if (i10 > 0) {
                    i9 += this.mVerticalSpacing;
                }
            }
            setMeasuredDimension(size, ViewGroup.resolveSizeAndState(i9, i2, 0));
            return;
        }
        throw new UnsupportedOperationException("Needs a maximum width");
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        boolean isLayoutRtl = isLayoutRtl();
        int childCount = getChildCount();
        int i6 = this.mNumColumns;
        int i7 = ((childCount + i6) - 1) / i6;
        int i8 = 0;
        for (int i9 = 0; i9 < i7; i9++) {
            int width = isLayoutRtl ? getWidth() : 0;
            int i10 = this.mNumColumns;
            int i11 = i9 * i10;
            int min = Math.min(i10 + i11, childCount);
            int i12 = 0;
            while (i11 < min) {
                View childAt = getChildAt(i11);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (isLayoutRtl) {
                    width -= measuredWidth;
                }
                childAt.layout(width, i8, width + measuredWidth, i8 + measuredHeight);
                i12 = Math.max(i12, measuredHeight);
                if (isLayoutRtl) {
                    i5 = width - this.mHorizontalSpacing;
                } else {
                    i5 = width + measuredWidth + this.mHorizontalSpacing;
                }
                i11++;
            }
            i8 += i12 + this.mVerticalSpacing;
        }
    }

    public static class ViewGroupAdapterBridge extends DataSetObserver {
        public final BaseAdapter mAdapter;
        public boolean mReleased = false;
        public final WeakReference<ViewGroup> mViewGroup;

        public static void link(ViewGroup viewGroup, BaseAdapter baseAdapter) {
            new ViewGroupAdapterBridge(viewGroup, baseAdapter);
        }

        public ViewGroupAdapterBridge(ViewGroup viewGroup, BaseAdapter baseAdapter) {
            this.mViewGroup = new WeakReference<>(viewGroup);
            this.mAdapter = baseAdapter;
            baseAdapter.registerDataSetObserver(this);
            refresh();
        }

        public final void refresh() {
            if (!this.mReleased) {
                ViewGroup viewGroup = (ViewGroup) this.mViewGroup.get();
                if (viewGroup == null) {
                    release();
                    return;
                }
                int childCount = viewGroup.getChildCount();
                int count = this.mAdapter.getCount();
                int max = Math.max(childCount, count);
                for (int i = 0; i < max; i++) {
                    if (i < count) {
                        View view = null;
                        if (i < childCount) {
                            view = viewGroup.getChildAt(i);
                        }
                        View view2 = this.mAdapter.getView(i, view, viewGroup);
                        if (view == null) {
                            viewGroup.addView(view2);
                        } else if (view != view2) {
                            viewGroup.removeViewAt(i);
                            viewGroup.addView(view2, i);
                        }
                    } else {
                        viewGroup.removeViewAt(viewGroup.getChildCount() - 1);
                    }
                }
            }
        }

        public void onChanged() {
            refresh();
        }

        public void onInvalidated() {
            release();
        }

        public final void release() {
            if (!this.mReleased) {
                this.mReleased = true;
                this.mAdapter.unregisterDataSetObserver(this);
            }
        }
    }
}
