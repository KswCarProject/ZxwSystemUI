package com.airbnb.lottie;

import java.util.HashMap;
import java.util.Map;

public class TextDelegate {
    public final LottieAnimationView animationView = null;
    public boolean cacheText = true;
    public final LottieDrawable drawable = null;
    public final Map<String, String> stringMap = new HashMap();

    public final String getText(String str) {
        return str;
    }

    public final String getTextInternal(String str) {
        if (this.cacheText && this.stringMap.containsKey(str)) {
            return this.stringMap.get(str);
        }
        String text = getText(str);
        if (this.cacheText) {
            this.stringMap.put(str, text);
        }
        return text;
    }
}
