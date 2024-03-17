package com.android.systemui.settings.brightness;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.Log;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.settings.brightness.ToggleSlider;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;

public class BrightnessController implements ToggleSlider.Listener, MirroredBrightnessController {
    public static final Uri BRIGHTNESS_FOR_VR_FLOAT_URI = Settings.System.getUriFor("screen_brightness_for_vr_float");
    public static final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor("screen_brightness_mode");
    public final Handler mBackgroundHandler;
    public int mBrightness = 100;
    public float mBrightnessMax = 1.0f;
    public float mBrightnessMin = 0.0f;
    public final BrightnessObserver mBrightnessObserver;
    public final BroadcastReceiver mBrightnessReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ZXW_ACTION_CHANGE_BRIGHTNESS_SETTINGS")) {
                int i = 100;
                int intExtra = intent.getIntExtra("ZXW_ACTION_CHANGE_BRIGHTNESS_EXTRA", 100);
                if (intExtra < 0) {
                    i = 0;
                } else if (intExtra <= 100) {
                    i = intExtra;
                }
                BrightnessController.this.mBrightness = i;
                BrightnessController.this.animateSliderTo(i);
                Log.i("CentralSurfaces.BrightnessController", "mBrightness = " + BrightnessController.this.mBrightness);
            }
        }
    };
    public final Context mContext;
    public final ToggleSlider mControl;
    public boolean mControlValueInitialized;
    public final int mDisplayId;
    public final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public void onDisplayChanged(int i) {
            BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
        }
    };
    public final DisplayManager mDisplayManager;
    public boolean mExternalChange;
    public final Handler mHandler;
    public boolean mListening;
    public final float mMaximumBacklightForVr;
    public final float mMinimumBacklightForVr;
    public ValueAnimator mSliderAnimator;
    public final Runnable mStartListeningRunnable = new Runnable() {
        public void run() {
            if (!BrightnessController.this.mListening) {
                BrightnessController.this.mListening = true;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("ZXW_ACTION_CHANGE_BRIGHTNESS_SETTINGS");
                BrightnessController.this.mContext.registerReceiverAsUser(BrightnessController.this.mBrightnessReceiver, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
                BrightnessController.this.mControl.setOnChangedListener(BrightnessController.this);
                BrightnessController.this.mUpdateSliderRunnable.run();
                BrightnessController.this.mContext.sendBroadcast(new Intent("com.szchoiceway.systemui.getBrigntness"));
            }
        }
    };
    public final Runnable mStopListeningRunnable = new Runnable() {
        public void run() {
            if (BrightnessController.this.mListening) {
                BrightnessController.this.mListening = false;
                BrightnessController.this.mContext.unregisterReceiver(BrightnessController.this.mBrightnessReceiver);
                BrightnessController.this.mControl.setOnChangedListener((ToggleSlider.Listener) null);
            }
        }
    };
    public final Runnable mUpdateModeRunnable = new Runnable() {
        public void run() {
        }
    };
    public final Runnable mUpdateSliderRunnable = new Runnable() {
        public void run() {
            BrightnessController.this.mHandler.obtainMessage(1, BrightnessController.this.mBrightness, 0).sendToTarget();
        }
    };
    public final CurrentUserTracker mUserTracker;
    public final IVrManager mVrManager;
    public final IVrStateCallbacks mVrStateCallbacks = new IVrStateCallbacks.Stub() {
        public void onVrStateChanged(boolean z) {
            BrightnessController.this.mHandler.obtainMessage(4, z ? 1 : 0, 0).sendToTarget();
        }
    };

    public void setMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mControl.setMirrorControllerAndMirror(brightnessMirrorController);
    }

    public class BrightnessObserver extends ContentObserver {
        public void onChange(boolean z, Uri uri) {
        }

        public BrightnessObserver(Handler handler) {
            super(handler);
        }
    }

    public BrightnessController(Context context, ToggleSlider toggleSlider, BroadcastDispatcher broadcastDispatcher, Handler handler) {
        AnonymousClass8 r1 = new Handler() {
            public void handleMessage(Message message) {
                BrightnessController.this.mExternalChange = true;
                try {
                    int i = message.what;
                    if (i != 1) {
                        if (i == 2) {
                            BrightnessController.this.mExternalChange = false;
                        } else if (i == 3) {
                            BrightnessController.this.mExternalChange = false;
                        } else if (i != 4) {
                            super.handleMessage(message);
                        } else {
                            BrightnessController.this.mExternalChange = false;
                        }
                        BrightnessController.this.mExternalChange = false;
                        return;
                    }
                    BrightnessController.this.updateSliderEx(message.arg1);
                } finally {
                    BrightnessController.this.mExternalChange = false;
                }
            }
        };
        this.mHandler = r1;
        this.mContext = context;
        this.mControl = toggleSlider;
        toggleSlider.setMax(100);
        toggleSlider.setValue(this.mBrightness);
        this.mBackgroundHandler = handler;
        this.mUserTracker = new CurrentUserTracker(broadcastDispatcher) {
            public void onUserSwitched(int i) {
                BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateModeRunnable);
                BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
            }
        };
        this.mBrightnessObserver = new BrightnessObserver(r1);
        this.mDisplayId = context.getDisplayId();
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mMinimumBacklightForVr = powerManager.getBrightnessConstraint(5);
        this.mMaximumBacklightForVr = powerManager.getBrightnessConstraint(6);
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mVrManager = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
    }

    public void registerCallbacks() {
        this.mBackgroundHandler.post(this.mStartListeningRunnable);
    }

    public void unregisterCallbacks() {
        this.mBackgroundHandler.post(this.mStopListeningRunnable);
        this.mControlValueInitialized = false;
    }

    public void onChanged(boolean z, int i, boolean z2) {
        Intent intent = new Intent("ZXW_ACTION_CHANGE_BRIGHTNESS_SYSTEM");
        intent.putExtra("ZXW_ACTION_CHANGE_BRIGHTNESS_EXTRA", i);
        this.mContext.sendBroadcast(intent);
        this.mBrightness = i;
    }

    public void checkRestrictionAndSetEnabled() {
        this.mBackgroundHandler.post(new Runnable() {
            public void run() {
                BrightnessController.this.mControl.setEnforcedAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(BrightnessController.this.mContext, "no_config_brightness", BrightnessController.this.mUserTracker.getCurrentUserId()));
            }
        });
    }

    public final void updateSliderEx(int i) {
        animateSliderTo(i);
    }

    public final void animateSliderTo(int i) {
        if (!this.mControlValueInitialized || !this.mControl.isVisible()) {
            this.mControl.setValue(i);
            this.mControlValueInitialized = true;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mControl.getValue(), i});
        this.mSliderAnimator = ofInt;
        ofInt.addUpdateListener(new BrightnessController$$ExternalSyntheticLambda0(this));
        this.mSliderAnimator.setDuration((long) ((Math.abs(this.mControl.getValue() - i) * 3000) / 65535));
        this.mSliderAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSliderTo$0(ValueAnimator valueAnimator) {
        this.mExternalChange = true;
        this.mControl.setValue(((Integer) valueAnimator.getAnimatedValue()).intValue());
        this.mExternalChange = false;
    }

    public static class Factory {
        public final Handler mBackgroundHandler;
        public final BroadcastDispatcher mBroadcastDispatcher;
        public final Context mContext;

        public Factory(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler) {
            this.mContext = context;
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mBackgroundHandler = handler;
        }

        public BrightnessController create(ToggleSlider toggleSlider) {
            return new BrightnessController(this.mContext, toggleSlider, this.mBroadcastDispatcher, this.mBackgroundHandler);
        }
    }
}
