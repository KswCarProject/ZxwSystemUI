package com.android.systemui.statusbar.notification.collection.init;

import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.io.PrintWriter;

public class NotifPipelineInitializer implements Dumpable {
    public final DumpManager mDumpManager;
    public final GroupCoalescer mGroupCoalescer;
    public final ShadeListBuilder mListBuilder;
    public final NotifCollection mNotifCollection;
    public final NotifInflaterImpl mNotifInflater;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotifCoordinators mNotifPluggableCoordinators;
    public final NotifPipeline mPipelineWrapper;
    public final RenderStageManager mRenderStageManager;
    public final ShadeViewManagerFactory mShadeViewManagerFactory;

    public NotifPipelineInitializer(NotifPipeline notifPipeline, GroupCoalescer groupCoalescer, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, RenderStageManager renderStageManager, NotifCoordinators notifCoordinators, NotifInflaterImpl notifInflaterImpl, DumpManager dumpManager, ShadeViewManagerFactory shadeViewManagerFactory, NotifPipelineFlags notifPipelineFlags) {
        this.mPipelineWrapper = notifPipeline;
        this.mGroupCoalescer = groupCoalescer;
        this.mNotifCollection = notifCollection;
        this.mListBuilder = shadeListBuilder;
        this.mRenderStageManager = renderStageManager;
        this.mNotifPluggableCoordinators = notifCoordinators;
        this.mDumpManager = dumpManager;
        this.mNotifInflater = notifInflaterImpl;
        this.mShadeViewManagerFactory = shadeViewManagerFactory;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }

    public void initialize(NotificationListener notificationListener, NotificationRowBinderImpl notificationRowBinderImpl, NotificationListContainer notificationListContainer, NotifStackController notifStackController) {
        this.mDumpManager.registerDumpable("NotifPipeline", this);
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotifInflater.setRowBinder(notificationRowBinderImpl);
        }
        this.mNotifPluggableCoordinators.attach(this.mPipelineWrapper);
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mShadeViewManagerFactory.create(notificationListContainer, notifStackController).attach(this.mRenderStageManager);
        }
        this.mRenderStageManager.attach(this.mListBuilder);
        this.mListBuilder.attach(this.mNotifCollection);
        this.mNotifCollection.attach(this.mGroupCoalescer);
        this.mGroupCoalescer.attach(notificationListener);
        Log.d("NotifPipeline", "Notif pipeline initialized. rendering=" + this.mNotifPipelineFlags.isNewPipelineEnabled());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mNotifPluggableCoordinators.dump(printWriter, strArr);
        this.mGroupCoalescer.dump(printWriter, strArr);
    }
}
