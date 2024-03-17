package com.android.systemui.statusbar.phone.userswitcher;

import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.UserInfoController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarUserInfoTracker.kt */
public final class StatusBarUserInfoTracker implements CallbackController<CurrentUserChipInfoUpdatedListener>, Dumpable {
    @NotNull
    public final Executor backgroundExecutor;
    @Nullable
    public Drawable currentUserAvatar;
    @Nullable
    public String currentUserName;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final List<CurrentUserChipInfoUpdatedListener> listeners = new ArrayList();
    public boolean listening;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final UserInfoController.OnUserInfoChangedListener userInfoChangedListener = new StatusBarUserInfoTracker$userInfoChangedListener$1(this);
    @NotNull
    public final UserInfoController userInfoController;
    @NotNull
    public final UserManager userManager;
    public boolean userSwitcherEnabled;

    public StatusBarUserInfoTracker(@NotNull UserInfoController userInfoController2, @NotNull UserManager userManager2, @NotNull DumpManager dumpManager2, @NotNull Executor executor, @NotNull Executor executor2) {
        this.userInfoController = userInfoController2;
        this.userManager = userManager2;
        this.dumpManager = dumpManager2;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        dumpManager2.registerDumpable("StatusBarUserInfoTracker", this);
    }

    @Nullable
    public final String getCurrentUserName() {
        return this.currentUserName;
    }

    @Nullable
    public final Drawable getCurrentUserAvatar() {
        return this.currentUserAvatar;
    }

    public final boolean getUserSwitcherEnabled() {
        return this.userSwitcherEnabled;
    }

    public void addCallback(@NotNull CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        if (this.listeners.isEmpty()) {
            startListening();
        }
        if (!this.listeners.contains(currentUserChipInfoUpdatedListener)) {
            this.listeners.add(currentUserChipInfoUpdatedListener);
        }
    }

    public void removeCallback(@NotNull CurrentUserChipInfoUpdatedListener currentUserChipInfoUpdatedListener) {
        this.listeners.remove(currentUserChipInfoUpdatedListener);
        if (this.listeners.isEmpty()) {
            stopListening();
        }
    }

    public final void notifyListenersUserInfoChanged() {
        for (CurrentUserChipInfoUpdatedListener onCurrentUserChipInfoUpdated : this.listeners) {
            onCurrentUserChipInfoUpdated.onCurrentUserChipInfoUpdated();
        }
    }

    public final void notifyListenersSettingChanged() {
        for (CurrentUserChipInfoUpdatedListener onStatusBarUserSwitcherSettingChanged : this.listeners) {
            onStatusBarUserSwitcherSettingChanged.onStatusBarUserSwitcherSettingChanged(getUserSwitcherEnabled());
        }
    }

    public final void startListening() {
        this.listening = true;
        this.userInfoController.addCallback(this.userInfoChangedListener);
    }

    public final void stopListening() {
        this.listening = false;
        this.userInfoController.removeCallback(this.userInfoChangedListener);
    }

    public final void checkEnabled() {
        this.backgroundExecutor.execute(new StatusBarUserInfoTracker$checkEnabled$1(this));
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("  userSwitcherEnabled=", Boolean.valueOf(this.userSwitcherEnabled)));
        printWriter.println(Intrinsics.stringPlus("  listening=", Boolean.valueOf(this.listening)));
    }
}
