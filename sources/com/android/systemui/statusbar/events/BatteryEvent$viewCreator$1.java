package com.android.systemui.statusbar.events;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusEvent.kt */
public final class BatteryEvent$viewCreator$1 extends Lambda implements Function1<Context, BGImageView> {
    public static final BatteryEvent$viewCreator$1 INSTANCE = new BatteryEvent$viewCreator$1();

    public BatteryEvent$viewCreator$1() {
        super(1);
    }

    @NotNull
    public final BGImageView invoke(@NotNull Context context) {
        BGImageView bGImageView = new BGImageView(context);
        bGImageView.setImageDrawable(new ThemedBatteryDrawable(context, -1));
        bGImageView.setBackgroundDrawable(new ColorDrawable(-16711936));
        return bGImageView;
    }
}
