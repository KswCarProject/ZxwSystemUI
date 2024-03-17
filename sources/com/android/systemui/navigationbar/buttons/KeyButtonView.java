package com.android.systemui.navigationbar.buttons;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
import com.android.systemui.R$styleable;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;

public class KeyButtonView extends ImageView implements ButtonInterface {
    public static final String TAG = KeyButtonView.class.getSimpleName();
    public AudioManager mAudioManager;
    public final Runnable mCheckLongPress;
    public int mCode;
    public int mContentDescriptionRes;
    public float mDarkIntensity;
    public long mDownTime;
    public boolean mGestureAborted;
    public boolean mHasOvalBg;
    public final InputManager mInputManager;
    public boolean mIsVertical;
    @VisibleForTesting
    public boolean mLongClicked;
    public final MetricsLogger mMetricsLogger;
    public View.OnClickListener mOnClickListener;
    public final Paint mOvalBgPaint;
    public final OverviewProxyService mOverviewProxyService;
    public final boolean mPlaySounds;
    public final KeyButtonRipple mRipple;
    public int mTouchDownX;
    public int mTouchDownY;
    public final UiEventLogger mUiEventLogger;

    @VisibleForTesting
    public enum NavBarButtonEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_HOME_BUTTON_TAP(533),
        NAVBAR_BACK_BUTTON_TAP(534),
        NAVBAR_OVERVIEW_BUTTON_TAP(535),
        NAVBAR_IME_SWITCHER_BUTTON_TAP(923),
        NAVBAR_HOME_BUTTON_LONGPRESS(536),
        NAVBAR_BACK_BUTTON_LONGPRESS(537),
        NAVBAR_OVERVIEW_BUTTON_LONGPRESS(538),
        NONE(0);
        
        private final int mId;

