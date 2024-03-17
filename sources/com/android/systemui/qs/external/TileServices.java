package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.quicksettings.IQSService;
import android.service.quicksettings.Tile;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import javax.inject.Provider;

public class TileServices extends IQSService.Stub {
    public static final Comparator<TileServiceManager> SERVICE_SORT = new Comparator<TileServiceManager>() {
        public int compare(TileServiceManager tileServiceManager, TileServiceManager tileServiceManager2) {
            return -Integer.compare(tileServiceManager.getBindPriority(), tileServiceManager2.getBindPriority());
        }
    };
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public final Provider<Handler> mHandlerProvider;
    public final QSTileHost mHost;
    public final KeyguardStateController mKeyguardStateController;
    public final Handler mMainHandler;
    public int mMaxBound = 3;
    public final CommandQueue.Callbacks mRequestListeningCallback;
    public final ArrayMap<CustomTile, TileServiceManager> mServices = new ArrayMap<>();
    public final ArrayMap<ComponentName, CustomTile> mTiles = new ArrayMap<>();
    public final ArrayMap<IBinder, CustomTile> mTokenMap = new ArrayMap<>();
    public final UserTracker mUserTracker;

    public TileServices(QSTileHost qSTileHost, Provider<Handler> provider, BroadcastDispatcher broadcastDispatcher, UserTracker userTracker, KeyguardStateController keyguardStateController, CommandQueue commandQueue) {
        AnonymousClass2 r0 = new CommandQueue.Callbacks() {
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$requestTileServiceListeningState$0(ComponentName componentName) {
                TileServices.this.requestListening(componentName);
            }

            public void requestTileServiceListeningState(ComponentName componentName) {
                TileServices.this.mMainHandler.post(new TileServices$2$$ExternalSyntheticLambda0(this, componentName));
            }
        };
        this.mRequestListeningCallback = r0;
        this.mHost = qSTileHost;
        this.mKeyguardStateController = keyguardStateController;
        this.mContext = qSTileHost.getContext();
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mHandlerProvider = provider;
        this.mMainHandler = provider.get();
        this.mUserTracker = userTracker;
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) r0);
    }

    public Context getContext() {
        return this.mContext;
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public TileServiceManager getTileWrapper(CustomTile customTile) {
        ComponentName component = customTile.getComponent();
        TileServiceManager onCreateTileService = onCreateTileService(component, this.mBroadcastDispatcher);
        synchronized (this.mServices) {
            this.mServices.put(customTile, onCreateTileService);
            this.mTiles.put(component, customTile);
            this.mTokenMap.put(onCreateTileService.getToken(), customTile);
        }
        onCreateTileService.startLifecycleManagerAndAddTile();
        return onCreateTileService;
    }

    public TileServiceManager onCreateTileService(ComponentName componentName, BroadcastDispatcher broadcastDispatcher) {
        return new TileServiceManager(this, this.mHandlerProvider.get(), componentName, broadcastDispatcher, this.mUserTracker);
    }

    public void freeService(CustomTile customTile, TileServiceManager tileServiceManager) {
        synchronized (this.mServices) {
            tileServiceManager.setBindAllowed(false);
            tileServiceManager.handleDestroy();
            this.mServices.remove(customTile);
            this.mTokenMap.remove(tileServiceManager.getToken());
            this.mTiles.remove(customTile.getComponent());
            this.mMainHandler.post(new TileServices$$ExternalSyntheticLambda0(this, customTile.getComponent().getClassName()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$freeService$0(String str) {
        this.mHost.getIconController().removeAllIconsForSlot(str);
    }

    public void recalculateBindAllowance() {
        ArrayList arrayList;
        synchronized (this.mServices) {
            arrayList = new ArrayList(this.mServices.values());
        }
        int size = arrayList.size();
        if (size > this.mMaxBound) {
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                ((TileServiceManager) arrayList.get(i)).calculateBindPriority(currentTimeMillis);
            }
            Collections.sort(arrayList, SERVICE_SORT);
        }
        int i2 = 0;
        while (i2 < this.mMaxBound && i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(true);
            i2++;
        }
        while (i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(false);
            i2++;
        }
    }

    public final void verifyCaller(CustomTile customTile) {
        try {
            if (Binder.getCallingUid() != this.mContext.getPackageManager().getPackageUidAsUser(customTile.getComponent().getPackageName(), Binder.getCallingUserHandle().getIdentifier())) {
                throw new SecurityException("Component outside caller's uid");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new SecurityException(e);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:17|18|19|20|21|22) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void requestListening(android.content.ComponentName r4) {
        /*
            r3 = this;
            android.util.ArrayMap<com.android.systemui.qs.external.CustomTile, com.android.systemui.qs.external.TileServiceManager> r0 = r3.mServices
            monitor-enter(r0)
            com.android.systemui.qs.external.CustomTile r1 = r3.getTileForComponent(r4)     // Catch:{ all -> 0x005c }
            if (r1 != 0) goto L_0x0021
            java.lang.String r3 = "TileServices"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x005c }
            r1.<init>()     // Catch:{ all -> 0x005c }
            java.lang.String r2 = "Couldn't find tile for "
            r1.append(r2)     // Catch:{ all -> 0x005c }
            r1.append(r4)     // Catch:{ all -> 0x005c }
            java.lang.String r4 = r1.toString()     // Catch:{ all -> 0x005c }
            android.util.Log.d(r3, r4)     // Catch:{ all -> 0x005c }
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x0021:
            android.util.ArrayMap<com.android.systemui.qs.external.CustomTile, com.android.systemui.qs.external.TileServiceManager> r3 = r3.mServices     // Catch:{ all -> 0x005c }
            java.lang.Object r3 = r3.get(r1)     // Catch:{ all -> 0x005c }
            com.android.systemui.qs.external.TileServiceManager r3 = (com.android.systemui.qs.external.TileServiceManager) r3     // Catch:{ all -> 0x005c }
            if (r3 != 0) goto L_0x0047
            java.lang.String r3 = "TileServices"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x005c }
            r4.<init>()     // Catch:{ all -> 0x005c }
            java.lang.String r2 = "No TileServiceManager found in requestListening for tile "
            r4.append(r2)     // Catch:{ all -> 0x005c }
            java.lang.String r1 = r1.getTileSpec()     // Catch:{ all -> 0x005c }
            r4.append(r1)     // Catch:{ all -> 0x005c }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x005c }
            android.util.Log.e(r3, r4)     // Catch:{ all -> 0x005c }
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x0047:
            boolean r4 = r3.isActiveTile()     // Catch:{ all -> 0x005c }
            if (r4 != 0) goto L_0x004f
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x004f:
            r4 = 1
            r3.setBindRequested(r4)     // Catch:{ all -> 0x005c }
            android.service.quicksettings.IQSTileService r3 = r3.getTileService()     // Catch:{ RemoteException -> 0x005a }
            r3.onStartListening()     // Catch:{ RemoteException -> 0x005a }
        L_0x005a:
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x005c:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.TileServices.requestListening(android.content.ComponentName):void");
    }

    public void updateQsTile(Tile tile, IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null) {
                    if (tileServiceManager.isLifecycleStarted()) {
                        tileServiceManager.clearPendingBind();
                        tileServiceManager.setLastUpdate(System.currentTimeMillis());
                        tileForToken.updateTileState(tile);
                        tileForToken.refreshState();
                        return;
                    }
                }
                Log.e("TileServices", "TileServiceManager not started for " + tileForToken.getComponent(), new IllegalStateException());
            }
        }
    }

    public void onStartSuccessful(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null) {
                    if (tileServiceManager.isLifecycleStarted()) {
                        tileServiceManager.clearPendingBind();
                        tileForToken.refreshState();
                        return;
                    }
                }
                Log.e("TileServices", "TileServiceManager not started for " + tileForToken.getComponent(), new IllegalStateException());
            }
        }
    }

    public void onShowDialog(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.onDialogShown();
            this.mHost.forceCollapsePanels();
            TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
            Objects.requireNonNull(tileServiceManager);
            tileServiceManager.setShowingDialog(true);
        }
    }

    public void onDialogHidden(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
            Objects.requireNonNull(tileServiceManager);
            tileServiceManager.setShowingDialog(false);
            tileForToken.onDialogHidden();
        }
    }

    public void onStartActivity(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            this.mHost.forceCollapsePanels();
        }
    }

    public void updateStatusIcon(IBinder iBinder, Icon icon, String str) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            try {
                final ComponentName component = tileForToken.getComponent();
                String packageName = component.getPackageName();
                UserHandle callingUserHandle = IQSService.Stub.getCallingUserHandle();
                if (this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, callingUserHandle.getIdentifier()).applicationInfo.isSystemApp()) {
                    final StatusBarIcon statusBarIcon = icon != null ? new StatusBarIcon(callingUserHandle, packageName, icon, 0, 0, str) : null;
                    this.mMainHandler.post(new Runnable() {
                        public void run() {
                            StatusBarIconController iconController = TileServices.this.mHost.getIconController();
                            iconController.setIcon(component.getClassName(), statusBarIcon);
                            iconController.setExternalIcon(component.getClassName());
                        }
                    });
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    public Tile getTile(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken == null) {
            return null;
        }
        verifyCaller(tileForToken);
        return tileForToken.getQsTile();
    }

    public void startUnlockAndRun(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.startUnlockAndRun();
        }
    }

    public boolean isLocked() {
        return this.mKeyguardStateController.isShowing();
    }

    public boolean isSecure() {
        return this.mKeyguardStateController.isMethodSecure() && this.mKeyguardStateController.isShowing();
    }

    public final CustomTile getTileForToken(IBinder iBinder) {
        CustomTile customTile;
        synchronized (this.mServices) {
            customTile = this.mTokenMap.get(iBinder);
        }
        return customTile;
    }

    public final CustomTile getTileForComponent(ComponentName componentName) {
        CustomTile customTile;
        synchronized (this.mServices) {
            customTile = this.mTiles.get(componentName);
        }
        return customTile;
    }
}
