package com.google.android.setupdesign.template;

import android.os.Handler;
import android.os.Looper;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;

public class RequireScrollMixin implements Mixin {
    public ScrollHandlingDelegate delegate;
    public boolean everScrolledToBottom = false;
    public final Handler handler = new Handler(Looper.getMainLooper());
    public boolean requiringScrollToBottom = false;

    public interface OnRequireScrollStateChangedListener {
        void onRequireScrollStateChanged(boolean z);
    }

    public interface ScrollHandlingDelegate {
    }

    public RequireScrollMixin(TemplateLayout templateLayout) {
    }

    public void setScrollHandlingDelegate(ScrollHandlingDelegate scrollHandlingDelegate) {
        this.delegate = scrollHandlingDelegate;
    }

    public void notifyScrollabilityChange(boolean z) {
        if (z != this.requiringScrollToBottom) {
            if (!z) {
                postScrollStateChange(false);
                this.requiringScrollToBottom = false;
                this.everScrolledToBottom = true;
            } else if (!this.everScrolledToBottom) {
                postScrollStateChange(true);
                this.requiringScrollToBottom = true;
            }
        }
    }

    public final void postScrollStateChange(final boolean z) {
        this.handler.post(new Runnable() {
            public void run() {
                if (RequireScrollMixin.this.getClass() != null) {
                    RequireScrollMixin.this.getClass().onRequireScrollStateChanged(z);
                }
            }
        });
    }
}
