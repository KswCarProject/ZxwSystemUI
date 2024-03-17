package com.android.keyguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.telephony.PinResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;

public class KeyguardSimPukViewController extends KeyguardPinBasedInputViewController<KeyguardSimPukView> {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public CheckSimPuk mCheckSimPukThread;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public String mPinText;
    public String mPukText;
    public int mRemainingAttempts;
    public AlertDialog mRemainingAttemptsDialog;
    public boolean mShowDefaultMessage;
    public ImageView mSimImageView;
    public ProgressDialog mSimUnlockProgressDialog;
    public StateMachine mStateMachine = new StateMachine();
    public int mSubId = -1;
    public final TelephonyManager mTelephonyManager;
    public KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onSimStateChanged(int i, int i2, int i3) {
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "onSimStateChanged(subId=" + i + ",state=" + i3 + ")");
            }
            if (i3 == 5) {
                KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                KeyguardSimPukViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
                return;
            }
            KeyguardSimPukViewController.this.resetState();
        }
    };

    public boolean shouldLockout(long j) {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardSimPukViewController(KeyguardSimPukView keyguardSimPukView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPukView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mSimImageView = (ImageView) ((KeyguardSimPukView) this.mView).findViewById(R$id.keyguard_sim);
    }

    public void onViewAttached() {
        super.onViewAttached();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    public void onViewDetached() {
        super.onViewDetached();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
    }

    public void resetState() {
        super.resetState();
        this.mStateMachine.reset();
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardSimPukView) this.mView).reloadColors();
    }

    public void verifyPasswordAndUnlock() {
        this.mStateMachine.next();
    }

    public class StateMachine {
        public int mState;

        public StateMachine() {
            this.mState = 0;
        }

        public void next() {
            int i;
            int i2 = this.mState;
            if (i2 == 0) {
                if (KeyguardSimPukViewController.this.checkPuk()) {
                    this.mState = 1;
                    i = R$string.kg_puk_enter_pin_hint;
                } else {
                    i = R$string.kg_invalid_sim_puk_hint;
                }
            } else if (i2 == 1) {
                if (KeyguardSimPukViewController.this.checkPin()) {
                    this.mState = 2;
                    i = R$string.kg_enter_confirm_pin_hint;
                } else {
                    i = R$string.kg_invalid_sim_pin_hint;
                }
            } else if (i2 != 2) {
                i = 0;
            } else if (KeyguardSimPukViewController.this.confirmPin()) {
                this.mState = 3;
                i = R$string.keyguard_sim_unlock_progress_dialog_message;
                KeyguardSimPukViewController.this.updateSim();
            } else {
                this.mState = 1;
                i = R$string.kg_invalid_confirm_pin_hint;
            }
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).resetPasswordText(true, true);
            if (i != 0) {
                KeyguardSimPukViewController.this.mMessageAreaController.setMessage(i);
            }
        }

        public void reset() {
            KeyguardSimPukViewController.this.mPinText = "";
            KeyguardSimPukViewController.this.mPukText = "";
            int i = 0;
            this.mState = 0;
            KeyguardSimPukViewController.this.handleSubInfoChangeIfNeeded();
            if (KeyguardSimPukViewController.this.mShowDefaultMessage) {
                KeyguardSimPukViewController.this.showDefaultMessage();
            }
            boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId);
            KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).findViewById(R$id.keyguard_esim_area);
            keyguardEsimArea.setSubscriptionId(KeyguardSimPukViewController.this.mSubId);
            if (!isEsimLocked) {
                i = 8;
            }
            keyguardEsimArea.setVisibility(i);
            KeyguardSimPukViewController.this.mPasswordEntry.requestFocus();
        }
    }

    public final void showDefaultMessage() {
        String str;
        CharSequence charSequence;
        int i = this.mRemainingAttempts;
        if (i >= 0) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mMessageAreaController;
            T t = this.mView;
            keyguardMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) t).getPukPasswordErrorMessage(i, true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) t).getContext(), this.mSubId), this.mSubId));
            return;
        }
        boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) this.mView).getContext(), this.mSubId);
        TelephonyManager telephonyManager = this.mTelephonyManager;
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        Resources resources = ((KeyguardSimPukView) this.mView).getResources();
        TypedArray obtainStyledAttributes = ((KeyguardSimPukView) this.mView).getContext().obtainStyledAttributes(new int[]{16842904});
        int color = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        if (activeModemCount < 2) {
            str = resources.getString(R$string.kg_puk_enter_puk_hint);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            if (subscriptionInfoForSubId != null) {
                charSequence = subscriptionInfoForSubId.getDisplayName();
            } else {
                charSequence = "";
            }
            String string = resources.getString(R$string.kg_puk_enter_puk_hint_multi, new Object[]{charSequence});
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(R$string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        this.mMessageAreaController.setMessage((CharSequence) str);
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
        new CheckSimPuk("", "", this.mSubId) {
            public void onSimLockChangedResponse(PinResult pinResult) {
                if (pinResult == null) {
                    Log.e("KeyguardSimPukView", "onSimCheckResponse, pin result is NULL");
                    return;
                }
                Log.d("KeyguardSimPukView", "onSimCheckResponse  empty One result " + pinResult.toString());
                if (pinResult.getAttemptsRemaining() >= 0) {
                    KeyguardSimPukViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                    keyguardSimPukViewController.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) keyguardSimPukViewController.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), true, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                }
            }
        }.start();
    }

    public final boolean checkPuk() {
        if (this.mPasswordEntry.getText().length() != 8) {
            return false;
        }
        this.mPukText = this.mPasswordEntry.getText();
        return true;
    }

    public final boolean checkPin() {
        int length = this.mPasswordEntry.getText().length();
        if (length < 4 || length > 8) {
            return false;
        }
        this.mPinText = this.mPasswordEntry.getText();
        return true;
    }

    public boolean confirmPin() {
        return this.mPinText.equals(this.mPasswordEntry.getText());
    }

    public final void updateSim() {
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPukThread == null) {
            AnonymousClass3 r0 = new CheckSimPuk(this.mPukText, this.mPinText, this.mSubId) {
                public void onSimLockChangedResponse(PinResult pinResult) {
                    ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new KeyguardSimPukViewController$3$$ExternalSyntheticLambda0(this, pinResult));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onSimLockChangedResponse$0(PinResult pinResult) {
                    if (KeyguardSimPukViewController.this.mSimUnlockProgressDialog != null) {
                        KeyguardSimPukViewController.this.mSimUnlockProgressDialog.hide();
                    }
                    ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).resetPasswordText(true, pinResult.getResult() != 0);
                    if (pinResult.getResult() == 0) {
                        KeyguardSimPukViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPukViewController.this.mSubId);
                        KeyguardSimPukViewController.this.mRemainingAttempts = -1;
                        KeyguardSimPukViewController.this.mShowDefaultMessage = true;
                        KeyguardSimPukViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
                    } else {
                        KeyguardSimPukViewController.this.mShowDefaultMessage = false;
                        if (pinResult.getResult() == 1) {
                            KeyguardSimPukViewController keyguardSimPukViewController = KeyguardSimPukViewController.this;
                            keyguardSimPukViewController.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) keyguardSimPukViewController.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                            if (pinResult.getAttemptsRemaining() <= 2) {
                                KeyguardSimPukViewController.this.getPukRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                            } else {
                                KeyguardSimPukViewController keyguardSimPukViewController2 = KeyguardSimPukViewController.this;
                                keyguardSimPukViewController2.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) keyguardSimPukViewController2.mView).getPukPasswordErrorMessage(pinResult.getAttemptsRemaining(), false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).getContext(), KeyguardSimPukViewController.this.mSubId), KeyguardSimPukViewController.this.mSubId));
                            }
                        } else {
                            KeyguardSimPukViewController keyguardSimPukViewController3 = KeyguardSimPukViewController.this;
                            keyguardSimPukViewController3.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPukView) keyguardSimPukViewController3.mView).getResources().getString(R$string.kg_password_puk_failed));
                        }
                        if (KeyguardSimPukViewController.DEBUG) {
                            Log.d("KeyguardSimPukView", "verifyPasswordAndUnlock  UpdateSim.onSimCheckResponse:  attemptsRemaining=" + pinResult.getAttemptsRemaining());
                        }
                    }
                    KeyguardSimPukViewController.this.mStateMachine.reset();
                    KeyguardSimPukViewController.this.mCheckSimPukThread = null;
                }
            };
            this.mCheckSimPukThread = r0;
            r0.start();
        }
    }

    public final Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPukView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPukView) this.mView).getResources().getString(R$string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            if (!(((KeyguardSimPukView) this.mView).getContext() instanceof Activity)) {
                this.mSimUnlockProgressDialog.getWindow().setType(2009);
            }
        }
        return this.mSimUnlockProgressDialog;
    }

    public final void handleSubInfoChangeIfNeeded() {
        int nextSubIdForState = this.mKeyguardUpdateMonitor.getNextSubIdForState(3);
        if (nextSubIdForState == this.mSubId || !SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
            this.mShowDefaultMessage = false;
            return;
        }
        this.mSubId = nextSubIdForState;
        this.mShowDefaultMessage = true;
        this.mRemainingAttempts = -1;
    }

    public final Dialog getPukRemainingAttemptsDialog(int i) {
        T t = this.mView;
        String pukPasswordErrorMessage = ((KeyguardSimPukView) t).getPukPasswordErrorMessage(i, false, KeyguardEsimArea.isEsimLocked(((KeyguardSimPukView) t).getContext(), this.mSubId), this.mSubId);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPukView) this.mView).getContext());
            builder.setMessage(pukPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(2009);
        } else {
            alertDialog.setMessage(pukPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    public void onPause() {
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
    }

    public abstract class CheckSimPuk extends Thread {
        public final String mPin;
        public final String mPuk;
        public final int mSubId;

        /* renamed from: onSimLockChangedResponse */
        public abstract void lambda$run$0(PinResult pinResult);

        public CheckSimPuk(String str, String str2, int i) {
            this.mPuk = str;
            this.mPin = str2;
            this.mSubId = i;
        }

        public void run() {
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "call supplyIccLockPuk(subid=" + this.mSubId + ")");
            }
            PinResult supplyIccLockPuk = KeyguardSimPukViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPuk(this.mPuk, this.mPin);
            if (KeyguardSimPukViewController.DEBUG) {
                Log.v("KeyguardSimPukView", "supplyIccLockPuk returned: " + supplyIccLockPuk.toString());
            }
            ((KeyguardSimPukView) KeyguardSimPukViewController.this.mView).post(new KeyguardSimPukViewController$CheckSimPuk$$ExternalSyntheticLambda0(this, supplyIccLockPuk));
        }
    }
}
