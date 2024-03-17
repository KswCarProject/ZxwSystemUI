package com.android.systemui.controls.ui;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.ViewGroup;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsAnimations;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsActivity.kt */
public final class ControlsActivity extends LifecycleActivity {
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    public BroadcastReceiver broadcastReceiver;
    public ViewGroup parent;
    @NotNull
    public final ControlsUiController uiController;

    public ControlsActivity(@NotNull ControlsUiController controlsUiController, @NotNull BroadcastDispatcher broadcastDispatcher2) {
        this.uiController = controlsUiController;
        this.broadcastDispatcher = broadcastDispatcher2;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.controls_fullscreen);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        int i = R$id.control_detail_root;
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById(i), getWindow(), getIntent()));
        ((ViewGroup) requireViewById(i)).setOnApplyWindowInsetsListener(ControlsActivity$onCreate$1$1.INSTANCE);
        initBroadcastReceiver();
    }

    public void onStart() {
        super.onStart();
        ViewGroup viewGroup = (ViewGroup) requireViewById(R$id.global_actions_controls);
        this.parent = viewGroup;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        viewGroup.setAlpha(0.0f);
        ControlsUiController controlsUiController = this.uiController;
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            viewGroup3 = null;
        }
        controlsUiController.show(viewGroup3, new ControlsActivity$onStart$1(this), this);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 != null) {
            viewGroup2 = viewGroup4;
        }
        controlsAnimations.enterAnimation(viewGroup2).start();
    }

    public void onBackPressed() {
        finish();
    }

    public void onStop() {
        super.onStop();
        this.uiController.hide();
    }

    public void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            broadcastReceiver2 = null;
        }
        broadcastDispatcher2.unregisterReceiver(broadcastReceiver2);
    }

    public final void initBroadcastReceiver() {
        this.broadcastReceiver = new ControlsActivity$initBroadcastReceiver$1(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            broadcastReceiver2 = null;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, broadcastReceiver2, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }
}
