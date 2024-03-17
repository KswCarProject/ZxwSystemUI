package com.android.keyguard;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.systemui.R$string;

class KeyguardEsimArea extends Button implements View.OnClickListener {
    public EuiccManager mEuiccManager;
    public BroadcastReceiver mReceiver;
    public int mSubscriptionId;

    public KeyguardEsimArea(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 16974425);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode;
                if ("com.android.keyguard.disable_esim".equals(intent.getAction()) && (resultCode = getResultCode()) != 0) {
                    Log.e("KeyguardEsimArea", "Error disabling esim, result code = " + resultCode);
                    AlertDialog create = new AlertDialog.Builder(KeyguardEsimArea.this.mContext).setMessage(R$string.error_disable_esim_msg).setTitle(R$string.error_disable_esim_title).setCancelable(false).setPositiveButton(R$string.ok, (DialogInterface.OnClickListener) null).create();
                    create.getWindow().setType(2009);
                    create.show();
                }
            }
        };
        this.mEuiccManager = (EuiccManager) context.getSystemService("euicc");
        setOnClickListener(this);
    }

    public void setSubscriptionId(int i) {
        this.mSubscriptionId = i;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("com.android.keyguard.disable_esim"), "com.android.systemui.permission.SELF", (Handler) null, 2);
    }

    public static boolean isEsimLocked(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        if (((EuiccManager) context.getSystemService("euicc")).isEnabled() && (activeSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfo(i)) != null && activeSubscriptionInfo.isEmbedded()) {
            return true;
        }
        return false;
    }

    public void onDetachedFromWindow() {
        this.mContext.unregisterReceiver(this.mReceiver);
        super.onDetachedFromWindow();
    }

    public void onClick(View view) {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfo(this.mSubscriptionId);
        if (activeSubscriptionInfo == null) {
            Log.e("KeyguardEsimArea", "No active subscription with subscriptionId: " + this.mSubscriptionId);
            return;
        }
        Intent intent = new Intent("com.android.keyguard.disable_esim");
        intent.setPackage(this.mContext.getPackageName());
        this.mEuiccManager.switchToSubscription(-1, activeSubscriptionInfo.getPortIndex(), PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 167772160, UserHandle.SYSTEM));
    }
}
