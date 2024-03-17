package com.android.systemui.qs;

import dagger.internal.Factory;

public final class QSExpansionPathInterpolator_Factory implements Factory<QSExpansionPathInterpolator> {

    public static final class InstanceHolder {
        public static final QSExpansionPathInterpolator_Factory INSTANCE = new QSExpansionPathInterpolator_Factory();
    }

    public QSExpansionPathInterpolator get() {
        return newInstance();
    }

    public static QSExpansionPathInterpolator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSExpansionPathInterpolator newInstance() {
        return new QSExpansionPathInterpolator();
    }
}
