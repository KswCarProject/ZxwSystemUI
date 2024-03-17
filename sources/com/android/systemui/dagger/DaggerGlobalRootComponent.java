package com.android.systemui.dagger;

import android.animation.AnimationHandler;
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
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IBatteryStats;
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
import com.android.keyguard.CarrierText;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import com.android.keyguard.KeyguardBiometricLockoutLogger_Factory;
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
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.LockIconViewController_Factory;
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
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetBatteryMeterViewFactory;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetCarrierTextFactory;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardClockSwitchFactory;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardSliceViewFactory;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
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
import com.android.systemui.LatencyTester;
import com.android.systemui.LatencyTester_Factory;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.ScreenDecorations_Factory;
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
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.AuthRippleController_Factory;
import com.android.systemui.biometrics.AuthRippleView;
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
import com.android.systemui.clipboardoverlay.ClipboardListener;
import com.android.systemui.clipboardoverlay.ClipboardListener_Factory;
import com.android.systemui.clipboardoverlay.ClipboardOverlayControllerFactory;
import com.android.systemui.clipboardoverlay.ClipboardOverlayControllerFactory_Factory;
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
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory_Factory;
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
import com.android.systemui.flags.FeatureFlags;
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
import com.android.systemui.log.dagger.LogModule_ProvideCollapsedSbFragmentLogBufferFactory;
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
import com.android.systemui.log.dagger.LogModule_ProvideSwipeAwayGestureLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideToastLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidesMediaTimeoutListenerLogBufferFactory;
import com.android.systemui.lowlightclock.LowLightClockController;
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
import com.android.systemui.plugins.DarkIconDispatcher;
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
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.recents.ScreenPinningRequest_Factory;
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
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.KeyguardIndicationController_Factory;
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
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationShelfController_Factory;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.QsFrameTranslateImpl;
import com.android.systemui.statusbar.QsFrameTranslateImpl_Factory;
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
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.commandline.CommandRegistry_Factory;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.CallbackHandler_Factory;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory_Factory;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.core.StatusBarInitializer_Factory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideCommandQueueFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationMediaManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideSmartReplyControllerFactory;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.PrivacyDotViewController_Factory;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController_Factory;
import com.android.systemui.statusbar.events.SystemEventCoordinator;
import com.android.systemui.statusbar.events.SystemEventCoordinator_Factory;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler_Factory;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler_Factory;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger_Factory;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.statusbar.gesture.TapGestureDetector_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController_Factory;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager_Factory;
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
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationClickerLogger;
import com.android.systemui.statusbar.notification.NotificationClickerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationClicker_Builder_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager_Factory;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.SectionClassifier_Factory;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager_Factory;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder_Factory;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorImpl_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsModule_NotifCoordinatorsFactory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions_Factory;
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
import com.android.systemui.statusbar.notification.collection.render.NodeSpecBuilderLogger;
import com.android.systemui.statusbar.notification.collection.render.NodeSpecBuilderLogger_Factory;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn_Factory;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory_Impl;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManager_Factory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideCommonNotifCollectionFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupExpansionManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupMembershipManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotifGutsViewManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotifShadeEventSourceFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationsControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideOnUserInteractionCallbackFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconBuilder_Factory;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconManager_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder_Factory;
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
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer_Factory;
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
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLogger;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper_Builder_Factory;
import com.android.systemui.statusbar.notification.stack.StackStateLogger;
import com.android.systemui.statusbar.notification.stack.StackStateLogger_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoHideController_Factory_Factory;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.C0004AutoHideController_Factory;
import com.android.systemui.statusbar.phone.C0005LightBarController_Factory;
import com.android.systemui.statusbar.phone.C0006LightBarTransitionsController_Factory;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks_Factory;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl_Factory;
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
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardBypassController_Factory;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl_Factory;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.statusbar.phone.KeyguardLiftController_Factory;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger_Factory;
import com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController;
import com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController_Factory;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory_Factory;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController_Factory_Impl;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.LightsOutNotifController_Factory;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger_Factory;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.LockscreenWallpaper_Factory;
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
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_PanelEventsEmitter_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationsQSContainerController;
import com.android.systemui.statusbar.phone.NotificationsQSContainerController_Factory;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy_Factory;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController_Factory_Factory;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarDemoMode;
import com.android.systemui.statusbar.phone.StatusBarDemoMode_Factory;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener_Factory;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController_TintedIconManager_Factory_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy_Factory;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager_Factory;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager_Factory;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.TapAgainViewController;
import com.android.systemui.statusbar.phone.TapAgainViewController_Factory;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetAuthRippleViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetBatteryMeterViewControllerFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetBatteryMeterViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetLockIconViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetNotificationPanelViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetTapAgainViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationShelfFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesStatusBarWindowViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesStatusIconContainerFactory;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideBatteryMeterViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideClockFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideLightsOutNotifViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideOperatorNameViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags_Factory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger_Factory;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager_Factory;
import com.android.systemui.statusbar.phone.shade.transition.C0007SplitShadeOverScroller_Factory;
import com.android.systemui.statusbar.phone.shade.transition.NoOpOverScroller_Factory;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController_Factory;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller_Factory_Impl;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController_Factory;
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
import com.android.systemui.statusbar.policy.Clock;
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
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController_Factory;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
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
import com.android.systemui.statusbar.policy.UserInfoController;
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
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity_Factory;
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
import com.android.systemui.tuner.TunerService;
import com.android.systemui.tuner.TunerServiceImpl;
import com.android.systemui.tuner.TunerServiceImpl_Factory;
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
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvidesMainMessageRouterFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.io.Files_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_MemoryTile_Factory;
import com.android.systemui.util.leak.GarbageMonitor_Service_Factory;
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
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
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
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.compatui.CompatUIController;
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
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayLayoutFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory;
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
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory;
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
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideAppPairsFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideBubbleControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideFreeformTaskListenerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideFullscreenUnfoldControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideLegacySplitScreenFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideOneHandedControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipAnimationControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipAppOpsListenerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipBoundsStateFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipMotionHelperFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipParamsChangedForwarderFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipSnapAlgorithmFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTaskOrganizerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTouchHandlerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionStateFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideSplitScreenControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideStageTaskUnfoldControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideUnfoldBackgroundControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidesPipBoundsAlgorithmFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidesPipPhoneMenuControllerFactory;
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
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
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

public final class DaggerGlobalRootComponent implements GlobalRootComponent {
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
    public Provider<ClipboardManager> provideClipboardManagerProvider;
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
    public Provider<IBatteryStats> provideIBatteryStatsProvider;
    public Provider<IDreamManager> provideIDreamManagerProvider;
    public Provider<INotificationManager> provideINotificationManagerProvider;
    public Provider<IPackageManager> provideIPackageManagerProvider;
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
    public Provider<MediaRouter2Manager> provideMediaRouter2ManagerProvider;
    public Provider<MediaSessionManager> provideMediaSessionManagerProvider;
    public Provider<MetricsLogger> provideMetricsLoggerProvider;
    public Provider<Optional<NaturalRotationUnfoldProgressProvider>> provideNaturalRotationProgressProvider;
    public Provider<NetworkScoreManager> provideNetworkScoreManagerProvider;
    public Provider<NotificationManager> provideNotificationManagerProvider;
    public Provider<NotificationMessagingUtil> provideNotificationMessagingUtilProvider;
    public Provider<Optional<TelecomManager>> provideOptionalTelecomManagerProvider;
    public Provider<Optional<Vibrator>> provideOptionalVibratorProvider;
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

