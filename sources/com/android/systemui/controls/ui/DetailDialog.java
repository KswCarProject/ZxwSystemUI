package com.android.systemui.controls.ui;

import android.app.ActivityTaskManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.TaskView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: DetailDialog.kt */
public final class DetailDialog extends Dialog {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Context activityContext;
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final BroadcastSender broadcastSender;
    @NotNull
    public final ControlViewHolder cvh;
    public int detailTaskId = -1;
    @NotNull
    public final Intent fillInIntent;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final PendingIntent pendingIntent;
    @NotNull
    public final TaskView.Listener stateCallback;
    @NotNull
    public final TaskView taskView;
    public View taskViewContainer;
    public final float taskWidthPercentWidth;

    @NotNull
    public final Context getActivityContext() {
        return this.activityContext;
    }

    @NotNull
    public final BroadcastSender getBroadcastSender() {
        return this.broadcastSender;
    }

    @NotNull
    public final TaskView getTaskView() {
        return this.taskView;
    }

    @NotNull
    public final PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    @NotNull
    public final KeyguardStateController getKeyguardStateController() {
        return this.keyguardStateController;
    }

    @NotNull
    public final ActivityStarter getActivityStarter() {
        return this.activityStarter;
    }

    public DetailDialog(@NotNull Context context, @NotNull BroadcastSender broadcastSender2, @NotNull TaskView taskView2, @NotNull PendingIntent pendingIntent2, @NotNull ControlViewHolder controlViewHolder, @NotNull KeyguardStateController keyguardStateController2, @NotNull ActivityStarter activityStarter2) {
        super(context, R$style.Theme_SystemUI_Dialog_Control_DetailPanel);
        this.activityContext = context;
        this.broadcastSender = broadcastSender2;
        this.taskView = taskView2;
        this.pendingIntent = pendingIntent2;
        this.cvh = controlViewHolder;
        this.keyguardStateController = keyguardStateController2;
        this.activityStarter = activityStarter2;
        this.taskWidthPercentWidth = context.getResources().getFloat(R$dimen.controls_task_view_width_percentage);
        Intent intent = new Intent();
        intent.putExtra("controls.DISPLAY_IN_PANEL", true);
        intent.addFlags(524288);
        intent.addFlags(134217728);
        this.fillInIntent = intent;
        DetailDialog$stateCallback$1 detailDialog$stateCallback$1 = new DetailDialog$stateCallback$1(this);
        this.stateCallback = detailDialog$stateCallback$1;
        getWindow().addFlags(32);
        getWindow().addPrivateFlags(536870912);
        setContentView(R$layout.controls_detail_dialog);
        this.taskViewContainer = requireViewById(R$id.control_task_view_container);
        ViewGroup viewGroup = (ViewGroup) requireViewById(R$id.controls_activity_view);
        viewGroup.addView(getTaskView());
        viewGroup.setAlpha(0.0f);
        ((ImageView) requireViewById(R$id.control_detail_close)).setOnClickListener(new DetailDialog$2$1(this));
        requireViewById(R$id.control_detail_root).setOnClickListener(new DetailDialog$3$1(this));
        ((ImageView) requireViewById(R$id.control_detail_open_in_app)).setOnClickListener(new DetailDialog$4$1(this));
        getWindow().getDecorView().setOnApplyWindowInsetsListener(AnonymousClass5.INSTANCE);
        if (ScreenDecorationsUtils.supportsRoundedCornersOnWindows(getContext().getResources())) {
            taskView2.setCornerRadius((float) getContext().getResources().getDimensionPixelSize(R$dimen.controls_activity_view_corner_radius));
        }
        taskView2.setListener(controlViewHolder.getUiExecutor(), detailDialog$stateCallback$1);
    }

    /* compiled from: DetailDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void setDetailTaskId(int i) {
        this.detailTaskId = i;
    }

    public final void removeDetailTask() {
        if (this.detailTaskId != -1) {
            ActivityTaskManager.getInstance().removeTask(this.detailTaskId);
            this.detailTaskId = -1;
        }
    }

    @NotNull
    public final Rect getTaskViewBounds() {
        WindowMetrics currentWindowMetrics = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics();
        Rect bounds = currentWindowMetrics.getBounds();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        return new Rect(bounds.left - insetsIgnoringVisibility.left, bounds.top + insetsIgnoringVisibility.top + getContext().getResources().getDimensionPixelSize(R$dimen.controls_detail_dialog_header_height), bounds.right - insetsIgnoringVisibility.right, bounds.bottom - insetsIgnoringVisibility.bottom);
    }

    public void dismiss() {
        if (isShowing()) {
            this.taskView.release();
            super.dismiss();
        }
    }
}
