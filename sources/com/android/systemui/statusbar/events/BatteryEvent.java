package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.statusbar.events.StatusEvent;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusEvent.kt */
public final class BatteryEvent implements StatusEvent {
    public final boolean forceVisible;
    public final int priority = 50;
    public final boolean showAnimation = true;
    @NotNull
    public final Function1<Context, BGImageView> viewCreator = BatteryEvent$viewCreator$1.INSTANCE;

    public boolean shouldUpdateFromEvent(@Nullable StatusEvent statusEvent) {
        return StatusEvent.DefaultImpls.shouldUpdateFromEvent(this, statusEvent);
    }

    public void updateFromEvent(@Nullable StatusEvent statusEvent) {
        StatusEvent.DefaultImpls.updateFromEvent(this, statusEvent);
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean getForceVisible() {
        return this.forceVisible;
    }

    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    @NotNull
    public Function1<Context, BGImageView> getViewCreator() {
        return this.viewCreator;
    }

    @NotNull
    public String toString() {
        return BatteryEvent.class.getSimpleName();
    }
}
