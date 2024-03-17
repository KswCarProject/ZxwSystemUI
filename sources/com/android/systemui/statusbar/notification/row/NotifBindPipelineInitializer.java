package com.android.systemui.statusbar.notification.row;

public class NotifBindPipelineInitializer {
    public NotifBindPipeline mNotifBindPipeline;
    public RowContentBindStage mRowContentBindStage;

    public NotifBindPipelineInitializer(NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage) {
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
    }

    public void initialize() {
        this.mNotifBindPipeline.setStage(this.mRowContentBindStage);
    }
}
