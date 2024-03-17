package com.android.keyguard;

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
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;

public class KeyguardSimPinViewController extends KeyguardPinBasedInputViewController<KeyguardSimPinView> {
    public CheckSimPin mCheckSimPinThread;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public int mRemainingAttempts;
    public AlertDialog mRemainingAttemptsDialog;
    public boolean mShowDefaultMessage;
    public ImageView mSimImageView;
    public ProgressDialog mSimUnlockProgressDialog;
    public int mSlotId;
    public int mSubId = -1;
    public final TelephonyManager mTelephonyManager;
    public KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onSimStateChanged(int i, int i2, int i3) {
            Log.v("KeyguardSimPinView", "onSimStateChanged(subId=" + i + ",slotId=" + i2 + ",simState=" + i3 + ")");
            if (i3 == 5 || i3 == 10) {
                KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                KeyguardSimPinViewController.this.resetState();
                return;
            }
            KeyguardSimPinViewController.this.resetState();
        }
    };

    public boolean startDisappearAnimation(Runnable runnable) {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardSimPinViewController(KeyguardSimPinView keyguardSimPinView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPinView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mSimImageView = (ImageView) ((KeyguardSimPinView) this.mView).findViewById(R$id.keyguard_sim);
    }

    public void onViewAttached() {
        super.onViewAttached();
    }

    public void resetState() {
        super.resetState();
        Log.v("KeyguardSimPinView", "Resetting state mShowDefaultMessage=" + this.mShowDefaultMessage);
        handleSubInfoChangeIfNeeded();
        this.mMessageAreaController.setMessage((CharSequence) "");
        if (this.mShowDefaultMessage) {
            showDefaultMessage();
        }
        T t = this.mView;
        ((KeyguardSimPinView) t).setEsimLocked(KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) t).getContext(), this.mSubId), this.mSubId);
    }

    public void onResume(int i) {
        super.onResume(i);
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
    }

    public void onPause() {
        super.onPause();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
        this.mMessageAreaController.setMessage((CharSequence) "");
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardSimPinView) this.mView).reloadColors();
    }

    public void verifyPasswordAndUnlock() {
        if (this.mPasswordEntry.getText().length() < 4) {
            this.mMessageAreaController.setMessage(R$string.kg_invalid_sim_pin_hint);
            ((KeyguardSimPinView) this.mView).resetPasswordText(true, true);
            getKeyguardSecurityCallback().userActivity();
            return;
        }
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPinThread == null) {
            AnonymousClass2 r0 = new CheckSimPin(this.mPasswordEntry.getText(), this.mSubId) {
                public void onSimCheckResponse(PinResult pinResult) {
                    ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).post(new KeyguardSimPinViewController$2$$ExternalSyntheticLambda0(this, pinResult));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onSimCheckResponse$0(PinResult pinResult) {
                    KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    if (KeyguardSimPinViewController.this.mSimUnlockProgressDialog != null) {
                        KeyguardSimPinViewController.this.mSimUnlockProgressDialog.hide();
                    }
                    ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).resetPasswordText(true, pinResult.getResult() != 0);
                    if (pinResult.getResult() == 0) {
                        KeyguardSimPinViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPinViewController.this.mSubId);
                        KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                        KeyguardSimPinViewController.this.mShowDefaultMessage = true;
                        KeyguardSimPinViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
                    } else {
                        KeyguardSimPinViewController.this.mShowDefaultMessage = false;
                        if (pinResult.getResult() != 1) {
                            KeyguardSimPinViewController keyguardSimPinViewController = KeyguardSimPinViewController.this;
                            keyguardSimPinViewController.mMessageAreaController.setMessage((CharSequence) ((KeyguardSimPinView) keyguardSimPinViewController.mView).getResources().getString(R$string.kg_password_pin_failed));
                        } else if (pinResult.getAttemptsRemaining() <= 2) {
                            KeyguardSimPinViewController.this.getSimRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                        } else {
                            KeyguardSimPinViewController keyguardSimPinViewController2 = KeyguardSimPinViewController.this;
                            keyguardSimPinViewController2.mMessageAreaController.setMessage((CharSequence) keyguardSimPinViewController2.getPinPasswordErrorMessage(pinResult.getAttemptsRemaining(), false));
                        }
                        Log.d("KeyguardSimPinView", "verifyPasswordAndUnlock  CheckSimPin.onSimCheckResponse: " + pinResult + " attemptsRemaining=" + pinResult.getAttemptsRemaining());
                    }
                    KeyguardSimPinViewController.this.getKeyguardSecurityCallback().userActivity();
                    KeyguardSimPinViewController.this.mCheckSimPinThread = null;
                }
            };
            this.mCheckSimPinThread = r0;
            r0.start();
        }
    }

    public final Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPinView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            this.mSimUnlockProgressDialog.getWindow().setType(2009);
        }
        return this.mSimUnlockProgressDialog;
    }

    public final Dialog getSimRemainingAttemptsDialog(int i) {
        String pinPasswordErrorMessage = getPinPasswordErrorMessage(i, false);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPinView) this.mView).getContext());
            builder.setMessage(pinPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(2009);
        } else {
            alertDialog.setMessage(pinPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    public final String getPinPasswordErrorMessage(int i, boolean z) {
        String str;
        int i2;
        int i3;
        if (i == 0) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_password_wrong_pin_code_pukked);
        } else if (i <= 0) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(z ? R$string.kg_sim_pin_instructions : R$string.kg_password_pin_failed);
        } else if (TelephonyManager.getDefault().getSimCount() > 1) {
            if (z) {
                i3 = R$plurals.kg_password_default_pin_message_multi_sim;
            } else {
                i3 = R$plurals.kg_password_wrong_pin_code_multi_sim;
            }
            str = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(i3, i, new Object[]{Integer.valueOf(this.mSlotId), Integer.valueOf(i)});
        } else {
            if (z) {
                i2 = R$plurals.kg_password_default_pin_message;
            } else {
                i2 = R$plurals.kg_password_wrong_pin_code;
            }
            str = ((KeyguardSimPinView) this.mView).getContext().getResources().getQuantityString(i2, i, new Object[]{Integer.valueOf(i)});
        }
        if (KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId)) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        Log.d("KeyguardSimPinView", "getPinPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        return str;
    }

    public final void showDefaultMessage() {
        setLockedSimMessage();
        if (this.mRemainingAttempts < 0) {
            this.mSlotId = SubscriptionManager.getSlotIndex(this.mSubId) + 1;
            new CheckSimPin("", this.mSubId) {
                public void onSimCheckResponse(PinResult pinResult) {
                    Log.d("KeyguardSimPinView", "onSimCheckResponse  empty One result " + pinResult.toString());
                    if (pinResult.getAttemptsRemaining() >= 0) {
                        KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                        KeyguardSimPinViewController.this.setLockedSimMessage();
                    }
                }
            }.start();
        }
    }

    public abstract class CheckSimPin extends Thread {
        public final String mPin;
        public int mSubId;

        /* renamed from: onSimCheckResponse */
        public abstract void lambda$run$0(PinResult pinResult);

        public CheckSimPin(String str, int i) {
            this.mPin = str;
            this.mSubId = i;
        }

        public void run() {
            Log.v("KeyguardSimPinView", "call supplyIccLockPin(subid=" + this.mSubId + ")");
            PinResult supplyIccLockPin = KeyguardSimPinViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPin(this.mPin);
            Log.v("KeyguardSimPinView", "supplyIccLockPin returned: " + supplyIccLockPin.toString());
            ((KeyguardSimPinView) KeyguardSimPinViewController.this.mView).post(new KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0(this, supplyIccLockPin));
        }
    }

    public final void setLockedSimMessage() {
        String str;
        boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId);
        TelephonyManager telephonyManager = this.mTelephonyManager;
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        Resources resources = ((KeyguardSimPinView) this.mView).getResources();
        TypedArray obtainStyledAttributes = ((KeyguardSimPinView) this.mView).getContext().obtainStyledAttributes(new int[]{16842904});
        int color = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        if (activeModemCount < 2) {
            str = resources.getString(R$string.kg_sim_pin_instructions);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            String string = resources.getString(R$string.kg_sim_pin_instructions_multi, new Object[]{subscriptionInfoForSubId != null ? subscriptionInfoForSubId.getDisplayName() : ""});
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(R$string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        if (((KeyguardSimPinView) this.mView).getVisibility() == 0) {
            this.mMessageAreaController.setMessage((CharSequence) str);
        }
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    public final void handleSubInfoChangeIfNeeded() {
        int unlockedSubIdForState = this.mKeyguardUpdateMonitor.getUnlockedSubIdForState(2);
        if (SubscriptionManager.isValidSubscriptionId(unlockedSubIdForState)) {
            Log.v("KeyguardSimPinView", "handleSubInfoChangeIfNeeded mSubId=" + this.mSubId + " subId=" + unlockedSubIdForState);
            this.mShowDefaultMessage = true;
            if (unlockedSubIdForState != this.mSubId) {
                this.mSubId = unlockedSubIdForState;
                this.mRemainingAttempts = -1;
                return;
            }
            return;
        }
        this.mShowDefaultMessage = false;
    }
}
