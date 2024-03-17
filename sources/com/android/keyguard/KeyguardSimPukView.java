package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.telephony.SubscriptionInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import java.util.HashMap;
import java.util.Map;

public class KeyguardSimPukView extends KeyguardPinBasedInputView {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public ImageView mSimImageView;
    public Map<String, String> mWrongPukCodeMessageMap;

    public int getPromptReasonStringRes(int i) {
        return 0;
    }

    public void startAppearAnimation() {
    }

    public KeyguardSimPukView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardSimPukView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWrongPukCodeMessageMap = new HashMap(4);
        updateWrongPukMessageMap(context);
    }

    public void updateWrongPukMessageMap(Context context) {
        String[] stringArray = context.getResources().getStringArray(R$array.kg_wrong_puk_code_message_list);
        if (stringArray.length == 0) {
            Log.d("KeyguardSimPukView", "There is no customization PUK prompt");
            return;
        }
        for (String str : stringArray) {
            String[] split = str.trim().split(":");
            if (split.length != 2) {
                Log.e("KeyguardSimPukView", "invalid key value config " + str);
            } else {
                this.mWrongPukCodeMessageMap.put(split[0], split[1]);
            }
        }
    }

    public final String getMessageTextForWrongPukCode(int i) {
        SubscriptionInfo subscriptionInfoForSubId = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).getSubscriptionInfoForSubId(i);
        if (subscriptionInfoForSubId == null) {
            return null;
        }
        return this.mWrongPukCodeMessageMap.get(subscriptionInfoForSubId.getMccString() + subscriptionInfoForSubId.getMncString());
    }

    public String getPukPasswordErrorMessage(int i, boolean z, boolean z2, int i2) {
        String str;
        int i3;
        int i4;
        if (i == 0) {
            str = getMessageTextForWrongPukCode(i2);
            if (str == null) {
                str = getContext().getString(R$string.kg_password_wrong_puk_code_dead);
            }
        } else if (i > 0) {
            if (z) {
                i4 = R$plurals.kg_password_default_puk_message;
            } else {
                i4 = R$plurals.kg_password_wrong_puk_code;
            }
            str = getContext().getResources().getQuantityString(i4, i, new Object[]{Integer.valueOf(i)});
        } else {
            if (z) {
                i3 = R$string.kg_puk_enter_puk_hint;
            } else {
                i3 = R$string.kg_password_puk_failed;
            }
            str = getContext().getString(i3);
        }
        if (z2) {
            str = getResources().getString(R$string.kg_sim_lock_esim_instructions, new Object[]{str});
        }
        if (DEBUG) {
            Log.d("KeyguardSimPukView", "getPukPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        }
        return str;
    }

    public int getPasswordTextViewId() {
        return R$id.pukEntry;
    }

    public void onFinishInflate() {
        this.mSimImageView = (ImageView) findViewById(R$id.keyguard_sim);
        super.onFinishInflate();
        View view = this.mEcaView;
        if (view instanceof EmergencyCarrierArea) {
            ((EmergencyCarrierArea) view).setCarrierTextVisible(true);
        }
    }

    public CharSequence getTitle() {
        return getContext().getString(17040525);
    }

    public void reloadColors() {
        super.reloadColors();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842808});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        DrawableCompat.setTint(DrawableCompat.wrap(this.mSimImageView.getDrawable()), color);
    }
}
