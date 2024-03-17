package com.android.systemui.accessibility.floatingmenu;

import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import java.util.Arrays;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AnnotationLinkSpan$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ AnnotationLinkSpan.LinkInfo[] f$0;
    public final /* synthetic */ SpannableStringBuilder f$1;
    public final /* synthetic */ SpannableString f$2;

    public /* synthetic */ AnnotationLinkSpan$$ExternalSyntheticLambda0(AnnotationLinkSpan.LinkInfo[] linkInfoArr, SpannableStringBuilder spannableStringBuilder, SpannableString spannableString) {
        this.f$0 = linkInfoArr;
        this.f$1 = spannableStringBuilder;
        this.f$2 = spannableString;
    }

    public final void accept(Object obj) {
        Arrays.asList(this.f$0).stream().filter(new AnnotationLinkSpan$$ExternalSyntheticLambda1(((Annotation) obj).getValue())).findFirst().flatMap(new AnnotationLinkSpan$$ExternalSyntheticLambda2()).ifPresent(new AnnotationLinkSpan$$ExternalSyntheticLambda3(this.f$1, this.f$2, (Annotation) obj));
    }
}
