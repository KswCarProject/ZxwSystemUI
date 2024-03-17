package com.google.android.setupdesign.template;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RequireScrollMixin;

public class RecyclerViewScrollHandlingDelegate implements RequireScrollMixin.ScrollHandlingDelegate {
    public final RecyclerView recyclerView;
    public final RequireScrollMixin requireScrollMixin;

    public RecyclerViewScrollHandlingDelegate(RequireScrollMixin requireScrollMixin2, RecyclerView recyclerView2) {
        this.requireScrollMixin = requireScrollMixin2;
        this.recyclerView = recyclerView2;
    }
}
