package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.Preconditions;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.ArrayList;
import java.util.List;

public final class CompositeDateValidator implements CalendarConstraints.DateValidator {
    public static final Operator ALL_OPERATOR = new Operator() {
        public int getId() {
            return 2;
        }

        public boolean isValid(List<CalendarConstraints.DateValidator> list, long j) {
            for (CalendarConstraints.DateValidator next : list) {
                if (next != null && !next.isValid(j)) {
                    return false;
                }
            }
            return true;
        }
    };
    public static final Operator ANY_OPERATOR = new Operator() {
        public int getId() {
            return 1;
        }

        public boolean isValid(List<CalendarConstraints.DateValidator> list, long j) {
            for (CalendarConstraints.DateValidator next : list) {
                if (next != null && next.isValid(j)) {
                    return true;
                }
            }
            return false;
        }
    };
    public static final Parcelable.Creator<CompositeDateValidator> CREATOR = new Parcelable.Creator<CompositeDateValidator>() {
        public CompositeDateValidator createFromParcel(Parcel parcel) {
            Operator operator;
            ArrayList readArrayList = parcel.readArrayList(CalendarConstraints.DateValidator.class.getClassLoader());
            int readInt = parcel.readInt();
            if (readInt == 2) {
                operator = CompositeDateValidator.ALL_OPERATOR;
            } else if (readInt == 1) {
                operator = CompositeDateValidator.ANY_OPERATOR;
            } else {
                operator = CompositeDateValidator.ALL_OPERATOR;
            }
            return new CompositeDateValidator((List) Preconditions.checkNotNull(readArrayList), operator);
        }

        public CompositeDateValidator[] newArray(int i) {
            return new CompositeDateValidator[i];
        }
    };
    public final Operator operator;
    public final List<CalendarConstraints.DateValidator> validators;

    public interface Operator {
        int getId();

        boolean isValid(List<CalendarConstraints.DateValidator> list, long j);
    }

    public int describeContents() {
        return 0;
    }

    public CompositeDateValidator(List<CalendarConstraints.DateValidator> list, Operator operator2) {
        this.validators = list;
        this.operator = operator2;
    }

    public boolean isValid(long j) {
        return this.operator.isValid(this.validators, j);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.validators);
        parcel.writeInt(this.operator.getId());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CompositeDateValidator)) {
            return false;
        }
        CompositeDateValidator compositeDateValidator = (CompositeDateValidator) obj;
        if (!this.validators.equals(compositeDateValidator.validators) || this.operator.getId() != compositeDateValidator.operator.getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.validators.hashCode();
    }
}
