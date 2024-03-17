package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.SelectWalletCardRequest;
import android.service.quickaccesswallet.WalletCard;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$dimen;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class WalletScreenController implements WalletCardCarousel.OnSelectionListener, QuickAccessWalletClient.OnWalletCardsRetrievedCallback, KeyguardStateController.Callback {
    public static final long SELECTION_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(30);
    public final ActivityStarter mActivityStarter;
    public final WalletCardCarousel mCardCarousel;
    public Context mContext;
    public final Executor mExecutor;
    public final FalsingManager mFalsingManager;
    public final Handler mHandler;
    @VisibleForTesting
    public boolean mIsDismissed;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final SharedPreferences mPrefs;
    @VisibleForTesting
    public String mSelectedCardId;
    public final Runnable mSelectionRunnable = new WalletScreenController$$ExternalSyntheticLambda2(this);
    public final UiEventLogger mUiEventLogger;
    public final QuickAccessWalletClient mWalletClient;
    public final WalletView mWalletView;

    public WalletScreenController(Context context, WalletView walletView, QuickAccessWalletClient quickAccessWalletClient, ActivityStarter activityStarter, Executor executor, Handler handler, UserTracker userTracker, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mWalletClient = quickAccessWalletClient;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        this.mPrefs = userTracker.getUserContext().getSharedPreferences("WalletScreenCtrl", 0);
        this.mWalletView = walletView;
        walletView.setMinimumHeight(getExpectedMinHeight());
        walletView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        WalletCardCarousel cardCarousel = walletView.getCardCarousel();
        this.mCardCarousel = cardCarousel;
        if (cardCarousel != null) {
            cardCarousel.setSelectionListener(this);
        }
    }

    public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
        if (!this.mIsDismissed) {
            Log.i("WalletScreenCtrl", "Successfully retrieved wallet cards.");
            List<WalletCard> walletCards = getWalletCardsResponse.getWalletCards();
            ArrayList arrayList = new ArrayList(walletCards.size());
            for (WalletCard qAWalletCardViewInfo : walletCards) {
                arrayList.add(new QAWalletCardViewInfo(this.mContext, qAWalletCardViewInfo));
            }
            this.mHandler.post(new WalletScreenController$$ExternalSyntheticLambda0(this, arrayList, getWalletCardsResponse));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onWalletCardsRetrieved$0(List list, GetWalletCardsResponse getWalletCardsResponse) {
        if (!this.mIsDismissed) {
            if (list.isEmpty()) {
                showEmptyStateView();
            } else {
                int selectedIndex = getWalletCardsResponse.getSelectedIndex();
                if (selectedIndex >= list.size()) {
                    Log.w("WalletScreenCtrl", "Invalid selected card index, showing empty state.");
                    showEmptyStateView();
                } else {
                    this.mWalletView.showCardCarousel(list, selectedIndex, true ^ this.mKeyguardStateController.isUnlocked(), this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning());
                }
            }
            this.mUiEventLogger.log(WalletUiEvent.QAW_IMPRESSION);
            removeMinHeightAndRecordHeightOnLayout();
        }
    }

    public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
        this.mHandler.post(new WalletScreenController$$ExternalSyntheticLambda1(this, getWalletCardsError));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onWalletCardRetrievalError$1(GetWalletCardsError getWalletCardsError) {
        if (!this.mIsDismissed) {
            this.mWalletView.showErrorMessage(getWalletCardsError.getMessage());
        }
    }

    public void onKeyguardFadingAwayChanged() {
        queryWalletCards();
    }

    public void onUnlockedChanged() {
        queryWalletCards();
    }

    public void onUncenteredClick(int i) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mCardCarousel.smoothScrollToPosition(i);
        }
    }

    public void onCardSelected(WalletCardViewInfo walletCardViewInfo) {
        if (!this.mIsDismissed) {
            String str = this.mSelectedCardId;
            if (str != null && !str.equals(walletCardViewInfo.getCardId())) {
                this.mUiEventLogger.log(WalletUiEvent.QAW_CHANGE_CARD);
            }
            this.mSelectedCardId = walletCardViewInfo.getCardId();
            selectCard();
        }
    }

    public final void selectCard() {
        this.mHandler.removeCallbacks(this.mSelectionRunnable);
        String str = this.mSelectedCardId;
        if (!this.mIsDismissed && str != null) {
            this.mWalletClient.selectWalletCard(new SelectWalletCardRequest(str));
            this.mHandler.postDelayed(this.mSelectionRunnable, SELECTION_DELAY_MILLIS);
        }
    }

    public void onCardClicked(WalletCardViewInfo walletCardViewInfo) {
        if (!this.mFalsingManager.isFalseTap(1) && (walletCardViewInfo instanceof QAWalletCardViewInfo)) {
            QAWalletCardViewInfo qAWalletCardViewInfo = (QAWalletCardViewInfo) walletCardViewInfo;
            if (qAWalletCardViewInfo.mWalletCard != null && qAWalletCardViewInfo.mWalletCard.getPendingIntent() != null) {
                if (!this.mKeyguardStateController.isUnlocked()) {
                    this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_CARD_CLICK);
                }
                this.mUiEventLogger.log(WalletUiEvent.QAW_CLICK_CARD);
                this.mActivityStarter.startPendingIntentDismissingKeyguard(walletCardViewInfo.getPendingIntent());
            }
        }
    }

    public void queryWalletCards() {
        if (!this.mIsDismissed) {
            int cardWidthPx = this.mCardCarousel.getCardWidthPx();
            int cardHeightPx = this.mCardCarousel.getCardHeightPx();
            if (cardWidthPx != 0 && cardHeightPx != 0) {
                this.mWalletView.show();
                this.mWalletView.hideErrorMessage();
                this.mWalletClient.getWalletCards(this.mExecutor, new GetWalletCardsRequest(cardWidthPx, cardHeightPx, this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_screen_header_icon_size), 10), this);
            }
        }
    }

    public void onDismissed() {
        if (!this.mIsDismissed) {
            this.mIsDismissed = true;
            this.mSelectedCardId = null;
            this.mHandler.removeCallbacks(this.mSelectionRunnable);
            this.mWalletClient.notifyWalletDismissed();
            this.mWalletView.animateDismissal();
            this.mContext = null;
        }
    }

    public final void showEmptyStateView() {
        Drawable logo = this.mWalletClient.getLogo();
        CharSequence serviceLabel = this.mWalletClient.getServiceLabel();
        CharSequence shortcutLongLabel = this.mWalletClient.getShortcutLongLabel();
        Intent createWalletIntent = this.mWalletClient.createWalletIntent();
        if (logo == null || TextUtils.isEmpty(serviceLabel) || TextUtils.isEmpty(shortcutLongLabel) || createWalletIntent == null) {
            Log.w("WalletScreenCtrl", "QuickAccessWalletService manifest entry mis-configured");
            this.mWalletView.hide();
            this.mPrefs.edit().putInt("wallet_view_height", 0).apply();
            return;
        }
        this.mWalletView.showEmptyStateView(logo, serviceLabel, shortcutLongLabel, new WalletScreenController$$ExternalSyntheticLambda3(this, createWalletIntent));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showEmptyStateView$2(Intent intent, View view) {
        this.mActivityStarter.startActivity(intent, true);
    }

    public final int getExpectedMinHeight() {
        int i = this.mPrefs.getInt("wallet_view_height", -1);
        return i == -1 ? this.mContext.getResources().getDimensionPixelSize(R$dimen.min_wallet_empty_height) : i;
    }

    public final void removeMinHeightAndRecordHeightOnLayout() {
        this.mWalletView.setMinimumHeight(0);
        this.mWalletView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                WalletScreenController.this.mWalletView.removeOnLayoutChangeListener(this);
                WalletScreenController.this.mPrefs.edit().putInt("wallet_view_height", i4 - i2).apply();
            }
        });
    }

    @VisibleForTesting
    public static class QAWalletCardViewInfo implements WalletCardViewInfo {
        public final Drawable mCardDrawable;
        public final Drawable mIconDrawable;
        public final WalletCard mWalletCard;

        public QAWalletCardViewInfo(Context context, WalletCard walletCard) {
            Drawable drawable;
            this.mWalletCard = walletCard;
            this.mCardDrawable = walletCard.getCardImage().loadDrawable(context);
            Icon cardIcon = walletCard.getCardIcon();
            if (cardIcon == null) {
                drawable = null;
            } else {
                drawable = cardIcon.loadDrawable(context);
            }
            this.mIconDrawable = drawable;
        }

        public String getCardId() {
            return this.mWalletCard.getCardId();
        }

        public Drawable getCardDrawable() {
            return this.mCardDrawable;
        }

        public CharSequence getContentDescription() {
            return this.mWalletCard.getContentDescription();
        }

        public Drawable getIcon() {
            return this.mIconDrawable;
        }

        public CharSequence getLabel() {
            CharSequence cardLabel = this.mWalletCard.getCardLabel();
            return cardLabel == null ? "" : cardLabel;
        }

        public PendingIntent getPendingIntent() {
            return this.mWalletCard.getPendingIntent();
        }
    }
}
