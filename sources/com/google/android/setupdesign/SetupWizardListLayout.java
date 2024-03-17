package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.setupdesign.template.ListMixin;
import com.google.android.setupdesign.template.ListViewScrollHandlingDelegate;
import com.google.android.setupdesign.template.RequireScrollMixin;

public class SetupWizardListLayout extends SetupWizardLayout {
    public ListMixin listMixin;

    public SetupWizardListLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    private void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            ListMixin listMixin2 = new ListMixin(this, attributeSet, i);
            this.listMixin = listMixin2;
            registerMixin(ListMixin.class, listMixin2);
            RequireScrollMixin requireScrollMixin = (RequireScrollMixin) getMixin(RequireScrollMixin.class);
            requireScrollMixin.setScrollHandlingDelegate(new ListViewScrollHandlingDelegate(requireScrollMixin, getListView()));
        }
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.sud_list_template;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 16908298;
        }
        return super.findContainer(i);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.listMixin.onLayout();
    }

    public ListView getListView() {
        return this.listMixin.getListView();
    }
}
