package com.android.systemui.qs;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.tuner.TunerService;

public class QSPanelController extends QSPanelControllerBase<QSPanel> {
    public final BrightnessController mBrightnessController;
    public final BrightnessMirrorHandler mBrightnessMirrorHandler;
    public final BrightnessSliderController mBrightnessSliderController;
    public final FalsingManager mFalsingManager;
    public boolean mGridContentVisible = true;
    public final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() {
        public void onConfigurationChange(Configuration configuration) {
            ((QSPanel) QSPanelController.this.mView).updateResources();
            if (((QSPanel) QSPanelController.this.mView).isListening()) {
                QSPanelController.this.refreshAllTiles();
            }
        }
    };
    public final QSCustomizerController mQsCustomizerController;
    public final QSTileRevealController.Factory mQsTileRevealControllerFactory;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public View.OnTouchListener mTileLayoutTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 1) {
                return false;
            }
            QSPanelController.this.mFalsingManager.isFalseTouch(15);
            return false;
        }
    };
    public final TunerService mTunerService;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public QSPanelController(QSPanel qSPanel, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, QSTileRevealController.Factory factory, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory2, BrightnessSliderController.Factory factory3, FalsingManager falsingManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        super(qSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mTunerService = tunerService;
        this.mQsCustomizerController = qSCustomizerController;
        this.mQsTileRevealControllerFactory = factory;
        this.mFalsingManager = falsingManager;
        BrightnessSliderController create = factory3.create(getContext(), (ViewGroup) this.mView);
        this.mBrightnessSliderController = create;
        ((QSPanel) this.mView).setBrightnessView(create.getRootView());
        BrightnessController create2 = factory2.create(create);
        this.mBrightnessController = create2;
        this.mBrightnessMirrorHandler = new BrightnessMirrorHandler(create2);
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void onInit() {
        super.onInit();
        this.mMediaHost.setExpansion(1.0f);
        this.mMediaHost.setShowsOnlyActiveMedia(false);
        this.mMediaHost.init(0);
        this.mQsCustomizerController.init();
        this.mBrightnessSliderController.init();
    }

    public void onViewAttached() {
        super.onViewAttached();
        updateMediaDisappearParameters();
        this.mTunerService.addTunable((TunerService.Tunable) this.mView, "qs_show_brightness");
        ((QSPanel) this.mView).updateResources();
        if (((QSPanel) this.mView).isListening()) {
            refreshAllTiles();
        }
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        switchTileLayout(true);
        this.mBrightnessMirrorHandler.onQsPanelAttached();
        ((PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout()).setOnTouchListener(this.mTileLayoutTouchListener);
    }

    public QSTileRevealController createTileRevealController() {
        return this.mQsTileRevealControllerFactory.create(this, (PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout());
    }

    public void onViewDetached() {
        this.mTunerService.removeTunable((TunerService.Tunable) this.mView);
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mBrightnessMirrorHandler.onQsPanelDettached();
        super.onViewDetached();
    }

    public void setVisibility(int i) {
        ((QSPanel) this.mView).setVisibility(i);
    }

    public void setListening(boolean z, boolean z2) {
        setListening(z && z2);
        if (z) {
            this.mBrightnessController.registerCallbacks();
        } else {
            this.mBrightnessController.unregisterCallbacks();
        }
    }

    public void setBrightnessMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mBrightnessMirrorHandler.setController(brightnessMirrorController);
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public void updateResources() {
        ((QSPanel) this.mView).updateResources();
    }

    public void refreshAllTiles() {
        this.mBrightnessController.checkRestrictionAndSetEnabled();
        super.refreshAllTiles();
    }

    public void showEdit(View view) {
        view.post(new QSPanelController$$ExternalSyntheticLambda0(this, view));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showEdit$0(View view) {
        if (!this.mQsCustomizerController.isCustomizing()) {
            int[] locationOnScreen = view.getLocationOnScreen();
            this.mQsCustomizerController.show(locationOnScreen[0] + (view.getWidth() / 2), locationOnScreen[1] + (view.getHeight() / 2), false);
        }
    }

    public void setPageListener(PagedTileLayout.PageListener pageListener) {
        ((QSPanel) this.mView).setPageListener(pageListener);
    }

    public void setContentMargins(int i, int i2) {
        ((QSPanel) this.mView).setContentMargins(i, i2, this.mMediaHost.getHostView());
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        ((QSPanel) this.mView).setFooterPageIndicator(pageIndicator);
    }

    public boolean isExpanded() {
        return ((QSPanel) this.mView).isExpanded();
    }

    public void setPageMargin(int i) {
        ((QSPanel) this.mView).setPageMargin(i);
    }

    public boolean isBouncerInTransit() {
        return this.mStatusBarKeyguardViewManager.isBouncerInTransit();
    }
}
