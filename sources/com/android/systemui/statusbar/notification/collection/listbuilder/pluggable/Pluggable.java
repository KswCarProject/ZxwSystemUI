package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import android.os.Trace;

public abstract class Pluggable<This> {
    public PluggableListener<This> mListener;
    public final String mName;

    public interface PluggableListener<T> {
        void onPluggableInvalidated(T t);
    }

    public void onCleanup() {
    }

    public Pluggable(String str) {
        this.mName = str;
    }

    public final String getName() {
        return this.mName;
    }

    public final void invalidateList() {
        if (this.mListener != null) {
            Trace.beginSection("Pluggable<" + this.mName + ">.invalidateList");
            this.mListener.onPluggableInvalidated(this);
            Trace.endSection();
        }
    }

    public final void setInvalidationListener(PluggableListener<This> pluggableListener) {
        this.mListener = pluggableListener;
    }
}
