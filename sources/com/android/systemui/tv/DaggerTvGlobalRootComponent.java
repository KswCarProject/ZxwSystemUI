package com.android.systemui.tv;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.INotificationManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.NightDisplayListener;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerExemptionManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.app.AssistUtils;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.keyguard.ActiveUnlockConfig_Factory;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.AdminSecondaryLockScreenController_Factory_Factory;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardDisplayManager_Factory;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardHostViewController_Factory;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardInputViewController_Factory_Factory;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardMessageAreaController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSecurityModel_Factory;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.keyguard.KeyguardSecurityViewFlipperController;
import com.android.keyguard.KeyguardSecurityViewFlipperController_Factory;
import com.android.keyguard.KeyguardSliceView;
import com.android.keyguard.KeyguardSliceViewController;
import com.android.keyguard.KeyguardSliceViewController_Factory;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.keyguard.KeyguardUnfoldTransition_Factory;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor_Factory;
import com.android.keyguard.LiftToActivateListener_Factory;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.clock.ClockManager;
import com.android.keyguard.clock.ClockManager_Factory;
import com.android.keyguard.clock.ClockModule_ProvideClockInfoListFactory;
import com.android.keyguard.clock.ClockOptionsProvider;
import com.android.keyguard.clock.ClockOptionsProvider_MembersInjector;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardHostViewFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardClockSwitchFactory;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardSliceViewFactory;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.keyguard.mediator.ScreenOnCoordinator_Factory;
import com.android.launcher3.icons.IconProvider;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.ActivityIntentHelper_Factory;
import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.ActivityStarterDelegate_Factory;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.BootCompleteCacheImpl_Factory;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.Dependency_Factory;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.ForegroundServiceController_Factory;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.ForegroundServiceNotificationListener_Factory;
import com.android.systemui.ForegroundServicesDialog;
import com.android.systemui.ForegroundServicesDialog_Factory;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.ImageWallpaper_Factory;
import com.android.systemui.InitController;
import com.android.systemui.InitController_Factory;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.SliceBroadcastRelayHandler_Factory;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.SystemUIAppComponentFactory_MembersInjector;
import com.android.systemui.SystemUIService;
import com.android.systemui.SystemUIService_Factory;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.UiOffloadThread_Factory;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver_Factory;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver_Factory;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.ModeSwitchesController_Factory;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.SystemActions_Factory;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.accessibility.WindowMagnification_Factory;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController_Factory;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.appops.AppOpsControllerImpl;
import com.android.systemui.appops.AppOpsControllerImpl_Factory;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistLogger_Factory;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistManager_Factory;
import com.android.systemui.assist.AssistModule_ProvideAssistUtilsFactory;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.PhoneStateMonitor_Factory;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.assist.ui.DefaultUiController_Factory;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.battery.BatteryMeterViewController_Factory;
import com.android.systemui.biometrics.AlternateUdfpsTouchProvider;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthController_Factory;
import com.android.systemui.biometrics.SidefpsController;
import com.android.systemui.biometrics.SidefpsController_Factory;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsController_Factory;
import com.android.systemui.biometrics.UdfpsHapticsSimulator;
import com.android.systemui.biometrics.UdfpsHapticsSimulator_Factory;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.biometrics.UdfpsShell;
import com.android.systemui.biometrics.UdfpsShell_Factory;
import com.android.systemui.biometrics.dagger.BiometricsModule_ProvidesPluginExecutorFactory;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastDispatcherStartable;
import com.android.systemui.broadcast.BroadcastDispatcherStartable_Factory;
import com.android.systemui.broadcast.BroadcastDispatcher_Factory;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.broadcast.BroadcastSender_Factory;
import com.android.systemui.broadcast.PendingRemovalStore;
import com.android.systemui.broadcast.PendingRemovalStore_Factory;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger_Factory;
import com.android.systemui.classifier.BrightLineFalsingManager;
import com.android.systemui.classifier.BrightLineFalsingManager_Factory;
import com.android.systemui.classifier.DiagonalClassifier_Factory;
import com.android.systemui.classifier.DistanceClassifier_Factory;
import com.android.systemui.classifier.DoubleTapClassifier;
import com.android.systemui.classifier.DoubleTapClassifier_Factory;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollectorImpl_Factory;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.FalsingDataProvider_Factory;
import com.android.systemui.classifier.FalsingManagerProxy;
import com.android.systemui.classifier.FalsingManagerProxy_Factory;
import com.android.systemui.classifier.FalsingModule_ProvidesBrightLineGestureClassifiersFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTimeoutMsFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTouchSlopFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesSingleTapTouchSlopFactory;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.classifier.HistoryTracker_Factory;
import com.android.systemui.classifier.PointerCountClassifier_Factory;
import com.android.systemui.classifier.ProximityClassifier_Factory;
import com.android.systemui.classifier.SingleTapClassifier;
import com.android.systemui.classifier.SingleTapClassifier_Factory;
import com.android.systemui.classifier.TypeClassifier;
import com.android.systemui.classifier.TypeClassifier_Factory;
import com.android.systemui.classifier.ZigZagClassifier_Factory;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.colorextraction.SysuiColorExtractor_Factory;
import com.android.systemui.controls.ControlsMetricsLoggerImpl;
import com.android.systemui.controls.ControlsMetricsLoggerImpl_Factory;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.CustomIconCache_Factory;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.dagger.ControlsComponent_Factory;
import com.android.systemui.controls.dagger.ControlsModule_ProvidesControlsFeatureEnabledFactory;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsEditingActivity_Factory;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity_Factory;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.controls.management.ControlsListingControllerImpl_Factory;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity_Factory;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.controls.management.ControlsRequestDialog_Factory;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl_Factory;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsActivity_Factory;
import com.android.systemui.controls.ui.ControlsUiControllerImpl;
import com.android.systemui.controls.ui.ControlsUiControllerImpl_Factory;
import com.android.systemui.dagger.AndroidInternalsModule;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideLockPatternUtilsFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideMetricsLoggerFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideNotificationMessagingUtilFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideUiEventLoggerFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.ContextComponentResolver;
import com.android.systemui.dagger.ContextComponentResolver_Factory;
import com.android.systemui.dagger.FrameworkServicesModule;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAccessibilityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityTaskManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAlarmManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAudioManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCaptioningManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCarrierConfigManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideColorDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideConnectivityManagagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideContentResolverFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDevicePolicyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDeviceStateManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayIdFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideFaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIAudioServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIDreamManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideINotificationManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIStatusBarServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWallPaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInputMethodManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInteractionJankMonitorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIsTestHarnessFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideKeyguardManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLatencyTrackerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLauncherAppsFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaProjectionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaRouter2ManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaSessionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNetworkScoreManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNotificationManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOptionalTelecomManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOverlayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerWrapperFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePermissionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePowerExemptionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePowerManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideResourcesFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSafetyCenterManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSensorPrivacyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSharePreferencesFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideShortcutManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSmartspaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSubcriptionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelecomManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelephonyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTrustManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideUiModeManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideUserManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideVibratorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideViewConfigurationFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWallpaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWifiManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProviderLayoutInflaterFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesChoreographerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesFingerprintManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesSensorManagerFactory;
import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dagger.GlobalModule_ProvideDisplayMetricsFactory;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.NightDisplayListenerModule_Builder_Factory;
import com.android.systemui.dagger.NightDisplayListenerModule_ProvideNightDisplayListenerFactory;
import com.android.systemui.dagger.PluginModule_ProvideActivityStarterFactory;
import com.android.systemui.dagger.SettingsLibraryModule_ProvideLocalBluetoothControllerFactory;
import com.android.systemui.dagger.SharedLibraryModule;
import com.android.systemui.dagger.SharedLibraryModule_ProvideActivityManagerWrapperFactory;
import com.android.systemui.dagger.SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory;
import com.android.systemui.dagger.SharedLibraryModule_ProvideTaskStackChangeListenersFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideBubblesManagerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideSysUiStateFactory;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.demomode.dagger.DemoModeModule_ProvideDemoModeControllerFactory;
import com.android.systemui.dock.DockManagerImpl;
import com.android.systemui.dock.DockManagerImpl_Factory;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.doze.AlwaysOnDisplayPolicy_Factory;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeAuthRemover_Factory;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeDockHandler_Factory;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeFalsingManagerAdapter_Factory;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeLog_Factory;
import com.android.systemui.doze.DozeLogger;
import com.android.systemui.doze.DozeLogger_Factory;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeMachine_Factory;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozePauser_Factory;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenBrightness_Factory;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenState_Factory;
import com.android.systemui.doze.DozeService;
import com.android.systemui.doze.DozeService_Factory;
import com.android.systemui.doze.DozeSuppressor;
import com.android.systemui.doze.DozeSuppressor_Factory;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeTriggers_Factory;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeUi_Factory;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.doze.DozeWallpaperState_Factory;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.doze.dagger.DozeModule_ProvidesBrightnessSensorsFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeMachinePartsFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeWakeLockFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesWrappedServiceFactory;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.DreamOverlayContainerViewController_Factory;
import com.android.systemui.dreams.DreamOverlayNotificationCountProvider;
import com.android.systemui.dreams.DreamOverlayNotificationCountProvider_Factory;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dreams.DreamOverlayService_Factory;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController_Factory;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController_Factory;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationCollectionLiveData;
import com.android.systemui.dreams.complication.ComplicationCollectionLiveData_Factory;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel_Factory;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.ComplicationHostViewController_Factory;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine_Factory;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;
import com.android.systemui.dreams.complication.ComplicationViewModelTransformer;
import com.android.systemui.dreams.complication.ComplicationViewModelTransformer_Factory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationHostViewFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationPaddingFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationModule_ProvidesComplicationCollectionViewModelFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationModule_ProvidesVisibilityControllerFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayContentViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleOwnerFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleRegistryFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesMaxBurnInOffsetFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesMillisUntilFullJitterFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesTouchInsetManagerFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesTouchInsetSessionFactory;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler_Factory;
import com.android.systemui.dreams.touch.InputSession;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsClosingFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory;
import com.android.systemui.dreams.touch.dagger.HideComplicationModule_ProvidesHideComplicationTouchHandlerFactory;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.DumpHandler_Factory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.DumpManager_Factory;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.dump.LogBufferEulogizer_Factory;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.LogBufferFreezer_Factory;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService_Factory;
import com.android.systemui.flags.FeatureFlagsDebug;
import com.android.systemui.flags.FeatureFlagsDebug_Factory;
import com.android.systemui.flags.FlagManager;
import com.android.systemui.flags.FlagsModule_ProvideFlagManagerFactory;
import com.android.systemui.flags.FlagsModule_ProvidesAllFlagsFactory;
import com.android.systemui.flags.SystemPropertiesHelper;
import com.android.systemui.flags.SystemPropertiesHelper_Factory;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.fragments.FragmentService_Factory;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.globalactions.GlobalActionsComponent_Factory;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.globalactions.GlobalActionsDialogLite_Factory;
import com.android.systemui.globalactions.GlobalActionsImpl;
import com.android.systemui.globalactions.GlobalActionsImpl_Factory;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity_Factory;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper_Factory;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.keyboard.KeyboardUI_Factory;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.DismissCallbackRegistry_Factory;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher_Factory;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardService_Factory;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.KeyguardSliceProvider_MembersInjector;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController_Factory;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider_Factory;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.ScreenLifecycle_Factory;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle_Factory;
import com.android.systemui.keyguard.WorkLockActivity;
import com.android.systemui.keyguard.WorkLockActivity_Factory;
import com.android.systemui.keyguard.dagger.KeyguardModule;
import com.android.systemui.keyguard.dagger.KeyguardModule_NewKeyguardViewMediatorFactory;
import com.android.systemui.keyguard.dagger.KeyguardModule_ProvidesViewMediatorCallbackFactory;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogBufferFactory_Factory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.log.SessionTracker_Factory;
import com.android.systemui.log.dagger.LogModule_ProvideBroadcastDispatcherLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideDozeLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideLSShadeTransitionControllerBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideLogcatEchoTrackerFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaBrowserBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaCarouselControllerBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaMuteAwaitLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaTttReceiverLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaTttSenderLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaViewLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNearbyMediaDevicesLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotifInteractionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationHeadsUpLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationSectionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidePrivacyLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideQSFragmentDisableLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideQuickSettingsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideToastLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidesMediaTimeoutListenerLogBufferFactory;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.KeyguardMediaController_Factory;
import com.android.systemui.media.LocalMediaManagerFactory;
import com.android.systemui.media.LocalMediaManagerFactory_Factory;
import com.android.systemui.media.MediaBrowserFactory;
import com.android.systemui.media.MediaBrowserFactory_Factory;
import com.android.systemui.media.MediaCarouselController;
import com.android.systemui.media.MediaCarouselControllerLogger;
import com.android.systemui.media.MediaCarouselControllerLogger_Factory;
import com.android.systemui.media.MediaCarouselController_Factory;
import com.android.systemui.media.MediaControlPanel;
import com.android.systemui.media.MediaControlPanel_Factory;
import com.android.systemui.media.MediaControllerFactory;
import com.android.systemui.media.MediaControllerFactory_Factory;
import com.android.systemui.media.MediaDataCombineLatest_Factory;
import com.android.systemui.media.MediaDataFilter;
import com.android.systemui.media.MediaDataFilter_Factory;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDataManager_Factory;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.MediaDeviceManager_Factory;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.media.MediaFeatureFlag_Factory;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaFlags_Factory;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHierarchyManager_Factory;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaHostStatesManager_Factory;
import com.android.systemui.media.MediaHost_MediaHostStateHolder_Factory;
import com.android.systemui.media.MediaResumeListener;
import com.android.systemui.media.MediaResumeListener_Factory;
import com.android.systemui.media.MediaSessionBasedFilter;
import com.android.systemui.media.MediaSessionBasedFilter_Factory;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.media.MediaTimeoutListener_Factory;
import com.android.systemui.media.MediaTimeoutLogger;
import com.android.systemui.media.MediaTimeoutLogger_Factory;
import com.android.systemui.media.MediaUiEventLogger;
import com.android.systemui.media.MediaUiEventLogger_Factory;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.MediaViewController_Factory;
import com.android.systemui.media.MediaViewLogger;
import com.android.systemui.media.MediaViewLogger_Factory;
import com.android.systemui.media.ResumeMediaBrowserFactory;
import com.android.systemui.media.ResumeMediaBrowserFactory_Factory;
import com.android.systemui.media.ResumeMediaBrowserLogger;
import com.android.systemui.media.ResumeMediaBrowserLogger_Factory;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.media.RingtonePlayer_Factory;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.SeekBarViewModel_Factory;
import com.android.systemui.media.SmartspaceMediaDataProvider_Factory;
import com.android.systemui.media.dagger.MediaModule_ProvidesKeyguardMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerReceiverFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerSenderFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttCommandLineHelperFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttReceiverLoggerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttSenderLoggerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesNearbyMediaDevicesManagerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQSMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQuickQSMediaHostFactory;
import com.android.systemui.media.dialog.MediaOutputBroadcastDialogFactory;
import com.android.systemui.media.dialog.MediaOutputBroadcastDialogFactory_Factory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory_Factory;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitLogger;
import com.android.systemui.media.muteawait.MediaMuteAwaitLogger_Factory;
import com.android.systemui.media.nearby.NearbyMediaDevicesLogger;
import com.android.systemui.media.nearby.NearbyMediaDevicesLogger_Factory;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager_Factory;
import com.android.systemui.media.systemsounds.HomeSoundEffectController;
import com.android.systemui.media.systemsounds.HomeSoundEffectController_Factory;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper_Factory;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.MediaTttFlags_Factory;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver_Factory;
import com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger_Factory;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender_Factory;
import com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger;
import com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger_Factory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavBarHelper_Factory;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarController_Factory;
import com.android.systemui.navigationbar.NavigationBarFrame;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideEdgeBackGestureHandlerFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideLayoutInflaterFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideNavigationBarFrameFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideNavigationBarviewFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideWindowManagerFactory;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationBarTransitions_Factory;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationBar_Factory;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.NavigationModeController_Factory;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.navigationbar.TaskbarDelegate_Factory;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.DeadZone_Factory;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler_Factory_Factory;
import com.android.systemui.people.PeopleProvider;
import com.android.systemui.people.PeopleProvider_MembersInjector;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.PeopleSpaceActivity_Factory;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.LaunchConversationActivity_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider_Factory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginDependencyProvider_Factory;
import com.android.systemui.plugins.PluginEnablerImpl;
import com.android.systemui.plugins.PluginEnablerImpl_Factory;
import com.android.systemui.plugins.PluginsModule_ProvidePluginInstanceManagerFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginDebugFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginExecutorFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginInstanceFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginManagerFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginPrefsFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPrivilegedPluginsFactory;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimatesImpl;
import com.android.systemui.power.EnhancedEstimatesImpl_Factory;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.power.PowerNotificationWarnings_Factory;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerUI_Factory;
import com.android.systemui.privacy.AppOpsPrivacyItemMonitor;
import com.android.systemui.privacy.AppOpsPrivacyItemMonitor_Factory;
import com.android.systemui.privacy.MediaProjectionPrivacyItemMonitor;
import com.android.systemui.privacy.MediaProjectionPrivacyItemMonitor_Factory;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyConfig_Factory;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyDialogController_Factory;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyItemController_Factory;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.privacy.logging.PrivacyLogger_Factory;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip_Factory;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController_Factory;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.AutoAddTracker_Builder_Factory;
import com.android.systemui.qs.FgsManagerController;
import com.android.systemui.qs.FgsManagerController_Factory;
import com.android.systemui.qs.FooterActionsController;
import com.android.systemui.qs.FooterActionsController_Factory;
import com.android.systemui.qs.FooterActionsView;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.HeaderPrivacyIconsController_Factory;
import com.android.systemui.qs.QSAnimator;
import com.android.systemui.qs.QSAnimator_Factory;
import com.android.systemui.qs.QSContainerImpl;
import com.android.systemui.qs.QSContainerImplController;
import com.android.systemui.qs.QSContainerImplController_Factory;
import com.android.systemui.qs.QSExpansionPathInterpolator;
import com.android.systemui.qs.QSExpansionPathInterpolator_Factory;
import com.android.systemui.qs.QSFgsManagerFooter;
import com.android.systemui.qs.QSFgsManagerFooter_Factory;
import com.android.systemui.qs.QSFooter;
import com.android.systemui.qs.QSFooterView;
import com.android.systemui.qs.QSFooterViewController;
import com.android.systemui.qs.QSFooterViewController_Factory;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSFragmentDisableFlagsLogger;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.qs.QSPanelController_Factory;
import com.android.systemui.qs.QSSecurityFooter_Factory;
import com.android.systemui.qs.QSSquishinessController;
import com.android.systemui.qs.QSSquishinessController_Factory;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.QSTileHost_Factory;
import com.android.systemui.qs.QSTileRevealController_Factory_Factory;
import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickQSPanelController;
import com.android.systemui.qs.QuickQSPanelController_Factory;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.qs.QuickStatusBarHeaderController_Factory;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.ReduceBrightColorsController_Factory;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.qs.carrier.QSCarrierGroupController_Builder_Factory;
import com.android.systemui.qs.carrier.QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.customize.QSCustomizerController_Factory;
import com.android.systemui.qs.customize.TileAdapter;
import com.android.systemui.qs.customize.TileAdapter_Factory;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.customize.TileQueryHelper_Factory;
import com.android.systemui.qs.dagger.QSFlagsModule_IsPMLiteEnabledFactory;
import com.android.systemui.qs.dagger.QSFlagsModule_IsReduceBrightColorsAvailableFactory;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideQSPanelFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideRootViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideThemedContextFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideThemedLayoutInflaterFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesBatteryMeterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesPrivacyChipFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSContainerImplFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSCutomizerFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterActionsViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSSecurityFooterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSUsingMediaPlayerFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickQSPanelFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickStatusBarHeaderFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesStatusIconContainerFactory;
import com.android.systemui.qs.dagger.QSModule_ProvideAutoTileManagerFactory;
import com.android.systemui.qs.external.C0000TileLifecycleManager_Factory;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.CustomTileStatePersister_Factory;
import com.android.systemui.qs.external.CustomTile_Builder_Factory;
import com.android.systemui.qs.external.PackageManagerAdapter;
import com.android.systemui.qs.external.PackageManagerAdapter_Factory;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileLifecycleManager_Factory_Impl;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.external.TileServiceRequestController_Builder_Factory;
import com.android.systemui.qs.external.TileServices;
import com.android.systemui.qs.external.TileServices_Factory;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.logging.QSLogger_Factory;
import com.android.systemui.qs.tileimpl.QSFactoryImpl;
import com.android.systemui.qs.tileimpl.QSFactoryImpl_Factory;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AirplaneModeTile_Factory;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.AlarmTile_Factory;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BatterySaverTile_Factory;
import com.android.systemui.qs.tiles.BlackScreenTile;
import com.android.systemui.qs.tiles.BlackScreenTile_Factory;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.BluetoothTile_Factory;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CameraToggleTile_Factory;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CastTile_Factory;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.CellularTile_Factory;
import com.android.systemui.qs.tiles.ColorCorrectionTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile_Factory;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.ColorInversionTile_Factory;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DataSaverTile_Factory;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DeviceControlsTile_Factory;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.DndTile_Factory;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.FlashlightTile_Factory;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.HotspotTile_Factory;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.InternetTile_Factory;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.LocationTile_Factory;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile_Factory;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NfcTile_Factory;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.NightDisplayTile_Factory;
import com.android.systemui.qs.tiles.OneHandedModeTile;
import com.android.systemui.qs.tiles.OneHandedModeTile_Factory;
import com.android.systemui.qs.tiles.QRCodeScannerTile;
import com.android.systemui.qs.tiles.QRCodeScannerTile_Factory;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile_Factory;
import com.android.systemui.qs.tiles.RebootTile;
import com.android.systemui.qs.tiles.RebootTile_Factory;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile_Factory;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.RotationLockTile_Factory;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.ScreenRecordTile_Factory;
import com.android.systemui.qs.tiles.ScreenShotTile;
import com.android.systemui.qs.tiles.ScreenShotTile_Factory;
import com.android.systemui.qs.tiles.SettingTile;
import com.android.systemui.qs.tiles.SettingTile_Factory;
import com.android.systemui.qs.tiles.SoundTile;
import com.android.systemui.qs.tiles.SoundTile_Factory;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.UiModeNightTile_Factory;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.qs.tiles.UserDetailView_Adapter_Factory;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WifiTile_Factory;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.qs.tiles.WorkModeTile_Factory;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.qs.tiles.dialog.InternetDialogController_Factory;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory_Factory;
import com.android.systemui.qs.tiles.dialog.WifiStateWorker;
import com.android.systemui.qs.tiles.dialog.WifiStateWorker_Factory;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.qs.user.UserSwitchDialogController_Factory;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyRecentsImpl_Factory;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.recents.RecentsModule_ProvideRecentsImplFactory;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.RecordingController_Factory;
import com.android.systemui.screenrecord.RecordingService;
import com.android.systemui.screenrecord.RecordingService_Factory;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.ActionProxyReceiver_Factory;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.DeleteScreenshotReceiver_Factory;
import com.android.systemui.screenshot.ImageExporter_Factory;
import com.android.systemui.screenshot.ImageTileSet_Factory;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotActivity_Factory;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.screenshot.LongScreenshotData_Factory;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotController_Factory;
import com.android.systemui.screenshot.ScreenshotNotificationsController;
import com.android.systemui.screenshot.ScreenshotNotificationsController_Factory;
import com.android.systemui.screenshot.ScreenshotSmartActions;
import com.android.systemui.screenshot.ScreenshotSmartActions_Factory;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureClient_Factory;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.ScrollCaptureController_Factory;
import com.android.systemui.screenshot.SmartActionsReceiver;
import com.android.systemui.screenshot.SmartActionsReceiver_Factory;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.screenshot.TakeScreenshotService_Factory;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.systemui.screenshot.TimeoutHandler_Factory;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity_Factory;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity_Factory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessController_Factory_Factory;
import com.android.systemui.settings.brightness.BrightnessDialog;
import com.android.systemui.settings.brightness.BrightnessDialog_Factory;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.BrightnessSliderController_Factory_Factory;
import com.android.systemui.settings.dagger.SettingsModule_ProvideUserTrackerFactory;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager_Factory;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.shortcut.ShortcutKeyDispatcher_Factory;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.ActionClickLogger_Factory;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.BlurUtils_Factory;
import com.android.systemui.statusbar.C0001LockscreenShadeKeyguardTransitionController_Factory;
import com.android.systemui.statusbar.C0002SingleShadeLockScreenOverScroller_Factory;
import com.android.systemui.statusbar.C0003SplitShadeLockScreenOverScroller_Factory;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.DisableFlagsLogger_Factory;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController_Factory_Impl;
import com.android.systemui.statusbar.LockscreenShadeScrimTransitionController;
import com.android.systemui.statusbar.LockscreenShadeScrimTransitionController_Factory;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController_Factory;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.MediaArtworkProcessor_Factory;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationClickNotifier_Factory;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.NotificationInteractionTracker_Factory;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationListener_Factory;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl_Factory;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder_Factory;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller_Factory_Impl;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller_Factory_Impl;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.StatusBarStateControllerImpl_Factory;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.commandline.CommandRegistry_Factory;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.CallbackHandler_Factory;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory_Factory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideCommandQueueFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationMediaManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideSmartReplyControllerFactory;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.PrivacyDotViewController_Factory;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController_Factory;
import com.android.systemui.statusbar.events.SystemEventCoordinator;
import com.android.systemui.statusbar.events.SystemEventCoordinator_Factory;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler_Factory;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.statusbar.gesture.TapGestureDetector_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController_Factory;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.AssistantFeedbackController_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.ConversationNotificationManager_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor_Factory;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicChildBindController_Factory;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController_Factory;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.notification.InstantAppNotifier_Factory;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotifPipelineFlags_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager_Factory;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger_Factory;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger_Factory;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider_Factory;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController_Factory;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideCommonNotifCollectionFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupExpansionManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupMembershipManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideOnUserInteractionCallbackFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconBuilder_Factory;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconManager_Factory;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProviderImpl_Factory;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger_Factory;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationLogger_ExpansionStateLogger_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary_Factory;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl_Factory;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog_Builder_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableViewController;
import com.android.systemui.statusbar.notification.row.ExpandableViewController_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline_Factory;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager_Factory;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCache;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl_Factory;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater_Factory;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger_Factory;
import com.android.systemui.statusbar.notification.row.RowContentBindStage_Factory;
import com.android.systemui.statusbar.notification.row.RowInflaterTask_Factory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoHideController_Factory_Factory;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.C0004AutoHideController_Factory;
import com.android.systemui.statusbar.phone.C0005LightBarController_Factory;
import com.android.systemui.statusbar.phone.C0006LightBarTransitionsController_Factory;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl_Factory;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl_Factory;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeParameters_Factory;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeScrimController_Factory;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.DozeServiceHost_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardBypassController_Factory;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl_Factory;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger_Factory;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory_Factory;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController_Factory_Impl;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger_Factory;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl_Factory;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper_Factory;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController_Factory;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_PanelEventsEmitter_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager_Factory;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager_Factory;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityController_Factory;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper_Factory;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.android.systemui.statusbar.policy.BatteryStateNotifier_Factory;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl_Factory;
import com.android.systemui.statusbar.policy.CastControllerImpl;
import com.android.systemui.statusbar.policy.CastControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController_Factory;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl_Factory;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl_Factory;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger_Factory;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.statusbar.policy.HotspotControllerImpl_Factory;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl_Factory;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl_Factory;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl_Factory;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler_Factory;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputUriController_Factory;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.SafetyController_Factory;
import com.android.systemui.statusbar.policy.SecurityControllerImpl;
import com.android.systemui.statusbar.policy.SecurityControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyConstants_Factory;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl_Factory;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.UserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.statusbar.policy.VariableDateViewController_Factory_Factory;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.statusbar.policy.WalletControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl_Factory;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAccessPointControllerImplFactory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideDataSaverControllerFactory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory;
import com.android.systemui.statusbar.tv.TvStatusBar;
import com.android.systemui.statusbar.tv.TvStatusBar_Factory;
import com.android.systemui.statusbar.tv.VpnStatusObserver;
import com.android.systemui.statusbar.tv.VpnStatusObserver_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanel;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanel_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowController_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowModule_ProvidesStatusBarWindowViewFactory;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowView;
import com.android.systemui.telephony.TelephonyCallback_Factory;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.telephony.TelephonyListenerManager_Factory;
import com.android.systemui.theme.ThemeModule_ProvideLauncherPackageFactory;
import com.android.systemui.theme.ThemeModule_ProvideThemePickerPackageFactory;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayApplier_Factory;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.theme.ThemeOverlayController_Factory;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.toast.ToastFactory_Factory;
import com.android.systemui.toast.ToastLogger;
import com.android.systemui.toast.ToastLogger_Factory;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.toast.ToastUI_Factory;
import com.android.systemui.touch.TouchInsetManager;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.ProtoTracer_Factory;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunablePadding_TunablePaddingService_Factory;
import com.android.systemui.tuner.TunerActivity;
import com.android.systemui.tuner.TunerActivity_Factory;
import com.android.systemui.tuner.TunerServiceImpl;
import com.android.systemui.tuner.TunerServiceImpl_Factory;
import com.android.systemui.tv.TvGlobalRootComponent;
import com.android.systemui.tv.TvSysUIComponent;
import com.android.systemui.tv.TvWMComponent;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.FoldAodAnimationController_Factory;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.SysUIUnfoldModule;
import com.android.systemui.unfold.SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.UnfoldLatencyTracker_Factory;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation_Factory;
import com.android.systemui.unfold.UnfoldSharedModule;
import com.android.systemui.unfold.UnfoldSharedModule_HingeAngleProviderFactory;
import com.android.systemui.unfold.UnfoldSharedModule_ProvideFoldStateProviderFactory;
import com.android.systemui.unfold.UnfoldSharedModule_UnfoldTransitionProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideNaturalRotationProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideShellProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideStatusBarScopedTransitionProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggerFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ScreenStatusProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_TracingTagPrefixFactory;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController_Factory;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory;
import com.android.systemui.unfold.util.C0008ScaleAwareTransitionProgressProvider_Factory;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.usb.StorageNotification;
import com.android.systemui.usb.StorageNotification_Factory;
import com.android.systemui.usb.UsbAudioWarningDialogMessage_Factory;
import com.android.systemui.usb.UsbConfirmActivity;
import com.android.systemui.usb.UsbConfirmActivity_Factory;
import com.android.systemui.usb.UsbDebuggingActivity;
import com.android.systemui.usb.UsbDebuggingActivity_Factory;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity_Factory;
import com.android.systemui.usb.UsbPermissionActivity;
import com.android.systemui.usb.UsbPermissionActivity_Factory;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.user.CreateUserActivity_Factory;
import com.android.systemui.user.UserCreator;
import com.android.systemui.user.UserCreator_Factory;
import com.android.systemui.user.UserModule_ProvideEditUserInfoControllerFactory;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.user.UserSwitcherActivity_Factory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierConfigTracker_Factory;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DeviceConfigProxy_Factory;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.util.NotificationChannels_Factory;
import com.android.systemui.util.RingerModeTrackerImpl;
import com.android.systemui.util.RingerModeTrackerImpl_Factory;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.WallpaperController_Factory;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.ExecutionImpl_Factory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideHandlerFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainExecutorFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainHandlerFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgHandlerFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideTimeTickHandlerFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.io.Files_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_MemoryTile_Factory;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakModule;
import com.android.systemui.util.leak.LeakModule_ProvidesLeakDetectorFactory;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.leak.LeakReporter_Factory;
import com.android.systemui.util.leak.TrackedCollections_Factory;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.AsyncSensorManager_Factory;
import com.android.systemui.util.sensors.PostureDependentProximitySensor_Factory;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ProximitySensorImpl_Factory;
import com.android.systemui.util.sensors.SensorModule_ProvidePostureToProximitySensorMappingFactory;
import com.android.systemui.util.sensors.SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory;
import com.android.systemui.util.sensors.SensorModule_ProvidePrimaryProximitySensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideProximityCheckFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideProximitySensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideSecondaryProximitySensorFactory;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import com.android.systemui.util.sensors.ThresholdSensorImpl_BuilderFactory_Factory;
import com.android.systemui.util.sensors.ThresholdSensorImpl_Builder_Factory;
import com.android.systemui.util.settings.GlobalSettingsImpl_Factory;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.systemui.util.time.DateFormatUtil_Factory;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.util.time.SystemClockImpl_Factory;
import com.android.systemui.util.view.ViewUtil;
import com.android.systemui.util.view.ViewUtil_Factory;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.DelayedWakeLock_Builder_Factory;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.util.wakelock.WakeLock_Builder_Factory;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl_Factory;
import com.android.systemui.volume.VolumeDialogComponent;
import com.android.systemui.volume.VolumeDialogComponent_Factory;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl_Factory;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.volume.VolumeUI_Factory;
import com.android.systemui.volume.dagger.VolumeModule_ProvideVolumeDialogFactory;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.controller.QuickAccessWalletController_Factory;
import com.android.systemui.wallet.dagger.WalletModule_ProvideQuickAccessWalletClientFactory;
import com.android.systemui.wallet.ui.WalletActivity;
import com.android.systemui.wallet.ui.WalletActivity_Factory;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.WMShell;
import com.android.systemui.wmshell.WMShell_Factory;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.android.wm.shell.animation.FlingAnimationUtils_Builder_Factory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipAnimationControllerFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipAppOpsListenerFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipParamsChangedForwarderFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipSnapAlgorithmFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipTaskOrganizerFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidePipTransitionStateFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsAlgorithmFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsControllerFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsStateFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipNotificationControllerFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipTransitionFactory;
import com.android.wm.shell.dagger.TvPipModule_ProvidesTvPipMenuControllerFactory;
import com.android.wm.shell.dagger.TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideAppPairsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideBubblesFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideCompatUIControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideCompatUIFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayAreaHelperFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayImeControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayInsetsControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFreeformTaskListenerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFullscreenTaskListenerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideHideDisplayCutoutFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideIconProviderFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideLegacySplitScreenFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideOneHandedFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipMediaControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipUiEventLoggerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRemoteTransitionsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerImplFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellInitFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellInitImplFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellTaskOrganizerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSplitScreenFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingSurfaceFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSyncTransactionQueueFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSystemWindowsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewTransitionsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTransactionPoolFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTransitionsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideWindowManagerShellWrapperFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProviderTaskStackListenerImplFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidesOneHandedControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidesSplitScreenControllerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldTransitionHandler;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import dagger.internal.MapBuilder;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.SetBuilder;
import dagger.internal.SetFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DaggerTvGlobalRootComponent implements TvGlobalRootComponent {
    public static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());
    public Provider<ATraceLoggerTransitionProgressListener> aTraceLoggerTransitionProgressListenerProvider;
    public final Context context;
    public Provider<Context> contextProvider;
    public Provider<DeviceFoldStateProvider> deviceFoldStateProvider;
    public Provider<DumpManager> dumpManagerProvider;
    public Provider<ScaleAwareTransitionProgressProvider.Factory> factoryProvider;
    public final GlobalModule globalModule;
    public Provider<HingeAngleProvider> hingeAngleProvider;
    public Provider<LifecycleScreenStatusProvider> lifecycleScreenStatusProvider;
    public Provider<PluginDependencyProvider> pluginDependencyProvider;
    public Provider<PluginEnablerImpl> pluginEnablerImplProvider;
    public Provider<AccessibilityManager> provideAccessibilityManagerProvider;
    public Provider<ActivityManager> provideActivityManagerProvider;
    public Provider<ActivityTaskManager> provideActivityTaskManagerProvider;
    public Provider<AlarmManager> provideAlarmManagerProvider;
    public Provider<AmbientDisplayConfiguration> provideAmbientDisplayConfigurationProvider;
    public Provider<AudioManager> provideAudioManagerProvider;
    public Provider<CaptioningManager> provideCaptioningManagerProvider;
    public Provider<CarrierConfigManager> provideCarrierConfigManagerProvider;
    public Provider<ColorDisplayManager> provideColorDisplayManagerProvider;
    public Provider<ConnectivityManager> provideConnectivityManagagerProvider;
    public Provider<ContentResolver> provideContentResolverProvider;
    public Provider<CrossWindowBlurListeners> provideCrossWindowBlurListenersProvider;
    public Provider<DevicePolicyManager> provideDevicePolicyManagerProvider;
    public Provider<DeviceStateManager> provideDeviceStateManagerProvider;
    public Provider<Integer> provideDisplayIdProvider;
    public Provider<DisplayManager> provideDisplayManagerProvider;
    public Provider<DisplayMetrics> provideDisplayMetricsProvider;
    public Provider<Execution> provideExecutionProvider;
    public Provider<FaceManager> provideFaceManagerProvider;
    public Provider<FoldStateProvider> provideFoldStateProvider;
    public Provider<IActivityManager> provideIActivityManagerProvider;
    public Provider<IAudioService> provideIAudioServiceProvider;
    public Provider<IDreamManager> provideIDreamManagerProvider;
    public Provider<INotificationManager> provideINotificationManagerProvider;
    public Provider<IStatusBarService> provideIStatusBarServiceProvider;
    public Provider<IWindowManager> provideIWindowManagerProvider;
    public Provider<InputMethodManager> provideInputMethodManagerProvider;
    public Provider<InteractionJankMonitor> provideInteractionJankMonitorProvider;
    public Provider<Boolean> provideIsTestHarnessProvider;
    public Provider<KeyguardManager> provideKeyguardManagerProvider;
    public Provider<LatencyTracker> provideLatencyTrackerProvider;
    public Provider<LauncherApps> provideLauncherAppsProvider;
    public Provider<LockPatternUtils> provideLockPatternUtilsProvider;
    public Provider<DelayableExecutor> provideMainDelayableExecutorProvider;
    public Provider<Executor> provideMainExecutorProvider;
    public Provider<Handler> provideMainHandlerProvider;
    public Provider<MediaProjectionManager> provideMediaProjectionManagerProvider;
    public Provider<MediaRouter2Manager> provideMediaRouter2ManagerProvider;
    public Provider<MediaSessionManager> provideMediaSessionManagerProvider;
    public Provider<MetricsLogger> provideMetricsLoggerProvider;
    public Provider<Optional<NaturalRotationUnfoldProgressProvider>> provideNaturalRotationProgressProvider;
    public Provider<NetworkScoreManager> provideNetworkScoreManagerProvider;
    public Provider<NotificationManager> provideNotificationManagerProvider;
    public Provider<NotificationMessagingUtil> provideNotificationMessagingUtilProvider;
    public Provider<Optional<TelecomManager>> provideOptionalTelecomManagerProvider;
    public Provider<OverlayManager> provideOverlayManagerProvider;
    public Provider<PackageManager> providePackageManagerProvider;
    public Provider<PackageManagerWrapper> providePackageManagerWrapperProvider;
    public Provider<PermissionManager> providePermissionManagerProvider;
    public Provider<PluginActionManager.Factory> providePluginInstanceManagerFactoryProvider;
    public Provider<PowerExemptionManager> providePowerExemptionManagerProvider;
    public Provider<PowerManager> providePowerManagerProvider;
    public Provider<Resources> provideResourcesProvider;
    public Provider<SafetyCenterManager> provideSafetyCenterManagerProvider;
    public Provider<SensorPrivacyManager> provideSensorPrivacyManagerProvider;
    public Provider<SharedPreferences> provideSharePreferencesProvider;
    public Provider<ShellUnfoldProgressProvider> provideShellProgressProvider;
    public Provider<ShortcutManager> provideShortcutManagerProvider;
    public Provider<SmartspaceManager> provideSmartspaceManagerProvider;
    public Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provideStatusBarScopedTransitionProvider;
    public Provider<SubscriptionManager> provideSubcriptionManagerProvider;
    public Provider<TelecomManager> provideTelecomManagerProvider;
    public Provider<TelephonyManager> provideTelephonyManagerProvider;
    public Provider<TrustManager> provideTrustManagerProvider;
    public Provider<Executor> provideUiBackgroundExecutorProvider;
    public Provider<UiEventLogger> provideUiEventLoggerProvider;
    public Provider<UiModeManager> provideUiModeManagerProvider;
    public Provider<UnfoldTransitionConfig> provideUnfoldTransitionConfigProvider;
    public Provider<UserManager> provideUserManagerProvider;
    public Provider<Vibrator> provideVibratorProvider;
    public Provider<ViewConfiguration> provideViewConfigurationProvider;
    public Provider<WallpaperManager> provideWallpaperManagerProvider;
    public Provider<WifiManager> provideWifiManagerProvider;
    public Provider<WindowManager> provideWindowManagerProvider;
    public Provider<LayoutInflater> providerLayoutInflaterProvider;
    public Provider<Choreographer> providesChoreographerProvider;
    public Provider<FingerprintManager> providesFingerprintManagerProvider;
    public Provider<Optional<FoldStateLogger>> providesFoldStateLoggerProvider;
    public Provider<Optional<FoldStateLoggingProvider>> providesFoldStateLoggingProvider;
    public Provider<Executor> providesPluginExecutorProvider;
    public Provider<PluginInstance.Factory> providesPluginInstanceFactoryProvider;
    public Provider<PluginManager> providesPluginManagerProvider;
    public Provider<PluginPrefs> providesPluginPrefsProvider;
    public Provider<List<String>> providesPrivilegedPluginsProvider;
    public Provider<SensorManager> providesSensorManagerProvider;
    public Provider<QSExpansionPathInterpolator> qSExpansionPathInterpolatorProvider;
    public C0008ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider;
    public Provider<ScreenLifecycle> screenLifecycleProvider;
    public Provider<ScreenStatusProvider> screenStatusProvider;
    public Provider<String> tracingTagPrefixProvider;
    public Provider<UncaughtExceptionPreHandlerManager> uncaughtExceptionPreHandlerManagerProvider;
    public Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

    public DaggerTvGlobalRootComponent(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.context = context2;
        this.globalModule = globalModule2;
        initialize(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
        initialize2(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
    }

    public static TvGlobalRootComponent.Builder builder() {
        return new Builder();
    }

    public final Resources mainResources() {
        return FrameworkServicesModule_ProvideResourcesFactory.provideResources(this.context);
    }

    public final DisplayMetrics displayMetrics() {
        return GlobalModule_ProvideDisplayMetricsFactory.provideDisplayMetrics(this.globalModule, this.context);
    }

    public final Handler mainHandler() {
        return GlobalConcurrencyModule_ProvideMainHandlerFactory.provideMainHandler(GlobalConcurrencyModule_ProvideMainLooperFactory.provideMainLooper());
    }

    public final void initialize(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.contextProvider = InstanceFactory.create(context2);
        this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.create());
        this.provideUiEventLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideUiEventLoggerFactory.create());
        this.provideUnfoldTransitionConfigProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory.create(unfoldTransitionModule, this.contextProvider));
        Provider<ContentResolver> provider = DoubleCheck.provider(FrameworkServicesModule_ProvideContentResolverFactory.create(this.contextProvider));
        this.provideContentResolverProvider = provider;
        C0008ScaleAwareTransitionProgressProvider_Factory create = C0008ScaleAwareTransitionProgressProvider_Factory.create(provider);
        this.scaleAwareTransitionProgressProvider = create;
        this.factoryProvider = ScaleAwareTransitionProgressProvider_Factory_Impl.create(create);
        UnfoldTransitionModule_TracingTagPrefixFactory create2 = UnfoldTransitionModule_TracingTagPrefixFactory.create(unfoldTransitionModule);
        this.tracingTagPrefixProvider = create2;
        this.aTraceLoggerTransitionProgressListenerProvider = ATraceLoggerTransitionProgressListener_Factory.create(create2);
        this.providesSensorManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesSensorManagerFactory.create(this.contextProvider));
        Provider<Executor> provider2 = DoubleCheck.provider(GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory.create());
        this.provideUiBackgroundExecutorProvider = provider2;
        this.hingeAngleProvider = UnfoldSharedModule_HingeAngleProviderFactory.create(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, this.providesSensorManagerProvider, provider2);
        Provider<DumpManager> provider3 = DoubleCheck.provider(DumpManager_Factory.create());
        this.dumpManagerProvider = provider3;
        Provider<ScreenLifecycle> provider4 = DoubleCheck.provider(ScreenLifecycle_Factory.create(provider3));
        this.screenLifecycleProvider = provider4;
        Provider<LifecycleScreenStatusProvider> provider5 = DoubleCheck.provider(LifecycleScreenStatusProvider_Factory.create(provider4));
        this.lifecycleScreenStatusProvider = provider5;
        this.screenStatusProvider = UnfoldTransitionModule_ScreenStatusProviderFactory.create(unfoldTransitionModule, provider5);
        this.provideDeviceStateManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDeviceStateManagerFactory.create(this.contextProvider));
        this.provideActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityManagerFactory.create(this.contextProvider));
        this.provideMainExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainExecutorFactory.create(this.contextProvider));
        GlobalConcurrencyModule_ProvideMainHandlerFactory create3 = GlobalConcurrencyModule_ProvideMainHandlerFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
        this.provideMainHandlerProvider = create3;
        DeviceFoldStateProvider_Factory create4 = DeviceFoldStateProvider_Factory.create(this.contextProvider, this.hingeAngleProvider, this.screenStatusProvider, this.provideDeviceStateManagerProvider, this.provideActivityManagerProvider, this.provideMainExecutorProvider, create3);
        this.deviceFoldStateProvider = create4;
        Provider<FoldStateProvider> provider6 = DoubleCheck.provider(UnfoldSharedModule_ProvideFoldStateProviderFactory.create(unfoldSharedModule, create4));
        this.provideFoldStateProvider = provider6;
        Provider<Optional<UnfoldTransitionProgressProvider>> provider7 = DoubleCheck.provider(UnfoldSharedModule_UnfoldTransitionProgressProviderFactory.create(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, this.factoryProvider, this.aTraceLoggerTransitionProgressListenerProvider, provider6));
        this.unfoldTransitionProgressProvider = provider7;
        this.provideShellProgressProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideShellProgressProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, provider7));
        this.providePackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerFactory.create(this.contextProvider));
        this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
        this.provideMetricsLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideMetricsLoggerFactory.create(androidInternalsModule));
        this.providesPluginExecutorProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
        this.provideNotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNotificationManagerFactory.create(this.contextProvider));
        this.pluginEnablerImplProvider = DoubleCheck.provider(PluginEnablerImpl_Factory.create(this.contextProvider, this.providePackageManagerProvider));
        PluginsModule_ProvidesPrivilegedPluginsFactory create5 = PluginsModule_ProvidesPrivilegedPluginsFactory.create(this.contextProvider);
        this.providesPrivilegedPluginsProvider = create5;
        Provider<PluginInstance.Factory> provider8 = DoubleCheck.provider(PluginsModule_ProvidesPluginInstanceFactoryFactory.create(create5, PluginsModule_ProvidesPluginDebugFactory.create()));
        this.providesPluginInstanceFactoryProvider = provider8;
        this.providePluginInstanceManagerFactoryProvider = DoubleCheck.provider(PluginsModule_ProvidePluginInstanceManagerFactoryFactory.create(this.contextProvider, this.providePackageManagerProvider, this.provideMainExecutorProvider, this.providesPluginExecutorProvider, this.provideNotificationManagerProvider, this.pluginEnablerImplProvider, this.providesPrivilegedPluginsProvider, provider8));
        this.uncaughtExceptionPreHandlerManagerProvider = DoubleCheck.provider(UncaughtExceptionPreHandlerManager_Factory.create());
        this.providesPluginPrefsProvider = PluginsModule_ProvidesPluginPrefsFactory.create(this.contextProvider);
        this.providesPluginManagerProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginManagerFactory.create(this.contextProvider, this.providePluginInstanceManagerFactoryProvider, PluginsModule_ProvidesPluginDebugFactory.create(), this.uncaughtExceptionPreHandlerManagerProvider, this.pluginEnablerImplProvider, this.providesPluginPrefsProvider, this.providesPrivilegedPluginsProvider));
        this.provideDisplayMetricsProvider = GlobalModule_ProvideDisplayMetricsFactory.create(globalModule2, this.contextProvider);
        this.providePowerManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerManagerFactory.create(this.contextProvider));
        this.provideViewConfigurationProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideViewConfigurationFactory.create(this.contextProvider));
        this.provideResourcesProvider = FrameworkServicesModule_ProvideResourcesFactory.create(this.contextProvider);
        this.provideInteractionJankMonitorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInteractionJankMonitorFactory.create());
        this.provideLockPatternUtilsProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideLockPatternUtilsFactory.create(androidInternalsModule, this.contextProvider));
        this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
        this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
        this.provideWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWindowManagerFactory.create(this.contextProvider));
        this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
        this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
        this.providerLayoutInflaterProvider = DoubleCheck.provider(FrameworkServicesModule_ProviderLayoutInflaterFactory.create(frameworkServicesModule, this.contextProvider));
        this.provideMainDelayableExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
        this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
        this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
        this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
        this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providesPluginManagerProvider));
        this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
        this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
        this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
        this.provideAmbientDisplayConfigurationProvider = FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory.create(frameworkServicesModule, this.contextProvider);
        this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.create());
        Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider9 = DoubleCheck.provider(UnfoldTransitionModule_ProvideNaturalRotationProgressProviderFactory.create(unfoldTransitionModule, this.contextProvider, this.provideIWindowManagerProvider, this.unfoldTransitionProgressProvider));
        this.provideNaturalRotationProgressProvider = provider9;
        this.provideStatusBarScopedTransitionProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideStatusBarScopedTransitionProviderFactory.create(unfoldTransitionModule, provider9));
        this.providesChoreographerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesChoreographerFactory.create(frameworkServicesModule));
        this.provideNotificationMessagingUtilProvider = AndroidInternalsModule_ProvideNotificationMessagingUtilFactory.create(androidInternalsModule, this.contextProvider);
        this.provideLauncherAppsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLauncherAppsFactory.create(this.contextProvider));
        this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
        this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
        this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
        this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
        this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
        this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
        this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
        this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
        this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
        this.providePowerExemptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerExemptionManagerFactory.create(this.contextProvider));
        this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
        this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
        this.provideIsTestHarnessProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIsTestHarnessFactory.create());
        this.provideINotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideINotificationManagerFactory.create(frameworkServicesModule));
        this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
        this.provideSharePreferencesProvider = FrameworkServicesModule_ProvideSharePreferencesFactory.create(frameworkServicesModule, this.contextProvider);
        this.provideTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelecomManagerFactory.create(this.contextProvider));
        this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
        this.provideMediaProjectionManagerProvider = FrameworkServicesModule_ProvideMediaProjectionManagerFactory.create(this.contextProvider);
        this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.create());
        this.provideCaptioningManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCaptioningManagerFactory.create(this.contextProvider));
        Provider<Optional<FoldStateLoggingProvider>> provider10 = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, this.provideFoldStateProvider));
        this.providesFoldStateLoggingProvider = provider10;
        this.providesFoldStateLoggerProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggerFactory.create(unfoldTransitionModule, provider10));
        this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
        this.provideSubcriptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSubcriptionManagerFactory.create(this.contextProvider));
        this.provideConnectivityManagagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideConnectivityManagagerFactory.create(this.contextProvider));
        this.provideWifiManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWifiManagerFactory.create(this.contextProvider));
        this.provideCarrierConfigManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCarrierConfigManagerFactory.create(this.contextProvider));
        this.provideNetworkScoreManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNetworkScoreManagerFactory.create(this.contextProvider));
        this.provideShortcutManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideShortcutManagerFactory.create(this.contextProvider));
        this.provideOptionalTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalTelecomManagerFactory.create(this.contextProvider));
        this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
        this.provideSmartspaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSmartspaceManagerFactory.create(this.contextProvider));
        this.provideUiModeManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUiModeManagerFactory.create(this.contextProvider));
        this.provideDisplayIdProvider = FrameworkServicesModule_ProvideDisplayIdFactory.create(this.contextProvider);
        this.provideSafetyCenterManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSafetyCenterManagerFactory.create(this.contextProvider));
        this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
    }

    public final void initialize2(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
    }

    public TvWMComponent.Builder getWMComponentBuilder() {
        return new TvWMComponentBuilder();
    }

    public TvSysUIComponent.Builder getSysUIComponent() {
        return new TvSysUIComponentBuilder();
    }

    public static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
        return ABSENT_JDK_OPTIONAL_PROVIDER;
    }

    public static final class PresentJdkOptionalInstanceProvider<T> implements Provider<Optional<T>> {
        public final Provider<T> delegate;

        public PresentJdkOptionalInstanceProvider(Provider<T> provider) {
            this.delegate = (Provider) Preconditions.checkNotNull(provider);
        }

        public Optional<T> get() {
            return Optional.of(this.delegate.get());
        }

        public static <T> Provider<Optional<T>> of(Provider<T> provider) {
            return new PresentJdkOptionalInstanceProvider(provider);
        }
    }

    public static final class Builder implements TvGlobalRootComponent.Builder {
        public Context context;

        public Builder() {
        }

        public Builder context(Context context2) {
            this.context = (Context) Preconditions.checkNotNull(context2);
            return this;
        }

        public TvGlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new DaggerTvGlobalRootComponent(new GlobalModule(), new AndroidInternalsModule(), new FrameworkServicesModule(), new UnfoldTransitionModule(), new UnfoldSharedModule(), this.context);
        }
    }

    public final class TvWMComponentBuilder implements TvWMComponent.Builder {
        public HandlerThread setShellMainThread;

        public TvWMComponentBuilder() {
        }

        public TvWMComponentBuilder setShellMainThread(HandlerThread handlerThread) {
            this.setShellMainThread = handlerThread;
            return this;
        }

        public TvWMComponent build() {
            return new TvWMComponentImpl(this.setShellMainThread);
        }
    }

    public final class TvWMComponentImpl implements TvWMComponent {
        public Provider<Optional<DisplayImeController>> dynamicOverrideOptionalOfDisplayImeControllerProvider;
        public Provider<Optional<FreeformTaskListener>> dynamicOverrideOptionalOfFreeformTaskListenerProvider;
        public Provider<Optional<FullscreenTaskListener>> dynamicOverrideOptionalOfFullscreenTaskListenerProvider;
        public Provider<Optional<FullscreenUnfoldController>> dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider;
        public Provider<Optional<OneHandedController>> dynamicOverrideOptionalOfOneHandedControllerProvider;
        public Provider<Optional<SplitScreenController>> dynamicOverrideOptionalOfSplitScreenControllerProvider;
        public Provider<Optional<StartingWindowTypeAlgorithm>> dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider;
        public Provider<Optional<AppPairsController>> optionalOfAppPairsControllerProvider;
        public Provider<Optional<BubbleController>> optionalOfBubbleControllerProvider;
        public Provider<Optional<LegacySplitScreenController>> optionalOfLegacySplitScreenControllerProvider;
        public Provider<Optional<PipTouchHandler>> optionalOfPipTouchHandlerProvider;
        public Provider<Optional<ShellUnfoldProgressProvider>> optionalOfShellUnfoldProgressProvider;
        public Provider<Optional<AppPairs>> provideAppPairsProvider;
        public Provider<Optional<BackAnimationController>> provideBackAnimationControllerProvider;
        public Provider<Optional<BackAnimation>> provideBackAnimationProvider;
        public Provider<Optional<Bubbles>> provideBubblesProvider;
        public Provider<CompatUIController> provideCompatUIControllerProvider;
        public Provider<Optional<CompatUI>> provideCompatUIProvider;
        public Provider<Optional<DisplayAreaHelper>> provideDisplayAreaHelperProvider;
        public Provider<DisplayController> provideDisplayControllerProvider;
        public Provider<DisplayImeController> provideDisplayImeControllerProvider;
        public Provider<DisplayInsetsController> provideDisplayInsetsControllerProvider;
        public Provider<DragAndDropController> provideDragAndDropControllerProvider;
        public Provider<Optional<DragAndDrop>> provideDragAndDropProvider;
        public Provider<Optional<FreeformTaskListener>> provideFreeformTaskListenerProvider;
        public Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        public Provider<Optional<FullscreenUnfoldController>> provideFullscreenUnfoldControllerProvider;
        public Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        public Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        public Provider<IconProvider> provideIconProvider;
        public Provider<KidsModeTaskOrganizer> provideKidsModeTaskOrganizerProvider;
        public Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider;
        public Provider<Optional<OneHanded>> provideOneHandedProvider;
        public Provider<PipAnimationController> providePipAnimationControllerProvider;
        public Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        public Provider<PipMediaController> providePipMediaControllerProvider;
        public Provider<PipParamsChangedForwarder> providePipParamsChangedForwarderProvider;
        public Provider<Optional<Pip>> providePipProvider;
        public Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        public Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        public Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        public Provider<PipTransitionState> providePipTransitionStateProvider;
        public Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        public Provider<Optional<RecentTasksController>> provideRecentTasksControllerProvider;
        public Provider<Optional<RecentTasks>> provideRecentTasksProvider;
        public Provider<ShellTransitions> provideRemoteTransitionsProvider;
        public Provider<RootDisplayAreaOrganizer> provideRootDisplayAreaOrganizerProvider;
        public Provider<Handler> provideSharedBackgroundHandlerProvider;
        public Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        public Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        public Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        public Provider<ShellInitImpl> provideShellInitImplProvider;
        public Provider<ShellInit> provideShellInitProvider;
        public Provider<ShellExecutor> provideShellMainExecutorProvider;
        public Provider<Handler> provideShellMainHandlerProvider;
        public Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        public Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        public Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        public Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        public Provider<StartingWindowController> provideStartingWindowControllerProvider;
        public Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
        public Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider2;
        public Provider<SyncTransactionQueue> provideSyncTransactionQueueProvider;
        public Provider<ShellExecutor> provideSysUIMainExecutorProvider;
        public Provider<SystemWindows> provideSystemWindowsProvider;
        public Provider<Optional<TaskSurfaceHelperController>> provideTaskSurfaceHelperControllerProvider;
        public Provider<Optional<TaskSurfaceHelper>> provideTaskSurfaceHelperProvider;
        public Provider<TaskViewFactoryController> provideTaskViewFactoryControllerProvider;
        public Provider<Optional<TaskViewFactory>> provideTaskViewFactoryProvider;
        public Provider<TaskViewTransitions> provideTaskViewTransitionsProvider;
        public Provider<TransactionPool> provideTransactionPoolProvider;
        public Provider<Transitions> provideTransitionsProvider;
        public Provider<TvPipBoundsAlgorithm> provideTvPipBoundsAlgorithmProvider;
        public Provider<TvPipBoundsController> provideTvPipBoundsControllerProvider;
        public Provider<TvPipBoundsState> provideTvPipBoundsStateProvider;
        public Provider<TvPipNotificationController> provideTvPipNotificationControllerProvider;
        public Provider<PipTransitionController> provideTvPipTransitionProvider;
        public Provider<Optional<UnfoldTransitionHandler>> provideUnfoldTransitionHandlerProvider;
        public Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        public Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        public Provider<Optional<OneHandedController>> providesOneHandedControllerProvider;
        public Provider<Optional<SplitScreenController>> providesSplitScreenControllerProvider;
        public Provider<TvPipMenuController> providesTvPipMenuControllerProvider;
        public Provider<HandlerThread> setShellMainThreadProvider;

        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        public TvWMComponentImpl(HandlerThread handlerThread) {
            initialize(handlerThread);
        }

        public final void initialize(HandlerThread handlerThread) {
            this.setShellMainThreadProvider = InstanceFactory.createNullable(handlerThread);
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.setShellMainThreadProvider, WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.dynamicOverrideOptionalOfDisplayImeControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.provideDisplayInsetsControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayInsetsControllerFactory.create(DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayImeControllerFactory.create(this.dynamicOverrideOptionalOfDisplayImeControllerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideIconProvider = DoubleCheck.provider(WMShellBaseModule_ProvideIconProviderFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideIconProvider, this.provideShellMainExecutorProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideShellTaskOrganizerProvider = new DelegateFactory();
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, this.provideDisplayControllerProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideShellAnimationExecutorProvider));
            this.provideCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider, this.provideShellMainExecutorProvider, this.provideTransitionsProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideRecentTasksControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providerTaskStackListenerImplProvider, this.provideShellMainExecutorProvider));
            DelegateFactory.setDelegate(this.provideShellTaskOrganizerProvider, DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideCompatUIControllerProvider, this.provideRecentTasksControllerProvider)));
            this.provideKidsModeTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideRecentTasksControllerProvider));
            this.optionalOfBubbleControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            Provider<Optional<SplitScreenController>> r1 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfSplitScreenControllerProvider = r1;
            this.providesSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesSplitScreenControllerFactory.create(r1, DaggerTvGlobalRootComponent.this.contextProvider));
            this.optionalOfAppPairsControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.optionalOfPipTouchHandlerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            Provider<Optional<ShellUnfoldProgressProvider>> r12 = PresentJdkOptionalInstanceProvider.of(DaggerTvGlobalRootComponent.this.provideShellProgressProvider);
            this.optionalOfShellUnfoldProgressProvider = r12;
            Provider<Optional<FullscreenUnfoldController>> provider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory.create(this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider, r12));
            this.provideFullscreenUnfoldControllerProvider = provider;
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider, this.provideSyncTransactionQueueProvider, provider, this.provideRecentTasksControllerProvider));
            this.provideUnfoldTransitionHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory.create(this.optionalOfShellUnfoldProgressProvider, this.provideTransactionPoolProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<FreeformTaskListener>> r13 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFreeformTaskListenerProvider = r13;
            this.provideFreeformTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFreeformTaskListenerFactory.create(r13, DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            Provider<StartingWindowTypeAlgorithm> provider2 = DoubleCheck.provider(TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory.create());
            this.provideStartingWindowTypeAlgorithmProvider = provider2;
            Provider<Optional<StartingWindowTypeAlgorithm>> r14 = PresentJdkOptionalInstanceProvider.of(provider2);
            this.dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider = r14;
            this.provideStartingWindowTypeAlgorithmProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory.create(r14));
            Provider<StartingWindowController> provider3 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider2, this.provideIconProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider3;
            Provider<ShellInitImpl> provider4 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfBubbleControllerProvider, this.providesSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideFullscreenUnfoldControllerProvider, this.provideUnfoldTransitionHandlerProvider, this.provideFreeformTaskListenerProvider, this.provideRecentTasksControllerProvider, this.provideTransitionsProvider, provider3, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider4;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider4));
            this.optionalOfLegacySplitScreenControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.provideTvPipBoundsStateProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsStateFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvidePipSnapAlgorithmFactory.create());
            this.provideTvPipBoundsAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsAlgorithmFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideTvPipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            this.provideTvPipBoundsControllerProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider, this.provideTvPipBoundsStateProvider, this.provideTvPipBoundsAlgorithmProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider));
            this.providesTvPipMenuControllerProvider = DoubleCheck.provider(TvPipModule_ProvidesTvPipMenuControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideTvPipBoundsStateProvider, this.provideSystemWindowsProvider, this.providePipMediaControllerProvider, this.provideShellMainHandlerProvider));
            this.providePipTransitionStateProvider = DoubleCheck.provider(TvPipModule_ProvidePipTransitionStateFactory.create());
            Provider<PipSurfaceTransactionHelper> provider5 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider5;
            Provider<PipAnimationController> provider6 = DoubleCheck.provider(TvPipModule_ProvidePipAnimationControllerFactory.create(provider5));
            this.providePipAnimationControllerProvider = provider6;
            this.provideTvPipTransitionProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipTransitionFactory.create(this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, provider6, this.provideTvPipBoundsAlgorithmProvider, this.provideTvPipBoundsStateProvider, this.providesTvPipMenuControllerProvider));
            this.providePipParamsChangedForwarderProvider = DoubleCheck.provider(TvPipModule_ProvidePipParamsChangedForwarderFactory.create());
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider));
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(TvPipModule_ProvidePipTaskOrganizerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesTvPipMenuControllerProvider, this.provideSyncTransactionQueueProvider, this.provideTvPipBoundsStateProvider, this.providePipTransitionStateProvider, this.provideTvPipBoundsAlgorithmProvider, this.providePipAnimationControllerProvider, this.provideTvPipTransitionProvider, this.providePipParamsChangedForwarderProvider, this.providePipSurfaceTransactionHelperProvider, this.providesSplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(TvPipModule_ProvidePipAppOpsListenerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTvPipNotificationControllerProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipNotificationControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipMediaControllerProvider, this.providePipParamsChangedForwarderProvider, this.provideTvPipBoundsStateProvider, this.provideShellMainHandlerProvider));
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.providePipProvider = DoubleCheck.provider(TvPipModule_ProvidePipFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideTvPipBoundsStateProvider, this.provideTvPipBoundsAlgorithmProvider, this.provideTvPipBoundsControllerProvider, this.providePipAppOpsListenerProvider, this.providePipTaskOrganizerProvider, this.providesTvPipMenuControllerProvider, this.providePipMediaControllerProvider, this.provideTvPipTransitionProvider, this.provideTvPipNotificationControllerProvider, this.providerTaskStackListenerImplProvider, this.providePipParamsChangedForwarderProvider, this.provideDisplayControllerProvider, this.provideWindowManagerShellWrapperProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<OneHandedController>> r15 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfOneHandedControllerProvider = r15;
            this.providesOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesOneHandedControllerFactory.create(r15));
            Provider<Optional<HideDisplayCutoutController>> provider7 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider7;
            Provider<ShellCommandHandlerImpl> provider8 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.providesSplitScreenControllerProvider, this.providePipProvider, this.providesOneHandedControllerProvider, provider7, this.optionalOfAppPairsControllerProvider, this.provideRecentTasksControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider8;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider8));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.providesOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.providesSplitScreenControllerProvider));
            this.provideAppPairsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.optionalOfBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewTransitions> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideTaskViewTransitionsProvider = provider9;
            Provider<TaskViewFactoryController> provider10 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, this.provideSyncTransactionQueueProvider, provider9));
            this.provideTaskViewFactoryControllerProvider = provider10;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider10));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<RootDisplayAreaOrganizer> provider11 = DoubleCheck.provider(WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider));
            this.provideRootDisplayAreaOrganizerProvider = provider11;
            this.provideDisplayAreaHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayAreaHelperFactory.create(this.provideShellMainExecutorProvider, provider11));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory create = WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider);
            this.provideTaskSurfaceHelperControllerProvider = create;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(create));
            this.provideRecentTasksProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksFactory.create(this.provideRecentTasksControllerProvider));
            this.provideCompatUIProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIFactory.create(this.provideCompatUIControllerProvider));
            this.provideDragAndDropProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropFactory.create(this.provideDragAndDropControllerProvider));
            this.provideSharedBackgroundHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory.create());
            Provider<Optional<BackAnimationController>> provider12 = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideSharedBackgroundHandlerProvider));
            this.provideBackAnimationControllerProvider = provider12;
            this.provideBackAnimationProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationFactory.create(provider12));
        }

        public ShellInit getShellInit() {
            return this.provideShellInitProvider.get();
        }

        public Optional<ShellCommandHandler> getShellCommandHandler() {
            return this.provideShellCommandHandlerProvider.get();
        }

        public Optional<OneHanded> getOneHanded() {
            return this.provideOneHandedProvider.get();
        }

        public Optional<Pip> getPip() {
            return this.providePipProvider.get();
        }

        public Optional<LegacySplitScreen> getLegacySplitScreen() {
            return this.provideLegacySplitScreenProvider.get();
        }

        public Optional<LegacySplitScreenController> getLegacySplitScreenController() {
            return Optional.empty();
        }

        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.get();
        }

        public Optional<SplitScreenController> getSplitScreenController() {
            return this.providesSplitScreenControllerProvider.get();
        }

        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider.get();
        }

        public Optional<Bubbles> getBubbles() {
            return this.provideBubblesProvider.get();
        }

        public Optional<HideDisplayCutout> getHideDisplayCutout() {
            return this.provideHideDisplayCutoutProvider.get();
        }

        public Optional<TaskViewFactory> getTaskViewFactory() {
            return this.provideTaskViewFactoryProvider.get();
        }

        public ShellTransitions getTransitions() {
            return this.provideRemoteTransitionsProvider.get();
        }

        public Optional<StartingSurface> getStartingSurface() {
            return this.provideStartingSurfaceProvider.get();
        }

        public Optional<DisplayAreaHelper> getDisplayAreaHelper() {
            return this.provideDisplayAreaHelperProvider.get();
        }

        public Optional<TaskSurfaceHelper> getTaskSurfaceHelper() {
            return this.provideTaskSurfaceHelperProvider.get();
        }

        public Optional<RecentTasks> getRecentTasks() {
            return this.provideRecentTasksProvider.get();
        }

        public Optional<CompatUI> getCompatUI() {
            return this.provideCompatUIProvider.get();
        }

        public Optional<DragAndDrop> getDragAndDrop() {
            return this.provideDragAndDropProvider.get();
        }

        public Optional<BackAnimation> getBackAnimation() {
            return this.provideBackAnimationProvider.get();
        }
    }

    public final class TvSysUIComponentBuilder implements TvSysUIComponent.Builder {
        public Optional<AppPairs> setAppPairs;
        public Optional<BackAnimation> setBackAnimation;
        public Optional<Bubbles> setBubbles;
        public Optional<CompatUI> setCompatUI;
        public Optional<DisplayAreaHelper> setDisplayAreaHelper;
        public Optional<DragAndDrop> setDragAndDrop;
        public Optional<HideDisplayCutout> setHideDisplayCutout;
        public Optional<LegacySplitScreen> setLegacySplitScreen;
        public Optional<LegacySplitScreenController> setLegacySplitScreenController;
        public Optional<OneHanded> setOneHanded;
        public Optional<Pip> setPip;
        public Optional<RecentTasks> setRecentTasks;
        public Optional<ShellCommandHandler> setShellCommandHandler;
        public Optional<SplitScreen> setSplitScreen;
        public Optional<SplitScreenController> setSplitScreenController;
        public Optional<StartingSurface> setStartingSurface;
        public Optional<TaskSurfaceHelper> setTaskSurfaceHelper;
        public Optional<TaskViewFactory> setTaskViewFactory;
        public ShellTransitions setTransitions;

        public TvSysUIComponentBuilder() {
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.pip.Pip>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setPip(java.util.Optional<com.android.wm.shell.pip.Pip> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setPip = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setPip(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setLegacySplitScreen(java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setLegacySplitScreen(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreenController>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setLegacySplitScreenController(java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreenController> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreenController = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setLegacySplitScreenController(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setSplitScreen(java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setSplitScreen(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setSplitScreenController(java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreenController = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setSplitScreenController(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.apppairs.AppPairs>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setAppPairs(java.util.Optional<com.android.wm.shell.apppairs.AppPairs> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setAppPairs = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setAppPairs(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.onehanded.OneHanded>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setOneHanded(java.util.Optional<com.android.wm.shell.onehanded.OneHanded> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setOneHanded = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setOneHanded(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.bubbles.Bubbles>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setBubbles(java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBubbles = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setBubbles(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.TaskViewFactory>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setTaskViewFactory(java.util.Optional<com.android.wm.shell.TaskViewFactory> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskViewFactory = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setTaskViewFactory(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setHideDisplayCutout(java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setHideDisplayCutout = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setHideDisplayCutout(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.ShellCommandHandler>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setShellCommandHandler(java.util.Optional<com.android.wm.shell.ShellCommandHandler> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setShellCommandHandler = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setShellCommandHandler(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        public TvSysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setStartingSurface(java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setStartingSurface = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setStartingSurface(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setDisplayAreaHelper(java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDisplayAreaHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setDisplayAreaHelper(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setTaskSurfaceHelper(java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskSurfaceHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setTaskSurfaceHelper(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.recents.RecentTasks>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setRecentTasks(java.util.Optional<com.android.wm.shell.recents.RecentTasks> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setRecentTasks = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setRecentTasks(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.compatui.CompatUI>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setCompatUI(java.util.Optional<com.android.wm.shell.compatui.CompatUI> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setCompatUI = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setCompatUI(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setDragAndDrop(java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDragAndDrop = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setDragAndDrop(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.back.BackAnimation>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setBackAnimation(java.util.Optional<com.android.wm.shell.back.BackAnimation> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBackAnimation = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setBackAnimation(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        public TvSysUIComponent build() {
            Preconditions.checkBuilderRequirement(this.setPip, Optional.class);
            Preconditions.checkBuilderRequirement(this.setLegacySplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setLegacySplitScreenController, Optional.class);
            Preconditions.checkBuilderRequirement(this.setSplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setSplitScreenController, Optional.class);
            Preconditions.checkBuilderRequirement(this.setAppPairs, Optional.class);
            Preconditions.checkBuilderRequirement(this.setOneHanded, Optional.class);
            Preconditions.checkBuilderRequirement(this.setBubbles, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskViewFactory, Optional.class);
            Preconditions.checkBuilderRequirement(this.setHideDisplayCutout, Optional.class);
            Preconditions.checkBuilderRequirement(this.setShellCommandHandler, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTransitions, ShellTransitions.class);
            Preconditions.checkBuilderRequirement(this.setStartingSurface, Optional.class);
            Preconditions.checkBuilderRequirement(this.setDisplayAreaHelper, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskSurfaceHelper, Optional.class);
            Preconditions.checkBuilderRequirement(this.setRecentTasks, Optional.class);
            Preconditions.checkBuilderRequirement(this.setCompatUI, Optional.class);
            Preconditions.checkBuilderRequirement(this.setDragAndDrop, Optional.class);
            Preconditions.checkBuilderRequirement(this.setBackAnimation, Optional.class);
            DaggerTvGlobalRootComponent daggerTvGlobalRootComponent = DaggerTvGlobalRootComponent.this;
            LeakModule leakModule = r2;
            LeakModule leakModule2 = new LeakModule();
            NightDisplayListenerModule nightDisplayListenerModule = r2;
            NightDisplayListenerModule nightDisplayListenerModule2 = new NightDisplayListenerModule();
            SharedLibraryModule sharedLibraryModule = r2;
            SharedLibraryModule sharedLibraryModule2 = new SharedLibraryModule();
            KeyguardModule keyguardModule = r2;
            KeyguardModule keyguardModule2 = new KeyguardModule();
            SysUIUnfoldModule sysUIUnfoldModule = r2;
            SysUIUnfoldModule sysUIUnfoldModule2 = new SysUIUnfoldModule();
            return new TvSysUIComponentImpl(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, this.setPip, this.setLegacySplitScreen, this.setLegacySplitScreenController, this.setSplitScreen, this.setSplitScreenController, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setDisplayAreaHelper, this.setTaskSurfaceHelper, this.setRecentTasks, this.setCompatUI, this.setDragAndDrop, this.setBackAnimation);
        }
    }

    public final class TvSysUIComponentImpl implements TvSysUIComponent {
        public Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
        public Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
        public Provider<AccessibilityController> accessibilityControllerProvider;
        public Provider<AccessibilityFloatingMenuController> accessibilityFloatingMenuControllerProvider;
        public Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
        public Provider<ActionClickLogger> actionClickLoggerProvider;
        public Provider<ActionProxyReceiver> actionProxyReceiverProvider;
        public Provider<ActiveUnlockConfig> activeUnlockConfigProvider;
        public Provider<ActivityIntentHelper> activityIntentHelperProvider;
        public Provider<ActivityStarterDelegate> activityStarterDelegateProvider;
        public Provider<UserDetailView.Adapter> adapterProvider;
        public Provider<AirplaneModeTile> airplaneModeTileProvider;
        public Provider<AlarmTile> alarmTileProvider;
        public Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
        public Provider<AmbientState> ambientStateProvider;
        public Provider<AppOpsControllerImpl> appOpsControllerImplProvider;
        public Provider<AppOpsPrivacyItemMonitor> appOpsPrivacyItemMonitorProvider;
        public Provider<AssistLogger> assistLoggerProvider;
        public Provider<AssistManager> assistManagerProvider;
        public Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
        public Provider<AsyncSensorManager> asyncSensorManagerProvider;
        public Provider<AuthController> authControllerProvider;
        public Provider<AutoHideController> autoHideControllerProvider;
        public Provider<BatterySaverTile> batterySaverTileProvider;
        public Provider<BatteryStateNotifier> batteryStateNotifierProvider;
        public Provider<BindEventManagerImpl> bindEventManagerImplProvider;
        public Provider<RotationPolicyWrapper> bindRotationPolicyWrapperProvider;
        public Provider<SystemClock> bindSystemClockProvider;
        public Provider<BiometricUnlockController> biometricUnlockControllerProvider;
        public Provider<BlackScreenTile> blackScreenTileProvider;
        public Provider<BluetoothControllerImpl> bluetoothControllerImplProvider;
        public Provider<BluetoothTile> bluetoothTileProvider;
        public Provider<BlurUtils> blurUtilsProvider;
        public Provider<BootCompleteCacheImpl> bootCompleteCacheImplProvider;
        public Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
        public Provider<BrightnessDialog> brightnessDialogProvider;
        public Provider<BroadcastDispatcherLogger> broadcastDispatcherLoggerProvider;
        public Provider<BroadcastDispatcher> broadcastDispatcherProvider;
        public Provider<BroadcastDispatcherStartable> broadcastDispatcherStartableProvider;
        public Provider<BroadcastSender> broadcastSenderProvider;
        public Provider<ThresholdSensorImpl.BuilderFactory> builderFactoryProvider;
        public Provider<ThresholdSensorImpl.Builder> builderProvider;
        public Provider<WakeLock.Builder> builderProvider2;
        public Provider<DelayedWakeLock.Builder> builderProvider3;
        public Provider<CustomTile.Builder> builderProvider4;
        public Provider<NightDisplayListenerModule.Builder> builderProvider5;
        public Provider<AutoAddTracker.Builder> builderProvider6;
        public Provider<TileServiceRequestController.Builder> builderProvider7;
        public Provider<CallbackHandler> callbackHandlerProvider;
        public Provider<CameraToggleTile> cameraToggleTileProvider;
        public Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        public Provider<CastControllerImpl> castControllerImplProvider;
        public Provider<CastTile> castTileProvider;
        public Provider<CellularTile> cellularTileProvider;
        public Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        public Provider<ClockManager> clockManagerProvider;
        public Provider<ColorCorrectionTile> colorCorrectionTileProvider;
        public Provider<ColorInversionTile> colorInversionTileProvider;
        public Provider<CommandRegistry> commandRegistryProvider;
        public Provider<ConfigurationControllerImpl> configurationControllerImplProvider;
        public Provider<ContextComponentResolver> contextComponentResolverProvider;
        public Provider<ControlActionCoordinatorImpl> controlActionCoordinatorImplProvider;
        public Provider<ControlsActivity> controlsActivityProvider;
        public Provider<ControlsBindingControllerImpl> controlsBindingControllerImplProvider;
        public Provider<ControlsComponent> controlsComponentProvider;
        public Provider<ControlsControllerImpl> controlsControllerImplProvider;
        public Provider<ControlsEditingActivity> controlsEditingActivityProvider;
        public Provider<ControlsFavoritingActivity> controlsFavoritingActivityProvider;
        public Provider<ControlsListingControllerImpl> controlsListingControllerImplProvider;
        public Provider<ControlsMetricsLoggerImpl> controlsMetricsLoggerImplProvider;
        public Provider<ControlsProviderSelectorActivity> controlsProviderSelectorActivityProvider;
        public Provider<ControlsRequestDialog> controlsRequestDialogProvider;
        public Provider<ControlsUiControllerImpl> controlsUiControllerImplProvider;
        public Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
        public Provider<ConversationNotificationProcessor> conversationNotificationProcessorProvider;
        public Provider<CreateUserActivity> createUserActivityProvider;
        public Provider<CustomIconCache> customIconCacheProvider;
        public Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        public Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        public Provider<DataSaverTile> dataSaverTileProvider;
        public Provider<DebugModeFilterProvider> debugModeFilterProvider;
        public Provider<DefaultUiController> defaultUiControllerProvider;
        public Provider<DeleteScreenshotReceiver> deleteScreenshotReceiverProvider;
        public Provider<Dependency> dependencyProvider;
        public Provider<DeviceConfigProxy> deviceConfigProxyProvider;
        public Provider<DeviceControlsControllerImpl> deviceControlsControllerImplProvider;
        public Provider<DeviceControlsTile> deviceControlsTileProvider;
        public Provider<DevicePostureControllerImpl> devicePostureControllerImplProvider;
        public Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerImplProvider;
        public Provider<DeviceStateRotationLockSettingController> deviceStateRotationLockSettingControllerProvider;
        public Provider diagonalClassifierProvider;
        public Provider<DisableFlagsLogger> disableFlagsLoggerProvider;
        public Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
        public Provider distanceClassifierProvider;
        public Provider<DndTile> dndTileProvider;
        public Provider<DockManagerImpl> dockManagerImplProvider;
        public Provider<DoubleTapClassifier> doubleTapClassifierProvider;
        public Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
        public Provider<DozeLog> dozeLogProvider;
        public Provider<DozeLogger> dozeLoggerProvider;
        public Provider<DozeParameters> dozeParametersProvider;
        public Provider<DozeScrimController> dozeScrimControllerProvider;
        public Provider<DozeServiceHost> dozeServiceHostProvider;
        public Provider<DozeService> dozeServiceProvider;
        public Provider<DreamOverlayComponent.Factory> dreamOverlayComponentFactoryProvider;
        public Provider<DreamOverlayNotificationCountProvider> dreamOverlayNotificationCountProvider;
        public Provider<DreamOverlayService> dreamOverlayServiceProvider;
        public Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
        public Provider<DumpHandler> dumpHandlerProvider;
        public Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
        public Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        public Provider<EnhancedEstimatesImpl> enhancedEstimatesImplProvider;
        public Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        public Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        public Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        public Provider<LightBarTransitionsController.Factory> factoryProvider;
        public Provider<EdgeBackGestureHandler.Factory> factoryProvider2;
        public Provider<KeyguardBouncer.Factory> factoryProvider3;
        public Provider<KeyguardMessageAreaController.Factory> factoryProvider4;
        public Provider<LockscreenShadeKeyguardTransitionController.Factory> factoryProvider5;
        public Provider<SplitShadeLockScreenOverScroller.Factory> factoryProvider6;
        public Provider<SingleShadeLockScreenOverScroller.Factory> factoryProvider7;
        public Provider<BrightnessSliderController.Factory> factoryProvider8;
        public Provider<TileLifecycleManager.Factory> factoryProvider9;
        public Provider falsingCollectorImplProvider;
        public Provider<FalsingDataProvider> falsingDataProvider;
        public Provider<FalsingManagerProxy> falsingManagerProxyProvider;
        public Provider<FeatureFlagsDebug> featureFlagsDebugProvider;
        public Provider<FgsManagerController> fgsManagerControllerProvider;
        public Provider<Files> filesProvider;
        public Provider<FlashlightControllerImpl> flashlightControllerImplProvider;
        public Provider<FlashlightTile> flashlightTileProvider;
        public Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        public Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        public Provider<ForegroundServicesDialog> foregroundServicesDialogProvider;
        public Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        public Provider<FragmentService> fragmentServiceProvider;
        public Provider<GarbageMonitor> garbageMonitorProvider;
        public Provider<GlobalActionsComponent> globalActionsComponentProvider;
        public Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        public Provider<GlobalActionsImpl> globalActionsImplProvider;
        public Provider globalSettingsImplProvider;
        public Provider<HdmiCecSetMenuLanguageActivity> hdmiCecSetMenuLanguageActivityProvider;
        public Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;
        public Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
        public Provider<HighPriorityProvider> highPriorityProvider;
        public Provider<HistoryTracker> historyTrackerProvider;
        public Provider<HomeSoundEffectController> homeSoundEffectControllerProvider;
        public Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        public Provider<HotspotTile> hotspotTileProvider;
        public Provider<IconBuilder> iconBuilderProvider;
        public Provider<IconManager> iconManagerProvider;
        public Provider imageExporterProvider;
        public Provider imageTileSetProvider;
        public Provider<InitController> initControllerProvider;
        public Provider<InstantAppNotifier> instantAppNotifierProvider;
        public Provider<InternetDialogController> internetDialogControllerProvider;
        public Provider<InternetDialogFactory> internetDialogFactoryProvider;
        public Provider<InternetTile> internetTileProvider;
        public Provider<Boolean> isPMLiteEnabledProvider;
        public Provider<Boolean> isReduceBrightColorsAvailableProvider;
        public Provider<KeyboardUI> keyboardUIProvider;
        public Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        public Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        public Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        public Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        public Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        public Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        public Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        public Provider keyguardNotificationVisibilityProviderImplProvider;
        public Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        public Provider<KeyguardService> keyguardServiceProvider;
        public Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        public Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        public Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        public Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        public Provider<LSShadeTransitionLogger> lSShadeTransitionLoggerProvider;
        public Provider<LaunchConversationActivity> launchConversationActivityProvider;
        public Provider<LeakReporter> leakReporterProvider;
        public Provider<LightBarController> lightBarControllerProvider;
        public C0006LightBarTransitionsController_Factory lightBarTransitionsControllerProvider;
        public Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
        public Provider<LocationControllerImpl> locationControllerImplProvider;
        public Provider<LocationTile> locationTileProvider;
        public Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
        public C0001LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionControllerProvider;
        public Provider<LockscreenShadeScrimTransitionController> lockscreenShadeScrimTransitionControllerProvider;
        public Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
        public Provider<LockscreenSmartspaceController> lockscreenSmartspaceControllerProvider;
        public Provider<LogBufferEulogizer> logBufferEulogizerProvider;
        public Provider<LogBufferFactory> logBufferFactoryProvider;
        public Provider<LogBufferFreezer> logBufferFreezerProvider;
        public Provider<LongScreenshotActivity> longScreenshotActivityProvider;
        public Provider<LongScreenshotData> longScreenshotDataProvider;
        public Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
        public Provider<ManagedProfileControllerImpl> managedProfileControllerImplProvider;
        public Provider<Map<Class<?>, Provider<Activity>>> mapOfClassOfAndProviderOfActivityProvider;
        public Provider<Map<Class<?>, Provider<BroadcastReceiver>>> mapOfClassOfAndProviderOfBroadcastReceiverProvider;
        public Provider<Map<Class<?>, Provider<CoreStartable>>> mapOfClassOfAndProviderOfCoreStartableProvider;
        public Provider<Map<Class<?>, Provider<RecentsImplementation>>> mapOfClassOfAndProviderOfRecentsImplementationProvider;
        public Provider<Map<Class<?>, Provider<Service>>> mapOfClassOfAndProviderOfServiceProvider;
        public Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
        public Provider<MediaBrowserFactory> mediaBrowserFactoryProvider;
        public Provider<MediaCarouselControllerLogger> mediaCarouselControllerLoggerProvider;
        public Provider<MediaCarouselController> mediaCarouselControllerProvider;
        public Provider<MediaContainerController> mediaContainerControllerProvider;
        public Provider<MediaControlPanel> mediaControlPanelProvider;
        public Provider<MediaControllerFactory> mediaControllerFactoryProvider;
        public Provider<MediaDataFilter> mediaDataFilterProvider;
        public Provider<MediaDataManager> mediaDataManagerProvider;
        public Provider<MediaDeviceManager> mediaDeviceManagerProvider;
        public Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
        public Provider<MediaFlags> mediaFlagsProvider;
        public Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
        public Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
        public Provider<MediaMuteAwaitConnectionCli> mediaMuteAwaitConnectionCliProvider;
        public Provider<MediaMuteAwaitConnectionManagerFactory> mediaMuteAwaitConnectionManagerFactoryProvider;
        public Provider<MediaMuteAwaitLogger> mediaMuteAwaitLoggerProvider;
        public Provider<MediaOutputBroadcastDialogFactory> mediaOutputBroadcastDialogFactoryProvider;
        public Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
        public Provider<MediaOutputDialogReceiver> mediaOutputDialogReceiverProvider;
        public Provider<MediaProjectionPrivacyItemMonitor> mediaProjectionPrivacyItemMonitorProvider;
        public Provider<MediaResumeListener> mediaResumeListenerProvider;
        public Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
        public Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
        public Provider<MediaTimeoutLogger> mediaTimeoutLoggerProvider;
        public Provider<MediaTttChipControllerReceiver> mediaTttChipControllerReceiverProvider;
        public Provider<MediaTttChipControllerSender> mediaTttChipControllerSenderProvider;
        public Provider<MediaTttCommandLineHelper> mediaTttCommandLineHelperProvider;
        public Provider<MediaTttFlags> mediaTttFlagsProvider;
        public Provider<MediaTttReceiverUiEventLogger> mediaTttReceiverUiEventLoggerProvider;
        public Provider<MediaTttSenderUiEventLogger> mediaTttSenderUiEventLoggerProvider;
        public Provider<MediaUiEventLogger> mediaUiEventLoggerProvider;
        public Provider<MediaViewController> mediaViewControllerProvider;
        public Provider<MediaViewLogger> mediaViewLoggerProvider;
        public Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
        public Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
        public Provider<ModeSwitchesController> modeSwitchesControllerProvider;
        public Provider<Set<FalsingClassifier>> namedSetOfFalsingClassifierProvider;
        public Provider<NavBarHelper> navBarHelperProvider;
        public Provider<NavigationBarComponent.Factory> navigationBarComponentFactoryProvider;
        public Provider<NavigationBarController> navigationBarControllerProvider;
        public Provider<NavigationModeController> navigationModeControllerProvider;
        public Provider<NearbyMediaDevicesLogger> nearbyMediaDevicesLoggerProvider;
        public Provider<NearbyMediaDevicesManager> nearbyMediaDevicesManagerProvider;
        public Provider<NetworkControllerImpl> networkControllerImplProvider;
        public Provider<KeyguardViewMediator> newKeyguardViewMediatorProvider;
        public Provider<NextAlarmControllerImpl> nextAlarmControllerImplProvider;
        public Provider<NfcTile> nfcTileProvider;
        public Provider<NightDisplayTile> nightDisplayTileProvider;
        public Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        public Provider<NotifBindPipeline> notifBindPipelineProvider;
        public Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        public Provider<NotifCollection> notifCollectionProvider;
        public Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        public Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;
        public Provider notifPipelineChoreographerImplProvider;
        public Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
        public Provider<NotifPipeline> notifPipelineProvider;
        public Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        public Provider<NotificationChannels> notificationChannelsProvider;
        public Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        public Provider<NotificationContentInflater> notificationContentInflaterProvider;
        public Provider<NotificationEntryManagerLogger> notificationEntryManagerLoggerProvider;
        public Provider<NotificationFilter> notificationFilterProvider;
        public Provider<NotificationGroupAlertTransferHelper> notificationGroupAlertTransferHelperProvider;
        public Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
        public Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
        public Provider<NotificationInteractionTracker> notificationInteractionTrackerProvider;
        public Provider<NotificationInterruptLogger> notificationInterruptLoggerProvider;
        public Provider<NotificationInterruptStateProviderImpl> notificationInterruptStateProviderImplProvider;
        public Provider<NotificationListener> notificationListenerProvider;
        public Provider<NotificationListenerWithPlugins> notificationListenerWithPluginsProvider;
        public Provider<NotificationLockscreenUserManagerImpl> notificationLockscreenUserManagerImplProvider;
        public Provider<NotificationPersonExtractorPluginBoundary> notificationPersonExtractorPluginBoundaryProvider;
        public Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        public Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        public Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        public Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        public Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        public Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        public Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        public Provider<NotificationVisibilityProviderImpl> notificationVisibilityProviderImplProvider;
        public Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        public Provider<OneHandedModeTile> oneHandedModeTileProvider;
        public Provider<Optional<AlternateUdfpsTouchProvider>> optionalOfAlternateUdfpsTouchProvider;
        public Provider<Optional<BcSmartspaceDataPlugin>> optionalOfBcSmartspaceDataPluginProvider;
        public Provider<Optional<CentralSurfaces>> optionalOfCentralSurfacesProvider;
        public Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalOfControlsFavoritePersistenceWrapperProvider;
        public Provider<Optional<ControlsTileResourceConfiguration>> optionalOfControlsTileResourceConfigurationProvider;
        public Provider<Optional<Recents>> optionalOfRecentsProvider;
        public Provider<Optional<UdfpsHbmProvider>> optionalOfUdfpsHbmProvider;
        public Provider<OverviewProxyRecentsImpl> overviewProxyRecentsImplProvider;
        public Provider<OverviewProxyService> overviewProxyServiceProvider;
        public Provider<PackageManagerAdapter> packageManagerAdapterProvider;
        public Provider panelEventsEmitterProvider;
        public Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
        public Provider<PendingRemovalStore> pendingRemovalStoreProvider;
        public Provider<PeopleNotificationIdentifierImpl> peopleNotificationIdentifierImplProvider;
        public Provider<PeopleSpaceActivity> peopleSpaceActivityProvider;
        public Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
        public Provider<PeopleSpaceWidgetPinnedReceiver> peopleSpaceWidgetPinnedReceiverProvider;
        public Provider<PeopleSpaceWidgetProvider> peopleSpaceWidgetProvider;
        public Provider<PhoneStateMonitor> phoneStateMonitorProvider;
        public Provider pointerCountClassifierProvider;
        public Provider postureDependentProximitySensorProvider;
        public Provider<PowerNotificationWarnings> powerNotificationWarningsProvider;
        public Provider<PowerUI> powerUIProvider;
        public Provider<PrivacyConfig> privacyConfigProvider;
        public Provider<PrivacyDialogController> privacyDialogControllerProvider;
        public Provider<PrivacyDotViewController> privacyDotViewControllerProvider;
        public Provider<PrivacyItemController> privacyItemControllerProvider;
        public Provider<PrivacyLogger> privacyLoggerProvider;
        public Provider<ProtoTracer> protoTracerProvider;
        public Provider<AccessPointControllerImpl> provideAccessPointControllerImplProvider;
        public Provider<ActivityLaunchAnimator> provideActivityLaunchAnimatorProvider;
        public Provider<ActivityManagerWrapper> provideActivityManagerWrapperProvider;
        public Provider<ActivityStarter> provideActivityStarterProvider;
        public Provider<Boolean> provideAllowNotificationLongPressProvider;
        public Provider<AssistUtils> provideAssistUtilsProvider;
        public Provider<DeviceStateRotationLockSettingsManager> provideAutoRotateSettingsManagerProvider;
        public Provider<AutoTileManager> provideAutoTileManagerProvider;
        public Provider<DelayableExecutor> provideBackgroundDelayableExecutorProvider;
        public Provider<Executor> provideBackgroundExecutorProvider;
        public Provider<RepeatableExecutor> provideBackgroundRepeatableExecutorProvider;
        public Provider<BatteryController> provideBatteryControllerProvider;
        public Provider<Handler> provideBgHandlerProvider;
        public Provider<Looper> provideBgLooperProvider;
        public Provider<LogBuffer> provideBroadcastDispatcherLogBufferProvider;
        public Provider<Optional<BubblesManager>> provideBubblesManagerProvider;
        public Provider provideClockInfoListProvider;
        public Provider<CommandQueue> provideCommandQueueProvider;
        public Provider<CommonNotifCollection> provideCommonNotifCollectionProvider;
        public Provider<DataSaverController> provideDataSaverControllerProvider;
        public Provider<DelayableExecutor> provideDelayableExecutorProvider;
        public Provider<DemoModeController> provideDemoModeControllerProvider;
        public Provider<DevicePolicyManagerWrapper> provideDevicePolicyManagerWrapperProvider;
        public Provider<DialogLaunchAnimator> provideDialogLaunchAnimatorProvider;
        public Provider<LogBuffer> provideDozeLogBufferProvider;
        public Provider<Executor> provideExecutorProvider;
        public Provider<FlagManager> provideFlagManagerProvider;
        public Provider<GroupExpansionManager> provideGroupExpansionManagerProvider;
        public Provider<GroupMembershipManager> provideGroupMembershipManagerProvider;
        public Provider<HeadsUpManagerPhone> provideHeadsUpManagerPhoneProvider;
        public Provider<IndividualSensorPrivacyController> provideIndividualSensorPrivacyControllerProvider;
        public Provider<LogBuffer> provideLSShadeTransitionControllerBufferProvider;
        public Provider<String> provideLauncherPackageProvider;
        public Provider<String> provideLeakReportEmailProvider;
        public Provider<LocalBluetoothManager> provideLocalBluetoothControllerProvider;
        public Provider<LogcatEchoTracker> provideLogcatEchoTrackerProvider;
        public Provider<Executor> provideLongRunningExecutorProvider;
        public Provider<Looper> provideLongRunningLooperProvider;
        public Provider<LogBuffer> provideMediaBrowserBufferProvider;
        public Provider<LogBuffer> provideMediaCarouselControllerBufferProvider;
        public Provider<LogBuffer> provideMediaMuteAwaitLogBufferProvider;
        public Provider<LogBuffer> provideMediaTttReceiverLogBufferProvider;
        public Provider<LogBuffer> provideMediaTttSenderLogBufferProvider;
        public Provider<LogBuffer> provideMediaViewLogBufferProvider;
        public Provider<LogBuffer> provideNearbyMediaDevicesLogBufferProvider;
        public Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        public Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        public Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        public Provider<NotificationEntryManager> provideNotificationEntryManagerProvider;
        public Provider<NotificationGutsManager> provideNotificationGutsManagerProvider;
        public Provider<LogBuffer> provideNotificationHeadsUpLogBufferProvider;
        public Provider<NotificationLogger> provideNotificationLoggerProvider;
        public Provider<NotificationMediaManager> provideNotificationMediaManagerProvider;
        public Provider<NotificationPanelLogger> provideNotificationPanelLoggerProvider;
        public Provider<NotificationRemoteInputManager> provideNotificationRemoteInputManagerProvider;
        public Provider<LogBuffer> provideNotificationSectionLogBufferProvider;
        public Provider<NotificationViewHierarchyManager> provideNotificationViewHierarchyManagerProvider;
        public Provider<NotificationVisibilityProvider> provideNotificationVisibilityProvider;
        public Provider<LogBuffer> provideNotificationsLogBufferProvider;
        public Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        public Provider<ThresholdSensor[]> providePostureToProximitySensorMappingProvider;
        public Provider<ThresholdSensor[]> providePostureToSecondaryProximitySensorMappingProvider;
        public Provider<ThresholdSensor> providePrimaryProximitySensorProvider;
        public Provider<LogBuffer> providePrivacyLogBufferProvider;
        public Provider<ProximityCheck> provideProximityCheckProvider;
        public Provider<ProximitySensor> provideProximitySensorProvider;
        public Provider<LogBuffer> provideQSFragmentDisableLogBufferProvider;
        public Provider<QuickAccessWalletClient> provideQuickAccessWalletClientProvider;
        public Provider<LogBuffer> provideQuickSettingsLogBufferProvider;
        public Provider<RecentsImplementation> provideRecentsImplProvider;
        public Provider<Recents> provideRecentsProvider;
        public Provider<ThresholdSensor> provideSecondaryProximitySensorProvider;
        public Provider<SensorPrivacyController> provideSensorPrivacyControllerProvider;
        public Provider<SmartReplyController> provideSmartReplyControllerProvider;
        public Provider<Optional<SysUIUnfoldComponent>> provideSysUIUnfoldComponentProvider;
        public Provider<SysUiState> provideSysUiStateProvider;
        public Provider<TaskStackChangeListeners> provideTaskStackChangeListenersProvider;
        public Provider<String> provideThemePickerPackageProvider;
        public Provider<Handler> provideTimeTickHandlerProvider;
        public Provider<LogBuffer> provideToastLogBufferProvider;
        public Provider<TvNotificationHandler> provideTvNotificationHandlerProvider;
        public Provider<UserTracker> provideUserTrackerProvider;
        public Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        public Provider<VolumeDialog> provideVolumeDialogProvider;
        public Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        public Provider<MessageRouter> providesBackgroundMessageRouterProvider;
        public Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        public Provider<Boolean> providesControlsFeatureEnabledProvider;
        public Provider<DeviceProvisionedController> providesDeviceProvisionedControllerProvider;
        public Provider<String[]> providesDeviceStateRotationLockDefaultsProvider;
        public Provider<Float> providesDoubleTapTouchSlopProvider;
        public Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        public Provider<MediaHost> providesKeyguardMediaHostProvider;
        public Provider<LeakDetector> providesLeakDetectorProvider;
        public Provider<Optional<MediaMuteAwaitConnectionCli>> providesMediaMuteAwaitConnectionCliProvider;
        public Provider<LogBuffer> providesMediaTimeoutListenerLogBufferProvider;
        public Provider<Optional<MediaTttChipControllerReceiver>> providesMediaTttChipControllerReceiverProvider;
        public Provider<Optional<MediaTttChipControllerSender>> providesMediaTttChipControllerSenderProvider;
        public Provider<Optional<MediaTttCommandLineHelper>> providesMediaTttCommandLineHelperProvider;
        public Provider<MediaTttLogger> providesMediaTttReceiverLoggerProvider;
        public Provider<MediaTttLogger> providesMediaTttSenderLoggerProvider;
        public Provider<Optional<NearbyMediaDevicesManager>> providesNearbyMediaDevicesManagerProvider;
        public Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        public Provider<Executor> providesPluginExecutorProvider;
        public Provider<MediaHost> providesQSMediaHostProvider;
        public Provider<MediaHost> providesQuickQSMediaHostProvider;
        public Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesSilentHeaderSubcomponentProvider;
        public Provider<Float> providesSingleTapTouchSlopProvider;
        public Provider<StatusBarWindowView> providesStatusBarWindowViewProvider;
        public Provider<ViewMediatorCallback> providesViewMediatorCallbackProvider;
        public Provider proximityClassifierProvider;
        public Provider proximitySensorImplProvider;
        public Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        public Provider<QRCodeScannerController> qRCodeScannerControllerProvider;
        public Provider<QRCodeScannerTile> qRCodeScannerTileProvider;
        public Provider<QSFactoryImpl> qSFactoryImplProvider;
        public Provider<QSLogger> qSLoggerProvider;
        public Provider<QSTileHost> qSTileHostProvider;
        public Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
        public Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
        public Provider<RebootTile> rebootTileProvider;
        public Provider<RecordingController> recordingControllerProvider;
        public Provider<RecordingService> recordingServiceProvider;
        public Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
        public Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
        public Provider<RemoteInputNotificationRebuilder> remoteInputNotificationRebuilderProvider;
        public Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
        public Provider<RemoteInputUriController> remoteInputUriControllerProvider;
        public Provider<RenderStageManager> renderStageManagerProvider;
        public Provider<ResumeMediaBrowserFactory> resumeMediaBrowserFactoryProvider;
        public Provider<ResumeMediaBrowserLogger> resumeMediaBrowserLoggerProvider;
        public Provider<RingerModeTrackerImpl> ringerModeTrackerImplProvider;
        public Provider<RingtonePlayer> ringtonePlayerProvider;
        public Provider<RotationLockControllerImpl> rotationLockControllerImplProvider;
        public Provider<RotationLockTile> rotationLockTileProvider;
        public Provider<RotationPolicyWrapperImpl> rotationPolicyWrapperImplProvider;
        public Provider<RowContentBindStageLogger> rowContentBindStageLoggerProvider;
        public Provider<RowContentBindStage> rowContentBindStageProvider;
        public Provider<SafetyController> safetyControllerProvider;
        public Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
        public Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
        public Provider<ScreenRecordTile> screenRecordTileProvider;
        public Provider<ScreenShotTile> screenShotTileProvider;
        public Provider<ScreenshotController> screenshotControllerProvider;
        public Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        public Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        public Provider<ScrimController> scrimControllerProvider;
        public Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        public Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        public Provider secureSettingsImplProvider;
        public Provider<SecurityControllerImpl> securityControllerImplProvider;
        public Provider<SeekBarViewModel> seekBarViewModelProvider;
        public Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        public Provider<SessionTracker> sessionTrackerProvider;
        public Provider<Optional<BackAnimation>> setBackAnimationProvider;
        public Provider<Optional<Bubbles>> setBubblesProvider;
        public Provider<Optional<CompatUI>> setCompatUIProvider;
        public Provider<Optional<DisplayAreaHelper>> setDisplayAreaHelperProvider;
        public Provider<Optional<DragAndDrop>> setDragAndDropProvider;
        public Provider<Optional<HideDisplayCutout>> setHideDisplayCutoutProvider;
        public Provider<Optional<LegacySplitScreenController>> setLegacySplitScreenControllerProvider;
        public Provider<Set<PrivacyItemMonitor>> setOfPrivacyItemMonitorProvider;
        public Provider<Optional<OneHanded>> setOneHandedProvider;
        public Provider<Optional<Pip>> setPipProvider;
        public Provider<Optional<RecentTasks>> setRecentTasksProvider;
        public Provider<Optional<ShellCommandHandler>> setShellCommandHandlerProvider;
        public Provider<Optional<SplitScreenController>> setSplitScreenControllerProvider;
        public Provider<Optional<SplitScreen>> setSplitScreenProvider;
        public Provider<Optional<StartingSurface>> setStartingSurfaceProvider;
        public Provider<Optional<TaskViewFactory>> setTaskViewFactoryProvider;
        public Provider<ShellTransitions> setTransitionsProvider;
        public Provider<SettingTile> settingTileProvider;
        public Provider<ShadeControllerImpl> shadeControllerImplProvider;
        public Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        public Provider<ShadeListBuilder> shadeListBuilderProvider;
        public Provider<ShortcutKeyDispatcher> shortcutKeyDispatcherProvider;
        public Provider<SidefpsController> sidefpsControllerProvider;
        public C0002SingleShadeLockScreenOverScroller_Factory singleShadeLockScreenOverScrollerProvider;
        public Provider<SingleTapClassifier> singleTapClassifierProvider;
        public Provider<SliceBroadcastRelayHandler> sliceBroadcastRelayHandlerProvider;
        public Provider<SmartActionInflaterImpl> smartActionInflaterImplProvider;
        public Provider<SmartActionsReceiver> smartActionsReceiverProvider;
        public Provider<SmartReplyConstants> smartReplyConstantsProvider;
        public Provider<SmartReplyInflaterImpl> smartReplyInflaterImplProvider;
        public Provider<SmartReplyStateInflaterImpl> smartReplyStateInflaterImplProvider;
        public Provider<SoundTile> soundTileProvider;
        public C0003SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScrollerProvider;
        public Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        public Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        public Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        public Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        public Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        public Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        public Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
        public Provider<StorageNotification> storageNotificationProvider;
        public Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        public Provider<SysUIUnfoldComponent.Factory> sysUIUnfoldComponentFactoryProvider;
        public Provider<SystemActions> systemActionsProvider;
        public Provider<SystemEventChipAnimationController> systemEventChipAnimationControllerProvider;
        public Provider<SystemEventCoordinator> systemEventCoordinatorProvider;
        public Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;
        public Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        public Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        public Provider<SystemUIDialogManager> systemUIDialogManagerProvider;
        public Provider<SystemUIService> systemUIServiceProvider;
        public Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        public Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        public Provider<TapGestureDetector> tapGestureDetectorProvider;
        public Provider<TaskbarDelegate> taskbarDelegateProvider;
        public Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        public Provider<ThemeOverlayApplier> themeOverlayApplierProvider;
        public Provider<ThemeOverlayController> themeOverlayControllerProvider;
        public final /* synthetic */ DaggerTvGlobalRootComponent this$0;
        public C0000TileLifecycleManager_Factory tileLifecycleManagerProvider;
        public Provider<TileServices> tileServicesProvider;
        public Provider<TimeoutHandler> timeoutHandlerProvider;
        public Provider<ToastFactory> toastFactoryProvider;
        public Provider<ToastLogger> toastLoggerProvider;
        public Provider<ToastUI> toastUIProvider;
        public Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        public Provider<TunerActivity> tunerActivityProvider;
        public Provider<TunerServiceImpl> tunerServiceImplProvider;
        public Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        public Provider<TvNotificationPanel> tvNotificationPanelProvider;
        public Provider<TvOngoingPrivacyChip> tvOngoingPrivacyChipProvider;
        public Provider<TvStatusBar> tvStatusBarProvider;
        public Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        public Provider<TypeClassifier> typeClassifierProvider;
        public Provider<UdfpsController> udfpsControllerProvider;
        public Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        public Provider<UdfpsShell> udfpsShellProvider;
        public Provider<UiModeNightTile> uiModeNightTileProvider;
        public Provider<UiOffloadThread> uiOffloadThreadProvider;
        public Provider<UnfoldLatencyTracker> unfoldLatencyTrackerProvider;
        public Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
        public Provider<UsbConfirmActivity> usbConfirmActivityProvider;
        public Provider<UsbDebuggingActivity> usbDebuggingActivityProvider;
        public Provider<UsbDebuggingSecondaryUserActivity> usbDebuggingSecondaryUserActivityProvider;
        public Provider<UsbPermissionActivity> usbPermissionActivityProvider;
        public Provider<UserCreator> userCreatorProvider;
        public Provider<UserInfoControllerImpl> userInfoControllerImplProvider;
        public Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
        public Provider<UserSwitcherActivity> userSwitcherActivityProvider;
        public Provider<UserSwitcherController> userSwitcherControllerProvider;
        public Provider<VibratorHelper> vibratorHelperProvider;
        public Provider<ViewUtil> viewUtilProvider;
        public Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        public Provider<VisualStabilityProvider> visualStabilityProvider;
        public Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        public Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        public Provider<VolumeUI> volumeUIProvider;
        public Provider<VpnStatusObserver> vpnStatusObserverProvider;
        public Provider<WMShell> wMShellProvider;
        public Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
        public Provider<WalletActivity> walletActivityProvider;
        public Provider<WalletControllerImpl> walletControllerImplProvider;
        public Provider<WallpaperController> wallpaperControllerProvider;
        public Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;
        public Provider<WifiStateWorker> wifiStateWorkerProvider;
        public Provider<WifiStatusTrackerFactory> wifiStatusTrackerFactoryProvider;
        public Provider<WifiTile> wifiTileProvider;
        public Provider<WindowMagnification> windowMagnificationProvider;
        public Provider<WorkLockActivity> workLockActivityProvider;
        public Provider<WorkModeTile> workModeTileProvider;
        public Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        public Provider zigZagClassifierProvider;

        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        public TvSysUIComponentImpl(DaggerTvGlobalRootComponent daggerTvGlobalRootComponent, LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.this$0 = daggerTvGlobalRootComponent;
            initialize(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize2(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize3(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize4(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize5(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize6(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
            initialize7(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, optional10, optional11, shellTransitions, optional12, optional13, optional14, optional15, optional16, optional17, optional18);
        }

        public final Object secureSettingsImpl() {
            return SecureSettingsImpl_Factory.newInstance((ContentResolver) this.this$0.provideContentResolverProvider.get());
        }

        public final void initialize(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.bootCompleteCacheImplProvider = DoubleCheck.provider(BootCompleteCacheImpl_Factory.create(this.this$0.dumpManagerProvider));
            this.configurationControllerImplProvider = DoubleCheck.provider(ConfigurationControllerImpl_Factory.create(this.this$0.contextProvider));
            this.globalSettingsImplProvider = GlobalSettingsImpl_Factory.create(this.this$0.provideContentResolverProvider);
            this.provideDemoModeControllerProvider = DoubleCheck.provider(DemoModeModule_ProvideDemoModeControllerFactory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.globalSettingsImplProvider));
            this.providesLeakDetectorProvider = DoubleCheck.provider(LeakModule_ProvidesLeakDetectorFactory.create(leakModule, this.this$0.dumpManagerProvider, TrackedCollections_Factory.create()));
            Provider<Looper> provider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.create());
            this.provideBgLooperProvider = provider;
            this.provideBgHandlerProvider = SysUIConcurrencyModule_ProvideBgHandlerFactory.create(provider);
            this.provideUserTrackerProvider = DoubleCheck.provider(SettingsModule_ProvideUserTrackerFactory.create(this.this$0.contextProvider, this.this$0.provideUserManagerProvider, this.this$0.dumpManagerProvider, this.provideBgHandlerProvider));
            Provider<TunerServiceImpl> provider2 = DoubleCheck.provider(TunerServiceImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.providesLeakDetectorProvider, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.tunerServiceImplProvider = provider2;
            this.tunerActivityProvider = TunerActivity_Factory.create(this.provideDemoModeControllerProvider, provider2);
            this.foregroundServicesDialogProvider = ForegroundServicesDialog_Factory.create(this.this$0.provideMetricsLoggerProvider);
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundExecutorFactory.create(this.provideBgLooperProvider));
            this.provideLogcatEchoTrackerProvider = DoubleCheck.provider(LogModule_ProvideLogcatEchoTrackerFactory.create(this.this$0.provideContentResolverProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            Provider<LogBufferFactory> provider3 = DoubleCheck.provider(LogBufferFactory_Factory.create(this.this$0.dumpManagerProvider, this.provideLogcatEchoTrackerProvider));
            this.logBufferFactoryProvider = provider3;
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideBroadcastDispatcherLogBufferFactory.create(provider3));
            this.provideBroadcastDispatcherLogBufferProvider = provider4;
            BroadcastDispatcherLogger_Factory create = BroadcastDispatcherLogger_Factory.create(provider4);
            this.broadcastDispatcherLoggerProvider = create;
            this.pendingRemovalStoreProvider = PendingRemovalStore_Factory.create(create);
            Provider<BroadcastDispatcher> provider5 = DoubleCheck.provider(BroadcastDispatcher_Factory.create(this.this$0.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.this$0.dumpManagerProvider, this.broadcastDispatcherLoggerProvider, this.provideUserTrackerProvider, this.pendingRemovalStoreProvider));
            this.broadcastDispatcherProvider = provider5;
            this.workLockActivityProvider = WorkLockActivity_Factory.create(provider5, this.this$0.provideUserManagerProvider, this.this$0.providePackageManagerProvider);
            this.deviceConfigProxyProvider = DoubleCheck.provider(DeviceConfigProxy_Factory.create());
            this.enhancedEstimatesImplProvider = DoubleCheck.provider(EnhancedEstimatesImpl_Factory.create());
            this.provideBatteryControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideBatteryControllerFactory.create(this.this$0.contextProvider, this.enhancedEstimatesImplProvider, this.this$0.providePowerManagerProvider, this.broadcastDispatcherProvider, this.provideDemoModeControllerProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.dockManagerImplProvider = DoubleCheck.provider(DockManagerImpl_Factory.create());
            Provider<FalsingDataProvider> provider6 = DoubleCheck.provider(FalsingDataProvider_Factory.create(this.this$0.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.dockManagerImplProvider));
            this.falsingDataProvider = provider6;
            DistanceClassifier_Factory create2 = DistanceClassifier_Factory.create(provider6, this.deviceConfigProxyProvider);
            this.distanceClassifierProvider = create2;
            this.proximityClassifierProvider = ProximityClassifier_Factory.create(create2, this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.pointerCountClassifierProvider = PointerCountClassifier_Factory.create(this.falsingDataProvider);
            this.typeClassifierProvider = TypeClassifier_Factory.create(this.falsingDataProvider);
            this.diagonalClassifierProvider = DiagonalClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            ZigZagClassifier_Factory create3 = ZigZagClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.zigZagClassifierProvider = create3;
            this.providesBrightLineGestureClassifiersProvider = FalsingModule_ProvidesBrightLineGestureClassifiersFactory.create(this.distanceClassifierProvider, this.proximityClassifierProvider, this.pointerCountClassifierProvider, this.typeClassifierProvider, this.diagonalClassifierProvider, create3);
            this.namedSetOfFalsingClassifierProvider = SetFactory.builder(0, 1).addCollectionProvider(this.providesBrightLineGestureClassifiersProvider).build();
            FalsingModule_ProvidesSingleTapTouchSlopFactory create4 = FalsingModule_ProvidesSingleTapTouchSlopFactory.create(this.this$0.provideViewConfigurationProvider);
            this.providesSingleTapTouchSlopProvider = create4;
            this.singleTapClassifierProvider = SingleTapClassifier_Factory.create(this.falsingDataProvider, create4);
            FalsingModule_ProvidesDoubleTapTouchSlopFactory create5 = FalsingModule_ProvidesDoubleTapTouchSlopFactory.create(this.this$0.provideResourcesProvider);
            this.providesDoubleTapTouchSlopProvider = create5;
            this.doubleTapClassifierProvider = DoubleTapClassifier_Factory.create(this.falsingDataProvider, this.singleTapClassifierProvider, create5, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create());
            Provider<SystemClock> provider7 = DoubleCheck.provider(SystemClockImpl_Factory.create());
            this.bindSystemClockProvider = provider7;
            this.historyTrackerProvider = DoubleCheck.provider(HistoryTracker_Factory.create(provider7));
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(StatusBarStateControllerImpl_Factory.create(this.this$0.provideUiEventLoggerProvider, this.this$0.dumpManagerProvider, this.this$0.provideInteractionJankMonitorProvider));
            this.protoTracerProvider = DoubleCheck.provider(ProtoTracer_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider));
            this.commandRegistryProvider = DoubleCheck.provider(CommandRegistry_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider));
            this.provideCommandQueueProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideCommandQueueFactory.create(this.this$0.contextProvider, this.protoTracerProvider, this.commandRegistryProvider));
            this.panelExpansionStateManagerProvider = DoubleCheck.provider(PanelExpansionStateManager_Factory.create());
            this.falsingManagerProxyProvider = new DelegateFactory();
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.asyncSensorManagerProvider = DoubleCheck.provider(AsyncSensorManager_Factory.create(this.this$0.providesSensorManagerProvider, ThreadFactoryImpl_Factory.create(), this.this$0.providesPluginManagerProvider));
            ThresholdSensorImpl_BuilderFactory_Factory create6 = ThresholdSensorImpl_BuilderFactory_Factory.create(this.this$0.provideResourcesProvider, this.asyncSensorManagerProvider, this.this$0.provideExecutionProvider);
            this.builderFactoryProvider = create6;
            this.providePostureToProximitySensorMappingProvider = SensorModule_ProvidePostureToProximitySensorMappingFactory.create(create6, this.this$0.provideResourcesProvider);
            this.providePostureToSecondaryProximitySensorMappingProvider = SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory.create(this.builderFactoryProvider, this.this$0.provideResourcesProvider);
            this.devicePostureControllerImplProvider = DoubleCheck.provider(DevicePostureControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideDeviceStateManagerProvider, this.this$0.provideMainExecutorProvider));
            this.postureDependentProximitySensorProvider = PostureDependentProximitySensor_Factory.create(this.providePostureToProximitySensorMappingProvider, this.providePostureToSecondaryProximitySensorMappingProvider, this.this$0.provideMainDelayableExecutorProvider, this.this$0.provideExecutionProvider, this.devicePostureControllerImplProvider);
            this.builderProvider = ThresholdSensorImpl_Builder_Factory.create(this.this$0.provideResourcesProvider, this.asyncSensorManagerProvider, this.this$0.provideExecutionProvider);
            this.providePrimaryProximitySensorProvider = SensorModule_ProvidePrimaryProximitySensorFactory.create(this.this$0.providesSensorManagerProvider, this.builderProvider);
            SensorModule_ProvideSecondaryProximitySensorFactory create7 = SensorModule_ProvideSecondaryProximitySensorFactory.create(this.builderProvider);
            this.provideSecondaryProximitySensorProvider = create7;
            this.proximitySensorImplProvider = ProximitySensorImpl_Factory.create(this.providePrimaryProximitySensorProvider, create7, this.this$0.provideMainDelayableExecutorProvider, this.this$0.provideExecutionProvider);
            this.provideProximitySensorProvider = SensorModule_ProvideProximitySensorFactory.create(this.this$0.provideResourcesProvider, this.postureDependentProximitySensorProvider, this.proximitySensorImplProvider);
            DelegateFactory delegateFactory = new DelegateFactory();
            this.keyguardStateControllerImplProvider = delegateFactory;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, this.provideProximitySensorProvider, this.statusBarStateControllerImplProvider, delegateFactory, this.provideBatteryControllerProvider, this.dockManagerImplProvider, this.this$0.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            this.statusBarKeyguardViewManagerProvider = new DelegateFactory();
            this.dismissCallbackRegistryProvider = DoubleCheck.provider(DismissCallbackRegistry_Factory.create(this.this$0.provideUiBackgroundExecutorProvider));
            SecureSettingsImpl_Factory create8 = SecureSettingsImpl_Factory.create(this.this$0.provideContentResolverProvider);
            this.secureSettingsImplProvider = create8;
            Provider<DeviceProvisionedControllerImpl> provider8 = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(create8, this.globalSettingsImplProvider, this.provideUserTrackerProvider, this.this$0.dumpManagerProvider, this.provideBgHandlerProvider, this.this$0.provideMainExecutorProvider));
            this.deviceProvisionedControllerImplProvider = provider8;
            this.providesDeviceProvisionedControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory.create(provider8));
            Provider<Optional<CentralSurfaces>> r1 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.optionalOfCentralSurfacesProvider = r1;
            Provider<ActivityStarterDelegate> provider9 = DoubleCheck.provider(ActivityStarterDelegate_Factory.create(r1));
            this.activityStarterDelegateProvider = provider9;
            this.provideActivityStarterProvider = PluginModule_ProvideActivityStarterFactory.create(provider9, this.this$0.pluginDependencyProvider);
            this.builderProvider2 = WakeLock_Builder_Factory.create(this.this$0.contextProvider);
            this.broadcastSenderProvider = DoubleCheck.provider(BroadcastSender_Factory.create(this.this$0.contextProvider, this.builderProvider2, this.provideBackgroundExecutorProvider));
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(this.this$0.provideTelephonyManagerProvider, this.this$0.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            Provider<Looper> provider10 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider10;
            this.provideLongRunningExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider10));
            this.provideDialogLaunchAnimatorProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory.create(this.this$0.provideIDreamManagerProvider));
            this.userSwitcherControllerProvider = DoubleCheck.provider(UserSwitcherController_Factory.create(this.this$0.contextProvider, this.this$0.provideIActivityManagerProvider, this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideMainHandlerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.this$0.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider, this.provideBackgroundExecutorProvider, this.provideLongRunningExecutorProvider, this.this$0.provideMainExecutorProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.provideLatencyTrackerProvider, this.this$0.dumpManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(this.this$0.contextProvider, this.providesDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.this$0.provideUiBackgroundExecutorProvider, this.this$0.dumpManagerProvider));
            this.navigationBarControllerProvider = new DelegateFactory();
            this.alwaysOnDisplayPolicyProvider = DoubleCheck.provider(AlwaysOnDisplayPolicy_Factory.create(this.this$0.contextProvider));
            this.provideFlagManagerProvider = FlagsModule_ProvideFlagManagerFactory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider);
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            this.featureFlagsDebugProvider = DoubleCheck.provider(FeatureFlagsDebug_Factory.create(this.provideFlagManagerProvider, this.this$0.contextProvider, this.secureSettingsImplProvider, this.systemPropertiesHelperProvider, this.this$0.provideResourcesProvider, this.this$0.dumpManagerProvider, FlagsModule_ProvidesAllFlagsFactory.create(), this.commandRegistryProvider, this.this$0.provideIStatusBarServiceProvider));
            this.sysUIUnfoldComponentFactoryProvider = new Provider<SysUIUnfoldComponent.Factory>() {
                public SysUIUnfoldComponent.Factory get() {
                    return new SysUIUnfoldComponentFactory();
                }
            };
            this.provideSysUIUnfoldComponentProvider = DoubleCheck.provider(SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory.create(sysUIUnfoldModule, this.this$0.unfoldTransitionProgressProvider, this.this$0.provideNaturalRotationProgressProvider, this.this$0.provideStatusBarScopedTransitionProvider, this.sysUIUnfoldComponentFactoryProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(this.this$0.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.this$0.dumpManagerProvider));
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.dozeParametersProvider = new DelegateFactory();
            Provider<UnlockedScreenOffAnimationController> provider11 = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(this.this$0.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider, this.globalSettingsImplProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.providePowerManagerProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
            this.unlockedScreenOffAnimationControllerProvider = provider11;
            this.screenOffAnimationControllerProvider = DoubleCheck.provider(ScreenOffAnimationController_Factory.create(this.provideSysUIUnfoldComponentProvider, provider11, this.wakefulnessLifecycleProvider));
            Provider<DozeParameters> provider12 = this.dozeParametersProvider;
            Provider<DozeParameters> provider13 = provider12;
            DelegateFactory.setDelegate(provider13, DoubleCheck.provider(DozeParameters_Factory.create(this.this$0.contextProvider, this.provideBgHandlerProvider, this.this$0.provideResourcesProvider, this.this$0.provideAmbientDisplayConfigurationProvider, this.alwaysOnDisplayPolicyProvider, this.this$0.providePowerManagerProvider, this.provideBatteryControllerProvider, this.tunerServiceImplProvider, this.this$0.dumpManagerProvider, this.featureFlagsDebugProvider, this.screenOffAnimationControllerProvider, this.provideSysUIUnfoldComponentProvider, this.unlockedScreenOffAnimationControllerProvider, this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider)));
            this.notifLiveDataStoreImplProvider = DoubleCheck.provider(NotifLiveDataStoreImpl_Factory.create(this.this$0.provideMainExecutorProvider));
            this.notifPipelineFlagsProvider = NotifPipelineFlags_Factory.create(this.this$0.contextProvider, this.featureFlagsDebugProvider);
            Provider<LogBuffer> provider14 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider14;
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(provider14);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(this.this$0.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.notifPipelineFlagsProvider, this.notifCollectionLoggerProvider, this.this$0.provideMainHandlerProvider, this.logBufferEulogizerProvider, this.this$0.dumpManagerProvider));
            this.notifPipelineChoreographerImplProvider = DoubleCheck.provider(NotifPipelineChoreographerImpl_Factory.create(this.this$0.providesChoreographerProvider, this.this$0.provideMainDelayableExecutorProvider));
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(this.this$0.provideIStatusBarServiceProvider, this.this$0.provideMainExecutorProvider));
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            Provider<ExtensionControllerImpl> provider15 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(this.this$0.contextProvider, this.providesLeakDetectorProvider, this.this$0.providesPluginManagerProvider, this.tunerServiceImplProvider, this.configurationControllerImplProvider));
            this.extensionControllerImplProvider = provider15;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider15));
            this.notificationGroupManagerLegacyProvider = new DelegateFactory();
        }

        public final void initialize2(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            Provider<GroupMembershipManager> provider = DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.notifPipelineFlagsProvider, this.notificationGroupManagerLegacyProvider));
            this.provideGroupMembershipManagerProvider = provider;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, provider));
            Factory<Bubbles> create = InstanceFactory.create(optional8);
            this.setBubblesProvider = create;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create, this.this$0.dumpManagerProvider)));
            this.notificationLockscreenUserManagerImplProvider = new DelegateFactory();
            this.provideNotificationVisibilityProvider = new DelegateFactory();
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideSmartReplyControllerFactory.create(this.this$0.dumpManagerProvider, this.provideNotificationVisibilityProvider, this.this$0.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.provideNotificationEntryManagerProvider = new DelegateFactory();
            this.remoteInputNotificationRebuilderProvider = DoubleCheck.provider(RemoteInputNotificationRebuilder_Factory.create(this.this$0.contextProvider));
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(this.this$0.provideIStatusBarServiceProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifInteractionLogBufferProvider = provider2;
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(provider2);
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory.create(this.this$0.contextProvider, this.notifPipelineFlagsProvider, this.notificationLockscreenUserManagerImplProvider, this.provideSmartReplyControllerProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.remoteInputNotificationRebuilderProvider, this.optionalOfCentralSurfacesProvider, this.statusBarStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider, this.this$0.dumpManagerProvider));
            this.provideCommonNotifCollectionProvider = new DelegateFactory();
            NotifBindPipelineLogger_Factory create2 = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create2;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create2, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create3 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create3;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create3);
            Provider<BindEventManagerImpl> provider3 = DoubleCheck.provider(BindEventManagerImpl_Factory.create());
            this.bindEventManagerImplProvider = provider3;
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(provider3, this.notificationGroupManagerLegacyProvider, this.this$0.contextProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineFlagsProvider, this.this$0.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(this.this$0.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.mediaFeatureFlagProvider = MediaFeatureFlag_Factory.create(this.this$0.contextProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideActivityManagerWrapperFactory.create(sharedLibraryModule));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory.create(sharedLibraryModule));
            Provider<KeyguardDismissUtil> provider4 = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            this.keyguardDismissUtilProvider = provider4;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, provider4, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, this.this$0.contextProvider);
            Provider<LogBuffer> provider5 = DoubleCheck.provider(LogModule_ProvideNotificationHeadsUpLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationHeadsUpLogBufferProvider = provider5;
            this.headsUpManagerLoggerProvider = HeadsUpManagerLogger_Factory.create(provider5);
            this.keyguardBypassControllerProvider = new DelegateFactory();
            this.visualStabilityProvider = DoubleCheck.provider(VisualStabilityProvider_Factory.create());
            Provider<HeadsUpManagerPhone> provider6 = DoubleCheck.provider(TvSystemUIModule_ProvideHeadsUpManagerPhoneFactory.create(this.this$0.contextProvider, this.headsUpManagerLoggerProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, this.visualStabilityProvider, this.configurationControllerImplProvider));
            this.provideHeadsUpManagerPhoneProvider = provider6;
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityStarterProvider, this.provideSmartReplyControllerProvider, provider6);
            SmartReplyStateInflaterImpl_Factory create4 = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, this.this$0.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create4;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create4));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create5 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create5;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create5));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() {
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder();
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(this.this$0.contextProvider);
            this.iconManagerProvider = DoubleCheck.provider(IconManager_Factory.create(this.provideCommonNotifCollectionProvider, this.this$0.provideLauncherAppsProvider, this.iconBuilderProvider));
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider, this.notifPipelineFlagsProvider));
            Provider<NotificationRowBinderImpl> provider7 = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider, this.notifPipelineFlagsProvider));
            this.notificationRowBinderImplProvider = provider7;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.notifPipelineFlagsProvider, provider7, this.provideNotificationRemoteInputManagerProvider, this.providesLeakDetectorProvider, this.this$0.provideIStatusBarServiceProvider, this.notifLiveDataStoreImplProvider, this.this$0.dumpManagerProvider)));
            this.notificationInteractionTrackerProvider = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, this.provideNotificationEntryManagerProvider));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeListBuilderProvider = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.this$0.dumpManagerProvider, this.notifPipelineChoreographerImplProvider, this.notifPipelineFlagsProvider, this.notificationInteractionTrackerProvider, this.shadeListBuilderLoggerProvider, this.bindSystemClockProvider));
            Provider<RenderStageManager> provider8 = DoubleCheck.provider(RenderStageManager_Factory.create());
            this.renderStageManagerProvider = provider8;
            Provider<NotifPipeline> provider9 = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifPipelineFlagsProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, provider8));
            this.notifPipelineProvider = provider9;
            DelegateFactory.setDelegate(this.provideCommonNotifCollectionProvider, DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.notifPipelineFlagsProvider, provider9, this.provideNotificationEntryManagerProvider)));
            NotificationVisibilityProviderImpl_Factory create6 = NotificationVisibilityProviderImpl_Factory.create(this.notifLiveDataStoreImplProvider, this.provideCommonNotifCollectionProvider);
            this.notificationVisibilityProviderImplProvider = create6;
            DelegateFactory.setDelegate(this.provideNotificationVisibilityProvider, DoubleCheck.provider(create6));
            DelegateFactory.setDelegate(this.notificationLockscreenUserManagerImplProvider, DoubleCheck.provider(NotificationLockscreenUserManagerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideUserManagerProvider, this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.notificationClickNotifierProvider, this.this$0.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, this.this$0.provideMainHandlerProvider, this.providesDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.secureSettingsImplProvider, this.this$0.dumpManagerProvider)));
            DelegateFactory.setDelegate(this.keyguardBypassControllerProvider, DoubleCheck.provider(KeyguardBypassController_Factory.create(this.this$0.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.this$0.dumpManagerProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            this.authControllerProvider = new DelegateFactory();
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.this$0.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.screenOffAnimationControllerProvider, this.authControllerProvider));
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create(this.this$0.dumpManagerProvider));
            this.setPipProvider = InstanceFactory.create(optional);
            this.setLegacySplitScreenControllerProvider = InstanceFactory.create(optional3);
            this.setSplitScreenProvider = InstanceFactory.create(optional4);
            this.setSplitScreenControllerProvider = InstanceFactory.create(optional5);
            this.setOneHandedProvider = InstanceFactory.create(optional7);
            this.setRecentTasksProvider = InstanceFactory.create(optional15);
            this.setBackAnimationProvider = InstanceFactory.create(optional18);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional12);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            Provider<LogBuffer> provider10 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider10;
            this.dozeLoggerProvider = DozeLogger_Factory.create(provider10);
            Provider<DozeLog> provider11 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider, this.dozeLoggerProvider));
            this.dozeLogProvider = provider11;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider11, this.statusBarStateControllerImplProvider));
            C0006LightBarTransitionsController_Factory create7 = C0006LightBarTransitionsController_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider);
            this.lightBarTransitionsControllerProvider = create7;
            this.factoryProvider = LightBarTransitionsController_Factory_Impl.create(create7);
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(this.this$0.contextProvider, this.factoryProvider, this.this$0.dumpManagerProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(C0005LightBarController_Factory.create(this.this$0.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider, this.this$0.dumpManagerProvider));
            this.builderProvider3 = DelayedWakeLock_Builder_Factory.create(this.this$0.contextProvider);
            this.keyguardUnlockAnimationControllerProvider = new DelegateFactory();
            this.scrimControllerProvider = DoubleCheck.provider(ScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, this.this$0.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider3, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.configurationControllerImplProvider, this.this$0.provideMainExecutorProvider, this.screenOffAnimationControllerProvider, this.panelExpansionStateManagerProvider, this.keyguardUnlockAnimationControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(this.this$0.contextProvider));
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.bootCompleteCacheImplProvider, this.statusBarStateControllerImplProvider));
            this.overviewProxyServiceProvider = new DelegateFactory();
            this.assistLoggerProvider = DoubleCheck.provider(AssistLogger_Factory.create(this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider));
            this.assistManagerProvider = new DelegateFactory();
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(this.this$0.contextProvider, this.assistLoggerProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideMetricsLoggerProvider, this.assistManagerProvider));
            DelegateFactory.setDelegate(this.assistManagerProvider, DoubleCheck.provider(AssistManager_Factory.create(this.providesDeviceProvisionedControllerProvider, this.this$0.contextProvider, this.provideAssistUtilsProvider, this.provideCommandQueueProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.defaultUiControllerProvider, this.assistLoggerProvider, this.this$0.provideMainHandlerProvider)));
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.provideWindowManagerProvider, this.optionalOfCentralSurfacesProvider, this.assistManagerProvider));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            this.mediaControllerFactoryProvider = MediaControllerFactory_Factory.create(this.this$0.contextProvider);
            Provider<LogBuffer> provider12 = DoubleCheck.provider(LogModule_ProvidesMediaTimeoutListenerLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providesMediaTimeoutListenerLogBufferProvider = provider12;
            this.mediaTimeoutLoggerProvider = DoubleCheck.provider(MediaTimeoutLogger_Factory.create(provider12));
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(this.mediaControllerFactoryProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaTimeoutLoggerProvider, this.statusBarStateControllerImplProvider, this.bindSystemClockProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(this.this$0.contextProvider);
            Provider<LogBuffer> provider13 = DoubleCheck.provider(LogModule_ProvideMediaBrowserBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaBrowserBufferProvider = provider13;
            this.resumeMediaBrowserLoggerProvider = DoubleCheck.provider(ResumeMediaBrowserLogger_Factory.create(provider13));
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(this.this$0.contextProvider, this.mediaBrowserFactoryProvider, this.resumeMediaBrowserLoggerProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(this.this$0.contextProvider, this.this$0.provideMediaSessionManagerProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(SettingsLibraryModule_ProvideLocalBluetoothControllerFactory.create(this.this$0.contextProvider, this.provideBgHandlerProvider));
            this.localMediaManagerFactoryProvider = LocalMediaManagerFactory_Factory.create(this.this$0.contextProvider, this.provideLocalBluetoothControllerProvider);
            this.mediaFlagsProvider = DoubleCheck.provider(MediaFlags_Factory.create(this.featureFlagsDebugProvider));
        }

        public final void initialize3(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideMediaMuteAwaitLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaMuteAwaitLogBufferProvider = provider;
            this.mediaMuteAwaitLoggerProvider = DoubleCheck.provider(MediaMuteAwaitLogger_Factory.create(provider));
            this.mediaMuteAwaitConnectionManagerFactoryProvider = DoubleCheck.provider(MediaMuteAwaitConnectionManagerFactory_Factory.create(this.mediaFlagsProvider, this.this$0.contextProvider, this.mediaMuteAwaitLoggerProvider, this.this$0.provideMainExecutorProvider));
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.mediaControllerFactoryProvider, this.localMediaManagerFactoryProvider, this.this$0.provideMediaRouter2ManagerProvider, this.mediaMuteAwaitConnectionManagerFactoryProvider, this.configurationControllerImplProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.this$0.dumpManagerProvider);
            this.mediaUiEventLoggerProvider = DoubleCheck.provider(MediaUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.notificationLockscreenUserManagerImplProvider, this.this$0.provideMainExecutorProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.this$0.dumpManagerProvider, this.broadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.provideActivityStarterProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider, this.mediaFlagsProvider, this.mediaUiEventLoggerProvider));
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationMediaManagerFactory.create(this.this$0.contextProvider, this.optionalOfCentralSurfacesProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.notifPipelineFlagsProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.this$0.dumpManagerProvider));
            this.sessionTrackerProvider = DoubleCheck.provider(SessionTracker_Factory.create(this.this$0.contextProvider, this.this$0.provideIStatusBarServiceProvider, this.authControllerProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.scrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.this$0.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.this$0.provideMetricsLoggerProvider, this.this$0.dumpManagerProvider, this.this$0.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.this$0.screenLifecycleProvider, this.authControllerProvider, this.statusBarStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.sessionTrackerProvider, this.this$0.provideLatencyTrackerProvider, this.screenOffAnimationControllerProvider));
            DelegateFactory.setDelegate(this.keyguardUnlockAnimationControllerProvider, DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(this.this$0.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.featureFlagsDebugProvider, this.biometricUnlockControllerProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider)));
            DelegateFactory.setDelegate(this.overviewProxyServiceProvider, DoubleCheck.provider(OverviewProxyService_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.navigationBarControllerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setLegacySplitScreenControllerProvider, this.setSplitScreenProvider, this.setSplitScreenControllerProvider, this.setOneHandedProvider, this.setRecentTasksProvider, this.setBackAnimationProvider, this.setStartingSurfaceProvider, this.broadcastDispatcherProvider, this.setTransitionsProvider, this.this$0.screenLifecycleProvider, this.this$0.provideUiEventLoggerProvider, this.keyguardUnlockAnimationControllerProvider, this.provideAssistUtilsProvider, this.this$0.dumpManagerProvider)));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(this.this$0.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(this.this$0.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(this.this$0.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider2 = DoubleCheck.provider(TvSystemUIModule_ProvideRecentsFactory.create(this.this$0.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider2;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.of(provider2);
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(this.this$0.contextProvider, this.notificationShadeWindowControllerImplProvider, this.optionalOfCentralSurfacesProvider, this.optionalOfRecentsProvider));
            this.navBarHelperProvider = DoubleCheck.provider(NavBarHelper_Factory.create(this.this$0.contextProvider, this.this$0.provideAccessibilityManagerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.systemActionsProvider, this.overviewProxyServiceProvider, this.assistManagerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.provideUserTrackerProvider, this.this$0.dumpManagerProvider));
            this.factoryProvider2 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.this$0.providesPluginManagerProvider, this.this$0.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, this.this$0.provideViewConfigurationProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIWindowManagerProvider, this.falsingManagerProxyProvider, this.this$0.provideLatencyTrackerProvider);
            this.taskbarDelegateProvider = DoubleCheck.provider(TaskbarDelegate_Factory.create(this.this$0.contextProvider, this.factoryProvider2, this.factoryProvider));
            this.navigationBarComponentFactoryProvider = new Provider<NavigationBarComponent.Factory>() {
                public NavigationBarComponent.Factory get() {
                    return new NavigationBarComponentFactory();
                }
            };
            this.autoHideControllerProvider = DoubleCheck.provider(C0004AutoHideController_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideIWindowManagerProvider));
            Provider<NavigationBarController> provider3 = this.navigationBarControllerProvider;
            Provider<NavigationBarController> provider4 = provider3;
            DelegateFactory.setDelegate(provider4, DoubleCheck.provider(NavigationBarController_Factory.create(this.this$0.contextProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.provideSysUiStateProvider, this.provideCommandQueueProvider, this.this$0.provideMainHandlerProvider, this.configurationControllerImplProvider, this.navBarHelperProvider, this.taskbarDelegateProvider, this.navigationBarComponentFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.dumpManagerProvider, this.autoHideControllerProvider, this.lightBarControllerProvider, this.setPipProvider, this.setBackAnimationProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() {
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory();
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(this.this$0.contextProvider, this.navigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.this$0.provideUiBackgroundExecutorProvider);
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(this.this$0.provideResourcesProvider, this.this$0.provideCrossWindowBlurListenersProvider, this.this$0.dumpManagerProvider));
            this.wallpaperControllerProvider = DoubleCheck.provider(WallpaperController_Factory.create(this.this$0.provideWallpaperManagerProvider));
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, this.this$0.providesChoreographerProvider, this.wallpaperControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.configurationControllerImplProvider));
            this.screenOnCoordinatorProvider = DoubleCheck.provider(ScreenOnCoordinator_Factory.create(this.this$0.screenLifecycleProvider, this.provideSysUIUnfoldComponentProvider, this.this$0.provideExecutionProvider));
            this.dreamOverlayStateControllerProvider = DoubleCheck.provider(DreamOverlayStateController_Factory.create(this.this$0.provideMainExecutorProvider));
            this.provideActivityLaunchAnimatorProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory.create());
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(this.this$0.contextProvider, this.falsingCollectorImplProvider, this.this$0.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider, this.this$0.providePowerManagerProvider, this.this$0.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.screenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider, this.screenOnCoordinatorProvider, this.this$0.provideInteractionJankMonitorProvider, this.dreamOverlayStateControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideActivityLaunchAnimatorProvider)));
            this.providesViewMediatorCallbackProvider = KeyguardModule_ProvidesViewMediatorCallbackFactory.create(keyguardModule, this.newKeyguardViewMediatorProvider);
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(this.this$0.provideResourcesProvider, this.this$0.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() {
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory();
                }
            };
            this.factoryProvider3 = KeyguardBouncer_Factory_Factory.create(this.this$0.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.this$0.provideMainHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.factoryProvider4 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider);
            DelegateFactory.setDelegate(this.statusBarKeyguardViewManagerProvider, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(this.this$0.contextProvider, this.providesViewMediatorCallbackProvider, this.this$0.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dreamOverlayStateControllerProvider, this.navigationModeControllerProvider, this.dockManagerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider3, this.factoryProvider4, this.provideSysUIUnfoldComponentProvider, this.shadeControllerImplProvider, this.this$0.provideLatencyTrackerProvider)));
            this.provideLSShadeTransitionControllerBufferProvider = DoubleCheck.provider(LogModule_ProvideLSShadeTransitionControllerBufferFactory.create(this.logBufferFactoryProvider));
            Provider<LockscreenGestureLogger> provider5 = DoubleCheck.provider(LockscreenGestureLogger_Factory.create(this.this$0.provideMetricsLoggerProvider));
            this.lockscreenGestureLoggerProvider = provider5;
            this.lSShadeTransitionLoggerProvider = LSShadeTransitionLogger_Factory.create(this.provideLSShadeTransitionControllerBufferProvider, provider5, this.this$0.provideDisplayMetricsProvider);
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            Provider<LogBuffer> provider6 = DoubleCheck.provider(LogModule_ProvideMediaViewLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaViewLogBufferProvider = provider6;
            this.mediaViewLoggerProvider = DoubleCheck.provider(MediaViewLogger_Factory.create(provider6));
            this.mediaViewControllerProvider = MediaViewController_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.mediaHostStatesManagerProvider, this.mediaViewLoggerProvider);
            Provider<DelayableExecutor> provider7 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBackgroundDelayableExecutorProvider = provider7;
            Provider<RepeatableExecutor> provider8 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory.create(provider7));
            this.provideBackgroundRepeatableExecutorProvider = provider8;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider8);
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideNearbyMediaDevicesLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNearbyMediaDevicesLogBufferProvider = provider9;
            Provider<NearbyMediaDevicesLogger> provider10 = DoubleCheck.provider(NearbyMediaDevicesLogger_Factory.create(provider9));
            this.nearbyMediaDevicesLoggerProvider = provider10;
            Provider<NearbyMediaDevicesManager> provider11 = DoubleCheck.provider(NearbyMediaDevicesManager_Factory.create(this.provideCommandQueueProvider, provider10));
            this.nearbyMediaDevicesManagerProvider = provider11;
            this.providesNearbyMediaDevicesManagerProvider = DoubleCheck.provider(MediaModule_ProvidesNearbyMediaDevicesManagerFactory.create(this.mediaFlagsProvider, provider11));
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.this$0.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.this$0.provideAudioManagerProvider, this.this$0.providePowerExemptionManagerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(this.this$0.contextProvider));
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider, this.falsingManagerProxyProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider, this.keyguardStateControllerImplProvider, this.activityIntentHelperProvider, this.notificationLockscreenUserManagerImplProvider);
            Provider<LogBuffer> provider12 = DoubleCheck.provider(LogModule_ProvideMediaCarouselControllerBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaCarouselControllerBufferProvider = provider12;
            this.mediaCarouselControllerLoggerProvider = DoubleCheck.provider(MediaCarouselControllerLogger_Factory.create(provider12));
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(this.this$0.contextProvider, this.mediaControlPanelProvider, this.visualStabilityProvider, this.mediaHostStatesManagerProvider, this.provideActivityStarterProvider, this.bindSystemClockProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.this$0.dumpManagerProvider, this.mediaUiEventLoggerProvider, this.mediaCarouselControllerLoggerProvider)));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(this.this$0.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.configurationControllerImplProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider, this.dreamOverlayStateControllerProvider));
            Provider<MediaHost> provider13 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider13;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider13, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.this$0.contextProvider, this.configurationControllerImplProvider));
            this.notificationSectionsFeatureManagerProvider = DoubleCheck.provider(NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, this.this$0.contextProvider));
            Provider<LogBuffer> provider14 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider14;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider14));
            this.mediaContainerControllerProvider = DoubleCheck.provider(MediaContainerController_Factory.create(this.this$0.providerLayoutInflaterProvider));
            AnonymousClass6 r1 = new Provider<SectionHeaderControllerSubcomponent.Builder>() {
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder();
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = r1;
            Provider<SectionHeaderControllerSubcomponent> provider15 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory.create(r1));
            this.providesIncomingHeaderSubcomponentProvider = provider15;
            this.providesIncomingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory.create(provider15);
            Provider<SectionHeaderControllerSubcomponent> provider16 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider16;
            this.providesPeopleHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory.create(provider16);
            Provider<SectionHeaderControllerSubcomponent> provider17 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider17;
            this.providesAlertingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory.create(provider17);
            Provider<SectionHeaderControllerSubcomponent> provider18 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider18;
            NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory create = NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory.create(provider18);
            this.providesSilentHeaderControllerProvider = create;
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.notifPipelineFlagsProvider, this.mediaContainerControllerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, create);
            this.ambientStateProvider = DoubleCheck.provider(AmbientState_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.lockscreenShadeScrimTransitionControllerProvider = LockscreenShadeScrimTransitionController_Factory.create(this.scrimControllerProvider, this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider);
            C0001LockscreenShadeKeyguardTransitionController_Factory create2 = C0001LockscreenShadeKeyguardTransitionController_Factory.create(this.mediaHierarchyManagerProvider, this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider);
            this.lockscreenShadeKeyguardTransitionControllerProvider = create2;
            this.factoryProvider5 = LockscreenShadeKeyguardTransitionController_Factory_Impl.create(create2);
            C0003SplitShadeLockScreenOverScroller_Factory create3 = C0003SplitShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.this$0.contextProvider, this.scrimControllerProvider, this.statusBarStateControllerImplProvider);
            this.splitShadeLockScreenOverScrollerProvider = create3;
            this.factoryProvider6 = SplitShadeLockScreenOverScroller_Factory_Impl.create(create3);
            C0002SingleShadeLockScreenOverScroller_Factory create4 = C0002SingleShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.this$0.contextProvider, this.statusBarStateControllerImplProvider);
            this.singleShadeLockScreenOverScrollerProvider = create4;
            this.factoryProvider7 = SingleShadeLockScreenOverScroller_Factory_Impl.create(create4);
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lSShadeTransitionLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.falsingCollectorImplProvider, this.ambientStateProvider, this.mediaHierarchyManagerProvider, this.lockscreenShadeScrimTransitionControllerProvider, this.factoryProvider5, this.notificationShadeDepthControllerProvider, this.this$0.contextProvider, this.factoryProvider6, this.factoryProvider7, this.wakefulnessLifecycleProvider, this.configurationControllerImplProvider, this.falsingManagerProxyProvider, this.this$0.dumpManagerProvider));
            Provider<VibratorHelper> provider19 = DoubleCheck.provider(VibratorHelper_Factory.create(this.this$0.provideVibratorProvider, this.provideBackgroundExecutorProvider));
            this.vibratorHelperProvider = provider19;
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, provider19, this.keyguardUpdateMonitorProvider));
            this.udfpsShellProvider = DoubleCheck.provider(UdfpsShell_Factory.create(this.commandRegistryProvider));
            this.optionalOfUdfpsHbmProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.systemUIDialogManagerProvider = DoubleCheck.provider(SystemUIDialogManager_Factory.create(this.this$0.dumpManagerProvider, this.statusBarKeyguardViewManagerProvider));
            this.optionalOfAlternateUdfpsTouchProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.providesPluginExecutorProvider = DoubleCheck.provider(BiometricsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(this.this$0.contextProvider, this.this$0.provideExecutionProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesFingerprintManagerProvider, this.this$0.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.this$0.provideMainDelayableExecutorProvider, this.panelExpansionStateManagerProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.falsingManagerProxyProvider, this.this$0.providePowerManagerProvider, this.this$0.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.this$0.screenLifecycleProvider, this.vibratorHelperProvider, this.udfpsHapticsSimulatorProvider, this.udfpsShellProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.this$0.provideDisplayManagerProvider, this.this$0.provideMainHandlerProvider, this.configurationControllerImplProvider, this.bindSystemClockProvider, this.unlockedScreenOffAnimationControllerProvider, this.systemUIDialogManagerProvider, this.this$0.provideLatencyTrackerProvider, this.provideActivityLaunchAnimatorProvider, this.optionalOfAlternateUdfpsTouchProvider, this.providesPluginExecutorProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(this.this$0.contextProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesFingerprintManagerProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideActivityTaskManagerProvider, this.overviewProxyServiceProvider, this.this$0.provideDisplayManagerProvider, this.this$0.provideMainDelayableExecutorProvider, this.this$0.provideMainHandlerProvider));
            Provider<AuthController> provider20 = this.authControllerProvider;
            Provider r2 = this.this$0.contextProvider;
            Provider r3 = this.this$0.provideExecutionProvider;
            Provider<CommandQueue> provider21 = this.provideCommandQueueProvider;
            Provider r5 = this.this$0.provideActivityTaskManagerProvider;
            Provider r6 = this.this$0.provideWindowManagerProvider;
            Provider r7 = this.this$0.providesFingerprintManagerProvider;
            Provider r8 = this.this$0.provideFaceManagerProvider;
            Provider<UdfpsController> provider22 = this.udfpsControllerProvider;
            Provider<SidefpsController> provider23 = this.sidefpsControllerProvider;
            Provider r11 = this.this$0.provideDisplayManagerProvider;
            Provider<WakefulnessLifecycle> provider24 = this.wakefulnessLifecycleProvider;
            Provider r13 = this.this$0.provideUserManagerProvider;
            Provider r14 = this.this$0.provideLockPatternUtilsProvider;
            Provider<AuthController> provider25 = provider20;
            DelegateFactory.setDelegate(provider25, DoubleCheck.provider(AuthController_Factory.create(r2, r3, provider21, r5, r6, r7, r8, provider22, provider23, r11, provider24, r13, r14, this.statusBarStateControllerImplProvider, this.this$0.provideMainHandlerProvider, this.provideBackgroundDelayableExecutorProvider)));
            this.activeUnlockConfigProvider = DoubleCheck.provider(ActiveUnlockConfig_Factory.create(this.this$0.provideMainHandlerProvider, this.secureSettingsImplProvider, this.this$0.provideContentResolverProvider, this.this$0.dumpManagerProvider));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(this.this$0.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.this$0.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.provideLatencyTrackerProvider, this.activeUnlockConfigProvider)));
            DelegateFactory.setDelegate(this.keyguardStateControllerImplProvider, DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(this.this$0.contextProvider, this.keyguardUpdateMonitorProvider, this.this$0.provideLockPatternUtilsProvider, this.keyguardUnlockAnimationControllerProvider, this.this$0.dumpManagerProvider)));
        }

        public final void initialize4(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.this$0.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, this.historyTrackerProvider, this.keyguardStateControllerImplProvider, this.this$0.provideAccessibilityManagerProvider, this.this$0.provideIsTestHarnessProvider);
            DelegateFactory.setDelegate(this.falsingManagerProxyProvider, DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.this$0.providesPluginManagerProvider, this.this$0.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.this$0.dumpManagerProvider, this.brightLineFalsingManagerProvider)));
            BrightnessSliderController_Factory_Factory create = BrightnessSliderController_Factory_Factory.create(this.falsingManagerProxyProvider);
            this.factoryProvider8 = create;
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.broadcastDispatcherProvider, create, this.provideBgHandlerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbPermissionActivityProvider = UsbPermissionActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            this.usbConfirmActivityProvider = UsbConfirmActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            UserCreator_Factory create2 = UserCreator_Factory.create(this.this$0.contextProvider, this.this$0.provideUserManagerProvider);
            this.userCreatorProvider = create2;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(create2, UserModule_ProvideEditUserInfoControllerFactory.create(), this.this$0.provideIActivityManagerProvider);
            this.notificationListenerProvider = DoubleCheck.provider(NotificationListener_Factory.create(this.this$0.contextProvider, this.this$0.provideNotificationManagerProvider, this.bindSystemClockProvider, this.this$0.provideMainExecutorProvider, this.this$0.providesPluginManagerProvider));
            Provider<TvNotificationHandler> provider = DoubleCheck.provider(TvSystemUIModule_ProvideTvNotificationHandlerFactory.create(this.this$0.contextProvider, this.notificationListenerProvider));
            this.provideTvNotificationHandlerProvider = provider;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(provider);
            Provider<PeopleSpaceWidgetManager> provider2 = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(this.this$0.contextProvider, this.this$0.provideLauncherAppsProvider, this.provideCommonNotifCollectionProvider, this.this$0.providePackageManagerProvider, this.setBubblesProvider, this.this$0.provideUserManagerProvider, this.this$0.provideNotificationManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider2;
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(provider2);
            this.imageExporterProvider = ImageExporter_Factory.create(this.this$0.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(this.this$0.provideUiEventLoggerProvider, this.imageExporterProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.debugModeFilterProvider = DoubleCheck.provider(DebugModeFilterProvider_Factory.create(this.commandRegistryProvider, this.this$0.dumpManagerProvider));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.providesDeviceProvisionedControllerProvider));
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideIndividualSensorPrivacyControllerFactory.create(this.this$0.provideSensorPrivacyManagerProvider));
            Provider<AppOpsControllerImpl> provider3 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgLooperProvider, this.this$0.dumpManagerProvider, this.this$0.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.broadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider3;
            Provider<ForegroundServiceController> provider4 = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider3, this.this$0.provideMainHandlerProvider));
            this.foregroundServiceControllerProvider = provider4;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.debugModeFilterProvider, this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider4, this.notificationLockscreenUserManagerImplProvider, this.mediaFeatureFlagProvider));
            this.notificationInterruptLoggerProvider = NotificationInterruptLogger_Factory.create(this.provideNotificationsLogBufferProvider, this.provideNotificationHeadsUpLogBufferProvider);
            this.highPriorityProvider = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.keyguardNotificationVisibilityProviderImplProvider = DoubleCheck.provider(KeyguardNotificationVisibilityProviderImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider, this.statusBarStateControllerImplProvider, this.broadcastDispatcherProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(this.this$0.provideContentResolverProvider, this.this$0.providePowerManagerProvider, this.this$0.provideIDreamManagerProvider, this.this$0.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationInterruptLoggerProvider, this.this$0.provideMainHandlerProvider, this.notifPipelineFlagsProvider, this.keyguardNotificationVisibilityProviderImplProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider, this.globalSettingsImplProvider));
            Provider<Optional<BubblesManager>> provider5 = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(this.this$0.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.shadeControllerImplProvider, this.configurationControllerImplProvider, this.this$0.provideIStatusBarServiceProvider, this.this$0.provideINotificationManagerProvider, this.provideNotificationVisibilityProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.notifPipelineFlagsProvider, this.this$0.dumpManagerProvider, this.this$0.provideMainExecutorProvider));
            this.provideBubblesManagerProvider = provider5;
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, provider5, this.this$0.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            Provider<HdmiCecSetMenuLanguageHelper> provider6 = DoubleCheck.provider(HdmiCecSetMenuLanguageHelper_Factory.create(this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider));
            this.hdmiCecSetMenuLanguageHelperProvider = provider6;
            this.hdmiCecSetMenuLanguageActivityProvider = HdmiCecSetMenuLanguageActivity_Factory.create(provider6);
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(this.this$0.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.provideDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional9);
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(this.this$0.contextProvider, this.provideDelayableExecutorProvider, this.this$0.provideMainDelayableExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.keyguardStateControllerImplProvider, this.setTaskViewFactoryProvider, this.controlsMetricsLoggerImplProvider, this.vibratorHelperProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.this$0.provideMainHandlerProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            this.controlsUiControllerImplProvider = DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, this.this$0.contextProvider, this.this$0.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.this$0.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.this$0.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(this.this$0.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider, this.broadcastDispatcherProvider);
            this.userSwitcherActivityProvider = UserSwitcherActivity_Factory.create(this.userSwitcherControllerProvider, this.broadcastDispatcherProvider, this.this$0.providerLayoutInflaterProvider, this.falsingManagerProxyProvider, this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.provideUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(23).put(TunerActivity.class, this.tunerActivityProvider).put(ForegroundServicesDialog.class, this.foregroundServicesDialogProvider).put(WorkLockActivity.class, this.workLockActivityProvider).put(BrightnessDialog.class, this.brightnessDialogProvider).put(UsbDebuggingActivity.class, this.usbDebuggingActivityProvider).put(UsbDebuggingSecondaryUserActivity.class, this.usbDebuggingSecondaryUserActivityProvider).put(UsbPermissionActivity.class, this.usbPermissionActivityProvider).put(UsbConfirmActivity.class, this.usbConfirmActivityProvider).put(CreateUserActivity.class, this.createUserActivityProvider).put(TvNotificationPanelActivity.class, this.tvNotificationPanelActivityProvider).put(PeopleSpaceActivity.class, this.peopleSpaceActivityProvider).put(LongScreenshotActivity.class, this.longScreenshotActivityProvider).put(LaunchConversationActivity.class, this.launchConversationActivityProvider).put(SensorUseStartedActivity.class, this.sensorUseStartedActivityProvider).put(TvUnblockSensorActivity.class, this.tvUnblockSensorActivityProvider).put(HdmiCecSetMenuLanguageActivity.class, this.hdmiCecSetMenuLanguageActivityProvider).put(ControlsProviderSelectorActivity.class, this.controlsProviderSelectorActivityProvider).put(ControlsFavoritingActivity.class, this.controlsFavoritingActivityProvider).put(ControlsEditingActivity.class, this.controlsEditingActivityProvider).put(ControlsRequestDialog.class, this.controlsRequestDialogProvider).put(ControlsActivity.class, this.controlsActivityProvider).put(UserSwitcherActivity.class, this.userSwitcherActivityProvider).put(WalletActivity.class, this.walletActivityProvider).build();
            AnonymousClass7 r1 = new Provider<DozeComponent.Builder>() {
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory();
                }
            };
            this.dozeComponentBuilderProvider = r1;
            this.dozeServiceProvider = DozeService_Factory.create(r1, this.this$0.providesPluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider7 = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.this$0.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider7;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider7, this.setTransitionsProvider);
            this.dreamOverlayComponentFactoryProvider = new Provider<DreamOverlayComponent.Factory>() {
                public DreamOverlayComponent.Factory get() {
                    return new DreamOverlayComponentFactory();
                }
            };
            this.dreamOverlayServiceProvider = DreamOverlayService_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.dreamOverlayComponentFactoryProvider, this.dreamOverlayStateControllerProvider, this.keyguardUpdateMonitorProvider, this.this$0.provideUiEventLoggerProvider);
            this.notificationListenerWithPluginsProvider = NotificationListenerWithPlugins_Factory.create(this.this$0.providesPluginManagerProvider);
            this.broadcastDispatcherStartableProvider = BroadcastDispatcherStartable_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider);
            this.globalActionsComponentProvider = new DelegateFactory();
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(this.this$0.provideAudioManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(this.this$0.contextProvider, this.globalActionsComponentProvider, this.this$0.provideAudioManagerProvider, this.this$0.provideIDreamManagerProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, this.vibratorHelperProvider, this.this$0.provideResourcesProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.this$0.provideUserManagerProvider, this.this$0.provideTrustManagerProvider, this.this$0.provideIActivityManagerProvider, this.this$0.provideTelecomManagerProvider, this.this$0.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, this.this$0.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, this.this$0.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.this$0.provideUiEventLoggerProvider, this.ringerModeTrackerImplProvider, this.this$0.provideMainHandlerProvider, this.this$0.providePackageManagerProvider, this.optionalOfCentralSurfacesProvider, this.keyguardUpdateMonitorProvider, this.provideDialogLaunchAnimatorProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.provideTaskStackChangeListenersProvider = DoubleCheck.provider(SharedLibraryModule_ProvideTaskStackChangeListenersFactory.create(sharedLibraryModule));
            this.homeSoundEffectControllerProvider = DoubleCheck.provider(HomeSoundEffectController_Factory.create(this.this$0.contextProvider, this.this$0.provideAudioManagerProvider, this.provideTaskStackChangeListenersProvider, this.provideActivityManagerWrapperProvider, this.this$0.providePackageManagerProvider));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.this$0.provideUiBackgroundExecutorProvider));
            this.keyboardUIProvider = DoubleCheck.provider(KeyboardUI_Factory.create(this.this$0.contextProvider));
            this.powerNotificationWarningsProvider = DoubleCheck.provider(PowerNotificationWarnings_Factory.create(this.this$0.contextProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideBatteryControllerProvider, this.provideDialogLaunchAnimatorProvider, this.this$0.provideUiEventLoggerProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideCommandQueueProvider, this.optionalOfCentralSurfacesProvider, this.powerNotificationWarningsProvider, this.enhancedEstimatesImplProvider, this.this$0.providePowerManagerProvider));
            this.ringtonePlayerProvider = DoubleCheck.provider(RingtonePlayer_Factory.create(this.this$0.contextProvider));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(this.this$0.contextProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider));
            this.storageNotificationProvider = DoubleCheck.provider(StorageNotification_Factory.create(this.this$0.contextProvider));
            this.provideLauncherPackageProvider = ThemeModule_ProvideLauncherPackageFactory.create(this.this$0.provideResourcesProvider);
            this.provideThemePickerPackageProvider = ThemeModule_ProvideThemePickerPackageFactory.create(this.this$0.provideResourcesProvider);
            this.themeOverlayApplierProvider = DoubleCheck.provider(ThemeOverlayApplier_Factory.create(this.this$0.provideOverlayManagerProvider, this.provideBackgroundExecutorProvider, this.provideLauncherPackageProvider, this.provideThemePickerPackageProvider, this.this$0.dumpManagerProvider));
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideBgHandlerProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.themeOverlayApplierProvider, this.secureSettingsImplProvider, this.this$0.provideWallpaperManagerProvider, this.this$0.provideUserManagerProvider, this.providesDeviceProvisionedControllerProvider, this.provideUserTrackerProvider, this.this$0.dumpManagerProvider, this.featureFlagsDebugProvider, this.this$0.provideResourcesProvider, this.wakefulnessLifecycleProvider));
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.this$0.providerLayoutInflaterProvider, this.this$0.providesPluginManagerProvider, this.this$0.dumpManagerProvider));
            Provider<LogBuffer> provider8 = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider8;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider8);
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.tvNotificationPanelProvider = DoubleCheck.provider(TvNotificationPanel_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider));
            this.privacyConfigProvider = DoubleCheck.provider(PrivacyConfig_Factory.create(this.this$0.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.this$0.dumpManagerProvider));
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider9;
            PrivacyLogger_Factory create3 = PrivacyLogger_Factory.create(provider9);
            this.privacyLoggerProvider = create3;
            this.appOpsPrivacyItemMonitorProvider = DoubleCheck.provider(AppOpsPrivacyItemMonitor_Factory.create(this.appOpsControllerImplProvider, this.provideUserTrackerProvider, this.privacyConfigProvider, this.provideBackgroundDelayableExecutorProvider, create3));
            this.mediaProjectionPrivacyItemMonitorProvider = DoubleCheck.provider(MediaProjectionPrivacyItemMonitor_Factory.create(this.this$0.provideMediaProjectionManagerProvider, this.this$0.providePackageManagerProvider, this.privacyConfigProvider, this.provideBgHandlerProvider, this.bindSystemClockProvider, this.privacyLoggerProvider));
            this.setOfPrivacyItemMonitorProvider = SetFactory.builder(2, 0).addProvider(this.appOpsPrivacyItemMonitorProvider).addProvider(this.mediaProjectionPrivacyItemMonitorProvider).build();
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.this$0.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.privacyConfigProvider, this.setOfPrivacyItemMonitorProvider, this.privacyLoggerProvider, this.bindSystemClockProvider, this.this$0.dumpManagerProvider));
            this.tvOngoingPrivacyChipProvider = DoubleCheck.provider(TvOngoingPrivacyChip_Factory.create(this.this$0.contextProvider, this.privacyItemControllerProvider, this.this$0.provideIWindowManagerProvider));
            this.tvStatusBarProvider = DoubleCheck.provider(TvStatusBar_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.assistManagerProvider));
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), this.this$0.provideAudioManagerProvider, this.this$0.provideNotificationManagerProvider, this.vibratorHelperProvider, this.this$0.provideIAudioServiceProvider, this.this$0.provideAccessibilityManagerProvider, this.this$0.providePackageManagerProvider, this.wakefulnessLifecycleProvider, this.this$0.provideCaptioningManagerProvider, this.this$0.provideKeyguardManagerProvider, this.this$0.provideActivityManagerProvider));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(this.this$0.provideAccessibilityManagerProvider));
            this.provideVolumeDialogProvider = VolumeModule_ProvideVolumeDialogFactory.create(this.this$0.contextProvider, this.volumeDialogControllerImplProvider, this.accessibilityManagerWrapperProvider, this.providesDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.mediaOutputDialogFactoryProvider, this.provideActivityStarterProvider, this.this$0.provideInteractionJankMonitorProvider);
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(this.this$0.contextProvider, this.newKeyguardViewMediatorProvider, this.provideActivityStarterProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider, this.this$0.pluginDependencyProvider, this.extensionControllerImplProvider, this.tunerServiceImplProvider, this.provideVolumeDialogProvider));
        }

        public final void initialize5(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(this.this$0.contextProvider, this.volumeDialogComponentProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.this$0.dumpManagerProvider));
            this.vpnStatusObserverProvider = DoubleCheck.provider(VpnStatusObserver_Factory.create(this.this$0.contextProvider, this.securityControllerImplProvider));
            this.modeSwitchesControllerProvider = DoubleCheck.provider(ModeSwitchesController_Factory.create(this.this$0.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.provideCommandQueueProvider, this.modeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional10);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional11);
            this.setCompatUIProvider = InstanceFactory.create(optional16);
            this.setDragAndDropProvider = InstanceFactory.create(optional17);
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(this.this$0.contextProvider));
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(this.this$0.contextProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.setCompatUIProvider, this.setDragAndDropProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.this$0.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, this.userInfoControllerImplProvider, this.this$0.provideMainExecutorProvider));
            this.mapOfClassOfAndProviderOfCoreStartableProvider = MapProviderFactory.builder(21).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(HomeSoundEffectController.class, this.homeSoundEffectControllerProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(PowerUI.class, this.powerUIProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(TvNotificationHandler.class, this.provideTvNotificationHandlerProvider).put(TvNotificationPanel.class, this.tvNotificationPanelProvider).put(TvOngoingPrivacyChip.class, this.tvOngoingPrivacyChipProvider).put(TvStatusBar.class, this.tvStatusBarProvider).put(VolumeUI.class, this.volumeUIProvider).put(VpnStatusObserver.class, this.vpnStatusObserverProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).build();
            this.dumpHandlerProvider = DumpHandler_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.logBufferEulogizerProvider, this.mapOfClassOfAndProviderOfCoreStartableProvider, this.this$0.uncaughtExceptionPreHandlerManagerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.this$0.dumpManagerProvider, this.this$0.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, this.this$0.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, this.this$0.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(this.this$0.provideMainHandlerProvider, this.dumpHandlerProvider, this.broadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            Provider<RecordingController> provider = DoubleCheck.provider(RecordingController_Factory.create(this.broadcastDispatcherProvider, this.provideUserTrackerProvider));
            this.recordingControllerProvider = provider;
            this.recordingServiceProvider = RecordingService_Factory.create(provider, this.provideLongRunningExecutorProvider, this.this$0.provideUiEventLoggerProvider, this.this$0.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.create());
            this.screenshotNotificationsControllerProvider = ScreenshotNotificationsController_Factory.create(this.this$0.contextProvider, this.this$0.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = ScrollCaptureClient_Factory.create(this.this$0.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.this$0.contextProvider);
            this.imageTileSetProvider = ImageTileSet_Factory.create(GlobalConcurrencyModule_ProvideHandlerFactory.create());
            this.scrollCaptureControllerProvider = ScrollCaptureController_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, this.imageTileSetProvider, this.this$0.provideUiEventLoggerProvider);
            this.timeoutHandlerProvider = TimeoutHandler_Factory.create(this.this$0.contextProvider);
            ScreenshotController_Factory create = ScreenshotController_Factory.create(this.this$0.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, this.this$0.provideUiEventLoggerProvider, this.imageExporterProvider, this.this$0.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, this.this$0.provideActivityManagerProvider, this.timeoutHandlerProvider, this.broadcastSenderProvider);
            this.screenshotControllerProvider = create;
            this.takeScreenshotServiceProvider = TakeScreenshotService_Factory.create(create, this.this$0.provideUserManagerProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider, this.this$0.contextProvider, this.provideBackgroundExecutorProvider);
            this.mapOfClassOfAndProviderOfServiceProvider = MapProviderFactory.builder(9).put(DozeService.class, this.dozeServiceProvider).put(ImageWallpaper.class, ImageWallpaper_Factory.create()).put(KeyguardService.class, this.keyguardServiceProvider).put(DreamOverlayService.class, this.dreamOverlayServiceProvider).put(NotificationListenerWithPlugins.class, this.notificationListenerWithPluginsProvider).put(SystemUIService.class, this.systemUIServiceProvider).put(SystemUIAuxiliaryDumpService.class, this.systemUIAuxiliaryDumpServiceProvider).put(RecordingService.class, this.recordingServiceProvider).put(TakeScreenshotService.class, this.takeScreenshotServiceProvider).build();
            this.overviewProxyRecentsImplProvider = DoubleCheck.provider(OverviewProxyRecentsImpl_Factory.create(this.optionalOfCentralSurfacesProvider, this.overviewProxyServiceProvider));
            this.mapOfClassOfAndProviderOfRecentsImplementationProvider = MapProviderFactory.builder(1).put(OverviewProxyRecentsImpl.class, this.overviewProxyRecentsImplProvider).build();
            this.actionProxyReceiverProvider = ActionProxyReceiver_Factory.create(this.optionalOfCentralSurfacesProvider, this.provideActivityManagerWrapperProvider, this.screenshotSmartActionsProvider);
            this.deleteScreenshotReceiverProvider = DeleteScreenshotReceiver_Factory.create(this.screenshotSmartActionsProvider, this.provideBackgroundExecutorProvider);
            this.smartActionsReceiverProvider = SmartActionsReceiver_Factory.create(this.screenshotSmartActionsProvider);
            MediaOutputBroadcastDialogFactory_Factory create2 = MediaOutputBroadcastDialogFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.this$0.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.this$0.provideAudioManagerProvider, this.this$0.providePowerExemptionManagerProvider);
            this.mediaOutputBroadcastDialogFactoryProvider = create2;
            this.mediaOutputDialogReceiverProvider = MediaOutputDialogReceiver_Factory.create(this.mediaOutputDialogFactoryProvider, create2);
            this.peopleSpaceWidgetPinnedReceiverProvider = PeopleSpaceWidgetPinnedReceiver_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.peopleSpaceWidgetProvider = PeopleSpaceWidgetProvider_Factory.create(this.peopleSpaceWidgetManagerProvider);
            MapProviderFactory<K, V> build = MapProviderFactory.builder(6).put(ActionProxyReceiver.class, this.actionProxyReceiverProvider).put(DeleteScreenshotReceiver.class, this.deleteScreenshotReceiverProvider).put(SmartActionsReceiver.class, this.smartActionsReceiverProvider).put(MediaOutputDialogReceiver.class, this.mediaOutputDialogReceiverProvider).put(PeopleSpaceWidgetPinnedReceiver.class, this.peopleSpaceWidgetPinnedReceiverProvider).put(PeopleSpaceWidgetProvider.class, this.peopleSpaceWidgetProvider).build();
            this.mapOfClassOfAndProviderOfBroadcastReceiverProvider = build;
            DelegateFactory.setDelegate(this.contextComponentResolverProvider, DoubleCheck.provider(ContextComponentResolver_Factory.create(this.mapOfClassOfAndProviderOfActivityProvider, this.mapOfClassOfAndProviderOfServiceProvider, this.mapOfClassOfAndProviderOfRecentsImplementationProvider, build)));
            this.unfoldLatencyTrackerProvider = DoubleCheck.provider(UnfoldLatencyTracker_Factory.create(this.this$0.provideLatencyTrackerProvider, this.this$0.provideDeviceStateManagerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.this$0.contextProvider, this.this$0.screenLifecycleProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(this.this$0.contextProvider, this.appOpsControllerImplProvider, this.deviceConfigProxyProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider, this.this$0.providePackageManagerProvider, this.this$0.provideUiEventLoggerProvider, this.secureSettingsImplProvider));
            RotationPolicyWrapperImpl_Factory create3 = RotationPolicyWrapperImpl_Factory.create(this.this$0.contextProvider, this.secureSettingsImplProvider);
            this.rotationPolicyWrapperImplProvider = create3;
            this.bindRotationPolicyWrapperProvider = DoubleCheck.provider(create3);
            this.provideAutoRotateSettingsManagerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory.create(this.this$0.contextProvider));
            this.deviceStateRotationLockSettingControllerProvider = DoubleCheck.provider(DeviceStateRotationLockSettingController_Factory.create(this.bindRotationPolicyWrapperProvider, this.this$0.provideDeviceStateManagerProvider, this.this$0.provideMainExecutorProvider, this.provideAutoRotateSettingsManagerProvider));
            StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory create4 = StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory.create(this.this$0.provideResourcesProvider);
            this.providesDeviceStateRotationLockDefaultsProvider = create4;
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(this.bindRotationPolicyWrapperProvider, this.deviceStateRotationLockSettingControllerProvider, create4));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider, this.this$0.dumpManagerProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider));
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider));
            NightDisplayListenerModule nightDisplayListenerModule2 = nightDisplayListenerModule;
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, this.this$0.contextProvider, this.provideBgHandlerProvider);
            this.reduceBrightColorsControllerProvider = DoubleCheck.provider(ReduceBrightColorsController_Factory.create(this.provideUserTrackerProvider, this.provideBgHandlerProvider, this.this$0.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(this.this$0.provideAlarmManagerProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider, this.this$0.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(this.this$0.provideCarrierConfigManagerProvider, this.broadcastDispatcherProvider));
            this.wifiStatusTrackerFactoryProvider = WifiStatusTrackerFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideNetworkScoreManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider);
            this.wifiStateWorkerProvider = DoubleCheck.provider(WifiStateWorker_Factory.create(this.broadcastDispatcherProvider, this.provideBackgroundDelayableExecutorProvider, this.this$0.provideWifiManagerProvider));
            this.internetDialogControllerProvider = InternetDialogController_Factory.create(this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideActivityStarterProvider, this.provideAccessPointControllerImplProvider, this.this$0.provideSubcriptionManagerProvider, this.this$0.provideTelephonyManagerProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.keyguardStateControllerImplProvider, this.this$0.provideWindowManagerProvider, this.toastFactoryProvider, this.provideBgHandlerProvider, this.carrierConfigTrackerProvider, this.locationControllerImplProvider, this.provideDialogLaunchAnimatorProvider, this.wifiStateWorkerProvider);
            this.internetDialogFactoryProvider = DoubleCheck.provider(InternetDialogFactory_Factory.create(this.this$0.provideMainHandlerProvider, this.provideBackgroundExecutorProvider, this.internetDialogControllerProvider, this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.keyguardStateControllerImplProvider));
            Provider<NetworkControllerImpl> provider2 = DoubleCheck.provider(NetworkControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.this$0.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.providesDeviceProvisionedControllerProvider, this.broadcastDispatcherProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, this.this$0.provideWifiManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.wifiStatusTrackerFactoryProvider, this.this$0.provideMainHandlerProvider, this.internetDialogFactoryProvider, this.featureFlagsDebugProvider, this.this$0.dumpManagerProvider));
            this.networkControllerImplProvider = provider2;
            this.provideDataSaverControllerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideDataSaverControllerFactory.create(provider2));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(this.this$0.contextProvider));
            this.provideLeakReportEmailProvider = DoubleCheck.provider(TvSystemUIModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(this.this$0.contextProvider, this.providesLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.providesBackgroundMessageRouterProvider = SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory.create(this.provideBackgroundDelayableExecutorProvider);
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.providesBackgroundMessageRouterProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.this$0.dumpManagerProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarWindowModule_ProvidesStatusBarWindowViewFactory.create(this.this$0.providerLayoutInflaterProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(this.this$0.contextProvider, this.providesStatusBarWindowViewProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIWindowManagerProvider, this.statusBarContentInsetsProvider, this.this$0.provideResourcesProvider, this.this$0.unfoldTransitionProgressProvider));
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider, this.configurationControllerImplProvider, this.tunerServiceImplProvider, this.this$0.dumpManagerProvider));
            AnonymousClass9 r1 = new Provider<FragmentService.FragmentCreator.Factory>() {
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory();
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.notifPipelineFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(this.this$0.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, this.this$0.provideMainExecutorProvider));
            this.accessibilityFloatingMenuControllerProvider = DoubleCheck.provider(AccessibilityFloatingMenuController_Factory.create(this.this$0.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.notificationGroupAlertTransferHelperProvider = DoubleCheck.provider(NotificationGroupAlertTransferHelper_Factory.create(this.rowContentBindStageProvider, this.statusBarStateControllerImplProvider, this.notificationGroupManagerLegacyProvider));
            this.provideVisualStabilityManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.provideNotificationEntryManagerProvider, this.visualStabilityProvider, this.this$0.provideMainHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, this.this$0.dumpManagerProvider));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(this.this$0.contextProvider, this.this$0.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.deviceConfigProxyProvider));
            this.panelEventsEmitterProvider = DoubleCheck.provider(NotificationPanelViewController_PanelEventsEmitter_Factory.create());
            Provider<VisualStabilityCoordinator> provider3 = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideDelayableExecutorProvider, this.this$0.dumpManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.panelEventsEmitterProvider, this.statusBarStateControllerImplProvider, this.visualStabilityProvider, this.wakefulnessLifecycleProvider));
            this.visualStabilityCoordinatorProvider = provider3;
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.notifPipelineFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifCollectionProvider, this.provideNotificationVisibilityProvider, provider3, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(this.this$0.contextProvider, this.optionalOfCentralSurfacesProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider, this.this$0.provideAccessibilityManagerProvider, this.highPriorityProvider, this.this$0.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, this.this$0.provideLauncherAppsProvider, this.this$0.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, this.this$0.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider, this.this$0.dumpManagerProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.this$0.provideUiBackgroundExecutorProvider);
            this.provideNotificationPanelLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.notificationListenerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.notifPipelineFlagsProvider, this.notifLiveDataStoreImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, this.provideNotificationPanelLoggerProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.featureFlagsDebugProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider, this.notifPipelineFlagsProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider));
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(this.this$0.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideTimeTickHandlerFactory.create());
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(this.this$0.contextProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesPluginManagerProvider, this.sysuiColorExtractorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideSensorPrivacyControllerFactory.create(this.this$0.provideSensorPrivacyManagerProvider));
        }

        public final void initialize6(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider));
            SystemEventChipAnimationController_Factory create = SystemEventChipAnimationController_Factory.create(this.this$0.contextProvider, this.statusBarWindowControllerProvider, this.statusBarContentInsetsProvider);
            this.systemEventChipAnimationControllerProvider = create;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create, this.statusBarWindowControllerProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider, this.this$0.provideMainDelayableExecutorProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(this.this$0.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.dependencyProvider = DoubleCheck.provider(Dependency_Factory.create(this.this$0.dumpManagerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.zenModeControllerImplProvider, this.hdmiCecSetMenuLanguageHelperProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.reduceBrightColorsControllerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.providesDeviceProvisionedControllerProvider, this.this$0.providesPluginManagerProvider, this.assistManagerProvider, this.securityControllerImplProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.configurationControllerImplProvider, this.statusBarIconControllerImplProvider, this.this$0.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.this$0.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.this$0.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.powerNotificationWarningsProvider, this.lightBarControllerProvider, this.this$0.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesImplProvider, this.vibratorHelperProvider, this.this$0.provideIStatusBarServiceProvider, this.this$0.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.notificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, this.this$0.provideSensorPrivacyManagerProvider, this.autoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.this$0.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.this$0.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.dockManagerImplProvider, this.this$0.provideINotificationManagerProvider, this.provideSysUiStateProvider, this.this$0.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider2, this.this$0.provideUiEventLoggerProvider, this.statusBarContentInsetsProvider, this.internetDialogFactoryProvider, this.featureFlagsDebugProvider, this.notificationSectionsManagerProvider, this.screenOffAnimationControllerProvider, this.ambientStateProvider, this.provideGroupMembershipManagerProvider, this.provideGroupExpansionManagerProvider, this.systemUIDialogManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.mediaTttFlagsProvider = DoubleCheck.provider(MediaTttFlags_Factory.create(this.featureFlagsDebugProvider));
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideMediaTttSenderLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttSenderLogBufferProvider = provider;
            this.providesMediaTttSenderLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttSenderLoggerFactory.create(provider));
            this.viewUtilProvider = DoubleCheck.provider(ViewUtil_Factory.create());
            this.tapGestureDetectorProvider = DoubleCheck.provider(TapGestureDetector_Factory.create(this.this$0.contextProvider));
            this.mediaTttSenderUiEventLoggerProvider = DoubleCheck.provider(MediaTttSenderUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerSender> provider2 = DoubleCheck.provider(MediaTttChipControllerSender_Factory.create(this.provideCommandQueueProvider, this.this$0.contextProvider, this.providesMediaTttSenderLoggerProvider, this.this$0.provideWindowManagerProvider, this.viewUtilProvider, this.this$0.provideMainDelayableExecutorProvider, this.tapGestureDetectorProvider, this.this$0.providePowerManagerProvider, this.mediaTttSenderUiEventLoggerProvider));
            this.mediaTttChipControllerSenderProvider = provider2;
            this.providesMediaTttChipControllerSenderProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerSenderFactory.create(this.mediaTttFlagsProvider, provider2));
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideMediaTttReceiverLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttReceiverLogBufferProvider = provider3;
            this.providesMediaTttReceiverLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttReceiverLoggerFactory.create(provider3));
            this.mediaTttReceiverUiEventLoggerProvider = DoubleCheck.provider(MediaTttReceiverUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerReceiver> provider4 = DoubleCheck.provider(MediaTttChipControllerReceiver_Factory.create(this.provideCommandQueueProvider, this.this$0.contextProvider, this.providesMediaTttReceiverLoggerProvider, this.this$0.provideWindowManagerProvider, this.viewUtilProvider, this.provideDelayableExecutorProvider, this.tapGestureDetectorProvider, this.this$0.providePowerManagerProvider, this.this$0.provideMainHandlerProvider, this.mediaTttReceiverUiEventLoggerProvider));
            this.mediaTttChipControllerReceiverProvider = provider4;
            this.providesMediaTttChipControllerReceiverProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerReceiverFactory.create(this.mediaTttFlagsProvider, provider4));
            Provider<MediaTttCommandLineHelper> provider5 = DoubleCheck.provider(MediaTttCommandLineHelper_Factory.create(this.commandRegistryProvider, this.this$0.contextProvider, this.this$0.provideMainExecutorProvider));
            this.mediaTttCommandLineHelperProvider = provider5;
            this.providesMediaTttCommandLineHelperProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttCommandLineHelperFactory.create(this.mediaTttFlagsProvider, provider5));
            Provider<MediaMuteAwaitConnectionCli> provider6 = DoubleCheck.provider(MediaMuteAwaitConnectionCli_Factory.create(this.commandRegistryProvider, this.this$0.contextProvider));
            this.mediaMuteAwaitConnectionCliProvider = provider6;
            this.providesMediaMuteAwaitConnectionCliProvider = DoubleCheck.provider(MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory.create(this.mediaFlagsProvider, provider6));
            this.notificationChannelsProvider = NotificationChannels_Factory.create(this.this$0.contextProvider);
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.setDisplayAreaHelperProvider = InstanceFactory.create(optional13);
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(TvSystemUIModule_ProvideAllowNotificationLongPressFactory.create());
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.screenOffAnimationControllerProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(this.this$0.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.notificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.screenOffAnimationControllerProvider));
            this.optionalOfBcSmartspaceDataPluginProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.lockscreenSmartspaceControllerProvider = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(this.this$0.contextProvider, this.featureFlagsDebugProvider, this.this$0.provideSmartspaceManagerProvider, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.this$0.provideContentResolverProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.this$0.provideExecutionProvider, this.this$0.provideMainExecutorProvider, this.this$0.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.notificationSectionsFeatureManagerProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(this.this$0.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider, this.this$0.dumpManagerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, this.this$0.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.provideSysUIUnfoldComponentProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.provideProximityCheckProvider = SensorModule_ProvideProximityCheckFactory.create(this.provideProximitySensorProvider, this.this$0.provideMainDelayableExecutorProvider);
            this.dreamOverlayNotificationCountProvider = DoubleCheck.provider(DreamOverlayNotificationCountProvider_Factory.create(this.notificationListenerProvider, this.provideBackgroundExecutorProvider));
            this.statusBarWindowStateControllerProvider = DoubleCheck.provider(StatusBarWindowStateController_Factory.create(this.this$0.provideDisplayIdProvider, this.provideCommandQueueProvider));
            this.qSTileHostProvider = new DelegateFactory();
            Provider<LogBuffer> provider7 = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideQuickSettingsLogBufferProvider = provider7;
            this.qSLoggerProvider = QSLogger_Factory.create(provider7);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(this.this$0.contextProvider);
            this.tileServicesProvider = TileServices_Factory.create(this.qSTileHostProvider, this.this$0.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.provideCommandQueueProvider);
            this.builderProvider4 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider, this.tileServicesProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider, this.internetDialogFactoryProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider);
            this.cellularTileProvider = CellularTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.dndTileProvider = DndTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.zenModeControllerImplProvider, this.this$0.provideSharePreferencesProvider, this.secureSettingsImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.colorInversionTileProvider = ColorInversionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.airplaneModeTileProvider = AirplaneModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider, this.this$0.provideConnectivityManagagerProvider, this.globalSettingsImplProvider);
            this.workModeTileProvider = WorkModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.managedProfileControllerImplProvider);
            this.rotationLockTileProvider = RotationLockTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.rotationLockControllerImplProvider, this.this$0.provideSensorPrivacyManagerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.flashlightTileProvider = FlashlightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.flashlightControllerImplProvider);
            this.locationTileProvider = LocationTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.castTileProvider = CastTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.castControllerImplProvider, this.keyguardStateControllerImplProvider, this.networkControllerImplProvider, this.hotspotControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.hotspotTileProvider = HotspotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider);
            this.batterySaverTileProvider = BatterySaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.dataSaverTileProvider = DataSaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideDataSaverControllerProvider, this.provideDialogLaunchAnimatorProvider);
            this.builderProvider5 = NightDisplayListenerModule_Builder_Factory.create(this.this$0.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.this$0.provideColorDisplayManagerProvider, this.builderProvider5);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.configurationControllerImplProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider, this.keyguardStateControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            Provider<Boolean> provider8 = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(this.this$0.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider8;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider8, this.reduceBrightColorsControllerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.providesControlsFeatureEnabledProvider = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(this.this$0.providePackageManagerProvider));
            this.optionalOfControlsTileResourceConfigurationProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.controlsComponentProvider = DoubleCheck.provider(ControlsComponent_Factory.create(this.providesControlsFeatureEnabledProvider, this.this$0.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.this$0.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.optionalOfControlsTileResourceConfigurationProvider));
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.controlsComponentProvider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            this.provideQuickAccessWalletClientProvider = DoubleCheck.provider(WalletModule_ProvideQuickAccessWalletClientFactory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider));
            this.quickAccessWalletControllerProvider = DoubleCheck.provider(QuickAccessWalletController_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.provideQuickAccessWalletClientProvider, this.bindSystemClockProvider));
            this.quickAccessWalletTileProvider = QuickAccessWalletTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, this.this$0.providePackageManagerProvider, this.secureSettingsImplProvider, this.quickAccessWalletControllerProvider);
            this.qRCodeScannerControllerProvider = DoubleCheck.provider(QRCodeScannerController_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider));
            this.qRCodeScannerTileProvider = QRCodeScannerTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.qRCodeScannerControllerProvider);
            this.oneHandedModeTileProvider = OneHandedModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.colorCorrectionTileProvider = ColorCorrectionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.screenShotTileProvider = ScreenShotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.blackScreenTileProvider = BlackScreenTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.settingTileProvider = SettingTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.soundTileProvider = SoundTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            RebootTile_Factory create2 = RebootTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.rebootTileProvider = create2;
            this.qSFactoryImplProvider = DoubleCheck.provider(QSFactoryImpl_Factory.create(this.qSTileHostProvider, this.builderProvider4, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qRCodeScannerTileProvider, this.oneHandedModeTileProvider, this.colorCorrectionTileProvider, this.screenShotTileProvider, this.blackScreenTileProvider, this.settingTileProvider, this.soundTileProvider, create2));
            this.builderProvider6 = DoubleCheck.provider(AutoAddTracker_Builder_Factory.create(this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.qSTileHostProvider, this.this$0.dumpManagerProvider, this.this$0.provideMainHandlerProvider, this.provideBackgroundExecutorProvider));
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(this.this$0.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.safetyControllerProvider = SafetyController_Factory.create(this.this$0.contextProvider, this.this$0.providePackageManagerProvider, this.this$0.provideSafetyCenterManagerProvider, this.provideBgHandlerProvider);
            this.provideAutoTileManagerProvider = QSModule_ProvideAutoTileManagerFactory.create(this.this$0.contextProvider, this.builderProvider6, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.reduceBrightColorsControllerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.safetyControllerProvider, this.isReduceBrightColorsAvailableProvider);
            this.builderProvider7 = DoubleCheck.provider(TileServiceRequestController_Builder_Factory.create(this.provideCommandQueueProvider, this.commandRegistryProvider));
            this.packageManagerAdapterProvider = PackageManagerAdapter_Factory.create(this.this$0.contextProvider);
            C0000TileLifecycleManager_Factory create3 = C0000TileLifecycleManager_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.tileServicesProvider, this.packageManagerAdapterProvider, this.broadcastDispatcherProvider);
            this.tileLifecycleManagerProvider = create3;
            this.factoryProvider9 = TileLifecycleManager_Factory_Impl.create(create3);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(this.this$0.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplProvider, this.this$0.provideMainHandlerProvider, this.provideBgLooperProvider, this.this$0.providesPluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.this$0.dumpManagerProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.qSLoggerProvider, this.this$0.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider, this.builderProvider7, this.factoryProvider9)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.provideQSFragmentDisableLogBufferProvider = DoubleCheck.provider(LogModule_ProvideQSFragmentDisableLogBufferFactory.create(this.logBufferFactoryProvider));
            this.disableFlagsLoggerProvider = DoubleCheck.provider(DisableFlagsLogger_Factory.create());
        }

        public final void initialize7(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(this.this$0.providePermissionManagerProvider, this.this$0.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider, this.this$0.provideUiEventLoggerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory.create());
            UserDetailView_Adapter_Factory create = UserDetailView_Adapter_Factory.create(this.this$0.contextProvider, this.userSwitcherControllerProvider, this.this$0.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.adapterProvider = create;
            this.userSwitchDialogControllerProvider = DoubleCheck.provider(UserSwitchDialogController_Factory.create(create, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.provideDialogLaunchAnimatorProvider, this.this$0.provideUiEventLoggerProvider));
            this.fgsManagerControllerProvider = DoubleCheck.provider(FgsManagerController_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bindSystemClockProvider, this.this$0.provideIActivityManagerProvider, this.this$0.providePackageManagerProvider, this.provideUserTrackerProvider, this.deviceConfigProxyProvider, this.provideDialogLaunchAnimatorProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(QSFlagsModule_IsPMLiteEnabledFactory.create(this.featureFlagsDebugProvider, this.globalSettingsImplProvider));
        }

        public BootCompleteCacheImpl provideBootCacheImpl() {
            return this.bootCompleteCacheImplProvider.get();
        }

        public ConfigurationController getConfigurationController() {
            return this.configurationControllerImplProvider.get();
        }

        public ContextComponentHelper getContextComponentHelper() {
            return this.contextComponentResolverProvider.get();
        }

        public UnfoldLatencyTracker getUnfoldLatencyTracker() {
            return this.unfoldLatencyTrackerProvider.get();
        }

        public Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider() {
            return (Optional) this.this$0.providesFoldStateLoggingProvider.get();
        }

        public Optional<FoldStateLogger> getFoldStateLogger() {
            return (Optional) this.this$0.providesFoldStateLoggerProvider.get();
        }

        public Dependency createDependency() {
            return this.dependencyProvider.get();
        }

        public DumpManager createDumpManager() {
            return (DumpManager) this.this$0.dumpManagerProvider.get();
        }

        public InitController getInitController() {
            return this.initControllerProvider.get();
        }

        public Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent() {
            return this.provideSysUIUnfoldComponentProvider.get();
        }

        public Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider() {
            return (Optional) this.this$0.provideNaturalRotationProgressProvider.get();
        }

        public Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender() {
            return this.providesMediaTttChipControllerSenderProvider.get();
        }

        public Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver() {
            return this.providesMediaTttChipControllerReceiverProvider.get();
        }

        public Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper() {
            return this.providesMediaTttCommandLineHelperProvider.get();
        }

        public Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli() {
            return this.providesMediaMuteAwaitConnectionCliProvider.get();
        }

        public Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager() {
            return this.providesNearbyMediaDevicesManagerProvider.get();
        }

        public Map<Class<?>, Provider<CoreStartable>> getStartables() {
            return MapBuilder.newMapBuilder(21).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(HomeSoundEffectController.class, this.homeSoundEffectControllerProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(PowerUI.class, this.powerUIProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(TvNotificationHandler.class, this.provideTvNotificationHandlerProvider).put(TvNotificationPanel.class, this.tvNotificationPanelProvider).put(TvOngoingPrivacyChip.class, this.tvOngoingPrivacyChipProvider).put(TvStatusBar.class, this.tvStatusBarProvider).put(VolumeUI.class, this.volumeUIProvider).put(VpnStatusObserver.class, this.vpnStatusObserverProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).build();
        }

        public Map<Class<?>, Provider<CoreStartable>> getPerUserStartables() {
            return Collections.singletonMap(NotificationChannels.class, this.notificationChannelsProvider);
        }

        public void inject(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            injectSystemUIAppComponentFactory(systemUIAppComponentFactory);
        }

        public void inject(KeyguardSliceProvider keyguardSliceProvider) {
            injectKeyguardSliceProvider(keyguardSliceProvider);
        }

        public void inject(ClockOptionsProvider clockOptionsProvider) {
            injectClockOptionsProvider(clockOptionsProvider);
        }

        public void inject(PeopleProvider peopleProvider) {
            injectPeopleProvider(peopleProvider);
        }

        public final SystemUIAppComponentFactory injectSystemUIAppComponentFactory(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            SystemUIAppComponentFactory_MembersInjector.injectMComponentHelper(systemUIAppComponentFactory, this.contextComponentResolverProvider.get());
            return systemUIAppComponentFactory;
        }

        public final KeyguardSliceProvider injectKeyguardSliceProvider(KeyguardSliceProvider keyguardSliceProvider) {
            KeyguardSliceProvider_MembersInjector.injectMDozeParameters(keyguardSliceProvider, this.dozeParametersProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMZenModeController(keyguardSliceProvider, this.zenModeControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMNextAlarmController(keyguardSliceProvider, this.nextAlarmControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProvider, (AlarmManager) this.this$0.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProvider, (ContentResolver) this.this$0.provideContentResolverProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMMediaManager(keyguardSliceProvider, this.provideNotificationMediaManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMStatusBarStateController(keyguardSliceProvider, this.statusBarStateControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardBypassController(keyguardSliceProvider, this.keyguardBypassControllerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardUpdateMonitor(keyguardSliceProvider, this.keyguardUpdateMonitorProvider.get());
            return keyguardSliceProvider;
        }

        public final ClockOptionsProvider injectClockOptionsProvider(ClockOptionsProvider clockOptionsProvider) {
            ClockOptionsProvider_MembersInjector.injectMClockInfosProvider(clockOptionsProvider, this.provideClockInfoListProvider);
            return clockOptionsProvider;
        }

        public final PeopleProvider injectPeopleProvider(PeopleProvider peopleProvider) {
            PeopleProvider_MembersInjector.injectMPeopleSpaceWidgetManager(peopleProvider, this.peopleSpaceWidgetManagerProvider.get());
            return peopleProvider;
        }

        public final class SysUIUnfoldComponentFactory implements SysUIUnfoldComponent.Factory {
            public SysUIUnfoldComponentFactory() {
            }

            public SysUIUnfoldComponent create(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
                Preconditions.checkNotNull(unfoldTransitionProgressProvider);
                Preconditions.checkNotNull(naturalRotationUnfoldProgressProvider);
                Preconditions.checkNotNull(scopedUnfoldTransitionProgressProvider);
                return new SysUIUnfoldComponentImpl(unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
            }
        }

        public final class SysUIUnfoldComponentImpl implements SysUIUnfoldComponent {
            public Provider<FoldAodAnimationController> foldAodAnimationControllerProvider;
            public Provider<KeyguardUnfoldTransition> keyguardUnfoldTransitionProvider;
            public Provider<NotificationPanelUnfoldAnimationController> notificationPanelUnfoldAnimationControllerProvider;
            public Provider<UnfoldTransitionProgressProvider> p1Provider;
            public Provider<NaturalRotationUnfoldProgressProvider> p2Provider;
            public Provider<ScopedUnfoldTransitionProgressProvider> p3Provider;
            public Provider<StatusBarMoveFromCenterAnimationController> statusBarMoveFromCenterAnimationControllerProvider;
            public Provider<UnfoldLightRevealOverlayAnimation> unfoldLightRevealOverlayAnimationProvider;
            public Provider<UnfoldTransitionWallpaperController> unfoldTransitionWallpaperControllerProvider;

            public SysUIUnfoldComponentImpl(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
                initialize(unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
            }

            public final void initialize(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
                this.p2Provider = InstanceFactory.create(naturalRotationUnfoldProgressProvider);
                this.keyguardUnfoldTransitionProvider = DoubleCheck.provider(KeyguardUnfoldTransition_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.p2Provider));
                Factory create = InstanceFactory.create(scopedUnfoldTransitionProgressProvider);
                this.p3Provider = create;
                this.statusBarMoveFromCenterAnimationControllerProvider = DoubleCheck.provider(StatusBarMoveFromCenterAnimationController_Factory.create(create, TvSysUIComponentImpl.this.this$0.provideWindowManagerProvider));
                this.notificationPanelUnfoldAnimationControllerProvider = DoubleCheck.provider(NotificationPanelUnfoldAnimationController_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.p2Provider));
                this.foldAodAnimationControllerProvider = DoubleCheck.provider(FoldAodAnimationController_Factory.create(TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.this$0.provideDeviceStateManagerProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.globalSettingsImplProvider));
                Factory create2 = InstanceFactory.create(unfoldTransitionProgressProvider);
                this.p1Provider = create2;
                this.unfoldTransitionWallpaperControllerProvider = DoubleCheck.provider(UnfoldTransitionWallpaperController_Factory.create(create2, TvSysUIComponentImpl.this.wallpaperControllerProvider));
                this.unfoldLightRevealOverlayAnimationProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.this$0.provideDeviceStateManagerProvider, TvSysUIComponentImpl.this.this$0.provideDisplayManagerProvider, this.p1Provider, TvSysUIComponentImpl.this.setDisplayAreaHelperProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.this$0.provideUiBackgroundExecutorProvider, TvSysUIComponentImpl.this.this$0.provideIWindowManagerProvider));
            }

            public KeyguardUnfoldTransition getKeyguardUnfoldTransition() {
                return this.keyguardUnfoldTransitionProvider.get();
            }

            public StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController() {
                return this.statusBarMoveFromCenterAnimationControllerProvider.get();
            }

            public NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController() {
                return this.notificationPanelUnfoldAnimationControllerProvider.get();
            }

            public FoldAodAnimationController getFoldAodAnimationController() {
                return this.foldAodAnimationControllerProvider.get();
            }

            public UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController() {
                return this.unfoldTransitionWallpaperControllerProvider.get();
            }

            public UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation() {
                return this.unfoldLightRevealOverlayAnimationProvider.get();
            }
        }

        public final class ExpandableNotificationRowComponentBuilder implements ExpandableNotificationRowComponent.Builder {
            public ExpandableNotificationRow expandableNotificationRow;
            public NotificationListContainer listContainer;
            public NotificationEntry notificationEntry;
            public ExpandableNotificationRow.OnExpandClickListener onExpandClickListener;

            public ExpandableNotificationRowComponentBuilder() {
            }

            public ExpandableNotificationRowComponentBuilder expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow2) {
                this.expandableNotificationRow = (ExpandableNotificationRow) Preconditions.checkNotNull(expandableNotificationRow2);
                return this;
            }

            public ExpandableNotificationRowComponentBuilder notificationEntry(NotificationEntry notificationEntry2) {
                this.notificationEntry = (NotificationEntry) Preconditions.checkNotNull(notificationEntry2);
                return this;
            }

            public ExpandableNotificationRowComponentBuilder onExpandClickListener(ExpandableNotificationRow.OnExpandClickListener onExpandClickListener2) {
                this.onExpandClickListener = (ExpandableNotificationRow.OnExpandClickListener) Preconditions.checkNotNull(onExpandClickListener2);
                return this;
            }

            public ExpandableNotificationRowComponentBuilder listContainer(NotificationListContainer notificationListContainer) {
                this.listContainer = (NotificationListContainer) Preconditions.checkNotNull(notificationListContainer);
                return this;
            }

            public ExpandableNotificationRowComponent build() {
                Preconditions.checkBuilderRequirement(this.expandableNotificationRow, ExpandableNotificationRow.class);
                Preconditions.checkBuilderRequirement(this.notificationEntry, NotificationEntry.class);
                Preconditions.checkBuilderRequirement(this.onExpandClickListener, ExpandableNotificationRow.OnExpandClickListener.class);
                Preconditions.checkBuilderRequirement(this.listContainer, NotificationListContainer.class);
                return new ExpandableNotificationRowComponentImpl(this.expandableNotificationRow, this.notificationEntry, this.onExpandClickListener, this.listContainer);
            }
        }

        public final class ExpandableNotificationRowComponentImpl implements ExpandableNotificationRowComponent {
            public Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
            public Provider<ExpandableNotificationRowController> expandableNotificationRowControllerProvider;
            public Provider<ExpandableNotificationRowDragController> expandableNotificationRowDragControllerProvider;
            public Provider<ExpandableNotificationRow> expandableNotificationRowProvider;
            public Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
            public Provider<ExpandableViewController> expandableViewControllerProvider;
            public Provider<NotificationTapHelper.Factory> factoryProvider;
            public Provider<NotificationListContainer> listContainerProvider;
            public final NotificationEntry notificationEntry;
            public Provider<NotificationEntry> notificationEntryProvider;
            public Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
            public Provider<String> provideAppNameProvider;
            public Provider<String> provideNotificationKeyProvider;
            public Provider<StatusBarNotification> provideStatusBarNotificationProvider;
            public Provider<RemoteInputViewSubcomponent.Factory> remoteInputViewSubcomponentFactoryProvider;

            public ExpandableNotificationRowComponentImpl(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
                this.notificationEntry = notificationEntry2;
                initialize(expandableNotificationRow, notificationEntry2, onExpandClickListener, notificationListContainer);
            }

            public final void initialize(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
                this.expandableNotificationRowProvider = InstanceFactory.create(expandableNotificationRow);
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, TvSysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider);
                this.remoteInputViewSubcomponentFactoryProvider = new Provider<RemoteInputViewSubcomponent.Factory>() {
                    public RemoteInputViewSubcomponent.Factory get() {
                        return new RemoteInputViewSubcomponentFactory();
                    }
                };
                this.listContainerProvider = InstanceFactory.create(notificationListContainer);
                Factory create3 = InstanceFactory.create(notificationEntry2);
                this.notificationEntryProvider = create3;
                this.provideStatusBarNotificationProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory.create(create3);
                this.provideAppNameProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.provideStatusBarNotificationProvider);
                this.provideNotificationKeyProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory.create(this.provideStatusBarNotificationProvider);
                this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
                this.expandableNotificationRowDragControllerProvider = ExpandableNotificationRowDragController_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, TvSysUIComponentImpl.this.shadeControllerImplProvider);
                this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.activatableNotificationViewControllerProvider, this.remoteInputViewSubcomponentFactoryProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, this.listContainerProvider, TvSysUIComponentImpl.this.provideNotificationMediaManagerProvider, TvSysUIComponentImpl.this.smartReplyConstantsProvider, TvSysUIComponentImpl.this.provideSmartReplyControllerProvider, TvSysUIComponentImpl.this.this$0.providesPluginManagerProvider, TvSysUIComponentImpl.this.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.provideGroupMembershipManagerProvider, TvSysUIComponentImpl.this.provideGroupExpansionManagerProvider, TvSysUIComponentImpl.this.rowContentBindStageProvider, TvSysUIComponentImpl.this.provideNotificationLoggerProvider, TvSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.provideNotificationGutsManagerProvider, TvSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, TvSysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, TvSysUIComponentImpl.this.featureFlagsDebugProvider, TvSysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, TvSysUIComponentImpl.this.provideBubblesManagerProvider, this.expandableNotificationRowDragControllerProvider));
            }

            public ExpandableNotificationRowController getExpandableNotificationRowController() {
                return this.expandableNotificationRowControllerProvider.get();
            }

            public final class RemoteInputViewSubcomponentFactory implements RemoteInputViewSubcomponent.Factory {
                public RemoteInputViewSubcomponentFactory() {
                }

                public RemoteInputViewSubcomponent create(RemoteInputView remoteInputView, RemoteInputController remoteInputController) {
                    Preconditions.checkNotNull(remoteInputView);
                    Preconditions.checkNotNull(remoteInputController);
                    return new RemoteInputViewSubcomponentI(remoteInputView, remoteInputController);
                }
            }

            public final class RemoteInputViewSubcomponentI implements RemoteInputViewSubcomponent {
                public final RemoteInputController remoteInputController;
                public final RemoteInputView view;

                public RemoteInputViewSubcomponentI(RemoteInputView remoteInputView, RemoteInputController remoteInputController2) {
                    this.view = remoteInputView;
                    this.remoteInputController = remoteInputController2;
                }

                public final RemoteInputViewControllerImpl remoteInputViewControllerImpl() {
                    return new RemoteInputViewControllerImpl(this.view, ExpandableNotificationRowComponentImpl.this.notificationEntry, (RemoteInputQuickSettingsDisabler) TvSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), this.remoteInputController, (ShortcutManager) TvSysUIComponentImpl.this.this$0.provideShortcutManagerProvider.get(), (UiEventLogger) TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider.get());
                }

                public RemoteInputViewController getController() {
                    return remoteInputViewControllerImpl();
                }
            }
        }

        public final class NavigationBarComponentFactory implements NavigationBarComponent.Factory {
            public NavigationBarComponentFactory() {
            }

            public NavigationBarComponent create(Context context, Bundle bundle) {
                Preconditions.checkNotNull(context);
                return new NavigationBarComponentImpl(context, bundle);
            }
        }

        public final class NavigationBarComponentImpl implements NavigationBarComponent {
            public Provider<Context> contextProvider;
            public Provider<DeadZone> deadZoneProvider;
            public Provider<LightBarController.Factory> factoryProvider;
            public Provider<AutoHideController.Factory> factoryProvider2;
            public Provider<NavigationBar> navigationBarProvider;
            public Provider<NavigationBarTransitions> navigationBarTransitionsProvider;
            public Provider<EdgeBackGestureHandler> provideEdgeBackGestureHandlerProvider;
            public Provider<LayoutInflater> provideLayoutInflaterProvider;
            public Provider<NavigationBarFrame> provideNavigationBarFrameProvider;
            public Provider<NavigationBarView> provideNavigationBarviewProvider;
            public Provider<WindowManager> provideWindowManagerProvider;
            public Provider<Bundle> savedStateProvider;

            public NavigationBarComponentImpl(Context context, Bundle bundle) {
                initialize(context, bundle);
            }

            public final void initialize(Context context, Bundle bundle) {
                Factory create = InstanceFactory.create(context);
                this.contextProvider = create;
                Provider<LayoutInflater> provider = DoubleCheck.provider(NavigationBarModule_ProvideLayoutInflaterFactory.create(create));
                this.provideLayoutInflaterProvider = provider;
                Provider<NavigationBarFrame> provider2 = DoubleCheck.provider(NavigationBarModule_ProvideNavigationBarFrameFactory.create(provider));
                this.provideNavigationBarFrameProvider = provider2;
                this.provideNavigationBarviewProvider = DoubleCheck.provider(NavigationBarModule_ProvideNavigationBarviewFactory.create(this.provideLayoutInflaterProvider, provider2));
                this.savedStateProvider = InstanceFactory.createNullable(bundle);
                this.provideWindowManagerProvider = DoubleCheck.provider(NavigationBarModule_ProvideWindowManagerFactory.create(this.contextProvider));
                this.factoryProvider = LightBarController_Factory_Factory.create(TvSysUIComponentImpl.this.darkIconDispatcherImplProvider, TvSysUIComponentImpl.this.provideBatteryControllerProvider, TvSysUIComponentImpl.this.navigationModeControllerProvider, TvSysUIComponentImpl.this.this$0.dumpManagerProvider);
                this.factoryProvider2 = AutoHideController_Factory_Factory.create(TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideIWindowManagerProvider);
                this.deadZoneProvider = DeadZone_Factory.create(this.provideNavigationBarviewProvider);
                this.navigationBarTransitionsProvider = DoubleCheck.provider(NavigationBarTransitions_Factory.create(this.provideNavigationBarviewProvider, TvSysUIComponentImpl.this.this$0.provideIWindowManagerProvider, TvSysUIComponentImpl.this.factoryProvider));
                this.provideEdgeBackGestureHandlerProvider = DoubleCheck.provider(NavigationBarModule_ProvideEdgeBackGestureHandlerFactory.create(TvSysUIComponentImpl.this.factoryProvider2, this.contextProvider));
                this.navigationBarProvider = DoubleCheck.provider(NavigationBar_Factory.create(this.provideNavigationBarviewProvider, this.provideNavigationBarFrameProvider, this.savedStateProvider, this.contextProvider, this.provideWindowManagerProvider, TvSysUIComponentImpl.this.assistManagerProvider, TvSysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, TvSysUIComponentImpl.this.providesDeviceProvisionedControllerProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.overviewProxyServiceProvider, TvSysUIComponentImpl.this.navigationModeControllerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, TvSysUIComponentImpl.this.provideSysUiStateProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.provideCommandQueueProvider, TvSysUIComponentImpl.this.setPipProvider, TvSysUIComponentImpl.this.optionalOfRecentsProvider, TvSysUIComponentImpl.this.optionalOfCentralSurfacesProvider, TvSysUIComponentImpl.this.shadeControllerImplProvider, TvSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TvSysUIComponentImpl.this.notificationShadeDepthControllerProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.navBarHelperProvider, TvSysUIComponentImpl.this.lightBarControllerProvider, this.factoryProvider, TvSysUIComponentImpl.this.autoHideControllerProvider, this.factoryProvider2, TvSysUIComponentImpl.this.this$0.provideOptionalTelecomManagerProvider, TvSysUIComponentImpl.this.this$0.provideInputMethodManagerProvider, this.deadZoneProvider, TvSysUIComponentImpl.this.deviceConfigProxyProvider, this.navigationBarTransitionsProvider, this.provideEdgeBackGestureHandlerProvider, TvSysUIComponentImpl.this.setBackAnimationProvider, TvSysUIComponentImpl.this.provideUserTrackerProvider));
            }

            public NavigationBar getNavigationBar() {
                return this.navigationBarProvider.get();
            }
        }

        public final class KeyguardStatusViewComponentFactory implements KeyguardStatusViewComponent.Factory {
            public KeyguardStatusViewComponentFactory() {
            }

            public KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView) {
                Preconditions.checkNotNull(keyguardStatusView);
                return new KeyguardStatusViewComponentImpl(keyguardStatusView);
            }
        }

        public final class KeyguardStatusViewComponentImpl implements KeyguardStatusViewComponent {
            public Provider<KeyguardClockSwitch> getKeyguardClockSwitchProvider;
            public Provider<KeyguardSliceView> getKeyguardSliceViewProvider;
            public Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
            public final KeyguardStatusView presentation;
            public Provider<KeyguardStatusView> presentationProvider;

            public KeyguardStatusViewComponentImpl(KeyguardStatusView keyguardStatusView) {
                this.presentation = keyguardStatusView;
                initialize(keyguardStatusView);
            }

            public final KeyguardClockSwitch keyguardClockSwitch() {
                return KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.getKeyguardClockSwitch(this.presentation);
            }

            public final void initialize(KeyguardStatusView keyguardStatusView) {
                Factory create = InstanceFactory.create(keyguardStatusView);
                this.presentationProvider = create;
                KeyguardStatusViewModule_GetKeyguardClockSwitchFactory create2 = KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.create(create);
                this.getKeyguardClockSwitchProvider = create2;
                KeyguardStatusViewModule_GetKeyguardSliceViewFactory create3 = KeyguardStatusViewModule_GetKeyguardSliceViewFactory.create(create2);
                this.getKeyguardSliceViewProvider = create3;
                this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, TvSysUIComponentImpl.this.provideActivityStarterProvider, TvSysUIComponentImpl.this.configurationControllerImplProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.this$0.dumpManagerProvider));
            }

            public KeyguardClockSwitchController getKeyguardClockSwitchController() {
                return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) TvSysUIComponentImpl.this.sysuiColorExtractorProvider.get(), (ClockManager) TvSysUIComponentImpl.this.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) TvSysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) TvSysUIComponentImpl.this.broadcastDispatcherProvider.get(), (BatteryController) TvSysUIComponentImpl.this.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (LockscreenSmartspaceController) TvSysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) TvSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SecureSettings) TvSysUIComponentImpl.this.secureSettingsImpl(), (Executor) TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), TvSysUIComponentImpl.this.this$0.mainResources(), (DumpManager) TvSysUIComponentImpl.this.this$0.dumpManagerProvider.get());
            }

            public KeyguardStatusViewController getKeyguardStatusViewController() {
                return new KeyguardStatusViewController(this.presentation, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) TvSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (ConfigurationController) TvSysUIComponentImpl.this.configurationControllerImplProvider.get(), (DozeParameters) TvSysUIComponentImpl.this.dozeParametersProvider.get(), (ScreenOffAnimationController) TvSysUIComponentImpl.this.screenOffAnimationControllerProvider.get());
            }
        }

        public final class KeyguardBouncerComponentFactory implements KeyguardBouncerComponent.Factory {
            public KeyguardBouncerComponentFactory() {
            }

            public KeyguardBouncerComponent create(ViewGroup viewGroup) {
                Preconditions.checkNotNull(viewGroup);
                return new KeyguardBouncerComponentImpl(viewGroup);
            }
        }

        public final class KeyguardBouncerComponentImpl implements KeyguardBouncerComponent {
            public Provider<ViewGroup> bouncerContainerProvider;
            public Provider<AdminSecondaryLockScreenController.Factory> factoryProvider;
            public Provider<EmergencyButtonController.Factory> factoryProvider2;
            public Provider<KeyguardInputViewController.Factory> factoryProvider3;
            public Provider factoryProvider4;
            public Provider<KeyguardHostViewController> keyguardHostViewControllerProvider;
            public Provider<KeyguardSecurityViewFlipperController> keyguardSecurityViewFlipperControllerProvider;
            public Provider liftToActivateListenerProvider;
            public Provider<KeyguardHostView> providesKeyguardHostViewProvider;
            public Provider<KeyguardSecurityContainer> providesKeyguardSecurityContainerProvider;
            public Provider<KeyguardSecurityViewFlipper> providesKeyguardSecurityViewFlipperProvider;

            public KeyguardBouncerComponentImpl(ViewGroup viewGroup) {
                initialize(viewGroup);
            }

            public final void initialize(ViewGroup viewGroup) {
                Factory create = InstanceFactory.create(viewGroup);
                this.bouncerContainerProvider = create;
                Provider<KeyguardHostView> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(create, TvSysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesKeyguardHostViewProvider = provider;
                this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider));
                this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.providesKeyguardSecurityContainerProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider));
                this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
                this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(TvSysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider);
                this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(TvSysUIComponentImpl.this.configurationControllerImplProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.this$0.providePowerManagerProvider, TvSysUIComponentImpl.this.this$0.provideActivityTaskManagerProvider, TvSysUIComponentImpl.this.shadeControllerImplProvider, TvSysUIComponentImpl.this.this$0.provideTelecomManagerProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider);
                this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.this$0.provideLockPatternUtilsProvider, TvSysUIComponentImpl.this.this$0.provideLatencyTrackerProvider, TvSysUIComponentImpl.this.factoryProvider4, TvSysUIComponentImpl.this.this$0.provideInputMethodManagerProvider, TvSysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, TvSysUIComponentImpl.this.this$0.provideResourcesProvider, this.liftToActivateListenerProvider, TvSysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, this.factoryProvider2, TvSysUIComponentImpl.this.devicePostureControllerImplProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider);
                this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, TvSysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
                this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, TvSysUIComponentImpl.this.this$0.provideLockPatternUtilsProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.keyguardSecurityModelProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, TvSysUIComponentImpl.this.configurationControllerImplProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.userSwitcherControllerProvider, TvSysUIComponentImpl.this.featureFlagsDebugProvider, TvSysUIComponentImpl.this.globalSettingsImplProvider, TvSysUIComponentImpl.this.sessionTrackerProvider);
                this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.this$0.provideAudioManagerProvider, TvSysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.providesViewMediatorCallbackProvider, this.factoryProvider4));
            }

            public KeyguardHostViewController getKeyguardHostViewController() {
                return this.keyguardHostViewControllerProvider.get();
            }
        }

        public final class SectionHeaderControllerSubcomponentBuilder implements SectionHeaderControllerSubcomponent.Builder {
            public String clickIntentAction;
            public Integer headerText;
            public String nodeLabel;

            public SectionHeaderControllerSubcomponentBuilder() {
            }

            public SectionHeaderControllerSubcomponentBuilder nodeLabel(String str) {
                this.nodeLabel = (String) Preconditions.checkNotNull(str);
                return this;
            }

            public SectionHeaderControllerSubcomponentBuilder headerText(int i) {
                this.headerText = (Integer) Preconditions.checkNotNull(Integer.valueOf(i));
                return this;
            }

            public SectionHeaderControllerSubcomponentBuilder clickIntentAction(String str) {
                this.clickIntentAction = (String) Preconditions.checkNotNull(str);
                return this;
            }

            public SectionHeaderControllerSubcomponent build() {
                Class<String> cls = String.class;
                Preconditions.checkBuilderRequirement(this.nodeLabel, cls);
                Preconditions.checkBuilderRequirement(this.headerText, Integer.class);
                Preconditions.checkBuilderRequirement(this.clickIntentAction, cls);
                return new SectionHeaderControllerSubcomponentImpl(this.nodeLabel, this.headerText, this.clickIntentAction);
            }
        }

        public final class SectionHeaderControllerSubcomponentImpl implements SectionHeaderControllerSubcomponent {
            public Provider<String> clickIntentActionProvider;
            public Provider<Integer> headerTextProvider;
            public Provider<String> nodeLabelProvider;
            public Provider<SectionHeaderNodeControllerImpl> sectionHeaderNodeControllerImplProvider;

            public SectionHeaderControllerSubcomponentImpl(String str, Integer num, String str2) {
                initialize(str, num, str2);
            }

            public final void initialize(String str, Integer num, String str2) {
                this.nodeLabelProvider = InstanceFactory.create(str);
                this.headerTextProvider = InstanceFactory.create(num);
                this.clickIntentActionProvider = InstanceFactory.create(str2);
                this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, TvSysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, this.headerTextProvider, TvSysUIComponentImpl.this.provideActivityStarterProvider, this.clickIntentActionProvider));
            }

            public NodeController getNodeController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
            }

            public SectionHeaderController getHeaderController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
            }
        }

        public final class DozeComponentFactory implements DozeComponent.Builder {
            public DozeComponentFactory() {
            }

            public DozeComponent build(DozeMachine.Service service) {
                Preconditions.checkNotNull(service);
                return new DozeComponentImpl(service);
            }
        }

        public final class DozeComponentImpl implements DozeComponent {
            public Provider<DozeAuthRemover> dozeAuthRemoverProvider;
            public Provider<DozeDockHandler> dozeDockHandlerProvider;
            public Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
            public Provider<DozeMachine> dozeMachineProvider;
            public Provider<DozeMachine.Service> dozeMachineServiceProvider;
            public Provider<DozePauser> dozePauserProvider;
            public Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
            public Provider<DozeScreenState> dozeScreenStateProvider;
            public Provider<DozeSuppressor> dozeSuppressorProvider;
            public Provider<DozeTriggers> dozeTriggersProvider;
            public Provider<DozeUi> dozeUiProvider;
            public Provider<DozeWallpaperState> dozeWallpaperStateProvider;
            public Provider<Optional<Sensor>[]> providesBrightnessSensorsProvider;
            public Provider<DozeMachine.Part[]> providesDozeMachinePartsProvider;
            public Provider<WakeLock> providesDozeWakeLockProvider;
            public Provider<DozeMachine.Service> providesWrappedServiceProvider;

            public DozeComponentImpl(DozeMachine.Service service) {
                initialize(service);
            }

            public final void initialize(DozeMachine.Service service) {
                Factory create = InstanceFactory.create(service);
                this.dozeMachineServiceProvider = create;
                this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.dozeParametersProvider));
                this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(TvSysUIComponentImpl.this.builderProvider3, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider));
                this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideAlarmManagerProvider, TvSysUIComponentImpl.this.alwaysOnDisplayPolicyProvider));
                this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(TvSysUIComponentImpl.this.falsingCollectorImplProvider));
                this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.dockManagerImplProvider, TvSysUIComponentImpl.this.provideProximitySensorProvider, TvSysUIComponentImpl.this.provideProximityCheckProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.secureSettingsImplProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.devicePostureControllerImplProvider));
                this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.this$0.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.dozeLogProvider));
                this.providesBrightnessSensorsProvider = DozeModule_ProvidesBrightnessSensorsFactory.create(TvSysUIComponentImpl.this.asyncSensorManagerProvider, TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.dozeParametersProvider);
                this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.providesWrappedServiceProvider, TvSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesBrightnessSensorsProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), TvSysUIComponentImpl.this.alwaysOnDisplayPolicyProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.devicePostureControllerImplProvider, TvSysUIComponentImpl.this.dozeLogProvider));
                this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.dozeParametersProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.udfpsControllerProvider, TvSysUIComponentImpl.this.dozeLogProvider, this.dozeScreenBrightnessProvider));
                this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), TvSysUIComponentImpl.this.biometricUnlockControllerProvider, TvSysUIComponentImpl.this.dozeParametersProvider));
                this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(TvSysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, TvSysUIComponentImpl.this.dockManagerImplProvider));
                this.dozeAuthRemoverProvider = DoubleCheck.provider(DozeAuthRemover_Factory.create(TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider));
                Provider<DozeSuppressor> provider = DoubleCheck.provider(DozeSuppressor_Factory.create(TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.this$0.provideUiModeManagerProvider, TvSysUIComponentImpl.this.biometricUnlockControllerProvider));
                this.dozeSuppressorProvider = provider;
                this.providesDozeMachinePartsProvider = DozeModule_ProvidesDozeMachinePartsFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, this.dozeAuthRemoverProvider, provider);
                this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, TvSysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.provideBatteryControllerProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.dockManagerImplProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, this.providesDozeMachinePartsProvider));
            }

            public DozeMachine getDozeMachine() {
                return this.dozeMachineProvider.get();
            }
        }

        public final class DreamOverlayComponentFactory implements DreamOverlayComponent.Factory {
            public DreamOverlayComponentFactory() {
            }

            public DreamOverlayComponent create(ViewModelStore viewModelStore, Complication.Host host) {
                Preconditions.checkNotNull(viewModelStore);
                Preconditions.checkNotNull(host);
                return new DreamOverlayComponentImpl(viewModelStore, host);
            }
        }

        public final class DreamOverlayComponentImpl implements DreamOverlayComponent {
            public Provider<FlingAnimationUtils.Builder> builderProvider;
            public Provider<ComplicationCollectionLiveData> complicationCollectionLiveDataProvider;
            public Provider<ComplicationCollectionViewModel> complicationCollectionViewModelProvider;
            public Provider<ComplicationHostViewController> complicationHostViewControllerProvider;
            public Provider<ComplicationLayoutEngine> complicationLayoutEngineProvider;
            public Provider<ComplicationViewModelComponent.Factory> complicationViewModelComponentFactoryProvider;
            public Provider<ComplicationViewModelTransformer> complicationViewModelTransformerProvider;
            public Provider<DateFormatUtil> dateFormatUtilProvider;
            public Provider<DreamOverlayContainerViewController> dreamOverlayContainerViewControllerProvider;
            public Provider<DreamOverlayStatusBarViewController> dreamOverlayStatusBarViewControllerProvider;
            public final Complication.Host host;
            public Provider<Long> providesBurnInProtectionUpdateIntervalProvider;
            public Provider<ComplicationCollectionViewModel> providesComplicationCollectionViewModelProvider;
            public Provider<ConstraintLayout> providesComplicationHostViewProvider;
            public Provider<Integer> providesComplicationPaddingProvider;
            public Provider<Integer> providesComplicationsFadeInDurationProvider;
            public Provider<Integer> providesComplicationsFadeOutDurationProvider;
            public Provider<Integer> providesComplicationsRestoreTimeoutProvider;
            public Provider<DreamOverlayContainerView> providesDreamOverlayContainerViewProvider;
            public Provider<ViewGroup> providesDreamOverlayContentViewProvider;
            public Provider<DreamOverlayStatusBarView> providesDreamOverlayStatusBarViewProvider;
            public Provider<LifecycleOwner> providesLifecycleOwnerProvider;
            public Provider<Lifecycle> providesLifecycleProvider;
            public Provider<LifecycleRegistry> providesLifecycleRegistryProvider;
            public Provider<Integer> providesMaxBurnInOffsetProvider;
            public Provider<Long> providesMillisUntilFullJitterProvider;
            public Provider<TouchInsetManager> providesTouchInsetManagerProvider;
            public Provider<TouchInsetManager.TouchInsetSession> providesTouchInsetSessionProvider;
            public final ViewModelStore store;
            public Provider<ViewModelStore> storeProvider;

            public DreamOverlayComponentImpl(ViewModelStore viewModelStore, Complication.Host host2) {
                this.store = viewModelStore;
                this.host = host2;
                initialize(viewModelStore, host2);
            }

            public final FlingAnimationUtils namedFlingAnimationUtils() {
                return BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory.providesSwipeToBouncerFlingAnimationUtilsOpening(this.builderProvider);
            }

            public final FlingAnimationUtils namedFlingAnimationUtils2() {
                return BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsClosingFactory.providesSwipeToBouncerFlingAnimationUtilsClosing(this.builderProvider);
            }

            public final float namedFloat() {
                return BouncerSwipeModule.providesSwipeToBouncerStartRegion(TvSysUIComponentImpl.this.this$0.mainResources());
            }

            public final BouncerSwipeTouchHandler bouncerSwipeTouchHandler() {
                return new BouncerSwipeTouchHandler(TvSysUIComponentImpl.this.this$0.displayMetrics(), (StatusBarKeyguardViewManager) TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider.get(), Optional.empty(), (NotificationShadeWindowController) TvSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider.get(), BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory.providesValueAnimatorCreator(), BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory.providesVelocityTrackerFactory(), namedFlingAnimationUtils(), namedFlingAnimationUtils2(), namedFloat(), (UiEventLogger) TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider.get());
            }

            public final DreamTouchHandler providesBouncerSwipeTouchHandler() {
                return BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler());
            }

            public final Complication.VisibilityController visibilityController() {
                return ComplicationModule_ProvidesVisibilityControllerFactory.providesVisibilityController(this.complicationLayoutEngineProvider.get());
            }

            public final HideComplicationTouchHandler hideComplicationTouchHandler() {
                return HideComplicationTouchHandler_Factory.newInstance(visibilityController(), this.providesComplicationsRestoreTimeoutProvider.get().intValue(), this.providesTouchInsetManagerProvider.get(), (Executor) TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), TvSysUIComponentImpl.this.this$0.mainHandler());
            }

            public final DreamTouchHandler providesHideComplicationTouchHandler() {
                return HideComplicationModule_ProvidesHideComplicationTouchHandlerFactory.providesHideComplicationTouchHandler(hideComplicationTouchHandler());
            }

            public final Set<DreamTouchHandler> setOfDreamTouchHandler() {
                return SetBuilder.newSetBuilder(2).add(providesBouncerSwipeTouchHandler()).add(providesHideComplicationTouchHandler()).build();
            }

            public final void initialize(ViewModelStore viewModelStore, Complication.Host host2) {
                this.providesDreamOverlayContainerViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory.create(TvSysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesComplicationHostViewProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationHostViewFactory.create(TvSysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesComplicationPaddingProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationPaddingFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider));
                Provider<TouchInsetManager> provider = DoubleCheck.provider(DreamOverlayModule_ProvidesTouchInsetManagerFactory.create(TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, this.providesDreamOverlayContainerViewProvider));
                this.providesTouchInsetManagerProvider = provider;
                this.providesTouchInsetSessionProvider = DreamOverlayModule_ProvidesTouchInsetSessionFactory.create(provider);
                this.providesComplicationsFadeInDurationProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider));
                Provider<Integer> provider2 = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider));
                this.providesComplicationsFadeOutDurationProvider = provider2;
                this.complicationLayoutEngineProvider = DoubleCheck.provider(ComplicationLayoutEngine_Factory.create(this.providesComplicationHostViewProvider, this.providesComplicationPaddingProvider, this.providesTouchInsetSessionProvider, this.providesComplicationsFadeInDurationProvider, provider2));
                DelegateFactory delegateFactory = new DelegateFactory();
                this.providesLifecycleOwnerProvider = delegateFactory;
                Provider<LifecycleRegistry> provider3 = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleRegistryFactory.create(delegateFactory));
                this.providesLifecycleRegistryProvider = provider3;
                DelegateFactory.setDelegate(this.providesLifecycleOwnerProvider, DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleOwnerFactory.create(provider3)));
                this.storeProvider = InstanceFactory.create(viewModelStore);
                this.complicationCollectionLiveDataProvider = ComplicationCollectionLiveData_Factory.create(TvSysUIComponentImpl.this.dreamOverlayStateControllerProvider);
                AnonymousClass1 r13 = new Provider<ComplicationViewModelComponent.Factory>() {
                    public ComplicationViewModelComponent.Factory get() {
                        return new ComplicationViewModelComponentFactory();
                    }
                };
                this.complicationViewModelComponentFactoryProvider = r13;
                ComplicationViewModelTransformer_Factory create = ComplicationViewModelTransformer_Factory.create(r13);
                this.complicationViewModelTransformerProvider = create;
                ComplicationCollectionViewModel_Factory create2 = ComplicationCollectionViewModel_Factory.create(this.complicationCollectionLiveDataProvider, create);
                this.complicationCollectionViewModelProvider = create2;
                ComplicationModule_ProvidesComplicationCollectionViewModelFactory create3 = ComplicationModule_ProvidesComplicationCollectionViewModelFactory.create(this.storeProvider, create2);
                this.providesComplicationCollectionViewModelProvider = create3;
                this.complicationHostViewControllerProvider = ComplicationHostViewController_Factory.create(this.providesComplicationHostViewProvider, this.complicationLayoutEngineProvider, this.providesLifecycleOwnerProvider, create3);
                this.providesDreamOverlayContentViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContentViewFactory.create(this.providesDreamOverlayContainerViewProvider));
                this.providesDreamOverlayStatusBarViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory.create(this.providesDreamOverlayContainerViewProvider));
                this.dateFormatUtilProvider = DateFormatUtil_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider);
                this.dreamOverlayStatusBarViewControllerProvider = DoubleCheck.provider(DreamOverlayStatusBarViewController_Factory.create(this.providesDreamOverlayStatusBarViewProvider, TvSysUIComponentImpl.this.this$0.provideResourcesProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.this$0.provideConnectivityManagagerProvider, this.providesTouchInsetSessionProvider, TvSysUIComponentImpl.this.this$0.provideAlarmManagerProvider, TvSysUIComponentImpl.this.nextAlarmControllerImplProvider, this.dateFormatUtilProvider, TvSysUIComponentImpl.this.provideIndividualSensorPrivacyControllerProvider, TvSysUIComponentImpl.this.dreamOverlayNotificationCountProvider, TvSysUIComponentImpl.this.zenModeControllerImplProvider, TvSysUIComponentImpl.this.statusBarWindowStateControllerProvider));
                this.providesMaxBurnInOffsetProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesMaxBurnInOffsetFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider));
                this.providesBurnInProtectionUpdateIntervalProvider = DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider);
                this.providesMillisUntilFullJitterProvider = DreamOverlayModule_ProvidesMillisUntilFullJitterFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider);
                this.dreamOverlayContainerViewControllerProvider = DoubleCheck.provider(DreamOverlayContainerViewController_Factory.create(this.providesDreamOverlayContainerViewProvider, this.complicationHostViewControllerProvider, this.providesDreamOverlayContentViewProvider, this.dreamOverlayStatusBarViewControllerProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, TvSysUIComponentImpl.this.blurUtilsProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideResourcesProvider, this.providesMaxBurnInOffsetProvider, this.providesBurnInProtectionUpdateIntervalProvider, this.providesMillisUntilFullJitterProvider));
                this.providesLifecycleProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleFactory.create(this.providesLifecycleOwnerProvider));
                this.builderProvider = FlingAnimationUtils_Builder_Factory.create(TvSysUIComponentImpl.this.this$0.provideDisplayMetricsProvider);
                this.providesComplicationsRestoreTimeoutProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory.create(TvSysUIComponentImpl.this.this$0.provideResourcesProvider));
            }

            public DreamOverlayContainerViewController getDreamOverlayContainerViewController() {
                return this.dreamOverlayContainerViewControllerProvider.get();
            }

            public LifecycleRegistry getLifecycleRegistry() {
                return this.providesLifecycleRegistryProvider.get();
            }

            public LifecycleOwner getLifecycleOwner() {
                return this.providesLifecycleOwnerProvider.get();
            }

            public DreamOverlayTouchMonitor getDreamOverlayTouchMonitor() {
                return new DreamOverlayTouchMonitor((Executor) TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), this.providesLifecycleProvider.get(), new InputSessionComponentFactory(), setOfDreamTouchHandler());
            }

            public final class ComplicationViewModelComponentFactory implements ComplicationViewModelComponent.Factory {
                public ComplicationViewModelComponentFactory() {
                }

                public ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId) {
                    Preconditions.checkNotNull(complication);
                    Preconditions.checkNotNull(complicationId);
                    return new ComplicationViewModelComponentI(complication, complicationId);
                }
            }

            public final class ComplicationViewModelComponentI implements ComplicationViewModelComponent {
                public final Complication complication;
                public final ComplicationId id;

                public ComplicationViewModelComponentI(Complication complication2, ComplicationId complicationId) {
                    this.complication = complication2;
                    this.id = complicationId;
                }

                public final ComplicationViewModel complicationViewModel() {
                    return new ComplicationViewModel(this.complication, this.id, DreamOverlayComponentImpl.this.host);
                }

                public ComplicationViewModelProvider getViewModelProvider() {
                    return new ComplicationViewModelProvider(DreamOverlayComponentImpl.this.store, complicationViewModel());
                }
            }

            public final class InputSessionComponentFactory implements InputSessionComponent.Factory {
                public InputSessionComponentFactory() {
                }

                public InputSessionComponent create(String str, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
                    Preconditions.checkNotNull(str);
                    Preconditions.checkNotNull(inputChannelCompat$InputEventListener);
                    Preconditions.checkNotNull(onGestureListener);
                    Preconditions.checkNotNull(Boolean.valueOf(z));
                    return new InputSessionComponentI(str, inputChannelCompat$InputEventListener, onGestureListener, Boolean.valueOf(z));
                }
            }

            public final class InputSessionComponentI implements InputSessionComponent {
                public final GestureDetector.OnGestureListener gestureListener;
                public final InputChannelCompat$InputEventListener inputEventListener;
                public final String name;
                public final Boolean pilferOnGestureConsume;

                public InputSessionComponentI(String str, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, GestureDetector.OnGestureListener onGestureListener, Boolean bool) {
                    this.name = str;
                    this.inputEventListener = inputChannelCompat$InputEventListener;
                    this.gestureListener = onGestureListener;
                    this.pilferOnGestureConsume = bool;
                }

                public InputSession getInputSession() {
                    return new InputSession(this.name, this.inputEventListener, this.gestureListener, this.pilferOnGestureConsume.booleanValue());
                }
            }
        }

        public final class FragmentCreatorFactory implements FragmentService.FragmentCreator.Factory {
            public FragmentCreatorFactory() {
            }

            public FragmentService.FragmentCreator build() {
                return new FragmentCreatorImpl();
            }
        }

        public final class FragmentCreatorImpl implements FragmentService.FragmentCreator {
            public FragmentCreatorImpl() {
            }

            public final QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger() {
                return new QSFragmentDisableFlagsLogger((LogBuffer) TvSysUIComponentImpl.this.provideQSFragmentDisableLogBufferProvider.get(), (DisableFlagsLogger) TvSysUIComponentImpl.this.disableFlagsLoggerProvider.get());
            }

            public QSFragment createQSFragment() {
                return new QSFragment((RemoteInputQuickSettingsDisabler) TvSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), (QSTileHost) TvSysUIComponentImpl.this.qSTileHostProvider.get(), (StatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (CommandQueue) TvSysUIComponentImpl.this.provideCommandQueueProvider.get(), (MediaHost) TvSysUIComponentImpl.this.providesQSMediaHostProvider.get(), (MediaHost) TvSysUIComponentImpl.this.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) TvSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(), qSFragmentDisableFlagsLogger(), (FalsingManager) TvSysUIComponentImpl.this.falsingManagerProxyProvider.get(), (DumpManager) TvSysUIComponentImpl.this.this$0.dumpManagerProvider.get());
            }
        }

        public final class QSFragmentComponentFactory implements QSFragmentComponent.Factory {
            public QSFragmentComponentFactory() {
            }

            public QSFragmentComponent create(QSFragment qSFragment) {
                Preconditions.checkNotNull(qSFragment);
                return new QSFragmentComponentImpl(qSFragment);
            }
        }

        public final class QSFragmentComponentImpl implements QSFragmentComponent {
            public Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
            public Provider<CarrierTextManager.Builder> builderProvider;
            public Provider<QSCarrierGroupController.Builder> builderProvider2;
            public Provider factoryProvider;
            public Provider<BrightnessController.Factory> factoryProvider2;
            public Provider<VariableDateViewController.Factory> factoryProvider3;
            public Provider<MultiUserSwitchController.Factory> factoryProvider4;
            public Provider<FooterActionsController> footerActionsControllerProvider;
            public Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
            public Provider<QSPanel> provideQSPanelProvider;
            public Provider<View> provideRootViewProvider;
            public Provider<Context> provideThemedContextProvider;
            public Provider<LayoutInflater> provideThemedLayoutInflaterProvider;
            public Provider<BatteryMeterView> providesBatteryMeterViewProvider;
            public Provider<OngoingPrivacyChip> providesPrivacyChipProvider;
            public Provider<QSContainerImpl> providesQSContainerImplProvider;
            public Provider<QSCustomizer> providesQSCutomizerProvider;
            public Provider<View> providesQSFgsManagerFooterViewProvider;
            public Provider<FooterActionsView> providesQSFooterActionsViewProvider;
            public Provider<QSFooter> providesQSFooterProvider;
            public Provider<QSFooterView> providesQSFooterViewProvider;
            public Provider<View> providesQSSecurityFooterViewProvider;
            public Provider<Boolean> providesQSUsingCollapsedLandscapeMediaProvider;
            public Provider<Boolean> providesQSUsingMediaPlayerProvider;
            public Provider<QuickQSPanel> providesQuickQSPanelProvider;
            public Provider<QuickStatusBarHeader> providesQuickStatusBarHeaderProvider;
            public Provider<StatusIconContainer> providesStatusIconContainerProvider;
            public Provider<QSAnimator> qSAnimatorProvider;
            public Provider<QSContainerImplController> qSContainerImplControllerProvider;
            public Provider<QSCustomizerController> qSCustomizerControllerProvider;
            public Provider<QSFgsManagerFooter> qSFgsManagerFooterProvider;
            public Provider<QSFooterViewController> qSFooterViewControllerProvider;
            public Provider<QSPanelController> qSPanelControllerProvider;
            public Provider qSSecurityFooterProvider;
            public Provider<QSSquishinessController> qSSquishinessControllerProvider;
            public Provider<QSFragment> qsFragmentProvider;
            public Provider<QuickQSPanelController> quickQSPanelControllerProvider;
            public Provider quickStatusBarHeaderControllerProvider;
            public Provider<TileAdapter> tileAdapterProvider;
            public Provider<TileQueryHelper> tileQueryHelperProvider;

            public QSFragmentComponentImpl(QSFragment qSFragment) {
                initialize(qSFragment);
            }

            public final void initialize(QSFragment qSFragment) {
                Factory create = InstanceFactory.create(qSFragment);
                this.qsFragmentProvider = create;
                QSFragmentModule_ProvideRootViewFactory create2 = QSFragmentModule_ProvideRootViewFactory.create(create);
                this.provideRootViewProvider = create2;
                this.provideQSPanelProvider = QSFragmentModule_ProvideQSPanelFactory.create(create2);
                this.providesQSCutomizerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSCutomizerFactory.create(this.provideRootViewProvider));
                this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.provideUserTrackerProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider));
                QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
                this.provideThemedContextProvider = create3;
                this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(create3, TvSysUIComponentImpl.this.qSTileHostProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider));
                this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, TvSysUIComponentImpl.this.qSTileHostProvider, this.tileAdapterProvider, TvSysUIComponentImpl.this.this$0.screenLifecycleProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.lightBarControllerProvider, TvSysUIComponentImpl.this.configurationControllerImplProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider));
                this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(TvSysUIComponentImpl.this.this$0.contextProvider);
                this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, this.qSCustomizerControllerProvider));
                this.factoryProvider2 = BrightnessController_Factory_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.provideBgHandlerProvider);
                this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, TvSysUIComponentImpl.this.providesQSMediaHostProvider, this.factoryProvider, TvSysUIComponentImpl.this.this$0.dumpManagerProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.qSLoggerProvider, this.factoryProvider2, TvSysUIComponentImpl.this.factoryProvider8, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider));
                QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create4 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
                this.providesQuickStatusBarHeaderProvider = create4;
                this.providesQuickQSPanelProvider = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create4);
                this.providesQSUsingCollapsedLandscapeMediaProvider = QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory.create(TvSysUIComponentImpl.this.this$0.contextProvider);
                Provider<QuickQSPanelController> provider = DoubleCheck.provider(QuickQSPanelController_Factory.create(this.providesQuickQSPanelProvider, TvSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, TvSysUIComponentImpl.this.providesQuickQSMediaHostProvider, this.providesQSUsingCollapsedLandscapeMediaProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.qSLoggerProvider, TvSysUIComponentImpl.this.this$0.dumpManagerProvider));
                this.quickQSPanelControllerProvider = provider;
                this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider, TvSysUIComponentImpl.this.qSTileHostProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.this$0.qSExpansionPathInterpolatorProvider));
                this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
                this.providesPrivacyChipProvider = DoubleCheck.provider(QSFragmentModule_ProvidesPrivacyChipFactory.create(this.providesQuickStatusBarHeaderProvider));
                this.providesStatusIconContainerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesStatusIconContainerFactory.create(this.providesQuickStatusBarHeaderProvider));
                this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(TvSysUIComponentImpl.this.privacyItemControllerProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, this.providesPrivacyChipProvider, TvSysUIComponentImpl.this.privacyDialogControllerProvider, TvSysUIComponentImpl.this.privacyLoggerProvider, this.providesStatusIconContainerProvider, TvSysUIComponentImpl.this.this$0.providePermissionManagerProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideActivityStarterProvider, TvSysUIComponentImpl.this.appOpsControllerImplProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.this$0.provideSafetyCenterManagerProvider);
                this.builderProvider = CarrierTextManager_Builder_Factory.create(TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.this$0.provideResourcesProvider, TvSysUIComponentImpl.this.this$0.provideWifiManagerProvider, TvSysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.telephonyListenerManagerProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(TvSysUIComponentImpl.this.provideActivityStarterProvider, TvSysUIComponentImpl.this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), TvSysUIComponentImpl.this.networkControllerImplProvider, this.builderProvider, TvSysUIComponentImpl.this.this$0.contextProvider, TvSysUIComponentImpl.this.carrierConfigTrackerProvider, TvSysUIComponentImpl.this.featureFlagsDebugProvider, TvSysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                this.factoryProvider3 = VariableDateViewController_Factory_Factory.create(TvSysUIComponentImpl.this.bindSystemClockProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.provideTimeTickHandlerProvider);
                QSFragmentModule_ProvidesBatteryMeterViewFactory create5 = QSFragmentModule_ProvidesBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
                this.providesBatteryMeterViewProvider = create5;
                this.batteryMeterViewControllerProvider = BatteryMeterViewController_Factory.create(create5, TvSysUIComponentImpl.this.configurationControllerImplProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.this$0.provideContentResolverProvider, TvSysUIComponentImpl.this.provideBatteryControllerProvider);
                Provider provider2 = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, this.headerPrivacyIconsControllerProvider, TvSysUIComponentImpl.this.statusBarIconControllerImplProvider, TvSysUIComponentImpl.this.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, TvSysUIComponentImpl.this.sysuiColorExtractorProvider, TvSysUIComponentImpl.this.this$0.qSExpansionPathInterpolatorProvider, TvSysUIComponentImpl.this.featureFlagsDebugProvider, this.factoryProvider3, this.batteryMeterViewControllerProvider, TvSysUIComponentImpl.this.statusBarContentInsetsProvider));
                this.quickStatusBarHeaderControllerProvider = provider2;
                this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, provider2, TvSysUIComponentImpl.this.configurationControllerImplProvider));
                QSFragmentModule_ProvidesQSFooterViewFactory create6 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
                this.providesQSFooterViewProvider = create6;
                Provider<QSFooterViewController> provider3 = DoubleCheck.provider(QSFooterViewController_Factory.create(create6, TvSysUIComponentImpl.this.provideUserTrackerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.provideActivityStarterProvider, this.qSPanelControllerProvider));
                this.qSFooterViewControllerProvider = provider3;
                this.providesQSFooterProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSFooterFactory.create(provider3));
                this.qSSquishinessControllerProvider = DoubleCheck.provider(QSSquishinessController_Factory.create(this.qSAnimatorProvider, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider));
                this.providesQSFooterActionsViewProvider = QSFragmentModule_ProvidesQSFooterActionsViewFactory.create(this.provideRootViewProvider);
                this.factoryProvider4 = DoubleCheck.provider(MultiUserSwitchController_Factory_Factory.create(TvSysUIComponentImpl.this.this$0.provideUserManagerProvider, TvSysUIComponentImpl.this.userSwitcherControllerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.userSwitchDialogControllerProvider, TvSysUIComponentImpl.this.featureFlagsDebugProvider, TvSysUIComponentImpl.this.provideActivityStarterProvider));
                QSFragmentModule_ProvideThemedLayoutInflaterFactory create7 = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(this.provideThemedContextProvider);
                this.provideThemedLayoutInflaterProvider = create7;
                Provider<View> provider4 = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(create7, this.providesQSFooterActionsViewProvider));
                this.providesQSSecurityFooterViewProvider = provider4;
                this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider4, TvSysUIComponentImpl.this.provideUserTrackerProvider, TvSysUIComponentImpl.this.this$0.provideMainHandlerProvider, TvSysUIComponentImpl.this.provideActivityStarterProvider, TvSysUIComponentImpl.this.securityControllerImplProvider, TvSysUIComponentImpl.this.provideDialogLaunchAnimatorProvider, TvSysUIComponentImpl.this.provideBgLooperProvider, TvSysUIComponentImpl.this.broadcastDispatcherProvider));
                Provider<View> provider5 = DoubleCheck.provider(QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, this.providesQSFooterActionsViewProvider));
                this.providesQSFgsManagerFooterViewProvider = provider5;
                this.qSFgsManagerFooterProvider = DoubleCheck.provider(QSFgsManagerFooter_Factory.create(provider5, TvSysUIComponentImpl.this.this$0.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider, TvSysUIComponentImpl.this.fgsManagerControllerProvider));
                this.footerActionsControllerProvider = DoubleCheck.provider(FooterActionsController_Factory.create(this.providesQSFooterActionsViewProvider, this.factoryProvider4, TvSysUIComponentImpl.this.provideActivityStarterProvider, TvSysUIComponentImpl.this.this$0.provideUserManagerProvider, TvSysUIComponentImpl.this.provideUserTrackerProvider, TvSysUIComponentImpl.this.userInfoControllerImplProvider, TvSysUIComponentImpl.this.providesDeviceProvisionedControllerProvider, this.qSSecurityFooterProvider, this.qSFgsManagerFooterProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.globalActionsDialogLiteProvider, TvSysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.isPMLiteEnabledProvider, TvSysUIComponentImpl.this.globalSettingsImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
            }

            public QSPanelController getQSPanelController() {
                return this.qSPanelControllerProvider.get();
            }

            public QuickQSPanelController getQuickQSPanelController() {
                return this.quickQSPanelControllerProvider.get();
            }

            public QSAnimator getQSAnimator() {
                return this.qSAnimatorProvider.get();
            }

            public QSContainerImplController getQSContainerImplController() {
                return this.qSContainerImplControllerProvider.get();
            }

            public QSFooter getQSFooter() {
                return this.providesQSFooterProvider.get();
            }

            public QSCustomizerController getQSCustomizerController() {
                return this.qSCustomizerControllerProvider.get();
            }

            public QSSquishinessController getQSSquishinessController() {
                return this.qSSquishinessControllerProvider.get();
            }

            public FooterActionsController getQSFooterActionController() {
                return this.footerActionsControllerProvider.get();
            }
        }
    }
}
