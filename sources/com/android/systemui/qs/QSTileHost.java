package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileServiceKey;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import javax.inject.Provider;

public class QSTileHost implements QSHost, TunerService.Tunable, PluginListener<QSFactory>, Dumpable {
    public static final boolean DEBUG = Log.isLoggable("QSTileHost", 3);
    public AutoTileManager mAutoTiles;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final List<QSHost.Callback> mCallbacks = new ArrayList();
    public final Optional<CentralSurfaces> mCentralSurfacesOptional;
    public final Context mContext;
    public int mCurrentUser;
    public final CustomTileStatePersister mCustomTileStatePersister;
    public final DumpManager mDumpManager;
    public final StatusBarIconController mIconController;
    public final InstanceIdSequence mInstanceIdSequence;
    public final PluginManager mPluginManager;
    public final QSLogger mQSLogger;
    public final ArrayList<QSFactory> mQsFactories;
    public SecureSettings mSecureSettings;
    public TileLifecycleManager.Factory mTileLifeCycleManagerFactory;
    public final TileServiceRequestController mTileServiceRequestController;
    public final ArrayList<String> mTileSpecs = new ArrayList<>();
    public final LinkedHashMap<String, QSTile> mTiles = new LinkedHashMap<>();
    public final TunerService mTunerService;
    public final UiEventLogger mUiEventLogger;
    public Context mUserContext;
    public UserTracker mUserTracker;

    public void warn(String str, Throwable th) {
    }

    public QSTileHost(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, Handler handler, Looper looper, PluginManager pluginManager, TunerService tunerService, Provider<AutoTileManager> provider, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, Optional<CentralSurfaces> optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister, TileServiceRequestController.Builder builder, TileLifecycleManager.Factory factory) {
        Context context2 = context;
        DumpManager dumpManager2 = dumpManager;
        ArrayList<QSFactory> arrayList = new ArrayList<>();
        this.mQsFactories = arrayList;
        this.mIconController = statusBarIconController;
        this.mContext = context2;
        this.mUserContext = context2;
        this.mTunerService = tunerService;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager2;
        this.mQSLogger = qSLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTileServiceRequestController = builder.create(this);
        this.mTileLifeCycleManagerFactory = factory;
        this.mInstanceIdSequence = new InstanceIdSequence(1048576);
        this.mCentralSurfacesOptional = optional;
        QSFactory qSFactory2 = qSFactory;
        arrayList.add(qSFactory);
        pluginManager.addPluginListener(this, QSFactory.class, true);
        dumpManager2.registerDumpable("QSTileHost", this);
        this.mUserTracker = userTracker;
        this.mSecureSettings = secureSettings;
        this.mCustomTileStatePersister = customTileStatePersister;
        Provider<AutoTileManager> provider2 = provider;
        Handler handler2 = handler;
        handler.post(new QSTileHost$$ExternalSyntheticLambda2(this, tunerService, provider));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TunerService tunerService, Provider provider) {
        tunerService.addTunable(this, "sysui_qs_tiles");
        this.mAutoTiles = (AutoTileManager) provider.get();
        this.mTileServiceRequestController.init();
    }

    public StatusBarIconController getIconController() {
        return this.mIconController;
    }

    public InstanceId getNewInstanceId() {
        return this.mInstanceIdSequence.newInstanceId();
    }

