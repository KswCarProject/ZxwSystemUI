package com.android.systemui.statusbar.phone;

import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.UserManager;
import android.telecom.TelecomManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.time.DateFormatUtil;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class PhoneStatusBarPolicy_Factory implements Factory<PhoneStatusBarPolicy> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<BluetoothController> bluetoothControllerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CastController> castControllerProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<DataSaverController> dataSaverControllerProvider;
    public final Provider<DateFormatUtil> dateFormatUtilProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<Integer> displayIdProvider;
    public final Provider<HotspotController> hotspotControllerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<StatusBarIconController> iconControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<LocationController> locationControllerProvider;
    public final Provider<NextAlarmController> nextAlarmControllerProvider;
    public final Provider<PrivacyItemController> privacyItemControllerProvider;
    public final Provider<PrivacyLogger> privacyLoggerProvider;
    public final Provider<RecordingController> recordingControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<RingerModeTracker> ringerModeTrackerProvider;
    public final Provider<RotationLockController> rotationLockControllerProvider;
    public final Provider<SensorPrivacyController> sensorPrivacyControllerProvider;
    public final Provider<SharedPreferences> sharedPreferencesProvider;
    public final Provider<TelecomManager> telecomManagerProvider;
    public final Provider<Executor> uiBgExecutorProvider;
    public final Provider<UserInfoController> userInfoControllerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<ZenModeController> zenModeControllerProvider;

    public PhoneStatusBarPolicy_Factory(Provider<StatusBarIconController> provider, Provider<CommandQueue> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<Resources> provider5, Provider<CastController> provider6, Provider<HotspotController> provider7, Provider<BluetoothController> provider8, Provider<NextAlarmController> provider9, Provider<UserInfoController> provider10, Provider<RotationLockController> provider11, Provider<DataSaverController> provider12, Provider<ZenModeController> provider13, Provider<DeviceProvisionedController> provider14, Provider<KeyguardStateController> provider15, Provider<LocationController> provider16, Provider<SensorPrivacyController> provider17, Provider<IActivityManager> provider18, Provider<AlarmManager> provider19, Provider<UserManager> provider20, Provider<DevicePolicyManager> provider21, Provider<RecordingController> provider22, Provider<TelecomManager> provider23, Provider<Integer> provider24, Provider<SharedPreferences> provider25, Provider<DateFormatUtil> provider26, Provider<RingerModeTracker> provider27, Provider<PrivacyItemController> provider28, Provider<PrivacyLogger> provider29) {
        this.iconControllerProvider = provider;
        this.commandQueueProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.resourcesProvider = provider5;
        this.castControllerProvider = provider6;
        this.hotspotControllerProvider = provider7;
        this.bluetoothControllerProvider = provider8;
        this.nextAlarmControllerProvider = provider9;
        this.userInfoControllerProvider = provider10;
        this.rotationLockControllerProvider = provider11;
        this.dataSaverControllerProvider = provider12;
        this.zenModeControllerProvider = provider13;
        this.deviceProvisionedControllerProvider = provider14;
        this.keyguardStateControllerProvider = provider15;
        this.locationControllerProvider = provider16;
        this.sensorPrivacyControllerProvider = provider17;
        this.iActivityManagerProvider = provider18;
        this.alarmManagerProvider = provider19;
        this.userManagerProvider = provider20;
        this.devicePolicyManagerProvider = provider21;
        this.recordingControllerProvider = provider22;
        this.telecomManagerProvider = provider23;
        this.displayIdProvider = provider24;
        this.sharedPreferencesProvider = provider25;
        this.dateFormatUtilProvider = provider26;
        this.ringerModeTrackerProvider = provider27;
        this.privacyItemControllerProvider = provider28;
        this.privacyLoggerProvider = provider29;
    }

    public PhoneStatusBarPolicy get() {
        return newInstance(this.iconControllerProvider.get(), this.commandQueueProvider.get(), this.broadcastDispatcherProvider.get(), this.uiBgExecutorProvider.get(), this.resourcesProvider.get(), this.castControllerProvider.get(), this.hotspotControllerProvider.get(), this.bluetoothControllerProvider.get(), this.nextAlarmControllerProvider.get(), this.userInfoControllerProvider.get(), this.rotationLockControllerProvider.get(), this.dataSaverControllerProvider.get(), this.zenModeControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.locationControllerProvider.get(), this.sensorPrivacyControllerProvider.get(), this.iActivityManagerProvider.get(), this.alarmManagerProvider.get(), this.userManagerProvider.get(), this.devicePolicyManagerProvider.get(), this.recordingControllerProvider.get(), this.telecomManagerProvider.get(), this.displayIdProvider.get().intValue(), this.sharedPreferencesProvider.get(), this.dateFormatUtilProvider.get(), this.ringerModeTrackerProvider.get(), this.privacyItemControllerProvider.get(), this.privacyLoggerProvider.get());
    }

    public static PhoneStatusBarPolicy_Factory create(Provider<StatusBarIconController> provider, Provider<CommandQueue> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<Resources> provider5, Provider<CastController> provider6, Provider<HotspotController> provider7, Provider<BluetoothController> provider8, Provider<NextAlarmController> provider9, Provider<UserInfoController> provider10, Provider<RotationLockController> provider11, Provider<DataSaverController> provider12, Provider<ZenModeController> provider13, Provider<DeviceProvisionedController> provider14, Provider<KeyguardStateController> provider15, Provider<LocationController> provider16, Provider<SensorPrivacyController> provider17, Provider<IActivityManager> provider18, Provider<AlarmManager> provider19, Provider<UserManager> provider20, Provider<DevicePolicyManager> provider21, Provider<RecordingController> provider22, Provider<TelecomManager> provider23, Provider<Integer> provider24, Provider<SharedPreferences> provider25, Provider<DateFormatUtil> provider26, Provider<RingerModeTracker> provider27, Provider<PrivacyItemController> provider28, Provider<PrivacyLogger> provider29) {
        return new PhoneStatusBarPolicy_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29);
    }

    public static PhoneStatusBarPolicy newInstance(StatusBarIconController statusBarIconController, CommandQueue commandQueue, BroadcastDispatcher broadcastDispatcher, Executor executor, Resources resources, CastController castController, HotspotController hotspotController, BluetoothController bluetoothController, NextAlarmController nextAlarmController, UserInfoController userInfoController, RotationLockController rotationLockController, DataSaverController dataSaverController, ZenModeController zenModeController, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, LocationController locationController, SensorPrivacyController sensorPrivacyController, IActivityManager iActivityManager, AlarmManager alarmManager, UserManager userManager, DevicePolicyManager devicePolicyManager, RecordingController recordingController, TelecomManager telecomManager, int i, SharedPreferences sharedPreferences, DateFormatUtil dateFormatUtil, RingerModeTracker ringerModeTracker, PrivacyItemController privacyItemController, PrivacyLogger privacyLogger) {
        return new PhoneStatusBarPolicy(statusBarIconController, commandQueue, broadcastDispatcher, executor, resources, castController, hotspotController, bluetoothController, nextAlarmController, userInfoController, rotationLockController, dataSaverController, zenModeController, deviceProvisionedController, keyguardStateController, locationController, sensorPrivacyController, iActivityManager, alarmManager, userManager, devicePolicyManager, recordingController, telecomManager, i, sharedPreferences, dateFormatUtil, ringerModeTracker, privacyItemController, privacyLogger);
    }
}
