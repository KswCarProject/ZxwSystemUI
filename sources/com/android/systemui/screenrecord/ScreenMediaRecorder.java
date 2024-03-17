package com.android.systemui.screenrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenMediaRecorder {
    public ScreenInternalAudioRecorder mAudio;
    public ScreenRecordingAudioSource mAudioSource;
    public Context mContext;
    public Surface mInputSurface;
    public MediaRecorder.OnInfoListener mListener;
    public MediaProjection mMediaProjection;
    public MediaRecorder mMediaRecorder;
    public ScreenRecordingMuxer mMuxer;
    public File mTempAudioFile;
    public File mTempVideoFile;
    public int mUser;
    public VirtualDisplay mVirtualDisplay;

    public ScreenMediaRecorder(Context context, int i, ScreenRecordingAudioSource screenRecordingAudioSource, MediaRecorder.OnInfoListener onInfoListener) {
        this.mContext = context;
        this.mUser = i;
        this.mListener = onInfoListener;
        this.mAudioSource = screenRecordingAudioSource;
    }

    public final void prepare() throws IOException, RemoteException, RuntimeException {
        boolean z = false;
        this.mMediaProjection = new MediaProjection(this.mContext, IMediaProjection.Stub.asInterface(IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection")).createProjection(this.mUser, this.mContext.getPackageName(), 0, false).asBinder()));
        File cacheDir = this.mContext.getCacheDir();
        cacheDir.mkdirs();
        this.mTempVideoFile = File.createTempFile("temp", ".mp4", cacheDir);
        MediaRecorder mediaRecorder = new MediaRecorder();
        this.mMediaRecorder = mediaRecorder;
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        ScreenRecordingAudioSource screenRecordingAudioSource2 = ScreenRecordingAudioSource.MIC;
        if (screenRecordingAudioSource == screenRecordingAudioSource2) {
            mediaRecorder.setAudioSource(0);
        }
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int[] supportedSize = getSupportedSize(displayMetrics.widthPixels, displayMetrics.heightPixels, (int) windowManager.getDefaultDisplay().getRefreshRate());
        int i = supportedSize[0];
        int i2 = supportedSize[1];
        int i3 = supportedSize[2];
        this.mMediaRecorder.setVideoEncoder(2);
        this.mMediaRecorder.setVideoEncodingProfileLevel(8, 256);
        this.mMediaRecorder.setVideoSize(i, i2);
        this.mMediaRecorder.setVideoFrameRate(i3);
        this.mMediaRecorder.setVideoEncodingBitRate((((i * i2) * i3) / 30) * 6);
        this.mMediaRecorder.setMaxDuration(3600000);
        this.mMediaRecorder.setMaxFileSize(5000000000L);
        if (this.mAudioSource == screenRecordingAudioSource2) {
            this.mMediaRecorder.setAudioEncoder(4);
            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioEncodingBitRate(196000);
            this.mMediaRecorder.setAudioSamplingRate(44100);
        }
        this.mMediaRecorder.setOutputFile(this.mTempVideoFile);
        this.mMediaRecorder.prepare();
        Surface surface = this.mMediaRecorder.getSurface();
        this.mInputSurface = surface;
        this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("Recording Display", i, i2, displayMetrics.densityDpi, 16, surface, (VirtualDisplay.Callback) null, (Handler) null);
        this.mMediaRecorder.setOnInfoListener(this.mListener);
        ScreenRecordingAudioSource screenRecordingAudioSource3 = this.mAudioSource;
        if (screenRecordingAudioSource3 == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource3 == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mTempAudioFile = File.createTempFile("temp", ".aac", this.mContext.getCacheDir());
            String absolutePath = this.mTempAudioFile.getAbsolutePath();
            MediaProjection mediaProjection = this.mMediaProjection;
            if (this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                z = true;
            }
            this.mAudio = new ScreenInternalAudioRecorder(absolutePath, mediaProjection, z);
        }
    }

    public final int[] getSupportedSize(int i, int i2, int i3) throws IOException {
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType("video/avc");
        MediaCodecInfo.VideoCapabilities videoCapabilities = createDecoderByType.getCodecInfo().getCapabilitiesForType("video/avc").getVideoCapabilities();
        createDecoderByType.release();
        int intValue = videoCapabilities.getSupportedWidths().getUpper().intValue();
        int intValue2 = videoCapabilities.getSupportedHeights().getUpper().intValue();
        int widthAlignment = i % videoCapabilities.getWidthAlignment() != 0 ? i - (i % videoCapabilities.getWidthAlignment()) : i;
        int heightAlignment = i2 % videoCapabilities.getHeightAlignment() != 0 ? i2 - (i2 % videoCapabilities.getHeightAlignment()) : i2;
        if (intValue < widthAlignment || intValue2 < heightAlignment || !videoCapabilities.isSizeSupported(widthAlignment, heightAlignment)) {
            double d = (double) i;
            double d2 = (double) i2;
            double min = Math.min(((double) intValue) / d, ((double) intValue2) / d2);
            int i4 = (int) (d * min);
            int i5 = (int) (d2 * min);
            if (i4 % videoCapabilities.getWidthAlignment() != 0) {
                i4 -= i4 % videoCapabilities.getWidthAlignment();
            }
            if (i5 % videoCapabilities.getHeightAlignment() != 0) {
                i5 -= i5 % videoCapabilities.getHeightAlignment();
            }
            int intValue3 = videoCapabilities.getSupportedFrameRatesFor(i4, i5).getUpper().intValue();
            if (intValue3 < i3) {
                i3 = intValue3;
            }
            Log.d("ScreenMediaRecorder", "Resized by " + min + ": " + i4 + ", " + i5 + ", " + i3);
            return new int[]{i4, i5, i3};
        }
        int intValue4 = videoCapabilities.getSupportedFrameRatesFor(widthAlignment, heightAlignment).getUpper().intValue();
        if (intValue4 < i3) {
            i3 = intValue4;
        }
        Log.d("ScreenMediaRecorder", "Screen size supported at rate " + i3);
        return new int[]{widthAlignment, heightAlignment, i3};
    }

    public void start() throws IOException, RemoteException, RuntimeException {
        Log.d("ScreenMediaRecorder", "start recording");
        prepare();
        this.mMediaRecorder.start();
        recordInternalAudio();
    }

    public void end() {
        this.mMediaRecorder.stop();
        this.mMediaRecorder.release();
        this.mInputSurface.release();
        this.mVirtualDisplay.release();
        this.mMediaProjection.stop();
        this.mMediaRecorder = null;
        this.mMediaProjection = null;
        stopInternalAudioRecording();
        Log.d("ScreenMediaRecorder", "end recording");
    }

    public final void stopInternalAudioRecording() {
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.end();
            this.mAudio = null;
        }
    }

    public final void recordInternalAudio() throws IllegalStateException {
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.start();
        }
    }

    public SavedRecording save() throws IOException {
        String format = new SimpleDateFormat("'screen-'yyyyMMdd-HHmmss'.mp4'").format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", format);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Video.Media.getContentUri("external_primary"), contentValues);
        Log.d("ScreenMediaRecorder", insert.toString());
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL) {
            try {
                Log.d("ScreenMediaRecorder", "muxing recording");
                File createTempFile = File.createTempFile("temp", ".mp4", this.mContext.getCacheDir());
                ScreenRecordingMuxer screenRecordingMuxer = new ScreenRecordingMuxer(0, createTempFile.getAbsolutePath(), this.mTempVideoFile.getAbsolutePath(), this.mTempAudioFile.getAbsolutePath());
                this.mMuxer = screenRecordingMuxer;
                screenRecordingMuxer.mux();
                this.mTempVideoFile.delete();
                this.mTempVideoFile = createTempFile;
            } catch (IOException e) {
                Log.e("ScreenMediaRecorder", "muxing recording " + e.getMessage());
                e.printStackTrace();
            }
        }
        OutputStream openOutputStream = contentResolver.openOutputStream(insert, "w");
        Files.copy(this.mTempVideoFile.toPath(), openOutputStream);
        openOutputStream.close();
        File file = this.mTempAudioFile;
        if (file != null) {
            file.delete();
        }
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        SavedRecording savedRecording = new SavedRecording(insert, this.mTempVideoFile, new Size(displayMetrics.widthPixels, displayMetrics.heightPixels));
        this.mTempVideoFile.delete();
        return savedRecording;
    }

    public class SavedRecording {
        public Bitmap mThumbnailBitmap;
        public Uri mUri;

        public SavedRecording(Uri uri, File file, Size size) {
            this.mUri = uri;
            try {
                this.mThumbnailBitmap = ThumbnailUtils.createVideoThumbnail(file, size, (CancellationSignal) null);
            } catch (IOException e) {
                Log.e("ScreenMediaRecorder", "Error creating thumbnail", e);
            }
        }

        public Uri getUri() {
            return this.mUri;
        }

        public Bitmap getThumbnail() {
            return this.mThumbnailBitmap;
        }
    }
}
