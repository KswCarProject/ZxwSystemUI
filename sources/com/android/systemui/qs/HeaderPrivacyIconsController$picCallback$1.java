package com.android.systemui.qs;

import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$picCallback$1 implements PrivacyItemController.Callback {
    public final /* synthetic */ HeaderPrivacyIconsController this$0;

    public HeaderPrivacyIconsController$picCallback$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onPrivacyItemsChanged(@NotNull List<PrivacyItem> list) {
        this.this$0.privacyChip.setPrivacyList(list);
        this.this$0.setChipVisibility(!list.isEmpty());
    }

    public void onFlagMicCameraChanged(boolean z) {
        if (this.this$0.micCameraIndicatorsEnabled != z) {
            this.this$0.micCameraIndicatorsEnabled = z;
            update();
        }
    }

    public void onFlagLocationChanged(boolean z) {
        if (this.this$0.locationIndicatorsEnabled != z) {
            this.this$0.locationIndicatorsEnabled = z;
            update();
        }
    }

    public final void update() {
        this.this$0.updatePrivacyIconSlots();
        HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
        headerPrivacyIconsController.setChipVisibility(!headerPrivacyIconsController.privacyChip.getPrivacyList().isEmpty());
    }
}
