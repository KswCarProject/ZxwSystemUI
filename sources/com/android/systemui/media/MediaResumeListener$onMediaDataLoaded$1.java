package com.android.systemui.media;

import android.content.pm.ResolveInfo;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$onMediaDataLoaded$1 implements Runnable {
    public final /* synthetic */ List<ResolveInfo> $inf;
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaResumeListener this$0;

    public MediaResumeListener$onMediaDataLoaded$1(MediaResumeListener mediaResumeListener, String str, List<? extends ResolveInfo> list) {
        this.this$0 = mediaResumeListener;
        this.$key = str;
        this.$inf = list;
    }

    public final void run() {
        MediaResumeListener mediaResumeListener = this.this$0;
        String str = this.$key;
        List<ResolveInfo> list = this.$inf;
        Intrinsics.checkNotNull(list);
        mediaResumeListener.tryUpdateResumptionList(str, list.get(0).getComponentInfo().getComponentName());
    }
}
