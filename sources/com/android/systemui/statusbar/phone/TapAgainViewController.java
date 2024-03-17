package com.android.systemui.statusbar.phone;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;

public class TapAgainViewController extends ViewController<TapAgainView> {
    public final ConfigurationController mConfigurationController;
    @VisibleForTesting
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            ((TapAgainView) TapAgainViewController.this.mView).updateColor();
        }

        public void onThemeChanged() {
            ((TapAgainView) TapAgainViewController.this.mView).updateColor();
        }
    };
    public final DelayableExecutor mDelayableExecutor;
    public final long mDoubleTapTimeMs;
    public Runnable mHideCanceler;

    public TapAgainViewController(TapAgainView tapAgainView, DelayableExecutor delayableExecutor, ConfigurationController configurationController, long j) {
        super(tapAgainView);
        this.mDelayableExecutor = delayableExecutor;
        this.mConfigurationController = configurationController;
        this.mDoubleTapTimeMs = j;
    }

    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public void show() {
        Runnable runnable = this.mHideCanceler;
        if (runnable != null) {
            runnable.run();
        }
        ((TapAgainView) this.mView).animateIn();
        this.mHideCanceler = this.mDelayableExecutor.executeDelayed(new TapAgainViewController$$ExternalSyntheticLambda0(this), this.mDoubleTapTimeMs);
    }

    public void hide() {
        this.mHideCanceler = null;
        ((TapAgainView) this.mView).animateOut();
    }
}
