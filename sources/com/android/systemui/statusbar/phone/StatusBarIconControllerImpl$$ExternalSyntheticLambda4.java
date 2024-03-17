package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.StatusBarIconController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarIconControllerImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ StatusBarIconControllerImpl$$ExternalSyntheticLambda4(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((StatusBarIconController.IconManager) obj).onRemoveIcon(this.f$0);
    }
}
