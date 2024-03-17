package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RecyclerMixin;
import com.google.android.setupdesign.template.RecyclerViewScrollHandlingDelegate;
import com.google.android.setupdesign.template.RequireScrollMixin;

public class SetupWizardRecyclerLayout extends SetupWizardLayout {
    public RecyclerMixin recyclerMixin;

    public SetupWizardRecyclerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    private void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            this.recyclerMixin.parseAttributes(attributeSet, i);
            registerMixin(RecyclerMixin.class, this.recyclerMixin);
            RequireScrollMixin requireScrollMixin = (RequireScrollMixin) getMixin(RequireScrollMixin.class);
            requireScrollMixin.setScrollHandlingDelegate(new RecyclerViewScrollHandlingDelegate(requireScrollMixin, getRecyclerView()));
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.recyclerMixin.onLayout();
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerMixin.getRecyclerView();
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = R$id.sud_recycler_view;
        }
        return super.findContainer(i);
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.sud_recycler_template;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    public void onTemplateInflated() {
        View findViewById = findViewById(R$id.sud_recycler_view);
        if (findViewById instanceof RecyclerView) {
            this.recyclerMixin = new RecyclerMixin(this, (RecyclerView) findViewById);
            return;
        }
        throw new IllegalStateException("SetupWizardRecyclerLayout should use a template with recycler view");
    }

    public <T extends View> T findManagedViewById(int i) {
        T findViewById;
        View header = this.recyclerMixin.getHeader();
        if (header == null || (findViewById = header.findViewById(i)) == null) {
            return super.findViewById(i);
        }
        return findViewById;
    }
}
