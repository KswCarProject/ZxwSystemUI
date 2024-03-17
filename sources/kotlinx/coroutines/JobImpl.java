package kotlinx.coroutines;

import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public class JobImpl extends JobSupport implements CompletableJob {
    public final boolean handlesException = handlesException();

    public boolean getOnCancelComplete$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return true;
    }

    public JobImpl(@Nullable Job job) {
        super(true);
        initParentJob(job);
    }

    public boolean getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this.handlesException;
    }

    public final boolean handlesException() {
        JobSupport job;
        ChildHandle parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        ChildHandleNode childHandleNode = parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof ChildHandleNode ? (ChildHandleNode) parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines : null;
        JobSupport job2 = childHandleNode == null ? null : childHandleNode.getJob();
        if (job2 == null) {
            return false;
        }
        while (!job2.getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) {
            ChildHandle parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines2 = job2.getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            ChildHandleNode childHandleNode2 = parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines2 instanceof ChildHandleNode ? (ChildHandleNode) parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines2 : null;
            if (childHandleNode2 == null) {
                job = null;
                continue;
            } else {
                job = childHandleNode2.getJob();
                continue;
            }
            if (job2 == null) {
                return false;
            }
        }
        return true;
    }
}
