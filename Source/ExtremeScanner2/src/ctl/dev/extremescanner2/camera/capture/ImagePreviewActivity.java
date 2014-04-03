package ctl.dev.extremescanner2.camera.capture;

import java.io.File;
import java.io.IOException;

import ctl.dev.extremescanner2.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImagePreviewActivity extends Activity {
	Button btn_cancel, btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_preview);
		ImageView img = (ImageView) findViewById(R.id.image_preview_img);
		final String path = getIntent().getExtras().getString(
				"path");
		img.setImageBitmap(getBitmapFromFile(path));
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();				
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, new Intent().putExtra("path", path));
				finish();
			}
		});
	}

	Bitmap getBitmapFromFile(String path) {
		File imgFile = new File(path);

		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			return myBitmap;
		} else
			return null;
	}
}
