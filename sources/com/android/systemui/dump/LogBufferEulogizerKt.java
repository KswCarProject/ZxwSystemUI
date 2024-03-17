package com.android.systemui.dump;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogBufferEulogizer.kt */
public final class LogBufferEulogizerKt {
    @NotNull
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
    public static final long MAX_AGE_TO_DUMP = TimeUnit.HOURS.toMillis(48);
    public static final long MIN_WRITE_GAP = TimeUnit.MINUTES.toMillis(5);
}
