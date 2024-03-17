package com.android.keyguard;

import android.R;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import com.android.systemui.animation.Interpolators;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AnimatableClockView.kt */
public final class AnimatableClockView extends TextView {
    public final int chargeAnimationDelay;
    @Nullable
    public CharSequence descFormat;
    public int dozingColor;
    public final int dozingWeightInternal;
    @Nullable
    public CharSequence format;
    public final boolean isSingleLineInternal;
    @NotNull
    public CharSequence lastMeasureCall;
    public float lineSpacingScale;
    public int lockScreenColor;
    public final int lockScreenWeightInternal;
    @Nullable
    public Runnable onTextAnimatorInitialized;
    @NotNull
    public final String tag;
    @Nullable
    public TextAnimator textAnimator;
    public final Calendar time;

    /* compiled from: AnimatableClockView.kt */
    public interface DozeStateGetter {
        boolean isDozing();
    }

    public AnimatableClockView(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
    }

    public AnimatableClockView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
    }

    public AnimatableClockView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX INFO: finally extract failed */
    public AnimatableClockView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.tag = "AnimatableClockView";
        this.lastMeasureCall = "";
        this.time = Calendar.getInstance();
        this.lineSpacingScale = 1.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AnimatableClockView, i, i2);
        try {
            this.dozingWeightInternal = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_dozeWeight, 100);
            this.lockScreenWeightInternal = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_lockScreenWeight, 300);
            this.chargeAnimationDelay = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_chargeAnimationDelay, 200);
            obtainStyledAttributes.recycle();
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.TextView, i, i2);
            try {
                boolean z = obtainStyledAttributes2.getBoolean(32, false);
                obtainStyledAttributes2.recycle();
                this.isSingleLineInternal = z;
                refreshFormat();
            } catch (Throwable th) {
                obtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            obtainStyledAttributes.recycle();
            throw th2;
        }
    }

    public final int getDozingWeight() {
        boolean useBoldedVersion = useBoldedVersion();
        int i = this.dozingWeightInternal;
        return useBoldedVersion ? i + 100 : i;
    }

    public final int getLockScreenWeight() {
        boolean useBoldedVersion = useBoldedVersion();
        int i = this.lockScreenWeightInternal;
        return useBoldedVersion ? i + 100 : i;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    public final boolean useBoldedVersion() {
        return getResources().getConfiguration().fontWeightAdjustment > 100;
    }

    public final void refreshTime() {
        this.time.setTimeInMillis(System.currentTimeMillis());
        setContentDescription(DateFormat.format(this.descFormat, this.time));
        CharSequence format2 = DateFormat.format(this.format, this.time);
        if (!TextUtils.equals(getText(), format2)) {
            setText(format2);
        }
    }

    public final void onTimeZoneChanged(@Nullable TimeZone timeZone) {
        this.time.setTimeZone(timeZone);
        refreshFormat();
    }

    @SuppressLint({"DrawAllocation"})
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.lastMeasureCall = DateFormat.format(this.descFormat, System.currentTimeMillis());
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 == null) {
            this.textAnimator = new TextAnimator(getLayout(), new AnimatableClockView$onMeasure$1(this));
            Runnable runnable = this.onTextAnimatorInitialized;
            if (runnable != null) {
                runnable.run();
            }
            this.onTextAnimatorInitialized = null;
            return;
        }
        textAnimator2.updateLayout(getLayout());
    }

    public void onDraw(@NotNull Canvas canvas) {
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 != null) {
            textAnimator2.draw(canvas);
        }
    }

    public final void setLineSpacingScale(float f) {
        this.lineSpacingScale = f;
        setLineSpacing(0.0f, f);
    }

    public final void setColors(int i, int i2) {
        this.dozingColor = i;
        this.lockScreenColor = i2;
    }

    public final void animateAppearOnLockscreen() {
        if (this.textAnimator != null) {
            setTextStyle(getDozingWeight(), -1.0f, Integer.valueOf(this.lockScreenColor), false, 0, 0, (Runnable) null);
            setTextStyle(getLockScreenWeight(), -1.0f, Integer.valueOf(this.lockScreenColor), true, 350, 0, (Runnable) null);
        }
    }

    public final void animateFoldAppear() {
        if (this.textAnimator != null) {
            setTextStyle(this.lockScreenWeightInternal, -1.0f, Integer.valueOf(this.lockScreenColor), false, 0, 0, (Runnable) null);
            setTextStyle(this.dozingWeightInternal, -1.0f, Integer.valueOf(this.dozingColor), true, Interpolators.EMPHASIZED_DECELERATE, 600, 0, (Runnable) null);
        }
    }

    public final void animateCharge(@NotNull DozeStateGetter dozeStateGetter) {
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 != null) {
            Intrinsics.checkNotNull(textAnimator2);
            if (!textAnimator2.isRunning()) {
                setTextStyle(dozeStateGetter.isDozing() ? getLockScreenWeight() : getDozingWeight(), -1.0f, (Integer) null, true, 500, (long) this.chargeAnimationDelay, new AnimatableClockView$animateCharge$startAnimPhase2$1(this, dozeStateGetter));
            }
        }
    }

    public final void animateDoze(boolean z, boolean z2) {
        setTextStyle(z ? getDozingWeight() : getLockScreenWeight(), -1.0f, Integer.valueOf(z ? this.dozingColor : this.lockScreenColor), z2, 300, 0, (Runnable) null);
    }

    public final void setTextStyle(int i, float f, Integer num, boolean z, TimeInterpolator timeInterpolator, long j, long j2, Runnable runnable) {
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 == null) {
            this.onTextAnimatorInitialized = new AnimatableClockView$setTextStyle$1(this, i, f, num, j, timeInterpolator, j2, runnable);
        } else if (textAnimator2 != null) {
            textAnimator2.setTextStyle(i, f, num, z, j, timeInterpolator, j2, runnable);
        }
    }

    public final void setTextStyle(int i, float f, Integer num, boolean z, long j, long j2, Runnable runnable) {
        setTextStyle(i, f, num, z, (TimeInterpolator) null, j, j2, runnable);
    }

    public final void refreshFormat() {
        String str;
        Patterns patterns = Patterns.INSTANCE;
        patterns.update(getContext());
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z = this.isSingleLineInternal;
        if (!z || !is24HourFormat) {
            str = (z || !is24HourFormat) ? (!z || is24HourFormat) ? "hh\nmm" : patterns.getSClockView12() : "HH\nmm";
        } else {
            str = patterns.getSClockView24();
        }
        this.format = str;
        this.descFormat = is24HourFormat ? patterns.getSClockView24() : patterns.getSClockView12();
        refreshTime();
    }

    public final void dump(@NotNull PrintWriter printWriter) {
        printWriter.println(String.valueOf(this));
        printWriter.println(Intrinsics.stringPlus("    measuredWidth=", Integer.valueOf(getMeasuredWidth())));
        printWriter.println(Intrinsics.stringPlus("    measuredHeight=", Integer.valueOf(getMeasuredHeight())));
        printWriter.println(Intrinsics.stringPlus("    singleLineInternal=", Boolean.valueOf(this.isSingleLineInternal)));
        printWriter.println(Intrinsics.stringPlus("    lastMeasureCall=", this.lastMeasureCall));
        printWriter.println(Intrinsics.stringPlus("    currText=", getText()));
        printWriter.println(Intrinsics.stringPlus("    currTimeContextDesc=", getContentDescription()));
        printWriter.println(Intrinsics.stringPlus("    time=", this.time));
    }

    /* compiled from: AnimatableClockView.kt */
    public static final class Patterns {
        @NotNull
        public static final Patterns INSTANCE = new Patterns();
        @Nullable
        public static String sCacheKey;
        @Nullable
        public static String sClockView12;
        @Nullable
        public static String sClockView24;

        @Nullable
        public final String getSClockView12() {
            return sClockView12;
        }

        @Nullable
        public final String getSClockView24() {
            return sClockView24;
        }

        public final void update(@NotNull Context context) {
            Locale locale = Locale.getDefault();
            Resources resources = context.getResources();
            String string = resources.getString(R$string.clock_12hr_format);
            String string2 = resources.getString(R$string.clock_24hr_format);
            String str = locale.toString() + string + string2;
            if (!Intrinsics.areEqual((Object) str, (Object) sCacheKey)) {
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(locale, string);
                sClockView12 = bestDateTimePattern;
                if (!StringsKt__StringsKt.contains$default(string, "a", false, 2, (Object) null)) {
                    String replace = new Regex("a").replace(bestDateTimePattern, "");
                    int length = replace.length() - 1;
                    int i = 0;
                    boolean z = false;
                    while (i <= length) {
                        boolean z2 = Intrinsics.compare((int) replace.charAt(!z ? i : length), 32) <= 0;
                        if (!z) {
                            if (!z2) {
                                z = true;
                            } else {
                                i++;
                            }
                        } else if (!z2) {
                            break;
                        } else {
                            length--;
                        }
                    }
                    sClockView12 = replace.subSequence(i, length + 1).toString();
                }
                sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
                sCacheKey = str;
            }
        }
    }
}
