package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;
import java.util.concurrent.Executor;

public class FontsContractCompat {

    public static class FontRequestCallback {
        public void onTypefaceRequestFailed(int i) {
            throw null;
        }

        public void onTypefaceRetrieved(Typeface typeface) {
            throw null;
        }
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fontInfoArr) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fontInfoArr, 0);
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        return FontProvider.getFontFamilyResult(context, fontRequest, cancellationSignal);
    }

    public static Typeface requestFont(Context context, FontRequest fontRequest, int i, boolean z, int i2, Handler handler, FontRequestCallback fontRequestCallback) {
        CallbackWithHandler callbackWithHandler = new CallbackWithHandler(fontRequestCallback, handler);
        if (z) {
            return FontRequestWorker.requestFontSync(context, fontRequest, callbackWithHandler, i, i2);
        }
        return FontRequestWorker.requestFontAsync(context, fontRequest, i, (Executor) null, callbackWithHandler);
    }

    public static void resetTypefaceCache() {
        FontRequestWorker.resetTypefaceCache();
    }

    public static class FontInfo {
        public final boolean mItalic;
        public final int mResultCode;
        public final int mTtcIndex;
        public final Uri mUri;
        public final int mWeight;

        @Deprecated
        public FontInfo(Uri uri, int i, int i2, boolean z, int i3) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = i;
            this.mWeight = i2;
            this.mItalic = z;
            this.mResultCode = i3;
        }

        public static FontInfo create(Uri uri, int i, int i2, boolean z, int i3) {
            return new FontInfo(uri, i, i2, z, i3);
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    public static class FontFamilyResult {
        public final FontInfo[] mFonts;
        public final int mStatusCode;

        @Deprecated
        public FontFamilyResult(int i, FontInfo[] fontInfoArr) {
            this.mStatusCode = i;
            this.mFonts = fontInfoArr;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public static FontFamilyResult create(int i, FontInfo[] fontInfoArr) {
            return new FontFamilyResult(i, fontInfoArr);
        }
    }

    @Deprecated
    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest fontRequest, Resources resources) throws PackageManager.NameNotFoundException {
        return FontProvider.getProvider(packageManager, fontRequest, resources);
    }
}