        /* access modifiers changed from: public */
        NavBarButtonEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public KeyButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyButtonView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, InputManager.getInstance(), new UiEventLoggerImpl());
    }

    @VisibleForTesting
    public KeyButtonView(Context context, AttributeSet attributeSet, int i, InputManager inputManager, UiEventLogger uiEventLogger) {
        super(context, attributeSet);
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOvalBgPaint = new Paint(3);
        this.mHasOvalBg = false;
        this.mCheckLongPress = new Runnable() {
            public void run() {
                if (!KeyButtonView.this.isPressed()) {
                    return;
                }
                if (KeyButtonView.this.isLongClickable()) {
                    KeyButtonView.this.performLongClick();
                    KeyButtonView.this.mLongClicked = true;
                    return;
                }
                if (KeyButtonView.this.mCode != 0) {
                    KeyButtonView.this.sendEvent(0, 128);
                    KeyButtonView.this.sendAccessibilityEvent(2);
                }
                KeyButtonView.this.mLongClicked = true;
            }
        };
        this.mUiEventLogger = uiEventLogger;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyButtonView, i, 0);
        this.mCode = obtainStyledAttributes.getInteger(R$styleable.KeyButtonView_keyCode, 0);
        this.mPlaySounds = obtainStyledAttributes.getBoolean(R$styleable.KeyButtonView_playSound, true);
        TypedValue typedValue = new TypedValue();
        if (obtainStyledAttributes.getValue(R$styleable.KeyButtonView_android_contentDescription, typedValue)) {
            this.mContentDescriptionRes = typedValue.resourceId;
        }
        obtainStyledAttributes.recycle();
        setClickable(true);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        KeyButtonRipple keyButtonRipple = new KeyButtonRipple(context, this, R$dimen.key_button_ripple_max_width);
        this.mRipple = keyButtonRipple;
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mInputManager = inputManager;
        setBackground(keyButtonRipple);
        setWillNotDraw(false);
        forceHasOverlappingRendering(false);
    }

    public boolean isClickable() {
        return this.mCode != 0 || super.isClickable();
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
    }

    public void loadAsync(Icon icon) {
        new AsyncTask<Icon, Void, Drawable>() {
            public Drawable doInBackground(Icon... iconArr) {
                return iconArr[0].loadDrawable(KeyButtonView.this.mContext);
            }

            public void onPostExecute(Drawable drawable) {
                KeyButtonView.this.setImageDrawable(drawable);
            }
        }.execute(new Icon[]{icon});
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = this.mContentDescriptionRes;
        if (i != 0) {
            setContentDescription(this.mContext.getString(i));
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mCode != 0) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, (CharSequence) null));
            if (isLongClickable()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, (CharSequence) null));
            }
        }
    }

    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            jumpDrawablesToCurrentState();
        }
    }

    public boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (i == 16 && this.mCode != 0) {
            sendEvent(0, 0, SystemClock.uptimeMillis());
            sendEvent(1, 0);
            sendAccessibilityEvent(1);
            playSoundEffect(0);
            return true;
        } else if (i != 32 || this.mCode == 0) {
            return super.performAccessibilityActionInternal(i, bundle);
        } else {
            sendEvent(0, 128);
            sendEvent(1, 0);
            sendAccessibilityEvent(2);
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        View.OnClickListener onClickListener;
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mGestureAborted = false;
        }
        if (this.mGestureAborted) {
            setPressed(false);
            return false;
        }
        if (action == 0) {
            this.mDownTime = SystemClock.uptimeMillis();
            this.mLongClicked = false;
            setPressed(true);
            this.mTouchDownX = (int) motionEvent.getX();
            this.mTouchDownY = (int) motionEvent.getY();
            if (this.mCode != 0) {
                sendEvent(0, 0, this.mDownTime);
            } else {
                performHapticFeedback(1);
            }
            if (!shouldShowSwipeUpUI) {
                playSoundEffect(0);
            }
            removeCallbacks(this.mCheckLongPress);
            postDelayed(this.mCheckLongPress, (long) ViewConfiguration.getLongPressTimeout());
        } else if (action == 1) {
            boolean z = isPressed() && !this.mLongClicked;
            setPressed(false);
            boolean z2 = SystemClock.uptimeMillis() - this.mDownTime > 150;
            if (shouldShowSwipeUpUI) {
                if (z) {
                    performHapticFeedback(1);
                    playSoundEffect(0);
                }
            } else if (z2 && !this.mLongClicked) {
                performHapticFeedback(8);
            }
            if (this.mCode != 0) {
                if (z) {
                    sendEvent(1, 0);
                    sendAccessibilityEvent(1);
                } else {
                    sendEvent(1, 32);
                }
            } else if (z && (onClickListener = this.mOnClickListener) != null) {
                onClickListener.onClick(this);
                sendAccessibilityEvent(1);
            }
            removeCallbacks(this.mCheckLongPress);
        } else if (action == 2) {
            int y = (int) motionEvent.getY();
            float quickStepTouchSlopPx = QuickStepContract.getQuickStepTouchSlopPx(getContext());
            if (((float) Math.abs(((int) motionEvent.getX()) - this.mTouchDownX)) > quickStepTouchSlopPx || ((float) Math.abs(y - this.mTouchDownY)) > quickStepTouchSlopPx) {
                setPressed(false);
                removeCallbacks(this.mCheckLongPress);
            }
        } else if (action == 3) {
            setPressed(false);
            if (this.mCode != 0) {
                sendEvent(1, 32);
            }
            removeCallbacks(this.mCheckLongPress);
        }
        return true;
    }

    public void setImageDrawable(Drawable drawable) {
        KeyButtonRipple.Type type;
        super.setImageDrawable(drawable);
        if (drawable != null) {
            KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) drawable;
            keyButtonDrawable.setDarkIntensity(this.mDarkIntensity);
            boolean hasOvalBg = keyButtonDrawable.hasOvalBg();
            this.mHasOvalBg = hasOvalBg;
            if (hasOvalBg) {
                this.mOvalBgPaint.setColor(keyButtonDrawable.getDrawableBackgroundColor());
            }
            KeyButtonRipple keyButtonRipple = this.mRipple;
            if (keyButtonDrawable.hasOvalBg()) {
                type = KeyButtonRipple.Type.OVAL;
            } else {
                type = KeyButtonRipple.Type.ROUNDED_RECT;
            }
            keyButtonRipple.setType(type);
        }
    }

    public void playSoundEffect(int i) {
        if (this.mPlaySounds) {
            this.mAudioManager.playSoundEffect(i, ActivityManager.getCurrentUser());
        }
    }

    public void sendEvent(int i, int i2) {
        sendEvent(i, i2, SystemClock.uptimeMillis());
    }

    public final void logSomePresses(int i, int i2) {
        UiEventLogger.UiEventEnum uiEventEnum;
        boolean z = (i2 & 128) != 0;
        UiEventLogger.UiEventEnum uiEventEnum2 = NavBarButtonEvent.NONE;
        if (i == 1 && this.mLongClicked) {
            return;
        }
        if ((i != 0 || z) && (i2 & 32) == 0 && (i2 & 256) == 0) {
            int i3 = this.mCode;
            if (i3 != 3) {
                if (i3 != 4) {
                    uiEventEnum = i3 != 187 ? uiEventEnum2 : z ? NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_LONGPRESS : NavBarButtonEvent.NAVBAR_OVERVIEW_BUTTON_TAP;
                } else if (z) {
                    uiEventEnum = NavBarButtonEvent.NAVBAR_BACK_BUTTON_LONGPRESS;
                } else {
                    uiEventEnum = NavBarButtonEvent.NAVBAR_BACK_BUTTON_TAP;
                }
            } else if (z) {
                uiEventEnum = NavBarButtonEvent.NAVBAR_HOME_BUTTON_LONGPRESS;
            } else {
                uiEventEnum = NavBarButtonEvent.NAVBAR_HOME_BUTTON_TAP;
            }
            if (uiEventEnum != uiEventEnum2) {
                this.mUiEventLogger.log(uiEventEnum);
            }
        }
    }

    public final void sendEvent(int i, int i2, long j) {
        int i3 = i2;
        this.mMetricsLogger.write(new LogMaker(931).setType(4).setSubtype(this.mCode).addTaggedData(933, Integer.valueOf(i)).addTaggedData(932, Integer.valueOf(i2)));
        logSomePresses(i, i2);
        if (this.mCode != 4 || i3 == 128) {
            int i4 = i;
        } else {
            String str = TAG;
            Log.i(str, "Back button event: " + KeyEvent.actionToString(i));
            if (i == 1) {
                this.mOverviewProxyService.notifyBackAction((i3 & 32) == 0, -1, -1, true, false);
            }
        }
        KeyEvent keyEvent = new KeyEvent(this.mDownTime, j, i, this.mCode, (i3 & 128) != 0 ? 1 : 0, 0, -1, 0, i3 | 8 | 64, 257);
        int displayId = getDisplay() != null ? getDisplay().getDisplayId() : -1;
        if (displayId != -1) {
            keyEvent.setDisplayId(displayId);
        }
        this.mInputManager.injectInputEvent(keyEvent, 0);
    }

    public void abortCurrentGesture() {
        Log.d("b/63783866", "KeyButtonView.abortCurrentGesture");
        if (this.mCode != 0) {
            sendEvent(1, 32);
        }
        setPressed(false);
        this.mRipple.abortDelayedRipple();
        this.mGestureAborted = true;
    }

    public void setDarkIntensity(float f) {
        this.mDarkIntensity = f;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            ((KeyButtonDrawable) drawable).setDarkIntensity(f);
            invalidate();
        }
        this.mRipple.setDarkIntensity(f);
    }

    public void setDelayTouchFeedback(boolean z) {
        this.mRipple.setDelayTouchFeedback(z);
    }

    public void draw(Canvas canvas) {
        if (this.mHasOvalBg) {
            float min = (float) Math.min(getWidth(), getHeight());
            canvas.drawOval(0.0f, 0.0f, min, min, this.mOvalBgPaint);
        }
        super.draw(canvas);
    }

    public void setVertical(boolean z) {
        this.mIsVertical = z;
    }
}
