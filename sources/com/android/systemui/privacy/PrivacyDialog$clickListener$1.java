package com.android.systemui.privacy;

import android.content.Intent;
import android.view.View;
import com.android.systemui.privacy.PrivacyDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;

/* compiled from: PrivacyDialog.kt */
public final class PrivacyDialog$clickListener$1 implements View.OnClickListener {
    public final /* synthetic */ Function4<String, Integer, CharSequence, Intent, Unit> $activityStarter;

    public PrivacyDialog$clickListener$1(Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4) {
        this.$activityStarter = function4;
    }

    public final void onClick(View view) {
        Object tag = view.getTag();
        if (tag != null) {
            PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) tag;
            this.$activityStarter.invoke(privacyElement.getPackageName(), Integer.valueOf(privacyElement.getUserId()), privacyElement.getAttributionTag(), privacyElement.getNavigationIntent());
        }
    }
}