    public DaggerGlobalRootComponent(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.context = context2;
        this.globalModule = globalModule2;
        initialize(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
        initialize2(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
    }

    public static GlobalRootComponent.Builder builder() {
        return new Builder();
    }

    public final Handler mainHandler() {
        return GlobalConcurrencyModule_ProvideMainHandlerFactory.provideMainHandler(GlobalConcurrencyModule_ProvideMainLooperFactory.provideMainLooper());
    }

    public final Resources mainResources() {
        return FrameworkServicesModule_ProvideResourcesFactory.provideResources(this.context);
    }

    public final DisplayMetrics displayMetrics() {
        return GlobalModule_ProvideDisplayMetricsFactory.provideDisplayMetrics(this.globalModule, this.context);
    }

    public final void initialize(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.contextProvider = InstanceFactory.create(context2);
        this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.create());
        this.provideUiEventLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideUiEventLoggerFactory.create());
        this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.create());
        this.provideWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWindowManagerFactory.create(this.contextProvider));
        this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
        this.provideLauncherAppsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLauncherAppsFactory.create(this.contextProvider));
        this.provideInteractionJankMonitorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInteractionJankMonitorFactory.create());
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
        this.provideLockPatternUtilsProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideLockPatternUtilsFactory.create(androidInternalsModule, this.contextProvider));
        this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
        this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
        this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
        this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
        this.providerLayoutInflaterProvider = DoubleCheck.provider(FrameworkServicesModule_ProviderLayoutInflaterFactory.create(frameworkServicesModule, this.contextProvider));
        this.provideMainDelayableExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
        this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
        this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
        this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
        this.provideNotificationMessagingUtilProvider = AndroidInternalsModule_ProvideNotificationMessagingUtilFactory.create(androidInternalsModule, this.contextProvider);
        this.providesChoreographerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesChoreographerFactory.create(frameworkServicesModule));
        this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
        this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
        this.provideAmbientDisplayConfigurationProvider = FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory.create(frameworkServicesModule, this.contextProvider);
        Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider9 = DoubleCheck.provider(UnfoldTransitionModule_ProvideNaturalRotationProgressProviderFactory.create(unfoldTransitionModule, this.contextProvider, this.provideIWindowManagerProvider, this.unfoldTransitionProgressProvider));
        this.provideNaturalRotationProgressProvider = provider9;
        this.provideStatusBarScopedTransitionProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideStatusBarScopedTransitionProviderFactory.create(unfoldTransitionModule, provider9));
        this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
        this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
        this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
        this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
        this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
        this.provideDisplayIdProvider = FrameworkServicesModule_ProvideDisplayIdFactory.create(this.contextProvider);
        this.provideCarrierConfigManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCarrierConfigManagerFactory.create(this.contextProvider));
        this.provideSubcriptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSubcriptionManagerFactory.create(this.contextProvider));
        this.provideConnectivityManagagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideConnectivityManagagerFactory.create(this.contextProvider));
        this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
        this.provideWifiManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWifiManagerFactory.create(this.contextProvider));
        this.provideNetworkScoreManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNetworkScoreManagerFactory.create(this.contextProvider));
        this.providePowerExemptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerExemptionManagerFactory.create(this.contextProvider));
        this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
        this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
        this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
        this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
        this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
        this.provideINotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideINotificationManagerFactory.create(frameworkServicesModule));
        this.provideShortcutManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideShortcutManagerFactory.create(this.contextProvider));
        this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
        this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.create());
        this.provideCaptioningManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCaptioningManagerFactory.create(this.contextProvider));
        this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providesPluginManagerProvider));
        this.provideTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelecomManagerFactory.create(this.contextProvider));
        this.provideSharePreferencesProvider = FrameworkServicesModule_ProvideSharePreferencesFactory.create(frameworkServicesModule, this.contextProvider);
        this.provideIBatteryStatsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIBatteryStatsFactory.create());
        this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
        this.provideIsTestHarnessProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIsTestHarnessFactory.create());
        this.provideClipboardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideClipboardManagerFactory.create(this.contextProvider));
        this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
        Provider<Optional<FoldStateLoggingProvider>> provider10 = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, this.provideFoldStateProvider));
        this.providesFoldStateLoggingProvider = provider10;
        this.providesFoldStateLoggerProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggerFactory.create(unfoldTransitionModule, provider10));
        this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
        this.provideIPackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIPackageManagerFactory.create());
        this.provideSmartspaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSmartspaceManagerFactory.create(this.contextProvider));
        this.provideSafetyCenterManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSafetyCenterManagerFactory.create(this.contextProvider));
        this.provideOptionalTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalTelecomManagerFactory.create(this.contextProvider));
        this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
    }

    public final void initialize2(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
        this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
        this.provideOptionalVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalVibratorFactory.create(this.contextProvider));
        this.provideUiModeManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUiModeManagerFactory.create(this.contextProvider));
        this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
    }

    public WMComponent.Builder getWMComponentBuilder() {
        return new WMComponentBuilder();
    }

    public SysUIComponent.Builder getSysUIComponent() {
        return new SysUIComponentBuilder();
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

    public static final class Builder implements GlobalRootComponent.Builder {
        public Context context;

        public Builder() {
        }

        public Builder context(Context context2) {
            this.context = (Context) Preconditions.checkNotNull(context2);
            return this;
        }

        public GlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new DaggerGlobalRootComponent(new GlobalModule(), new AndroidInternalsModule(), new FrameworkServicesModule(), new UnfoldTransitionModule(), new UnfoldSharedModule(), this.context);
        }
    }

    public final class WMComponentBuilder implements WMComponent.Builder {
        public HandlerThread setShellMainThread;

        public WMComponentBuilder() {
        }

        public WMComponentBuilder setShellMainThread(HandlerThread handlerThread) {
            this.setShellMainThread = handlerThread;
            return this;
        }

        public WMComponent build() {
            return new WMComponentImpl(this.setShellMainThread);
        }
    }

    public final class WMComponentImpl implements WMComponent {
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
        public Provider<AppPairsController> provideAppPairsProvider;
        public Provider<Optional<AppPairs>> provideAppPairsProvider2;
        public Provider<Optional<BackAnimationController>> provideBackAnimationControllerProvider;
        public Provider<Optional<BackAnimation>> provideBackAnimationProvider;
        public Provider<BubbleController> provideBubbleControllerProvider;
        public Provider<Optional<Bubbles>> provideBubblesProvider;
        public Provider<CompatUIController> provideCompatUIControllerProvider;
        public Provider<Optional<CompatUI>> provideCompatUIProvider;
        public Provider<Optional<DisplayAreaHelper>> provideDisplayAreaHelperProvider;
        public Provider<DisplayController> provideDisplayControllerProvider;
        public Provider<DisplayImeController> provideDisplayImeControllerProvider;
        public Provider<DisplayInsetsController> provideDisplayInsetsControllerProvider;
        public Provider<DisplayLayout> provideDisplayLayoutProvider;
        public Provider<DragAndDropController> provideDragAndDropControllerProvider;
        public Provider<Optional<DragAndDrop>> provideDragAndDropProvider;
        public Provider<FloatingContentCoordinator> provideFloatingContentCoordinatorProvider;
        public Provider<FreeformTaskListener> provideFreeformTaskListenerProvider;
        public Provider<Optional<FreeformTaskListener>> provideFreeformTaskListenerProvider2;
        public Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        public Provider<FullscreenUnfoldController> provideFullscreenUnfoldControllerProvider;
        public Provider<Optional<FullscreenUnfoldController>> provideFullscreenUnfoldControllerProvider2;
        public Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        public Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        public Provider<IconProvider> provideIconProvider;
        public Provider<KidsModeTaskOrganizer> provideKidsModeTaskOrganizerProvider;
        public Provider<LegacySplitScreenController> provideLegacySplitScreenProvider;
        public Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider2;
        public Provider<OneHandedController> provideOneHandedControllerProvider;
        public Provider<Optional<OneHanded>> provideOneHandedProvider;
        public Provider<PipAnimationController> providePipAnimationControllerProvider;
        public Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        public Provider<PipBoundsState> providePipBoundsStateProvider;
        public Provider<PipMediaController> providePipMediaControllerProvider;
        public Provider<PipMotionHelper> providePipMotionHelperProvider;
        public Provider<PipParamsChangedForwarder> providePipParamsChangedForwarderProvider;
        public Provider<Optional<Pip>> providePipProvider;
        public Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        public Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        public Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        public Provider<PipTouchHandler> providePipTouchHandlerProvider;
        public Provider<PipTransitionController> providePipTransitionControllerProvider;
        public Provider<PipTransitionState> providePipTransitionStateProvider;
        public Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        public Provider<Optional<RecentTasksController>> provideRecentTasksControllerProvider;
        public Provider<Optional<RecentTasks>> provideRecentTasksProvider;
        public Provider<ShellTransitions> provideRemoteTransitionsProvider;
        public Provider<RootDisplayAreaOrganizer> provideRootDisplayAreaOrganizerProvider;
        public Provider<RootTaskDisplayAreaOrganizer> provideRootTaskDisplayAreaOrganizerProvider;
        public Provider<ShellExecutor> provideSharedBackgroundExecutorProvider;
        public Provider<Handler> provideSharedBackgroundHandlerProvider;
        public Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        public Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        public Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        public Provider<ShellInitImpl> provideShellInitImplProvider;
        public Provider<ShellInit> provideShellInitProvider;
        public Provider<ShellExecutor> provideShellMainExecutorProvider;
        public Provider<AnimationHandler> provideShellMainExecutorSfVsyncAnimationHandlerProvider;
        public Provider<Handler> provideShellMainHandlerProvider;
        public Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        public Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        public Provider<SplitScreenController> provideSplitScreenControllerProvider;
        public Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        public Provider<Optional<StageTaskUnfoldController>> provideStageTaskUnfoldControllerProvider;
        public Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        public Provider<StartingWindowController> provideStartingWindowControllerProvider;
        public Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
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
        public Provider<UnfoldBackgroundController> provideUnfoldBackgroundControllerProvider;
        public Provider<Optional<UnfoldTransitionHandler>> provideUnfoldTransitionHandlerProvider;
        public Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        public Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        public Provider<Optional<OneHandedController>> providesOneHandedControllerProvider;
        public Provider<PipBoundsAlgorithm> providesPipBoundsAlgorithmProvider;
        public Provider<PhonePipMenuController> providesPipPhoneMenuControllerProvider;
        public Provider<Optional<SplitScreenController>> providesSplitScreenControllerProvider;
        public Provider<HandlerThread> setShellMainThreadProvider;

        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        public WMComponentImpl(HandlerThread handlerThread) {
            initialize(handlerThread);
        }

        public final void initialize(HandlerThread handlerThread) {
            this.setShellMainThreadProvider = InstanceFactory.createNullable(handlerThread);
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.setShellMainThreadProvider, WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, DaggerGlobalRootComponent.this.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.dynamicOverrideOptionalOfDisplayImeControllerProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.provideDisplayInsetsControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayInsetsControllerFactory.create(DaggerGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayImeControllerFactory.create(this.dynamicOverrideOptionalOfDisplayImeControllerProvider, DaggerGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideIconProvider = DoubleCheck.provider(WMShellBaseModule_ProvideIconProviderFactory.create(DaggerGlobalRootComponent.this.contextProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, DaggerGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideIconProvider, this.provideShellMainExecutorProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideShellTaskOrganizerProvider = new DelegateFactory();
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, this.provideDisplayControllerProvider, DaggerGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideShellAnimationExecutorProvider));
            this.provideCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider, this.provideShellMainExecutorProvider, this.provideTransitionsProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideRecentTasksControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providerTaskStackListenerImplProvider, this.provideShellMainExecutorProvider));
            DelegateFactory.setDelegate(this.provideShellTaskOrganizerProvider, DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerGlobalRootComponent.this.contextProvider, this.provideCompatUIControllerProvider, this.provideRecentTasksControllerProvider)));
            this.provideKidsModeTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, DaggerGlobalRootComponent.this.contextProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideRecentTasksControllerProvider));
            this.provideFloatingContentCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFloatingContentCoordinatorFactory.create());
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.provideDisplayLayoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayLayoutFactory.create());
            Provider<OneHandedController> provider = DoubleCheck.provider(WMShellModule_ProvideOneHandedControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, DaggerGlobalRootComponent.this.provideWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayLayoutProvider, this.providerTaskStackListenerImplProvider, DaggerGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerGlobalRootComponent.this.provideInteractionJankMonitorProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.provideOneHandedControllerProvider = provider;
            this.dynamicOverrideOptionalOfOneHandedControllerProvider = PresentJdkOptionalInstanceProvider.of(provider);
            Provider<Handler> provider2 = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory.create());
            this.provideSharedBackgroundHandlerProvider = provider2;
            this.provideSharedBackgroundExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory.create(provider2));
            this.provideTaskViewTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewTransitionsFactory.create(this.provideTransitionsProvider));
            Provider<BubbleController> provider3 = DoubleCheck.provider(WMShellModule_ProvideBubbleControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideFloatingContentCoordinatorProvider, DaggerGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerGlobalRootComponent.this.provideWindowManagerProvider, this.provideWindowManagerShellWrapperProvider, DaggerGlobalRootComponent.this.provideUserManagerProvider, DaggerGlobalRootComponent.this.provideLauncherAppsProvider, this.providerTaskStackListenerImplProvider, DaggerGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideDisplayControllerProvider, this.dynamicOverrideOptionalOfOneHandedControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideSharedBackgroundExecutorProvider, this.provideTaskViewTransitionsProvider, this.provideSyncTransactionQueueProvider));
            this.provideBubbleControllerProvider = provider3;
            this.optionalOfBubbleControllerProvider = PresentJdkOptionalInstanceProvider.of(provider3);
            this.provideRootTaskDisplayAreaOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerGlobalRootComponent.this.contextProvider));
            this.optionalOfShellUnfoldProgressProvider = PresentJdkOptionalInstanceProvider.of(DaggerGlobalRootComponent.this.provideShellProgressProvider);
            this.provideUnfoldBackgroundControllerProvider = DoubleCheck.provider(WMShellModule_ProvideUnfoldBackgroundControllerFactory.create(this.provideRootTaskDisplayAreaOrganizerProvider, DaggerGlobalRootComponent.this.contextProvider));
            this.provideStageTaskUnfoldControllerProvider = WMShellModule_ProvideStageTaskUnfoldControllerFactory.create(this.optionalOfShellUnfoldProgressProvider, DaggerGlobalRootComponent.this.contextProvider, this.provideTransactionPoolProvider, this.provideUnfoldBackgroundControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider);
            Provider<SplitScreenController> provider4 = DoubleCheck.provider(WMShellModule_ProvideSplitScreenControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, DaggerGlobalRootComponent.this.contextProvider, this.provideRootTaskDisplayAreaOrganizerProvider, this.provideShellMainExecutorProvider, this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideTransitionsProvider, this.provideTransactionPoolProvider, this.provideIconProvider, this.provideRecentTasksControllerProvider, this.provideStageTaskUnfoldControllerProvider));
            this.provideSplitScreenControllerProvider = provider4;
            Provider<Optional<SplitScreenController>> r1 = PresentJdkOptionalInstanceProvider.of(provider4);
            this.dynamicOverrideOptionalOfSplitScreenControllerProvider = r1;
            this.providesSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesSplitScreenControllerFactory.create(r1, DaggerGlobalRootComponent.this.contextProvider));
            Provider<AppPairsController> provider5 = DoubleCheck.provider(WMShellModule_ProvideAppPairsFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider));
            this.provideAppPairsProvider = provider5;
            this.optionalOfAppPairsControllerProvider = PresentJdkOptionalInstanceProvider.of(provider5);
            this.providePipBoundsStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipBoundsStateFactory.create(DaggerGlobalRootComponent.this.contextProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, DaggerGlobalRootComponent.this.provideIWindowManagerProvider));
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(DaggerGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerGlobalRootComponent.this.providePackageManagerProvider));
            this.providesPipPhoneMenuControllerProvider = DoubleCheck.provider(WMShellModule_ProvidesPipPhoneMenuControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.provideSystemWindowsProvider, this.providesSplitScreenControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidePipSnapAlgorithmFactory.create());
            this.providesPipBoundsAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidesPipBoundsAlgorithmFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            this.providePipTransitionStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionStateFactory.create());
            Provider<PipSurfaceTransactionHelper> provider6 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider6;
            this.providePipAnimationControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipAnimationControllerFactory.create(provider6));
            this.providePipTransitionControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, this.providePipAnimationControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTransitionStateProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providesSplitScreenControllerProvider));
            this.providePipParamsChangedForwarderProvider = DoubleCheck.provider(WMShellModule_ProvidePipParamsChangedForwarderFactory.create());
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTaskOrganizerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideSyncTransactionQueueProvider, this.providePipTransitionStateProvider, this.providePipBoundsStateProvider, this.providesPipBoundsAlgorithmProvider, this.providesPipPhoneMenuControllerProvider, this.providePipAnimationControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providePipTransitionControllerProvider, this.providePipParamsChangedForwarderProvider, this.providesSplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipMotionHelperProvider = DoubleCheck.provider(WMShellModule_ProvidePipMotionHelperFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSnapAlgorithmProvider, this.providePipTransitionControllerProvider, this.provideFloatingContentCoordinatorProvider));
            Provider<PipTouchHandler> provider7 = DoubleCheck.provider(WMShellModule_ProvidePipTouchHandlerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providesPipPhoneMenuControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providePipMotionHelperProvider, this.provideFloatingContentCoordinatorProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider));
            this.providePipTouchHandlerProvider = provider7;
            this.optionalOfPipTouchHandlerProvider = PresentJdkOptionalInstanceProvider.of(provider7);
            this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            Provider<FullscreenUnfoldController> provider8 = DoubleCheck.provider(WMShellModule_ProvideFullscreenUnfoldControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.optionalOfShellUnfoldProgressProvider, this.provideUnfoldBackgroundControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider));
            this.provideFullscreenUnfoldControllerProvider = provider8;
            Provider<Optional<FullscreenUnfoldController>> r12 = PresentJdkOptionalInstanceProvider.of(provider8);
            this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider = r12;
            Provider<Optional<FullscreenUnfoldController>> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory.create(r12, this.optionalOfShellUnfoldProgressProvider));
            this.provideFullscreenUnfoldControllerProvider2 = provider9;
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider, this.provideSyncTransactionQueueProvider, provider9, this.provideRecentTasksControllerProvider));
            this.provideUnfoldTransitionHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory.create(this.optionalOfShellUnfoldProgressProvider, this.provideTransactionPoolProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider));
            Provider<FreeformTaskListener> provider10 = DoubleCheck.provider(WMShellModule_ProvideFreeformTaskListenerFactory.create(this.provideSyncTransactionQueueProvider));
            this.provideFreeformTaskListenerProvider = provider10;
            Provider<Optional<FreeformTaskListener>> r13 = PresentJdkOptionalInstanceProvider.of(provider10);
            this.dynamicOverrideOptionalOfFreeformTaskListenerProvider = r13;
            this.provideFreeformTaskListenerProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideFreeformTaskListenerFactory.create(r13, DaggerGlobalRootComponent.this.contextProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            Provider<Optional<StartingWindowTypeAlgorithm>> r14 = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider = r14;
            this.provideStartingWindowTypeAlgorithmProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory.create(r14));
            Provider<StartingWindowController> provider11 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider, this.provideIconProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider11;
            Provider<ShellInitImpl> provider12 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfBubbleControllerProvider, this.providesSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideFullscreenUnfoldControllerProvider2, this.provideUnfoldTransitionHandlerProvider, this.provideFreeformTaskListenerProvider2, this.provideRecentTasksControllerProvider, this.provideTransitionsProvider, provider11, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider12;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider12));
            this.provideShellMainExecutorSfVsyncAnimationHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory.create(this.provideShellMainExecutorProvider));
            Provider<LegacySplitScreenController> provider13 = DoubleCheck.provider(WMShellModule_ProvideLegacySplitScreenFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideSystemWindowsProvider, this.provideDisplayImeControllerProvider, this.provideTransactionPoolProvider, this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.providerTaskStackListenerImplProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider, this.provideShellMainExecutorSfVsyncAnimationHandlerProvider));
            this.provideLegacySplitScreenProvider = provider13;
            this.optionalOfLegacySplitScreenControllerProvider = PresentJdkOptionalInstanceProvider.of(provider13);
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(WMShellModule_ProvidePipAppOpsListenerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.providePipTouchHandlerProvider, this.provideShellMainExecutorProvider));
            this.providesOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesOneHandedControllerFactory.create(this.dynamicOverrideOptionalOfOneHandedControllerProvider));
            this.providePipProvider = DoubleCheck.provider(WMShellModule_ProvidePipFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.providePipAppOpsListenerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipTaskOrganizerProvider, this.providePipTouchHandlerProvider, this.providePipTransitionControllerProvider, this.provideWindowManagerShellWrapperProvider, this.providerTaskStackListenerImplProvider, this.providePipParamsChangedForwarderProvider, this.providesOneHandedControllerProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<HideDisplayCutoutController>> provider14 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider14;
            Provider<ShellCommandHandlerImpl> provider15 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.providesSplitScreenControllerProvider, this.providePipProvider, this.providesOneHandedControllerProvider, provider14, this.optionalOfAppPairsControllerProvider, this.provideRecentTasksControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider15;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider15));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.providesOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.providesSplitScreenControllerProvider));
            this.provideAppPairsProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.optionalOfBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewFactoryController> provider16 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, this.provideSyncTransactionQueueProvider, this.provideTaskViewTransitionsProvider));
            this.provideTaskViewFactoryControllerProvider = provider16;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider16));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<RootDisplayAreaOrganizer> provider17 = DoubleCheck.provider(WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider));
            this.provideRootDisplayAreaOrganizerProvider = provider17;
            this.provideDisplayAreaHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayAreaHelperFactory.create(this.provideShellMainExecutorProvider, provider17));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory create = WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider);
            this.provideTaskSurfaceHelperControllerProvider = create;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(create));
            this.provideRecentTasksProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksFactory.create(this.provideRecentTasksControllerProvider));
            this.provideCompatUIProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIFactory.create(this.provideCompatUIControllerProvider));
            this.provideDragAndDropProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropFactory.create(this.provideDragAndDropControllerProvider));
            Provider<Optional<BackAnimationController>> provider18 = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationControllerFactory.create(DaggerGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideSharedBackgroundHandlerProvider));
            this.provideBackAnimationControllerProvider = provider18;
            this.provideBackAnimationProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationFactory.create(provider18));
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
            return this.provideLegacySplitScreenProvider2.get();
        }

        public Optional<LegacySplitScreenController> getLegacySplitScreenController() {
            return Optional.of(this.provideLegacySplitScreenProvider.get());
        }

        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.get();
        }

        public Optional<SplitScreenController> getSplitScreenController() {
            return this.providesSplitScreenControllerProvider.get();
        }

        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider2.get();
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

    public final class SysUIComponentBuilder implements SysUIComponent.Builder {
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

        public SysUIComponentBuilder() {
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.pip.Pip>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setPip(java.util.Optional<com.android.wm.shell.pip.Pip> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setPip = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setPip(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setLegacySplitScreen(java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setLegacySplitScreen(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreenController>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setLegacySplitScreenController(java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreenController> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreenController = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setLegacySplitScreenController(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setSplitScreen(java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setSplitScreen(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setSplitScreenController(java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreenController = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setSplitScreenController(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.apppairs.AppPairs>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setAppPairs(java.util.Optional<com.android.wm.shell.apppairs.AppPairs> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setAppPairs = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setAppPairs(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.onehanded.OneHanded>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setOneHanded(java.util.Optional<com.android.wm.shell.onehanded.OneHanded> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setOneHanded = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setOneHanded(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.bubbles.Bubbles>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setBubbles(java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBubbles = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setBubbles(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.TaskViewFactory>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setTaskViewFactory(java.util.Optional<com.android.wm.shell.TaskViewFactory> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskViewFactory = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setTaskViewFactory(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setHideDisplayCutout(java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setHideDisplayCutout = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setHideDisplayCutout(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.ShellCommandHandler>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setShellCommandHandler(java.util.Optional<com.android.wm.shell.ShellCommandHandler> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setShellCommandHandler = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setShellCommandHandler(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        public SysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setStartingSurface(java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setStartingSurface = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setStartingSurface(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setDisplayAreaHelper(java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDisplayAreaHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setDisplayAreaHelper(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setTaskSurfaceHelper(java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskSurfaceHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setTaskSurfaceHelper(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.recents.RecentTasks>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setRecentTasks(java.util.Optional<com.android.wm.shell.recents.RecentTasks> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setRecentTasks = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setRecentTasks(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.compatui.CompatUI>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setCompatUI(java.util.Optional<com.android.wm.shell.compatui.CompatUI> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setCompatUI = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setCompatUI(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setDragAndDrop(java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDragAndDrop = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setDragAndDrop(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.back.BackAnimation>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setBackAnimation(java.util.Optional<com.android.wm.shell.back.BackAnimation> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBackAnimation = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setBackAnimation(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        public SysUIComponent build() {
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
            DaggerGlobalRootComponent daggerGlobalRootComponent = DaggerGlobalRootComponent.this;
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
            return new SysUIComponentImpl(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, this.setPip, this.setLegacySplitScreen, this.setLegacySplitScreenController, this.setSplitScreen, this.setSplitScreenController, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setDisplayAreaHelper, this.setTaskSurfaceHelper, this.setRecentTasks, this.setCompatUI, this.setDragAndDrop, this.setBackAnimation);
        }
    }

    public final class SysUIComponentImpl implements SysUIComponent {
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
        public Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
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
        public Provider<DeviceProvisionedController> bindDeviceProvisionedControllerProvider;
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
        public Provider<NotificationClicker.Builder> builderProvider3;
        public Provider<DelayedWakeLock.Builder> builderProvider4;
        public Provider<CustomTile.Builder> builderProvider5;
        public Provider<NightDisplayListenerModule.Builder> builderProvider6;
        public Provider<AutoAddTracker.Builder> builderProvider7;
        public Provider<TileServiceRequestController.Builder> builderProvider8;
        public Provider<CallbackHandler> callbackHandlerProvider;
        public Provider<CameraToggleTile> cameraToggleTileProvider;
        public Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        public Provider<CastControllerImpl> castControllerImplProvider;
        public Provider<CastTile> castTileProvider;
        public Provider<CellularTile> cellularTileProvider;
        public Provider<CentralSurfacesComponent.Factory> centralSurfacesComponentFactoryProvider;
        public Provider<CentralSurfacesImpl> centralSurfacesImplProvider;
        public Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        public Provider<ClipboardListener> clipboardListenerProvider;
        public Provider<ClipboardOverlayControllerFactory> clipboardOverlayControllerFactoryProvider;
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
        public Provider<CoordinatorsSubcomponent.Factory> coordinatorsSubcomponentFactoryProvider;
        public Provider<CreateUserActivity> createUserActivityProvider;
        public Provider<CustomIconCache> customIconCacheProvider;
        public Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        public Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        public Provider<DataSaverTile> dataSaverTileProvider;
        public Provider<DateFormatUtil> dateFormatUtilProvider;
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
        public Provider<Optional<LowLightClockController>> dynamicOverrideOptionalOfLowLightClockControllerProvider;
        public Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        public Provider<EnhancedEstimatesImpl> enhancedEstimatesImplProvider;
        public Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        public Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        public Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        public Provider<LightBarTransitionsController.Factory> factoryProvider;
        public Provider<SplitShadeOverScroller.Factory> factoryProvider10;
        public Provider<StatusBarIconController.TintedIconManager.Factory> factoryProvider11;
        public Provider<EdgeBackGestureHandler.Factory> factoryProvider2;
        public Provider<LockscreenShadeKeyguardTransitionController.Factory> factoryProvider3;
        public Provider<SplitShadeLockScreenOverScroller.Factory> factoryProvider4;
        public Provider<SingleShadeLockScreenOverScroller.Factory> factoryProvider5;
        public Provider<BrightnessSliderController.Factory> factoryProvider6;
        public Provider<KeyguardBouncer.Factory> factoryProvider7;
        public Provider<KeyguardMessageAreaController.Factory> factoryProvider8;
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
        public Provider<GroupCoalescerLogger> groupCoalescerLoggerProvider;
        public Provider<GroupCoalescer> groupCoalescerProvider;
        public Provider<HdmiCecSetMenuLanguageActivity> hdmiCecSetMenuLanguageActivityProvider;
        public Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;
        public Provider<HeadsUpController> headsUpControllerProvider;
        public Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
        public Provider<HeadsUpViewBinderLogger> headsUpViewBinderLoggerProvider;
        public Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        public Provider<HighPriorityProvider> highPriorityProvider;
        public Provider<HistoryTracker> historyTrackerProvider;
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
        public Provider<KeyguardBiometricLockoutLogger> keyguardBiometricLockoutLoggerProvider;
        public Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        public Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        public Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        public Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        public Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        public Provider<KeyguardIndicationController> keyguardIndicationControllerProvider;
        public Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        public Provider<KeyguardLiftController> keyguardLiftControllerProvider;
        public Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        public Provider keyguardNotificationVisibilityProviderImplProvider;
        public Provider<KeyguardQsUserSwitchComponent.Factory> keyguardQsUserSwitchComponentFactoryProvider;
        public Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        public Provider<KeyguardService> keyguardServiceProvider;
        public Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        public Provider<KeyguardStatusBarViewComponent.Factory> keyguardStatusBarViewComponentFactoryProvider;
        public Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        public Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        public Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        public Provider<KeyguardUserSwitcherComponent.Factory> keyguardUserSwitcherComponentFactoryProvider;
        public Provider<LSShadeTransitionLogger> lSShadeTransitionLoggerProvider;
        public Provider<LatencyTester> latencyTesterProvider;
        public Provider<LaunchConversationActivity> launchConversationActivityProvider;
        public Provider<LeakReporter> leakReporterProvider;
        public Provider<LegacyNotificationPresenterExtensions> legacyNotificationPresenterExtensionsProvider;
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
        public Provider<LockscreenWallpaper> lockscreenWallpaperProvider;
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
        public Provider<NodeSpecBuilderLogger> nodeSpecBuilderLoggerProvider;
        public Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
        public Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        public Provider<NotifBindPipeline> notifBindPipelineProvider;
        public Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        public Provider<NotifCollection> notifCollectionProvider;
        public Provider<NotifCoordinators> notifCoordinatorsProvider;
        public Provider<NotifInflaterImpl> notifInflaterImplProvider;
        public Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        public Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;
        public Provider notifPipelineChoreographerImplProvider;
        public Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
        public Provider<NotifPipelineInitializer> notifPipelineInitializerProvider;
        public Provider<NotifPipeline> notifPipelineProvider;
        public Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        public Provider<NotifUiAdjustmentProvider> notifUiAdjustmentProvider;
        public Provider<NotifViewBarn> notifViewBarnProvider;
        public Provider<NotificationChannels> notificationChannelsProvider;
        public Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        public Provider<NotificationClickerLogger> notificationClickerLoggerProvider;
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
        public Provider<NotificationRankingManager> notificationRankingManagerProvider;
        public Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        public Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        public Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        public Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        public Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        public Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        public Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        public Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;
        public Provider<NotificationStackSizeCalculator> notificationStackSizeCalculatorProvider;
        public Provider<NotificationVisibilityProviderImpl> notificationVisibilityProviderImplProvider;
        public Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        public Provider<NotificationsControllerImpl> notificationsControllerImplProvider;
        public Provider<NotificationsControllerStub> notificationsControllerStubProvider;
        public Provider<OneHandedModeTile> oneHandedModeTileProvider;
        public Provider<OngoingCallFlags> ongoingCallFlagsProvider;
        public Provider<OngoingCallLogger> ongoingCallLoggerProvider;
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
        public Provider<PhoneStatusBarPolicy> phoneStatusBarPolicyProvider;
        public Provider pointerCountClassifierProvider;
        public Provider postureDependentProximitySensorProvider;
        public Provider<PowerNotificationWarnings> powerNotificationWarningsProvider;
        public Provider<PowerUI> powerUIProvider;
        public Provider<PrivacyConfig> privacyConfigProvider;
        public Provider<PrivacyDialogController> privacyDialogControllerProvider;
        public Provider<PrivacyDotDecorProviderFactory> privacyDotDecorProviderFactoryProvider;
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
        public Provider<LogBuffer> provideCollapsedSbFragmentLogBufferProvider;
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
        public Provider<Optional<LowLightClockController>> provideLowLightClockControllerProvider;
        public Provider<LogBuffer> provideMediaBrowserBufferProvider;
        public Provider<LogBuffer> provideMediaCarouselControllerBufferProvider;
        public Provider<LogBuffer> provideMediaMuteAwaitLogBufferProvider;
        public Provider<LogBuffer> provideMediaTttReceiverLogBufferProvider;
        public Provider<LogBuffer> provideMediaTttSenderLogBufferProvider;
        public Provider<LogBuffer> provideMediaViewLogBufferProvider;
        public Provider<LogBuffer> provideNearbyMediaDevicesLogBufferProvider;
        public Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        public Provider<NotifGutsViewManager> provideNotifGutsViewManagerProvider;
        public Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        public Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        public Provider<NotifShadeEventSource> provideNotifShadeEventSourceProvider;
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
        public Provider<NotificationsController> provideNotificationsControllerProvider;
        public Provider<LogBuffer> provideNotificationsLogBufferProvider;
        public Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        public Provider<OngoingCallController> provideOngoingCallControllerProvider;
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
        public Provider<LogBuffer> provideSwipeAwayGestureLogBufferProvider;
        public Provider<Optional<SysUIUnfoldComponent>> provideSysUIUnfoldComponentProvider;
        public Provider<SysUiState> provideSysUiStateProvider;
        public Provider<String> provideThemePickerPackageProvider;
        public Provider<Handler> provideTimeTickHandlerProvider;
        public Provider<LogBuffer> provideToastLogBufferProvider;
        public Provider<UserTracker> provideUserTrackerProvider;
        public Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        public Provider<VolumeDialog> provideVolumeDialogProvider;
        public Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        public Provider<NodeController> providesAlertingHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        public Provider<MessageRouter> providesBackgroundMessageRouterProvider;
        public Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        public Provider<Boolean> providesControlsFeatureEnabledProvider;
        public Provider<String[]> providesDeviceStateRotationLockDefaultsProvider;
        public Provider<Float> providesDoubleTapTouchSlopProvider;
        public Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        public Provider<NodeController> providesIncomingHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        public Provider<MediaHost> providesKeyguardMediaHostProvider;
        public Provider<LeakDetector> providesLeakDetectorProvider;
        public Provider<MessageRouter> providesMainMessageRouterProvider;
        public Provider<Optional<MediaMuteAwaitConnectionCli>> providesMediaMuteAwaitConnectionCliProvider;
        public Provider<LogBuffer> providesMediaTimeoutListenerLogBufferProvider;
        public Provider<Optional<MediaTttChipControllerReceiver>> providesMediaTttChipControllerReceiverProvider;
        public Provider<Optional<MediaTttChipControllerSender>> providesMediaTttChipControllerSenderProvider;
        public Provider<Optional<MediaTttCommandLineHelper>> providesMediaTttCommandLineHelperProvider;
        public Provider<MediaTttLogger> providesMediaTttReceiverLoggerProvider;
        public Provider<MediaTttLogger> providesMediaTttSenderLoggerProvider;
        public Provider<Optional<NearbyMediaDevicesManager>> providesNearbyMediaDevicesManagerProvider;
        public Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        public Provider<NodeController> providesPeopleHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        public Provider<Executor> providesPluginExecutorProvider;
        public Provider<MediaHost> providesQSMediaHostProvider;
        public Provider<MediaHost> providesQuickQSMediaHostProvider;
        public Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        public Provider<NodeController> providesSilentHeaderNodeControllerProvider;
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
        public Provider<QsFrameTranslateImpl> qsFrameTranslateImplProvider;
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
        public Provider<ScreenDecorations> screenDecorationsProvider;
        public Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
        public Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
        public Provider<ScreenPinningRequest> screenPinningRequestProvider;
        public Provider<ScreenRecordTile> screenRecordTileProvider;
        public Provider<ScreenShotTile> screenShotTileProvider;
        public Provider<ScreenshotController> screenshotControllerProvider;
        public Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        public Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        public Provider<ScrimController> scrimControllerProvider;
        public Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        public Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        public Provider<SectionClassifier> sectionClassifierProvider;
        public Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        public Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
        public Provider secureSettingsImplProvider;
        public Provider<SecurityControllerImpl> securityControllerImplProvider;
        public Provider<SeekBarViewModel> seekBarViewModelProvider;
        public Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        public Provider<GarbageMonitor.Service> serviceProvider;
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
        public Provider<ShadeEventCoordinatorLogger> shadeEventCoordinatorLoggerProvider;
        public Provider<ShadeEventCoordinator> shadeEventCoordinatorProvider;
        public Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        public Provider<ShadeListBuilder> shadeListBuilderProvider;
        public Provider<ShadeTransitionController> shadeTransitionControllerProvider;
        public Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
        public Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;
        public ShadeViewManager_Factory shadeViewManagerProvider;
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
        public C0007SplitShadeOverScroller_Factory splitShadeOverScrollerProvider;
        public Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        public Provider<StatusBarHideIconsForBouncerManager> statusBarHideIconsForBouncerManagerProvider;
        public Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        public Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        public Provider<StatusBarLocationPublisher> statusBarLocationPublisherProvider;
        public Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        public Provider<StatusBarSignalPolicy> statusBarSignalPolicyProvider;
        public Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        public Provider<StatusBarTouchableRegionManager> statusBarTouchableRegionManagerProvider;
        public Provider<StatusBarUserInfoTracker> statusBarUserInfoTrackerProvider;
        public Provider<StatusBarUserSwitcherFeatureController> statusBarUserSwitcherFeatureControllerProvider;
        public Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        public Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
        public Provider<StorageNotification> storageNotificationProvider;
        public Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        public Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
        public Provider<SwipeStatusBarAwayGestureLogger> swipeStatusBarAwayGestureLoggerProvider;
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
        public Provider<TargetSdkResolver> targetSdkResolverProvider;
        public Provider<TaskbarDelegate> taskbarDelegateProvider;
        public Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        public Provider<ThemeOverlayApplier> themeOverlayApplierProvider;
        public Provider<ThemeOverlayController> themeOverlayControllerProvider;
        public final /* synthetic */ DaggerGlobalRootComponent this$0;
        public C0000TileLifecycleManager_Factory tileLifecycleManagerProvider;
        public Provider<TileServices> tileServicesProvider;
        public Provider<TimeoutHandler> timeoutHandlerProvider;
        public Provider<ToastFactory> toastFactoryProvider;
        public Provider<ToastLogger> toastLoggerProvider;
        public Provider<ToastUI> toastUIProvider;
        public Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        public Provider<TunerActivity> tunerActivityProvider;
        public Provider<TunerServiceImpl> tunerServiceImplProvider;
        public Provider<TvNotificationHandler> tvNotificationHandlerProvider;
        public Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
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
        public Provider<WiredChargingRippleController> wiredChargingRippleControllerProvider;
        public Provider<WorkLockActivity> workLockActivityProvider;
        public Provider<WorkModeTile> workModeTileProvider;
        public Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        public Provider zigZagClassifierProvider;

        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        public SysUIComponentImpl(DaggerGlobalRootComponent daggerGlobalRootComponent, LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.this$0 = daggerGlobalRootComponent;
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
            this.provideBatteryControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideBatteryControllerFactory.create(this.this$0.contextProvider, this.enhancedEstimatesImplProvider, this.this$0.providePowerManagerProvider, this.broadcastDispatcherProvider, this.provideDemoModeControllerProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider));
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
            this.bindDeviceProvisionedControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory.create(provider8));
            this.centralSurfacesImplProvider = new DelegateFactory();
            this.provideFlagManagerProvider = FlagsModule_ProvideFlagManagerFactory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider);
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            this.featureFlagsDebugProvider = DoubleCheck.provider(FeatureFlagsDebug_Factory.create(this.provideFlagManagerProvider, this.this$0.contextProvider, this.secureSettingsImplProvider, this.systemPropertiesHelperProvider, this.this$0.provideResourcesProvider, this.this$0.dumpManagerProvider, FlagsModule_ProvidesAllFlagsFactory.create(), this.commandRegistryProvider, this.this$0.provideIStatusBarServiceProvider));
            this.notifPipelineFlagsProvider = NotifPipelineFlags_Factory.create(this.this$0.contextProvider, this.featureFlagsDebugProvider);
            this.notificationListenerProvider = DoubleCheck.provider(NotificationListener_Factory.create(this.this$0.contextProvider, this.this$0.provideNotificationManagerProvider, this.bindSystemClockProvider, this.this$0.provideMainExecutorProvider, this.this$0.providesPluginManagerProvider));
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider9;
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(provider9);
            Provider<ExtensionControllerImpl> provider10 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(this.this$0.contextProvider, this.providesLeakDetectorProvider, this.this$0.providesPluginManagerProvider, this.tunerServiceImplProvider, this.configurationControllerImplProvider));
            this.extensionControllerImplProvider = provider10;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider10));
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.notificationGroupManagerLegacyProvider = delegateFactory2;
            Provider<GroupMembershipManager> provider11 = DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.notifPipelineFlagsProvider, delegateFactory2));
            this.provideGroupMembershipManagerProvider = provider11;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, provider11));
            Factory<Bubbles> create9 = InstanceFactory.create(optional8);
            this.setBubblesProvider = create9;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create9, this.this$0.dumpManagerProvider)));
            this.notifLiveDataStoreImplProvider = DoubleCheck.provider(NotifLiveDataStoreImpl_Factory.create(this.this$0.provideMainExecutorProvider));
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(this.this$0.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.notifPipelineFlagsProvider, this.notifCollectionLoggerProvider, this.this$0.provideMainHandlerProvider, this.logBufferEulogizerProvider, this.this$0.dumpManagerProvider));
            this.notifPipelineChoreographerImplProvider = DoubleCheck.provider(NotifPipelineChoreographerImpl_Factory.create(this.this$0.providesChoreographerProvider, this.this$0.provideMainDelayableExecutorProvider));
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(this.this$0.provideIStatusBarServiceProvider, this.this$0.provideMainExecutorProvider));
            DelegateFactory delegateFactory3 = new DelegateFactory();
            this.provideNotificationEntryManagerProvider = delegateFactory3;
            this.notificationInteractionTrackerProvider = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, delegateFactory3));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeListBuilderProvider = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.this$0.dumpManagerProvider, this.notifPipelineChoreographerImplProvider, this.notifPipelineFlagsProvider, this.notificationInteractionTrackerProvider, this.shadeListBuilderLoggerProvider, this.bindSystemClockProvider));
            Provider<RenderStageManager> provider12 = DoubleCheck.provider(RenderStageManager_Factory.create());
            this.renderStageManagerProvider = provider12;
            Provider<NotifPipeline> provider13 = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifPipelineFlagsProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, provider12));
            this.notifPipelineProvider = provider13;
            Provider<CommonNotifCollection> provider14 = DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.notifPipelineFlagsProvider, provider13, this.provideNotificationEntryManagerProvider));
            this.provideCommonNotifCollectionProvider = provider14;
            NotificationVisibilityProviderImpl_Factory create10 = NotificationVisibilityProviderImpl_Factory.create(this.notifLiveDataStoreImplProvider, provider14);
            this.notificationVisibilityProviderImplProvider = create10;
            this.provideNotificationVisibilityProvider = DoubleCheck.provider(create10);
            this.notificationLockscreenUserManagerImplProvider = DoubleCheck.provider(NotificationLockscreenUserManagerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideUserManagerProvider, this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.notificationClickNotifierProvider, this.this$0.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, this.this$0.provideMainHandlerProvider, this.bindDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.secureSettingsImplProvider, this.this$0.dumpManagerProvider));
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideSmartReplyControllerFactory.create(this.this$0.dumpManagerProvider, this.provideNotificationVisibilityProvider, this.this$0.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.remoteInputNotificationRebuilderProvider = DoubleCheck.provider(RemoteInputNotificationRebuilder_Factory.create(this.this$0.contextProvider));
            this.optionalOfCentralSurfacesProvider = new DelegateFactory();
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(this.this$0.provideIStatusBarServiceProvider));
            this.provideNotifInteractionLogBufferProvider = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
        }

        public final void initialize2(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory.create(this.this$0.contextProvider, this.notifPipelineFlagsProvider, this.notificationLockscreenUserManagerImplProvider, this.provideSmartReplyControllerProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.remoteInputNotificationRebuilderProvider, this.optionalOfCentralSurfacesProvider, this.statusBarStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider, this.this$0.dumpManagerProvider));
            NotifBindPipelineLogger_Factory create = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create2 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create2;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create2);
            Provider<BindEventManagerImpl> provider = DoubleCheck.provider(BindEventManagerImpl_Factory.create());
            this.bindEventManagerImplProvider = provider;
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(provider, this.notificationGroupManagerLegacyProvider, this.this$0.contextProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineFlagsProvider, this.this$0.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(this.this$0.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.mediaFeatureFlagProvider = MediaFeatureFlag_Factory.create(this.this$0.contextProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideActivityManagerWrapperFactory.create(sharedLibraryModule));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory.create(sharedLibraryModule));
            Provider<KeyguardDismissUtil> provider2 = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            this.keyguardDismissUtilProvider = provider2;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, provider2, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, this.this$0.contextProvider);
            this.provideActivityStarterProvider = new DelegateFactory();
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideNotificationHeadsUpLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationHeadsUpLogBufferProvider = provider3;
            this.headsUpManagerLoggerProvider = HeadsUpManagerLogger_Factory.create(provider3);
            this.keyguardBypassControllerProvider = DoubleCheck.provider(KeyguardBypassController_Factory.create(this.this$0.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.this$0.dumpManagerProvider));
            this.visualStabilityProvider = DoubleCheck.provider(VisualStabilityProvider_Factory.create());
            Provider<HeadsUpManagerPhone> provider4 = DoubleCheck.provider(ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory.create(this.this$0.contextProvider, this.headsUpManagerLoggerProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, this.visualStabilityProvider, this.configurationControllerImplProvider));
            this.provideHeadsUpManagerPhoneProvider = provider4;
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityStarterProvider, this.provideSmartReplyControllerProvider, provider4);
            SmartReplyStateInflaterImpl_Factory create3 = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, this.this$0.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create3;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create3));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create4 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create4;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create4));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() {
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder();
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(this.this$0.contextProvider);
            this.iconManagerProvider = DoubleCheck.provider(IconManager_Factory.create(this.provideCommonNotifCollectionProvider, this.this$0.provideLauncherAppsProvider, this.iconBuilderProvider));
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider, this.notifPipelineFlagsProvider));
            Provider<NotificationRowBinderImpl> provider5 = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider, this.notifPipelineFlagsProvider));
            this.notificationRowBinderImplProvider = provider5;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.notifPipelineFlagsProvider, provider5, this.provideNotificationRemoteInputManagerProvider, this.providesLeakDetectorProvider, this.this$0.provideIStatusBarServiceProvider, this.notifLiveDataStoreImplProvider, this.this$0.dumpManagerProvider)));
            this.debugModeFilterProvider = DoubleCheck.provider(DebugModeFilterProvider_Factory.create(this.commandRegistryProvider, this.this$0.dumpManagerProvider));
            this.alwaysOnDisplayPolicyProvider = DoubleCheck.provider(AlwaysOnDisplayPolicy_Factory.create(this.this$0.contextProvider));
            this.sysUIUnfoldComponentFactoryProvider = new Provider<SysUIUnfoldComponent.Factory>() {
                public SysUIUnfoldComponent.Factory get() {
                    return new SysUIUnfoldComponentFactory();
                }
            };
            this.provideSysUIUnfoldComponentProvider = DoubleCheck.provider(SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory.create(sysUIUnfoldModule, this.this$0.unfoldTransitionProgressProvider, this.this$0.provideNaturalRotationProgressProvider, this.this$0.provideStatusBarScopedTransitionProvider, this.sysUIUnfoldComponentFactoryProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(this.this$0.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.this$0.dumpManagerProvider));
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.dozeParametersProvider = new DelegateFactory();
            Provider<UnlockedScreenOffAnimationController> provider6 = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(this.this$0.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider, this.globalSettingsImplProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.providePowerManagerProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
            this.unlockedScreenOffAnimationControllerProvider = provider6;
            this.screenOffAnimationControllerProvider = DoubleCheck.provider(ScreenOffAnimationController_Factory.create(this.provideSysUIUnfoldComponentProvider, provider6, this.wakefulnessLifecycleProvider));
            Provider<DozeParameters> provider7 = this.dozeParametersProvider;
            Provider r2 = this.this$0.contextProvider;
            Provider<Handler> provider8 = this.provideBgHandlerProvider;
            Provider r4 = this.this$0.provideResourcesProvider;
            Provider r5 = this.this$0.provideAmbientDisplayConfigurationProvider;
            Provider<AlwaysOnDisplayPolicy> provider9 = this.alwaysOnDisplayPolicyProvider;
            Provider r7 = this.this$0.providePowerManagerProvider;
            Provider<BatteryController> provider10 = this.provideBatteryControllerProvider;
            Provider<TunerServiceImpl> provider11 = this.tunerServiceImplProvider;
            Provider r10 = this.this$0.dumpManagerProvider;
            Provider<FeatureFlagsDebug> provider12 = this.featureFlagsDebugProvider;
            Provider<ScreenOffAnimationController> provider13 = this.screenOffAnimationControllerProvider;
            Provider<Optional<SysUIUnfoldComponent>> provider14 = this.provideSysUIUnfoldComponentProvider;
            Provider<UnlockedScreenOffAnimationController> provider15 = this.unlockedScreenOffAnimationControllerProvider;
            Provider<DozeParameters> provider16 = provider7;
            DelegateFactory.setDelegate(provider16, DoubleCheck.provider(DozeParameters_Factory.create(r2, provider8, r4, r5, provider9, r7, provider10, provider11, r10, provider12, provider13, provider14, provider15, this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            this.authControllerProvider = new DelegateFactory();
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.this$0.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.screenOffAnimationControllerProvider, this.authControllerProvider));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            this.mediaControllerFactoryProvider = MediaControllerFactory_Factory.create(this.this$0.contextProvider);
            Provider<LogBuffer> provider17 = DoubleCheck.provider(LogModule_ProvidesMediaTimeoutListenerLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providesMediaTimeoutListenerLogBufferProvider = provider17;
            this.mediaTimeoutLoggerProvider = DoubleCheck.provider(MediaTimeoutLogger_Factory.create(provider17));
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(this.mediaControllerFactoryProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaTimeoutLoggerProvider, this.statusBarStateControllerImplProvider, this.bindSystemClockProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(this.this$0.contextProvider);
            Provider<LogBuffer> provider18 = DoubleCheck.provider(LogModule_ProvideMediaBrowserBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaBrowserBufferProvider = provider18;
            this.resumeMediaBrowserLoggerProvider = DoubleCheck.provider(ResumeMediaBrowserLogger_Factory.create(provider18));
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(this.this$0.contextProvider, this.mediaBrowserFactoryProvider, this.resumeMediaBrowserLoggerProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(this.this$0.contextProvider, this.this$0.provideMediaSessionManagerProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(SettingsLibraryModule_ProvideLocalBluetoothControllerFactory.create(this.this$0.contextProvider, this.provideBgHandlerProvider));
            this.localMediaManagerFactoryProvider = LocalMediaManagerFactory_Factory.create(this.this$0.contextProvider, this.provideLocalBluetoothControllerProvider);
            this.mediaFlagsProvider = DoubleCheck.provider(MediaFlags_Factory.create(this.featureFlagsDebugProvider));
            Provider<LogBuffer> provider19 = DoubleCheck.provider(LogModule_ProvideMediaMuteAwaitLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaMuteAwaitLogBufferProvider = provider19;
            this.mediaMuteAwaitLoggerProvider = DoubleCheck.provider(MediaMuteAwaitLogger_Factory.create(provider19));
            this.mediaMuteAwaitConnectionManagerFactoryProvider = DoubleCheck.provider(MediaMuteAwaitConnectionManagerFactory_Factory.create(this.mediaFlagsProvider, this.this$0.contextProvider, this.mediaMuteAwaitLoggerProvider, this.this$0.provideMainExecutorProvider));
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.mediaControllerFactoryProvider, this.localMediaManagerFactoryProvider, this.this$0.provideMediaRouter2ManagerProvider, this.mediaMuteAwaitConnectionManagerFactoryProvider, this.configurationControllerImplProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.this$0.dumpManagerProvider);
            this.builderProvider2 = WakeLock_Builder_Factory.create(this.this$0.contextProvider);
            this.broadcastSenderProvider = DoubleCheck.provider(BroadcastSender_Factory.create(this.this$0.contextProvider, this.builderProvider2, this.provideBackgroundExecutorProvider));
            this.mediaUiEventLoggerProvider = DoubleCheck.provider(MediaUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.notificationLockscreenUserManagerImplProvider, this.this$0.provideMainExecutorProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.this$0.dumpManagerProvider, this.broadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.provideActivityStarterProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider, this.mediaFlagsProvider, this.mediaUiEventLoggerProvider));
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationMediaManagerFactory.create(this.this$0.contextProvider, this.optionalOfCentralSurfacesProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.notifPipelineFlagsProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.this$0.dumpManagerProvider));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.bindDeviceProvisionedControllerProvider));
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideIndividualSensorPrivacyControllerFactory.create(this.this$0.provideSensorPrivacyManagerProvider));
            Provider<AppOpsControllerImpl> provider20 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgLooperProvider, this.this$0.dumpManagerProvider, this.this$0.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.broadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider20;
            Provider<ForegroundServiceController> provider21 = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider20, this.this$0.provideMainHandlerProvider));
            this.foregroundServiceControllerProvider = provider21;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.debugModeFilterProvider, this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider21, this.notificationLockscreenUserManagerImplProvider, this.mediaFeatureFlagProvider));
            this.notificationSectionsFeatureManagerProvider = DoubleCheck.provider(NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, this.this$0.contextProvider));
            Provider<HighPriorityProvider> provider22 = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.highPriorityProvider = provider22;
            this.notificationRankingManagerProvider = NotificationRankingManager_Factory.create(this.provideNotificationMediaManagerProvider, this.notificationGroupManagerLegacyProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationFilterProvider, this.notificationEntryManagerLoggerProvider, this.notificationSectionsFeatureManagerProvider, this.peopleNotificationIdentifierImplProvider, provider22, this.keyguardEnvironmentImplProvider);
            this.targetSdkResolverProvider = DoubleCheck.provider(TargetSdkResolver_Factory.create(this.this$0.contextProvider));
            this.groupCoalescerLoggerProvider = GroupCoalescerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.groupCoalescerProvider = GroupCoalescer_Factory.create(this.this$0.provideMainDelayableExecutorProvider, this.bindSystemClockProvider, this.groupCoalescerLoggerProvider);
            AnonymousClass3 r1 = new Provider<CoordinatorsSubcomponent.Factory>() {
                public CoordinatorsSubcomponent.Factory get() {
                    return new CoordinatorsSubcomponentFactory();
                }
            };
            this.coordinatorsSubcomponentFactoryProvider = r1;
            this.notifCoordinatorsProvider = DoubleCheck.provider(CoordinatorsModule_NotifCoordinatorsFactory.create(r1));
            this.notifInflaterImplProvider = DoubleCheck.provider(NotifInflaterImpl_Factory.create(this.notifInflationErrorManagerProvider));
            this.mediaContainerControllerProvider = DoubleCheck.provider(MediaContainerController_Factory.create(this.this$0.providerLayoutInflaterProvider));
            this.sectionHeaderVisibilityProvider = DoubleCheck.provider(SectionHeaderVisibilityProvider_Factory.create(this.this$0.contextProvider));
            this.nodeSpecBuilderLoggerProvider = NodeSpecBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeViewDifferLoggerProvider = ShadeViewDifferLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifViewBarnProvider = DoubleCheck.provider(NotifViewBarn_Factory.create());
            ShadeViewManager_Factory create5 = ShadeViewManager_Factory.create(this.this$0.contextProvider, this.mediaContainerControllerProvider, this.notificationSectionsFeatureManagerProvider, this.sectionHeaderVisibilityProvider, this.nodeSpecBuilderLoggerProvider, this.shadeViewDifferLoggerProvider, this.notifViewBarnProvider);
            this.shadeViewManagerProvider = create5;
            this.shadeViewManagerFactoryProvider = ShadeViewManagerFactory_Impl.create(create5);
            this.notifPipelineInitializerProvider = DoubleCheck.provider(NotifPipelineInitializer_Factory.create(this.notifPipelineProvider, this.groupCoalescerProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, this.renderStageManagerProvider, this.notifCoordinatorsProvider, this.notifInflaterImplProvider, this.this$0.dumpManagerProvider, this.shadeViewManagerFactoryProvider, this.notifPipelineFlagsProvider));
            this.notifBindPipelineInitializerProvider = NotifBindPipelineInitializer_Factory.create(this.notifBindPipelineProvider, this.rowContentBindStageProvider);
            this.notificationGroupAlertTransferHelperProvider = DoubleCheck.provider(NotificationGroupAlertTransferHelper_Factory.create(this.rowContentBindStageProvider, this.statusBarStateControllerImplProvider, this.notificationGroupManagerLegacyProvider));
            this.headsUpViewBinderLoggerProvider = HeadsUpViewBinderLogger_Factory.create(this.provideNotificationHeadsUpLogBufferProvider);
            this.headsUpViewBinderProvider = DoubleCheck.provider(HeadsUpViewBinder_Factory.create(this.this$0.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider, this.headsUpViewBinderLoggerProvider));
            this.notificationInterruptLoggerProvider = NotificationInterruptLogger_Factory.create(this.provideNotificationsLogBufferProvider, this.provideNotificationHeadsUpLogBufferProvider);
            this.keyguardNotificationVisibilityProviderImplProvider = DoubleCheck.provider(KeyguardNotificationVisibilityProviderImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider, this.statusBarStateControllerImplProvider, this.broadcastDispatcherProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(this.this$0.provideContentResolverProvider, this.this$0.providePowerManagerProvider, this.this$0.provideIDreamManagerProvider, this.this$0.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationInterruptLoggerProvider, this.this$0.provideMainHandlerProvider, this.notifPipelineFlagsProvider, this.keyguardNotificationVisibilityProviderImplProvider));
            this.provideVisualStabilityManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.provideNotificationEntryManagerProvider, this.visualStabilityProvider, this.this$0.provideMainHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, this.this$0.dumpManagerProvider));
        }

        public final void initialize3(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.headsUpControllerProvider = DoubleCheck.provider(HeadsUpController_Factory.create(this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, this.provideVisualStabilityManagerProvider, this.notificationListenerProvider));
            NotificationClickerLogger_Factory create = NotificationClickerLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.notificationClickerLoggerProvider = create;
            this.builderProvider3 = NotificationClicker_Builder_Factory.create(create);
            this.animatedImageNotificationManagerProvider = DoubleCheck.provider(AnimatedImageNotificationManager_Factory.create(this.provideCommonNotifCollectionProvider, this.bindEventManagerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider));
            Provider<PeopleSpaceWidgetManager> provider = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(this.this$0.contextProvider, this.this$0.provideLauncherAppsProvider, this.provideCommonNotifCollectionProvider, this.this$0.providePackageManagerProvider, this.setBubblesProvider, this.this$0.provideUserManagerProvider, this.this$0.provideNotificationManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider;
            this.notificationsControllerImplProvider = DoubleCheck.provider(NotificationsControllerImpl_Factory.create(this.centralSurfacesImplProvider, this.notifPipelineFlagsProvider, this.notificationListenerProvider, this.provideNotificationEntryManagerProvider, this.debugModeFilterProvider, this.notificationRankingManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.notifLiveDataStoreImplProvider, this.targetSdkResolverProvider, this.notifPipelineInitializerProvider, this.notifBindPipelineInitializerProvider, this.bindDeviceProvisionedControllerProvider, this.notificationRowBinderImplProvider, this.bindEventManagerImplProvider, this.remoteInputUriControllerProvider, this.notificationGroupManagerLegacyProvider, this.notificationGroupAlertTransferHelperProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpControllerProvider, this.headsUpViewBinderProvider, this.builderProvider3, this.animatedImageNotificationManagerProvider, provider, this.setBubblesProvider));
            this.notificationsControllerStubProvider = NotificationsControllerStub_Factory.create(this.notificationListenerProvider);
            this.provideNotificationsControllerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationsControllerFactory.create(this.this$0.contextProvider, this.notificationsControllerImplProvider, this.notificationsControllerStubProvider));
            AnonymousClass4 r1 = new Provider<FragmentService.FragmentCreator.Factory>() {
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory();
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            C0006LightBarTransitionsController_Factory create2 = C0006LightBarTransitionsController_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider);
            this.lightBarTransitionsControllerProvider = create2;
            this.factoryProvider = LightBarTransitionsController_Factory_Impl.create(create2);
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(this.this$0.contextProvider, this.factoryProvider, this.this$0.dumpManagerProvider));
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(this.this$0.contextProvider, this.bindDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.this$0.provideUiBackgroundExecutorProvider, this.this$0.dumpManagerProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(C0005LightBarController_Factory.create(this.this$0.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider, this.this$0.dumpManagerProvider));
            this.autoHideControllerProvider = DoubleCheck.provider(C0004AutoHideController_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideIWindowManagerProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarWindowModule_ProvidesStatusBarWindowViewFactory.create(this.this$0.providerLayoutInflaterProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(this.this$0.contextProvider, this.providesStatusBarWindowViewProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIWindowManagerProvider, this.statusBarContentInsetsProvider, this.this$0.provideResourcesProvider, this.this$0.unfoldTransitionProgressProvider));
            this.statusBarWindowStateControllerProvider = DoubleCheck.provider(StatusBarWindowStateController_Factory.create(this.this$0.provideDisplayIdProvider, this.provideCommandQueueProvider));
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider, this.configurationControllerImplProvider, this.tunerServiceImplProvider, this.this$0.dumpManagerProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(this.this$0.provideCarrierConfigManagerProvider, this.broadcastDispatcherProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(this.this$0.provideTelephonyManagerProvider, this.this$0.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider, this.this$0.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.wifiStatusTrackerFactoryProvider = WifiStatusTrackerFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideNetworkScoreManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider);
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.this$0.providerLayoutInflaterProvider, this.this$0.providesPluginManagerProvider, this.this$0.dumpManagerProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(this.this$0.contextProvider, this.appOpsControllerImplProvider, this.deviceConfigProxyProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider, this.this$0.providePackageManagerProvider, this.this$0.provideUiEventLoggerProvider, this.secureSettingsImplProvider));
            this.provideDialogLaunchAnimatorProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory.create(this.this$0.provideIDreamManagerProvider));
            Provider<DelayableExecutor> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBackgroundDelayableExecutorProvider = provider2;
            this.wifiStateWorkerProvider = DoubleCheck.provider(WifiStateWorker_Factory.create(this.broadcastDispatcherProvider, provider2, this.this$0.provideWifiManagerProvider));
            this.internetDialogControllerProvider = InternetDialogController_Factory.create(this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideActivityStarterProvider, this.provideAccessPointControllerImplProvider, this.this$0.provideSubcriptionManagerProvider, this.this$0.provideTelephonyManagerProvider, this.this$0.provideWifiManagerProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.keyguardStateControllerImplProvider, this.this$0.provideWindowManagerProvider, this.toastFactoryProvider, this.provideBgHandlerProvider, this.carrierConfigTrackerProvider, this.locationControllerImplProvider, this.provideDialogLaunchAnimatorProvider, this.wifiStateWorkerProvider);
            this.internetDialogFactoryProvider = DoubleCheck.provider(InternetDialogFactory_Factory.create(this.this$0.provideMainHandlerProvider, this.provideBackgroundExecutorProvider, this.internetDialogControllerProvider, this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.keyguardStateControllerImplProvider));
            this.networkControllerImplProvider = DoubleCheck.provider(NetworkControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.this$0.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.bindDeviceProvisionedControllerProvider, this.broadcastDispatcherProvider, this.this$0.provideConnectivityManagagerProvider, this.this$0.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, this.this$0.provideWifiManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.wifiStatusTrackerFactoryProvider, this.this$0.provideMainHandlerProvider, this.internetDialogFactoryProvider, this.featureFlagsDebugProvider, this.this$0.dumpManagerProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.this$0.dumpManagerProvider));
            this.statusBarSignalPolicyProvider = DoubleCheck.provider(StatusBarSignalPolicy_Factory.create(this.this$0.contextProvider, this.statusBarIconControllerImplProvider, this.carrierConfigTrackerProvider, this.networkControllerImplProvider, this.securityControllerImplProvider, this.tunerServiceImplProvider, this.featureFlagsDebugProvider));
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.screenOffAnimationControllerProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.notificationSectionsFeatureManagerProvider));
            this.provideLSShadeTransitionControllerBufferProvider = DoubleCheck.provider(LogModule_ProvideLSShadeTransitionControllerBufferFactory.create(this.logBufferFactoryProvider));
            Provider<LockscreenGestureLogger> provider3 = DoubleCheck.provider(LockscreenGestureLogger_Factory.create(this.this$0.provideMetricsLoggerProvider));
            this.lockscreenGestureLoggerProvider = provider3;
            this.lSShadeTransitionLoggerProvider = LSShadeTransitionLogger_Factory.create(this.provideLSShadeTransitionControllerBufferProvider, provider3, this.this$0.provideDisplayMetricsProvider);
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideMediaViewLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaViewLogBufferProvider = provider4;
            this.mediaViewLoggerProvider = DoubleCheck.provider(MediaViewLogger_Factory.create(provider4));
            this.mediaViewControllerProvider = MediaViewController_Factory.create(this.this$0.contextProvider, this.configurationControllerImplProvider, this.mediaHostStatesManagerProvider, this.mediaViewLoggerProvider);
            Provider<RepeatableExecutor> provider5 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory.create(this.provideBackgroundDelayableExecutorProvider));
            this.provideBackgroundRepeatableExecutorProvider = provider5;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider5);
            Provider<LogBuffer> provider6 = DoubleCheck.provider(LogModule_ProvideNearbyMediaDevicesLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNearbyMediaDevicesLogBufferProvider = provider6;
            Provider<NearbyMediaDevicesLogger> provider7 = DoubleCheck.provider(NearbyMediaDevicesLogger_Factory.create(provider6));
            this.nearbyMediaDevicesLoggerProvider = provider7;
            Provider<NearbyMediaDevicesManager> provider8 = DoubleCheck.provider(NearbyMediaDevicesManager_Factory.create(this.provideCommandQueueProvider, provider7));
            this.nearbyMediaDevicesManagerProvider = provider8;
            this.providesNearbyMediaDevicesManagerProvider = DoubleCheck.provider(MediaModule_ProvidesNearbyMediaDevicesManagerFactory.create(this.mediaFlagsProvider, provider8));
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(this.this$0.contextProvider, this.this$0.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.this$0.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.this$0.provideAudioManagerProvider, this.this$0.providePowerExemptionManagerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(this.this$0.contextProvider));
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(this.this$0.contextProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider, this.falsingManagerProxyProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider, this.keyguardStateControllerImplProvider, this.activityIntentHelperProvider, this.notificationLockscreenUserManagerImplProvider);
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideMediaCarouselControllerBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaCarouselControllerBufferProvider = provider9;
            this.mediaCarouselControllerLoggerProvider = DoubleCheck.provider(MediaCarouselControllerLogger_Factory.create(provider9));
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(this.this$0.contextProvider, this.mediaControlPanelProvider, this.visualStabilityProvider, this.mediaHostStatesManagerProvider, this.provideActivityStarterProvider, this.bindSystemClockProvider, this.this$0.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.this$0.dumpManagerProvider, this.mediaUiEventLoggerProvider, this.mediaCarouselControllerLoggerProvider)));
            this.dreamOverlayStateControllerProvider = DoubleCheck.provider(DreamOverlayStateController_Factory.create(this.this$0.provideMainExecutorProvider));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(this.this$0.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.configurationControllerImplProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider, this.dreamOverlayStateControllerProvider));
            Provider<MediaHost> provider10 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider10;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider10, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.this$0.contextProvider, this.configurationControllerImplProvider));
            Provider<LogBuffer> provider11 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider11;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider11));
            AnonymousClass5 r12 = new Provider<SectionHeaderControllerSubcomponent.Builder>() {
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder();
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = r12;
            Provider<SectionHeaderControllerSubcomponent> provider12 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory.create(r12));
            this.providesIncomingHeaderSubcomponentProvider = provider12;
            this.providesIncomingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory.create(provider12);
            Provider<SectionHeaderControllerSubcomponent> provider13 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider13;
            this.providesPeopleHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory.create(provider13);
            Provider<SectionHeaderControllerSubcomponent> provider14 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider14;
            this.providesAlertingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory.create(provider14);
            Provider<SectionHeaderControllerSubcomponent> provider15 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider15;
            NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory create3 = NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory.create(provider15);
            this.providesSilentHeaderControllerProvider = create3;
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.notifPipelineFlagsProvider, this.mediaContainerControllerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, create3);
            this.ambientStateProvider = DoubleCheck.provider(AmbientState_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.builderProvider4 = DelayedWakeLock_Builder_Factory.create(this.this$0.contextProvider);
            Provider<LogBuffer> provider16 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider16;
            this.dozeLoggerProvider = DozeLogger_Factory.create(provider16);
            Provider<DozeLog> provider17 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider, this.dozeLoggerProvider));
            this.dozeLogProvider = provider17;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider17, this.statusBarStateControllerImplProvider));
            this.scrimControllerProvider = new DelegateFactory();
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(this.this$0.contextProvider));
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.bootCompleteCacheImplProvider, this.statusBarStateControllerImplProvider));
            this.overviewProxyServiceProvider = new DelegateFactory();
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create(this.this$0.dumpManagerProvider));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(this.this$0.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(this.this$0.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(this.this$0.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider18 = DoubleCheck.provider(ReferenceSystemUIModule_ProvideRecentsFactory.create(this.this$0.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider18;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.of(provider18);
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(this.this$0.contextProvider, this.notificationShadeWindowControllerImplProvider, this.optionalOfCentralSurfacesProvider, this.optionalOfRecentsProvider));
            this.assistManagerProvider = new DelegateFactory();
            this.navBarHelperProvider = DoubleCheck.provider(NavBarHelper_Factory.create(this.this$0.contextProvider, this.this$0.provideAccessibilityManagerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.systemActionsProvider, this.overviewProxyServiceProvider, this.assistManagerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.provideUserTrackerProvider, this.this$0.dumpManagerProvider));
            this.factoryProvider2 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.this$0.providesPluginManagerProvider, this.this$0.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, this.this$0.provideViewConfigurationProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideIWindowManagerProvider, this.falsingManagerProxyProvider, this.this$0.provideLatencyTrackerProvider);
            this.taskbarDelegateProvider = DoubleCheck.provider(TaskbarDelegate_Factory.create(this.this$0.contextProvider, this.factoryProvider2, this.factoryProvider));
            this.navigationBarComponentFactoryProvider = new Provider<NavigationBarComponent.Factory>() {
                public NavigationBarComponent.Factory get() {
                    return new NavigationBarComponentFactory();
                }
            };
            this.setPipProvider = InstanceFactory.create(optional);
            this.setBackAnimationProvider = InstanceFactory.create(optional18);
        }

        public final void initialize4(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.navigationBarControllerProvider = DoubleCheck.provider(NavigationBarController_Factory.create(this.this$0.contextProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.provideSysUiStateProvider, this.provideCommandQueueProvider, this.this$0.provideMainHandlerProvider, this.configurationControllerImplProvider, this.navBarHelperProvider, this.taskbarDelegateProvider, this.navigationBarComponentFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.dumpManagerProvider, this.autoHideControllerProvider, this.lightBarControllerProvider, this.setPipProvider, this.setBackAnimationProvider));
            this.setLegacySplitScreenControllerProvider = InstanceFactory.create(optional3);
            this.setSplitScreenProvider = InstanceFactory.create(optional4);
            this.setSplitScreenControllerProvider = InstanceFactory.create(optional5);
            this.setOneHandedProvider = InstanceFactory.create(optional7);
            this.setRecentTasksProvider = InstanceFactory.create(optional15);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional12);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            this.keyguardUnlockAnimationControllerProvider = new DelegateFactory();
            DelegateFactory.setDelegate(this.overviewProxyServiceProvider, DoubleCheck.provider(OverviewProxyService_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.navigationBarControllerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setLegacySplitScreenControllerProvider, this.setSplitScreenProvider, this.setSplitScreenControllerProvider, this.setOneHandedProvider, this.setRecentTasksProvider, this.setBackAnimationProvider, this.setStartingSurfaceProvider, this.broadcastDispatcherProvider, this.setTransitionsProvider, this.this$0.screenLifecycleProvider, this.this$0.provideUiEventLoggerProvider, this.keyguardUnlockAnimationControllerProvider, this.provideAssistUtilsProvider, this.this$0.dumpManagerProvider)));
            this.assistLoggerProvider = DoubleCheck.provider(AssistLogger_Factory.create(this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider));
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(this.this$0.contextProvider, this.assistLoggerProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideMetricsLoggerProvider, this.assistManagerProvider));
            DelegateFactory.setDelegate(this.assistManagerProvider, DoubleCheck.provider(AssistManager_Factory.create(this.bindDeviceProvisionedControllerProvider, this.this$0.contextProvider, this.provideAssistUtilsProvider, this.provideCommandQueueProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.defaultUiControllerProvider, this.assistLoggerProvider, this.this$0.provideMainHandlerProvider)));
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.provideWindowManagerProvider, this.optionalOfCentralSurfacesProvider, this.assistManagerProvider));
            this.sessionTrackerProvider = DoubleCheck.provider(SessionTracker_Factory.create(this.this$0.contextProvider, this.this$0.provideIStatusBarServiceProvider, this.authControllerProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.scrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.this$0.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.this$0.provideMetricsLoggerProvider, this.this$0.dumpManagerProvider, this.this$0.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.this$0.screenLifecycleProvider, this.authControllerProvider, this.statusBarStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.sessionTrackerProvider, this.this$0.provideLatencyTrackerProvider, this.screenOffAnimationControllerProvider));
            DelegateFactory.setDelegate(this.keyguardUnlockAnimationControllerProvider, DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(this.this$0.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.featureFlagsDebugProvider, this.biometricUnlockControllerProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider)));
            DelegateFactory.setDelegate(this.scrimControllerProvider, DoubleCheck.provider(ScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, this.this$0.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider4, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.configurationControllerImplProvider, this.this$0.provideMainExecutorProvider, this.screenOffAnimationControllerProvider, this.panelExpansionStateManagerProvider, this.keyguardUnlockAnimationControllerProvider, this.statusBarKeyguardViewManagerProvider)));
            this.lockscreenShadeScrimTransitionControllerProvider = LockscreenShadeScrimTransitionController_Factory.create(this.scrimControllerProvider, this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider);
            C0001LockscreenShadeKeyguardTransitionController_Factory create = C0001LockscreenShadeKeyguardTransitionController_Factory.create(this.mediaHierarchyManagerProvider, this.this$0.contextProvider, this.configurationControllerImplProvider, this.this$0.dumpManagerProvider);
            this.lockscreenShadeKeyguardTransitionControllerProvider = create;
            this.factoryProvider3 = LockscreenShadeKeyguardTransitionController_Factory_Impl.create(create);
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(this.this$0.provideResourcesProvider, this.this$0.provideCrossWindowBlurListenersProvider, this.this$0.dumpManagerProvider));
            this.wallpaperControllerProvider = DoubleCheck.provider(WallpaperController_Factory.create(this.this$0.provideWallpaperManagerProvider));
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, this.this$0.providesChoreographerProvider, this.wallpaperControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.configurationControllerImplProvider));
            C0003SplitShadeLockScreenOverScroller_Factory create2 = C0003SplitShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.this$0.contextProvider, this.scrimControllerProvider, this.statusBarStateControllerImplProvider);
            this.splitShadeLockScreenOverScrollerProvider = create2;
            this.factoryProvider4 = SplitShadeLockScreenOverScroller_Factory_Impl.create(create2);
            C0002SingleShadeLockScreenOverScroller_Factory create3 = C0002SingleShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.this$0.contextProvider, this.statusBarStateControllerImplProvider);
            this.singleShadeLockScreenOverScrollerProvider = create3;
            this.factoryProvider5 = SingleShadeLockScreenOverScroller_Factory_Impl.create(create3);
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lSShadeTransitionLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.falsingCollectorImplProvider, this.ambientStateProvider, this.mediaHierarchyManagerProvider, this.lockscreenShadeScrimTransitionControllerProvider, this.factoryProvider3, this.notificationShadeDepthControllerProvider, this.this$0.contextProvider, this.factoryProvider4, this.factoryProvider5, this.wakefulnessLifecycleProvider, this.configurationControllerImplProvider, this.falsingManagerProxyProvider, this.this$0.dumpManagerProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(this.this$0.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider, this.this$0.dumpManagerProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.shadeEventCoordinatorLoggerProvider = ShadeEventCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeEventCoordinatorProvider = DoubleCheck.provider(ShadeEventCoordinator_Factory.create(this.this$0.provideMainExecutorProvider, this.shadeEventCoordinatorLoggerProvider));
            LegacyNotificationPresenterExtensions_Factory create4 = LegacyNotificationPresenterExtensions_Factory.create(this.provideNotificationEntryManagerProvider);
            this.legacyNotificationPresenterExtensionsProvider = create4;
            this.provideNotifShadeEventSourceProvider = DoubleCheck.provider(NotificationsModule_ProvideNotifShadeEventSourceFactory.create(this.notifPipelineFlagsProvider, this.shadeEventCoordinatorProvider, create4));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(this.this$0.contextProvider, this.this$0.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.deviceConfigProxyProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider, this.globalSettingsImplProvider));
            this.provideBubblesManagerProvider = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(this.this$0.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.shadeControllerImplProvider, this.configurationControllerImplProvider, this.this$0.provideIStatusBarServiceProvider, this.this$0.provideINotificationManagerProvider, this.provideNotificationVisibilityProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.notifPipelineFlagsProvider, this.this$0.dumpManagerProvider, this.this$0.provideMainExecutorProvider));
            this.provideDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.panelEventsEmitterProvider = DoubleCheck.provider(NotificationPanelViewController_PanelEventsEmitter_Factory.create());
            Provider<VisualStabilityCoordinator> provider = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideDelayableExecutorProvider, this.this$0.dumpManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.panelEventsEmitterProvider, this.statusBarStateControllerImplProvider, this.visualStabilityProvider, this.wakefulnessLifecycleProvider));
            this.visualStabilityCoordinatorProvider = provider;
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.notifPipelineFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifCollectionProvider, this.provideNotificationVisibilityProvider, provider, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(this.this$0.contextProvider, this.optionalOfCentralSurfacesProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider, this.this$0.provideAccessibilityManagerProvider, this.highPriorityProvider, this.this$0.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, this.this$0.provideLauncherAppsProvider, this.this$0.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, this.this$0.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider, this.this$0.dumpManagerProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.this$0.provideUiBackgroundExecutorProvider);
            this.provideNotificationPanelLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.notificationListenerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.notifPipelineFlagsProvider, this.notifLiveDataStoreImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, this.provideNotificationPanelLoggerProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.featureFlagsDebugProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider, this.notifPipelineFlagsProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.userSwitcherControllerProvider = new DelegateFactory();
            this.accessibilityFloatingMenuControllerProvider = DoubleCheck.provider(AccessibilityFloatingMenuController_Factory.create(this.this$0.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.lockscreenWallpaperProvider = DoubleCheck.provider(LockscreenWallpaper_Factory.create(this.this$0.provideWallpaperManagerProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider, this.provideNotificationMediaManagerProvider, this.this$0.provideMainHandlerProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(this.this$0.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.notificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.screenOffAnimationControllerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, this.this$0.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.provideSysUIUnfoldComponentProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.screenPinningRequestProvider = ScreenPinningRequest_Factory.create(this.this$0.contextProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.broadcastDispatcherProvider);
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(this.this$0.provideAudioManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.vibratorHelperProvider = DoubleCheck.provider(VibratorHelper_Factory.create(this.this$0.provideVibratorProvider, this.provideBackgroundExecutorProvider));
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), this.this$0.provideAudioManagerProvider, this.this$0.provideNotificationManagerProvider, this.vibratorHelperProvider, this.this$0.provideIAudioServiceProvider, this.this$0.provideAccessibilityManagerProvider, this.this$0.providePackageManagerProvider, this.wakefulnessLifecycleProvider, this.this$0.provideCaptioningManagerProvider, this.this$0.provideKeyguardManagerProvider, this.this$0.provideActivityManagerProvider));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(this.this$0.provideAccessibilityManagerProvider));
            this.provideVolumeDialogProvider = VolumeModule_ProvideVolumeDialogFactory.create(this.this$0.contextProvider, this.volumeDialogControllerImplProvider, this.accessibilityManagerWrapperProvider, this.bindDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.mediaOutputDialogFactoryProvider, this.provideActivityStarterProvider, this.this$0.provideInteractionJankMonitorProvider);
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(this.this$0.contextProvider, this.newKeyguardViewMediatorProvider, this.provideActivityStarterProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider, this.this$0.pluginDependencyProvider, this.extensionControllerImplProvider, this.tunerServiceImplProvider, this.provideVolumeDialogProvider));
            this.centralSurfacesComponentFactoryProvider = new Provider<CentralSurfacesComponent.Factory>() {
                public CentralSurfacesComponent.Factory get() {
                    return new CentralSurfacesComponentFactory();
                }
            };
            this.providesViewMediatorCallbackProvider = new DelegateFactory();
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideTimeTickHandlerFactory.create());
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(this.this$0.contextProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.provideBgHandlerProvider, this.this$0.dumpManagerProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(this.this$0.provideAlarmManagerProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider));
            RotationPolicyWrapperImpl_Factory create5 = RotationPolicyWrapperImpl_Factory.create(this.this$0.contextProvider, this.secureSettingsImplProvider);
            this.rotationPolicyWrapperImplProvider = create5;
            this.bindRotationPolicyWrapperProvider = DoubleCheck.provider(create5);
            this.provideAutoRotateSettingsManagerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory.create(this.this$0.contextProvider));
            this.deviceStateRotationLockSettingControllerProvider = DoubleCheck.provider(DeviceStateRotationLockSettingController_Factory.create(this.bindRotationPolicyWrapperProvider, this.this$0.provideDeviceStateManagerProvider, this.this$0.provideMainExecutorProvider, this.provideAutoRotateSettingsManagerProvider));
            StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory create6 = StatusBarPolicyModule_ProvidesDeviceStateRotationLockDefaultsFactory.create(this.this$0.provideResourcesProvider);
            this.providesDeviceStateRotationLockDefaultsProvider = create6;
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(this.bindRotationPolicyWrapperProvider, this.deviceStateRotationLockSettingControllerProvider, create6));
            this.provideDataSaverControllerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideDataSaverControllerFactory.create(this.networkControllerImplProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory.create(this.this$0.provideSensorPrivacyManagerProvider));
            this.recordingControllerProvider = DoubleCheck.provider(RecordingController_Factory.create(this.broadcastDispatcherProvider, this.provideUserTrackerProvider));
            this.dateFormatUtilProvider = DateFormatUtil_Factory.create(this.this$0.contextProvider);
            this.privacyConfigProvider = DoubleCheck.provider(PrivacyConfig_Factory.create(this.this$0.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.this$0.dumpManagerProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider2;
            PrivacyLogger_Factory create7 = PrivacyLogger_Factory.create(provider2);
            this.privacyLoggerProvider = create7;
            this.appOpsPrivacyItemMonitorProvider = DoubleCheck.provider(AppOpsPrivacyItemMonitor_Factory.create(this.appOpsControllerImplProvider, this.provideUserTrackerProvider, this.privacyConfigProvider, this.provideBackgroundDelayableExecutorProvider, create7));
            this.setOfPrivacyItemMonitorProvider = SetFactory.builder(1, 0).addProvider(this.appOpsPrivacyItemMonitorProvider).build();
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.this$0.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.privacyConfigProvider, this.setOfPrivacyItemMonitorProvider, this.privacyLoggerProvider, this.bindSystemClockProvider, this.this$0.dumpManagerProvider));
            this.phoneStatusBarPolicyProvider = PhoneStatusBarPolicy_Factory.create(this.statusBarIconControllerImplProvider, this.provideCommandQueueProvider, this.broadcastDispatcherProvider, this.this$0.provideUiBackgroundExecutorProvider, this.this$0.provideResourcesProvider, this.castControllerImplProvider, this.hotspotControllerImplProvider, this.bluetoothControllerImplProvider, this.nextAlarmControllerImplProvider, this.userInfoControllerImplProvider, this.rotationLockControllerImplProvider, this.provideDataSaverControllerProvider, this.zenModeControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.locationControllerImplProvider, this.provideSensorPrivacyControllerProvider, this.this$0.provideIActivityManagerProvider, this.this$0.provideAlarmManagerProvider, this.this$0.provideUserManagerProvider, this.this$0.provideDevicePolicyManagerProvider, this.recordingControllerProvider, this.this$0.provideTelecomManagerProvider, this.this$0.provideDisplayIdProvider, this.this$0.provideSharePreferencesProvider, this.dateFormatUtilProvider, this.ringerModeTrackerImplProvider, this.privacyItemControllerProvider, this.privacyLoggerProvider);
            this.keyguardIndicationControllerProvider = DoubleCheck.provider(KeyguardIndicationController_Factory.create(this.this$0.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.builderProvider2, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideIBatteryStatsProvider, this.this$0.provideUserManagerProvider, this.this$0.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.falsingManagerProxyProvider, this.this$0.provideLockPatternUtilsProvider, this.this$0.screenLifecycleProvider, this.this$0.provideIActivityManagerProvider, this.keyguardBypassControllerProvider));
            this.statusBarTouchableRegionManagerProvider = DoubleCheck.provider(StatusBarTouchableRegionManager_Factory.create(this.this$0.contextProvider, this.notificationShadeWindowControllerImplProvider, this.configurationControllerImplProvider, this.provideHeadsUpManagerPhoneProvider));
            this.factoryProvider6 = new DelegateFactory();
            this.ongoingCallLoggerProvider = DoubleCheck.provider(OngoingCallLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideSwipeAwayGestureLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideSwipeAwayGestureLogBufferProvider = provider3;
            this.swipeStatusBarAwayGestureLoggerProvider = SwipeStatusBarAwayGestureLogger_Factory.create(provider3);
            this.swipeStatusBarAwayGestureHandlerProvider = DoubleCheck.provider(SwipeStatusBarAwayGestureHandler_Factory.create(this.this$0.contextProvider, this.statusBarWindowControllerProvider, this.swipeStatusBarAwayGestureLoggerProvider));
            this.ongoingCallFlagsProvider = DoubleCheck.provider(OngoingCallFlags_Factory.create(this.featureFlagsDebugProvider));
            this.provideOngoingCallControllerProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory.create(this.this$0.contextProvider, this.provideCommonNotifCollectionProvider, this.bindSystemClockProvider, this.provideActivityStarterProvider, this.this$0.provideMainExecutorProvider, this.this$0.provideIActivityManagerProvider, this.ongoingCallLoggerProvider, this.this$0.dumpManagerProvider, this.statusBarWindowControllerProvider, this.swipeStatusBarAwayGestureHandlerProvider, this.statusBarStateControllerImplProvider, this.ongoingCallFlagsProvider));
            this.statusBarHideIconsForBouncerManagerProvider = DoubleCheck.provider(StatusBarHideIconsForBouncerManager_Factory.create(this.provideCommandQueueProvider, this.this$0.provideMainDelayableExecutorProvider, this.statusBarWindowStateControllerProvider, this.this$0.dumpManagerProvider));
            this.providesMainMessageRouterProvider = SysUIConcurrencyModule_ProvidesMainMessageRouterFactory.create(this.this$0.provideMainDelayableExecutorProvider);
            this.provideActivityLaunchAnimatorProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory.create());
            this.wiredChargingRippleControllerProvider = DoubleCheck.provider(WiredChargingRippleController_Factory.create(this.commandRegistryProvider, this.provideBatteryControllerProvider, this.configurationControllerImplProvider, this.featureFlagsDebugProvider, this.this$0.contextProvider, this.this$0.provideWindowManagerProvider, this.bindSystemClockProvider, this.this$0.provideUiEventLoggerProvider));
        }

        public final void initialize5(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            DelegateFactory.setDelegate(this.centralSurfacesImplProvider, DoubleCheck.provider(CentralSurfacesImpl_Factory.create(this.this$0.contextProvider, this.provideNotificationsControllerProvider, this.fragmentServiceProvider, this.lightBarControllerProvider, this.autoHideControllerProvider, this.statusBarWindowControllerProvider, this.statusBarWindowStateControllerProvider, this.keyguardUpdateMonitorProvider, this.statusBarSignalPolicyProvider, this.pulseExpansionHandlerProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.keyguardStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.dynamicPrivacyControllerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.broadcastDispatcherProvider, this.provideNotifShadeEventSourceProvider, this.provideNotificationEntryManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationLoggerProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationViewHierarchyManagerProvider, this.panelExpansionStateManagerProvider, this.newKeyguardViewMediatorProvider, this.this$0.provideDisplayMetricsProvider, this.this$0.provideMetricsLoggerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.provideNotificationMediaManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.provideNotificationRemoteInputManagerProvider, this.userSwitcherControllerProvider, this.networkControllerImplProvider, this.provideBatteryControllerProvider, this.sysuiColorExtractorProvider, this.this$0.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.provideBubblesManagerProvider, this.setBubblesProvider, this.provideVisualStabilityManagerProvider, this.bindDeviceProvisionedControllerProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.assistManagerProvider, this.configurationControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.scrimControllerProvider, this.lockscreenWallpaperProvider, this.lockscreenGestureLoggerProvider, this.biometricUnlockControllerProvider, this.dozeServiceHostProvider, this.this$0.providePowerManagerProvider, this.screenPinningRequestProvider, this.dozeScrimControllerProvider, this.volumeDialogComponentProvider, this.provideCommandQueueProvider, this.centralSurfacesComponentFactoryProvider, this.this$0.providesPluginManagerProvider, this.shadeControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.providesViewMediatorCallbackProvider, this.initControllerProvider, this.provideTimeTickHandlerProvider, this.this$0.pluginDependencyProvider, this.keyguardDismissUtilProvider, this.extensionControllerImplProvider, this.userInfoControllerImplProvider, this.phoneStatusBarPolicyProvider, this.keyguardIndicationControllerProvider, this.provideDemoModeControllerProvider, this.notificationShadeDepthControllerProvider, this.statusBarTouchableRegionManagerProvider, this.notificationIconAreaControllerProvider, this.factoryProvider6, this.screenOffAnimationControllerProvider, this.wallpaperControllerProvider, this.provideOngoingCallControllerProvider, this.statusBarHideIconsForBouncerManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.featureFlagsDebugProvider, this.keyguardUnlockAnimationControllerProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMainDelayableExecutorProvider, this.providesMainMessageRouterProvider, this.this$0.provideWallpaperManagerProvider, this.setStartingSurfaceProvider, this.provideActivityLaunchAnimatorProvider, this.notifPipelineFlagsProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.provideDeviceStateManagerProvider, this.dreamOverlayStateControllerProvider, this.wiredChargingRippleControllerProvider, this.this$0.provideIDreamManagerProvider, this.setLegacySplitScreenControllerProvider)));
            DelegateFactory.setDelegate(this.optionalOfCentralSurfacesProvider, PresentJdkOptionalInstanceProvider.of(this.centralSurfacesImplProvider));
            Provider<ActivityStarterDelegate> provider = DoubleCheck.provider(ActivityStarterDelegate_Factory.create(this.optionalOfCentralSurfacesProvider));
            this.activityStarterDelegateProvider = provider;
            DelegateFactory.setDelegate(this.provideActivityStarterProvider, PluginModule_ProvideActivityStarterFactory.create(provider, this.this$0.pluginDependencyProvider));
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider2;
            this.provideLongRunningExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider2));
            DelegateFactory.setDelegate(this.userSwitcherControllerProvider, DoubleCheck.provider(UserSwitcherController_Factory.create(this.this$0.contextProvider, this.this$0.provideIActivityManagerProvider, this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideMainHandlerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.this$0.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider, this.provideBackgroundExecutorProvider, this.provideLongRunningExecutorProvider, this.this$0.provideMainExecutorProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.provideLatencyTrackerProvider, this.this$0.dumpManagerProvider, this.provideDialogLaunchAnimatorProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() {
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory();
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(this.this$0.contextProvider, this.navigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.this$0.provideUiBackgroundExecutorProvider);
            this.screenOnCoordinatorProvider = DoubleCheck.provider(ScreenOnCoordinator_Factory.create(this.this$0.screenLifecycleProvider, this.provideSysUIUnfoldComponentProvider, this.this$0.provideExecutionProvider));
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(this.this$0.contextProvider, this.falsingCollectorImplProvider, this.this$0.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider, this.this$0.providePowerManagerProvider, this.this$0.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.this$0.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.screenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider, this.screenOnCoordinatorProvider, this.this$0.provideInteractionJankMonitorProvider, this.dreamOverlayStateControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideActivityLaunchAnimatorProvider)));
            DelegateFactory.setDelegate(this.providesViewMediatorCallbackProvider, KeyguardModule_ProvidesViewMediatorCallbackFactory.create(keyguardModule, this.newKeyguardViewMediatorProvider));
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(this.this$0.provideResourcesProvider, this.this$0.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() {
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory();
                }
            };
            this.factoryProvider7 = KeyguardBouncer_Factory_Factory.create(this.this$0.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.this$0.provideMainHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.factoryProvider8 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider);
            DelegateFactory.setDelegate(this.statusBarKeyguardViewManagerProvider, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(this.this$0.contextProvider, this.providesViewMediatorCallbackProvider, this.this$0.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dreamOverlayStateControllerProvider, this.navigationModeControllerProvider, this.dockManagerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider7, this.factoryProvider8, this.provideSysUIUnfoldComponentProvider, this.shadeControllerImplProvider, this.this$0.provideLatencyTrackerProvider)));
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, this.vibratorHelperProvider, this.keyguardUpdateMonitorProvider));
            this.udfpsShellProvider = DoubleCheck.provider(UdfpsShell_Factory.create(this.commandRegistryProvider));
            this.optionalOfUdfpsHbmProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.systemUIDialogManagerProvider = DoubleCheck.provider(SystemUIDialogManager_Factory.create(this.this$0.dumpManagerProvider, this.statusBarKeyguardViewManagerProvider));
            this.optionalOfAlternateUdfpsTouchProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.providesPluginExecutorProvider = DoubleCheck.provider(BiometricsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(this.this$0.contextProvider, this.this$0.provideExecutionProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesFingerprintManagerProvider, this.this$0.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.this$0.provideMainDelayableExecutorProvider, this.panelExpansionStateManagerProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.falsingManagerProxyProvider, this.this$0.providePowerManagerProvider, this.this$0.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.this$0.screenLifecycleProvider, this.vibratorHelperProvider, this.udfpsHapticsSimulatorProvider, this.udfpsShellProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.this$0.provideDisplayManagerProvider, this.this$0.provideMainHandlerProvider, this.configurationControllerImplProvider, this.bindSystemClockProvider, this.unlockedScreenOffAnimationControllerProvider, this.systemUIDialogManagerProvider, this.this$0.provideLatencyTrackerProvider, this.provideActivityLaunchAnimatorProvider, this.optionalOfAlternateUdfpsTouchProvider, this.providesPluginExecutorProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(this.this$0.contextProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesFingerprintManagerProvider, this.this$0.provideWindowManagerProvider, this.this$0.provideActivityTaskManagerProvider, this.overviewProxyServiceProvider, this.this$0.provideDisplayManagerProvider, this.this$0.provideMainDelayableExecutorProvider, this.this$0.provideMainHandlerProvider));
            Provider<AuthController> provider3 = this.authControllerProvider;
            Provider r3 = this.this$0.contextProvider;
            Provider r4 = this.this$0.provideExecutionProvider;
            Provider<CommandQueue> provider4 = this.provideCommandQueueProvider;
            Provider r6 = this.this$0.provideActivityTaskManagerProvider;
            Provider r7 = this.this$0.provideWindowManagerProvider;
            Provider r8 = this.this$0.providesFingerprintManagerProvider;
            Provider r9 = this.this$0.provideFaceManagerProvider;
            Provider<UdfpsController> provider5 = this.udfpsControllerProvider;
            Provider<SidefpsController> provider6 = this.sidefpsControllerProvider;
            Provider r12 = this.this$0.provideDisplayManagerProvider;
            Provider<WakefulnessLifecycle> provider7 = this.wakefulnessLifecycleProvider;
            Provider r14 = this.this$0.provideUserManagerProvider;
            Provider r15 = this.this$0.provideLockPatternUtilsProvider;
            Provider<StatusBarStateControllerImpl> provider8 = this.statusBarStateControllerImplProvider;
            DelegateFactory.setDelegate(provider3, DoubleCheck.provider(AuthController_Factory.create(r3, r4, provider4, r6, r7, r8, r9, provider5, provider6, r12, provider7, r14, r15, provider8, this.this$0.provideMainHandlerProvider, this.provideBackgroundDelayableExecutorProvider)));
            this.activeUnlockConfigProvider = DoubleCheck.provider(ActiveUnlockConfig_Factory.create(this.this$0.provideMainHandlerProvider, this.secureSettingsImplProvider, this.this$0.provideContentResolverProvider, this.this$0.dumpManagerProvider));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(this.this$0.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.this$0.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.this$0.provideInteractionJankMonitorProvider, this.this$0.provideLatencyTrackerProvider, this.activeUnlockConfigProvider)));
            DelegateFactory.setDelegate(this.keyguardStateControllerImplProvider, DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(this.this$0.contextProvider, this.keyguardUpdateMonitorProvider, this.this$0.provideLockPatternUtilsProvider, this.keyguardUnlockAnimationControllerProvider, this.this$0.dumpManagerProvider)));
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.this$0.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, this.historyTrackerProvider, this.keyguardStateControllerImplProvider, this.this$0.provideAccessibilityManagerProvider, this.this$0.provideIsTestHarnessProvider);
            DelegateFactory.setDelegate(this.falsingManagerProxyProvider, DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.this$0.providesPluginManagerProvider, this.this$0.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.this$0.dumpManagerProvider, this.brightLineFalsingManagerProvider)));
            DelegateFactory.setDelegate(this.factoryProvider6, BrightnessSliderController_Factory_Factory.create(this.falsingManagerProxyProvider));
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.broadcastDispatcherProvider, this.factoryProvider6, this.provideBgHandlerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbPermissionActivityProvider = UsbPermissionActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            this.usbConfirmActivityProvider = UsbConfirmActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            UserCreator_Factory create = UserCreator_Factory.create(this.this$0.contextProvider, this.this$0.provideUserManagerProvider);
            this.userCreatorProvider = create;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(create, UserModule_ProvideEditUserInfoControllerFactory.create(), this.this$0.provideIActivityManagerProvider);
            TvNotificationHandler_Factory create2 = TvNotificationHandler_Factory.create(this.this$0.contextProvider, this.notificationListenerProvider);
            this.tvNotificationHandlerProvider = create2;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(create2);
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.imageExporterProvider = ImageExporter_Factory.create(this.this$0.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(this.this$0.provideUiEventLoggerProvider, this.imageExporterProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.provideBubblesManagerProvider, this.this$0.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            Provider<HdmiCecSetMenuLanguageHelper> provider9 = DoubleCheck.provider(HdmiCecSetMenuLanguageHelper_Factory.create(this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider));
            this.hdmiCecSetMenuLanguageHelperProvider = provider9;
            this.hdmiCecSetMenuLanguageActivityProvider = HdmiCecSetMenuLanguageActivity_Factory.create(provider9);
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(this.this$0.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional9);
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(this.this$0.contextProvider, this.provideDelayableExecutorProvider, this.this$0.provideMainDelayableExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.keyguardStateControllerImplProvider, this.setTaskViewFactoryProvider, this.controlsMetricsLoggerImplProvider, this.vibratorHelperProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.this$0.provideMainHandlerProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            this.controlsUiControllerImplProvider = DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, this.this$0.contextProvider, this.this$0.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.this$0.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.this$0.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(this.this$0.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider, this.broadcastDispatcherProvider);
            this.userSwitcherActivityProvider = UserSwitcherActivity_Factory.create(this.userSwitcherControllerProvider, this.broadcastDispatcherProvider, this.this$0.providerLayoutInflaterProvider, this.falsingManagerProxyProvider, this.this$0.provideUserManagerProvider, this.provideUserTrackerProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, this.this$0.provideUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(23).put(TunerActivity.class, this.tunerActivityProvider).put(ForegroundServicesDialog.class, this.foregroundServicesDialogProvider).put(WorkLockActivity.class, this.workLockActivityProvider).put(BrightnessDialog.class, this.brightnessDialogProvider).put(UsbDebuggingActivity.class, this.usbDebuggingActivityProvider).put(UsbDebuggingSecondaryUserActivity.class, this.usbDebuggingSecondaryUserActivityProvider).put(UsbPermissionActivity.class, this.usbPermissionActivityProvider).put(UsbConfirmActivity.class, this.usbConfirmActivityProvider).put(CreateUserActivity.class, this.createUserActivityProvider).put(TvNotificationPanelActivity.class, this.tvNotificationPanelActivityProvider).put(PeopleSpaceActivity.class, this.peopleSpaceActivityProvider).put(LongScreenshotActivity.class, this.longScreenshotActivityProvider).put(LaunchConversationActivity.class, this.launchConversationActivityProvider).put(SensorUseStartedActivity.class, this.sensorUseStartedActivityProvider).put(TvUnblockSensorActivity.class, this.tvUnblockSensorActivityProvider).put(HdmiCecSetMenuLanguageActivity.class, this.hdmiCecSetMenuLanguageActivityProvider).put(ControlsProviderSelectorActivity.class, this.controlsProviderSelectorActivityProvider).put(ControlsFavoritingActivity.class, this.controlsFavoritingActivityProvider).put(ControlsEditingActivity.class, this.controlsEditingActivityProvider).put(ControlsRequestDialog.class, this.controlsRequestDialogProvider).put(ControlsActivity.class, this.controlsActivityProvider).put(UserSwitcherActivity.class, this.userSwitcherActivityProvider).put(WalletActivity.class, this.walletActivityProvider).build();
            AnonymousClass10 r1 = new Provider<DozeComponent.Builder>() {
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory();
                }
            };
            this.dozeComponentBuilderProvider = r1;
            this.dozeServiceProvider = DozeService_Factory.create(r1, this.this$0.providesPluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider10 = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.this$0.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider10;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider10, this.setTransitionsProvider);
            this.dreamOverlayComponentFactoryProvider = new Provider<DreamOverlayComponent.Factory>() {
                public DreamOverlayComponent.Factory get() {
                    return new DreamOverlayComponentFactory();
                }
            };
            this.dreamOverlayServiceProvider = DreamOverlayService_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.dreamOverlayComponentFactoryProvider, this.dreamOverlayStateControllerProvider, this.keyguardUpdateMonitorProvider, this.this$0.provideUiEventLoggerProvider);
            this.notificationListenerWithPluginsProvider = NotificationListenerWithPlugins_Factory.create(this.this$0.providesPluginManagerProvider);
            this.broadcastDispatcherStartableProvider = BroadcastDispatcherStartable_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider);
            this.clipboardOverlayControllerFactoryProvider = DoubleCheck.provider(ClipboardOverlayControllerFactory_Factory.create(this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.this$0.provideUiEventLoggerProvider));
            this.clipboardListenerProvider = DoubleCheck.provider(ClipboardListener_Factory.create(this.this$0.contextProvider, this.deviceConfigProxyProvider, this.clipboardOverlayControllerFactoryProvider, this.this$0.provideClipboardManagerProvider, this.this$0.provideUiEventLoggerProvider));
            this.providesBackgroundMessageRouterProvider = SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory.create(this.provideBackgroundDelayableExecutorProvider);
            this.provideLeakReportEmailProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(this.this$0.contextProvider, this.providesLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(this.this$0.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.providesBackgroundMessageRouterProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.this$0.dumpManagerProvider));
            this.serviceProvider = DoubleCheck.provider(GarbageMonitor_Service_Factory.create(this.this$0.contextProvider, this.garbageMonitorProvider));
            this.globalActionsComponentProvider = new DelegateFactory();
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(this.this$0.contextProvider, this.globalActionsComponentProvider, this.this$0.provideAudioManagerProvider, this.this$0.provideIDreamManagerProvider, this.this$0.provideDevicePolicyManagerProvider, this.this$0.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, this.vibratorHelperProvider, this.this$0.provideResourcesProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.this$0.provideUserManagerProvider, this.this$0.provideTrustManagerProvider, this.this$0.provideIActivityManagerProvider, this.this$0.provideTelecomManagerProvider, this.this$0.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, this.this$0.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, this.this$0.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.this$0.provideUiEventLoggerProvider, this.ringerModeTrackerImplProvider, this.this$0.provideMainHandlerProvider, this.this$0.providePackageManagerProvider, this.optionalOfCentralSurfacesProvider, this.keyguardUpdateMonitorProvider, this.provideDialogLaunchAnimatorProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.this$0.provideUiBackgroundExecutorProvider));
            this.keyboardUIProvider = DoubleCheck.provider(KeyboardUI_Factory.create(this.this$0.contextProvider));
            this.keyguardBiometricLockoutLoggerProvider = DoubleCheck.provider(KeyguardBiometricLockoutLogger_Factory.create(this.this$0.contextProvider, this.this$0.provideUiEventLoggerProvider, this.keyguardUpdateMonitorProvider, this.sessionTrackerProvider));
            this.latencyTesterProvider = DoubleCheck.provider(LatencyTester_Factory.create(this.this$0.contextProvider, this.biometricUnlockControllerProvider, this.broadcastDispatcherProvider, this.deviceConfigProxyProvider, this.this$0.provideMainDelayableExecutorProvider));
            this.powerNotificationWarningsProvider = DoubleCheck.provider(PowerNotificationWarnings_Factory.create(this.this$0.contextProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideBatteryControllerProvider, this.provideDialogLaunchAnimatorProvider, this.this$0.provideUiEventLoggerProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideCommandQueueProvider, this.optionalOfCentralSurfacesProvider, this.powerNotificationWarningsProvider, this.enhancedEstimatesImplProvider, this.this$0.providePowerManagerProvider));
            this.ringtonePlayerProvider = DoubleCheck.provider(RingtonePlayer_Factory.create(this.this$0.contextProvider));
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider));
            SystemEventChipAnimationController_Factory create3 = SystemEventChipAnimationController_Factory.create(this.this$0.contextProvider, this.statusBarWindowControllerProvider, this.statusBarContentInsetsProvider);
            this.systemEventChipAnimationControllerProvider = create3;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create3, this.statusBarWindowControllerProvider, this.this$0.dumpManagerProvider, this.bindSystemClockProvider, this.this$0.provideMainDelayableExecutorProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(this.this$0.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.privacyDotDecorProviderFactoryProvider = DoubleCheck.provider(PrivacyDotDecorProviderFactory_Factory.create(this.this$0.provideResourcesProvider));
        }

        public final void initialize6(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
            this.screenDecorationsProvider = DoubleCheck.provider(ScreenDecorations_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.tunerServiceImplProvider, this.provideUserTrackerProvider, this.privacyDotViewControllerProvider, ThreadFactoryImpl_Factory.create(), this.privacyDotDecorProviderFactoryProvider));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(this.this$0.contextProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider));
            this.storageNotificationProvider = DoubleCheck.provider(StorageNotification_Factory.create(this.this$0.contextProvider));
            this.provideLauncherPackageProvider = ThemeModule_ProvideLauncherPackageFactory.create(this.this$0.provideResourcesProvider);
            this.provideThemePickerPackageProvider = ThemeModule_ProvideThemePickerPackageFactory.create(this.this$0.provideResourcesProvider);
            this.themeOverlayApplierProvider = DoubleCheck.provider(ThemeOverlayApplier_Factory.create(this.this$0.provideOverlayManagerProvider, this.provideBackgroundExecutorProvider, this.provideLauncherPackageProvider, this.provideThemePickerPackageProvider, this.this$0.dumpManagerProvider));
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider, this.provideBgHandlerProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.themeOverlayApplierProvider, this.secureSettingsImplProvider, this.this$0.provideWallpaperManagerProvider, this.this$0.provideUserManagerProvider, this.bindDeviceProvisionedControllerProvider, this.provideUserTrackerProvider, this.this$0.dumpManagerProvider, this.featureFlagsDebugProvider, this.this$0.provideResourcesProvider, this.wakefulnessLifecycleProvider));
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider);
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(this.this$0.contextProvider, this.volumeDialogComponentProvider));
            this.modeSwitchesControllerProvider = DoubleCheck.provider(ModeSwitchesController_Factory.create(this.this$0.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(this.this$0.contextProvider, this.this$0.provideMainHandlerProvider, this.provideCommandQueueProvider, this.modeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional10);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional11);
            this.setCompatUIProvider = InstanceFactory.create(optional16);
            this.setDragAndDropProvider = InstanceFactory.create(optional17);
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(this.this$0.contextProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.setCompatUIProvider, this.setDragAndDropProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.this$0.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, this.userInfoControllerImplProvider, this.this$0.provideMainExecutorProvider));
            this.keyguardLiftControllerProvider = DoubleCheck.provider(KeyguardLiftController_Factory.create(this.this$0.contextProvider, this.statusBarStateControllerImplProvider, this.asyncSensorManagerProvider, this.keyguardUpdateMonitorProvider, this.this$0.dumpManagerProvider));
            this.mapOfClassOfAndProviderOfCoreStartableProvider = MapProviderFactory.builder(27).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(AuthController.class, this.authControllerProvider).put(ClipboardListener.class, this.clipboardListenerProvider).put(GarbageMonitor.class, this.serviceProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(KeyguardBiometricLockoutLogger.class, this.keyguardBiometricLockoutLoggerProvider).put(KeyguardViewMediator.class, this.newKeyguardViewMediatorProvider).put(LatencyTester.class, this.latencyTesterProvider).put(PowerUI.class, this.powerUIProvider).put(Recents.class, this.provideRecentsProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ScreenDecorations.class, this.screenDecorationsProvider).put(SessionTracker.class, this.sessionTrackerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(SystemActions.class, this.systemActionsProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(VolumeUI.class, this.volumeUIProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).put(KeyguardLiftController.class, this.keyguardLiftControllerProvider).put(CentralSurfaces.class, this.centralSurfacesImplProvider).build();
            this.dumpHandlerProvider = DumpHandler_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider, this.logBufferEulogizerProvider, this.mapOfClassOfAndProviderOfCoreStartableProvider, this.this$0.uncaughtExceptionPreHandlerManagerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.this$0.dumpManagerProvider, this.this$0.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, this.this$0.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, this.this$0.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(this.this$0.provideMainHandlerProvider, this.dumpHandlerProvider, this.broadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            this.recordingServiceProvider = RecordingService_Factory.create(this.recordingControllerProvider, this.provideLongRunningExecutorProvider, this.this$0.provideUiEventLoggerProvider, this.this$0.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
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
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(this.this$0.contextProvider, this.this$0.dumpManagerProvider));
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, this.this$0.contextProvider, this.provideBgHandlerProvider);
            this.reduceBrightColorsControllerProvider = DoubleCheck.provider(ReduceBrightColorsController_Factory.create(this.provideUserTrackerProvider, this.provideBgHandlerProvider, this.this$0.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(this.this$0.contextProvider, this.broadcastDispatcherProvider));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(this.this$0.contextProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.notifPipelineFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(this.this$0.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, this.this$0.provideMainExecutorProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(this.this$0.contextProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider));
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(this.this$0.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(this.this$0.contextProvider, this.this$0.providerLayoutInflaterProvider, this.this$0.providesPluginManagerProvider, this.sysuiColorExtractorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider));
            this.dependencyProvider = DoubleCheck.provider(Dependency_Factory.create(this.this$0.dumpManagerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.zenModeControllerImplProvider, this.hdmiCecSetMenuLanguageHelperProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.reduceBrightColorsControllerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.bindDeviceProvisionedControllerProvider, this.this$0.providesPluginManagerProvider, this.assistManagerProvider, this.securityControllerImplProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.configurationControllerImplProvider, this.statusBarIconControllerImplProvider, this.this$0.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.this$0.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.this$0.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.powerNotificationWarningsProvider, this.lightBarControllerProvider, this.this$0.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesImplProvider, this.vibratorHelperProvider, this.this$0.provideIStatusBarServiceProvider, this.this$0.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.notificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, this.this$0.provideSensorPrivacyManagerProvider, this.autoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.this$0.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.this$0.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.dockManagerImplProvider, this.this$0.provideINotificationManagerProvider, this.provideSysUiStateProvider, this.this$0.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider2, this.this$0.provideUiEventLoggerProvider, this.statusBarContentInsetsProvider, this.internetDialogFactoryProvider, this.featureFlagsDebugProvider, this.notificationSectionsManagerProvider, this.screenOffAnimationControllerProvider, this.ambientStateProvider, this.provideGroupMembershipManagerProvider, this.provideGroupExpansionManagerProvider, this.systemUIDialogManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.mediaTttFlagsProvider = DoubleCheck.provider(MediaTttFlags_Factory.create(this.featureFlagsDebugProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvideMediaTttSenderLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttSenderLogBufferProvider = provider2;
            this.providesMediaTttSenderLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttSenderLoggerFactory.create(provider2));
            this.viewUtilProvider = DoubleCheck.provider(ViewUtil_Factory.create());
            this.tapGestureDetectorProvider = DoubleCheck.provider(TapGestureDetector_Factory.create(this.this$0.contextProvider));
            this.mediaTttSenderUiEventLoggerProvider = DoubleCheck.provider(MediaTttSenderUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerSender> provider3 = DoubleCheck.provider(MediaTttChipControllerSender_Factory.create(this.provideCommandQueueProvider, this.this$0.contextProvider, this.providesMediaTttSenderLoggerProvider, this.this$0.provideWindowManagerProvider, this.viewUtilProvider, this.this$0.provideMainDelayableExecutorProvider, this.tapGestureDetectorProvider, this.this$0.providePowerManagerProvider, this.mediaTttSenderUiEventLoggerProvider));
            this.mediaTttChipControllerSenderProvider = provider3;
            this.providesMediaTttChipControllerSenderProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerSenderFactory.create(this.mediaTttFlagsProvider, provider3));
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideMediaTttReceiverLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttReceiverLogBufferProvider = provider4;
            this.providesMediaTttReceiverLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttReceiverLoggerFactory.create(provider4));
            this.mediaTttReceiverUiEventLoggerProvider = DoubleCheck.provider(MediaTttReceiverUiEventLogger_Factory.create(this.this$0.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerReceiver> provider5 = DoubleCheck.provider(MediaTttChipControllerReceiver_Factory.create(this.provideCommandQueueProvider, this.this$0.contextProvider, this.providesMediaTttReceiverLoggerProvider, this.this$0.provideWindowManagerProvider, this.viewUtilProvider, this.provideDelayableExecutorProvider, this.tapGestureDetectorProvider, this.this$0.providePowerManagerProvider, this.this$0.provideMainHandlerProvider, this.mediaTttReceiverUiEventLoggerProvider));
            this.mediaTttChipControllerReceiverProvider = provider5;
            this.providesMediaTttChipControllerReceiverProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerReceiverFactory.create(this.mediaTttFlagsProvider, provider5));
            Provider<MediaTttCommandLineHelper> provider6 = DoubleCheck.provider(MediaTttCommandLineHelper_Factory.create(this.commandRegistryProvider, this.this$0.contextProvider, this.this$0.provideMainExecutorProvider));
            this.mediaTttCommandLineHelperProvider = provider6;
            this.providesMediaTttCommandLineHelperProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttCommandLineHelperFactory.create(this.mediaTttFlagsProvider, provider6));
            Provider<MediaMuteAwaitConnectionCli> provider7 = DoubleCheck.provider(MediaMuteAwaitConnectionCli_Factory.create(this.commandRegistryProvider, this.this$0.contextProvider));
            this.mediaMuteAwaitConnectionCliProvider = provider7;
            this.providesMediaMuteAwaitConnectionCliProvider = DoubleCheck.provider(MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory.create(this.mediaFlagsProvider, provider7));
            this.notificationChannelsProvider = NotificationChannels_Factory.create(this.this$0.contextProvider);
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory.create());
            this.setDisplayAreaHelperProvider = InstanceFactory.create(optional13);
            this.sectionClassifierProvider = DoubleCheck.provider(SectionClassifier_Factory.create());
            this.providesAlertingHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory.create(this.providesAlertingHeaderSubcomponentProvider);
            this.providesSilentHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory.create(this.providesSilentHeaderSubcomponentProvider);
            this.providesIncomingHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory.create(this.providesIncomingHeaderSubcomponentProvider);
            this.provideNotifGutsViewManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotifGutsViewManagerFactory.create(this.provideNotificationGutsManagerProvider));
            this.providesPeopleHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory.create(this.providesPeopleHeaderSubcomponentProvider);
            this.notifUiAdjustmentProvider = DoubleCheck.provider(NotifUiAdjustmentProvider_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.sectionClassifierProvider));
            this.optionalOfBcSmartspaceDataPluginProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.lockscreenSmartspaceControllerProvider = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(this.this$0.contextProvider, this.featureFlagsDebugProvider, this.this$0.provideSmartspaceManagerProvider, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.this$0.provideContentResolverProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.this$0.provideExecutionProvider, this.this$0.provideMainExecutorProvider, this.this$0.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.qSTileHostProvider = new DelegateFactory();
            Provider<LogBuffer> provider8 = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideQuickSettingsLogBufferProvider = provider8;
            this.qSLoggerProvider = QSLogger_Factory.create(provider8);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(this.this$0.contextProvider);
            this.tileServicesProvider = TileServices_Factory.create(this.qSTileHostProvider, this.this$0.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.provideCommandQueueProvider);
            this.builderProvider5 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider, this.tileServicesProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider, this.internetDialogFactoryProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider);
        }

        public final void initialize7(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<LegacySplitScreenController> optional3, Optional<SplitScreen> optional4, Optional<SplitScreenController> optional5, Optional<AppPairs> optional6, Optional<OneHanded> optional7, Optional<Bubbles> optional8, Optional<TaskViewFactory> optional9, Optional<HideDisplayCutout> optional10, Optional<ShellCommandHandler> optional11, ShellTransitions shellTransitions, Optional<StartingSurface> optional12, Optional<DisplayAreaHelper> optional13, Optional<TaskSurfaceHelper> optional14, Optional<RecentTasks> optional15, Optional<CompatUI> optional16, Optional<DragAndDrop> optional17, Optional<BackAnimation> optional18) {
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
            this.builderProvider6 = NightDisplayListenerModule_Builder_Factory.create(this.this$0.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.this$0.provideColorDisplayManagerProvider, this.builderProvider6);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.configurationControllerImplProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider, this.keyguardStateControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            Provider<Boolean> provider = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(this.this$0.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider, this.reduceBrightColorsControllerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.this$0.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.providesControlsFeatureEnabledProvider = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(this.this$0.providePackageManagerProvider));
            this.optionalOfControlsTileResourceConfigurationProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
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
            RebootTile_Factory create = RebootTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.this$0.provideMainHandlerProvider, this.this$0.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.rebootTileProvider = create;
            this.qSFactoryImplProvider = DoubleCheck.provider(QSFactoryImpl_Factory.create(this.qSTileHostProvider, this.builderProvider5, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qRCodeScannerTileProvider, this.oneHandedModeTileProvider, this.colorCorrectionTileProvider, this.screenShotTileProvider, this.blackScreenTileProvider, this.settingTileProvider, this.soundTileProvider, create));
            this.builderProvider7 = DoubleCheck.provider(AutoAddTracker_Builder_Factory.create(this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.qSTileHostProvider, this.this$0.dumpManagerProvider, this.this$0.provideMainHandlerProvider, this.provideBackgroundExecutorProvider));
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(this.this$0.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.safetyControllerProvider = SafetyController_Factory.create(this.this$0.contextProvider, this.this$0.providePackageManagerProvider, this.this$0.provideSafetyCenterManagerProvider, this.provideBgHandlerProvider);
            this.provideAutoTileManagerProvider = QSModule_ProvideAutoTileManagerFactory.create(this.this$0.contextProvider, this.builderProvider7, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.reduceBrightColorsControllerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.safetyControllerProvider, this.isReduceBrightColorsAvailableProvider);
            this.builderProvider8 = DoubleCheck.provider(TileServiceRequestController_Builder_Factory.create(this.provideCommandQueueProvider, this.commandRegistryProvider));
            this.packageManagerAdapterProvider = PackageManagerAdapter_Factory.create(this.this$0.contextProvider);
            C0000TileLifecycleManager_Factory create2 = C0000TileLifecycleManager_Factory.create(this.this$0.provideMainHandlerProvider, this.this$0.contextProvider, this.tileServicesProvider, this.packageManagerAdapterProvider, this.broadcastDispatcherProvider);
            this.tileLifecycleManagerProvider = create2;
            this.factoryProvider9 = TileLifecycleManager_Factory_Impl.create(create2);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(this.this$0.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplProvider, this.this$0.provideMainHandlerProvider, this.provideBgLooperProvider, this.this$0.providesPluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.this$0.dumpManagerProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.qSLoggerProvider, this.this$0.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider, this.builderProvider8, this.factoryProvider9)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.provideQSFragmentDisableLogBufferProvider = DoubleCheck.provider(LogModule_ProvideQSFragmentDisableLogBufferFactory.create(this.logBufferFactoryProvider));
            this.disableFlagsLoggerProvider = DoubleCheck.provider(DisableFlagsLogger_Factory.create());
            this.notificationShelfComponentBuilderProvider = new Provider<NotificationShelfComponent.Builder>() {
                public NotificationShelfComponent.Builder get() {
                    return new NotificationShelfComponentBuilder();
                }
            };
            C0007SplitShadeOverScroller_Factory create3 = C0007SplitShadeOverScroller_Factory.create(this.configurationControllerImplProvider, this.this$0.dumpManagerProvider, this.this$0.contextProvider, this.scrimControllerProvider);
            this.splitShadeOverScrollerProvider = create3;
            this.factoryProvider10 = SplitShadeOverScroller_Factory_Impl.create(create3);
            this.shadeTransitionControllerProvider = DoubleCheck.provider(ShadeTransitionController_Factory.create(this.configurationControllerImplProvider, this.panelExpansionStateManagerProvider, this.this$0.contextProvider, this.factoryProvider10, NoOpOverScroller_Factory.create()));
            this.notificationStackSizeCalculatorProvider = DoubleCheck.provider(NotificationStackSizeCalculator_Factory.create(this.statusBarStateControllerImplProvider, this.lockscreenShadeTransitionControllerProvider, this.this$0.provideResourcesProvider));
            this.keyguardQsUserSwitchComponentFactoryProvider = new Provider<KeyguardQsUserSwitchComponent.Factory>() {
                public KeyguardQsUserSwitchComponent.Factory get() {
                    return new KeyguardQsUserSwitchComponentFactory();
                }
            };
            this.keyguardUserSwitcherComponentFactoryProvider = new Provider<KeyguardUserSwitcherComponent.Factory>() {
                public KeyguardUserSwitcherComponent.Factory get() {
                    return new KeyguardUserSwitcherComponentFactory();
                }
            };
            this.keyguardStatusBarViewComponentFactoryProvider = new Provider<KeyguardStatusBarViewComponent.Factory>() {
                public KeyguardStatusBarViewComponent.Factory get() {
                    return new KeyguardStatusBarViewComponentFactory();
                }
            };
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(this.this$0.providePermissionManagerProvider, this.this$0.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.this$0.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider, this.this$0.provideUiEventLoggerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory.create());
            this.qsFrameTranslateImplProvider = DoubleCheck.provider(QsFrameTranslateImpl_Factory.create(this.centralSurfacesImplProvider));
            Provider<Optional<LowLightClockController>> r1 = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfLowLightClockControllerProvider = r1;
            this.provideLowLightClockControllerProvider = DoubleCheck.provider(SystemUIModule_ProvideLowLightClockControllerFactory.create(r1));
            this.statusBarLocationPublisherProvider = DoubleCheck.provider(StatusBarLocationPublisher_Factory.create());
            this.provideCollapsedSbFragmentLogBufferProvider = DoubleCheck.provider(LogModule_ProvideCollapsedSbFragmentLogBufferFactory.create(this.logBufferFactoryProvider));
            this.statusBarUserInfoTrackerProvider = DoubleCheck.provider(StatusBarUserInfoTracker_Factory.create(this.userInfoControllerImplProvider, this.this$0.provideUserManagerProvider, this.this$0.dumpManagerProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider));
            this.statusBarUserSwitcherFeatureControllerProvider = DoubleCheck.provider(StatusBarUserSwitcherFeatureController_Factory.create(this.featureFlagsDebugProvider));
            UserDetailView_Adapter_Factory create4 = UserDetailView_Adapter_Factory.create(this.this$0.contextProvider, this.userSwitcherControllerProvider, this.this$0.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.adapterProvider = create4;
            this.userSwitchDialogControllerProvider = DoubleCheck.provider(UserSwitchDialogController_Factory.create(create4, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.provideDialogLaunchAnimatorProvider, this.this$0.provideUiEventLoggerProvider));
            this.provideProximityCheckProvider = SensorModule_ProvideProximityCheckFactory.create(this.provideProximitySensorProvider, this.this$0.provideMainDelayableExecutorProvider);
            this.dreamOverlayNotificationCountProvider = DoubleCheck.provider(DreamOverlayNotificationCountProvider_Factory.create(this.notificationListenerProvider, this.provideBackgroundExecutorProvider));
            this.fgsManagerControllerProvider = DoubleCheck.provider(FgsManagerController_Factory.create(this.this$0.contextProvider, this.this$0.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bindSystemClockProvider, this.this$0.provideIActivityManagerProvider, this.this$0.providePackageManagerProvider, this.provideUserTrackerProvider, this.deviceConfigProxyProvider, this.provideDialogLaunchAnimatorProvider, this.broadcastDispatcherProvider, this.this$0.dumpManagerProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(QSFlagsModule_IsPMLiteEnabledFactory.create(this.featureFlagsDebugProvider, this.globalSettingsImplProvider));
            this.factoryProvider11 = DoubleCheck.provider(StatusBarIconController_TintedIconManager_Factory_Factory.create(this.featureFlagsDebugProvider));
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
            return MapBuilder.newMapBuilder(27).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(AuthController.class, this.authControllerProvider).put(ClipboardListener.class, this.clipboardListenerProvider).put(GarbageMonitor.class, this.serviceProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(KeyguardBiometricLockoutLogger.class, this.keyguardBiometricLockoutLoggerProvider).put(KeyguardViewMediator.class, this.newKeyguardViewMediatorProvider).put(LatencyTester.class, this.latencyTesterProvider).put(PowerUI.class, this.powerUIProvider).put(Recents.class, this.provideRecentsProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ScreenDecorations.class, this.screenDecorationsProvider).put(SessionTracker.class, this.sessionTrackerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(SystemActions.class, this.systemActionsProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(VolumeUI.class, this.volumeUIProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).put(KeyguardLiftController.class, this.keyguardLiftControllerProvider).put(CentralSurfaces.class, this.centralSurfacesImplProvider).build();
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
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.falsingCollectorImplProvider);
                this.remoteInputViewSubcomponentFactoryProvider = new Provider<RemoteInputViewSubcomponent.Factory>() {
                    public RemoteInputViewSubcomponent.Factory get() {
                        return new RemoteInputViewSubcomponentFactory();
                    }
                };
                this.listContainerProvider = InstanceFactory.create(notificationListContainer);
                Factory create3 = InstanceFactory.create(notificationEntry2);
                this.notificationEntryProvider = create3;
                this.provideStatusBarNotificationProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory.create(create3);
                this.provideAppNameProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory.create(SysUIComponentImpl.this.this$0.contextProvider, this.provideStatusBarNotificationProvider);
                this.provideNotificationKeyProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory.create(this.provideStatusBarNotificationProvider);
                this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
                this.expandableNotificationRowDragControllerProvider = ExpandableNotificationRowDragController_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.shadeControllerImplProvider);
                this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.activatableNotificationViewControllerProvider, this.remoteInputViewSubcomponentFactoryProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, this.listContainerProvider, SysUIComponentImpl.this.provideNotificationMediaManagerProvider, SysUIComponentImpl.this.smartReplyConstantsProvider, SysUIComponentImpl.this.provideSmartReplyControllerProvider, SysUIComponentImpl.this.this$0.providesPluginManagerProvider, SysUIComponentImpl.this.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.provideGroupMembershipManagerProvider, SysUIComponentImpl.this.provideGroupExpansionManagerProvider, SysUIComponentImpl.this.rowContentBindStageProvider, SysUIComponentImpl.this.provideNotificationLoggerProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIComponentImpl.this.provideAllowNotificationLongPressProvider, SysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, SysUIComponentImpl.this.provideBubblesManagerProvider, this.expandableNotificationRowDragControllerProvider));
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
                    return new RemoteInputViewControllerImpl(this.view, ExpandableNotificationRowComponentImpl.this.notificationEntry, (RemoteInputQuickSettingsDisabler) SysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), this.remoteInputController, (ShortcutManager) SysUIComponentImpl.this.this$0.provideShortcutManagerProvider.get(), (UiEventLogger) SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider.get());
                }

                public RemoteInputViewController getController() {
                    return remoteInputViewControllerImpl();
                }
            }
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
                this.keyguardUnfoldTransitionProvider = DoubleCheck.provider(KeyguardUnfoldTransition_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.p2Provider));
                Factory create = InstanceFactory.create(scopedUnfoldTransitionProgressProvider);
                this.p3Provider = create;
                this.statusBarMoveFromCenterAnimationControllerProvider = DoubleCheck.provider(StatusBarMoveFromCenterAnimationController_Factory.create(create, SysUIComponentImpl.this.this$0.provideWindowManagerProvider));
                this.notificationPanelUnfoldAnimationControllerProvider = DoubleCheck.provider(NotificationPanelUnfoldAnimationController_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.p2Provider));
                this.foldAodAnimationControllerProvider = DoubleCheck.provider(FoldAodAnimationController_Factory.create(SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideDeviceStateManagerProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.globalSettingsImplProvider));
                Factory create2 = InstanceFactory.create(unfoldTransitionProgressProvider);
                this.p1Provider = create2;
                this.unfoldTransitionWallpaperControllerProvider = DoubleCheck.provider(UnfoldTransitionWallpaperController_Factory.create(create2, SysUIComponentImpl.this.wallpaperControllerProvider));
                this.unfoldLightRevealOverlayAnimationProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideDeviceStateManagerProvider, SysUIComponentImpl.this.this$0.provideDisplayManagerProvider, this.p1Provider, SysUIComponentImpl.this.setDisplayAreaHelperProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.this$0.provideUiBackgroundExecutorProvider, SysUIComponentImpl.this.this$0.provideIWindowManagerProvider));
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

        public final class CoordinatorsSubcomponentFactory implements CoordinatorsSubcomponent.Factory {
            public CoordinatorsSubcomponentFactory() {
            }

            public CoordinatorsSubcomponent create() {
                return new CoordinatorsSubcomponentImpl();
            }
        }

        public final class CoordinatorsSubcomponentImpl implements CoordinatorsSubcomponent {
            public Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
            public Provider<BubbleCoordinator> bubbleCoordinatorProvider;
            public Provider<ConversationCoordinator> conversationCoordinatorProvider;
            public Provider<DataStoreCoordinator> dataStoreCoordinatorProvider;
            public Provider<DebugModeCoordinator> debugModeCoordinatorProvider;
            public Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
            public Provider<GroupCountCoordinator> groupCountCoordinatorProvider;
            public Provider<GutsCoordinatorLogger> gutsCoordinatorLoggerProvider;
            public Provider<GutsCoordinator> gutsCoordinatorProvider;
            public Provider<HeadsUpCoordinatorLogger> headsUpCoordinatorLoggerProvider;
            public Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
            public Provider<HideLocallyDismissedNotifsCoordinator> hideLocallyDismissedNotifsCoordinatorProvider;
            public Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
            public Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
            public Provider<MediaCoordinator> mediaCoordinatorProvider;
            public Provider<NotifCoordinatorsImpl> notifCoordinatorsImplProvider;
            public Provider<PreparationCoordinatorLogger> preparationCoordinatorLoggerProvider;
            public Provider<PreparationCoordinator> preparationCoordinatorProvider;
            public Provider<RankingCoordinator> rankingCoordinatorProvider;
            public Provider<RemoteInputCoordinator> remoteInputCoordinatorProvider;
            public Provider<RowAppearanceCoordinator> rowAppearanceCoordinatorProvider;
            public Provider sensitiveContentCoordinatorImplProvider;
            public Provider<SharedCoordinatorLogger> sharedCoordinatorLoggerProvider;
            public Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
            public Provider<StackCoordinator> stackCoordinatorProvider;
            public Provider<ViewConfigCoordinator> viewConfigCoordinatorProvider;

            public CoordinatorsSubcomponentImpl() {
                initialize();
            }

            public final void initialize() {
                this.dataStoreCoordinatorProvider = DoubleCheck.provider(DataStoreCoordinator_Factory.create(SysUIComponentImpl.this.notifLiveDataStoreImplProvider));
                this.hideLocallyDismissedNotifsCoordinatorProvider = DoubleCheck.provider(HideLocallyDismissedNotifsCoordinator_Factory.create());
                this.sharedCoordinatorLoggerProvider = SharedCoordinatorLogger_Factory.create(SysUIComponentImpl.this.provideNotificationsLogBufferProvider);
                this.hideNotifsForOtherUsersCoordinatorProvider = DoubleCheck.provider(HideNotifsForOtherUsersCoordinator_Factory.create(SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, this.sharedCoordinatorLoggerProvider));
                this.keyguardCoordinatorProvider = DoubleCheck.provider(KeyguardCoordinator_Factory.create(SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.highPriorityProvider, SysUIComponentImpl.this.sectionHeaderVisibilityProvider, SysUIComponentImpl.this.keyguardNotificationVisibilityProviderImplProvider, this.sharedCoordinatorLoggerProvider));
                this.rankingCoordinatorProvider = DoubleCheck.provider(RankingCoordinator_Factory.create(SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.highPriorityProvider, SysUIComponentImpl.this.sectionClassifierProvider, SysUIComponentImpl.this.providesAlertingHeaderNodeControllerProvider, SysUIComponentImpl.this.providesSilentHeaderControllerProvider, SysUIComponentImpl.this.providesSilentHeaderNodeControllerProvider));
                this.appOpsCoordinatorProvider = DoubleCheck.provider(AppOpsCoordinator_Factory.create(SysUIComponentImpl.this.foregroundServiceControllerProvider, SysUIComponentImpl.this.appOpsControllerImplProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider));
                this.deviceProvisionedCoordinatorProvider = DoubleCheck.provider(DeviceProvisionedCoordinator_Factory.create(SysUIComponentImpl.this.bindDeviceProvisionedControllerProvider, SysUIComponentImpl.this.this$0.provideIPackageManagerProvider));
                this.bubbleCoordinatorProvider = DoubleCheck.provider(BubbleCoordinator_Factory.create(SysUIComponentImpl.this.provideBubblesManagerProvider, SysUIComponentImpl.this.setBubblesProvider, SysUIComponentImpl.this.notifCollectionProvider));
                HeadsUpCoordinatorLogger_Factory create = HeadsUpCoordinatorLogger_Factory.create(SysUIComponentImpl.this.provideNotificationHeadsUpLogBufferProvider);
                this.headsUpCoordinatorLoggerProvider = create;
                this.headsUpCoordinatorProvider = DoubleCheck.provider(HeadsUpCoordinator_Factory.create(create, SysUIComponentImpl.this.bindSystemClockProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.headsUpViewBinderProvider, SysUIComponentImpl.this.notificationInterruptStateProviderImplProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.providesIncomingHeaderNodeControllerProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider));
                this.gutsCoordinatorLoggerProvider = GutsCoordinatorLogger_Factory.create(SysUIComponentImpl.this.provideNotificationsLogBufferProvider);
                this.gutsCoordinatorProvider = DoubleCheck.provider(GutsCoordinator_Factory.create(SysUIComponentImpl.this.provideNotifGutsViewManagerProvider, this.gutsCoordinatorLoggerProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider));
                this.conversationCoordinatorProvider = DoubleCheck.provider(ConversationCoordinator_Factory.create(SysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, SysUIComponentImpl.this.iconManagerProvider, SysUIComponentImpl.this.providesPeopleHeaderNodeControllerProvider));
                this.debugModeCoordinatorProvider = DoubleCheck.provider(DebugModeCoordinator_Factory.create(SysUIComponentImpl.this.debugModeFilterProvider));
                this.groupCountCoordinatorProvider = DoubleCheck.provider(GroupCountCoordinator_Factory.create());
                this.mediaCoordinatorProvider = DoubleCheck.provider(MediaCoordinator_Factory.create(SysUIComponentImpl.this.mediaFeatureFlagProvider, SysUIComponentImpl.this.this$0.provideIStatusBarServiceProvider, SysUIComponentImpl.this.iconManagerProvider));
                PreparationCoordinatorLogger_Factory create2 = PreparationCoordinatorLogger_Factory.create(SysUIComponentImpl.this.provideNotificationsLogBufferProvider);
                this.preparationCoordinatorLoggerProvider = create2;
                this.preparationCoordinatorProvider = DoubleCheck.provider(PreparationCoordinator_Factory.create(create2, SysUIComponentImpl.this.notifInflaterImplProvider, SysUIComponentImpl.this.notifInflationErrorManagerProvider, SysUIComponentImpl.this.notifViewBarnProvider, SysUIComponentImpl.this.notifUiAdjustmentProvider, SysUIComponentImpl.this.this$0.provideIStatusBarServiceProvider, SysUIComponentImpl.this.bindEventManagerImplProvider));
                this.remoteInputCoordinatorProvider = DoubleCheck.provider(RemoteInputCoordinator_Factory.create(SysUIComponentImpl.this.this$0.dumpManagerProvider, SysUIComponentImpl.this.remoteInputNotificationRebuilderProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.provideSmartReplyControllerProvider));
                this.rowAppearanceCoordinatorProvider = DoubleCheck.provider(RowAppearanceCoordinator_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.assistantFeedbackControllerProvider, SysUIComponentImpl.this.sectionClassifierProvider));
                this.stackCoordinatorProvider = DoubleCheck.provider(StackCoordinator_Factory.create(SysUIComponentImpl.this.notificationIconAreaControllerProvider));
                this.smartspaceDedupingCoordinatorProvider = DoubleCheck.provider(SmartspaceDedupingCoordinator_Factory.create(SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.lockscreenSmartspaceControllerProvider, SysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.notifPipelineProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, SysUIComponentImpl.this.bindSystemClockProvider));
                this.viewConfigCoordinatorProvider = DoubleCheck.provider(ViewConfigCoordinator_Factory.create(SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider));
                this.sensitiveContentCoordinatorImplProvider = DoubleCheck.provider(SensitiveContentCoordinatorImpl_Factory.create(SysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider));
                this.notifCoordinatorsImplProvider = DoubleCheck.provider(NotifCoordinatorsImpl_Factory.create(SysUIComponentImpl.this.this$0.dumpManagerProvider, SysUIComponentImpl.this.notifPipelineFlagsProvider, this.dataStoreCoordinatorProvider, this.hideLocallyDismissedNotifsCoordinatorProvider, this.hideNotifsForOtherUsersCoordinatorProvider, this.keyguardCoordinatorProvider, this.rankingCoordinatorProvider, this.appOpsCoordinatorProvider, this.deviceProvisionedCoordinatorProvider, this.bubbleCoordinatorProvider, this.headsUpCoordinatorProvider, this.gutsCoordinatorProvider, this.conversationCoordinatorProvider, this.debugModeCoordinatorProvider, this.groupCountCoordinatorProvider, this.mediaCoordinatorProvider, this.preparationCoordinatorProvider, this.remoteInputCoordinatorProvider, this.rowAppearanceCoordinatorProvider, this.stackCoordinatorProvider, SysUIComponentImpl.this.shadeEventCoordinatorProvider, this.smartspaceDedupingCoordinatorProvider, this.viewConfigCoordinatorProvider, SysUIComponentImpl.this.visualStabilityCoordinatorProvider, this.sensitiveContentCoordinatorImplProvider));
            }

            public NotifCoordinators getNotifCoordinators() {
                return this.notifCoordinatorsImplProvider.get();
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
                return new QSFragmentDisableFlagsLogger((LogBuffer) SysUIComponentImpl.this.provideQSFragmentDisableLogBufferProvider.get(), (DisableFlagsLogger) SysUIComponentImpl.this.disableFlagsLoggerProvider.get());
            }

            public QSFragment createQSFragment() {
                return new QSFragment((RemoteInputQuickSettingsDisabler) SysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), (QSTileHost) SysUIComponentImpl.this.qSTileHostProvider.get(), (StatusBarStateController) SysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (CommandQueue) SysUIComponentImpl.this.provideCommandQueueProvider.get(), (MediaHost) SysUIComponentImpl.this.providesQSMediaHostProvider.get(), (MediaHost) SysUIComponentImpl.this.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) SysUIComponentImpl.this.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(), qSFragmentDisableFlagsLogger(), (FalsingManager) SysUIComponentImpl.this.falsingManagerProxyProvider.get(), (DumpManager) SysUIComponentImpl.this.this$0.dumpManagerProvider.get());
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
                this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, this.headerTextProvider, SysUIComponentImpl.this.provideActivityStarterProvider, this.clickIntentActionProvider));
            }

            public NodeController getNodeController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
            }

            public SectionHeaderController getHeaderController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
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
                this.factoryProvider = LightBarController_Factory_Factory.create(SysUIComponentImpl.this.darkIconDispatcherImplProvider, SysUIComponentImpl.this.provideBatteryControllerProvider, SysUIComponentImpl.this.navigationModeControllerProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider);
                this.factoryProvider2 = AutoHideController_Factory_Factory.create(SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideIWindowManagerProvider);
                this.deadZoneProvider = DeadZone_Factory.create(this.provideNavigationBarviewProvider);
                this.navigationBarTransitionsProvider = DoubleCheck.provider(NavigationBarTransitions_Factory.create(this.provideNavigationBarviewProvider, SysUIComponentImpl.this.this$0.provideIWindowManagerProvider, SysUIComponentImpl.this.factoryProvider));
                this.provideEdgeBackGestureHandlerProvider = DoubleCheck.provider(NavigationBarModule_ProvideEdgeBackGestureHandlerFactory.create(SysUIComponentImpl.this.factoryProvider2, this.contextProvider));
                this.navigationBarProvider = DoubleCheck.provider(NavigationBar_Factory.create(this.provideNavigationBarviewProvider, this.provideNavigationBarFrameProvider, this.savedStateProvider, this.contextProvider, this.provideWindowManagerProvider, SysUIComponentImpl.this.assistManagerProvider, SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, SysUIComponentImpl.this.bindDeviceProvisionedControllerProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.overviewProxyServiceProvider, SysUIComponentImpl.this.navigationModeControllerProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.provideSysUiStateProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.provideCommandQueueProvider, SysUIComponentImpl.this.setPipProvider, SysUIComponentImpl.this.optionalOfRecentsProvider, SysUIComponentImpl.this.optionalOfCentralSurfacesProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.notificationShadeDepthControllerProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.navBarHelperProvider, SysUIComponentImpl.this.lightBarControllerProvider, this.factoryProvider, SysUIComponentImpl.this.autoHideControllerProvider, this.factoryProvider2, SysUIComponentImpl.this.this$0.provideOptionalTelecomManagerProvider, SysUIComponentImpl.this.this$0.provideInputMethodManagerProvider, this.deadZoneProvider, SysUIComponentImpl.this.deviceConfigProxyProvider, this.navigationBarTransitionsProvider, this.provideEdgeBackGestureHandlerProvider, SysUIComponentImpl.this.setBackAnimationProvider, SysUIComponentImpl.this.provideUserTrackerProvider));
            }

            public NavigationBar getNavigationBar() {
                return this.navigationBarProvider.get();
            }
        }

        public final class CentralSurfacesComponentFactory implements CentralSurfacesComponent.Factory {
            public CentralSurfacesComponentFactory() {
            }

            public CentralSurfacesComponent create() {
                return new CentralSurfacesComponentImpl();
            }
        }

        public final class CentralSurfacesComponentImpl implements CentralSurfacesComponent {
            public Provider<AuthRippleController> authRippleControllerProvider;
            public Provider builderProvider;
            public Provider<FlingAnimationUtils.Builder> builderProvider2;
            public Provider<CarrierTextManager.Builder> builderProvider3;
            public Provider<QSCarrierGroupController.Builder> builderProvider4;
            public Provider<CentralSurfacesCommandQueueCallbacks> centralSurfacesCommandQueueCallbacksProvider;
            public Provider<EmergencyButtonController.Factory> factoryProvider;
            public Provider<AuthRippleView> getAuthRippleViewProvider;
            public Provider<BatteryMeterViewController> getBatteryMeterViewControllerProvider;
            public Provider<BatteryMeterView> getBatteryMeterViewProvider;
            public Provider<View> getLargeScreenShadeHeaderBarViewProvider;
            public Provider<LockIconView> getLockIconViewProvider;
            public Provider<NotificationPanelView> getNotificationPanelViewProvider;
            public Provider<NotificationsQuickSettingsContainer> getNotificationsQuickSettingsContainerProvider;
            public Provider<OngoingPrivacyChip> getSplitShadeOngoingPrivacyChipProvider;
            public Provider<TapAgainView> getTapAgainViewProvider;
            public Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
            public Provider<LargeScreenShadeHeaderController> largeScreenShadeHeaderControllerProvider;
            public Provider<LockIconViewController> lockIconViewControllerProvider;
            public Provider<NotificationLaunchAnimatorControllerProvider> notificationLaunchAnimatorControllerProvider;
            public Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
            public Provider<NotificationShadeWindowViewController> notificationShadeWindowViewControllerProvider;
            public Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
            public Provider<NotificationStackScrollLogger> notificationStackScrollLoggerProvider;
            public Provider<NotificationsQSContainerController> notificationsQSContainerControllerProvider;
            public Provider<NotificationListContainer> provideListContainerProvider;
            public Provider<NotificationShadeWindowView> providesNotificationShadeWindowViewProvider;
            public Provider<NotificationShelf> providesNotificationShelfProvider;
            public Provider<NotificationStackScrollLayout> providesNotificationStackScrollLayoutProvider;
            public Provider<NotificationShelfController> providesStatusBarWindowViewProvider;
            public Provider<StatusIconContainer> providesStatusIconContainerProvider;
            public Provider<StackStateLogger> stackStateLoggerProvider;
            public Provider<StatusBarHeadsUpChangeListener> statusBarHeadsUpChangeListenerProvider;
            public Provider<StatusBarInitializer> statusBarInitializerProvider;
            public Provider<StatusBarNotificationActivityStarterLogger> statusBarNotificationActivityStarterLoggerProvider;
            public Provider statusBarNotificationActivityStarterProvider;
            public Provider statusBarNotificationPresenterProvider;
            public Provider<TapAgainViewController> tapAgainViewControllerProvider;

            public CentralSurfacesComponentImpl() {
                initialize();
            }

            public final CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger() {
                return new CollapsedStatusBarFragmentLogger((LogBuffer) SysUIComponentImpl.this.provideCollapsedSbFragmentLogBufferProvider.get(), (DisableFlagsLogger) SysUIComponentImpl.this.disableFlagsLoggerProvider.get());
            }

            public final OperatorNameViewController.Factory operatorNameViewControllerFactory() {
                return new OperatorNameViewController.Factory((DarkIconDispatcher) SysUIComponentImpl.this.darkIconDispatcherImplProvider.get(), (NetworkController) SysUIComponentImpl.this.networkControllerImplProvider.get(), (TunerService) SysUIComponentImpl.this.tunerServiceImplProvider.get(), (TelephonyManager) SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider.get(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (CarrierConfigTracker) SysUIComponentImpl.this.carrierConfigTrackerProvider.get());
            }

            public final void initialize() {
                Provider<NotificationShadeWindowView> provider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory.create(SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesNotificationShadeWindowViewProvider = provider;
                this.providesNotificationStackScrollLayoutProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory.create(provider));
                this.providesNotificationShelfProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationShelfFactory.create(SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, this.providesNotificationStackScrollLayoutProvider));
                this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesStatusBarWindowViewFactory.create(SysUIComponentImpl.this.notificationShelfComponentBuilderProvider, this.providesNotificationShelfProvider));
                this.builderProvider = NotificationSwipeHelper_Builder_Factory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.provideViewConfigurationProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.featureFlagsDebugProvider);
                this.stackStateLoggerProvider = StackStateLogger_Factory.create(SysUIComponentImpl.this.provideNotificationHeadsUpLogBufferProvider);
                this.notificationStackScrollLoggerProvider = NotificationStackScrollLogger_Factory.create(SysUIComponentImpl.this.provideNotificationHeadsUpLogBufferProvider);
                this.notificationStackScrollLayoutControllerProvider = DoubleCheck.provider(NotificationStackScrollLayoutController_Factory.create(SysUIComponentImpl.this.provideAllowNotificationLongPressProvider, SysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIComponentImpl.this.provideNotificationVisibilityProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.notificationRoundnessManagerProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.bindDeviceProvisionedControllerProvider, SysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardMediaControllerProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.zenModeControllerImplProvider, SysUIComponentImpl.this.sysuiColorExtractorProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, this.builderProvider, SysUIComponentImpl.this.centralSurfacesImplProvider, SysUIComponentImpl.this.scrimControllerProvider, SysUIComponentImpl.this.notificationGroupManagerLegacyProvider, SysUIComponentImpl.this.provideGroupExpansionManagerProvider, SysUIComponentImpl.this.providesSilentHeaderControllerProvider, SysUIComponentImpl.this.notifPipelineFlagsProvider, SysUIComponentImpl.this.notifPipelineProvider, SysUIComponentImpl.this.notifCollectionProvider, SysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, SysUIComponentImpl.this.shadeTransitionControllerProvider, SysUIComponentImpl.this.this$0.provideIStatusBarServiceProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.provideVisualStabilityManagerProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.this$0.provideInteractionJankMonitorProvider, this.stackStateLoggerProvider, this.notificationStackScrollLoggerProvider, SysUIComponentImpl.this.notificationStackSizeCalculatorProvider));
                this.getNotificationPanelViewProvider = DoubleCheck.provider(StatusBarViewModule_GetNotificationPanelViewFactory.create(this.providesNotificationShadeWindowViewProvider));
                this.builderProvider2 = FlingAnimationUtils_Builder_Factory.create(SysUIComponentImpl.this.this$0.provideDisplayMetricsProvider);
                Provider<NotificationsQuickSettingsContainer> provider2 = DoubleCheck.provider(StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory.create(this.providesNotificationShadeWindowViewProvider));
                this.getNotificationsQuickSettingsContainerProvider = provider2;
                this.notificationsQSContainerControllerProvider = NotificationsQSContainerController_Factory.create(provider2, SysUIComponentImpl.this.navigationModeControllerProvider, SysUIComponentImpl.this.overviewProxyServiceProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider);
                this.getLockIconViewProvider = DoubleCheck.provider(StatusBarViewModule_GetLockIconViewFactory.create(this.providesNotificationShadeWindowViewProvider));
                this.getAuthRippleViewProvider = DoubleCheck.provider(StatusBarViewModule_GetAuthRippleViewFactory.create(this.providesNotificationShadeWindowViewProvider));
                this.authRippleControllerProvider = DoubleCheck.provider(AuthRippleController_Factory.create(SysUIComponentImpl.this.centralSurfacesImplProvider, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.authControllerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.commandRegistryProvider, SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.biometricUnlockControllerProvider, SysUIComponentImpl.this.udfpsControllerProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, this.getAuthRippleViewProvider));
                this.lockIconViewControllerProvider = DoubleCheck.provider(LockIconViewController_Factory.create(this.getLockIconViewProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.authControllerProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider, SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, SysUIComponentImpl.this.vibratorHelperProvider, this.authRippleControllerProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider));
                Provider<TapAgainView> provider3 = DoubleCheck.provider(StatusBarViewModule_GetTapAgainViewFactory.create(this.getNotificationPanelViewProvider));
                this.getTapAgainViewProvider = provider3;
                this.tapAgainViewControllerProvider = DoubleCheck.provider(TapAgainViewController_Factory.create(provider3, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, SysUIComponentImpl.this.configurationControllerImplProvider, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create()));
                Provider<View> provider4 = DoubleCheck.provider(StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory.create(this.providesNotificationShadeWindowViewProvider, SysUIComponentImpl.this.featureFlagsDebugProvider));
                this.getLargeScreenShadeHeaderBarViewProvider = provider4;
                this.getSplitShadeOngoingPrivacyChipProvider = DoubleCheck.provider(StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory.create(provider4));
                this.providesStatusIconContainerProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesStatusIconContainerFactory.create(this.getLargeScreenShadeHeaderBarViewProvider));
                this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(SysUIComponentImpl.this.privacyItemControllerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, this.getSplitShadeOngoingPrivacyChipProvider, SysUIComponentImpl.this.privacyDialogControllerProvider, SysUIComponentImpl.this.privacyLoggerProvider, this.providesStatusIconContainerProvider, SysUIComponentImpl.this.this$0.providePermissionManagerProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.appOpsControllerImplProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.this$0.provideSafetyCenterManagerProvider);
                this.builderProvider3 = CarrierTextManager_Builder_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.provideWifiManagerProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.telephonyListenerManagerProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider4 = QSCarrierGroupController_Builder_Factory.create(SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), SysUIComponentImpl.this.networkControllerImplProvider, this.builderProvider3, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.carrierConfigTrackerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                Provider<BatteryMeterView> provider5 = DoubleCheck.provider(StatusBarViewModule_GetBatteryMeterViewFactory.create(this.getLargeScreenShadeHeaderBarViewProvider));
                this.getBatteryMeterViewProvider = provider5;
                this.getBatteryMeterViewControllerProvider = DoubleCheck.provider(StatusBarViewModule_GetBatteryMeterViewControllerFactory.create(provider5, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideContentResolverProvider, SysUIComponentImpl.this.provideBatteryControllerProvider));
                this.largeScreenShadeHeaderControllerProvider = DoubleCheck.provider(LargeScreenShadeHeaderController_Factory.create(this.getLargeScreenShadeHeaderBarViewProvider, SysUIComponentImpl.this.statusBarIconControllerImplProvider, this.headerPrivacyIconsControllerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, this.builderProvider4, SysUIComponentImpl.this.featureFlagsDebugProvider, this.getBatteryMeterViewControllerProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider));
                this.provideListContainerProvider = DoubleCheck.provider(NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory.create(this.notificationStackScrollLayoutControllerProvider));
                this.factoryProvider = EmergencyButtonController_Factory_Factory.create(SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.this$0.providePowerManagerProvider, SysUIComponentImpl.this.this$0.provideActivityTaskManagerProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.this$0.provideTelecomManagerProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider);
                this.notificationPanelViewControllerProvider = DoubleCheck.provider(NotificationPanelViewController_Factory.create(this.getNotificationPanelViewProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.notificationWakeUpCoordinatorProvider, SysUIComponentImpl.this.pulseExpansionHandlerProvider, SysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, SysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.statusBarWindowStateControllerProvider, SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIComponentImpl.this.dozeLogProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.provideCommandQueueProvider, SysUIComponentImpl.this.vibratorHelperProvider, SysUIComponentImpl.this.this$0.provideLatencyTrackerProvider, SysUIComponentImpl.this.this$0.providePowerManagerProvider, SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, SysUIComponentImpl.this.this$0.provideDisplayIdProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.this$0.provideActivityManagerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, this.builderProvider2, SysUIComponentImpl.this.statusBarTouchableRegionManagerProvider, SysUIComponentImpl.this.conversationNotificationManagerProvider, SysUIComponentImpl.this.mediaHierarchyManagerProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, this.notificationsQSContainerControllerProvider, this.notificationStackScrollLayoutControllerProvider, SysUIComponentImpl.this.keyguardStatusViewComponentFactoryProvider, SysUIComponentImpl.this.keyguardQsUserSwitchComponentFactoryProvider, SysUIComponentImpl.this.keyguardUserSwitcherComponentFactoryProvider, SysUIComponentImpl.this.keyguardStatusBarViewComponentFactoryProvider, SysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, SysUIComponentImpl.this.notificationIconAreaControllerProvider, SysUIComponentImpl.this.authControllerProvider, SysUIComponentImpl.this.scrimControllerProvider, SysUIComponentImpl.this.this$0.provideUserManagerProvider, SysUIComponentImpl.this.mediaDataManagerProvider, SysUIComponentImpl.this.notificationShadeDepthControllerProvider, SysUIComponentImpl.this.ambientStateProvider, this.lockIconViewControllerProvider, SysUIComponentImpl.this.keyguardMediaControllerProvider, SysUIComponentImpl.this.privacyDotViewControllerProvider, this.tapAgainViewControllerProvider, SysUIComponentImpl.this.navigationModeControllerProvider, SysUIComponentImpl.this.fragmentServiceProvider, SysUIComponentImpl.this.this$0.provideContentResolverProvider, SysUIComponentImpl.this.quickAccessWalletControllerProvider, SysUIComponentImpl.this.qRCodeScannerControllerProvider, SysUIComponentImpl.this.recordingControllerProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.secureSettingsImplProvider, this.largeScreenShadeHeaderControllerProvider, SysUIComponentImpl.this.screenOffAnimationControllerProvider, SysUIComponentImpl.this.lockscreenGestureLoggerProvider, SysUIComponentImpl.this.panelExpansionStateManagerProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.provideSysUIUnfoldComponentProvider, SysUIComponentImpl.this.controlsComponentProvider, SysUIComponentImpl.this.this$0.provideInteractionJankMonitorProvider, SysUIComponentImpl.this.qsFrameTranslateImplProvider, SysUIComponentImpl.this.provideSysUiStateProvider, SysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider, this.provideListContainerProvider, SysUIComponentImpl.this.panelEventsEmitterProvider, SysUIComponentImpl.this.notificationStackSizeCalculatorProvider, SysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider, SysUIComponentImpl.this.shadeTransitionControllerProvider, SysUIComponentImpl.this.bindSystemClockProvider, this.factoryProvider));
                this.notificationShadeWindowViewControllerProvider = DoubleCheck.provider(NotificationShadeWindowViewController_Factory.create(SysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.dockManagerImplProvider, SysUIComponentImpl.this.notificationShadeDepthControllerProvider, this.providesNotificationShadeWindowViewProvider, this.notificationPanelViewControllerProvider, SysUIComponentImpl.this.panelExpansionStateManagerProvider, this.notificationStackScrollLayoutControllerProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.statusBarWindowStateControllerProvider, this.lockIconViewControllerProvider, SysUIComponentImpl.this.provideLowLightClockControllerProvider, SysUIComponentImpl.this.centralSurfacesImplProvider, SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider, SysUIComponentImpl.this.ambientStateProvider));
                this.statusBarHeadsUpChangeListenerProvider = DoubleCheck.provider(StatusBarHeadsUpChangeListener_Factory.create(SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIComponentImpl.this.statusBarWindowControllerProvider, this.notificationPanelViewControllerProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.provideNotificationsControllerProvider, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.dozeScrimControllerProvider));
                this.centralSurfacesCommandQueueCallbacksProvider = DoubleCheck.provider(CentralSurfacesCommandQueueCallbacks_Factory.create(SysUIComponentImpl.this.centralSurfacesImplProvider, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.provideCommandQueueProvider, this.notificationPanelViewControllerProvider, SysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.bindDeviceProvisionedControllerProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.assistManagerProvider, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, this.providesNotificationShadeWindowViewProvider, this.notificationStackScrollLayoutControllerProvider, SysUIComponentImpl.this.statusBarHideIconsForBouncerManagerProvider, SysUIComponentImpl.this.this$0.providePowerManagerProvider, SysUIComponentImpl.this.vibratorHelperProvider, SysUIComponentImpl.this.this$0.provideOptionalVibratorProvider, SysUIComponentImpl.this.lightBarControllerProvider, SysUIComponentImpl.this.disableFlagsLoggerProvider, SysUIComponentImpl.this.this$0.provideDisplayIdProvider));
                this.statusBarInitializerProvider = DoubleCheck.provider(StatusBarInitializer_Factory.create(SysUIComponentImpl.this.statusBarWindowControllerProvider));
                this.statusBarNotificationActivityStarterLoggerProvider = StatusBarNotificationActivityStarterLogger_Factory.create(SysUIComponentImpl.this.provideNotifInteractionLogBufferProvider);
                this.statusBarNotificationPresenterProvider = DoubleCheck.provider(StatusBarNotificationPresenter_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.notificationPanelViewControllerProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, this.providesNotificationShadeWindowViewProvider, SysUIComponentImpl.this.provideActivityStarterProvider, this.notificationStackScrollLayoutControllerProvider, SysUIComponentImpl.this.dozeScrimControllerProvider, SysUIComponentImpl.this.scrimControllerProvider, SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.keyguardIndicationControllerProvider, SysUIComponentImpl.this.centralSurfacesImplProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, SysUIComponentImpl.this.provideCommandQueueProvider, SysUIComponentImpl.this.provideNotificationViewHierarchyManagerProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.provideNotifShadeEventSourceProvider, SysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIComponentImpl.this.provideNotificationMediaManagerProvider, SysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.lockscreenGestureLoggerProvider, SysUIComponentImpl.this.initControllerProvider, SysUIComponentImpl.this.notificationInterruptStateProviderImplProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.notifPipelineFlagsProvider, SysUIComponentImpl.this.statusBarRemoteInputCallbackProvider, this.provideListContainerProvider));
                this.notificationLaunchAnimatorControllerProvider = DoubleCheck.provider(NotificationLaunchAnimatorControllerProvider_Factory.create(this.notificationShadeWindowViewControllerProvider, this.provideListContainerProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.this$0.provideInteractionJankMonitorProvider));
                this.statusBarNotificationActivityStarterProvider = DoubleCheck.provider(StatusBarNotificationActivityStarter_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.provideCommandQueueProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), SysUIComponentImpl.this.provideExecutorProvider, SysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIComponentImpl.this.notifPipelineProvider, SysUIComponentImpl.this.provideNotificationVisibilityProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.notificationClickNotifierProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.this$0.provideKeyguardManagerProvider, SysUIComponentImpl.this.this$0.provideIDreamManagerProvider, SysUIComponentImpl.this.provideBubblesManagerProvider, SysUIComponentImpl.this.assistManagerProvider, SysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIComponentImpl.this.provideGroupMembershipManagerProvider, SysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.notificationInterruptStateProviderImplProvider, SysUIComponentImpl.this.this$0.provideLockPatternUtilsProvider, SysUIComponentImpl.this.statusBarRemoteInputCallbackProvider, SysUIComponentImpl.this.activityIntentHelperProvider, SysUIComponentImpl.this.notifPipelineFlagsProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, this.statusBarNotificationActivityStarterLoggerProvider, SysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, SysUIComponentImpl.this.centralSurfacesImplProvider, this.statusBarNotificationPresenterProvider, this.notificationPanelViewControllerProvider, SysUIComponentImpl.this.provideActivityLaunchAnimatorProvider, this.notificationLaunchAnimatorControllerProvider));
            }

            public NotificationShadeWindowView getNotificationShadeWindowView() {
                return this.providesNotificationShadeWindowViewProvider.get();
            }

            public NotificationShelfController getNotificationShelfController() {
                return this.providesStatusBarWindowViewProvider.get();
            }

            public NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
                return this.notificationStackScrollLayoutControllerProvider.get();
            }

            public NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
                return this.notificationShadeWindowViewControllerProvider.get();
            }

            public NotificationPanelViewController getNotificationPanelViewController() {
                return this.notificationPanelViewControllerProvider.get();
            }

            public LockIconViewController getLockIconViewController() {
                return this.lockIconViewControllerProvider.get();
            }

            public AuthRippleController getAuthRippleController() {
                return this.authRippleControllerProvider.get();
            }

            public StatusBarHeadsUpChangeListener getStatusBarHeadsUpChangeListener() {
                return this.statusBarHeadsUpChangeListenerProvider.get();
            }

            public CentralSurfacesCommandQueueCallbacks getCentralSurfacesCommandQueueCallbacks() {
                return this.centralSurfacesCommandQueueCallbacksProvider.get();
            }

            public LargeScreenShadeHeaderController getLargeScreenShadeHeaderController() {
                return this.largeScreenShadeHeaderControllerProvider.get();
            }

            public CollapsedStatusBarFragment createCollapsedStatusBarFragment() {
                StatusBarFragmentComponentFactory statusBarFragmentComponentFactory = r2;
                StatusBarFragmentComponentFactory statusBarFragmentComponentFactory2 = new StatusBarFragmentComponentFactory();
                return StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory.createCollapsedStatusBarFragment(statusBarFragmentComponentFactory, (OngoingCallController) SysUIComponentImpl.this.provideOngoingCallControllerProvider.get(), (SystemStatusAnimationScheduler) SysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.get(), (StatusBarLocationPublisher) SysUIComponentImpl.this.statusBarLocationPublisherProvider.get(), (NotificationIconAreaController) SysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (PanelExpansionStateManager) SysUIComponentImpl.this.panelExpansionStateManagerProvider.get(), (FeatureFlags) SysUIComponentImpl.this.featureFlagsDebugProvider.get(), (StatusBarIconController) SysUIComponentImpl.this.statusBarIconControllerImplProvider.get(), (StatusBarHideIconsForBouncerManager) SysUIComponentImpl.this.statusBarHideIconsForBouncerManagerProvider.get(), (KeyguardStateController) SysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), this.notificationPanelViewControllerProvider.get(), (NetworkController) SysUIComponentImpl.this.networkControllerImplProvider.get(), (StatusBarStateController) SysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (CommandQueue) SysUIComponentImpl.this.provideCommandQueueProvider.get(), (CarrierConfigTracker) SysUIComponentImpl.this.carrierConfigTrackerProvider.get(), collapsedStatusBarFragmentLogger(), operatorNameViewControllerFactory(), (SecureSettings) SysUIComponentImpl.this.secureSettingsImpl(), (Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get());
            }

            public StatusBarInitializer getStatusBarInitializer() {
                return this.statusBarInitializerProvider.get();
            }

            public Set<CentralSurfacesComponent.Startable> getStartables() {
                return Collections.emptySet();
            }

            public NotificationActivityStarter getNotificationActivityStarter() {
                return (NotificationActivityStarter) this.statusBarNotificationActivityStarterProvider.get();
            }

            public NotificationPresenter getNotificationPresenter() {
                return (NotificationPresenter) this.statusBarNotificationPresenterProvider.get();
            }

            public NotificationRowBinderImpl.BindRowCallback getBindRowCallback() {
                return (NotificationRowBinderImpl.BindRowCallback) this.statusBarNotificationPresenterProvider.get();
            }

            public NotificationListContainer getNotificationListContainer() {
                return this.provideListContainerProvider.get();
            }

            public final class StatusBarFragmentComponentFactory implements StatusBarFragmentComponent.Factory {
                public StatusBarFragmentComponentFactory() {
                }

                public StatusBarFragmentComponent create(CollapsedStatusBarFragment collapsedStatusBarFragment) {
                    Preconditions.checkNotNull(collapsedStatusBarFragment);
                    return new StatusBarFragmentComponentI(collapsedStatusBarFragment);
                }
            }

            public final class StatusBarFragmentComponentI implements StatusBarFragmentComponent {
                public Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
                public Provider<CollapsedStatusBarFragment> collapsedStatusBarFragmentProvider;
                public Provider<PhoneStatusBarViewController.Factory> factoryProvider;
                public Provider<HeadsUpAppearanceController> headsUpAppearanceControllerProvider;
                public Provider<LightsOutNotifController> lightsOutNotifControllerProvider;
                public Provider<BatteryMeterView> provideBatteryMeterViewProvider;
                public Provider<Clock> provideClockProvider;
                public Provider<View> provideLightsOutNotifViewProvider;
                public Provider<Optional<View>> provideOperatorFrameNameViewProvider;
                public Provider<View> provideOperatorNameViewProvider;
                public Provider<PhoneStatusBarTransitions> providePhoneStatusBarTransitionsProvider;
                public Provider<PhoneStatusBarViewController> providePhoneStatusBarViewControllerProvider;
                public Provider<PhoneStatusBarView> providePhoneStatusBarViewProvider;
                public Provider<StatusBarUserSwitcherContainer> provideStatusBarUserSwitcherContainerProvider;
                public Provider<HeadsUpStatusBarView> providesHeasdUpStatusBarViewProvider;
                public Provider<StatusBarDemoMode> statusBarDemoModeProvider;
                public Provider<StatusBarUserSwitcherControllerImpl> statusBarUserSwitcherControllerImplProvider;

                public /* bridge */ /* synthetic */ void init() {
                    super.init();
                }

                public StatusBarFragmentComponentI(CollapsedStatusBarFragment collapsedStatusBarFragment) {
                    initialize(collapsedStatusBarFragment);
                }

                public final void initialize(CollapsedStatusBarFragment collapsedStatusBarFragment) {
                    Factory create = InstanceFactory.create(collapsedStatusBarFragment);
                    this.collapsedStatusBarFragmentProvider = create;
                    Provider<PhoneStatusBarView> provider = DoubleCheck.provider(StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory.create(create));
                    this.providePhoneStatusBarViewProvider = provider;
                    this.provideBatteryMeterViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideBatteryMeterViewFactory.create(provider));
                    Provider<StatusBarUserSwitcherContainer> provider2 = DoubleCheck.provider(StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory.create(this.providePhoneStatusBarViewProvider));
                    this.provideStatusBarUserSwitcherContainerProvider = provider2;
                    StatusBarUserSwitcherControllerImpl_Factory create2 = StatusBarUserSwitcherControllerImpl_Factory.create(provider2, SysUIComponentImpl.this.statusBarUserInfoTrackerProvider, SysUIComponentImpl.this.statusBarUserSwitcherFeatureControllerProvider, SysUIComponentImpl.this.userSwitchDialogControllerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.falsingManagerProxyProvider);
                    this.statusBarUserSwitcherControllerImplProvider = create2;
                    this.bindStatusBarUserSwitcherControllerProvider = DoubleCheck.provider(create2);
                    PhoneStatusBarViewController_Factory_Factory create3 = PhoneStatusBarViewController_Factory_Factory.create(SysUIComponentImpl.this.provideSysUIUnfoldComponentProvider, SysUIComponentImpl.this.this$0.provideStatusBarScopedTransitionProvider, this.bindStatusBarUserSwitcherControllerProvider, SysUIComponentImpl.this.viewUtilProvider, SysUIComponentImpl.this.configurationControllerImplProvider);
                    this.factoryProvider = create3;
                    this.providePhoneStatusBarViewControllerProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory.create(create3, this.providePhoneStatusBarViewProvider, CentralSurfacesComponentImpl.this.notificationPanelViewControllerProvider));
                    this.providesHeasdUpStatusBarViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory.create(this.providePhoneStatusBarViewProvider));
                    this.provideClockProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideClockFactory.create(this.providePhoneStatusBarViewProvider));
                    this.provideOperatorFrameNameViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory.create(this.providePhoneStatusBarViewProvider));
                    this.headsUpAppearanceControllerProvider = DoubleCheck.provider(HeadsUpAppearanceController_Factory.create(SysUIComponentImpl.this.notificationIconAreaControllerProvider, SysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.notificationWakeUpCoordinatorProvider, SysUIComponentImpl.this.darkIconDispatcherImplProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.provideCommandQueueProvider, CentralSurfacesComponentImpl.this.notificationStackScrollLayoutControllerProvider, CentralSurfacesComponentImpl.this.notificationPanelViewControllerProvider, this.providesHeasdUpStatusBarViewProvider, this.provideClockProvider, this.provideOperatorFrameNameViewProvider));
                    Provider<View> provider3 = DoubleCheck.provider(StatusBarFragmentModule_ProvideLightsOutNotifViewFactory.create(this.providePhoneStatusBarViewProvider));
                    this.provideLightsOutNotifViewProvider = provider3;
                    this.lightsOutNotifControllerProvider = DoubleCheck.provider(LightsOutNotifController_Factory.create(provider3, SysUIComponentImpl.this.this$0.provideWindowManagerProvider, SysUIComponentImpl.this.notifLiveDataStoreImplProvider, SysUIComponentImpl.this.provideCommandQueueProvider));
                    this.provideOperatorNameViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideOperatorNameViewFactory.create(this.providePhoneStatusBarViewProvider));
                    this.providePhoneStatusBarTransitionsProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory.create(this.providePhoneStatusBarViewProvider, SysUIComponentImpl.this.statusBarWindowControllerProvider));
                    this.statusBarDemoModeProvider = DoubleCheck.provider(StatusBarDemoMode_Factory.create(this.provideClockProvider, this.provideOperatorNameViewProvider, SysUIComponentImpl.this.provideDemoModeControllerProvider, this.providePhoneStatusBarTransitionsProvider, SysUIComponentImpl.this.navigationBarControllerProvider, SysUIComponentImpl.this.this$0.provideDisplayIdProvider));
                }

                public BatteryMeterViewController getBatteryMeterViewController() {
                    return new BatteryMeterViewController(this.provideBatteryMeterViewProvider.get(), (ConfigurationController) SysUIComponentImpl.this.configurationControllerImplProvider.get(), (TunerService) SysUIComponentImpl.this.tunerServiceImplProvider.get(), (BroadcastDispatcher) SysUIComponentImpl.this.broadcastDispatcherProvider.get(), SysUIComponentImpl.this.this$0.mainHandler(), (ContentResolver) SysUIComponentImpl.this.this$0.provideContentResolverProvider.get(), (BatteryController) SysUIComponentImpl.this.provideBatteryControllerProvider.get());
                }

                public PhoneStatusBarView getPhoneStatusBarView() {
                    return this.providePhoneStatusBarViewProvider.get();
                }

                public PhoneStatusBarViewController getPhoneStatusBarViewController() {
                    return this.providePhoneStatusBarViewControllerProvider.get();
                }

                public HeadsUpAppearanceController getHeadsUpAppearanceController() {
                    return this.headsUpAppearanceControllerProvider.get();
                }

                public LightsOutNotifController getLightsOutNotifController() {
                    return this.lightsOutNotifControllerProvider.get();
                }

                public StatusBarDemoMode getStatusBarDemoMode() {
                    return this.statusBarDemoModeProvider.get();
                }

                public PhoneStatusBarTransitions getPhoneStatusBarTransitions() {
                    return this.providePhoneStatusBarTransitionsProvider.get();
                }
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
                this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider));
            }

            public KeyguardClockSwitchController getKeyguardClockSwitchController() {
                return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) SysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) SysUIComponentImpl.this.sysuiColorExtractorProvider.get(), (ClockManager) SysUIComponentImpl.this.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) SysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) SysUIComponentImpl.this.broadcastDispatcherProvider.get(), (BatteryController) SysUIComponentImpl.this.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (LockscreenSmartspaceController) SysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) SysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SecureSettings) SysUIComponentImpl.this.secureSettingsImpl(), (Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), SysUIComponentImpl.this.this$0.mainResources(), (DumpManager) SysUIComponentImpl.this.this$0.dumpManagerProvider.get());
            }

            public KeyguardStatusViewController getKeyguardStatusViewController() {
                return new KeyguardStatusViewController(this.presentation, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) SysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (ConfigurationController) SysUIComponentImpl.this.configurationControllerImplProvider.get(), (DozeParameters) SysUIComponentImpl.this.dozeParametersProvider.get(), (ScreenOffAnimationController) SysUIComponentImpl.this.screenOffAnimationControllerProvider.get());
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
                Provider<KeyguardHostView> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(create, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesKeyguardHostViewProvider = provider;
                this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider));
                this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.providesKeyguardSecurityContainerProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider));
                this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
                this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider);
                this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.this$0.providePowerManagerProvider, SysUIComponentImpl.this.this$0.provideActivityTaskManagerProvider, SysUIComponentImpl.this.shadeControllerImplProvider, SysUIComponentImpl.this.this$0.provideTelecomManagerProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider);
                this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideLockPatternUtilsProvider, SysUIComponentImpl.this.this$0.provideLatencyTrackerProvider, SysUIComponentImpl.this.factoryProvider8, SysUIComponentImpl.this.this$0.provideInputMethodManagerProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, this.liftToActivateListenerProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, this.factoryProvider2, SysUIComponentImpl.this.devicePostureControllerImplProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider);
                this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
                this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, SysUIComponentImpl.this.this$0.provideLockPatternUtilsProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.keyguardSecurityModelProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.falsingCollectorImplProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.userSwitcherControllerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.globalSettingsImplProvider, SysUIComponentImpl.this.sessionTrackerProvider);
                this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.this$0.provideAudioManagerProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.providesViewMediatorCallbackProvider, this.factoryProvider4));
            }

            public KeyguardHostViewController getKeyguardHostViewController() {
                return this.keyguardHostViewControllerProvider.get();
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
                this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.dozeParametersProvider));
                this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(SysUIComponentImpl.this.builderProvider4, SysUIComponentImpl.this.this$0.provideMainHandlerProvider));
                this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideAlarmManagerProvider, SysUIComponentImpl.this.alwaysOnDisplayPolicyProvider));
                this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(SysUIComponentImpl.this.falsingCollectorImplProvider));
                this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, SysUIComponentImpl.this.dockManagerImplProvider, SysUIComponentImpl.this.provideProximitySensorProvider, SysUIComponentImpl.this.provideProximityCheckProvider, SysUIComponentImpl.this.dozeLogProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.secureSettingsImplProvider, SysUIComponentImpl.this.authControllerProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.devicePostureControllerImplProvider));
                this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.dozeLogProvider));
                this.providesBrightnessSensorsProvider = DozeModule_ProvidesBrightnessSensorsFactory.create(SysUIComponentImpl.this.asyncSensorManagerProvider, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.dozeParametersProvider);
                this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.providesWrappedServiceProvider, SysUIComponentImpl.this.asyncSensorManagerProvider, this.providesBrightnessSensorsProvider, SysUIComponentImpl.this.dozeServiceHostProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), SysUIComponentImpl.this.alwaysOnDisplayPolicyProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.devicePostureControllerImplProvider, SysUIComponentImpl.this.dozeLogProvider));
                this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.dozeParametersProvider, this.providesDozeWakeLockProvider, SysUIComponentImpl.this.authControllerProvider, SysUIComponentImpl.this.udfpsControllerProvider, SysUIComponentImpl.this.dozeLogProvider, this.dozeScreenBrightnessProvider));
                this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), SysUIComponentImpl.this.biometricUnlockControllerProvider, SysUIComponentImpl.this.dozeParametersProvider));
                this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(SysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, SysUIComponentImpl.this.dockManagerImplProvider));
                this.dozeAuthRemoverProvider = DoubleCheck.provider(DozeAuthRemover_Factory.create(SysUIComponentImpl.this.keyguardUpdateMonitorProvider));
                Provider<DozeSuppressor> provider = DoubleCheck.provider(DozeSuppressor_Factory.create(SysUIComponentImpl.this.dozeServiceHostProvider, SysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, SysUIComponentImpl.this.dozeLogProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.this$0.provideUiModeManagerProvider, SysUIComponentImpl.this.biometricUnlockControllerProvider));
                this.dozeSuppressorProvider = provider;
                this.providesDozeMachinePartsProvider = DozeModule_ProvidesDozeMachinePartsFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, this.dozeAuthRemoverProvider, provider);
                this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, SysUIComponentImpl.this.this$0.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.provideBatteryControllerProvider, SysUIComponentImpl.this.dozeLogProvider, SysUIComponentImpl.this.dockManagerImplProvider, SysUIComponentImpl.this.dozeServiceHostProvider, this.providesDozeMachinePartsProvider));
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
                return BouncerSwipeModule.providesSwipeToBouncerStartRegion(SysUIComponentImpl.this.this$0.mainResources());
            }

            public final BouncerSwipeTouchHandler bouncerSwipeTouchHandler() {
                return new BouncerSwipeTouchHandler(SysUIComponentImpl.this.this$0.displayMetrics(), (StatusBarKeyguardViewManager) SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider.get(), Optional.of((CentralSurfaces) SysUIComponentImpl.this.centralSurfacesImplProvider.get()), (NotificationShadeWindowController) SysUIComponentImpl.this.notificationShadeWindowControllerImplProvider.get(), BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory.providesValueAnimatorCreator(), BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory.providesVelocityTrackerFactory(), namedFlingAnimationUtils(), namedFlingAnimationUtils2(), namedFloat(), (UiEventLogger) SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider.get());
            }

            public final DreamTouchHandler providesBouncerSwipeTouchHandler() {
                return BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler());
            }

            public final Complication.VisibilityController visibilityController() {
                return ComplicationModule_ProvidesVisibilityControllerFactory.providesVisibilityController(this.complicationLayoutEngineProvider.get());
            }

            public final HideComplicationTouchHandler hideComplicationTouchHandler() {
                return HideComplicationTouchHandler_Factory.newInstance(visibilityController(), this.providesComplicationsRestoreTimeoutProvider.get().intValue(), this.providesTouchInsetManagerProvider.get(), (Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), SysUIComponentImpl.this.this$0.mainHandler());
            }

            public final DreamTouchHandler providesHideComplicationTouchHandler() {
                return HideComplicationModule_ProvidesHideComplicationTouchHandlerFactory.providesHideComplicationTouchHandler(hideComplicationTouchHandler());
            }

            public final Set<DreamTouchHandler> setOfDreamTouchHandler() {
                return SetBuilder.newSetBuilder(2).add(providesBouncerSwipeTouchHandler()).add(providesHideComplicationTouchHandler()).build();
            }

            public final void initialize(ViewModelStore viewModelStore, Complication.Host host2) {
                this.providesDreamOverlayContainerViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory.create(SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesComplicationHostViewProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationHostViewFactory.create(SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider));
                this.providesComplicationPaddingProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationPaddingFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider));
                Provider<TouchInsetManager> provider = DoubleCheck.provider(DreamOverlayModule_ProvidesTouchInsetManagerFactory.create(SysUIComponentImpl.this.this$0.provideMainExecutorProvider, this.providesDreamOverlayContainerViewProvider));
                this.providesTouchInsetManagerProvider = provider;
                this.providesTouchInsetSessionProvider = DreamOverlayModule_ProvidesTouchInsetSessionFactory.create(provider);
                this.providesComplicationsFadeInDurationProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider));
                Provider<Integer> provider2 = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider));
                this.providesComplicationsFadeOutDurationProvider = provider2;
                this.complicationLayoutEngineProvider = DoubleCheck.provider(ComplicationLayoutEngine_Factory.create(this.providesComplicationHostViewProvider, this.providesComplicationPaddingProvider, this.providesTouchInsetSessionProvider, this.providesComplicationsFadeInDurationProvider, provider2));
                DelegateFactory delegateFactory = new DelegateFactory();
                this.providesLifecycleOwnerProvider = delegateFactory;
                Provider<LifecycleRegistry> provider3 = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleRegistryFactory.create(delegateFactory));
                this.providesLifecycleRegistryProvider = provider3;
                DelegateFactory.setDelegate(this.providesLifecycleOwnerProvider, DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleOwnerFactory.create(provider3)));
                this.storeProvider = InstanceFactory.create(viewModelStore);
                this.complicationCollectionLiveDataProvider = ComplicationCollectionLiveData_Factory.create(SysUIComponentImpl.this.dreamOverlayStateControllerProvider);
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
                Provider<DreamOverlayStatusBarView> provider4 = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory.create(this.providesDreamOverlayContainerViewProvider));
                this.providesDreamOverlayStatusBarViewProvider = provider4;
                this.dreamOverlayStatusBarViewControllerProvider = DoubleCheck.provider(DreamOverlayStatusBarViewController_Factory.create(provider4, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.this$0.provideConnectivityManagagerProvider, this.providesTouchInsetSessionProvider, SysUIComponentImpl.this.this$0.provideAlarmManagerProvider, SysUIComponentImpl.this.nextAlarmControllerImplProvider, SysUIComponentImpl.this.dateFormatUtilProvider, SysUIComponentImpl.this.provideIndividualSensorPrivacyControllerProvider, SysUIComponentImpl.this.dreamOverlayNotificationCountProvider, SysUIComponentImpl.this.zenModeControllerImplProvider, SysUIComponentImpl.this.statusBarWindowStateControllerProvider));
                this.providesMaxBurnInOffsetProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesMaxBurnInOffsetFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider));
                this.providesBurnInProtectionUpdateIntervalProvider = DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider);
                this.providesMillisUntilFullJitterProvider = DreamOverlayModule_ProvidesMillisUntilFullJitterFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider);
                this.dreamOverlayContainerViewControllerProvider = DoubleCheck.provider(DreamOverlayContainerViewController_Factory.create(this.providesDreamOverlayContainerViewProvider, this.complicationHostViewControllerProvider, this.providesDreamOverlayContentViewProvider, this.dreamOverlayStatusBarViewControllerProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIComponentImpl.this.blurUtilsProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, this.providesMaxBurnInOffsetProvider, this.providesBurnInProtectionUpdateIntervalProvider, this.providesMillisUntilFullJitterProvider));
                this.providesLifecycleProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleFactory.create(this.providesLifecycleOwnerProvider));
                this.builderProvider = FlingAnimationUtils_Builder_Factory.create(SysUIComponentImpl.this.this$0.provideDisplayMetricsProvider);
                this.providesComplicationsRestoreTimeoutProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory.create(SysUIComponentImpl.this.this$0.provideResourcesProvider));
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
                return new DreamOverlayTouchMonitor((Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), this.providesLifecycleProvider.get(), new InputSessionComponentFactory(), setOfDreamTouchHandler());
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
                this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.provideUserTrackerProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider));
                QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
                this.provideThemedContextProvider = create3;
                this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(create3, SysUIComponentImpl.this.qSTileHostProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider));
                this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, SysUIComponentImpl.this.qSTileHostProvider, this.tileAdapterProvider, SysUIComponentImpl.this.this$0.screenLifecycleProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.lightBarControllerProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider));
                this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(SysUIComponentImpl.this.this$0.contextProvider);
                this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, this.qSCustomizerControllerProvider));
                this.factoryProvider2 = BrightnessController_Factory_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.provideBgHandlerProvider);
                this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, SysUIComponentImpl.this.providesQSMediaHostProvider, this.factoryProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.qSLoggerProvider, this.factoryProvider2, SysUIComponentImpl.this.factoryProvider6, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.statusBarKeyguardViewManagerProvider));
                QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create4 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
                this.providesQuickStatusBarHeaderProvider = create4;
                this.providesQuickQSPanelProvider = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create4);
                this.providesQSUsingCollapsedLandscapeMediaProvider = QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory.create(SysUIComponentImpl.this.this$0.contextProvider);
                Provider<QuickQSPanelController> provider = DoubleCheck.provider(QuickQSPanelController_Factory.create(this.providesQuickQSPanelProvider, SysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, SysUIComponentImpl.this.providesQuickQSMediaHostProvider, this.providesQSUsingCollapsedLandscapeMediaProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.qSLoggerProvider, SysUIComponentImpl.this.this$0.dumpManagerProvider));
                this.quickQSPanelControllerProvider = provider;
                this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider, SysUIComponentImpl.this.qSTileHostProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.this$0.qSExpansionPathInterpolatorProvider));
                this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
                this.providesPrivacyChipProvider = DoubleCheck.provider(QSFragmentModule_ProvidesPrivacyChipFactory.create(this.providesQuickStatusBarHeaderProvider));
                this.providesStatusIconContainerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesStatusIconContainerFactory.create(this.providesQuickStatusBarHeaderProvider));
                this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(SysUIComponentImpl.this.privacyItemControllerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, this.providesPrivacyChipProvider, SysUIComponentImpl.this.privacyDialogControllerProvider, SysUIComponentImpl.this.privacyLoggerProvider, this.providesStatusIconContainerProvider, SysUIComponentImpl.this.this$0.providePermissionManagerProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.appOpsControllerImplProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.this$0.provideSafetyCenterManagerProvider);
                this.builderProvider = CarrierTextManager_Builder_Factory.create(SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.provideWifiManagerProvider, SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider, SysUIComponentImpl.this.telephonyListenerManagerProvider, SysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), SysUIComponentImpl.this.networkControllerImplProvider, this.builderProvider, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.carrierConfigTrackerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                this.factoryProvider3 = VariableDateViewController_Factory_Factory.create(SysUIComponentImpl.this.bindSystemClockProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.provideTimeTickHandlerProvider);
                QSFragmentModule_ProvidesBatteryMeterViewFactory create5 = QSFragmentModule_ProvidesBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
                this.providesBatteryMeterViewProvider = create5;
                this.batteryMeterViewControllerProvider = BatteryMeterViewController_Factory.create(create5, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.tunerServiceImplProvider, SysUIComponentImpl.this.broadcastDispatcherProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.this$0.provideContentResolverProvider, SysUIComponentImpl.this.provideBatteryControllerProvider);
                Provider provider2 = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, this.headerPrivacyIconsControllerProvider, SysUIComponentImpl.this.statusBarIconControllerImplProvider, SysUIComponentImpl.this.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, SysUIComponentImpl.this.sysuiColorExtractorProvider, SysUIComponentImpl.this.this$0.qSExpansionPathInterpolatorProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, this.factoryProvider3, this.batteryMeterViewControllerProvider, SysUIComponentImpl.this.statusBarContentInsetsProvider));
                this.quickStatusBarHeaderControllerProvider = provider2;
                this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, provider2, SysUIComponentImpl.this.configurationControllerImplProvider));
                QSFragmentModule_ProvidesQSFooterViewFactory create6 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
                this.providesQSFooterViewProvider = create6;
                Provider<QSFooterViewController> provider3 = DoubleCheck.provider(QSFooterViewController_Factory.create(create6, SysUIComponentImpl.this.provideUserTrackerProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.provideActivityStarterProvider, this.qSPanelControllerProvider));
                this.qSFooterViewControllerProvider = provider3;
                this.providesQSFooterProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSFooterFactory.create(provider3));
                this.qSSquishinessControllerProvider = DoubleCheck.provider(QSSquishinessController_Factory.create(this.qSAnimatorProvider, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider));
                this.providesQSFooterActionsViewProvider = QSFragmentModule_ProvidesQSFooterActionsViewFactory.create(this.provideRootViewProvider);
                this.factoryProvider4 = DoubleCheck.provider(MultiUserSwitchController_Factory_Factory.create(SysUIComponentImpl.this.this$0.provideUserManagerProvider, SysUIComponentImpl.this.userSwitcherControllerProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.userSwitchDialogControllerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.provideActivityStarterProvider));
                QSFragmentModule_ProvideThemedLayoutInflaterFactory create7 = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(this.provideThemedContextProvider);
                this.provideThemedLayoutInflaterProvider = create7;
                Provider<View> provider4 = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(create7, this.providesQSFooterActionsViewProvider));
                this.providesQSSecurityFooterViewProvider = provider4;
                this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider4, SysUIComponentImpl.this.provideUserTrackerProvider, SysUIComponentImpl.this.this$0.provideMainHandlerProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.securityControllerImplProvider, SysUIComponentImpl.this.provideDialogLaunchAnimatorProvider, SysUIComponentImpl.this.provideBgLooperProvider, SysUIComponentImpl.this.broadcastDispatcherProvider));
                Provider<View> provider5 = DoubleCheck.provider(QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, this.providesQSFooterActionsViewProvider));
                this.providesQSFgsManagerFooterViewProvider = provider5;
                this.qSFgsManagerFooterProvider = DoubleCheck.provider(QSFgsManagerFooter_Factory.create(provider5, SysUIComponentImpl.this.this$0.provideMainExecutorProvider, SysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIComponentImpl.this.fgsManagerControllerProvider));
                this.footerActionsControllerProvider = DoubleCheck.provider(FooterActionsController_Factory.create(this.providesQSFooterActionsViewProvider, this.factoryProvider4, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.this$0.provideUserManagerProvider, SysUIComponentImpl.this.provideUserTrackerProvider, SysUIComponentImpl.this.userInfoControllerImplProvider, SysUIComponentImpl.this.bindDeviceProvisionedControllerProvider, this.qSSecurityFooterProvider, this.qSFgsManagerFooterProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.this$0.provideMetricsLoggerProvider, SysUIComponentImpl.this.globalActionsDialogLiteProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider, SysUIComponentImpl.this.isPMLiteEnabledProvider, SysUIComponentImpl.this.globalSettingsImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
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

        public final class NotificationShelfComponentBuilder implements NotificationShelfComponent.Builder {
            public NotificationShelf notificationShelf;

            public NotificationShelfComponentBuilder() {
            }

            public NotificationShelfComponentBuilder notificationShelf(NotificationShelf notificationShelf2) {
                this.notificationShelf = (NotificationShelf) Preconditions.checkNotNull(notificationShelf2);
                return this;
            }

            public NotificationShelfComponent build() {
                Preconditions.checkBuilderRequirement(this.notificationShelf, NotificationShelf.class);
                return new NotificationShelfComponentImpl(this.notificationShelf);
            }
        }

        public final class NotificationShelfComponentImpl implements NotificationShelfComponent {
            public Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
            public Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
            public Provider<ExpandableViewController> expandableViewControllerProvider;
            public Provider<NotificationTapHelper.Factory> factoryProvider;
            public Provider<NotificationShelfController> notificationShelfControllerProvider;
            public Provider<NotificationShelf> notificationShelfProvider;

            public NotificationShelfComponentImpl(NotificationShelf notificationShelf) {
                initialize(notificationShelf);
            }

            public final void initialize(NotificationShelf notificationShelf) {
                this.notificationShelfProvider = InstanceFactory.create(notificationShelf);
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.this$0.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.notificationShelfProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.notificationShelfProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                ActivatableNotificationViewController_Factory create3 = ActivatableNotificationViewController_Factory.create(this.notificationShelfProvider, this.factoryProvider, create2, SysUIComponentImpl.this.this$0.provideAccessibilityManagerProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.falsingCollectorImplProvider);
                this.activatableNotificationViewControllerProvider = create3;
                this.notificationShelfControllerProvider = DoubleCheck.provider(NotificationShelfController_Factory.create(this.notificationShelfProvider, create3, SysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider));
            }

            public NotificationShelfController getNotificationShelfController() {
                return this.notificationShelfControllerProvider.get();
            }
        }

        public final class KeyguardQsUserSwitchComponentFactory implements KeyguardQsUserSwitchComponent.Factory {
            public KeyguardQsUserSwitchComponentFactory() {
            }

            public KeyguardQsUserSwitchComponent build(FrameLayout frameLayout) {
                Preconditions.checkNotNull(frameLayout);
                return new KeyguardQsUserSwitchComponentImpl(frameLayout);
            }
        }

        public final class KeyguardQsUserSwitchComponentImpl implements KeyguardQsUserSwitchComponent {
            public Provider<KeyguardQsUserSwitchController> keyguardQsUserSwitchControllerProvider;
            public Provider<FrameLayout> userAvatarContainerProvider;

            public KeyguardQsUserSwitchComponentImpl(FrameLayout frameLayout) {
                initialize(frameLayout);
            }

            public final void initialize(FrameLayout frameLayout) {
                Factory create = InstanceFactory.create(frameLayout);
                this.userAvatarContainerProvider = create;
                this.keyguardQsUserSwitchControllerProvider = DoubleCheck.provider(KeyguardQsUserSwitchController_Factory.create(create, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.userSwitcherControllerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.falsingManagerProxyProvider, SysUIComponentImpl.this.configurationControllerImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.screenOffAnimationControllerProvider, SysUIComponentImpl.this.userSwitchDialogControllerProvider, SysUIComponentImpl.this.this$0.provideUiEventLoggerProvider));
            }

            public KeyguardQsUserSwitchController getKeyguardQsUserSwitchController() {
                return this.keyguardQsUserSwitchControllerProvider.get();
            }
        }

        public final class KeyguardUserSwitcherComponentFactory implements KeyguardUserSwitcherComponent.Factory {
            public KeyguardUserSwitcherComponentFactory() {
            }

            public KeyguardUserSwitcherComponent build(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Preconditions.checkNotNull(keyguardUserSwitcherView);
                return new KeyguardUserSwitcherComponentImpl(keyguardUserSwitcherView);
            }
        }

        public final class KeyguardUserSwitcherComponentImpl implements KeyguardUserSwitcherComponent {
            public Provider<KeyguardUserSwitcherController> keyguardUserSwitcherControllerProvider;
            public Provider<KeyguardUserSwitcherView> keyguardUserSwitcherViewProvider;

            public KeyguardUserSwitcherComponentImpl(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                initialize(keyguardUserSwitcherView);
            }

            public final void initialize(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Factory create = InstanceFactory.create(keyguardUserSwitcherView);
                this.keyguardUserSwitcherViewProvider = create;
                this.keyguardUserSwitcherControllerProvider = DoubleCheck.provider(KeyguardUserSwitcherController_Factory.create(create, SysUIComponentImpl.this.this$0.contextProvider, SysUIComponentImpl.this.this$0.provideResourcesProvider, SysUIComponentImpl.this.this$0.providerLayoutInflaterProvider, SysUIComponentImpl.this.this$0.screenLifecycleProvider, SysUIComponentImpl.this.userSwitcherControllerProvider, SysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIComponentImpl.this.dozeParametersProvider, SysUIComponentImpl.this.screenOffAnimationControllerProvider));
            }

            public KeyguardUserSwitcherController getKeyguardUserSwitcherController() {
                return this.keyguardUserSwitcherControllerProvider.get();
            }
        }

        public final class KeyguardStatusBarViewComponentFactory implements KeyguardStatusBarViewComponent.Factory {
            public KeyguardStatusBarViewComponentFactory() {
            }

            public KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider) {
                Preconditions.checkNotNull(keyguardStatusBarView);
                Preconditions.checkNotNull(notificationPanelViewStateProvider);
                return new KeyguardStatusBarViewComponentImpl(keyguardStatusBarView, notificationPanelViewStateProvider);
            }
        }

        public final class KeyguardStatusBarViewComponentImpl implements KeyguardStatusBarViewComponent {
            public Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
            public Provider<BatteryMeterView> getBatteryMeterViewProvider;
            public Provider<CarrierText> getCarrierTextProvider;
            public Provider<StatusBarUserSwitcherContainer> getUserSwitcherContainerProvider;
            public final NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider;
            public Provider<StatusBarUserSwitcherControllerImpl> statusBarUserSwitcherControllerImplProvider;
            public final KeyguardStatusBarView view;
            public Provider<KeyguardStatusBarView> viewProvider;

            public KeyguardStatusBarViewComponentImpl(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider2) {
                this.view = keyguardStatusBarView;
                this.notificationPanelViewStateProvider = notificationPanelViewStateProvider2;
                initialize(keyguardStatusBarView, notificationPanelViewStateProvider2);
            }

            public final CarrierTextManager.Builder carrierTextManagerBuilder() {
                return new CarrierTextManager.Builder(SysUIComponentImpl.this.this$0.context, SysUIComponentImpl.this.this$0.mainResources(), (WifiManager) SysUIComponentImpl.this.this$0.provideWifiManagerProvider.get(), (TelephonyManager) SysUIComponentImpl.this.this$0.provideTelephonyManagerProvider.get(), (TelephonyListenerManager) SysUIComponentImpl.this.telephonyListenerManagerProvider.get(), (WakefulnessLifecycle) SysUIComponentImpl.this.wakefulnessLifecycleProvider.get(), (Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get(), (Executor) SysUIComponentImpl.this.provideBackgroundExecutorProvider.get(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            public final CarrierTextController carrierTextController() {
                return new CarrierTextController(this.getCarrierTextProvider.get(), carrierTextManagerBuilder(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            public final BatteryMeterViewController batteryMeterViewController() {
                return new BatteryMeterViewController(this.getBatteryMeterViewProvider.get(), (ConfigurationController) SysUIComponentImpl.this.configurationControllerImplProvider.get(), (TunerService) SysUIComponentImpl.this.tunerServiceImplProvider.get(), (BroadcastDispatcher) SysUIComponentImpl.this.broadcastDispatcherProvider.get(), SysUIComponentImpl.this.this$0.mainHandler(), (ContentResolver) SysUIComponentImpl.this.this$0.provideContentResolverProvider.get(), (BatteryController) SysUIComponentImpl.this.provideBatteryControllerProvider.get());
            }

            public final void initialize(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider2) {
                Factory create = InstanceFactory.create(keyguardStatusBarView);
                this.viewProvider = create;
                this.getCarrierTextProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetCarrierTextFactory.create(create));
                this.getBatteryMeterViewProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetBatteryMeterViewFactory.create(this.viewProvider));
                Provider<StatusBarUserSwitcherContainer> provider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory.create(this.viewProvider));
                this.getUserSwitcherContainerProvider = provider;
                StatusBarUserSwitcherControllerImpl_Factory create2 = StatusBarUserSwitcherControllerImpl_Factory.create(provider, SysUIComponentImpl.this.statusBarUserInfoTrackerProvider, SysUIComponentImpl.this.statusBarUserSwitcherFeatureControllerProvider, SysUIComponentImpl.this.userSwitchDialogControllerProvider, SysUIComponentImpl.this.featureFlagsDebugProvider, SysUIComponentImpl.this.provideActivityStarterProvider, SysUIComponentImpl.this.falsingManagerProxyProvider);
                this.statusBarUserSwitcherControllerImplProvider = create2;
                this.bindStatusBarUserSwitcherControllerProvider = DoubleCheck.provider(create2);
            }

            public KeyguardStatusBarViewController getKeyguardStatusBarViewController() {
                return new KeyguardStatusBarViewController(this.view, carrierTextController(), (ConfigurationController) SysUIComponentImpl.this.configurationControllerImplProvider.get(), (SystemStatusAnimationScheduler) SysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.get(), (BatteryController) SysUIComponentImpl.this.provideBatteryControllerProvider.get(), (UserInfoController) SysUIComponentImpl.this.userInfoControllerImplProvider.get(), (StatusBarIconController) SysUIComponentImpl.this.statusBarIconControllerImplProvider.get(), (StatusBarIconController.TintedIconManager.Factory) SysUIComponentImpl.this.factoryProvider11.get(), batteryMeterViewController(), this.notificationPanelViewStateProvider, (KeyguardStateController) SysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (KeyguardBypassController) SysUIComponentImpl.this.keyguardBypassControllerProvider.get(), (KeyguardUpdateMonitor) SysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (BiometricUnlockController) SysUIComponentImpl.this.biometricUnlockControllerProvider.get(), (SysuiStatusBarStateController) SysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (StatusBarContentInsetsProvider) SysUIComponentImpl.this.statusBarContentInsetsProvider.get(), (UserManager) SysUIComponentImpl.this.this$0.provideUserManagerProvider.get(), (StatusBarUserSwitcherFeatureController) SysUIComponentImpl.this.statusBarUserSwitcherFeatureControllerProvider.get(), this.bindStatusBarUserSwitcherControllerProvider.get(), (StatusBarUserInfoTracker) SysUIComponentImpl.this.statusBarUserInfoTrackerProvider.get(), (SecureSettings) SysUIComponentImpl.this.secureSettingsImpl(), (Executor) SysUIComponentImpl.this.this$0.provideMainExecutorProvider.get());
            }
        }
    }
}
