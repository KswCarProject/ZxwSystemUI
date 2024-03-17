package com.android.systemui.privacy.logging;

import android.permission.PermissionGroupUsage;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyLogger.kt */
public final class PrivacyLogger {
    @NotNull
    public final LogBuffer buffer;

    public PrivacyLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logUpdatedItemFromAppOps(int i, int i2, @NotNull String str, boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logUpdatedItemFromAppOps$2 privacyLogger$logUpdatedItemFromAppOps$2 = PrivacyLogger$logUpdatedItemFromAppOps$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUpdatedItemFromAppOps$2);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logUpdatedItemFromMediaProjection(int i, @NotNull String str, boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logUpdatedItemFromMediaProjection$2 privacyLogger$logUpdatedItemFromMediaProjection$2 = PrivacyLogger$logUpdatedItemFromMediaProjection$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUpdatedItemFromMediaProjection$2);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logRetrievedPrivacyItemsList(@NotNull List<PrivacyItem> list) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logRetrievedPrivacyItemsList$2 privacyLogger$logRetrievedPrivacyItemsList$2 = PrivacyLogger$logRetrievedPrivacyItemsList$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logRetrievedPrivacyItemsList$2);
        obtain.setStr1(listToString(list));
        logBuffer.commit(obtain);
    }

    public final void logPrivacyItemsToHold(@NotNull List<PrivacyItem> list) {
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logPrivacyItemsToHold$2 privacyLogger$logPrivacyItemsToHold$2 = PrivacyLogger$logPrivacyItemsToHold$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsToHold$2);
        obtain.setStr1(listToString(list));
        logBuffer.commit(obtain);
    }

    public final void logPrivacyItemsUpdateScheduled(long j) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyItemsUpdateScheduled$2 privacyLogger$logPrivacyItemsUpdateScheduled$2 = PrivacyLogger$logPrivacyItemsUpdateScheduled$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsUpdateScheduled$2);
        obtain.setStr1(PrivacyLoggerKt.DATE_FORMAT.format(Long.valueOf(System.currentTimeMillis() + j)));
        logBuffer.commit(obtain);
    }

    public final void logCurrentProfilesChanged(@NotNull List<Integer> list) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logCurrentProfilesChanged$2 privacyLogger$logCurrentProfilesChanged$2 = PrivacyLogger$logCurrentProfilesChanged$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logCurrentProfilesChanged$2);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logChipVisible(boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logChipVisible$2 privacyLogger$logChipVisible$2 = PrivacyLogger$logChipVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logChipVisible$2);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logStatusBarIconsVisible(boolean z, boolean z2, boolean z3) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStatusBarIconsVisible$2 privacyLogger$logStatusBarIconsVisible$2 = PrivacyLogger$logStatusBarIconsVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStatusBarIconsVisible$2);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setBool3(z3);
        logBuffer.commit(obtain);
    }

    public final void logUnfilteredPermGroupUsage(@NotNull List<PermissionGroupUsage> list) {
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logUnfilteredPermGroupUsage$2 privacyLogger$logUnfilteredPermGroupUsage$2 = PrivacyLogger$logUnfilteredPermGroupUsage$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUnfilteredPermGroupUsage$2);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logShowDialogContents(@NotNull List<PrivacyDialog.PrivacyElement> list) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logShowDialogContents$2 privacyLogger$logShowDialogContents$2 = PrivacyLogger$logShowDialogContents$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logShowDialogContents$2);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logPrivacyDialogDismissed() {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyDialogDismissed$2 privacyLogger$logPrivacyDialogDismissed$2 = PrivacyLogger$logPrivacyDialogDismissed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyDialogDismissed$2));
    }

    public final void logStartSettingsActivityFromDialog(@NotNull String str, int i) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStartSettingsActivityFromDialog$2 privacyLogger$logStartSettingsActivityFromDialog$2 = PrivacyLogger$logStartSettingsActivityFromDialog$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStartSettingsActivityFromDialog$2);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final String listToString(List<PrivacyItem> list) {
        return CollectionsKt___CollectionsKt.joinToString$default(list, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, PrivacyLogger$listToString$1.INSTANCE, 30, (Object) null);
    }
}
