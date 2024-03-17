package com.android.systemui.settings.brightness;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSliderController;

public class BrightnessDialog extends Activity {
    public final Handler mBackgroundHandler;
    public BrightnessController mBrightnessController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final BrightnessSliderController.Factory mToggleSliderFactory;

    public BrightnessDialog(BroadcastDispatcher broadcastDispatcher, BrightnessSliderController.Factory factory, Handler handler) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mToggleSliderFactory = factory;
        this.mBackgroundHandler = handler;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setGravity(49);
        window.clearFlags(2);
        window.requestFeature(1);
        window.getDecorView();
        window.setLayout(-1, -2);
        setContentView(R$layout.brightness_mirror_container);
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.brightness_mirror_container);
        frameLayout.setVisibility(0);
        BrightnessSliderController create = this.mToggleSliderFactory.create(this, frameLayout);
        create.init();
        frameLayout.addView(create.getRootView(), -1, -2);
        this.mBrightnessController = new BrightnessController(this, create, this.mBroadcastDispatcher, this.mBackgroundHandler);
    }

    public void onStart() {
        super.onStart();
        this.mBrightnessController.registerCallbacks();
        MetricsLogger.visible(this, 220);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(17432576, 17432577);
    }

    public void onStop() {
        super.onStop();
        MetricsLogger.hidden(this, 220);
        this.mBrightnessController.unregisterCallbacks();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 25 || i == 24 || i == 164) {
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
