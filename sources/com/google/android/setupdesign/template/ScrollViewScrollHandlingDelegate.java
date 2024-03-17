package com.google.android.setupdesign.template;

import android.util.Log;
import android.widget.ScrollView;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.view.BottomScrollView;

public class ScrollViewScrollHandlingDelegate implements RequireScrollMixin.ScrollHandlingDelegate, BottomScrollView.BottomScrollListener {
    public final RequireScrollMixin requireScrollMixin;
    public final BottomScrollView scrollView;

    public ScrollViewScrollHandlingDelegate(RequireScrollMixin requireScrollMixin2, ScrollView scrollView2) {
        this.requireScrollMixin = requireScrollMixin2;
        if (scrollView2 instanceof BottomScrollView) {
            this.scrollView = (BottomScrollView) scrollView2;
            return;
        }
        Log.w("ScrollViewDelegate", "Cannot set non-BottomScrollView. Found=" + scrollView2);
        this.scrollView = null;
    }

    public void onScrolledToBottom() {
        this.requireScrollMixin.notifyScrollabilityChange(false);
    }

    public void onRequiresScroll() {
        this.requireScrollMixin.notifyScrollabilityChange(true);
    }
}
