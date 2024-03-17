package com.android.systemui.qs;

import android.view.View;
import com.android.systemui.privacy.PrivacyChipEvent;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$onParentVisible$1 implements View.OnClickListener {
    public final /* synthetic */ HeaderPrivacyIconsController this$0;

    public HeaderPrivacyIconsController$onParentVisible$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public final void onClick(View view) {
        this.this$0.uiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_CLICK);
        if (this.this$0.safetyCenterEnabled) {
            this.this$0.showSafetyCenter();
        } else {
            this.this$0.privacyDialogController.showDialog(this.this$0.privacyChip.getContext());
        }
    }
}
