package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;

public interface WalletCardViewInfo {
    Drawable getCardDrawable();

    String getCardId();

    CharSequence getContentDescription();

    Drawable getIcon();

    CharSequence getLabel();

    PendingIntent getPendingIntent();

    boolean isUiEquivalent(WalletCardViewInfo walletCardViewInfo) {
        if (walletCardViewInfo == null) {
            return false;
        }
        return getCardId().equals(walletCardViewInfo.getCardId());
    }
}
