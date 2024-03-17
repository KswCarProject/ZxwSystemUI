package com.android.systemui.media;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$onMediaDataLoaded$1 implements Runnable {
    public final /* synthetic */ MediaData $data;
    public final /* synthetic */ boolean $immediately;
    public final /* synthetic */ String $key;
    public final /* synthetic */ String $oldKey;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$onMediaDataLoaded$1(MediaData mediaData, String str, String str2, MediaSessionBasedFilter mediaSessionBasedFilter, boolean z) {
        this.$data = mediaData;
        this.$oldKey = str;
        this.$key = str2;
        this.this$0 = mediaSessionBasedFilter;
        this.$immediately = z;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: android.media.session.MediaController} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r9 = this;
            com.android.systemui.media.MediaData r0 = r9.$data
            android.media.session.MediaSession$Token r0 = r0.getToken()
            if (r0 != 0) goto L_0x0009
            goto L_0x0012
        L_0x0009:
            com.android.systemui.media.MediaSessionBasedFilter r1 = r9.this$0
            java.util.Set r1 = r1.tokensWithNotifications
            r1.add(r0)
        L_0x0012:
            java.lang.String r0 = r9.$oldKey
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0022
            java.lang.String r3 = r9.$key
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r0)
            if (r0 != 0) goto L_0x0022
            r0 = r1
            goto L_0x0023
        L_0x0022:
            r0 = r2
        L_0x0023:
            if (r0 == 0) goto L_0x0044
            com.android.systemui.media.MediaSessionBasedFilter r3 = r9.this$0
            java.util.Map r3 = r3.keyedTokens
            java.lang.String r4 = r9.$oldKey
            java.lang.Object r3 = r3.remove(r4)
            java.util.Set r3 = (java.util.Set) r3
            if (r3 != 0) goto L_0x0036
            goto L_0x0044
        L_0x0036:
            com.android.systemui.media.MediaSessionBasedFilter r4 = r9.this$0
            java.lang.String r5 = r9.$key
            java.util.Map r4 = r4.keyedTokens
            java.lang.Object r3 = r4.put(r5, r3)
            java.util.Set r3 = (java.util.Set) r3
        L_0x0044:
            com.android.systemui.media.MediaData r3 = r9.$data
            android.media.session.MediaSession$Token r3 = r3.getToken()
            r4 = 0
            if (r3 == 0) goto L_0x008b
            com.android.systemui.media.MediaSessionBasedFilter r3 = r9.this$0
            java.util.Map r3 = r3.keyedTokens
            java.lang.String r5 = r9.$key
            java.lang.Object r3 = r3.get(r5)
            java.util.Set r3 = (java.util.Set) r3
            if (r3 != 0) goto L_0x005f
            r3 = r4
            goto L_0x006d
        L_0x005f:
            com.android.systemui.media.MediaData r5 = r9.$data
            android.media.session.MediaSession$Token r5 = r5.getToken()
            boolean r3 = r3.add(r5)
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
        L_0x006d:
            if (r3 != 0) goto L_0x008b
            com.android.systemui.media.MediaSessionBasedFilter r3 = r9.this$0
            com.android.systemui.media.MediaData r5 = r9.$data
            java.lang.String r6 = r9.$key
            android.media.session.MediaSession$Token[] r7 = new android.media.session.MediaSession.Token[r1]
            android.media.session.MediaSession$Token r5 = r5.getToken()
            r7[r2] = r5
            java.util.Set r5 = kotlin.collections.SetsKt__SetsKt.mutableSetOf(r7)
            java.util.Map r3 = r3.keyedTokens
            java.lang.Object r3 = r3.put(r6, r5)
            java.util.Set r3 = (java.util.Set) r3
        L_0x008b:
            com.android.systemui.media.MediaSessionBasedFilter r3 = r9.this$0
            java.util.LinkedHashMap r3 = r3.packageControllers
            com.android.systemui.media.MediaData r5 = r9.$data
            java.lang.String r5 = r5.getPackageName()
            java.lang.Object r3 = r3.get(r5)
            java.util.List r3 = (java.util.List) r3
            if (r3 != 0) goto L_0x00a1
            r5 = r4
            goto L_0x00cf
        L_0x00a1:
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.util.Iterator r3 = r3.iterator()
        L_0x00ac:
            boolean r6 = r3.hasNext()
            if (r6 == 0) goto L_0x00cf
            java.lang.Object r6 = r3.next()
            r7 = r6
            android.media.session.MediaController r7 = (android.media.session.MediaController) r7
            android.media.session.MediaController$PlaybackInfo r7 = r7.getPlaybackInfo()
            if (r7 != 0) goto L_0x00c1
        L_0x00bf:
            r7 = r2
            goto L_0x00c9
        L_0x00c1:
            int r7 = r7.getPlaybackType()
            r8 = 2
            if (r7 != r8) goto L_0x00bf
            r7 = r1
        L_0x00c9:
            if (r7 == 0) goto L_0x00ac
            r5.add(r6)
            goto L_0x00ac
        L_0x00cf:
            if (r5 != 0) goto L_0x00d3
        L_0x00d1:
            r1 = r2
            goto L_0x00d9
        L_0x00d3:
            int r3 = r5.size()
            if (r3 != r1) goto L_0x00d1
        L_0x00d9:
            if (r1 == 0) goto L_0x00e2
            java.lang.Object r1 = kotlin.collections.CollectionsKt___CollectionsKt.firstOrNull(r5)
            r4 = r1
            android.media.session.MediaController r4 = (android.media.session.MediaController) r4
        L_0x00e2:
            if (r0 != 0) goto L_0x015c
            if (r4 == 0) goto L_0x015c
            android.media.session.MediaSession$Token r0 = r4.getSessionToken()
            com.android.systemui.media.MediaData r1 = r9.$data
            android.media.session.MediaSession$Token r1 = r1.getToken()
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r1)
            if (r0 != 0) goto L_0x015c
            com.android.systemui.media.MediaSessionBasedFilter r0 = r9.this$0
            java.util.Set r0 = r0.tokensWithNotifications
            android.media.session.MediaSession$Token r1 = r4.getSessionToken()
            boolean r0 = r0.contains(r1)
            if (r0 != 0) goto L_0x0107
            goto L_0x015c
        L_0x0107:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "filtering key="
            r0.append(r1)
            java.lang.String r1 = r9.$key
            r0.append(r1)
            java.lang.String r1 = " local="
            r0.append(r1)
            com.android.systemui.media.MediaData r1 = r9.$data
            android.media.session.MediaSession$Token r1 = r1.getToken()
            r0.append(r1)
            java.lang.String r1 = " remote="
            r0.append(r1)
            android.media.session.MediaSession$Token r1 = r4.getSessionToken()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "MediaSessionBasedFilter"
            android.util.Log.d(r1, r0)
            com.android.systemui.media.MediaSessionBasedFilter r0 = r9.this$0
            java.util.Map r0 = r0.keyedTokens
            java.lang.String r1 = r9.$key
            java.lang.Object r0 = r0.get(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            java.util.Set r0 = (java.util.Set) r0
            android.media.session.MediaSession$Token r1 = r4.getSessionToken()
            boolean r0 = r0.contains(r1)
            if (r0 != 0) goto L_0x0169
            com.android.systemui.media.MediaSessionBasedFilter r0 = r9.this$0
            java.lang.String r9 = r9.$key
            r0.dispatchMediaDataRemoved(r9)
            goto L_0x0169
        L_0x015c:
            com.android.systemui.media.MediaSessionBasedFilter r0 = r9.this$0
            java.lang.String r1 = r9.$key
            java.lang.String r2 = r9.$oldKey
            com.android.systemui.media.MediaData r3 = r9.$data
            boolean r9 = r9.$immediately
            r0.dispatchMediaDataLoaded(r1, r2, r3, r9)
        L_0x0169:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataLoaded$1.run():void");
    }
}
