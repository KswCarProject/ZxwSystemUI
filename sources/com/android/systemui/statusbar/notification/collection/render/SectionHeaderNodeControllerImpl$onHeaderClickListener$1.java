package com.android.systemui.statusbar.notification.collection.render;

import android.content.Intent;
import android.view.View;

/* compiled from: SectionHeaderController.kt */
public final class SectionHeaderNodeControllerImpl$onHeaderClickListener$1 implements View.OnClickListener {
    public final /* synthetic */ SectionHeaderNodeControllerImpl this$0;

    public SectionHeaderNodeControllerImpl$onHeaderClickListener$1(SectionHeaderNodeControllerImpl sectionHeaderNodeControllerImpl) {
        this.this$0 = sectionHeaderNodeControllerImpl;
    }

    public final void onClick(View view) {
        this.this$0.activityStarter.startActivity(new Intent(this.this$0.clickIntentAction), true, true, 536870912);
    }
}
