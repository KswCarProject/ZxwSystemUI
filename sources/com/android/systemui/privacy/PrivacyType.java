package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyItem.kt */
public enum PrivacyType {
    TYPE_CAMERA(R$string.privacy_type_camera, 17303170, "android.permission-group.CAMERA", "camera"),
    TYPE_MICROPHONE(R$string.privacy_type_microphone, 17303175, "android.permission-group.MICROPHONE", "microphone"),
    TYPE_LOCATION(R$string.privacy_type_location, 17303174, "android.permission-group.LOCATION", "location"),
    TYPE_MEDIA_PROJECTION(R$string.privacy_type_media_projection, R$drawable.stat_sys_cast, "android.permission-group.UNDEFINED", "media projection");
    
    private final int iconId;
    @NotNull
    private final String logName;
    private final int nameId;
    @NotNull
    private final String permGroupName;

    /* access modifiers changed from: public */
    PrivacyType(int i, int i2, String str, String str2) {
        this.nameId = i;
        this.iconId = i2;
        this.permGroupName = str;
        this.logName = str2;
    }

    public final int getNameId() {
        return this.nameId;
    }

    public final int getIconId() {
        return this.iconId;
    }

    @NotNull
    public final String getPermGroupName() {
        return this.permGroupName;
    }

    @NotNull
    public final String getLogName() {
        return this.logName;
    }

    public final String getName(@NotNull Context context) {
        return context.getResources().getString(this.nameId);
    }

    public final Drawable getIcon(@NotNull Context context) {
        return context.getResources().getDrawable(this.iconId, context.getTheme());
    }
}
