package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageLoader {
    public final ContentResolver mResolver;

    public static class Result {
        public Bitmap bitmap;
        public File fileName;
        public Uri uri;

        public String toString() {
            return "Result{" + "uri=" + this.uri + ", fileName=" + this.fileName + ", bitmap=" + this.bitmap + '}';
        }
    }

    public ImageLoader(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    public ListenableFuture<Result> load(File file) {
        return CallbackToFutureAdapter.getFuture(new ImageLoader$$ExternalSyntheticLambda0(file));
    }

    public static /* synthetic */ Object lambda$load$1(File file, CallbackToFutureAdapter.Completer completer) throws Exception {
        BufferedInputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            Result result = new Result();
            result.fileName = file;
            result.bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            completer.set(result);
            bufferedInputStream.close();
            return "BitmapFactory#decodeStream";
        } catch (IOException e) {
            completer.setException(e);
            return "BitmapFactory#decodeStream";
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }
}
