package ctl.dev.extremescanner2.camera.capture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.extremescanner2.R;
import ctl.dev.extremescanner2.camera.util.CameraUtil;

public class CameraActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	public static Camera getCamera() {
		return mCamera;
	}

	public void setCamera(Camera mCamera) {
		CameraActivity.mCamera = mCamera;
	}

	private static Camera mCamera;

	ImageView btn_capture;
	ToggleButton tbtn_flash;
	private static String tag = "CameraActivity";

	private Context context;
	public static final int MEDIA_TYPE_IMAGE = 1;
	CameraConfig config;
	CameraPreview mPreview;
	FrameLayout frameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		context = this;
		config = new CameraConfig();
		frameLayout = (FrameLayout) findViewById(R.id.preview_fl_layout);
		mPreview = new CameraPreview(config);
		frameLayout.addView(mPreview);
		
		btn_capture = (ImageView) findViewById(R.id.preview_btn_capture);

		btn_capture.setOnClickListener(this);

		tbtn_flash = (ToggleButton) findViewById(R.id.preview_toggle_flash);
		tbtn_flash.setOnCheckedChangeListener(this);
		Log.e("Done", "CREATE");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK)
		{
			setResult(RESULT_OK, data);
			finish();
		}
		if(resultCode == RESULT_CANCELED)
		{
			
		}
	}

	/** Handle file after capture picture */
	private PictureCallback mPictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.e(tag, "take picture complete");
			try {
				File pictureFile = CameraUtil.getOutoutMediaFile(context,
						CameraUtil.PUBLIC_STORAGE);
				if (pictureFile == null) {
					new EasyMessageBox("Error creating media file", "Error",
							context).show();
					return;
				}

				try {

					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					startActivityForResult(new Intent(context, ImagePreviewActivity.class)
							.putExtra("path", pictureFile.getAbsolutePath()),0);
					
					Log.e(tag, "file saved at " + pictureFile.getAbsolutePath()
							+ "");
//				Toast.makeText(context,
//						"file saved at " + pictureFile.getPath(),
//						Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					Log.e(tag, e.getMessage() + "");
					new EasyMessageBox(e.getLocalizedMessage(),
							"FileNotFoundException", context).show();
					e.printStackTrace();
				} catch (IOException e) {

					new EasyMessageBox(e.getLocalizedMessage(), "IOException",
							context).show();
					Log.e(tag, e.getMessage() + "");
					
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_capture) {
			try {
				try {
					getCamera().takePicture(null, null, mPictureCallback);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	public class CameraPreview extends SurfaceView implements Callback {
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
		
		AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				try {
					autoFocusHandler.postDelayed(doAutoFocus, 1000);
					Log.e("Go here", "FOCUS");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		private Handler autoFocusHandler;

		
		private CameraConfig config;

		public CameraConfig getConfig() {
			return config;
		}

		public void setConfig(CameraConfig config) {
			this.config = config;
		}

		private SurfaceHolder mHolder;

		private List<Size> mSupportedPreviewSizes;

		private List<Camera.Size> mSupportedPictureSizes;

		
		int TYPE_ORIENTATION_LANSCAPE = 1;
		int TYPE_ORIENTATION_POTRAIT = 2;

		public Size getPreviewSize() {
			return mPreviewSize;
		}

		public void setPreviewSize(Size mPreviewSize) {
			this.mPreviewSize = mPreviewSize;
		}

		private Size mPreviewSize,mPictureSize;
		

		@SuppressWarnings("deprecation")
		public CameraPreview(CameraConfig config) {
			super(CameraActivity.this);
			autoFocusHandler = new Handler();
			
			
			// get list preview size support
			try {
				mCamera = CameraUtil.getCameraInstance(getContext());
				List<Size> localSizes = mCamera.getParameters()
						.getSupportedPreviewSizes();
				
				List<Size> localPicSizes = mCamera.getParameters()
						.getSupportedPictureSizes();
				
				mSupportedPictureSizes = localPicSizes;
				mSupportedPreviewSizes = localSizes;

				// set up a SurfaceHolder.Callback to control surface
				mHolder = getHolder();
				mHolder.addCallback(this);
				// deprecated but required on Android version prior 3.0
				 mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.config = config;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated catch block
			if (mCamera == null) {
				mCamera = CameraUtil.getCameraInstance(getContext());
			}
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			try {
				releaseCamera();
				autoFocusHandler.removeCallbacks(doAutoFocus);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void releaseCamera() {
			try {
				mCamera.release();
				mCamera = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		@SuppressLint("NewApi")
		@SuppressLint("NewApi")
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// is used when preview change or rotate.

			if (mHolder.getSurface() == null) {
				new EasyMessageBox("surface null", "mHolder.getSurface()",
						getContext()).show();
				return;
			}

			// Stop preview before resizing or rotating
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();

				new EasyMessageBox(e.getLocalizedMessage(),
						"mCamera.stopPreview()", getContext()).show();
			}

			// make the change
//			setCameraDisplayOrientation();

			//**set preview size*/
			Parameters parameters = mCamera.getParameters();
			mPreviewSize = getBestPreviewSize((width*3)/4,width);
			Log.e("Size", mPreviewSize.width + "x" + mPreviewSize.height);
//			btn_size.setText(mPreviewSize.height + "x" + mPreviewSize.width);
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

			

			
			
			
			/**set image size*/
			mPictureSize = getBestPicureSize(640, 480);
			parameters.setPictureSize(mPictureSize.width,mPictureSize.height);
			
			
			
//			parameters.setRotation(90); // set rotation to save the picture

//			Log.e("config.isEnableFlash()", config.isEnableFlash() + "");
//			Log.e("config.isEnableFocus()", config.isEnableFocus() + "");
//
			if (config.isEnableFlash()) {
				parameters.setFlashMode(Parameters.FLASH_MODE_ON);
			} else {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);

			}

			
			mCamera.setParameters(parameters);
			// restart preview with new setting
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
				/**set focus auto*/
				mCamera.autoFocus(autoFocusCB);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		public int setCameraDisplayOrientation() {
			int type = -1;
			android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
			android.hardware.Camera.getCameraInfo(0, info);
			int rotation = ((Activity) getContext()).getWindowManager()
					.getDefaultDisplay().getRotation();
			int degrees = 0;
			switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				type = TYPE_ORIENTATION_LANSCAPE;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				type = TYPE_ORIENTATION_POTRAIT;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				type = TYPE_ORIENTATION_LANSCAPE;
				break;
			case Surface.ROTATION_270:
				type = TYPE_ORIENTATION_POTRAIT;
				break;
			}

			int result;
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % 360;
				result = (360 - result) % 360; // compensate the mirror
			} else { // back-facing
				result = (info.orientation - degrees + 360) % 360;
			}
			Log.e("camera orientation", result + "");
			mCamera.setDisplayOrientation(result);

			return type;
		}
		
		private Camera.Size getBestPicureSize(int width, int height)
		{
		        Camera.Size result=null;    
//		        Camera.Parameters p = camera.getParameters();
		        for (Camera.Size size : mSupportedPictureSizes) {
		            if (size.width<=width && size.height<=height) {
		                if (result==null) {
		                    result=size;
		                } else {
		                    int resultArea=result.width*result.height;
		                    int newArea=size.width*size.height;

		                    if (newArea>resultArea) {
		                        result=size;
		                    }
		                }
		            }
		        }
		    return result;

		}
		
		private Camera.Size getBestPreviewSize(int width, int height)
		{
		        Camera.Size result=null;    
//		        Camera.Parameters p = camera.getParameters();
		        for (Camera.Size size : mSupportedPreviewSizes) {
		            if (size.width<=width && size.height<=height) {
		                if (result==null) {
		                    result=size;
		                } else {
		                    int resultArea=result.width*result.height;
		                    int newArea=size.width*size.height;

		                    if (newArea>resultArea) {
		                        result=size;
		                    }
		                }
		            }
		        }
		    return result;

		}
		

	}

	private void setSurfaceSize(float videoWidth, float videoHeight) {

        // // Get the dimensions of the video
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        FrameLayout.LayoutParams lp = (LayoutParams) mPreview.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
        mPreview.setLayoutParams(lp);
    }
	
	Parameters parameters;

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {

		parameters = mCamera.getParameters();
		if (v == tbtn_flash) {
//			config.setEnableFlash(isChecked);
//
//			if (config.isEnableFlash()) {
//				parameters.setFlashMode(Parameters.FLASH_MODE_ON);
//			} else {
//				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
//
//			}
		}
		
		mCamera.setParameters(parameters);
	}

}
