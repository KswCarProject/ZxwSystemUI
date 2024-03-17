package com.android.systemui.biometrics;

import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: UdfpsAnimationViewController.kt */
public final class UdfpsAnimationViewController$panelExpansionListener$1 implements PanelExpansionListener {
    public final /* synthetic */ T $view;
    public final /* synthetic */ UdfpsAnimationViewController<T> this$0;

    public UdfpsAnimationViewController$panelExpansionListener$1(UdfpsAnimationViewController<T> udfpsAnimationViewController, T t) {
        this.this$0 = udfpsAnimationViewController;
        this.$view = t;
    }

    public final void onPanelExpansionChanged(@NotNull PanelExpansionChangeEvent panelExpansionChangeEvent) {
        this.this$0.setNotificationShadeVisible(panelExpansionChangeEvent.getExpanded() && panelExpansionChangeEvent.getFraction() > 0.0f);
        this.$view.onExpansionChanged(panelExpansionChangeEvent.getFraction());
        this.this$0.updatePauseAuth();
    }
}
