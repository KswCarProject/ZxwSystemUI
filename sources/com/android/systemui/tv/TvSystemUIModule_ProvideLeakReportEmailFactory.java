package com.android.systemui.tv;

import dagger.internal.Factory;

public final class TvSystemUIModule_ProvideLeakReportEmailFactory implements Factory<String> {

    public static final class InstanceHolder {
        public static final TvSystemUIModule_ProvideLeakReportEmailFactory INSTANCE = new TvSystemUIModule_ProvideLeakReportEmailFactory();
    }

    public String get() {
        return provideLeakReportEmail();
    }

    public static TvSystemUIModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return TvSystemUIModule.provideLeakReportEmail();
    }
}
