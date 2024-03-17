package androidx.activity;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback {
    public CopyOnWriteArrayList<Cancellable> mCancellables = new CopyOnWriteArrayList<>();
    public boolean mEnabled;

    public abstract void handleOnBackPressed();

    public OnBackPressedCallback(boolean z) {
        this.mEnabled = z;
    }

    public final void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public final boolean isEnabled() {
        return this.mEnabled;
    }

    public final void remove() {
        Iterator<Cancellable> it = this.mCancellables.iterator();
        while (it.hasNext()) {
            it.next().cancel();
        }
    }

    public void addCancellable(Cancellable cancellable) {
        this.mCancellables.add(cancellable);
    }

    public void removeCancellable(Cancellable cancellable) {
        this.mCancellables.remove(cancellable);
    }
}
