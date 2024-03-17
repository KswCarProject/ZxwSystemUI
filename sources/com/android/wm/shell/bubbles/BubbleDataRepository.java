package com.android.wm.shell.bubbles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.LauncherApps;
import android.content.pm.UserInfo;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BubbleDataRepository.kt */
public final class BubbleDataRepository {
    @NotNull
    public final CoroutineScope ioScope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    @Nullable
    public Job job;
    @NotNull
    public final LauncherApps launcherApps;
    @NotNull
    public final ShellExecutor mainExecutor;
    @NotNull
    public final BubblePersistentRepository persistentRepository;
    @NotNull
    public final BubbleVolatileRepository volatileRepository;

    public BubbleDataRepository(@NotNull Context context, @NotNull LauncherApps launcherApps2, @NotNull ShellExecutor shellExecutor) {
        this.launcherApps = launcherApps2;
        this.mainExecutor = shellExecutor;
        this.volatileRepository = new BubbleVolatileRepository(launcherApps2);
        this.persistentRepository = new BubblePersistentRepository(context);
    }

    public final void addBubble(int i, @NotNull Bubble bubble) {
        addBubbles(i, CollectionsKt__CollectionsJVMKt.listOf(bubble));
    }

    public final void addBubbles(int i, @NotNull List<? extends Bubble> list) {
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.addBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubbles(int i, @NotNull List<? extends Bubble> list) {
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.removeBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubblesForUser(int i, int i2) {
        if (this.volatileRepository.removeBubblesForUser(i, i2)) {
            persistToDisk();
        }
    }

    public final void sanitizeBubbles(@NotNull List<? extends UserInfo> list) {
        Iterable<UserInfo> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (UserInfo userInfo : iterable) {
            arrayList.add(Integer.valueOf(userInfo.id));
        }
        if (this.volatileRepository.sanitizeBubbles(arrayList)) {
            persistToDisk();
        }
    }

    public final List<BubbleEntity> transform(List<? extends Bubble> list) {
        ArrayList arrayList = new ArrayList();
        for (Bubble bubble : list) {
            int identifier = bubble.getUser().getIdentifier();
            String packageName = bubble.getPackageName();
            String metadataShortcutId = bubble.getMetadataShortcutId();
            BubbleEntity bubbleEntity = null;
            if (metadataShortcutId != null) {
                String key = bubble.getKey();
                int rawDesiredHeight = bubble.getRawDesiredHeight();
                int rawDesiredHeightResId = bubble.getRawDesiredHeightResId();
                String title = bubble.getTitle();
                int taskId = bubble.getTaskId();
                LocusId locusId = bubble.getLocusId();
                bubbleEntity = new BubbleEntity(identifier, packageName, metadataShortcutId, key, rawDesiredHeight, rawDesiredHeightResId, title, taskId, locusId == null ? null : locusId.getId());
            }
            if (bubbleEntity != null) {
                arrayList.add(bubbleEntity);
            }
        }
        return arrayList;
    }

    public final void persistToDisk() {
        this.job = BuildersKt__Builders_commonKt.launch$default(this.ioScope, (CoroutineContext) null, (CoroutineStart) null, new BubbleDataRepository$persistToDisk$1(this.job, this, (Continuation<? super BubbleDataRepository$persistToDisk$1>) null), 3, (Object) null);
    }

    @NotNull
    @SuppressLint({"WrongConstant"})
    public final Job loadBubbles(int i, @NotNull Function1<? super List<? extends Bubble>, Unit> function1) {
        return BuildersKt__Builders_commonKt.launch$default(this.ioScope, (CoroutineContext) null, (CoroutineStart) null, new BubbleDataRepository$loadBubbles$1(this, i, function1, (Continuation<? super BubbleDataRepository$loadBubbles$1>) null), 3, (Object) null);
    }
}
