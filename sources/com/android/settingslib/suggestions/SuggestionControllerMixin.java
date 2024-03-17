package com.android.settingslib.suggestions;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.service.settings.suggestions.Suggestion;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.List;

@Deprecated
public class SuggestionControllerMixin implements LifecycleObserver, LoaderManager.LoaderCallbacks<List<Suggestion>> {
    public final Context mContext;
    public boolean mSuggestionLoaded;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        throw null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        throw null;
    }

    public Loader<List<Suggestion>> onCreateLoader(int i, Bundle bundle) {
        if (i == 42) {
            this.mSuggestionLoaded = false;
            return new SuggestionLoader(this.mContext, (SuggestionController) null);
        }
        throw new IllegalArgumentException("This loader id is not supported " + i);
    }

    public void onLoadFinished(Loader<List<Suggestion>> loader, List<Suggestion> list) {
        this.mSuggestionLoaded = true;
        throw null;
    }

    public void onLoaderReset(Loader<List<Suggestion>> loader) {
        this.mSuggestionLoaded = false;
    }
}
