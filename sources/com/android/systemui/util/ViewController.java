package com.android.systemui.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public abstract class ViewController<T extends View> {
    public boolean mInited;
    public View.OnAttachStateChangeListener mOnAttachStateListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View view) {
            ViewController.this.onViewAttached();
        }

        public void onViewDetachedFromWindow(View view) {
            ViewController.this.onViewDetached();
        }
    };
    public final T mView;

    public void onInit() {
    }

    public abstract void onViewAttached();

    public abstract void onViewDetached();

    public ViewController(T t) {
        this.mView = t;
    }

    public void init() {
        if (!this.mInited) {
            onInit();
            this.mInited = true;
            if (isAttachedToWindow()) {
                this.mOnAttachStateListener.onViewAttachedToWindow(this.mView);
            }
            addOnAttachStateChangeListener(this.mOnAttachStateListener);
        }
    }

    public Context getContext() {
        return this.mView.getContext();
    }

    public Resources getResources() {
        return this.mView.getResources();
    }

    public boolean isAttachedToWindow() {
        T t = this.mView;
        return t != null && t.isAttachedToWindow();
    }

    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        T t = this.mView;
        if (t != null) {
            t.addOnAttachStateChangeListener(onAttachStateChangeListener);
        }
    }
}
