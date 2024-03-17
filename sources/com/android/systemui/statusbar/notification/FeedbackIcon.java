package com.android.systemui.statusbar.notification;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FeedbackIcon.kt */
public final class FeedbackIcon {
    public final int contentDescRes;
    public final int iconRes;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FeedbackIcon)) {
            return false;
        }
        FeedbackIcon feedbackIcon = (FeedbackIcon) obj;
        return this.iconRes == feedbackIcon.iconRes && this.contentDescRes == feedbackIcon.contentDescRes;
    }

    public int hashCode() {
        return (Integer.hashCode(this.iconRes) * 31) + Integer.hashCode(this.contentDescRes);
    }

    @NotNull
    public String toString() {
        return "FeedbackIcon(iconRes=" + this.iconRes + ", contentDescRes=" + this.contentDescRes + ')';
    }

    public FeedbackIcon(int i, int i2) {
        this.iconRes = i;
        this.contentDescRes = i2;
    }

    public final int getIconRes() {
        return this.iconRes;
    }

    public final int getContentDescRes() {
        return this.contentDescRes;
    }
}
