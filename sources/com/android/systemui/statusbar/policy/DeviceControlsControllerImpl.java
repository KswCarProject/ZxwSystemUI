package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.R$array;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DeviceControlsControllerImpl.kt */
public final class DeviceControlsControllerImpl implements DeviceControlsController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public DeviceControlsController.Callback callback;
    @NotNull
    public final Context context;
    @NotNull
    public final ControlsComponent controlsComponent;
    @NotNull
    public final DeviceControlsControllerImpl$listingCallback$1 listingCallback = new DeviceControlsControllerImpl$listingCallback$1(this);
    @Nullable
    public Integer position;
    @NotNull
    public final SecureSettings secureSettings;
    @NotNull
    public final UserContextProvider userContextProvider;

    public DeviceControlsControllerImpl(@NotNull Context context2, @NotNull ControlsComponent controlsComponent2, @NotNull UserContextProvider userContextProvider2, @NotNull SecureSettings secureSettings2) {
        this.context = context2;
        this.controlsComponent = controlsComponent2;
        this.userContextProvider = userContextProvider2;
        this.secureSettings = secureSettings2;
    }

    @Nullable
    public final Integer getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.position;
    }

    public final void setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable Integer num) {
        this.position = num;
    }

    /* compiled from: DeviceControlsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void checkMigrationToQs() {
        this.controlsComponent.getControlsController().ifPresent(new DeviceControlsControllerImpl$checkMigrationToQs$1(this));
    }

    public void setCallback(@NotNull DeviceControlsController.Callback callback2) {
        removeCallback();
        this.callback = callback2;
        if (this.secureSettings.getInt("controls_enabled", 1) == 0) {
            fireControlsUpdate();
            return;
        }
        checkMigrationToQs();
        this.controlsComponent.getControlsListingController().ifPresent(new DeviceControlsControllerImpl$setCallback$1(this));
    }

    public void removeCallback() {
        this.position = null;
        this.callback = null;
        this.controlsComponent.getControlsListingController().ifPresent(new DeviceControlsControllerImpl$removeCallback$1(this));
    }

    public final void fireControlsUpdate() {
        Log.i("DeviceControlsControllerImpl", Intrinsics.stringPlus("Setting DeviceControlsTile position: ", this.position));
        DeviceControlsController.Callback callback2 = this.callback;
        if (callback2 != null) {
            callback2.onControlsUpdate(this.position);
        }
    }

    public final void seedFavorites(List<ControlsServiceInfo> list) {
        String[] stringArray = this.context.getResources().getStringArray(R$array.config_controlsPreferredPackages);
        SharedPreferences sharedPreferences = this.userContextProvider.getUserContext().getSharedPreferences("controls_prefs", 0);
        Set<String> stringSet = sharedPreferences.getStringSet("SeedingCompleted", SetsKt__SetsKt.emptySet());
        ControlsController controlsController = this.controlsComponent.getControlsController().get();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < Math.min(2, stringArray.length); i++) {
            String str = stringArray[i];
            for (ControlsServiceInfo controlsServiceInfo : list) {
                if (str.equals(controlsServiceInfo.componentName.getPackageName()) && !stringSet.contains(str)) {
                    if (controlsController.countFavoritesForComponent(controlsServiceInfo.componentName) > 0) {
                        addPackageToSeededSet(sharedPreferences, str);
                    } else {
                        arrayList.add(controlsServiceInfo.componentName);
                    }
                }
            }
        }
        if (!arrayList.isEmpty()) {
            controlsController.seedFavoritesForComponents(arrayList, new DeviceControlsControllerImpl$seedFavorites$2(this, sharedPreferences));
        }
    }

    public final void addPackageToSeededSet(SharedPreferences sharedPreferences, String str) {
        Set mutableSet = CollectionsKt___CollectionsKt.toMutableSet(sharedPreferences.getStringSet("SeedingCompleted", SetsKt__SetsKt.emptySet()));
        mutableSet.add(str);
        sharedPreferences.edit().putStringSet("SeedingCompleted", mutableSet).apply();
    }
}
