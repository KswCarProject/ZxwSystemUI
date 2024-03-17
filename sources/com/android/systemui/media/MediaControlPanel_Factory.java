package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaControlPanel_Factory implements Factory<MediaControlPanel> {
    public final Provider<ActivityIntentHelper> activityIntentHelperProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<MediaUiEventLogger> loggerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<MediaCarouselController> mediaCarouselControllerProvider;
    public final Provider<MediaDataManager> mediaDataManagerProvider;
    public final Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
    public final Provider<MediaViewController> mediaViewControllerProvider;
    public final Provider<SeekBarViewModel> seekBarViewModelProvider;
    public final Provider<SystemClock> systemClockProvider;

    public MediaControlPanel_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<MediaViewController> provider6, Provider<SeekBarViewModel> provider7, Provider<MediaDataManager> provider8, Provider<MediaOutputDialogFactory> provider9, Provider<MediaCarouselController> provider10, Provider<FalsingManager> provider11, Provider<SystemClock> provider12, Provider<MediaUiEventLogger> provider13, Provider<KeyguardStateController> provider14, Provider<ActivityIntentHelper> provider15, Provider<NotificationLockscreenUserManager> provider16) {
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.activityStarterProvider = provider4;
        this.broadcastSenderProvider = provider5;
        this.mediaViewControllerProvider = provider6;
        this.seekBarViewModelProvider = provider7;
        this.mediaDataManagerProvider = provider8;
        this.mediaOutputDialogFactoryProvider = provider9;
        this.mediaCarouselControllerProvider = provider10;
        this.falsingManagerProvider = provider11;
        this.systemClockProvider = provider12;
        this.loggerProvider = provider13;
        this.keyguardStateControllerProvider = provider14;
        this.activityIntentHelperProvider = provider15;
        this.lockscreenUserManagerProvider = provider16;
    }

    public MediaControlPanel get() {
        return newInstance(this.contextProvider.get(), this.backgroundExecutorProvider.get(), this.mainExecutorProvider.get(), this.activityStarterProvider.get(), this.broadcastSenderProvider.get(), this.mediaViewControllerProvider.get(), this.seekBarViewModelProvider.get(), DoubleCheck.lazy(this.mediaDataManagerProvider), this.mediaOutputDialogFactoryProvider.get(), this.mediaCarouselControllerProvider.get(), this.falsingManagerProvider.get(), this.systemClockProvider.get(), this.loggerProvider.get(), this.keyguardStateControllerProvider.get(), this.activityIntentHelperProvider.get(), this.lockscreenUserManagerProvider.get());
    }

    public static MediaControlPanel_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<MediaViewController> provider6, Provider<SeekBarViewModel> provider7, Provider<MediaDataManager> provider8, Provider<MediaOutputDialogFactory> provider9, Provider<MediaCarouselController> provider10, Provider<FalsingManager> provider11, Provider<SystemClock> provider12, Provider<MediaUiEventLogger> provider13, Provider<KeyguardStateController> provider14, Provider<ActivityIntentHelper> provider15, Provider<NotificationLockscreenUserManager> provider16) {
        return new MediaControlPanel_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static MediaControlPanel newInstance(Context context, Executor executor, Executor executor2, ActivityStarter activityStarter, BroadcastSender broadcastSender, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController, FalsingManager falsingManager, SystemClock systemClock, MediaUiEventLogger mediaUiEventLogger, KeyguardStateController keyguardStateController, ActivityIntentHelper activityIntentHelper, NotificationLockscreenUserManager notificationLockscreenUserManager) {
        return new MediaControlPanel(context, executor, executor2, activityStarter, broadcastSender, mediaViewController, seekBarViewModel, lazy, mediaOutputDialogFactory, mediaCarouselController, falsingManager, systemClock, mediaUiEventLogger, keyguardStateController, activityIntentHelper, notificationLockscreenUserManager);
    }
}
