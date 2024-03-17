package com.android.systemui.statusbar.phone;

import com.android.systemui.R$bool;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarContentInsetsProvider.kt */
public final class StatusBarContentInsetsProvider$isPrivacyDotEnabled$2 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ StatusBarContentInsetsProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(0);
        this.this$0 = statusBarContentInsetsProvider;
    }

    @NotNull
    public final Boolean invoke() {
        return Boolean.valueOf(this.this$0.getContext().getResources().getBoolean(R$bool.config_enablePrivacyDot));
    }
}
