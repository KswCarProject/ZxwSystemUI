package com.android.systemui.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.Assert;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl extends BroadcastReceiver implements UserTracker, Dumpable {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(UserTrackerImpl.class, "userId", "getUserId()I", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(UserTrackerImpl.class, "userHandle", "getUserHandle()Landroid/os/UserHandle;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(UserTrackerImpl.class, "userContext", "getUserContext()Landroid/content/Context;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(UserTrackerImpl.class, "userProfiles", "getUserProfiles()Ljava/util/List;", 0))};
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Handler backgroundHandler;
    @NotNull
    public final List<DataItem> callbacks;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    public boolean initialized;
    @NotNull
    public final Object mutex = new Object();
    @NotNull
    public final SynchronizedDelegate userContext$delegate;
    @NotNull
    public final SynchronizedDelegate userHandle$delegate;
    @NotNull
    public final SynchronizedDelegate userId$delegate;
    @NotNull
    public final UserManager userManager;
    @NotNull
    public final SynchronizedDelegate userProfiles$delegate;

    public UserTrackerImpl(@NotNull Context context2, @NotNull UserManager userManager2, @NotNull DumpManager dumpManager2, @NotNull Handler handler) {
        this.context = context2;
        this.userManager = userManager2;
        this.dumpManager = dumpManager2;
        this.backgroundHandler = handler;
        this.userId$delegate = new SynchronizedDelegate(Integer.valueOf(context2.getUserId()));
        this.userHandle$delegate = new SynchronizedDelegate(context2.getUser());
        this.userContext$delegate = new SynchronizedDelegate(context2);
        this.userProfiles$delegate = new SynchronizedDelegate(CollectionsKt__CollectionsKt.emptyList());
        this.callbacks = new ArrayList();
    }

    /* compiled from: UserTrackerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final boolean getInitialized() {
        return this.initialized;
    }

    public int getUserId() {
        return ((Number) this.userId$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[0])).intValue();
    }

    public final void setUserId(int i) {
        this.userId$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[0], Integer.valueOf(i));
    }

    @NotNull
    public UserHandle getUserHandle() {
        return (UserHandle) this.userHandle$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[1]);
    }

    public final void setUserHandle(UserHandle userHandle) {
        this.userHandle$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[1], userHandle);
    }

    @NotNull
    public Context getUserContext() {
        return (Context) this.userContext$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[2]);
    }

    public final void setUserContext(Context context2) {
        this.userContext$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[2], context2);
    }

    @NotNull
    public UserInfo getUserInfo() {
        boolean z;
        int userId = getUserId();
        for (UserInfo userInfo : getUserProfiles()) {
            if (userInfo.id == userId) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                return userInfo;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    @NotNull
    public List<UserInfo> getUserProfiles() {
        return (List) this.userProfiles$delegate.getValue(this, (KProperty<?>) $$delegatedProperties[3]);
    }

    public final void setUserProfiles(List<? extends UserInfo> list) {
        this.userProfiles$delegate.setValue(this, (KProperty<?>) $$delegatedProperties[3], list);
    }

    public final void initialize(int i) {
        if (!this.initialized) {
            this.initialized = true;
            setUserIdInternal(i);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
            intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNLOCKED");
            this.context.registerReceiverForAllUsers(this, intentFilter, (String) null, this.backgroundHandler);
            this.dumpManager.registerDumpable("UserTrackerImpl", this);
        }
    }

    public void onReceive(@NotNull Context context2, @NotNull Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action.hashCode()) {
                case -1462075554:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_UNLOCKED")) {
                        return;
                    }
                    break;
                case -1238404651:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        return;
                    }
                    break;
                case -864107122:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        return;
                    }
                    break;
                case 959232034:
                    if (action.equals("android.intent.action.USER_SWITCHED")) {
                        handleSwitchUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
                        return;
                    }
                    return;
                case 1051477093:
                    if (!action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                        return;
                    }
                    break;
                default:
                    return;
            }
            handleProfilesChanged();
        }
    }

    @NotNull
    public Context createCurrentUserContext(@NotNull Context context2) {
        Context createContextAsUser;
        synchronized (this.mutex) {
            createContextAsUser = context2.createContextAsUser(getUserHandle(), 0);
        }
        return createContextAsUser;
    }

    public final Pair<Context, List<UserInfo>> setUserIdInternal(int i) {
        List profiles = this.userManager.getProfiles(i);
        UserHandle userHandle = new UserHandle(i);
        Context createContextAsUser = this.context.createContextAsUser(userHandle, 0);
        synchronized (this.mutex) {
            setUserId(i);
            setUserHandle(userHandle);
            setUserContext(createContextAsUser);
            Iterable<UserInfo> iterable = profiles;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles(arrayList);
            Unit unit = Unit.INSTANCE;
        }
        return TuplesKt.to(createContextAsUser, profiles);
    }

    public final void handleSwitchUser(int i) {
        List<DataItem> list;
        Assert.isNotMainThread();
        if (i == -10000) {
            Log.w("UserTrackerImpl", "handleSwitchUser - Couldn't get new id from intent");
        } else if (i != getUserId()) {
            Log.i("UserTrackerImpl", Intrinsics.stringPlus("Switching to user ", Integer.valueOf(i)));
            Pair<Context, List<UserInfo>> userIdInternal = setUserIdInternal(i);
            Context component1 = userIdInternal.component1();
            List component2 = userIdInternal.component2();
            synchronized (this.callbacks) {
                list = CollectionsKt___CollectionsKt.toList(this.callbacks);
            }
            for (DataItem dataItem : list) {
                if (dataItem.getCallback().get() != null) {
                    dataItem.getExecutor().execute(new UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1(dataItem, i, component1, component2));
                }
            }
        }
    }

    public final void handleProfilesChanged() {
        List<DataItem> list;
        Assert.isNotMainThread();
        List profiles = this.userManager.getProfiles(getUserId());
        synchronized (this.mutex) {
            Iterable<UserInfo> iterable = profiles;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(new UserInfo(userInfo));
            }
            setUserProfiles(arrayList);
            Unit unit = Unit.INSTANCE;
        }
        synchronized (this.callbacks) {
            list = CollectionsKt___CollectionsKt.toList(this.callbacks);
        }
        for (DataItem dataItem : list) {
            if (dataItem.getCallback().get() != null) {
                dataItem.getExecutor().execute(new UserTrackerImpl$handleProfilesChanged$$inlined$notifySubscribers$1(dataItem, profiles));
            }
        }
    }

    public void addCallback(@NotNull UserTracker.Callback callback, @NotNull Executor executor) {
        synchronized (this.callbacks) {
            this.callbacks.add(new DataItem(new WeakReference(callback), executor));
        }
    }

    public void removeCallback(@NotNull UserTracker.Callback callback) {
        synchronized (this.callbacks) {
            this.callbacks.removeIf(new UserTrackerImpl$removeCallback$1$1(callback));
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        List<DataItem> list;
        printWriter.println(Intrinsics.stringPlus("Initialized: ", Boolean.valueOf(this.initialized)));
        if (this.initialized) {
            printWriter.println(Intrinsics.stringPlus("userId: ", Integer.valueOf(getUserId())));
            Iterable<UserInfo> userProfiles = getUserProfiles();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo fullString : userProfiles) {
                arrayList.add(fullString.toFullString());
            }
            printWriter.println(Intrinsics.stringPlus("userProfiles: ", arrayList));
        }
        synchronized (this.callbacks) {
            list = CollectionsKt___CollectionsKt.toList(this.callbacks);
        }
        printWriter.println("Callbacks:");
        for (DataItem callback : list) {
            UserTracker.Callback callback2 = (UserTracker.Callback) callback.getCallback().get();
            if (callback2 != null) {
                printWriter.println(Intrinsics.stringPlus("  ", callback2));
            }
        }
    }

    /* compiled from: UserTrackerImpl.kt */
    public static final class SynchronizedDelegate<T> implements ReadWriteProperty<UserTrackerImpl, T> {
        @NotNull
        public T value;

        public SynchronizedDelegate(@NotNull T t) {
            this.value = t;
        }

        @NotNull
        public T getValue(@NotNull UserTrackerImpl userTrackerImpl, @NotNull KProperty<?> kProperty) {
            T t;
            if (userTrackerImpl.getInitialized()) {
                synchronized (userTrackerImpl.mutex) {
                    t = this.value;
                }
                return t;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Must initialize before getting ", kProperty.getName()));
        }

        public void setValue(@NotNull UserTrackerImpl userTrackerImpl, @NotNull KProperty<?> kProperty, @NotNull T t) {
            synchronized (userTrackerImpl.mutex) {
                this.value = t;
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
