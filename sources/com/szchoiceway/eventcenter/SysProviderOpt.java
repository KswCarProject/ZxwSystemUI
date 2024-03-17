package com.szchoiceway.eventcenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SysProviderOpt {
    public ContentResolver mCntResolver;
    public Context mContext;
    public Uri mUri = Uri.parse("content://com.szchoiceway.eventcenter.SysVarProvider/SysVar");

    public SysProviderOpt(Context context) {
        this.mContext = context;
        this.mCntResolver = context.getContentResolver();
    }

    public int getRecordInteger(String str, int i) {
        String[] strArr = {str};
        Cursor cursor = null;
        try {
            Cursor query = this.mCntResolver.query(this.mUri, (String[]) null, "keyname=?", strArr, (String) null);
            if (query != null && query.getCount() > 0 && query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("keyvalue"));
                if (string.length() > 0) {
                    i = Integer.valueOf(string).intValue();
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            DebugLog.e("SysProviderOpt", e.toString());
        }
        return i;
    }
}
