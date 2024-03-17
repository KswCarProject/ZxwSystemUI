package com.android.systemui.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener implements MediaDataManager.Listener, Dumpable {
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final Context context;
    public int currentUserId;
    @Nullable
    public ResumeMediaBrowser mediaBrowser;
    @NotNull
    public final MediaResumeListener$mediaBrowserCallback$1 mediaBrowserCallback;
    @NotNull
    public final ResumeMediaBrowserFactory mediaBrowserFactory;
    public MediaDataManager mediaDataManager;
    @NotNull
    public final ConcurrentLinkedQueue<Pair<ComponentName, Long>> resumeComponents = new ConcurrentLinkedQueue<>();
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final TunerService tunerService;
    public boolean useMediaResumption;
    @NotNull
    public final BroadcastReceiver userChangeReceiver;

    @VisibleForTesting
    public static /* synthetic */ void getUserChangeReceiver$annotations() {
    }

    public MediaResumeListener(@NotNull Context context2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull Executor executor, @NotNull TunerService tunerService2, @NotNull ResumeMediaBrowserFactory resumeMediaBrowserFactory, @NotNull DumpManager dumpManager, @NotNull SystemClock systemClock2) {
        this.context = context2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.backgroundExecutor = executor;
        this.tunerService = tunerService2;
        this.mediaBrowserFactory = resumeMediaBrowserFactory;
        this.systemClock = systemClock2;
        this.useMediaResumption = Utils.useMediaResumption(context2);
        this.currentUserId = context2.getUserId();
        MediaResumeListener$userChangeReceiver$1 mediaResumeListener$userChangeReceiver$1 = new MediaResumeListener$userChangeReceiver$1(this);
        this.userChangeReceiver = mediaResumeListener$userChangeReceiver$1;
        this.mediaBrowserCallback = new MediaResumeListener$mediaBrowserCallback$1(this);
        if (this.useMediaResumption) {
            dumpManager.registerDumpable("MediaResumeListener", this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, mediaResumeListener$userChangeReceiver$1, intentFilter, (Executor) null, UserHandle.ALL, 0, (String) null, 48, (Object) null);
            loadSavedComponents();
        }
    }

    public void onMediaDataRemoved(@NotNull String str) {
        MediaDataManager.Listener.DefaultImpls.onMediaDataRemoved(this, str);
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    public final void setMediaBrowser(ResumeMediaBrowser resumeMediaBrowser) {
        ResumeMediaBrowser resumeMediaBrowser2 = this.mediaBrowser;
        if (resumeMediaBrowser2 != null) {
            resumeMediaBrowser2.disconnect();
        }
        this.mediaBrowser = resumeMediaBrowser;
    }

    public final void setManager(@NotNull MediaDataManager mediaDataManager2) {
        this.mediaDataManager = mediaDataManager2;
        this.tunerService.addTunable(new MediaResumeListener$setManager$1(this), "qs_media_resumption");
    }

    public final void loadSavedComponents() {
        long j;
        List<String> split;
        boolean z;
        this.resumeComponents.clear();
        boolean z2 = false;
        List<String> list = null;
        String string = this.context.getSharedPreferences("media_control_prefs", 0).getString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), (String) null);
        if (string != null && (split = new Regex(":").split(string, 0)) != null) {
            if (!split.isEmpty()) {
                ListIterator<String> listIterator = split.listIterator(split.size());
                while (true) {
                    if (!listIterator.hasPrevious()) {
                        break;
                    }
                    if (listIterator.previous().length() == 0) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (!z) {
                        list = CollectionsKt___CollectionsKt.take(split, listIterator.nextIndex() + 1);
                        break;
                    }
                }
            }
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        if (list != null) {
            boolean z3 = false;
            for (String split$default : list) {
                List split$default2 = StringsKt__StringsKt.split$default(split$default, new String[]{"/"}, false, 0, 6, (Object) null);
                ComponentName componentName = new ComponentName((String) split$default2.get(0), (String) split$default2.get(1));
                if (split$default2.size() == 3) {
                    try {
                        j = Long.parseLong((String) split$default2.get(2));
                    } catch (NumberFormatException unused) {
                        j = this.systemClock.currentTimeMillis();
                    }
                } else {
                    j = this.systemClock.currentTimeMillis();
                    z3 = true;
                }
                this.resumeComponents.add(TuplesKt.to(componentName, Long.valueOf(j)));
            }
            z2 = z3;
        }
        Log.d("MediaResumeListener", Intrinsics.stringPlus("loaded resume components ", Arrays.toString(this.resumeComponents.toArray())));
        if (z2) {
            writeSharedPrefs();
        }
    }

    public final void loadMediaResumptionControls() {
        if (this.useMediaResumption) {
            long currentTimeMillis = this.systemClock.currentTimeMillis();
            for (Pair pair : this.resumeComponents) {
                if (currentTimeMillis - ((Number) pair.getSecond()).longValue() <= MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT()) {
                    this.mediaBrowserFactory.create(this.mediaBrowserCallback, (ComponentName) pair.getFirst()).findRecentMedia();
                }
            }
        }
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        ArrayList arrayList;
        if (this.useMediaResumption) {
            if (!str.equals(str2)) {
                setMediaBrowser((ResumeMediaBrowser) null);
            }
            if (mediaData.getResumeAction() == null && !mediaData.getHasCheckedForResume() && mediaData.isLocalSession()) {
                Log.d("MediaResumeListener", Intrinsics.stringPlus("Checking for service component for ", mediaData.getPackageName()));
                List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);
                if (queryIntentServices == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    for (Object next : queryIntentServices) {
                        if (Intrinsics.areEqual((Object) ((ResolveInfo) next).serviceInfo.packageName, (Object) mediaData.getPackageName())) {
                            arrayList.add(next);
                        }
                    }
                }
                if (arrayList == null || arrayList.size() <= 0) {
                    MediaDataManager mediaDataManager2 = this.mediaDataManager;
                    if (mediaDataManager2 == null) {
                        mediaDataManager2 = null;
                    }
                    mediaDataManager2.setResumeAction(str, (Runnable) null);
                    return;
                }
                this.backgroundExecutor.execute(new MediaResumeListener$onMediaDataLoaded$1(this, str, arrayList));
            }
        }
    }

    public final void tryUpdateResumptionList(String str, ComponentName componentName) {
        Log.d("MediaResumeListener", Intrinsics.stringPlus("Testing if we can connect to ", componentName));
        MediaDataManager mediaDataManager2 = this.mediaDataManager;
        if (mediaDataManager2 == null) {
            mediaDataManager2 = null;
        }
        mediaDataManager2.setResumeAction(str, (Runnable) null);
        setMediaBrowser(this.mediaBrowserFactory.create(new MediaResumeListener$tryUpdateResumptionList$1(componentName, this, str), componentName));
        ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
        if (resumeMediaBrowser != null) {
            resumeMediaBrowser.testConnection();
        }
    }

    public final void updateResumptionList(ComponentName componentName) {
        T t;
        ConcurrentLinkedQueue<Pair<ComponentName, Long>> concurrentLinkedQueue = this.resumeComponents;
        Iterator<T> it = concurrentLinkedQueue.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            if (((ComponentName) ((Pair) t).getFirst()).equals(componentName)) {
                break;
            }
        }
        concurrentLinkedQueue.remove(t);
        this.resumeComponents.add(TuplesKt.to(componentName, Long.valueOf(this.systemClock.currentTimeMillis())));
        if (this.resumeComponents.size() > 5) {
            this.resumeComponents.remove();
        }
        writeSharedPrefs();
    }

    public final void writeSharedPrefs() {
        StringBuilder sb = new StringBuilder();
        for (Pair pair : this.resumeComponents) {
            sb.append(((ComponentName) pair.getFirst()).flattenToString());
            sb.append("/");
            sb.append(((Number) pair.getSecond()).longValue());
            sb.append(":");
        }
        this.context.getSharedPreferences("media_control_prefs", 0).edit().putString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), sb.toString()).apply();
    }

    public final Runnable getResumeAction(ComponentName componentName) {
        return new MediaResumeListener$getResumeAction$1(this, componentName);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("resumeComponents: ", this.resumeComponents));
    }
}
