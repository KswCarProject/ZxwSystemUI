package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.settingslib.R$drawable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceIconUtil {
    public static final int DEFAULT_ICON = R$drawable.ic_smartphone;
    public final Map<Integer, Device> mAudioDeviceTypeToIconMap = new HashMap();
    public final Map<Integer, Device> mMediaRouteTypeToIconMap = new HashMap();

    public DeviceIconUtil() {
        int i = R$drawable.ic_headphone;
        List asList = Arrays.asList(new Device[]{new Device(11, 11, i), new Device(22, 22, i), new Device(12, 12, i), new Device(13, 13, i), new Device(9, 9, i), new Device(3, 3, i), new Device(4, 4, i), new Device(2, 2, R$drawable.ic_smartphone)});
        for (int i2 = 0; i2 < asList.size(); i2++) {
            Device device = (Device) asList.get(i2);
            this.mAudioDeviceTypeToIconMap.put(Integer.valueOf(device.mAudioDeviceType), device);
            this.mMediaRouteTypeToIconMap.put(Integer.valueOf(device.mMediaRouteType), device);
        }
    }

    public Drawable getIconFromAudioDeviceType(int i, Context context) {
        return context.getDrawable(getIconResIdFromAudioDeviceType(i));
    }

    public int getIconResIdFromAudioDeviceType(int i) {
        if (this.mAudioDeviceTypeToIconMap.containsKey(Integer.valueOf(i))) {
            return this.mAudioDeviceTypeToIconMap.get(Integer.valueOf(i)).mIconDrawableRes;
        }
        return DEFAULT_ICON;
    }

    public int getIconResIdFromMediaRouteType(int i) {
        if (this.mMediaRouteTypeToIconMap.containsKey(Integer.valueOf(i))) {
            return this.mMediaRouteTypeToIconMap.get(Integer.valueOf(i)).mIconDrawableRes;
        }
        return DEFAULT_ICON;
    }

    public static class Device {
        public final int mAudioDeviceType;
        public final int mIconDrawableRes;
        public final int mMediaRouteType;

        public Device(int i, int i2, int i3) {
            this.mAudioDeviceType = i;
            this.mMediaRouteType = i2;
            this.mIconDrawableRes = i3;
        }
    }
}
