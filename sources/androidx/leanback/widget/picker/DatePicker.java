package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.picker.PickerUtility;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatePicker extends Picker {
    public static final int[] DATE_FIELDS = {5, 2, 1};
    public int mColDayIndex;
    public int mColMonthIndex;
    public int mColYearIndex;
    public PickerUtility.DateConstant mConstant;
    public Calendar mCurrentDate;
    public final DateFormat mDateFormat;
    public String mDatePickerFormat;
    public PickerColumn mDayColumn;
    public Calendar mMaxDate;
    public Calendar mMinDate;
    public PickerColumn mMonthColumn;
    public Calendar mTempDate;
    public PickerColumn mYearColumn;

    public DatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.datePickerStyle);
    }

    /* JADX INFO: finally extract failed */
    @SuppressLint({"CustomViewStyleable"})
    public DatePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        updateCurrentLocale();
        int[] iArr = R$styleable.lbDatePicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        try {
            String string = obtainStyledAttributes.getString(R$styleable.lbDatePicker_android_minDate);
            String string2 = obtainStyledAttributes.getString(R$styleable.lbDatePicker_android_maxDate);
            String string3 = obtainStyledAttributes.getString(R$styleable.lbDatePicker_datePickerFormat);
            obtainStyledAttributes.recycle();
            this.mTempDate.clear();
            if (TextUtils.isEmpty(string)) {
                this.mTempDate.set(1900, 0, 1);
            } else if (!parseDate(string, this.mTempDate)) {
                this.mTempDate.set(1900, 0, 1);
            }
            this.mMinDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            this.mTempDate.clear();
            if (TextUtils.isEmpty(string2)) {
                this.mTempDate.set(2100, 0, 1);
            } else if (!parseDate(string2, this.mTempDate)) {
                this.mTempDate.set(2100, 0, 1);
            }
            this.mMaxDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            setDatePickerFormat(TextUtils.isEmpty(string3) ? new String(android.text.format.DateFormat.getDateFormatOrder(context)) : string3);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setTime(this.mDateFormat.parse(str));
            return true;
        } catch (ParseException unused) {
            Log.w("DatePicker", "Date: " + str + " not in format: " + "MM/dd/yyyy");
            return false;
        }
    }

    public String getBestYearMonthDayPattern(String str) {
        String str2;
        if (PickerUtility.SUPPORTS_BEST_DATE_TIME_PATTERN) {
            str2 = android.text.format.DateFormat.getBestDateTimePattern(this.mConstant.locale, str);
        } else {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            if (dateFormat instanceof SimpleDateFormat) {
                str2 = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
            } else {
                str2 = "MM/dd/yyyy";
            }
        }
        if (TextUtils.isEmpty(str2)) {
            return "MM/dd/yyyy";
        }
        return str2;
    }

    public List<CharSequence> extractSeparators() {
        String bestYearMonthDayPattern = getBestYearMonthDayPattern(this.mDatePickerFormat);
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        char[] cArr = {'Y', 'y', 'M', 'm', 'D', 'd'};
        boolean z = false;
        char c = 0;
        for (int i = 0; i < bestYearMonthDayPattern.length(); i++) {
            char charAt = bestYearMonthDayPattern.charAt(i);
            if (charAt != ' ') {
                if (charAt != '\'') {
                    if (z) {
                        sb.append(charAt);
                    } else if (!isAnyOf(charAt, cArr)) {
                        sb.append(charAt);
                    } else if (charAt != c) {
                        arrayList.add(sb.toString());
                        sb.setLength(0);
                    }
                    c = charAt;
                } else if (!z) {
                    sb.setLength(0);
                    z = true;
                } else {
                    z = false;
                }
            }
        }
        arrayList.add(sb.toString());
        return arrayList;
    }

    public static boolean isAnyOf(char c, char[] cArr) {
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    public void setDatePickerFormat(String str) {
        if (TextUtils.isEmpty(str)) {
            str = new String(android.text.format.DateFormat.getDateFormatOrder(getContext()));
        }
        if (!TextUtils.equals(this.mDatePickerFormat, str)) {
            this.mDatePickerFormat = str;
            List<CharSequence> extractSeparators = extractSeparators();
            if (extractSeparators.size() == str.length() + 1) {
                setSeparators(extractSeparators);
                this.mDayColumn = null;
                this.mMonthColumn = null;
                this.mYearColumn = null;
                this.mColMonthIndex = -1;
                this.mColDayIndex = -1;
                this.mColYearIndex = -1;
                String upperCase = str.toUpperCase(this.mConstant.locale);
                ArrayList arrayList = new ArrayList(3);
                for (int i = 0; i < upperCase.length(); i++) {
                    char charAt = upperCase.charAt(i);
                    if (charAt != 'D') {
                        if (charAt != 'M') {
                            if (charAt != 'Y') {
                                throw new IllegalArgumentException("datePicker format error");
                            } else if (this.mYearColumn == null) {
                                PickerColumn pickerColumn = new PickerColumn();
                                this.mYearColumn = pickerColumn;
                                arrayList.add(pickerColumn);
                                this.mColYearIndex = i;
                                this.mYearColumn.setLabelFormat("%d");
                            } else {
                                throw new IllegalArgumentException("datePicker format error");
                            }
                        } else if (this.mMonthColumn == null) {
                            PickerColumn pickerColumn2 = new PickerColumn();
                            this.mMonthColumn = pickerColumn2;
                            arrayList.add(pickerColumn2);
                            this.mMonthColumn.setStaticLabels(this.mConstant.months);
                            this.mColMonthIndex = i;
                        } else {
                            throw new IllegalArgumentException("datePicker format error");
                        }
                    } else if (this.mDayColumn == null) {
                        PickerColumn pickerColumn3 = new PickerColumn();
                        this.mDayColumn = pickerColumn3;
                        arrayList.add(pickerColumn3);
                        this.mDayColumn.setLabelFormat("%02d");
                        this.mColDayIndex = i;
                    } else {
                        throw new IllegalArgumentException("datePicker format error");
                    }
                }
                setColumns(arrayList);
                updateSpinners(false);
                return;
            }
            throw new IllegalStateException("Separators size: " + extractSeparators.size() + " must equal the size of datePickerFormat: " + str.length() + " + 1");
        }
    }

    public final void updateCurrentLocale() {
        PickerUtility.DateConstant dateConstantInstance = PickerUtility.getDateConstantInstance(Locale.getDefault(), getContext().getResources());
        this.mConstant = dateConstantInstance;
        this.mTempDate = PickerUtility.getCalendarForLocale(this.mTempDate, dateConstantInstance.locale);
        this.mMinDate = PickerUtility.getCalendarForLocale(this.mMinDate, this.mConstant.locale);
        this.mMaxDate = PickerUtility.getCalendarForLocale(this.mMaxDate, this.mConstant.locale);
        this.mCurrentDate = PickerUtility.getCalendarForLocale(this.mCurrentDate, this.mConstant.locale);
        PickerColumn pickerColumn = this.mMonthColumn;
        if (pickerColumn != null) {
            pickerColumn.setStaticLabels(this.mConstant.months);
            setColumnAt(this.mColMonthIndex, this.mMonthColumn);
        }
    }

    public final void onColumnValueChanged(int i, int i2) {
        this.mTempDate.setTimeInMillis(this.mCurrentDate.getTimeInMillis());
        int currentValue = getColumnAt(i).getCurrentValue();
        if (i == this.mColDayIndex) {
            this.mTempDate.add(5, i2 - currentValue);
        } else if (i == this.mColMonthIndex) {
            this.mTempDate.add(2, i2 - currentValue);
        } else if (i == this.mColYearIndex) {
            this.mTempDate.add(1, i2 - currentValue);
        } else {
            throw new IllegalArgumentException();
        }
        setDate(this.mTempDate.get(1), this.mTempDate.get(2), this.mTempDate.get(5));
    }

    public final void setDate(int i, int i2, int i3) {
        setDate(i, i2, i3, false);
    }

    public void setDate(int i, int i2, int i3, boolean z) {
        if (isNewDate(i, i2, i3)) {
            this.mCurrentDate.set(i, i2, i3);
            if (this.mCurrentDate.before(this.mMinDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
            } else if (this.mCurrentDate.after(this.mMaxDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
            }
            updateSpinners(z);
        }
    }

    public final boolean isNewDate(int i, int i2, int i3) {
        if (this.mCurrentDate.get(1) == i && this.mCurrentDate.get(2) == i3 && this.mCurrentDate.get(5) == i2) {
            return false;
        }
        return true;
    }

    public static boolean updateMin(PickerColumn pickerColumn, int i) {
        if (i == pickerColumn.getMinValue()) {
            return false;
        }
        pickerColumn.setMinValue(i);
        return true;
    }

    public static boolean updateMax(PickerColumn pickerColumn, int i) {
        if (i == pickerColumn.getMaxValue()) {
            return false;
        }
        pickerColumn.setMaxValue(i);
        return true;
    }

    public void updateSpinnersImpl(boolean z) {
        boolean z2;
        boolean z3;
        int[] iArr = {this.mColDayIndex, this.mColMonthIndex, this.mColYearIndex};
        boolean z4 = true;
        boolean z5 = true;
        for (int length = DATE_FIELDS.length - 1; length >= 0; length--) {
            int i = iArr[length];
            if (i >= 0) {
                int i2 = DATE_FIELDS[length];
                PickerColumn columnAt = getColumnAt(i);
                if (z4) {
                    z2 = updateMin(columnAt, this.mMinDate.get(i2));
                } else {
                    z2 = updateMin(columnAt, this.mCurrentDate.getActualMinimum(i2));
                }
                boolean z6 = z2 | false;
                if (z5) {
                    z3 = updateMax(columnAt, this.mMaxDate.get(i2));
                } else {
                    z3 = updateMax(columnAt, this.mCurrentDate.getActualMaximum(i2));
                }
                boolean z7 = z6 | z3;
                z4 &= this.mCurrentDate.get(i2) == this.mMinDate.get(i2);
                z5 &= this.mCurrentDate.get(i2) == this.mMaxDate.get(i2);
                if (z7) {
                    setColumnAt(iArr[length], columnAt);
                }
                setColumnValue(iArr[length], this.mCurrentDate.get(i2), z);
            }
        }
    }

    public final void updateSpinners(final boolean z) {
        post(new Runnable() {
            public void run() {
                DatePicker.this.updateSpinnersImpl(z);
            }
        });
    }
}
