package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaData.kt */
public final class MediaData {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final List<MediaAction> actions;
    @NotNull
    public final List<Integer> actionsToShowInCompact;
    public boolean active;
    @Nullable
    public final String app;
    @Nullable
    public final Icon appIcon;
    public final int appUid;
    @Nullable
    public final CharSequence artist;
    @Nullable
    public final Icon artwork;
    @Nullable
    public final PendingIntent clickIntent;
    @Nullable
    public final MediaDeviceData device;
    public boolean hasCheckedForResume;
    public final boolean initialized;
    @NotNull
    public final InstanceId instanceId;
    public final boolean isClearable;
    @Nullable
    public final Boolean isPlaying;
    public long lastActive;
    @Nullable
    public final String notificationKey;
    @NotNull
    public final String packageName;
    public int playbackLocation;
    @Nullable
    public Runnable resumeAction;
    public boolean resumption;
    @Nullable
    public final MediaButton semanticActions;
    @Nullable
    public final CharSequence song;
    @Nullable
    public final MediaSession.Token token;
    public final int userId;

    public static /* synthetic */ MediaData copy$default(MediaData mediaData, int i, boolean z, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List list, List list2, MediaButton mediaButton, String str2, MediaSession.Token token2, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i2, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j, InstanceId instanceId2, int i3, int i4, Object obj) {
        MediaData mediaData2 = mediaData;
        int i5 = i4;
        return mediaData.copy((i5 & 1) != 0 ? mediaData2.userId : i, (i5 & 2) != 0 ? mediaData2.initialized : z, (i5 & 4) != 0 ? mediaData2.app : str, (i5 & 8) != 0 ? mediaData2.appIcon : icon, (i5 & 16) != 0 ? mediaData2.artist : charSequence, (i5 & 32) != 0 ? mediaData2.song : charSequence2, (i5 & 64) != 0 ? mediaData2.artwork : icon2, (i5 & 128) != 0 ? mediaData2.actions : list, (i5 & 256) != 0 ? mediaData2.actionsToShowInCompact : list2, (i5 & 512) != 0 ? mediaData2.semanticActions : mediaButton, (i5 & 1024) != 0 ? mediaData2.packageName : str2, (i5 & 2048) != 0 ? mediaData2.token : token2, (i5 & 4096) != 0 ? mediaData2.clickIntent : pendingIntent, (i5 & 8192) != 0 ? mediaData2.device : mediaDeviceData, (i5 & 16384) != 0 ? mediaData2.active : z2, (i5 & 32768) != 0 ? mediaData2.resumeAction : runnable, (i5 & 65536) != 0 ? mediaData2.playbackLocation : i2, (i5 & 131072) != 0 ? mediaData2.resumption : z3, (i5 & 262144) != 0 ? mediaData2.notificationKey : str3, (i5 & 524288) != 0 ? mediaData2.hasCheckedForResume : z4, (i5 & 1048576) != 0 ? mediaData2.isPlaying : bool, (i5 & 2097152) != 0 ? mediaData2.isClearable : z5, (i5 & 4194304) != 0 ? mediaData2.lastActive : j, (i5 & 8388608) != 0 ? mediaData2.instanceId : instanceId2, (i5 & 16777216) != 0 ? mediaData2.appUid : i3);
    }

