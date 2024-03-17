package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.RemeasuringLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QSPanel extends LinearLayout implements TunerService.Tunable {
    public View mBrightnessView;
    public final ArrayMap<View, Integer> mChildrenLayoutTop = new ArrayMap<>();
    public final Rect mClippingRect = new Rect();
    public Runnable mCollapseExpandAction;
    public int mContentMarginEnd;
    public int mContentMarginStart;
    public final Context mContext;
    public boolean mExpanded;
    public View mFooter;
    public PageIndicator mFooterPageIndicator;
    public LinearLayout mHorizontalContentContainer;
    public LinearLayout mHorizontalLinearLayout;
    public boolean mListening;
    public ViewGroup mMediaHostView;
    public final int mMediaTopMargin;
    public final int mMediaTotalBottomMargin;
    public int mMovableContentStartIndex;
    public final List<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new ArrayList();
    public boolean mShouldMoveMediaOnExpansion = true;
    public float mSquishinessFraction = 1.0f;
    public QSTileLayout mTileLayout;
    public boolean mUsingHorizontalLayout;
    public boolean mUsingMediaPlayer;

    public interface OnConfigurationChangedListener {
        void onConfigurationChange(Configuration configuration);
    }

    public interface QSTileLayout {
        void addTile(QSPanelControllerBase.TileRecord tileRecord);

        int getHeight();

        int getNumVisibleTiles();

        int getTilesHeight();

        void removeTile(QSPanelControllerBase.TileRecord tileRecord);

        void restoreInstanceState(Bundle bundle) {
        }

        void saveInstanceState(Bundle bundle) {
        }

        void setExpansion(float f, float f2) {
        }

        void setListening(boolean z, UiEventLogger uiEventLogger);

        boolean setMaxColumns(int i) {
            return false;
        }

        boolean setMinRows(int i) {
            return false;
        }

        void setSquishinessFraction(float f);

        boolean updateResources();
    }

    public boolean displayMediaMarginsOnMedia() {
        return true;
    }

    public String getDumpableTag() {
        return "QSPanel";
    }

    public boolean mediaNeedsTopMargin() {
        return false;
    }

    public final boolean needsDynamicRowsAndColumns() {
        return true;
    }

    public QSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUsingMediaPlayer = Utils.useQsMediaPlayer(context);
        this.mMediaTotalBottomMargin = getResources().getDimensionPixelSize(R$dimen.quick_settings_bottom_margin_media);
        this.mMediaTopMargin = getResources().getDimensionPixelSize(R$dimen.qs_tile_margin_vertical);
        this.mContext = context;
        setOrientation(1);
        this.mMovableContentStartIndex = getChildCount();
    }

    public void initialize() {
        this.mTileLayout = getOrCreateTileLayout();
        if (this.mUsingMediaPlayer) {
            RemeasuringLinearLayout remeasuringLinearLayout = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalLinearLayout = remeasuringLinearLayout;
            remeasuringLinearLayout.setOrientation(0);
            this.mHorizontalLinearLayout.setClipChildren(false);
            this.mHorizontalLinearLayout.setClipToPadding(false);
            RemeasuringLinearLayout remeasuringLinearLayout2 = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalContentContainer = remeasuringLinearLayout2;
            remeasuringLinearLayout2.setOrientation(1);
            setHorizontalContentContainerClipping();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            layoutParams.setMarginStart(0);
            layoutParams.setMarginEnd((int) this.mContext.getResources().getDimension(R$dimen.qs_media_padding));
            layoutParams.gravity = 16;
            this.mHorizontalLinearLayout.addView(this.mHorizontalContentContainer, layoutParams);
            addView(this.mHorizontalLinearLayout, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        }
    }

    public void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipChildren(true);
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.addOnLayoutChangeListener(new QSPanel$$ExternalSyntheticLambda1(this));
        Rect rect = this.mClippingRect;
        rect.left = 0;
        rect.top = -1000;
        this.mHorizontalContentContainer.setClipBounds(rect);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setHorizontalContentContainerClipping$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i3 - i;
        if (i9 != i7 - i5 || i4 - i2 != i8 - i6) {
            Rect rect = this.mClippingRect;
            rect.right = i9;
            rect.bottom = i4 - i2;
            this.mHorizontalContentContainer.setClipBounds(rect);
        }
    }

    public void setBrightnessView(View view) {
        View view2 = this.mBrightnessView;
        if (view2 != null) {
            removeView(view2);
            this.mMovableContentStartIndex--;
        }
        addView(view, 0);
        this.mBrightnessView = view;
        setBrightnessViewMargin();
        this.mMovableContentStartIndex++;
    }

    public final void setBrightnessViewMargin() {
        View view = this.mBrightnessView;
        if (view != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_brightness_margin_top);
            marginLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_brightness_margin_bottom);
            this.mBrightnessView.setLayoutParams(marginLayoutParams);
        }
    }

    public QSTileLayout getOrCreateTileLayout() {
        if (this.mTileLayout == null) {
            QSTileLayout qSTileLayout = (QSTileLayout) LayoutInflater.from(this.mContext).inflate(R$layout.qs_paged_tile_layout, this, false);
            this.mTileLayout = qSTileLayout;
            qSTileLayout.setSquishinessFraction(this.mSquishinessFraction);
        }
        return this.mTileLayout;
    }

    public void setSquishinessFraction(float f) {
        if (Float.compare(f, this.mSquishinessFraction) != 0) {
            this.mSquishinessFraction = f;
            QSTileLayout qSTileLayout = this.mTileLayout;
            if (qSTileLayout != null) {
                qSTileLayout.setSquishinessFraction(f);
                if (getMeasuredWidth() != 0) {
                    updateViewPositions();
                }
            }
        }
    }

    public void onMeasure(int i, int i2) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            PageIndicator pageIndicator = this.mFooterPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(((PagedTileLayout) qSTileLayout).getNumPages());
            }
            if (((View) this.mTileLayout).getParent() == this) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(10000, 1073741824);
                ((PagedTileLayout) this.mTileLayout).setExcessHeight(10000 - View.MeasureSpec.getSize(i2));
                i2 = makeMeasureSpec;
            }
        }
        super.onMeasure(i, i2);
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                paddingBottom = paddingBottom + childAt.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), paddingBottom);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            this.mChildrenLayoutTop.put(childAt, Integer.valueOf(childAt.getTop()));
        }
        updateViewPositions();
    }

    public final void updateViewPositions() {
        int tilesHeight = this.mTileLayout.getTilesHeight() - this.mTileLayout.getHeight();
        boolean z = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (z) {
                int i2 = (childAt != this.mMediaHostView || this.mShouldMoveMediaOnExpansion) ? tilesHeight : 0;
                Integer num = this.mChildrenLayoutTop.get(childAt);
                Objects.requireNonNull(num);
                int intValue = num.intValue() + i2;
                childAt.setLeftTopRightBottom(childAt.getLeft(), intValue, childAt.getRight(), childAt.getHeight() + intValue);
            }
            if (childAt == this.mTileLayout) {
                z = true;
            }
        }
    }

    public void onTuningChanged(String str, String str2) {
        View view;
        if ("qs_show_brightness".equals(str) && (view = this.mBrightnessView) != null) {
            updateViewVisibilityForTuningValue(view, str2);
        }
    }

    public final void updateViewVisibilityForTuningValue(View view, String str) {
        view.setVisibility(TunerService.parseIntegerSwitch(str, true) ? 0 : 8);
    }

    public View getBrightnessView() {
        return this.mBrightnessView;
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        if (this.mTileLayout instanceof PagedTileLayout) {
            this.mFooterPageIndicator = pageIndicator;
            updatePageIndicator();
        }
    }

    public final void updatePageIndicator() {
        PageIndicator pageIndicator;
        if ((this.mTileLayout instanceof PagedTileLayout) && (pageIndicator = this.mFooterPageIndicator) != null) {
            pageIndicator.setVisibility(8);
            ((PagedTileLayout) this.mTileLayout).setPageIndicator(this.mFooterPageIndicator);
        }
    }

    public void updateResources() {
        updatePadding();
        updatePageIndicator();
        setBrightnessViewMargin();
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout != null) {
            qSTileLayout.updateResources();
        }
    }

    public void updatePadding() {
        Resources resources = this.mContext.getResources();
        setPaddingRelative(getPaddingStart(), resources.getDimensionPixelSize(R$dimen.qs_panel_padding_top), getPaddingEnd(), resources.getDimensionPixelSize(R$dimen.qs_panel_padding_bottom));
    }

    public void addOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.add(onConfigurationChangedListener);
    }

    public void removeOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.remove(onConfigurationChangedListener);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mOnConfigurationChangedListeners.forEach(new QSPanel$$ExternalSyntheticLambda0(configuration));
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFooter = findViewById(R$id.qs_footer);
    }

    public final void updateHorizontalLinearLayoutMargins() {
        if (this.mHorizontalLinearLayout != null && !displayMediaMarginsOnMedia()) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mHorizontalLinearLayout.getLayoutParams();
            layoutParams.bottomMargin = Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0);
            this.mHorizontalLinearLayout.setLayoutParams(layoutParams);
        }
    }

    public final void switchAllContentToParent(ViewGroup viewGroup, QSTileLayout qSTileLayout) {
        int i = viewGroup == this ? this.mMovableContentStartIndex : 0;
        switchToParent((View) qSTileLayout, viewGroup, i);
        int i2 = i + 1;
        View view = this.mFooter;
        if (view != null) {
            switchToParent(view, viewGroup, i2);
        }
    }

    public final void switchToParent(View view, ViewGroup viewGroup, int i) {
        switchToParent(view, viewGroup, i, getDumpableTag());
    }

    public final void reAttachMediaHost(ViewGroup viewGroup, boolean z) {
        int i;
        if (this.mUsingMediaPlayer) {
            this.mMediaHostView = viewGroup;
            LinearLayout linearLayout = z ? this.mHorizontalLinearLayout : this;
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
            if (viewGroup2 != linearLayout) {
                if (viewGroup2 != null) {
                    viewGroup2.removeView(viewGroup);
                }
                linearLayout.addView(viewGroup);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
                layoutParams.height = -2;
                int i2 = 0;
                layoutParams.width = z ? 0 : -1;
                layoutParams.weight = z ? 1.0f : 0.0f;
                if (!z || displayMediaMarginsOnMedia()) {
                    i = Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0);
                } else {
                    i = 0;
                }
                layoutParams.bottomMargin = i;
                if (mediaNeedsTopMargin() && !z) {
                    i2 = this.mMediaTopMargin;
                }
                layoutParams.topMargin = i2;
            }
        }
    }

    public void setExpanded(boolean z) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            if (!z) {
                QSTileLayout qSTileLayout = this.mTileLayout;
                if (qSTileLayout instanceof PagedTileLayout) {
                    ((PagedTileLayout) qSTileLayout).setCurrentItem(0, false);
                }
            }
        }
    }

    public void setPageListener(PagedTileLayout.PageListener pageListener) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            ((PagedTileLayout) qSTileLayout).setPageListener(pageListener);
        }
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public void setListening(boolean z) {
        this.mListening = z;
    }

    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        tileRecord.tileView.onStateChanged(state);
    }

    public QSEvent openPanelEvent() {
        return QSEvent.QS_PANEL_EXPANDED;
    }

    public QSEvent closePanelEvent() {
        return QSEvent.QS_PANEL_COLLAPSED;
    }

    public void addTile(final QSPanelControllerBase.TileRecord tileRecord) {
        AnonymousClass1 r0 = new QSTile.Callback() {
            public void onStateChanged(QSTile.State state) {
                QSPanel.this.drawTile(tileRecord, state);
            }
        };
        tileRecord.tile.addCallback(r0);
        tileRecord.callback = r0;
        tileRecord.tileView.init(tileRecord.tile);
        tileRecord.tile.refreshState();
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout != null) {
            qSTileLayout.addTile(tileRecord);
        }
    }

    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mTileLayout.removeTile(tileRecord);
    }

    public QSTileLayout getTileLayout() {
        return this.mTileLayout;
    }

    public void setContentMargins(int i, int i2, ViewGroup viewGroup) {
        this.mContentMarginStart = i;
        this.mContentMarginEnd = i2;
        updateMediaHostContentMargins(viewGroup);
    }

    public void updateMediaHostContentMargins(ViewGroup viewGroup) {
        if (this.mUsingMediaPlayer) {
            updateMargins(viewGroup, 0, this.mUsingHorizontalLayout ? this.mContentMarginEnd : 0);
        }
    }

    public void updateMargins(View view, int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMarginStart(i);
            layoutParams.setMarginEnd(i2);
            view.setLayoutParams(layoutParams);
        }
    }

    public boolean isListening() {
        return this.mListening;
    }

    public void setPageMargin(int i) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            ((PagedTileLayout) qSTileLayout).setPageMargin(i);
        }
    }

    public void setUsingHorizontalLayout(boolean z, ViewGroup viewGroup, boolean z2) {
        if (z != this.mUsingHorizontalLayout || z2) {
            this.mUsingHorizontalLayout = z;
            switchAllContentToParent(z ? this.mHorizontalContentContainer : this, this.mTileLayout);
            reAttachMediaHost(viewGroup, z);
            if (needsDynamicRowsAndColumns()) {
                int i = 2;
                this.mTileLayout.setMinRows(z ? 2 : 1);
                QSTileLayout qSTileLayout = this.mTileLayout;
                if (!z) {
                    i = 5;
                }
                qSTileLayout.setMaxColumns(i);
            }
            updateMargins(viewGroup);
            this.mHorizontalLinearLayout.setVisibility(z ? 0 : 8);
        }
    }

    public final void updateMargins(ViewGroup viewGroup) {
        updateMediaHostContentMargins(viewGroup);
        updateHorizontalLinearLayoutMargins();
        updatePadding();
    }

    public void setShouldMoveMediaOnExpansion(boolean z) {
        this.mShouldMoveMediaOnExpansion = z;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
    }

    public boolean performAccessibilityAction(int i, Bundle bundle) {
        Runnable runnable;
        if ((i != 262144 && i != 524288) || (runnable = this.mCollapseExpandAction) == null) {
            return super.performAccessibilityAction(i, bundle);
        }
        runnable.run();
        return true;
    }

    public void setCollapseExpandAction(Runnable runnable) {
        this.mCollapseExpandAction = runnable;
    }

    public static void switchToParent(View view, ViewGroup viewGroup, int i, String str) {
        if (viewGroup == null) {
            Log.w(str, "Trying to move view to null parent", new IllegalStateException());
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) view.getParent();
        if (viewGroup2 != viewGroup) {
            if (viewGroup2 != null) {
                viewGroup2.removeView(view);
            }
            viewGroup.addView(view, i);
        } else if (viewGroup.indexOfChild(view) != i) {
            viewGroup.removeView(view);
            viewGroup.addView(view, i);
        }
    }
}
