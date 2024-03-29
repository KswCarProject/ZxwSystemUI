package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class SingleTapClassifier_Factory implements Factory<SingleTapClassifier> {
    public final Provider<FalsingDataProvider> dataProvider;
    public final Provider<Float> touchSlopProvider;

    public SingleTapClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<Float> provider2) {
        this.dataProvider = provider;
        this.touchSlopProvider = provider2;
    }

    public SingleTapClassifier get() {
        return newInstance(this.dataProvider.get(), this.touchSlopProvider.get().floatValue());
    }

    public static SingleTapClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<Float> provider2) {
        return new SingleTapClassifier_Factory(provider, provider2);
    }

    public static SingleTapClassifier newInstance(FalsingDataProvider falsingDataProvider, float f) {
        return new SingleTapClassifier(falsingDataProvider, f);
    }
}
