package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.systemui.R$bool;
import com.android.systemui.R$integer;
import com.android.systemui.util.DeviceConfigProxy;

public final class SmartReplyConstants {
    public final Context mContext;
    public final boolean mDefaultEditChoicesBeforeSending;
    public final boolean mDefaultEnabled;
    public final int mDefaultMaxNumActions;
    public final int mDefaultMaxSqueezeRemeasureAttempts;
    public final int mDefaultMinNumSystemGeneratedReplies;
    public final int mDefaultOnClickInitDelay;
    public final boolean mDefaultRequiresP;
    public final boolean mDefaultShowInHeadsUp;
    public final DeviceConfigProxy mDeviceConfig;
    public volatile boolean mEditChoicesBeforeSending;
    public volatile boolean mEnabled;
    public final Handler mHandler;
    public volatile int mMaxNumActions;
    public volatile int mMaxSqueezeRemeasureAttempts;
    public volatile int mMinNumSystemGeneratedReplies;
    public volatile long mOnClickInitDelay;
    public final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() {
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (!"systemui".equals(properties.getNamespace())) {
                Log.e("SmartReplyConstants", "Received update from DeviceConfig for unrelated namespace: " + properties.getNamespace());
                return;
            }
            SmartReplyConstants.this.updateConstants();
        }
    };
    public final KeyValueListParser mParser = new KeyValueListParser(',');
    public volatile boolean mRequiresTargetingP;
    public volatile boolean mShowInHeadsUp;

    public SmartReplyConstants(Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        this.mHandler = handler;
        this.mContext = context;
        Resources resources = context.getResources();
        this.mDefaultEnabled = resources.getBoolean(R$bool.config_smart_replies_in_notifications_enabled);
        this.mDefaultRequiresP = resources.getBoolean(R$bool.config_smart_replies_in_notifications_requires_targeting_p);
        this.mDefaultMaxSqueezeRemeasureAttempts = resources.getInteger(R$integer.config_smart_replies_in_notifications_max_squeeze_remeasure_attempts);
        this.mDefaultEditChoicesBeforeSending = resources.getBoolean(R$bool.config_smart_replies_in_notifications_edit_choices_before_sending);
        this.mDefaultShowInHeadsUp = resources.getBoolean(R$bool.config_smart_replies_in_notifications_show_in_heads_up);
        this.mDefaultMinNumSystemGeneratedReplies = resources.getInteger(R$integer.config_smart_replies_in_notifications_min_num_system_generated_replies);
        this.mDefaultMaxNumActions = resources.getInteger(R$integer.config_smart_replies_in_notifications_max_num_actions);
        this.mDefaultOnClickInitDelay = resources.getInteger(R$integer.config_smart_replies_in_notifications_onclick_init_delay);
        this.mDeviceConfig = deviceConfigProxy;
        registerDeviceConfigListener();
        updateConstants();
    }

    public final void registerDeviceConfigListener() {
        this.mDeviceConfig.addOnPropertiesChangedListener("systemui", new SmartReplyConstants$$ExternalSyntheticLambda0(this), this.mOnPropertiesChangedListener);
    }

    public final void postToHandler(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public final void updateConstants() {
        synchronized (this) {
            this.mEnabled = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_enabled", this.mDefaultEnabled);
            this.mRequiresTargetingP = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_requires_targeting_p", this.mDefaultRequiresP);
            this.mMaxSqueezeRemeasureAttempts = this.mDeviceConfig.getInt("systemui", "ssin_max_squeeze_remeasure_attempts", this.mDefaultMaxSqueezeRemeasureAttempts);
            this.mEditChoicesBeforeSending = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_edit_choices_before_sending", this.mDefaultEditChoicesBeforeSending);
            this.mShowInHeadsUp = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_show_in_heads_up", this.mDefaultShowInHeadsUp);
            this.mMinNumSystemGeneratedReplies = this.mDeviceConfig.getInt("systemui", "ssin_min_num_system_generated_replies", this.mDefaultMinNumSystemGeneratedReplies);
            this.mMaxNumActions = this.mDeviceConfig.getInt("systemui", "ssin_max_num_actions", this.mDefaultMaxNumActions);
            this.mOnClickInitDelay = (long) this.mDeviceConfig.getInt("systemui", "ssin_onclick_init_delay", this.mDefaultOnClickInitDelay);
        }
    }

    public final boolean readDeviceConfigBooleanOrDefaultIfEmpty(String str, boolean z) {
        String property = this.mDeviceConfig.getProperty("systemui", str);
        if (TextUtils.isEmpty(property)) {
            return z;
        }
        if ("true".equals(property)) {
            return true;
        }
        if ("false".equals(property)) {
            return false;
        }
        return z;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean requiresTargetingP() {
        return this.mRequiresTargetingP;
    }

    public int getMaxSqueezeRemeasureAttempts() {
        return this.mMaxSqueezeRemeasureAttempts;
    }

    public boolean getEffectiveEditChoicesBeforeSending(int i) {
        if (i == 1) {
            return false;
        }
        if (i != 2) {
            return this.mEditChoicesBeforeSending;
        }
        return true;
    }

    public boolean getShowInHeadsUp() {
        return this.mShowInHeadsUp;
    }

    public int getMinNumSystemGeneratedReplies() {
        return this.mMinNumSystemGeneratedReplies;
    }

    public int getMaxNumActions() {
        return this.mMaxNumActions;
    }

    public long getOnClickInitDelay() {
        return this.mOnClickInitDelay;
    }
}
