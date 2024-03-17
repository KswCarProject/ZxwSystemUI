package com.android.systemui.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.core.lifecycle.Lifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LifecycleActivity.kt */
public class LifecycleActivity extends Activity implements LifecycleOwner {
    @NotNull
    public final Lifecycle lifecycle = new Lifecycle(this);

    @NotNull
    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    public void onCreate(@Nullable Bundle bundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle);
    }

    public void onCreate(@Nullable Bundle bundle, @Nullable PersistableBundle persistableBundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle, persistableBundle);
    }

    public void onStart() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    public void onResume() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    public void onPause() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    public void onStop() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    public void onDestroy() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}
