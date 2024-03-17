package com.google.zxing.common.reedsolomon;

public final class GenericGF {
    public static final GenericGF AZTEC_DATA_10 = new GenericGF(1033, 1024, 1);
    public static final GenericGF AZTEC_DATA_12 = new GenericGF(4201, 4096, 1);
    public static final GenericGF AZTEC_DATA_6;
    public static final GenericGF AZTEC_DATA_8;
    public static final GenericGF AZTEC_PARAM = new GenericGF(19, 16, 1);
    public static final GenericGF DATA_MATRIX_FIELD_256;
    public static final GenericGF MAXICODE_FIELD_64;
    public static final GenericGF QR_CODE_FIELD_256 = new GenericGF(285, 256, 0);
    public int[] expTable;
    public final int generatorBase;
    public boolean initialized = false;
    public int[] logTable;
    public GenericGFPoly one;
    public final int primitive;
    public final int size;
    public GenericGFPoly zero;

    public static int addOrSubtract(int i, int i2) {
        return i ^ i2;
    }

    static {
        GenericGF genericGF = new GenericGF(67, 64, 1);
        AZTEC_DATA_6 = genericGF;
        GenericGF genericGF2 = new GenericGF(301, 256, 1);
        DATA_MATRIX_FIELD_256 = genericGF2;
        AZTEC_DATA_8 = genericGF2;
        MAXICODE_FIELD_64 = genericGF;
    }

    public GenericGF(int i, int i2, int i3) {
        this.primitive = i;
        this.size = i2;
        this.generatorBase = i3;
        if (i2 <= 0) {
            initialize();
        }
    }

    public final void initialize() {
        int i = this.size;
        this.expTable = new int[i];
        this.logTable = new int[i];
        int i2 = 0;
        int i3 = 1;
        while (true) {
            int i4 = this.size;
            if (i2 >= i4) {
                break;
            }
            this.expTable[i2] = i3;
            i3 <<= 1;
            if (i3 >= i4) {
                i3 = (i3 ^ this.primitive) & (i4 - 1);
            }
            i2++;
        }
        for (int i5 = 0; i5 < this.size - 1; i5++) {
            this.logTable[this.expTable[i5]] = i5;
        }
        this.zero = new GenericGFPoly(this, new int[]{0});
        this.one = new GenericGFPoly(this, new int[]{1});
        this.initialized = true;
    }

    public final void checkInit() {
        if (!this.initialized) {
            initialize();
        }
    }

    public GenericGFPoly getZero() {
        checkInit();
        return this.zero;
    }

    public GenericGFPoly buildMonomial(int i, int i2) {
        checkInit();
        if (i < 0) {
            throw new IllegalArgumentException();
        } else if (i2 == 0) {
            return this.zero;
        } else {
            int[] iArr = new int[(i + 1)];
            iArr[0] = i2;
            return new GenericGFPoly(this, iArr);
        }
    }

    public int exp(int i) {
        checkInit();
        return this.expTable[i];
    }

    public int log(int i) {
        checkInit();
        if (i != 0) {
            return this.logTable[i];
        }
        throw new IllegalArgumentException();
    }

    public int inverse(int i) {
        checkInit();
        if (i != 0) {
            return this.expTable[(this.size - this.logTable[i]) - 1];
        }
        throw new ArithmeticException();
    }

    public int multiply(int i, int i2) {
        checkInit();
        if (i == 0 || i2 == 0) {
            return 0;
        }
        int[] iArr = this.expTable;
        int[] iArr2 = this.logTable;
        return iArr[(iArr2[i] + iArr2[i2]) % (this.size - 1)];
    }

    public int getGeneratorBase() {
        return this.generatorBase;
    }

    public String toString() {
        return "GF(0x" + Integer.toHexString(this.primitive) + ',' + this.size + ')';
    }
}