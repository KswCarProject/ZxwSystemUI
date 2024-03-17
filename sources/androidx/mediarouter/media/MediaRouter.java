package androidx.mediarouter.media;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import androidx.collection.ArrayMap;
import androidx.core.app.ActivityManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Pair;
import androidx.mediarouter.media.MediaRoute2Provider;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher;
import androidx.mediarouter.media.SystemMediaRouteProvider;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public final class MediaRouter {
    public static final boolean DEBUG = Log.isLoggable("MediaRouter", 3);
    public static GlobalMediaRouter sGlobal;
    public final ArrayList<CallbackRecord> mCallbackRecords = new ArrayList<>();
    public final Context mContext;

    public static abstract class ControlRequestCallback {
        public void onError(String str, Bundle bundle) {
        }

        public void onResult(Bundle bundle) {
        }
    }

    public MediaRouter(Context context) {
        this.mContext = context;
    }

    public static MediaRouter getInstance(Context context) {
        if (context != null) {
            checkCallingThread();
            if (sGlobal == null) {
                sGlobal = new GlobalMediaRouter(context.getApplicationContext());
            }
            return sGlobal.getRouter(context);
        }
        throw new IllegalArgumentException("context must not be null");
    }

    public static GlobalMediaRouter getGlobalRouter() {
        GlobalMediaRouter globalMediaRouter = sGlobal;
        if (globalMediaRouter == null) {
            return null;
        }
        globalMediaRouter.ensureInitialized();
        return sGlobal;
    }

    public List<RouteInfo> getRoutes() {
        checkCallingThread();
        GlobalMediaRouter globalRouter = getGlobalRouter();
        if (globalRouter == null) {
            return Collections.emptyList();
        }
        return globalRouter.getRoutes();
    }

    public RouteInfo getSelectedRoute() {
        checkCallingThread();
        return getGlobalRouter().getSelectedRoute();
    }

    public void unselect(int i) {
        if (i < 0 || i > 3) {
            throw new IllegalArgumentException("Unsupported reason to unselect route");
        }
        checkCallingThread();
        GlobalMediaRouter globalRouter = getGlobalRouter();
        RouteInfo chooseFallbackRoute = globalRouter.chooseFallbackRoute();
        if (globalRouter.getSelectedRoute() != chooseFallbackRoute) {
            globalRouter.selectRoute(chooseFallbackRoute, i);
        }
    }

    public void addMemberToDynamicGroup(RouteInfo routeInfo) {
        if (routeInfo != null) {
            checkCallingThread();
            getGlobalRouter().addMemberToDynamicGroup(routeInfo);
            return;
        }
        throw new NullPointerException("route must not be null");
    }

    public void removeMemberFromDynamicGroup(RouteInfo routeInfo) {
        if (routeInfo != null) {
            checkCallingThread();
            getGlobalRouter().removeMemberFromDynamicGroup(routeInfo);
            return;
        }
        throw new NullPointerException("route must not be null");
    }

    public void transferToRoute(RouteInfo routeInfo) {
        if (routeInfo != null) {
            checkCallingThread();
            getGlobalRouter().transferToRoute(routeInfo);
            return;
        }
        throw new NullPointerException("route must not be null");
    }

    public boolean isRouteAvailable(MediaRouteSelector mediaRouteSelector, int i) {
        if (mediaRouteSelector != null) {
            checkCallingThread();
            return getGlobalRouter().isRouteAvailable(mediaRouteSelector, i);
        }
        throw new IllegalArgumentException("selector must not be null");
    }

    public void addCallback(MediaRouteSelector mediaRouteSelector, Callback callback) {
        addCallback(mediaRouteSelector, callback, 0);
    }

    public void addCallback(MediaRouteSelector mediaRouteSelector, Callback callback, int i) {
        CallbackRecord callbackRecord;
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (callback != null) {
            checkCallingThread();
            if (DEBUG) {
                Log.d("MediaRouter", "addCallback: selector=" + mediaRouteSelector + ", callback=" + callback + ", flags=" + Integer.toHexString(i));
            }
            int findCallbackRecord = findCallbackRecord(callback);
            if (findCallbackRecord < 0) {
                callbackRecord = new CallbackRecord(this, callback);
                this.mCallbackRecords.add(callbackRecord);
            } else {
                callbackRecord = this.mCallbackRecords.get(findCallbackRecord);
            }
            boolean z = false;
            boolean z2 = true;
            if (i != callbackRecord.mFlags) {
                callbackRecord.mFlags = i;
                z = true;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if ((i & 1) != 0) {
                z = true;
            }
            callbackRecord.mTimestamp = elapsedRealtime;
            if (!callbackRecord.mSelector.contains(mediaRouteSelector)) {
                callbackRecord.mSelector = new MediaRouteSelector.Builder(callbackRecord.mSelector).addSelector(mediaRouteSelector).build();
            } else {
                z2 = z;
            }
            if (z2) {
                getGlobalRouter().updateDiscoveryRequest();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    public void removeCallback(Callback callback) {
        if (callback != null) {
            checkCallingThread();
            if (DEBUG) {
                Log.d("MediaRouter", "removeCallback: callback=" + callback);
            }
            int findCallbackRecord = findCallbackRecord(callback);
            if (findCallbackRecord >= 0) {
                this.mCallbackRecords.remove(findCallbackRecord);
                getGlobalRouter().updateDiscoveryRequest();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public final int findCallbackRecord(Callback callback) {
        int size = this.mCallbackRecords.size();
        for (int i = 0; i < size; i++) {
            if (this.mCallbackRecords.get(i).mCallback == callback) {
                return i;
            }
        }
        return -1;
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        GlobalMediaRouter globalMediaRouter = sGlobal;
        if (globalMediaRouter == null) {
            return null;
        }
        return globalMediaRouter.getMediaSessionToken();
    }

    public MediaRouterParams getRouterParams() {
        checkCallingThread();
        GlobalMediaRouter globalRouter = getGlobalRouter();
        if (globalRouter == null) {
            return null;
        }
        return globalRouter.getRouterParams();
    }

    public static void checkCallingThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("The media router service must only be accessed on the application's main thread.");
        }
    }

    public static boolean isMediaTransferEnabled() {
        if (sGlobal == null) {
            return false;
        }
        return getGlobalRouter().isMediaTransferEnabled();
    }

    public static boolean isGroupVolumeUxEnabled() {
        if (sGlobal == null) {
            return false;
        }
        return getGlobalRouter().isGroupVolumeUxEnabled();
    }

    public static int getGlobalCallbackCount() {
        if (sGlobal == null) {
            return 0;
        }
        return getGlobalRouter().getCallbackCount();
    }

    public static boolean isTransferToLocalEnabled() {
        GlobalMediaRouter globalRouter = getGlobalRouter();
        if (globalRouter == null) {
            return false;
        }
        return globalRouter.isTransferToLocalEnabled();
    }

    public static class RouteInfo {
        public boolean mCanDisconnect;
        public int mConnectionState;
        public final ArrayList<IntentFilter> mControlFilters = new ArrayList<>();
        public String mDescription;
        public MediaRouteDescriptor mDescriptor;
        public final String mDescriptorId;
        public int mDeviceType;
        public Map<String, MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> mDynamicGroupDescriptors;
        public boolean mEnabled;
        public Bundle mExtras;
        public Uri mIconUri;
        public List<RouteInfo> mMemberRoutes = new ArrayList();
        public String mName;
        public int mPlaybackStream;
        public int mPlaybackType;
        public Display mPresentationDisplay;
        public int mPresentationDisplayId = -1;
        public final ProviderInfo mProvider;
        public IntentSender mSettingsIntent;
        public final String mUniqueId;
        public int mVolume;
        public int mVolumeHandling;
        public int mVolumeMax;

        public RouteInfo(ProviderInfo providerInfo, String str, String str2) {
            this.mProvider = providerInfo;
            this.mDescriptorId = str;
            this.mUniqueId = str2;
        }

        public ProviderInfo getProvider() {
            return this.mProvider;
        }

        public String getId() {
            return this.mUniqueId;
        }

        public String getName() {
            return this.mName;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public Uri getIconUri() {
            return this.mIconUri;
        }

        public boolean isEnabled() {
            return this.mEnabled;
        }

        public int getConnectionState() {
            return this.mConnectionState;
        }

        public boolean isSelected() {
            MediaRouter.checkCallingThread();
            return MediaRouter.getGlobalRouter().getSelectedRoute() == this;
        }

        public boolean isDefault() {
            MediaRouter.checkCallingThread();
            return MediaRouter.getGlobalRouter().getDefaultRoute() == this;
        }

        public boolean matchesSelector(MediaRouteSelector mediaRouteSelector) {
            if (mediaRouteSelector != null) {
                MediaRouter.checkCallingThread();
                return mediaRouteSelector.matchesControlFilters(this.mControlFilters);
            }
            throw new IllegalArgumentException("selector must not be null");
        }

        public boolean supportsControlCategory(String str) {
            if (str != null) {
                MediaRouter.checkCallingThread();
                int size = this.mControlFilters.size();
                for (int i = 0; i < size; i++) {
                    if (this.mControlFilters.get(i).hasCategory(str)) {
                        return true;
                    }
                }
                return false;
            }
            throw new IllegalArgumentException("category must not be null");
        }

        public int getPlaybackType() {
            return this.mPlaybackType;
        }

        public int getPlaybackStream() {
            return this.mPlaybackStream;
        }

        public int getDeviceType() {
            return this.mDeviceType;
        }

        public boolean isDefaultOrBluetooth() {
            if (isDefault() || this.mDeviceType == 3) {
                return true;
            }
            if (!isSystemMediaRouteProvider(this) || !supportsControlCategory("android.media.intent.category.LIVE_AUDIO") || supportsControlCategory("android.media.intent.category.LIVE_VIDEO")) {
                return false;
            }
            return true;
        }

        public boolean isSelectable() {
            return this.mDescriptor != null && this.mEnabled;
        }

        public static boolean isSystemMediaRouteProvider(RouteInfo routeInfo) {
            return TextUtils.equals(routeInfo.getProviderInstance().getMetadata().getPackageName(), ThemeOverlayApplier.ANDROID_PACKAGE);
        }

        public int getVolumeHandling() {
            if (!isGroup() || MediaRouter.isGroupVolumeUxEnabled()) {
                return this.mVolumeHandling;
            }
            return 0;
        }

        public int getVolume() {
            return this.mVolume;
        }

        public int getVolumeMax() {
            return this.mVolumeMax;
        }

        public boolean canDisconnect() {
            return this.mCanDisconnect;
        }

        public void requestSetVolume(int i) {
            MediaRouter.checkCallingThread();
            MediaRouter.getGlobalRouter().requestSetVolume(this, Math.min(this.mVolumeMax, Math.max(0, i)));
        }

        public void requestUpdateVolume(int i) {
            MediaRouter.checkCallingThread();
            if (i != 0) {
                MediaRouter.getGlobalRouter().requestUpdateVolume(this, i);
            }
        }

        public int getPresentationDisplayId() {
            return this.mPresentationDisplayId;
        }

        public void select() {
            MediaRouter.checkCallingThread();
            MediaRouter.getGlobalRouter().selectRoute(this, 3);
        }

        public boolean isGroup() {
            return getMemberRoutes().size() >= 1;
        }

        public DynamicGroupState getDynamicGroupState(RouteInfo routeInfo) {
            if (routeInfo != null) {
                Map<String, MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> map = this.mDynamicGroupDescriptors;
                if (map == null || !map.containsKey(routeInfo.mUniqueId)) {
                    return null;
                }
                return new DynamicGroupState(this.mDynamicGroupDescriptors.get(routeInfo.mUniqueId));
            }
            throw new NullPointerException("route must not be null");
        }

        public List<RouteInfo> getMemberRoutes() {
            return Collections.unmodifiableList(this.mMemberRoutes);
        }

        public MediaRouteProvider.DynamicGroupRouteController getDynamicGroupController() {
            MediaRouter.checkCallingThread();
            MediaRouteProvider.RouteController routeController = MediaRouter.getGlobalRouter().mSelectedRouteController;
            if (routeController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                return (MediaRouteProvider.DynamicGroupRouteController) routeController;
            }
            return null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MediaRouter.RouteInfo{ uniqueId=" + this.mUniqueId + ", name=" + this.mName + ", description=" + this.mDescription + ", iconUri=" + this.mIconUri + ", enabled=" + this.mEnabled + ", connectionState=" + this.mConnectionState + ", canDisconnect=" + this.mCanDisconnect + ", playbackType=" + this.mPlaybackType + ", playbackStream=" + this.mPlaybackStream + ", deviceType=" + this.mDeviceType + ", volumeHandling=" + this.mVolumeHandling + ", volume=" + this.mVolume + ", volumeMax=" + this.mVolumeMax + ", presentationDisplayId=" + this.mPresentationDisplayId + ", extras=" + this.mExtras + ", settingsIntent=" + this.mSettingsIntent + ", providerPackageName=" + this.mProvider.getPackageName());
            if (isGroup()) {
                sb.append(", members=[");
                int size = this.mMemberRoutes.size();
                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    if (this.mMemberRoutes.get(i) != this) {
                        sb.append(this.mMemberRoutes.get(i).getId());
                    }
                }
                sb.append(']');
            }
            sb.append(" }");
            return sb.toString();
        }

        public int maybeUpdateDescriptor(MediaRouteDescriptor mediaRouteDescriptor) {
            if (this.mDescriptor != mediaRouteDescriptor) {
                return updateDescriptor(mediaRouteDescriptor);
            }
            return 0;
        }

        public final boolean isSameControlFilters(List<IntentFilter> list, List<IntentFilter> list2) {
            if (list == list2) {
                return true;
            }
            if (list == null || list2 == null) {
                return false;
            }
            ListIterator<IntentFilter> listIterator = list.listIterator();
            ListIterator<IntentFilter> listIterator2 = list2.listIterator();
            while (listIterator.hasNext() && listIterator2.hasNext()) {
                if (!isSameControlFilter(listIterator.next(), listIterator2.next())) {
                    return false;
                }
            }
            if (listIterator.hasNext() || listIterator2.hasNext()) {
                return false;
            }
            return true;
        }

        public final boolean isSameControlFilter(IntentFilter intentFilter, IntentFilter intentFilter2) {
            int countActions;
            if (intentFilter == intentFilter2) {
                return true;
            }
            if (intentFilter == null || intentFilter2 == null || (countActions = intentFilter.countActions()) != intentFilter2.countActions()) {
                return false;
            }
            for (int i = 0; i < countActions; i++) {
                if (!intentFilter.getAction(i).equals(intentFilter2.getAction(i))) {
                    return false;
                }
            }
            int countCategories = intentFilter.countCategories();
            if (countCategories != intentFilter2.countCategories()) {
                return false;
            }
            for (int i2 = 0; i2 < countCategories; i2++) {
                if (!intentFilter.getCategory(i2).equals(intentFilter2.getCategory(i2))) {
                    return false;
                }
            }
            return true;
        }

        public int updateDescriptor(MediaRouteDescriptor mediaRouteDescriptor) {
            int i;
            this.mDescriptor = mediaRouteDescriptor;
            boolean z = false;
            if (mediaRouteDescriptor == null) {
                return 0;
            }
            if (!ObjectsCompat.equals(this.mName, mediaRouteDescriptor.getName())) {
                this.mName = mediaRouteDescriptor.getName();
                i = 1;
            } else {
                i = 0;
            }
            if (!ObjectsCompat.equals(this.mDescription, mediaRouteDescriptor.getDescription())) {
                this.mDescription = mediaRouteDescriptor.getDescription();
                i |= 1;
            }
            if (!ObjectsCompat.equals(this.mIconUri, mediaRouteDescriptor.getIconUri())) {
                this.mIconUri = mediaRouteDescriptor.getIconUri();
                i |= 1;
            }
            if (this.mEnabled != mediaRouteDescriptor.isEnabled()) {
                this.mEnabled = mediaRouteDescriptor.isEnabled();
                i |= 1;
            }
            if (this.mConnectionState != mediaRouteDescriptor.getConnectionState()) {
                this.mConnectionState = mediaRouteDescriptor.getConnectionState();
                i |= 1;
            }
            if (!isSameControlFilters(this.mControlFilters, mediaRouteDescriptor.getControlFilters())) {
                this.mControlFilters.clear();
                this.mControlFilters.addAll(mediaRouteDescriptor.getControlFilters());
                i |= 1;
            }
            if (this.mPlaybackType != mediaRouteDescriptor.getPlaybackType()) {
                this.mPlaybackType = mediaRouteDescriptor.getPlaybackType();
                i |= 1;
            }
            if (this.mPlaybackStream != mediaRouteDescriptor.getPlaybackStream()) {
                this.mPlaybackStream = mediaRouteDescriptor.getPlaybackStream();
                i |= 1;
            }
            if (this.mDeviceType != mediaRouteDescriptor.getDeviceType()) {
                this.mDeviceType = mediaRouteDescriptor.getDeviceType();
                i |= 1;
            }
            if (this.mVolumeHandling != mediaRouteDescriptor.getVolumeHandling()) {
                this.mVolumeHandling = mediaRouteDescriptor.getVolumeHandling();
                i |= 3;
            }
            if (this.mVolume != mediaRouteDescriptor.getVolume()) {
                this.mVolume = mediaRouteDescriptor.getVolume();
                i |= 3;
            }
            if (this.mVolumeMax != mediaRouteDescriptor.getVolumeMax()) {
                this.mVolumeMax = mediaRouteDescriptor.getVolumeMax();
                i |= 3;
            }
            if (this.mPresentationDisplayId != mediaRouteDescriptor.getPresentationDisplayId()) {
                this.mPresentationDisplayId = mediaRouteDescriptor.getPresentationDisplayId();
                this.mPresentationDisplay = null;
                i |= 5;
            }
            if (!ObjectsCompat.equals(this.mExtras, mediaRouteDescriptor.getExtras())) {
                this.mExtras = mediaRouteDescriptor.getExtras();
                i |= 1;
            }
            if (!ObjectsCompat.equals(this.mSettingsIntent, mediaRouteDescriptor.getSettingsActivity())) {
                this.mSettingsIntent = mediaRouteDescriptor.getSettingsActivity();
                i |= 1;
            }
            if (this.mCanDisconnect != mediaRouteDescriptor.canDisconnectAndKeepPlaying()) {
                this.mCanDisconnect = mediaRouteDescriptor.canDisconnectAndKeepPlaying();
                i |= 5;
            }
            List<String> groupMemberIds = mediaRouteDescriptor.getGroupMemberIds();
            ArrayList arrayList = new ArrayList();
            if (groupMemberIds.size() != this.mMemberRoutes.size()) {
                z = true;
            }
            if (!groupMemberIds.isEmpty()) {
                GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
                for (String uniqueId : groupMemberIds) {
                    RouteInfo route = globalRouter.getRoute(globalRouter.getUniqueId(getProvider(), uniqueId));
                    if (route != null) {
                        arrayList.add(route);
                        if (!z && !this.mMemberRoutes.contains(route)) {
                            z = true;
                        }
                    }
                }
            }
            if (!z) {
                return i;
            }
            this.mMemberRoutes = arrayList;
            return i | 1;
        }

        public String getDescriptorId() {
            return this.mDescriptorId;
        }

        public MediaRouteProvider getProviderInstance() {
            return this.mProvider.getProviderInstance();
        }

        public void updateDynamicDescriptors(Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            this.mMemberRoutes.clear();
            if (this.mDynamicGroupDescriptors == null) {
                this.mDynamicGroupDescriptors = new ArrayMap();
            }
            this.mDynamicGroupDescriptors.clear();
            for (MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor next : collection) {
                RouteInfo findRouteByDynamicRouteDescriptor = findRouteByDynamicRouteDescriptor(next);
                if (findRouteByDynamicRouteDescriptor != null) {
                    this.mDynamicGroupDescriptors.put(findRouteByDynamicRouteDescriptor.mUniqueId, next);
                    if (next.getSelectionState() == 2 || next.getSelectionState() == 3) {
                        this.mMemberRoutes.add(findRouteByDynamicRouteDescriptor);
                    }
                }
            }
            MediaRouter.getGlobalRouter().mCallbackHandler.post(259, this);
        }

        public RouteInfo findRouteByDynamicRouteDescriptor(MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor) {
            return getProvider().findRouteByDescriptorId(dynamicRouteDescriptor.getRouteDescriptor().getId());
        }

        public static final class DynamicGroupState {
            public final MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor mDynamicDescriptor;

            public DynamicGroupState(MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor) {
                this.mDynamicDescriptor = dynamicRouteDescriptor;
            }

            public int getSelectionState() {
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = this.mDynamicDescriptor;
                if (dynamicRouteDescriptor != null) {
                    return dynamicRouteDescriptor.getSelectionState();
                }
                return 1;
            }

            public boolean isUnselectable() {
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = this.mDynamicDescriptor;
                return dynamicRouteDescriptor == null || dynamicRouteDescriptor.isUnselectable();
            }

            public boolean isGroupable() {
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = this.mDynamicDescriptor;
                return dynamicRouteDescriptor != null && dynamicRouteDescriptor.isGroupable();
            }

            public boolean isTransferable() {
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = this.mDynamicDescriptor;
                return dynamicRouteDescriptor != null && dynamicRouteDescriptor.isTransferable();
            }
        }
    }

    public static final class ProviderInfo {
        public MediaRouteProviderDescriptor mDescriptor;
        public final MediaRouteProvider.ProviderMetadata mMetadata;
        public final MediaRouteProvider mProviderInstance;
        public final List<RouteInfo> mRoutes = new ArrayList();

        public ProviderInfo(MediaRouteProvider mediaRouteProvider) {
            this.mProviderInstance = mediaRouteProvider;
            this.mMetadata = mediaRouteProvider.getMetadata();
        }

        public MediaRouteProvider getProviderInstance() {
            MediaRouter.checkCallingThread();
            return this.mProviderInstance;
        }

        public String getPackageName() {
            return this.mMetadata.getPackageName();
        }

        public ComponentName getComponentName() {
            return this.mMetadata.getComponentName();
        }

        public List<RouteInfo> getRoutes() {
            MediaRouter.checkCallingThread();
            return Collections.unmodifiableList(this.mRoutes);
        }

        public boolean updateDescriptor(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            if (this.mDescriptor == mediaRouteProviderDescriptor) {
                return false;
            }
            this.mDescriptor = mediaRouteProviderDescriptor;
            return true;
        }

        public int findRouteIndexByDescriptorId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (this.mRoutes.get(i).mDescriptorId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public RouteInfo findRouteByDescriptorId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (this.mRoutes.get(i).mDescriptorId.equals(str)) {
                    return this.mRoutes.get(i);
                }
            }
            return null;
        }

        public boolean supportsDynamicGroup() {
            MediaRouteProviderDescriptor mediaRouteProviderDescriptor = this.mDescriptor;
            return mediaRouteProviderDescriptor != null && mediaRouteProviderDescriptor.supportsDynamicGroupRoute();
        }

        public String toString() {
            return "MediaRouter.RouteProviderInfo{ packageName=" + getPackageName() + " }";
        }
    }

    public static abstract class Callback {
        public void onProviderAdded(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onProviderChanged(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onProviderRemoved(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onRouteAdded(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public abstract void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo);

        public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteRemoved(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        @Deprecated
        public void onRouteSelected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        @Deprecated
        public void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteVolumeChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouterParamsChanged(MediaRouter mediaRouter, MediaRouterParams mediaRouterParams) {
        }

        public void onRouteSelected(MediaRouter mediaRouter, RouteInfo routeInfo, int i) {
            onRouteSelected(mediaRouter, routeInfo);
        }

        public void onRouteSelected(MediaRouter mediaRouter, RouteInfo routeInfo, int i, RouteInfo routeInfo2) {
            onRouteSelected(mediaRouter, routeInfo, i);
        }

        public void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo, int i) {
            onRouteUnselected(mediaRouter, routeInfo);
        }
    }

    public static final class CallbackRecord {
        public final Callback mCallback;
        public int mFlags;
        public final MediaRouter mRouter;
        public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
        public long mTimestamp;

        public CallbackRecord(MediaRouter mediaRouter, Callback callback) {
            this.mRouter = mediaRouter;
            this.mCallback = callback;
        }

        public boolean filterRouteEvent(RouteInfo routeInfo, int i, RouteInfo routeInfo2, int i2) {
            if ((this.mFlags & 2) != 0 || routeInfo.matchesSelector(this.mSelector)) {
                return true;
            }
            if (!MediaRouter.isTransferToLocalEnabled() || !routeInfo.isDefaultOrBluetooth() || i != 262 || i2 != 3 || routeInfo2 == null) {
                return false;
            }
            return !routeInfo2.isDefaultOrBluetooth();
        }
    }

    public static final class GlobalMediaRouter implements SystemMediaRouteProvider.SyncCallback, RegisteredMediaRouteProviderWatcher.Callback {
        public MediaRouterActiveScanThrottlingHelper mActiveScanThrottlingHelper;
        public final Context mApplicationContext;
        public RouteInfo mBluetoothRoute;
        public int mCallbackCount;
        public final CallbackHandler mCallbackHandler = new CallbackHandler();
        public MediaSessionCompat mCompatSession;
        public RouteInfo mDefaultRoute;
        public MediaRouteDiscoveryRequest mDiscoveryRequest;
        public MediaRouteDiscoveryRequest mDiscoveryRequestForMr2Provider;
        public MediaRouteProvider.DynamicGroupRouteController.OnDynamicRoutesChangedListener mDynamicRoutesListener = new MediaRouteProvider.DynamicGroupRouteController.OnDynamicRoutesChangedListener() {
            public void onRoutesChanged(MediaRouteProvider.DynamicGroupRouteController dynamicGroupRouteController, MediaRouteDescriptor mediaRouteDescriptor, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
                GlobalMediaRouter globalMediaRouter = GlobalMediaRouter.this;
                if (dynamicGroupRouteController == globalMediaRouter.mRequestedRouteController && mediaRouteDescriptor != null) {
                    ProviderInfo provider = globalMediaRouter.mRequestedRoute.getProvider();
                    String id = mediaRouteDescriptor.getId();
                    RouteInfo routeInfo = new RouteInfo(provider, id, GlobalMediaRouter.this.assignRouteUniqueId(provider, id));
                    routeInfo.maybeUpdateDescriptor(mediaRouteDescriptor);
                    GlobalMediaRouter globalMediaRouter2 = GlobalMediaRouter.this;
                    if (globalMediaRouter2.mSelectedRoute != routeInfo) {
                        globalMediaRouter2.notifyTransfer(globalMediaRouter2, routeInfo, globalMediaRouter2.mRequestedRouteController, 3, globalMediaRouter2.mRequestedRoute, collection);
                        GlobalMediaRouter globalMediaRouter3 = GlobalMediaRouter.this;
                        globalMediaRouter3.mRequestedRoute = null;
                        globalMediaRouter3.mRequestedRouteController = null;
                    }
                } else if (dynamicGroupRouteController == globalMediaRouter.mSelectedRouteController) {
                    if (mediaRouteDescriptor != null) {
                        globalMediaRouter.updateRouteDescriptorAndNotify(globalMediaRouter.mSelectedRoute, mediaRouteDescriptor);
                    }
                    GlobalMediaRouter.this.mSelectedRoute.updateDynamicDescriptors(collection);
                }
            }
        };
        public boolean mIsInitialized;
        public final boolean mLowRam;
        public MediaRoute2Provider mMr2Provider;
        public final RemoteControlClientCompat$PlaybackInfo mPlaybackInfo = new RemoteControlClientCompat$PlaybackInfo();
        public final ProviderCallback mProviderCallback = new ProviderCallback();
        public final ArrayList<ProviderInfo> mProviders = new ArrayList<>();
        public RegisteredMediaRouteProviderWatcher mRegisteredProviderWatcher;
        public final ArrayList<RemoteControlClientRecord> mRemoteControlClients = new ArrayList<>();
        public RouteInfo mRequestedRoute;
        public MediaRouteProvider.RouteController mRequestedRouteController;
        public final Map<String, MediaRouteProvider.RouteController> mRouteControllerMap = new HashMap();
        public MediaRouterParams mRouterParams;
        public final ArrayList<WeakReference<MediaRouter>> mRouters = new ArrayList<>();
        public final ArrayList<RouteInfo> mRoutes = new ArrayList<>();
        public RouteInfo mSelectedRoute;
        public MediaRouteProvider.RouteController mSelectedRouteController;
        public final MediaSessionCompat.OnActiveChangeListener mSessionActiveListener = new MediaSessionCompat.OnActiveChangeListener() {
        };
        public SystemMediaRouteProvider mSystemProvider;
        public PrepareTransferNotifier mTransferNotifier;
        public boolean mTransferReceiverDeclared;
        public final Map<Pair<String, String>, String> mUniqueIdMap = new HashMap();

        public GlobalMediaRouter(Context context) {
            this.mApplicationContext = context;
            this.mLowRam = ActivityManagerCompat.isLowRamDevice((ActivityManager) context.getSystemService("activity"));
        }

        @SuppressLint({"NewApi", "SyntheticAccessor"})
        public void ensureInitialized() {
            if (!this.mIsInitialized) {
                this.mIsInitialized = true;
                boolean isDeclared = MediaTransferReceiver.isDeclared(this.mApplicationContext);
                this.mTransferReceiverDeclared = isDeclared;
                if (isDeclared) {
                    this.mMr2Provider = new MediaRoute2Provider(this.mApplicationContext, new Mr2ProviderCallback());
                } else {
                    this.mMr2Provider = null;
                }
                this.mSystemProvider = SystemMediaRouteProvider.obtain(this.mApplicationContext, this);
                start();
            }
        }

        public final void start() {
            this.mActiveScanThrottlingHelper = new MediaRouterActiveScanThrottlingHelper(new Runnable() {
                public void run() {
                    GlobalMediaRouter.this.updateDiscoveryRequest();
                }
            });
            addProvider(this.mSystemProvider);
            MediaRoute2Provider mediaRoute2Provider = this.mMr2Provider;
            if (mediaRoute2Provider != null) {
                addProvider(mediaRoute2Provider);
            }
            RegisteredMediaRouteProviderWatcher registeredMediaRouteProviderWatcher = new RegisteredMediaRouteProviderWatcher(this.mApplicationContext, this);
            this.mRegisteredProviderWatcher = registeredMediaRouteProviderWatcher;
            registeredMediaRouteProviderWatcher.start();
        }

        public MediaRouter getRouter(Context context) {
            int size = this.mRouters.size();
            while (true) {
                size--;
                if (size >= 0) {
                    MediaRouter mediaRouter = (MediaRouter) this.mRouters.get(size).get();
                    if (mediaRouter == null) {
                        this.mRouters.remove(size);
                    } else if (mediaRouter.mContext == context) {
                        return mediaRouter;
                    }
                } else {
                    MediaRouter mediaRouter2 = new MediaRouter(context);
                    this.mRouters.add(new WeakReference(mediaRouter2));
                    return mediaRouter2;
                }
            }
        }

        public void requestSetVolume(RouteInfo routeInfo, int i) {
            MediaRouteProvider.RouteController routeController;
            MediaRouteProvider.RouteController routeController2;
            if (routeInfo == this.mSelectedRoute && (routeController2 = this.mSelectedRouteController) != null) {
                routeController2.onSetVolume(i);
            } else if (!this.mRouteControllerMap.isEmpty() && (routeController = this.mRouteControllerMap.get(routeInfo.mUniqueId)) != null) {
                routeController.onSetVolume(i);
            }
        }

        public void requestUpdateVolume(RouteInfo routeInfo, int i) {
            MediaRouteProvider.RouteController routeController;
            MediaRouteProvider.RouteController routeController2;
            if (routeInfo == this.mSelectedRoute && (routeController2 = this.mSelectedRouteController) != null) {
                routeController2.onUpdateVolume(i);
            } else if (!this.mRouteControllerMap.isEmpty() && (routeController = this.mRouteControllerMap.get(routeInfo.mUniqueId)) != null) {
                routeController.onUpdateVolume(i);
            }
        }

        public RouteInfo getRoute(String str) {
            Iterator<RouteInfo> it = this.mRoutes.iterator();
            while (it.hasNext()) {
                RouteInfo next = it.next();
                if (next.mUniqueId.equals(str)) {
                    return next;
                }
            }
            return null;
        }

        public List<RouteInfo> getRoutes() {
            return this.mRoutes;
        }

        public MediaRouterParams getRouterParams() {
            return this.mRouterParams;
        }

        public RouteInfo getDefaultRoute() {
            RouteInfo routeInfo = this.mDefaultRoute;
            if (routeInfo != null) {
                return routeInfo;
            }
            throw new IllegalStateException("There is no default route.  The media router has not yet been fully initialized.");
        }

        public RouteInfo getSelectedRoute() {
            RouteInfo routeInfo = this.mSelectedRoute;
            if (routeInfo != null) {
                return routeInfo;
            }
            throw new IllegalStateException("There is no currently selected route.  The media router has not yet been fully initialized.");
        }

        public RouteInfo.DynamicGroupState getDynamicGroupState(RouteInfo routeInfo) {
            return this.mSelectedRoute.getDynamicGroupState(routeInfo);
        }

        public void addMemberToDynamicGroup(RouteInfo routeInfo) {
            if (this.mSelectedRouteController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                RouteInfo.DynamicGroupState dynamicGroupState = getDynamicGroupState(routeInfo);
                if (this.mSelectedRoute.getMemberRoutes().contains(routeInfo) || dynamicGroupState == null || !dynamicGroupState.isGroupable()) {
                    Log.w("MediaRouter", "Ignoring attempt to add a non-groupable route to dynamic group : " + routeInfo);
                    return;
                }
                ((MediaRouteProvider.DynamicGroupRouteController) this.mSelectedRouteController).onAddMemberRoute(routeInfo.getDescriptorId());
                return;
            }
            throw new IllegalStateException("There is no currently selected dynamic group route.");
        }

        public void removeMemberFromDynamicGroup(RouteInfo routeInfo) {
            if (this.mSelectedRouteController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                RouteInfo.DynamicGroupState dynamicGroupState = getDynamicGroupState(routeInfo);
                if (!this.mSelectedRoute.getMemberRoutes().contains(routeInfo) || dynamicGroupState == null || !dynamicGroupState.isUnselectable()) {
                    Log.w("MediaRouter", "Ignoring attempt to remove a non-unselectable member route : " + routeInfo);
                } else if (this.mSelectedRoute.getMemberRoutes().size() <= 1) {
                    Log.w("MediaRouter", "Ignoring attempt to remove the last member route.");
                } else {
                    ((MediaRouteProvider.DynamicGroupRouteController) this.mSelectedRouteController).onRemoveMemberRoute(routeInfo.getDescriptorId());
                }
            } else {
                throw new IllegalStateException("There is no currently selected dynamic group route.");
            }
        }

        public void transferToRoute(RouteInfo routeInfo) {
            if (this.mSelectedRouteController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                RouteInfo.DynamicGroupState dynamicGroupState = getDynamicGroupState(routeInfo);
                if (dynamicGroupState == null || !dynamicGroupState.isTransferable()) {
                    Log.w("MediaRouter", "Ignoring attempt to transfer to a non-transferable route.");
                } else {
                    ((MediaRouteProvider.DynamicGroupRouteController) this.mSelectedRouteController).onUpdateMemberRoutes(Collections.singletonList(routeInfo.getDescriptorId()));
                }
            } else {
                throw new IllegalStateException("There is no currently selected dynamic group route.");
            }
        }

        public void selectRoute(RouteInfo routeInfo, int i) {
            if (!this.mRoutes.contains(routeInfo)) {
                Log.w("MediaRouter", "Ignoring attempt to select removed route: " + routeInfo);
            } else if (!routeInfo.mEnabled) {
                Log.w("MediaRouter", "Ignoring attempt to select disabled route: " + routeInfo);
            } else {
                MediaRouteProvider providerInstance = routeInfo.getProviderInstance();
                MediaRoute2Provider mediaRoute2Provider = this.mMr2Provider;
                if (providerInstance != mediaRoute2Provider || this.mSelectedRoute == routeInfo) {
                    selectRouteInternal(routeInfo, i);
                } else {
                    mediaRoute2Provider.transferTo(routeInfo.getDescriptorId());
                }
            }
        }

        public boolean isRouteAvailable(MediaRouteSelector mediaRouteSelector, int i) {
            if (mediaRouteSelector.isEmpty()) {
                return false;
            }
            if ((i & 2) == 0 && this.mLowRam) {
                return true;
            }
            MediaRouterParams mediaRouterParams = this.mRouterParams;
            boolean z = mediaRouterParams != null && mediaRouterParams.isOutputSwitcherEnabled() && isMediaTransferEnabled();
            int size = this.mRoutes.size();
            for (int i2 = 0; i2 < size; i2++) {
                RouteInfo routeInfo = this.mRoutes.get(i2);
                if (((i & 1) == 0 || !routeInfo.isDefaultOrBluetooth()) && ((!z || routeInfo.isDefaultOrBluetooth() || routeInfo.getProviderInstance() == this.mMr2Provider) && routeInfo.matchesSelector(mediaRouteSelector))) {
                    return true;
                }
            }
            return false;
        }

        public void updateDiscoveryRequest() {
            MediaRouteSelector.Builder builder = new MediaRouteSelector.Builder();
            this.mActiveScanThrottlingHelper.reset();
            int size = this.mRouters.size();
            int i = 0;
            boolean z = false;
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                MediaRouter mediaRouter = (MediaRouter) this.mRouters.get(size).get();
                if (mediaRouter == null) {
                    this.mRouters.remove(size);
                } else {
                    int size2 = mediaRouter.mCallbackRecords.size();
                    i += size2;
                    for (int i2 = 0; i2 < size2; i2++) {
                        CallbackRecord callbackRecord = mediaRouter.mCallbackRecords.get(i2);
                        builder.addSelector(callbackRecord.mSelector);
                        boolean z2 = (callbackRecord.mFlags & 1) != 0;
                        this.mActiveScanThrottlingHelper.requestActiveScan(z2, callbackRecord.mTimestamp);
                        if (z2) {
                            z = true;
                        }
                        int i3 = callbackRecord.mFlags;
                        if ((i3 & 4) != 0 && !this.mLowRam) {
                            z = true;
                        }
                        if ((i3 & 8) != 0) {
                            z = true;
                        }
                    }
                }
            }
            boolean finalizeActiveScanAndScheduleSuppressActiveScanRunnable = this.mActiveScanThrottlingHelper.finalizeActiveScanAndScheduleSuppressActiveScanRunnable();
            this.mCallbackCount = i;
            MediaRouteSelector build = z ? builder.build() : MediaRouteSelector.EMPTY;
            updateMr2ProviderDiscoveryRequest(builder.build(), finalizeActiveScanAndScheduleSuppressActiveScanRunnable);
            MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest = this.mDiscoveryRequest;
            if (mediaRouteDiscoveryRequest == null || !mediaRouteDiscoveryRequest.getSelector().equals(build) || this.mDiscoveryRequest.isActiveScan() != finalizeActiveScanAndScheduleSuppressActiveScanRunnable) {
                if (!build.isEmpty() || finalizeActiveScanAndScheduleSuppressActiveScanRunnable) {
                    this.mDiscoveryRequest = new MediaRouteDiscoveryRequest(build, finalizeActiveScanAndScheduleSuppressActiveScanRunnable);
                } else if (this.mDiscoveryRequest != null) {
                    this.mDiscoveryRequest = null;
                } else {
                    return;
                }
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Updated discovery request: " + this.mDiscoveryRequest);
                }
                if (z && !finalizeActiveScanAndScheduleSuppressActiveScanRunnable && this.mLowRam) {
                    Log.i("MediaRouter", "Forcing passive route discovery on a low-RAM device, system performance may be affected.  Please consider using CALLBACK_FLAG_REQUEST_DISCOVERY instead of CALLBACK_FLAG_FORCE_DISCOVERY.");
                }
                int size3 = this.mProviders.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    MediaRouteProvider mediaRouteProvider = this.mProviders.get(i4).mProviderInstance;
                    if (mediaRouteProvider != this.mMr2Provider) {
                        mediaRouteProvider.setDiscoveryRequest(this.mDiscoveryRequest);
                    }
                }
            }
        }

        public final void updateMr2ProviderDiscoveryRequest(MediaRouteSelector mediaRouteSelector, boolean z) {
            if (isMediaTransferEnabled()) {
                MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest = this.mDiscoveryRequestForMr2Provider;
                if (mediaRouteDiscoveryRequest == null || !mediaRouteDiscoveryRequest.getSelector().equals(mediaRouteSelector) || this.mDiscoveryRequestForMr2Provider.isActiveScan() != z) {
                    if (!mediaRouteSelector.isEmpty() || z) {
                        this.mDiscoveryRequestForMr2Provider = new MediaRouteDiscoveryRequest(mediaRouteSelector, z);
                    } else if (this.mDiscoveryRequestForMr2Provider != null) {
                        this.mDiscoveryRequestForMr2Provider = null;
                    } else {
                        return;
                    }
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Updated MediaRoute2Provider's discovery request: " + this.mDiscoveryRequestForMr2Provider);
                    }
                    this.mMr2Provider.setDiscoveryRequest(this.mDiscoveryRequestForMr2Provider);
                }
            }
        }

        public int getCallbackCount() {
            return this.mCallbackCount;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r1 = r1.mRouterParams;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean isMediaTransferEnabled() {
            /*
                r1 = this;
                boolean r0 = r1.mTransferReceiverDeclared
                if (r0 == 0) goto L_0x0010
                androidx.mediarouter.media.MediaRouterParams r1 = r1.mRouterParams
                if (r1 == 0) goto L_0x000e
                boolean r1 = r1.isMediaTransferReceiverEnabled()
                if (r1 == 0) goto L_0x0010
            L_0x000e:
                r1 = 1
                goto L_0x0011
            L_0x0010:
                r1 = 0
            L_0x0011:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter.GlobalMediaRouter.isMediaTransferEnabled():boolean");
        }

        public boolean isTransferToLocalEnabled() {
            MediaRouterParams mediaRouterParams = this.mRouterParams;
            if (mediaRouterParams == null) {
                return false;
            }
            return mediaRouterParams.isTransferToLocalEnabled();
        }

        public boolean isGroupVolumeUxEnabled() {
            Bundle bundle;
            MediaRouterParams mediaRouterParams = this.mRouterParams;
            if (mediaRouterParams == null || (bundle = mediaRouterParams.mExtras) == null || bundle.getBoolean("androidx.mediarouter.media.MediaRouterParams.ENABLE_GROUP_VOLUME_UX", true)) {
                return true;
            }
            return false;
        }

        public void addProvider(MediaRouteProvider mediaRouteProvider) {
            if (findProviderInfo(mediaRouteProvider) == null) {
                ProviderInfo providerInfo = new ProviderInfo(mediaRouteProvider);
                this.mProviders.add(providerInfo);
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Provider added: " + providerInfo);
                }
                this.mCallbackHandler.post(513, providerInfo);
                updateProviderContents(providerInfo, mediaRouteProvider.getDescriptor());
                mediaRouteProvider.setCallback(this.mProviderCallback);
                mediaRouteProvider.setDiscoveryRequest(this.mDiscoveryRequest);
            }
        }

        public void removeProvider(MediaRouteProvider mediaRouteProvider) {
            ProviderInfo findProviderInfo = findProviderInfo(mediaRouteProvider);
            if (findProviderInfo != null) {
                mediaRouteProvider.setCallback((MediaRouteProvider.Callback) null);
                mediaRouteProvider.setDiscoveryRequest((MediaRouteDiscoveryRequest) null);
                updateProviderContents(findProviderInfo, (MediaRouteProviderDescriptor) null);
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Provider removed: " + findProviderInfo);
                }
                this.mCallbackHandler.post(514, findProviderInfo);
                this.mProviders.remove(findProviderInfo);
            }
        }

        public void releaseProviderController(RegisteredMediaRouteProvider registeredMediaRouteProvider, MediaRouteProvider.RouteController routeController) {
            if (this.mSelectedRouteController == routeController) {
                selectRoute(chooseFallbackRoute(), 2);
            }
        }

        public void updateProviderDescriptor(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            ProviderInfo findProviderInfo = findProviderInfo(mediaRouteProvider);
            if (findProviderInfo != null) {
                updateProviderContents(findProviderInfo, mediaRouteProviderDescriptor);
            }
        }

        public final ProviderInfo findProviderInfo(MediaRouteProvider mediaRouteProvider) {
            int size = this.mProviders.size();
            for (int i = 0; i < size; i++) {
                if (this.mProviders.get(i).mProviderInstance == mediaRouteProvider) {
                    return this.mProviders.get(i);
                }
            }
            return null;
        }

        public final void updateProviderContents(ProviderInfo providerInfo, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            boolean z;
            if (providerInfo.updateDescriptor(mediaRouteProviderDescriptor)) {
                int i = 0;
                if (mediaRouteProviderDescriptor == null || (!mediaRouteProviderDescriptor.isValid() && mediaRouteProviderDescriptor != this.mSystemProvider.getDescriptor())) {
                    Log.w("MediaRouter", "Ignoring invalid provider descriptor: " + mediaRouteProviderDescriptor);
                    z = false;
                } else {
                    List<MediaRouteDescriptor> routes = mediaRouteProviderDescriptor.getRoutes();
                    ArrayList<Pair> arrayList = new ArrayList<>();
                    ArrayList<Pair> arrayList2 = new ArrayList<>();
                    z = false;
                    for (MediaRouteDescriptor next : routes) {
                        if (next == null || !next.isValid()) {
                            Log.w("MediaRouter", "Ignoring invalid system route descriptor: " + next);
                        } else {
                            String id = next.getId();
                            int findRouteIndexByDescriptorId = providerInfo.findRouteIndexByDescriptorId(id);
                            if (findRouteIndexByDescriptorId < 0) {
                                RouteInfo routeInfo = new RouteInfo(providerInfo, id, assignRouteUniqueId(providerInfo, id));
                                int i2 = i + 1;
                                providerInfo.mRoutes.add(i, routeInfo);
                                this.mRoutes.add(routeInfo);
                                if (next.getGroupMemberIds().size() > 0) {
                                    arrayList.add(new Pair(routeInfo, next));
                                } else {
                                    routeInfo.maybeUpdateDescriptor(next);
                                    if (MediaRouter.DEBUG) {
                                        Log.d("MediaRouter", "Route added: " + routeInfo);
                                    }
                                    this.mCallbackHandler.post(257, routeInfo);
                                }
                                i = i2;
                            } else if (findRouteIndexByDescriptorId < i) {
                                Log.w("MediaRouter", "Ignoring route descriptor with duplicate id: " + next);
                            } else {
                                RouteInfo routeInfo2 = providerInfo.mRoutes.get(findRouteIndexByDescriptorId);
                                int i3 = i + 1;
                                Collections.swap(providerInfo.mRoutes, findRouteIndexByDescriptorId, i);
                                if (next.getGroupMemberIds().size() > 0) {
                                    arrayList2.add(new Pair(routeInfo2, next));
                                } else if (updateRouteDescriptorAndNotify(routeInfo2, next) != 0 && routeInfo2 == this.mSelectedRoute) {
                                    z = true;
                                }
                                i = i3;
                            }
                        }
                    }
                    for (Pair pair : arrayList) {
                        RouteInfo routeInfo3 = (RouteInfo) pair.first;
                        routeInfo3.maybeUpdateDescriptor((MediaRouteDescriptor) pair.second);
                        if (MediaRouter.DEBUG) {
                            Log.d("MediaRouter", "Route added: " + routeInfo3);
                        }
                        this.mCallbackHandler.post(257, routeInfo3);
                    }
                    for (Pair pair2 : arrayList2) {
                        RouteInfo routeInfo4 = (RouteInfo) pair2.first;
                        if (updateRouteDescriptorAndNotify(routeInfo4, (MediaRouteDescriptor) pair2.second) != 0 && routeInfo4 == this.mSelectedRoute) {
                            z = true;
                        }
                    }
                }
                for (int size = providerInfo.mRoutes.size() - 1; size >= i; size--) {
                    RouteInfo routeInfo5 = providerInfo.mRoutes.get(size);
                    routeInfo5.maybeUpdateDescriptor((MediaRouteDescriptor) null);
                    this.mRoutes.remove(routeInfo5);
                }
                updateSelectedRouteIfNeeded(z);
                for (int size2 = providerInfo.mRoutes.size() - 1; size2 >= i; size2--) {
                    RouteInfo remove = providerInfo.mRoutes.remove(size2);
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route removed: " + remove);
                    }
                    this.mCallbackHandler.post(258, remove);
                }
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Provider changed: " + providerInfo);
                }
                this.mCallbackHandler.post(515, providerInfo);
            }
        }

        public int updateRouteDescriptorAndNotify(RouteInfo routeInfo, MediaRouteDescriptor mediaRouteDescriptor) {
            int maybeUpdateDescriptor = routeInfo.maybeUpdateDescriptor(mediaRouteDescriptor);
            if (maybeUpdateDescriptor != 0) {
                if ((maybeUpdateDescriptor & 1) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(259, routeInfo);
                }
                if ((maybeUpdateDescriptor & 2) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route volume changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(260, routeInfo);
                }
                if ((maybeUpdateDescriptor & 4) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route presentation display changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(261, routeInfo);
                }
            }
            return maybeUpdateDescriptor;
        }

        public String assignRouteUniqueId(ProviderInfo providerInfo, String str) {
            String flattenToShortString = providerInfo.getComponentName().flattenToShortString();
            String str2 = flattenToShortString + ":" + str;
            if (findRouteByUniqueId(str2) < 0) {
                this.mUniqueIdMap.put(new Pair(flattenToShortString, str), str2);
                return str2;
            }
            Log.w("MediaRouter", "Either " + str + " isn't unique in " + flattenToShortString + " or we're trying to assign a unique ID for an already added route");
            int i = 2;
            while (true) {
                String format = String.format(Locale.US, "%s_%d", new Object[]{str2, Integer.valueOf(i)});
                if (findRouteByUniqueId(format) < 0) {
                    this.mUniqueIdMap.put(new Pair(flattenToShortString, str), format);
                    return format;
                }
                i++;
            }
        }

        public final int findRouteByUniqueId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (this.mRoutes.get(i).mUniqueId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public String getUniqueId(ProviderInfo providerInfo, String str) {
            return this.mUniqueIdMap.get(new Pair(providerInfo.getComponentName().flattenToShortString(), str));
        }

        public void updateSelectedRouteIfNeeded(boolean z) {
            RouteInfo routeInfo = this.mDefaultRoute;
            if (routeInfo != null && !routeInfo.isSelectable()) {
                Log.i("MediaRouter", "Clearing the default route because it is no longer selectable: " + this.mDefaultRoute);
                this.mDefaultRoute = null;
            }
            if (this.mDefaultRoute == null && !this.mRoutes.isEmpty()) {
                Iterator<RouteInfo> it = this.mRoutes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next = it.next();
                    if (isSystemDefaultRoute(next) && next.isSelectable()) {
                        this.mDefaultRoute = next;
                        Log.i("MediaRouter", "Found default route: " + this.mDefaultRoute);
                        break;
                    }
                }
            }
            RouteInfo routeInfo2 = this.mBluetoothRoute;
            if (routeInfo2 != null && !routeInfo2.isSelectable()) {
                Log.i("MediaRouter", "Clearing the bluetooth route because it is no longer selectable: " + this.mBluetoothRoute);
                this.mBluetoothRoute = null;
            }
            if (this.mBluetoothRoute == null && !this.mRoutes.isEmpty()) {
                Iterator<RouteInfo> it2 = this.mRoutes.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    RouteInfo next2 = it2.next();
                    if (isSystemLiveAudioOnlyRoute(next2) && next2.isSelectable()) {
                        this.mBluetoothRoute = next2;
                        Log.i("MediaRouter", "Found bluetooth route: " + this.mBluetoothRoute);
                        break;
                    }
                }
            }
            RouteInfo routeInfo3 = this.mSelectedRoute;
            if (routeInfo3 == null || !routeInfo3.isEnabled()) {
                Log.i("MediaRouter", "Unselecting the current route because it is no longer selectable: " + this.mSelectedRoute);
                selectRouteInternal(chooseFallbackRoute(), 0);
            } else if (z) {
                maybeUpdateMemberRouteControllers();
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        public RouteInfo chooseFallbackRoute() {
            Iterator<RouteInfo> it = this.mRoutes.iterator();
            while (it.hasNext()) {
                RouteInfo next = it.next();
                if (next != this.mDefaultRoute && isSystemLiveAudioOnlyRoute(next) && next.isSelectable()) {
                    return next;
                }
            }
            return this.mDefaultRoute;
        }

        public final boolean isSystemLiveAudioOnlyRoute(RouteInfo routeInfo) {
            return routeInfo.getProviderInstance() == this.mSystemProvider && routeInfo.supportsControlCategory("android.media.intent.category.LIVE_AUDIO") && !routeInfo.supportsControlCategory("android.media.intent.category.LIVE_VIDEO");
        }

        public final boolean isSystemDefaultRoute(RouteInfo routeInfo) {
            return routeInfo.getProviderInstance() == this.mSystemProvider && routeInfo.mDescriptorId.equals("DEFAULT_ROUTE");
        }

        public void selectRouteInternal(RouteInfo routeInfo, int i) {
            if (MediaRouter.sGlobal == null || (this.mBluetoothRoute != null && routeInfo.isDefault())) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                StringBuilder sb = new StringBuilder();
                for (int i2 = 3; i2 < stackTrace.length; i2++) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    sb.append(stackTraceElement.getClassName());
                    sb.append(".");
                    sb.append(stackTraceElement.getMethodName());
                    sb.append(":");
                    sb.append(stackTraceElement.getLineNumber());
                    sb.append("  ");
                }
                if (MediaRouter.sGlobal == null) {
                    Log.w("MediaRouter", "setSelectedRouteInternal is called while sGlobal is null: pkgName=" + this.mApplicationContext.getPackageName() + ", callers=" + sb.toString());
                } else {
                    Log.w("MediaRouter", "Default route is selected while a BT route is available: pkgName=" + this.mApplicationContext.getPackageName() + ", callers=" + sb.toString());
                }
            }
            if (this.mSelectedRoute != routeInfo) {
                if (this.mRequestedRoute != null) {
                    this.mRequestedRoute = null;
                    MediaRouteProvider.RouteController routeController = this.mRequestedRouteController;
                    if (routeController != null) {
                        routeController.onUnselect(3);
                        this.mRequestedRouteController.onRelease();
                        this.mRequestedRouteController = null;
                    }
                }
                if (isMediaTransferEnabled() && routeInfo.getProvider().supportsDynamicGroup()) {
                    MediaRouteProvider.DynamicGroupRouteController onCreateDynamicGroupRouteController = routeInfo.getProviderInstance().onCreateDynamicGroupRouteController(routeInfo.mDescriptorId);
                    if (onCreateDynamicGroupRouteController != null) {
                        onCreateDynamicGroupRouteController.setOnDynamicRoutesChangedListener(ContextCompat.getMainExecutor(this.mApplicationContext), this.mDynamicRoutesListener);
                        this.mRequestedRoute = routeInfo;
                        this.mRequestedRouteController = onCreateDynamicGroupRouteController;
                        onCreateDynamicGroupRouteController.onSelect();
                        return;
                    }
                    Log.w("MediaRouter", "setSelectedRouteInternal: Failed to create dynamic group route controller. route=" + routeInfo);
                }
                MediaRouteProvider.RouteController onCreateRouteController = routeInfo.getProviderInstance().onCreateRouteController(routeInfo.mDescriptorId);
                if (onCreateRouteController != null) {
                    onCreateRouteController.onSelect();
                }
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Route selected: " + routeInfo);
                }
                if (this.mSelectedRoute == null) {
                    this.mSelectedRoute = routeInfo;
                    this.mSelectedRouteController = onCreateRouteController;
                    this.mCallbackHandler.post(262, new Pair(null, routeInfo), i);
                    return;
                }
                notifyTransfer(this, routeInfo, onCreateRouteController, i, (RouteInfo) null, (Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor>) null);
            }
        }

        public void maybeUpdateMemberRouteControllers() {
            if (this.mSelectedRoute.isGroup()) {
                List<RouteInfo> memberRoutes = this.mSelectedRoute.getMemberRoutes();
                HashSet hashSet = new HashSet();
                for (RouteInfo routeInfo : memberRoutes) {
                    hashSet.add(routeInfo.mUniqueId);
                }
                Iterator<Map.Entry<String, MediaRouteProvider.RouteController>> it = this.mRouteControllerMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry next = it.next();
                    if (!hashSet.contains(next.getKey())) {
                        MediaRouteProvider.RouteController routeController = (MediaRouteProvider.RouteController) next.getValue();
                        routeController.onUnselect(0);
                        routeController.onRelease();
                        it.remove();
                    }
                }
                for (RouteInfo next2 : memberRoutes) {
                    if (!this.mRouteControllerMap.containsKey(next2.mUniqueId)) {
                        MediaRouteProvider.RouteController onCreateRouteController = next2.getProviderInstance().onCreateRouteController(next2.mDescriptorId, this.mSelectedRoute.mDescriptorId);
                        onCreateRouteController.onSelect();
                        this.mRouteControllerMap.put(next2.mUniqueId, onCreateRouteController);
                    }
                }
            }
        }

        public void notifyTransfer(GlobalMediaRouter globalMediaRouter, RouteInfo routeInfo, MediaRouteProvider.RouteController routeController, int i, RouteInfo routeInfo2, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            PrepareTransferNotifier prepareTransferNotifier = this.mTransferNotifier;
            if (prepareTransferNotifier != null) {
                prepareTransferNotifier.cancel();
                this.mTransferNotifier = null;
            }
            PrepareTransferNotifier prepareTransferNotifier2 = new PrepareTransferNotifier(globalMediaRouter, routeInfo, routeController, i, routeInfo2, collection);
            this.mTransferNotifier = prepareTransferNotifier2;
            int i2 = prepareTransferNotifier2.mReason;
            prepareTransferNotifier2.finishTransfer();
        }

        public void onSystemRouteSelectedByDescriptorId(String str) {
            RouteInfo findRouteByDescriptorId;
            this.mCallbackHandler.removeMessages(262);
            ProviderInfo findProviderInfo = findProviderInfo(this.mSystemProvider);
            if (findProviderInfo != null && (findRouteByDescriptorId = findProviderInfo.findRouteByDescriptorId(str)) != null) {
                findRouteByDescriptorId.select();
            }
        }

        public MediaSessionCompat.Token getMediaSessionToken() {
            MediaSessionCompat mediaSessionCompat = this.mCompatSession;
            if (mediaSessionCompat != null) {
                return mediaSessionCompat.getSessionToken();
            }
            return null;
        }

        @SuppressLint({"NewApi"})
        public void updatePlaybackInfoFromSelectedRoute() {
            RouteInfo routeInfo = this.mSelectedRoute;
            if (routeInfo != null) {
                this.mPlaybackInfo.volume = routeInfo.getVolume();
                this.mPlaybackInfo.volumeMax = this.mSelectedRoute.getVolumeMax();
                this.mPlaybackInfo.volumeHandling = this.mSelectedRoute.getVolumeHandling();
                this.mPlaybackInfo.playbackStream = this.mSelectedRoute.getPlaybackStream();
                this.mPlaybackInfo.playbackType = this.mSelectedRoute.getPlaybackType();
                if (!isMediaTransferEnabled() || this.mSelectedRoute.getProviderInstance() != this.mMr2Provider) {
                    this.mPlaybackInfo.volumeControlId = null;
                } else {
                    this.mPlaybackInfo.volumeControlId = MediaRoute2Provider.getSessionIdForRouteController(this.mSelectedRouteController);
                }
                int size = this.mRemoteControlClients.size();
                for (int i = 0; i < size; i++) {
                    this.mRemoteControlClients.get(i).updatePlaybackInfo();
                }
            }
        }

        public final class ProviderCallback extends MediaRouteProvider.Callback {
            public ProviderCallback() {
            }

            public void onDescriptorChanged(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
                GlobalMediaRouter.this.updateProviderDescriptor(mediaRouteProvider, mediaRouteProviderDescriptor);
            }
        }

        public final class Mr2ProviderCallback extends MediaRoute2Provider.Callback {
            public Mr2ProviderCallback() {
            }

            public void onSelectRoute(String str, int i) {
                RouteInfo routeInfo;
                Iterator<RouteInfo> it = GlobalMediaRouter.this.getRoutes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        routeInfo = null;
                        break;
                    }
                    routeInfo = it.next();
                    if (routeInfo.getProviderInstance() == GlobalMediaRouter.this.mMr2Provider && TextUtils.equals(str, routeInfo.getDescriptorId())) {
                        break;
                    }
                }
                if (routeInfo == null) {
                    Log.w("MediaRouter", "onSelectRoute: The target RouteInfo is not found for descriptorId=" + str);
                    return;
                }
                GlobalMediaRouter.this.selectRouteInternal(routeInfo, i);
            }

            public void onSelectFallbackRoute(int i) {
                selectRouteToFallbackRoute(i);
            }

            public void onReleaseController(MediaRouteProvider.RouteController routeController) {
                if (routeController == GlobalMediaRouter.this.mSelectedRouteController) {
                    selectRouteToFallbackRoute(2);
                } else if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "A RouteController unrelated to the selected route is released. controller=" + routeController);
                }
            }

            public void selectRouteToFallbackRoute(int i) {
                RouteInfo chooseFallbackRoute = GlobalMediaRouter.this.chooseFallbackRoute();
                if (GlobalMediaRouter.this.getSelectedRoute() != chooseFallbackRoute) {
                    GlobalMediaRouter.this.selectRouteInternal(chooseFallbackRoute, i);
                }
            }
        }

        public final class RemoteControlClientRecord {
            public final /* synthetic */ GlobalMediaRouter this$0;

            public void updatePlaybackInfo() {
                RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo = this.this$0.mPlaybackInfo;
                throw null;
            }
        }

        public final class CallbackHandler extends Handler {
            public final List<RouteInfo> mDynamicGroupRoutes = new ArrayList();
            public final ArrayList<CallbackRecord> mTempCallbackRecords = new ArrayList<>();

            public CallbackHandler() {
            }

            public void post(int i, Object obj) {
                obtainMessage(i, obj).sendToTarget();
            }

            public void post(int i, Object obj, int i2) {
                Message obtainMessage = obtainMessage(i, obj);
                obtainMessage.arg1 = i2;
                obtainMessage.sendToTarget();
            }

            public void handleMessage(Message message) {
                int i = message.what;
                Object obj = message.obj;
                int i2 = message.arg1;
                if (i == 259 && GlobalMediaRouter.this.getSelectedRoute().getId().equals(((RouteInfo) obj).getId())) {
                    GlobalMediaRouter.this.updateSelectedRouteIfNeeded(true);
                }
                syncWithSystemProvider(i, obj);
                try {
                    int size = GlobalMediaRouter.this.mRouters.size();
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        MediaRouter mediaRouter = (MediaRouter) GlobalMediaRouter.this.mRouters.get(size).get();
                        if (mediaRouter == null) {
                            GlobalMediaRouter.this.mRouters.remove(size);
                        } else {
                            this.mTempCallbackRecords.addAll(mediaRouter.mCallbackRecords);
                        }
                    }
                    int size2 = this.mTempCallbackRecords.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        invokeCallback(this.mTempCallbackRecords.get(i3), i, obj, i2);
                    }
                } finally {
                    this.mTempCallbackRecords.clear();
                }
            }

            public final void syncWithSystemProvider(int i, Object obj) {
                if (i == 262) {
                    RouteInfo routeInfo = (RouteInfo) ((Pair) obj).second;
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteSelected(routeInfo);
                    if (GlobalMediaRouter.this.mDefaultRoute != null && routeInfo.isDefaultOrBluetooth()) {
                        for (RouteInfo onSyncRouteRemoved : this.mDynamicGroupRoutes) {
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteRemoved(onSyncRouteRemoved);
                        }
                        this.mDynamicGroupRoutes.clear();
                    }
                } else if (i != 264) {
                    switch (i) {
                        case 257:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteAdded((RouteInfo) obj);
                            return;
                        case 258:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteRemoved((RouteInfo) obj);
                            return;
                        case 259:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteChanged((RouteInfo) obj);
                            return;
                        default:
                            return;
                    }
                } else {
                    RouteInfo routeInfo2 = (RouteInfo) ((Pair) obj).second;
                    this.mDynamicGroupRoutes.add(routeInfo2);
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteAdded(routeInfo2);
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteSelected(routeInfo2);
                }
            }

            public final void invokeCallback(CallbackRecord callbackRecord, int i, Object obj, int i2) {
                RouteInfo routeInfo;
                MediaRouter mediaRouter = callbackRecord.mRouter;
                Callback callback = callbackRecord.mCallback;
                int i3 = 65280 & i;
                if (i3 == 256) {
                    RouteInfo routeInfo2 = (i == 264 || i == 262) ? (RouteInfo) ((Pair) obj).second : (RouteInfo) obj;
                    if (i == 264 || i == 262) {
                        routeInfo = (RouteInfo) ((Pair) obj).first;
                    } else {
                        routeInfo = null;
                    }
                    if (routeInfo2 != null && callbackRecord.filterRouteEvent(routeInfo2, i, routeInfo, i2)) {
                        switch (i) {
                            case 257:
                                callback.onRouteAdded(mediaRouter, routeInfo2);
                                return;
                            case 258:
                                callback.onRouteRemoved(mediaRouter, routeInfo2);
                                return;
                            case 259:
                                callback.onRouteChanged(mediaRouter, routeInfo2);
                                return;
                            case 260:
                                callback.onRouteVolumeChanged(mediaRouter, routeInfo2);
                                return;
                            case 261:
                                callback.onRoutePresentationDisplayChanged(mediaRouter, routeInfo2);
                                return;
                            case 262:
                                callback.onRouteSelected(mediaRouter, routeInfo2, i2, routeInfo2);
                                return;
                            case 263:
                                callback.onRouteUnselected(mediaRouter, routeInfo2, i2);
                                return;
                            case 264:
                                callback.onRouteSelected(mediaRouter, routeInfo2, i2, routeInfo);
                                return;
                            default:
                                return;
                        }
                    }
                } else if (i3 == 512) {
                    ProviderInfo providerInfo = (ProviderInfo) obj;
                    switch (i) {
                        case 513:
                            callback.onProviderAdded(mediaRouter, providerInfo);
                            return;
                        case 514:
                            callback.onProviderRemoved(mediaRouter, providerInfo);
                            return;
                        case 515:
                            callback.onProviderChanged(mediaRouter, providerInfo);
                            return;
                        default:
                            return;
                    }
                } else if (i3 == 768 && i == 769) {
                    callback.onRouterParamsChanged(mediaRouter, (MediaRouterParams) obj);
                }
            }
        }
    }

    public static final class PrepareTransferNotifier {
        public boolean mCanceled = false;
        public boolean mFinished = false;
        public final RouteInfo mFromRoute;
        public ListenableFuture<Void> mFuture = null;
        public final List<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> mMemberRoutes;
        public final int mReason;
        public final RouteInfo mRequestedRoute;
        public final WeakReference<GlobalMediaRouter> mRouter;
        public final RouteInfo mToRoute;
        public final MediaRouteProvider.RouteController mToRouteController;

        public PrepareTransferNotifier(GlobalMediaRouter globalMediaRouter, RouteInfo routeInfo, MediaRouteProvider.RouteController routeController, int i, RouteInfo routeInfo2, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            ArrayList arrayList = null;
            this.mRouter = new WeakReference<>(globalMediaRouter);
            this.mToRoute = routeInfo;
            this.mToRouteController = routeController;
            this.mReason = i;
            this.mFromRoute = globalMediaRouter.mSelectedRoute;
            this.mRequestedRoute = routeInfo2;
            this.mMemberRoutes = collection != null ? new ArrayList(collection) : arrayList;
            globalMediaRouter.mCallbackHandler.postDelayed(new MediaRouter$PrepareTransferNotifier$$ExternalSyntheticLambda0(this), WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS);
        }

        public void finishTransfer() {
            ListenableFuture<Void> listenableFuture;
            MediaRouter.checkCallingThread();
            if (!this.mFinished && !this.mCanceled) {
                GlobalMediaRouter globalMediaRouter = (GlobalMediaRouter) this.mRouter.get();
                if (globalMediaRouter == null || globalMediaRouter.mTransferNotifier != this || ((listenableFuture = this.mFuture) != null && listenableFuture.isCancelled())) {
                    cancel();
                    return;
                }
                this.mFinished = true;
                globalMediaRouter.mTransferNotifier = null;
                unselectFromRouteAndNotify();
                selectToRouteAndNotify();
            }
        }

        public void cancel() {
            if (!this.mFinished && !this.mCanceled) {
                this.mCanceled = true;
                MediaRouteProvider.RouteController routeController = this.mToRouteController;
                if (routeController != null) {
                    routeController.onUnselect(0);
                    this.mToRouteController.onRelease();
                }
            }
        }

        public final void unselectFromRouteAndNotify() {
            RouteInfo routeInfo;
            GlobalMediaRouter globalMediaRouter = (GlobalMediaRouter) this.mRouter.get();
            if (globalMediaRouter != null && globalMediaRouter.mSelectedRoute == (routeInfo = this.mFromRoute)) {
                globalMediaRouter.mCallbackHandler.post(263, routeInfo, this.mReason);
                MediaRouteProvider.RouteController routeController = globalMediaRouter.mSelectedRouteController;
                if (routeController != null) {
                    routeController.onUnselect(this.mReason);
                    globalMediaRouter.mSelectedRouteController.onRelease();
                }
                if (!globalMediaRouter.mRouteControllerMap.isEmpty()) {
                    for (MediaRouteProvider.RouteController next : globalMediaRouter.mRouteControllerMap.values()) {
                        next.onUnselect(this.mReason);
                        next.onRelease();
                    }
                    globalMediaRouter.mRouteControllerMap.clear();
                }
                globalMediaRouter.mSelectedRouteController = null;
            }
        }

        public final void selectToRouteAndNotify() {
            GlobalMediaRouter globalMediaRouter = (GlobalMediaRouter) this.mRouter.get();
            if (globalMediaRouter != null) {
                RouteInfo routeInfo = this.mToRoute;
                globalMediaRouter.mSelectedRoute = routeInfo;
                globalMediaRouter.mSelectedRouteController = this.mToRouteController;
                RouteInfo routeInfo2 = this.mRequestedRoute;
                if (routeInfo2 == null) {
                    globalMediaRouter.mCallbackHandler.post(262, new Pair(this.mFromRoute, routeInfo), this.mReason);
                } else {
                    globalMediaRouter.mCallbackHandler.post(264, new Pair(routeInfo2, routeInfo), this.mReason);
                }
                globalMediaRouter.mRouteControllerMap.clear();
                globalMediaRouter.maybeUpdateMemberRouteControllers();
                globalMediaRouter.updatePlaybackInfoFromSelectedRoute();
                List<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> list = this.mMemberRoutes;
                if (list != null) {
                    globalMediaRouter.mSelectedRoute.updateDynamicDescriptors(list);
                }
            }
        }
    }
}
