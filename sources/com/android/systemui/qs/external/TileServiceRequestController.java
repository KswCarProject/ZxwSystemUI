package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.systemui.R$string;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.external.TileRequestDialog;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final CommandQueue commandQueue;
    @NotNull
    public final TileServiceRequestController$commandQueueCallback$1 commandQueueCallback;
    @NotNull
    public final CommandRegistry commandRegistry;
    @Nullable
    public Function1<? super String, Unit> dialogCanceller;
    @NotNull
    public final Function0<TileRequestDialog> dialogCreator;
    @NotNull
    public final TileRequestDialogEventLogger eventLogger;
    @NotNull
    public final QSTileHost qsTileHost;

    public TileServiceRequestController(@NotNull QSTileHost qSTileHost, @NotNull CommandQueue commandQueue2, @NotNull CommandRegistry commandRegistry2, @NotNull TileRequestDialogEventLogger tileRequestDialogEventLogger, @NotNull Function0<TileRequestDialog> function0) {
        this.qsTileHost = qSTileHost;
        this.commandQueue = commandQueue2;
        this.commandRegistry = commandRegistry2;
        this.eventLogger = tileRequestDialogEventLogger;
        this.dialogCreator = function0;
        this.commandQueueCallback = new TileServiceRequestController$commandQueueCallback$1(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TileServiceRequestController(final QSTileHost qSTileHost, CommandQueue commandQueue2, CommandRegistry commandRegistry2, TileRequestDialogEventLogger tileRequestDialogEventLogger, Function0 function0, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(qSTileHost, commandQueue2, commandRegistry2, tileRequestDialogEventLogger, (i & 16) != 0 ? new Function0<TileRequestDialog>() {
            @NotNull
            public final TileRequestDialog invoke() {
                return new TileRequestDialog(qSTileHost.getContext());
            }
        } : function0);
    }

    /* compiled from: TileServiceRequestController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void init() {
        this.commandRegistry.registerCommand("tile-service-add", new TileServiceRequestController$init$1(this));
        this.commandQueue.addCallback((CommandQueue.Callbacks) this.commandQueueCallback);
    }

    public final void addTile(ComponentName componentName) {
        this.qsTileHost.addTile(componentName, true);
    }

    public final void requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull ComponentName componentName, @NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @Nullable Icon icon, @NotNull Consumer<Integer> consumer) {
        InstanceId newInstanceId = this.eventLogger.newInstanceId();
        String packageName = componentName.getPackageName();
        if (isTileAlreadyAdded(componentName)) {
            consumer.accept(1);
            this.eventLogger.logTileAlreadyAdded(packageName, newInstanceId);
            return;
        }
        SystemUIDialog createDialog = createDialog(new TileRequestDialog.TileData(charSequence, charSequence2, icon), new SingleShotConsumer(new TileServiceRequestController$requestTileAdd$dialogResponse$1(this, componentName, packageName, newInstanceId, consumer)));
        this.dialogCanceller = new TileServiceRequestController$requestTileAdd$1$1(packageName, createDialog, this);
        createDialog.show();
        this.eventLogger.logDialogShown(packageName, newInstanceId);
    }

    public final SystemUIDialog createDialog(TileRequestDialog.TileData tileData, SingleShotConsumer<Integer> singleShotConsumer) {
        TileServiceRequestController$createDialog$dialogClickListener$1 tileServiceRequestController$createDialog$dialogClickListener$1 = new TileServiceRequestController$createDialog$dialogClickListener$1(singleShotConsumer);
        TileRequestDialog invoke = this.dialogCreator.invoke();
        TileRequestDialog tileRequestDialog = invoke;
        tileRequestDialog.setTileData(tileData);
        tileRequestDialog.setShowForAllUsers(true);
        tileRequestDialog.setCanceledOnTouchOutside(true);
        tileRequestDialog.setOnCancelListener(new TileServiceRequestController$createDialog$1$1(singleShotConsumer));
        tileRequestDialog.setOnDismissListener(new TileServiceRequestController$createDialog$1$2(singleShotConsumer));
        tileRequestDialog.setPositiveButton(R$string.qs_tile_request_dialog_add, tileServiceRequestController$createDialog$dialogClickListener$1);
        tileRequestDialog.setNegativeButton(R$string.qs_tile_request_dialog_not_add, tileServiceRequestController$createDialog$dialogClickListener$1);
        return invoke;
    }

    public final boolean isTileAlreadyAdded(ComponentName componentName) {
        return this.qsTileHost.indexOf(CustomTile.toSpec(componentName)) != -1;
    }

    /* compiled from: TileServiceRequestController.kt */
    public final class TileServiceRequestCommand implements Command {
        public TileServiceRequestCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(list.get(0));
            if (unflattenFromString == null) {
                Log.w("TileServiceRequestController", Intrinsics.stringPlus("Malformed componentName ", list.get(0)));
            } else {
                TileServiceRequestController.this.requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(unflattenFromString, list.get(1), list.get(2), (Icon) null, TileServiceRequestController$TileServiceRequestCommand$execute$1.INSTANCE);
            }
        }
    }

    /* compiled from: TileServiceRequestController.kt */
    public static final class SingleShotConsumer<T> implements Consumer<T> {
        @NotNull
        public final Consumer<T> consumer;
        @NotNull
        public final AtomicBoolean dispatched = new AtomicBoolean(false);

        public SingleShotConsumer(@NotNull Consumer<T> consumer2) {
            this.consumer = consumer2;
        }

        public void accept(T t) {
            if (this.dispatched.compareAndSet(false, true)) {
                this.consumer.accept(t);
            }
        }
    }

    /* compiled from: TileServiceRequestController.kt */
    public static final class Builder {
        @NotNull
        public final CommandQueue commandQueue;
        @NotNull
        public final CommandRegistry commandRegistry;

        public Builder(@NotNull CommandQueue commandQueue2, @NotNull CommandRegistry commandRegistry2) {
            this.commandQueue = commandQueue2;
            this.commandRegistry = commandRegistry2;
        }

        @NotNull
        public final TileServiceRequestController create(@NotNull QSTileHost qSTileHost) {
            return new TileServiceRequestController(qSTileHost, this.commandQueue, this.commandRegistry, new TileRequestDialogEventLogger(), (Function0) null, 16, (DefaultConstructorMarker) null);
        }
    }
}
