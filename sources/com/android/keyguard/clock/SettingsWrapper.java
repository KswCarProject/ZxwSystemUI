package com.android.keyguard.clock;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsWrapper {
    public final ContentResolver mContentResolver;
    public final Migration mMigration;

    public interface Migration {
        void migrate(String str, int i);
    }

    public SettingsWrapper(ContentResolver contentResolver) {
        this(contentResolver, new Migrator(contentResolver));
    }

    @VisibleForTesting
    public SettingsWrapper(ContentResolver contentResolver, Migration migration) {
        this.mContentResolver = contentResolver;
        this.mMigration = migration;
    }

    public String getLockScreenCustomClockFace(int i) {
        return decode(Settings.Secure.getStringForUser(this.mContentResolver, "lock_screen_custom_clock_face", i), i);
    }

    public String getDockedClockFace(int i) {
        return Settings.Secure.getStringForUser(this.mContentResolver, "docked_clock_face", i);
    }

    @VisibleForTesting
    public String decode(String str, int i) {
        if (str == null) {
            return str;
        }
        try {
            try {
                return new JSONObject(str).getString("clock");
            } catch (JSONException e) {
                Log.e("ClockFaceSettings", "JSON object does not contain clock field.", e);
                return null;
            }
        } catch (JSONException e2) {
            Log.e("ClockFaceSettings", "Settings value is not valid JSON", e2);
            this.mMigration.migrate(str, i);
            return str;
        }
    }

    public static final class Migrator implements Migration {
        public final ContentResolver mContentResolver;

        public Migrator(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }

        public void migrate(String str, int i) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("clock", str);
                Settings.Secure.putStringForUser(this.mContentResolver, "lock_screen_custom_clock_face", jSONObject.toString(), i);
            } catch (JSONException e) {
                Log.e("ClockFaceSettings", "Failed migrating settings value to JSON format", e);
            }
        }
    }
}
