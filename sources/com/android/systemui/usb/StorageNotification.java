package com.android.systemui.usb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.UserHandle;
import android.os.storage.DiskInfo;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.VolumeRecord;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import com.android.systemui.CoreStartable;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.NotificationChannels;

public class StorageNotification extends CoreStartable {
    public final BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            StorageNotification.this.mNotificationManager.cancelAsUser((String) null, 1397575510, UserHandle.ALL);
        }
    };
    public final StorageEventListener mListener = new StorageEventListener() {
        public void onVolumeStateChanged(VolumeInfo volumeInfo, int i, int i2) {
            StorageNotification.this.onVolumeStateChangedInternal(volumeInfo);
        }

        public void onVolumeRecordChanged(VolumeRecord volumeRecord) {
            VolumeInfo findVolumeByUuid = StorageNotification.this.mStorageManager.findVolumeByUuid(volumeRecord.getFsUuid());
            if (findVolumeByUuid != null && findVolumeByUuid.isMountedReadable()) {
                StorageNotification.this.onVolumeStateChangedInternal(findVolumeByUuid);
            }
        }

        public void onVolumeForgotten(String str) {
            StorageNotification.this.mNotificationManager.cancelAsUser(str, 1397772886, UserHandle.ALL);
        }

        public void onDiskScanned(DiskInfo diskInfo, int i) {
            StorageNotification.this.onDiskScannedInternal(diskInfo, i);
        }

        public void onDiskDestroyed(DiskInfo diskInfo) {
            StorageNotification.this.onDiskDestroyedInternal(diskInfo);
        }
    };
    public final PackageManager.MoveCallback mMoveCallback = new PackageManager.MoveCallback() {
        public void onCreated(int i, Bundle bundle) {
            MoveInfo moveInfo = new MoveInfo();
            moveInfo.moveId = i;
            moveInfo.extras = bundle;
            if (bundle != null) {
                moveInfo.packageName = bundle.getString("android.intent.extra.PACKAGE_NAME");
                moveInfo.label = bundle.getString("android.intent.extra.TITLE");
                moveInfo.volumeUuid = bundle.getString("android.os.storage.extra.FS_UUID");
            }
            StorageNotification.this.mMoves.put(i, moveInfo);
        }

        public void onStatusChanged(int i, int i2, long j) {
            MoveInfo moveInfo = (MoveInfo) StorageNotification.this.mMoves.get(i);
            if (moveInfo == null) {
                Log.w("StorageNotification", "Ignoring unknown move " + i);
            } else if (PackageManager.isMoveStatusFinished(i2)) {
                StorageNotification.this.onMoveFinished(moveInfo, i2);
            } else {
                StorageNotification.this.onMoveProgress(moveInfo, i2, j);
            }
        }
    };
    public final SparseArray<MoveInfo> mMoves = new SparseArray<>();
    public NotificationManager mNotificationManager;
    public final BroadcastReceiver mSnoozeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            StorageNotification.this.mStorageManager.setVolumeSnoozed(intent.getStringExtra("android.os.storage.extra.FS_UUID"), true);
        }
    };
    public StorageManager mStorageManager;

    public final Notification onVolumeFormatting(VolumeInfo volumeInfo) {
        return null;
    }

    public final Notification onVolumeUnmounted(VolumeInfo volumeInfo) {
        return null;
    }

    public StorageNotification(Context context) {
        super(context);
    }

    public static class MoveInfo {
        public Bundle extras;
        public String label;
        public int moveId;
        public String packageName;
        public String volumeUuid;

        public MoveInfo() {
        }
    }

    public void start() {
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        this.mStorageManager = storageManager;
        storageManager.registerListener(this.mListener);
        this.mContext.registerReceiver(this.mSnoozeReceiver, new IntentFilter("com.android.systemui.action.SNOOZE_VOLUME"), "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", (Handler) null, 2);
        this.mContext.registerReceiver(this.mFinishReceiver, new IntentFilter("com.android.systemui.action.FINISH_WIZARD"), "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", (Handler) null, 2);
        for (DiskInfo diskInfo : this.mStorageManager.getDisks()) {
            onDiskScannedInternal(diskInfo, diskInfo.volumeCount);
        }
        for (VolumeInfo onVolumeStateChangedInternal : this.mStorageManager.getVolumes()) {
            onVolumeStateChangedInternal(onVolumeStateChangedInternal);
        }
        this.mContext.getPackageManager().registerMoveCallback(this.mMoveCallback, new Handler());
        updateMissingPrivateVolumes();
    }

    public final void updateMissingPrivateVolumes() {
        if (!isTv() && !isAutomotive()) {
            for (VolumeRecord volumeRecord : this.mStorageManager.getVolumeRecords()) {
                if (volumeRecord.getType() == 1) {
                    String fsUuid = volumeRecord.getFsUuid();
                    VolumeInfo findVolumeByUuid = this.mStorageManager.findVolumeByUuid(fsUuid);
                    if ((findVolumeByUuid == null || !findVolumeByUuid.isMountedWritable()) && !volumeRecord.isSnoozed()) {
                        String string = this.mContext.getString(17040233, new Object[]{volumeRecord.getNickname()});
                        String string2 = this.mContext.getString(17040232);
                        Notification.Builder extend = new Notification.Builder(this.mContext, NotificationChannels.STORAGE).setSmallIcon(17302840).setColor(this.mContext.getColor(17170460)).setContentTitle(string).setContentText(string2).setContentIntent(buildForgetPendingIntent(volumeRecord)).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setDeleteIntent(buildSnoozeIntent(fsUuid)).extend(new Notification.TvExtender());
                        SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
                        this.mNotificationManager.notifyAsUser(fsUuid, 1397772886, extend.build(), UserHandle.ALL);
                    } else {
                        this.mNotificationManager.cancelAsUser(fsUuid, 1397772886, UserHandle.ALL);
                    }
                }
            }
        }
    }

    public final void onDiskScannedInternal(DiskInfo diskInfo, int i) {
        if (i != 0 || diskInfo.size <= 0) {
            this.mNotificationManager.cancelAsUser(diskInfo.getId(), 1396986699, UserHandle.ALL);
            return;
        }
        String string = this.mContext.getString(17040263, new Object[]{diskInfo.getDescription()});
        String string2 = this.mContext.getString(17040262, new Object[]{diskInfo.getDescription()});
        Notification.Builder extend = new Notification.Builder(this.mContext, NotificationChannels.STORAGE).setSmallIcon(getSmallIcon(diskInfo, 6)).setColor(this.mContext.getColor(17170460)).setContentTitle(string).setContentText(string2).setContentIntent(buildInitPendingIntent(diskInfo)).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1).setLocalOnly(true).setCategory("err").extend(new Notification.TvExtender());
        SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
        this.mNotificationManager.notifyAsUser(diskInfo.getId(), 1396986699, extend.build(), UserHandle.ALL);
    }

    public final void onDiskDestroyedInternal(DiskInfo diskInfo) {
        this.mNotificationManager.cancelAsUser(diskInfo.getId(), 1396986699, UserHandle.ALL);
    }

    public final void onVolumeStateChangedInternal(VolumeInfo volumeInfo) {
        int type = volumeInfo.getType();
        if (type == 0) {
            onPublicVolumeStateChangedInternal(volumeInfo);
        } else if (type == 1) {
            onPrivateVolumeStateChangedInternal(volumeInfo);
        }
    }

    public final void onPrivateVolumeStateChangedInternal(VolumeInfo volumeInfo) {
        Log.d("StorageNotification", "Notifying about private volume: " + volumeInfo.toString());
        updateMissingPrivateVolumes();
    }

    public final void onPublicVolumeStateChangedInternal(VolumeInfo volumeInfo) {
        Notification notification;
        Log.d("StorageNotification", "Notifying about public volume: " + volumeInfo.toString());
        if (volumeInfo.getMountUserId() == -10000) {
            Log.d("StorageNotification", "Ignore public volume state change event of removed user");
            return;
        }
        switch (volumeInfo.getState()) {
            case 0:
                notification = onVolumeUnmounted(volumeInfo);
                break;
            case 1:
                notification = onVolumeChecking(volumeInfo);
                break;
            case 2:
            case 3:
                notification = onVolumeMounted(volumeInfo);
                break;
            case 4:
                notification = onVolumeFormatting(volumeInfo);
                break;
            case 5:
                notification = onVolumeEjecting(volumeInfo);
                break;
            case 6:
                notification = onVolumeUnmountable(volumeInfo);
                break;
            case 7:
                notification = onVolumeRemoved(volumeInfo);
                break;
            case 8:
                notification = onVolumeBadRemoval(volumeInfo);
                break;
            default:
                notification = null;
                break;
        }
        if (notification != null) {
            this.mNotificationManager.notifyAsUser(volumeInfo.getId(), 1397773634, notification, UserHandle.of(volumeInfo.getMountUserId()));
        } else {
            this.mNotificationManager.cancelAsUser(volumeInfo.getId(), 1397773634, UserHandle.of(volumeInfo.getMountUserId()));
        }
    }

    public final Notification onVolumeChecking(VolumeInfo volumeInfo) {
        DiskInfo disk = volumeInfo.getDisk();
        return buildNotificationBuilder(volumeInfo, this.mContext.getString(17040230, new Object[]{disk.getDescription()}), this.mContext.getString(17040229, new Object[]{disk.getDescription()})).setCategory("progress").setOngoing(true).build();
    }

    public final Notification onVolumeMounted(VolumeInfo volumeInfo) {
        VolumeRecord findRecordByUuid = this.mStorageManager.findRecordByUuid(volumeInfo.getFsUuid());
        DiskInfo disk = volumeInfo.getDisk();
        if (findRecordByUuid.isSnoozed() && disk.isAdoptable()) {
            return null;
        }
        if (!disk.isAdoptable() || findRecordByUuid.isInited()) {
            String description = disk.getDescription();
            String string = this.mContext.getString(17040244, new Object[]{disk.getDescription()});
            PendingIntent buildBrowsePendingIntent = buildBrowsePendingIntent(volumeInfo);
            Notification.Builder category = buildNotificationBuilder(volumeInfo, description, string).addAction(new Notification.Action(17302463, this.mContext.getString(17040228), buildBrowsePendingIntent)).addAction(new Notification.Action(17302440, this.mContext.getString(17040257), buildUnmountPendingIntent(volumeInfo))).setContentIntent(buildBrowsePendingIntent).setCategory("sys");
            if (disk.isAdoptable()) {
                category.setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid()));
            }
            return category.build();
        }
        String description2 = disk.getDescription();
        String string2 = this.mContext.getString(17040240, new Object[]{disk.getDescription()});
        PendingIntent buildInitPendingIntent = buildInitPendingIntent(volumeInfo);
        PendingIntent buildUnmountPendingIntent = buildUnmountPendingIntent(volumeInfo);
        if (isAutomotive()) {
            return buildNotificationBuilder(volumeInfo, description2, string2).setContentIntent(buildUnmountPendingIntent).setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid())).build();
        }
        return buildNotificationBuilder(volumeInfo, description2, string2).addAction(new Notification.Action(17302846, this.mContext.getString(17040231), buildInitPendingIntent)).addAction(new Notification.Action(17302440, this.mContext.getString(17040257), buildUnmountPendingIntent)).setContentIntent(buildInitPendingIntent).setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid())).build();
    }

    public final Notification onVolumeEjecting(VolumeInfo volumeInfo) {
        DiskInfo disk = volumeInfo.getDisk();
        return buildNotificationBuilder(volumeInfo, this.mContext.getString(17040261, new Object[]{disk.getDescription()}), this.mContext.getString(17040260, new Object[]{disk.getDescription()})).setCategory("progress").setOngoing(true).build();
    }

    public final Notification onVolumeUnmountable(VolumeInfo volumeInfo) {
        PendingIntent pendingIntent;
        DiskInfo disk = volumeInfo.getDisk();
        String string = this.mContext.getString(17040259, new Object[]{disk.getDescription()});
        String string2 = this.mContext.getString(17040258, new Object[]{disk.getDescription()});
        if (isAutomotive()) {
            pendingIntent = buildUnmountPendingIntent(volumeInfo);
        } else {
            pendingIntent = buildInitPendingIntent(volumeInfo);
        }
        return buildNotificationBuilder(volumeInfo, string, string2).setContentIntent(pendingIntent).setCategory("err").build();
    }

    public final Notification onVolumeRemoved(VolumeInfo volumeInfo) {
        if (!volumeInfo.isPrimary()) {
            return null;
        }
        DiskInfo disk = volumeInfo.getDisk();
        return buildNotificationBuilder(volumeInfo, this.mContext.getString(17040243, new Object[]{disk.getDescription()}), this.mContext.getString(17040242, new Object[]{disk.getDescription()})).setCategory("err").build();
    }

    public final Notification onVolumeBadRemoval(VolumeInfo volumeInfo) {
        if (!volumeInfo.isPrimary()) {
            return null;
        }
        DiskInfo disk = volumeInfo.getDisk();
        return buildNotificationBuilder(volumeInfo, this.mContext.getString(17040227, new Object[]{disk.getDescription()}), this.mContext.getString(17040226, new Object[]{disk.getDescription()})).setCategory("err").build();
    }

    public final void onMoveProgress(MoveInfo moveInfo, int i, long j) {
        String str;
        CharSequence charSequence;
        PendingIntent pendingIntent;
        if (!TextUtils.isEmpty(moveInfo.label)) {
            str = this.mContext.getString(17040236, new Object[]{moveInfo.label});
        } else {
            str = this.mContext.getString(17040239);
        }
        if (j < 0) {
            charSequence = null;
        } else {
            charSequence = DateUtils.formatDuration(j);
        }
        if (moveInfo.packageName != null) {
            pendingIntent = buildWizardMovePendingIntent(moveInfo);
        } else {
            pendingIntent = buildWizardMigratePendingIntent(moveInfo);
        }
        Notification.Builder ongoing = new Notification.Builder(this.mContext, NotificationChannels.STORAGE).setSmallIcon(17302840).setColor(this.mContext.getColor(17170460)).setContentTitle(str).setContentText(charSequence).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setVisibility(1).setLocalOnly(true).setCategory("progress").setProgress(100, i, false).setOngoing(true);
        SystemUIApplication.overrideNotificationAppName(this.mContext, ongoing, false);
        this.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, ongoing.build(), UserHandle.ALL);
    }

    public final void onMoveFinished(MoveInfo moveInfo, int i) {
        String str;
        String str2;
        PendingIntent pendingIntent;
        String str3 = moveInfo.packageName;
        if (str3 != null) {
            this.mNotificationManager.cancelAsUser(str3, 1397575510, UserHandle.ALL);
            return;
        }
        VolumeInfo primaryStorageCurrentVolume = this.mContext.getPackageManager().getPrimaryStorageCurrentVolume();
        String bestVolumeDescription = this.mStorageManager.getBestVolumeDescription(primaryStorageCurrentVolume);
        if (i == -100) {
            str = this.mContext.getString(17040238);
            str2 = this.mContext.getString(17040237, new Object[]{bestVolumeDescription});
        } else {
            str = this.mContext.getString(17040235);
            str2 = this.mContext.getString(17040234);
        }
        if (primaryStorageCurrentVolume == null || primaryStorageCurrentVolume.getDisk() == null) {
            pendingIntent = primaryStorageCurrentVolume != null ? buildVolumeSettingsPendingIntent(primaryStorageCurrentVolume) : null;
        } else {
            pendingIntent = buildWizardReadyPendingIntent(primaryStorageCurrentVolume.getDisk());
        }
        Notification.Builder autoCancel = new Notification.Builder(this.mContext, NotificationChannels.STORAGE).setSmallIcon(17302840).setColor(this.mContext.getColor(17170460)).setContentTitle(str).setContentText(str2).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setAutoCancel(true);
        SystemUIApplication.overrideNotificationAppName(this.mContext, autoCancel, false);
        this.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, autoCancel.build(), UserHandle.ALL);
    }

    public final int getSmallIcon(DiskInfo diskInfo, int i) {
        if (!diskInfo.isSd() && diskInfo.isUsb()) {
            return 17302882;
        }
        return 17302840;
    }

    public final Notification.Builder buildNotificationBuilder(VolumeInfo volumeInfo, CharSequence charSequence, CharSequence charSequence2) {
        Notification.Builder extend = new Notification.Builder(this.mContext, NotificationChannels.STORAGE).setSmallIcon(getSmallIcon(volumeInfo.getDisk(), volumeInfo.getState())).setColor(this.mContext.getColor(17170460)).setContentTitle(charSequence).setContentText(charSequence2).setStyle(new Notification.BigTextStyle().bigText(charSequence2)).setVisibility(1).setLocalOnly(true).extend(new Notification.TvExtender());
        SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
        return extend;
    }

    public final PendingIntent buildInitPendingIntent(DiskInfo diskInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.NEW_STORAGE");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardInit");
        }
        intent.putExtra("android.os.storage.extra.DISK_ID", diskInfo.getId());
        return PendingIntent.getActivityAsUser(this.mContext, diskInfo.getId().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildInitPendingIntent(VolumeInfo volumeInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.NEW_STORAGE");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardInit");
        }
        intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
        return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildUnmountPendingIntent(VolumeInfo volumeInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.UNMOUNT_STORAGE");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
        } else if (isAutomotive()) {
            intent.setClassName("com.android.car.settings", "com.android.car.settings.storage.StorageUnmountReceiver");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getBroadcastAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, UserHandle.CURRENT);
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageUnmountReceiver");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getBroadcastAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, UserHandle.CURRENT);
        }
    }

    public final PendingIntent buildBrowsePendingIntent(VolumeInfo volumeInfo) {
        StrictMode.VmPolicy allowVmViolations = StrictMode.allowVmViolations();
        try {
            Intent buildBrowseIntentForUser = volumeInfo.buildBrowseIntentForUser(volumeInfo.getMountUserId());
            return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), buildBrowseIntentForUser, 335544320, (Bundle) null, UserHandle.CURRENT);
        } finally {
            StrictMode.setVmPolicy(allowVmViolations);
        }
    }

    public final PendingIntent buildVolumeSettingsPendingIntent(VolumeInfo volumeInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("android.settings.INTERNAL_STORAGE_SETTINGS");
        } else if (isAutomotive()) {
            return null;
        } else {
            int type = volumeInfo.getType();
            if (type == 0) {
                intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PublicVolumeSettingsActivity");
            } else if (type != 1) {
                return null;
            } else {
                intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PrivateVolumeSettingsActivity");
            }
        }
        intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
        return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildSnoozeIntent(String str) {
        Intent intent = new Intent("com.android.systemui.action.SNOOZE_VOLUME");
        intent.putExtra("android.os.storage.extra.FS_UUID", str);
        return PendingIntent.getBroadcastAsUser(this.mContext, str.hashCode(), intent, 335544320, UserHandle.CURRENT);
    }

    public final PendingIntent buildForgetPendingIntent(VolumeRecord volumeRecord) {
        Intent intent = new Intent();
        intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PrivateVolumeForgetActivity");
        intent.putExtra("android.os.storage.extra.FS_UUID", volumeRecord.getFsUuid());
        return PendingIntent.getActivityAsUser(this.mContext, volumeRecord.getFsUuid().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildWizardMigratePendingIntent(MoveInfo moveInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.MIGRATE_STORAGE");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardMigrateProgress");
        }
        intent.putExtra("android.content.pm.extra.MOVE_ID", moveInfo.moveId);
        VolumeInfo findVolumeByQualifiedUuid = this.mStorageManager.findVolumeByQualifiedUuid(moveInfo.volumeUuid);
        if (findVolumeByQualifiedUuid != null) {
            intent.putExtra("android.os.storage.extra.VOLUME_ID", findVolumeByQualifiedUuid.getId());
        }
        return PendingIntent.getActivityAsUser(this.mContext, moveInfo.moveId, intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildWizardMovePendingIntent(MoveInfo moveInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.MOVE_APP");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardMoveProgress");
        }
        intent.putExtra("android.content.pm.extra.MOVE_ID", moveInfo.moveId);
        return PendingIntent.getActivityAsUser(this.mContext, moveInfo.moveId, intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final PendingIntent buildWizardReadyPendingIntent(DiskInfo diskInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("android.settings.INTERNAL_STORAGE_SETTINGS");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardReady");
        }
        intent.putExtra("android.os.storage.extra.DISK_ID", diskInfo.getId());
        return PendingIntent.getActivityAsUser(this.mContext, diskInfo.getId().hashCode(), intent, 335544320, (Bundle) null, UserHandle.CURRENT);
    }

    public final boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    public final boolean isTv() {
        return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
    }
}
