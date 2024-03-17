package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R$styleable;
import com.android.systemui.R$string;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyChipBuilder.kt */
public final class PrivacyChipBuilder {
    @NotNull
    public final List<Pair<PrivacyApplication, List<PrivacyType>>> appsAndTypes;
    @NotNull
    public final Context context;
    public final String lastSeparator;
    public final String separator;
    @NotNull
    public final List<PrivacyType> types;

    public PrivacyChipBuilder(@NotNull Context context2, @NotNull List<PrivacyItem> list) {
        this.context = context2;
        this.separator = context2.getString(R$string.ongoing_privacy_dialog_separator);
        this.lastSeparator = context2.getString(R$string.ongoing_privacy_dialog_last_separator);
        Iterable<PrivacyItem> iterable = list;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (PrivacyItem privacyItem : iterable) {
            PrivacyApplication application = privacyItem.getApplication();
            Object obj = linkedHashMap.get(application);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(application, obj);
            }
            ((List) obj).add(privacyItem.getPrivacyType());
        }
        this.appsAndTypes = CollectionsKt___CollectionsKt.sortedWith(MapsKt___MapsKt.toList(linkedHashMap), ComparisonsKt__ComparisonsKt.compareBy(AnonymousClass3.INSTANCE, AnonymousClass4.INSTANCE));
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyItem privacyType : iterable) {
            arrayList.add(privacyType.getPrivacyType());
        }
        this.types = CollectionsKt___CollectionsKt.sorted(CollectionsKt___CollectionsKt.distinct(arrayList));
    }

    @NotNull
    public final List<Drawable> generateIcons() {
        Iterable<PrivacyType> iterable = this.types;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyType icon : iterable) {
            arrayList.add(icon.getIcon(this.context));
        }
        return arrayList;
    }

    public final <T> StringBuilder joinWithAnd(List<? extends T> list) {
        StringBuilder sb = (StringBuilder) CollectionsKt___CollectionsKt.joinTo$default(list.subList(0, list.size() - 1), new StringBuilder(), this.separator, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, R$styleable.AppCompatTheme_windowMinWidthMajor, (Object) null);
        sb.append(this.lastSeparator);
        sb.append(CollectionsKt___CollectionsKt.last(list));
        return sb;
    }

    @NotNull
    public final String joinTypes() {
        int size = this.types.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return this.types.get(0).getName(this.context);
        }
        Iterable<PrivacyType> iterable = this.types;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyType name : iterable) {
            arrayList.add(name.getName(this.context));
        }
        return joinWithAnd(arrayList).toString();
    }
}
