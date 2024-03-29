package com.android.systemui.util.time;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DateFormatUtil_Factory implements Factory<DateFormatUtil> {
    public final Provider<Context> contextProvider;

    public DateFormatUtil_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public DateFormatUtil get() {
        return newInstance(this.contextProvider.get());
    }

    public static DateFormatUtil_Factory create(Provider<Context> provider) {
        return new DateFormatUtil_Factory(provider);
    }

    public static DateFormatUtil newInstance(Context context) {
        return new DateFormatUtil(context);
    }
}
