package com.android.systemui.qs;

import android.view.View;
import com.android.systemui.R$string;
import com.android.systemui.qs.FgsManagerController;
import kotlin.jvm.internal.Ref$ObjectRef;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$AppListAdapter$onBindViewHolder$2$1 implements View.OnClickListener {
    public final /* synthetic */ Ref$ObjectRef<FgsManagerController.RunningApp> $runningApp;
    public final /* synthetic */ FgsManagerController.AppItemViewHolder $this_with;
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$AppListAdapter$onBindViewHolder$2$1(FgsManagerController.AppItemViewHolder appItemViewHolder, FgsManagerController fgsManagerController, Ref$ObjectRef<FgsManagerController.RunningApp> ref$ObjectRef) {
        this.$this_with = appItemViewHolder;
        this.this$0 = fgsManagerController;
        this.$runningApp = ref$ObjectRef;
    }

    public final void onClick(View view) {
        this.$this_with.getStopButton().setText(R$string.fgs_manager_app_item_stop_button_stopped_label);
        this.this$0.stopPackage(((FgsManagerController.RunningApp) this.$runningApp.element).getUserId(), ((FgsManagerController.RunningApp) this.$runningApp.element).getPackageName(), ((FgsManagerController.RunningApp) this.$runningApp.element).getTimeStarted());
    }
}
