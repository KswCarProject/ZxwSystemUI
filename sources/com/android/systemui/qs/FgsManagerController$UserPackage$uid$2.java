package com.android.systemui.qs;

import com.android.systemui.qs.FgsManagerController;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$UserPackage$uid$2 extends Lambda implements Function0<Integer> {
    public final /* synthetic */ FgsManagerController this$0;
    public final /* synthetic */ FgsManagerController.UserPackage this$1;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FgsManagerController$UserPackage$uid$2(FgsManagerController fgsManagerController, FgsManagerController.UserPackage userPackage) {
        super(0);
        this.this$0 = fgsManagerController;
        this.this$1 = userPackage;
    }

    @NotNull
    public final Integer invoke() {
        return Integer.valueOf(this.this$0.packageManager.getPackageUidAsUser(this.this$1.getPackageName(), this.this$1.getUserId()));
    }
}
