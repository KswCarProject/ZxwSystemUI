package com.android.settingslib.drawer;

import android.content.pm.ProviderInfo;
import android.os.Parcel;

public class ProviderTile extends Tile {
    public String mAuthority = ((ProviderInfo) this.mComponentInfo).authority;
    public String mKey = getMetaData().getString("com.android.settings.keyhint");

    public ProviderTile(Parcel parcel) {
        super(parcel);
    }
}
