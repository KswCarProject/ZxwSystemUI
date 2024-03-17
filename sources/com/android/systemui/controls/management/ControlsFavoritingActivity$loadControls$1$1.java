package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$loadControls$1$1<T> implements Consumer {
    public final /* synthetic */ CharSequence $emptyZoneString;
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    public ControlsFavoritingActivity$loadControls$1$1(ControlsFavoritingActivity controlsFavoritingActivity, CharSequence charSequence) {
        this.this$0 = controlsFavoritingActivity;
        this.$emptyZoneString = charSequence;
    }

    public final void accept(@NotNull ControlsController.LoadData loadData) {
        List<ControlStatus> allControls = loadData.getAllControls();
        List<String> favoritesIds = loadData.getFavoritesIds();
        final boolean errorOnLoad = loadData.getErrorOnLoad();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object next : allControls) {
            Object structure = ((ControlStatus) next).getControl().getStructure();
            if (structure == null) {
                structure = "";
            }
            Object obj = linkedHashMap.get(structure);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(structure, obj);
            }
            ((List) obj).add(next);
        }
        ControlsFavoritingActivity controlsFavoritingActivity = this.this$0;
        CharSequence charSequence = this.$emptyZoneString;
        ArrayList arrayList = new ArrayList(linkedHashMap.size());
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            arrayList.add(new StructureContainer((CharSequence) entry.getKey(), new AllModel((List) entry.getValue(), favoritesIds, charSequence, controlsFavoritingActivity.controlsModelCallback)));
        }
        Comparator access$getComparator$p = this.this$0.comparator;
        if (access$getComparator$p == null) {
            access$getComparator$p = null;
        }
        controlsFavoritingActivity.listOfStructures = CollectionsKt___CollectionsKt.sortedWith(arrayList, access$getComparator$p);
        List access$getListOfStructures$p = this.this$0.listOfStructures;
        ControlsFavoritingActivity controlsFavoritingActivity2 = this.this$0;
        Iterator it = access$getListOfStructures$p.iterator();
        final int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (Intrinsics.areEqual((Object) ((StructureContainer) it.next()).getStructureName(), (Object) controlsFavoritingActivity2.structureExtra)) {
                break;
            } else {
                i++;
            }
        }
        if (i == -1) {
            i = 0;
        }
        if (this.this$0.getIntent().getBooleanExtra("extra_single_structure", false)) {
            ControlsFavoritingActivity controlsFavoritingActivity3 = this.this$0;
            controlsFavoritingActivity3.listOfStructures = CollectionsKt__CollectionsJVMKt.listOf(controlsFavoritingActivity3.listOfStructures.get(i));
        }
        Executor access$getExecutor$p = this.this$0.executor;
        final ControlsFavoritingActivity controlsFavoritingActivity4 = this.this$0;
        access$getExecutor$p.execute(new Runnable() {
            /* JADX WARNING: type inference failed for: r8v2, types: [androidx.viewpager2.widget.ViewPager2] */
            /* JADX WARNING: Multi-variable type inference failed */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final void run() {
                /*
                    r8 = this;
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    androidx.viewpager2.widget.ViewPager2 r0 = r0.structurePager
                    r1 = 0
                    if (r0 != 0) goto L_0x000a
                    r0 = r1
                L_0x000a:
                    com.android.systemui.controls.management.StructureAdapter r2 = new com.android.systemui.controls.management.StructureAdapter
                    com.android.systemui.controls.management.ControlsFavoritingActivity r3 = r10
                    java.util.List r3 = r3.listOfStructures
                    r2.<init>(r3)
                    r0.setAdapter(r2)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    androidx.viewpager2.widget.ViewPager2 r0 = r0.structurePager
                    if (r0 != 0) goto L_0x0021
                    r0 = r1
                L_0x0021:
                    int r2 = r3
                    r0.setCurrentItem(r2)
                    boolean r0 = r11
                    r2 = 0
                    r3 = 1
                    r4 = 8
                    if (r0 == 0) goto L_0x0063
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    android.widget.TextView r0 = r0.statusText
                    if (r0 != 0) goto L_0x0037
                    r0 = r1
                L_0x0037:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r5 = r10
                    android.content.res.Resources r5 = r5.getResources()
                    int r6 = com.android.systemui.R$string.controls_favorite_load_error
                    java.lang.Object[] r3 = new java.lang.Object[r3]
                    com.android.systemui.controls.management.ControlsFavoritingActivity r7 = r10
                    java.lang.CharSequence r7 = r7.appName
                    if (r7 != 0) goto L_0x004b
                    java.lang.String r7 = ""
                L_0x004b:
                    r3[r2] = r7
                    java.lang.String r2 = r5.getString(r6, r3)
                    r0.setText(r2)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r8 = r10
                    android.widget.TextView r8 = r8.subtitleView
                    if (r8 != 0) goto L_0x005d
                    goto L_0x005e
                L_0x005d:
                    r1 = r8
                L_0x005e:
                    r1.setVisibility(r4)
                    goto L_0x010c
                L_0x0063:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    java.util.List r0 = r0.listOfStructures
                    boolean r0 = r0.isEmpty()
                    if (r0 == 0) goto L_0x0096
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    android.widget.TextView r0 = r0.statusText
                    if (r0 != 0) goto L_0x0078
                    r0 = r1
                L_0x0078:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r2 = r10
                    android.content.res.Resources r2 = r2.getResources()
                    int r3 = com.android.systemui.R$string.controls_favorite_load_none
                    java.lang.String r2 = r2.getString(r3)
                    r0.setText(r2)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r8 = r10
                    android.widget.TextView r8 = r8.subtitleView
                    if (r8 != 0) goto L_0x0090
                    goto L_0x0091
                L_0x0090:
                    r1 = r8
                L_0x0091:
                    r1.setVisibility(r4)
                    goto L_0x010c
                L_0x0096:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    android.widget.TextView r0 = r0.statusText
                    if (r0 != 0) goto L_0x009f
                    r0 = r1
                L_0x009f:
                    r0.setVisibility(r4)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    com.android.systemui.controls.management.ManagementPageIndicator r0 = r0.pageIndicator
                    if (r0 != 0) goto L_0x00ab
                    r0 = r1
                L_0x00ab:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r4 = r10
                    java.util.List r4 = r4.listOfStructures
                    int r4 = r4.size()
                    r0.setNumPages(r4)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    com.android.systemui.controls.management.ManagementPageIndicator r0 = r0.pageIndicator
                    if (r0 != 0) goto L_0x00c1
                    r0 = r1
                L_0x00c1:
                    r4 = 0
                    r0.setLocation(r4)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r0 = r10
                    com.android.systemui.controls.management.ManagementPageIndicator r0 = r0.pageIndicator
                    if (r0 != 0) goto L_0x00ce
                    r0 = r1
                L_0x00ce:
                    com.android.systemui.controls.management.ControlsFavoritingActivity r4 = r10
                    java.util.List r4 = r4.listOfStructures
                    int r4 = r4.size()
                    if (r4 <= r3) goto L_0x00db
                    goto L_0x00dc
                L_0x00db:
                    r2 = 4
                L_0x00dc:
                    r0.setVisibility(r2)
                    com.android.systemui.controls.management.ControlsAnimations r0 = com.android.systemui.controls.management.ControlsAnimations.INSTANCE
                    com.android.systemui.controls.management.ControlsFavoritingActivity r2 = r10
                    com.android.systemui.controls.management.ManagementPageIndicator r2 = r2.pageIndicator
                    if (r2 != 0) goto L_0x00ea
                    r2 = r1
                L_0x00ea:
                    android.animation.Animator r2 = r0.enterAnimation(r2)
                    com.android.systemui.controls.management.ControlsFavoritingActivity r3 = r10
                    com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1 r4 = new com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1
                    r4.<init>(r3)
                    r2.addListener(r4)
                    r2.start()
                    com.android.systemui.controls.management.ControlsFavoritingActivity r8 = r10
                    androidx.viewpager2.widget.ViewPager2 r8 = r8.structurePager
                    if (r8 != 0) goto L_0x0104
                    goto L_0x0105
                L_0x0104:
                    r1 = r8
                L_0x0105:
                    android.animation.Animator r8 = r0.enterAnimation(r1)
                    r8.start()
                L_0x010c:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1.AnonymousClass2.run():void");
            }
        });
    }
}
