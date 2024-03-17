package com.android.systemui.controls.ui;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import com.android.wm.shell.TaskView;
import org.jetbrains.annotations.Nullable;

/* compiled from: DetailDialog.kt */
public final class DetailDialog$stateCallback$1 implements TaskView.Listener {
    public final /* synthetic */ DetailDialog this$0;

    public DetailDialog$stateCallback$1(DetailDialog detailDialog) {
        this.this$0 = detailDialog;
    }

    public void onInitialized() {
        View access$getTaskViewContainer$p = this.this$0.taskViewContainer;
        if (access$getTaskViewContainer$p == null) {
            access$getTaskViewContainer$p = null;
        }
        DetailDialog detailDialog = this.this$0;
        ViewGroup.LayoutParams layoutParams = access$getTaskViewContainer$p.getLayoutParams();
        layoutParams.width = (int) (((float) access$getTaskViewContainer$p.getWidth()) * detailDialog.taskWidthPercentWidth);
        access$getTaskViewContainer$p.setLayoutParams(layoutParams);
        this.this$0.getTaskView().startActivity(this.this$0.getPendingIntent(), this.this$0.fillInIntent, ActivityOptions.makeCustomAnimation(this.this$0.getActivityContext(), 0, 0), this.this$0.getTaskViewBounds());
    }

    public void onTaskRemovalStarted(int i) {
        this.this$0.setDetailTaskId(-1);
        this.this$0.dismiss();
    }

    public void onTaskCreated(int i, @Nullable ComponentName componentName) {
        this.this$0.setDetailTaskId(i);
        ((ViewGroup) this.this$0.requireViewById(R$id.controls_activity_view)).setAlpha(1.0f);
    }

    public void onReleased() {
        this.this$0.removeDetailTask();
    }

    public void onBackPressedOnTaskRoot(int i) {
        this.this$0.dismiss();
    }
}
