package com.android.systemui.privacy;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.permission.PermissionGroupUsage;
import android.util.Log;
import com.android.systemui.privacy.PrivacyDialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogController$showDialog$1 implements Runnable {
    public final /* synthetic */ Context $context;
    public final /* synthetic */ PrivacyDialogController this$0;

    public PrivacyDialogController$showDialog$1(PrivacyDialogController privacyDialogController, Context context) {
        this.this$0 = privacyDialogController;
        this.$context = context;
    }

    public final void run() {
        PrivacyDialog.PrivacyElement privacyElement;
        Object obj;
        CharSequence charSequence;
        boolean z;
        boolean z2;
        List<PermissionGroupUsage> access$permGroupUsage = this.this$0.permGroupUsage();
        List<UserInfo> userProfiles = this.this$0.userTracker.getUserProfiles();
        this.this$0.privacyLogger.logUnfilteredPermGroupUsage(access$permGroupUsage);
        PrivacyDialogController privacyDialogController = this.this$0;
        final ArrayList arrayList = new ArrayList();
        for (PermissionGroupUsage permissionGroupUsage : access$permGroupUsage) {
            PrivacyType access$filterType = privacyDialogController.filterType(privacyDialogController.permGroupToPrivacyType(permissionGroupUsage.getPermissionGroupName()));
            Iterator it = userProfiles.iterator();
            while (true) {
                privacyElement = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (((UserInfo) obj).id == UserHandle.getUserId(permissionGroupUsage.getUid())) {
                    z2 = true;
                    continue;
                } else {
                    z2 = false;
                    continue;
                }
                if (z2) {
                    break;
                }
            }
            UserInfo userInfo = (UserInfo) obj;
            if ((userInfo != null || permissionGroupUsage.isPhoneCall()) && access$filterType != null) {
                if (permissionGroupUsage.isPhoneCall()) {
                    charSequence = "";
                } else {
                    charSequence = privacyDialogController.getLabelForPackage(permissionGroupUsage.getPackageName(), permissionGroupUsage.getUid());
                }
                CharSequence charSequence2 = charSequence;
                int userId = UserHandle.getUserId(permissionGroupUsage.getUid());
                String packageName = permissionGroupUsage.getPackageName();
                CharSequence attributionTag = permissionGroupUsage.getAttributionTag();
                CharSequence attributionLabel = permissionGroupUsage.getAttributionLabel();
                CharSequence proxyLabel = permissionGroupUsage.getProxyLabel();
                long lastAccessTimeMillis = permissionGroupUsage.getLastAccessTimeMillis();
                boolean isActive = permissionGroupUsage.isActive();
                if (userInfo == null) {
                    z = false;
                } else {
                    z = userInfo.isManagedProfile();
                }
                privacyElement = new PrivacyDialog.PrivacyElement(access$filterType, packageName, userId, charSequence2, attributionTag, attributionLabel, proxyLabel, lastAccessTimeMillis, isActive, z, permissionGroupUsage.isPhoneCall(), permissionGroupUsage.getPermissionGroupName(), privacyDialogController.getManagePermissionIntent(permissionGroupUsage.getPackageName(), userId, permissionGroupUsage.getPermissionGroupName(), permissionGroupUsage.getAttributionTag(), permissionGroupUsage.getAttributionLabel() != null));
            }
            if (privacyElement != null) {
                arrayList.add(privacyElement);
            }
        }
        Executor access$getUiExecutor$p = this.this$0.uiExecutor;
        final PrivacyDialogController privacyDialogController2 = this.this$0;
        final Context context = this.$context;
        access$getUiExecutor$p.execute(new Runnable() {
            public final void run() {
                List access$filterAndSelect = privacyDialogController2.filterAndSelect(arrayList);
                if (!access$filterAndSelect.isEmpty()) {
                    PrivacyDialog makeDialog = privacyDialogController2.dialogProvider.makeDialog(context, access$filterAndSelect, new PrivacyDialogController$showDialog$1$1$d$1(privacyDialogController2));
                    makeDialog.setShowForAllUsers(true);
                    makeDialog.addOnDismissListener(privacyDialogController2.onDialogDismissed);
                    makeDialog.show();
                    privacyDialogController2.privacyLogger.logShowDialogContents(access$filterAndSelect);
                    privacyDialogController2.dialog = makeDialog;
                    return;
                }
                Log.w("PrivacyDialogController", "Trying to show empty dialog");
            }
        });
    }
}
