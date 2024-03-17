package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AndroidInternalsModule_ProvideLockPatternUtilsFactory implements Factory<LockPatternUtils> {
    public final Provider<Context> contextProvider;
    public final AndroidInternalsModule module;

    public AndroidInternalsModule_ProvideLockPatternUtilsFactory(AndroidInternalsModule androidInternalsModule, Provider<Context> provider) {
        this.module = androidInternalsModule;
        this.contextProvider = provider;
    }

    public LockPatternUtils get() {
        return provideLockPatternUtils(this.module, this.contextProvider.get());
    }

    public static AndroidInternalsModule_ProvideLockPatternUtilsFactory create(AndroidInternalsModule androidInternalsModule, Provider<Context> provider) {
        return new AndroidInternalsModule_ProvideLockPatternUtilsFactory(androidInternalsModule, provider);
    }

    public static LockPatternUtils provideLockPatternUtils(AndroidInternalsModule androidInternalsModule, Context context) {
        return (LockPatternUtils) Preconditions.checkNotNullFromProvides(androidInternalsModule.provideLockPatternUtils(context));
    }
}
