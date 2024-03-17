package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanelControllerBase;

public class QuickQSPanel extends QSPanel {
    public boolean mDisabledByPolicy;
    public int mMaxTiles = getResources().getInteger(R$integer.quick_qs_panel_max_tiles);

    public boolean displayMediaMarginsOnMedia() {
        return false;
    }

    public String getDumpableTag() {
        return "QuickQSPanel";
    }

    public boolean mediaNeedsTopMargin() {
        return true;
    }

    public QuickQSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.setClipChildren(false);
    }

    public TileLayout getOrCreateTileLayout() {
        return new QQSSideLabelTileLayout(this.mContext);
    }

    public void updatePadding() {
        setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getResources().getDimensionPixelSize(R$dimen.qqs_layout_padding_bottom));
    }

    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        if (state instanceof QSTile.SignalState) {
            QSTile.SignalState signalState = new QSTile.SignalState();
            state.copyTo(signalState);
            signalState.activityIn = false;
            signalState.activityOut = false;
            state = signalState;
        }
        super.drawTile(tileRecord, state);
    }

    public void setMaxTiles(int i) {
        this.mMaxTiles = i;
    }

    public void onTuningChanged(String str, String str2) {
        if ("qs_show_brightness".equals(str)) {
            super.onTuningChanged(str, "0");
        }
    }

    public int getNumQuickTiles() {
        return this.mMaxTiles;
    }

    public void setDisabledByPolicy(boolean z) {
        if (z != this.mDisabledByPolicy) {
            this.mDisabledByPolicy = z;
            setVisibility(z ? 8 : 0);
        }
    }

    public void setVisibility(int i) {
        if (this.mDisabledByPolicy) {
            if (getVisibility() != 8) {
                i = 8;
            } else {
                return;
            }
        }
        super.setVisibility(i);
    }

    public QSEvent openPanelEvent() {
        return QSEvent.QQS_PANEL_EXPANDED;
    }

    public QSEvent closePanelEvent() {
        return QSEvent.QQS_PANEL_COLLAPSED;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
    }

    public static class QQSSideLabelTileLayout extends SideLabelTileLayout {
        public boolean mLastSelected;

        public QQSSideLabelTileLayout(Context context) {
            super(context, (AttributeSet) null);
            setClipChildren(false);
            setClipToPadding(false);
            setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            setMaxColumns(5);
        }

        public boolean updateResources() {
            this.mCellHeightResId = R$dimen.qs_quick_tile_size;
            boolean updateResources = super.updateResources();
            this.mMaxAllowedRows = getResources().getInteger(R$integer.quick_qs_panel_max_rows);
            return updateResources;
        }

        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateResources();
        }

        public void onMeasure(int i, int i2) {
            updateMaxRows(10000, this.mRecords.size());
            super.onMeasure(i, i2);
        }

        public void setListening(boolean z, UiEventLogger uiEventLogger) {
            boolean z2 = !this.mListening && z;
            super.setListening(z, uiEventLogger);
            if (z2) {
                for (int i = 0; i < getNumVisibleTiles(); i++) {
                    QSTile qSTile = this.mRecords.get(i).tile;
                    uiEventLogger.logWithInstanceId(QSEvent.QQS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
                }
            }
        }

        public void setExpansion(float f, float f2) {
            if (f <= 0.0f || f >= 1.0f) {
                boolean z = f == 1.0f || f2 < 0.0f;
                if (this.mLastSelected != z) {
                    setImportantForAccessibility(4);
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).setSelected(z);
                    }
                    setImportantForAccessibility(0);
                    this.mLastSelected = z;
                }
            }
        }
    }
}
