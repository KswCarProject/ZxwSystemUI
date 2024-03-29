package com.android.wm.shell.onehanded;

import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;

public class OneHandedTouchHandler implements OneHandedTransitionCallback {
    public InputEventReceiver mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public boolean mIsEnabled;
    public boolean mIsInOutsideRegion;
    public boolean mIsOnStopTransitioning;
    public final Rect mLastUpdatedBounds = new Rect();
    public final ShellExecutor mMainExecutor;
    public final OneHandedTimeoutHandler mTimeoutHandler;
    public OneHandedTouchEventCallback mTouchEventCallback;

    public interface OneHandedTouchEventCallback {
        void onStop();
    }

    public OneHandedTouchHandler(OneHandedTimeoutHandler oneHandedTimeoutHandler, ShellExecutor shellExecutor) {
        this.mTimeoutHandler = oneHandedTimeoutHandler;
        this.mMainExecutor = shellExecutor;
        updateIsEnabled();
    }

    public void onOneHandedEnabled(boolean z) {
        this.mIsEnabled = z;
        updateIsEnabled();
    }

    public void registerTouchEventListener(OneHandedTouchEventCallback oneHandedTouchEventCallback) {
        this.mTouchEventCallback = oneHandedTouchEventCallback;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001b, code lost:
        if (r3 != 3) goto L_0x003f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean onMotionEvent(android.view.MotionEvent r3) {
        /*
            r2 = this;
            float r0 = r3.getX()
            float r1 = r3.getY()
            boolean r0 = r2.isWithinTouchOutsideRegion(r0, r1)
            r2.mIsInOutsideRegion = r0
            int r3 = r3.getAction()
            r0 = 1
            if (r3 == 0) goto L_0x0036
            if (r3 == r0) goto L_0x001e
            r1 = 2
            if (r3 == r1) goto L_0x0036
            r1 = 3
            if (r3 == r1) goto L_0x001e
            goto L_0x003f
        L_0x001e:
            com.android.wm.shell.onehanded.OneHandedTimeoutHandler r3 = r2.mTimeoutHandler
            r3.resetTimer()
            boolean r3 = r2.mIsInOutsideRegion
            if (r3 == 0) goto L_0x0032
            boolean r3 = r2.mIsOnStopTransitioning
            if (r3 != 0) goto L_0x0032
            com.android.wm.shell.onehanded.OneHandedTouchHandler$OneHandedTouchEventCallback r3 = r2.mTouchEventCallback
            r3.onStop()
            r2.mIsOnStopTransitioning = r0
        L_0x0032:
            r3 = 0
            r2.mIsInOutsideRegion = r3
            goto L_0x003f
        L_0x0036:
            boolean r3 = r2.mIsInOutsideRegion
            if (r3 != 0) goto L_0x003f
            com.android.wm.shell.onehanded.OneHandedTimeoutHandler r2 = r2.mTimeoutHandler
            r2.resetTimer()
        L_0x003f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.onehanded.OneHandedTouchHandler.onMotionEvent(android.view.MotionEvent):boolean");
    }

    public final void disposeInputChannel() {
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
    }

    public final boolean isWithinTouchOutsideRegion(float f, float f2) {
        return Math.round(f2) < this.mLastUpdatedBounds.top;
    }

    public final void onInputEvent(InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            onMotionEvent((MotionEvent) inputEvent);
        }
    }

    public final void updateIsEnabled() {
        disposeInputChannel();
        if (this.mIsEnabled) {
            this.mInputMonitor = InputManager.getInstance().monitorGestureInput("onehanded-touch", 0);
            try {
                this.mMainExecutor.executeBlocking(new OneHandedTouchHandler$$ExternalSyntheticLambda0(this));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to create input event receiver", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateIsEnabled$0() {
        this.mInputEventReceiver = new EventReceiver(this.mInputMonitor.getInputChannel(), Looper.myLooper());
    }

    public void onStartFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
    }

    public void onStopFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
        this.mIsOnStopTransitioning = false;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedTouchHandler");
        printWriter.print("  mLastUpdatedBounds=");
        printWriter.println(this.mLastUpdatedBounds);
    }

    public class EventReceiver extends InputEventReceiver {
        public EventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEvent(InputEvent inputEvent) {
            OneHandedTouchHandler.this.onInputEvent(inputEvent);
            finishInputEvent(inputEvent, true);
        }
    }
}
