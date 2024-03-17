package com.android.systemui.statusbar;

import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class SplitShadeLockScreenOverScroller_Factory_Impl implements SplitShadeLockScreenOverScroller.Factory {
    public final C0003SplitShadeLockScreenOverScroller_Factory delegateFactory;

    public SplitShadeLockScreenOverScroller_Factory_Impl(C0003SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScroller_Factory) {
        this.delegateFactory = splitShadeLockScreenOverScroller_Factory;
    }

    public SplitShadeLockScreenOverScroller create(QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return this.delegateFactory.get(qs, notificationStackScrollLayoutController);
    }

    public static Provider<SplitShadeLockScreenOverScroller.Factory> create(C0003SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScroller_Factory) {
        return InstanceFactory.create(new SplitShadeLockScreenOverScroller_Factory_Impl(splitShadeLockScreenOverScroller_Factory));
    }
}
