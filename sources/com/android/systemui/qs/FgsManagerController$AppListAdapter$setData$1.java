package com.android.systemui.qs;

import androidx.recyclerview.widget.DiffUtil;
import com.android.systemui.qs.FgsManagerController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$AppListAdapter$setData$1 extends DiffUtil.Callback {
    public final /* synthetic */ List<FgsManagerController.RunningApp> $newData;
    public final /* synthetic */ Ref$ObjectRef<List<FgsManagerController.RunningApp>> $oldData;

    public FgsManagerController$AppListAdapter$setData$1(Ref$ObjectRef<List<FgsManagerController.RunningApp>> ref$ObjectRef, List<FgsManagerController.RunningApp> list) {
        this.$oldData = ref$ObjectRef;
        this.$newData = list;
    }

    public int getOldListSize() {
        return ((List) this.$oldData.element).size();
    }

    public int getNewListSize() {
        return this.$newData.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        return Intrinsics.areEqual(((List) this.$oldData.element).get(i), (Object) this.$newData.get(i2));
    }

    public boolean areContentsTheSame(int i, int i2) {
        return ((FgsManagerController.RunningApp) ((List) this.$oldData.element).get(i)).getStopped() == this.$newData.get(i2).getStopped();
    }
}
