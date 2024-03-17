package com.android.systemui.privacy;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.permission.PermissionGroupUsage;
import android.permission.PermissionManager;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final AppOpsController appOpsController;
    @NotNull
    public final Executor backgroundExecutor;
    @Nullable
    public Dialog dialog;
    @NotNull
    public final DialogProvider dialogProvider;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final PrivacyDialogController$onDialogDismissed$1 onDialogDismissed;
    @NotNull
    public final PackageManager packageManager;
    @NotNull
    public final PermissionManager permissionManager;
    @NotNull
    public final PrivacyItemController privacyItemController;
    @NotNull
    public final PrivacyLogger privacyLogger;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final Executor uiExecutor;
    @NotNull
    public final UserTracker userTracker;

    /* compiled from: PrivacyDialogController.kt */
    public interface DialogProvider {
        @NotNull
        PrivacyDialog makeDialog(@NotNull Context context, @NotNull List<PrivacyDialog.PrivacyElement> list, @NotNull Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4);
    }

    public PrivacyDialogController(@NotNull PermissionManager permissionManager2, @NotNull PackageManager packageManager2, @NotNull PrivacyItemController privacyItemController2, @NotNull UserTracker userTracker2, @NotNull ActivityStarter activityStarter2, @NotNull Executor executor, @NotNull Executor executor2, @NotNull PrivacyLogger privacyLogger2, @NotNull KeyguardStateController keyguardStateController2, @NotNull AppOpsController appOpsController2, @NotNull UiEventLogger uiEventLogger2, @NotNull DialogProvider dialogProvider2) {
        this.permissionManager = permissionManager2;
        this.packageManager = packageManager2;
        this.privacyItemController = privacyItemController2;
        this.userTracker = userTracker2;
        this.activityStarter = activityStarter2;
        this.backgroundExecutor = executor;
        this.uiExecutor = executor2;
        this.privacyLogger = privacyLogger2;
        this.keyguardStateController = keyguardStateController2;
        this.appOpsController = appOpsController2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogProvider = dialogProvider2;
        this.onDialogDismissed = new PrivacyDialogController$onDialogDismissed$1(this);
    }

    public PrivacyDialogController(@NotNull PermissionManager permissionManager2, @NotNull PackageManager packageManager2, @NotNull PrivacyItemController privacyItemController2, @NotNull UserTracker userTracker2, @NotNull ActivityStarter activityStarter2, @NotNull Executor executor, @NotNull Executor executor2, @NotNull PrivacyLogger privacyLogger2, @NotNull KeyguardStateController keyguardStateController2, @NotNull AppOpsController appOpsController2, @NotNull UiEventLogger uiEventLogger2) {
        this(permissionManager2, packageManager2, privacyItemController2, userTracker2, activityStarter2, executor, executor2, privacyLogger2, keyguardStateController2, appOpsController2, uiEventLogger2, PrivacyDialogControllerKt.defaultDialogProvider);
    }

    /* compiled from: PrivacyDialogController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void startActivity(String str, int i, CharSequence charSequence, Intent intent) {
        Dialog dialog2;
        if (intent == null) {
            intent = getDefaultManageAppPermissionsIntent(str, i);
        }
        this.uiEventLogger.log(PrivacyDialogEvent.PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS, i, str);
        this.privacyLogger.logStartSettingsActivityFromDialog(str, i);
        if (!this.keyguardStateController.isUnlocked() && (dialog2 = this.dialog) != null) {
            dialog2.hide();
        }
        this.activityStarter.startActivity(intent, true, (ActivityStarter.Callback) new PrivacyDialogController$startActivity$1(this));
    }

    public final Intent getManagePermissionIntent(String str, int i, CharSequence charSequence, CharSequence charSequence2, boolean z) {
        ActivityInfo activityInfo;
        if (charSequence2 != null && z) {
            Intent intent = new Intent("android.intent.action.MANAGE_PERMISSION_USAGE");
            intent.setPackage(str);
            intent.putExtra("android.intent.extra.PERMISSION_GROUP_NAME", charSequence.toString());
            intent.putExtra("android.intent.extra.ATTRIBUTION_TAGS", new String[]{charSequence2.toString()});
            intent.putExtra("android.intent.extra.SHOWING_ATTRIBUTION", true);
            ResolveInfo resolveActivity = this.packageManager.resolveActivity(intent, PackageManager.ResolveInfoFlags.of(0));
            if (!(resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null || !Intrinsics.areEqual((Object) activityInfo.permission, (Object) "android.permission.START_VIEW_PERMISSION_USAGE"))) {
                intent.setComponent(new ComponentName(str, resolveActivity.activityInfo.name));
                return intent;
            }
        }
        return getDefaultManageAppPermissionsIntent(str, i);
    }

    @NotNull
    public final Intent getDefaultManageAppPermissionsIntent(@NotNull String str, int i) {
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        return intent;
    }

    public final List<PermissionGroupUsage> permGroupUsage() {
        return this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
    }

    public final void showDialog(@NotNull Context context) {
        dismissDialog();
        this.backgroundExecutor.execute(new PrivacyDialogController$showDialog$1(this, context));
    }

    public final void dismissDialog() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
    }

    public final CharSequence getLabelForPackage(String str, int i) {
        try {
            return this.packageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(i)).loadLabel(this.packageManager);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("PrivacyDialogController", Intrinsics.stringPlus("Label not found for: ", str));
            return str;
        }
    }

    public final PrivacyType permGroupToPrivacyType(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -1140935117) {
            if (hashCode != 828638019) {
                if (hashCode == 1581272376 && str.equals("android.permission-group.MICROPHONE")) {
                    return PrivacyType.TYPE_MICROPHONE;
                }
            } else if (str.equals("android.permission-group.LOCATION")) {
                return PrivacyType.TYPE_LOCATION;
            }
        } else if (str.equals("android.permission-group.CAMERA")) {
            return PrivacyType.TYPE_CAMERA;
        }
        return null;
    }

    public final PrivacyType filterType(PrivacyType privacyType) {
        if (privacyType == null) {
            return null;
        }
        if ((!(privacyType == PrivacyType.TYPE_CAMERA || privacyType == PrivacyType.TYPE_MICROPHONE) || !this.privacyItemController.getMicCameraAvailable()) && (privacyType != PrivacyType.TYPE_LOCATION || !this.privacyItemController.getLocationAvailable())) {
            privacyType = null;
        }
        return privacyType;
    }

    public final List<PrivacyDialog.PrivacyElement> filterAndSelect(List<PrivacyDialog.PrivacyElement> list) {
        List list2;
        Object obj;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            PrivacyType type = ((PrivacyDialog.PrivacyElement) next).getType();
            Object obj2 = linkedHashMap.get(type);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(type, obj2);
            }
            ((List) obj2).add(next);
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry value : MapsKt__MapsJVMKt.toSortedMap(linkedHashMap).entrySet()) {
            Iterable iterable = (List) value.getValue();
            ArrayList arrayList2 = new ArrayList();
            for (Object next2 : iterable) {
                if (((PrivacyDialog.PrivacyElement) next2).getActive()) {
                    arrayList2.add(next2);
                }
            }
            if (!arrayList2.isEmpty()) {
                list2 = CollectionsKt___CollectionsKt.sortedWith(arrayList2, new PrivacyDialogController$filterAndSelect$lambda6$$inlined$sortedByDescending$1());
            } else {
                Iterator it = iterable.iterator();
                if (!it.hasNext()) {
                    obj = null;
                } else {
                    obj = it.next();
                    if (it.hasNext()) {
                        long lastActiveTimestamp = ((PrivacyDialog.PrivacyElement) obj).getLastActiveTimestamp();
                        do {
                            Object next3 = it.next();
                            long lastActiveTimestamp2 = ((PrivacyDialog.PrivacyElement) next3).getLastActiveTimestamp();
                            if (lastActiveTimestamp < lastActiveTimestamp2) {
                                obj = next3;
                                lastActiveTimestamp = lastActiveTimestamp2;
                            }
                        } while (it.hasNext());
                    }
                }
                PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) obj;
                if (privacyElement == null) {
                    list2 = null;
                } else {
                    list2 = CollectionsKt__CollectionsJVMKt.listOf(privacyElement);
                }
                if (list2 == null) {
                    list2 = CollectionsKt__CollectionsKt.emptyList();
                }
            }
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, list2);
        }
        return arrayList;
    }
}
