package com.android.systemui.privacy;

import android.content.Context;
import android.content.Intent;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogControllerKt$defaultDialogProvider$1 implements PrivacyDialogController.DialogProvider {
    @NotNull
    public PrivacyDialog makeDialog(@NotNull Context context, @NotNull List<PrivacyDialog.PrivacyElement> list, @NotNull Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4) {
        return new PrivacyDialog(context, list, function4);
    }
}
