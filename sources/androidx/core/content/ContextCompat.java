package androidx.core.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import java.io.File;
import java.util.concurrent.Executor;

@SuppressLint({"PrivateConstructorForUtilityClass"})
public class ContextCompat {
    public static final Object sLock = new Object();
    public static final Object sSync = new Object();

    public static boolean startActivities(Context context, Intent[] intentArr, Bundle bundle) {
        Api16Impl.startActivities(context, intentArr, bundle);
        return true;
    }

    public static File[] getExternalFilesDirs(Context context, String str) {
        return Api19Impl.getExternalFilesDirs(context, str);
    }

    public static File[] getExternalCacheDirs(Context context) {
        return Api19Impl.getExternalCacheDirs(context);
    }

    public static Drawable getDrawable(Context context, int i) {
        return Api21Impl.getDrawable(context, i);
    }

    public static ColorStateList getColorStateList(Context context, int i) {
        return ResourcesCompat.getColorStateList(context.getResources(), i, context.getTheme());
    }

    public static int getColor(Context context, int i) {
        return Api23Impl.getColor(context, i);
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        return Api24Impl.createDeviceProtectedStorageContext(context);
    }

    public static Executor getMainExecutor(Context context) {
        return Api28Impl.getMainExecutor(context);
    }

    public static <T> T getSystemService(Context context, Class<T> cls) {
        return Api23Impl.getSystemService(context, cls);
    }

    public static class Api16Impl {
        public static void startActivities(Context context, Intent[] intentArr, Bundle bundle) {
            context.startActivities(intentArr, bundle);
        }
    }

    public static class Api19Impl {
        public static File[] getExternalCacheDirs(Context context) {
            return context.getExternalCacheDirs();
        }

        public static File[] getExternalFilesDirs(Context context, String str) {
            return context.getExternalFilesDirs(str);
        }
    }

    public static class Api21Impl {
        public static Drawable getDrawable(Context context, int i) {
            return context.getDrawable(i);
        }
    }

    public static class Api23Impl {
        public static int getColor(Context context, int i) {
            return context.getColor(i);
        }

        public static <T> T getSystemService(Context context, Class<T> cls) {
            return context.getSystemService(cls);
        }
    }

    public static class Api24Impl {
        public static Context createDeviceProtectedStorageContext(Context context) {
            return context.createDeviceProtectedStorageContext();
        }
    }

    public static class Api28Impl {
        public static Executor getMainExecutor(Context context) {
            return context.getMainExecutor();
        }
    }
}
