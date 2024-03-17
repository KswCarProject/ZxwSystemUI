package com.google.android.setupcompat.internal;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;

public class FocusChangedMetricHelper {
    public static final String getScreenName(Activity activity) {
        return activity.getComponentName().toShortString();
    }

    public static final Bundle getExtraBundle(Activity activity, TemplateLayout templateLayout, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString(ResourceEntry.KEY_PACKAGE_NAME, activity.getComponentName().getPackageName());
        bundle.putString("screenName", activity.getComponentName().getShortClassName());
        bundle.putInt("hash", templateLayout.hashCode());
        bundle.putBoolean("focus", z);
        bundle.putLong("timeInMillis", System.currentTimeMillis());
        return bundle;
    }
}
