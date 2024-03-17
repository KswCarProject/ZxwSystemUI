package com.android.systemui.privacy;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyItem.kt */
public final class PrivacyItem {
    @NotNull
    public final PrivacyApplication application;
    @NotNull
    public final String log;
    public final boolean paused;
    @NotNull
    public final PrivacyType privacyType;
    public final long timeStampElapsed;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyItem)) {
            return false;
        }
        PrivacyItem privacyItem = (PrivacyItem) obj;
        return this.privacyType == privacyItem.privacyType && Intrinsics.areEqual((Object) this.application, (Object) privacyItem.application) && this.timeStampElapsed == privacyItem.timeStampElapsed && this.paused == privacyItem.paused;
    }

    public int hashCode() {
        int hashCode = ((((this.privacyType.hashCode() * 31) + this.application.hashCode()) * 31) + Long.hashCode(this.timeStampElapsed)) * 31;
        boolean z = this.paused;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "PrivacyItem(privacyType=" + this.privacyType + ", application=" + this.application + ", timeStampElapsed=" + this.timeStampElapsed + ", paused=" + this.paused + ')';
    }

    public PrivacyItem(@NotNull PrivacyType privacyType2, @NotNull PrivacyApplication privacyApplication, long j, boolean z) {
        this.privacyType = privacyType2;
        this.application = privacyApplication;
        this.timeStampElapsed = j;
        this.paused = z;
        this.log = '(' + privacyType2.getLogName() + ", " + privacyApplication.getPackageName() + '(' + privacyApplication.getUid() + "), " + j + ", paused=" + z + ')';
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ PrivacyItem(PrivacyType privacyType2, PrivacyApplication privacyApplication, long j, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(privacyType2, privacyApplication, (i & 4) != 0 ? -1 : j, (i & 8) != 0 ? false : z);
    }

    @NotNull
    public final PrivacyType getPrivacyType() {
        return this.privacyType;
    }

    @NotNull
    public final PrivacyApplication getApplication() {
        return this.application;
    }

    public final long getTimeStampElapsed() {
        return this.timeStampElapsed;
    }

    public final boolean getPaused() {
        return this.paused;
    }

    @NotNull
    public final String getLog() {
        return this.log;
    }
}
