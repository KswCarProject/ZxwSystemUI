package com.android.settingslib;

import android.content.Context;
import android.system.StructUtsname;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceInfoUtils {
    public static String formatKernelVersion(Context context, StructUtsname structUtsname) {
        if (structUtsname == null) {
            return context.getString(R$string.status_unavailable);
        }
        Matcher matcher = Pattern.compile("(#\\d+) (?:.*?)?((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)").matcher(structUtsname.version);
        if (!matcher.matches()) {
            Log.e("DeviceInfoUtils", "Regex did not match on uname version " + structUtsname.version);
            return context.getString(R$string.status_unavailable);
        }
        return structUtsname.release + "\n" + matcher.group(1) + " " + matcher.group(2);
    }

    public static String getFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo != null) {
            String line1Number = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).getLine1Number();
            if (!TextUtils.isEmpty(line1Number)) {
                return PhoneNumberUtils.formatNumber(line1Number);
            }
        }
        return null;
    }

    public static String getBidiFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        return BidiFormatter.getInstance().unicodeWrap(getFormattedPhoneNumber(context, subscriptionInfo), TextDirectionHeuristics.LTR);
    }
}
