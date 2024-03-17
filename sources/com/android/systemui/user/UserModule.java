package com.android.systemui.user;

import com.android.settingslib.users.EditUserInfoController;

public abstract class UserModule {
    public static EditUserInfoController provideEditUserInfoController() {
        return new EditUserInfoController("com.android.systemui.fileprovider");
    }
}
