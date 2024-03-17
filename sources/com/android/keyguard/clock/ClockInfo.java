package com.android.keyguard.clock;

import android.graphics.Bitmap;
import java.util.function.Supplier;

public final class ClockInfo {
    public final String mId;
    public final String mName;
    public final Supplier<Bitmap> mPreview;
    public final Supplier<Bitmap> mThumbnail;
    public final Supplier<String> mTitle;

    public ClockInfo(String str, Supplier<String> supplier, String str2, Supplier<Bitmap> supplier2, Supplier<Bitmap> supplier3) {
        this.mName = str;
        this.mTitle = supplier;
        this.mId = str2;
        this.mThumbnail = supplier2;
        this.mPreview = supplier3;
    }

    public String getName() {
        return this.mName;
    }

    public String getTitle() {
        return this.mTitle.get();
    }

    public String getId() {
        return this.mId;
    }

    public Bitmap getThumbnail() {
        return this.mThumbnail.get();
    }

    public Bitmap getPreview() {
        return this.mPreview.get();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public String mId;
        public String mName;
        public Supplier<Bitmap> mPreview;
        public Supplier<Bitmap> mThumbnail;
        public Supplier<String> mTitle;

        public ClockInfo build() {
            return new ClockInfo(this.mName, this.mTitle, this.mId, this.mThumbnail, this.mPreview);
        }

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder setTitle(Supplier<String> supplier) {
            this.mTitle = supplier;
            return this;
        }

        public Builder setId(String str) {
            this.mId = str;
            return this;
        }

        public Builder setThumbnail(Supplier<Bitmap> supplier) {
            this.mThumbnail = supplier;
            return this;
        }

        public Builder setPreview(Supplier<Bitmap> supplier) {
            this.mPreview = supplier;
            return this;
        }
    }
}
