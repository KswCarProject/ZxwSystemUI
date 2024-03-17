package com.android.settingslib.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

@Deprecated
public abstract class AsyncLoader<T> extends AsyncTaskLoader<T> {
    public T mResult;

    public abstract void onDiscardResult(T t);

    public AsyncLoader(Context context) {
        super(context);
    }

    public void onStartLoading() {
        T t = this.mResult;
        if (t != null) {
            deliverResult(t);
        }
        if (takeContentChanged() || this.mResult == null) {
            forceLoad();
        }
    }

    public void onStopLoading() {
        cancelLoad();
    }

    public void deliverResult(T t) {
        if (!isReset()) {
            T t2 = this.mResult;
            this.mResult = t;
            if (isStarted()) {
                super.deliverResult(t);
            }
            if (t2 != null && t2 != this.mResult) {
                onDiscardResult(t2);
            }
        } else if (t != null) {
            onDiscardResult(t);
        }
    }

    public void onReset() {
        super.onReset();
        onStopLoading();
        T t = this.mResult;
        if (t != null) {
            onDiscardResult(t);
        }
        this.mResult = null;
    }

    public void onCanceled(T t) {
        super.onCanceled(t);
        if (t != null) {
            onDiscardResult(t);
        }
    }
}
