package ctl.dev.extremescanner2.camera;

import ctl.dev.extremescanner2.R;
import ctl.dev.extremescanner2.R.id;
import ctl.dev.extremescanner2.R.layout;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
/* Import ZBar Class files */

public class CameraActivity extends FragmentActivity{

	public static boolean isCameraView;
	public boolean isLocal= false;
	
	
	
	static {
		System.loadLibrary("iconv");
	}



	Button btn_inputcode;

	FragmentTransaction ft;
	
	private CameraPreview mPreview;

    TextView scanText;

    ImageScanner scanner;

    private boolean barcodeScanned = false;

    static {
        System.loadLibrary("iconv");
    } 

    @Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    	setContentView(R.layout.camera);
    	 /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        scanner.setConfig(Symbol.EAN8, Config.ENABLE, 1);
//        scanner.setConfig(0, Config.X_DENSITY, 3);
//        scanner.setConfig(0, Config.Y_DENSITY, 3);
        mPreview = new CameraPreview(this, previewCb);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
    }

	@Override
	public void onResume() {
		super.onResume();
		isCameraView = true;
		if (barcodeScanned) {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					barcodeScanned = false;
				}
			}, 1500);

		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {

			if (!barcodeScanned) {
				Camera.Parameters parameters = camera.getParameters();
				Size size = parameters.getPreviewSize();

				Image barcode = new Image(size.width, size.height, "Y800");
				barcode.setData(data);

				int result = scanner.scanImage(barcode);

				if (result != 0) {
					SymbolSet syms = scanner.getResults();

					for (Symbol sym : syms) {
//						if (sym.getType() == Symbol.I25) {
//						Log.e("sym.getData()", sym.getData()+"");
							barcodeScanned = true;
							Intent intent = new Intent();
							intent.putExtra("QRCode", sym.getData());
							setResult(12, intent);
							finish();
//						}
					}
				}
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK){
			setResult(Activity.RESULT_OK, data);
			finish();
		}
	};
}
