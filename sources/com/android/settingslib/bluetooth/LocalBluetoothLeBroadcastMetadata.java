package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudioCodecConfigMetadata;
import android.bluetooth.BluetoothLeAudioContentMetadata;
import android.bluetooth.BluetoothLeBroadcastChannel;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothLeBroadcastSubgroup;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class LocalBluetoothLeBroadcastMetadata {
    public byte[] mBroadcastCode;
    public int mBroadcastId;
    public boolean mIsEncrypted;
    public int mPaSyncInterval;
    public int mPresentationDelayMicros;
    public int mSourceAddressType;
    public int mSourceAdvertisingSid;
    public BluetoothDevice mSourceDevice;
    public List<BluetoothLeBroadcastSubgroup> mSubgroupList;

    public LocalBluetoothLeBroadcastMetadata(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
        this.mSourceAddressType = bluetoothLeBroadcastMetadata.getSourceAddressType();
        this.mSourceDevice = bluetoothLeBroadcastMetadata.getSourceDevice();
        this.mSourceAdvertisingSid = bluetoothLeBroadcastMetadata.getSourceAdvertisingSid();
        this.mBroadcastId = bluetoothLeBroadcastMetadata.getBroadcastId();
        this.mPaSyncInterval = bluetoothLeBroadcastMetadata.getPaSyncInterval();
        this.mIsEncrypted = bluetoothLeBroadcastMetadata.isEncrypted();
        this.mBroadcastCode = bluetoothLeBroadcastMetadata.getBroadcastCode();
        this.mPresentationDelayMicros = bluetoothLeBroadcastMetadata.getPresentationDelayMicros();
        this.mSubgroupList = bluetoothLeBroadcastMetadata.getSubgroups();
    }

    public String convertToQrCodeString() {
        String convertSubgroupToString = convertSubgroupToString(this.mSubgroupList);
        return "BT:" + "T:" + "<" + this.mSourceAddressType + ">" + ";" + "D:" + "<" + this.mSourceDevice + ">" + ";" + "AS:" + "<" + this.mSourceAdvertisingSid + ">" + ";" + "B:" + "<" + this.mBroadcastId + ">" + ";" + "SI:" + "<" + this.mPaSyncInterval + ">" + ";" + "E:" + "<" + this.mIsEncrypted + ">" + ";" + "C:" + "<" + Arrays.toString(this.mBroadcastCode) + ">" + ";" + "D:" + "<" + this.mPresentationDelayMicros + ">" + ";" + "G:" + "<" + convertSubgroupToString + ">" + ";";
    }

    public final String convertSubgroupToString(List<BluetoothLeBroadcastSubgroup> list) {
        StringBuilder sb = new StringBuilder();
        for (BluetoothLeBroadcastSubgroup next : list) {
            String convertAudioCodecConfigToString = convertAudioCodecConfigToString(next.getCodecSpecificConfig());
            String convertAudioContentToString = convertAudioContentToString(next.getContentMetadata());
            String convertChannelToString = convertChannelToString(next.getChannels());
            sb.append("CID:" + "<" + next.getCodecId() + ">" + ";" + "CC:" + "<" + convertAudioCodecConfigToString + ">" + ";" + "AC:" + "<" + convertAudioContentToString + ">" + ";" + "BC:" + "<" + convertChannelToString + ">" + ";");
        }
        return sb.toString();
    }

    public final String convertAudioCodecConfigToString(BluetoothLeAudioCodecConfigMetadata bluetoothLeAudioCodecConfigMetadata) {
        String valueOf = String.valueOf(bluetoothLeAudioCodecConfigMetadata.getAudioLocation());
        String str = new String(bluetoothLeAudioCodecConfigMetadata.getRawMetadata(), StandardCharsets.UTF_8);
        return "AL:" + "<" + valueOf + ">" + ";" + "CCRM:" + "<" + str + ">" + ";";
    }

    public final String convertAudioContentToString(BluetoothLeAudioContentMetadata bluetoothLeAudioContentMetadata) {
        String str = new String(bluetoothLeAudioContentMetadata.getRawMetadata(), StandardCharsets.UTF_8);
        return "PI:" + "<" + bluetoothLeAudioContentMetadata.getProgramInfo() + ">" + ";" + "L:" + "<" + bluetoothLeAudioContentMetadata.getLanguage() + ">" + ";" + "ACRM:" + "<" + str + ">" + ";";
    }

    public final String convertChannelToString(List<BluetoothLeBroadcastChannel> list) {
        StringBuilder sb = new StringBuilder();
        for (BluetoothLeBroadcastChannel next : list) {
            String convertAudioCodecConfigToString = convertAudioCodecConfigToString(next.getCodecMetadata());
            sb.append("CI:" + "<" + next.getChannelIndex() + ">" + ";" + "BCCM:" + "<" + convertAudioCodecConfigToString + ">" + ";");
        }
        return sb.toString();
    }
}
