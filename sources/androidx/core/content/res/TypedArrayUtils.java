package androidx.core.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

public class TypedArrayUtils {
    public static boolean hasAttribute(XmlPullParser xmlPullParser, String str) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", str) != null;
    }

    public static float getNamedFloat(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, float f) {
        if (!hasAttribute(xmlPullParser, str)) {
            return f;
        }
        return typedArray.getFloat(i, f);
    }

    public static boolean getNamedBoolean(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, boolean z) {
        if (!hasAttribute(xmlPullParser, str)) {
            return z;
        }
        return typedArray.getBoolean(i, z);
    }

    public static int getNamedInt(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, int i2) {
        if (!hasAttribute(xmlPullParser, str)) {
            return i2;
        }
        return typedArray.getInt(i, i2);
    }

    public static int getNamedResourceId(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, int i2) {
        if (!hasAttribute(xmlPullParser, str)) {
            return i2;
        }
        return typedArray.getResourceId(i, i2);
    }

    public static String getNamedString(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i) {
        if (!hasAttribute(xmlPullParser, str)) {
            return null;
        }
        return typedArray.getString(i);
    }

    public static boolean getBoolean(TypedArray typedArray, int i, int i2, boolean z) {
        return typedArray.getBoolean(i, typedArray.getBoolean(i2, z));
    }

    public static Drawable getDrawable(TypedArray typedArray, int i, int i2) {
        Drawable drawable = typedArray.getDrawable(i);
        return drawable == null ? typedArray.getDrawable(i2) : drawable;
    }

    public static int getInt(TypedArray typedArray, int i, int i2, int i3) {
        return typedArray.getInt(i, typedArray.getInt(i2, i3));
    }

    public static int getResourceId(TypedArray typedArray, int i, int i2, int i3) {
        return typedArray.getResourceId(i, typedArray.getResourceId(i2, i3));
    }

    public static String getString(TypedArray typedArray, int i, int i2) {
        String string = typedArray.getString(i);
        return string == null ? typedArray.getString(i2) : string;
    }

    public static CharSequence getText(TypedArray typedArray, int i, int i2) {
        CharSequence text = typedArray.getText(i);
        return text == null ? typedArray.getText(i2) : text;
    }

    public static CharSequence[] getTextArray(TypedArray typedArray, int i, int i2) {
        CharSequence[] textArray = typedArray.getTextArray(i);
        return textArray == null ? typedArray.getTextArray(i2) : textArray;
    }

    public static int getAttr(Context context, int i, int i2) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.resourceId != 0 ? i : i2;
    }
}
