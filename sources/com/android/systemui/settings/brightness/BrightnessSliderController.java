package com.android.systemui.settings.brightness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.android.settingslib.RestrictedLockUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessSliderView;
import com.android.systemui.settings.brightness.ToggleSlider;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.util.ViewController;
import com.szchoiceway.eventcenter.SysProviderOpt;

public class BrightnessSliderController extends ViewController<BrightnessSliderView> implements ToggleSlider {
    public final FalsingManager mFalsingManager;
    public ToggleSlider.Listener mListener;
    public ToggleSlider mMirror;
    public BrightnessMirrorController mMirrorController;
    public final Gefingerpoken mOnInterceptListener = new Gefingerpoken() {
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 1 && actionMasked != 3) {
                return false;
            }
            BrightnessSliderController.this.mFalsingManager.isFalseTouch(10);
            return false;
        }
    };
    public final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (BrightnessSliderController.this.mListener != null) {
                BrightnessSliderController.this.mListener.onChanged(BrightnessSliderController.this.mTracking, i, false);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            BrightnessSliderController.this.mTracking = true;
            if (BrightnessSliderController.this.mListener != null) {
                BrightnessSliderController.this.mListener.onChanged(BrightnessSliderController.this.mTracking, BrightnessSliderController.this.getValue(), false);
            }
            if (BrightnessSliderController.this.mMirrorController != null) {
                BrightnessSliderController.this.mMirrorController.showMirror();
                BrightnessSliderController.this.mMirrorController.setLocationAndSize(BrightnessSliderController.this.mView);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            BrightnessSliderController.this.mTracking = false;
            if (BrightnessSliderController.this.mListener != null) {
                BrightnessSliderController.this.mListener.onChanged(BrightnessSliderController.this.mTracking, BrightnessSliderController.this.getValue(), true);
            }
            if (BrightnessSliderController.this.mMirrorController != null) {
                BrightnessSliderController.this.mMirrorController.hideMirror();
            }
        }
    };
    public boolean mTracking;

    public BrightnessSliderController(BrightnessSliderView brightnessSliderView, FalsingManager falsingManager) {
        super(brightnessSliderView);
        this.mFalsingManager = falsingManager;
    }

    public View getRootView() {
        return this.mView;
    }

    public void onViewAttached() {
        ((BrightnessSliderView) this.mView).setOnSeekBarChangeListener(this.mSeekListener);
        ((BrightnessSliderView) this.mView).setOnInterceptListener(this.mOnInterceptListener);
    }

    public void onViewDetached() {
        ((BrightnessSliderView) this.mView).setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) null);
        ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener((BrightnessSliderView.DispatchTouchEventListener) null);
        ((BrightnessSliderView) this.mView).setOnInterceptListener((Gefingerpoken) null);
    }

    public boolean mirrorTouchEvent(MotionEvent motionEvent) {
        if (this.mMirror != null) {
            return copyEventToMirror(motionEvent);
        }
        return ((BrightnessSliderView) this.mView).dispatchTouchEvent(motionEvent);
    }

    public final boolean copyEventToMirror(MotionEvent motionEvent) {
        MotionEvent copy = motionEvent.copy();
        boolean mirrorTouchEvent = this.mMirror.mirrorTouchEvent(copy);
        copy.recycle();
        return mirrorTouchEvent;
    }

    public void setEnforcedAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        ((BrightnessSliderView) this.mView).setEnforcedAdmin(enforcedAdmin);
    }

    public final void setMirror(ToggleSlider toggleSlider) {
        this.mMirror = toggleSlider;
        if (toggleSlider != null) {
            toggleSlider.setMax(((BrightnessSliderView) this.mView).getMax());
            this.mMirror.setValue(((BrightnessSliderView) this.mView).getValue());
            ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener(new BrightnessSliderController$$ExternalSyntheticLambda0(this));
            return;
        }
        ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener((BrightnessSliderView.DispatchTouchEventListener) null);
    }

    public void setMirrorControllerAndMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mMirrorController = brightnessMirrorController;
        setMirror(brightnessMirrorController.getToggleSlider());
    }

    public void setOnChangedListener(ToggleSlider.Listener listener) {
        this.mListener = listener;
    }

    public void setMax(int i) {
        ((BrightnessSliderView) this.mView).setMax(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            toggleSlider.setMax(i);
        }
    }

    public void setValue(int i) {
        ((BrightnessSliderView) this.mView).setValue(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            toggleSlider.setValue(i);
        }
    }

    public int getValue() {
        return ((BrightnessSliderView) this.mView).getValue();
    }

    public boolean isVisible() {
        return ((BrightnessSliderView) this.mView).isVisibleToUser();
    }

    public static class Factory {
        public final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager) {
            this.mFalsingManager = falsingManager;
        }

        public BrightnessSliderController create(Context context, ViewGroup viewGroup) {
            int i;
            if (new SysProviderOpt(context).getRecordInteger("KSW_DATA_PRODUCT_INDEX", 0) >= 1) {
                i = R$layout.quick_settings_brightness_dialog_hide;
            } else {
                i = getLayout();
            }
            return new BrightnessSliderController((BrightnessSliderView) LayoutInflater.from(context).inflate(i, viewGroup, false), this.mFalsingManager);
        }

        public final int getLayout() {
            return R$layout.quick_settings_brightness_dialog;
        }
    }
}
