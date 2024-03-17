package com.android.keyguard.clock;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import androidx.lifecycle.Observer;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.settings.CurrentUserObservable;
import com.android.systemui.shared.plugins.PluginManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class ClockManager {
    public final List<Supplier<ClockPlugin>> mBuiltinClocks;
    public final ContentObserver mContentObserver;
    public final ContentResolver mContentResolver;
    public final Context mContext;
    public final CurrentUserObservable mCurrentUserObservable;
    public final Observer<Integer> mCurrentUserObserver;
    public final DockManager.DockEventListener mDockEventListener;
    public final DockManager mDockManager;
    public final int mHeight;
    public boolean mIsDocked;
    public final Map<ClockChangedListener, AvailableClocks> mListeners;
    public final Handler mMainHandler;
    public final PluginManager mPluginManager;
    public final AvailableClocks mPreviewClocks;
    public final SettingsWrapper mSettingsWrapper;
    public final int mWidth;

    public interface ClockChangedListener {
        void onClockChanged(ClockPlugin clockPlugin);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Integer num) {
        reload();
    }

    public ClockManager(Context context, LayoutInflater layoutInflater, PluginManager pluginManager, SysuiColorExtractor sysuiColorExtractor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher) {
        this(context, layoutInflater, pluginManager, sysuiColorExtractor, context.getContentResolver(), new CurrentUserObservable(broadcastDispatcher), new SettingsWrapper(context.getContentResolver()), dockManager);
    }

    public ClockManager(Context context, LayoutInflater layoutInflater, PluginManager pluginManager, SysuiColorExtractor sysuiColorExtractor, ContentResolver contentResolver, CurrentUserObservable currentUserObservable, SettingsWrapper settingsWrapper, DockManager dockManager) {
        this.mBuiltinClocks = new ArrayList();
        Handler handler = new Handler(Looper.getMainLooper());
        this.mMainHandler = handler;
        this.mContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (Objects.equals(Integer.valueOf(i2), ClockManager.this.mCurrentUserObservable.getCurrentUser().getValue())) {
                    ClockManager.this.reload();
                }
            }
        };
        this.mCurrentUserObserver = new ClockManager$$ExternalSyntheticLambda0(this);
        this.mDockEventListener = new DockManager.DockEventListener() {
        };
        this.mListeners = new ArrayMap();
        this.mContext = context;
        this.mPluginManager = pluginManager;
        this.mContentResolver = contentResolver;
        this.mSettingsWrapper = settingsWrapper;
        this.mCurrentUserObservable = currentUserObservable;
        this.mDockManager = dockManager;
        this.mPreviewClocks = new AvailableClocks();
        Resources resources = context.getResources();
        addBuiltinClock(new ClockManager$$ExternalSyntheticLambda1(resources, layoutInflater, sysuiColorExtractor));
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mWidth = displayMetrics.widthPixels;
        this.mHeight = displayMetrics.heightPixels;
    }

    public static /* synthetic */ ClockPlugin lambda$new$1(Resources resources, LayoutInflater layoutInflater, SysuiColorExtractor sysuiColorExtractor) {
        return new DefaultClockController(resources, layoutInflater, sysuiColorExtractor);
    }

    public void addOnClockChangedListener(ClockChangedListener clockChangedListener) {
        if (this.mListeners.isEmpty()) {
            register();
        }
        AvailableClocks availableClocks = new AvailableClocks();
        for (int i = 0; i < this.mBuiltinClocks.size(); i++) {
            availableClocks.addClockPlugin((ClockPlugin) this.mBuiltinClocks.get(i).get());
        }
        this.mListeners.put(clockChangedListener, availableClocks);
        this.mPluginManager.addPluginListener(availableClocks, ClockPlugin.class, true);
        reload();
    }

    public void removeOnClockChangedListener(ClockChangedListener clockChangedListener) {
        this.mPluginManager.removePluginListener(this.mListeners.remove(clockChangedListener));
        if (this.mListeners.isEmpty()) {
            unregister();
        }
    }

    public List<ClockInfo> getClockInfos() {
        return this.mPreviewClocks.getInfo();
    }

    public boolean isDocked() {
        return this.mIsDocked;
    }

    public ContentObserver getContentObserver() {
        return this.mContentObserver;
    }

    public void addBuiltinClock(Supplier<ClockPlugin> supplier) {
        this.mPreviewClocks.addClockPlugin(supplier.get());
        this.mBuiltinClocks.add(supplier);
    }

    public final void register() {
        this.mPluginManager.addPluginListener(this.mPreviewClocks, ClockPlugin.class, true);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("lock_screen_custom_clock_face"), false, this.mContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("docked_clock_face"), false, this.mContentObserver, -1);
        this.mCurrentUserObservable.getCurrentUser().observeForever(this.mCurrentUserObserver);
        DockManager dockManager = this.mDockManager;
        if (dockManager != null) {
            dockManager.addListener(this.mDockEventListener);
        }
    }

    public final void unregister() {
        this.mPluginManager.removePluginListener(this.mPreviewClocks);
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        this.mCurrentUserObservable.getCurrentUser().removeObserver(this.mCurrentUserObserver);
        DockManager dockManager = this.mDockManager;
        if (dockManager != null) {
            dockManager.removeListener(this.mDockEventListener);
        }
    }

    public final void reload() {
        this.mPreviewClocks.reloadCurrentClock();
        this.mListeners.forEach(new ClockManager$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$reload$3(ClockChangedListener clockChangedListener, AvailableClocks availableClocks) {
        availableClocks.reloadCurrentClock();
        ClockPlugin currentClock = availableClocks.getCurrentClock();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (currentClock instanceof DefaultClockController) {
                currentClock = null;
            }
            clockChangedListener.onClockChanged(currentClock);
            return;
        }
        this.mMainHandler.post(new ClockManager$$ExternalSyntheticLambda3(clockChangedListener, currentClock));
    }

    public static /* synthetic */ void lambda$reload$2(ClockChangedListener clockChangedListener, ClockPlugin clockPlugin) {
        if (clockPlugin instanceof DefaultClockController) {
            clockPlugin = null;
        }
        clockChangedListener.onClockChanged(clockPlugin);
    }

    public final class AvailableClocks implements PluginListener<ClockPlugin> {
        public final List<ClockInfo> mClockInfo;
        public final Map<String, ClockPlugin> mClocks;
        public ClockPlugin mCurrentClock;

        public AvailableClocks() {
            this.mClocks = new ArrayMap();
            this.mClockInfo = new ArrayList();
        }

        public void onPluginConnected(ClockPlugin clockPlugin, Context context) {
            addClockPlugin(clockPlugin);
            reloadIfNeeded(clockPlugin);
        }

        public void onPluginDisconnected(ClockPlugin clockPlugin) {
            removeClockPlugin(clockPlugin);
            reloadIfNeeded(clockPlugin);
        }

        public ClockPlugin getCurrentClock() {
            return this.mCurrentClock;
        }

        public List<ClockInfo> getInfo() {
            return this.mClockInfo;
        }

        public void addClockPlugin(ClockPlugin clockPlugin) {
            String name = clockPlugin.getClass().getName();
            this.mClocks.put(clockPlugin.getClass().getName(), clockPlugin);
            this.mClockInfo.add(ClockInfo.builder().setName(clockPlugin.getName()).setTitle(new ClockManager$AvailableClocks$$ExternalSyntheticLambda0(clockPlugin)).setId(name).setThumbnail(new ClockManager$AvailableClocks$$ExternalSyntheticLambda1(clockPlugin)).setPreview(new ClockManager$AvailableClocks$$ExternalSyntheticLambda2(this, clockPlugin)).build());
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Bitmap lambda$addClockPlugin$0(ClockPlugin clockPlugin) {
            return clockPlugin.getPreview(ClockManager.this.mWidth, ClockManager.this.mHeight);
        }

        public final void removeClockPlugin(ClockPlugin clockPlugin) {
            String name = clockPlugin.getClass().getName();
            this.mClocks.remove(name);
            for (int i = 0; i < this.mClockInfo.size(); i++) {
                if (name.equals(this.mClockInfo.get(i).getId())) {
                    this.mClockInfo.remove(i);
                    return;
                }
            }
        }

        public final void reloadIfNeeded(ClockPlugin clockPlugin) {
            boolean z = true;
            boolean z2 = clockPlugin == this.mCurrentClock;
            reloadCurrentClock();
            if (clockPlugin != this.mCurrentClock) {
                z = false;
            }
            if (z2 || z) {
                ClockManager.this.reload();
            }
        }

        public void reloadCurrentClock() {
            this.mCurrentClock = getClockPlugin();
        }

        public final ClockPlugin getClockPlugin() {
            ClockPlugin clockPlugin;
            String dockedClockFace;
            if (!ClockManager.this.isDocked() || (dockedClockFace = ClockManager.this.mSettingsWrapper.getDockedClockFace(ClockManager.this.mCurrentUserObservable.getCurrentUser().getValue().intValue())) == null) {
                clockPlugin = null;
            } else {
                clockPlugin = this.mClocks.get(dockedClockFace);
                if (clockPlugin != null) {
                    return clockPlugin;
                }
            }
            String lockScreenCustomClockFace = ClockManager.this.mSettingsWrapper.getLockScreenCustomClockFace(ClockManager.this.mCurrentUserObservable.getCurrentUser().getValue().intValue());
            return lockScreenCustomClockFace != null ? this.mClocks.get(lockScreenCustomClockFace) : clockPlugin;
        }
    }
}
