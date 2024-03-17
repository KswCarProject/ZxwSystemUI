package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.WifiUtils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InternetAdapter extends RecyclerView.Adapter<InternetViewHolder> {
    public Context mContext;
    public View mHolderView;
    public final InternetDialogController mInternetDialogController;
    @VisibleForTesting
    public int mMaxEntriesCount = 3;
    public List<WifiEntry> mWifiEntries;
    @VisibleForTesting
    public int mWifiEntriesCount;

    public InternetAdapter(InternetDialogController internetDialogController) {
        this.mInternetDialogController = internetDialogController;
    }

    public InternetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        this.mContext = context;
        this.mHolderView = LayoutInflater.from(context).inflate(R$layout.internet_list_item, viewGroup, false);
        return new InternetViewHolder(this.mHolderView, this.mInternetDialogController);
    }

    public void onBindViewHolder(InternetViewHolder internetViewHolder, int i) {
        List<WifiEntry> list = this.mWifiEntries;
        if (list != null && i < this.mWifiEntriesCount) {
            internetViewHolder.onBind(list.get(i));
        }
    }

    public void setWifiEntries(List<WifiEntry> list, int i) {
        this.mWifiEntries = list;
        int i2 = this.mMaxEntriesCount;
        if (i >= i2) {
            i = i2;
        }
        this.mWifiEntriesCount = i;
    }

    public int getItemCount() {
        return this.mWifiEntriesCount;
    }

    public void setMaxEntriesCount(int i) {
        if (i >= 0 && this.mMaxEntriesCount != i) {
            this.mMaxEntriesCount = i;
            if (this.mWifiEntriesCount > i) {
                this.mWifiEntriesCount = i;
                notifyDataSetChanged();
            }
        }
    }

    public static class InternetViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mContainerLayout;
        public final Context mContext;
        public final InternetDialogController mInternetDialogController;
        public final ImageView mWifiEndIcon;
        public final ImageView mWifiIcon;
        @VisibleForTesting
        public WifiUtils.InternetIconInjector mWifiIconInjector;
        public final LinearLayout mWifiListLayout;
        public final LinearLayout mWifiNetworkLayout;
        public final TextView mWifiSummaryText;
        public final TextView mWifiTitleText;

        public InternetViewHolder(View view, InternetDialogController internetDialogController) {
            super(view);
            this.mContext = view.getContext();
            this.mInternetDialogController = internetDialogController;
            this.mContainerLayout = (LinearLayout) view.requireViewById(R$id.internet_container);
            this.mWifiListLayout = (LinearLayout) view.requireViewById(R$id.wifi_list);
            this.mWifiNetworkLayout = (LinearLayout) view.requireViewById(R$id.wifi_network_layout);
            this.mWifiIcon = (ImageView) view.requireViewById(R$id.wifi_icon);
            this.mWifiTitleText = (TextView) view.requireViewById(R$id.wifi_title);
            this.mWifiSummaryText = (TextView) view.requireViewById(R$id.wifi_summary);
            this.mWifiEndIcon = (ImageView) view.requireViewById(R$id.wifi_end_icon);
            this.mWifiIconInjector = internetDialogController.getWifiIconInjector();
        }

        public void onBind(WifiEntry wifiEntry) {
            this.mWifiIcon.setImageDrawable(getWifiDrawable(wifiEntry.getLevel(), wifiEntry.shouldShowXLevelIcon()));
            setWifiNetworkLayout(wifiEntry.getTitle(), Html.fromHtml(wifiEntry.getSummary(false), 0));
            int connectedState = wifiEntry.getConnectedState();
            updateEndIcon(connectedState, wifiEntry.getSecurity());
            this.mWifiListLayout.setEnabled(shouldEnabled(wifiEntry));
            if (connectedState != 0) {
                this.mWifiListLayout.setOnClickListener(new InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda0(this, wifiEntry));
            } else {
                this.mWifiListLayout.setOnClickListener(new InternetAdapter$InternetViewHolder$$ExternalSyntheticLambda1(this, wifiEntry));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$0(WifiEntry wifiEntry, View view) {
            this.mInternetDialogController.launchWifiDetailsSetting(wifiEntry.getKey(), view);
        }

        public boolean shouldEnabled(WifiEntry wifiEntry) {
            if (!wifiEntry.canConnect() && !wifiEntry.canDisconnect() && !wifiEntry.isSaved()) {
                return false;
            }
            return true;
        }

        /* renamed from: onWifiClick */
        public void lambda$onBind$1(WifiEntry wifiEntry, View view) {
            if (wifiEntry.shouldEditBeforeConnect()) {
                Intent wifiDialogIntent = WifiUtils.getWifiDialogIntent(wifiEntry.getKey(), true);
                wifiDialogIntent.addFlags(268435456);
                wifiDialogIntent.addFlags(131072);
                this.mContext.startActivity(wifiDialogIntent);
            } else if (wifiEntry.canConnect()) {
                this.mInternetDialogController.connect(wifiEntry);
            } else if (wifiEntry.isSaved()) {
                Log.w("InternetAdapter", "The saved Wi-Fi network does not allow to connect. SSID:" + wifiEntry.getSsid());
                this.mInternetDialogController.launchWifiDetailsSetting(wifiEntry.getKey(), view);
            }
        }

        public void setWifiNetworkLayout(CharSequence charSequence, CharSequence charSequence2) {
            this.mWifiTitleText.setText(charSequence);
            if (TextUtils.isEmpty(charSequence2)) {
                this.mWifiSummaryText.setVisibility(8);
                return;
            }
            this.mWifiSummaryText.setVisibility(0);
            this.mWifiSummaryText.setText(charSequence2);
        }

        public Drawable getWifiDrawable(int i, boolean z) {
            Drawable icon;
            if (i == -1 || (icon = this.mWifiIconInjector.getIcon(z, i)) == null) {
                return null;
            }
            icon.setTint(Utils.getColorAttrDefaultColor(this.mContext, 16843282));
            AtomicReference atomicReference = new AtomicReference();
            atomicReference.set(icon);
            return (Drawable) atomicReference.get();
        }

        public void updateEndIcon(int i, int i2) {
            Drawable drawable;
            if (i != 0) {
                drawable = this.mContext.getDrawable(R$drawable.ic_settings_24dp);
            } else {
                drawable = (i2 == 0 || i2 == 4) ? null : this.mContext.getDrawable(R$drawable.ic_friction_lock_closed);
            }
            if (drawable == null) {
                this.mWifiEndIcon.setVisibility(8);
                return;
            }
            this.mWifiEndIcon.setVisibility(0);
            this.mWifiEndIcon.setImageDrawable(drawable);
        }
    }
}
