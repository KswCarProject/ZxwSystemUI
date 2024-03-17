package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.settingslib.development.DevelopmentSettingsEnabler;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.qs.TouchAnimator;

public class QSFooterView extends FrameLayout {
    public TextView mBuildText;
    public final ContentObserver mDeveloperSettingsObserver = new ContentObserver(new Handler(this.mContext.getMainLooper())) {
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            QSFooterView.this.setBuildText();
        }
    };
    public View mEditButton;
    public boolean mExpanded;
    public float mExpansionAmount;
    public TouchAnimator mFooterAnimator;
    public PageIndicator mPageIndicator;
    public boolean mQsDisabled;
    public boolean mShouldShowBuildText;

    public QSFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPageIndicator = (PageIndicator) findViewById(R$id.footer_page_indicator);
        this.mBuildText = (TextView) findViewById(R$id.build);
        this.mEditButton = findViewById(16908291);
        updateResources();
        setImportantForAccessibility(1);
        setBuildText();
    }

    public final void setBuildText() {
        if (this.mBuildText != null) {
            if (DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(this.mContext)) {
                this.mBuildText.setText(this.mContext.getString(17039825, new Object[]{Build.VERSION.RELEASE_OR_CODENAME, Build.ID}));
                this.mBuildText.setSelected(true);
                this.mShouldShowBuildText = true;
                return;
            }
            this.mBuildText.setText((CharSequence) null);
            this.mShouldShowBuildText = false;
            this.mBuildText.setSelected(false);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    public final void updateResources() {
        updateFooterAnimator();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R$dimen.qs_footers_margin_bottom);
        setLayoutParams(marginLayoutParams);
    }

    public final void updateFooterAnimator() {
        this.mFooterAnimator = createFooterAnimator();
    }

    public final TouchAnimator createFooterAnimator() {
        return new TouchAnimator.Builder().addFloat(this.mPageIndicator, "alpha", 0.0f, 1.0f).addFloat(this.mBuildText, "alpha", 0.0f, 1.0f).addFloat(this.mEditButton, "alpha", 0.0f, 1.0f).setStartDelay(0.9f).build();
    }

    public void setKeyguardShowing() {
        setExpansion(this.mExpansionAmount);
    }

    public void setExpanded(boolean z) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            updateEverything();
        }
    }

    public void setExpansion(float f) {
        this.mExpansionAmount = f;
        TouchAnimator touchAnimator = this.mFooterAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("development_settings_enabled"), false, this.mDeveloperSettingsObserver, -1);
    }

    public void onDetachedFromWindow() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mDeveloperSettingsObserver);
        super.onDetachedFromWindow();
    }

    public void disable(int i) {
        boolean z = true;
        if ((i & 1) == 0) {
            z = false;
        }
        if (z != this.mQsDisabled) {
            this.mQsDisabled = z;
            updateEverything();
        }
    }

    public void updateEverything() {
        post(new QSFooterView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEverything$0() {
        updateVisibilities();
        updateClickabilities();
        setClickable(false);
    }

    public final void updateClickabilities() {
        TextView textView = this.mBuildText;
        textView.setLongClickable(textView.getVisibility() == 0);
    }

    public final void updateVisibilities() {
        this.mBuildText.setVisibility((!this.mExpanded || !this.mShouldShowBuildText) ? 4 : 0);
    }
}
