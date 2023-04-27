package com.example.cpu_zproject;

import static android.content.ContentValues.TAG;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.camera2.CaptureRequest;
import android.opengl.GLES31;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.util.List;

public class DeviceInfo {

    private Context context;
    CaptureRequest.Builder builder;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getModelName() {
        return Build.MODEL;
    }

    public String getModelNumber() {
        return Build.DISPLAY;
    }

    public long getTotalRAM(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            return mi.totalMem/1000000000;
        } else {
            return -1;
        }
    }

    public long getAvailableStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize)/1000000000;
    }

    public int getBatteryLevel() {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int) ((float) level / (float) scale * 100.0f);
    }

    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getProcessorInfo() {
        return Build.HARDWARE;
    }

    public String getGPUInfo() {
        String renderer = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            renderer = GLES31.glGetString(GLES31.GL_RENDERER);
        } else {
            renderer = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getName();
        }
        return renderer;
    }

    public float getCameraMegaPixels() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        float megaPixels = 0f;
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera camera = Camera.open(i);
                Camera.Parameters params = camera.getParameters();
                Camera.Size size = params.getPictureSize();
                megaPixels = (float) (size.width * size.height) / 1000000;
                camera.release();
            }
        }
        return megaPixels;
    }

    String camera(int val) {
        Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        int maxWidthIndex = 0;
        for (
                int i = 1; i < supportedSizes.size(); i++) {
            if (supportedSizes.get(i).width > supportedSizes.get(maxWidthIndex).width) {
                maxWidthIndex = i;
            }
        }

        int megapixels = (int) (supportedSizes.get(maxWidthIndex).width * supportedSizes.get(maxWidthIndex).height / 1000000);
        float aperture = params.getFocalLength() / Float.parseFloat(params.get("horizontal-view-angle"));
        camera.release();
        if (val == 1)
            return Integer.toString(megapixels);
        else
            return Float.toString(aperture);
    }

    public void capture() {
        Log.d(TAG, "capture");
        builder.build();
        Log.d(TAG, "APERTURE: " + builder.get(CaptureRequest.LENS_APERTURE));
        Log.d(TAG, "ISO: " + builder.get(CaptureRequest.SENSOR_SENSITIVITY));
        Log.d(TAG, "EXPOSURE: " + builder.get(CaptureRequest.SENSOR_EXPOSURE_TIME));
    }

    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}

// live sensor reading
