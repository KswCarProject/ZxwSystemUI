package com.android.systemui.statusbar.notification.row;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ImageResolver;
import com.android.internal.widget.LocalImageResolver;
import com.android.internal.widget.MessagingMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationInlineImageResolver implements ImageResolver {
    public static final String TAG = NotificationInlineImageResolver.class.getSimpleName();
    public final Context mContext;
    public final ImageCache mImageCache;
    @VisibleForTesting
    public int mMaxImageHeight;
    @VisibleForTesting
    public int mMaxImageWidth;
    public Set<Uri> mWantedUriSet;

    public interface ImageCache {
        Drawable get(Uri uri);

        boolean hasEntry(Uri uri);

        void preload(Uri uri);

        void purge();

        void setImageResolver(NotificationInlineImageResolver notificationInlineImageResolver);
    }

    public NotificationInlineImageResolver(Context context, ImageCache imageCache) {
        this.mContext = context.getApplicationContext();
        this.mImageCache = imageCache;
        if (imageCache != null) {
            imageCache.setImageResolver(this);
        }
        updateMaxImageSizes();
    }

    public boolean hasCache() {
        return this.mImageCache != null && !isLowRam();
    }

    public final boolean isLowRam() {
        return ActivityManager.isLowRamDeviceStatic();
    }

    public void updateMaxImageSizes() {
        this.mMaxImageWidth = getMaxImageWidth();
        this.mMaxImageHeight = getMaxImageHeight();
    }

    @VisibleForTesting
    public int getMaxImageWidth() {
        return this.mContext.getResources().getDimensionPixelSize(isLowRam() ? 17105393 : 17105392);
    }

    @VisibleForTesting
    public int getMaxImageHeight() {
        return this.mContext.getResources().getDimensionPixelSize(isLowRam() ? 17105391 : 17105390);
    }

    public Drawable resolveImage(Uri uri) {
        try {
            return LocalImageResolver.resolveImage(uri, this.mContext, this.mMaxImageWidth, this.mMaxImageHeight);
        } catch (Exception e) {
            String str = TAG;
            Log.d(str, "resolveImage: Can't load image from " + uri, e);
            return null;
        }
    }

    public Drawable loadImage(Uri uri) {
        return hasCache() ? loadImageFromCache(uri) : resolveImage(uri);
    }

    public final Drawable loadImageFromCache(Uri uri) {
        if (!this.mImageCache.hasEntry(uri)) {
            this.mImageCache.preload(uri);
        }
        return this.mImageCache.get(uri);
    }

    public void preloadImages(Notification notification) {
        if (hasCache()) {
            retrieveWantedUriSet(notification);
            getWantedUriSet().forEach(new NotificationInlineImageResolver$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadImages$0(Uri uri) {
        if (!this.mImageCache.hasEntry(uri)) {
            this.mImageCache.preload(uri);
        }
    }

    public void purgeCache() {
        if (hasCache()) {
            this.mImageCache.purge();
        }
    }

    public final void retrieveWantedUriSet(Notification notification) {
        List<Notification.MessagingStyle.Message> list;
        HashSet hashSet = new HashSet();
        Bundle bundle = notification.extras;
        if (bundle != null) {
            Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
            List<Notification.MessagingStyle.Message> list2 = null;
            if (parcelableArray == null) {
                list = null;
            } else {
                list = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray);
            }
            if (list != null) {
                for (Notification.MessagingStyle.Message message : list) {
                    if (MessagingMessage.hasImage(message)) {
                        hashSet.add(message.getDataUri());
                    }
                }
            }
            Parcelable[] parcelableArray2 = bundle.getParcelableArray("android.messages.historic");
            if (parcelableArray2 != null) {
                list2 = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray2);
            }
            if (list2 != null) {
                for (Notification.MessagingStyle.Message message2 : list2) {
                    if (MessagingMessage.hasImage(message2)) {
                        hashSet.add(message2.getDataUri());
                    }
                }
            }
            this.mWantedUriSet = hashSet;
        }
    }

    public Set<Uri> getWantedUriSet() {
        return this.mWantedUriSet;
    }
}
