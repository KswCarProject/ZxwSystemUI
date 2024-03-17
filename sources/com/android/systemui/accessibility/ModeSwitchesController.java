package com.android.systemui.accessibility;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.accessibility.MagnificationModeSwitch;

public class ModeSwitchesController implements MagnificationModeSwitch.SwitchListener {
    public MagnificationModeSwitch.SwitchListener mSwitchListenerDelegate;
    public final DisplayIdIndexSupplier<MagnificationModeSwitch> mSwitchSupplier;

    public ModeSwitchesController(Context context) {
        this.mSwitchSupplier = new SwitchSupplier(context, (DisplayManager) context.getSystemService(DisplayManager.class), new ModeSwitchesController$$ExternalSyntheticLambda0(this));
    }

    @VisibleForTesting
    public ModeSwitchesController(DisplayIdIndexSupplier<MagnificationModeSwitch> displayIdIndexSupplier) {
        this.mSwitchSupplier = displayIdIndexSupplier;
    }

    public void showButton(int i, int i2) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.showButton(i2);
        }
    }

    public void removeButton(int i) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.lambda$new$2();
        }
    }

    public void onSwitch(int i, int i2) {
        MagnificationModeSwitch.SwitchListener switchListener = this.mSwitchListenerDelegate;
        if (switchListener != null) {
            switchListener.onSwitch(i, i2);
        }
    }

    public void setSwitchListenerDelegate(MagnificationModeSwitch.SwitchListener switchListener) {
        this.mSwitchListenerDelegate = switchListener;
    }

    public static class SwitchSupplier extends DisplayIdIndexSupplier<MagnificationModeSwitch> {
        public final Context mContext;
        public final MagnificationModeSwitch.SwitchListener mSwitchListener;

        public SwitchSupplier(Context context, DisplayManager displayManager, MagnificationModeSwitch.SwitchListener switchListener) {
            super(displayManager);
            this.mContext = context;
            this.mSwitchListener = switchListener;
        }

        public MagnificationModeSwitch createInstance(Display display) {
            return new MagnificationModeSwitch(this.mContext.createWindowContext(display, 2039, (Bundle) null), this.mSwitchListener);
        }
    }
}
