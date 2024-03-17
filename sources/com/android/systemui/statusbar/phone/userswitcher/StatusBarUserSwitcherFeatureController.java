package com.android.systemui.statusbar.phone.userswitcher;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.FlagListenable;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarUserSwitcherFeatureController.kt */
public final class StatusBarUserSwitcherFeatureController implements CallbackController<OnUserSwitcherPreferenceChangeListener> {
    @NotNull
    public final FeatureFlags flags;
    @NotNull
    public final List<OnUserSwitcherPreferenceChangeListener> listeners = new ArrayList();

    public StatusBarUserSwitcherFeatureController(@NotNull FeatureFlags featureFlags) {
        this.flags = featureFlags;
        featureFlags.addListener(Flags.STATUS_BAR_USER_SWITCHER, new FlagListenable.Listener(this) {
            public final /* synthetic */ StatusBarUserSwitcherFeatureController this$0;

            {
                this.this$0 = r1;
            }

            public final void onFlagChanged(@NotNull FlagListenable.FlagEvent flagEvent) {
                flagEvent.requestNoRestart();
                this.this$0.notifyListeners();
            }
        });
    }

    public final boolean isStatusBarUserSwitcherFeatureEnabled() {
        return this.flags.isEnabled(Flags.STATUS_BAR_USER_SWITCHER);
    }

    public void addCallback(@NotNull OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        if (!this.listeners.contains(onUserSwitcherPreferenceChangeListener)) {
            this.listeners.add(onUserSwitcherPreferenceChangeListener);
        }
    }

    public void removeCallback(@NotNull OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        this.listeners.remove(onUserSwitcherPreferenceChangeListener);
    }

    public final void notifyListeners() {
        boolean isEnabled = this.flags.isEnabled(Flags.STATUS_BAR_USER_SWITCHER);
        for (OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChange : this.listeners) {
            onUserSwitcherPreferenceChange.onUserSwitcherPreferenceChange(isEnabled);
        }
    }
}
