package com.android.systemui.keyguard;

import android.content.res.ColorStateList;
import android.os.SystemClock;
import android.text.TextUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyguardIndicationRotateTextViewController extends ViewController<KeyguardIndicationTextView> implements Dumpable {
    public int mCurrIndicationType = -1;
    public CharSequence mCurrMessage;
    public final DelayableExecutor mExecutor;
    public final Map<Integer, KeyguardIndication> mIndicationMessages = new HashMap();
    public final List<Integer> mIndicationQueue = new LinkedList();
    public final ColorStateList mInitialTextColorState;
    public boolean mIsDozing;
    public long mLastIndicationSwitch;
    public final float mMaxAlpha;
    public ShowNextIndication mShowNextIndicationRunnable;
    public final StatusBarStateController mStatusBarStateController;
    public StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onDozeAmountChanged(float f, float f2) {
            ((KeyguardIndicationTextView) KeyguardIndicationRotateTextViewController.this.mView).setAlpha((1.0f - f) * KeyguardIndicationRotateTextViewController.this.mMaxAlpha);
        }

        public void onDozingChanged(boolean z) {
            if (z != KeyguardIndicationRotateTextViewController.this.mIsDozing) {
                KeyguardIndicationRotateTextViewController.this.mIsDozing = z;
                if (KeyguardIndicationRotateTextViewController.this.mIsDozing) {
                    KeyguardIndicationRotateTextViewController.this.showIndication(-1);
                } else if (KeyguardIndicationRotateTextViewController.this.mIndicationQueue.size() > 0) {
                    KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = KeyguardIndicationRotateTextViewController.this;
                    keyguardIndicationRotateTextViewController.showIndication(((Integer) keyguardIndicationRotateTextViewController.mIndicationQueue.get(0)).intValue());
                }
            }
        }
    };

    public KeyguardIndicationRotateTextViewController(KeyguardIndicationTextView keyguardIndicationTextView, DelayableExecutor delayableExecutor, StatusBarStateController statusBarStateController) {
        super(keyguardIndicationTextView);
        this.mMaxAlpha = keyguardIndicationTextView.getAlpha();
        this.mExecutor = delayableExecutor;
        T t = this.mView;
        this.mInitialTextColorState = t != null ? ((KeyguardIndicationTextView) t).getTextColors() : ColorStateList.valueOf(-1);
        this.mStatusBarStateController = statusBarStateController;
        init();
    }

    public void onViewAttached() {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
    }

    public void onViewDetached() {
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        cancelScheduledIndication();
    }

    public void updateIndication(int i, KeyguardIndication keyguardIndication, boolean z) {
        if (i != 10) {
            long minVisibilityMillis = getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(this.mCurrIndicationType)));
            boolean z2 = true;
            boolean z3 = keyguardIndication != null && !TextUtils.isEmpty(keyguardIndication.getMessage());
            if (!z3) {
                this.mIndicationMessages.remove(Integer.valueOf(i));
                this.mIndicationQueue.removeIf(new KeyguardIndicationRotateTextViewController$$ExternalSyntheticLambda0(i));
            } else {
                if (!this.mIndicationQueue.contains(Integer.valueOf(i))) {
                    this.mIndicationQueue.add(Integer.valueOf(i));
                }
                this.mIndicationMessages.put(Integer.valueOf(i), keyguardIndication);
            }
            if (!this.mIsDozing) {
                long uptimeMillis = SystemClock.uptimeMillis() - this.mLastIndicationSwitch;
                if (uptimeMillis < minVisibilityMillis) {
                    z2 = false;
                }
                if (z3) {
                    int i2 = this.mCurrIndicationType;
                    if (i2 == -1 || i2 == i) {
                        showIndication(i);
                    } else if (z) {
                        if (z2) {
                            showIndication(i);
                            return;
                        }
                        this.mIndicationQueue.removeIf(new KeyguardIndicationRotateTextViewController$$ExternalSyntheticLambda1(i));
                        this.mIndicationQueue.add(0, Integer.valueOf(i));
                        scheduleShowNextIndication(minVisibilityMillis - uptimeMillis);
                    } else if (!isNextIndicationScheduled()) {
                        long max = Math.max(getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(i))), 3500);
                        if (uptimeMillis >= max) {
                            showIndication(i);
                        } else {
                            scheduleShowNextIndication(max - uptimeMillis);
                        }
                    }
                } else if (this.mCurrIndicationType == i && !z3 && z) {
                    if (z2) {
                        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
                        if (showNextIndication != null) {
                            showNextIndication.runImmediately();
                        } else {
                            showIndication(-1);
                        }
                    } else {
                        scheduleShowNextIndication(minVisibilityMillis - uptimeMillis);
                    }
                }
            }
        }
    }

    public static /* synthetic */ boolean lambda$updateIndication$0(int i, Integer num) {
        return num.intValue() == i;
    }

    public static /* synthetic */ boolean lambda$updateIndication$1(int i, Integer num) {
        return num.intValue() == i;
    }

    public void hideIndication(int i) {
        if (this.mIndicationMessages.containsKey(Integer.valueOf(i)) && !TextUtils.isEmpty(this.mIndicationMessages.get(Integer.valueOf(i)).getMessage())) {
            updateIndication(i, (KeyguardIndication) null, true);
        }
    }

    public void showTransient(CharSequence charSequence) {
        updateIndication(5, new KeyguardIndication.Builder().setMessage(charSequence).setMinVisibilityMillis(2600L).setTextColor(this.mInitialTextColorState).build(), true);
    }

    public void hideTransient() {
        hideIndication(5);
    }

    public boolean hasIndications() {
        return this.mIndicationMessages.keySet().size() > 0;
    }

    public void clearMessages() {
        this.mCurrIndicationType = -1;
        this.mIndicationQueue.clear();
        this.mIndicationMessages.clear();
        ((KeyguardIndicationTextView) this.mView).clearMessages();
    }

    public final void showIndication(int i) {
        cancelScheduledIndication();
        CharSequence charSequence = this.mCurrMessage;
        int i2 = this.mCurrIndicationType;
        this.mCurrIndicationType = i;
        this.mCurrMessage = this.mIndicationMessages.get(Integer.valueOf(i)) != null ? this.mIndicationMessages.get(Integer.valueOf(i)).getMessage() : null;
        this.mIndicationQueue.removeIf(new KeyguardIndicationRotateTextViewController$$ExternalSyntheticLambda2(i));
        if (this.mCurrIndicationType != -1) {
            this.mIndicationQueue.add(Integer.valueOf(i));
        }
        this.mLastIndicationSwitch = SystemClock.uptimeMillis();
        if (!TextUtils.equals(charSequence, this.mCurrMessage) || i2 != this.mCurrIndicationType) {
            ((KeyguardIndicationTextView) this.mView).switchIndication(this.mIndicationMessages.get(Integer.valueOf(i)));
        }
        if (this.mCurrIndicationType != -1 && this.mIndicationQueue.size() > 1) {
            scheduleShowNextIndication(Math.max(getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(i))), 3500));
        }
    }

    public static /* synthetic */ boolean lambda$showIndication$2(int i, Integer num) {
        return num.intValue() == i;
    }

    public final long getMinVisibilityMillis(KeyguardIndication keyguardIndication) {
        if (keyguardIndication == null || keyguardIndication.getMinVisibilityMillis() == null) {
            return 0;
        }
        return keyguardIndication.getMinVisibilityMillis().longValue();
    }

    public boolean isNextIndicationScheduled() {
        return this.mShowNextIndicationRunnable != null;
    }

    public final void scheduleShowNextIndication(long j) {
        cancelScheduledIndication();
        this.mShowNextIndicationRunnable = new ShowNextIndication(j);
    }

    public final void cancelScheduledIndication() {
        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
        if (showNextIndication != null) {
            showNextIndication.cancelDelayedExecution();
            this.mShowNextIndicationRunnable = null;
        }
    }

    public class ShowNextIndication {
        public Runnable mCancelDelayedRunnable;
        public final Runnable mShowIndicationRunnable;

        public ShowNextIndication(long j) {
            KeyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0 keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0 = new KeyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0(this);
            this.mShowIndicationRunnable = keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0;
            this.mCancelDelayedRunnable = KeyguardIndicationRotateTextViewController.this.mExecutor.executeDelayed(keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0, j);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            int i;
            if (KeyguardIndicationRotateTextViewController.this.mIndicationQueue.size() == 0) {
                i = -1;
            } else {
                i = ((Integer) KeyguardIndicationRotateTextViewController.this.mIndicationQueue.get(0)).intValue();
            }
            KeyguardIndicationRotateTextViewController.this.showIndication(i);
        }

        public void runImmediately() {
            cancelDelayedExecution();
            this.mShowIndicationRunnable.run();
        }

        public void cancelDelayedExecution() {
            Runnable runnable = this.mCancelDelayedRunnable;
            if (runnable != null) {
                runnable.run();
                this.mCancelDelayedRunnable = null;
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardIndicationRotatingTextViewController:");
        printWriter.println("    currentMessage=" + ((KeyguardIndicationTextView) this.mView).getText());
        printWriter.println("    dozing:" + this.mIsDozing);
        printWriter.println("    queue:" + this.mIndicationQueue.toString());
        printWriter.println("    showNextIndicationRunnable:" + this.mShowNextIndicationRunnable);
        if (hasIndications()) {
            printWriter.println("    All messages:");
            for (Integer intValue : this.mIndicationMessages.keySet()) {
                int intValue2 = intValue.intValue();
                printWriter.println("        type=" + intValue2 + " " + this.mIndicationMessages.get(Integer.valueOf(intValue2)));
            }
        }
    }
}
