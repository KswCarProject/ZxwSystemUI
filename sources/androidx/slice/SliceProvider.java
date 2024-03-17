package androidx.slice;

import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import androidx.core.app.CoreComponentFactory;
import androidx.slice.compat.CompatPermissionManager;
import androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class SliceProvider extends ContentProvider implements CoreComponentFactory.CompatWrapped {
    public static Clock sClock;
    public static Set<SliceSpec> sSpecs;
    public String[] mAuthorities;
    public String mAuthority;
    public final String[] mAutoGrantPermissions = new String[0];
    public final Object mCompatLock = new Object();
    public Context mContext = null;
    public List<Uri> mPinnedSliceUris;
    public final Object mPinnedSliceUrisLock = new Object();

    public final int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        return 0;
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        return null;
    }

    public final Uri canonicalize(Uri uri) {
        return null;
    }

    public final int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public final String getType(Uri uri) {
        return "vnd.android.slice";
    }

    public final Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public abstract Slice onBindSlice(Uri uri);

    public PendingIntent onCreatePermissionRequest(Uri uri, String str) {
        return null;
    }

    public abstract boolean onCreateSliceProvider();

    public void onSlicePinned(Uri uri) {
    }

    public void onSliceUnpinned(Uri uri) {
    }

    public final Cursor query(Uri uri, String[] strArr, Bundle bundle, CancellationSignal cancellationSignal) {
        return null;
    }

    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        return null;
    }

    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    public Object getWrapper() {
        return new SliceProviderWrapperContainer$SliceProviderWrapper(this, this.mAutoGrantPermissions);
    }

    public final boolean onCreate() {
        return onCreateSliceProvider();
    }

    public CompatPermissionManager onCreatePermissionManager(String[] strArr) {
        Context context = getContext();
        return new CompatPermissionManager(context, "slice_perms_" + getClass().getName(), Process.myUid(), strArr);
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (this.mContext == null) {
            this.mContext = context;
            if (providerInfo != null) {
                setAuthorities(providerInfo.authority);
            }
        }
    }

    private void setAuthorities(String str) {
        if (str == null) {
            return;
        }
        if (str.indexOf(59) == -1) {
            this.mAuthority = str;
            this.mAuthorities = null;
            return;
        }
        this.mAuthority = null;
        this.mAuthorities = str.split(";");
    }

    public void handleSlicePinned(Uri uri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (!pinnedSlices.contains(uri)) {
            pinnedSlices.add(uri);
        }
    }

    public void handleSliceUnpinned(Uri uri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (pinnedSlices.contains(uri)) {
            pinnedSlices.remove(uri);
        }
    }

    public Uri onMapIntentToUri(Intent intent) {
        throw new UnsupportedOperationException("This provider has not implemented intent to uri mapping");
    }

    public Collection<Uri> onGetSliceDescendants(Uri uri) {
        return Collections.emptyList();
    }

    public List<Uri> getPinnedSlices() {
        synchronized (this.mPinnedSliceUrisLock) {
            if (this.mPinnedSliceUris == null) {
                this.mPinnedSliceUris = new ArrayList(SliceManager.getInstance(getContext()).getPinnedSlices());
            }
        }
        return this.mPinnedSliceUris;
    }

    public static void setSpecs(Set<SliceSpec> set) {
        sSpecs = set;
    }

    public static Set<SliceSpec> getCurrentSpecs() {
        return sSpecs;
    }

    public static Clock getClock() {
        return sClock;
    }
}
