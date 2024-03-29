package androidx.core.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;

public final class AppOpsManagerCompat {
    public static String permissionToOp(String str) {
        return Api23Impl.permissionToOp(str);
    }

    public static int noteProxyOpNoThrow(Context context, String str, String str2) {
        return Api23Impl.noteProxyOpNoThrow((AppOpsManager) Api23Impl.getSystemService(context, AppOpsManager.class), str, str2);
    }

    public static int checkOrNoteProxyOp(Context context, int i, String str, String str2) {
        AppOpsManager systemService = Api29Impl.getSystemService(context);
        int checkOpNoThrow = Api29Impl.checkOpNoThrow(systemService, str, Binder.getCallingUid(), str2);
        if (checkOpNoThrow != 0) {
            return checkOpNoThrow;
        }
        return Api29Impl.checkOpNoThrow(systemService, str, i, Api29Impl.getOpPackageName(context));
    }

    public static class Api29Impl {
        public static AppOpsManager getSystemService(Context context) {
            return (AppOpsManager) context.getSystemService(AppOpsManager.class);
        }

        public static int checkOpNoThrow(AppOpsManager appOpsManager, String str, int i, String str2) {
            if (appOpsManager == null) {
                return 1;
            }
            return appOpsManager.checkOpNoThrow(str, i, str2);
        }

        public static String getOpPackageName(Context context) {
            return context.getOpPackageName();
        }
    }

    public static class Api23Impl {
        public static String permissionToOp(String str) {
            return AppOpsManager.permissionToOp(str);
        }

        public static <T> T getSystemService(Context context, Class<T> cls) {
            return context.getSystemService(cls);
        }

        public static int noteProxyOpNoThrow(AppOpsManager appOpsManager, String str, String str2) {
            return appOpsManager.noteProxyOpNoThrow(str, str2);
        }
    }
}
