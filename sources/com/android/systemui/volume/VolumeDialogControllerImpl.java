package com.android.systemui.volume;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.IAudioService;
import android.media.IVolumeController;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.VolumePolicy;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.service.notification.Condition;
import android.service.notification.ZenModeConfig;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.GuardedBy;
import com.android.settingslib.volume.MediaSessions;
import com.android.settingslib.volume.Util;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.util.RingerModeLiveData;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.concurrency.ThreadFactory;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class VolumeDialogControllerImpl implements VolumeDialogController, Dumpable {
    public static final AudioAttributes SONIFICIATION_VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public static final ArrayMap<Integer, Integer> STREAMS;
    public static final String TAG = Util.logTag(VolumeDialogControllerImpl.class);
    public final ActivityManager mActivityManager;
    public final AudioManager mAudio;
    public final IAudioService mAudioService;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public C mCallbacks = new C();
    public final CaptioningManager mCaptioningManager;
    public final Context mContext;
    public boolean mDeviceInteractive = true;
    public final boolean mHasVibrator;
    public final KeyguardManager mKeyguardManager;
    public long mLastToggledRingerOn;
    public final MediaSessions mMediaSessions;
    public final MediaSessionsCallbacks mMediaSessionsCallbacksW;
    public final NotificationManager mNoMan;
    public final SettingObserver mObserver;
    public final PackageManager mPackageManager;
    public final Receiver mReceiver;
    public final RingerModeObservers mRingerModeObservers;
    public final MediaRouter2Manager mRouter2Manager;
    public boolean mShowA11yStream;
    public boolean mShowSafetyWarning;
    public boolean mShowVolumeDialog;
    public final VolumeDialogController.State mState = new VolumeDialogController.State();
    @GuardedBy({"this"})
    public UserActivityListener mUserActivityListener;
    public final VibratorHelper mVibrator;
    public final VC mVolumeController;
    public VolumePolicy mVolumePolicy;
    public final WakefulnessLifecycle.Observer mWakefullnessLifecycleObserver;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final W mWorker;
    public final Looper mWorkerLooper;

    public interface UserActivityListener {
        void onUserActivity();
    }

    public static boolean isLogWorthy(int i) {
        return i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 6;
    }

    public static boolean isRinger(int i) {
        return i == 2 || i == 5;
    }

    static {
        ArrayMap<Integer, Integer> arrayMap = new ArrayMap<>();
        STREAMS = arrayMap;
        arrayMap.put(4, Integer.valueOf(R$string.stream_alarm));
        arrayMap.put(6, Integer.valueOf(R$string.stream_bluetooth_sco));
        arrayMap.put(8, Integer.valueOf(R$string.stream_dtmf));
        arrayMap.put(3, Integer.valueOf(R$string.stream_music));
        arrayMap.put(10, Integer.valueOf(R$string.stream_accessibility));
        arrayMap.put(5, Integer.valueOf(R$string.stream_notification));
        arrayMap.put(2, Integer.valueOf(R$string.stream_ring));
        arrayMap.put(1, Integer.valueOf(R$string.stream_system));
        arrayMap.put(7, Integer.valueOf(R$string.stream_system_enforced));
        arrayMap.put(9, Integer.valueOf(R$string.stream_tts));
        arrayMap.put(0, Integer.valueOf(R$string.stream_voice_call));
    }

    public VolumeDialogControllerImpl(Context context, BroadcastDispatcher broadcastDispatcher, RingerModeTracker ringerModeTracker, ThreadFactory threadFactory, AudioManager audioManager, NotificationManager notificationManager, VibratorHelper vibratorHelper, IAudioService iAudioService, AccessibilityManager accessibilityManager, PackageManager packageManager, WakefulnessLifecycle wakefulnessLifecycle, CaptioningManager captioningManager, KeyguardManager keyguardManager, ActivityManager activityManager) {
        WakefulnessLifecycle wakefulnessLifecycle2 = wakefulnessLifecycle;
        Receiver receiver = new Receiver();
        this.mReceiver = receiver;
        VC vc = new VC();
        this.mVolumeController = vc;
        AnonymousClass1 r3 = new WakefulnessLifecycle.Observer() {
            public void onStartedWakingUp() {
                VolumeDialogControllerImpl.this.mDeviceInteractive = true;
            }

            public void onFinishedGoingToSleep() {
                VolumeDialogControllerImpl.this.mDeviceInteractive = false;
            }
        };
        this.mWakefullnessLifecycleObserver = r3;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mPackageManager = packageManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle2;
        Events.writeEvent(5, new Object[0]);
        ThreadFactory threadFactory2 = threadFactory;
        Looper buildLooperOnNewThread = threadFactory.buildLooperOnNewThread(VolumeDialogControllerImpl.class.getSimpleName());
        this.mWorkerLooper = buildLooperOnNewThread;
        W w = new W(buildLooperOnNewThread);
        this.mWorker = w;
        this.mRouter2Manager = MediaRouter2Manager.getInstance(applicationContext);
        MediaSessionsCallbacks mediaSessionsCallbacks = new MediaSessionsCallbacks(applicationContext);
        this.mMediaSessionsCallbacksW = mediaSessionsCallbacks;
        this.mMediaSessions = createMediaSessions(applicationContext, buildLooperOnNewThread, mediaSessionsCallbacks);
        this.mAudio = audioManager;
        this.mNoMan = notificationManager;
        SettingObserver settingObserver = new SettingObserver(w);
        this.mObserver = settingObserver;
        RingerModeObservers ringerModeObservers = new RingerModeObservers((RingerModeLiveData) ringerModeTracker.getRingerMode(), (RingerModeLiveData) ringerModeTracker.getRingerModeInternal());
        this.mRingerModeObservers = ringerModeObservers;
        ringerModeObservers.init();
        this.mBroadcastDispatcher = broadcastDispatcher;
        settingObserver.init();
        receiver.init();
        this.mVibrator = vibratorHelper;
        this.mHasVibrator = vibratorHelper.hasVibrator();
        this.mAudioService = iAudioService;
        this.mCaptioningManager = captioningManager;
        this.mKeyguardManager = keyguardManager;
        this.mActivityManager = activityManager;
        vc.setA11yMode(accessibilityManager.isAccessibilityVolumeStreamActive() ? 1 : 0);
        wakefulnessLifecycle2.addObserver(r3);
    }

    public AudioManager getAudioManager() {
        return this.mAudio;
    }

    public void dismiss() {
        this.mCallbacks.onDismissRequested(2);
    }

    public void setVolumeController() {
        try {
            this.mAudio.setVolumeController(this.mVolumeController);
        } catch (SecurityException e) {
            Log.w(TAG, "Unable to set the volume controller", e);
        }
    }

    public void setAudioManagerStreamVolume(int i, int i2, int i3) {
        this.mAudio.setStreamVolume(i, i2, i3);
    }

    public int getAudioManagerStreamVolume(int i) {
        return this.mAudio.getLastAudibleStreamVolume(i);
    }

    public int getAudioManagerStreamMaxVolume(int i) {
        return this.mAudio.getStreamMaxVolume(i);
    }

    public int getAudioManagerStreamMinVolume(int i) {
        return this.mAudio.getStreamMinVolumeInt(i);
    }

    public void register() {
        setVolumeController();
        setVolumePolicy(this.mVolumePolicy);
        showDndTile();
        try {
            this.mMediaSessions.init();
        } catch (SecurityException e) {
            Log.w(TAG, "No access to media sessions", e);
        }
    }

    public void setVolumePolicy(VolumePolicy volumePolicy) {
        this.mVolumePolicy = volumePolicy;
        if (volumePolicy != null) {
            try {
                this.mAudio.setVolumePolicy(volumePolicy);
            } catch (NoSuchMethodError unused) {
                Log.w(TAG, "No volume policy api");
            }
        }
    }

    public MediaSessions createMediaSessions(Context context, Looper looper, MediaSessions.Callbacks callbacks) {
        return new MediaSessions(context, looper, callbacks);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(VolumeDialogControllerImpl.class.getSimpleName() + " state:");
        printWriter.print("  mVolumePolicy: ");
        printWriter.println(this.mVolumePolicy);
        printWriter.print("  mState: ");
        printWriter.println(this.mState.toString(4));
        printWriter.print("  mHasVibrator: ");
        printWriter.println(this.mHasVibrator);
        synchronized (this.mMediaSessionsCallbacksW.mRemoteStreams) {
            printWriter.print("  mRemoteStreams: ");
            printWriter.println(this.mMediaSessionsCallbacksW.mRemoteStreams.values());
        }
        printWriter.print("  mShowA11yStream: ");
        printWriter.println(this.mShowA11yStream);
        printWriter.println();
        this.mMediaSessions.dump(printWriter);
    }

    public void addCallback(VolumeDialogController.Callbacks callbacks, Handler handler) {
        this.mCallbacks.add(callbacks, handler);
        callbacks.onAccessibilityModeChanged(Boolean.valueOf(this.mShowA11yStream));
    }

    public void setUserActivityListener(UserActivityListener userActivityListener) {
        synchronized (this) {
            this.mUserActivityListener = userActivityListener;
        }
    }

    public void removeCallback(VolumeDialogController.Callbacks callbacks) {
        this.mCallbacks.remove(callbacks);
    }

    public void getState() {
        this.mWorker.sendEmptyMessage(3);
    }

    public boolean areCaptionsEnabled() {
        return this.mCaptioningManager.isSystemAudioCaptioningEnabled();
    }

    public void setCaptionsEnabled(boolean z) {
        this.mCaptioningManager.setSystemAudioCaptioningEnabled(z);
    }

    public void getCaptionsComponentState(boolean z) {
        this.mWorker.obtainMessage(16, Boolean.valueOf(z)).sendToTarget();
    }

    public void notifyVisible(boolean z) {
        this.mWorker.obtainMessage(12, z ? 1 : 0, 0).sendToTarget();
    }

    public void userActivity() {
        this.mWorker.removeMessages(13);
        this.mWorker.sendEmptyMessage(13);
    }

    public void setRingerMode(int i, boolean z) {
        this.mWorker.obtainMessage(4, i, z ? 1 : 0).sendToTarget();
    }

    public void setStreamVolume(int i, int i2) {
        this.mWorker.obtainMessage(10, i, i2).sendToTarget();
    }

    public void setActiveStream(int i) {
        this.mWorker.obtainMessage(11, i, 0).sendToTarget();
    }

    public void setEnableDialogs(boolean z, boolean z2) {
        this.mShowVolumeDialog = z;
        this.mShowSafetyWarning = z2;
    }

    public void scheduleTouchFeedback() {
        this.mLastToggledRingerOn = System.currentTimeMillis();
    }

    public final void playTouchFeedback() {
        if (System.currentTimeMillis() - this.mLastToggledRingerOn < 1000) {
            try {
                this.mAudioService.playSoundEffect(5, -2);
            } catch (RemoteException unused) {
            }
        }
    }

    public void vibrate(VibrationEffect vibrationEffect) {
        this.mVibrator.vibrate(vibrationEffect, SONIFICIATION_VIBRATION_ATTRIBUTES);
    }

    public boolean hasVibrator() {
        return this.mHasVibrator;
    }

    public final void onNotifyVisibleW(boolean z) {
        this.mAudio.notifyVolumeControllerVisible(this.mVolumeController, z);
        if (!z && updateActiveStreamW(-1)) {
            this.mCallbacks.onStateChanged(this.mState);
        }
    }

    public final void onUserActivityW() {
        synchronized (this) {
            UserActivityListener userActivityListener = this.mUserActivityListener;
            if (userActivityListener != null) {
                userActivityListener.onUserActivity();
            }
        }
    }

    public final void onShowSafetyWarningW(int i) {
        if (this.mShowSafetyWarning) {
            this.mCallbacks.onShowSafetyWarning(i);
        }
    }

    public final void onGetCaptionsComponentStateW(boolean z) {
        this.mCallbacks.onCaptionComponentStateChanged(Boolean.valueOf(this.mCaptioningManager.isSystemAudioCaptioningUiEnabled()), Boolean.valueOf(z));
    }

    public final void onAccessibilityModeChanged(Boolean bool) {
        this.mCallbacks.onAccessibilityModeChanged(bool);
    }

    public final boolean checkRoutedToBluetoothW(int i) {
        if (i != 3) {
            return false;
        }
        return false | updateStreamRoutedToBluetoothW(i, (this.mAudio.getDevicesForStream(3) & 536871808) != 0);
    }

    public final boolean shouldShowUI(int i) {
        int wakefulness = this.mWakefulnessLifecycle.getWakefulness();
        if (wakefulness == 0 || wakefulness == 3 || !this.mDeviceInteractive || (i & 1) == 0 || !this.mShowVolumeDialog) {
            return false;
        }
        return true;
    }

    public boolean onVolumeChangedW(int i, int i2) {
        boolean shouldShowUI = shouldShowUI(i2);
        boolean z = (i2 & 4096) != 0;
        boolean z2 = (i2 & 2048) != 0;
        boolean z3 = (i2 & 128) != 0;
        boolean updateActiveStreamW = shouldShowUI ? updateActiveStreamW(i) | false : false;
        int audioManagerStreamVolume = getAudioManagerStreamVolume(i);
        boolean updateStreamLevelW = updateActiveStreamW | updateStreamLevelW(i, audioManagerStreamVolume) | checkRoutedToBluetoothW(shouldShowUI ? 3 : i);
        if (updateStreamLevelW) {
            this.mCallbacks.onStateChanged(this.mState);
        }
        if (shouldShowUI) {
            onShowRequestedW(1);
        }
        if (z2) {
            this.mCallbacks.onShowVibrateHint();
        }
        if (z3) {
            this.mCallbacks.onShowSilentHint();
        }
        if (updateStreamLevelW && z) {
            Events.writeEvent(4, Integer.valueOf(i), Integer.valueOf(audioManagerStreamVolume));
        }
        return updateStreamLevelW;
    }

    public final boolean updateActiveStreamW(int i) {
        VolumeDialogController.State state = this.mState;
        if (i == state.activeStream) {
            return false;
        }
        state.activeStream = i;
        Events.writeEvent(2, Integer.valueOf(i));
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "updateActiveStreamW " + i);
        }
        if (i >= 100) {
            i = -1;
        }
        if (D.BUG) {
            String str2 = TAG;
            Log.d(str2, "forceVolumeControlStream " + i);
        }
        this.mAudio.forceVolumeControlStream(i);
        return true;
    }

    public final VolumeDialogController.StreamState streamStateW(int i) {
        VolumeDialogController.StreamState streamState = this.mState.states.get(i);
        if (streamState != null) {
            return streamState;
        }
        VolumeDialogController.StreamState streamState2 = new VolumeDialogController.StreamState();
        this.mState.states.put(i, streamState2);
        return streamState2;
    }

    public final void onGetStateW() {
        for (Integer intValue : STREAMS.keySet()) {
            int intValue2 = intValue.intValue();
            updateStreamLevelW(intValue2, getAudioManagerStreamVolume(intValue2));
            streamStateW(intValue2).levelMin = getAudioManagerStreamMinVolume(intValue2);
            streamStateW(intValue2).levelMax = Math.max(1, getAudioManagerStreamMaxVolume(intValue2));
            updateStreamMuteW(intValue2, this.mAudio.isStreamMute(intValue2));
            VolumeDialogController.StreamState streamStateW = streamStateW(intValue2);
            streamStateW.muteSupported = this.mAudio.isStreamAffectedByMute(intValue2);
            streamStateW.name = STREAMS.get(Integer.valueOf(intValue2)).intValue();
            checkRoutedToBluetoothW(intValue2);
        }
        updateRingerModeExternalW(this.mRingerModeObservers.mRingerMode.getValue().intValue());
        updateZenModeW();
        updateZenConfig();
        updateEffectsSuppressorW(this.mNoMan.getEffectsSuppressor());
        this.mCallbacks.onStateChanged(this.mState);
    }

    public final boolean updateStreamRoutedToBluetoothW(int i, boolean z) {
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        if (streamStateW.routedToBluetooth == z) {
            return false;
        }
        streamStateW.routedToBluetooth = z;
        if (!D.BUG) {
            return true;
        }
        String str = TAG;
        Log.d(str, "updateStreamRoutedToBluetoothW stream=" + i + " routedToBluetooth=" + z);
        return true;
    }

    public final boolean updateStreamLevelW(int i, int i2) {
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        if (streamStateW.level == i2) {
            return false;
        }
        streamStateW.level = i2;
        if (isLogWorthy(i)) {
            Events.writeEvent(10, Integer.valueOf(i), Integer.valueOf(i2));
        }
        return true;
    }

    public final boolean updateStreamMuteW(int i, boolean z) {
        VolumeDialogController.StreamState streamStateW = streamStateW(i);
        if (streamStateW.muted == z) {
            return false;
        }
        streamStateW.muted = z;
        if (isLogWorthy(i)) {
            Events.writeEvent(15, Integer.valueOf(i), Boolean.valueOf(z));
        }
        if (z && isRinger(i)) {
            updateRingerModeInternalW(this.mRingerModeObservers.mRingerModeInternal.getValue().intValue());
        }
        return true;
    }

    public final boolean updateEffectsSuppressorW(ComponentName componentName) {
        if (Objects.equals(this.mState.effectsSuppressor, componentName)) {
            return false;
        }
        VolumeDialogController.State state = this.mState;
        state.effectsSuppressor = componentName;
        state.effectsSuppressorName = getApplicationName(this.mPackageManager, componentName);
        VolumeDialogController.State state2 = this.mState;
        Events.writeEvent(14, state2.effectsSuppressor, state2.effectsSuppressorName);
        return true;
    }

    public static String getApplicationName(PackageManager packageManager, ComponentName componentName) {
        if (componentName == null) {
            return null;
        }
        String packageName = componentName.getPackageName();
        try {
            String trim = Objects.toString(packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager), "").trim();
            return trim.length() > 0 ? trim : packageName;
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    public final boolean updateZenModeW() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", 0);
        VolumeDialogController.State state = this.mState;
        if (state.zenMode == i) {
            return false;
        }
        state.zenMode = i;
        Events.writeEvent(13, Integer.valueOf(i));
        return true;
    }

    public final boolean updateZenConfig() {
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        int i = consolidatedNotificationPolicy.priorityCategories;
        boolean z = (i & 32) == 0;
        boolean z2 = (i & 64) == 0;
        boolean z3 = (i & 128) == 0;
        boolean areAllPriorityOnlyRingerSoundsMuted = ZenModeConfig.areAllPriorityOnlyRingerSoundsMuted(consolidatedNotificationPolicy);
        VolumeDialogController.State state = this.mState;
        if (state.disallowAlarms == z && state.disallowMedia == z2 && state.disallowRinger == areAllPriorityOnlyRingerSoundsMuted && state.disallowSystem == z3) {
            return false;
        }
        state.disallowAlarms = z;
        state.disallowMedia = z2;
        state.disallowSystem = z3;
        state.disallowRinger = areAllPriorityOnlyRingerSoundsMuted;
        Events.writeEvent(17, "disallowAlarms=" + z + " disallowMedia=" + z2 + " disallowSystem=" + z3 + " disallowRinger=" + areAllPriorityOnlyRingerSoundsMuted);
        return true;
    }

    public final boolean updateRingerModeExternalW(int i) {
        VolumeDialogController.State state = this.mState;
        if (i == state.ringerModeExternal) {
            return false;
        }
        state.ringerModeExternal = i;
        Events.writeEvent(12, Integer.valueOf(i));
        return true;
    }

    public final boolean updateRingerModeInternalW(int i) {
        VolumeDialogController.State state = this.mState;
        if (i == state.ringerModeInternal) {
            return false;
        }
        state.ringerModeInternal = i;
        Events.writeEvent(11, Integer.valueOf(i));
        if (this.mState.ringerModeInternal == 2) {
            playTouchFeedback();
        }
        return true;
    }

    public final void onShowRequestedW(int i) {
        this.mCallbacks.onShowRequested(i, this.mKeyguardManager.isKeyguardLocked(), this.mActivityManager.getLockTaskModeState());
    }

    public final void onSetRingerModeW(int i, boolean z) {
        if (z) {
            this.mAudio.setRingerMode(i);
        } else {
            this.mAudio.setRingerModeInternal(i);
        }
    }

    public final void onSetStreamMuteW(int i, boolean z) {
        this.mAudio.adjustStreamVolume(i, z ? -100 : 100, 0);
    }

    public final void onSetStreamVolumeW(int i, int i2) {
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "onSetStreamVolume " + i + " level=" + i2);
        }
        if (i >= 100) {
            this.mMediaSessionsCallbacksW.setStreamVolume(i, i2);
        } else {
            setAudioManagerStreamVolume(i, i2, 0);
        }
    }

    public final void onSetActiveStreamW(int i) {
        if (updateActiveStreamW(i)) {
            this.mCallbacks.onStateChanged(this.mState);
        }
    }

    public final void onSetExitConditionW(Condition condition) {
        this.mNoMan.setZenMode(this.mState.zenMode, condition != null ? condition.id : null, TAG);
    }

    public final void onSetZenModeW(int i) {
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "onSetZenModeW " + i);
        }
        this.mNoMan.setZenMode(i, (Uri) null, TAG);
    }

    public final void onDismissRequestedW(int i) {
        this.mCallbacks.onDismissRequested(i);
    }

    public void showDndTile() {
        if (D.BUG) {
            Log.d(TAG, "showDndTile");
        }
        DndTile.setVisible(this.mContext, true);
    }

    public final class VC extends IVolumeController.Stub {
        public final String TAG;

        public VC() {
            this.TAG = VolumeDialogControllerImpl.TAG + ".VC";
        }

        public void displaySafeVolumeWarning(int i) throws RemoteException {
            if (D.BUG) {
                String str = this.TAG;
                Log.d(str, "displaySafeVolumeWarning " + Util.audioManagerFlagsToString(i));
            }
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(14, i, 0).sendToTarget();
        }

        public void volumeChanged(int i, int i2) throws RemoteException {
            if (D.BUG) {
                String str = this.TAG;
                Log.d(str, "volumeChanged " + AudioSystem.streamToString(i) + " " + Util.audioManagerFlagsToString(i2));
            }
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(1, i, i2).sendToTarget();
        }

        public void masterMuteChanged(int i) throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "masterMuteChanged");
            }
        }

        public void setLayoutDirection(int i) throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "setLayoutDirection");
            }
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(8, i, 0).sendToTarget();
        }

        public void dismiss() throws RemoteException {
            if (D.BUG) {
                Log.d(this.TAG, "dismiss requested");
            }
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(2, 2, 0).sendToTarget();
            VolumeDialogControllerImpl.this.mWorker.sendEmptyMessage(2);
        }

        public void setA11yMode(int i) {
            if (D.BUG) {
                String str = this.TAG;
                Log.d(str, "setA11yMode to " + i);
            }
            if (i == 0) {
                VolumeDialogControllerImpl.this.mShowA11yStream = false;
            } else if (i != 1) {
                String str2 = this.TAG;
                Log.e(str2, "Invalid accessibility mode " + i);
            } else {
                VolumeDialogControllerImpl.this.mShowA11yStream = true;
            }
            VolumeDialogControllerImpl.this.mWorker.obtainMessage(15, Boolean.valueOf(VolumeDialogControllerImpl.this.mShowA11yStream)).sendToTarget();
        }
    }

    public final class W extends Handler {
        public W(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            switch (message.what) {
                case 1:
                    VolumeDialogControllerImpl.this.onVolumeChangedW(message.arg1, message.arg2);
                    return;
                case 2:
                    VolumeDialogControllerImpl.this.onDismissRequestedW(message.arg1);
                    return;
                case 3:
                    VolumeDialogControllerImpl.this.onGetStateW();
                    return;
                case 4:
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    int i = message.arg1;
                    if (message.arg2 == 0) {
                        z = false;
                    }
                    volumeDialogControllerImpl.onSetRingerModeW(i, z);
                    return;
                case 5:
                    VolumeDialogControllerImpl.this.onSetZenModeW(message.arg1);
                    return;
                case 6:
                    VolumeDialogControllerImpl.this.onSetExitConditionW((Condition) message.obj);
                    return;
                case 7:
                    VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                    int i2 = message.arg1;
                    if (message.arg2 == 0) {
                        z = false;
                    }
                    volumeDialogControllerImpl2.onSetStreamMuteW(i2, z);
                    return;
                case 8:
                    VolumeDialogControllerImpl.this.mCallbacks.onLayoutDirectionChanged(message.arg1);
                    return;
                case 9:
                    VolumeDialogControllerImpl.this.mCallbacks.onConfigurationChanged();
                    return;
                case 10:
                    VolumeDialogControllerImpl.this.onSetStreamVolumeW(message.arg1, message.arg2);
                    return;
                case 11:
                    VolumeDialogControllerImpl.this.onSetActiveStreamW(message.arg1);
                    return;
                case 12:
                    VolumeDialogControllerImpl volumeDialogControllerImpl3 = VolumeDialogControllerImpl.this;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    volumeDialogControllerImpl3.onNotifyVisibleW(z);
                    return;
                case 13:
                    VolumeDialogControllerImpl.this.onUserActivityW();
                    return;
                case 14:
                    VolumeDialogControllerImpl.this.onShowSafetyWarningW(message.arg1);
                    return;
                case 15:
                    VolumeDialogControllerImpl.this.onAccessibilityModeChanged((Boolean) message.obj);
                    return;
                case 16:
                    VolumeDialogControllerImpl.this.onGetCaptionsComponentStateW(((Boolean) message.obj).booleanValue());
                    return;
                default:
                    return;
            }
        }
    }

    public static class C implements VolumeDialogController.Callbacks {
        public final Map<VolumeDialogController.Callbacks, Handler> mCallbackMap = new ConcurrentHashMap();

        public void add(VolumeDialogController.Callbacks callbacks, Handler handler) {
            if (callbacks == null || handler == null) {
                throw new IllegalArgumentException();
            }
            this.mCallbackMap.put(callbacks, handler);
        }

        public void remove(VolumeDialogController.Callbacks callbacks) {
            this.mCallbackMap.remove(callbacks);
        }

        public void onShowRequested(int i, boolean z, int i2) {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                final int i3 = i;
                final boolean z2 = z;
                final int i4 = i2;
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onShowRequested(i3, z2, i4);
                    }
                });
            }
        }

        public void onDismissRequested(final int i) {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onDismissRequested(i);
                    }
                });
            }
        }

        public void onStateChanged(VolumeDialogController.State state) {
            long currentTimeMillis = System.currentTimeMillis();
            final VolumeDialogController.State copy = state.copy();
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onStateChanged(copy);
                    }
                });
            }
            Events.writeState(currentTimeMillis, copy);
        }

        public void onLayoutDirectionChanged(final int i) {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onLayoutDirectionChanged(i);
                    }
                });
            }
        }

        public void onConfigurationChanged() {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onConfigurationChanged();
                    }
                });
            }
        }

        public void onShowVibrateHint() {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onShowVibrateHint();
                    }
                });
            }
        }

        public void onShowSilentHint() {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onShowSilentHint();
                    }
                });
            }
        }

        public void onScreenOff() {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onScreenOff();
                    }
                });
            }
        }

        public void onShowSafetyWarning(final int i) {
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onShowSafetyWarning(i);
                    }
                });
            }
        }

        public void onAccessibilityModeChanged(Boolean bool) {
            final boolean z = bool != null && bool.booleanValue();
            for (final Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new Runnable() {
                    public void run() {
                        ((VolumeDialogController.Callbacks) next.getKey()).onAccessibilityModeChanged(Boolean.valueOf(z));
                    }
                });
            }
        }

        public void onCaptionComponentStateChanged(Boolean bool, Boolean bool2) {
            boolean z = bool != null && bool.booleanValue();
            for (Map.Entry next : this.mCallbackMap.entrySet()) {
                ((Handler) next.getValue()).post(new VolumeDialogControllerImpl$C$$ExternalSyntheticLambda0(next, z, bool2));
            }
        }
    }

    public final class RingerModeObservers {
        public final RingerModeLiveData mRingerMode;
        public final RingerModeLiveData mRingerModeInternal;
        public final Observer<Integer> mRingerModeInternalObserver = new Observer<Integer>() {
            public void onChanged(Integer num) {
                VolumeDialogControllerImpl.this.mWorker.post(new VolumeDialogControllerImpl$RingerModeObservers$2$$ExternalSyntheticLambda0(this, num));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onChanged$0(Integer num) {
                int intValue = num.intValue();
                if (RingerModeObservers.this.mRingerModeInternal.getInitialSticky()) {
                    VolumeDialogControllerImpl.this.mState.ringerModeInternal = intValue;
                }
                if (D.BUG) {
                    String r0 = VolumeDialogControllerImpl.TAG;
                    Log.d(r0, "onChange internal_ringer_mode rm=" + Util.ringerModeToString(intValue));
                }
                if (VolumeDialogControllerImpl.this.updateRingerModeInternalW(intValue)) {
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
                }
            }
        };
        public final Observer<Integer> mRingerModeObserver = new Observer<Integer>() {
            public void onChanged(Integer num) {
                VolumeDialogControllerImpl.this.mWorker.post(new VolumeDialogControllerImpl$RingerModeObservers$1$$ExternalSyntheticLambda0(this, num));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onChanged$0(Integer num) {
                int intValue = num.intValue();
                if (RingerModeObservers.this.mRingerMode.getInitialSticky()) {
                    VolumeDialogControllerImpl.this.mState.ringerModeExternal = intValue;
                }
                if (D.BUG) {
                    String r0 = VolumeDialogControllerImpl.TAG;
                    Log.d(r0, "onChange ringer_mode rm=" + Util.ringerModeToString(intValue));
                }
                if (VolumeDialogControllerImpl.this.updateRingerModeExternalW(intValue)) {
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
                }
            }
        };

        public RingerModeObservers(RingerModeLiveData ringerModeLiveData, RingerModeLiveData ringerModeLiveData2) {
            this.mRingerMode = ringerModeLiveData;
            this.mRingerModeInternal = ringerModeLiveData2;
        }

        public void init() {
            int intValue = this.mRingerMode.getValue().intValue();
            if (intValue != -1) {
                VolumeDialogControllerImpl.this.mState.ringerModeExternal = intValue;
            }
            this.mRingerMode.observeForever(this.mRingerModeObserver);
            int intValue2 = this.mRingerModeInternal.getValue().intValue();
            if (intValue2 != -1) {
                VolumeDialogControllerImpl.this.mState.ringerModeInternal = intValue2;
            }
            this.mRingerModeInternal.observeForever(this.mRingerModeInternalObserver);
        }
    }

    public final class SettingObserver extends ContentObserver {
        public final Uri ZEN_MODE_CONFIG_URI = Settings.Global.getUriFor("zen_mode_config_etag");
        public final Uri ZEN_MODE_URI = Settings.Global.getUriFor("zen_mode");

        public SettingObserver(Handler handler) {
            super(handler);
        }

        public void init() {
            VolumeDialogControllerImpl.this.mContext.getContentResolver().registerContentObserver(this.ZEN_MODE_URI, false, this);
            VolumeDialogControllerImpl.this.mContext.getContentResolver().registerContentObserver(this.ZEN_MODE_CONFIG_URI, false, this);
        }

        public void onChange(boolean z, Uri uri) {
            boolean r2 = this.ZEN_MODE_URI.equals(uri) ? VolumeDialogControllerImpl.this.updateZenModeW() : false;
            if (this.ZEN_MODE_CONFIG_URI.equals(uri)) {
                r2 |= VolumeDialogControllerImpl.this.updateZenConfig();
            }
            if (r2) {
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
            }
        }
    }

    public final class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        public void init() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
            intentFilter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
            intentFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
            intentFilter.addAction("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
            intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
            volumeDialogControllerImpl.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, volumeDialogControllerImpl.mWorker);
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean z = false;
            if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                int intExtra = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int intExtra2 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                int intExtra3 = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
                if (D.BUG) {
                    String r1 = VolumeDialogControllerImpl.TAG;
                    Log.d(r1, "onReceive VOLUME_CHANGED_ACTION stream=" + intExtra + " level=" + intExtra2 + " oldLevel=" + intExtra3);
                }
                z = VolumeDialogControllerImpl.this.updateStreamLevelW(intExtra, intExtra2);
            } else if (action.equals("android.media.STREAM_DEVICES_CHANGED_ACTION")) {
                int intExtra4 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int intExtra5 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_DEVICES", -1);
                int intExtra6 = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_DEVICES", -1);
                if (D.BUG) {
                    String r12 = VolumeDialogControllerImpl.TAG;
                    Log.d(r12, "onReceive STREAM_DEVICES_CHANGED_ACTION stream=" + intExtra4 + " devices=" + intExtra5 + " oldDevices=" + intExtra6);
                }
                z = VolumeDialogControllerImpl.this.checkRoutedToBluetoothW(intExtra4) | VolumeDialogControllerImpl.this.onVolumeChangedW(intExtra4, 0);
            } else if (action.equals("android.media.STREAM_MUTE_CHANGED_ACTION")) {
                int intExtra7 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                boolean booleanExtra = intent.getBooleanExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", false);
                if (D.BUG) {
                    String r0 = VolumeDialogControllerImpl.TAG;
                    Log.d(r0, "onReceive STREAM_MUTE_CHANGED_ACTION stream=" + intExtra7 + " muted=" + booleanExtra);
                }
                z = VolumeDialogControllerImpl.this.updateStreamMuteW(intExtra7, booleanExtra);
            } else if (action.equals("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_EFFECTS_SUPPRESSOR_CHANGED");
                }
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                z = volumeDialogControllerImpl.updateEffectsSuppressorW(volumeDialogControllerImpl.mNoMan.getEffectsSuppressor());
            } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_CONFIGURATION_CHANGED");
                }
                VolumeDialogControllerImpl.this.mCallbacks.onConfigurationChanged();
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_SCREEN_OFF");
                }
                VolumeDialogControllerImpl.this.mCallbacks.onScreenOff();
            } else if (action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                if (D.BUG) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onReceive ACTION_CLOSE_SYSTEM_DIALOGS");
                }
                VolumeDialogControllerImpl.this.dismiss();
            }
            if (z) {
                VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                volumeDialogControllerImpl2.mCallbacks.onStateChanged(volumeDialogControllerImpl2.mState);
            }
        }
    }

    public final class MediaSessionsCallbacks implements MediaSessions.Callbacks {
        public int mNextStream = 100;
        public final HashMap<MediaSession.Token, Integer> mRemoteStreams = new HashMap<>();
        public final boolean mVolumeAdjustmentForRemoteGroupSessions;

        public MediaSessionsCallbacks(Context context) {
            this.mVolumeAdjustmentForRemoteGroupSessions = context.getResources().getBoolean(17891831);
        }

        public void onRemoteUpdate(MediaSession.Token token, String str, MediaController.PlaybackInfo playbackInfo) {
            int intValue;
            if (showForSession(token)) {
                addStream(token, "onRemoteUpdate");
                synchronized (this.mRemoteStreams) {
                    intValue = this.mRemoteStreams.get(token).intValue();
                }
                Slog.d(VolumeDialogControllerImpl.TAG, "onRemoteUpdate: stream: " + intValue + " volume: " + playbackInfo.getCurrentVolume());
                boolean z = true;
                boolean z2 = VolumeDialogControllerImpl.this.mState.states.indexOfKey(intValue) < 0;
                VolumeDialogController.StreamState r5 = VolumeDialogControllerImpl.this.streamStateW(intValue);
                r5.dynamic = true;
                r5.levelMin = 0;
                r5.levelMax = playbackInfo.getMaxVolume();
                if (r5.level != playbackInfo.getCurrentVolume()) {
                    r5.level = playbackInfo.getCurrentVolume();
                    z2 = true;
                }
                if (!Objects.equals(r5.remoteLabel, str)) {
                    r5.name = -1;
                    r5.remoteLabel = str;
                } else {
                    z = z2;
                }
                if (z) {
                    Log.d(VolumeDialogControllerImpl.TAG, "onRemoteUpdate: " + str + ": " + r5.level + " of " + r5.levelMax);
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
                }
            }
        }

        public void onRemoteVolumeChanged(MediaSession.Token token, int i) {
            int intValue;
            if (showForSession(token)) {
                addStream(token, "onRemoteVolumeChanged");
                synchronized (this.mRemoteStreams) {
                    intValue = this.mRemoteStreams.get(token).intValue();
                }
                boolean r5 = VolumeDialogControllerImpl.this.shouldShowUI(i);
                String r0 = VolumeDialogControllerImpl.TAG;
                Slog.d(r0, "onRemoteVolumeChanged: stream: " + intValue + " showui? " + r5);
                boolean r4 = VolumeDialogControllerImpl.this.updateActiveStreamW(intValue);
                if (r5) {
                    r4 |= VolumeDialogControllerImpl.this.checkRoutedToBluetoothW(3);
                }
                if (r4) {
                    Slog.d(VolumeDialogControllerImpl.TAG, "onRemoteChanged: updatingState");
                    VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl.mCallbacks.onStateChanged(volumeDialogControllerImpl.mState);
                }
                if (r5) {
                    VolumeDialogControllerImpl.this.onShowRequestedW(2);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
            com.android.systemui.volume.VolumeDialogControllerImpl.m3740$$Nest$fgetmState(r3.this$0).states.remove(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x004f, code lost:
            if (com.android.systemui.volume.VolumeDialogControllerImpl.m3740$$Nest$fgetmState(r3.this$0).activeStream != r4) goto L_0x0057;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0051, code lost:
            com.android.systemui.volume.VolumeDialogControllerImpl.m3761$$Nest$mupdateActiveStreamW(r3.this$0, -1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0057, code lost:
            r3 = r3.this$0;
            r3.mCallbacks.onStateChanged(com.android.systemui.volume.VolumeDialogControllerImpl.m3740$$Nest$fgetmState(r3));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onRemoteRemoved(android.media.session.MediaSession.Token r4) {
            /*
                r3 = this;
                boolean r0 = r3.showForSession(r4)
                if (r0 == 0) goto L_0x0066
                java.util.HashMap<android.media.session.MediaSession$Token, java.lang.Integer> r0 = r3.mRemoteStreams
                monitor-enter(r0)
                java.util.HashMap<android.media.session.MediaSession$Token, java.lang.Integer> r1 = r3.mRemoteStreams     // Catch:{ all -> 0x0063 }
                boolean r1 = r1.containsKey(r4)     // Catch:{ all -> 0x0063 }
                if (r1 != 0) goto L_0x002f
                java.lang.String r3 = com.android.systemui.volume.VolumeDialogControllerImpl.TAG     // Catch:{ all -> 0x0063 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
                r1.<init>()     // Catch:{ all -> 0x0063 }
                java.lang.String r2 = "onRemoteRemoved: stream doesn't exist, aborting remote removed for token:"
                r1.append(r2)     // Catch:{ all -> 0x0063 }
                java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0063 }
                r1.append(r4)     // Catch:{ all -> 0x0063 }
                java.lang.String r4 = r1.toString()     // Catch:{ all -> 0x0063 }
                android.util.Log.d(r3, r4)     // Catch:{ all -> 0x0063 }
                monitor-exit(r0)     // Catch:{ all -> 0x0063 }
                return
            L_0x002f:
                java.util.HashMap<android.media.session.MediaSession$Token, java.lang.Integer> r1 = r3.mRemoteStreams     // Catch:{ all -> 0x0063 }
                java.lang.Object r4 = r1.get(r4)     // Catch:{ all -> 0x0063 }
                java.lang.Integer r4 = (java.lang.Integer) r4     // Catch:{ all -> 0x0063 }
                int r4 = r4.intValue()     // Catch:{ all -> 0x0063 }
                monitor-exit(r0)     // Catch:{ all -> 0x0063 }
                com.android.systemui.volume.VolumeDialogControllerImpl r0 = com.android.systemui.volume.VolumeDialogControllerImpl.this
                com.android.systemui.plugins.VolumeDialogController$State r0 = r0.mState
                android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r0 = r0.states
                r0.remove(r4)
                com.android.systemui.volume.VolumeDialogControllerImpl r0 = com.android.systemui.volume.VolumeDialogControllerImpl.this
                com.android.systemui.plugins.VolumeDialogController$State r0 = r0.mState
                int r0 = r0.activeStream
                if (r0 != r4) goto L_0x0057
                com.android.systemui.volume.VolumeDialogControllerImpl r4 = com.android.systemui.volume.VolumeDialogControllerImpl.this
                r0 = -1
                boolean unused = r4.updateActiveStreamW(r0)
            L_0x0057:
                com.android.systemui.volume.VolumeDialogControllerImpl r3 = com.android.systemui.volume.VolumeDialogControllerImpl.this
                com.android.systemui.volume.VolumeDialogControllerImpl$C r4 = r3.mCallbacks
                com.android.systemui.plugins.VolumeDialogController$State r3 = r3.mState
                r4.onStateChanged(r3)
                goto L_0x0066
            L_0x0063:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0063 }
                throw r3
            L_0x0066:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogControllerImpl.MediaSessionsCallbacks.onRemoteRemoved(android.media.session.MediaSession$Token):void");
        }

        public void setStreamVolume(int i, int i2) {
            MediaSession.Token findToken = findToken(i);
            if (findToken == null) {
                String r1 = VolumeDialogControllerImpl.TAG;
                Log.w(r1, "setStreamVolume: No token found for stream: " + i);
            } else if (showForSession(findToken)) {
                VolumeDialogControllerImpl.this.mMediaSessions.setVolume(findToken, i2);
            }
        }

        public final boolean showForSession(MediaSession.Token token) {
            boolean z;
            if (this.mVolumeAdjustmentForRemoteGroupSessions) {
                return true;
            }
            String packageName = new MediaController(VolumeDialogControllerImpl.this.mContext, token).getPackageName();
            Iterator it = VolumeDialogControllerImpl.this.mRouter2Manager.getRoutingSessions(packageName).iterator();
            boolean z2 = false;
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                RoutingSessionInfo routingSessionInfo = (RoutingSessionInfo) it.next();
                if (!routingSessionInfo.isSystemSession()) {
                    if (routingSessionInfo.getSelectedRoutes().size() > 1) {
                        z = true;
                        z2 = true;
                        break;
                    }
                    z2 = true;
                }
            }
            if (z2) {
                return !z;
            }
            Log.d(VolumeDialogControllerImpl.TAG, "No routing session for " + packageName);
            return false;
        }

        public final MediaSession.Token findToken(int i) {
            synchronized (this.mRemoteStreams) {
                for (Map.Entry next : this.mRemoteStreams.entrySet()) {
                    if (((Integer) next.getValue()).equals(Integer.valueOf(i))) {
                        MediaSession.Token token = (MediaSession.Token) next.getKey();
                        return token;
                    }
                }
                return null;
            }
        }

        public final void addStream(MediaSession.Token token, String str) {
            synchronized (this.mRemoteStreams) {
                if (!this.mRemoteStreams.containsKey(token)) {
                    this.mRemoteStreams.put(token, Integer.valueOf(this.mNextStream));
                    String r1 = VolumeDialogControllerImpl.TAG;
                    Log.d(r1, str + ": added stream " + this.mNextStream + " from token + " + token.toString());
                    this.mNextStream = this.mNextStream + 1;
                }
            }
        }
    }
}
