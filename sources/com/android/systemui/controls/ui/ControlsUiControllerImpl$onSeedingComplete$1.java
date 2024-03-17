package com.android.systemui.controls.ui;

import android.view.ViewGroup;
import com.android.systemui.controls.controller.StructureInfo;
import java.util.Iterator;
import java.util.function.Consumer;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$onSeedingComplete$1<T> implements Consumer {
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$onSeedingComplete$1(ControlsUiControllerImpl controlsUiControllerImpl) {
        this.this$0 = controlsUiControllerImpl;
    }

    public final void accept(Boolean bool) {
        Object obj;
        ViewGroup viewGroup = null;
        if (bool.booleanValue()) {
            ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
            Iterator it = controlsUiControllerImpl.getControlsController().get().getFavorites().iterator();
            if (!it.hasNext()) {
                obj = null;
            } else {
                obj = it.next();
                if (it.hasNext()) {
                    int size = ((StructureInfo) obj).getControls().size();
                    do {
                        Object next = it.next();
                        int size2 = ((StructureInfo) next).getControls().size();
                        if (size < size2) {
                            obj = next;
                            size = size2;
                        }
                    } while (it.hasNext());
                }
            }
            StructureInfo structureInfo = (StructureInfo) obj;
            if (structureInfo == null) {
                structureInfo = ControlsUiControllerImpl.EMPTY_STRUCTURE;
            }
            controlsUiControllerImpl.selectedStructure = structureInfo;
            ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
            controlsUiControllerImpl2.updatePreferences(controlsUiControllerImpl2.selectedStructure);
        }
        ControlsUiControllerImpl controlsUiControllerImpl3 = this.this$0;
        ViewGroup access$getParent$p = controlsUiControllerImpl3.parent;
        if (access$getParent$p != null) {
            viewGroup = access$getParent$p;
        }
        controlsUiControllerImpl3.reload(viewGroup);
    }
}
