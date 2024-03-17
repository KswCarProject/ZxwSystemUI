package com.android.systemui.controls.management;

import androidx.recyclerview.widget.RecyclerView;

/* compiled from: ControlsProviderSelectorActivity.kt */
public final class ControlsProviderSelectorActivity$onStart$3$1 extends RecyclerView.AdapterDataObserver {
    public boolean hasAnimated;
    public final /* synthetic */ ControlsProviderSelectorActivity this$0;

    public ControlsProviderSelectorActivity$onStart$3$1(ControlsProviderSelectorActivity controlsProviderSelectorActivity) {
        this.this$0 = controlsProviderSelectorActivity;
    }

    public void onChanged() {
        if (!this.hasAnimated) {
            this.hasAnimated = true;
            ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
            RecyclerView access$getRecyclerView$p = this.this$0.recyclerView;
            if (access$getRecyclerView$p == null) {
                access$getRecyclerView$p = null;
            }
            controlsAnimations.enterAnimation(access$getRecyclerView$p).start();
        }
    }
}
