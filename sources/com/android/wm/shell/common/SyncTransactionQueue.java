package com.android.wm.shell.common;

import android.util.Slog;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.wm.shell.transition.LegacyTransitions$ILegacyTransition;
import com.android.wm.shell.transition.LegacyTransitions$LegacyTransition;
import java.util.ArrayList;

public final class SyncTransactionQueue {
    public SyncCallback mInFlight = null;
    public final ShellExecutor mMainExecutor;
    public final Runnable mOnReplyTimeout = new SyncTransactionQueue$$ExternalSyntheticLambda0(this);
    public final ArrayList<SyncCallback> mQueue = new ArrayList<>();
    public final ArrayList<TransactionRunnable> mRunnables = new ArrayList<>();
    public final TransactionPool mTransactionPool;

    public interface TransactionRunnable {
        void runWithTransaction(SurfaceControl.Transaction transaction);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mQueue) {
            SyncCallback syncCallback = this.mInFlight;
            if (syncCallback != null && this.mQueue.contains(syncCallback)) {
                Slog.w("SyncTransactionQueue", "Sync Transaction timed-out: " + this.mInFlight.mWCT);
                SyncCallback syncCallback2 = this.mInFlight;
                syncCallback2.onTransactionReady(syncCallback2.mId, new SurfaceControl.Transaction());
            }
        }
    }

    public SyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
    }

    public void queue(WindowContainerTransaction windowContainerTransaction) {
        if (!windowContainerTransaction.isEmpty()) {
            SyncCallback syncCallback = new SyncCallback(windowContainerTransaction);
            synchronized (this.mQueue) {
                this.mQueue.add(syncCallback);
                if (this.mQueue.size() == 1) {
                    syncCallback.send();
                }
            }
        }
    }

    public void queue(LegacyTransitions$ILegacyTransition legacyTransitions$ILegacyTransition, int i, WindowContainerTransaction windowContainerTransaction) {
        if (!windowContainerTransaction.isEmpty()) {
            SyncCallback syncCallback = new SyncCallback(legacyTransitions$ILegacyTransition, i, windowContainerTransaction);
            synchronized (this.mQueue) {
                this.mQueue.add(syncCallback);
                if (this.mQueue.size() == 1) {
                    syncCallback.send();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean queueIfWaiting(android.window.WindowContainerTransaction r4) {
        /*
            r3 = this;
            boolean r0 = r4.isEmpty()
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r0 = r3.mQueue
            monitor-enter(r0)
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r2 = r3.mQueue     // Catch:{ all -> 0x002d }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x0015
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r1
        L_0x0015:
            com.android.wm.shell.common.SyncTransactionQueue$SyncCallback r1 = new com.android.wm.shell.common.SyncTransactionQueue$SyncCallback     // Catch:{ all -> 0x002d }
            r1.<init>(r4)     // Catch:{ all -> 0x002d }
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r4 = r3.mQueue     // Catch:{ all -> 0x002d }
            r4.add(r1)     // Catch:{ all -> 0x002d }
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r3 = r3.mQueue     // Catch:{ all -> 0x002d }
            int r3 = r3.size()     // Catch:{ all -> 0x002d }
            r4 = 1
            if (r3 != r4) goto L_0x002b
            r1.send()     // Catch:{ all -> 0x002d }
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r4
        L_0x002d:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.SyncTransactionQueue.queueIfWaiting(android.window.WindowContainerTransaction):boolean");
    }

    public void runInSync(TransactionRunnable transactionRunnable) {
        synchronized (this.mQueue) {
            if (this.mInFlight != null) {
                this.mRunnables.add(transactionRunnable);
                return;
            }
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            transactionRunnable.runWithTransaction(acquire);
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    public final void onTransactionReceived(SurfaceControl.Transaction transaction) {
        int size = this.mRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mRunnables.get(i).runWithTransaction(transaction);
        }
        this.mRunnables.subList(0, size).clear();
    }

    public class SyncCallback extends WindowContainerTransactionCallback {
        public int mId = -1;
        public final LegacyTransitions$LegacyTransition mLegacyTransition;
        public final WindowContainerTransaction mWCT;

        public SyncCallback(WindowContainerTransaction windowContainerTransaction) {
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = null;
        }

        public SyncCallback(LegacyTransitions$ILegacyTransition legacyTransitions$ILegacyTransition, int i, WindowContainerTransaction windowContainerTransaction) {
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = new LegacyTransitions$LegacyTransition(i, legacyTransitions$ILegacyTransition);
        }

        public void send() {
            if (SyncTransactionQueue.this.mInFlight != this) {
                if (SyncTransactionQueue.this.mInFlight == null) {
                    SyncTransactionQueue.this.mInFlight = this;
                    if (this.mLegacyTransition != null) {
                        this.mId = new WindowOrganizer().startLegacyTransition(this.mLegacyTransition.getType(), this.mLegacyTransition.getAdapter(), this, this.mWCT);
                    } else {
                        this.mId = new WindowOrganizer().applySyncTransaction(this.mWCT, this);
                    }
                    SyncTransactionQueue.this.mMainExecutor.executeDelayed(SyncTransactionQueue.this.mOnReplyTimeout, 5300);
                    return;
                }
                throw new IllegalStateException("Sync Transactions must be serialized. In Flight: " + SyncTransactionQueue.this.mInFlight.mId + " - " + SyncTransactionQueue.this.mInFlight.mWCT);
            }
        }

        public void onTransactionReady(int i, SurfaceControl.Transaction transaction) {
            SyncTransactionQueue.this.mMainExecutor.execute(new SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0(this, i, transaction));
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x009b, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public /* synthetic */ void lambda$onTransactionReady$0(int r4, android.view.SurfaceControl.Transaction r5) {
            /*
                r3 = this;
                com.android.wm.shell.common.SyncTransactionQueue r0 = com.android.wm.shell.common.SyncTransactionQueue.this
                java.util.ArrayList r0 = r0.mQueue
                monitor-enter(r0)
                int r1 = r3.mId     // Catch:{ all -> 0x009c }
                if (r1 == r4) goto L_0x002d
                java.lang.String r5 = "SyncTransactionQueue"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x009c }
                r1.<init>()     // Catch:{ all -> 0x009c }
                java.lang.String r2 = "Got an unexpected onTransactionReady. Expected "
                r1.append(r2)     // Catch:{ all -> 0x009c }
                int r3 = r3.mId     // Catch:{ all -> 0x009c }
                r1.append(r3)     // Catch:{ all -> 0x009c }
                java.lang.String r3 = " but got "
                r1.append(r3)     // Catch:{ all -> 0x009c }
                r1.append(r4)     // Catch:{ all -> 0x009c }
                java.lang.String r3 = r1.toString()     // Catch:{ all -> 0x009c }
                android.util.Slog.e(r5, r3)     // Catch:{ all -> 0x009c }
                monitor-exit(r0)     // Catch:{ all -> 0x009c }
                return
            L_0x002d:
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                r1 = 0
                r4.mInFlight = r1     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.ShellExecutor r4 = r4.mMainExecutor     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.SyncTransactionQueue r1 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                java.lang.Runnable r1 = r1.mOnReplyTimeout     // Catch:{ all -> 0x009c }
                r4.removeCallbacks(r1)     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                java.util.ArrayList r4 = r4.mQueue     // Catch:{ all -> 0x009c }
                r4.remove(r3)     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                r4.onTransactionReceived(r5)     // Catch:{ all -> 0x009c }
                com.android.wm.shell.transition.LegacyTransitions$LegacyTransition r4 = r3.mLegacyTransition     // Catch:{ all -> 0x009c }
                if (r4 == 0) goto L_0x0078
                android.window.IWindowContainerTransactionCallback r4 = r4.getSyncCallback()     // Catch:{ RemoteException -> 0x005e }
                int r1 = r3.mId     // Catch:{ RemoteException -> 0x005e }
                r4.onTransactionReady(r1, r5)     // Catch:{ RemoteException -> 0x005e }
                goto L_0x007e
            L_0x005e:
                r4 = move-exception
                java.lang.String r5 = "SyncTransactionQueue"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x009c }
                r1.<init>()     // Catch:{ all -> 0x009c }
                java.lang.String r2 = "Error sending callback to legacy transition: "
                r1.append(r2)     // Catch:{ all -> 0x009c }
                int r2 = r3.mId     // Catch:{ all -> 0x009c }
                r1.append(r2)     // Catch:{ all -> 0x009c }
                java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x009c }
                android.util.Slog.e(r5, r1, r4)     // Catch:{ all -> 0x009c }
                goto L_0x007e
            L_0x0078:
                r5.apply()     // Catch:{ all -> 0x009c }
                r5.close()     // Catch:{ all -> 0x009c }
            L_0x007e:
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                java.util.ArrayList r4 = r4.mQueue     // Catch:{ all -> 0x009c }
                boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x009c }
                if (r4 != 0) goto L_0x009a
                com.android.wm.shell.common.SyncTransactionQueue r3 = com.android.wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009c }
                java.util.ArrayList r3 = r3.mQueue     // Catch:{ all -> 0x009c }
                r4 = 0
                java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x009c }
                com.android.wm.shell.common.SyncTransactionQueue$SyncCallback r3 = (com.android.wm.shell.common.SyncTransactionQueue.SyncCallback) r3     // Catch:{ all -> 0x009c }
                r3.send()     // Catch:{ all -> 0x009c }
            L_0x009a:
                monitor-exit(r0)     // Catch:{ all -> 0x009c }
                return
            L_0x009c:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x009c }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.SyncTransactionQueue.SyncCallback.lambda$onTransactionReady$0(int, android.view.SurfaceControl$Transaction):void");
        }
    }
}
