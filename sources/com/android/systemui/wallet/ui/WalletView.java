package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import java.util.List;

public class WalletView extends FrameLayout implements WalletCardCarousel.OnCardScrollListener {
    public final Button mActionButton;
    public final float mAnimationTranslationX;
    public final Button mAppButton;
    public final WalletCardCarousel mCardCarousel;
    public final ViewGroup mCardCarouselContainer;
    public final TextView mCardLabel;
    public View.OnClickListener mDeviceLockedActionOnClickListener;
    public final ViewGroup mEmptyStateView;
    public final TextView mErrorView;
    public FalsingCollector mFalsingCollector;
    public final ImageView mIcon;
    public boolean mIsDeviceLocked;
    public boolean mIsUdfpsEnabled;
    public final Interpolator mOutInterpolator;
    public View.OnClickListener mShowWalletAppOnClickListener;
    public final Button mToolbarAppButton;

    public WalletView(Context context) {
        this(context, (AttributeSet) null);
    }

    public WalletView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsDeviceLocked = false;
        this.mIsUdfpsEnabled = false;
        FrameLayout.inflate(context, R$layout.wallet_fullscreen, this);
        this.mCardCarouselContainer = (ViewGroup) requireViewById(R$id.card_carousel_container);
        WalletCardCarousel walletCardCarousel = (WalletCardCarousel) requireViewById(R$id.card_carousel);
        this.mCardCarousel = walletCardCarousel;
        walletCardCarousel.setCardScrollListener(this);
        this.mIcon = (ImageView) requireViewById(R$id.icon);
        this.mCardLabel = (TextView) requireViewById(R$id.label);
        this.mAppButton = (Button) requireViewById(R$id.wallet_app_button);
        this.mToolbarAppButton = (Button) requireViewById(R$id.wallet_toolbar_app_button);
        this.mActionButton = (Button) requireViewById(R$id.wallet_action_button);
        this.mErrorView = (TextView) requireViewById(R$id.error_view);
        this.mEmptyStateView = (ViewGroup) requireViewById(R$id.wallet_empty_state);
        this.mOutInterpolator = AnimationUtils.loadInterpolator(context, 17563650);
        this.mAnimationTranslationX = ((float) walletCardCarousel.getCardWidthPx()) / 4.0f;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mCardCarousel.setExpectedViewWidth(getWidth());
    }

    public final void updateViewForOrientation(@Configuration.Orientation int i) {
        if (i == 1) {
            renderViewPortrait();
        } else if (i == 2) {
            renderViewLandscape();
        }
        this.mCardCarousel.resetAdapter();
        ViewGroup.LayoutParams layoutParams = this.mCardCarouselContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = getResources().getDimensionPixelSize(R$dimen.wallet_card_carousel_container_top_margin);
        }
    }

    public final void renderViewPortrait() {
        this.mAppButton.setVisibility(0);
        this.mToolbarAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(0);
        requireViewById(R$id.dynamic_placeholder).setVisibility(0);
        this.mAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    public final void renderViewLandscape() {
        this.mToolbarAppButton.setVisibility(0);
        this.mAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(8);
        requireViewById(R$id.dynamic_placeholder).setVisibility(8);
        this.mToolbarAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mCardCarousel.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }

    public void onCardScroll(WalletCardViewInfo walletCardViewInfo, WalletCardViewInfo walletCardViewInfo2, float f) {
        CharSequence labelText = getLabelText(walletCardViewInfo);
        Drawable headerIcon = getHeaderIcon(this.mContext, walletCardViewInfo);
        renderActionButton(walletCardViewInfo, this.mIsDeviceLocked, this.mIsUdfpsEnabled);
        if (walletCardViewInfo.isUiEquivalent(walletCardViewInfo2)) {
            this.mCardLabel.setAlpha(1.0f);
            this.mIcon.setAlpha(1.0f);
            this.mActionButton.setAlpha(1.0f);
            return;
        }
        this.mCardLabel.setText(labelText);
        this.mIcon.setImageDrawable(headerIcon);
        this.mCardLabel.setAlpha(f);
        this.mIcon.setAlpha(f);
        this.mActionButton.setAlpha(f);
    }

    public void showCardCarousel(List<WalletCardViewInfo> list, int i, boolean z, boolean z2) {
        boolean data = this.mCardCarousel.setData(list, i, this.mIsDeviceLocked != z);
        this.mIsDeviceLocked = z;
        this.mIsUdfpsEnabled = z2;
        this.mCardCarouselContainer.setVisibility(0);
        this.mCardCarousel.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
        this.mIcon.setImageDrawable(getHeaderIcon(this.mContext, list.get(i)));
        this.mCardLabel.setText(getLabelText(list.get(i)));
        updateViewForOrientation(getResources().getConfiguration().orientation);
        renderActionButton(list.get(i), z, this.mIsUdfpsEnabled);
        if (data) {
            animateViewsShown(this.mIcon, this.mCardLabel, this.mActionButton);
        }
    }

    public void animateDismissal() {
        if (this.mCardCarouselContainer.getVisibility() == 0) {
            this.mCardCarousel.animate().translationX(this.mAnimationTranslationX).setInterpolator(this.mOutInterpolator).setDuration(200).start();
            this.mCardCarouselContainer.animate().alpha(0.0f).setDuration(100).setStartDelay(50).start();
        }
    }

    public void showEmptyStateView(Drawable drawable, CharSequence charSequence, CharSequence charSequence2, View.OnClickListener onClickListener) {
        this.mEmptyStateView.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mCardCarousel.setVisibility(8);
        this.mIcon.setImageDrawable(drawable);
        this.mIcon.setContentDescription(charSequence);
        this.mCardLabel.setText(R$string.wallet_empty_state_label);
        ((ImageView) this.mEmptyStateView.requireViewById(R$id.empty_state_icon)).setImageDrawable(this.mContext.getDrawable(R$drawable.ic_qs_plus));
        ((TextView) this.mEmptyStateView.requireViewById(R$id.empty_state_title)).setText(charSequence2);
        this.mEmptyStateView.setOnClickListener(onClickListener);
        this.mAppButton.setOnClickListener(onClickListener);
    }

    public void showErrorMessage(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = getResources().getText(R$string.wallet_error_generic);
        }
        this.mErrorView.setText(charSequence);
        this.mErrorView.setVisibility(0);
        this.mCardCarouselContainer.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
    }

    public void setDeviceLockedActionOnClickListener(View.OnClickListener onClickListener) {
        this.mDeviceLockedActionOnClickListener = onClickListener;
    }

    public void setShowWalletAppOnClickListener(View.OnClickListener onClickListener) {
        this.mShowWalletAppOnClickListener = onClickListener;
    }

    public void hide() {
        setVisibility(8);
    }

    public void show() {
        setVisibility(0);
    }

    public void hideErrorMessage() {
        this.mErrorView.setVisibility(8);
    }

    public WalletCardCarousel getCardCarousel() {
        return this.mCardCarousel;
    }

    @VisibleForTesting
    public Button getAppButton() {
        return this.mAppButton;
    }

    @VisibleForTesting
    public TextView getErrorView() {
        return this.mErrorView;
    }

    @VisibleForTesting
    public ViewGroup getEmptyStateView() {
        return this.mEmptyStateView;
    }

    @VisibleForTesting
    public ViewGroup getCardCarouselContainer() {
        return this.mCardCarouselContainer;
    }

    @VisibleForTesting
    public TextView getCardLabel() {
        return this.mCardLabel;
    }

    public static Drawable getHeaderIcon(Context context, WalletCardViewInfo walletCardViewInfo) {
        Drawable icon = walletCardViewInfo.getIcon();
        if (icon != null) {
            icon.setTint(Utils.getColorAttrDefaultColor(context, 17956900));
        }
        return icon;
    }

    public final void renderActionButton(WalletCardViewInfo walletCardViewInfo, boolean z, boolean z2) {
        View.OnClickListener onClickListener;
        CharSequence actionButtonText = getActionButtonText(walletCardViewInfo);
        if (z2 || actionButtonText == null) {
            this.mActionButton.setVisibility(8);
            return;
        }
        this.mActionButton.setVisibility(0);
        this.mActionButton.setText(actionButtonText);
        Button button = this.mActionButton;
        if (z) {
            onClickListener = this.mDeviceLockedActionOnClickListener;
        } else {
            onClickListener = new WalletView$$ExternalSyntheticLambda0(walletCardViewInfo);
        }
        button.setOnClickListener(onClickListener);
    }

    public static /* synthetic */ void lambda$renderActionButton$0(WalletCardViewInfo walletCardViewInfo, View view) {
        try {
            walletCardViewInfo.getPendingIntent().send();
        } catch (PendingIntent.CanceledException unused) {
            Log.w("WalletView", "Error sending pending intent for wallet card.");
        }
    }

    public static void animateViewsShown(View... viewArr) {
        for (View view : viewArr) {
            if (view.getVisibility() == 0) {
                view.setAlpha(0.0f);
                view.animate().alpha(1.0f).setDuration(100).start();
            }
        }
    }

    public static CharSequence getLabelText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        return split.length == 2 ? split[0] : walletCardViewInfo.getLabel();
    }

    public static CharSequence getActionButtonText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        if (split.length == 2) {
            return split[1];
        }
        return null;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        FalsingCollector falsingCollector = this.mFalsingCollector;
        if (falsingCollector != null) {
            falsingCollector.onTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        FalsingCollector falsingCollector2 = this.mFalsingCollector;
        if (falsingCollector2 != null) {
            falsingCollector2.onMotionEventComplete();
        }
        return dispatchTouchEvent;
    }

    public void setFalsingCollector(FalsingCollector falsingCollector) {
        this.mFalsingCollector = falsingCollector;
    }
}
