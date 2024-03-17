package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import com.android.systemui.controls.management.FavoritesModel;

/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$favoritesModelCallback$1 implements FavoritesModel.FavoritesModelCallback {
    public final /* synthetic */ ControlsEditingActivity this$0;

    public ControlsEditingActivity$favoritesModelCallback$1(ControlsEditingActivity controlsEditingActivity) {
        this.this$0 = controlsEditingActivity;
    }

    public void onNoneChanged(boolean z) {
        TextView textView = null;
        if (z) {
            TextView access$getSubtitle$p = this.this$0.subtitle;
            if (access$getSubtitle$p != null) {
                textView = access$getSubtitle$p;
            }
            textView.setText(ControlsEditingActivity.EMPTY_TEXT_ID);
            return;
        }
        TextView access$getSubtitle$p2 = this.this$0.subtitle;
        if (access$getSubtitle$p2 != null) {
            textView = access$getSubtitle$p2;
        }
        textView.setText(ControlsEditingActivity.SUBTITLE_ID);
    }

    public void onFirstChange() {
        View access$getSaveButton$p = this.this$0.saveButton;
        if (access$getSaveButton$p == null) {
            access$getSaveButton$p = null;
        }
        access$getSaveButton$p.setEnabled(true);
    }
}
