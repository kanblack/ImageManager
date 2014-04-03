package ctl.dev.extremescanner2.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mHolder;

	public SurfaceHolder getmHolder() {
		return mHolder;
	}

	public void setmHolder(SurfaceHolder mHolder) {
		this.mHolder = mHolder;
	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public PreviewCallback getPreviewCallback() {
		return previewCallback;
	}

	public void setPreviewCallback(PreviewCallback previewCallback) {
		this.previewCallback = previewCallback;
	}

	private Camera mCamera;
	private PreviewCallback previewCallback;
	// private AutoFocusCallback autoFocusCallback;

	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private Handler autoFocusHandler;

	private Runnable doAutoFocus = new Runnable() {
		public void run() {

			try {
				mCamera.autoFocus(autoFocusCB);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public CameraPreview(Context context, PreviewCallback previewCb) {
		super(context);
		previewCallback = previewCb;
		autoFocusHandler = new Handler();
		/*
		 * Set camera to continuous focus if supported, otherwise use software
		 * auto-focus. Only works for API level >=9.
		 */
		/*
		 * Camera.Parameters parameters = camera.getParameters(); for (String f
		 * : parameters.getSupportedFocusModes()) { if (f ==
		 * Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
		 * mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		 * autoFocusCallback = null; break; } }
		 */

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("DBG", "Error setting camera preview: " + e.getMessage());
		} catch (Exception e) {

		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Camera preview released in activity

		if (mCamera != null) {
			
			mCamera.setPreviewCallback(null);
			mCamera.autoFocus(null);
			mCamera.release();
			mCamera = null;
			autoFocusHandler.removeCallbacks(doAutoFocus);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		/*
		 * If your preview can change or rotate, take care of those events here.
		 * Make sure to stop the preview before resizing or reformatting it.
		 */
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		try {
			// Hard code camera surface rotation 90 degs to match Activity view
			// in portrait
			mCamera.setDisplayOrientation(0);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(autoFocusCB);

		} catch (Exception e) {
			Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}
	}
}
