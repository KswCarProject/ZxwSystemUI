package com.android.wm.shell.pip.tv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.android.internal.util.ImageUtils;
import com.android.wm.shell.R;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.List;

public class TvPipNotificationController {
    public final ActionBroadcastReceiver mActionBroadcastReceiver;
    public Bitmap mActivityIcon;
    public final Context mContext;
    public final List<RemoteAction> mCustomActions = new ArrayList();
    public RemoteAction mCustomCloseAction;
    public String mDefaultTitle;
    public Delegate mDelegate;
    public boolean mIsNotificationShown;
    public final Handler mMainHandler;
    public final List<RemoteAction> mMediaActions = new ArrayList();
    public MediaSession.Token mMediaSessionToken;
    public final Notification.Builder mNotificationBuilder;
    public final NotificationManager mNotificationManager;
    public final PackageManager mPackageManager;
    public String mPackageName;
    public String mPipSubtitle;
    public String mPipTitle;
    public final TvPipBoundsState mTvPipBoundsState;

    public interface Delegate {
        void closePip();

        void enterPipMovementMenu();

        void movePipToFullscreen();

        void showPictureInPictureMenu();

        void togglePipExpansion();
    }

    public TvPipNotificationController(Context context, PipMediaController pipMediaController, PipParamsChangedForwarder pipParamsChangedForwarder, TvPipBoundsState tvPipBoundsState, Handler handler) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mMainHandler = handler;
        this.mTvPipBoundsState = tvPipBoundsState;
        this.mNotificationBuilder = new Notification.Builder(context, "TVPIP").setLocalOnly(true).setOngoing(true).setCategory("sys").setShowWhen(true).setSmallIcon(R.drawable.pip_icon).setAllowSystemGeneratedContextualActions(false).setContentIntent(createPendingIntent(context, "com.android.wm.shell.pip.tv.notification.action.FULLSCREEN")).setDeleteIntent(getCloseAction().actionIntent).extend(new Notification.TvExtender().setContentIntent(createPendingIntent(context, "com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU")).setDeleteIntent(createPendingIntent(context, "com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP")));
        this.mActionBroadcastReceiver = new ActionBroadcastReceiver();
        pipMediaController.addActionListener(new TvPipNotificationController$$ExternalSyntheticLambda0(this));
        pipMediaController.addTokenListener(new TvPipNotificationController$$ExternalSyntheticLambda1(this));
        pipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onExpandedAspectRatioChanged(float f) {
                TvPipNotificationController.this.updateExpansionState();
            }

            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                TvPipNotificationController.this.mCustomActions.clear();
                TvPipNotificationController.this.mCustomActions.addAll(list);
                TvPipNotificationController.this.mCustomCloseAction = remoteAction;
                TvPipNotificationController.this.updateNotificationContent();
            }

            public void onTitleChanged(String str) {
                TvPipNotificationController.this.mPipTitle = str;
                TvPipNotificationController.this.updateNotificationContent();
            }

            public void onSubtitleChanged(String str) {
                TvPipNotificationController.this.mPipSubtitle = str;
                TvPipNotificationController.this.updateNotificationContent();
            }
        });
        onConfigurationChanged(context);
    }

    public void setDelegate(Delegate delegate) {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            String valueOf = String.valueOf(delegate);
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 624798311, 0, (String) null, "TvPipNotification", valueOf);
        }
        if (this.mDelegate != null) {
            throw new IllegalStateException("The delegate has already been set and should not change.");
        } else if (delegate != null) {
            this.mDelegate = delegate;
        } else {
            throw new IllegalArgumentException("The delegate must not be null.");
        }
    }

    public void show(String str) {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            String valueOf = String.valueOf(str);
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1476597384, 0, (String) null, "TvPipNotification", valueOf);
        }
        if (this.mDelegate != null) {
            this.mIsNotificationShown = true;
            this.mPackageName = str;
            this.mActivityIcon = getActivityIcon();
            this.mActionBroadcastReceiver.register();
            updateNotificationContent();
            return;
        }
        throw new IllegalStateException("Delegate is not set.");
    }

    public void dismiss() {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -858212510, 0, (String) null, "TvPipNotification");
        }
        this.mIsNotificationShown = false;
        this.mPackageName = null;
        this.mActionBroadcastReceiver.unregister();
        this.mNotificationManager.cancel("TvPip", 1100);
    }

    public final Notification.Action getToggleAction(boolean z) {
        if (z) {
            return createSystemAction(R.drawable.pip_ic_collapse, R.string.pip_collapse, "com.android.wm.shell.pip.tv.notification.action.TOGGLE_EXPANDED_PIP");
        }
        return createSystemAction(R.drawable.pip_ic_expand, R.string.pip_expand, "com.android.wm.shell.pip.tv.notification.action.TOGGLE_EXPANDED_PIP");
    }

    public final Notification.Action createSystemAction(int i, int i2, String str) {
        Notification.Action.Builder builder = new Notification.Action.Builder(Icon.createWithResource(this.mContext, i), this.mContext.getString(i2), createPendingIntent(this.mContext, str));
        builder.setContextual(true);
        return builder.build();
    }

    public final void onMediaActionsChanged(List<RemoteAction> list) {
        this.mMediaActions.clear();
        this.mMediaActions.addAll(list);
        if (this.mCustomActions.isEmpty()) {
            updateNotificationContent();
        }
    }

    public final void onMediaSessionTokenChanged(MediaSession.Token token) {
        this.mMediaSessionToken = token;
        updateNotificationContent();
    }

    public final Notification.Action remoteToNotificationAction(RemoteAction remoteAction) {
        return remoteToNotificationAction(remoteAction, 0);
    }

    public final Notification.Action remoteToNotificationAction(RemoteAction remoteAction, int i) {
        Notification.Action.Builder builder = new Notification.Action.Builder(remoteAction.getIcon(), remoteAction.getTitle(), remoteAction.getActionIntent());
        if (remoteAction.getContentDescription() != null) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence("android.pictureContentDescription", remoteAction.getContentDescription());
            builder.addExtras(bundle);
        }
        builder.setSemanticAction(i);
        builder.setContextual(true);
        return builder.build();
    }

    public final Notification.Action[] getNotificationActions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getFullscreenAction());
        arrayList.add(getCloseAction());
        for (RemoteAction next : this.mCustomActions.isEmpty() ? this.mMediaActions : this.mCustomActions) {
            if (!PipUtils.remoteActionsMatch(this.mCustomCloseAction, next) && next.isEnabled()) {
                arrayList.add(remoteToNotificationAction(next));
            }
        }
        arrayList.add(getMoveAction());
        if (this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio() > 0.0f && this.mTvPipBoundsState.isTvExpandedPipSupported()) {
            arrayList.add(getToggleAction(this.mTvPipBoundsState.isTvPipExpanded()));
        }
        return (Notification.Action[]) arrayList.toArray(new Notification.Action[0]);
    }

    public final Notification.Action getCloseAction() {
        RemoteAction remoteAction = this.mCustomCloseAction;
        if (remoteAction == null) {
            return createSystemAction(R.drawable.pip_ic_close_white, R.string.pip_close, "com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP");
        }
        return remoteToNotificationAction(remoteAction, 4);
    }

    public final Notification.Action getFullscreenAction() {
        return createSystemAction(R.drawable.pip_ic_fullscreen_white, R.string.pip_fullscreen, "com.android.wm.shell.pip.tv.notification.action.FULLSCREEN");
    }

    public final Notification.Action getMoveAction() {
        return createSystemAction(R.drawable.pip_ic_move_white, R.string.pip_move, "com.android.wm.shell.pip.tv.notification.action.MOVE_PIP");
    }

    public void onConfigurationChanged(Context context) {
        this.mDefaultTitle = context.getResources().getString(R.string.pip_notification_unknown_title);
        updateNotificationContent();
    }

    public void updateExpansionState() {
        updateNotificationContent();
    }

    public final void updateNotificationContent() {
        if (this.mPackageManager != null && this.mIsNotificationShown) {
            Notification.Action[] notificationActions = getNotificationActions();
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1328080840, 0, (String) null, "TvPipNotification", String.valueOf(getNotificationTitle()), String.valueOf(this.mPipSubtitle), String.valueOf(this.mMediaSessionToken), String.valueOf(notificationActions.length));
            }
            for (Notification.Action action : notificationActions) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1234294823, 0, (String) null, "TvPipNotification", String.valueOf(action.toString()));
                }
            }
            this.mNotificationBuilder.setWhen(System.currentTimeMillis()).setContentTitle(getNotificationTitle()).setContentText(this.mPipSubtitle).setSubText(getApplicationLabel(this.mPackageName)).setActions(notificationActions);
            setPipIcon();
            Bundle bundle = new Bundle();
            bundle.putParcelable("android.mediaSession", this.mMediaSessionToken);
            this.mNotificationBuilder.setExtras(bundle);
            this.mNotificationBuilder.extend(new Notification.TvExtender().setContentIntent(createPendingIntent(this.mContext, "com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU")).setDeleteIntent(createPendingIntent(this.mContext, "com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP")));
            this.mNotificationManager.notify("TvPip", 1100, this.mNotificationBuilder.build());
        }
    }

    public final String getNotificationTitle() {
        if (!TextUtils.isEmpty(this.mPipTitle)) {
            return this.mPipTitle;
        }
        String applicationLabel = getApplicationLabel(this.mPackageName);
        if (!TextUtils.isEmpty(applicationLabel)) {
            return applicationLabel;
        }
        return this.mDefaultTitle;
    }

    public final String getApplicationLabel(String str) {
        try {
            return this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfo(str, 0)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public final void setPipIcon() {
        Bitmap bitmap = this.mActivityIcon;
        if (bitmap != null) {
            this.mNotificationBuilder.setLargeIcon(bitmap);
        } else {
            this.mNotificationBuilder.setLargeIcon(Icon.createWithResource(this.mContext, R.drawable.pip_icon));
        }
    }

    public final Bitmap getActivityIcon() {
        ComponentName componentName;
        Context context = this.mContext;
        if (context == null || (componentName = (ComponentName) PipUtils.getTopPipActivity(context).first) == null) {
            return null;
        }
        try {
            return ImageUtils.buildScaledBitmap(this.mPackageManager.getActivityIcon(componentName), this.mContext.getResources().getDimensionPixelSize(17104901), this.mContext.getResources().getDimensionPixelSize(17104902), true);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static PendingIntent createPendingIntent(Context context, String str) {
        return PendingIntent.getBroadcast(context, 0, new Intent(str).setPackage(context.getPackageName()), 201326592);
    }

    public class ActionBroadcastReceiver extends BroadcastReceiver {
        public final IntentFilter mIntentFilter;
        public boolean mRegistered;

        public ActionBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            this.mIntentFilter = intentFilter;
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.MOVE_PIP");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.TOGGLE_EXPANDED_PIP");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.FULLSCREEN");
            this.mRegistered = false;
        }

        public void register() {
            if (!this.mRegistered) {
                TvPipNotificationController.this.mContext.registerReceiverForAllUsers(this, this.mIntentFilter, "com.android.systemui.permission.SELF", TvPipNotificationController.this.mMainHandler);
                this.mRegistered = true;
            }
        }

        public void unregister() {
            if (this.mRegistered) {
                TvPipNotificationController.this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                String valueOf = String.valueOf(action);
                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1952489189, 0, (String) null, "TvPipNotification", valueOf);
            }
            if ("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU".equals(action)) {
                TvPipNotificationController.this.mDelegate.showPictureInPictureMenu();
            } else if ("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP".equals(action)) {
                TvPipNotificationController.this.mDelegate.closePip();
            } else if ("com.android.wm.shell.pip.tv.notification.action.MOVE_PIP".equals(action)) {
                TvPipNotificationController.this.mDelegate.enterPipMovementMenu();
            } else if ("com.android.wm.shell.pip.tv.notification.action.TOGGLE_EXPANDED_PIP".equals(action)) {
                TvPipNotificationController.this.mDelegate.togglePipExpansion();
            } else if ("com.android.wm.shell.pip.tv.notification.action.FULLSCREEN".equals(action)) {
                TvPipNotificationController.this.mDelegate.movePipToFullscreen();
            }
        }
    }
}
