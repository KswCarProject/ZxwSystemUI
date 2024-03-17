package com.android.systemui.camera;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.android.systemui.R$string;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CameraIntents.kt */
public final class CameraIntents {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final String DEFAULT_INSECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA";
    @NotNull
    public static final String DEFAULT_SECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA_SECURE";

    @NotNull
    public static final Intent getInsecureCameraIntent(@NotNull Context context) {
        return Companion.getInsecureCameraIntent(context);
    }

    @Nullable
    public static final String getOverrideCameraPackage(@NotNull Context context) {
        return Companion.getOverrideCameraPackage(context);
    }

    @NotNull
    public static final Intent getSecureCameraIntent(@NotNull Context context) {
        return Companion.getSecureCameraIntent(context);
    }

    public static final boolean isInsecureCameraIntent(@Nullable Intent intent) {
        return Companion.isInsecureCameraIntent(intent);
    }

    public static final boolean isSecureCameraIntent(@Nullable Intent intent) {
        return Companion.isSecureCameraIntent(intent);
    }

    /* compiled from: CameraIntents.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final String getDEFAULT_SECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_SECURE_CAMERA_INTENT_ACTION;
        }

        @NotNull
        public final String getDEFAULT_INSECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_INSECURE_CAMERA_INTENT_ACTION;
        }

        @Nullable
        public final String getOverrideCameraPackage(@NotNull Context context) {
            String string = context.getResources().getString(R$string.config_cameraGesturePackage);
            if (string != null && !TextUtils.isEmpty(string)) {
                return string;
            }
            return null;
        }

        @NotNull
        public final Intent getInsecureCameraIntent(@NotNull Context context) {
            Intent intent = new Intent(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            return intent;
        }

        @NotNull
        public final Intent getSecureCameraIntent(@NotNull Context context) {
            Intent intent = new Intent(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            return intent.addFlags(8388608);
        }

        public final boolean isSecureCameraIntent(@Nullable Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return false;
            }
            return action.equals(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
        }

        public final boolean isInsecureCameraIntent(@Nullable Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return false;
            }
            return action.equals(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
        }
    }
}
