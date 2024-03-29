package androidx.leanback.widget;

import android.util.Property;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Parallax<PropertyT extends Property> {
    public final List<ParallaxEffect> mEffects = new ArrayList(4);
    public float[] mFloatValues = new float[4];
    public final List<PropertyT> mProperties;
    public final List<PropertyT> mPropertiesReadOnly;
    public int[] mValues = new int[4];

    public Parallax() {
        ArrayList arrayList = new ArrayList();
        this.mProperties = arrayList;
        this.mPropertiesReadOnly = Collections.unmodifiableList(arrayList);
    }

    public final void verifyFloatProperties() throws IllegalStateException {
        if (this.mProperties.size() >= 2) {
            float floatPropertyValue = getFloatPropertyValue(0);
            int i = 1;
            while (i < this.mProperties.size()) {
                float floatPropertyValue2 = getFloatPropertyValue(i);
                if (floatPropertyValue2 < floatPropertyValue) {
                    int i2 = i - 1;
                    throw new IllegalStateException(String.format("Parallax Property[%d]\"%s\" is smaller than Property[%d]\"%s\"", new Object[]{Integer.valueOf(i), ((Property) this.mProperties.get(i)).getName(), Integer.valueOf(i2), ((Property) this.mProperties.get(i2)).getName()}));
                } else if (floatPropertyValue == -3.4028235E38f && floatPropertyValue2 == Float.MAX_VALUE) {
                    int i3 = i - 1;
                    throw new IllegalStateException(String.format("Parallax Property[%d]\"%s\" is UNKNOWN_BEFORE and Property[%d]\"%s\" is UNKNOWN_AFTER", new Object[]{Integer.valueOf(i3), ((Property) this.mProperties.get(i3)).getName(), Integer.valueOf(i), ((Property) this.mProperties.get(i)).getName()}));
                } else {
                    i++;
                    floatPropertyValue = floatPropertyValue2;
                }
            }
        }
    }

    public final float getFloatPropertyValue(int i) {
        return this.mFloatValues[i];
    }

    public void updateValues() {
        for (int i = 0; i < this.mEffects.size(); i++) {
            this.mEffects.get(i).performMapping(this);
        }
    }
}
