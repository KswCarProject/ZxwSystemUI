package androidx.slice;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Pair;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.SliceLiveData;
import java.util.concurrent.Executor;

public abstract class SliceViewManagerBase extends SliceViewManager {
    public final Context mContext;
    public final ArrayMap<Pair<Uri, SliceViewManager.SliceCallback>, SliceListenerImpl> mListenerLookup = new ArrayMap<>();

    public SliceViewManagerBase(Context context) {
        this.mContext = context;
    }

    public void registerSliceCallback(Uri uri, SliceViewManager.SliceCallback sliceCallback) {
        final Handler handler = new Handler(Looper.getMainLooper());
        registerSliceCallback(uri, new Executor() {
            public void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, sliceCallback);
    }

    public void registerSliceCallback(Uri uri, Executor executor, SliceViewManager.SliceCallback sliceCallback) {
        getListener(uri, sliceCallback, new SliceListenerImpl(uri, executor, sliceCallback)).startListening();
    }

    public void unregisterSliceCallback(Uri uri, SliceViewManager.SliceCallback sliceCallback) {
        synchronized (this.mListenerLookup) {
            SliceListenerImpl remove = this.mListenerLookup.remove(new Pair(uri, sliceCallback));
            if (remove != null) {
                remove.stopListening();
            }
        }
    }

    public final SliceListenerImpl getListener(Uri uri, SliceViewManager.SliceCallback sliceCallback, SliceListenerImpl sliceListenerImpl) {
        Pair pair = new Pair(uri, sliceCallback);
        synchronized (this.mListenerLookup) {
            SliceListenerImpl put = this.mListenerLookup.put(pair, sliceListenerImpl);
            if (put != null) {
                put.stopListening();
            }
        }
        return sliceListenerImpl;
    }

    public class SliceListenerImpl {
        public final SliceViewManager.SliceCallback mCallback;
        public final Executor mExecutor;
        public final ContentObserver mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                AsyncTask.execute(SliceListenerImpl.this.mUpdateSlice);
            }
        };
        public boolean mPinned;
        public final Runnable mUpdateSlice = new Runnable() {
            public void run() {
                SliceListenerImpl.this.tryPin();
                SliceListenerImpl sliceListenerImpl = SliceListenerImpl.this;
                final Slice bindSlice = Slice.bindSlice(SliceViewManagerBase.this.mContext, sliceListenerImpl.mUri, SliceLiveData.SUPPORTED_SPECS);
                SliceListenerImpl.this.mExecutor.execute(new Runnable() {
                    public void run() {
                        SliceListenerImpl.this.mCallback.onSliceUpdated(bindSlice);
                    }
                });
            }
        };
        public Uri mUri;

        public SliceListenerImpl(Uri uri, Executor executor, SliceViewManager.SliceCallback sliceCallback) {
            this.mUri = uri;
            this.mExecutor = executor;
            this.mCallback = sliceCallback;
        }

        public void startListening() {
            ContentProviderClient acquireContentProviderClient = SliceViewManagerBase.this.mContext.getContentResolver().acquireContentProviderClient(this.mUri);
            if (acquireContentProviderClient != null) {
                acquireContentProviderClient.release();
                SliceViewManagerBase.this.mContext.getContentResolver().registerContentObserver(this.mUri, true, this.mObserver);
                tryPin();
            }
        }

        public void tryPin() {
            if (!this.mPinned) {
                try {
                    SliceViewManagerBase.this.pinSlice(this.mUri);
                    this.mPinned = true;
                } catch (SecurityException unused) {
                }
            }
        }

        public void stopListening() {
            SliceViewManagerBase.this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
            if (this.mPinned) {
                SliceViewManagerBase.this.unpinSlice(this.mUri);
                this.mPinned = false;
            }
        }
    }
}
