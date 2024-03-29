package com.android.keyguard.clock;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileNotFoundException;
import java.util.List;
import javax.inject.Provider;

public final class ClockOptionsProvider extends ContentProvider {
    public Provider<List<ClockInfo>> mClockInfosProvider;

    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return true;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @VisibleForTesting
    public ClockOptionsProvider(Provider<List<ClockInfo>> provider) {
        this.mClockInfosProvider = provider;
    }

    public String getType(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() > 0) {
            return ("preview".equals(pathSegments.get(0)) || "thumbnail".equals(pathSegments.get(0))) ? "image/png" : "vnd.android.cursor.dir/clock_faces";
        }
        return "vnd.android.cursor.dir/clock_faces";
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (!"/list_options".equals(uri.getPath())) {
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"name", "title", "id", "thumbnail", "preview"});
        List list = this.mClockInfosProvider.get();
        for (int i = 0; i < list.size(); i++) {
            ClockInfo clockInfo = (ClockInfo) list.get(i);
            matrixCursor.newRow().add("name", clockInfo.getName()).add("title", clockInfo.getTitle()).add("id", clockInfo.getId()).add("thumbnail", createThumbnailUri(clockInfo)).add("preview", createPreviewUri(clockInfo));
        }
        return matrixCursor;
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        ClockInfo clockInfo;
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 2 || (!"preview".equals(pathSegments.get(0)) && !"thumbnail".equals(pathSegments.get(0)))) {
            throw new FileNotFoundException("Invalid preview url");
        }
        String str2 = pathSegments.get(1);
        if (!TextUtils.isEmpty(str2)) {
            List list = this.mClockInfosProvider.get();
            int i = 0;
            while (true) {
                if (i >= list.size()) {
                    clockInfo = null;
                    break;
                } else if (str2.equals(((ClockInfo) list.get(i)).getId())) {
                    clockInfo = (ClockInfo) list.get(i);
                    break;
                } else {
                    i++;
                }
            }
            if (clockInfo != null) {
                return openPipeHelper(uri, "image/png", (Bundle) null, "preview".equals(pathSegments.get(0)) ? clockInfo.getPreview() : clockInfo.getThumbnail(), new MyWriter());
            }
            throw new FileNotFoundException("Invalid preview url, id not found");
        }
        throw new FileNotFoundException("Invalid preview url, missing id");
    }

    public final Uri createThumbnailUri(ClockInfo clockInfo) {
        return new Uri.Builder().scheme("content").authority("com.android.keyguard.clock").appendPath("thumbnail").appendPath(clockInfo.getId()).build();
    }

    public final Uri createPreviewUri(ClockInfo clockInfo) {
        return new Uri.Builder().scheme("content").authority("com.android.keyguard.clock").appendPath("preview").appendPath(clockInfo.getId()).build();
    }

    public static class MyWriter implements ContentProvider.PipeDataWriter<Bitmap> {
        public MyWriter() {
        }

        public void writeDataToPipe(ParcelFileDescriptor parcelFileDescriptor, Uri uri, String str, Bundle bundle, Bitmap bitmap) {
            ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream;
            try {
                autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, autoCloseOutputStream);
                autoCloseOutputStream.close();
                return;
            } catch (Exception e) {
                Log.w("ClockOptionsProvider", "fail to write to pipe", e);
                return;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }
    }
}
