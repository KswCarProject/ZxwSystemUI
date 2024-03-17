package androidx.mediarouter.media;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.util.ObjectsCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

public abstract class MediaRouteProvider {
    public Callback mCallback;
    public final Context mContext;
    public MediaRouteProviderDescriptor mDescriptor;
    public MediaRouteDiscoveryRequest mDiscoveryRequest;
    public final ProviderHandler mHandler;
    public final ProviderMetadata mMetadata;
    public boolean mPendingDescriptorChange;
    public boolean mPendingDiscoveryRequestChange;

    public static abstract class Callback {
        public abstract void onDescriptorChanged(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor);
    }

    public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
    }

    public MediaRouteProvider(Context context) {
        this(context, (ProviderMetadata) null);
    }

    public MediaRouteProvider(Context context, ProviderMetadata providerMetadata) {
        this.mHandler = new ProviderHandler();
        if (context != null) {
            this.mContext = context;
            if (providerMetadata == null) {
                this.mMetadata = new ProviderMetadata(new ComponentName(context, getClass()));
            } else {
                this.mMetadata = providerMetadata;
            }
        } else {
            throw new IllegalArgumentException("context must not be null");
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final ProviderMetadata getMetadata() {
        return this.mMetadata;
    }

    public final void setCallback(Callback callback) {
        MediaRouter.checkCallingThread();
        this.mCallback = callback;
    }

    public final MediaRouteDiscoveryRequest getDiscoveryRequest() {
        return this.mDiscoveryRequest;
    }

    public final void setDiscoveryRequest(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        MediaRouter.checkCallingThread();
        if (!ObjectsCompat.equals(this.mDiscoveryRequest, mediaRouteDiscoveryRequest)) {
            setDiscoveryRequestInternal(mediaRouteDiscoveryRequest);
        }
    }

    public final void setDiscoveryRequestInternal(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        this.mDiscoveryRequest = mediaRouteDiscoveryRequest;
        if (!this.mPendingDiscoveryRequestChange) {
            this.mPendingDiscoveryRequestChange = true;
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public void deliverDiscoveryRequestChanged() {
        this.mPendingDiscoveryRequestChange = false;
        onDiscoveryRequestChanged(this.mDiscoveryRequest);
    }

    public final MediaRouteProviderDescriptor getDescriptor() {
        return this.mDescriptor;
    }

    public final void setDescriptor(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
        MediaRouter.checkCallingThread();
        if (this.mDescriptor != mediaRouteProviderDescriptor) {
            this.mDescriptor = mediaRouteProviderDescriptor;
            if (!this.mPendingDescriptorChange) {
                this.mPendingDescriptorChange = true;
                this.mHandler.sendEmptyMessage(1);
            }
        }
    }

    public void deliverDescriptorChanged() {
        this.mPendingDescriptorChange = false;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDescriptorChanged(this, this.mDescriptor);
        }
    }

    public RouteController onCreateRouteController(String str) {
        if (str != null) {
            return null;
        }
        throw new IllegalArgumentException("routeId cannot be null");
    }

    public RouteController onCreateRouteController(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("routeId cannot be null");
        } else if (str2 != null) {
            return onCreateRouteController(str);
        } else {
            throw new IllegalArgumentException("routeGroupId cannot be null");
        }
    }

    public DynamicGroupRouteController onCreateDynamicGroupRouteController(String str) {
        if (str != null) {
            return null;
        }
        throw new IllegalArgumentException("initialMemberRouteId cannot be null.");
    }

    public static final class ProviderMetadata {
        public final ComponentName mComponentName;

        public ProviderMetadata(ComponentName componentName) {
            if (componentName != null) {
                this.mComponentName = componentName;
                return;
            }
            throw new IllegalArgumentException("componentName must not be null");
        }

        public String getPackageName() {
            return this.mComponentName.getPackageName();
        }

        public ComponentName getComponentName() {
            return this.mComponentName;
        }

        public String toString() {
            return "ProviderMetadata{ componentName=" + this.mComponentName.flattenToShortString() + " }";
        }
    }

    public static abstract class RouteController {
        public void onRelease() {
        }

        public void onSelect() {
        }

        public void onSetVolume(int i) {
        }

        @Deprecated
        public void onUnselect() {
        }

        public void onUpdateVolume(int i) {
        }

        public void onUnselect(int i) {
            onUnselect();
        }
    }

    public static abstract class DynamicGroupRouteController extends RouteController {
        public Executor mExecutor;
        public OnDynamicRoutesChangedListener mListener;
        public final Object mLock = new Object();
        public MediaRouteDescriptor mPendingGroupRoute;
        public Collection<DynamicRouteDescriptor> mPendingRoutes;

        public interface OnDynamicRoutesChangedListener {
            void onRoutesChanged(DynamicGroupRouteController dynamicGroupRouteController, MediaRouteDescriptor mediaRouteDescriptor, Collection<DynamicRouteDescriptor> collection);
        }

        public String getGroupableSelectionTitle() {
            return null;
        }

        public String getTransferableSectionTitle() {
            return null;
        }

        public abstract void onAddMemberRoute(String str);

        public abstract void onRemoveMemberRoute(String str);

        public abstract void onUpdateMemberRoutes(List<String> list);

        public void setOnDynamicRoutesChangedListener(Executor executor, final OnDynamicRoutesChangedListener onDynamicRoutesChangedListener) {
            synchronized (this.mLock) {
                if (executor == null) {
                    throw new NullPointerException("Executor shouldn't be null");
                } else if (onDynamicRoutesChangedListener != null) {
                    this.mExecutor = executor;
                    this.mListener = onDynamicRoutesChangedListener;
                    Collection<DynamicRouteDescriptor> collection = this.mPendingRoutes;
                    if (collection != null && !collection.isEmpty()) {
                        final MediaRouteDescriptor mediaRouteDescriptor = this.mPendingGroupRoute;
                        final Collection<DynamicRouteDescriptor> collection2 = this.mPendingRoutes;
                        this.mPendingGroupRoute = null;
                        this.mPendingRoutes = null;
                        this.mExecutor.execute(new Runnable() {
                            public void run() {
                                onDynamicRoutesChangedListener.onRoutesChanged(DynamicGroupRouteController.this, mediaRouteDescriptor, collection2);
                            }
                        });
                    }
                } else {
                    throw new NullPointerException("Listener shouldn't be null");
                }
            }
        }

        public final void notifyDynamicRoutesChanged(final MediaRouteDescriptor mediaRouteDescriptor, final Collection<DynamicRouteDescriptor> collection) {
            if (mediaRouteDescriptor == null) {
                throw new NullPointerException("groupRoute must not be null");
            } else if (collection != null) {
                synchronized (this.mLock) {
                    Executor executor = this.mExecutor;
                    if (executor != null) {
                        final OnDynamicRoutesChangedListener onDynamicRoutesChangedListener = this.mListener;
                        executor.execute(new Runnable() {
                            public void run() {
                                onDynamicRoutesChangedListener.onRoutesChanged(DynamicGroupRouteController.this, mediaRouteDescriptor, collection);
                            }
                        });
                    } else {
                        this.mPendingGroupRoute = mediaRouteDescriptor;
                        this.mPendingRoutes = new ArrayList(collection);
                    }
                }
            } else {
                throw new NullPointerException("dynamicRoutes must not be null");
            }
        }

        public static final class DynamicRouteDescriptor {
            public final boolean mIsGroupable;
            public final boolean mIsTransferable;
            public final boolean mIsUnselectable;
            public final MediaRouteDescriptor mMediaRouteDescriptor;
            public final int mSelectionState;

            public DynamicRouteDescriptor(MediaRouteDescriptor mediaRouteDescriptor, int i, boolean z, boolean z2, boolean z3) {
                this.mMediaRouteDescriptor = mediaRouteDescriptor;
                this.mSelectionState = i;
                this.mIsUnselectable = z;
                this.mIsGroupable = z2;
                this.mIsTransferable = z3;
            }

            public MediaRouteDescriptor getRouteDescriptor() {
                return this.mMediaRouteDescriptor;
            }

            public int getSelectionState() {
                return this.mSelectionState;
            }

            public boolean isUnselectable() {
                return this.mIsUnselectable;
            }

            public boolean isGroupable() {
                return this.mIsGroupable;
            }

            public boolean isTransferable() {
                return this.mIsTransferable;
            }

            public static DynamicRouteDescriptor fromBundle(Bundle bundle) {
                if (bundle == null) {
                    return null;
                }
                return new DynamicRouteDescriptor(MediaRouteDescriptor.fromBundle(bundle.getBundle("mrDescriptor")), bundle.getInt("selectionState", 1), bundle.getBoolean("isUnselectable", false), bundle.getBoolean("isGroupable", false), bundle.getBoolean("isTransferable", false));
            }

            public static final class Builder {
                public boolean mIsGroupable = false;
                public boolean mIsTransferable = false;
                public boolean mIsUnselectable = false;
                public final MediaRouteDescriptor mRouteDescriptor;
                public int mSelectionState = 1;

                public Builder(MediaRouteDescriptor mediaRouteDescriptor) {
                    if (mediaRouteDescriptor != null) {
                        this.mRouteDescriptor = mediaRouteDescriptor;
                        return;
                    }
                    throw new NullPointerException("descriptor must not be null");
                }

                public Builder setSelectionState(int i) {
                    this.mSelectionState = i;
                    return this;
                }

                public Builder setIsUnselectable(boolean z) {
                    this.mIsUnselectable = z;
                    return this;
                }

                public Builder setIsGroupable(boolean z) {
                    this.mIsGroupable = z;
                    return this;
                }

                public Builder setIsTransferable(boolean z) {
                    this.mIsTransferable = z;
                    return this;
                }

                public DynamicRouteDescriptor build() {
                    return new DynamicRouteDescriptor(this.mRouteDescriptor, this.mSelectionState, this.mIsUnselectable, this.mIsGroupable, this.mIsTransferable);
                }
            }
        }
    }

    public final class ProviderHandler extends Handler {
        public ProviderHandler() {
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MediaRouteProvider.this.deliverDescriptorChanged();
            } else if (i == 2) {
                MediaRouteProvider.this.deliverDiscoveryRequestChanged();
            }
        }
    }
}
