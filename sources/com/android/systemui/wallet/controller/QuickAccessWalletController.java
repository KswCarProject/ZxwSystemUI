package com.android.systemui.wallet.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wallet.ui.WalletActivity;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class QuickAccessWalletController {
    public static final long RECREATION_TIME_WINDOW = TimeUnit.MINUTES.toMillis(10);
    public final Executor mBgExecutor;
    public final SystemClock mClock;
    public final Context mContext;
    public int mDefaultPaymentAppChangeEvents = 0;
    public ContentObserver mDefaultPaymentAppObserver;
    public final Executor mExecutor;
    public long mQawClientCreatedTimeMillis;
    public QuickAccessWalletClient mQuickAccessWalletClient;
    public final SecureSettings mSecureSettings;
    public boolean mWalletEnabled = false;
    public int mWalletPreferenceChangeEvents = 0;
    public ContentObserver mWalletPreferenceObserver;

    public enum WalletChangeEvent {
        DEFAULT_PAYMENT_APP_CHANGE,
        WALLET_PREFERENCE_CHANGE
    }

    public QuickAccessWalletController(Context context, Executor executor, Executor executor2, SecureSettings secureSettings, QuickAccessWalletClient quickAccessWalletClient, SystemClock systemClock) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mBgExecutor = executor2;
        this.mSecureSettings = secureSettings;
        this.mQuickAccessWalletClient = quickAccessWalletClient;
        this.mClock = systemClock;
        this.mQawClientCreatedTimeMillis = systemClock.elapsedRealtime();
    }

    public boolean isWalletEnabled() {
        return this.mWalletEnabled;
    }

    public QuickAccessWalletClient getWalletClient() {
        return this.mQuickAccessWalletClient;
    }

    public void setupWalletChangeObservers(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback, WalletChangeEvent... walletChangeEventArr) {
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE) {
                setupWalletPreferenceObserver();
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE) {
                setupDefaultPaymentAppObserver(onWalletCardsRetrievedCallback);
            }
        }
    }

    public void unregisterWalletChangeObservers(WalletChangeEvent... walletChangeEventArr) {
        ContentObserver contentObserver;
        ContentObserver contentObserver2;
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE && (contentObserver2 = this.mWalletPreferenceObserver) != null) {
                int i = this.mWalletPreferenceChangeEvents - 1;
                this.mWalletPreferenceChangeEvents = i;
                if (i == 0) {
                    this.mSecureSettings.unregisterContentObserver(contentObserver2);
                }
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE && (contentObserver = this.mDefaultPaymentAppObserver) != null) {
                int i2 = this.mDefaultPaymentAppChangeEvents - 1;
                this.mDefaultPaymentAppChangeEvents = i2;
                if (i2 == 0) {
                    this.mSecureSettings.unregisterContentObserver(contentObserver);
                }
            }
        }
    }

    public void updateWalletPreference() {
        this.mWalletEnabled = this.mQuickAccessWalletClient.isWalletServiceAvailable() && this.mQuickAccessWalletClient.isWalletFeatureAvailable() && this.mQuickAccessWalletClient.isWalletFeatureAvailableWhenDeviceLocked();
    }

    public void queryWalletCards(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        if (this.mClock.elapsedRealtime() - this.mQawClientCreatedTimeMillis > RECREATION_TIME_WINDOW) {
            Log.i("QAWController", "Re-creating the QAW client to avoid stale.");
            reCreateWalletClient();
        }
        if (!this.mQuickAccessWalletClient.isWalletFeatureAvailable()) {
            Log.d("QAWController", "QuickAccessWallet feature is not available.");
            return;
        }
        this.mQuickAccessWalletClient.getWalletCards(this.mBgExecutor, new GetWalletCardsRequest(this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_tile_card_view_width), this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_tile_card_view_height), this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_icon_size), 1), onWalletCardsRetrievedCallback);
    }

    public void reCreateWalletClient() {
        this.mQuickAccessWalletClient = QuickAccessWalletClient.create(this.mContext, this.mBgExecutor);
        this.mQawClientCreatedTimeMillis = this.mClock.elapsedRealtime();
    }

    public void startQuickAccessUiIntent(ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller, boolean z) {
        this.mQuickAccessWalletClient.getWalletPendingIntent(this.mExecutor, new QuickAccessWalletController$$ExternalSyntheticLambda0(this, activityStarter, controller, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startQuickAccessUiIntent$0(ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller, boolean z, PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            startQuickAccessViaPendingIntent(pendingIntent, activityStarter, controller);
            return;
        }
        Intent intent = null;
        if (!z) {
            intent = this.mQuickAccessWalletClient.createWalletIntent();
        }
        if (intent == null) {
            intent = getSysUiWalletIntent();
        }
        startQuickAccessViaIntent(intent, z, activityStarter, controller);
    }

    public final Intent getSysUiWalletIntent() {
        return new Intent(this.mContext, WalletActivity.class).setAction("android.intent.action.VIEW");
    }

    public final void startQuickAccessViaIntent(Intent intent, boolean z, ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller) {
        if (z) {
            activityStarter.startActivity(intent, true, controller, true);
        } else {
            activityStarter.postStartActivityDismissingKeyguard(intent, 0, controller);
        }
    }

    public final void startQuickAccessViaPendingIntent(PendingIntent pendingIntent, ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller) {
        activityStarter.postStartActivityDismissingKeyguard(pendingIntent, controller);
    }

    public final void setupDefaultPaymentAppObserver(final QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        if (this.mDefaultPaymentAppObserver == null) {
            this.mDefaultPaymentAppObserver = new ContentObserver((Handler) null) {
                public void onChange(boolean z) {
                    QuickAccessWalletController.this.mExecutor.execute(new QuickAccessWalletController$1$$ExternalSyntheticLambda0(this, onWalletCardsRetrievedCallback));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onChange$0(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
                    QuickAccessWalletController.this.reCreateWalletClient();
                    QuickAccessWalletController.this.updateWalletPreference();
                    QuickAccessWalletController.this.queryWalletCards(onWalletCardsRetrievedCallback);
                }
            };
            this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("nfc_payment_default_component"), false, this.mDefaultPaymentAppObserver, -1);
        }
        this.mDefaultPaymentAppChangeEvents++;
    }

    public final void setupWalletPreferenceObserver() {
        if (this.mWalletPreferenceObserver == null) {
            this.mWalletPreferenceObserver = new ContentObserver((Handler) null) {
                public void onChange(boolean z) {
                    QuickAccessWalletController.this.mExecutor.execute(new QuickAccessWalletController$2$$ExternalSyntheticLambda0(this));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onChange$0() {
                    QuickAccessWalletController.this.updateWalletPreference();
                }
            };
            this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("lockscreen_show_wallet"), false, this.mWalletPreferenceObserver, -1);
        }
        this.mWalletPreferenceChangeEvents++;
    }
}
