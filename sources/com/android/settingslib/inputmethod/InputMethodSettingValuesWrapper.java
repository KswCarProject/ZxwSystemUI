package com.android.settingslib.inputmethod;

import android.content.ContentResolver;
import android.content.Context;
import android.util.SparseArray;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;

public class InputMethodSettingValuesWrapper {
    @GuardedBy({"sInstanceMapLock"})
    public static SparseArray<InputMethodSettingValuesWrapper> sInstanceMap = new SparseArray<>();
    public static final Object sInstanceMapLock = new Object();
    public final ContentResolver mContentResolver;
    public final InputMethodManager mImm;
    public final ArrayList<InputMethodInfo> mMethodList = new ArrayList<>();

    public static InputMethodSettingValuesWrapper getInstance(Context context) {
        int userId = context.getUserId();
        synchronized (sInstanceMapLock) {
            if (sInstanceMap.size() == 0) {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper = new InputMethodSettingValuesWrapper(context);
                sInstanceMap.put(userId, inputMethodSettingValuesWrapper);
                return inputMethodSettingValuesWrapper;
            } else if (sInstanceMap.indexOfKey(userId) >= 0) {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper2 = sInstanceMap.get(userId);
                return inputMethodSettingValuesWrapper2;
            } else {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper3 = new InputMethodSettingValuesWrapper(context);
                sInstanceMap.put(context.getUserId(), inputMethodSettingValuesWrapper3);
                return inputMethodSettingValuesWrapper3;
            }
        }
    }

    public InputMethodSettingValuesWrapper(Context context) {
        this.mContentResolver = context.getContentResolver();
        this.mImm = (InputMethodManager) context.getSystemService(InputMethodManager.class);
        refreshAllInputMethodAndSubtypes();
    }

    public void refreshAllInputMethodAndSubtypes() {
        this.mMethodList.clear();
        this.mMethodList.addAll(this.mImm.getInputMethodListAsUser(this.mContentResolver.getUserId(), 1));
    }
}
