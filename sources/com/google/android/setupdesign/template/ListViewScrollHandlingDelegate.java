package com.google.android.setupdesign.template;

import android.widget.AbsListView;
import android.widget.ListView;
import com.google.android.setupdesign.template.RequireScrollMixin;

public class ListViewScrollHandlingDelegate implements RequireScrollMixin.ScrollHandlingDelegate, AbsListView.OnScrollListener {
    public final ListView listView;
    public final RequireScrollMixin requireScrollMixin;

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public ListViewScrollHandlingDelegate(RequireScrollMixin requireScrollMixin2, ListView listView2) {
        this.requireScrollMixin = requireScrollMixin2;
        this.listView = listView2;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i + i2 >= i3) {
            this.requireScrollMixin.notifyScrollabilityChange(false);
        } else {
            this.requireScrollMixin.notifyScrollabilityChange(true);
        }
    }
}
