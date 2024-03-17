package com.android.systemui.statusbar.notification;

import dagger.internal.Factory;

public final class SectionClassifier_Factory implements Factory<SectionClassifier> {

    public static final class InstanceHolder {
        public static final SectionClassifier_Factory INSTANCE = new SectionClassifier_Factory();
    }

    public SectionClassifier get() {
        return newInstance();
    }

    public static SectionClassifier_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SectionClassifier newInstance() {
        return new SectionClassifier();
    }
}
