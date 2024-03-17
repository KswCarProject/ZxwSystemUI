package com.android.systemui.user;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherPopupMenu.kt */
public final class UserSwitcherPopupMenu$createSpacer$1 extends View {
    public final /* synthetic */ int $height;

    public void draw(@NotNull Canvas canvas) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserSwitcherPopupMenu$createSpacer$1(int i, Context context) {
        super(context);
        this.$height = i;
    }

    public void onMeasure(int i, int i2) {
        setMeasuredDimension(1, this.$height);
    }
}
