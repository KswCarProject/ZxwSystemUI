package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class TypeClassifier_Factory implements Factory<TypeClassifier> {
    public final Provider<FalsingDataProvider> dataProvider;

    public TypeClassifier_Factory(Provider<FalsingDataProvider> provider) {
        this.dataProvider = provider;
    }

    public TypeClassifier get() {
        return newInstance(this.dataProvider.get());
    }

    public static TypeClassifier_Factory create(Provider<FalsingDataProvider> provider) {
        return new TypeClassifier_Factory(provider);
    }

    public static TypeClassifier newInstance(FalsingDataProvider falsingDataProvider) {
        return new TypeClassifier(falsingDataProvider);
    }
}
