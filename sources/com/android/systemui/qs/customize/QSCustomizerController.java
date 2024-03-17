package com.android.systemui.qs.customize;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSEditEvent;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;

public class QSCustomizerController extends ViewController<QSCustomizer> {
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            ((QSCustomizer) QSCustomizerController.this.mView).updateNavBackDrop(configuration, QSCustomizerController.this.mLightBarController);
            ((QSCustomizer) QSCustomizerController.this.mView).updateResources();
            if (QSCustomizerController.this.mTileAdapter.updateNumColumns()) {
                RecyclerView.LayoutManager layoutManager = ((QSCustomizer) QSCustomizerController.this.mView).getRecyclerView().getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    ((GridLayoutManager) layoutManager).setSpanCount(QSCustomizerController.this.mTileAdapter.getNumColumns());
                }
            }
        }
    };
    public final KeyguardStateController.Callback mKeyguardCallback = new KeyguardStateController.Callback() {
        public void onKeyguardShowingChanged() {
            if (((QSCustomizer) QSCustomizerController.this.mView).isAttachedToWindow() && QSCustomizerController.this.mKeyguardStateController.isShowing() && !((QSCustomizer) QSCustomizerController.this.mView).isOpening()) {
                QSCustomizerController.this.hide();
            }
        }
    };
    public final KeyguardStateController mKeyguardStateController;
    public final LightBarController mLightBarController;
    public final Toolbar.OnMenuItemClickListener mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() != 1) {
                return false;
            }
            QSCustomizerController.this.mUiEventLogger.log(QSEditEvent.QS_EDIT_RESET);
            QSCustomizerController.this.reset();
            return false;
        }
    };
    public final QSTileHost mQsTileHost;
    public final ScreenLifecycle mScreenLifecycle;
    public final TileAdapter mTileAdapter;
    public final TileQueryHelper mTileQueryHelper;
    public final Toolbar mToolbar;
    public final UiEventLogger mUiEventLogger;

    public QSCustomizerController(QSCustomizer qSCustomizer, TileQueryHelper tileQueryHelper, QSTileHost qSTileHost, TileAdapter tileAdapter, ScreenLifecycle screenLifecycle, KeyguardStateController keyguardStateController, LightBarController lightBarController, ConfigurationController configurationController, UiEventLogger uiEventLogger) {
        super(qSCustomizer);
        this.mTileQueryHelper = tileQueryHelper;
        this.mQsTileHost = qSTileHost;
        this.mTileAdapter = tileAdapter;
        this.mScreenLifecycle = screenLifecycle;
        this.mKeyguardStateController = keyguardStateController;
        this.mLightBarController = lightBarController;
        this.mConfigurationController = configurationController;
        this.mUiEventLogger = uiEventLogger;
        this.mToolbar = (Toolbar) ((QSCustomizer) this.mView).findViewById(16908731);
    }

    public void onViewAttached() {
        ((QSCustomizer) this.mView).updateNavBackDrop(getResources().getConfiguration(), this.mLightBarController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mTileQueryHelper.setListener(this.mTileAdapter);
        this.mTileAdapter.changeHalfMargin(getResources().getDimensionPixelSize(R$dimen.qs_tile_margin_horizontal) / 2);
        final RecyclerView recyclerView = ((QSCustomizer) this.mView).getRecyclerView();
        recyclerView.setAdapter(this.mTileAdapter);
        this.mTileAdapter.getItemTouchHelper().attachToRecyclerView(recyclerView);
        AnonymousClass4 r1 = new GridLayoutManager(getContext(), this.mTileAdapter.getNumColumns()) {
            public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            }

            public void calculateItemDecorationsForChild(View view, Rect rect) {
                if (!(view instanceof TextView)) {
                    rect.setEmpty();
                    QSCustomizerController.this.mTileAdapter.getMarginItemDecoration().getItemOffsets(rect, view, recyclerView, new RecyclerView.State());
                    ((GridLayoutManager.LayoutParams) view.getLayoutParams()).leftMargin = rect.left;
                    ((GridLayoutManager.LayoutParams) view.getLayoutParams()).rightMargin = rect.right;
                }
            }
        };
        r1.setSpanSizeLookup(this.mTileAdapter.getSizeLookup());
        recyclerView.setLayoutManager(r1);
        recyclerView.addItemDecoration(this.mTileAdapter.getItemDecoration());
        recyclerView.addItemDecoration(this.mTileAdapter.getMarginItemDecoration());
        this.mToolbar.setOnMenuItemClickListener(this.mOnMenuItemClickListener);
        this.mToolbar.setNavigationOnClickListener(new QSCustomizerController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$0(View view) {
        hide();
    }

    public void onViewDetached() {
        this.mTileQueryHelper.setListener((TileQueryHelper.TileStateListener) null);
        this.mToolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) null);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public final void reset() {
        this.mTileAdapter.resetTileSpecs(QSTileHost.getDefaultSpecs(getContext()));
    }

    public boolean isCustomizing() {
        return ((QSCustomizer) this.mView).isCustomizing();
    }

    public void show(int i, int i2, boolean z) {
        if (!((QSCustomizer) this.mView).isShown()) {
            setTileSpecs();
            if (z) {
                ((QSCustomizer) this.mView).showImmediately();
            } else {
                ((QSCustomizer) this.mView).show(i, i2, this.mTileAdapter);
                this.mUiEventLogger.log(QSEditEvent.QS_EDIT_OPEN);
            }
            this.mTileQueryHelper.queryTiles(this.mQsTileHost);
            this.mKeyguardStateController.addCallback(this.mKeyguardCallback);
            ((QSCustomizer) this.mView).updateNavColors(this.mLightBarController);
        }
    }

    public void setQs(QSFragment qSFragment) {
        ((QSCustomizer) this.mView).setQs(qSFragment);
    }

    public void restoreInstanceState(Bundle bundle) {
        if (bundle.getBoolean("qs_customizing")) {
            ((QSCustomizer) this.mView).setVisibility(0);
            ((QSCustomizer) this.mView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    ((QSCustomizer) QSCustomizerController.this.mView).removeOnLayoutChangeListener(this);
                    QSCustomizerController.this.show(0, 0, true);
                }
            });
        }
    }

    public void saveInstanceState(Bundle bundle) {
        if (((QSCustomizer) this.mView).isShown()) {
            this.mKeyguardStateController.removeCallback(this.mKeyguardCallback);
        }
        bundle.putBoolean("qs_customizing", ((QSCustomizer) this.mView).isCustomizing());
    }

    public void setEditLocation(int i, int i2) {
        ((QSCustomizer) this.mView).setEditLocation(i, i2);
    }

    public void setContainerController(QSContainerController qSContainerController) {
        ((QSCustomizer) this.mView).setContainerController(qSContainerController);
    }

    public boolean isShown() {
        return ((QSCustomizer) this.mView).isShown();
    }

    public void hide() {
        boolean z = this.mScreenLifecycle.getScreenState() != 0;
        if (((QSCustomizer) this.mView).isShown()) {
            this.mUiEventLogger.log(QSEditEvent.QS_EDIT_CLOSED);
            this.mToolbar.dismissPopupMenus();
            ((QSCustomizer) this.mView).setCustomizing(false);
            save();
            ((QSCustomizer) this.mView).hide(z);
            ((QSCustomizer) this.mView).updateNavColors(this.mLightBarController);
            this.mKeyguardStateController.removeCallback(this.mKeyguardCallback);
        }
    }

    public final void save() {
        if (this.mTileQueryHelper.isFinished()) {
            this.mTileAdapter.saveSpecs(this.mQsTileHost);
        }
    }

    public final void setTileSpecs() {
        ArrayList arrayList = new ArrayList();
        for (QSTile tileSpec : this.mQsTileHost.getTiles()) {
            arrayList.add(tileSpec.getTileSpec());
        }
        this.mTileAdapter.setTileSpecs(arrayList);
    }
}
