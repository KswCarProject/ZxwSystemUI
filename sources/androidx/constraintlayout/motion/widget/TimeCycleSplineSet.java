package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public abstract class TimeCycleSplineSet {
    public static float VAL_2PI = 6.2831855f;
    public int count;
    public float last_cycle = Float.NaN;
    public long last_time;
    public float[] mCache = new float[3];
    public boolean mContinue = false;
    public CurveFit mCurveFit;
    public int[] mTimePoints = new int[10];
    public String mType;
    public float[][] mValues = ((float[][]) Array.newInstance(float.class, new int[]{10, 3}));
    public int mWaveShape = 0;

    public abstract boolean setProperty(View view, float f, long j, KeyCache keyCache);

    public String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i = 0; i < this.count; i++) {
            str = str + "[" + this.mTimePoints[i] + " , " + decimalFormat.format(this.mValues[i]) + "] ";
        }
        return str;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public float get(float f, long j, View view, KeyCache keyCache) {
        long j2 = j;
        View view2 = view;
        KeyCache keyCache2 = keyCache;
        this.mCurveFit.getPos((double) f, this.mCache);
        float[] fArr = this.mCache;
        float f2 = fArr[1];
        int i = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
        if (i == 0) {
            this.mContinue = false;
            return fArr[2];
        }
        if (Float.isNaN(this.last_cycle)) {
            float floatValue = keyCache2.getFloatValue(view2, this.mType, 0);
            this.last_cycle = floatValue;
            if (Float.isNaN(floatValue)) {
                this.last_cycle = 0.0f;
            }
        }
        float f3 = (float) ((((double) this.last_cycle) + ((((double) (j2 - this.last_time)) * 1.0E-9d) * ((double) f2))) % 1.0d);
        this.last_cycle = f3;
        keyCache2.setFloatValue(view2, this.mType, 0, f3);
        this.last_time = j2;
        float f4 = this.mCache[0];
        float calcWave = (calcWave(this.last_cycle) * f4) + this.mCache[2];
        this.mContinue = (f4 == 0.0f && i == 0) ? false : true;
        return calcWave;
    }

    public float calcWave(float f) {
        switch (this.mWaveShape) {
            case 1:
                return Math.signum(f * VAL_2PI);
            case 2:
                return 1.0f - Math.abs(f);
            case 3:
                return (((f * 2.0f) + 1.0f) % 2.0f) - 1.0f;
            case 4:
                return 1.0f - (((f * 2.0f) + 1.0f) % 2.0f);
            case 5:
                return (float) Math.cos((double) (f * VAL_2PI));
            case 6:
                float abs = 1.0f - Math.abs(((f * 4.0f) % 4.0f) - 2.0f);
                return 1.0f - (abs * abs);
            default:
                return (float) Math.sin((double) (f * VAL_2PI));
        }
    }

    public static TimeCycleSplineSet makeCustomSpline(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new CustomSet(str, sparseArray);
    }

    public static TimeCycleSplineSet makeSpline(String str, long j) {
        TimeCycleSplineSet timeCycleSplineSet;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1249320806:
                if (str.equals("rotationX")) {
                    c = 0;
                    break;
                }
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c = 1;
                    break;
                }
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c = 2;
                    break;
                }
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c = 3;
                    break;
                }
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c = 4;
                    break;
                }
                break;
            case -1001078227:
                if (str.equals("progress")) {
                    c = 5;
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c = 6;
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c = 7;
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c = 8;
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c = 9;
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c = 10;
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c = 11;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                timeCycleSplineSet = new RotationXset();
                break;
            case 1:
                timeCycleSplineSet = new RotationYset();
                break;
            case 2:
                timeCycleSplineSet = new TranslationXset();
                break;
            case 3:
                timeCycleSplineSet = new TranslationYset();
                break;
            case 4:
                timeCycleSplineSet = new TranslationZset();
                break;
            case 5:
                timeCycleSplineSet = new ProgressSet();
                break;
            case 6:
                timeCycleSplineSet = new ScaleXset();
                break;
            case 7:
                timeCycleSplineSet = new ScaleYset();
                break;
            case 8:
                timeCycleSplineSet = new RotationSet();
                break;
            case 9:
                timeCycleSplineSet = new ElevationSet();
                break;
            case 10:
                timeCycleSplineSet = new PathRotate();
                break;
            case 11:
                timeCycleSplineSet = new AlphaSet();
                break;
            default:
                return null;
        }
        timeCycleSplineSet.setStartTime(j);
        return timeCycleSplineSet;
    }

    public void setStartTime(long j) {
        this.last_time = j;
    }

    public void setPoint(int i, float f, float f2, int i2, float f3) {
        int[] iArr = this.mTimePoints;
        int i3 = this.count;
        iArr[i3] = i;
        float[] fArr = this.mValues[i3];
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        this.mWaveShape = Math.max(this.mWaveShape, i2);
        this.count++;
    }

    public void setup(int i) {
        int i2 = this.count;
        if (i2 == 0) {
            Log.e("SplineSet", "Error no points added to " + this.mType);
            return;
        }
        Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, i2 - 1);
        int i3 = 1;
        int i4 = 0;
        while (true) {
            int[] iArr = this.mTimePoints;
            if (i3 >= iArr.length) {
                break;
            }
            if (iArr[i3] != iArr[i3 - 1]) {
                i4++;
            }
            i3++;
        }
        double[] dArr = new double[i4];
        int[] iArr2 = new int[2];
        iArr2[1] = 3;
        iArr2[0] = i4;
        double[][] dArr2 = (double[][]) Array.newInstance(double.class, iArr2);
        int i5 = 0;
        for (int i6 = 0; i6 < this.count; i6++) {
            if (i6 > 0) {
                int[] iArr3 = this.mTimePoints;
                if (iArr3[i6] == iArr3[i6 - 1]) {
                }
            }
            dArr[i5] = ((double) this.mTimePoints[i6]) * 0.01d;
            double[] dArr3 = dArr2[i5];
            float[] fArr = this.mValues[i6];
            dArr3[0] = (double) fArr[0];
            dArr3[1] = (double) fArr[1];
            dArr3[2] = (double) fArr[2];
            i5++;
        }
        this.mCurveFit = CurveFit.get(i, dArr, dArr2);
    }

    public static class ElevationSet extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setElevation(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class AlphaSet extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setAlpha(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class RotationSet extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotation(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class RotationXset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotationX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class RotationYset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotationY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class PathRotate extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            return this.mContinue;
        }

        public boolean setPathRotate(View view, KeyCache keyCache, float f, long j, double d, double d2) {
            view.setRotation(get(f, j, view, keyCache) + ((float) Math.toDegrees(Math.atan2(d2, d))));
            return this.mContinue;
        }
    }

    public static class ScaleXset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setScaleX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class ScaleYset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setScaleY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class TranslationXset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class TranslationYset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class TranslationZset extends TimeCycleSplineSet {
        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationZ(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public static class CustomSet extends TimeCycleSplineSet {
        public String mAttributeName;
        public float[] mCache;
        public SparseArray<ConstraintAttribute> mConstraintAttributeList;
        public float[] mTempValues;
        public SparseArray<float[]> mWaveProperties = new SparseArray<>();

        public CustomSet(String str, SparseArray<ConstraintAttribute> sparseArray) {
            this.mAttributeName = str.split(",")[1];
            this.mConstraintAttributeList = sparseArray;
        }

        public void setup(int i) {
            int size = this.mConstraintAttributeList.size();
            int noOfInterpValues = this.mConstraintAttributeList.valueAt(0).noOfInterpValues();
            double[] dArr = new double[size];
            int i2 = noOfInterpValues + 2;
            this.mTempValues = new float[i2];
            this.mCache = new float[noOfInterpValues];
            int[] iArr = new int[2];
            iArr[1] = i2;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, iArr);
            for (int i3 = 0; i3 < size; i3++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i3);
                float[] valueAt = this.mWaveProperties.valueAt(i3);
                dArr[i3] = ((double) keyAt) * 0.01d;
                this.mConstraintAttributeList.valueAt(i3).getValuesToInterpolate(this.mTempValues);
                int i4 = 0;
                while (true) {
                    float[] fArr = this.mTempValues;
                    if (i4 >= fArr.length) {
                        break;
                    }
                    dArr2[i3][i4] = (double) fArr[i4];
                    i4++;
                }
                double[] dArr3 = dArr2[i3];
                dArr3[noOfInterpValues] = (double) valueAt[0];
                dArr3[noOfInterpValues + 1] = (double) valueAt[1];
            }
            this.mCurveFit = CurveFit.get(i, dArr, dArr2);
        }

        public void setPoint(int i, float f, float f2, int i2, float f3) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        public void setPoint(int i, ConstraintAttribute constraintAttribute, float f, int i2, float f2) {
            this.mConstraintAttributeList.append(i, constraintAttribute);
            this.mWaveProperties.append(i, new float[]{f, f2});
            this.mWaveShape = Math.max(this.mWaveShape, i2);
        }

        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            this.mCurveFit.getPos((double) f, this.mTempValues);
            float[] fArr = this.mTempValues;
            float f2 = fArr[fArr.length - 2];
            float f3 = fArr[fArr.length - 1];
            float f4 = (float) ((((double) this.last_cycle) + ((((double) (j - this.last_time)) * 1.0E-9d) * ((double) f2))) % 1.0d);
            this.last_cycle = f4;
            this.last_time = j;
            float calcWave = calcWave(f4);
            this.mContinue = false;
            int i = 0;
            while (true) {
                float[] fArr2 = this.mCache;
                if (i >= fArr2.length) {
                    break;
                }
                boolean z = this.mContinue;
                float f5 = this.mTempValues[i];
                this.mContinue = z | (((double) f5) != 0.0d);
                fArr2[i] = (f5 * calcWave) + f3;
                i++;
            }
            this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(view, this.mCache);
            if (f2 != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }
    }

    public static class ProgressSet extends TimeCycleSplineSet {
        public boolean mNoMethod = false;

        public boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            View view2 = view;
            if (view2 instanceof MotionLayout) {
                ((MotionLayout) view2).setProgress(get(f, j, view, keyCache));
            } else if (this.mNoMethod) {
                return false;
            } else {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", new Class[]{Float.TYPE});
                } catch (NoSuchMethodException unused) {
                    this.mNoMethod = true;
                }
                Method method2 = method;
                if (method2 != null) {
                    try {
                        method2.invoke(view, new Object[]{Float.valueOf(get(f, j, view, keyCache))});
                    } catch (IllegalAccessException e) {
                        Log.e("SplineSet", "unable to setProgress", e);
                    } catch (InvocationTargetException e2) {
                        Log.e("SplineSet", "unable to setProgress", e2);
                    }
                }
            }
            return this.mContinue;
        }
    }

    public static class Sort {
        public static void doubleQuickSort(int[] iArr, float[][] fArr, int i, int i2) {
            int[] iArr2 = new int[(iArr.length + 10)];
            iArr2[0] = i2;
            iArr2[1] = i;
            int i3 = 2;
            while (i3 > 0) {
                int i4 = i3 - 1;
                int i5 = iArr2[i4];
                i3 = i4 - 1;
                int i6 = iArr2[i3];
                if (i5 < i6) {
                    int partition = partition(iArr, fArr, i5, i6);
                    int i7 = i3 + 1;
                    iArr2[i3] = partition - 1;
                    int i8 = i7 + 1;
                    iArr2[i7] = i5;
                    int i9 = i8 + 1;
                    iArr2[i8] = i6;
                    i3 = i9 + 1;
                    iArr2[i9] = partition + 1;
                }
            }
        }

        public static int partition(int[] iArr, float[][] fArr, int i, int i2) {
            int i3 = iArr[i2];
            int i4 = i;
            while (i < i2) {
                if (iArr[i] <= i3) {
                    swap(iArr, fArr, i4, i);
                    i4++;
                }
                i++;
            }
            swap(iArr, fArr, i4, i2);
            return i4;
        }

        public static void swap(int[] iArr, float[][] fArr, int i, int i2) {
            int i3 = iArr[i];
            iArr[i] = iArr[i2];
            iArr[i2] = i3;
            float[] fArr2 = fArr[i];
            fArr[i] = fArr[i2];
            fArr[i2] = fArr2;
        }
    }
}
