package okio;

import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

/* compiled from: SegmentPool.kt */
public final class SegmentPool {
    public static final int HASH_BUCKET_COUNT;
    @NotNull
    public static final SegmentPool INSTANCE = new SegmentPool();
    @NotNull
    public static final Segment LOCK = new Segment(new byte[0], 0, 0, false, false);
    public static final int MAX_SIZE = 65536;
    @NotNull
    public static final AtomicReference<Segment>[] hashBuckets;

    static {
        int highestOneBit = Integer.highestOneBit((Runtime.getRuntime().availableProcessors() * 2) - 1);
        HASH_BUCKET_COUNT = highestOneBit;
        AtomicReference<Segment>[] atomicReferenceArr = new AtomicReference[highestOneBit];
        for (int i = 0; i < highestOneBit; i++) {
            atomicReferenceArr[i] = new AtomicReference<>();
        }
        hashBuckets = atomicReferenceArr;
    }

    @NotNull
    public static final Segment take() {
        AtomicReference<Segment> firstRef = INSTANCE.firstRef();
        Segment segment = LOCK;
        Segment andSet = firstRef.getAndSet(segment);
        if (andSet == segment) {
            return new Segment();
        }
        if (andSet == null) {
            firstRef.set((Object) null);
            return new Segment();
        }
        firstRef.set(andSet.next);
        andSet.next = null;
        andSet.limit = 0;
        return andSet;
    }

    public static final void recycle(@NotNull Segment segment) {
        AtomicReference<Segment> firstRef;
        Segment segment2;
        int i;
        if (!(segment.next == null && segment.prev == null)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!segment.shared && (segment2 = firstRef.get()) != LOCK) {
            if (segment2 == null) {
                i = 0;
            } else {
                i = segment2.limit;
            }
            if (i < MAX_SIZE) {
                segment.next = segment2;
                segment.pos = 0;
                segment.limit = i + 8192;
                if (!(firstRef = INSTANCE.firstRef()).compareAndSet(segment2, segment)) {
                    segment.next = null;
                }
            }
        }
    }

    public final AtomicReference<Segment> firstRef() {
        return hashBuckets[(int) (Thread.currentThread().getId() & (((long) HASH_BUCKET_COUNT) - 1))];
    }
}
