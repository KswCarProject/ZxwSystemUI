package kotlin.text;

import androidx.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Indent.kt */
public class StringsKt__IndentKt extends StringsKt__AppendableKt {
    @NotNull
    public static final String trimIndent(@NotNull String str) {
        return replaceIndent(str, "");
    }

    @NotNull
    public static final String replaceIndent(@NotNull String str, @NotNull String str2) {
        int i;
        List<String> lines = StringsKt__StringsKt.lines(str);
        ArrayList<String> arrayList = new ArrayList<>();
        for (Object next : lines) {
            if (!StringsKt__StringsJVMKt.isBlank((String) next)) {
                arrayList.add(next);
            }
        }
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10));
        for (String indentWidth$StringsKt__IndentKt : arrayList) {
            arrayList2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(indentWidth$StringsKt__IndentKt)));
        }
        Integer num = (Integer) CollectionsKt___CollectionsKt.minOrNull(arrayList2);
        int i2 = 0;
        if (num == null) {
            i = 0;
        } else {
            i = num.intValue();
        }
        int length = str.length() + (str2.length() * lines.size());
        Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(str2);
        int lastIndex = CollectionsKt__CollectionsKt.getLastIndex(lines);
        ArrayList arrayList3 = new ArrayList();
        for (Object next2 : lines) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            String str3 = (String) next2;
            if ((i2 == 0 || i2 == lastIndex) && StringsKt__StringsJVMKt.isBlank(str3)) {
                str3 = null;
            } else {
                String drop = StringsKt___StringsKt.drop(str3, i);
                if (drop != null) {
                    str3 = indentFunction$StringsKt__IndentKt.invoke(drop);
                }
            }
            if (str3 != null) {
                arrayList3.add(str3);
            }
            i2 = i3;
        }
        return ((StringBuilder) CollectionsKt___CollectionsKt.joinTo$default(arrayList3, new StringBuilder(length), "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, R$styleable.AppCompatTheme_windowMinWidthMajor, (Object) null)).toString();
    }

    public static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String str) {
        if (str.length() == 0) {
            return StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        }
        return new StringsKt__IndentKt$getIndentFunction$2(str);
    }

    public static final int indentWidth$StringsKt__IndentKt(String str) {
        int length = str.length();
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            }
            int i2 = i + 1;
            if (!CharsKt__CharJVMKt.isWhitespace(str.charAt(i))) {
                break;
            }
            i = i2;
        }
        return i == -1 ? str.length() : i;
    }
}
