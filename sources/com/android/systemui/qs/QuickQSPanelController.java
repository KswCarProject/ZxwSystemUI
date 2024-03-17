package com.android.systemui.qs;

import android.content.res.Configuration;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$integer;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.util.leak.RotationUtils;
import java.util.ArrayList;

public class QuickQSPanelController extends QSPanelControllerBase<QuickQSPanel> {
    public final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QuickQSPanelController$$ExternalSyntheticLambda0(this);
    public final boolean mUsingCollapsedLandscapeMedia;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Configuration configuration) {
        int integer = getResources().getInteger(R$integer.quick_qs_panel_max_tiles);
        if (integer != ((QuickQSPanel) this.mView).getNumQuickTiles()) {
            setMaxTiles(integer);
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public QuickQSPanelController(QuickQSPanel quickQSPanel, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, boolean z2, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(quickQSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mUsingCollapsedLandscapeMedia = z2;
    }

    public void onInit() {
        super.onInit();
        updateMediaExpansion();
        this.mMediaHost.setShowsOnlyActiveMedia(true);
        this.mMediaHost.init(1);
    }

    public final void updateMediaExpansion() {
        int rotation = getRotation();
        boolean z = true;
        if (!(rotation == 1 || rotation == 3)) {
            z = false;
        }
        if (!this.mUsingCollapsedLandscapeMedia || !z) {
            this.mMediaHost.setExpansion(1.0f);
        } else {
            this.mMediaHost.setExpansion(0.0f);
        }
    }

    public int getRotation() {
        return RotationUtils.getRotation(getContext());
    }

    public void onViewAttached() {
        super.onViewAttached();
        ((QuickQSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
    }

    public void onViewDetached() {
        super.onViewDetached();
        ((QuickQSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
    }

    public final void setMaxTiles(int i) {
        ((QuickQSPanel) this.mView).setMaxTiles(i);
        setTiles();
    }

    public void onConfigurationChanged() {
        updateMediaExpansion();
    }

    public void setTiles() {
        ArrayList arrayList = new ArrayList();
        for (QSTile add : this.mHost.getTiles()) {
            arrayList.add(add);
            if (arrayList.size() == ((QuickQSPanel) this.mView).getNumQuickTiles()) {
                break;
            }
        }
        super.setTiles(arrayList, true);
    }

    public void setContentMargins(int i, int i2) {
        ((QuickQSPanel) this.mView).setContentMargins(i, i2, this.mMediaHost.getHostView());
    }
}
