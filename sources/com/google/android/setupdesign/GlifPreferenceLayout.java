package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RecyclerMixin;

public class GlifPreferenceLayout extends GlifRecyclerLayout {
    public GlifPreferenceLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = R$id.sud_layout_content;
        }
        return super.findContainer(i);
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.sud_glif_preference_template;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    public void onTemplateInflated() {
        this.recyclerMixin = new RecyclerMixin(this, (RecyclerView) LayoutInflater.from(getContext()).inflate(R$layout.sud_glif_preference_recycler_view, this, false));
    }
}
