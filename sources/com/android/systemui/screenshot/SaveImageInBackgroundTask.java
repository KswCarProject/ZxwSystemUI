package com.android.systemui.screenshot;

import android.app.ActivityTaskManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SaveImageInBackgroundTask extends AsyncTask<Void, Void, Void> {
    public static final String TAG = LogConfig.logTag(SaveImageInBackgroundTask.class);
    public final Context mContext;
    public final ScreenshotController.SavedImageData mImageData;
    public final ImageExporter mImageExporter;
    public long mImageTime;
    public final ScreenshotController.SaveImageInBackgroundData mParams;
    public final ScreenshotController.QuickShareData mQuickShareData;
    public final Random mRandom = new Random();
    public String mScreenshotId;
    public final ScreenshotSmartActions mScreenshotSmartActions;
    public final Supplier<ScreenshotController.SavedImageData.ActionTransition> mSharedElementTransition;
    public final boolean mSmartActionsEnabled;
    public final ScreenshotNotificationSmartActionsProvider mSmartActionsProvider;

    public SaveImageInBackgroundTask(Context context, ImageExporter imageExporter, ScreenshotSmartActions screenshotSmartActions, ScreenshotController.SaveImageInBackgroundData saveImageInBackgroundData, Supplier<ScreenshotController.SavedImageData.ActionTransition> supplier) {
        this.mContext = context;
        this.mScreenshotSmartActions = screenshotSmartActions;
        this.mImageData = new ScreenshotController.SavedImageData();
        this.mQuickShareData = new ScreenshotController.QuickShareData();
        this.mSharedElementTransition = supplier;
        this.mImageExporter = imageExporter;
        this.mParams = saveImageInBackgroundData;
        boolean z = DeviceConfig.getBoolean("systemui", "enable_screenshot_notification_smart_actions", true);
        this.mSmartActionsEnabled = z;
        if (z) {
            this.mSmartActionsProvider = SystemUIFactory.getInstance().createScreenshotNotificationSmartActionsProvider(context, AsyncTask.THREAD_POOL_EXECUTOR, new Handler());
        } else {
            this.mSmartActionsProvider = new ScreenshotNotificationSmartActionsProvider();
        }
    }

    public Void doInBackground(Void... voidArr) {
        if (isCancelled()) {
            return null;
        }
        UUID randomUUID = UUID.randomUUID();
        UserHandle userHandleOfForegroundApplication = getUserHandleOfForegroundApplication(this.mContext);
        Thread.currentThread().setPriority(10);
        Bitmap bitmap = this.mParams.image;
        this.mScreenshotId = String.format("Screenshot_%s", new Object[]{randomUUID});
        try {
            if (this.mSmartActionsEnabled && this.mParams.mQuickShareActionsReadyListener != null) {
                queryQuickShareAction(bitmap, userHandleOfForegroundApplication);
            }
            ImageExporter.Result result = this.mImageExporter.export(new SaveImageInBackgroundTask$$ExternalSyntheticLambda1(), randomUUID, bitmap).get();
            Uri uri = result.uri;
            this.mImageTime = result.timestamp;
            ScreenshotSmartActions screenshotSmartActions = this.mScreenshotSmartActions;
            String str = this.mScreenshotId;
            ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider = this.mSmartActionsProvider;
            ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType screenshotSmartActionType = ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.REGULAR_SMART_ACTIONS;
            CompletableFuture<List<Notification.Action>> smartActionsFuture = screenshotSmartActions.getSmartActionsFuture(str, uri, bitmap, screenshotNotificationSmartActionsProvider, screenshotSmartActionType, this.mSmartActionsEnabled, userHandleOfForegroundApplication);
            ArrayList arrayList = new ArrayList();
            if (this.mSmartActionsEnabled) {
                arrayList.addAll(buildSmartActions(this.mScreenshotSmartActions.getSmartActions(this.mScreenshotId, smartActionsFuture, DeviceConfig.getInt("systemui", "screenshot_notification_smart_actions_timeout_ms", 1000), this.mSmartActionsProvider, screenshotSmartActionType), this.mContext));
            }
            ScreenshotController.SavedImageData savedImageData = this.mImageData;
            savedImageData.uri = uri;
            savedImageData.smartActions = arrayList;
            Context context = this.mContext;
            savedImageData.shareTransition = createShareAction(context, context.getResources(), uri);
            ScreenshotController.SavedImageData savedImageData2 = this.mImageData;
            Context context2 = this.mContext;
            savedImageData2.editTransition = createEditAction(context2, context2.getResources(), uri);
            ScreenshotController.SavedImageData savedImageData3 = this.mImageData;
            Context context3 = this.mContext;
            savedImageData3.deleteAction = createDeleteAction(context3, context3.getResources(), uri);
            this.mImageData.quickShareAction = createQuickShareAction(this.mContext, this.mQuickShareData.quickShareAction, uri);
            this.mParams.mActionsReadyListener.onActionsReady(this.mImageData);
            this.mParams.finisher.accept(this.mImageData.uri);
            this.mParams.image = null;
        } catch (Exception unused) {
            this.mParams.clearImage();
            this.mImageData.reset();
            this.mQuickShareData.reset();
            this.mParams.mActionsReadyListener.onActionsReady(this.mImageData);
            this.mParams.finisher.accept((Object) null);
        }
        return null;
    }

    public void setActionsReadyListener(ScreenshotController.ActionsReadyListener actionsReadyListener) {
        this.mParams.mActionsReadyListener = actionsReadyListener;
    }

    public void onCancelled(Void voidR) {
        this.mImageData.reset();
        this.mQuickShareData.reset();
        this.mParams.mActionsReadyListener.onActionsReady(this.mImageData);
        this.mParams.finisher.accept((Object) null);
        this.mParams.clearImage();
    }

    @VisibleForTesting
    public Supplier<ScreenshotController.SavedImageData.ActionTransition> createShareAction(Context context, Resources resources, Uri uri) {
        return new SaveImageInBackgroundTask$$ExternalSyntheticLambda2(this, uri, context, resources);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ ScreenshotController.SavedImageData.ActionTransition lambda$createShareAction$0(Uri uri, Context context, Resources resources) {
        ScreenshotController.SavedImageData.ActionTransition actionTransition = this.mSharedElementTransition.get();
        String format = String.format("Screenshot (%s)", new Object[]{DateFormat.getDateTimeInstance().format(new Date(this.mImageTime))});
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setDataAndType(uri, "image/png");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setClipData(new ClipData(new ClipDescription("content", new String[]{"text/plain"}), new ClipData.Item(uri)));
        intent.putExtra("android.intent.extra.SUBJECT", format);
        intent.addFlags(1).addFlags(2);
        actionTransition.action = new Notification.Action.Builder(Icon.createWithResource(resources, R$drawable.ic_screenshot_share), resources.getString(17041498), PendingIntent.getBroadcastAsUser(context, context.getUserId(), new Intent(context, ActionProxyReceiver.class).putExtra("android:screenshot_action_intent", PendingIntent.getActivityAsUser(context, 0, Intent.createChooser(intent, (CharSequence) null).addFlags(268468224).addFlags(1), 335544320, actionTransition.bundle, UserHandle.CURRENT)).putExtra("android:screenshot_disallow_enter_pip", true).putExtra("android:screenshot_id", this.mScreenshotId).putExtra("android:smart_actions_enabled", this.mSmartActionsEnabled).setAction("android.intent.action.SEND").addFlags(268435456), 335544320, UserHandle.SYSTEM)).build();
        return actionTransition;
    }

    @VisibleForTesting
    public Supplier<ScreenshotController.SavedImageData.ActionTransition> createEditAction(Context context, Resources resources, Uri uri) {
        return new SaveImageInBackgroundTask$$ExternalSyntheticLambda0(this, context, uri, resources);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ ScreenshotController.SavedImageData.ActionTransition lambda$createEditAction$1(Context context, Uri uri, Resources resources) {
        ScreenshotController.SavedImageData.ActionTransition actionTransition = this.mSharedElementTransition.get();
        String string = context.getString(R$string.config_screenshotEditor);
        Intent intent = new Intent("android.intent.action.EDIT");
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        }
        intent.setDataAndType(uri, "image/png");
        intent.addFlags(1);
        intent.addFlags(2);
        intent.addFlags(268468224);
        actionTransition.action = new Notification.Action.Builder(Icon.createWithResource(resources, R$drawable.ic_screenshot_edit), resources.getString(17041453), PendingIntent.getBroadcastAsUser(context, this.mContext.getUserId(), new Intent(context, ActionProxyReceiver.class).putExtra("android:screenshot_action_intent", PendingIntent.getActivityAsUser(context, 0, intent, 67108864, actionTransition.bundle, UserHandle.CURRENT)).putExtra("android:screenshot_id", this.mScreenshotId).putExtra("android:smart_actions_enabled", this.mSmartActionsEnabled).putExtra("android:screenshot_override_transition", true).setAction("android.intent.action.EDIT").addFlags(268435456), 335544320, UserHandle.SYSTEM)).build();
        return actionTransition;
    }

    @VisibleForTesting
    public Notification.Action createDeleteAction(Context context, Resources resources, Uri uri) {
        return new Notification.Action.Builder(Icon.createWithResource(resources, R$drawable.ic_screenshot_delete), resources.getString(17040147), PendingIntent.getBroadcast(context, this.mContext.getUserId(), new Intent(context, DeleteScreenshotReceiver.class).putExtra("android:screenshot_uri_id", uri.toString()).putExtra("android:screenshot_id", this.mScreenshotId).putExtra("android:smart_actions_enabled", this.mSmartActionsEnabled).addFlags(268435456), 1409286144)).build();
    }

    public final UserHandle getUserHandleOfForegroundApplication(Context context) {
        int i;
        UserManager userManager = UserManager.get(context);
        try {
            i = ActivityTaskManager.getService().getLastResumedActivityUserId();
        } catch (RemoteException unused) {
            i = context.getUserId();
        }
        return userManager.getUserInfo(i).getUserHandle();
    }

    public final List<Notification.Action> buildSmartActions(List<Notification.Action> list, Context context) {
        ArrayList arrayList = new ArrayList();
        for (Notification.Action next : list) {
            Bundle extras = next.getExtras();
            String string = extras.getString("action_type", "Smart Action");
            Intent addFlags = new Intent(context, SmartActionsReceiver.class).putExtra("android:screenshot_action_intent", next.actionIntent).addFlags(268435456);
            addIntentExtras(this.mScreenshotId, addFlags, string, this.mSmartActionsEnabled);
            arrayList.add(new Notification.Action.Builder(next.getIcon(), next.title, PendingIntent.getBroadcast(context, this.mRandom.nextInt(), addFlags, 335544320)).setContextual(true).addExtras(extras).build());
        }
        return arrayList;
    }

    public static void addIntentExtras(String str, Intent intent, String str2, boolean z) {
        intent.putExtra("android:screenshot_action_type", str2).putExtra("android:screenshot_id", str).putExtra("android:smart_actions_enabled", z);
    }

    @VisibleForTesting
    private Notification.Action createQuickShareAction(Context context, Notification.Action action, Uri uri) {
        if (action == null) {
            return null;
        }
        Intent intent = action.actionIntent.getIntent();
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.putExtra("android.intent.extra.SUBJECT", String.format("Screenshot (%s)", new Object[]{DateFormat.getDateTimeInstance().format(new Date(this.mImageTime))}));
        intent.setClipData(new ClipData(new ClipDescription("content", new String[]{"image/png"}), new ClipData.Item(uri)));
        intent.addFlags(1);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 335544320);
        Bundle extras = action.getExtras();
        String string = extras.getString("action_type", "Smart Action");
        Intent addFlags = new Intent(context, SmartActionsReceiver.class).putExtra("android:screenshot_action_intent", activity).addFlags(268435456);
        addIntentExtras(this.mScreenshotId, addFlags, string, this.mSmartActionsEnabled);
        return new Notification.Action.Builder(action.getIcon(), action.title, PendingIntent.getBroadcast(context, this.mRandom.nextInt(), addFlags, 335544320)).setContextual(true).addExtras(extras).build();
    }

    public final void queryQuickShareAction(Bitmap bitmap, UserHandle userHandle) {
        ScreenshotSmartActions screenshotSmartActions = this.mScreenshotSmartActions;
        String str = this.mScreenshotId;
        ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider = this.mSmartActionsProvider;
        ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType screenshotSmartActionType = ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.QUICK_SHARE_ACTION;
        List<Notification.Action> smartActions = this.mScreenshotSmartActions.getSmartActions(this.mScreenshotId, screenshotSmartActions.getSmartActionsFuture(str, (Uri) null, bitmap, screenshotNotificationSmartActionsProvider, screenshotSmartActionType, this.mSmartActionsEnabled, userHandle), DeviceConfig.getInt("systemui", "screenshot_notification_quick_share_actions_timeout_ms", 500), this.mSmartActionsProvider, screenshotSmartActionType);
        if (!smartActions.isEmpty()) {
            this.mQuickShareData.quickShareAction = smartActions.get(0);
            this.mParams.mQuickShareActionsReadyListener.onActionsReady(this.mQuickShareData);
        }
    }
}
