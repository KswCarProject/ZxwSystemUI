package com.google.android.setupcompat.logging;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.Validations;
import com.google.android.setupcompat.util.ObjectUtils;
import java.util.regex.Pattern;

public final class MetricKey implements Parcelable {
    public static final Parcelable.Creator<MetricKey> CREATOR = new Parcelable.Creator<MetricKey>() {
        public MetricKey createFromParcel(Parcel parcel) {
            return new MetricKey(parcel.readString(), parcel.readString());
        }

        public MetricKey[] newArray(int i) {
            return new MetricKey[i];
        }
    };
    public static final Pattern METRIC_KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]+");
    public static final Pattern SCREEN_COMPONENTNAME_PATTERN = Pattern.compile("^([a-z]+[.])+[A-Z][a-zA-Z0-9]+");
    public static final Pattern SCREEN_NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]+");
    public final String name;
    public final String screenName;

    public int describeContents() {
        return 0;
    }

    public static MetricKey get(String str, Activity activity) {
        String className = activity.getComponentName().getClassName();
        Validations.assertLengthInRange(str, "MetricKey.name", 5, 30);
        Preconditions.checkArgument(METRIC_KEY_PATTERN.matcher(str).matches(), "Invalid MetricKey, only alpha numeric characters are allowed.");
        return new MetricKey(str, className);
    }

    public static Bundle fromMetricKey(MetricKey metricKey) {
        Preconditions.checkNotNull(metricKey, "MetricKey cannot be null.");
        Bundle bundle = new Bundle();
        bundle.putInt("MetricKey_version", 1);
        bundle.putString("MetricKey_name", metricKey.name());
        bundle.putString("MetricKey_screenName", metricKey.screenName());
        return bundle;
    }

    public String name() {
        return this.name;
    }

    public String screenName() {
        return this.screenName;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.screenName);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MetricKey)) {
            return false;
        }
        MetricKey metricKey = (MetricKey) obj;
        if (!ObjectUtils.equals(this.name, metricKey.name) || !ObjectUtils.equals(this.screenName, metricKey.screenName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ObjectUtils.hashCode(this.name, this.screenName);
    }

    public MetricKey(String str, String str2) {
        this.name = str;
        this.screenName = str2;
    }
}
