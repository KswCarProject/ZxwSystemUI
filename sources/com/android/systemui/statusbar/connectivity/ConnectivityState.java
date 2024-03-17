package com.android.systemui.statusbar.connectivity;

import com.android.settingslib.SignalIcon$IconGroup;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConnectivityState.kt */
public class ConnectivityState {
    public boolean activityIn;
    public boolean activityOut;
    public boolean connected;
    public boolean enabled;
    @Nullable
    public SignalIcon$IconGroup iconGroup;
    public int inetCondition;
    public int level;
    public int rssi;
    public long time;

    @NotNull
    public String toString() {
        if (this.time == 0) {
            return Intrinsics.stringPlus("Empty ", getClass().getSimpleName());
        }
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    @NotNull
    public List<String> tableColumns() {
        return CollectionsKt__CollectionsKt.listOf("connected", "enabled", "activityIn", "activityOut", "level", "iconGroup", "inetCondition", "rssi", "time");
    }

    @NotNull
    public List<String> tableData() {
        Iterable<Object> listOf = CollectionsKt__CollectionsKt.listOf(Boolean.valueOf(this.connected), Boolean.valueOf(this.enabled), Boolean.valueOf(this.activityIn), Boolean.valueOf(this.activityOut), Integer.valueOf(this.level), this.iconGroup, Integer.valueOf(this.inetCondition), Integer.valueOf(this.rssi), ConnectivityStateKt.sSDF.format(Long.valueOf(this.time)));
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(listOf, 10));
        for (Object valueOf : listOf) {
            arrayList.add(String.valueOf(valueOf));
        }
        return arrayList;
    }

    public void copyFrom(@NotNull ConnectivityState connectivityState) {
        this.connected = connectivityState.connected;
        this.enabled = connectivityState.enabled;
        this.activityIn = connectivityState.activityIn;
        this.activityOut = connectivityState.activityOut;
        this.level = connectivityState.level;
        this.iconGroup = connectivityState.iconGroup;
        this.inetCondition = connectivityState.inetCondition;
        this.rssi = connectivityState.rssi;
        this.time = connectivityState.time;
    }

    public void toString(@NotNull StringBuilder sb) {
        sb.append("connected=" + this.connected + ',');
        sb.append("enabled=" + this.enabled + ',');
        sb.append("level=" + this.level + ',');
        sb.append("inetCondition=" + this.inetCondition + ',');
        sb.append("iconGroup=" + this.iconGroup + ',');
        sb.append("activityIn=" + this.activityIn + ',');
        sb.append("activityOut=" + this.activityOut + ',');
        sb.append("rssi=" + this.rssi + ',');
        sb.append(Intrinsics.stringPlus("lastModified=", ConnectivityStateKt.sSDF.format(Long.valueOf(this.time))));
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null || !Intrinsics.areEqual((Object) obj.getClass(), (Object) getClass())) {
            return false;
        }
        ConnectivityState connectivityState = (ConnectivityState) obj;
        if (connectivityState.connected == this.connected && connectivityState.enabled == this.enabled && connectivityState.level == this.level && connectivityState.inetCondition == this.inetCondition && connectivityState.iconGroup == this.iconGroup && connectivityState.activityIn == this.activityIn && connectivityState.activityOut == this.activityOut && connectivityState.rssi == this.rssi) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = ((((((((Boolean.hashCode(this.connected) * 31) + Boolean.hashCode(this.enabled)) * 31) + Boolean.hashCode(this.activityIn)) * 31) + Boolean.hashCode(this.activityOut)) * 31) + this.level) * 31;
        SignalIcon$IconGroup signalIcon$IconGroup = this.iconGroup;
        return ((((((hashCode + (signalIcon$IconGroup == null ? 0 : signalIcon$IconGroup.hashCode())) * 31) + this.inetCondition) * 31) + this.rssi) * 31) + Long.hashCode(this.time);
    }
}