    public void onPluginConnected(QSFactory qSFactory, Context context) {
        this.mQsFactories.add(0, qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    public void onPluginDisconnected(QSFactory qSFactory) {
        this.mQsFactories.remove(qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    public UiEventLogger getUiEventLogger() {
        return this.mUiEventLogger;
    }

    public void addCallback(QSHost.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(QSHost.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public Collection<QSTile> getTiles() {
        return this.mTiles.values();
    }

    public void collapsePanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda10());
    }

    public void forceCollapsePanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda8());
    }

    public void openPanels() {
        this.mCentralSurfacesOptional.ifPresent(new QSTileHost$$ExternalSyntheticLambda9());
    }

    public Context getContext() {
        return this.mContext;
    }

    public Context getUserContext() {
        return this.mUserContext;
    }

    public int getUserId() {
        return this.mCurrentUser;
    }

    public int indexOf(String str) {
        return this.mTileSpecs.indexOf(str);
    }

    public void onTuningChanged(String str, String str2) {
        boolean z;
        if ("sysui_qs_tiles".equals(str)) {
            Log.d("QSTileHost", "Recreating tiles");
            if (str2 == null && UserManager.isDeviceInDemoMode(this.mContext)) {
                str2 = this.mContext.getResources().getString(R$string.quick_settings_tiles_retail_mode);
            }
            List<String> loadTileSpecs = loadTileSpecs(this.mContext, str2);
            int userId = this.mUserTracker.getUserId();
            if (userId != this.mCurrentUser) {
                this.mUserContext = this.mUserTracker.getUserContext();
                AutoTileManager autoTileManager = this.mAutoTiles;
                if (autoTileManager != null) {
                    autoTileManager.lambda$changeUser$0(UserHandle.of(userId));
                }
            }
            if (!loadTileSpecs.equals(this.mTileSpecs) || userId != this.mCurrentUser) {
                this.mTiles.entrySet().stream().filter(new QSTileHost$$ExternalSyntheticLambda4(loadTileSpecs)).forEach(new QSTileHost$$ExternalSyntheticLambda5(this));
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (String next : loadTileSpecs) {
                    QSTile qSTile = this.mTiles.get(next);
                    if (qSTile == null || (z && ((CustomTile) qSTile).getUser() != userId)) {
                        if (qSTile != null) {
                            qSTile.destroy();
                            Log.d("QSTileHost", "Destroying tile for wrong user: " + next);
                            this.mQSLogger.logTileDestroyed(next, "Tile for wrong user");
                        }
                        Log.d("QSTileHost", "Creating tile: " + next);
                        try {
                            QSTile createTile = createTile(next);
                            if (createTile != null) {
                                createTile.setTileSpec(next);
                                if (createTile.isAvailable()) {
                                    linkedHashMap.put(next, createTile);
                                    this.mQSLogger.logTileAdded(next);
                                } else {
                                    createTile.destroy();
                                    Log.d("QSTileHost", "Destroying not available tile: " + next);
                                    this.mQSLogger.logTileDestroyed(next, "Tile not available");
                                }
                            }
                        } catch (Throwable th) {
                            Log.w("QSTileHost", "Error creating tile for spec: " + next, th);
                        }
                    } else if (qSTile.isAvailable()) {
                        if (DEBUG) {
                            Log.d("QSTileHost", "Adding " + qSTile);
                        }
                        qSTile.removeCallbacks();
                        if (!((z = qSTile instanceof CustomTile)) && this.mCurrentUser != userId) {
                            qSTile.userSwitch(userId);
                        }
                        linkedHashMap.put(next, qSTile);
                        this.mQSLogger.logTileAdded(next);
                    } else {
                        qSTile.destroy();
                        Log.d("QSTileHost", "Destroying not available tile: " + next);
                        this.mQSLogger.logTileDestroyed(next, "Tile not available");
                    }
                }
                this.mCurrentUser = userId;
                ArrayList arrayList = new ArrayList(this.mTileSpecs);
                this.mTileSpecs.clear();
                this.mTileSpecs.addAll(loadTileSpecs);
                this.mTiles.clear();
                this.mTiles.putAll(linkedHashMap);
                if (!linkedHashMap.isEmpty() || loadTileSpecs.isEmpty()) {
                    for (int i = 0; i < this.mCallbacks.size(); i++) {
                        this.mCallbacks.get(i).onTilesChanged();
                    }
                    return;
                }
                Log.d("QSTileHost", "No valid tiles on tuning changed. Setting to default.");
                changeTiles(arrayList, loadTileSpecs(this.mContext, ""));
            }
        }
    }

    public static /* synthetic */ boolean lambda$onTuningChanged$2(List list, Map.Entry entry) {
        return !list.contains(entry.getKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTuningChanged$3(Map.Entry entry) {
        Log.d("QSTileHost", "Destroying tile: " + ((String) entry.getKey()));
        this.mQSLogger.logTileDestroyed((String) entry.getKey(), "Tile removed");
        ((QSTile) entry.getValue()).destroy();
    }

    public void removeTile(String str) {
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda7(str));
    }

    public void removeTiles(Collection<String> collection) {
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda3(collection));
    }

    public void unmarkTileAsAutoAdded(String str) {
        AutoTileManager autoTileManager = this.mAutoTiles;
        if (autoTileManager != null) {
            autoTileManager.unmarkTileAsAutoAdded(str);
        }
    }

    public void addTile(String str) {
        addTile(str, -1);
    }

    public void addTile(String str, int i) {
        if (str.equals("work")) {
            Log.wtfStack("QSTileHost", "Adding work tile");
        }
        changeTileSpecs(new QSTileHost$$ExternalSyntheticLambda6(str, i));
    }

    public static /* synthetic */ boolean lambda$addTile$6(String str, int i, List list) {
        if (list.contains(str)) {
            return false;
        }
        int size = list.size();
        if (i == -1 || i >= size) {
            list.add(str);
            return true;
        }
        list.add(i, str);
        return true;
    }

    public void saveTilesToSettings(List<String> list) {
        if (list.contains("work")) {
            Log.wtfStack("QSTileHost", "Saving work tile");
        }
        this.mSecureSettings.putStringForUser("sysui_qs_tiles", TextUtils.join(",", list), (String) null, false, this.mCurrentUser, true);
    }

    public final void changeTileSpecs(Predicate<List<String>> predicate) {
        List<String> loadTileSpecs = loadTileSpecs(this.mContext, this.mSecureSettings.getStringForUser("sysui_qs_tiles", this.mCurrentUser));
        if (predicate.test(loadTileSpecs)) {
            saveTilesToSettings(loadTileSpecs);
        }
    }

    public void addTile(ComponentName componentName) {
        addTile(componentName, false);
    }

    public void addTile(ComponentName componentName, boolean z) {
        String spec = CustomTile.toSpec(componentName);
        if (!this.mTileSpecs.contains(spec)) {
            ArrayList arrayList = new ArrayList(this.mTileSpecs);
            if (z) {
                arrayList.add(spec);
            } else {
                arrayList.add(0, spec);
            }
            changeTiles(this.mTileSpecs, arrayList);
        }
    }

    public void removeTile(ComponentName componentName) {
        ArrayList arrayList = new ArrayList(this.mTileSpecs);
        arrayList.remove(CustomTile.toSpec(componentName));
        changeTiles(this.mTileSpecs, arrayList);
    }

    public void changeTiles(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList.get(i);
            if (str.startsWith("custom(") && !list2.contains(str)) {
                ComponentName componentFromSpec = CustomTile.getComponentFromSpec(str);
                TileLifecycleManager create = this.mTileLifeCycleManagerFactory.create(new Intent().setComponent(componentFromSpec), new UserHandle(this.mCurrentUser));
                create.onStopListening();
                create.onTileRemoved();
                this.mCustomTileStatePersister.removeState(new TileServiceKey(componentFromSpec, this.mCurrentUser));
                TileLifecycleManager.setTileAdded(this.mContext, componentFromSpec, false);
                create.flushMessagesAndUnbind();
            }
        }
        if (DEBUG) {
            Log.d("QSTileHost", "saveCurrentTiles " + list2);
        }
        saveTilesToSettings(list2);
    }

    public QSTile createTile(String str) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTile createTile = this.mQsFactories.get(i).createTile(str);
            if (createTile != null) {
                return createTile;
            }
        }
        return null;
    }

    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTileView createTileView = this.mQsFactories.get(i).createTileView(context, qSTile, z);
            if (createTileView != null) {
                return createTileView;
            }
        }
        throw new RuntimeException("Default factory didn't create view for " + qSTile.getTileSpec());
    }

    public static List<String> loadTileSpecs(Context context, String str) {
        Resources resources = context.getResources();
        String str2 = "";
        if (TextUtils.isEmpty(str2)) {
            str2 = resources.getString(R$string.quick_settings_tiles);
            if (DEBUG) {
                Log.d("QSTileHost", "Loaded tile specs from config: " + str2);
            }
        } else if (DEBUG) {
            Log.d("QSTileHost", "Loaded tile specs from setting: " + str2);
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (String trim : str2.split(",")) {
            String trim2 = trim.trim();
            if (!trim2.isEmpty()) {
                if (trim2.equals("default")) {
                    if (!z) {
                        for (String next : getDefaultSpecs(context)) {
                            if (!arraySet.contains(next)) {
                                arrayList.add(next);
                                arraySet.add(next);
                            }
                        }
                        z = true;
                    }
                } else if (!arraySet.contains(trim2)) {
                    arrayList.add(trim2);
                    arraySet.add(trim2);
                }
            }
        }
        if (arrayList.contains("internet")) {
            arrayList.remove("wifi");
            arrayList.remove("cell");
        } else if (arrayList.contains("wifi")) {
            arrayList.set(arrayList.indexOf("wifi"), "internet");
            arrayList.remove("cell");
        } else if (arrayList.contains("cell")) {
            arrayList.set(arrayList.indexOf("cell"), "internet");
        }
        return arrayList;
    }

    public static List<String> getDefaultSpecs(Context context) {
        ArrayList arrayList = new ArrayList();
        context.getResources();
        Log.i("QSTileHost", "getDefaultSpecs: internet,bt,sound,night,hotspot,blackscreen,screenshot,setting,reboot");
        arrayList.addAll(Arrays.asList("internet,bt,sound,night,hotspot,blackscreen,screenshot,setting,reboot".split(",")));
        if (Build.IS_DEBUGGABLE) {
            arrayList.add("dbg:mem");
        }
        return arrayList;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("QSTileHost:");
        this.mTiles.values().stream().filter(new QSTileHost$$ExternalSyntheticLambda0()).forEach(new QSTileHost$$ExternalSyntheticLambda1(printWriter, strArr));
    }

    public static /* synthetic */ boolean lambda$dump$7(QSTile qSTile) {
        return qSTile instanceof Dumpable;
    }
}
