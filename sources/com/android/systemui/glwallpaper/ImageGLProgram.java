package com.android.systemui.glwallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ImageGLProgram {
    public static final String TAG = "ImageGLProgram";
    public Context mContext;
    public int mProgramHandle;

    public ImageGLProgram(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public final int loadShaderProgram(int i, int i2) {
        return getProgramHandle(getShaderHandle(35633, getShaderResource(i)), getShaderHandle(35632, getShaderResource(i2)));
    }

    public final String getShaderResource(int i) {
        BufferedReader bufferedReader;
        Resources resources = this.mContext.getResources();
        StringBuilder sb = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(i)));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            bufferedReader.close();
        } catch (Resources.NotFoundException | IOException e) {
            Log.d(TAG, "Can not read the shader source", e);
            sb = null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        if (sb == null) {
            return "";
        }
        return sb.toString();
        throw th;
    }

    public final int getShaderHandle(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader == 0) {
            String str2 = TAG;
            Log.d(str2, "Create shader failed, type=" + i);
            return 0;
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        return glCreateShader;
    }

    public final int getProgramHandle(int i, int i2) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram == 0) {
            Log.d(TAG, "Can not create OpenGL ES program");
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, i);
        GLES20.glAttachShader(glCreateProgram, i2);
        GLES20.glLinkProgram(glCreateProgram);
        return glCreateProgram;
    }

    public boolean useGLProgram(int i, int i2) {
        int loadShaderProgram = loadShaderProgram(i, i2);
        this.mProgramHandle = loadShaderProgram;
        GLES20.glUseProgram(loadShaderProgram);
        return true;
    }

    public int getAttributeHandle(String str) {
        return GLES20.glGetAttribLocation(this.mProgramHandle, str);
    }

    public int getUniformHandle(String str) {
        return GLES20.glGetUniformLocation(this.mProgramHandle, str);
    }
}
