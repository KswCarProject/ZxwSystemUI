package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.system.OsConstants;
import android.util.Log;
import androidx.exifinterface.media.ExifInterfaceUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class ExifInterface {
    public static final Charset ASCII;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = {4};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = {8};
    public static final int[] BITS_PER_SAMPLE_RGB = {8, 8, 8};
    public static final Pattern DATETIME_PRIMARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4}):(\\d{2}):(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
    public static final Pattern DATETIME_SECONDARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
    public static final boolean DEBUG = Log.isLoggable("ExifInterface", 3);
    public static final byte[] EXIF_ASCII_PREFIX = {65, 83, 67, 73, 73, 0, 0, 0};
    public static final ExifTag[] EXIF_POINTER_TAGS = {new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1)};
    public static final ExifTag[][] EXIF_TAGS;
    public static final List<Integer> FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[]{2, 7, 4, 5});
    public static final Pattern GPS_TIMESTAMP_PATTERN = Pattern.compile("^(\\d{2}):(\\d{2}):(\\d{2})$");
    public static final byte[] HEIF_BRAND_HEIC = {104, 101, 105, 99};
    public static final byte[] HEIF_BRAND_MIF1 = {109, 105, 102, 49};
    public static final byte[] HEIF_TYPE_FTYP = {102, 116, 121, 112};
    public static final byte[] IDENTIFIER_EXIF_APP1;
    public static final byte[] IDENTIFIER_XMP_APP1;
    public static final ExifTag[] IFD_EXIF_TAGS;
    public static final int[] IFD_FORMAT_BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    public static final String[] IFD_FORMAT_NAMES = {"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};
    public static final ExifTag[] IFD_GPS_TAGS;
    public static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
    public static final ExifTag[] IFD_THUMBNAIL_TAGS;
    public static final ExifTag[] IFD_TIFF_TAGS;
    public static final byte[] JPEG_SIGNATURE = {-1, -40, -1};
    public static final Pattern NON_ZERO_TIME_PATTERN = Pattern.compile(".*[1-9].*");
    public static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
    public static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
    public static final byte[] ORF_MAKER_NOTE_HEADER_1 = {79, 76, 89, 77, 80, 0};
    public static final byte[] ORF_MAKER_NOTE_HEADER_2 = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    public static final ExifTag[] ORF_MAKER_NOTE_TAGS;
    public static final ExifTag[] PEF_TAGS;
    public static final byte[] PNG_CHUNK_TYPE_EXIF = {101, 88, 73, 102};
    public static final byte[] PNG_CHUNK_TYPE_IEND = {73, 69, 78, 68};
    public static final byte[] PNG_CHUNK_TYPE_IHDR = {73, 72, 68, 82};
    public static final byte[] PNG_SIGNATURE = {-119, 80, 78, 71, 13, 10, 26, 10};
    public static final List<Integer> ROTATION_ORDER = Arrays.asList(new Integer[]{1, 6, 3, 8});
    public static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
    public static final byte[] WEBP_CHUNK_TYPE_ANIM = "ANIM".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_ANMF = "ANMF".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_EXIF = {69, 88, 73, 70};
    public static final byte[] WEBP_CHUNK_TYPE_VP8 = "VP8 ".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_VP8L = "VP8L".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_VP8X = "VP8X".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_SIGNATURE_1 = {82, 73, 70, 70};
    public static final byte[] WEBP_SIGNATURE_2 = {87, 69, 66, 80};
    public static final byte[] WEBP_VP8_SIGNATURE = {-99, 1, 42};
    public static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap<>();
    public static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    public static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    public static SimpleDateFormat sFormatterPrimary;
    public static SimpleDateFormat sFormatterSecondary;
    public static final HashSet<String> sTagSetForCompatibility = new HashSet<>(Arrays.asList(new String[]{"FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"}));
    public boolean mAreThumbnailStripsConsecutive;
    public AssetManager.AssetInputStream mAssetInputStream;
    public final HashMap<String, ExifAttribute>[] mAttributes;
    public Set<Integer> mAttributesOffsets;
    public ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    public String mFilename;
    public boolean mHasThumbnail;
    public boolean mHasThumbnailStrips;
    public boolean mIsExifDataOnly;
    public int mMimeType;
    public boolean mModified;
    public int mOffsetToExifData;
    public int mOrfMakerNoteOffset;
    public int mOrfThumbnailLength;
    public int mOrfThumbnailOffset;
    public FileDescriptor mSeekableFileDescriptor;
    public byte[] mThumbnailBytes;
    public int mThumbnailCompression;
    public int mThumbnailLength;
    public int mThumbnailOffset;
    public boolean mXmpIsFromSeparateMarker;

    public static boolean isSupportedFormatForSavingAttributes(int i) {
        return i == 4 || i == 13 || i == 14 || i == 3 || i == 0;
    }

    public static boolean shouldSupportSeek(int i) {
        return (i == 4 || i == 9 || i == 13 || i == 14) ? false : true;
    }

    static {
        ExifTag[] exifTagArr = {new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7), new ExifTag("Xmp", 700, 1)};
        IFD_TIFF_TAGS = exifTagArr;
        ExifTag[] exifTagArr2 = {new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("SensitivityType", 34864, 3), new ExifTag("StandardOutputSensitivity", 34865, 4), new ExifTag("RecommendedExposureIndex", 34866, 4), new ExifTag("ISOSpeed", 34867, 4), new ExifTag("ISOSpeedLatitudeyyy", 34868, 4), new ExifTag("ISOSpeedLatitudezzz", 34869, 4), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("OffsetTime", 36880, 2), new ExifTag("OffsetTimeOriginal", 36881, 2), new ExifTag("OffsetTimeDigitized", 36882, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("CameraOwnerName", 42032, 2), new ExifTag("BodySerialNumber", 42033, 2), new ExifTag("LensSpecification", 42034, 5), new ExifTag("LensMake", 42035, 2), new ExifTag("LensModel", 42036, 2), new ExifTag("Gamma", 42240, 5), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
        IFD_EXIF_TAGS = exifTagArr2;
        ExifTag[] exifTagArr3 = {new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5, 10), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5, 10), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3), new ExifTag("GPSHPositioningError", 31, 5)};
        IFD_GPS_TAGS = exifTagArr3;
        ExifTag[] exifTagArr4 = {new ExifTag("InteroperabilityIndex", 1, 2)};
        IFD_INTEROPERABILITY_TAGS = exifTagArr4;
        ExifTag[] exifTagArr5 = {new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("ThumbnailOrientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Xmp", 700, 1), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
        IFD_THUMBNAIL_TAGS = exifTagArr5;
        ExifTag[] exifTagArr6 = {new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4)};
        ORF_MAKER_NOTE_TAGS = exifTagArr6;
        ExifTag[] exifTagArr7 = {new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4)};
        ORF_CAMERA_SETTINGS_TAGS = exifTagArr7;
        ExifTag[] exifTagArr8 = {new ExifTag("AspectFrame", 4371, 3)};
        ORF_IMAGE_PROCESSING_TAGS = exifTagArr8;
        ExifTag[] exifTagArr9 = {new ExifTag("ColorSpace", 55, 3)};
        PEF_TAGS = exifTagArr9;
        ExifTag[][] exifTagArr10 = {exifTagArr, exifTagArr2, exifTagArr3, exifTagArr4, exifTagArr5, exifTagArr, exifTagArr6, exifTagArr7, exifTagArr8, exifTagArr9};
        EXIF_TAGS = exifTagArr10;
        sExifTagMapsForReading = new HashMap[exifTagArr10.length];
        sExifTagMapsForWriting = new HashMap[exifTagArr10.length];
        Charset forName = Charset.forName("US-ASCII");
        ASCII = forName;
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(forName);
        IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(forName);
        Locale locale = Locale.US;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", locale);
        sFormatterPrimary = simpleDateFormat;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        sFormatterSecondary = simpleDateFormat2;
        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
        int i = 0;
        while (true) {
            ExifTag[][] exifTagArr11 = EXIF_TAGS;
            if (i < exifTagArr11.length) {
                sExifTagMapsForReading[i] = new HashMap<>();
                sExifTagMapsForWriting[i] = new HashMap<>();
                for (ExifTag exifTag : exifTagArr11[i]) {
                    sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                    sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
                }
                i++;
            } else {
                HashMap<Integer, Integer> hashMap = sExifPointerTagMap;
                ExifTag[] exifTagArr12 = EXIF_POINTER_TAGS;
                hashMap.put(Integer.valueOf(exifTagArr12[0].number), 5);
                hashMap.put(Integer.valueOf(exifTagArr12[1].number), 1);
                hashMap.put(Integer.valueOf(exifTagArr12[2].number), 2);
                hashMap.put(Integer.valueOf(exifTagArr12[3].number), 3);
                hashMap.put(Integer.valueOf(exifTagArr12[4].number), 7);
                hashMap.put(Integer.valueOf(exifTagArr12[5].number), 8);
                return;
            }
        }
    }

    public static class Rational {
        public final long denominator;
        public final long numerator;

        public Rational(double d) {
            this((long) (d * 10000.0d), 10000);
        }

        public Rational(long j, long j2) {
            if (j2 == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }

        public String toString() {
            return this.numerator + "/" + this.denominator;
        }

        public double calculate() {
            return ((double) this.numerator) / ((double) this.denominator);
        }
    }

    public static class ExifAttribute {
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        public ExifAttribute(int i, int i2, byte[] bArr) {
            this(i, i2, -1, bArr);
        }

        public ExifAttribute(int i, int i2, long j, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytesOffset = j;
            this.bytes = bArr;
        }

        public static ExifAttribute createUShort(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * iArr.length)]);
            wrap.order(byteOrder);
            for (int i : iArr) {
                wrap.putShort((short) i);
            }
            return new ExifAttribute(3, iArr.length, wrap.array());
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            return createUShort(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createULong(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * jArr.length)]);
            wrap.order(byteOrder);
            for (long j : jArr) {
                wrap.putInt((int) j);
            }
            return new ExifAttribute(4, jArr.length, wrap.array());
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            return createULong(new long[]{j}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * iArr.length)]);
            wrap.order(byteOrder);
            for (int putInt : iArr) {
                wrap.putInt(putInt);
            }
            return new ExifAttribute(9, iArr.length, wrap.array());
        }

        public static ExifAttribute createByte(String str) {
            if (str.length() != 1 || str.charAt(0) < '0' || str.charAt(0) > '1') {
                byte[] bytes2 = str.getBytes(ExifInterface.ASCII);
                return new ExifAttribute(1, bytes2.length, bytes2);
            }
            return new ExifAttribute(1, 1, new byte[]{(byte) (str.charAt(0) - '0')});
        }

        public static ExifAttribute createString(String str) {
            byte[] bytes2 = (str + 0).getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes2.length, bytes2);
        }

        public static ExifAttribute createURational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(5, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return createURational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(10, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createDouble(double[] dArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * dArr.length)]);
            wrap.order(byteOrder);
            for (double putDouble : dArr) {
                wrap.putDouble(putDouble);
            }
            return new ExifAttribute(12, dArr.length, wrap.array());
        }

        public String toString() {
            return "(" + ExifInterface.IFD_FORMAT_NAMES[this.format] + ", data length:" + this.bytes.length + ")";
        }

        /* JADX WARNING: Removed duplicated region for block: B:163:0x019b A[SYNTHETIC, Splitter:B:163:0x019b] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object getValue(java.nio.ByteOrder r11) {
            /*
                r10 = this;
                java.lang.String r0 = "IOException occurred while closing InputStream"
                java.lang.String r1 = "ExifInterface"
                r2 = 0
                androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r3 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ IOException -> 0x0185, all -> 0x0183 }
                byte[] r4 = r10.bytes     // Catch:{ IOException -> 0x0185, all -> 0x0183 }
                r3.<init>((byte[]) r4)     // Catch:{ IOException -> 0x0185, all -> 0x0183 }
                r3.setByteOrder(r11)     // Catch:{ IOException -> 0x0181 }
                int r11 = r10.format     // Catch:{ IOException -> 0x0181 }
                r4 = 0
                r5 = 1
                switch(r11) {
                    case 1: goto L_0x0148;
                    case 2: goto L_0x00fd;
                    case 3: goto L_0x00e3;
                    case 4: goto L_0x00c9;
                    case 5: goto L_0x00a6;
                    case 6: goto L_0x0148;
                    case 7: goto L_0x00fd;
                    case 8: goto L_0x008c;
                    case 9: goto L_0x0072;
                    case 10: goto L_0x004d;
                    case 11: goto L_0x0032;
                    case 12: goto L_0x0018;
                    default: goto L_0x0016;
                }     // Catch:{ IOException -> 0x0181 }
            L_0x0016:
                goto L_0x0178
            L_0x0018:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                double[] r11 = new double[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x001c:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x0029
                double r5 = r3.readDouble()     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x001c
            L_0x0029:
                r3.close()     // Catch:{ IOException -> 0x002d }
                goto L_0x0031
            L_0x002d:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x0031:
                return r11
            L_0x0032:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                double[] r11 = new double[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x0036:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x0044
                float r5 = r3.readFloat()     // Catch:{ IOException -> 0x0181 }
                double r5 = (double) r5     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x0036
            L_0x0044:
                r3.close()     // Catch:{ IOException -> 0x0048 }
                goto L_0x004c
            L_0x0048:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x004c:
                return r11
            L_0x004d:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                androidx.exifinterface.media.ExifInterface$Rational[] r11 = new androidx.exifinterface.media.ExifInterface.Rational[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x0051:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x0069
                int r5 = r3.readInt()     // Catch:{ IOException -> 0x0181 }
                long r5 = (long) r5     // Catch:{ IOException -> 0x0181 }
                int r7 = r3.readInt()     // Catch:{ IOException -> 0x0181 }
                long r7 = (long) r7     // Catch:{ IOException -> 0x0181 }
                androidx.exifinterface.media.ExifInterface$Rational r9 = new androidx.exifinterface.media.ExifInterface$Rational     // Catch:{ IOException -> 0x0181 }
                r9.<init>(r5, r7)     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r9     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x0051
            L_0x0069:
                r3.close()     // Catch:{ IOException -> 0x006d }
                goto L_0x0071
            L_0x006d:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x0071:
                return r11
            L_0x0072:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                int[] r11 = new int[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x0076:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x0083
                int r5 = r3.readInt()     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x0076
            L_0x0083:
                r3.close()     // Catch:{ IOException -> 0x0087 }
                goto L_0x008b
            L_0x0087:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x008b:
                return r11
            L_0x008c:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                int[] r11 = new int[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x0090:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x009d
                short r5 = r3.readShort()     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x0090
            L_0x009d:
                r3.close()     // Catch:{ IOException -> 0x00a1 }
                goto L_0x00a5
            L_0x00a1:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x00a5:
                return r11
            L_0x00a6:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                androidx.exifinterface.media.ExifInterface$Rational[] r11 = new androidx.exifinterface.media.ExifInterface.Rational[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x00aa:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x00c0
                long r5 = r3.readUnsignedInt()     // Catch:{ IOException -> 0x0181 }
                long r7 = r3.readUnsignedInt()     // Catch:{ IOException -> 0x0181 }
                androidx.exifinterface.media.ExifInterface$Rational r9 = new androidx.exifinterface.media.ExifInterface$Rational     // Catch:{ IOException -> 0x0181 }
                r9.<init>(r5, r7)     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r9     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x00aa
            L_0x00c0:
                r3.close()     // Catch:{ IOException -> 0x00c4 }
                goto L_0x00c8
            L_0x00c4:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x00c8:
                return r11
            L_0x00c9:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                long[] r11 = new long[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x00cd:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x00da
                long r5 = r3.readUnsignedInt()     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x00cd
            L_0x00da:
                r3.close()     // Catch:{ IOException -> 0x00de }
                goto L_0x00e2
            L_0x00de:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x00e2:
                return r11
            L_0x00e3:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                int[] r11 = new int[r11]     // Catch:{ IOException -> 0x0181 }
            L_0x00e7:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x00f4
                int r5 = r3.readUnsignedShort()     // Catch:{ IOException -> 0x0181 }
                r11[r4] = r5     // Catch:{ IOException -> 0x0181 }
                int r4 = r4 + 1
                goto L_0x00e7
            L_0x00f4:
                r3.close()     // Catch:{ IOException -> 0x00f8 }
                goto L_0x00fc
            L_0x00f8:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x00fc:
                return r11
            L_0x00fd:
                int r11 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                byte[] r6 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x0181 }
                int r6 = r6.length     // Catch:{ IOException -> 0x0181 }
                if (r11 < r6) goto L_0x011a
                r11 = r4
            L_0x0105:
                byte[] r6 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x0181 }
                int r7 = r6.length     // Catch:{ IOException -> 0x0181 }
                if (r11 >= r7) goto L_0x0117
                byte[] r7 = r10.bytes     // Catch:{ IOException -> 0x0181 }
                byte r7 = r7[r11]     // Catch:{ IOException -> 0x0181 }
                byte r8 = r6[r11]     // Catch:{ IOException -> 0x0181 }
                if (r7 == r8) goto L_0x0114
                r5 = r4
                goto L_0x0117
            L_0x0114:
                int r11 = r11 + 1
                goto L_0x0105
            L_0x0117:
                if (r5 == 0) goto L_0x011a
                int r4 = r6.length     // Catch:{ IOException -> 0x0181 }
            L_0x011a:
                java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0181 }
                r11.<init>()     // Catch:{ IOException -> 0x0181 }
            L_0x011f:
                int r5 = r10.numberOfComponents     // Catch:{ IOException -> 0x0181 }
                if (r4 >= r5) goto L_0x013b
                byte[] r5 = r10.bytes     // Catch:{ IOException -> 0x0181 }
                byte r5 = r5[r4]     // Catch:{ IOException -> 0x0181 }
                if (r5 != 0) goto L_0x012a
                goto L_0x013b
            L_0x012a:
                r6 = 32
                if (r5 < r6) goto L_0x0133
                char r5 = (char) r5     // Catch:{ IOException -> 0x0181 }
                r11.append(r5)     // Catch:{ IOException -> 0x0181 }
                goto L_0x0138
            L_0x0133:
                r5 = 63
                r11.append(r5)     // Catch:{ IOException -> 0x0181 }
            L_0x0138:
                int r4 = r4 + 1
                goto L_0x011f
            L_0x013b:
                java.lang.String r10 = r11.toString()     // Catch:{ IOException -> 0x0181 }
                r3.close()     // Catch:{ IOException -> 0x0143 }
                goto L_0x0147
            L_0x0143:
                r11 = move-exception
                android.util.Log.e(r1, r0, r11)
            L_0x0147:
                return r10
            L_0x0148:
                byte[] r10 = r10.bytes     // Catch:{ IOException -> 0x0181 }
                int r11 = r10.length     // Catch:{ IOException -> 0x0181 }
                if (r11 != r5) goto L_0x0168
                byte r11 = r10[r4]     // Catch:{ IOException -> 0x0181 }
                if (r11 < 0) goto L_0x0168
                if (r11 > r5) goto L_0x0168
                java.lang.String r10 = new java.lang.String     // Catch:{ IOException -> 0x0181 }
                char[] r5 = new char[r5]     // Catch:{ IOException -> 0x0181 }
                int r11 = r11 + 48
                char r11 = (char) r11     // Catch:{ IOException -> 0x0181 }
                r5[r4] = r11     // Catch:{ IOException -> 0x0181 }
                r10.<init>(r5)     // Catch:{ IOException -> 0x0181 }
                r3.close()     // Catch:{ IOException -> 0x0163 }
                goto L_0x0167
            L_0x0163:
                r11 = move-exception
                android.util.Log.e(r1, r0, r11)
            L_0x0167:
                return r10
            L_0x0168:
                java.lang.String r11 = new java.lang.String     // Catch:{ IOException -> 0x0181 }
                java.nio.charset.Charset r4 = androidx.exifinterface.media.ExifInterface.ASCII     // Catch:{ IOException -> 0x0181 }
                r11.<init>(r10, r4)     // Catch:{ IOException -> 0x0181 }
                r3.close()     // Catch:{ IOException -> 0x0173 }
                goto L_0x0177
            L_0x0173:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x0177:
                return r11
            L_0x0178:
                r3.close()     // Catch:{ IOException -> 0x017c }
                goto L_0x0180
            L_0x017c:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x0180:
                return r2
            L_0x0181:
                r10 = move-exception
                goto L_0x0187
            L_0x0183:
                r10 = move-exception
                goto L_0x0199
            L_0x0185:
                r10 = move-exception
                r3 = r2
            L_0x0187:
                java.lang.String r11 = "IOException occurred during reading a value"
                android.util.Log.w(r1, r11, r10)     // Catch:{ all -> 0x0197 }
                if (r3 == 0) goto L_0x0196
                r3.close()     // Catch:{ IOException -> 0x0192 }
                goto L_0x0196
            L_0x0192:
                r10 = move-exception
                android.util.Log.e(r1, r0, r10)
            L_0x0196:
                return r2
            L_0x0197:
                r10 = move-exception
                r2 = r3
            L_0x0199:
                if (r2 == 0) goto L_0x01a3
                r2.close()     // Catch:{ IOException -> 0x019f }
                goto L_0x01a3
            L_0x019f:
                r11 = move-exception
                android.util.Log.e(r1, r0, r11)
            L_0x01a3:
                throw r10
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (double) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return (double) iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        return rationalArr[0].calculate();
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i < jArr.length) {
                    sb.append(jArr[i]);
                    i++;
                    if (i != jArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i < iArr.length) {
                    sb.append(iArr[i]);
                    i++;
                    if (i != iArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i < dArr.length) {
                    sb.append(dArr[i]);
                    i++;
                    if (i != dArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    sb.append(rationalArr[i].numerator);
                    sb.append('/');
                    sb.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }
    }

    public static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        public ExifTag(String str, int i, int i2) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        public ExifTag(String str, int i, int i2, int i3) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }

        public boolean isFormatCompatible(int i) {
            int i2;
            int i3 = this.primaryFormat;
            if (i3 == 7 || i == 7 || i3 == i || (i2 = this.secondaryFormat) == i) {
                return true;
            }
            if ((i3 == 4 || i2 == 4) && i == 3) {
                return true;
            }
            if ((i3 == 9 || i2 == 9) && i == 8) {
                return true;
            }
            if ((i3 == 12 || i2 == 12) && i == 11) {
                return true;
            }
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ExifInterface(java.io.FileDescriptor r4) throws java.io.IOException {
        /*
            r3 = this;
            r3.<init>()
            androidx.exifinterface.media.ExifInterface$ExifTag[][] r0 = EXIF_TAGS
            int r1 = r0.length
            java.util.HashMap[] r1 = new java.util.HashMap[r1]
            r3.mAttributes = r1
            java.util.HashSet r1 = new java.util.HashSet
            int r0 = r0.length
            r1.<init>(r0)
            r3.mAttributesOffsets = r1
            java.nio.ByteOrder r0 = java.nio.ByteOrder.BIG_ENDIAN
            r3.mExifByteOrder = r0
            if (r4 == 0) goto L_0x0055
            r0 = 0
            r3.mAssetInputStream = r0
            r3.mFilename = r0
            r1 = 0
            boolean r2 = isSeekableFD(r4)
            if (r2 == 0) goto L_0x0035
            r3.mSeekableFileDescriptor = r4
            java.io.FileDescriptor r4 = androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.dup(r4)     // Catch:{ Exception -> 0x002c }
            r1 = 1
            goto L_0x0037
        L_0x002c:
            r3 = move-exception
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r0 = "Failed to duplicate file descriptor"
            r4.<init>(r0, r3)
            throw r4
        L_0x0035:
            r3.mSeekableFileDescriptor = r0
        L_0x0037:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ all -> 0x004b }
            r2.<init>(r4)     // Catch:{ all -> 0x004b }
            r3.loadAttributes(r2)     // Catch:{ all -> 0x0048 }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r2)
            if (r1 == 0) goto L_0x0047
            androidx.exifinterface.media.ExifInterfaceUtils.closeFileDescriptor(r4)
        L_0x0047:
            return
        L_0x0048:
            r3 = move-exception
            r0 = r2
            goto L_0x004c
        L_0x004b:
            r3 = move-exception
        L_0x004c:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r0)
            if (r1 == 0) goto L_0x0054
            androidx.exifinterface.media.ExifInterfaceUtils.closeFileDescriptor(r4)
        L_0x0054:
            throw r3
        L_0x0055:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r4 = "fileDescriptor cannot be null"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.<init>(java.io.FileDescriptor):void");
    }

    public final ExifAttribute getExifAttribute(String str) {
        if (str != null) {
            if ("ISOSpeedRatings".equals(str)) {
                if (DEBUG) {
                    Log.d("ExifInterface", "getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
                }
                str = "PhotographicSensitivity";
            }
            for (int i = 0; i < EXIF_TAGS.length; i++) {
                ExifAttribute exifAttribute = this.mAttributes[i].get(str);
                if (exifAttribute != null) {
                    return exifAttribute;
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public String getAttribute(String str) {
        if (str != null) {
            ExifAttribute exifAttribute = getExifAttribute(str);
            if (exifAttribute != null) {
                if (!sTagSetForCompatibility.contains(str)) {
                    return exifAttribute.getStringValue(this.mExifByteOrder);
                }
                if (str.equals("GPSTimeStamp")) {
                    int i = exifAttribute.format;
                    if (i == 5 || i == 10) {
                        Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                        if (rationalArr == null || rationalArr.length != 3) {
                            Log.w("ExifInterface", "Invalid GPS Timestamp array. array=" + Arrays.toString(rationalArr));
                            return null;
                        }
                        Rational rational = rationalArr[0];
                        Rational rational2 = rationalArr[1];
                        Rational rational3 = rationalArr[2];
                        return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) rational.numerator) / ((float) rational.denominator))), Integer.valueOf((int) (((float) rational2.numerator) / ((float) rational2.denominator))), Integer.valueOf((int) (((float) rational3.numerator) / ((float) rational3.denominator)))});
                    }
                    Log.w("ExifInterface", "GPS Timestamp format is not rational. format=" + exifAttribute.format);
                    return null;
                }
                try {
                    return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
                } catch (NumberFormatException unused) {
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:98:0x02ba, code lost:
        r15 = 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAttribute(java.lang.String r18, java.lang.String r19) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            if (r1 == 0) goto L_0x0368
            java.lang.String r3 = "DateTime"
            boolean r3 = r3.equals(r1)
            java.lang.String r4 = " : "
            java.lang.String r5 = "Invalid value for "
            java.lang.String r6 = "ExifInterface"
            if (r3 != 0) goto L_0x0026
            java.lang.String r3 = "DateTimeOriginal"
            boolean r3 = r3.equals(r1)
            if (r3 != 0) goto L_0x0026
            java.lang.String r3 = "DateTimeDigitized"
            boolean r3 = r3.equals(r1)
            if (r3 == 0) goto L_0x006d
        L_0x0026:
            if (r2 == 0) goto L_0x006d
            java.util.regex.Pattern r3 = DATETIME_PRIMARY_FORMAT_PATTERN
            java.util.regex.Matcher r3 = r3.matcher(r2)
            boolean r3 = r3.find()
            java.util.regex.Pattern r7 = DATETIME_SECONDARY_FORMAT_PATTERN
            java.util.regex.Matcher r7 = r7.matcher(r2)
            boolean r7 = r7.find()
            int r8 = r19.length()
            r9 = 19
            if (r8 != r9) goto L_0x0054
            if (r3 != 0) goto L_0x0049
            if (r7 != 0) goto L_0x0049
            goto L_0x0054
        L_0x0049:
            if (r7 == 0) goto L_0x006d
            java.lang.String r3 = "-"
            java.lang.String r7 = ":"
            java.lang.String r2 = r2.replaceAll(r3, r7)
            goto L_0x006d
        L_0x0054:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            r0.append(r1)
            r0.append(r4)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r6, r0)
            return
        L_0x006d:
            java.lang.String r3 = "ISOSpeedRatings"
            boolean r3 = r3.equals(r1)
            if (r3 == 0) goto L_0x0080
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x007e
            java.lang.String r1 = "setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY."
            android.util.Log.d(r6, r1)
        L_0x007e:
            java.lang.String r1 = "PhotographicSensitivity"
        L_0x0080:
            r3 = 2
            r7 = 1
            if (r2 == 0) goto L_0x0119
            java.util.HashSet<java.lang.String> r8 = sTagSetForCompatibility
            boolean r8 = r8.contains(r1)
            if (r8 == 0) goto L_0x0119
            java.lang.String r8 = "GPSTimeStamp"
            boolean r8 = r1.equals(r8)
            if (r8 == 0) goto L_0x00f2
            java.util.regex.Pattern r8 = GPS_TIMESTAMP_PATTERN
            java.util.regex.Matcher r8 = r8.matcher(r2)
            boolean r9 = r8.find()
            if (r9 != 0) goto L_0x00b9
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            r0.append(r1)
            r0.append(r4)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r6, r0)
            return
        L_0x00b9:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = r8.group(r7)
            int r4 = java.lang.Integer.parseInt(r4)
            r2.append(r4)
            java.lang.String r4 = "/1,"
            r2.append(r4)
            java.lang.String r5 = r8.group(r3)
            int r5 = java.lang.Integer.parseInt(r5)
            r2.append(r5)
            r2.append(r4)
            r4 = 3
            java.lang.String r4 = r8.group(r4)
            int r4 = java.lang.Integer.parseInt(r4)
            r2.append(r4)
            java.lang.String r4 = "/1"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            goto L_0x0119
        L_0x00f2:
            double r8 = java.lang.Double.parseDouble(r2)     // Catch:{ NumberFormatException -> 0x0100 }
            androidx.exifinterface.media.ExifInterface$Rational r10 = new androidx.exifinterface.media.ExifInterface$Rational     // Catch:{ NumberFormatException -> 0x0100 }
            r10.<init>(r8)     // Catch:{ NumberFormatException -> 0x0100 }
            java.lang.String r2 = r10.toString()     // Catch:{ NumberFormatException -> 0x0100 }
            goto L_0x0119
        L_0x0100:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            r0.append(r1)
            r0.append(r4)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r6, r0)
            return
        L_0x0119:
            r4 = 0
            r5 = r4
        L_0x011b:
            androidx.exifinterface.media.ExifInterface$ExifTag[][] r8 = EXIF_TAGS
            int r8 = r8.length
            if (r5 >= r8) goto L_0x0367
            r8 = 4
            if (r5 != r8) goto L_0x0129
            boolean r8 = r0.mHasThumbnail
            if (r8 != 0) goto L_0x0129
            goto L_0x035f
        L_0x0129:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifTag>[] r8 = sExifTagMapsForWriting
            r8 = r8[r5]
            java.lang.Object r8 = r8.get(r1)
            androidx.exifinterface.media.ExifInterface$ExifTag r8 = (androidx.exifinterface.media.ExifInterface.ExifTag) r8
            if (r8 == 0) goto L_0x035f
            if (r2 != 0) goto L_0x0140
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r8 = r0.mAttributes
            r8 = r8[r5]
            r8.remove(r1)
            goto L_0x035f
        L_0x0140:
            android.util.Pair r9 = guessDataFormat(r2)
            int r10 = r8.primaryFormat
            java.lang.Object r11 = r9.first
            java.lang.Integer r11 = (java.lang.Integer) r11
            int r11 = r11.intValue()
            r12 = -1
            if (r10 == r11) goto L_0x0210
            int r10 = r8.primaryFormat
            java.lang.Object r11 = r9.second
            java.lang.Integer r11 = (java.lang.Integer) r11
            int r11 = r11.intValue()
            if (r10 != r11) goto L_0x015f
            goto L_0x0210
        L_0x015f:
            int r10 = r8.secondaryFormat
            if (r10 == r12) goto L_0x017d
            java.lang.Object r11 = r9.first
            java.lang.Integer r11 = (java.lang.Integer) r11
            int r11 = r11.intValue()
            if (r10 == r11) goto L_0x0179
            int r10 = r8.secondaryFormat
            java.lang.Object r11 = r9.second
            java.lang.Integer r11 = (java.lang.Integer) r11
            int r11 = r11.intValue()
            if (r10 != r11) goto L_0x017d
        L_0x0179:
            int r8 = r8.secondaryFormat
            goto L_0x0212
        L_0x017d:
            int r10 = r8.primaryFormat
            if (r10 == r7) goto L_0x020e
            r11 = 7
            if (r10 == r11) goto L_0x020e
            if (r10 != r3) goto L_0x0188
            goto L_0x020e
        L_0x0188:
            boolean r10 = DEBUG
            if (r10 == 0) goto L_0x035f
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Given tag ("
            r10.append(r11)
            r10.append(r1)
            java.lang.String r11 = ") value didn't match with one of expected formats: "
            r10.append(r11)
            java.lang.String[] r11 = IFD_FORMAT_NAMES
            int r13 = r8.primaryFormat
            r13 = r11[r13]
            r10.append(r13)
            int r13 = r8.secondaryFormat
            java.lang.String r14 = ""
            java.lang.String r15 = ", "
            if (r13 != r12) goto L_0x01b1
            r8 = r14
            goto L_0x01c4
        L_0x01b1:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r13.append(r15)
            int r8 = r8.secondaryFormat
            r8 = r11[r8]
            r13.append(r8)
            java.lang.String r8 = r13.toString()
        L_0x01c4:
            r10.append(r8)
            java.lang.String r8 = " (guess: "
            r10.append(r8)
            java.lang.Object r8 = r9.first
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            r8 = r11[r8]
            r10.append(r8)
            java.lang.Object r8 = r9.second
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            if (r8 != r12) goto L_0x01e4
            goto L_0x01fd
        L_0x01e4:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r15)
            java.lang.Object r9 = r9.second
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r9 = r9.intValue()
            r9 = r11[r9]
            r8.append(r9)
            java.lang.String r14 = r8.toString()
        L_0x01fd:
            r10.append(r14)
            java.lang.String r8 = ")"
            r10.append(r8)
            java.lang.String r8 = r10.toString()
            android.util.Log.d(r6, r8)
            goto L_0x035f
        L_0x020e:
            r8 = r10
            goto L_0x0212
        L_0x0210:
            int r8 = r8.primaryFormat
        L_0x0212:
            java.lang.String r9 = "/"
            java.lang.String r10 = ","
            switch(r8) {
                case 1: goto L_0x0352;
                case 2: goto L_0x0345;
                case 3: goto L_0x031f;
                case 4: goto L_0x02f9;
                case 5: goto L_0x02bd;
                case 6: goto L_0x0219;
                case 7: goto L_0x0345;
                case 8: goto L_0x0219;
                case 9: goto L_0x0297;
                case 10: goto L_0x0259;
                case 11: goto L_0x0219;
                case 12: goto L_0x0234;
                default: goto L_0x0219;
            }
        L_0x0219:
            r15 = r7
            boolean r3 = DEBUG
            if (r3 == 0) goto L_0x0360
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Data format isn't one of expected formats: "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r6, r3)
            goto L_0x0360
        L_0x0234:
            java.lang.String[] r8 = r2.split(r10, r12)
            int r9 = r8.length
            double[] r9 = new double[r9]
            r10 = r4
        L_0x023c:
            int r11 = r8.length
            if (r10 >= r11) goto L_0x024a
            r11 = r8[r10]
            double r11 = java.lang.Double.parseDouble(r11)
            r9[r10] = r11
            int r10 = r10 + 1
            goto L_0x023c
        L_0x024a:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r8 = r0.mAttributes
            r8 = r8[r5]
            java.nio.ByteOrder r10 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r9 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createDouble(r9, r10)
            r8.put(r1, r9)
            goto L_0x035f
        L_0x0259:
            java.lang.String[] r8 = r2.split(r10, r12)
            int r10 = r8.length
            androidx.exifinterface.media.ExifInterface$Rational[] r10 = new androidx.exifinterface.media.ExifInterface.Rational[r10]
            r11 = r4
        L_0x0261:
            int r13 = r8.length
            if (r11 >= r13) goto L_0x0289
            r13 = r8[r11]
            java.lang.String[] r13 = r13.split(r9, r12)
            androidx.exifinterface.media.ExifInterface$Rational r14 = new androidx.exifinterface.media.ExifInterface$Rational
            r15 = r13[r4]
            double r3 = java.lang.Double.parseDouble(r15)
            long r3 = (long) r3
            r13 = r13[r7]
            r16 = r8
            double r7 = java.lang.Double.parseDouble(r13)
            long r7 = (long) r7
            r14.<init>(r3, r7)
            r10[r11] = r14
            int r11 = r11 + 1
            r8 = r16
            r3 = 2
            r4 = 0
            r7 = 1
            goto L_0x0261
        L_0x0289:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            java.nio.ByteOrder r4 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createSRational(r10, r4)
            r3.put(r1, r4)
            goto L_0x02ba
        L_0x0297:
            java.lang.String[] r3 = r2.split(r10, r12)
            int r4 = r3.length
            int[] r4 = new int[r4]
            r7 = 0
        L_0x029f:
            int r8 = r3.length
            if (r7 >= r8) goto L_0x02ad
            r8 = r3[r7]
            int r8 = java.lang.Integer.parseInt(r8)
            r4[r7] = r8
            int r7 = r7 + 1
            goto L_0x029f
        L_0x02ad:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            java.nio.ByteOrder r7 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createSLong(r4, r7)
            r3.put(r1, r4)
        L_0x02ba:
            r15 = 1
            goto L_0x0360
        L_0x02bd:
            java.lang.String[] r3 = r2.split(r10, r12)
            int r4 = r3.length
            androidx.exifinterface.media.ExifInterface$Rational[] r4 = new androidx.exifinterface.media.ExifInterface.Rational[r4]
            r7 = 0
        L_0x02c5:
            int r8 = r3.length
            if (r7 >= r8) goto L_0x02e9
            r8 = r3[r7]
            java.lang.String[] r8 = r8.split(r9, r12)
            androidx.exifinterface.media.ExifInterface$Rational r10 = new androidx.exifinterface.media.ExifInterface$Rational
            r11 = 0
            r13 = r8[r11]
            double r13 = java.lang.Double.parseDouble(r13)
            long r13 = (long) r13
            r15 = 1
            r8 = r8[r15]
            double r11 = java.lang.Double.parseDouble(r8)
            long r11 = (long) r11
            r10.<init>(r13, r11)
            r4[r7] = r10
            int r7 = r7 + 1
            r12 = -1
            goto L_0x02c5
        L_0x02e9:
            r15 = 1
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            java.nio.ByteOrder r7 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createURational((androidx.exifinterface.media.ExifInterface.Rational[]) r4, (java.nio.ByteOrder) r7)
            r3.put(r1, r4)
            goto L_0x0360
        L_0x02f9:
            r15 = r7
            r3 = r12
            java.lang.String[] r3 = r2.split(r10, r3)
            int r4 = r3.length
            long[] r4 = new long[r4]
            r7 = 0
        L_0x0303:
            int r8 = r3.length
            if (r7 >= r8) goto L_0x0311
            r8 = r3[r7]
            long r8 = java.lang.Long.parseLong(r8)
            r4[r7] = r8
            int r7 = r7 + 1
            goto L_0x0303
        L_0x0311:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            java.nio.ByteOrder r7 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong((long[]) r4, (java.nio.ByteOrder) r7)
            r3.put(r1, r4)
            goto L_0x0360
        L_0x031f:
            r15 = r7
            r3 = r12
            java.lang.String[] r3 = r2.split(r10, r3)
            int r4 = r3.length
            int[] r4 = new int[r4]
            r7 = 0
        L_0x0329:
            int r8 = r3.length
            if (r7 >= r8) goto L_0x0337
            r8 = r3[r7]
            int r8 = java.lang.Integer.parseInt(r8)
            r4[r7] = r8
            int r7 = r7 + 1
            goto L_0x0329
        L_0x0337:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            java.nio.ByteOrder r7 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort((int[]) r4, (java.nio.ByteOrder) r7)
            r3.put(r1, r4)
            goto L_0x0360
        L_0x0345:
            r15 = r7
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createString(r2)
            r3.put(r1, r4)
            goto L_0x0360
        L_0x0352:
            r15 = r7
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r5]
            androidx.exifinterface.media.ExifInterface$ExifAttribute r4 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createByte(r2)
            r3.put(r1, r4)
            goto L_0x0360
        L_0x035f:
            r15 = r7
        L_0x0360:
            int r5 = r5 + 1
            r7 = r15
            r3 = 2
            r4 = 0
            goto L_0x011b
        L_0x0367:
            return
        L_0x0368:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "tag shouldn't be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.setAttribute(java.lang.String, java.lang.String):void");
    }

    public final void removeAttribute(String str) {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            this.mAttributes[i].remove(str);
        }
    }

    public final void loadAttributes(InputStream inputStream) {
        if (inputStream != null) {
            int i = 0;
            while (i < EXIF_TAGS.length) {
                try {
                    this.mAttributes[i] = new HashMap<>();
                    i++;
                } catch (IOException | UnsupportedOperationException e) {
                    boolean z = DEBUG;
                    if (z) {
                        Log.w("ExifInterface", "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.", e);
                    }
                    addDefaultValuesForCompatibility();
                    if (!z) {
                        return;
                    }
                } catch (Throwable th) {
                    addDefaultValuesForCompatibility();
                    if (DEBUG) {
                        printAttributes();
                    }
                    throw th;
                }
            }
            if (!this.mIsExifDataOnly) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 5000);
                this.mMimeType = getMimeType(bufferedInputStream);
                inputStream = bufferedInputStream;
            }
            if (shouldSupportSeek(this.mMimeType)) {
                SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream = new SeekableByteOrderedDataInputStream(inputStream);
                if (this.mIsExifDataOnly) {
                    getStandaloneAttributes(seekableByteOrderedDataInputStream);
                } else {
                    int i2 = this.mMimeType;
                    if (i2 == 12) {
                        getHeifAttributes(seekableByteOrderedDataInputStream);
                    } else if (i2 == 7) {
                        getOrfAttributes(seekableByteOrderedDataInputStream);
                    } else if (i2 == 10) {
                        getRw2Attributes(seekableByteOrderedDataInputStream);
                    } else {
                        getRawAttributes(seekableByteOrderedDataInputStream);
                    }
                }
                seekableByteOrderedDataInputStream.seek((long) this.mOffsetToExifData);
                setThumbnailData(seekableByteOrderedDataInputStream);
            } else {
                ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(inputStream);
                int i3 = this.mMimeType;
                if (i3 == 4) {
                    getJpegAttributes(byteOrderedDataInputStream, 0, 0);
                } else if (i3 == 13) {
                    getPngAttributes(byteOrderedDataInputStream);
                } else if (i3 == 9) {
                    getRafAttributes(byteOrderedDataInputStream);
                } else if (i3 == 14) {
                    getWebpAttributes(byteOrderedDataInputStream);
                }
            }
            addDefaultValuesForCompatibility();
            if (!DEBUG) {
                return;
            }
            printAttributes();
            return;
        }
        throw new NullPointerException("inputstream shouldn't be null");
    }

    public static boolean isSeekableFD(FileDescriptor fileDescriptor) {
        try {
            ExifInterfaceUtils.Api21Impl.lseek(fileDescriptor, 0, OsConstants.SEEK_CUR);
            return true;
        } catch (Exception unused) {
            if (!DEBUG) {
                return false;
            }
            Log.d("ExifInterface", "The file descriptor for the given input is not seekable");
            return false;
        }
    }

    public final void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; i++) {
            Log.d("ExifInterface", "The size of tag group[" + i + "]: " + this.mAttributes[i].size());
            for (Map.Entry next : this.mAttributes[i].entrySet()) {
                ExifAttribute exifAttribute = (ExifAttribute) next.getValue();
                Log.d("ExifInterface", "tagName: " + ((String) next.getKey()) + ", tagType: " + exifAttribute.toString() + ", tagValue: '" + exifAttribute.getStringValue(this.mExifByteOrder) + "'");
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.io.BufferedInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.io.BufferedInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v19, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v20, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v21, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v23, resolved type: java.io.FileInputStream} */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x015a, code lost:
        r2.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00dc, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00dd, code lost:
        r9 = null;
        r1 = r6;
        r6 = r8;
        r8 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00e2, code lost:
        r7 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00e3, code lost:
        r8 = null;
        r9 = null;
        r1 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00e7, code lost:
        r13 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e8, code lost:
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00f9, code lost:
        androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.lseek(r13.mSeekableFileDescriptor, 0, android.system.OsConstants.SEEK_SET);
        r1 = new java.io.FileOutputStream(r13.mSeekableFileDescriptor);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0108, code lost:
        r1 = new java.io.FileOutputStream(r13.mFilename);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00e7 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:27:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00f9 A[Catch:{ Exception -> 0x0125, all -> 0x0121 }] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0108 A[Catch:{ Exception -> 0x0125, all -> 0x0121 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveAttributes() throws java.io.IOException {
        /*
            r13 = this;
            int r0 = r13.mMimeType
            boolean r0 = isSupportedFormatForSavingAttributes(r0)
            if (r0 == 0) goto L_0x017f
            java.io.FileDescriptor r0 = r13.mSeekableFileDescriptor
            if (r0 != 0) goto L_0x0019
            java.lang.String r0 = r13.mFilename
            if (r0 == 0) goto L_0x0011
            goto L_0x0019
        L_0x0011:
            java.io.IOException r13 = new java.io.IOException
            java.lang.String r0 = "ExifInterface does not support saving attributes for the current input."
            r13.<init>(r0)
            throw r13
        L_0x0019:
            boolean r0 = r13.mHasThumbnail
            if (r0 == 0) goto L_0x002e
            boolean r0 = r13.mHasThumbnailStrips
            if (r0 == 0) goto L_0x002e
            boolean r0 = r13.mAreThumbnailStripsConsecutive
            if (r0 == 0) goto L_0x0026
            goto L_0x002e
        L_0x0026:
            java.io.IOException r13 = new java.io.IOException
            java.lang.String r0 = "ExifInterface does not support saving attributes when the image file has non-consecutive thumbnail strips"
            r13.<init>(r0)
            throw r13
        L_0x002e:
            r0 = 1
            r13.mModified = r0
            byte[] r1 = r13.getThumbnail()
            r13.mThumbnailBytes = r1
            r1 = 0
            java.lang.String r2 = "temp"
            java.lang.String r3 = "tmp"
            java.io.File r2 = java.io.File.createTempFile(r2, r3)     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            java.lang.String r3 = r13.mFilename     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            r4 = 0
            if (r3 == 0) goto L_0x0050
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            java.lang.String r6 = r13.mFilename     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            r3.<init>(r6)     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            goto L_0x005e
        L_0x0050:
            java.io.FileDescriptor r3 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            int r6 = android.system.OsConstants.SEEK_SET     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.lseek(r3, r4, r6)     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            java.io.FileDescriptor r6 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x016d, all -> 0x016a }
            r3.<init>(r6)     // Catch:{ Exception -> 0x016d, all -> 0x016a }
        L_0x005e:
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0166, all -> 0x0162 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x0166, all -> 0x0162 }
            androidx.exifinterface.media.ExifInterfaceUtils.copy(r3, r6)     // Catch:{ Exception -> 0x0160, all -> 0x015e }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r3)
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r6)
            r3 = 0
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00eb, all -> 0x00e7 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x00eb, all -> 0x00e7 }
            java.lang.String r7 = r13.mFilename     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            if (r7 == 0) goto L_0x007e
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            java.lang.String r8 = r13.mFilename     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            goto L_0x008c
        L_0x007e:
            java.io.FileDescriptor r7 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            int r8 = android.system.OsConstants.SEEK_SET     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.lseek(r7, r4, r8)     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            java.io.FileDescriptor r8 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x00e2, all -> 0x00e7 }
        L_0x008c:
            java.io.BufferedInputStream r8 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00dc, all -> 0x00e7 }
            r8.<init>(r6)     // Catch:{ Exception -> 0x00dc, all -> 0x00e7 }
            java.io.BufferedOutputStream r9 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x00d6, all -> 0x00d2 }
            r9.<init>(r7)     // Catch:{ Exception -> 0x00d6, all -> 0x00d2 }
            int r10 = r13.mMimeType     // Catch:{ Exception -> 0x00cd }
            r11 = 4
            if (r10 != r11) goto L_0x009f
            r13.saveJpegAttributes(r8, r9)     // Catch:{ Exception -> 0x00cd }
            goto L_0x00be
        L_0x009f:
            r11 = 13
            if (r10 != r11) goto L_0x00a7
            r13.savePngAttributes(r8, r9)     // Catch:{ Exception -> 0x00cd }
            goto L_0x00be
        L_0x00a7:
            r11 = 14
            if (r10 != r11) goto L_0x00af
            r13.saveWebpAttributes(r8, r9)     // Catch:{ Exception -> 0x00cd }
            goto L_0x00be
        L_0x00af:
            r11 = 3
            if (r10 == r11) goto L_0x00b4
            if (r10 != 0) goto L_0x00be
        L_0x00b4:
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataOutputStream r10 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataOutputStream     // Catch:{ Exception -> 0x00cd }
            java.nio.ByteOrder r11 = java.nio.ByteOrder.BIG_ENDIAN     // Catch:{ Exception -> 0x00cd }
            r10.<init>(r9, r11)     // Catch:{ Exception -> 0x00cd }
            r13.writeExifSegment(r10)     // Catch:{ Exception -> 0x00cd }
        L_0x00be:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r8)
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r9)
            r2.delete()
            r13.mThumbnailBytes = r1
            return
        L_0x00ca:
            r13 = move-exception
            goto L_0x0151
        L_0x00cd:
            r1 = move-exception
            r12 = r6
            r6 = r1
            r1 = r12
            goto L_0x00f0
        L_0x00d2:
            r13 = move-exception
            r9 = r1
            goto L_0x0151
        L_0x00d6:
            r9 = move-exception
            r12 = r9
            r9 = r1
            r1 = r6
            r6 = r12
            goto L_0x00f0
        L_0x00dc:
            r8 = move-exception
            r9 = r1
            r1 = r6
            r6 = r8
            r8 = r9
            goto L_0x00f0
        L_0x00e2:
            r7 = move-exception
            r8 = r1
            r9 = r8
            r1 = r6
            goto L_0x00ee
        L_0x00e7:
            r13 = move-exception
            r9 = r1
            goto L_0x0152
        L_0x00eb:
            r7 = move-exception
            r8 = r1
            r9 = r8
        L_0x00ee:
            r6 = r7
            r7 = r9
        L_0x00f0:
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ Exception -> 0x012b, all -> 0x0128 }
            r10.<init>(r2)     // Catch:{ Exception -> 0x012b, all -> 0x0128 }
            java.lang.String r1 = r13.mFilename     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            if (r1 != 0) goto L_0x0108
            java.io.FileDescriptor r1 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            int r11 = android.system.OsConstants.SEEK_SET     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.lseek(r1, r4, r11)     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            java.io.FileDescriptor r13 = r13.mSeekableFileDescriptor     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            r1.<init>(r13)     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            goto L_0x010f
        L_0x0108:
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            java.lang.String r13 = r13.mFilename     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            r1.<init>(r13)     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
        L_0x010f:
            r7 = r1
            androidx.exifinterface.media.ExifInterfaceUtils.copy(r10, r7)     // Catch:{ Exception -> 0x0125, all -> 0x0121 }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r10)     // Catch:{ all -> 0x00ca }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r7)     // Catch:{ all -> 0x00ca }
            java.io.IOException r13 = new java.io.IOException     // Catch:{ all -> 0x00ca }
            java.lang.String r0 = "Failed to save new file"
            r13.<init>(r0, r6)     // Catch:{ all -> 0x00ca }
            throw r13     // Catch:{ all -> 0x00ca }
        L_0x0121:
            r13 = move-exception
            r0 = r3
            r1 = r10
            goto L_0x0148
        L_0x0125:
            r13 = move-exception
            r1 = r10
            goto L_0x012c
        L_0x0128:
            r13 = move-exception
            r0 = r3
            goto L_0x0148
        L_0x012b:
            r13 = move-exception
        L_0x012c:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x0147 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0147 }
            r4.<init>()     // Catch:{ all -> 0x0147 }
            java.lang.String r5 = "Failed to save new file. Original file is stored in "
            r4.append(r5)     // Catch:{ all -> 0x0147 }
            java.lang.String r5 = r2.getAbsolutePath()     // Catch:{ all -> 0x0147 }
            r4.append(r5)     // Catch:{ all -> 0x0147 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0147 }
            r3.<init>(r4, r13)     // Catch:{ all -> 0x0147 }
            throw r3     // Catch:{ all -> 0x0147 }
        L_0x0147:
            r13 = move-exception
        L_0x0148:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)     // Catch:{ all -> 0x014f }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r7)     // Catch:{ all -> 0x014f }
            throw r13     // Catch:{ all -> 0x014f }
        L_0x014f:
            r13 = move-exception
            r3 = r0
        L_0x0151:
            r1 = r8
        L_0x0152:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r9)
            if (r3 != 0) goto L_0x015d
            r2.delete()
        L_0x015d:
            throw r13
        L_0x015e:
            r13 = move-exception
            goto L_0x0164
        L_0x0160:
            r13 = move-exception
            goto L_0x0168
        L_0x0162:
            r13 = move-exception
            r6 = r1
        L_0x0164:
            r1 = r3
            goto L_0x0178
        L_0x0166:
            r13 = move-exception
            r6 = r1
        L_0x0168:
            r1 = r3
            goto L_0x016f
        L_0x016a:
            r13 = move-exception
            r6 = r1
            goto L_0x0178
        L_0x016d:
            r13 = move-exception
            r6 = r1
        L_0x016f:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0177 }
            java.lang.String r2 = "Failed to copy original file to temp file"
            r0.<init>(r2, r13)     // Catch:{ all -> 0x0177 }
            throw r0     // Catch:{ all -> 0x0177 }
        L_0x0177:
            r13 = move-exception
        L_0x0178:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r6)
            throw r13
        L_0x017f:
            java.io.IOException r13 = new java.io.IOException
            java.lang.String r0 = "ExifInterface only supports saving attributes for JPEG, PNG, WebP, and DNG formats."
            r13.<init>(r0)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.saveAttributes():void");
    }

    public byte[] getThumbnail() {
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return getThumbnailBytes();
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0064 A[SYNTHETIC, Splitter:B:34:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0081 A[Catch:{ Exception -> 0x0087 }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getThumbnailBytes() {
        /*
            r9 = this;
            java.lang.String r0 = "ExifInterface"
            boolean r1 = r9.mHasThumbnail
            r2 = 0
            if (r1 != 0) goto L_0x0008
            return r2
        L_0x0008:
            byte[] r1 = r9.mThumbnailBytes
            if (r1 == 0) goto L_0x000d
            return r1
        L_0x000d:
            android.content.res.AssetManager$AssetInputStream r1 = r9.mAssetInputStream     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            if (r1 == 0) goto L_0x002d
            boolean r3 = r1.markSupported()     // Catch:{ Exception -> 0x0029, all -> 0x0025 }
            if (r3 == 0) goto L_0x001c
            r1.reset()     // Catch:{ Exception -> 0x0029, all -> 0x0025 }
        L_0x001a:
            r3 = r2
            goto L_0x004e
        L_0x001c:
            java.lang.String r9 = "Cannot read thumbnail from inputstream without mark/reset support"
            android.util.Log.d(r0, r9)     // Catch:{ Exception -> 0x0029, all -> 0x0025 }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)
            return r2
        L_0x0025:
            r9 = move-exception
            r3 = r2
            goto L_0x00a5
        L_0x0029:
            r9 = move-exception
            r3 = r2
            goto L_0x0096
        L_0x002d:
            java.lang.String r1 = r9.mFilename     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            if (r1 == 0) goto L_0x0039
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            java.lang.String r3 = r9.mFilename     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            goto L_0x001a
        L_0x0039:
            java.io.FileDescriptor r1 = r9.mSeekableFileDescriptor     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            java.io.FileDescriptor r1 = androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.dup(r1)     // Catch:{ Exception -> 0x0093, all -> 0x0090 }
            r3 = 0
            int r5 = android.system.OsConstants.SEEK_SET     // Catch:{ Exception -> 0x008c, all -> 0x0089 }
            androidx.exifinterface.media.ExifInterfaceUtils.Api21Impl.lseek(r1, r3, r5)     // Catch:{ Exception -> 0x008c, all -> 0x0089 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x008c, all -> 0x0089 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x008c, all -> 0x0089 }
            r8 = r3
            r3 = r1
            r1 = r8
        L_0x004e:
            int r4 = r9.mThumbnailOffset     // Catch:{ Exception -> 0x0087 }
            int r5 = r9.mOffsetToExifData     // Catch:{ Exception -> 0x0087 }
            int r4 = r4 + r5
            long r4 = (long) r4     // Catch:{ Exception -> 0x0087 }
            long r4 = r1.skip(r4)     // Catch:{ Exception -> 0x0087 }
            int r6 = r9.mThumbnailOffset     // Catch:{ Exception -> 0x0087 }
            int r7 = r9.mOffsetToExifData     // Catch:{ Exception -> 0x0087 }
            int r6 = r6 + r7
            long r6 = (long) r6
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            java.lang.String r5 = "Corrupted image"
            if (r4 != 0) goto L_0x0081
            int r4 = r9.mThumbnailLength     // Catch:{ Exception -> 0x0087 }
            byte[] r4 = new byte[r4]     // Catch:{ Exception -> 0x0087 }
            int r6 = r1.read(r4)     // Catch:{ Exception -> 0x0087 }
            int r7 = r9.mThumbnailLength     // Catch:{ Exception -> 0x0087 }
            if (r6 != r7) goto L_0x007b
            r9.mThumbnailBytes = r4     // Catch:{ Exception -> 0x0087 }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)
            if (r3 == 0) goto L_0x007a
            androidx.exifinterface.media.ExifInterfaceUtils.closeFileDescriptor(r3)
        L_0x007a:
            return r4
        L_0x007b:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ Exception -> 0x0087 }
            r9.<init>(r5)     // Catch:{ Exception -> 0x0087 }
            throw r9     // Catch:{ Exception -> 0x0087 }
        L_0x0081:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ Exception -> 0x0087 }
            r9.<init>(r5)     // Catch:{ Exception -> 0x0087 }
            throw r9     // Catch:{ Exception -> 0x0087 }
        L_0x0087:
            r9 = move-exception
            goto L_0x0096
        L_0x0089:
            r9 = move-exception
            r3 = r1
            goto L_0x00a6
        L_0x008c:
            r9 = move-exception
            r3 = r1
            r1 = r2
            goto L_0x0096
        L_0x0090:
            r9 = move-exception
            r3 = r2
            goto L_0x00a6
        L_0x0093:
            r9 = move-exception
            r1 = r2
            r3 = r1
        L_0x0096:
            java.lang.String r4 = "Encountered exception while getting thumbnail"
            android.util.Log.d(r0, r4, r9)     // Catch:{ all -> 0x00a4 }
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r1)
            if (r3 == 0) goto L_0x00a3
            androidx.exifinterface.media.ExifInterfaceUtils.closeFileDescriptor(r3)
        L_0x00a3:
            return r2
        L_0x00a4:
            r9 = move-exception
        L_0x00a5:
            r2 = r1
        L_0x00a6:
            androidx.exifinterface.media.ExifInterfaceUtils.closeQuietly(r2)
            if (r3 == 0) goto L_0x00ae
            androidx.exifinterface.media.ExifInterfaceUtils.closeFileDescriptor(r3)
        L_0x00ae:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getThumbnailBytes():byte[]");
    }

    public final int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        if (isRafFormat(bArr)) {
            return 9;
        }
        if (isHeifFormat(bArr)) {
            return 12;
        }
        if (isOrfFormat(bArr)) {
            return 7;
        }
        if (isRw2Format(bArr)) {
            return 10;
        }
        if (isPngFormat(bArr)) {
            return 13;
        }
        return isWebpFormat(bArr) ? 14 : 0;
    }

    public static boolean isJpegFormat(byte[] bArr) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    public final boolean isRafFormat(byte[] bArr) throws IOException {
        byte[] bytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i = 0; i < bytes.length; i++) {
            if (bArr[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:58:0x0092 A[Catch:{ all -> 0x008b }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00a1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isHeifFormat(byte[] r14) throws java.io.IOException {
        /*
            r13 = this;
            r13 = 0
            r0 = 0
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r1 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ Exception -> 0x008d }
            r1.<init>((byte[]) r14)     // Catch:{ Exception -> 0x008d }
            int r0 = r1.readInt()     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            long r2 = (long) r0     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            r0 = 4
            byte[] r4 = new byte[r0]     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            r1.read(r4)     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            byte[] r5 = HEIF_TYPE_FTYP     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            boolean r4 = java.util.Arrays.equals(r4, r5)     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            if (r4 != 0) goto L_0x001e
            r1.close()
            return r13
        L_0x001e:
            r4 = 1
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            r7 = 16
            r9 = 8
            if (r6 != 0) goto L_0x0034
            long r2 = r1.readLong()     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            int r6 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r6 >= 0) goto L_0x0035
            r1.close()
            return r13
        L_0x0034:
            r7 = r9
        L_0x0035:
            int r6 = r14.length     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            long r11 = (long) r6     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            int r6 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r6 <= 0) goto L_0x003d
            int r14 = r14.length     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            long r2 = (long) r14
        L_0x003d:
            long r2 = r2 - r7
            int r14 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r14 >= 0) goto L_0x0046
            r1.close()
            return r13
        L_0x0046:
            byte[] r14 = new byte[r0]     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            r6 = 0
            r8 = r13
            r9 = r8
        L_0x004c:
            r10 = 4
            long r10 = r2 / r10
            int r10 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x0081
            int r10 = r1.read(r14)     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            if (r10 == r0) goto L_0x005e
            r1.close()
            return r13
        L_0x005e:
            int r10 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r10 != 0) goto L_0x0063
            goto L_0x007f
        L_0x0063:
            byte[] r10 = HEIF_BRAND_MIF1     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            boolean r10 = java.util.Arrays.equals(r14, r10)     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            r11 = 1
            if (r10 == 0) goto L_0x006e
            r8 = r11
            goto L_0x0077
        L_0x006e:
            byte[] r10 = HEIF_BRAND_HEIC     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            boolean r10 = java.util.Arrays.equals(r14, r10)     // Catch:{ Exception -> 0x0088, all -> 0x0085 }
            if (r10 == 0) goto L_0x0077
            r9 = r11
        L_0x0077:
            if (r8 == 0) goto L_0x007f
            if (r9 == 0) goto L_0x007f
            r1.close()
            return r11
        L_0x007f:
            long r6 = r6 + r4
            goto L_0x004c
        L_0x0081:
            r1.close()
            goto L_0x009e
        L_0x0085:
            r13 = move-exception
            r0 = r1
            goto L_0x009f
        L_0x0088:
            r14 = move-exception
            r0 = r1
            goto L_0x008e
        L_0x008b:
            r13 = move-exception
            goto L_0x009f
        L_0x008d:
            r14 = move-exception
        L_0x008e:
            boolean r1 = DEBUG     // Catch:{ all -> 0x008b }
            if (r1 == 0) goto L_0x0099
            java.lang.String r1 = "ExifInterface"
            java.lang.String r2 = "Exception parsing HEIF file type box."
            android.util.Log.d(r1, r2, r14)     // Catch:{ all -> 0x008b }
        L_0x0099:
            if (r0 == 0) goto L_0x009e
            r0.close()
        L_0x009e:
            return r13
        L_0x009f:
            if (r0 == 0) goto L_0x00a4
            r0.close()
        L_0x00a4:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.isHeifFormat(byte[]):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x002f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isOrfFormat(byte[] r4) throws java.io.IOException {
        /*
            r3 = this;
            r0 = 0
            r1 = 0
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r2 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ Exception -> 0x002d, all -> 0x0026 }
            r2.<init>((byte[]) r4)     // Catch:{ Exception -> 0x002d, all -> 0x0026 }
            java.nio.ByteOrder r4 = r3.readByteOrder(r2)     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r3.mExifByteOrder = r4     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r2.setByteOrder(r4)     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            short r3 = r2.readShort()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4 = 20306(0x4f52, float:2.8455E-41)
            if (r3 == r4) goto L_0x001c
            r4 = 21330(0x5352, float:2.989E-41)
            if (r3 != r4) goto L_0x001d
        L_0x001c:
            r0 = 1
        L_0x001d:
            r2.close()
            return r0
        L_0x0021:
            r3 = move-exception
            r1 = r2
            goto L_0x0027
        L_0x0024:
            r1 = r2
            goto L_0x002d
        L_0x0026:
            r3 = move-exception
        L_0x0027:
            if (r1 == 0) goto L_0x002c
            r1.close()
        L_0x002c:
            throw r3
        L_0x002d:
            if (r1 == 0) goto L_0x0032
            r1.close()
        L_0x0032:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.isOrfFormat(byte[]):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isRw2Format(byte[] r4) throws java.io.IOException {
        /*
            r3 = this;
            r0 = 0
            r1 = 0
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r2 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ Exception -> 0x0029, all -> 0x0022 }
            r2.<init>((byte[]) r4)     // Catch:{ Exception -> 0x0029, all -> 0x0022 }
            java.nio.ByteOrder r4 = r3.readByteOrder(r2)     // Catch:{ Exception -> 0x0020, all -> 0x001d }
            r3.mExifByteOrder = r4     // Catch:{ Exception -> 0x0020, all -> 0x001d }
            r2.setByteOrder(r4)     // Catch:{ Exception -> 0x0020, all -> 0x001d }
            short r3 = r2.readShort()     // Catch:{ Exception -> 0x0020, all -> 0x001d }
            r4 = 85
            if (r3 != r4) goto L_0x0019
            r0 = 1
        L_0x0019:
            r2.close()
            return r0
        L_0x001d:
            r3 = move-exception
            r1 = r2
            goto L_0x0023
        L_0x0020:
            r1 = r2
            goto L_0x0029
        L_0x0022:
            r3 = move-exception
        L_0x0023:
            if (r1 == 0) goto L_0x0028
            r1.close()
        L_0x0028:
            throw r3
        L_0x0029:
            if (r1 == 0) goto L_0x002e
            r1.close()
        L_0x002e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.isRw2Format(byte[]):boolean");
    }

    public final boolean isPngFormat(byte[] bArr) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr2 = PNG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    public final boolean isWebpFormat(byte[] bArr) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr2 = WEBP_SIGNATURE_1;
            if (i >= bArr2.length) {
                int i2 = 0;
                while (true) {
                    byte[] bArr3 = WEBP_SIGNATURE_2;
                    if (i2 >= bArr3.length) {
                        return true;
                    }
                    if (bArr[WEBP_SIGNATURE_1.length + i2 + 4] != bArr3[i2]) {
                        return false;
                    }
                    i2++;
                }
            } else if (bArr[i] != bArr2[i]) {
                return false;
            } else {
                i++;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0184 A[LOOP:0: B:8:0x0038->B:60:0x0184, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x018e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getJpegAttributes(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r22, int r23, int r24) throws java.io.IOException {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r2 = r24
            boolean r3 = DEBUG
            java.lang.String r4 = "ExifInterface"
            if (r3 == 0) goto L_0x0020
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "getJpegAttributes starting with: "
            r3.append(r5)
            r3.append(r1)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r4, r3)
        L_0x0020:
            java.nio.ByteOrder r3 = java.nio.ByteOrder.BIG_ENDIAN
            r1.setByteOrder(r3)
            byte r3 = r22.readByte()
            java.lang.String r5 = "Invalid marker: "
            r6 = -1
            if (r3 != r6) goto L_0x01d8
            byte r7 = r22.readByte()
            r8 = -40
            if (r7 != r8) goto L_0x01bd
            r3 = 2
            r5 = r3
        L_0x0038:
            byte r7 = r22.readByte()
            if (r7 != r6) goto L_0x01a0
            r7 = 1
            int r5 = r5 + r7
            byte r8 = r22.readByte()
            boolean r9 = DEBUG
            if (r9 == 0) goto L_0x0062
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Found JPEG segment indicator: "
            r10.append(r11)
            r11 = r8 & 255(0xff, float:3.57E-43)
            java.lang.String r11 = java.lang.Integer.toHexString(r11)
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.d(r4, r10)
        L_0x0062:
            int r5 = r5 + r7
            r10 = -39
            if (r8 == r10) goto L_0x019a
            r10 = -38
            if (r8 != r10) goto L_0x006d
            goto L_0x019a
        L_0x006d:
            int r10 = r22.readUnsignedShort()
            int r10 = r10 - r3
            int r5 = r5 + r3
            if (r9 == 0) goto L_0x009e
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r11 = "JPEG segment: "
            r9.append(r11)
            r11 = r8 & 255(0xff, float:3.57E-43)
            java.lang.String r11 = java.lang.Integer.toHexString(r11)
            r9.append(r11)
            java.lang.String r11 = " (length: "
            r9.append(r11)
            int r11 = r10 + 2
            r9.append(r11)
            java.lang.String r11 = ")"
            r9.append(r11)
            java.lang.String r9 = r9.toString()
            android.util.Log.d(r4, r9)
        L_0x009e:
            java.lang.String r9 = "Invalid length"
            if (r10 < 0) goto L_0x0194
            r11 = -31
            r12 = 0
            if (r8 == r11) goto L_0x0121
            r11 = -2
            if (r8 == r11) goto L_0x00f3
            switch(r8) {
                case -64: goto L_0x00ba;
                case -63: goto L_0x00ba;
                case -62: goto L_0x00ba;
                case -61: goto L_0x00ba;
                default: goto L_0x00ad;
            }
        L_0x00ad:
            switch(r8) {
                case -59: goto L_0x00ba;
                case -58: goto L_0x00ba;
                case -57: goto L_0x00ba;
                default: goto L_0x00b0;
            }
        L_0x00b0:
            switch(r8) {
                case -55: goto L_0x00ba;
                case -54: goto L_0x00ba;
                case -53: goto L_0x00ba;
                default: goto L_0x00b3;
            }
        L_0x00b3:
            switch(r8) {
                case -51: goto L_0x00ba;
                case -50: goto L_0x00ba;
                case -49: goto L_0x00ba;
                default: goto L_0x00b6;
            }
        L_0x00b6:
            r20 = r4
            goto L_0x0182
        L_0x00ba:
            r1.skipFully(r7)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r7 = r0.mAttributes
            r7 = r7[r2]
            r8 = 4
            if (r2 == r8) goto L_0x00c7
            java.lang.String r11 = "ImageLength"
            goto L_0x00c9
        L_0x00c7:
            java.lang.String r11 = "ThumbnailImageLength"
        L_0x00c9:
            int r12 = r22.readUnsignedShort()
            long r12 = (long) r12
            java.nio.ByteOrder r14 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r12 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong((long) r12, (java.nio.ByteOrder) r14)
            r7.put(r11, r12)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r7 = r0.mAttributes
            r7 = r7[r2]
            if (r2 == r8) goto L_0x00e0
            java.lang.String r8 = "ImageWidth"
            goto L_0x00e2
        L_0x00e0:
            java.lang.String r8 = "ThumbnailImageWidth"
        L_0x00e2:
            int r11 = r22.readUnsignedShort()
            long r11 = (long) r11
            java.nio.ByteOrder r13 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r11 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong((long) r11, (java.nio.ByteOrder) r13)
            r7.put(r8, r11)
            int r10 = r10 + -5
            goto L_0x00b6
        L_0x00f3:
            byte[] r8 = new byte[r10]
            int r11 = r1.read(r8)
            if (r11 != r10) goto L_0x0119
            java.lang.String r10 = "UserComment"
            java.lang.String r11 = r0.getAttribute(r10)
            if (r11 != 0) goto L_0x0115
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r11 = r0.mAttributes
            r7 = r11[r7]
            java.lang.String r11 = new java.lang.String
            java.nio.charset.Charset r13 = ASCII
            r11.<init>(r8, r13)
            androidx.exifinterface.media.ExifInterface$ExifAttribute r8 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createString(r11)
            r7.put(r10, r8)
        L_0x0115:
            r20 = r4
            goto L_0x0181
        L_0x0119:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Invalid exif"
            r0.<init>(r1)
            throw r0
        L_0x0121:
            byte[] r8 = new byte[r10]
            r1.readFully(r8)
            int r11 = r5 + r10
            byte[] r13 = IDENTIFIER_EXIF_APP1
            boolean r14 = androidx.exifinterface.media.ExifInterfaceUtils.startsWith(r8, r13)
            if (r14 == 0) goto L_0x0147
            int r7 = r13.length
            byte[] r7 = java.util.Arrays.copyOfRange(r8, r7, r10)
            int r5 = r23 + r5
            int r8 = r13.length
            int r5 = r5 + r8
            r0.mOffsetToExifData = r5
            r0.readExifSegment(r7, r2)
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r5 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream
            r5.<init>((byte[]) r7)
            r0.setThumbnailData(r5)
            goto L_0x017e
        L_0x0147:
            byte[] r13 = IDENTIFIER_XMP_APP1
            boolean r14 = androidx.exifinterface.media.ExifInterfaceUtils.startsWith(r8, r13)
            if (r14 == 0) goto L_0x017e
            int r14 = r13.length
            int r5 = r5 + r14
            int r13 = r13.length
            byte[] r8 = java.util.Arrays.copyOfRange(r8, r13, r10)
            java.lang.String r10 = "Xmp"
            java.lang.String r13 = r0.getAttribute(r10)
            if (r13 != 0) goto L_0x017e
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r13 = r0.mAttributes
            r13 = r13[r12]
            androidx.exifinterface.media.ExifInterface$ExifAttribute r15 = new androidx.exifinterface.media.ExifInterface$ExifAttribute
            r16 = 1
            int r14 = r8.length
            r20 = r4
            long r3 = (long) r5
            r5 = r14
            r14 = r15
            r6 = r15
            r15 = r16
            r16 = r5
            r17 = r3
            r19 = r8
            r14.<init>(r15, r16, r17, r19)
            r13.put(r10, r6)
            r0.mXmpIsFromSeparateMarker = r7
            goto L_0x0180
        L_0x017e:
            r20 = r4
        L_0x0180:
            r5 = r11
        L_0x0181:
            r10 = r12
        L_0x0182:
            if (r10 < 0) goto L_0x018e
            r1.skipFully(r10)
            int r5 = r5 + r10
            r4 = r20
            r3 = 2
            r6 = -1
            goto L_0x0038
        L_0x018e:
            java.io.IOException r0 = new java.io.IOException
            r0.<init>(r9)
            throw r0
        L_0x0194:
            java.io.IOException r0 = new java.io.IOException
            r0.<init>(r9)
            throw r0
        L_0x019a:
            java.nio.ByteOrder r0 = r0.mExifByteOrder
            r1.setByteOrder(r0)
            return
        L_0x01a0:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Invalid marker:"
            r1.append(r2)
            r2 = r7 & 255(0xff, float:3.57E-43)
            java.lang.String r2 = java.lang.Integer.toHexString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x01bd:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r5)
            r2 = r3 & 255(0xff, float:3.57E-43)
            java.lang.String r2 = java.lang.Integer.toHexString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x01d8:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r5)
            r2 = r3 & 255(0xff, float:3.57E-43)
            java.lang.String r2 = java.lang.Integer.toHexString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getJpegAttributes(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    public final void getRawAttributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        ExifAttribute exifAttribute;
        parseTiffHeaders(seekableByteOrderedDataInputStream);
        readImageFileDirectory(seekableByteOrderedDataInputStream, 0);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 0);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 5);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 4);
        validateImages();
        if (this.mMimeType == 8 && (exifAttribute = this.mAttributes[1].get("MakerNote")) != null) {
            SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream2 = new SeekableByteOrderedDataInputStream(exifAttribute.bytes);
            seekableByteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
            seekableByteOrderedDataInputStream2.skipFully(6);
            readImageFileDirectory(seekableByteOrderedDataInputStream2, 9);
            ExifAttribute exifAttribute2 = this.mAttributes[9].get("ColorSpace");
            if (exifAttribute2 != null) {
                this.mAttributes[1].put("ColorSpace", exifAttribute2);
            }
        }
    }

    public final void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        boolean z = DEBUG;
        if (z) {
            Log.d("ExifInterface", "getRafAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.skipFully(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.read(bArr2);
        byteOrderedDataInputStream.read(bArr3);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        int i3 = ByteBuffer.wrap(bArr3).getInt();
        byte[] bArr4 = new byte[i2];
        byteOrderedDataInputStream.skipFully(i - byteOrderedDataInputStream.position());
        byteOrderedDataInputStream.read(bArr4);
        getJpegAttributes(new ByteOrderedDataInputStream(bArr4), i, 5);
        byteOrderedDataInputStream.skipFully(i3 - byteOrderedDataInputStream.position());
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        int readInt = byteOrderedDataInputStream.readInt();
        if (z) {
            Log.d("ExifInterface", "numberOfDirectoryEntry: " + readInt);
        }
        for (int i4 = 0; i4 < readInt; i4++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                short readShort = byteOrderedDataInputStream.readShort();
                short readShort2 = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort((int) readShort, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort((int) readShort2, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", createUShort);
                this.mAttributes[0].put("ImageWidth", createUShort2);
                if (DEBUG) {
                    Log.d("ExifInterface", "Updated to length: " + readShort + ", width: " + readShort2);
                    return;
                }
                return;
            }
            byteOrderedDataInputStream.skipFully(readUnsignedShort2);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:52|53|54) */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0134, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x013d, code lost:
        throw new java.lang.UnsupportedOperationException("Failed to read EXIF from HEIF file. Given stream is either malformed or unsupported.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x013e, code lost:
        r1.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0141, code lost:
        throw r12;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:52:0x0136 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getHeifAttributes(final androidx.exifinterface.media.ExifInterface.SeekableByteOrderedDataInputStream r13) throws java.io.IOException {
        /*
            r12 = this;
            java.lang.String r0 = "yes"
            android.media.MediaMetadataRetriever r1 = new android.media.MediaMetadataRetriever
            r1.<init>()
            androidx.exifinterface.media.ExifInterface$1 r2 = new androidx.exifinterface.media.ExifInterface$1     // Catch:{ RuntimeException -> 0x0136 }
            r2.<init>(r13)     // Catch:{ RuntimeException -> 0x0136 }
            androidx.exifinterface.media.ExifInterfaceUtils.Api23Impl.setDataSource(r1, r2)     // Catch:{ RuntimeException -> 0x0136 }
            r2 = 33
            java.lang.String r2 = r1.extractMetadata(r2)     // Catch:{ RuntimeException -> 0x0136 }
            r3 = 34
            java.lang.String r3 = r1.extractMetadata(r3)     // Catch:{ RuntimeException -> 0x0136 }
            r4 = 26
            java.lang.String r4 = r1.extractMetadata(r4)     // Catch:{ RuntimeException -> 0x0136 }
            r5 = 17
            java.lang.String r5 = r1.extractMetadata(r5)     // Catch:{ RuntimeException -> 0x0136 }
            boolean r4 = r0.equals(r4)     // Catch:{ RuntimeException -> 0x0136 }
            r6 = 0
            if (r4 == 0) goto L_0x0042
            r0 = 29
            java.lang.String r6 = r1.extractMetadata(r0)     // Catch:{ RuntimeException -> 0x0136 }
            r0 = 30
            java.lang.String r0 = r1.extractMetadata(r0)     // Catch:{ RuntimeException -> 0x0136 }
            r4 = 31
            java.lang.String r4 = r1.extractMetadata(r4)     // Catch:{ RuntimeException -> 0x0136 }
            goto L_0x005d
        L_0x0042:
            boolean r0 = r0.equals(r5)     // Catch:{ RuntimeException -> 0x0136 }
            if (r0 == 0) goto L_0x005b
            r0 = 18
            java.lang.String r6 = r1.extractMetadata(r0)     // Catch:{ RuntimeException -> 0x0136 }
            r0 = 19
            java.lang.String r0 = r1.extractMetadata(r0)     // Catch:{ RuntimeException -> 0x0136 }
            r4 = 24
            java.lang.String r4 = r1.extractMetadata(r4)     // Catch:{ RuntimeException -> 0x0136 }
            goto L_0x005d
        L_0x005b:
            r0 = r6
            r4 = r0
        L_0x005d:
            r5 = 0
            if (r6 == 0) goto L_0x0073
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r7 = r12.mAttributes     // Catch:{ RuntimeException -> 0x0136 }
            r7 = r7[r5]     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r8 = "ImageWidth"
            int r9 = java.lang.Integer.parseInt(r6)     // Catch:{ RuntimeException -> 0x0136 }
            java.nio.ByteOrder r10 = r12.mExifByteOrder     // Catch:{ RuntimeException -> 0x0136 }
            androidx.exifinterface.media.ExifInterface$ExifAttribute r9 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort((int) r9, (java.nio.ByteOrder) r10)     // Catch:{ RuntimeException -> 0x0136 }
            r7.put(r8, r9)     // Catch:{ RuntimeException -> 0x0136 }
        L_0x0073:
            if (r0 == 0) goto L_0x0088
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r7 = r12.mAttributes     // Catch:{ RuntimeException -> 0x0136 }
            r7 = r7[r5]     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r8 = "ImageLength"
            int r9 = java.lang.Integer.parseInt(r0)     // Catch:{ RuntimeException -> 0x0136 }
            java.nio.ByteOrder r10 = r12.mExifByteOrder     // Catch:{ RuntimeException -> 0x0136 }
            androidx.exifinterface.media.ExifInterface$ExifAttribute r9 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort((int) r9, (java.nio.ByteOrder) r10)     // Catch:{ RuntimeException -> 0x0136 }
            r7.put(r8, r9)     // Catch:{ RuntimeException -> 0x0136 }
        L_0x0088:
            r7 = 6
            if (r4 == 0) goto L_0x00b2
            r8 = 1
            int r9 = java.lang.Integer.parseInt(r4)     // Catch:{ RuntimeException -> 0x0136 }
            r10 = 90
            if (r9 == r10) goto L_0x00a2
            r10 = 180(0xb4, float:2.52E-43)
            if (r9 == r10) goto L_0x00a0
            r10 = 270(0x10e, float:3.78E-43)
            if (r9 == r10) goto L_0x009d
            goto L_0x00a3
        L_0x009d:
            r8 = 8
            goto L_0x00a3
        L_0x00a0:
            r8 = 3
            goto L_0x00a3
        L_0x00a2:
            r8 = r7
        L_0x00a3:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r9 = r12.mAttributes     // Catch:{ RuntimeException -> 0x0136 }
            r9 = r9[r5]     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r10 = "Orientation"
            java.nio.ByteOrder r11 = r12.mExifByteOrder     // Catch:{ RuntimeException -> 0x0136 }
            androidx.exifinterface.media.ExifInterface$ExifAttribute r8 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort((int) r8, (java.nio.ByteOrder) r11)     // Catch:{ RuntimeException -> 0x0136 }
            r9.put(r10, r8)     // Catch:{ RuntimeException -> 0x0136 }
        L_0x00b2:
            if (r2 == 0) goto L_0x0105
            if (r3 == 0) goto L_0x0105
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ RuntimeException -> 0x0136 }
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ RuntimeException -> 0x0136 }
            if (r3 <= r7) goto L_0x00fd
            long r8 = (long) r2     // Catch:{ RuntimeException -> 0x0136 }
            r13.seek(r8)     // Catch:{ RuntimeException -> 0x0136 }
            byte[] r8 = new byte[r7]     // Catch:{ RuntimeException -> 0x0136 }
            int r9 = r13.read(r8)     // Catch:{ RuntimeException -> 0x0136 }
            if (r9 != r7) goto L_0x00f5
            int r2 = r2 + r7
            int r3 = r3 + -6
            byte[] r7 = IDENTIFIER_EXIF_APP1     // Catch:{ RuntimeException -> 0x0136 }
            boolean r7 = java.util.Arrays.equals(r8, r7)     // Catch:{ RuntimeException -> 0x0136 }
            if (r7 == 0) goto L_0x00ed
            byte[] r7 = new byte[r3]     // Catch:{ RuntimeException -> 0x0136 }
            int r13 = r13.read(r7)     // Catch:{ RuntimeException -> 0x0136 }
            if (r13 != r3) goto L_0x00e5
            r12.mOffsetToExifData = r2     // Catch:{ RuntimeException -> 0x0136 }
            r12.readExifSegment(r7, r5)     // Catch:{ RuntimeException -> 0x0136 }
            goto L_0x0105
        L_0x00e5:
            java.io.IOException r12 = new java.io.IOException     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r13 = "Can't read exif"
            r12.<init>(r13)     // Catch:{ RuntimeException -> 0x0136 }
            throw r12     // Catch:{ RuntimeException -> 0x0136 }
        L_0x00ed:
            java.io.IOException r12 = new java.io.IOException     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r13 = "Invalid identifier"
            r12.<init>(r13)     // Catch:{ RuntimeException -> 0x0136 }
            throw r12     // Catch:{ RuntimeException -> 0x0136 }
        L_0x00f5:
            java.io.IOException r12 = new java.io.IOException     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r13 = "Can't read identifier"
            r12.<init>(r13)     // Catch:{ RuntimeException -> 0x0136 }
            throw r12     // Catch:{ RuntimeException -> 0x0136 }
        L_0x00fd:
            java.io.IOException r12 = new java.io.IOException     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r13 = "Invalid exif length"
            r12.<init>(r13)     // Catch:{ RuntimeException -> 0x0136 }
            throw r12     // Catch:{ RuntimeException -> 0x0136 }
        L_0x0105:
            boolean r12 = DEBUG     // Catch:{ RuntimeException -> 0x0136 }
            if (r12 == 0) goto L_0x0130
            java.lang.String r12 = "ExifInterface"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ RuntimeException -> 0x0136 }
            r13.<init>()     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r2 = "Heif meta: "
            r13.append(r2)     // Catch:{ RuntimeException -> 0x0136 }
            r13.append(r6)     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r2 = "x"
            r13.append(r2)     // Catch:{ RuntimeException -> 0x0136 }
            r13.append(r0)     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r0 = ", rotation "
            r13.append(r0)     // Catch:{ RuntimeException -> 0x0136 }
            r13.append(r4)     // Catch:{ RuntimeException -> 0x0136 }
            java.lang.String r13 = r13.toString()     // Catch:{ RuntimeException -> 0x0136 }
            android.util.Log.d(r12, r13)     // Catch:{ RuntimeException -> 0x0136 }
        L_0x0130:
            r1.release()
            return
        L_0x0134:
            r12 = move-exception
            goto L_0x013e
        L_0x0136:
            java.lang.UnsupportedOperationException r12 = new java.lang.UnsupportedOperationException     // Catch:{ all -> 0x0134 }
            java.lang.String r13 = "Failed to read EXIF from HEIF file. Given stream is either malformed or unsupported."
            r12.<init>(r13)     // Catch:{ all -> 0x0134 }
            throw r12     // Catch:{ all -> 0x0134 }
        L_0x013e:
            r1.release()
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getHeifAttributes(androidx.exifinterface.media.ExifInterface$SeekableByteOrderedDataInputStream):void");
    }

    public final void getStandaloneAttributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        byte[] bArr = IDENTIFIER_EXIF_APP1;
        seekableByteOrderedDataInputStream.skipFully(bArr.length);
        byte[] bArr2 = new byte[seekableByteOrderedDataInputStream.available()];
        seekableByteOrderedDataInputStream.readFully(bArr2);
        this.mOffsetToExifData = bArr.length;
        readExifSegment(bArr2, 0);
    }

    public final void getOrfAttributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        int i;
        int i2;
        getRawAttributes(seekableByteOrderedDataInputStream);
        ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
        if (exifAttribute != null) {
            SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream2 = new SeekableByteOrderedDataInputStream(exifAttribute.bytes);
            seekableByteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
            byte[] bArr = ORF_MAKER_NOTE_HEADER_1;
            byte[] bArr2 = new byte[bArr.length];
            seekableByteOrderedDataInputStream2.readFully(bArr2);
            seekableByteOrderedDataInputStream2.seek(0);
            byte[] bArr3 = ORF_MAKER_NOTE_HEADER_2;
            byte[] bArr4 = new byte[bArr3.length];
            seekableByteOrderedDataInputStream2.readFully(bArr4);
            if (Arrays.equals(bArr2, bArr)) {
                seekableByteOrderedDataInputStream2.seek(8);
            } else if (Arrays.equals(bArr4, bArr3)) {
                seekableByteOrderedDataInputStream2.seek(12);
            }
            readImageFileDirectory(seekableByteOrderedDataInputStream2, 6);
            ExifAttribute exifAttribute2 = this.mAttributes[7].get("PreviewImageStart");
            ExifAttribute exifAttribute3 = this.mAttributes[7].get("PreviewImageLength");
            if (!(exifAttribute2 == null || exifAttribute3 == null)) {
                this.mAttributes[5].put("JPEGInterchangeFormat", exifAttribute2);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", exifAttribute3);
            }
            ExifAttribute exifAttribute4 = this.mAttributes[8].get("AspectFrame");
            if (exifAttribute4 != null) {
                int[] iArr = (int[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 4) {
                    Log.w("ExifInterface", "Invalid aspect frame values. frame=" + Arrays.toString(iArr));
                    return;
                }
                int i3 = iArr[2];
                int i4 = iArr[0];
                if (i3 > i4 && (i = iArr[3]) > (i2 = iArr[1])) {
                    int i5 = (i3 - i4) + 1;
                    int i6 = (i - i2) + 1;
                    if (i5 < i6) {
                        int i7 = i5 + i6;
                        i6 = i7 - i6;
                        i5 = i7 - i6;
                    }
                    ExifAttribute createUShort = ExifAttribute.createUShort(i5, this.mExifByteOrder);
                    ExifAttribute createUShort2 = ExifAttribute.createUShort(i6, this.mExifByteOrder);
                    this.mAttributes[0].put("ImageWidth", createUShort);
                    this.mAttributes[0].put("ImageLength", createUShort2);
                }
            }
        }
    }

    public final void getRw2Attributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getRw2Attributes starting with: " + seekableByteOrderedDataInputStream);
        }
        getRawAttributes(seekableByteOrderedDataInputStream);
        ExifAttribute exifAttribute = this.mAttributes[0].get("JpgFromRaw");
        if (exifAttribute != null) {
            getJpegAttributes(new ByteOrderedDataInputStream(exifAttribute.bytes), (int) exifAttribute.bytesOffset, 5);
        }
        ExifAttribute exifAttribute2 = this.mAttributes[0].get("ISO");
        ExifAttribute exifAttribute3 = this.mAttributes[1].get("PhotographicSensitivity");
        if (exifAttribute2 != null && exifAttribute3 == null) {
            this.mAttributes[1].put("PhotographicSensitivity", exifAttribute2);
        }
    }

    public final void getPngAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getPngAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        byte[] bArr = PNG_SIGNATURE;
        byteOrderedDataInputStream.skipFully(bArr.length);
        int length = bArr.length + 0;
        while (true) {
            try {
                int readInt = byteOrderedDataInputStream.readInt();
                int i = length + 4;
                byte[] bArr2 = new byte[4];
                if (byteOrderedDataInputStream.read(bArr2) == 4) {
                    int i2 = i + 4;
                    if (i2 == 16) {
                        if (!Arrays.equals(bArr2, PNG_CHUNK_TYPE_IHDR)) {
                            throw new IOException("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");
                        }
                    }
                    if (!Arrays.equals(bArr2, PNG_CHUNK_TYPE_IEND)) {
                        if (Arrays.equals(bArr2, PNG_CHUNK_TYPE_EXIF)) {
                            byte[] bArr3 = new byte[readInt];
                            if (byteOrderedDataInputStream.read(bArr3) == readInt) {
                                int readInt2 = byteOrderedDataInputStream.readInt();
                                CRC32 crc32 = new CRC32();
                                crc32.update(bArr2);
                                crc32.update(bArr3);
                                if (((int) crc32.getValue()) == readInt2) {
                                    this.mOffsetToExifData = i2;
                                    readExifSegment(bArr3, 0);
                                    validateImages();
                                    setThumbnailData(new ByteOrderedDataInputStream(bArr3));
                                    return;
                                }
                                throw new IOException("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: " + readInt2 + ", calculated CRC value: " + crc32.getValue());
                            }
                            throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(bArr2));
                        }
                        int i3 = readInt + 4;
                        byteOrderedDataInputStream.skipFully(i3);
                        length = i2 + i3;
                    } else {
                        return;
                    }
                } else {
                    throw new IOException("Encountered invalid length while parsing PNG chunktype");
                }
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt PNG file.");
            }
        }
    }

    public final void getWebpAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getWebpAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        byteOrderedDataInputStream.skipFully(WEBP_SIGNATURE_1.length);
        int readInt = byteOrderedDataInputStream.readInt() + 8;
        byte[] bArr = WEBP_SIGNATURE_2;
        byteOrderedDataInputStream.skipFully(bArr.length);
        int length = bArr.length + 8;
        while (true) {
            try {
                byte[] bArr2 = new byte[4];
                if (byteOrderedDataInputStream.read(bArr2) == 4) {
                    int readInt2 = byteOrderedDataInputStream.readInt();
                    int i = length + 4 + 4;
                    if (Arrays.equals(WEBP_CHUNK_TYPE_EXIF, bArr2)) {
                        byte[] bArr3 = new byte[readInt2];
                        if (byteOrderedDataInputStream.read(bArr3) == readInt2) {
                            this.mOffsetToExifData = i;
                            readExifSegment(bArr3, 0);
                            setThumbnailData(new ByteOrderedDataInputStream(bArr3));
                            return;
                        }
                        throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(bArr2));
                    }
                    if (readInt2 % 2 == 1) {
                        readInt2++;
                    }
                    length = i + readInt2;
                    if (length != readInt) {
                        if (length <= readInt) {
                            byteOrderedDataInputStream.skipFully(readInt2);
                        } else {
                            throw new IOException("Encountered WebP file with invalid chunk size");
                        }
                    } else {
                        return;
                    }
                } else {
                    throw new IOException("Encountered invalid length while parsing WebP chunktype");
                }
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt WebP file.");
            }
        }
    }

    public final void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "saveJpegAttributes starting with (inputStream: " + inputStream + ", outputStream: " + outputStream + ")");
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(inputStream);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        if (byteOrderedDataInputStream.readByte() == -1) {
            byteOrderedDataOutputStream.writeByte(-1);
            if (byteOrderedDataInputStream.readByte() == -40) {
                byteOrderedDataOutputStream.writeByte(-40);
                ExifAttribute exifAttribute = null;
                if (getAttribute("Xmp") != null && this.mXmpIsFromSeparateMarker) {
                    exifAttribute = this.mAttributes[0].remove("Xmp");
                }
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(-31);
                writeExifSegment(byteOrderedDataOutputStream);
                if (exifAttribute != null) {
                    this.mAttributes[0].put("Xmp", exifAttribute);
                }
                byte[] bArr = new byte[4096];
                while (byteOrderedDataInputStream.readByte() == -1) {
                    byte readByte = byteOrderedDataInputStream.readByte();
                    if (readByte == -39 || readByte == -38) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream);
                        return;
                    } else if (readByte != -31) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
                        byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort);
                        int i = readUnsignedShort - 2;
                        if (i >= 0) {
                            while (i > 0) {
                                int read = byteOrderedDataInputStream.read(bArr, 0, Math.min(i, 4096));
                                if (read < 0) {
                                    break;
                                }
                                byteOrderedDataOutputStream.write(bArr, 0, read);
                                i -= read;
                            }
                        } else {
                            throw new IOException("Invalid length");
                        }
                    } else {
                        int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort() - 2;
                        if (readUnsignedShort2 >= 0) {
                            byte[] bArr2 = new byte[6];
                            if (readUnsignedShort2 >= 6) {
                                if (byteOrderedDataInputStream.read(bArr2) != 6) {
                                    throw new IOException("Invalid exif");
                                } else if (Arrays.equals(bArr2, IDENTIFIER_EXIF_APP1)) {
                                    byteOrderedDataInputStream.skipFully(readUnsignedShort2 - 6);
                                }
                            }
                            byteOrderedDataOutputStream.writeByte(-1);
                            byteOrderedDataOutputStream.writeByte(readByte);
                            byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort2 + 2);
                            if (readUnsignedShort2 >= 6) {
                                readUnsignedShort2 -= 6;
                                byteOrderedDataOutputStream.write(bArr2);
                            }
                            while (readUnsignedShort2 > 0) {
                                int read2 = byteOrderedDataInputStream.read(bArr, 0, Math.min(readUnsignedShort2, 4096));
                                if (read2 < 0) {
                                    break;
                                }
                                byteOrderedDataOutputStream.write(bArr, 0, read2);
                                readUnsignedShort2 -= read2;
                            }
                        } else {
                            throw new IOException("Invalid length");
                        }
                    }
                }
                throw new IOException("Invalid marker");
            }
            throw new IOException("Invalid marker");
        }
        throw new IOException("Invalid marker");
    }

    public final void savePngAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "savePngAttributes starting with (inputStream: " + inputStream + ", outputStream: " + outputStream + ")");
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(inputStream);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream, byteOrder);
        byte[] bArr = PNG_SIGNATURE;
        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, bArr.length);
        int i = this.mOffsetToExifData;
        if (i == 0) {
            int readInt = byteOrderedDataInputStream.readInt();
            byteOrderedDataOutputStream.writeInt(readInt);
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, readInt + 4 + 4);
        } else {
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, ((i - bArr.length) - 4) - 4);
            byteOrderedDataInputStream.skipFully(byteOrderedDataInputStream.readInt() + 4 + 4);
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = new ByteOrderedDataOutputStream(byteArrayOutputStream2, byteOrder);
                writeExifSegment(byteOrderedDataOutputStream2);
                byte[] byteArray = ((ByteArrayOutputStream) byteOrderedDataOutputStream2.mOutputStream).toByteArray();
                byteOrderedDataOutputStream.write(byteArray);
                CRC32 crc32 = new CRC32();
                crc32.update(byteArray, 4, byteArray.length - 4);
                byteOrderedDataOutputStream.writeInt((int) crc32.getValue());
                ExifInterfaceUtils.closeQuietly(byteArrayOutputStream2);
                ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream);
            } catch (Throwable th) {
                th = th;
                byteArrayOutputStream = byteArrayOutputStream2;
                ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
            throw th;
        }
    }

    public final void saveWebpAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        int i;
        int i2;
        int i3;
        int i4;
        InputStream inputStream2 = inputStream;
        OutputStream outputStream2 = outputStream;
        if (DEBUG) {
            Log.d("ExifInterface", "saveWebpAttributes starting with (inputStream: " + inputStream2 + ", outputStream: " + outputStream2 + ")");
        }
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(inputStream2, byteOrder);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream2, byteOrder);
        byte[] bArr = WEBP_SIGNATURE_1;
        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, bArr.length);
        byte[] bArr2 = WEBP_SIGNATURE_2;
        byteOrderedDataInputStream.skipFully(bArr2.length + 4);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = new ByteOrderedDataOutputStream(byteArrayOutputStream2, byteOrder);
                int i5 = this.mOffsetToExifData;
                if (i5 != 0) {
                    ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2, ((i5 - ((bArr.length + 4) + bArr2.length)) - 4) - 4);
                    byteOrderedDataInputStream.skipFully(4);
                    byteOrderedDataInputStream.skipFully(byteOrderedDataInputStream.readInt());
                    writeExifSegment(byteOrderedDataOutputStream2);
                } else {
                    byte[] bArr3 = new byte[4];
                    if (byteOrderedDataInputStream.read(bArr3) == 4) {
                        byte[] bArr4 = WEBP_CHUNK_TYPE_VP8X;
                        boolean z = false;
                        if (Arrays.equals(bArr3, bArr4)) {
                            int readInt = byteOrderedDataInputStream.readInt();
                            byte[] bArr5 = new byte[(readInt % 2 == 1 ? readInt + 1 : readInt)];
                            byteOrderedDataInputStream.read(bArr5);
                            byte b = (byte) (8 | bArr5[0]);
                            bArr5[0] = b;
                            if (((b >> 1) & 1) == 1) {
                                z = true;
                            }
                            byteOrderedDataOutputStream2.write(bArr4);
                            byteOrderedDataOutputStream2.writeInt(readInt);
                            byteOrderedDataOutputStream2.write(bArr5);
                            if (z) {
                                copyChunksUpToGivenChunkType(byteOrderedDataInputStream, byteOrderedDataOutputStream2, WEBP_CHUNK_TYPE_ANIM, (byte[]) null);
                                while (true) {
                                    byte[] bArr6 = new byte[4];
                                    inputStream2.read(bArr6);
                                    if (!Arrays.equals(bArr6, WEBP_CHUNK_TYPE_ANMF)) {
                                        break;
                                    }
                                    copyWebPChunk(byteOrderedDataInputStream, byteOrderedDataOutputStream2, bArr6);
                                }
                                writeExifSegment(byteOrderedDataOutputStream2);
                            } else {
                                copyChunksUpToGivenChunkType(byteOrderedDataInputStream, byteOrderedDataOutputStream2, WEBP_CHUNK_TYPE_VP8, WEBP_CHUNK_TYPE_VP8L);
                                writeExifSegment(byteOrderedDataOutputStream2);
                            }
                        } else {
                            byte[] bArr7 = WEBP_CHUNK_TYPE_VP8;
                            if (Arrays.equals(bArr3, bArr7) || Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                                int readInt2 = byteOrderedDataInputStream.readInt();
                                int i6 = readInt2 % 2 == 1 ? readInt2 + 1 : readInt2;
                                byte[] bArr8 = new byte[3];
                                if (Arrays.equals(bArr3, bArr7)) {
                                    byteOrderedDataInputStream.read(bArr8);
                                    byte[] bArr9 = new byte[3];
                                    if (byteOrderedDataInputStream.read(bArr9) != 3 || !Arrays.equals(WEBP_VP8_SIGNATURE, bArr9)) {
                                        throw new IOException("Encountered error while checking VP8 signature");
                                    }
                                    i4 = byteOrderedDataInputStream.readInt();
                                    i3 = (i4 << 18) >> 18;
                                    i2 = (i4 << 2) >> 18;
                                    i6 -= 10;
                                    i = 0;
                                } else if (!Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                                    i4 = 0;
                                    i3 = 0;
                                    i2 = 0;
                                    i = 0;
                                } else if (byteOrderedDataInputStream.readByte() == 47) {
                                    i4 = byteOrderedDataInputStream.readInt();
                                    i = i4 & 8;
                                    i6 -= 5;
                                    i2 = ((i4 << 4) >> 18) + 1;
                                    i3 = ((i4 << 18) >> 18) + 1;
                                } else {
                                    throw new IOException("Encountered error while checking VP8L signature");
                                }
                                byteOrderedDataOutputStream2.write(bArr4);
                                byteOrderedDataOutputStream2.writeInt(10);
                                byte[] bArr10 = new byte[10];
                                byte b2 = (byte) (bArr10[0] | 8);
                                bArr10[0] = b2;
                                bArr10[0] = (byte) (b2 | (i << 4));
                                int i7 = i3 - 1;
                                int i8 = i2 - 1;
                                bArr10[4] = (byte) i7;
                                bArr10[5] = (byte) (i7 >> 8);
                                bArr10[6] = (byte) (i7 >> 16);
                                bArr10[7] = (byte) i8;
                                bArr10[8] = (byte) (i8 >> 8);
                                bArr10[9] = (byte) (i8 >> 16);
                                byteOrderedDataOutputStream2.write(bArr10);
                                byteOrderedDataOutputStream2.write(bArr3);
                                byteOrderedDataOutputStream2.writeInt(readInt2);
                                if (Arrays.equals(bArr3, bArr7)) {
                                    byteOrderedDataOutputStream2.write(bArr8);
                                    byteOrderedDataOutputStream2.write(WEBP_VP8_SIGNATURE);
                                    byteOrderedDataOutputStream2.writeInt(i4);
                                } else if (Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                                    byteOrderedDataOutputStream2.write(47);
                                    byteOrderedDataOutputStream2.writeInt(i4);
                                }
                                ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2, i6);
                                writeExifSegment(byteOrderedDataOutputStream2);
                            }
                        }
                    } else {
                        throw new IOException("Encountered invalid length while parsing WebP chunk type");
                    }
                }
                ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2);
                int size = byteArrayOutputStream2.size();
                byte[] bArr11 = WEBP_SIGNATURE_2;
                byteOrderedDataOutputStream.writeInt(size + bArr11.length);
                byteOrderedDataOutputStream.write(bArr11);
                byteArrayOutputStream2.writeTo(byteOrderedDataOutputStream);
                ExifInterfaceUtils.closeQuietly(byteArrayOutputStream2);
            } catch (Exception e) {
                e = e;
                byteArrayOutputStream = byteArrayOutputStream2;
                try {
                    throw new IOException("Failed to save WebP file", e);
                } catch (Throwable th) {
                    th = th;
                    ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = byteArrayOutputStream2;
                ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            throw new IOException("Failed to save WebP file", e);
        }
    }

    public final void copyChunksUpToGivenChunkType(ByteOrderedDataInputStream byteOrderedDataInputStream, ByteOrderedDataOutputStream byteOrderedDataOutputStream, byte[] bArr, byte[] bArr2) throws IOException {
        String str;
        while (true) {
            byte[] bArr3 = new byte[4];
            if (byteOrderedDataInputStream.read(bArr3) != 4) {
                StringBuilder sb = new StringBuilder();
                sb.append("Encountered invalid length while copying WebP chunks up tochunk type ");
                Charset charset = ASCII;
                sb.append(new String(bArr, charset));
                if (bArr2 == null) {
                    str = "";
                } else {
                    str = " or " + new String(bArr2, charset);
                }
                sb.append(str);
                throw new IOException(sb.toString());
            }
            copyWebPChunk(byteOrderedDataInputStream, byteOrderedDataOutputStream, bArr3);
            if (Arrays.equals(bArr3, bArr)) {
                return;
            }
            if (bArr2 != null && Arrays.equals(bArr3, bArr2)) {
                return;
            }
        }
    }

    public final void copyWebPChunk(ByteOrderedDataInputStream byteOrderedDataInputStream, ByteOrderedDataOutputStream byteOrderedDataOutputStream, byte[] bArr) throws IOException {
        int readInt = byteOrderedDataInputStream.readInt();
        byteOrderedDataOutputStream.write(bArr);
        byteOrderedDataOutputStream.writeInt(readInt);
        if (readInt % 2 == 1) {
            readInt++;
        }
        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, readInt);
    }

    public final void readExifSegment(byte[] bArr, int i) throws IOException {
        SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream = new SeekableByteOrderedDataInputStream(bArr);
        parseTiffHeaders(seekableByteOrderedDataInputStream);
        readImageFileDirectory(seekableByteOrderedDataInputStream, i);
    }

    public final void addDefaultValuesForCompatibility() {
        String attribute = getAttribute("DateTimeOriginal");
        if (attribute != null && getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(attribute));
        }
        if (getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    public final ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == 18761) {
            if (DEBUG) {
                Log.d("ExifInterface", "readExifSegment: Byte Align II");
            }
            return ByteOrder.LITTLE_ENDIAN;
        } else if (readShort == 19789) {
            if (DEBUG) {
                Log.d("ExifInterface", "readExifSegment: Byte Align MM");
            }
            return ByteOrder.BIG_ENDIAN;
        } else {
            throw new IOException("Invalid byte order: " + Integer.toHexString(readShort));
        }
    }

    public final void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        ByteOrder readByteOrder = readByteOrder(byteOrderedDataInputStream);
        this.mExifByteOrder = readByteOrder;
        byteOrderedDataInputStream.setByteOrder(readByteOrder);
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i = this.mMimeType;
        if (i == 7 || i == 10 || readUnsignedShort == 42) {
            int readInt = byteOrderedDataInputStream.readInt();
            if (readInt >= 8) {
                int i2 = readInt - 8;
                if (i2 > 0) {
                    byteOrderedDataInputStream.skipFully(i2);
                    return;
                }
                return;
            }
            throw new IOException("Invalid first Ifd offset: " + readInt);
        }
        throw new IOException("Invalid start code: " + Integer.toHexString(readUnsignedShort));
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0228  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0246  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0282  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void readImageFileDirectory(androidx.exifinterface.media.ExifInterface.SeekableByteOrderedDataInputStream r30, int r31) throws java.io.IOException {
        /*
            r29 = this;
            r0 = r29
            r1 = r30
            r2 = r31
            java.util.Set<java.lang.Integer> r3 = r0.mAttributesOffsets
            int r4 = r1.mPosition
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3.add(r4)
            short r3 = r30.readShort()
            boolean r4 = DEBUG
            java.lang.String r5 = "ExifInterface"
            if (r4 == 0) goto L_0x002f
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "numberOfDirectoryEntry: "
            r4.append(r6)
            r4.append(r3)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r5, r4)
        L_0x002f:
            if (r3 > 0) goto L_0x0032
            return
        L_0x0032:
            r4 = 0
            r6 = r4
        L_0x0034:
            r7 = 5
            if (r6 >= r3) goto L_0x0322
            int r12 = r30.readUnsignedShort()
            int r13 = r30.readUnsignedShort()
            int r15 = r30.readInt()
            int r14 = r30.position()
            long r8 = (long) r14
            r18 = 4
            long r8 = r8 + r18
            java.util.HashMap<java.lang.Integer, androidx.exifinterface.media.ExifInterface$ExifTag>[] r14 = sExifTagMapsForReading
            r14 = r14[r2]
            java.lang.Integer r11 = java.lang.Integer.valueOf(r12)
            java.lang.Object r11 = r14.get(r11)
            androidx.exifinterface.media.ExifInterface$ExifTag r11 = (androidx.exifinterface.media.ExifInterface.ExifTag) r11
            boolean r14 = DEBUG
            r10 = 3
            if (r14 == 0) goto L_0x0090
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.Integer r23 = java.lang.Integer.valueOf(r31)
            r7[r4] = r23
            java.lang.Integer r23 = java.lang.Integer.valueOf(r12)
            r21 = 1
            r7[r21] = r23
            if (r11 == 0) goto L_0x0074
            java.lang.String r4 = r11.name
            goto L_0x0075
        L_0x0074:
            r4 = 0
        L_0x0075:
            r22 = 2
            r7[r22] = r4
            java.lang.Integer r4 = java.lang.Integer.valueOf(r13)
            r7[r10] = r4
            java.lang.Integer r4 = java.lang.Integer.valueOf(r15)
            r20 = 4
            r7[r20] = r4
            java.lang.String r4 = "ifdType: %d, tagNumber: %d, tagName: %s, dataFormat: %d, numberOfComponents: %d"
            java.lang.String r4 = java.lang.String.format(r4, r7)
            android.util.Log.d(r5, r4)
        L_0x0090:
            r4 = 7
            if (r11 != 0) goto L_0x00ae
            if (r14 == 0) goto L_0x00a9
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r10 = "Skip the tag entry since tag number is not defined: "
            r7.append(r10)
            r7.append(r12)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r5, r7)
        L_0x00a9:
            r10 = r5
            r25 = r6
            goto L_0x012d
        L_0x00ae:
            if (r13 <= 0) goto L_0x0114
            int[] r7 = IFD_FORMAT_BYTES_PER_FORMAT
            int r10 = r7.length
            if (r13 < r10) goto L_0x00b6
            goto L_0x0114
        L_0x00b6:
            boolean r10 = r11.isFormatCompatible(r13)
            if (r10 != 0) goto L_0x00e1
            if (r14 == 0) goto L_0x00a9
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r10 = "Skip the tag entry since data format ("
            r7.append(r10)
            java.lang.String[] r10 = IFD_FORMAT_NAMES
            r10 = r10[r13]
            r7.append(r10)
            java.lang.String r10 = ") is unexpected for tag: "
            r7.append(r10)
            java.lang.String r10 = r11.name
            r7.append(r10)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r5, r7)
            goto L_0x00a9
        L_0x00e1:
            if (r13 != r4) goto L_0x00e5
            int r13 = r11.primaryFormat
        L_0x00e5:
            r10 = r5
            long r4 = (long) r15
            r7 = r7[r13]
            r25 = r6
            long r6 = (long) r7
            long r4 = r4 * r6
            r6 = 0
            int r26 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r26 < 0) goto L_0x00fd
            r6 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x00fb
            goto L_0x00fd
        L_0x00fb:
            r6 = 1
            goto L_0x0130
        L_0x00fd:
            if (r14 == 0) goto L_0x012f
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Skip the tag entry since the number of components is invalid: "
            r6.append(r7)
            r6.append(r15)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r10, r6)
            goto L_0x012f
        L_0x0114:
            r10 = r5
            r25 = r6
            if (r14 == 0) goto L_0x012d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Skip the tag entry since data format is invalid: "
            r4.append(r5)
            r4.append(r13)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r10, r4)
        L_0x012d:
            r4 = 0
        L_0x012f:
            r6 = 0
        L_0x0130:
            if (r6 != 0) goto L_0x013a
            r1.seek(r8)
            r26 = r3
            r8 = r10
            goto L_0x0319
        L_0x013a:
            int r6 = (r4 > r18 ? 1 : (r4 == r18 ? 0 : -1))
            java.lang.String r7 = "Compression"
            if (r6 <= 0) goto L_0x01c4
            int r6 = r30.readInt()
            r26 = r3
            if (r14 == 0) goto L_0x015f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r27 = r8
            java.lang.String r8 = "seek to data offset: "
            r3.append(r8)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r10, r3)
            goto L_0x0161
        L_0x015f:
            r27 = r8
        L_0x0161:
            int r3 = r0.mMimeType
            r8 = 7
            if (r3 != r8) goto L_0x01bb
            java.lang.String r3 = r11.name
            java.lang.String r8 = "MakerNote"
            boolean r3 = r8.equals(r3)
            if (r3 == 0) goto L_0x0173
            r0.mOrfMakerNoteOffset = r6
            goto L_0x01bb
        L_0x0173:
            r3 = 6
            if (r2 != r3) goto L_0x01bb
            java.lang.String r8 = r11.name
            java.lang.String r9 = "ThumbnailImage"
            boolean r8 = r9.equals(r8)
            if (r8 == 0) goto L_0x01bb
            r0.mOrfThumbnailOffset = r6
            r0.mOrfThumbnailLength = r15
            java.nio.ByteOrder r8 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r3 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort((int) r3, (java.nio.ByteOrder) r8)
            int r8 = r0.mOrfThumbnailOffset
            long r8 = (long) r8
            r18 = r15
            java.nio.ByteOrder r15 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r8 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong((long) r8, (java.nio.ByteOrder) r15)
            int r9 = r0.mOrfThumbnailLength
            r24 = r10
            long r9 = (long) r9
            java.nio.ByteOrder r15 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r9 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong((long) r9, (java.nio.ByteOrder) r15)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r10 = r0.mAttributes
            r15 = 4
            r10 = r10[r15]
            r10.put(r7, r3)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r15]
            java.lang.String r10 = "JPEGInterchangeFormat"
            r3.put(r10, r8)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r15]
            java.lang.String r8 = "JPEGInterchangeFormatLength"
            r3.put(r8, r9)
            goto L_0x01bf
        L_0x01bb:
            r24 = r10
            r18 = r15
        L_0x01bf:
            long r8 = (long) r6
            r1.seek(r8)
            goto L_0x01cc
        L_0x01c4:
            r26 = r3
            r27 = r8
            r24 = r10
            r18 = r15
        L_0x01cc:
            java.util.HashMap<java.lang.Integer, java.lang.Integer> r3 = sExifPointerTagMap
            java.lang.Integer r6 = java.lang.Integer.valueOf(r12)
            java.lang.Object r3 = r3.get(r6)
            java.lang.Integer r3 = (java.lang.Integer) r3
            if (r14 == 0) goto L_0x01f9
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r8 = "nextIfdType: "
            r6.append(r8)
            r6.append(r3)
            java.lang.String r8 = " byteCount: "
            r6.append(r8)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            r8 = r24
            android.util.Log.d(r8, r6)
            goto L_0x01fb
        L_0x01f9:
            r8 = r24
        L_0x01fb:
            r6 = 8
            if (r3 == 0) goto L_0x029f
            r4 = -1
            r7 = 3
            if (r13 == r7) goto L_0x0221
            r7 = 4
            if (r13 == r7) goto L_0x021c
            if (r13 == r6) goto L_0x0217
            r6 = 9
            if (r13 == r6) goto L_0x0212
            r6 = 13
            if (r13 == r6) goto L_0x0212
            goto L_0x0226
        L_0x0212:
            int r4 = r30.readInt()
            goto L_0x0225
        L_0x0217:
            short r4 = r30.readShort()
            goto L_0x0225
        L_0x021c:
            long r4 = r30.readUnsignedInt()
            goto L_0x0226
        L_0x0221:
            int r4 = r30.readUnsignedShort()
        L_0x0225:
            long r4 = (long) r4
        L_0x0226:
            if (r14 == 0) goto L_0x0240
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.Long r7 = java.lang.Long.valueOf(r4)
            r9 = 0
            r6[r9] = r7
            java.lang.String r7 = r11.name
            r9 = 1
            r6[r9] = r7
            java.lang.String r7 = "Offset: %d, tagName: %s"
            java.lang.String r6 = java.lang.String.format(r7, r6)
            android.util.Log.d(r8, r6)
        L_0x0240:
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x0282
            java.util.Set<java.lang.Integer> r6 = r0.mAttributesOffsets
            int r7 = (int) r4
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            boolean r6 = r6.contains(r7)
            if (r6 != 0) goto L_0x025e
            r1.seek(r4)
            int r3 = r3.intValue()
            r0.readImageFileDirectory(r1, r3)
            goto L_0x0298
        L_0x025e:
            if (r14 == 0) goto L_0x0298
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Skip jump into the IFD since it has already been read: IfdType "
            r6.append(r7)
            r6.append(r3)
            java.lang.String r3 = " (at "
            r6.append(r3)
            r6.append(r4)
            java.lang.String r3 = ")"
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            android.util.Log.d(r8, r3)
            goto L_0x0298
        L_0x0282:
            if (r14 == 0) goto L_0x0298
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r6 = "Skip jump into the IFD since its offset is invalid: "
            r3.append(r6)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r8, r3)
        L_0x0298:
            r9 = r27
            r1.seek(r9)
            goto L_0x0319
        L_0x029f:
            r9 = r27
            int r3 = r30.position()
            int r12 = r0.mOffsetToExifData
            int r3 = r3 + r12
            int r4 = (int) r4
            byte[] r4 = new byte[r4]
            r1.readFully(r4)
            androidx.exifinterface.media.ExifInterface$ExifAttribute r5 = new androidx.exifinterface.media.ExifInterface$ExifAttribute
            long r14 = (long) r3
            r19 = r14
            r14 = r5
            r3 = r18
            r15 = r13
            r16 = r3
            r17 = r19
            r19 = r4
            r14.<init>(r15, r16, r17, r19)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r0.mAttributes
            r3 = r3[r2]
            java.lang.String r4 = r11.name
            r3.put(r4, r5)
            java.lang.String r3 = r11.name
            java.lang.String r4 = "DNGVersion"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x02d6
            r3 = 3
            r0.mMimeType = r3
        L_0x02d6:
            java.lang.String r3 = r11.name
            java.lang.String r4 = "Make"
            boolean r3 = r4.equals(r3)
            if (r3 != 0) goto L_0x02ea
            java.lang.String r3 = r11.name
            java.lang.String r4 = "Model"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x02f8
        L_0x02ea:
            java.nio.ByteOrder r3 = r0.mExifByteOrder
            java.lang.String r3 = r5.getStringValue(r3)
            java.lang.String r4 = "PENTAX"
            boolean r3 = r3.contains(r4)
            if (r3 != 0) goto L_0x030b
        L_0x02f8:
            java.lang.String r3 = r11.name
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x030d
            java.nio.ByteOrder r3 = r0.mExifByteOrder
            int r3 = r5.getIntValue(r3)
            r4 = 65535(0xffff, float:9.1834E-41)
            if (r3 != r4) goto L_0x030d
        L_0x030b:
            r0.mMimeType = r6
        L_0x030d:
            int r3 = r30.position()
            long r3 = (long) r3
            int r3 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1))
            if (r3 == 0) goto L_0x0319
            r1.seek(r9)
        L_0x0319:
            int r6 = r25 + 1
            short r6 = (short) r6
            r5 = r8
            r3 = r26
            r4 = 0
            goto L_0x0034
        L_0x0322:
            r8 = r5
            int r2 = r30.readInt()
            boolean r3 = DEBUG
            if (r3 == 0) goto L_0x033e
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
            r6 = 0
            r4[r6] = r5
            java.lang.String r5 = "nextIfdOffset: %d"
            java.lang.String r4 = java.lang.String.format(r5, r4)
            android.util.Log.d(r8, r4)
        L_0x033e:
            long r4 = (long) r2
            r9 = 0
            int r6 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
            if (r6 <= 0) goto L_0x0388
            java.util.Set<java.lang.Integer> r6 = r0.mAttributesOffsets
            java.lang.Integer r9 = java.lang.Integer.valueOf(r2)
            boolean r6 = r6.contains(r9)
            if (r6 != 0) goto L_0x0371
            r1.seek(r4)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r3 = 4
            r2 = r2[r3]
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x0363
            r0.readImageFileDirectory(r1, r3)
            goto L_0x039e
        L_0x0363:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r2 = r2[r7]
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x039e
            r0.readImageFileDirectory(r1, r7)
            goto L_0x039e
        L_0x0371:
            if (r3 == 0) goto L_0x039e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Stop reading file since re-reading an IFD may cause an infinite loop: "
            r0.append(r1)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r8, r0)
            goto L_0x039e
        L_0x0388:
            if (r3 == 0) goto L_0x039e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Stop reading file since a wrong offset may cause an infinite loop: "
            r0.append(r1)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r8, r0)
        L_0x039e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.readImageFileDirectory(androidx.exifinterface.media.ExifInterface$SeekableByteOrderedDataInputStream, int):void");
    }

    public final void retrieveJpegImageSize(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute = this.mAttributes[i].get("ImageLength");
        ExifAttribute exifAttribute2 = this.mAttributes[i].get("ImageWidth");
        if (exifAttribute == null || exifAttribute2 == null) {
            ExifAttribute exifAttribute3 = this.mAttributes[i].get("JPEGInterchangeFormat");
            ExifAttribute exifAttribute4 = this.mAttributes[i].get("JPEGInterchangeFormatLength");
            if (exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute3.getIntValue(this.mExifByteOrder);
                seekableByteOrderedDataInputStream.seek((long) intValue);
                byte[] bArr = new byte[intValue2];
                seekableByteOrderedDataInputStream.read(bArr);
                getJpegAttributes(new ByteOrderedDataInputStream(bArr), intValue, i);
            }
        }
    }

    public final void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        HashMap<String, ExifAttribute> hashMap = this.mAttributes[4];
        ExifAttribute exifAttribute = hashMap.get("Compression");
        if (exifAttribute != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            this.mThumbnailCompression = intValue;
            if (intValue != 1) {
                if (intValue == 6) {
                    handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                    return;
                } else if (intValue != 7) {
                    return;
                }
            }
            if (isSupportedDataType(hashMap)) {
                handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                return;
            }
            return;
        }
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
    }

    public final void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("JPEGInterchangeFormat");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (this.mMimeType == 7) {
                intValue += this.mOrfMakerNoteOffset;
            }
            if (intValue > 0 && intValue2 > 0) {
                this.mHasThumbnail = true;
                if (this.mFilename == null && this.mAssetInputStream == null && this.mSeekableFileDescriptor == null) {
                    byte[] bArr = new byte[intValue2];
                    byteOrderedDataInputStream.skip((long) intValue);
                    byteOrderedDataInputStream.read(bArr);
                    this.mThumbnailBytes = bArr;
                }
                this.mThumbnailOffset = intValue;
                this.mThumbnailLength = intValue2;
            }
            if (DEBUG) {
                Log.d("ExifInterface", "Setting thumbnail attributes with offset: " + intValue + ", length: " + intValue2);
            }
        }
    }

    public final void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream;
        HashMap hashMap2 = hashMap;
        ExifAttribute exifAttribute = (ExifAttribute) hashMap2.get("StripOffsets");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap2.get("StripByteCounts");
        if (exifAttribute != null && exifAttribute2 != null) {
            long[] convertToLongArray = ExifInterfaceUtils.convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            long[] convertToLongArray2 = ExifInterfaceUtils.convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            if (convertToLongArray == null || convertToLongArray.length == 0) {
                Log.w("ExifInterface", "stripOffsets should not be null or have zero length.");
            } else if (convertToLongArray2 == null || convertToLongArray2.length == 0) {
                Log.w("ExifInterface", "stripByteCounts should not be null or have zero length.");
            } else if (convertToLongArray.length != convertToLongArray2.length) {
                Log.w("ExifInterface", "stripOffsets and stripByteCounts should have same length.");
            } else {
                long j = 0;
                for (long j2 : convertToLongArray2) {
                    j += j2;
                }
                int i = (int) j;
                byte[] bArr = new byte[i];
                this.mAreThumbnailStripsConsecutive = true;
                this.mHasThumbnailStrips = true;
                this.mHasThumbnail = true;
                int i2 = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < convertToLongArray.length; i4++) {
                    int i5 = (int) convertToLongArray[i4];
                    int i6 = (int) convertToLongArray2[i4];
                    if (i4 < convertToLongArray.length - 1 && ((long) (i5 + i6)) != convertToLongArray[i4 + 1]) {
                        this.mAreThumbnailStripsConsecutive = false;
                    }
                    int i7 = i5 - i2;
                    if (i7 < 0) {
                        Log.d("ExifInterface", "Invalid strip offset value");
                        return;
                    }
                    long j3 = (long) i7;
                    if (byteOrderedDataInputStream2.skip(j3) != j3) {
                        Log.d("ExifInterface", "Failed to skip " + i7 + " bytes.");
                        return;
                    }
                    int i8 = i2 + i7;
                    byte[] bArr2 = new byte[i6];
                    if (byteOrderedDataInputStream2.read(bArr2) != i6) {
                        Log.d("ExifInterface", "Failed to read " + i6 + " bytes.");
                        return;
                    }
                    i2 = i8 + i6;
                    System.arraycopy(bArr2, 0, bArr, i3, i6);
                    i3 += i6;
                }
                this.mThumbnailBytes = bArr;
                if (this.mAreThumbnailStripsConsecutive) {
                    this.mThumbnailOffset = (int) convertToLongArray[0];
                    this.mThumbnailLength = i;
                }
            }
        }
    }

    public final boolean isSupportedDataType(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute;
        int intValue;
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("BitsPerSample");
        if (exifAttribute2 != null) {
            int[] iArr = (int[]) exifAttribute2.getValue(this.mExifByteOrder);
            int[] iArr2 = BITS_PER_SAMPLE_RGB;
            if (Arrays.equals(iArr2, iArr)) {
                return true;
            }
            if (this.mMimeType == 3 && (exifAttribute = (ExifAttribute) hashMap.get("PhotometricInterpretation")) != null && (((intValue = exifAttribute.getIntValue(this.mExifByteOrder)) == 1 && Arrays.equals(iArr, BITS_PER_SAMPLE_GREYSCALE_2)) || (intValue == 6 && Arrays.equals(iArr, iArr2)))) {
                return true;
            }
        }
        if (!DEBUG) {
            return false;
        }
        Log.d("ExifInterface", "Unsupported data type value");
        return false;
    }

    public final boolean isThumbnail(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("ImageLength");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("ImageWidth");
        if (exifAttribute == null || exifAttribute2 == null) {
            return false;
        }
        return exifAttribute.getIntValue(this.mExifByteOrder) <= 512 && exifAttribute2.getIntValue(this.mExifByteOrder) <= 512;
    }

    public final void validateImages() throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = this.mAttributes[1].get("PixelXDimension");
        ExifAttribute exifAttribute2 = this.mAttributes[1].get("PixelYDimension");
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            this.mAttributes[0].put("ImageWidth", exifAttribute);
            this.mAttributes[0].put("ImageLength", exifAttribute2);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap<>();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
        replaceInvalidTags(0, "ThumbnailOrientation", "Orientation");
        replaceInvalidTags(0, "ThumbnailImageLength", "ImageLength");
        replaceInvalidTags(0, "ThumbnailImageWidth", "ImageWidth");
        replaceInvalidTags(5, "ThumbnailOrientation", "Orientation");
        replaceInvalidTags(5, "ThumbnailImageLength", "ImageLength");
        replaceInvalidTags(5, "ThumbnailImageWidth", "ImageWidth");
        replaceInvalidTags(4, "Orientation", "ThumbnailOrientation");
        replaceInvalidTags(4, "ImageLength", "ThumbnailImageLength");
        replaceInvalidTags(4, "ImageWidth", "ThumbnailImageWidth");
    }

    public final void updateImageSizeValues(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3 = this.mAttributes[i].get("DefaultCropSize");
        ExifAttribute exifAttribute4 = this.mAttributes[i].get("SensorTopBorder");
        ExifAttribute exifAttribute5 = this.mAttributes[i].get("SensorLeftBorder");
        ExifAttribute exifAttribute6 = this.mAttributes[i].get("SensorBottomBorder");
        ExifAttribute exifAttribute7 = this.mAttributes[i].get("SensorRightBorder");
        if (exifAttribute3 != null) {
            if (exifAttribute3.format == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    Log.w("ExifInterface", "Invalid crop size values. cropSize=" + Arrays.toString(rationalArr));
                    return;
                }
                exifAttribute2 = ExifAttribute.createURational(rationalArr[0], this.mExifByteOrder);
                exifAttribute = ExifAttribute.createURational(rationalArr[1], this.mExifByteOrder);
            } else {
                int[] iArr = (int[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    Log.w("ExifInterface", "Invalid crop size values. cropSize=" + Arrays.toString(iArr));
                    return;
                }
                exifAttribute2 = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                exifAttribute = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
            }
            this.mAttributes[i].put("ImageWidth", exifAttribute2);
            this.mAttributes[i].put("ImageLength", exifAttribute);
        } else if (exifAttribute4 == null || exifAttribute5 == null || exifAttribute6 == null || exifAttribute7 == null) {
            retrieveJpegImageSize(seekableByteOrderedDataInputStream, i);
        } else {
            int intValue = exifAttribute4.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute6.getIntValue(this.mExifByteOrder);
            int intValue3 = exifAttribute7.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute5.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(intValue3 - intValue4, this.mExifByteOrder);
                this.mAttributes[i].put("ImageLength", createUShort);
                this.mAttributes[i].put("ImageWidth", createUShort2);
            }
        }
    }

    public final int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream) throws IOException {
        ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = byteOrderedDataOutputStream;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        int[] iArr = new int[exifTagArr.length];
        int[] iArr2 = new int[exifTagArr.length];
        for (ExifTag exifTag : EXIF_POINTER_TAGS) {
            removeAttribute(exifTag.name);
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                removeAttribute("StripOffsets");
                removeAttribute("StripByteCounts");
            } else {
                removeAttribute("JPEGInterchangeFormat");
                removeAttribute("JPEGInterchangeFormatLength");
            }
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            for (Object obj : this.mAttributes[i].entrySet().toArray()) {
                Map.Entry entry = (Map.Entry) obj;
                if (entry.getValue() == null) {
                    this.mAttributes[i].remove(entry.getKey());
                }
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                this.mAttributes[4].put("StripOffsets", ExifAttribute.createUShort(0, this.mExifByteOrder));
                this.mAttributes[4].put("StripByteCounts", ExifAttribute.createUShort(this.mThumbnailLength, this.mExifByteOrder));
            } else {
                this.mAttributes[4].put("JPEGInterchangeFormat", ExifAttribute.createULong(0, this.mExifByteOrder));
                this.mAttributes[4].put("JPEGInterchangeFormatLength", ExifAttribute.createULong((long) this.mThumbnailLength, this.mExifByteOrder));
            }
        }
        for (int i2 = 0; i2 < EXIF_TAGS.length; i2++) {
            int i3 = 0;
            for (Map.Entry<String, ExifAttribute> value : this.mAttributes[i2].entrySet()) {
                int size = ((ExifAttribute) value.getValue()).size();
                if (size > 4) {
                    i3 += size;
                }
            }
            iArr2[i2] = iArr2[i2] + i3;
        }
        int i4 = 8;
        for (int i5 = 0; i5 < EXIF_TAGS.length; i5++) {
            if (!this.mAttributes[i5].isEmpty()) {
                iArr[i5] = i4;
                i4 += (this.mAttributes[i5].size() * 12) + 2 + 4 + iArr2[i5];
            }
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                this.mAttributes[4].put("StripOffsets", ExifAttribute.createUShort(i4, this.mExifByteOrder));
            } else {
                this.mAttributes[4].put("JPEGInterchangeFormat", ExifAttribute.createULong((long) i4, this.mExifByteOrder));
            }
            this.mThumbnailOffset = i4;
            i4 += this.mThumbnailLength;
        }
        if (this.mMimeType == 4) {
            i4 += 8;
        }
        if (DEBUG) {
            for (int i6 = 0; i6 < EXIF_TAGS.length; i6++) {
                Log.d("ExifInterface", String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", new Object[]{Integer.valueOf(i6), Integer.valueOf(iArr[i6]), Integer.valueOf(this.mAttributes[i6].size()), Integer.valueOf(iArr2[i6]), Integer.valueOf(i4)}));
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong((long) iArr[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong((long) iArr[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong((long) iArr[3], this.mExifByteOrder));
        }
        int i7 = this.mMimeType;
        if (i7 == 4) {
            byteOrderedDataOutputStream2.writeUnsignedShort(i4);
            byteOrderedDataOutputStream2.write(IDENTIFIER_EXIF_APP1);
        } else if (i7 == 13) {
            byteOrderedDataOutputStream2.writeInt(i4);
            byteOrderedDataOutputStream2.write(PNG_CHUNK_TYPE_EXIF);
        } else if (i7 == 14) {
            byteOrderedDataOutputStream2.write(WEBP_CHUNK_TYPE_EXIF);
            byteOrderedDataOutputStream2.writeInt(i4);
        }
        byteOrderedDataOutputStream2.writeShort(this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? (short) 19789 : 18761);
        byteOrderedDataOutputStream2.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream2.writeUnsignedShort(42);
        byteOrderedDataOutputStream2.writeUnsignedInt(8);
        for (int i8 = 0; i8 < EXIF_TAGS.length; i8++) {
            if (!this.mAttributes[i8].isEmpty()) {
                byteOrderedDataOutputStream2.writeUnsignedShort(this.mAttributes[i8].size());
                int size2 = iArr[i8] + 2 + (this.mAttributes[i8].size() * 12) + 4;
                for (Map.Entry next : this.mAttributes[i8].entrySet()) {
                    int i9 = sExifTagMapsForWriting[i8].get(next.getKey()).number;
                    ExifAttribute exifAttribute = (ExifAttribute) next.getValue();
                    int size3 = exifAttribute.size();
                    byteOrderedDataOutputStream2.writeUnsignedShort(i9);
                    byteOrderedDataOutputStream2.writeUnsignedShort(exifAttribute.format);
                    byteOrderedDataOutputStream2.writeInt(exifAttribute.numberOfComponents);
                    if (size3 > 4) {
                        byteOrderedDataOutputStream2.writeUnsignedInt((long) size2);
                        size2 += size3;
                    } else {
                        byteOrderedDataOutputStream2.write(exifAttribute.bytes);
                        if (size3 < 4) {
                            while (size3 < 4) {
                                byteOrderedDataOutputStream2.writeByte(0);
                                size3++;
                            }
                        }
                    }
                }
                if (i8 != 0 || this.mAttributes[4].isEmpty()) {
                    byteOrderedDataOutputStream2.writeUnsignedInt(0);
                } else {
                    byteOrderedDataOutputStream2.writeUnsignedInt((long) iArr[4]);
                }
                for (Map.Entry<String, ExifAttribute> value2 : this.mAttributes[i8].entrySet()) {
                    byte[] bArr = ((ExifAttribute) value2.getValue()).bytes;
                    if (bArr.length > 4) {
                        byteOrderedDataOutputStream2.write(bArr, 0, bArr.length);
                    }
                }
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream2.write(getThumbnailBytes());
        }
        if (this.mMimeType == 14 && i4 % 2 == 1) {
            byteOrderedDataOutputStream2.writeByte(0);
        }
        byteOrderedDataOutputStream2.setByteOrder(ByteOrder.BIG_ENDIAN);
        return i4;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:68|69|70) */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        java.lang.Double.parseDouble(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x015c, code lost:
        return new android.util.Pair<>(12, -1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0162, code lost:
        return new android.util.Pair<>(2, -1);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:68:0x014e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> guessDataFormat(java.lang.String r12) {
        /*
            java.lang.String r0 = ","
            boolean r1 = r12.contains(r0)
            r2 = 0
            r3 = 1
            r4 = 2
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)
            r6 = -1
            java.lang.Integer r7 = java.lang.Integer.valueOf(r6)
            if (r1 == 0) goto L_0x00a6
            java.lang.String[] r12 = r12.split(r0, r6)
            r0 = r12[r2]
            android.util.Pair r0 = guessDataFormat(r0)
            java.lang.Object r1 = r0.first
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r1 != r4) goto L_0x0029
            return r0
        L_0x0029:
            int r1 = r12.length
            if (r3 >= r1) goto L_0x00a5
            r1 = r12[r3]
            android.util.Pair r1 = guessDataFormat(r1)
            java.lang.Object r2 = r1.first
            java.lang.Integer r2 = (java.lang.Integer) r2
            java.lang.Object r4 = r0.first
            boolean r2 = r2.equals(r4)
            if (r2 != 0) goto L_0x004d
            java.lang.Object r2 = r1.second
            java.lang.Integer r2 = (java.lang.Integer) r2
            java.lang.Object r4 = r0.first
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x004b
            goto L_0x004d
        L_0x004b:
            r2 = r6
            goto L_0x0055
        L_0x004d:
            java.lang.Object r2 = r0.first
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
        L_0x0055:
            java.lang.Object r4 = r0.second
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r4 == r6) goto L_0x0080
            java.lang.Object r4 = r1.first
            java.lang.Integer r4 = (java.lang.Integer) r4
            java.lang.Object r8 = r0.second
            boolean r4 = r4.equals(r8)
            if (r4 != 0) goto L_0x0077
            java.lang.Object r1 = r1.second
            java.lang.Integer r1 = (java.lang.Integer) r1
            java.lang.Object r4 = r0.second
            boolean r1 = r1.equals(r4)
            if (r1 == 0) goto L_0x0080
        L_0x0077:
            java.lang.Object r1 = r0.second
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            goto L_0x0081
        L_0x0080:
            r1 = r6
        L_0x0081:
            if (r2 != r6) goto L_0x008b
            if (r1 != r6) goto L_0x008b
            android.util.Pair r12 = new android.util.Pair
            r12.<init>(r5, r7)
            return r12
        L_0x008b:
            if (r2 != r6) goto L_0x0097
            android.util.Pair r0 = new android.util.Pair
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r0.<init>(r1, r7)
            goto L_0x00a2
        L_0x0097:
            if (r1 != r6) goto L_0x00a2
            android.util.Pair r0 = new android.util.Pair
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r0.<init>(r1, r7)
        L_0x00a2:
            int r3 = r3 + 1
            goto L_0x0029
        L_0x00a5:
            return r0
        L_0x00a6:
            java.lang.String r0 = "/"
            boolean r1 = r12.contains(r0)
            r8 = 0
            if (r1 == 0) goto L_0x0105
            java.lang.String[] r12 = r12.split(r0, r6)
            int r0 = r12.length
            if (r0 != r4) goto L_0x00ff
            r0 = r12[r2]     // Catch:{ NumberFormatException -> 0x00ff }
            double r0 = java.lang.Double.parseDouble(r0)     // Catch:{ NumberFormatException -> 0x00ff }
            long r0 = (long) r0     // Catch:{ NumberFormatException -> 0x00ff }
            r12 = r12[r3]     // Catch:{ NumberFormatException -> 0x00ff }
            double r2 = java.lang.Double.parseDouble(r12)     // Catch:{ NumberFormatException -> 0x00ff }
            long r2 = (long) r2     // Catch:{ NumberFormatException -> 0x00ff }
            int r12 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            r4 = 10
            if (r12 < 0) goto L_0x00f5
            int r12 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r12 >= 0) goto L_0x00d0
            goto L_0x00f5
        L_0x00d0:
            r8 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r12 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            r0 = 5
            if (r12 > 0) goto L_0x00eb
            int r12 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r12 <= 0) goto L_0x00dd
            goto L_0x00eb
        L_0x00dd:
            android.util.Pair r12 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x00ff }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x00ff }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ NumberFormatException -> 0x00ff }
            r12.<init>(r1, r0)     // Catch:{ NumberFormatException -> 0x00ff }
            return r12
        L_0x00eb:
            android.util.Pair r12 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x00ff }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ NumberFormatException -> 0x00ff }
            r12.<init>(r0, r7)     // Catch:{ NumberFormatException -> 0x00ff }
            return r12
        L_0x00f5:
            android.util.Pair r12 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x00ff }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x00ff }
            r12.<init>(r0, r7)     // Catch:{ NumberFormatException -> 0x00ff }
            return r12
        L_0x00ff:
            android.util.Pair r12 = new android.util.Pair
            r12.<init>(r5, r7)
            return r12
        L_0x0105:
            long r0 = java.lang.Long.parseLong(r12)     // Catch:{ NumberFormatException -> 0x014e }
            java.lang.Long r0 = java.lang.Long.valueOf(r0)     // Catch:{ NumberFormatException -> 0x014e }
            long r1 = r0.longValue()     // Catch:{ NumberFormatException -> 0x014e }
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            r2 = 4
            if (r1 < 0) goto L_0x0130
            long r3 = r0.longValue()     // Catch:{ NumberFormatException -> 0x014e }
            r10 = 65535(0xffff, double:3.23786E-319)
            int r1 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r1 > 0) goto L_0x0130
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x014e }
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ NumberFormatException -> 0x014e }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ NumberFormatException -> 0x014e }
            r0.<init>(r1, r2)     // Catch:{ NumberFormatException -> 0x014e }
            return r0
        L_0x0130:
            long r0 = r0.longValue()     // Catch:{ NumberFormatException -> 0x014e }
            int r0 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            if (r0 >= 0) goto L_0x0144
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x014e }
            r1 = 9
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ NumberFormatException -> 0x014e }
            r0.<init>(r1, r7)     // Catch:{ NumberFormatException -> 0x014e }
            return r0
        L_0x0144:
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x014e }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)     // Catch:{ NumberFormatException -> 0x014e }
            r0.<init>(r1, r7)     // Catch:{ NumberFormatException -> 0x014e }
            return r0
        L_0x014e:
            java.lang.Double.parseDouble(r12)     // Catch:{ NumberFormatException -> 0x015d }
            android.util.Pair r12 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x015d }
            r0 = 12
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ NumberFormatException -> 0x015d }
            r12.<init>(r0, r7)     // Catch:{ NumberFormatException -> 0x015d }
            return r12
        L_0x015d:
            android.util.Pair r12 = new android.util.Pair
            r12.<init>(r5, r7)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.guessDataFormat(java.lang.String):android.util.Pair");
    }

    public static class SeekableByteOrderedDataInputStream extends ByteOrderedDataInputStream {
        public SeekableByteOrderedDataInputStream(byte[] bArr) throws IOException {
            super(bArr);
            this.mDataInputStream.mark(Integer.MAX_VALUE);
        }

        public SeekableByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            super(inputStream);
            if (inputStream.markSupported()) {
                this.mDataInputStream.mark(Integer.MAX_VALUE);
                return;
            }
            throw new IllegalArgumentException("Cannot create SeekableByteOrderedDataInputStream with stream that does not support mark/reset");
        }

        public void seek(long j) throws IOException {
            int i = this.mPosition;
            if (((long) i) > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
            } else {
                j -= (long) i;
            }
            skipFully((int) j);
        }
    }

    public static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        public ByteOrder mByteOrder;
        public final DataInputStream mDataInputStream;
        public int mPosition;
        public byte[] mSkipBuffer;

        public ByteOrderedDataInputStream(byte[] bArr) throws IOException {
            this(new ByteArrayInputStream(bArr), ByteOrder.BIG_ENDIAN);
        }

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            this(inputStream, ByteOrder.BIG_ENDIAN);
        }

        public ByteOrderedDataInputStream(InputStream inputStream, ByteOrder byteOrder) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            this.mDataInputStream = dataInputStream;
            dataInputStream.mark(0);
            this.mPosition = 0;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public int position() {
            return this.mPosition;
        }

        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mPosition += i2;
            this.mDataInputStream.readFully(bArr, i, i2);
        }

        public void readFully(byte[] bArr) throws IOException {
            this.mPosition += bArr.length;
            this.mDataInputStream.readFully(bArr);
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            int read = this.mDataInputStream.read();
            if (read >= 0) {
                return (byte) read;
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            int i;
            this.mPosition += 2;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    i = (read2 << 8) + read;
                } else if (byteOrder == BIG_ENDIAN) {
                    i = (read << 8) + read2;
                } else {
                    throw new IOException("Invalid byte order: " + this.mByteOrder);
                }
                return (short) i;
            }
            throw new EOFException();
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            int read3 = this.mDataInputStream.read();
            int read4 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                }
                if (byteOrder == BIG_ENDIAN) {
                    return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
            throw new EOFException();
        }

        public int skipBytes(int i) throws IOException {
            throw new UnsupportedOperationException("skipBytes is currently unsupported");
        }

        public void skipFully(int i) throws IOException {
            int i2 = 0;
            while (i2 < i) {
                int i3 = i - i2;
                int skip = (int) this.mDataInputStream.skip((long) i3);
                if (skip <= 0) {
                    if (this.mSkipBuffer == null) {
                        this.mSkipBuffer = new byte[8192];
                    }
                    skip = this.mDataInputStream.read(this.mSkipBuffer, 0, Math.min(8192, i3));
                    if (skip == -1) {
                        throw new EOFException("Reached EOF while skipping " + i + " bytes.");
                    }
                }
                i2 += skip;
            }
            this.mPosition += i2;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (read2 << 8) + read;
                }
                if (byteOrder == BIG_ENDIAN) {
                    return (read << 8) + read2;
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
            throw new EOFException();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            int read3 = this.mDataInputStream.read();
            int read4 = this.mDataInputStream.read();
            int read5 = this.mDataInputStream.read();
            int read6 = this.mDataInputStream.read();
            int read7 = this.mDataInputStream.read();
            int read8 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (((long) read8) << 56) + (((long) read7) << 48) + (((long) read6) << 40) + (((long) read5) << 32) + (((long) read4) << 24) + (((long) read3) << 16) + (((long) read2) << 8) + ((long) read);
                }
                int i = read2;
                if (byteOrder == BIG_ENDIAN) {
                    return (((long) read) << 56) + (((long) i) << 48) + (((long) read3) << 40) + (((long) read4) << 32) + (((long) read5) << 24) + (((long) read6) << 16) + (((long) read7) << 8) + ((long) read8);
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
            throw new EOFException();
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        public void mark(int i) {
            throw new UnsupportedOperationException("Mark is currently unsupported");
        }

        public void reset() {
            throw new UnsupportedOperationException("Reset is currently unsupported");
        }
    }

    public static class ByteOrderedDataOutputStream extends FilterOutputStream {
        public ByteOrder mByteOrder;
        public final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void write(byte[] bArr) throws IOException {
            this.mOutputStream.write(bArr);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.mOutputStream.write(bArr, i, i2);
        }

        public void writeByte(int i) throws IOException {
            this.mOutputStream.write(i);
        }

        public void writeShort(short s) throws IOException {
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((s >>> 0) & 255);
                this.mOutputStream.write((s >>> 8) & 255);
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((s >>> 8) & 255);
                this.mOutputStream.write((s >>> 0) & 255);
            }
        }

        public void writeInt(int i) throws IOException {
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((i >>> 0) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 24) & 255);
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((i >>> 24) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 0) & 255);
            }
        }

        public void writeUnsignedShort(int i) throws IOException {
            writeShort((short) i);
        }

        public void writeUnsignedInt(long j) throws IOException {
            writeInt((int) j);
        }
    }

    public final void swapBasedOnImageSize(int i, int i2) throws IOException {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            ExifAttribute exifAttribute = this.mAttributes[i].get("ImageLength");
            ExifAttribute exifAttribute2 = this.mAttributes[i].get("ImageWidth");
            ExifAttribute exifAttribute3 = this.mAttributes[i2].get("ImageLength");
            ExifAttribute exifAttribute4 = this.mAttributes[i2].get("ImageWidth");
            if (exifAttribute == null || exifAttribute2 == null) {
                if (DEBUG) {
                    Log.d("ExifInterface", "First image does not contain valid size information");
                }
            } else if (exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
                    HashMap<String, ExifAttribute> hashMap = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap;
                }
            } else if (DEBUG) {
                Log.d("ExifInterface", "Second image does not contain valid size information");
            }
        } else if (DEBUG) {
            Log.d("ExifInterface", "Cannot perform swap since only one image data exists");
        }
    }

    public final void replaceInvalidTags(int i, String str, String str2) {
        if (!this.mAttributes[i].isEmpty() && this.mAttributes[i].get(str) != null) {
            HashMap<String, ExifAttribute> hashMap = this.mAttributes[i];
            hashMap.put(str2, hashMap.get(str));
            this.mAttributes[i].remove(str);
        }
    }
}
