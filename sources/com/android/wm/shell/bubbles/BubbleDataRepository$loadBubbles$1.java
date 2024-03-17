package com.android.wm.shell.bubbles;

import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.UserHandle;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugMetadata(c = "com.android.wm.shell.bubbles.BubbleDataRepository$loadBubbles$1", f = "BubbleDataRepository.kt", l = {}, m = "invokeSuspend")
/* compiled from: BubbleDataRepository.kt */
public final class BubbleDataRepository$loadBubbles$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Function1<List<? extends Bubble>, Unit> $cb;
    public final /* synthetic */ int $userId;
    public int label;
    public final /* synthetic */ BubbleDataRepository this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BubbleDataRepository$loadBubbles$1(BubbleDataRepository bubbleDataRepository, int i, Function1<? super List<? extends Bubble>, Unit> function1, Continuation<? super BubbleDataRepository$loadBubbles$1> continuation) {
        super(2, continuation);
        this.this$0 = bubbleDataRepository;
        this.$userId = i;
        this.$cb = function1;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        return new BubbleDataRepository$loadBubbles$1(this.this$0, this.$userId, this.$cb, continuation);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$loadBubbles$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object obj2;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            List list = this.this$0.persistentRepository.readFromDisk().get(this.$userId);
            if (list == null) {
                return Unit.INSTANCE;
            }
            this.this$0.volatileRepository.addBubbles(this.$userId, list);
            Iterable<BubbleEntity> iterable = list;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity bubbleEntity : iterable) {
                arrayList.add(new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName()));
            }
            BubbleDataRepository bubbleDataRepository = this.this$0;
            ArrayList arrayList2 = new ArrayList();
            for (ShortcutKey shortcutKey : CollectionsKt___CollectionsKt.toSet(arrayList)) {
                List<ShortcutInfo> shortcuts = bubbleDataRepository.launcherApps.getShortcuts(new LauncherApps.ShortcutQuery().setPackage(shortcutKey.getPkg()).setQueryFlags(1041), UserHandle.of(shortcutKey.getUserId()));
                if (shortcuts == null) {
                    shortcuts = CollectionsKt__CollectionsKt.emptyList();
                }
                CollectionsKt__MutableCollectionsKt.addAll(arrayList2, shortcuts);
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object next : arrayList2) {
                ShortcutInfo shortcutInfo = (ShortcutInfo) next;
                ShortcutKey shortcutKey2 = new ShortcutKey(shortcutInfo.getUserId(), shortcutInfo.getPackage());
                Object obj3 = linkedHashMap.get(shortcutKey2);
                if (obj3 == null) {
                    obj3 = new ArrayList();
                    linkedHashMap.put(shortcutKey2, obj3);
                }
                ((List) obj3).add(next);
            }
            BubbleDataRepository bubbleDataRepository2 = this.this$0;
            final ArrayList arrayList3 = new ArrayList();
            for (BubbleEntity bubbleEntity2 : iterable) {
                List list2 = (List) linkedHashMap.get(new ShortcutKey(bubbleEntity2.getUserId(), bubbleEntity2.getPackageName()));
                Bubble bubble = null;
                if (list2 != null) {
                    Iterator it = list2.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            obj2 = null;
                            break;
                        }
                        obj2 = it.next();
                        if (Intrinsics.areEqual((Object) bubbleEntity2.getShortcutId(), (Object) ((ShortcutInfo) obj2).getId())) {
                            break;
                        }
                    }
                    ShortcutInfo shortcutInfo2 = (ShortcutInfo) obj2;
                    if (shortcutInfo2 != null) {
                        bubble = new Bubble(bubbleEntity2.getKey(), shortcutInfo2, bubbleEntity2.getDesiredHeight(), bubbleEntity2.getDesiredHeightResId(), bubbleEntity2.getTitle(), bubbleEntity2.getTaskId(), bubbleEntity2.getLocus(), bubbleDataRepository2.mainExecutor);
                    }
                }
                if (bubble != null) {
                    arrayList3.add(bubble);
                }
            }
            ShellExecutor access$getMainExecutor$p = this.this$0.mainExecutor;
            final Function1<List<? extends Bubble>, Unit> function1 = this.$cb;
            access$getMainExecutor$p.execute(new Runnable() {
                public final void run() {
                    function1.invoke(arrayList3);
                }
            });
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
