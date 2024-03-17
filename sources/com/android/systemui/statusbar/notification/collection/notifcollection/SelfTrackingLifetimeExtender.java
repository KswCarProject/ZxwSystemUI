package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SelfTrackingLifetimeExtender.kt */
public abstract class SelfTrackingLifetimeExtender implements NotifLifetimeExtender, Dumpable {
    public final boolean debug;
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback mCallback;
    public boolean mEnding;
    @NotNull
    public final ArrayMap<String, NotificationEntry> mEntriesExtended = new ArrayMap<>();
    @NotNull
    public final Handler mainHandler;
    @NotNull
    public final String name;
    @NotNull
    public final String tag;

    public void onCanceledLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
    }

    public void onStartedLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
    }

    public abstract boolean queryShouldExtendLifetime(@NotNull NotificationEntry notificationEntry);

    public SelfTrackingLifetimeExtender(@NotNull String str, @NotNull String str2, boolean z, @NotNull Handler handler) {
        this.tag = str;
        this.name = str2;
        this.debug = z;
        this.mainHandler = handler;
    }

    public final void warnIfEnding() {
        if (this.debug && this.mEnding) {
            Log.w(this.tag, "reentrant code while ending a lifetime extension");
        }
    }

    public final void endAllLifetimeExtensions() {
        List<NotificationEntry> list = CollectionsKt___CollectionsKt.toList(this.mEntriesExtended.values());
        if (this.debug) {
            String str = this.tag;
            Log.d(str, this.name + ".endAllLifetimeExtensions() entries=" + list);
        }
        this.mEntriesExtended.clear();
        warnIfEnding();
        this.mEnding = true;
        for (NotificationEntry notificationEntry : list) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                onEndLifetimeExtensionCallback = null;
            }
            onEndLifetimeExtensionCallback.onEndLifetimeExtension(this, notificationEntry);
        }
        this.mEnding = false;
    }

    public final void endLifetimeExtensionAfterDelay(@NotNull String str, long j) {
        if (this.debug) {
            String str2 = this.tag;
            Log.d(str2, this.name + ".endLifetimeExtensionAfterDelay(key=" + str + ", delayMillis=" + j + ") isExtending=" + isExtending(str));
        }
        if (isExtending(str)) {
            this.mainHandler.postDelayed(new SelfTrackingLifetimeExtender$endLifetimeExtensionAfterDelay$1(this, str), j);
        }
    }

    public final void endLifetimeExtension(@NotNull String str) {
        if (this.debug) {
            String str2 = this.tag;
            Log.d(str2, this.name + ".endLifetimeExtension(key=" + str + ") isExtending=" + isExtending(str));
        }
        warnIfEnding();
        this.mEnding = true;
        NotificationEntry remove = this.mEntriesExtended.remove(str);
        if (remove != null) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                onEndLifetimeExtensionCallback = null;
            }
            onEndLifetimeExtensionCallback.onEndLifetimeExtension(this, remove);
        }
        this.mEnding = false;
    }

    public final boolean isExtending(@NotNull String str) {
        return this.mEntriesExtended.containsKey(str);
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final boolean maybeExtendLifetime(@NotNull NotificationEntry notificationEntry, int i) {
        boolean queryShouldExtendLifetime = queryShouldExtendLifetime(notificationEntry);
        if (this.debug) {
            String str = this.tag;
            Log.d(str, this.name + ".shouldExtendLifetime(key=" + notificationEntry.getKey() + ", reason=" + i + ") isExtending=" + isExtending(notificationEntry.getKey()) + " shouldExtend=" + queryShouldExtendLifetime);
        }
        warnIfEnding();
        if (queryShouldExtendLifetime && this.mEntriesExtended.put(notificationEntry.getKey(), notificationEntry) == null) {
            onStartedLifetimeExtension(notificationEntry);
        }
        return queryShouldExtendLifetime;
    }

    public final void cancelLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
        if (this.debug) {
            String str = this.tag;
            Log.d(str, this.name + ".cancelLifetimeExtension(key=" + notificationEntry.getKey() + ") isExtending=" + isExtending(notificationEntry.getKey()));
        }
        warnIfEnding();
        this.mEntriesExtended.remove(notificationEntry.getKey());
        onCanceledLifetimeExtension(notificationEntry);
    }

    public final void setCallback(@NotNull NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        this.mCallback = onEndLifetimeExtensionCallback;
    }

    public final void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("LifetimeExtender: " + this.name + ':');
        printWriter.println(Intrinsics.stringPlus("  mEntriesExtended: ", Integer.valueOf(this.mEntriesExtended.size())));
        for (Map.Entry<String, NotificationEntry> key : this.mEntriesExtended.entrySet()) {
            printWriter.println(Intrinsics.stringPlus("  * ", key.getKey()));
        }
    }
}
