package com.android.systemui.dreams;

import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DreamOverlayStateController implements CallbackController<Callback> {
    public static final boolean DEBUG = Log.isLoggable("DreamOverlayStateCtlr", 3);
    public int mAvailableComplicationTypes = 0;
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public final Collection<Complication> mComplications = new HashSet();
    public final Executor mExecutor;
    public boolean mShouldShowComplications = false;
    public int mState;

    public interface Callback {
        void onAvailableComplicationTypesChanged() {
        }

        void onComplicationsChanged() {
        }

        void onStateChanged() {
        }
    }

    @VisibleForTesting
    public DreamOverlayStateController(Executor executor) {
        this.mExecutor = executor;
    }

    public void addComplication(Complication complication) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda8(this, complication));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addComplication$1(Complication complication) {
        if (this.mComplications.add(complication)) {
            this.mCallbacks.stream().forEach(new DreamOverlayStateController$$ExternalSyntheticLambda9());
        }
    }

    public Collection<Complication> getComplications() {
        return getComplications(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getComplications$4(Complication complication) {
        int requiredTypeAvailability = complication.getRequiredTypeAvailability();
        if (this.mShouldShowComplications) {
            if ((getAvailableComplicationTypes() & requiredTypeAvailability) == requiredTypeAvailability) {
                return true;
            }
            return false;
        } else if (requiredTypeAvailability == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Collection<Complication> getComplications(boolean z) {
        Collection<Complication> collection;
        if (z) {
            collection = (Collection) this.mComplications.stream().filter(new DreamOverlayStateController$$ExternalSyntheticLambda1(this)).collect(Collectors.toCollection(new DreamOverlayStateController$$ExternalSyntheticLambda2()));
        } else {
            collection = this.mComplications;
        }
        return Collections.unmodifiableCollection(collection);
    }

    public final void notifyCallbacks(Consumer<Callback> consumer) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda6(this, consumer));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyCallbacks$5(Consumer consumer) {
        Iterator<Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    public void addCallback(Callback callback) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda3(this, callback));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addCallback$6(Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
            if (!this.mComplications.isEmpty()) {
                callback.onComplicationsChanged();
            }
        }
    }

    public void removeCallback(Callback callback) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda0(this, callback));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeCallback$7(Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        this.mCallbacks.remove(callback);
    }

    public boolean isOverlayActive() {
        return containsState(1);
    }

    public final boolean containsState(int i) {
        return (this.mState & i) != 0;
    }

    public final void modifyState(int i, int i2) {
        int i3 = this.mState;
        if (i == 1) {
            this.mState = (~i2) & i3;
        } else if (i == 2) {
            this.mState = i3 | i2;
        }
        if (i3 != this.mState) {
            notifyCallbacks(new DreamOverlayStateController$$ExternalSyntheticLambda5());
        }
    }

    public void setOverlayActive(boolean z) {
        modifyState(z ? 2 : 1, 1);
    }

    public int getAvailableComplicationTypes() {
        return this.mAvailableComplicationTypes;
    }

    public void setAvailableComplicationTypes(int i) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda10(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAvailableComplicationTypes$9(int i) {
        this.mAvailableComplicationTypes = i;
        this.mCallbacks.forEach(new DreamOverlayStateController$$ExternalSyntheticLambda7());
    }

    public void setShouldShowComplications(boolean z) {
        this.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda4(this, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setShouldShowComplications$10(boolean z) {
        this.mShouldShowComplications = z;
        this.mCallbacks.forEach(new DreamOverlayStateController$$ExternalSyntheticLambda7());
    }
}
