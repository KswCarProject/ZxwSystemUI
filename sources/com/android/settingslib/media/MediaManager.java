package com.android.settingslib.media;

import android.app.Notification;
import android.content.Context;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MediaManager {
    public final Collection<MediaDeviceCallback> mCallbacks = new CopyOnWriteArrayList();
    public Context mContext;
    public final List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    public Notification mNotification;

    public interface MediaDeviceCallback {
        void onConnectedDeviceChanged(String str);

        void onDeviceAttributesChanged();

        void onDeviceListAdded(List<MediaDevice> list);

        void onRequestFailed(int i);
    }

    public MediaManager(Context context, Notification notification) {
        this.mContext = context;
        this.mNotification = notification;
    }

    public void registerCallback(MediaDeviceCallback mediaDeviceCallback) {
        if (!this.mCallbacks.contains(mediaDeviceCallback)) {
            this.mCallbacks.add(mediaDeviceCallback);
        }
    }

    public void unregisterCallback(MediaDeviceCallback mediaDeviceCallback) {
        if (this.mCallbacks.contains(mediaDeviceCallback)) {
            this.mCallbacks.remove(mediaDeviceCallback);
        }
    }

    public void dispatchDeviceListAdded() {
        for (MediaDeviceCallback onDeviceListAdded : getCallbacks()) {
            onDeviceListAdded.onDeviceListAdded(new ArrayList(this.mMediaDevices));
        }
    }

    public void dispatchConnectedDeviceChanged(String str) {
        for (MediaDeviceCallback onConnectedDeviceChanged : getCallbacks()) {
            onConnectedDeviceChanged.onConnectedDeviceChanged(str);
        }
    }

    public void dispatchDataChanged() {
        for (MediaDeviceCallback onDeviceAttributesChanged : getCallbacks()) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    public void dispatchOnRequestFailed(int i) {
        for (MediaDeviceCallback onRequestFailed : getCallbacks()) {
            onRequestFailed.onRequestFailed(i);
        }
    }

    public final Collection<MediaDeviceCallback> getCallbacks() {
        return new CopyOnWriteArrayList(this.mCallbacks);
    }
}
