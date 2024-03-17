package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.util.List;
import java.util.Set;

public class NotificationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    public int mActualHeight;
    public String mAppName;
    public OnAppSettingsClickListener mAppSettingsClickListener;
    public int mAppUid;
    public AssistantFeedbackController mAssistantFeedbackController;
    public TextView mAutomaticDescriptionView;
    public ChannelEditorDialogController mChannelEditorDialogController;
    public Integer mChosenImportance;
    public String mDelegatePkg;
    public NotificationEntry mEntry;
    public NotificationGuts mGutsContainer;
    public INotificationManager mINotificationManager;
    public boolean mIsAutomaticChosen;
    public boolean mIsDeviceProvisioned;
    public boolean mIsNonblockable;
    public boolean mIsSingleDefaultChannel;
    public boolean mIsSystemRegisteredCall;
    public MetricsLogger mMetricsLogger;
    public int mNumUniqueChannelsInRow;
    public View.OnClickListener mOnAlert = new NotificationInfo$$ExternalSyntheticLambda1(this);
    public View.OnClickListener mOnAutomatic = new NotificationInfo$$ExternalSyntheticLambda0(this);
    public View.OnClickListener mOnDismissSettings = new NotificationInfo$$ExternalSyntheticLambda3(this);
    public OnSettingsClickListener mOnSettingsClickListener;
    public View.OnClickListener mOnSilent = new NotificationInfo$$ExternalSyntheticLambda2(this);
    public OnUserInteractionCallback mOnUserInteractionCallback;
    public String mPackageName;
    public Drawable mPkgIcon;
    public PackageManager mPm;
    public boolean mPresentingChannelEditorDialog = false;
    public boolean mPressedApply;
    public TextView mPriorityDescriptionView;
    public StatusBarNotification mSbn;
    public boolean mShowAutomaticSetting;
    public TextView mSilentDescriptionView;
    public NotificationChannel mSingleNotificationChannel;
    @VisibleForTesting
    public boolean mSkipPost = false;
    public int mStartingChannelImportance;
    public UiEventLogger mUiEventLogger;
    public Set<NotificationChannel> mUniqueChannelsInRow;
    public boolean mWasShownHighPriority;

    public interface CheckSaveListener {
    }

    public interface OnAppSettingsClickListener {
        void onClick(View view, Intent intent);
    }

    public interface OnSettingsClickListener {
        void onClick(View view, NotificationChannel notificationChannel, int i);
    }

    public View getContentView() {
        return this;
    }

    @VisibleForTesting
    public boolean isAnimating() {
        return false;
    }

    public boolean needsFalsingProtection() {
        return true;
    }

    public boolean willBeRemoved() {
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        this.mIsAutomaticChosen = true;
        applyAlertingBehavior(2, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        this.mChosenImportance = 3;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(0, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        this.mChosenImportance = 2;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(1, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        this.mPressedApply = true;
        this.mGutsContainer.closeControls(view, true);
    }

    public NotificationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPriorityDescriptionView = (TextView) findViewById(R$id.alert_summary);
        this.mSilentDescriptionView = (TextView) findViewById(R$id.silence_summary);
        this.mAutomaticDescriptionView = (TextView) findViewById(R$id.automatic_summary);
    }

    public void bindNotification(PackageManager packageManager, INotificationManager iNotificationManager, OnUserInteractionCallback onUserInteractionCallback, ChannelEditorDialogController channelEditorDialogController, String str, NotificationChannel notificationChannel, Set<NotificationChannel> set, NotificationEntry notificationEntry, OnSettingsClickListener onSettingsClickListener, OnAppSettingsClickListener onAppSettingsClickListener, UiEventLogger uiEventLogger, boolean z, boolean z2, boolean z3, AssistantFeedbackController assistantFeedbackController) throws RemoteException {
        this.mINotificationManager = iNotificationManager;
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mPackageName = str;
        this.mUniqueChannelsInRow = set;
        this.mNumUniqueChannelsInRow = set.size();
        this.mEntry = notificationEntry;
        this.mSbn = notificationEntry.getSbn();
        this.mPm = packageManager;
        this.mAppSettingsClickListener = onAppSettingsClickListener;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mSingleNotificationChannel = notificationChannel;
        this.mStartingChannelImportance = notificationChannel.getImportance();
        this.mWasShownHighPriority = z3;
        this.mIsNonblockable = z2;
        this.mAppUid = this.mSbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mShowAutomaticSetting = this.mAssistantFeedbackController.isFeedbackEnabled();
        this.mUiEventLogger = uiEventLogger;
        boolean z4 = false;
        this.mIsSystemRegisteredCall = this.mSbn.getNotification().isStyle(Notification.CallStyle.class) && this.mINotificationManager.isInCall(this.mSbn.getPackageName(), this.mSbn.getUid());
        int numNotificationChannelsForPackage = this.mINotificationManager.getNumNotificationChannelsForPackage(str, this.mAppUid, false);
        int i = this.mNumUniqueChannelsInRow;
        if (i != 0) {
            this.mIsSingleDefaultChannel = i == 1 && this.mSingleNotificationChannel.getId().equals("miscellaneous") && numNotificationChannelsForPackage == 1;
            if (getAlertingBehavior() == 2) {
                z4 = true;
            }
            this.mIsAutomaticChosen = z4;
            bindHeader();
            bindChannelDetails();
            bindInlineControls();
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_OPEN);
            this.mMetricsLogger.write(notificationControlsLogMaker());
            return;
        }
        throw new IllegalArgumentException("bindNotification requires at least one channel");
    }

    public final void bindInlineControls() {
        if (this.mIsSystemRegisteredCall) {
            findViewById(R$id.non_configurable_call_text).setVisibility(0);
            findViewById(R$id.non_configurable_text).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(8);
            ((TextView) findViewById(R$id.done)).setText(R$string.inline_done_button);
            findViewById(R$id.turn_off_notifications).setVisibility(8);
        } else if (this.mIsNonblockable) {
            findViewById(R$id.non_configurable_text).setVisibility(0);
            findViewById(R$id.non_configurable_call_text).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(8);
            ((TextView) findViewById(R$id.done)).setText(R$string.inline_done_button);
            findViewById(R$id.turn_off_notifications).setVisibility(8);
        } else if (this.mNumUniqueChannelsInRow > 1) {
            findViewById(R$id.non_configurable_call_text).setVisibility(8);
            findViewById(R$id.non_configurable_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(0);
        } else {
            findViewById(R$id.non_configurable_call_text).setVisibility(8);
            findViewById(R$id.non_configurable_text).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(0);
        }
        View findViewById = findViewById(R$id.turn_off_notifications);
        findViewById.setOnClickListener(getTurnOffNotificationsClickListener());
        findViewById.setVisibility((!findViewById.hasOnClickListeners() || this.mIsNonblockable) ? 8 : 0);
        View findViewById2 = findViewById(R$id.done);
        findViewById2.setOnClickListener(this.mOnDismissSettings);
        findViewById2.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
        View findViewById3 = findViewById(R$id.silence);
        View findViewById4 = findViewById(R$id.alert);
        findViewById3.setOnClickListener(this.mOnSilent);
        findViewById4.setOnClickListener(this.mOnAlert);
        View findViewById5 = findViewById(R$id.automatic);
        if (this.mShowAutomaticSetting) {
            this.mAutomaticDescriptionView.setText(Html.fromHtml(this.mContext.getText(this.mAssistantFeedbackController.getInlineDescriptionResource(this.mEntry)).toString()));
            findViewById5.setVisibility(0);
            findViewById5.setOnClickListener(this.mOnAutomatic);
        } else {
            findViewById5.setVisibility(8);
        }
        applyAlertingBehavior(getAlertingBehavior(), false);
    }

    public final void bindHeader() {
        this.mPkgIcon = null;
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf(this.mPm.getApplicationLabel(applicationInfo));
                this.mPkgIcon = this.mPm.getApplicationIcon(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            this.mPkgIcon = this.mPm.getDefaultActivityIcon();
        }
        ((ImageView) findViewById(R$id.pkg_icon)).setImageDrawable(this.mPkgIcon);
        ((TextView) findViewById(R$id.pkg_name)).setText(this.mAppName);
        bindDelegate();
        View findViewById = findViewById(R$id.app_settings);
        Intent appSettingsIntent = getAppSettingsIntent(this.mPm, this.mPackageName, this.mSingleNotificationChannel, this.mSbn.getId(), this.mSbn.getTag());
        int i = 0;
        if (appSettingsIntent == null || TextUtils.isEmpty(this.mSbn.getNotification().getSettingsText())) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(new NotificationInfo$$ExternalSyntheticLambda7(this, appSettingsIntent));
        }
        View findViewById2 = findViewById(R$id.info);
        findViewById2.setOnClickListener(getSettingsOnClickListener());
        if (!findViewById2.hasOnClickListeners()) {
            i = 8;
        }
        findViewById2.setVisibility(i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindHeader$4(Intent intent, View view) {
        this.mAppSettingsClickListener.onClick(view, intent);
    }

    public final View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new NotificationInfo$$ExternalSyntheticLambda10(this, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSettingsOnClickListener$5(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNumUniqueChannelsInRow > 1 ? null : this.mSingleNotificationChannel, i);
    }

    public final View.OnClickListener getTurnOffNotificationsClickListener() {
        return new NotificationInfo$$ExternalSyntheticLambda8(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getTurnOffNotificationsClickListener$7(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        if (!this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = true;
            channelEditorDialogController.prepareDialogForApp(this.mAppName, this.mPackageName, this.mAppUid, this.mUniqueChannelsInRow, this.mPkgIcon, this.mOnSettingsClickListener);
            this.mChannelEditorDialogController.setOnFinishListener(new NotificationInfo$$ExternalSyntheticLambda9(this));
            this.mChannelEditorDialogController.show();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getTurnOffNotificationsClickListener$6() {
        this.mPresentingChannelEditorDialog = false;
        this.mGutsContainer.closeControls(this, false);
    }

    public final void bindChannelDetails() throws RemoteException {
        bindName();
        bindGroup();
    }

    public final void bindName() {
        TextView textView = (TextView) findViewById(R$id.channel_name);
        if (this.mIsSingleDefaultChannel || this.mNumUniqueChannelsInRow > 1) {
            textView.setVisibility(8);
        } else {
            textView.setText(this.mSingleNotificationChannel.getName());
        }
    }

    public final void bindDelegate() {
        TextView textView = (TextView) findViewById(R$id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = r4.mINotificationManager.getNotificationChannelGroupForPackage(r4.mSingleNotificationChannel.getGroup(), r4.mPackageName, r4.mAppUid);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void bindGroup() throws android.os.RemoteException {
        /*
            r4 = this;
            android.app.NotificationChannel r0 = r4.mSingleNotificationChannel
            if (r0 == 0) goto L_0x0021
            java.lang.String r0 = r0.getGroup()
            if (r0 == 0) goto L_0x0021
            android.app.INotificationManager r0 = r4.mINotificationManager
            android.app.NotificationChannel r1 = r4.mSingleNotificationChannel
            java.lang.String r1 = r1.getGroup()
            java.lang.String r2 = r4.mPackageName
            int r3 = r4.mAppUid
            android.app.NotificationChannelGroup r0 = r0.getNotificationChannelGroupForPackage(r1, r2, r3)
            if (r0 == 0) goto L_0x0021
            java.lang.CharSequence r0 = r0.getName()
            goto L_0x0022
        L_0x0021:
            r0 = 0
        L_0x0022:
            int r1 = com.android.systemui.R$id.group_name
            android.view.View r4 = r4.findViewById(r1)
            android.widget.TextView r4 = (android.widget.TextView) r4
            if (r0 == 0) goto L_0x0034
            r4.setText(r0)
            r0 = 0
            r4.setVisibility(r0)
            goto L_0x0039
        L_0x0034:
            r0 = 8
            r4.setVisibility(r0)
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotificationInfo.bindGroup():void");
    }

    public final void saveImportance() {
        if (!this.mIsNonblockable) {
            if (this.mChosenImportance == null) {
                this.mChosenImportance = Integer.valueOf(this.mStartingChannelImportance);
            }
            updateImportance();
        }
    }

    public final void updateImportance() {
        if (this.mChosenImportance != null) {
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_SAVE_IMPORTANCE);
            this.mMetricsLogger.write(importanceChangeLogMaker());
            int intValue = this.mChosenImportance.intValue();
            if (this.mStartingChannelImportance != -1000 && ((this.mWasShownHighPriority && this.mChosenImportance.intValue() >= 3) || (!this.mWasShownHighPriority && this.mChosenImportance.intValue() < 3))) {
                intValue = this.mStartingChannelImportance;
            }
            new Handler((Looper) Dependency.get(Dependency.BG_LOOPER)).post(new UpdateImportanceRunnable(this.mINotificationManager, this.mPackageName, this.mAppUid, this.mNumUniqueChannelsInRow == 1 ? this.mSingleNotificationChannel : null, this.mStartingChannelImportance, intValue, this.mIsAutomaticChosen));
            this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
        }
    }

    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    public final void applyAlertingBehavior(int i, boolean z) {
        int i2;
        boolean z2 = true;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(0);
            TransitionSet addTransition = transitionSet.addTransition(new Fade(2)).addTransition(new ChangeBounds());
            Transition duration = new Fade(1).setStartDelay(150).setDuration(200);
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            addTransition.addTransition(duration.setInterpolator(interpolator));
            transitionSet.setDuration(350);
            transitionSet.setInterpolator(interpolator);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        View findViewById = findViewById(R$id.alert);
        View findViewById2 = findViewById(R$id.silence);
        View findViewById3 = findViewById(R$id.automatic);
        if (i == 0) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda4(findViewById, findViewById2, findViewById3));
        } else if (i == 1) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda5(findViewById, findViewById2, findViewById3));
        } else if (i == 2) {
            this.mAutomaticDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new NotificationInfo$$ExternalSyntheticLambda6(findViewById3, findViewById, findViewById2));
        } else {
            throw new IllegalArgumentException("Unrecognized alerting behavior: " + i);
        }
        if (getAlertingBehavior() == i) {
            z2 = false;
        }
        TextView textView = (TextView) findViewById(R$id.done);
        if (z2) {
            i2 = R$string.inline_ok_button;
        } else {
            i2 = R$string.inline_done_button;
        }
        textView.setText(i2);
    }

    public static /* synthetic */ void lambda$applyAlertingBehavior$8(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    public static /* synthetic */ void lambda$applyAlertingBehavior$9(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(true);
        view3.setSelected(false);
    }

    public static /* synthetic */ void lambda$applyAlertingBehavior$10(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    public void onFinishedClosing() {
        Integer num = this.mChosenImportance;
        if (num != null) {
            this.mStartingChannelImportance = num.intValue();
        }
        bindInlineControls();
        logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_CLOSE);
        this.mMetricsLogger.write(notificationControlsLogMaker().setType(2));
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mGutsContainer != null && accessibilityEvent.getEventType() == 32) {
            if (this.mGutsContainer.isExposed()) {
                accessibilityEvent.getText().add(this.mContext.getString(R$string.notification_channel_controls_opened_accessibility, new Object[]{this.mAppName}));
                return;
            }
            accessibilityEvent.getText().add(this.mContext.getString(R$string.notification_channel_controls_closed_accessibility, new Object[]{this.mAppName}));
        }
    }

    public final Intent getAppSettingsIntent(PackageManager packageManager, String str, NotificationChannel notificationChannel, int i, String str2) {
        Intent intent = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.NOTIFICATION_PREFERENCES").setPackage(str);
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0 || queryIntentActivities.get(0) == null) {
            return null;
        }
        ActivityInfo activityInfo = queryIntentActivities.get(0).activityInfo;
        intent.setClassName(activityInfo.packageName, activityInfo.name);
        if (notificationChannel != null) {
            intent.putExtra("android.intent.extra.CHANNEL_ID", notificationChannel.getId());
        }
        intent.putExtra("android.intent.extra.NOTIFICATION_ID", i);
        intent.putExtra("android.intent.extra.NOTIFICATION_TAG", str2);
        return intent;
    }

    public void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    public boolean shouldBeSaved() {
        return this.mPressedApply;
    }

    public boolean handleCloseControls(boolean z, boolean z2) {
        ChannelEditorDialogController channelEditorDialogController;
        if (this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = false;
            channelEditorDialogController.setOnFinishListener((OnChannelEditorDialogFinishedListener) null);
            this.mChannelEditorDialogController.close();
        }
        if (z) {
            saveImportance();
        }
        return false;
    }

    public int getActualHeight() {
        return this.mActualHeight;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mActualHeight = getHeight();
    }

    public static class UpdateImportanceRunnable implements Runnable {
        public final int mAppUid;
        public final NotificationChannel mChannelToUpdate;
        public final int mCurrentImportance;
        public final INotificationManager mINotificationManager;
        public final int mNewImportance;
        public final String mPackageName;
        public final boolean mUnlockImportance;

        public UpdateImportanceRunnable(INotificationManager iNotificationManager, String str, int i, NotificationChannel notificationChannel, int i2, int i3, boolean z) {
            this.mINotificationManager = iNotificationManager;
            this.mPackageName = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mCurrentImportance = i2;
            this.mNewImportance = i3;
            this.mUnlockImportance = z;
        }

        public void run() {
            try {
                NotificationChannel notificationChannel = this.mChannelToUpdate;
                if (notificationChannel == null) {
                    this.mINotificationManager.setNotificationsEnabledWithImportanceLockForPackage(this.mPackageName, this.mAppUid, this.mNewImportance >= this.mCurrentImportance);
                } else if (this.mUnlockImportance) {
                    this.mINotificationManager.unlockNotificationChannel(this.mPackageName, this.mAppUid, notificationChannel.getId());
                } else {
                    notificationChannel.setImportance(this.mNewImportance);
                    this.mChannelToUpdate.lockFields(4);
                    this.mINotificationManager.updateNotificationChannelForPackage(this.mPackageName, this.mAppUid, this.mChannelToUpdate);
                }
            } catch (RemoteException e) {
                Log.e("InfoGuts", "Unable to update notification importance", e);
            }
        }
    }

    public final void logUiEvent(NotificationControlsEvent notificationControlsEvent) {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification != null) {
            this.mUiEventLogger.logWithInstanceId(notificationControlsEvent, statusBarNotification.getUid(), this.mSbn.getPackageName(), this.mSbn.getInstanceId());
        }
    }

    public final LogMaker getLogMaker() {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification == null) {
            return new LogMaker(1621);
        }
        return statusBarNotification.getLogMaker().setCategory(1621);
    }

    public final LogMaker importanceChangeLogMaker() {
        Integer num = this.mChosenImportance;
        return getLogMaker().setCategory(291).setType(4).setSubtype(Integer.valueOf(num != null ? num.intValue() : this.mStartingChannelImportance).intValue() - this.mStartingChannelImportance);
    }

    public final LogMaker notificationControlsLogMaker() {
        return getLogMaker().setCategory(204).setType(1).setSubtype(0);
    }

    public final int getAlertingBehavior() {
        if (!this.mShowAutomaticSetting || this.mSingleNotificationChannel.hasUserSetImportance()) {
            return this.mWasShownHighPriority ^ true ? 1 : 0;
        }
        return 2;
    }
}
