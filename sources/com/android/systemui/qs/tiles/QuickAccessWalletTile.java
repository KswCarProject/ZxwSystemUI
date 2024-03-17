package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletCard;
import android.util.Log;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import java.util.List;

public class QuickAccessWalletTile extends QSTileImpl<QSTile.State> {
    public final WalletCardRetriever mCardRetriever = new WalletCardRetriever();
    @VisibleForTesting
    public Drawable mCardViewDrawable;
    public final QuickAccessWalletController mController;
    public boolean mIsWalletUpdating = true;
    public final KeyguardStateController mKeyguardStateController;
    public final CharSequence mLabel = this.mContext.getString(R$string.wallet_title);
    public final PackageManager mPackageManager;
    public final SecureSettings mSecureSettings;
    public WalletCard mSelectedCard;

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    public QuickAccessWalletTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, KeyguardStateController keyguardStateController, PackageManager packageManager, SecureSettings secureSettings, QuickAccessWalletController quickAccessWalletController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mController = quickAccessWalletController;
        this.mKeyguardStateController = keyguardStateController;
        this.mPackageManager = packageManager;
        this.mSecureSettings = secureSettings;
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (z) {
            this.mController.setupWalletChangeObservers(this.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
            if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
                Log.i("QuickAccessWalletTile", "QAW service is unavailable, recreating the wallet client.");
                this.mController.reCreateWalletClient();
            }
            this.mController.queryWalletCards(this.mCardRetriever);
        }
    }

    public void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mUiHandler.post(new QuickAccessWalletTile$$ExternalSyntheticLambda0(this, controller));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0(ActivityLaunchAnimator.Controller controller) {
        this.mController.startQuickAccessUiIntent(this.mActivityStarter, controller, this.mSelectedCard != null);
    }

    public void handleUpdateState(QSTile.State state, Object obj) {
        QSTile.Icon icon;
        int i;
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        if (serviceLabel == null) {
            serviceLabel = this.mLabel;
        }
        state.label = serviceLabel;
        state.contentDescription = serviceLabel;
        Drawable tileIcon = this.mController.getWalletClient().getTileIcon();
        if (tileIcon == null) {
            icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_wallet_lockscreen);
        } else {
            icon = new QSTileImpl.DrawableIcon(tileIcon);
        }
        state.icon = icon;
        int i2 = 1;
        boolean z = !this.mKeyguardStateController.isUnlocked();
        if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
            state.state = 0;
            state.secondaryLabel = null;
            state.sideViewCustomDrawable = null;
            return;
        }
        WalletCard walletCard = this.mSelectedCard;
        if (walletCard != null) {
            if (!z) {
                i2 = 2;
            }
            state.state = i2;
            state.secondaryLabel = walletCard.getContentDescription();
            state.sideViewCustomDrawable = this.mCardViewDrawable;
        } else {
            state.state = 1;
            Context context = this.mContext;
            if (this.mIsWalletUpdating) {
                i = R$string.wallet_secondary_label_updating;
            } else {
                i = R$string.wallet_secondary_label_no_card;
            }
            state.secondaryLabel = context.getString(i);
            state.sideViewCustomDrawable = null;
        }
        state.stateDescription = state.secondaryLabel;
    }

    public boolean isAvailable() {
        return this.mPackageManager.hasSystemFeature("android.hardware.nfc.hce") && !this.mPackageManager.hasSystemFeature("org.chromium.arc") && this.mSecureSettings.getStringForUser("nfc_payment_default_component", -2) != null;
    }

    public CharSequence getTileLabel() {
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        return serviceLabel == null ? this.mLabel : serviceLabel;
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mController.unregisterWalletChangeObservers(QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
    }

    public class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        public WalletCardRetriever() {
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            Log.i("QuickAccessWalletTile", "Successfully retrieved wallet cards.");
            QuickAccessWalletTile.this.mIsWalletUpdating = false;
            List walletCards = getWalletCardsResponse.getWalletCards();
            if (walletCards.isEmpty()) {
                Log.d("QuickAccessWalletTile", "No wallet cards exist.");
                QuickAccessWalletTile quickAccessWalletTile = QuickAccessWalletTile.this;
                quickAccessWalletTile.mCardViewDrawable = null;
                quickAccessWalletTile.mSelectedCard = null;
                QuickAccessWalletTile.this.refreshState();
                return;
            }
            int selectedIndex = getWalletCardsResponse.getSelectedIndex();
            if (selectedIndex >= walletCards.size()) {
                Log.w("QuickAccessWalletTile", "Error retrieving cards: Invalid selected card index.");
                QuickAccessWalletTile.this.mSelectedCard = null;
                QuickAccessWalletTile.this.mCardViewDrawable = null;
                return;
            }
            QuickAccessWalletTile.this.mSelectedCard = (WalletCard) walletCards.get(selectedIndex);
            QuickAccessWalletTile quickAccessWalletTile2 = QuickAccessWalletTile.this;
            quickAccessWalletTile2.mCardViewDrawable = quickAccessWalletTile2.mSelectedCard.getCardImage().loadDrawable(QuickAccessWalletTile.this.mContext);
            QuickAccessWalletTile.this.refreshState();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            QuickAccessWalletTile.this.mIsWalletUpdating = false;
            QuickAccessWalletTile quickAccessWalletTile = QuickAccessWalletTile.this;
            quickAccessWalletTile.mCardViewDrawable = null;
            quickAccessWalletTile.mSelectedCard = null;
            QuickAccessWalletTile.this.refreshState();
        }
    }
}
