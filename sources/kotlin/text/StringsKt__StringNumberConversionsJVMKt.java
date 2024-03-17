package kotlin.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StringNumberConversionsJVM.kt */
public class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt {
    @Nullable
    public static final Float toFloatOrNull(@NotNull String str) {
        try {
            if (ScreenFloatValueRegEx.value.matches(str)) {
                return Float.valueOf(Float.parseFloat(str));
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
