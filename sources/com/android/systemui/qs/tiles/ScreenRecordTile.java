package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.KeyguardStateController;

public class ScreenRecordTile extends QSTileImpl<QSTile.BooleanState> implements RecordingController.RecordingStateChangeCallback {
    public final Callback mCallback;
    public final RecordingController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public final KeyguardStateController mKeyguardStateController;
    public long mMillisUntilFinished = 0;

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    public ScreenRecordTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RecordingController recordingController, KeyguardDismissUtil keyguardDismissUtil, KeyguardStateController keyguardStateController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mCallback = callback;
        this.mController = recordingController;
        recordingController.observe((LifecycleOwner) this, callback);
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mKeyguardStateController = keyguardStateController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
    }

    public QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = this.mContext.getString(R$string.quick_settings_screen_record_label);
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    public void handleClick(View view) {
        if (this.mController.isStarting()) {
            cancelCountdown();
        } else if (this.mController.isRecording()) {
            stopRecording();
        } else {
            this.mUiHandler.post(new ScreenRecordTile$$ExternalSyntheticLambda0(this, view));
        }
        refreshState();
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        CharSequence charSequence;
        boolean isStarting = this.mController.isStarting();
        boolean isRecording = this.mController.isRecording();
        booleanState.value = isRecording || isStarting;
        booleanState.state = (isRecording || isStarting) ? 2 : 1;
        booleanState.label = this.mContext.getString(R$string.quick_settings_screen_record_label);
        booleanState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_screenrecord);
        booleanState.forceExpandIcon = booleanState.state == 1;
        if (isRecording) {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_screen_record_stop);
        } else if (isStarting) {
            booleanState.secondaryLabel = String.format("%d...", new Object[]{Integer.valueOf((int) Math.floorDiv(this.mMillisUntilFinished + 500, 1000))});
        } else {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_screen_record_start);
        }
        if (TextUtils.isEmpty(booleanState.secondaryLabel)) {
            charSequence = booleanState.label;
        } else {
            charSequence = TextUtils.concat(new CharSequence[]{booleanState.label, ", ", booleanState.secondaryLabel});
        }
        booleanState.contentDescription = charSequence;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_screen_record_label);
    }

    /* renamed from: showPrompt */
    public final void lambda$handleClick$0(View view) {
        this.mKeyguardDismissUtil.executeWhenUnlocked(new ScreenRecordTile$$ExternalSyntheticLambda2(this, view != null && !this.mKeyguardStateController.isShowing(), this.mController.createScreenRecordDialog(this.mContext, new ScreenRecordTile$$ExternalSyntheticLambda1(this)), view), false, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showPrompt$1() {
        this.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
        getHost().collapsePanels();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showPrompt$2(boolean z, ScreenRecordDialog screenRecordDialog, View view) {
        if (z) {
            this.mDialogLaunchAnimator.showFromView(screenRecordDialog, view);
            return false;
        }
        screenRecordDialog.show();
        return false;
    }

    public final void cancelCountdown() {
        Log.d("ScreenRecordTile", "Cancelling countdown");
        this.mController.cancelCountdown();
    }

    public final void stopRecording() {
        this.mController.stopRecording();
    }

    public final class Callback implements RecordingController.RecordingStateChangeCallback {
        public Callback() {
        }

        public void onCountdown(long j) {
            ScreenRecordTile.this.mMillisUntilFinished = j;
            ScreenRecordTile.this.refreshState();
        }

        public void onCountdownEnd() {
            ScreenRecordTile.this.refreshState();
        }

        public void onRecordingStart() {
            ScreenRecordTile.this.refreshState();
        }

        public void onRecordingEnd() {
            ScreenRecordTile.this.refreshState();
        }
    }
}
