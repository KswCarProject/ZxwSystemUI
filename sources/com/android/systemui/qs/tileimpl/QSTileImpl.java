package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTile.State;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSEvent;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SideLabelTileLayout;
import com.android.systemui.qs.logging.QSLogger;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class QSTileImpl<TState extends QSTile.State> implements QSTile, LifecycleOwner, Dumpable {
    public static final Object ARG_SHOW_TRANSIENT_ENABLING = new Object();
    public static final boolean DEBUG = Log.isLoggable("Tile", 3);
    public final String TAG = ("Tile." + getClass().getSimpleName());
    public final ActivityStarter mActivityStarter;
    public final ArrayList<QSTile.Callback> mCallbacks = new ArrayList<>();
    public final Context mContext;
    @VisibleForTesting
    public RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    public final FalsingManager mFalsingManager;
    public final QSTileImpl<TState>.H mHandler;
    public final QSHost mHost;
    public final InstanceId mInstanceId;
    public int mIsFullQs;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    public final ArraySet<Object> mListeners = new ArraySet<>();
    public final MetricsLogger mMetricsLogger;
    public final QSLogger mQSLogger;
    public volatile int mReadyState;
    public final Object mStaleListener = new Object();
    public TState mState;
    public final StatusBarStateController mStatusBarStateController;
    public String mTileSpec;
    public TState mTmpState;
    public final UiEventLogger mUiEventLogger;
    public final Handler mUiHandler;

    public abstract Intent getLongClickIntent();

    @Deprecated
    public int getMetricsCategory() {
        return 0;
    }

    public long getStaleTimeout() {
        return 600000;
    }

    public abstract CharSequence getTileLabel();

    public abstract void handleClick(View view);

    public void handleInitialize() {
    }

    public abstract void handleUpdateState(TState tstate, Object obj);

    public boolean isAvailable() {
        return true;
    }

    public abstract TState newTileState();

    public void setDetailListening(boolean z) {
    }

    public QSTileImpl(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        this.mHost = qSHost;
        this.mContext = qSHost.getContext();
        this.mInstanceId = qSHost.getNewInstanceId();
        this.mUiEventLogger = qSHost.getUiEventLogger();
        this.mUiHandler = handler;
        this.mHandler = new H(looper);
        this.mFalsingManager = falsingManager;
        this.mQSLogger = qSLogger;
        this.mMetricsLogger = metricsLogger;
        this.mStatusBarStateController = statusBarStateController;
        this.mActivityStarter = activityStarter;
        resetStates();
        handler.post(new QSTileImpl$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public final void resetStates() {
        this.mState = newTileState();
        TState newTileState = newTileState();
        this.mTmpState = newTileState;
        TState tstate = this.mState;
        String str = this.mTileSpec;
        tstate.spec = str;
        newTileState.spec = str;
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    public InstanceId getInstanceId() {
        return this.mInstanceId;
    }

    public void setListening(Object obj, boolean z) {
        this.mHandler.obtainMessage(10, z ? 1 : 0, 0, obj).sendToTarget();
    }

    @VisibleForTesting
    public void handleStale() {
        if (!this.mListeners.isEmpty()) {
            refreshState();
        } else {
            setListening(this.mStaleListener, true);
        }
    }

    public String getTileSpec() {
        return this.mTileSpec;
    }

    public void setTileSpec(String str) {
        this.mTileSpec = str;
        this.mState.spec = str;
        this.mTmpState.spec = str;
    }

    public QSHost getHost() {
        return this.mHost;
    }

    public QSIconView createTileView(Context context) {
        return new QSIconViewImpl(context);
    }

    public void addCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(1, callback).sendToTarget();
    }

    public void removeCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(9, callback).sendToTarget();
    }

    public void removeCallbacks() {
        this.mHandler.sendEmptyMessage(8);
    }

    public void click(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(925).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mHandler.obtainMessage(2, view).sendToTarget();
        }
    }

    public void secondaryClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(926).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_SECONDARY_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileSecondaryClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(3, view).sendToTarget();
    }

    public void longClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(366).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_LONG_PRESS, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileLongClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(4, view).sendToTarget();
    }

    public LogMaker populate(LogMaker logMaker) {
        TState tstate = this.mState;
        if (tstate instanceof QSTile.BooleanState) {
            logMaker.addTaggedData(928, Integer.valueOf(((QSTile.BooleanState) tstate).value ? 1 : 0));
        }
        return logMaker.setSubtype(getMetricsCategory()).addTaggedData(1593, Integer.valueOf(this.mIsFullQs)).addTaggedData(927, Integer.valueOf(this.mHost.indexOf(this.mTileSpec)));
    }

    public void refreshState() {
        refreshState((Object) null);
    }

    public final boolean isListening() {
        return getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED);
    }

    public final void refreshState(Object obj) {
        this.mHandler.obtainMessage(5, obj).sendToTarget();
    }

    public void userSwitch(int i) {
        this.mHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    public void destroy() {
        this.mHandler.sendEmptyMessage(7);
    }

    public void initialize() {
        this.mHandler.sendEmptyMessage(12);
    }

    public TState getState() {
        return this.mState;
    }

    public final void handleAddCallback(QSTile.Callback callback) {
        this.mCallbacks.add(callback);
        callback.onStateChanged(this.mState);
    }

    public final void handleRemoveCallback(QSTile.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public final void handleRemoveCallbacks() {
        this.mCallbacks.clear();
    }

    public void postStale() {
        this.mHandler.sendEmptyMessage(11);
    }

    public void handleSecondaryClick(View view) {
        handleClick(view);
    }

    public void handleLongClick(View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, view != null ? ActivityLaunchAnimator.Controller.fromView(view, 32) : null);
    }

    public final void handleRefreshState(Object obj) {
        handleUpdateState(this.mTmpState, obj);
        boolean copyTo = this.mTmpState.copyTo(this.mState);
        if (this.mReadyState == 1) {
            this.mReadyState = 2;
            copyTo = true;
        }
        if (copyTo) {
            this.mQSLogger.logTileUpdated(this.mTileSpec, this.mState);
            handleStateChanged();
        }
        this.mHandler.removeMessages(11);
        this.mHandler.sendEmptyMessageDelayed(11, getStaleTimeout());
        setListening(this.mStaleListener, false);
    }

    public final void handleStateChanged() {
        if (this.mCallbacks.size() != 0) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                this.mCallbacks.get(i).onStateChanged(this.mState);
            }
        }
    }

    public void handleUserSwitch(int i) {
        handleRefreshState((Object) null);
    }

    public final void handleSetListeningInternal(Object obj, boolean z) {
        if (z) {
            if (this.mListeners.add(obj) && this.mListeners.size() == 1) {
                if (DEBUG) {
                    Log.d(this.TAG, "handleSetListening true");
                }
                handleSetListening(z);
                this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda1(this));
            }
        } else if (this.mListeners.remove(obj) && this.mListeners.size() == 0) {
            if (DEBUG) {
                Log.d(this.TAG, "handleSetListening false");
            }
            handleSetListening(z);
            this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda2(this));
        }
        updateIsFullQs();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListeningInternal$1() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
            if (this.mReadyState == 0) {
                this.mReadyState = 1;
            }
            refreshState();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListeningInternal$2() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
        }
    }

    public final void updateIsFullQs() {
        Iterator<Object> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (SideLabelTileLayout.class.equals(it.next().getClass())) {
                this.mIsFullQs = 1;
                return;
            }
        }
        this.mIsFullQs = 0;
    }

    public void handleSetListening(boolean z) {
        String str = this.mTileSpec;
        if (str != null) {
            this.mQSLogger.logTileChangeListening(str, z);
        }
    }

    public void handleDestroy() {
        this.mQSLogger.logTileDestroyed(this.mTileSpec, "Handle destroy");
        if (this.mListeners.size() != 0) {
            handleSetListening(false);
            this.mListeners.clear();
        }
        this.mCallbacks.clear();
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDestroy$3() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public void checkIfRestrictionEnforcedByAdminOnly(QSTile.State state, String str) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, this.mHost.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, str, this.mHost.getUserId())) {
            state.disabledByPolicy = false;
            this.mEnforcedAdmin = null;
            return;
        }
        state.disabledByPolicy = true;
        this.mEnforcedAdmin = checkIfRestrictionEnforced;
    }

    public String getMetricsSpec() {
        return this.mTileSpec;
    }

    public boolean isTileReady() {
        return this.mReadyState == 2;
    }

    public final class H extends Handler {
        @VisibleForTesting
        public static final int STALE = 11;

        @VisibleForTesting
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            try {
                int i = message.what;
                boolean z = true;
                if (i == 1) {
                    QSTileImpl.this.handleAddCallback((QSTile.Callback) message.obj);
                } else if (i == 8) {
                    QSTileImpl.this.handleRemoveCallbacks();
                } else if (i == 9) {
                    QSTileImpl.this.handleRemoveCallback((QSTile.Callback) message.obj);
                } else if (i == 2) {
                    QSTileImpl qSTileImpl = QSTileImpl.this;
                    if (qSTileImpl.mState.disabledByPolicy) {
                        QSTileImpl.this.mActivityStarter.postStartActivityDismissingKeyguard(RestrictedLockUtils.getShowAdminSupportDetailsIntent(qSTileImpl.mContext, qSTileImpl.mEnforcedAdmin), 0);
                        return;
                    }
                    qSTileImpl.handleClick((View) message.obj);
                } else if (i == 3) {
                    QSTileImpl.this.handleSecondaryClick((View) message.obj);
                } else if (i == 4) {
                    QSTileImpl.this.handleLongClick((View) message.obj);
                } else if (i == 5) {
                    QSTileImpl.this.handleRefreshState(message.obj);
                } else if (i == 6) {
                    QSTileImpl.this.handleUserSwitch(message.arg1);
                } else if (i == 7) {
                    QSTileImpl.this.handleDestroy();
                } else if (i == 10) {
                    QSTileImpl qSTileImpl2 = QSTileImpl.this;
                    Object obj = message.obj;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl2.handleSetListeningInternal(obj, z);
                } else if (i == 11) {
                    QSTileImpl.this.handleStale();
                } else if (i == 12) {
                    QSTileImpl.this.handleInitialize();
                } else {
                    throw new IllegalArgumentException("Unknown msg: " + message.what);
                }
            } catch (Throwable th) {
                String str = "Error in " + null;
                Log.w(QSTileImpl.this.TAG, str, th);
                QSTileImpl.this.mHost.warn(str, th);
            }
        }
    }

    public static class DrawableIcon extends QSTile.Icon {
        public final Drawable mDrawable;
        public final Drawable mInvisibleDrawable;

        public String toString() {
            return "DrawableIcon";
        }

        public DrawableIcon(Drawable drawable) {
            this.mDrawable = drawable;
            this.mInvisibleDrawable = drawable.getConstantState().newDrawable();
        }

        public Drawable getDrawable(Context context) {
            return this.mDrawable;
        }

        public Drawable getInvisibleDrawable(Context context) {
            return this.mInvisibleDrawable;
        }
    }

    public static class ResourceIcon extends QSTile.Icon {
        public static final SparseArray<QSTile.Icon> ICONS = new SparseArray<>();
        public final int mResId;

        public ResourceIcon(int i) {
            this.mResId = i;
        }

        public static synchronized QSTile.Icon get(int i) {
            QSTile.Icon icon;
            synchronized (ResourceIcon.class) {
                SparseArray<QSTile.Icon> sparseArray = ICONS;
                icon = sparseArray.get(i);
                if (icon == null) {
                    icon = new ResourceIcon(i);
                    sparseArray.put(i, icon);
                }
            }
            return icon;
        }

        public Drawable getDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public Drawable getInvisibleDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public boolean equals(Object obj) {
            return (obj instanceof ResourceIcon) && ((ResourceIcon) obj).mResId == this.mResId;
        }

        public String toString() {
            return String.format("ResourceIcon[resId=0x%08x]", new Object[]{Integer.valueOf(this.mResId)});
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(getState().toString());
    }
}
