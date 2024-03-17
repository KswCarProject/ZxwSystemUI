package kotlin.concurrent;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/* compiled from: Thread.kt */
public final class ThreadsKt$thread$thread$1 extends Thread {
    public final /* synthetic */ Function0<Unit> $block;

    public ThreadsKt$thread$thread$1(Function0<Unit> function0) {
        this.$block = function0;
    }

    public void run() {
        this.$block.invoke();
    }
}
