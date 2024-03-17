package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager$expandableViewHeaderState$1 implements NotificationSectionsManager.SectionUpdateState<T> {
    public final /* synthetic */ T $header;
    @Nullable
    public Integer currentPosition;
    @NotNull
    public final T header;
    @Nullable
    public Integer targetPosition;
    public final /* synthetic */ NotificationSectionsManager this$0;

    public NotificationSectionsManager$expandableViewHeaderState$1(T t, NotificationSectionsManager notificationSectionsManager) {
        this.$header = t;
        this.this$0 = notificationSectionsManager;
        this.header = t;
    }

    @Nullable
    public Integer getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(@Nullable Integer num) {
        this.currentPosition = num;
    }

    @Nullable
    public Integer getTargetPosition() {
        return this.targetPosition;
    }

    public void setTargetPosition(@Nullable Integer num) {
        this.targetPosition = num;
    }

    public void adjustViewPosition() {
        this.this$0.notifPipelineFlags.checkLegacyPipelineEnabled();
        Integer targetPosition2 = getTargetPosition();
        Integer currentPosition2 = getCurrentPosition();
        NotificationStackScrollLayout notificationStackScrollLayout = null;
        if (targetPosition2 == null) {
            if (currentPosition2 != null) {
                NotificationStackScrollLayout access$getParent$p = this.this$0.parent;
                if (access$getParent$p != null) {
                    notificationStackScrollLayout = access$getParent$p;
                }
                notificationStackScrollLayout.removeView(this.$header);
            }
        } else if (currentPosition2 == null) {
            this.$header.removeFromTransientContainer();
            NotificationStackScrollLayout access$getParent$p2 = this.this$0.parent;
            if (access$getParent$p2 != null) {
                notificationStackScrollLayout = access$getParent$p2;
            }
            notificationStackScrollLayout.addView(this.$header, targetPosition2.intValue());
        } else {
            NotificationStackScrollLayout access$getParent$p3 = this.this$0.parent;
            if (access$getParent$p3 != null) {
                notificationStackScrollLayout = access$getParent$p3;
            }
            notificationStackScrollLayout.changeViewPosition(this.$header, targetPosition2.intValue());
        }
    }
}
