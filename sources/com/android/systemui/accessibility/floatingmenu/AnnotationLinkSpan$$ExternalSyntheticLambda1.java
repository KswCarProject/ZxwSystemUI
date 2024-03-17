package com.android.systemui.accessibility.floatingmenu;

import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AnnotationLinkSpan$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ AnnotationLinkSpan$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return AnnotationLinkSpan.lambda$linkify$1(this.f$0, (AnnotationLinkSpan.LinkInfo) obj);
    }
}
