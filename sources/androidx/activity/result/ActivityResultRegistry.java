package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public abstract class ActivityResultRegistry {
    public final transient Map<String, CallbackAndContract<?>> mKeyToCallback = new HashMap();
    public final Map<String, LifecycleContainer> mKeyToLifecycleContainers = new HashMap();
    public final Map<String, Integer> mKeyToRc = new HashMap();
    public ArrayList<String> mLaunchedKeys = new ArrayList<>();
    public final Map<String, Object> mParsedPendingResults = new HashMap();
    public final Bundle mPendingResults = new Bundle();
    public Random mRandom = new Random();
    public final Map<Integer, String> mRcToKey = new HashMap();

    /* renamed from: androidx.activity.result.ActivityResultRegistry$1  reason: invalid class name */
    class AnonymousClass1 implements LifecycleEventObserver {
        public final /* synthetic */ ActivityResultRegistry this$0;
        public final /* synthetic */ ActivityResultCallback val$callback;
        public final /* synthetic */ ActivityResultContract val$contract;
        public final /* synthetic */ String val$key;

        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (Lifecycle.Event.ON_START.equals(event)) {
                this.this$0.mKeyToCallback.put(this.val$key, new CallbackAndContract(this.val$callback, this.val$contract));
                if (this.this$0.mParsedPendingResults.containsKey(this.val$key)) {
                    Object obj = this.this$0.mParsedPendingResults.get(this.val$key);
                    this.this$0.mParsedPendingResults.remove(this.val$key);
                    this.val$callback.onActivityResult(obj);
                }
                ActivityResult activityResult = (ActivityResult) this.this$0.mPendingResults.getParcelable(this.val$key);
                if (activityResult != null) {
                    this.this$0.mPendingResults.remove(this.val$key);
                    this.val$callback.onActivityResult(this.val$contract.parseResult(activityResult.getResultCode(), activityResult.getData()));
                }
            } else if (Lifecycle.Event.ON_STOP.equals(event)) {
                this.this$0.mKeyToCallback.remove(this.val$key);
            } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                this.this$0.unregister(this.val$key);
            }
        }
    }

    public final <I, O> ActivityResultLauncher<I> register(final String str, final ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        registerKey(str);
        this.mKeyToCallback.put(str, new CallbackAndContract(activityResultCallback, activityResultContract));
        if (this.mParsedPendingResults.containsKey(str)) {
            Object obj = this.mParsedPendingResults.get(str);
            this.mParsedPendingResults.remove(str);
            activityResultCallback.onActivityResult(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.mPendingResults.getParcelable(str);
        if (activityResult != null) {
            this.mPendingResults.remove(str);
            activityResultCallback.onActivityResult(activityResultContract.parseResult(activityResult.getResultCode(), activityResult.getData()));
        }
        return new ActivityResultLauncher<I>() {
            public void unregister() {
                ActivityResultRegistry.this.unregister(str);
            }
        };
    }

    public final void unregister(String str) {
        Integer remove;
        if (!this.mLaunchedKeys.contains(str) && (remove = this.mKeyToRc.remove(str)) != null) {
            this.mRcToKey.remove(remove);
        }
        this.mKeyToCallback.remove(str);
        if (this.mParsedPendingResults.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.mParsedPendingResults.get(str));
            this.mParsedPendingResults.remove(str);
        }
        if (this.mPendingResults.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.mPendingResults.getParcelable(str));
            this.mPendingResults.remove(str);
        }
        LifecycleContainer lifecycleContainer = this.mKeyToLifecycleContainers.get(str);
        if (lifecycleContainer != null) {
            lifecycleContainer.clearObservers();
            this.mKeyToLifecycleContainers.remove(str);
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        bundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList(this.mKeyToRc.values()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList(this.mKeyToRc.keySet()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList(this.mLaunchedKeys));
        bundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle) this.mPendingResults.clone());
        bundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", this.mRandom);
    }

    public final void onRestoreInstanceState(Bundle bundle) {
        if (bundle != null) {
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
            ArrayList<String> stringArrayList = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
            if (stringArrayList != null && integerArrayList != null) {
                this.mLaunchedKeys = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
                this.mRandom = (Random) bundle.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
                this.mPendingResults.putAll(bundle.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
                for (int i = 0; i < stringArrayList.size(); i++) {
                    String str = stringArrayList.get(i);
                    if (this.mKeyToRc.containsKey(str)) {
                        Integer remove = this.mKeyToRc.remove(str);
                        if (!this.mPendingResults.containsKey(str)) {
                            this.mRcToKey.remove(remove);
                        }
                    }
                    bindRcKey(integerArrayList.get(i).intValue(), stringArrayList.get(i));
                }
            }
        }
    }

    public final boolean dispatchResult(int i, int i2, Intent intent) {
        String str = this.mRcToKey.get(Integer.valueOf(i));
        if (str == null) {
            return false;
        }
        this.mLaunchedKeys.remove(str);
        doDispatch(str, i2, intent, this.mKeyToCallback.get(str));
        return true;
    }

    public final <O> void doDispatch(String str, int i, Intent intent, CallbackAndContract<O> callbackAndContract) {
        ActivityResultCallback<O> activityResultCallback;
        if (callbackAndContract == null || (activityResultCallback = callbackAndContract.mCallback) == null) {
            this.mParsedPendingResults.remove(str);
            this.mPendingResults.putParcelable(str, new ActivityResult(i, intent));
            return;
        }
        activityResultCallback.onActivityResult(callbackAndContract.mContract.parseResult(i, intent));
    }

    public final void registerKey(String str) {
        if (this.mKeyToRc.get(str) == null) {
            bindRcKey(generateRandomNumber(), str);
        }
    }

    public final int generateRandomNumber() {
        int nextInt = this.mRandom.nextInt(2147418112);
        while (true) {
            int i = nextInt + 65536;
            if (!this.mRcToKey.containsKey(Integer.valueOf(i))) {
                return i;
            }
            nextInt = this.mRandom.nextInt(2147418112);
        }
    }

    public final void bindRcKey(int i, String str) {
        this.mRcToKey.put(Integer.valueOf(i), str);
        this.mKeyToRc.put(str, Integer.valueOf(i));
    }

    public static class CallbackAndContract<O> {
        public final ActivityResultCallback<O> mCallback;
        public final ActivityResultContract<?, O> mContract;

        public CallbackAndContract(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.mCallback = activityResultCallback;
            this.mContract = activityResultContract;
        }
    }

    public static class LifecycleContainer {
        public final Lifecycle mLifecycle;
        public final ArrayList<LifecycleEventObserver> mObservers;

        public void clearObservers() {
            Iterator<LifecycleEventObserver> it = this.mObservers.iterator();
            while (it.hasNext()) {
                this.mLifecycle.removeObserver(it.next());
            }
            this.mObservers.clear();
        }
    }
}
