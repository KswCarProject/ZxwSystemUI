package com.android.wm.shell.pip.tv;

import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannedString;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipMenuView$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ TvPipMenuView f$0;
    public final /* synthetic */ SpannableString f$1;
    public final /* synthetic */ SpannedString f$2;

    public /* synthetic */ TvPipMenuView$$ExternalSyntheticLambda0(TvPipMenuView tvPipMenuView, SpannableString spannableString, SpannedString spannedString) {
        this.f$0 = tvPipMenuView;
        this.f$1 = spannableString;
        this.f$2 = spannedString;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$initEduText$0(this.f$1, this.f$2, (Annotation) obj);
    }
}
