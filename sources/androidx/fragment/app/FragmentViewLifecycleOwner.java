package androidx.fragment.app;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;

public class FragmentViewLifecycleOwner implements SavedStateRegistryOwner, ViewModelStoreOwner {
    public final Fragment mFragment;
    public LifecycleRegistry mLifecycleRegistry = null;
    public SavedStateRegistryController mSavedStateRegistryController = null;
    public final ViewModelStore mViewModelStore;

    public FragmentViewLifecycleOwner(Fragment fragment, ViewModelStore viewModelStore) {
        this.mFragment = fragment;
        this.mViewModelStore = viewModelStore;
    }

    public ViewModelStore getViewModelStore() {
        initialize();
        return this.mViewModelStore;
    }

    public void initialize() {
        if (this.mLifecycleRegistry == null) {
            this.mLifecycleRegistry = new LifecycleRegistry(this);
            this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
        }
    }

    public boolean isInitialized() {
        return this.mLifecycleRegistry != null;
    }

    public Lifecycle getLifecycle() {
        initialize();
        return this.mLifecycleRegistry;
    }

    public void setCurrentState(Lifecycle.State state) {
        this.mLifecycleRegistry.setCurrentState(state);
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        this.mLifecycleRegistry.handleLifecycleEvent(event);
    }

    public SavedStateRegistry getSavedStateRegistry() {
        initialize();
        return this.mSavedStateRegistryController.getSavedStateRegistry();
    }

    public void performRestore(Bundle bundle) {
        this.mSavedStateRegistryController.performRestore(bundle);
    }

    public void performSave(Bundle bundle) {
        this.mSavedStateRegistryController.performSave(bundle);
    }
}
