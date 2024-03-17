package com.android.systemui.log;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.constraintlayout.widget.R$styleable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogcatEchoTrackerDebug.kt */
public final class LogcatEchoTrackerDebug implements LogcatEchoTracker {
    @NotNull
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);
    @NotNull
    public final Map<String, LogLevel> cachedBufferLevels;
    @NotNull
    public final Map<String, LogLevel> cachedTagLevels;
    @NotNull
    public final ContentResolver contentResolver;
    public final boolean logInBackgroundThread;

    public /* synthetic */ LogcatEchoTrackerDebug(ContentResolver contentResolver2, DefaultConstructorMarker defaultConstructorMarker) {
        this(contentResolver2);
    }

    @NotNull
    public static final LogcatEchoTrackerDebug create(@NotNull ContentResolver contentResolver2, @NotNull Looper looper) {
        return Factory.create(contentResolver2, looper);
    }

    public LogcatEchoTrackerDebug(ContentResolver contentResolver2) {
        this.contentResolver = contentResolver2;
        this.cachedBufferLevels = new LinkedHashMap();
        this.cachedTagLevels = new LinkedHashMap();
        this.logInBackgroundThread = true;
    }

    public boolean getLogInBackgroundThread() {
        return this.logInBackgroundThread;
    }

    /* compiled from: LogcatEchoTrackerDebug.kt */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Factory() {
        }

        @NotNull
        public final LogcatEchoTrackerDebug create(@NotNull ContentResolver contentResolver, @NotNull Looper looper) {
            LogcatEchoTrackerDebug logcatEchoTrackerDebug = new LogcatEchoTrackerDebug(contentResolver, (DefaultConstructorMarker) null);
            logcatEchoTrackerDebug.attach(looper);
            return logcatEchoTrackerDebug;
        }
    }

    public final void attach(Looper looper) {
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/buffer"), true, new LogcatEchoTrackerDebug$attach$1(this, new Handler(looper)));
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/tag"), true, new LogcatEchoTrackerDebug$attach$2(this, new Handler(looper)));
    }

    public synchronized boolean isBufferLoggable(@NotNull String str, @NotNull LogLevel logLevel) {
        return logLevel.ordinal() >= getLogLevel(str, "systemui/buffer", this.cachedBufferLevels).ordinal();
    }

    public synchronized boolean isTagLoggable(@NotNull String str, @NotNull LogLevel logLevel) {
        return logLevel.compareTo(getLogLevel(str, "systemui/tag", this.cachedTagLevels)) >= 0;
    }

    public final LogLevel getLogLevel(String str, String str2, Map<String, LogLevel> map) {
        LogLevel logLevel = map.get(str);
        if (logLevel != null) {
            return logLevel;
        }
        LogLevel readSetting = readSetting(str2 + '/' + str);
        map.put(str, readSetting);
        return readSetting;
    }

    public final LogLevel readSetting(String str) {
        try {
            return parseProp(Settings.Global.getString(this.contentResolver, str));
        } catch (Settings.SettingNotFoundException unused) {
            return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
        }
    }

    public final LogLevel parseProp(String str) {
        String lowerCase = str == null ? null : str.toLowerCase(Locale.ROOT);
        if (lowerCase != null) {
            switch (lowerCase.hashCode()) {
                case -1408208058:
                    if (lowerCase.equals("assert")) {
                        return LogLevel.WTF;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginLeft:
                    if (lowerCase.equals("d")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginRight:
                    if (lowerCase.equals("e")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case R$styleable.Constraint_pathMotionArc:
                    if (lowerCase.equals("i")) {
                        return LogLevel.INFO;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionBarOverlay:
                    if (lowerCase.equals("v")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionModeOverlay:
                    if (lowerCase.equals("w")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 118057:
                    if (lowerCase.equals("wtf")) {
                        return LogLevel.WTF;
                    }
                    break;
                case 3237038:
                    if (lowerCase.equals("info")) {
                        return LogLevel.INFO;
                    }
                    break;
                case 3641990:
                    if (lowerCase.equals("warn")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 95458899:
                    if (lowerCase.equals("debug")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case 96784904:
                    if (lowerCase.equals("error")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case 351107458:
                    if (lowerCase.equals("verbose")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case 1124446108:
                    if (lowerCase.equals("warning")) {
                        return LogLevel.WARNING;
                    }
                    break;
            }
        }
        return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
    }
}
