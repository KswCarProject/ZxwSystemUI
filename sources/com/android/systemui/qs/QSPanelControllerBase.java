package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.res.Configuration;
import android.metrics.LogMaker;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.animation.DisappearParameters;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public abstract class QSPanelControllerBase<T extends QSPanel> extends ViewController<T> implements Dumpable {
    public String mCachedSpecs = "";
    public final DumpManager mDumpManager;
    public final QSTileHost mHost;
    public int mLastOrientation;
    public final MediaHost mMediaHost;
    public final Function1<Boolean, Unit> mMediaHostVisibilityListener = new QSPanelControllerBase$$ExternalSyntheticLambda1(this);
    public Consumer<Boolean> mMediaVisibilityChangedListener;
    public final MetricsLogger mMetricsLogger;
    @VisibleForTesting
    public final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() {
        public void onConfigurationChange(Configuration configuration) {
            QSPanelControllerBase qSPanelControllerBase = QSPanelControllerBase.this;
            qSPanelControllerBase.mShouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(qSPanelControllerBase.getResources());
            QSPanelControllerBase.this.onConfigurationChanged();
            if (configuration.orientation != QSPanelControllerBase.this.mLastOrientation) {
                QSPanelControllerBase.this.mLastOrientation = configuration.orientation;
                QSPanelControllerBase.this.switchTileLayout(false);
            }
        }
    };
    public final QSHost.Callback mQSHostCallback = new QSPanelControllerBase$$ExternalSyntheticLambda0(this);
    public final QSLogger mQSLogger;
    public final QSCustomizerController mQsCustomizerController;
    public QSTileRevealController mQsTileRevealController;
    public final ArrayList<TileRecord> mRecords = new ArrayList<>();
    public float mRevealExpansion;
    public boolean mShouldUseSplitNotificationShade;
    public final UiEventLogger mUiEventLogger;
    public boolean mUsingHorizontalLayout;
    public Runnable mUsingHorizontalLayoutChangedListener;
    public final boolean mUsingMediaPlayer;

    public QSTileRevealController createTileRevealController() {
        return null;
    }

    public void onConfigurationChanged() {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0(Boolean bool) {
        Consumer<Boolean> consumer = this.mMediaVisibilityChangedListener;
        if (consumer != null) {
            consumer.accept(bool);
        }
        switchTileLayout(false);
        return null;
    }

    public QSPanelControllerBase(T t, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(t);
        this.mHost = qSTileHost;
        this.mQsCustomizerController = qSCustomizerController;
        this.mUsingMediaPlayer = z;
        this.mMediaHost = mediaHost;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mQSLogger = qSLogger;
        this.mDumpManager = dumpManager;
        this.mShouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
    }

    public void onInit() {
        ((QSPanel) this.mView).initialize();
        this.mQSLogger.logAllTilesChangeListening(((QSPanel) this.mView).isListening(), ((QSPanel) this.mView).getDumpableTag(), "");
    }

    public MediaHost getMediaHost() {
        return this.mMediaHost;
    }

    public void setSquishinessFraction(float f) {
        ((QSPanel) this.mView).setSquishinessFraction(f);
    }

    public void onViewAttached() {
        QSTileRevealController createTileRevealController = createTileRevealController();
        this.mQsTileRevealController = createTileRevealController;
        if (createTileRevealController != null) {
            createTileRevealController.setExpansion(this.mRevealExpansion);
        }
        this.mMediaHost.addVisibilityChangeListener(this.mMediaHostVisibilityListener);
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.addCallback(this.mQSHostCallback);
        setTiles();
        this.mLastOrientation = getResources().getConfiguration().orientation;
        switchTileLayout(true);
        this.mDumpManager.registerDumpable(((QSPanel) this.mView).getDumpableTag(), this);
    }

    public void onViewDetached() {
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.removeCallback(this.mQSHostCallback);
        ((QSPanel) this.mView).getTileLayout().setListening(false, this.mUiEventLogger);
        this.mMediaHost.removeVisibilityChangeListener(this.mMediaHostVisibilityListener);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.removeCallbacks();
        }
        this.mRecords.clear();
        this.mDumpManager.unregisterDumpable(((QSPanel) this.mView).getDumpableTag());
    }

    public void setTiles() {
        setTiles(this.mHost.getTiles(), false);
    }

    public void setTiles(Collection<QSTile> collection, boolean z) {
        QSTileRevealController qSTileRevealController;
        if (!z && (qSTileRevealController = this.mQsTileRevealController) != null) {
            qSTileRevealController.updateRevealedTiles(collection);
        }
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            ((QSPanel) this.mView).removeTile(next);
            next.tile.removeCallback(next.callback);
        }
        this.mRecords.clear();
        this.mCachedSpecs = "";
        for (QSTile addTile : collection) {
            addTile(addTile, z);
        }
    }

    public void refreshAllTiles() {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (!next.tile.isListening()) {
                next.tile.refreshState();
            }
        }
    }

    public final void addTile(QSTile qSTile, boolean z) {
        TileRecord tileRecord = new TileRecord(qSTile, this.mHost.createTileView(getContext(), qSTile, z));
        ((QSPanel) this.mView).addTile(tileRecord);
        this.mRecords.add(tileRecord);
        this.mCachedSpecs = getTilesSpecs();
    }

    public void clickTile(ComponentName componentName) {
        String spec = CustomTile.toSpec(componentName);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile.getTileSpec().equals(spec)) {
                next.tile.click((View) null);
                return;
            }
        }
    }

    public boolean areThereTiles() {
        return !this.mRecords.isEmpty();
    }

    public QSTileView getTileView(QSTile qSTile) {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile == qSTile) {
                return next.tileView;
            }
        }
        return null;
    }

    public QSTileView getTileView(String str) {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (Objects.equals(next.tile.getTileSpec(), str)) {
                return next.tileView;
            }
        }
        return null;
    }

    public final String getTilesSpecs() {
        return (String) this.mRecords.stream().map(new QSPanelControllerBase$$ExternalSyntheticLambda2()).collect(Collectors.joining(","));
    }

    public void setExpanded(boolean z) {
        if (((QSPanel) this.mView).isExpanded() != z) {
            this.mQSLogger.logPanelExpanded(z, ((QSPanel) this.mView).getDumpableTag());
            ((QSPanel) this.mView).setExpanded(z);
            this.mMetricsLogger.visibility(111, z);
            if (!z) {
                this.mUiEventLogger.log(((QSPanel) this.mView).closePanelEvent());
                closeDetail();
                return;
            }
            this.mUiEventLogger.log(((QSPanel) this.mView).openPanelEvent());
            logTiles();
        }
    }

    public void closeDetail() {
        if (this.mQsCustomizerController.isShown()) {
            this.mQsCustomizerController.hide();
        }
    }

    public void setListening(boolean z) {
        if (((QSPanel) this.mView).isListening() != z) {
            ((QSPanel) this.mView).setListening(z);
            if (((QSPanel) this.mView).getTileLayout() != null) {
                this.mQSLogger.logAllTilesChangeListening(z, ((QSPanel) this.mView).getDumpableTag(), this.mCachedSpecs);
                ((QSPanel) this.mView).getTileLayout().setListening(z, this.mUiEventLogger);
            }
            if (((QSPanel) this.mView).isListening()) {
                refreshAllTiles();
            }
        }
    }

    public boolean switchTileLayout(boolean z) {
        boolean shouldUseHorizontalLayout = shouldUseHorizontalLayout();
        if (shouldUseHorizontalLayout == this.mUsingHorizontalLayout && !z) {
            return false;
        }
        this.mUsingHorizontalLayout = shouldUseHorizontalLayout;
        ((QSPanel) this.mView).setUsingHorizontalLayout(shouldUseHorizontalLayout, this.mMediaHost.getHostView(), z);
        updateMediaDisappearParameters();
        Runnable runnable = this.mUsingHorizontalLayoutChangedListener;
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    public void updateMediaDisappearParameters() {
        if (this.mUsingMediaPlayer) {
            DisappearParameters disappearParameters = this.mMediaHost.getDisappearParameters();
            if (this.mUsingHorizontalLayout) {
                disappearParameters.getDisappearSize().set(0.0f, 0.4f);
                disappearParameters.getGonePivot().set(1.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.25f, 1.0f);
                disappearParameters.setDisappearEnd(0.6f);
            } else {
                disappearParameters.getDisappearSize().set(1.0f, 0.0f);
                disappearParameters.getGonePivot().set(0.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.0f, 1.05f);
                disappearParameters.setDisappearEnd(0.95f);
            }
            disappearParameters.setFadeStartPosition(0.95f);
            disappearParameters.setDisappearStart(0.0f);
            this.mMediaHost.setDisappearParameters(disappearParameters);
        }
    }

    public boolean shouldUseHorizontalLayout() {
        if (!this.mShouldUseSplitNotificationShade && this.mUsingMediaPlayer && this.mMediaHost.getVisible() && this.mLastOrientation == 2) {
            return true;
        }
        return false;
    }

    public final void logTiles() {
        for (int i = 0; i < this.mRecords.size(); i++) {
            QSTile qSTile = this.mRecords.get(i).tile;
            this.mMetricsLogger.write(qSTile.populate(new LogMaker(qSTile.getMetricsCategory()).setType(1)));
        }
    }

    public void setRevealExpansion(float f) {
        this.mRevealExpansion = f;
        QSTileRevealController qSTileRevealController = this.mQsTileRevealController;
        if (qSTileRevealController != null) {
            qSTileRevealController.setExpansion(f);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.println("  Tile records:");
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile instanceof Dumpable) {
                printWriter.print("    ");
                ((Dumpable) next.tile).dump(printWriter, strArr);
                printWriter.print("    ");
                printWriter.println(next.tileView.toString());
            }
        }
        if (this.mMediaHost != null) {
            printWriter.println("  media bounds: " + this.mMediaHost.getCurrentBounds());
        }
    }

    public QSPanel.QSTileLayout getTileLayout() {
        return ((QSPanel) this.mView).getTileLayout();
    }

    public void setMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mMediaVisibilityChangedListener = consumer;
    }

    public void setUsingHorizontalLayoutChangeListener(Runnable runnable) {
        this.mUsingHorizontalLayoutChangedListener = runnable;
    }

    public View getBrightnessView() {
        return ((QSPanel) this.mView).getBrightnessView();
    }

    public void setCollapseExpandAction(Runnable runnable) {
        ((QSPanel) this.mView).setCollapseExpandAction(runnable);
    }

    public void setIsOnKeyguard(boolean z) {
        ((QSPanel) this.mView).setShouldMoveMediaOnExpansion(!(this.mShouldUseSplitNotificationShade && z));
    }

    public static final class TileRecord {
        public QSTile.Callback callback;
        public QSTile tile;
        public QSTileView tileView;

        public TileRecord(QSTile qSTile, QSTileView qSTileView) {
            this.tile = qSTile;
            this.tileView = qSTileView;
        }
    }
}
