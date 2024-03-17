package com.android.keyguard.clock;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.R$id;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ImageClock extends FrameLayout {
    public String mDescFormat;
    public ImageView mHourHand;
    public ImageView mMinuteHand;
    public final Calendar mTime;
    public TimeZone mTimeZone;

    public ImageClock(Context context) {
        this(context, (AttributeSet) null);
    }

    public ImageClock(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImageClock(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTime = Calendar.getInstance(TimeZone.getDefault());
        this.mDescFormat = ((SimpleDateFormat) DateFormat.getTimeFormat(context)).toLocalizedPattern();
    }

    public void onTimeChanged() {
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        this.mHourHand.setRotation((((float) this.mTime.get(10)) * 30.0f) + (((float) this.mTime.get(12)) * 0.5f));
        this.mMinuteHand.setRotation(((float) this.mTime.get(12)) * 6.0f);
        setContentDescription(DateFormat.format(this.mDescFormat, this.mTime));
        invalidate();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHourHand = (ImageView) findViewById(R$id.hour_hand);
        this.mMinuteHand = (ImageView) findViewById(R$id.minute_hand);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Calendar calendar = this.mTime;
        TimeZone timeZone = this.mTimeZone;
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        calendar.setTimeZone(timeZone);
        onTimeChanged();
    }
}
