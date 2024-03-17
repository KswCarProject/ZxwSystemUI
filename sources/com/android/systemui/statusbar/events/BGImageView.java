package com.android.systemui.statusbar.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.statusbar.events.BackgroundAnimatableView;
import org.jetbrains.annotations.NotNull;

@SuppressLint({"AppCompatCustomView"})
/* compiled from: StatusEvent.kt */
public final class BGImageView extends ImageView implements BackgroundAnimatableView {
    @NotNull
    public View getView() {
        return this;
    }

    public int getChipWidth() {
        return BackgroundAnimatableView.DefaultImpls.getChipWidth(this);
    }

    public BGImageView(@NotNull Context context) {
        super(context);
    }

    public void setBoundsForAnimation(int i, int i2, int i3, int i4) {
        setLeftTopRightBottom(i, i2, i3, i4);
    }
}
