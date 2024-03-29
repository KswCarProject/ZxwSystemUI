package com.google.android.setupcompat.portal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class NotificationComponent implements Parcelable {
    public static final Parcelable.Creator<NotificationComponent> CREATOR = new Parcelable.Creator<NotificationComponent>() {
        public NotificationComponent createFromParcel(Parcel parcel) {
            return new NotificationComponent(parcel);
        }

        public NotificationComponent[] newArray(int i) {
            return new NotificationComponent[i];
        }
    };
    public Bundle extraBundle;
    public final int notificationType;

    public int describeContents() {
        return 0;
    }

    public NotificationComponent(int i) {
        this.extraBundle = new Bundle();
        this.notificationType = i;
    }

    public NotificationComponent(Parcel parcel) {
        this(parcel.readInt());
        this.extraBundle = parcel.readBundle(Bundle.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.notificationType);
        parcel.writeBundle(this.extraBundle);
    }
}
