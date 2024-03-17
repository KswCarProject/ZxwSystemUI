package com.android.systemui.qs.carrier;

import com.android.systemui.qs.carrier.QSCarrierGroupController;
import dagger.internal.Factory;

public final class QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory implements Factory<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> {

    public static final class InstanceHolder {
        public static final QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory INSTANCE = new QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory();
    }

    public QSCarrierGroupController.SubscriptionManagerSlotIndexResolver get() {
        return newInstance();
    }

    public static QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSCarrierGroupController.SubscriptionManagerSlotIndexResolver newInstance() {
        return new QSCarrierGroupController.SubscriptionManagerSlotIndexResolver();
    }
}
