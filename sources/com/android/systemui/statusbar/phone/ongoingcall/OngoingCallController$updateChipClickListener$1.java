package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.PendingIntent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$updateChipClickListener$1 implements View.OnClickListener {
    public final /* synthetic */ View $backgroundView;
    public final /* synthetic */ PendingIntent $intent;
    public final /* synthetic */ OngoingCallController this$0;

    public OngoingCallController$updateChipClickListener$1(OngoingCallController ongoingCallController, PendingIntent pendingIntent, View view) {
        this.this$0 = ongoingCallController;
        this.$intent = pendingIntent;
        this.$backgroundView = view;
    }

    public final void onClick(View view) {
        this.this$0.logger.logChipClicked();
        this.this$0.activityStarter.postStartActivityDismissingKeyguard(this.$intent, ActivityLaunchAnimator.Controller.Companion.fromView(this.$backgroundView, 34));
    }
}
