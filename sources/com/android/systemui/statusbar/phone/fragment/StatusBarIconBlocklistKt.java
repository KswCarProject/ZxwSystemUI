package com.android.systemui.statusbar.phone.fragment;

import android.content.res.Resources;
import com.android.systemui.R$array;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarIconBlocklist.kt */
public final class StatusBarIconBlocklistKt {
    @NotNull
    public static final List<String> getStatusBarIconBlocklist(@NotNull Resources resources, @NotNull SecureSettings secureSettings) {
        List list = ArraysKt___ArraysKt.toList((T[]) resources.getStringArray(R$array.config_collapsed_statusbar_icon_blocklist));
        String string = resources.getString(17041587);
        boolean z = secureSettings.getIntForUser("status_bar_show_vibrate_icon", 0, -2) == 0;
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            if (!((String) next).equals(string) || z) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
