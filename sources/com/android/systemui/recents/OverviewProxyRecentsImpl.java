package com.android.systemui.recents;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.util.Optional;

public class OverviewProxyRecentsImpl implements RecentsImplementation {
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public Handler mHandler;
    public final OverviewProxyService mOverviewProxyService;

    public OverviewProxyRecentsImpl(Lazy<Optional<CentralSurfaces>> lazy, OverviewProxyService overviewProxyService) {
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mOverviewProxyService = overviewProxyService;
    }

    public void onStart(Context context) {
        this.mHandler = new Handler();
    }

    public void showRecentApps(boolean z) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewShown(z);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview show event to launcher.", e);
            }
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewHidden(z, z2);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview hide event to launcher.", e);
            }
        }
    }

    public void toggleRecentApps() {
        if (this.mOverviewProxyService.getProxy() != null) {
            OverviewProxyRecentsImpl$$ExternalSyntheticLambda0 overviewProxyRecentsImpl$$ExternalSyntheticLambda0 = new OverviewProxyRecentsImpl$$ExternalSyntheticLambda0(this);
            Optional optional = this.mCentralSurfacesOptionalLazy.get();
            if (((Boolean) optional.map(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8()).orElse(Boolean.FALSE)).booleanValue()) {
                ((CentralSurfaces) optional.get()).executeRunnableDismissingKeyguard(new OverviewProxyRecentsImpl$$ExternalSyntheticLambda1(this, overviewProxyRecentsImpl$$ExternalSyntheticLambda0), (Runnable) null, true, false, true);
            } else {
                overviewProxyRecentsImpl$$ExternalSyntheticLambda0.run();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecentApps$0() {
        try {
            if (this.mOverviewProxyService.getProxy() != null) {
                this.mOverviewProxyService.getProxy().onOverviewToggle();
                this.mOverviewProxyService.notifyToggleRecentApps();
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyRecentsImpl", "Cannot send toggle recents through proxy service.", e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecentApps$1(Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
