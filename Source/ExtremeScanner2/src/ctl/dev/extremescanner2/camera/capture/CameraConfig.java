package ctl.dev.extremescanner2.camera.capture;
public class CameraConfig 
{
	public boolean isEnableFlash() {
		return EnableFlash;
	}
	public void setEnableFlash(boolean enableFlash) {
		EnableFlash = enableFlash;
	}
	public boolean isEnableFocus() {
		return EnableFocus;
	}
	public void setEnableFocus(boolean enableFocus) {
		EnableFocus = enableFocus;
	}
	public int getCameraId() {
		return CameraId;
	}
	public void setCameraId(int cameraId) {
		CameraId = cameraId;
	}
	private boolean EnableFlash;
	private boolean EnableFocus;
	private int CameraId;		
}