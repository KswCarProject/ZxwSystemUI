package com.android.systemui.statusbar;

import android.content.res.Resources;
import com.android.systemui.R$string;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardIndicationController$$ExternalSyntheticLambda9 implements Supplier {
    public final /* synthetic */ Resources f$0;
    public final /* synthetic */ CharSequence f$1;

    public /* synthetic */ KeyguardIndicationController$$ExternalSyntheticLambda9(Resources resources, CharSequence charSequence) {
        this.f$0 = resources;
        this.f$1 = charSequence;
    }

    public final Object get() {
        return this.f$0.getString(R$string.do_disclosure_with_name, new Object[]{this.f$1});
    }
}
