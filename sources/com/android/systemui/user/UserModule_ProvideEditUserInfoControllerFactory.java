package com.android.systemui.user;

import com.android.settingslib.users.EditUserInfoController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class UserModule_ProvideEditUserInfoControllerFactory implements Factory<EditUserInfoController> {

    public static final class InstanceHolder {
        public static final UserModule_ProvideEditUserInfoControllerFactory INSTANCE = new UserModule_ProvideEditUserInfoControllerFactory();
    }

    public EditUserInfoController get() {
        return provideEditUserInfoController();
    }

    public static UserModule_ProvideEditUserInfoControllerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static EditUserInfoController provideEditUserInfoController() {
        return (EditUserInfoController) Preconditions.checkNotNullFromProvides(UserModule.provideEditUserInfoController());
    }
}
