package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger {
    @NotNull
    public final LogBuffer buffer;

    public ShadeListBuilderLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logOnBuildList() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logOnBuildList$2.INSTANCE));
    }

    public final void logEndBuildList(int i, int i2, int i3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logEndBuildList$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setInt1(i2);
        obtain.setInt2(i3);
        logBuffer.commit(obtain);
    }

    public final void logPreRenderInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPreRenderInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPreGroupFilterInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPreGroupFilterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logReorderingAllowedInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logReorderingAllowedInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPromoterInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logPromoterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifSectionInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logNotifSectionInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifComparatorInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logNotifComparatorInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logFinalizeFilterInvalidated(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalizeFilterInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateSummary(int i, @NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.WARNING, ShadeListBuilderLogger$logDuplicateSummary$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateTopLevelKey(int i, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.WARNING, ShadeListBuilderLogger$logDuplicateTopLevelKey$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logEntryAttachStateChanged(int i, @NotNull String str, @Nullable GroupEntry groupEntry, @Nullable GroupEntry groupEntry2) {
        String str2;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logEntryAttachStateChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        String str3 = null;
        if (groupEntry == null) {
            str2 = null;
        } else {
            str2 = groupEntry.getKey();
        }
        obtain.setStr2(str2);
        if (groupEntry2 != null) {
            str3 = groupEntry2.getKey();
        }
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logParentChanged(int i, @Nullable GroupEntry groupEntry, @Nullable GroupEntry groupEntry2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logParentChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (groupEntry == null) {
            str = null;
        } else {
            str = groupEntry.getKey();
        }
        obtain.setStr1(str);
        if (groupEntry2 != null) {
            str2 = groupEntry2.getKey();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logParentChangeSuppressed(int i, @Nullable GroupEntry groupEntry, @Nullable GroupEntry groupEntry2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logParentChangeSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (groupEntry == null) {
            str = null;
        } else {
            str = groupEntry.getKey();
        }
        obtain.setStr1(str);
        if (groupEntry2 != null) {
            str2 = groupEntry2.getKey();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logGroupPruningSuppressed(int i, @Nullable GroupEntry groupEntry) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logGroupPruningSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        if (groupEntry == null) {
            str = null;
        } else {
            str = groupEntry.getKey();
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPrunedReasonChanged(int i, @Nullable String str, @Nullable String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPrunedReasonChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logFilterChanged(int i, @Nullable NotifFilter notifFilter, @Nullable NotifFilter notifFilter2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logFilterChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (notifFilter == null) {
            str = null;
        } else {
            str = notifFilter.getName();
        }
        obtain.setStr1(str);
        if (notifFilter2 != null) {
            str2 = notifFilter2.getName();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logPromoterChanged(int i, @Nullable NotifPromoter notifPromoter, @Nullable NotifPromoter notifPromoter2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPromoterChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (notifPromoter == null) {
            str = null;
        } else {
            str = notifPromoter.getName();
        }
        obtain.setStr1(str);
        if (notifPromoter2 != null) {
            str2 = notifPromoter2.getName();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logSectionChanged(int i, @Nullable NotifSection notifSection, @Nullable NotifSection notifSection2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logSectionChanged$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (notifSection == null) {
            str = null;
        } else {
            str = notifSection.getLabel();
        }
        obtain.setStr1(str);
        if (notifSection2 != null) {
            str2 = notifSection2.getLabel();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logSectionChangeSuppressed(int i, @Nullable NotifSection notifSection, @Nullable NotifSection notifSection2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logSectionChangeSuppressed$2.INSTANCE);
        obtain.setLong1((long) i);
        String str2 = null;
        if (notifSection == null) {
            str = null;
        } else {
            str = notifSection.getLabel();
        }
        obtain.setStr1(str);
        if (notifSection2 != null) {
            str2 = notifSection2.getLabel();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logFinalList(@NotNull List<? extends ListEntry> list) {
        if (list.isEmpty()) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$2.INSTANCE));
        }
        int size = list.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            ListEntry listEntry = (ListEntry) list.get(i);
            LogBuffer logBuffer2 = this.buffer;
            LogLevel logLevel = LogLevel.DEBUG;
            LogMessageImpl obtain = logBuffer2.obtain("ShadeListBuilder", logLevel, ShadeListBuilderLogger$logFinalList$4.INSTANCE);
            obtain.setInt1(i);
            obtain.setStr1(listEntry.getKey());
            logBuffer2.commit(obtain);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    LogBuffer logBuffer3 = this.buffer;
                    LogMessageImpl obtain2 = logBuffer3.obtain("ShadeListBuilder", logLevel, ShadeListBuilderLogger$logFinalList$5$2.INSTANCE);
                    obtain2.setStr1(summary.getKey());
                    logBuffer3.commit(obtain2);
                }
                int size2 = groupEntry.getChildren().size();
                for (int i3 = 0; i3 < size2; i3++) {
                    LogBuffer logBuffer4 = this.buffer;
                    LogMessageImpl obtain3 = logBuffer4.obtain("ShadeListBuilder", LogLevel.DEBUG, ShadeListBuilderLogger$logFinalList$7.INSTANCE);
                    obtain3.setInt1(i3);
                    obtain3.setStr1(groupEntry.getChildren().get(i3).getKey());
                    logBuffer4.commit(obtain3);
                }
            }
            i = i2;
        }
    }

    public final void logPipelineRunSuppressed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeListBuilder", LogLevel.INFO, ShadeListBuilderLogger$logPipelineRunSuppressed$2.INSTANCE));
    }
}
