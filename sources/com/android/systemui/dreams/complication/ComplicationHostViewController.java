package com.android.systemui.dreams.complication;

import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.util.ViewController;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ComplicationHostViewController extends ViewController<ConstraintLayout> {
    public final ComplicationCollectionViewModel mComplicationCollectionViewModel;
    public final HashMap<ComplicationId, Complication.ViewHolder> mComplications = new HashMap<>();
    public final ComplicationLayoutEngine mLayoutEngine;
    public final LifecycleOwner mLifecycleOwner;

    public void onViewAttached() {
    }

    public void onViewDetached() {
    }

    public ComplicationHostViewController(ConstraintLayout constraintLayout, ComplicationLayoutEngine complicationLayoutEngine, LifecycleOwner lifecycleOwner, ComplicationCollectionViewModel complicationCollectionViewModel) {
        super(constraintLayout);
        this.mLayoutEngine = complicationLayoutEngine;
        this.mLifecycleOwner = lifecycleOwner;
        this.mComplicationCollectionViewModel = complicationCollectionViewModel;
    }

    public void onInit() {
        super.onInit();
        this.mComplicationCollectionViewModel.getComplications().observe(this.mLifecycleOwner, new ComplicationHostViewController$$ExternalSyntheticLambda0(this));
    }

    /* renamed from: updateComplications */
    public final void lambda$onInit$0(Collection<ComplicationViewModel> collection) {
        ((Collection) this.mComplications.keySet().stream().filter(new ComplicationHostViewController$$ExternalSyntheticLambda2((Collection) collection.stream().map(new ComplicationHostViewController$$ExternalSyntheticLambda1()).collect(Collectors.toSet()))).collect(Collectors.toSet())).forEach(new ComplicationHostViewController$$ExternalSyntheticLambda3(this));
        ((Collection) collection.stream().filter(new ComplicationHostViewController$$ExternalSyntheticLambda4(this)).collect(Collectors.toSet())).forEach(new ComplicationHostViewController$$ExternalSyntheticLambda5(this));
    }

    public static /* synthetic */ boolean lambda$updateComplications$2(Collection collection, ComplicationId complicationId) {
        return !collection.contains(complicationId);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateComplications$3(ComplicationId complicationId) {
        this.mLayoutEngine.removeComplication(complicationId);
        this.mComplications.remove(complicationId);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateComplications$4(ComplicationViewModel complicationViewModel) {
        return !this.mComplications.containsKey(complicationViewModel.getId());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateComplications$5(ComplicationViewModel complicationViewModel) {
        ComplicationId id = complicationViewModel.getId();
        Complication.ViewHolder createView = complicationViewModel.getComplication().createView(complicationViewModel);
        this.mComplications.put(id, createView);
        if (createView.getView().getParent() != null) {
            Log.e("ComplicationHostViewController", "View for complication " + complicationViewModel.getComplication().getClass() + " already has a parent. Make sure not to reuse complication views!");
        }
        ComplicationLayoutEngine complicationLayoutEngine = this.mLayoutEngine;
        View view = createView.getView();
        createView.getLayoutParams();
        complicationLayoutEngine.addComplication(id, view, (ComplicationLayoutParams) null, createView.getCategory());
    }

    public View getView() {
        return this.mView;
    }

    public List<View> getViewsAtPosition(int i) {
        return this.mLayoutEngine.getViewsAtPosition(i);
    }
}
