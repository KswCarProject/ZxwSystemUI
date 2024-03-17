package com.android.systemui.controls.ui;

import android.app.PendingIntent;
import android.content.pm.ResolveInfo;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.TaskViewFactory;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showDetail$1 implements Runnable {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ PendingIntent $pendingIntent;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    public ControlActionCoordinatorImpl$showDetail$1(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder) {
        this.this$0 = controlActionCoordinatorImpl;
        this.$pendingIntent = pendingIntent;
        this.$cvh = controlViewHolder;
    }

    public final void run() {
        final List<ResolveInfo> queryIntentActivities = this.this$0.context.getPackageManager().queryIntentActivities(this.$pendingIntent.getIntent(), 65536);
        DelayableExecutor access$getUiExecutor$p = this.this$0.uiExecutor;
        final ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
        final ControlViewHolder controlViewHolder = this.$cvh;
        final PendingIntent pendingIntent = this.$pendingIntent;
        access$getUiExecutor$p.execute(new Runnable() {
            public final void run() {
                if (!(!queryIntentActivities.isEmpty()) || !controlActionCoordinatorImpl.taskViewFactory.isPresent()) {
                    controlViewHolder.setErrorStatus();
                    return;
                }
                final ControlActionCoordinatorImpl controlActionCoordinatorImpl = controlActionCoordinatorImpl;
                final PendingIntent pendingIntent = pendingIntent;
                final ControlViewHolder controlViewHolder = controlViewHolder;
                ((TaskViewFactory) controlActionCoordinatorImpl.taskViewFactory.get()).create(controlActionCoordinatorImpl.context, controlActionCoordinatorImpl.uiExecutor, new Consumer() {
                    public final void accept(TaskView taskView) {
                        ControlActionCoordinatorImpl controlActionCoordinatorImpl = controlActionCoordinatorImpl;
                        DetailDialog detailDialog = new DetailDialog(controlActionCoordinatorImpl.getActivityContext(), controlActionCoordinatorImpl.broadcastSender, taskView, pendingIntent, controlViewHolder, controlActionCoordinatorImpl.keyguardStateController, controlActionCoordinatorImpl.activityStarter);
                        detailDialog.setOnDismissListener(new ControlActionCoordinatorImpl$showDetail$1$1$1$1$1(controlActionCoordinatorImpl));
                        detailDialog.show();
                        controlActionCoordinatorImpl.dialog = detailDialog;
                    }
                });
            }
        });
    }
}
