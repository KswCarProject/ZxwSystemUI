package com.android.systemui.statusbar.policy;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.SmartReplyController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SmartActionInflaterImpl_Factory implements Factory<SmartActionInflaterImpl> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<SmartReplyConstants> constantsProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<SmartReplyController> smartReplyControllerProvider;

    public SmartActionInflaterImpl_Factory(Provider<SmartReplyConstants> provider, Provider<ActivityStarter> provider2, Provider<SmartReplyController> provider3, Provider<HeadsUpManager> provider4) {
        this.constantsProvider = provider;
        this.activityStarterProvider = provider2;
        this.smartReplyControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
    }

    public SmartActionInflaterImpl get() {
        return newInstance(this.constantsProvider.get(), this.activityStarterProvider.get(), this.smartReplyControllerProvider.get(), this.headsUpManagerProvider.get());
    }

    public static SmartActionInflaterImpl_Factory create(Provider<SmartReplyConstants> provider, Provider<ActivityStarter> provider2, Provider<SmartReplyController> provider3, Provider<HeadsUpManager> provider4) {
        return new SmartActionInflaterImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static SmartActionInflaterImpl newInstance(SmartReplyConstants smartReplyConstants, ActivityStarter activityStarter, SmartReplyController smartReplyController, HeadsUpManager headsUpManager) {
        return new SmartActionInflaterImpl(smartReplyConstants, activityStarter, smartReplyController, headsUpManager);
    }
}
