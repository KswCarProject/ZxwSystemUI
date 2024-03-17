package androidx.lifecycle;

public interface DefaultLifecycleObserver extends FullLifecycleObserver {
    void onCreate(LifecycleOwner lifecycleOwner) {
    }

    void onDestroy(LifecycleOwner lifecycleOwner) {
    }

    void onStart(LifecycleOwner lifecycleOwner) {
    }

    void onStop(LifecycleOwner lifecycleOwner) {
    }
}
