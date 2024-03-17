package com.android.systemui.statusbar.phone;

import android.graphics.Color;
import android.os.Trace;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimView;

public enum ScrimState {
    UNINITIALIZED,
    OFF((String) null) {
        public boolean isLowPowerState() {
            return true;
        }

        public void prepare(ScrimState scrimState) {
            this.mFrontTint = -16777216;
            this.mBehindTint = -16777216;
            this.mFrontAlpha = 1.0f;
            this.mBehindAlpha = 1.0f;
            this.mAnimationDuration = 1000;
        }
    },
    KEYGUARD((String) null) {
        public void prepare(ScrimState scrimState) {
            float f;
            int i = 0;
            this.mBlankScreen = false;
            if (scrimState == ScrimState.AOD) {
                this.mAnimationDuration = 667;
                if (this.mDisplayRequiresBlanking) {
                    this.mBlankScreen = true;
                }
            } else if (scrimState == ScrimState.KEYGUARD) {
                this.mAnimationDuration = 667;
            } else {
                this.mAnimationDuration = 220;
            }
            this.mFrontTint = -16777216;
            this.mBehindTint = -16777216;
            boolean z = this.mClipQsScrim;
            if (z) {
                i = -16777216;
            }
            this.mNotifTint = i;
            float f2 = 0.0f;
            this.mFrontAlpha = 0.0f;
            if (z) {
                f = 1.0f;
            } else {
                f = this.mScrimBehindAlphaKeyguard;
            }
            this.mBehindAlpha = f;
            if (z) {
                f2 = this.mScrimBehindAlphaKeyguard;
            }
            this.mNotifAlpha = f2;
            if (z) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    AUTH_SCRIMMED_SHADE((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = -16777216;
            this.mFrontAlpha = 0.66f;
        }
    },
    AUTH_SCRIMMED((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mNotifTint = scrimState.mNotifTint;
            this.mNotifAlpha = scrimState.mNotifAlpha;
            this.mBehindTint = scrimState.mBehindTint;
            this.mBehindAlpha = scrimState.mBehindAlpha;
            this.mFrontTint = -16777216;
            this.mFrontAlpha = 0.66f;
        }
    },
    BOUNCER((String) null) {
        public void prepare(ScrimState scrimState) {
            boolean z = this.mClipQsScrim;
            this.mBehindAlpha = z ? 1.0f : this.mDefaultScrimAlpha;
            this.mBehindTint = z ? -16777216 : 0;
            this.mNotifAlpha = z ? this.mDefaultScrimAlpha : 0.0f;
            this.mNotifTint = 0;
            this.mFrontAlpha = 0.0f;
        }
    },
    BOUNCER_SCRIMMED((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mFrontAlpha = this.mDefaultScrimAlpha;
        }
    },
    SHADE_LOCKED((String) null) {
        public int getBehindTint() {
            return -16777216;
        }

        public void prepare(ScrimState scrimState) {
            boolean z = this.mClipQsScrim;
            this.mBehindAlpha = z ? 1.0f : this.mDefaultScrimAlpha;
            this.mNotifAlpha = 1.0f;
            this.mFrontAlpha = 0.0f;
            this.mBehindTint = -16777216;
            if (z) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    BRIGHTNESS_MIRROR((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
        }
    },
    AOD((String) null) {
        public boolean isLowPowerState() {
            return true;
        }

        public boolean shouldBlendWithMainColor() {
            return false;
        }

        public void prepare(ScrimState scrimState) {
            float f;
            boolean alwaysOn = this.mDozeParameters.getAlwaysOn();
            boolean isQuickPickupEnabled = this.mDozeParameters.isQuickPickupEnabled();
            boolean isDocked = this.mDockManager.isDocked();
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mFrontTint = -16777216;
            if (alwaysOn || isDocked || isQuickPickupEnabled) {
                f = this.mAodFrontScrimAlpha;
            } else {
                f = 1.0f;
            }
            this.mFrontAlpha = f;
            this.mBehindTint = -16777216;
            this.mBehindAlpha = 0.0f;
            this.mAnimationDuration = 1000;
            this.mAnimateChange = this.mDozeParameters.shouldControlScreenOff() && !this.mDozeParameters.shouldShowLightRevealScrim();
        }

        public float getMaxLightRevealScrimAlpha() {
            return (!this.mWallpaperSupportsAmbientMode || this.mHasBackdrop) ? 1.0f : 0.0f;
        }
    },
    PULSING((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mFrontAlpha = this.mAodFrontScrimAlpha;
            this.mBehindTint = -16777216;
            this.mFrontTint = -16777216;
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mAnimationDuration = this.mWakeLockScreenSensorActive ? 1000 : 220;
        }

        public float getMaxLightRevealScrimAlpha() {
            if (this.mWakeLockScreenSensorActive) {
                return 0.6f;
            }
            return ScrimState.AOD.getMaxLightRevealScrimAlpha();
        }
    },
    UNLOCKED((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : 0.0f;
            this.mNotifAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mAnimationDuration = this.mKeyguardFadingAway ? this.mKeyguardFadingAwayDuration : 300;
            ScrimState scrimState2 = ScrimState.AOD;
            this.mAnimateChange = !this.mLaunchingAffordanceWithPreview && !(scrimState == scrimState2 || scrimState == ScrimState.PULSING);
            this.mFrontTint = 0;
            this.mBehindTint = -16777216;
            this.mBlankScreen = false;
            if (scrimState == scrimState2) {
                updateScrimColor(this.mScrimInFront, 1.0f, -16777216);
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
                this.mFrontTint = -16777216;
                this.mBehindTint = -16777216;
                this.mBlankScreen = true;
            }
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    DREAMING((String) null) {
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = 0;
            this.mBehindTint = -16777216;
            boolean z = this.mClipQsScrim;
            this.mNotifTint = z ? -16777216 : 0;
            this.mFrontAlpha = 0.0f;
            this.mBehindAlpha = z ? 1.0f : 0.0f;
            this.mNotifAlpha = 0.0f;
            this.mBlankScreen = false;
            if (z) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    };
    
    public boolean mAnimateChange;
    public long mAnimationDuration;
    public float mAodFrontScrimAlpha;
    public float mBehindAlpha;
    public int mBehindTint;
    public boolean mBlankScreen;
    public boolean mClipQsScrim;
    public float mDefaultScrimAlpha;
    public boolean mDisplayRequiresBlanking;
    public DockManager mDockManager;
    public DozeParameters mDozeParameters;
    public float mFrontAlpha;
    public int mFrontTint;
    public boolean mHasBackdrop;
    public boolean mKeyguardFadingAway;
    public long mKeyguardFadingAwayDuration;
    public boolean mLaunchingAffordanceWithPreview;
    public float mNotifAlpha;
    public int mNotifTint;
    public ScrimView mScrimBehind;
    public float mScrimBehindAlphaKeyguard;
    public ScrimView mScrimInFront;
    public boolean mWakeLockScreenSensorActive;
    public boolean mWallpaperSupportsAmbientMode;

    public float getMaxLightRevealScrimAlpha() {
        return 1.0f;
    }

    public boolean isLowPowerState() {
        return false;
    }

    public void prepare(ScrimState scrimState) {
    }

    public boolean shouldBlendWithMainColor() {
        return true;
    }

    public void init(ScrimView scrimView, ScrimView scrimView2, DozeParameters dozeParameters, DockManager dockManager) {
        this.mScrimInFront = scrimView;
        this.mScrimBehind = scrimView2;
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        this.mDisplayRequiresBlanking = dozeParameters.getDisplayNeedsBlanking();
    }

    public float getFrontAlpha() {
        return this.mFrontAlpha;
    }

    public float getBehindAlpha() {
        return this.mBehindAlpha;
    }

    public float getNotifAlpha() {
        return this.mNotifAlpha;
    }

    public int getFrontTint() {
        return this.mFrontTint;
    }

    public int getBehindTint() {
        return this.mBehindTint;
    }

    public int getNotifTint() {
        return this.mNotifTint;
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public boolean getBlanksScreen() {
        return this.mBlankScreen;
    }

    public void updateScrimColor(ScrimView scrimView, float f, int i) {
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_alpha" : "back_scrim_alpha", (int) (255.0f * f));
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_tint" : "back_scrim_tint", Color.alpha(i));
        scrimView.setTint(i);
        scrimView.setViewAlpha(f);
    }

    public boolean getAnimateChange() {
        return this.mAnimateChange;
    }

    public void setAodFrontScrimAlpha(float f) {
        this.mAodFrontScrimAlpha = f;
    }

    public void setScrimBehindAlphaKeyguard(float f) {
        this.mScrimBehindAlphaKeyguard = f;
    }

    public void setDefaultScrimAlpha(float f) {
        this.mDefaultScrimAlpha = f;
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        this.mLaunchingAffordanceWithPreview = z;
    }

    public void setHasBackdrop(boolean z) {
        this.mHasBackdrop = z;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        this.mWakeLockScreenSensorActive = z;
    }

    public void setKeyguardFadingAway(boolean z, long j) {
        this.mKeyguardFadingAway = z;
        this.mKeyguardFadingAwayDuration = j;
    }

    public void setClipQsScrim(boolean z) {
        this.mClipQsScrim = z;
    }
}
