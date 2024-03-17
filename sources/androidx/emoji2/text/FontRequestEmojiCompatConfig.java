package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class FontRequestEmojiCompatConfig extends EmojiCompat.Config {
    public static final FontProviderHelper DEFAULT_FONTS_CONTRACT = new FontProviderHelper();

    public FontRequestEmojiCompatConfig(Context context, FontRequest fontRequest) {
        super(new FontRequestMetadataLoader(context, fontRequest, DEFAULT_FONTS_CONTRACT));
    }

    public FontRequestEmojiCompatConfig setLoadingExecutor(Executor executor) {
        ((FontRequestMetadataLoader) getMetadataRepoLoader()).setExecutor(executor);
        return this;
    }

    public static class FontRequestMetadataLoader implements EmojiCompat.MetadataRepoLoader {
        public EmojiCompat.MetadataRepoLoaderCallback mCallback;
        public final Context mContext;
        public Executor mExecutor;
        public final FontProviderHelper mFontProviderHelper;
        public final Object mLock = new Object();
        public Handler mMainHandler;
        public Runnable mMainHandlerLoadCallback;
        public ThreadPoolExecutor mMyThreadPoolExecutor;
        public ContentObserver mObserver;
        public final FontRequest mRequest;

        public FontRequestMetadataLoader(Context context, FontRequest fontRequest, FontProviderHelper fontProviderHelper) {
            Preconditions.checkNotNull(context, "Context cannot be null");
            Preconditions.checkNotNull(fontRequest, "FontRequest cannot be null");
            this.mContext = context.getApplicationContext();
            this.mRequest = fontRequest;
            this.mFontProviderHelper = fontProviderHelper;
        }

        public void setExecutor(Executor executor) {
            synchronized (this.mLock) {
                this.mExecutor = executor;
            }
        }

        public void load(EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback) {
            Preconditions.checkNotNull(metadataRepoLoaderCallback, "LoaderCallback cannot be null");
            synchronized (this.mLock) {
                this.mCallback = metadataRepoLoaderCallback;
            }
            loadInternal();
        }

        public void loadInternal() {
            synchronized (this.mLock) {
                if (this.mCallback != null) {
                    if (this.mExecutor == null) {
                        ThreadPoolExecutor createBackgroundPriorityExecutor = ConcurrencyHelpers.createBackgroundPriorityExecutor("emojiCompat");
                        this.mMyThreadPoolExecutor = createBackgroundPriorityExecutor;
                        this.mExecutor = createBackgroundPriorityExecutor;
                    }
                    this.mExecutor.execute(new FontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda0(this));
                }
            }
        }

        public final FontsContractCompat.FontInfo retrieveFontInfo() {
            try {
                FontsContractCompat.FontFamilyResult fetchFonts = this.mFontProviderHelper.fetchFonts(this.mContext, this.mRequest);
                if (fetchFonts.getStatusCode() == 0) {
                    FontsContractCompat.FontInfo[] fonts = fetchFonts.getFonts();
                    if (fonts != null && fonts.length != 0) {
                        return fonts[0];
                    }
                    throw new RuntimeException("fetchFonts failed (empty result)");
                }
                throw new RuntimeException("fetchFonts failed (" + fetchFonts.getStatusCode() + ")");
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException("provider not found", e);
            }
        }

        public final void cleanUp() {
            synchronized (this.mLock) {
                this.mCallback = null;
                ContentObserver contentObserver = this.mObserver;
                if (contentObserver != null) {
                    this.mFontProviderHelper.unregisterObserver(this.mContext, contentObserver);
                    this.mObserver = null;
                }
                Handler handler = this.mMainHandler;
                if (handler != null) {
                    handler.removeCallbacks(this.mMainHandlerLoadCallback);
                }
                this.mMainHandler = null;
                ThreadPoolExecutor threadPoolExecutor = this.mMyThreadPoolExecutor;
                if (threadPoolExecutor != null) {
                    threadPoolExecutor.shutdown();
                }
                this.mExecutor = null;
                this.mMyThreadPoolExecutor = null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            if (r1 != 2) goto L_0x001e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
            r2 = r4.mLock;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x001e, code lost:
            if (r1 != 0) goto L_0x0062;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            androidx.core.os.TraceCompat.beginSection("EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface");
            r1 = r4.mFontProviderHelper.buildTypeface(r4.mContext, r0);
            r0 = androidx.core.graphics.TypefaceCompatUtil.mmap(r4.mContext, (android.os.CancellationSignal) null, r0.getUri());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0038, code lost:
            if (r0 == null) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x003a, code lost:
            if (r1 == null) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x003c, code lost:
            r0 = androidx.emoji2.text.MetadataRepo.create(r1, r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            androidx.core.os.TraceCompat.endSection();
            r1 = r4.mLock;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0045, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
            r2 = r4.mCallback;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0048, code lost:
            if (r2 == null) goto L_0x004d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x004a, code lost:
            r2.onLoaded(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x004d, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            cleanUp();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x005c, code lost:
            throw new java.lang.RuntimeException("Unable to open file.");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x005d, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
            androidx.core.os.TraceCompat.endSection();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0061, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x007d, code lost:
            throw new java.lang.RuntimeException("fetchFonts result is not OK. (" + r1 + ")");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x007e, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0081, code lost:
            monitor-enter(r4.mLock);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            r2 = r4.mCallback;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x0084, code lost:
            if (r2 != null) goto L_0x0086;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x0086, code lost:
            r2.onFailed(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x008a, code lost:
            cleanUp();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
            r0 = retrieveFontInfo();
            r1 = r0.getResultCode();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void createMetadata() {
            /*
                r4 = this;
                java.lang.Object r0 = r4.mLock
                monitor-enter(r0)
                androidx.emoji2.text.EmojiCompat$MetadataRepoLoaderCallback r1 = r4.mCallback     // Catch:{ all -> 0x0091 }
                if (r1 != 0) goto L_0x0009
                monitor-exit(r0)     // Catch:{ all -> 0x0091 }
                return
            L_0x0009:
                monitor-exit(r0)     // Catch:{ all -> 0x0091 }
                androidx.core.provider.FontsContractCompat$FontInfo r0 = r4.retrieveFontInfo()     // Catch:{ all -> 0x007e }
                int r1 = r0.getResultCode()     // Catch:{ all -> 0x007e }
                r2 = 2
                if (r1 != r2) goto L_0x001e
                java.lang.Object r2 = r4.mLock     // Catch:{ all -> 0x007e }
                monitor-enter(r2)     // Catch:{ all -> 0x007e }
                monitor-exit(r2)     // Catch:{ all -> 0x001c }
                goto L_0x001e
            L_0x001a:
                monitor-exit(r2)     // Catch:{ all -> 0x001c }
                throw r0     // Catch:{ all -> 0x007e }
            L_0x001c:
                r0 = move-exception
                goto L_0x001a
            L_0x001e:
                if (r1 != 0) goto L_0x0062
                java.lang.String r1 = "EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface"
                androidx.core.os.TraceCompat.beginSection(r1)     // Catch:{ all -> 0x005d }
                androidx.emoji2.text.FontRequestEmojiCompatConfig$FontProviderHelper r1 = r4.mFontProviderHelper     // Catch:{ all -> 0x005d }
                android.content.Context r2 = r4.mContext     // Catch:{ all -> 0x005d }
                android.graphics.Typeface r1 = r1.buildTypeface(r2, r0)     // Catch:{ all -> 0x005d }
                android.content.Context r2 = r4.mContext     // Catch:{ all -> 0x005d }
                r3 = 0
                android.net.Uri r0 = r0.getUri()     // Catch:{ all -> 0x005d }
                java.nio.ByteBuffer r0 = androidx.core.graphics.TypefaceCompatUtil.mmap(r2, r3, r0)     // Catch:{ all -> 0x005d }
                if (r0 == 0) goto L_0x0055
                if (r1 == 0) goto L_0x0055
                androidx.emoji2.text.MetadataRepo r0 = androidx.emoji2.text.MetadataRepo.create(r1, r0)     // Catch:{ all -> 0x005d }
                androidx.core.os.TraceCompat.endSection()     // Catch:{ all -> 0x007e }
                java.lang.Object r1 = r4.mLock     // Catch:{ all -> 0x007e }
                monitor-enter(r1)     // Catch:{ all -> 0x007e }
                androidx.emoji2.text.EmojiCompat$MetadataRepoLoaderCallback r2 = r4.mCallback     // Catch:{ all -> 0x0052 }
                if (r2 == 0) goto L_0x004d
                r2.onLoaded(r0)     // Catch:{ all -> 0x0052 }
            L_0x004d:
                monitor-exit(r1)     // Catch:{ all -> 0x0052 }
                r4.cleanUp()     // Catch:{ all -> 0x007e }
                goto L_0x008d
            L_0x0052:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0052 }
                throw r0     // Catch:{ all -> 0x007e }
            L_0x0055:
                java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ all -> 0x005d }
                java.lang.String r1 = "Unable to open file."
                r0.<init>(r1)     // Catch:{ all -> 0x005d }
                throw r0     // Catch:{ all -> 0x005d }
            L_0x005d:
                r0 = move-exception
                androidx.core.os.TraceCompat.endSection()     // Catch:{ all -> 0x007e }
                throw r0     // Catch:{ all -> 0x007e }
            L_0x0062:
                java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ all -> 0x007e }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x007e }
                r2.<init>()     // Catch:{ all -> 0x007e }
                java.lang.String r3 = "fetchFonts result is not OK. ("
                r2.append(r3)     // Catch:{ all -> 0x007e }
                r2.append(r1)     // Catch:{ all -> 0x007e }
                java.lang.String r1 = ")"
                r2.append(r1)     // Catch:{ all -> 0x007e }
                java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x007e }
                r0.<init>(r1)     // Catch:{ all -> 0x007e }
                throw r0     // Catch:{ all -> 0x007e }
            L_0x007e:
                r0 = move-exception
                java.lang.Object r1 = r4.mLock
                monitor-enter(r1)
                androidx.emoji2.text.EmojiCompat$MetadataRepoLoaderCallback r2 = r4.mCallback     // Catch:{ all -> 0x008e }
                if (r2 == 0) goto L_0x0089
                r2.onFailed(r0)     // Catch:{ all -> 0x008e }
            L_0x0089:
                monitor-exit(r1)     // Catch:{ all -> 0x008e }
                r4.cleanUp()
            L_0x008d:
                return
            L_0x008e:
                r4 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x008e }
                throw r4
            L_0x0091:
                r4 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0091 }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.emoji2.text.FontRequestEmojiCompatConfig.FontRequestMetadataLoader.createMetadata():void");
        }
    }

    public static class FontProviderHelper {
        public FontsContractCompat.FontFamilyResult fetchFonts(Context context, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
            return FontsContractCompat.fetchFonts(context, (CancellationSignal) null, fontRequest);
        }

        public Typeface buildTypeface(Context context, FontsContractCompat.FontInfo fontInfo) throws PackageManager.NameNotFoundException {
            return FontsContractCompat.buildTypeface(context, (CancellationSignal) null, new FontsContractCompat.FontInfo[]{fontInfo});
        }

        public void unregisterObserver(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }
}
