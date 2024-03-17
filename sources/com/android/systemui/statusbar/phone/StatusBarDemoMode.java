package com.android.systemui.statusbar.phone;

import android.os.Bundle;
import android.view.View;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.List;

public class StatusBarDemoMode extends ViewController<View> implements DemoMode {
    public final Clock mClockView;
    public final DemoModeController mDemoModeController;
    public final int mDisplayId;
    public final NavigationBarController mNavigationBarController;
    public final View mOperatorNameView;
    public final PhoneStatusBarTransitions mPhoneStatusBarTransitions;

    public StatusBarDemoMode(Clock clock, View view, DemoModeController demoModeController, PhoneStatusBarTransitions phoneStatusBarTransitions, NavigationBarController navigationBarController, int i) {
        super(clock);
        this.mClockView = clock;
        this.mOperatorNameView = view;
        this.mDemoModeController = demoModeController;
        this.mPhoneStatusBarTransitions = phoneStatusBarTransitions;
        this.mNavigationBarController = navigationBarController;
        this.mDisplayId = i;
    }

    public void onViewAttached() {
        this.mDemoModeController.addCallback((DemoMode) this);
    }

    public void onViewDetached() {
        this.mDemoModeController.removeCallback((DemoMode) this);
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("bars");
        arrayList.add("clock");
        arrayList.add("operator");
        return arrayList;
    }

    public void onDemoModeStarted() {
        dispatchDemoModeStartedToView(this.mClockView);
        dispatchDemoModeStartedToView(this.mOperatorNameView);
    }

    public void onDemoModeFinished() {
        dispatchDemoModeFinishedToView(this.mClockView);
        dispatchDemoModeFinishedToView(this.mOperatorNameView);
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        if (str.equals("clock")) {
            dispatchDemoCommandToView(str, bundle, this.mClockView);
        }
        if (str.equals("operator")) {
            dispatchDemoCommandToView(str, bundle, this.mOperatorNameView);
        }
        if (str.equals("bars")) {
            String string = bundle.getString("mode");
            if ("opaque".equals(string)) {
                i = 4;
            } else if ("translucent".equals(string)) {
                i = 2;
            } else if ("semi-transparent".equals(string)) {
                i = 1;
            } else if ("transparent".equals(string)) {
                i = 0;
            } else {
                i = "warning".equals(string) ? 5 : -1;
            }
            if (i != -1) {
                this.mPhoneStatusBarTransitions.transitionTo(i, true);
                this.mNavigationBarController.transitionTo(this.mDisplayId, i, true);
            }
        }
    }

    public final void dispatchDemoModeStartedToView(View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeStarted();
        }
    }

    public final void dispatchDemoCommandToView(String str, Bundle bundle, View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).dispatchDemoCommand(str, bundle);
        }
    }

    public final void dispatchDemoModeFinishedToView(View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeFinished();
        }
    }
}
