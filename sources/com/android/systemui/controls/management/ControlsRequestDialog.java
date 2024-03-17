package com.android.systemui.controls.management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.ui.RenderInfo;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.LifecycleActivity;
import java.util.Collection;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsRequestDialog.kt */
public class ControlsRequestDialog extends LifecycleActivity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final ControlsRequestDialog$callback$1 callback = new ControlsRequestDialog$callback$1();
    public Control control;
    public ComponentName controlComponent;
    @NotNull
    public final ControlsController controller;
    @NotNull
    public final ControlsListingController controlsListingController;
    @NotNull
    public final ControlsRequestDialog$currentUserTracker$1 currentUserTracker;
    @Nullable
    public Dialog dialog;

    public ControlsRequestDialog(@NotNull ControlsController controlsController, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull ControlsListingController controlsListingController2) {
        this.controller = controlsController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.controlsListingController = controlsListingController2;
        this.currentUserTracker = new ControlsRequestDialog$currentUserTracker$1(this, broadcastDispatcher2);
    }

    /* compiled from: ControlsRequestDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.currentUserTracker.startTracking();
        this.controlsListingController.addCallback(this.callback);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -10000);
        int currentUserId = this.controller.getCurrentUserId();
        if (intExtra != currentUserId) {
            Log.w("ControlsRequestDialog", "Current user (" + currentUserId + ") different from request user (" + intExtra + ')');
            finish();
        }
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        if (componentName == null) {
            Log.e("ControlsRequestDialog", "Request did not contain componentName");
            finish();
            return;
        }
        this.controlComponent = componentName;
        Control parcelableExtra = getIntent().getParcelableExtra("android.service.controls.extra.CONTROL");
        if (parcelableExtra == null) {
            Log.e("ControlsRequestDialog", "Request did not contain control");
            finish();
            return;
        }
        this.control = parcelableExtra;
    }

    public void onResume() {
        super.onResume();
        CharSequence verifyComponentAndGetLabel = verifyComponentAndGetLabel();
        ComponentName componentName = null;
        if (verifyComponentAndGetLabel == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("The component specified (");
            ComponentName componentName2 = this.controlComponent;
            if (componentName2 != null) {
                componentName = componentName2;
            }
            sb.append(componentName.flattenToString());
            sb.append(" is not a valid ControlsProviderService");
            Log.e("ControlsRequestDialog", sb.toString());
            finish();
            return;
        }
        if (isCurrentFavorite()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The control ");
            ComponentName componentName3 = this.control;
            if (componentName3 != null) {
                componentName = componentName3;
            }
            sb2.append(componentName.getTitle());
            sb2.append(" is already a favorite");
            Log.w("ControlsRequestDialog", sb2.toString());
            finish();
        }
        Dialog createDialog = createDialog(verifyComponentAndGetLabel);
        this.dialog = createDialog;
        if (createDialog != null) {
            createDialog.show();
        }
    }

    public void onDestroy() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        this.currentUserTracker.stopTracking();
        this.controlsListingController.removeCallback(this.callback);
        super.onDestroy();
    }

    public final CharSequence verifyComponentAndGetLabel() {
        ControlsListingController controlsListingController2 = this.controlsListingController;
        ComponentName componentName = this.controlComponent;
        if (componentName == null) {
            componentName = null;
        }
        return controlsListingController2.getAppLabel(componentName);
    }

    public final boolean isCurrentFavorite() {
        boolean z;
        ControlsController controlsController = this.controller;
        ComponentName componentName = this.controlComponent;
        if (componentName == null) {
            componentName = null;
        }
        Iterable<StructureInfo> favoritesForComponent = controlsController.getFavoritesForComponent(componentName);
        if (!(favoritesForComponent instanceof Collection) || !((Collection) favoritesForComponent).isEmpty()) {
            for (StructureInfo controls : favoritesForComponent) {
                Iterable controls2 = controls.getControls();
                if (!(controls2 instanceof Collection) || !((Collection) controls2).isEmpty()) {
                    Iterator it = controls2.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String controlId = ((ControlInfo) it.next()).getControlId();
                        Control control2 = this.control;
                        if (control2 == null) {
                            control2 = null;
                        }
                        if (Intrinsics.areEqual((Object) controlId, (Object) control2.getControlId())) {
                            z = true;
                            continue;
                            break;
                        }
                    }
                }
                z = false;
                continue;
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public final Dialog createDialog(@NotNull CharSequence charSequence) {
        RenderInfo.Companion companion = RenderInfo.Companion;
        ComponentName componentName = this.controlComponent;
        Control control2 = null;
        ComponentName componentName2 = componentName == null ? null : componentName;
        Control control3 = this.control;
        if (control3 == null) {
            control3 = null;
        }
        RenderInfo lookup$default = RenderInfo.Companion.lookup$default(companion, this, componentName2, control3.getDeviceType(), 0, 8, (Object) null);
        View inflate = LayoutInflater.from(this).inflate(R$layout.controls_dialog, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.requireViewById(R$id.icon);
        imageView.setImageDrawable(lookup$default.getIcon());
        imageView.setImageTintList(imageView.getContext().getResources().getColorStateList(lookup$default.getForeground(), imageView.getContext().getTheme()));
        TextView textView = (TextView) inflate.requireViewById(R$id.title);
        Control control4 = this.control;
        if (control4 == null) {
            control4 = null;
        }
        textView.setText(control4.getTitle());
        TextView textView2 = (TextView) inflate.requireViewById(R$id.subtitle);
        Control control5 = this.control;
        if (control5 != null) {
            control2 = control5;
        }
        textView2.setText(control2.getSubtitle());
        inflate.requireViewById(R$id.control).setElevation(inflate.getResources().getFloat(R$dimen.control_card_elevation));
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R$string.controls_dialog_title)).setMessage(getString(R$string.controls_dialog_message, new Object[]{charSequence})).setPositiveButton(R$string.controls_dialog_ok, this).setNegativeButton(17039360, this).setOnCancelListener(this).setView(inflate).create();
        SystemUIDialog.registerDismissListener(create);
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onCancel(@Nullable DialogInterface dialogInterface) {
        finish();
    }

    public void onClick(@Nullable DialogInterface dialogInterface, int i) {
        if (i == -1) {
            ControlsController controlsController = this.controller;
            ComponentName componentName = this.controlComponent;
            Control control2 = null;
            if (componentName == null) {
                componentName = null;
            }
            Control control3 = this.control;
            if (control3 == null) {
                control3 = null;
            }
            CharSequence structure = control3.getStructure();
            if (structure == null) {
                structure = "";
            }
            Control control4 = this.control;
            if (control4 == null) {
                control4 = null;
            }
            String controlId = control4.getControlId();
            Control control5 = this.control;
            if (control5 == null) {
                control5 = null;
            }
            CharSequence title = control5.getTitle();
            Control control6 = this.control;
            if (control6 == null) {
                control6 = null;
            }
            CharSequence subtitle = control6.getSubtitle();
            Control control7 = this.control;
            if (control7 != null) {
                control2 = control7;
            }
            controlsController.addFavorite(componentName, structure, new ControlInfo(controlId, title, subtitle, control2.getDeviceType()));
        }
        finish();
    }
}
