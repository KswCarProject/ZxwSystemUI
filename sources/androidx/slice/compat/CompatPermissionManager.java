package androidx.slice.compat;

import android.content.Context;

public class CompatPermissionManager {
    public final String[] mAutoGrantPermissions;
    public final Context mContext;
    public final int mMyUid;
    public final String mPrefsName;

    public CompatPermissionManager(Context context, String str, int i, String[] strArr) {
        this.mContext = context;
        this.mPrefsName = str;
        this.mMyUid = i;
        this.mAutoGrantPermissions = strArr;
    }
}
