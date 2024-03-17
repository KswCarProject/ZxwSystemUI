package com.google.android.material.datepicker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.material.R$string;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class DateFormatTextWatcher extends TextWatcherAdapter {
    public final CalendarConstraints constraints;
    public final DateFormat dateFormat;
    public final String outOfRange;
    public final Runnable setErrorCallback;
    public Runnable setRangeErrorCallback;
    public final TextInputLayout textInputLayout;

    public abstract void onInvalidDate();

    public abstract void onValidDate(Long l);

    public DateFormatTextWatcher(final String str, DateFormat dateFormat2, TextInputLayout textInputLayout2, CalendarConstraints calendarConstraints) {
        this.dateFormat = dateFormat2;
        this.textInputLayout = textInputLayout2;
        this.constraints = calendarConstraints;
        this.outOfRange = textInputLayout2.getContext().getString(R$string.mtrl_picker_out_of_range);
        this.setErrorCallback = new Runnable() {
            public void run() {
                TextInputLayout access$000 = DateFormatTextWatcher.this.textInputLayout;
                DateFormat access$100 = DateFormatTextWatcher.this.dateFormat;
                Context context = access$000.getContext();
                String string = context.getString(R$string.mtrl_picker_invalid_format);
                String format = String.format(context.getString(R$string.mtrl_picker_invalid_format_use), new Object[]{str});
                String format2 = String.format(context.getString(R$string.mtrl_picker_invalid_format_example), new Object[]{access$100.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis()))});
                access$000.setError(string + "\n" + format + "\n" + format2);
                DateFormatTextWatcher.this.onInvalidDate();
            }
        };
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.textInputLayout.removeCallbacks(this.setErrorCallback);
        this.textInputLayout.removeCallbacks(this.setRangeErrorCallback);
        this.textInputLayout.setError((CharSequence) null);
        onValidDate((Long) null);
        if (!TextUtils.isEmpty(charSequence)) {
            try {
                Date parse = this.dateFormat.parse(charSequence.toString());
                this.textInputLayout.setError((CharSequence) null);
                long time = parse.getTime();
                if (!this.constraints.getDateValidator().isValid(time) || !this.constraints.isWithinBounds(time)) {
                    Runnable createRangeErrorCallback = createRangeErrorCallback(time);
                    this.setRangeErrorCallback = createRangeErrorCallback;
                    runValidation(this.textInputLayout, createRangeErrorCallback);
                    return;
                }
                onValidDate(Long.valueOf(parse.getTime()));
            } catch (ParseException unused) {
                runValidation(this.textInputLayout, this.setErrorCallback);
            }
        }
    }

    public final Runnable createRangeErrorCallback(final long j) {
        return new Runnable() {
            public void run() {
                DateFormatTextWatcher.this.textInputLayout.setError(String.format(DateFormatTextWatcher.this.outOfRange, new Object[]{DateStrings.getDateString(j)}));
                DateFormatTextWatcher.this.onInvalidDate();
            }
        };
    }

    public void runValidation(View view, Runnable runnable) {
        view.postDelayed(runnable, 1000);
    }
}
