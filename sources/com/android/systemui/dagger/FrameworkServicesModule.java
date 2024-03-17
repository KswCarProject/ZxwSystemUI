package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.PowerExemptionManager;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.Prefs;
import com.android.systemui.shared.system.PackageManagerWrapper;
import java.util.Optional;

public class FrameworkServicesModule {
    public static AccessibilityManager provideAccessibilityManager(Context context) {
        return (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
    }

    public static ActivityManager provideActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(ActivityManager.class);
    }

    public static AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(AlarmManager.class);
    }

    public AmbientDisplayConfiguration provideAmbientDisplayConfiguration(Context context) {
        return new AmbientDisplayConfiguration(context);
    }

    public static AudioManager provideAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AudioManager.class);
    }

    public static CaptioningManager provideCaptioningManager(Context context) {
        return (CaptioningManager) context.getSystemService(CaptioningManager.class);
    }

    public Choreographer providesChoreographer() {
        return Choreographer.getInstance();
    }

    public static ColorDisplayManager provideColorDisplayManager(Context context) {
        return (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    public static ConnectivityManager provideConnectivityManagager(Context context) {
        return (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
    }

    public static ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    public static DevicePolicyManager provideDevicePolicyManager(Context context) {
        return (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    public static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return CrossWindowBlurListeners.getInstance();
    }

    public static int provideDisplayId(Context context) {
        return context.getDisplayId();
    }

    public static DisplayManager provideDisplayManager(Context context) {
        return (DisplayManager) context.getSystemService(DisplayManager.class);
    }

    public static DeviceStateManager provideDeviceStateManager(Context context) {
        return (DeviceStateManager) context.getSystemService(DeviceStateManager.class);
    }

    public static IActivityManager provideIActivityManager() {
        return ActivityManager.getService();
    }

    public static ActivityTaskManager provideActivityTaskManager() {
        return ActivityTaskManager.getInstance();
    }

    public static IAudioService provideIAudioService() {
        return IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    }

    public static IBatteryStats provideIBatteryStats() {
        return IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
    }

    public static IDreamManager provideIDreamManager() {
        return IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
    }

    public static FaceManager provideFaceManager(Context context) {
        return (FaceManager) context.getSystemService(FaceManager.class);
    }

    public static FingerprintManager providesFingerprintManager(Context context) {
        return (FingerprintManager) context.getSystemService(FingerprintManager.class);
    }

    public static InteractionJankMonitor provideInteractionJankMonitor() {
        return InteractionJankMonitor.getInstance();
    }

    public static InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(InputMethodManager.class);
    }

    public static IPackageManager provideIPackageManager() {
        return IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    }

    public static IStatusBarService provideIStatusBarService() {
        return IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
    }

    public static IWallpaperManager provideIWallPaperManager() {
        return IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper"));
    }

    public static IWindowManager provideIWindowManager() {
        return WindowManagerGlobal.getWindowManagerService();
    }

    public static KeyguardManager provideKeyguardManager(Context context) {
        return (KeyguardManager) context.getSystemService(KeyguardManager.class);
    }

    public static LatencyTracker provideLatencyTracker(Context context) {
        return LatencyTracker.getInstance(context);
    }

    public static LauncherApps provideLauncherApps(Context context) {
        return (LauncherApps) context.getSystemService(LauncherApps.class);
    }

    public LayoutInflater providerLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }

    public static MediaProjectionManager provideMediaProjectionManager(Context context) {
        return (MediaProjectionManager) context.getSystemService(MediaProjectionManager.class);
    }

    public static MediaRouter2Manager provideMediaRouter2Manager(Context context) {
        return MediaRouter2Manager.getInstance(context);
    }

    public static MediaSessionManager provideMediaSessionManager(Context context) {
        return (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
    }

    public static NetworkScoreManager provideNetworkScoreManager(Context context) {
        return (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class);
    }

    public static NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(NotificationManager.class);
    }

    public INotificationManager provideINotificationManager() {
        return INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    }

    public static PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }

    public static PackageManagerWrapper providePackageManagerWrapper() {
        return PackageManagerWrapper.getInstance();
    }

    public static PowerManager providePowerManager(Context context) {
        return (PowerManager) context.getSystemService(PowerManager.class);
    }

    public static PowerExemptionManager providePowerExemptionManager(Context context) {
        return (PowerExemptionManager) context.getSystemService(PowerExemptionManager.class);
    }

    public SharedPreferences provideSharePreferences(Context context) {
        return Prefs.get(context);
    }

    public static UiModeManager provideUiModeManager(Context context) {
        return (UiModeManager) context.getSystemService(UiModeManager.class);
    }

    public static Resources provideResources(Context context) {
        return context.getResources();
    }

    public static SensorManager providesSensorManager(Context context) {
        return (SensorManager) context.getSystemService(SensorManager.class);
    }

    public static SensorPrivacyManager provideSensorPrivacyManager(Context context) {
        return (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    public static ShortcutManager provideShortcutManager(Context context) {
        return (ShortcutManager) context.getSystemService(ShortcutManager.class);
    }

    public static SubscriptionManager provideSubcriptionManager(Context context) {
        return (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    public static TelecomManager provideTelecomManager(Context context) {
        return (TelecomManager) context.getSystemService(TelecomManager.class);
    }

    public static Optional<TelecomManager> provideOptionalTelecomManager(Context context) {
        return Optional.ofNullable((TelecomManager) context.getSystemService(TelecomManager.class));
    }

    public static TelephonyManager provideTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    public static boolean provideIsTestHarness() {
        return ActivityManager.isRunningInUserTestHarness();
    }

    public static TrustManager provideTrustManager(Context context) {
        return (TrustManager) context.getSystemService(TrustManager.class);
    }

    public static Vibrator provideVibrator(Context context) {
        return (Vibrator) context.getSystemService(Vibrator.class);
    }

    public static Optional<Vibrator> provideOptionalVibrator(Context context) {
        return Optional.ofNullable((Vibrator) context.getSystemService(Vibrator.class));
    }

    public static ViewConfiguration provideViewConfiguration(Context context) {
        return ViewConfiguration.get(context);
    }

    public static UserManager provideUserManager(Context context) {
        return (UserManager) context.getSystemService(UserManager.class);
    }

    public static WallpaperManager provideWallpaperManager(Context context) {
        return (WallpaperManager) context.getSystemService(WallpaperManager.class);
    }

    public static WifiManager provideWifiManager(Context context) {
        return (WifiManager) context.getSystemService(WifiManager.class);
    }

    public static OverlayManager provideOverlayManager(Context context) {
        return (OverlayManager) context.getSystemService(OverlayManager.class);
    }

    public static CarrierConfigManager provideCarrierConfigManager(Context context) {
        return (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public static WindowManager provideWindowManager(Context context) {
        return (WindowManager) context.getSystemService(WindowManager.class);
    }

    public static PermissionManager providePermissionManager(Context context) {
        PermissionManager permissionManager = (PermissionManager) context.getSystemService(PermissionManager.class);
        if (permissionManager != null) {
            permissionManager.initializeUsageHelper();
        }
        return permissionManager;
    }

    public static ClipboardManager provideClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(ClipboardManager.class);
    }

    public static SmartspaceManager provideSmartspaceManager(Context context) {
        return (SmartspaceManager) context.getSystemService(SmartspaceManager.class);
    }

    public static SafetyCenterManager provideSafetyCenterManager(Context context) {
        return (SafetyCenterManager) context.getSystemService(SafetyCenterManager.class);
    }
}
