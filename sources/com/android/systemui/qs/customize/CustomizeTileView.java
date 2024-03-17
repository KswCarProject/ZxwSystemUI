package com.android.systemui.qs.customize;

import android.content.Context;
import android.text.TextUtils;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: CustomizeTileView.kt */
public final class CustomizeTileView extends QSTileViewImpl {
    public boolean showAppLabel;
    public boolean showSideView = true;

    public boolean animationsEnabled() {
        return false;
    }

    public boolean isLongClickable() {
        return false;
    }

    public CustomizeTileView(@NotNull Context context, @NotNull QSIconView qSIconView) {
        super(context, qSIconView, false);
    }

    public final void setShowAppLabel(boolean z) {
        this.showAppLabel = z;
        getSecondaryLabel().setVisibility(getVisibilityState(getSecondaryLabel().getText()));
    }

    public final void setShowSideView(boolean z) {
        this.showSideView = z;
        if (!z) {
            getSideView().setVisibility(8);
        }
    }

    public void handleStateChanged(@NotNull QSTile.State state) {
        super.handleStateChanged(state);
        setShowRippleEffect(false);
        getSecondaryLabel().setVisibility(getVisibilityState(state.secondaryLabel));
        if (!this.showSideView) {
            getSideView().setVisibility(8);
        }
    }

    public final int getVisibilityState(CharSequence charSequence) {
        return (!this.showAppLabel || TextUtils.isEmpty(charSequence)) ? 8 : 0;
    }

    public final void changeState(@NotNull QSTile.State state) {
        handleStateChanged(state);
    }
}
