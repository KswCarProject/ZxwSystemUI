package com.android.systemui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.Set;

public class CarrierConfigTracker extends BroadcastReceiver implements CallbackController<CarrierConfigChangedListener> {
    public final SparseBooleanArray mCallStrengthConfigs = new SparseBooleanArray();
    public final CarrierConfigManager mCarrierConfigManager;
    public final SparseBooleanArray mCarrierProvisionsWifiMergedNetworks = new SparseBooleanArray();
    public final Set<DefaultDataSubscriptionChangedListener> mDataListeners = new ArraySet();
    public boolean mDefaultCallStrengthConfig;
    public boolean mDefaultCallStrengthConfigLoaded;
    public boolean mDefaultCarrierProvisionsWifiMergedNetworks;
    public boolean mDefaultCarrierProvisionsWifiMergedNetworksLoaded;
    public boolean mDefaultNoCallingConfig;
    public boolean mDefaultNoCallingConfigLoaded;
    public boolean mDefaultShowOperatorNameConfig;
    public boolean mDefaultShowOperatorNameConfigLoaded;
    public final Set<CarrierConfigChangedListener> mListeners = new ArraySet();
    public final SparseBooleanArray mNoCallingConfigs = new SparseBooleanArray();
    public final SparseBooleanArray mShowOperatorNameConfigs = new SparseBooleanArray();

    public interface CarrierConfigChangedListener {
        void onCarrierConfigChanged();
    }

    public interface DefaultDataSubscriptionChangedListener {
        void onDefaultSubscriptionChanged(int i);
    }

