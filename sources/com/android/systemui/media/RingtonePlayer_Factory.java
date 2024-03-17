package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RingtonePlayer_Factory implements Factory<RingtonePlayer> {
    public final Provider<Context> contextProvider;

    public RingtonePlayer_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public RingtonePlayer get() {
        return newInstance(this.contextProvider.get());
    }

    public static RingtonePlayer_Factory create(Provider<Context> provider) {
        return new RingtonePlayer_Factory(provider);
    }

    public static RingtonePlayer newInstance(Context context) {
        return new RingtonePlayer(context);
    }
}
