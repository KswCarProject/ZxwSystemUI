package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.flags.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifPipelineFlags_Factory implements Factory<NotifPipelineFlags> {
    public final Provider<Context> contextProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;

    public NotifPipelineFlags_Factory(Provider<Context> provider, Provider<FeatureFlags> provider2) {
        this.contextProvider = provider;
        this.featureFlagsProvider = provider2;
    }

    public NotifPipelineFlags get() {
        return newInstance(this.contextProvider.get(), this.featureFlagsProvider.get());
    }

    public static NotifPipelineFlags_Factory create(Provider<Context> provider, Provider<FeatureFlags> provider2) {
        return new NotifPipelineFlags_Factory(provider, provider2);
    }

    public static NotifPipelineFlags newInstance(Context context, FeatureFlags featureFlags) {
        return new NotifPipelineFlags(context, featureFlags);
    }
}
