package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.R$drawable;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.StatusBarMobileView;
import com.android.systemui.statusbar.StatusBarWifiView;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DemoStatusIcons extends StatusIconContainer implements DemoMode, DarkIconDispatcher.DarkReceiver {
    public int mColor;
    public boolean mDemoMode;
    public final FeatureFlags mFeatureFlags;
    public final int mIconSize;
    public final ArrayList<StatusBarMobileView> mMobileViews = new ArrayList<>();
    public final LinearLayout mStatusIcons;
    public StatusBarWifiView mWifiView;

    public DemoStatusIcons(LinearLayout linearLayout, int i, FeatureFlags featureFlags) {
        super(linearLayout.getContext());
        this.mStatusIcons = linearLayout;
        this.mIconSize = i;
        this.mColor = -1;
        this.mFeatureFlags = featureFlags;
        if (linearLayout instanceof StatusIconContainer) {
            setShouldRestrictIcons(((StatusIconContainer) linearLayout).isRestrictingIcons());
        } else {
            setShouldRestrictIcons(false);
        }
        setLayoutParams(linearLayout.getLayoutParams());
        setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingTop(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        setOrientation(linearLayout.getOrientation());
        setGravity(16);
        ViewGroup viewGroup = (ViewGroup) linearLayout.getParent();
        viewGroup.addView(this, viewGroup.indexOfChild(linearLayout));
    }

    public void remove() {
        this.mMobileViews.clear();
        ((ViewGroup) getParent()).removeView(this);
    }

    public void setColor(int i) {
        this.mColor = i;
        updateColors();
    }

    public final void updateColors() {
        for (int i = 0; i < getChildCount(); i++) {
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) getChildAt(i);
            statusIconDisplayable.setStaticDrawableColor(this.mColor);
            statusIconDisplayable.setDecorColor(this.mColor);
        }
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("status");
        return arrayList;
    }

    public void onDemoModeStarted() {
        this.mDemoMode = true;
        this.mStatusIcons.setVisibility(8);
        setVisibility(0);
    }

    public void onDemoModeFinished() {
        this.mDemoMode = false;
        this.mStatusIcons.setVisibility(0);
        setVisibility(8);
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        String string = bundle.getString("volume");
        int i = 0;
        if (string != null) {
            updateSlot("volume", (String) null, string.equals("vibrate") ? R$drawable.stat_sys_ringer_vibrate : 0);
        }
        String string2 = bundle.getString("zen");
        if (string2 != null) {
            updateSlot("zen", (String) null, string2.equals("dnd") ? R$drawable.stat_sys_dnd : 0);
        }
        String string3 = bundle.getString("bluetooth");
        if (string3 != null) {
            updateSlot("bluetooth", (String) null, string3.equals("connected") ? R$drawable.stat_sys_data_bluetooth_connected : 0);
        }
        String string4 = bundle.getString("location");
        if (string4 != null) {
            updateSlot("location", (String) null, string4.equals("show") ? PhoneStatusBarPolicy.LOCATION_STATUS_ICON_ID : 0);
        }
        String string5 = bundle.getString("alarm");
        if (string5 != null) {
            updateSlot("alarm_clock", (String) null, string5.equals("show") ? R$drawable.stat_sys_alarm : 0);
        }
        String string6 = bundle.getString("tty");
        if (string6 != null) {
            updateSlot("tty", (String) null, string6.equals("show") ? R$drawable.stat_sys_tty_mode : 0);
        }
        String string7 = bundle.getString("mute");
        if (string7 != null) {
            updateSlot("mute", (String) null, string7.equals("show") ? 17301622 : 0);
        }
        String string8 = bundle.getString("speakerphone");
        if (string8 != null) {
            updateSlot("speakerphone", (String) null, string8.equals("show") ? 17301639 : 0);
        }
        String string9 = bundle.getString("cast");
        if (string9 != null) {
            updateSlot("cast", (String) null, string9.equals("show") ? R$drawable.stat_sys_cast : 0);
        }
        String string10 = bundle.getString("hotspot");
        if (string10 != null) {
            if (string10.equals("show")) {
                i = R$drawable.stat_sys_hotspot;
            }
            updateSlot("hotspot", (String) null, i);
        }
    }

    public final void updateSlot(String str, String str2, int i) {
        if (this.mDemoMode) {
            if (str2 == null) {
                str2 = this.mContext.getPackageName();
            }
            String str3 = str2;
            int i2 = 0;
            while (true) {
                if (i2 >= getChildCount()) {
                    i2 = -1;
                    break;
                }
                View childAt = getChildAt(i2);
                if (childAt instanceof StatusBarIconView) {
                    StatusBarIconView statusBarIconView = (StatusBarIconView) childAt;
                    if (str.equals(statusBarIconView.getTag())) {
                        if (i != 0) {
                            StatusBarIcon statusBarIcon = statusBarIconView.getStatusBarIcon();
                            statusBarIcon.visible = true;
                            statusBarIcon.icon = Icon.createWithResource(statusBarIcon.icon.getResPackage(), i);
                            statusBarIconView.set(statusBarIcon);
                            statusBarIconView.updateDrawable();
                            return;
                        }
                    }
                }
                i2++;
            }
            if (i != 0) {
                StatusBarIcon statusBarIcon2 = new StatusBarIcon(str3, UserHandle.SYSTEM, i, 0, 0, "Demo");
                statusBarIcon2.visible = true;
                StatusBarIconView statusBarIconView2 = new StatusBarIconView(getContext(), str, (StatusBarNotification) null, false);
                statusBarIconView2.setTag(str);
                statusBarIconView2.set(statusBarIcon2);
                statusBarIconView2.setStaticDrawableColor(this.mColor);
                statusBarIconView2.setDecorColor(this.mColor);
                addView(statusBarIconView2, 0, createLayoutParams());
            } else if (i2 != -1) {
                removeViewAt(i2);
            }
        }
    }

    public void addDemoWifiView(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        Log.d("DemoStatusIcons", "addDemoWifiView: ");
        StatusBarWifiView fromContext = StatusBarWifiView.fromContext(this.mContext, wifiIconState.slot);
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                break;
            } else if (getChildAt(i) instanceof StatusBarMobileView) {
                childCount = i;
                break;
            } else {
                i++;
            }
        }
        this.mWifiView = fromContext;
        fromContext.applyWifiState(wifiIconState);
        this.mWifiView.setStaticDrawableColor(this.mColor);
        addView(fromContext, childCount, createLayoutParams());
    }

    public void updateWifiState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        Log.d("DemoStatusIcons", "updateWifiState: ");
        StatusBarWifiView statusBarWifiView = this.mWifiView;
        if (statusBarWifiView == null) {
            addDemoWifiView(wifiIconState);
        } else {
            statusBarWifiView.applyWifiState(wifiIconState);
        }
    }

    public void addMobileView(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        Log.d("DemoStatusIcons", "addMobileView: ");
        StatusBarMobileView fromContext = StatusBarMobileView.fromContext(this.mContext, mobileIconState.slot, this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS));
        fromContext.applyMobileState(mobileIconState);
        fromContext.setStaticDrawableColor(this.mColor);
        this.mMobileViews.add(fromContext);
        addView(fromContext, getChildCount(), createLayoutParams());
    }

    public void updateMobileState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        Log.d("DemoStatusIcons", "updateMobileState: ");
        for (int i = 0; i < this.mMobileViews.size(); i++) {
            StatusBarMobileView statusBarMobileView = this.mMobileViews.get(i);
            if (statusBarMobileView.getState().subId == mobileIconState.subId) {
                statusBarMobileView.applyMobileState(mobileIconState);
                return;
            }
        }
        addMobileView(mobileIconState);
    }

    public void onRemoveIcon(StatusIconDisplayable statusIconDisplayable) {
        if (statusIconDisplayable.getSlot().equals("wifi")) {
            removeView(this.mWifiView);
            this.mWifiView = null;
            return;
        }
        StatusBarMobileView matchingMobileView = matchingMobileView(statusIconDisplayable);
        if (matchingMobileView != null) {
            removeView(matchingMobileView);
            this.mMobileViews.remove(matchingMobileView);
        }
    }

    public final StatusBarMobileView matchingMobileView(StatusIconDisplayable statusIconDisplayable) {
        if (!(statusIconDisplayable instanceof StatusBarMobileView)) {
            return null;
        }
        StatusBarMobileView statusBarMobileView = (StatusBarMobileView) statusIconDisplayable;
        Iterator<StatusBarMobileView> it = this.mMobileViews.iterator();
        while (it.hasNext()) {
            StatusBarMobileView next = it.next();
            if (next.getState().subId == statusBarMobileView.getState().subId) {
                return next;
            }
        }
        return null;
    }

    public final LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(-2, this.mIconSize);
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        setColor(DarkIconDispatcher.getTint(arrayList, this.mStatusIcons, i));
        StatusBarWifiView statusBarWifiView = this.mWifiView;
        if (statusBarWifiView != null) {
            statusBarWifiView.onDarkChanged(arrayList, f, i);
        }
        Iterator<StatusBarMobileView> it = this.mMobileViews.iterator();
        while (it.hasNext()) {
            it.next().onDarkChanged(arrayList, f, i);
        }
    }
}
