package androidx.slice.compat;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public class SliceProviderCompat {
    public static void grantSlicePermission(Context context, String str, String str2, Uri uri) {
        ProviderHolder acquireClient;
        try {
            acquireClient = acquireClient(context.getContentResolver(), uri);
            Bundle bundle = new Bundle();
            bundle.putParcelable("slice_uri", uri);
            bundle.putString("provider_pkg", str);
            bundle.putString("pkg", str2);
            acquireClient.mProvider.call("grant_perms", "supports_versioned_parcelable", bundle);
            acquireClient.close();
            return;
        } catch (RemoteException e) {
            Log.e("SliceProviderCompat", "Unable to get slice descendants", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static ProviderHolder acquireClient(ContentResolver contentResolver, Uri uri) {
        ContentProviderClient acquireUnstableContentProviderClient = contentResolver.acquireUnstableContentProviderClient(uri);
        if (acquireUnstableContentProviderClient != null) {
            return new ProviderHolder(acquireUnstableContentProviderClient);
        }
        throw new IllegalArgumentException("No provider found for " + uri);
    }

    public static class ProviderHolder implements AutoCloseable {
        public final ContentProviderClient mProvider;

        public ProviderHolder(ContentProviderClient contentProviderClient) {
            this.mProvider = contentProviderClient;
        }

        public void close() {
            ContentProviderClient contentProviderClient = this.mProvider;
            if (contentProviderClient != null) {
                contentProviderClient.close();
            }
        }
    }
}
