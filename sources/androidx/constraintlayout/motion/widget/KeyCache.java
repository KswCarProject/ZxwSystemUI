package androidx.constraintlayout.motion.widget;

import java.util.Arrays;
import java.util.HashMap;

public class KeyCache {
    public HashMap<Object, HashMap<String, float[]>> map = new HashMap<>();

    public void setFloatValue(Object obj, String str, int i, float f) {
        if (!this.map.containsKey(obj)) {
            HashMap hashMap = new HashMap();
            float[] fArr = new float[(i + 1)];
            fArr[i] = f;
            hashMap.put(str, fArr);
            this.map.put(obj, hashMap);
            return;
        }
        HashMap hashMap2 = this.map.get(obj);
        if (!hashMap2.containsKey(str)) {
            float[] fArr2 = new float[(i + 1)];
            fArr2[i] = f;
            hashMap2.put(str, fArr2);
            this.map.put(obj, hashMap2);
            return;
        }
        float[] fArr3 = (float[]) hashMap2.get(str);
        if (fArr3.length <= i) {
            fArr3 = Arrays.copyOf(fArr3, i + 1);
        }
        fArr3[i] = f;
        hashMap2.put(str, fArr3);
    }

    public float getFloatValue(Object obj, String str, int i) {
        if (!this.map.containsKey(obj)) {
            return Float.NaN;
        }
        HashMap hashMap = this.map.get(obj);
        if (!hashMap.containsKey(str)) {
            return Float.NaN;
        }
        float[] fArr = (float[]) hashMap.get(str);
        if (fArr.length > i) {
            return fArr[i];
        }
        return Float.NaN;
    }
}
