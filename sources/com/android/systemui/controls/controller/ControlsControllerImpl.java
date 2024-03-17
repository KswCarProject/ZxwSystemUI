package com.android.systemui.controls.controller;

import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt___SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl implements Dumpable, ControlsController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper;
    @NotNull
    public final ControlsBindingController bindingController;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final Context context;
    @NotNull
    public UserHandle currentUser;
    @NotNull
    public final DelayableExecutor executor;
    @NotNull
    public final ControlsControllerImpl$listingCallback$1 listingCallback;
    @NotNull
    public final ControlsListingController listingController;
    @NotNull
    public final ControlsFavoritePersistenceWrapper persistenceWrapper;
    @NotNull
    public final BroadcastReceiver restoreFinishedReceiver;
    @NotNull
    public final List<Consumer<Boolean>> seedingCallbacks = new ArrayList();
    public boolean seedingInProgress;
    @NotNull
    public final ContentObserver settingObserver;
    @NotNull
    public final ControlsUiController uiController;
    public boolean userChanging = true;
    @NotNull
    public UserStructure userStructure;
    @NotNull
    public final ControlsControllerImpl$userSwitchReceiver$1 userSwitchReceiver;

    @VisibleForTesting
    public static /* synthetic */ void getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getRestoreFinishedReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSettingObserver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public ControlsControllerImpl(@NotNull Context context2, @NotNull DelayableExecutor delayableExecutor, @NotNull ControlsUiController controlsUiController, @NotNull ControlsBindingController controlsBindingController, @NotNull ControlsListingController controlsListingController, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull Optional<ControlsFavoritePersistenceWrapper> optional, @NotNull DumpManager dumpManager, @NotNull UserTracker userTracker) {
        Context context3 = context2;
        DelayableExecutor delayableExecutor2 = delayableExecutor;
        ControlsListingController controlsListingController2 = controlsListingController;
        this.context = context3;
        this.executor = delayableExecutor2;
        this.uiController = controlsUiController;
        this.bindingController = controlsBindingController;
        this.listingController = controlsListingController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        UserHandle userHandle = userTracker.getUserHandle();
        this.currentUser = userHandle;
        this.userStructure = new UserStructure(context3, userHandle);
        this.persistenceWrapper = optional.orElseGet(new Supplier(this) {
            public final /* synthetic */ ControlsControllerImpl this$0;

            {
                this.this$0 = r1;
            }

            public final ControlsFavoritePersistenceWrapper get() {
                return new ControlsFavoritePersistenceWrapper(this.this$0.userStructure.getFile(), this.this$0.executor, new BackupManager(this.this$0.userStructure.getUserContext()));
            }
        });
        this.auxiliaryPersistenceWrapper = new AuxiliaryPersistenceWrapper(this.userStructure.getAuxiliaryFile(), delayableExecutor2);
        ControlsControllerImpl$userSwitchReceiver$1 controlsControllerImpl$userSwitchReceiver$1 = new ControlsControllerImpl$userSwitchReceiver$1(this);
        this.userSwitchReceiver = controlsControllerImpl$userSwitchReceiver$1;
        ControlsControllerImpl$restoreFinishedReceiver$1 controlsControllerImpl$restoreFinishedReceiver$1 = new ControlsControllerImpl$restoreFinishedReceiver$1(this);
        this.restoreFinishedReceiver = controlsControllerImpl$restoreFinishedReceiver$1;
        this.settingObserver = new ControlsControllerImpl$settingObserver$1(this);
        ControlsControllerImpl$listingCallback$1 controlsControllerImpl$listingCallback$1 = new ControlsControllerImpl$listingCallback$1(this);
        this.listingCallback = controlsControllerImpl$listingCallback$1;
        dumpManager.registerDumpable(ControlsControllerImpl.class.getName(), this);
        resetFavorites();
        this.userChanging = false;
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, controlsControllerImpl$userSwitchReceiver$1, new IntentFilter("android.intent.action.USER_SWITCHED"), delayableExecutor2, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        context2.registerReceiver(controlsControllerImpl$restoreFinishedReceiver$1, new IntentFilter("com.android.systemui.backup.RESTORE_FINISHED"), "com.android.systemui.permission.SELF", (Handler) null, 4);
        controlsListingController2.addCallback(controlsControllerImpl$listingCallback$1);
    }

    /* compiled from: ControlsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public int getCurrentUserId() {
        return this.currentUser.getIdentifier();
    }

    @NotNull
    public final AuxiliaryPersistenceWrapper getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.auxiliaryPersistenceWrapper;
    }

    public final void setValuesForUser(UserHandle userHandle) {
        Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Changing to user: ", userHandle));
        this.currentUser = userHandle;
        UserStructure userStructure2 = new UserStructure(this.context, userHandle);
        this.userStructure = userStructure2;
        this.persistenceWrapper.changeFileAndBackupManager(userStructure2.getFile(), new BackupManager(this.userStructure.getUserContext()));
        this.auxiliaryPersistenceWrapper.changeFile(this.userStructure.getAuxiliaryFile());
        resetFavorites();
        this.bindingController.changeUser(userHandle);
        this.listingController.changeUser(userHandle);
        this.userChanging = false;
    }

    public final void resetFavorites() {
        Favorites favorites = Favorites.INSTANCE;
        favorites.clear();
        favorites.load(this.persistenceWrapper.readFavorites());
    }

    public final boolean confirmAvailability() {
        if (!this.userChanging) {
            return true;
        }
        Log.w("ControlsControllerImpl", "Controls not available while user is changing");
        return false;
    }

    public void loadForComponent(@NotNull ComponentName componentName, @NotNull Consumer<ControlsController.LoadData> consumer, @NotNull Consumer<Runnable> consumer2) {
        if (!confirmAvailability()) {
            if (this.userChanging) {
                this.executor.executeDelayed(new ControlsControllerImpl$loadForComponent$1(this, componentName, consumer, consumer2), 500, TimeUnit.MILLISECONDS);
            }
            consumer.accept(ControlsControllerKt.createLoadDataObject(CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), true));
        }
        consumer2.accept(this.bindingController.bindAndLoad(componentName, new ControlsControllerImpl$loadForComponent$2(this, componentName, consumer)));
    }

    public boolean addSeedingFavoritesCallback(@NotNull Consumer<Boolean> consumer) {
        if (!this.seedingInProgress) {
            return false;
        }
        this.executor.execute(new ControlsControllerImpl$addSeedingFavoritesCallback$1(this, consumer));
        return true;
    }

    public void seedFavoritesForComponents(@NotNull List<ComponentName> list, @NotNull Consumer<SeedResponse> consumer) {
        if (!this.seedingInProgress && !list.isEmpty()) {
            if (confirmAvailability()) {
                this.seedingInProgress = true;
                startSeeding(list, consumer, false);
            } else if (this.userChanging) {
                this.executor.executeDelayed(new ControlsControllerImpl$seedFavoritesForComponents$1(this, list, consumer), 500, TimeUnit.MILLISECONDS);
            } else {
                for (ComponentName packageName : list) {
                    consumer.accept(new SeedResponse(packageName.getPackageName(), false));
                }
            }
        }
    }

    public final void startSeeding(List<ComponentName> list, Consumer<SeedResponse> consumer, boolean z) {
        if (list.isEmpty()) {
            endSeedingCall(!z);
            return;
        }
        ComponentName componentName = list.get(0);
        Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Beginning request to seed favorites for: ", componentName));
        this.bindingController.bindAndLoadSuggested(componentName, new ControlsControllerImpl$startSeeding$1(this, consumer, componentName, CollectionsKt___CollectionsKt.drop(list, 1), z));
    }

    public final void endSeedingCall(boolean z) {
        this.seedingInProgress = false;
        for (Consumer accept : this.seedingCallbacks) {
            accept.accept(Boolean.valueOf(z));
        }
        this.seedingCallbacks.clear();
    }

    public static /* synthetic */ ControlStatus createRemovedStatus$default(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = true;
        }
        return controlsControllerImpl.createRemovedStatus(componentName, controlInfo, charSequence, z);
    }

    public final ControlStatus createRemovedStatus(ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(componentName.getPackageName());
        return new ControlStatus(new Control.StatelessBuilder(controlInfo.getControlId(), PendingIntent.getActivity(this.context, componentName.hashCode(), intent, 67108864)).setTitle(controlInfo.getControlTitle()).setSubtitle(controlInfo.getControlSubtitle()).setStructure(charSequence).setDeviceType(controlInfo.getDeviceType()).build(), componentName, true, z);
    }

    public final Set<String> findRemoved(Set<String> set, List<Control> list) {
        Iterable<Control> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (Control controlId : iterable) {
            arrayList.add(controlId.getControlId());
        }
        return SetsKt___SetsKt.minus(set, arrayList);
    }

    public void subscribeToFavorites(@NotNull StructureInfo structureInfo) {
        if (confirmAvailability()) {
            this.bindingController.subscribe(structureInfo);
        }
    }

    public void unsubscribe() {
        if (confirmAvailability()) {
            this.bindingController.unsubscribe();
        }
    }

    public void addFavorite(@NotNull ComponentName componentName, @NotNull CharSequence charSequence, @NotNull ControlInfo controlInfo) {
        if (confirmAvailability()) {
            this.executor.execute(new ControlsControllerImpl$addFavorite$1(componentName, charSequence, controlInfo, this));
        }
    }

    public void replaceFavoritesForStructure(@NotNull StructureInfo structureInfo) {
        if (confirmAvailability()) {
            this.executor.execute(new ControlsControllerImpl$replaceFavoritesForStructure$1(structureInfo, this));
        }
    }

    public void refreshStatus(@NotNull ComponentName componentName, @NotNull Control control) {
        if (!confirmAvailability()) {
            Log.d("ControlsControllerImpl", "Controls not available");
            return;
        }
        if (control.getStatus() == 1) {
            this.executor.execute(new ControlsControllerImpl$refreshStatus$1(componentName, control, this));
        }
        this.uiController.onRefreshState(componentName, CollectionsKt__CollectionsJVMKt.listOf(control));
    }

    public void onActionResponse(@NotNull ComponentName componentName, @NotNull String str, int i) {
        if (confirmAvailability()) {
            this.uiController.onActionResponse(componentName, str, i);
        }
    }

    public void action(@NotNull ComponentName componentName, @NotNull ControlInfo controlInfo, @NotNull ControlAction controlAction) {
        if (confirmAvailability()) {
            this.bindingController.action(componentName, controlInfo, controlAction);
        }
    }

    @NotNull
    public List<StructureInfo> getFavorites() {
        return Favorites.INSTANCE.getAllStructures();
    }

    public int countFavoritesForComponent(@NotNull ComponentName componentName) {
        return Favorites.INSTANCE.getControlsForComponent(componentName).size();
    }

    @NotNull
    public List<StructureInfo> getFavoritesForComponent(@NotNull ComponentName componentName) {
        return Favorites.INSTANCE.getStructuresForComponent(componentName);
    }

    @NotNull
    public List<ControlInfo> getFavoritesForStructure(@NotNull ComponentName componentName, @NotNull CharSequence charSequence) {
        return Favorites.INSTANCE.getControlsForStructure(new StructureInfo(componentName, charSequence, CollectionsKt__CollectionsKt.emptyList()));
    }

    @NotNull
    public StructureInfo getPreferredStructure() {
        return this.uiController.getPreferredStructure(getFavorites());
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("ControlsController state:");
        printWriter.println(Intrinsics.stringPlus("  Changing users: ", Boolean.valueOf(this.userChanging)));
        printWriter.println(Intrinsics.stringPlus("  Current user: ", Integer.valueOf(this.currentUser.getIdentifier())));
        printWriter.println("  Favorites:");
        for (StructureInfo structureInfo : Favorites.INSTANCE.getAllStructures()) {
            printWriter.println(Intrinsics.stringPlus("    ", structureInfo));
            for (ControlInfo stringPlus : structureInfo.getControls()) {
                printWriter.println(Intrinsics.stringPlus("      ", stringPlus));
            }
        }
        printWriter.println(this.bindingController.toString());
    }
}
