package com.szchoiceway.eventcenter;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import java.util.HashMap;
import java.util.Map;

public class EventUtils {
    public static boolean ENABLE_CUSTOMER_PANELBAR = false;
    public static IActivityManager mIam = ActivityManagerNative.getDefault();
    public static final Map<Integer, Object> mValueList = new HashMap();
}
