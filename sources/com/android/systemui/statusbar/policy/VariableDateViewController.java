package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.SystemClock;
import java.util.Date;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController extends ViewController<VariableDateView> {
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public Date currentTime = new Date();
    @Nullable
    public DateFormat dateFormat;
    @NotNull
    public String datePattern;
    @NotNull
    public final BroadcastReceiver intentReceiver = new VariableDateViewController$intentReceiver$1(this);
    @NotNull
    public String lastText = "";
    public int lastWidth = Integer.MAX_VALUE;
    @NotNull
    public final VariableDateViewController$onMeasureListener$1 onMeasureListener = new VariableDateViewController$onMeasureListener$1(this);
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final Handler timeTickHandler;

    public VariableDateViewController(@NotNull SystemClock systemClock2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull Handler handler, @NotNull VariableDateView variableDateView) {
        super(variableDateView);
        this.systemClock = systemClock2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.timeTickHandler = handler;
        this.datePattern = variableDateView.getLongerPattern();
    }

    public final void setDatePattern(String str) {
        if (!Intrinsics.areEqual((Object) this.datePattern, (Object) str)) {
            this.datePattern = str;
            this.dateFormat = null;
            if (isAttachedToWindow()) {
                post(new VariableDateViewController$datePattern$1(this));
            }
        }
    }

    public final String getLongerPattern() {
        return ((VariableDateView) this.mView).getLongerPattern();
    }

    public final String getShorterPattern() {
        return ((VariableDateView) this.mView).getShorterPattern();
    }

    public final Boolean post(Function0<Unit> function0) {
        Handler handler = ((VariableDateView) this.mView).getHandler();
        if (handler == null) {
            return null;
        }
        return Boolean.valueOf(handler.post(new VariableDateViewControllerKt$sam$java_lang_Runnable$0(function0)));
    }

    public void onViewAttached() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.intentReceiver, intentFilter, new HandlerExecutor(this.timeTickHandler), UserHandle.SYSTEM, 0, (String) null, 48, (Object) null);
        post(new VariableDateViewController$onViewAttached$1(this));
        ((VariableDateView) this.mView).onAttach(this.onMeasureListener);
    }

    public void onViewDetached() {
        this.dateFormat = null;
        ((VariableDateView) this.mView).onAttach((VariableDateView.OnMeasureListener) null);
        this.broadcastDispatcher.unregisterReceiver(this.intentReceiver);
    }

    public final void updateClock() {
        if (this.dateFormat == null) {
            this.dateFormat = VariableDateViewControllerKt.getFormatFromPattern(this.datePattern);
        }
        this.currentTime.setTime(this.systemClock.currentTimeMillis());
        Date date = this.currentTime;
        DateFormat dateFormat2 = this.dateFormat;
        Intrinsics.checkNotNull(dateFormat2);
        String textForFormat = VariableDateViewControllerKt.getTextForFormat(date, dateFormat2);
        if (!Intrinsics.areEqual((Object) textForFormat, (Object) this.lastText)) {
            ((VariableDateView) this.mView).setText(textForFormat);
            this.lastText = textForFormat;
        }
    }

    public final void maybeChangeFormat(int i) {
        if (((VariableDateView) this.mView).getFreezeSwitching()) {
            return;
        }
        if (i > this.lastWidth && Intrinsics.areEqual((Object) this.datePattern, (Object) getLongerPattern())) {
            return;
        }
        if (i >= this.lastWidth || !Intrinsics.areEqual((Object) this.datePattern, (Object) "")) {
            float f = (float) i;
            if (((VariableDateView) this.mView).getDesiredWidthForText(VariableDateViewControllerKt.getTextForFormat(this.currentTime, VariableDateViewControllerKt.getFormatFromPattern(getLongerPattern()))) <= f) {
                changePattern(getLongerPattern());
                return;
            }
            if (((VariableDateView) this.mView).getDesiredWidthForText(VariableDateViewControllerKt.getTextForFormat(this.currentTime, VariableDateViewControllerKt.getFormatFromPattern(getShorterPattern()))) <= f) {
                changePattern(getShorterPattern());
            } else {
                changePattern("");
            }
        }
    }

    public final void changePattern(String str) {
        if (!str.equals(this.datePattern)) {
            setDatePattern(str);
        }
    }

    /* compiled from: VariableDateViewController.kt */
    public static final class Factory {
        @NotNull
        public final BroadcastDispatcher broadcastDispatcher;
        @NotNull
        public final Handler handler;
        @NotNull
        public final SystemClock systemClock;

        public Factory(@NotNull SystemClock systemClock2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull Handler handler2) {
            this.systemClock = systemClock2;
            this.broadcastDispatcher = broadcastDispatcher2;
            this.handler = handler2;
        }

        @NotNull
        public final VariableDateViewController create(@NotNull VariableDateView variableDateView) {
            return new VariableDateViewController(this.systemClock, this.broadcastDispatcher, this.handler, variableDateView);
        }
    }
}
