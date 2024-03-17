package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DeviceControlsTile.kt */
public final class DeviceControlsTile extends QSTileImpl<QSTile.State> {
    @NotNull
    public final ControlsComponent controlsComponent;
    @NotNull
    public AtomicBoolean hasControlsApps = new AtomicBoolean(false);
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final DeviceControlsTile$listingCallback$1 listingCallback = new DeviceControlsTile$listingCallback$1(this);

    public static /* synthetic */ void getIcon$annotations() {
    }

    @Nullable
    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    public void handleLongClick(@Nullable View view) {
    }

    public DeviceControlsTile(@NotNull QSHost qSHost, @NotNull Looper looper, @NotNull Handler handler, @NotNull FalsingManager falsingManager, @NotNull MetricsLogger metricsLogger, @NotNull StatusBarStateController statusBarStateController, @NotNull ActivityStarter activityStarter, @NotNull QSLogger qSLogger, @NotNull ControlsComponent controlsComponent2, @NotNull KeyguardStateController keyguardStateController2) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.controlsComponent = controlsComponent2;
        this.keyguardStateController = keyguardStateController2;
        controlsComponent2.getControlsListingController().ifPresent(new Consumer(this) {
            public final /* synthetic */ DeviceControlsTile this$0;

            {
                this.this$0 = r1;
            }

            public final void accept(@NotNull ControlsListingController controlsListingController) {
                DeviceControlsTile deviceControlsTile = this.this$0;
                controlsListingController.observe((LifecycleOwner) deviceControlsTile, deviceControlsTile.listingCallback);
            }
        });
    }

    @NotNull
    public final QSTile.Icon getIcon() {
        return QSTileImpl.ResourceIcon.get(this.controlsComponent.getTileImageId());
    }

    public boolean isAvailable() {
        return this.controlsComponent.getControlsController().isPresent();
    }

    @NotNull
    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.state = 0;
        state.handlesLongClick = false;
        return state;
    }

    public void handleClick(@Nullable View view) {
        ActivityLaunchAnimator.Controller controller;
        if (getState().state != 0) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(this.mContext, ControlsActivity.class));
            intent.addFlags(335544320);
            intent.putExtra("extra_animate", true);
            if (view == null) {
                controller = null;
            } else {
                controller = ActivityLaunchAnimator.Controller.Companion.fromView(view, 32);
            }
            this.mUiHandler.post(new DeviceControlsTile$handleClick$1(this, intent, controller));
        }
    }

    public void handleUpdateState(@NotNull QSTile.State state, @Nullable Object obj) {
        CharSequence tileLabel = getTileLabel();
        state.label = tileLabel;
        state.contentDescription = tileLabel;
        state.icon = getIcon();
        if (!this.controlsComponent.isEnabled() || !this.hasControlsApps.get()) {
            state.state = 0;
            return;
        }
        if (this.controlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
            CharSequence structure = this.controlsComponent.getControlsController().get().getPreferredStructure().getStructure();
            state.state = 2;
            if (Intrinsics.areEqual((Object) structure, (Object) getTileLabel())) {
                structure = null;
            }
            state.secondaryLabel = structure;
        } else {
            state.state = 1;
            state.secondaryLabel = this.mContext.getText(R$string.controls_tile_locked);
        }
        state.stateDescription = state.secondaryLabel;
    }

    @NotNull
    public CharSequence getTileLabel() {
        return this.mContext.getText(this.controlsComponent.getTileTitleId());
    }
}
