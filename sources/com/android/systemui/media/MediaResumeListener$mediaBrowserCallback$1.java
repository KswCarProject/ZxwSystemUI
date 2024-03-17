package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.util.Log;
import com.android.systemui.media.ResumeMediaBrowser;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$mediaBrowserCallback$1 extends ResumeMediaBrowser.Callback {
    public final /* synthetic */ MediaResumeListener this$0;

    public MediaResumeListener$mediaBrowserCallback$1(MediaResumeListener mediaResumeListener) {
        this.this$0 = mediaResumeListener;
    }

    public void addTrack(@NotNull MediaDescription mediaDescription, @NotNull ComponentName componentName, @NotNull ResumeMediaBrowser resumeMediaBrowser) {
        MediaSession.Token token = resumeMediaBrowser.getToken();
        PendingIntent appIntent = resumeMediaBrowser.getAppIntent();
        PackageManager packageManager = this.this$0.context.getPackageManager();
        Object packageName = componentName.getPackageName();
        Runnable access$getResumeAction = this.this$0.getResumeAction(componentName);
        try {
            packageName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentName.getPackageName(), 0));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MediaResumeListener", "Error getting package information", e);
        }
        Log.d("MediaResumeListener", Intrinsics.stringPlus("Adding resume controls ", mediaDescription));
        MediaDataManager access$getMediaDataManager$p = this.this$0.mediaDataManager;
        if (access$getMediaDataManager$p == null) {
            access$getMediaDataManager$p = null;
        }
        access$getMediaDataManager$p.addResumptionControls(this.this$0.currentUserId, mediaDescription, access$getResumeAction, token, packageName.toString(), appIntent, componentName.getPackageName());
    }
}
