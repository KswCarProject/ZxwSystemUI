package com.android.settingslib.users;

import android.app.Activity;
import android.view.View;
import com.android.settingslib.RestrictedLockUtils;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EditUserInfoController$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ Activity f$0;
    public final /* synthetic */ RestrictedLockUtils.EnforcedAdmin f$1;

    public /* synthetic */ EditUserInfoController$$ExternalSyntheticLambda0(Activity activity, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.f$0 = activity;
        this.f$1 = enforcedAdmin;
    }

    public final void onClick(View view) {
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.f$0, this.f$1);
    }
}
