package com.android.systemui.volume;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class VolumeDialogControllerImpl_Factory implements Factory<VolumeDialogControllerImpl> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<ActivityManager> activityManagerProvider;
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CaptioningManager> captioningManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<IAudioService> iAudioServiceProvider;
    public final Provider<KeyguardManager> keyguardManagerProvider;
    public final Provider<NotificationManager> notificationManagerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<RingerModeTracker> ringerModeTrackerProvider;
    public final Provider<ThreadFactory> theadFactoryProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public VolumeDialogControllerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<RingerModeTracker> provider3, Provider<ThreadFactory> provider4, Provider<AudioManager> provider5, Provider<NotificationManager> provider6, Provider<VibratorHelper> provider7, Provider<IAudioService> provider8, Provider<AccessibilityManager> provider9, Provider<PackageManager> provider10, Provider<WakefulnessLifecycle> provider11, Provider<CaptioningManager> provider12, Provider<KeyguardManager> provider13, Provider<ActivityManager> provider14) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.ringerModeTrackerProvider = provider3;
        this.theadFactoryProvider = provider4;
        this.audioManagerProvider = provider5;
        this.notificationManagerProvider = provider6;
        this.vibratorProvider = provider7;
        this.iAudioServiceProvider = provider8;
        this.accessibilityManagerProvider = provider9;
        this.packageManagerProvider = provider10;
        this.wakefulnessLifecycleProvider = provider11;
        this.captioningManagerProvider = provider12;
        this.keyguardManagerProvider = provider13;
        this.activityManagerProvider = provider14;
    }

    public VolumeDialogControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.ringerModeTrackerProvider.get(), this.theadFactoryProvider.get(), this.audioManagerProvider.get(), this.notificationManagerProvider.get(), this.vibratorProvider.get(), this.iAudioServiceProvider.get(), this.accessibilityManagerProvider.get(), this.packageManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.captioningManagerProvider.get(), this.keyguardManagerProvider.get(), this.activityManagerProvider.get());
    }

    public static VolumeDialogControllerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<RingerModeTracker> provider3, Provider<ThreadFactory> provider4, Provider<AudioManager> provider5, Provider<NotificationManager> provider6, Provider<VibratorHelper> provider7, Provider<IAudioService> provider8, Provider<AccessibilityManager> provider9, Provider<PackageManager> provider10, Provider<WakefulnessLifecycle> provider11, Provider<CaptioningManager> provider12, Provider<KeyguardManager> provider13, Provider<ActivityManager> provider14) {
        return new VolumeDialogControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static VolumeDialogControllerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher, RingerModeTracker ringerModeTracker, ThreadFactory threadFactory, AudioManager audioManager, NotificationManager notificationManager, VibratorHelper vibratorHelper, IAudioService iAudioService, AccessibilityManager accessibilityManager, PackageManager packageManager, WakefulnessLifecycle wakefulnessLifecycle, CaptioningManager captioningManager, KeyguardManager keyguardManager, ActivityManager activityManager) {
        return new VolumeDialogControllerImpl(context, broadcastDispatcher, ringerModeTracker, threadFactory, audioManager, notificationManager, vibratorHelper, iAudioService, accessibilityManager, packageManager, wakefulnessLifecycle, captioningManager, keyguardManager, activityManager);
    }
}
