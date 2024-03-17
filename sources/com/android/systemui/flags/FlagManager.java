package com.android.systemui.flags;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import com.android.systemui.flags.FlagListenable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$BooleanRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FlagManager.kt */
public final class FlagManager implements FlagListenable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public Consumer<Integer> clearCacheAction;
    @NotNull
    public final Context context;
    @NotNull
    public final Handler handler;
    @NotNull
    public final Set<PerFlagListener> listeners;
    @Nullable
    public Consumer<Boolean> onSettingsChangedAction;
    @NotNull
    public final FlagSettingsHelper settings;
    @NotNull
    public final ContentObserver settingsObserver;

    public FlagManager(@NotNull Context context2, @NotNull FlagSettingsHelper flagSettingsHelper, @NotNull Handler handler2) {
        this.context = context2;
        this.settings = flagSettingsHelper;
        this.handler = handler2;
        this.listeners = new LinkedHashSet();
        this.settingsObserver = new SettingsObserver();
    }

    /* compiled from: FlagManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public FlagManager(@NotNull Context context2, @NotNull Handler handler2) {
        this(context2, new FlagSettingsHelper(context2.getContentResolver()), handler2);
    }

    @Nullable
    public final Consumer<Boolean> getOnSettingsChangedAction() {
        return this.onSettingsChangedAction;
    }

    public final void setOnSettingsChangedAction(@Nullable Consumer<Boolean> consumer) {
        this.onSettingsChangedAction = consumer;
    }

    @Nullable
    public final Consumer<Integer> getClearCacheAction() {
        return this.clearCacheAction;
    }

    public final void setClearCacheAction(@Nullable Consumer<Integer> consumer) {
        this.clearCacheAction = consumer;
    }

    @Nullable
    public final <T> T readFlagValue(int i, @NotNull FlagSerializer<T> flagSerializer) {
        return flagSerializer.fromSettingsData(this.settings.getString(idToSettingsKey(i)));
    }

    public void addListener(@NotNull Flag<?> flag, @NotNull FlagListenable.Listener listener) {
        synchronized (this.listeners) {
            boolean isEmpty = this.listeners.isEmpty();
            this.listeners.add(new PerFlagListener(flag.getId(), listener));
            if (isEmpty) {
                this.settings.registerContentObserver("systemui/flags", true, this.settingsObserver);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @NotNull
    public final String idToSettingsKey(int i) {
        return Intrinsics.stringPlus("systemui/flags/", Integer.valueOf(i));
    }

    /* compiled from: FlagManager.kt */
    public final class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super(FlagManager.this.handler);
        }

        public void onChange(boolean z, @Nullable Uri uri) {
            if (uri != null) {
                List<String> pathSegments = uri.getPathSegments();
                try {
                    int parseInt = Integer.parseInt(pathSegments.get(pathSegments.size() - 1));
                    Consumer<Integer> clearCacheAction = FlagManager.this.getClearCacheAction();
                    if (clearCacheAction != null) {
                        clearCacheAction.accept(Integer.valueOf(parseInt));
                    }
                    FlagManager flagManager = FlagManager.this;
                    flagManager.dispatchListenersAndMaybeRestart(parseInt, flagManager.getOnSettingsChangedAction());
                } catch (NumberFormatException unused) {
                }
            }
        }
    }

    public final void dispatchListenersAndMaybeRestart(int i, @Nullable Consumer<Boolean> consumer) {
        ArrayList<FlagListenable.Listener> arrayList;
        synchronized (this.listeners) {
            arrayList = new ArrayList<>();
            for (PerFlagListener perFlagListener : this.listeners) {
                FlagListenable.Listener listener = perFlagListener.getId() == i ? perFlagListener.getListener() : null;
                if (listener != null) {
                    arrayList.add(listener);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10));
            for (FlagListenable.Listener onFlagChanged : arrayList) {
                Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
                onFlagChanged.onFlagChanged(new FlagManager$dispatchListenersAndMaybeRestart$suppressRestartList$1$event$1(i, ref$BooleanRef));
                arrayList2.add(Boolean.valueOf(ref$BooleanRef.element));
            }
            boolean z = true;
            if (!arrayList2.isEmpty()) {
                Iterator it = arrayList2.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (!((Boolean) it.next()).booleanValue()) {
                            z = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (consumer != null) {
                consumer.accept(Boolean.valueOf(z));
            }
        } else if (consumer != null) {
            consumer.accept(Boolean.FALSE);
        }
    }

    /* compiled from: FlagManager.kt */
    public static final class PerFlagListener {
        public final int id;
        @NotNull
        public final FlagListenable.Listener listener;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PerFlagListener)) {
                return false;
            }
            PerFlagListener perFlagListener = (PerFlagListener) obj;
            return this.id == perFlagListener.id && Intrinsics.areEqual((Object) this.listener, (Object) perFlagListener.listener);
        }

        public int hashCode() {
            return (Integer.hashCode(this.id) * 31) + this.listener.hashCode();
        }

        @NotNull
        public String toString() {
            return "PerFlagListener(id=" + this.id + ", listener=" + this.listener + ')';
        }

        public PerFlagListener(int i, @NotNull FlagListenable.Listener listener2) {
            this.id = i;
            this.listener = listener2;
        }

        public final int getId() {
            return this.id;
        }

        @NotNull
        public final FlagListenable.Listener getListener() {
            return this.listener;
        }
    }
}