    public CarrierConfigTracker(CarrierConfigManager carrierConfigManager, BroadcastDispatcher broadcastDispatcher) {
        this.mCarrierConfigManager = carrierConfigManager;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        broadcastDispatcher.registerReceiver(this, intentFilter);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
            updateFromNewCarrierConfig(intent);
        } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
            updateDefaultDataSubscription(intent);
        }
    }

    public final void updateFromNewCarrierConfig(Intent intent) {
        PersistableBundle configForSubId;
        int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
        if (SubscriptionManager.isValidSubscriptionId(intExtra) && (configForSubId = this.mCarrierConfigManager.getConfigForSubId(intExtra)) != null) {
            synchronized (this.mCallStrengthConfigs) {
                this.mCallStrengthConfigs.put(intExtra, configForSubId.getBoolean("display_call_strength_indicator_bool"));
            }
            synchronized (this.mNoCallingConfigs) {
                this.mNoCallingConfigs.put(intExtra, configForSubId.getBoolean("use_ip_for_calling_indicator_bool"));
            }
            synchronized (this.mCarrierProvisionsWifiMergedNetworks) {
                this.mCarrierProvisionsWifiMergedNetworks.put(intExtra, configForSubId.getBoolean("carrier_provisions_wifi_merged_networks_bool"));
            }
            synchronized (this.mShowOperatorNameConfigs) {
                this.mShowOperatorNameConfigs.put(intExtra, configForSubId.getBoolean("show_operator_name_in_statusbar_bool"));
            }
            notifyCarrierConfigChanged();
        }
    }

    public final void updateDefaultDataSubscription(Intent intent) {
        notifyDefaultDataSubscriptionChanged(intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1));
    }

    public final void notifyCarrierConfigChanged() {
        for (CarrierConfigChangedListener onCarrierConfigChanged : this.mListeners) {
            onCarrierConfigChanged.onCarrierConfigChanged();
        }
    }

    public final void notifyDefaultDataSubscriptionChanged(int i) {
        for (DefaultDataSubscriptionChangedListener onDefaultSubscriptionChanged : this.mDataListeners) {
            onDefaultSubscriptionChanged.onDefaultSubscriptionChanged(i);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        if (r2.mDefaultCallStrengthConfigLoaded != false) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        r2.mDefaultCallStrengthConfig = android.telephony.CarrierConfigManager.getDefaultConfig().getBoolean("display_call_strength_indicator_bool");
        r2.mDefaultCallStrengthConfigLoaded = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        return r2.mDefaultCallStrengthConfig;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getCallStrengthConfig(int r3) {
        /*
            r2 = this;
            android.util.SparseBooleanArray r0 = r2.mCallStrengthConfigs
            monitor-enter(r0)
            android.util.SparseBooleanArray r1 = r2.mCallStrengthConfigs     // Catch:{ all -> 0x002a }
            int r1 = r1.indexOfKey(r3)     // Catch:{ all -> 0x002a }
            if (r1 < 0) goto L_0x0013
            android.util.SparseBooleanArray r2 = r2.mCallStrengthConfigs     // Catch:{ all -> 0x002a }
            boolean r2 = r2.get(r3)     // Catch:{ all -> 0x002a }
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            boolean r3 = r2.mDefaultCallStrengthConfigLoaded
            if (r3 != 0) goto L_0x0027
            android.os.PersistableBundle r3 = android.telephony.CarrierConfigManager.getDefaultConfig()
            java.lang.String r0 = "display_call_strength_indicator_bool"
            boolean r3 = r3.getBoolean(r0)
            r2.mDefaultCallStrengthConfig = r3
            r3 = 1
            r2.mDefaultCallStrengthConfigLoaded = r3
        L_0x0027:
            boolean r2 = r2.mDefaultCallStrengthConfig
            return r2
        L_0x002a:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.CarrierConfigTracker.getCallStrengthConfig(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        if (r2.mDefaultNoCallingConfigLoaded != false) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        r2.mDefaultNoCallingConfig = android.telephony.CarrierConfigManager.getDefaultConfig().getBoolean("use_ip_for_calling_indicator_bool");
        r2.mDefaultNoCallingConfigLoaded = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        return r2.mDefaultNoCallingConfig;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getNoCallingConfig(int r3) {
        /*
            r2 = this;
            android.util.SparseBooleanArray r0 = r2.mNoCallingConfigs
            monitor-enter(r0)
            android.util.SparseBooleanArray r1 = r2.mNoCallingConfigs     // Catch:{ all -> 0x002a }
            int r1 = r1.indexOfKey(r3)     // Catch:{ all -> 0x002a }
            if (r1 < 0) goto L_0x0013
            android.util.SparseBooleanArray r2 = r2.mNoCallingConfigs     // Catch:{ all -> 0x002a }
            boolean r2 = r2.get(r3)     // Catch:{ all -> 0x002a }
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            boolean r3 = r2.mDefaultNoCallingConfigLoaded
            if (r3 != 0) goto L_0x0027
            android.os.PersistableBundle r3 = android.telephony.CarrierConfigManager.getDefaultConfig()
            java.lang.String r0 = "use_ip_for_calling_indicator_bool"
            boolean r3 = r3.getBoolean(r0)
            r2.mDefaultNoCallingConfig = r3
            r3 = 1
            r2.mDefaultNoCallingConfigLoaded = r3
        L_0x0027:
            boolean r2 = r2.mDefaultNoCallingConfig
            return r2
        L_0x002a:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.CarrierConfigTracker.getNoCallingConfig(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        if (r2.mDefaultCarrierProvisionsWifiMergedNetworksLoaded != false) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        r2.mDefaultCarrierProvisionsWifiMergedNetworks = android.telephony.CarrierConfigManager.getDefaultConfig().getBoolean("carrier_provisions_wifi_merged_networks_bool");
        r2.mDefaultCarrierProvisionsWifiMergedNetworksLoaded = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        return r2.mDefaultCarrierProvisionsWifiMergedNetworks;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getCarrierProvisionsWifiMergedNetworksBool(int r3) {
        /*
            r2 = this;
            android.util.SparseBooleanArray r0 = r2.mCarrierProvisionsWifiMergedNetworks
            monitor-enter(r0)
            android.util.SparseBooleanArray r1 = r2.mCarrierProvisionsWifiMergedNetworks     // Catch:{ all -> 0x002a }
            int r1 = r1.indexOfKey(r3)     // Catch:{ all -> 0x002a }
            if (r1 < 0) goto L_0x0013
            android.util.SparseBooleanArray r2 = r2.mCarrierProvisionsWifiMergedNetworks     // Catch:{ all -> 0x002a }
            boolean r2 = r2.get(r3)     // Catch:{ all -> 0x002a }
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            return r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            boolean r3 = r2.mDefaultCarrierProvisionsWifiMergedNetworksLoaded
            if (r3 != 0) goto L_0x0027
            android.os.PersistableBundle r3 = android.telephony.CarrierConfigManager.getDefaultConfig()
            java.lang.String r0 = "carrier_provisions_wifi_merged_networks_bool"
            boolean r3 = r3.getBoolean(r0)
            r2.mDefaultCarrierProvisionsWifiMergedNetworks = r3
            r3 = 1
            r2.mDefaultCarrierProvisionsWifiMergedNetworksLoaded = r3
        L_0x0027:
            boolean r2 = r2.mDefaultCarrierProvisionsWifiMergedNetworks
            return r2
        L_0x002a:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002a }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.CarrierConfigTracker.getCarrierProvisionsWifiMergedNetworksBool(int):boolean");
    }

    public boolean getShowOperatorNameInStatusBarConfigDefault() {
        if (!this.mDefaultShowOperatorNameConfigLoaded) {
            this.mDefaultShowOperatorNameConfig = CarrierConfigManager.getDefaultConfig().getBoolean("show_operator_name_in_statusbar_bool");
            this.mDefaultShowOperatorNameConfigLoaded = true;
        }
        return this.mDefaultShowOperatorNameConfig;
    }

    public boolean getShowOperatorNameInStatusBarConfig(int i) {
        if (this.mShowOperatorNameConfigs.indexOfKey(i) >= 0) {
            return this.mShowOperatorNameConfigs.get(i);
        }
        return getShowOperatorNameInStatusBarConfigDefault();
    }

    public void addCallback(CarrierConfigChangedListener carrierConfigChangedListener) {
        this.mListeners.add(carrierConfigChangedListener);
    }

    public void removeCallback(CarrierConfigChangedListener carrierConfigChangedListener) {
        this.mListeners.remove(carrierConfigChangedListener);
    }

    public void addDefaultDataSubscriptionChangedListener(DefaultDataSubscriptionChangedListener defaultDataSubscriptionChangedListener) {
        this.mDataListeners.add(defaultDataSubscriptionChangedListener);
    }

    public void removeDataSubscriptionChangedListener(DefaultDataSubscriptionChangedListener defaultDataSubscriptionChangedListener) {
        this.mDataListeners.remove(defaultDataSubscriptionChangedListener);
    }
}
