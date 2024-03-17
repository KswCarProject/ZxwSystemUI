package com.android.systemui.dagger;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import com.android.systemui.recents.RecentsImplementation;
import java.util.Map;
import javax.inject.Provider;

public class ContextComponentResolver implements ContextComponentHelper {
    public final Map<Class<?>, Provider<Activity>> mActivityCreators;
    public final Map<Class<?>, Provider<BroadcastReceiver>> mBroadcastReceiverCreators;
    public final Map<Class<?>, Provider<RecentsImplementation>> mRecentsCreators;
    public final Map<Class<?>, Provider<Service>> mServiceCreators;

    public ContextComponentResolver(Map<Class<?>, Provider<Activity>> map, Map<Class<?>, Provider<Service>> map2, Map<Class<?>, Provider<RecentsImplementation>> map3, Map<Class<?>, Provider<BroadcastReceiver>> map4) {
        this.mActivityCreators = map;
        this.mServiceCreators = map2;
        this.mRecentsCreators = map3;
        this.mBroadcastReceiverCreators = map4;
    }

    public Activity resolveActivity(String str) {
        return (Activity) resolve(str, this.mActivityCreators);
    }

    public BroadcastReceiver resolveBroadcastReceiver(String str) {
        return (BroadcastReceiver) resolve(str, this.mBroadcastReceiverCreators);
    }

    public RecentsImplementation resolveRecents(String str) {
        return (RecentsImplementation) resolve(str, this.mRecentsCreators);
    }

    public Service resolveService(String str) {
        return (Service) resolve(str, this.mServiceCreators);
    }

    public final <T> T resolve(String str, Map<Class<?>, Provider<T>> map) {
        try {
            Provider provider = map.get(Class.forName(str));
            if (provider == null) {
                return null;
            }
            return provider.get();
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }
}
