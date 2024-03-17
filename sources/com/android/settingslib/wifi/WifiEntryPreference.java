package com.android.settingslib.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.R$attr;
import com.android.settingslib.R$drawable;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.R$string;
import com.android.settingslib.Utils;
import com.android.wifitrackerlib.WifiEntry;

public class WifiEntryPreference extends Preference implements WifiEntry.WifiEntryCallback, View.OnClickListener {
    public static final int[] FRICTION_ATTRS = {R$attr.wifi_friction};
    public static final int[] STATE_SECURED = {R$attr.state_encrypted};
    public static final int[] WIFI_CONNECTION_STRENGTH = {R$string.accessibility_no_wifi, R$string.accessibility_wifi_one_bar, R$string.accessibility_wifi_two_bars, R$string.accessibility_wifi_three_bars, R$string.accessibility_wifi_signal_full};
    public CharSequence mContentDescription;
    public final StateListDrawable mFrictionSld;
    public int mLevel = -1;
    public boolean mShowX;
    public WifiEntry mWifiEntry;
    public int mWifiStandard;

    public static class IconInjector {
    }

    public WifiEntryPreference(Context context, WifiEntry wifiEntry, IconInjector iconInjector) {
        super(context);
        setLayoutResource(R$layout.preference_access_point);
        setWidgetLayoutResource(R$layout.access_point_friction_widget);
        this.mFrictionSld = getFrictionStateListDrawable();
        this.mWifiEntry = wifiEntry;
        wifiEntry.setListener(this);
        refresh();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Drawable icon = getIcon();
        if (icon != null) {
            icon.setLevel(this.mLevel);
        }
        preferenceViewHolder.itemView.setContentDescription(this.mContentDescription);
        preferenceViewHolder.findViewById(R$id.two_target_divider).setVisibility(4);
        ImageButton imageButton = (ImageButton) preferenceViewHolder.findViewById(R$id.icon_button);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R$id.friction_icon);
        if (this.mWifiEntry.getHelpUriString() == null || this.mWifiEntry.getConnectedState() != 0) {
            imageButton.setVisibility(8);
            if (imageView != null) {
                imageView.setVisibility(0);
                bindFrictionImage(imageView);
                return;
            }
            return;
        }
        Drawable drawable = getDrawable(R$drawable.ic_help);
        drawable.setTintList(Utils.getColorAttr(getContext(), 16843817));
        imageButton.setImageDrawable(drawable);
        imageButton.setVisibility(0);
        imageButton.setOnClickListener(this);
        imageButton.setContentDescription(getContext().getText(R$string.help_label));
        if (imageView != null) {
            imageView.setVisibility(8);
        }
    }

    public void refresh() {
        setTitle((CharSequence) this.mWifiEntry.getTitle());
        int level = this.mWifiEntry.getLevel();
        int wifiStandard = this.mWifiEntry.getWifiStandard();
        boolean shouldShowXLevelIcon = this.mWifiEntry.shouldShowXLevelIcon();
        if (!(level == this.mLevel && shouldShowXLevelIcon == this.mShowX && wifiStandard == this.mWifiStandard)) {
            this.mLevel = level;
            this.mWifiStandard = wifiStandard;
            this.mShowX = shouldShowXLevelIcon;
            updateIcon(shouldShowXLevelIcon, level, wifiStandard);
            notifyChanged();
        }
        String summary = this.mWifiEntry.getSummary(false);
        if (this.mWifiEntry.isPskSaeTransitionMode()) {
            summary = "WPA3(SAE Transition Mode) " + summary;
        } else if (this.mWifiEntry.isOweTransitionMode()) {
            summary = "WPA3(OWE Transition Mode) " + summary;
        } else if (this.mWifiEntry.getSecurity() == 5) {
            summary = "WPA3(SAE) " + summary;
        } else if (this.mWifiEntry.getSecurity() == 4) {
            summary = "WPA3(OWE) " + summary;
        }
        setSummary((CharSequence) summary);
        this.mContentDescription = buildContentDescription();
    }

    public void onUpdated() {
        refresh();
    }

    public final void updateIcon(boolean z, int i, int i2) {
        if (i == -1) {
            setIcon((Drawable) null);
            return;
        }
        throw null;
    }

    public final StateListDrawable getFrictionStateListDrawable() {
        TypedArray typedArray;
        try {
            typedArray = getContext().getTheme().obtainStyledAttributes(FRICTION_ATTRS);
        } catch (Resources.NotFoundException unused) {
            typedArray = null;
        }
        if (typedArray != null) {
            return (StateListDrawable) typedArray.getDrawable(0);
        }
        return null;
    }

    public final void bindFrictionImage(ImageView imageView) {
        if (imageView != null && this.mFrictionSld != null) {
            if (!(this.mWifiEntry.getSecurity() == 0 || this.mWifiEntry.getSecurity() == 4)) {
                this.mFrictionSld.setState(STATE_SECURED);
            }
            imageView.setImageDrawable(this.mFrictionSld.getCurrent());
        }
    }

    public CharSequence buildContentDescription() {
        String str;
        Context context = getContext();
        CharSequence title = getTitle();
        CharSequence summary = getSummary();
        if (!TextUtils.isEmpty(summary)) {
            title = TextUtils.concat(new CharSequence[]{title, ",", summary});
        }
        int level = this.mWifiEntry.getLevel();
        if (level >= 0) {
            int[] iArr = WIFI_CONNECTION_STRENGTH;
            if (level < iArr.length) {
                title = TextUtils.concat(new CharSequence[]{title, ",", context.getString(iArr[level])});
            }
        }
        CharSequence[] charSequenceArr = new CharSequence[3];
        charSequenceArr[0] = title;
        charSequenceArr[1] = ",";
        if (this.mWifiEntry.getSecurity() == 0) {
            str = context.getString(R$string.accessibility_wifi_security_type_none);
        } else {
            str = context.getString(R$string.accessibility_wifi_security_type_secured);
        }
        charSequenceArr[2] = str;
        return TextUtils.concat(charSequenceArr);
    }

    public void onClick(View view) {
        view.getId();
    }

    public final Drawable getDrawable(int i) {
        try {
            return getContext().getDrawable(i);
        } catch (Resources.NotFoundException unused) {
            return null;
        }
    }
}
