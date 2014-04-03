package ctl.dev.extremescanner2.camera.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ctl.dev.api.EasyMessageBox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CameraUtil {
	public final static int PUBLIC_STORAGE = 1;
	public final static int PRIVATE_STORAGE = 2;
	public final static String FILE_NAME = "ExtrameScanner";

	/** Check if device has a camera */
	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// has camera
			return true;
		} else {

			return false;
		}
	}



	/** Get an safe instance of the Camera object */
	public static Camera getCameraInstance(final Context context) {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
			new EasyMessageBox("Camera is not avaiable (in use or not exit)",
					"Error", context)
					.show(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							((Activity) context).finish();
						}
					});
		}

		return camera;
	}

    /** Get an safe instance of the Camera object */
    public static Camera getCameraInstance(final Context context,int cameraID) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraID);
        } catch (Exception e) {
            e.printStackTrace();
            new EasyMessageBox("Camera is not avaiable (in use or not exit)",
                    "Error", context)
                    .show(new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) context).finish();
                        }
                    });
        }

        return camera;
    }

    /**Release camera
     * @param context*/
    public static boolean releaseCamera(Context context)
    {
        if (getCameraInstance(context) != null)
        {
            getCameraInstance(context).release();
        }
        return  false;
    }

	/** Saving File */

	@SuppressLint("SimpleDateFormat")
	public static File getOutoutMediaFile(Context context, int type) {
		// Check SDCard is mounted
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED)) {
			new EasyMessageBox("SDCard is " + Environment.MEDIA_UNMOUNTED,
					"SDCard error", context);
			return null;
		}

		File mediaStorageDir = null;
		if (type == PUBLIC_STORAGE) {
			mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					FILE_NAME);
		} else if (type == PRIVATE_STORAGE) {
			mediaStorageDir = new File(
					Environment.getExternalStorageDirectory(), FILE_NAME);
		} else {
			new EasyMessageBox("The directory is not recommend", null, context)
					.show();
			return null;
		}

		if (mediaStorageDir != null && !mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				new EasyMessageBox("Failed to create directory", null, context)
						.show();

				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		File mediaFile = null;
		try {
			mediaFile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
//			Log.e("mediaFile", mediaFile.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mediaFile;
	}
}
