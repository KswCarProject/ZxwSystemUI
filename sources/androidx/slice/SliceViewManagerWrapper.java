package androidx.slice;

import android.app.slice.SliceManager;
import android.app.slice.SliceSpec;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import androidx.collection.ArrayMap;
import androidx.slice.widget.SliceLiveData;
import java.util.Set;

public class SliceViewManagerWrapper extends SliceViewManagerBase {
    public final ArrayMap<String, String> mCachedAuthorities;
    public final ArrayMap<String, Boolean> mCachedSuspendFlags;
    public final SliceManager mManager;
    public final Set<SliceSpec> mSpecs;

    public SliceViewManagerWrapper(Context context) {
        this(context, (SliceManager) context.getSystemService(SliceManager.class));
    }

    public SliceViewManagerWrapper(Context context, SliceManager sliceManager) {
        super(context);
        this.mCachedSuspendFlags = new ArrayMap<>();
        this.mCachedAuthorities = new ArrayMap<>();
        this.mManager = sliceManager;
        this.mSpecs = SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS);
    }

    public void pinSlice(Uri uri) {
        try {
            this.mManager.pinSlice(uri, this.mSpecs);
        } catch (RuntimeException e) {
            ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(uri);
            if (acquireContentProviderClient == null) {
                throw new IllegalArgumentException("No provider found for " + uri);
            }
            acquireContentProviderClient.release();
            throw e;
        }
    }

    public void unpinSlice(Uri uri) {
        try {
            this.mManager.unpinSlice(uri);
        } catch (IllegalStateException unused) {
        }
    }

    public Slice bindSlice(Uri uri) {
        if (isAuthoritySuspended(uri.getAuthority())) {
            return null;
        }
        return SliceConvert.wrap(this.mManager.bindSlice(uri, this.mSpecs), this.mContext);
    }

    public Slice bindSlice(Intent intent) {
        if (isPackageSuspended(intent)) {
            return null;
        }
        return SliceConvert.wrap(this.mManager.bindSlice(intent, this.mSpecs), this.mContext);
    }

    public final boolean isPackageSuspended(Intent intent) {
        if (intent.getComponent() != null) {
            return isPackageSuspended(intent.getComponent().getPackageName());
        }
        if (intent.getPackage() != null) {
            return isPackageSuspended(intent.getPackage());
        }
        if (intent.getData() != null) {
            return isAuthoritySuspended(intent.getData().getAuthority());
        }
        return false;
    }

    public final boolean isAuthoritySuspended(String str) {
        String str2 = this.mCachedAuthorities.get(str);
        if (str2 == null) {
            ProviderInfo resolveContentProvider = this.mContext.getPackageManager().resolveContentProvider(str, 0);
            if (resolveContentProvider == null) {
                return false;
            }
            str2 = resolveContentProvider.packageName;
            this.mCachedAuthorities.put(str, str2);
        }
        return isPackageSuspended(str2);
    }

    public final boolean isPackageSuspended(String str) {
        Boolean bool = this.mCachedSuspendFlags.get(str);
        if (bool == null) {
            try {
                Boolean valueOf = Boolean.valueOf((this.mContext.getPackageManager().getApplicationInfo(str, 0).flags & 1073741824) != 0);
                this.mCachedSuspendFlags.put(str, valueOf);
                bool = valueOf;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
        return bool.booleanValue();
    }
}