    @NotNull
    public final MediaData copy(int i, boolean z, @Nullable String str, @Nullable Icon icon, @Nullable CharSequence charSequence, @Nullable CharSequence charSequence2, @Nullable Icon icon2, @NotNull List<MediaAction> list, @NotNull List<Integer> list2, @Nullable MediaButton mediaButton, @NotNull String str2, @Nullable MediaSession.Token token2, @Nullable PendingIntent pendingIntent, @Nullable MediaDeviceData mediaDeviceData, boolean z2, @Nullable Runnable runnable, int i2, boolean z3, @Nullable String str3, boolean z4, @Nullable Boolean bool, boolean z5, long j, @NotNull InstanceId instanceId2, int i3) {
        return new MediaData(i, z, str, icon, charSequence, charSequence2, icon2, list, list2, mediaButton, str2, token2, pendingIntent, mediaDeviceData, z2, runnable, i2, z3, str3, z4, bool, z5, j, instanceId2, i3);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaData)) {
            return false;
        }
        MediaData mediaData = (MediaData) obj;
        return this.userId == mediaData.userId && this.initialized == mediaData.initialized && Intrinsics.areEqual((Object) this.app, (Object) mediaData.app) && Intrinsics.areEqual((Object) this.appIcon, (Object) mediaData.appIcon) && Intrinsics.areEqual((Object) this.artist, (Object) mediaData.artist) && Intrinsics.areEqual((Object) this.song, (Object) mediaData.song) && Intrinsics.areEqual((Object) this.artwork, (Object) mediaData.artwork) && Intrinsics.areEqual((Object) this.actions, (Object) mediaData.actions) && Intrinsics.areEqual((Object) this.actionsToShowInCompact, (Object) mediaData.actionsToShowInCompact) && Intrinsics.areEqual((Object) this.semanticActions, (Object) mediaData.semanticActions) && Intrinsics.areEqual((Object) this.packageName, (Object) mediaData.packageName) && Intrinsics.areEqual((Object) this.token, (Object) mediaData.token) && Intrinsics.areEqual((Object) this.clickIntent, (Object) mediaData.clickIntent) && Intrinsics.areEqual((Object) this.device, (Object) mediaData.device) && this.active == mediaData.active && Intrinsics.areEqual((Object) this.resumeAction, (Object) mediaData.resumeAction) && this.playbackLocation == mediaData.playbackLocation && this.resumption == mediaData.resumption && Intrinsics.areEqual((Object) this.notificationKey, (Object) mediaData.notificationKey) && this.hasCheckedForResume == mediaData.hasCheckedForResume && Intrinsics.areEqual((Object) this.isPlaying, (Object) mediaData.isPlaying) && this.isClearable == mediaData.isClearable && this.lastActive == mediaData.lastActive && Intrinsics.areEqual((Object) this.instanceId, (Object) mediaData.instanceId) && this.appUid == mediaData.appUid;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.userId) * 31;
        boolean z = this.initialized;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        String str = this.app;
        int i2 = 0;
        int hashCode2 = (i + (str == null ? 0 : str.hashCode())) * 31;
        Icon icon = this.appIcon;
        int hashCode3 = (hashCode2 + (icon == null ? 0 : icon.hashCode())) * 31;
        CharSequence charSequence = this.artist;
        int hashCode4 = (hashCode3 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        CharSequence charSequence2 = this.song;
        int hashCode5 = (hashCode4 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
        Icon icon2 = this.artwork;
        int hashCode6 = (((((hashCode5 + (icon2 == null ? 0 : icon2.hashCode())) * 31) + this.actions.hashCode()) * 31) + this.actionsToShowInCompact.hashCode()) * 31;
        MediaButton mediaButton = this.semanticActions;
        int hashCode7 = (((hashCode6 + (mediaButton == null ? 0 : mediaButton.hashCode())) * 31) + this.packageName.hashCode()) * 31;
        MediaSession.Token token2 = this.token;
        int hashCode8 = (hashCode7 + (token2 == null ? 0 : token2.hashCode())) * 31;
        PendingIntent pendingIntent = this.clickIntent;
        int hashCode9 = (hashCode8 + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31;
        MediaDeviceData mediaDeviceData = this.device;
        int hashCode10 = (hashCode9 + (mediaDeviceData == null ? 0 : mediaDeviceData.hashCode())) * 31;
        boolean z3 = this.active;
        if (z3) {
            z3 = true;
        }
        int i3 = (hashCode10 + (z3 ? 1 : 0)) * 31;
        Runnable runnable = this.resumeAction;
        int hashCode11 = (((i3 + (runnable == null ? 0 : runnable.hashCode())) * 31) + Integer.hashCode(this.playbackLocation)) * 31;
        boolean z4 = this.resumption;
        if (z4) {
            z4 = true;
        }
        int i4 = (hashCode11 + (z4 ? 1 : 0)) * 31;
        String str2 = this.notificationKey;
        int hashCode12 = (i4 + (str2 == null ? 0 : str2.hashCode())) * 31;
        boolean z5 = this.hasCheckedForResume;
        if (z5) {
            z5 = true;
        }
        int i5 = (hashCode12 + (z5 ? 1 : 0)) * 31;
        Boolean bool = this.isPlaying;
        if (bool != null) {
            i2 = bool.hashCode();
        }
        int i6 = (i5 + i2) * 31;
        boolean z6 = this.isClearable;
        if (!z6) {
            z2 = z6;
        }
        return ((((((i6 + (z2 ? 1 : 0)) * 31) + Long.hashCode(this.lastActive)) * 31) + this.instanceId.hashCode()) * 31) + Integer.hashCode(this.appUid);
    }

    @NotNull
    public String toString() {
        return "MediaData(userId=" + this.userId + ", initialized=" + this.initialized + ", app=" + this.app + ", appIcon=" + this.appIcon + ", artist=" + this.artist + ", song=" + this.song + ", artwork=" + this.artwork + ", actions=" + this.actions + ", actionsToShowInCompact=" + this.actionsToShowInCompact + ", semanticActions=" + this.semanticActions + ", packageName=" + this.packageName + ", token=" + this.token + ", clickIntent=" + this.clickIntent + ", device=" + this.device + ", active=" + this.active + ", resumeAction=" + this.resumeAction + ", playbackLocation=" + this.playbackLocation + ", resumption=" + this.resumption + ", notificationKey=" + this.notificationKey + ", hasCheckedForResume=" + this.hasCheckedForResume + ", isPlaying=" + this.isPlaying + ", isClearable=" + this.isClearable + ", lastActive=" + this.lastActive + ", instanceId=" + this.instanceId + ", appUid=" + this.appUid + ')';
    }

    public MediaData(int i, boolean z, @Nullable String str, @Nullable Icon icon, @Nullable CharSequence charSequence, @Nullable CharSequence charSequence2, @Nullable Icon icon2, @NotNull List<MediaAction> list, @NotNull List<Integer> list2, @Nullable MediaButton mediaButton, @NotNull String str2, @Nullable MediaSession.Token token2, @Nullable PendingIntent pendingIntent, @Nullable MediaDeviceData mediaDeviceData, boolean z2, @Nullable Runnable runnable, int i2, boolean z3, @Nullable String str3, boolean z4, @Nullable Boolean bool, boolean z5, long j, @NotNull InstanceId instanceId2, int i3) {
        this.userId = i;
        this.initialized = z;
        this.app = str;
        this.appIcon = icon;
        this.artist = charSequence;
        this.song = charSequence2;
        this.artwork = icon2;
        this.actions = list;
        this.actionsToShowInCompact = list2;
        this.semanticActions = mediaButton;
        this.packageName = str2;
        this.token = token2;
        this.clickIntent = pendingIntent;
        this.device = mediaDeviceData;
        this.active = z2;
        this.resumeAction = runnable;
        this.playbackLocation = i2;
        this.resumption = z3;
        this.notificationKey = str3;
        this.hasCheckedForResume = z4;
        this.isPlaying = bool;
        this.isClearable = z5;
        this.lastActive = j;
        this.instanceId = instanceId2;
        this.appUid = i3;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ MediaData(int r31, boolean r32, java.lang.String r33, android.graphics.drawable.Icon r34, java.lang.CharSequence r35, java.lang.CharSequence r36, android.graphics.drawable.Icon r37, java.util.List r38, java.util.List r39, com.android.systemui.media.MediaButton r40, java.lang.String r41, android.media.session.MediaSession.Token r42, android.app.PendingIntent r43, com.android.systemui.media.MediaDeviceData r44, boolean r45, java.lang.Runnable r46, int r47, boolean r48, java.lang.String r49, boolean r50, java.lang.Boolean r51, boolean r52, long r53, com.android.internal.logging.InstanceId r55, int r56, int r57, kotlin.jvm.internal.DefaultConstructorMarker r58) {
        /*
            r30 = this;
            r0 = r57
            r1 = r0 & 2
            r2 = 0
            if (r1 == 0) goto L_0x0009
            r5 = r2
            goto L_0x000b
        L_0x0009:
            r5 = r32
        L_0x000b:
            r1 = r0 & 512(0x200, float:7.175E-43)
            r3 = 0
            if (r1 == 0) goto L_0x0012
            r13 = r3
            goto L_0x0014
        L_0x0012:
            r13 = r40
        L_0x0014:
            r1 = 65536(0x10000, float:9.18355E-41)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x001c
            r20 = r2
            goto L_0x001e
        L_0x001c:
            r20 = r47
        L_0x001e:
            r1 = 131072(0x20000, float:1.83671E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0026
            r21 = r2
            goto L_0x0028
        L_0x0026:
            r21 = r48
        L_0x0028:
            r1 = 262144(0x40000, float:3.67342E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0030
            r22 = r3
            goto L_0x0032
        L_0x0030:
            r22 = r49
        L_0x0032:
            r1 = 524288(0x80000, float:7.34684E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x003a
            r23 = r2
            goto L_0x003c
        L_0x003a:
            r23 = r50
        L_0x003c:
            r1 = 1048576(0x100000, float:1.469368E-39)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0044
            r24 = r3
            goto L_0x0046
        L_0x0044:
            r24 = r51
        L_0x0046:
            r1 = 2097152(0x200000, float:2.938736E-39)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x004f
            r1 = 1
            r25 = r1
            goto L_0x0051
        L_0x004f:
            r25 = r52
        L_0x0051:
            r1 = 4194304(0x400000, float:5.877472E-39)
            r0 = r0 & r1
            if (r0 == 0) goto L_0x005b
            r0 = 0
            r26 = r0
            goto L_0x005d
        L_0x005b:
            r26 = r53
        L_0x005d:
            r3 = r30
            r4 = r31
            r6 = r33
            r7 = r34
            r8 = r35
            r9 = r36
            r10 = r37
            r11 = r38
            r12 = r39
            r14 = r41
            r15 = r42
            r16 = r43
            r17 = r44
            r18 = r45
            r19 = r46
            r28 = r55
            r29 = r56
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r28, r29)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaData.<init>(int, boolean, java.lang.String, android.graphics.drawable.Icon, java.lang.CharSequence, java.lang.CharSequence, android.graphics.drawable.Icon, java.util.List, java.util.List, com.android.systemui.media.MediaButton, java.lang.String, android.media.session.MediaSession$Token, android.app.PendingIntent, com.android.systemui.media.MediaDeviceData, boolean, java.lang.Runnable, int, boolean, java.lang.String, boolean, java.lang.Boolean, boolean, long, com.android.internal.logging.InstanceId, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final int getUserId() {
        return this.userId;
    }

    @Nullable
    public final String getApp() {
        return this.app;
    }

    @Nullable
    public final Icon getAppIcon() {
        return this.appIcon;
    }

    @Nullable
    public final CharSequence getArtist() {
        return this.artist;
    }

    @Nullable
    public final CharSequence getSong() {
        return this.song;
    }

    @Nullable
    public final Icon getArtwork() {
        return this.artwork;
    }

    @NotNull
    public final List<MediaAction> getActions() {
        return this.actions;
    }

    @NotNull
    public final List<Integer> getActionsToShowInCompact() {
        return this.actionsToShowInCompact;
    }

    @Nullable
    public final MediaButton getSemanticActions() {
        return this.semanticActions;
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    @Nullable
    public final MediaSession.Token getToken() {
        return this.token;
    }

    @Nullable
    public final PendingIntent getClickIntent() {
        return this.clickIntent;
    }

    @Nullable
    public final MediaDeviceData getDevice() {
        return this.device;
    }

    public final boolean getActive() {
        return this.active;
    }

    public final void setActive(boolean z) {
        this.active = z;
    }

    @Nullable
    public final Runnable getResumeAction() {
        return this.resumeAction;
    }

    public final void setResumeAction(@Nullable Runnable runnable) {
        this.resumeAction = runnable;
    }

    public final int getPlaybackLocation() {
        return this.playbackLocation;
    }

    public final boolean getResumption() {
        return this.resumption;
    }

    @Nullable
    public final String getNotificationKey() {
        return this.notificationKey;
    }

    public final boolean getHasCheckedForResume() {
        return this.hasCheckedForResume;
    }

    public final void setHasCheckedForResume(boolean z) {
        this.hasCheckedForResume = z;
    }

    @Nullable
    public final Boolean isPlaying() {
        return this.isPlaying;
    }

    public final boolean isClearable() {
        return this.isClearable;
    }

    public final long getLastActive() {
        return this.lastActive;
    }

    @NotNull
    public final InstanceId getInstanceId() {
        return this.instanceId;
    }

    public final int getAppUid() {
        return this.appUid;
    }

    /* compiled from: MediaData.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final boolean isLocalSession() {
        return this.playbackLocation == 0;
    }
}
