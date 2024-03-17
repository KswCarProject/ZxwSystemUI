package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialogController {
    @Nullable
    public Drawable appIcon;
    @Nullable
    public String appName;
    @Nullable
    public Boolean appNotificationsCurrentlyEnabled;
    public boolean appNotificationsEnabled = true;
    @Nullable
    public Integer appUid;
    @NotNull
    public final List<NotificationChannelGroup> channelGroupList = new ArrayList();
    @NotNull
    public final Context context;
    public ChannelEditorDialog dialog;
    @NotNull
    public final ChannelEditorDialog.Builder dialogBuilder;
    @NotNull
    public final Map<NotificationChannel, Integer> edits = new LinkedHashMap();
    @NotNull
    public final HashMap<String, CharSequence> groupNameLookup = new HashMap<>();
    @NotNull
    public final INotificationManager noMan;
    @Nullable
    public OnChannelEditorDialogFinishedListener onFinishListener;
    @Nullable
    public NotificationInfo.OnSettingsClickListener onSettingsClickListener;
    @Nullable
    public String packageName;
    @NotNull
    public final List<NotificationChannel> paddedChannels = new ArrayList();
    public boolean prepared;
    @NotNull
    public final List<NotificationChannel> providedChannels = new ArrayList();
    public final int wmFlags = -2130444288;

    @VisibleForTesting
    public static /* synthetic */ void getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public ChannelEditorDialogController(@NotNull Context context2, @NotNull INotificationManager iNotificationManager, @NotNull ChannelEditorDialog.Builder builder) {
        this.noMan = iNotificationManager;
        this.dialogBuilder = builder;
        this.context = context2.getApplicationContext();
    }

    @Nullable
    public final OnChannelEditorDialogFinishedListener getOnFinishListener() {
        return this.onFinishListener;
    }

    public final void setOnFinishListener(@Nullable OnChannelEditorDialogFinishedListener onChannelEditorDialogFinishedListener) {
        this.onFinishListener = onChannelEditorDialogFinishedListener;
    }

    @NotNull
    public final List<NotificationChannel> getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.paddedChannels;
    }

    @NotNull
    public final HashMap<String, CharSequence> getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.groupNameLookup;
    }

    public final void prepareDialogForApp(@NotNull String str, @NotNull String str2, int i, @NotNull Set<NotificationChannel> set, @NotNull Drawable drawable, @Nullable NotificationInfo.OnSettingsClickListener onSettingsClickListener2) {
        this.appName = str;
        this.packageName = str2;
        this.appUid = Integer.valueOf(i);
        this.appIcon = drawable;
        boolean checkAreAppNotificationsOn = checkAreAppNotificationsOn();
        this.appNotificationsEnabled = checkAreAppNotificationsOn;
        this.onSettingsClickListener = onSettingsClickListener2;
        this.appNotificationsCurrentlyEnabled = Boolean.valueOf(checkAreAppNotificationsOn);
        this.channelGroupList.clear();
        this.channelGroupList.addAll(fetchNotificationChannelGroups());
        buildGroupNameLookup();
        this.providedChannels.clear();
        this.providedChannels.addAll(set);
        padToFourChannels(set);
        initDialog();
        this.prepared = true;
    }

    public final void buildGroupNameLookup() {
        for (NotificationChannelGroup notificationChannelGroup : this.channelGroupList) {
            if (notificationChannelGroup.getId() != null) {
                getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core().put(notificationChannelGroup.getId(), notificationChannelGroup.getName());
            }
        }
    }

    public final void padToFourChannels(Set<NotificationChannel> set) {
        this.paddedChannels.clear();
        CollectionsKt__MutableCollectionsKt.addAll(this.paddedChannels, SequencesKt___SequencesKt.take(CollectionsKt___CollectionsKt.asSequence(set), 4));
        CollectionsKt__MutableCollectionsKt.addAll(this.paddedChannels, SequencesKt___SequencesKt.take(SequencesKt___SequencesKt.distinct(SequencesKt___SequencesKt.filterNot(getDisplayableChannels(CollectionsKt___CollectionsKt.asSequence(this.channelGroupList)), new ChannelEditorDialogController$padToFourChannels$1(this))), 4 - this.paddedChannels.size()));
        if (this.paddedChannels.size() == 1 && Intrinsics.areEqual((Object) "miscellaneous", (Object) this.paddedChannels.get(0).getId())) {
            this.paddedChannels.clear();
        }
    }

    public final Sequence<NotificationChannel> getDisplayableChannels(Sequence<NotificationChannelGroup> sequence) {
        return SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.flatMap(sequence, ChannelEditorDialogController$getDisplayableChannels$channels$1.INSTANCE), new ChannelEditorDialogController$getDisplayableChannels$$inlined$compareBy$1());
    }

    public final void show() {
        if (this.prepared) {
            ChannelEditorDialog channelEditorDialog = this.dialog;
            if (channelEditorDialog == null) {
                channelEditorDialog = null;
            }
            channelEditorDialog.show();
            return;
        }
        throw new IllegalStateException("Must call prepareDialogForApp() before calling show()");
    }

    public final void close() {
        done();
    }

    public final void done() {
        resetState();
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            channelEditorDialog = null;
        }
        channelEditorDialog.dismiss();
    }

    public final void resetState() {
        this.appIcon = null;
        this.appUid = null;
        this.packageName = null;
        this.appName = null;
        this.appNotificationsCurrentlyEnabled = null;
        this.edits.clear();
        this.paddedChannels.clear();
        this.providedChannels.clear();
        this.groupNameLookup.clear();
    }

    @NotNull
    public final CharSequence groupNameForId(@Nullable String str) {
        CharSequence charSequence = this.groupNameLookup.get(str);
        return charSequence == null ? "" : charSequence;
    }

    public final void proposeEditForChannel(@NotNull NotificationChannel notificationChannel, int i) {
        if (notificationChannel.getImportance() == i) {
            this.edits.remove(notificationChannel);
        } else {
            this.edits.put(notificationChannel, Integer.valueOf(i));
        }
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            channelEditorDialog = null;
        }
        channelEditorDialog.updateDoneButtonText(hasChanges());
    }

    public final void proposeSetAppNotificationsEnabled(boolean z) {
        this.appNotificationsEnabled = z;
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog == null) {
            channelEditorDialog = null;
        }
        channelEditorDialog.updateDoneButtonText(hasChanges());
    }

    public final boolean areAppNotificationsEnabled() {
        return this.appNotificationsEnabled;
    }

    public final boolean hasChanges() {
        return (this.edits.isEmpty() ^ true) || !Intrinsics.areEqual((Object) Boolean.valueOf(this.appNotificationsEnabled), (Object) this.appNotificationsCurrentlyEnabled);
    }

    public final List<NotificationChannelGroup> fetchNotificationChannelGroups() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            List<NotificationChannelGroup> list = iNotificationManager.getNotificationChannelGroupsForPackage(str, num.intValue(), false).getList();
            if (!(list instanceof List)) {
                list = null;
            }
            if (list == null) {
                return CollectionsKt__CollectionsKt.emptyList();
            }
            return list;
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error fetching channel groups", e);
            return CollectionsKt__CollectionsKt.emptyList();
        }
    }

    public final boolean checkAreAppNotificationsOn() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            return iNotificationManager.areNotificationsEnabledForPackage(str, num.intValue());
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
            return false;
        }
    }

    public final void applyAppNotificationsOn(boolean z) {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.setNotificationsEnabledForPackage(str, num.intValue(), z);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
        }
    }

    public final void setChannelImportance(NotificationChannel notificationChannel, int i) {
        try {
            notificationChannel.setImportance(i);
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.updateNotificationChannelForPackage(str, num.intValue(), notificationChannel);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Unable to update notification importance", e);
        }
    }

    @VisibleForTesting
    public final void apply() {
        for (Map.Entry next : this.edits.entrySet()) {
            NotificationChannel notificationChannel = (NotificationChannel) next.getKey();
            int intValue = ((Number) next.getValue()).intValue();
            if (notificationChannel.getImportance() != intValue) {
                setChannelImportance(notificationChannel, intValue);
            }
        }
        if (!Intrinsics.areEqual((Object) Boolean.valueOf(this.appNotificationsEnabled), (Object) this.appNotificationsCurrentlyEnabled)) {
            applyAppNotificationsOn(this.appNotificationsEnabled);
        }
    }

    @VisibleForTesting
    public final void launchSettings(@NotNull View view) {
        NotificationChannel notificationChannel = this.providedChannels.size() == 1 ? this.providedChannels.get(0) : null;
        NotificationInfo.OnSettingsClickListener onSettingsClickListener2 = this.onSettingsClickListener;
        if (onSettingsClickListener2 != null) {
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            onSettingsClickListener2.onClick(view, notificationChannel, num.intValue());
        }
    }

    public final void initDialog() {
        this.dialogBuilder.setContext(this.context);
        ChannelEditorDialog build = this.dialogBuilder.build();
        this.dialog = build;
        ChannelEditorDialog channelEditorDialog = null;
        if (build == null) {
            build = null;
        }
        Window window = build.getWindow();
        if (window != null) {
            window.requestFeature(1);
        }
        ChannelEditorDialog channelEditorDialog2 = this.dialog;
        if (channelEditorDialog2 == null) {
            channelEditorDialog2 = null;
        }
        channelEditorDialog2.setTitle(" ");
        ChannelEditorDialog channelEditorDialog3 = this.dialog;
        if (channelEditorDialog3 != null) {
            channelEditorDialog = channelEditorDialog3;
        }
        channelEditorDialog.setContentView(R$layout.notif_half_shelf);
        channelEditorDialog.setCanceledOnTouchOutside(true);
        channelEditorDialog.setOnDismissListener(new ChannelEditorDialogController$initDialog$1$1(this));
        ChannelEditorListView channelEditorListView = (ChannelEditorListView) channelEditorDialog.findViewById(R$id.half_shelf_container);
        if (channelEditorListView != null) {
            channelEditorListView.setController(this);
            channelEditorListView.setAppIcon(this.appIcon);
            channelEditorListView.setAppName(this.appName);
            channelEditorListView.setChannels(getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
        }
        channelEditorDialog.setOnShowListener(new ChannelEditorDialogController$initDialog$1$3(this, channelEditorListView));
        TextView textView = (TextView) channelEditorDialog.findViewById(R$id.done_button);
        if (textView != null) {
            textView.setOnClickListener(new ChannelEditorDialogController$initDialog$1$4(this));
        }
        TextView textView2 = (TextView) channelEditorDialog.findViewById(R$id.see_more_button);
        if (textView2 != null) {
            textView2.setOnClickListener(new ChannelEditorDialogController$initDialog$1$5(this));
        }
        Window window2 = channelEditorDialog.getWindow();
        if (window2 != null) {
            window2.setBackgroundDrawable(new ColorDrawable(0));
            window2.addFlags(this.wmFlags);
            window2.setType(2017);
            window2.setWindowAnimations(16973910);
            WindowManager.LayoutParams attributes = window2.getAttributes();
            attributes.format = -3;
            attributes.setTitle(ChannelEditorDialogController.class.getSimpleName());
            attributes.gravity = 81;
            attributes.setFitInsetsTypes(window2.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
            attributes.width = -1;
            attributes.height = -2;
            window2.setAttributes(attributes);
        }
    }
}
