package ctl.dev.extremescanner2.list;

import imageloader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import ctl.dev.api.ConnectListener2;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.api.HttpClient;
import ctl.dev.api.ParamReal;
import ctl.dev.api.service.object.ImageItem;
import ctl.dev.api.service.param.GetImagesParam;
import ctl.dev.api.service.receiver.GetImagesReceiver;
import ctl.dev.api.service.request.GetImagesRequest;
import ctl.dev.extremescanner2.R;
import ctl.dev.extremescanner2.camera.capture.CameraActivity;

public class ImageSliderFragment extends Fragment implements OnClickListener {
	public static ImageLoader imageLoader;

	public ImageSliderFragment newInstance(String stylecolor) {
		ImageSliderFragment f = new ImageSliderFragment();

		Bundle args = new Bundle();
		args.putString("stylecolor", stylecolor);
		f.setArguments(args);

		return f;
	}

	ImageView img_pagger, img_holder;
	View view;
	GetImagesRequest getImagesRequest;
	ArrayList<ImageItem> arrayImage;
	ViewPager viewPager;
	PagerAdapter imageAdapter;
	ProgressBar prg_loading;
	TextView tv_count;
	Button btn_upload, btn_verify;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	public ImageSliderFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = inflater.inflate(R.layout.image_slider, container, false);
			viewPager = (ViewPager) view.findViewById(R.id.view_pager);
			prg_loading = (ProgressBar) view
					.findViewById(R.id.image_silder_prg_loading);
			tv_count = (TextView) view.findViewById(R.id.image_silder_tv_count);
			btn_upload = (Button) view.findViewById(R.id.image_slider_upload);
			btn_verify = (Button) view.findViewById(R.id.image_slider_verify);

			img_holder = (ImageView) view
					.findViewById(R.id.image_slider_holder);
			img_pagger = (ImageView) view
					.findViewById(R.id.image_slider_pagger);
			btn_upload.setOnClickListener(this);
			btn_verify.setOnClickListener(this);

			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					selectedImage = arg0;
					if (arrayImage.size() > 0) {
						img_pagger.setVisibility(View.VISIBLE);
						if (arg0 == 0) {
							img_pagger.setImageResource(R.drawable.dot_state1);
						} else if (arg0 == arrayImage.size() - 1) {
							img_pagger.setImageResource(R.drawable.dot_state3);
						} else {
							img_pagger.setImageResource(R.drawable.dot_state2);
						}
					} else {
						img_pagger.setVisibility(View.GONE);
					}
					String show = String.format(
							getResources().getString(R.string.image_count),
							arg0 + 1, arrayImage.size() + "");
					tv_count.setText(show);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});

			if (getImagesRequest == null) {
				getImagesRequest = new GetImagesRequest(new GetImagesParam(),
						new GetImagesReceiver());
				getImagesRequest
						.setOnConnectListener(new ConnectListener2<GetImagesReceiver>() {

							@Override
							public void onSuccess(Object tag,
									JSONArray response, GetImagesReceiver object) {
								prg_loading.setVisibility(View.GONE);
								if (arrayImage != null) {
									arrayImage.clear();
								}
								arrayImage = (ArrayList<ImageItem>) object
										.getResult();
								imageAdapter = new PagerAdapter(getActivity()
										.getSupportFragmentManager(),
										arrayImage);
								viewPager.setAdapter(imageAdapter);
								String index = "1";
								if (arrayImage.size() == 0) {
									index = "0";
									img_pagger.setVisibility(View.GONE);
									img_holder.setVisibility(View.VISIBLE);
								} else {
									img_holder.setVisibility(View.GONE);
									img_pagger.setVisibility(View.VISIBLE);
								}
								String show = String.format(getResources()
										.getString(R.string.image_count),
										index, arrayImage.size() + "");
								tv_count.setText(show);

								// imageAdapter.notifyDataSetChanged();
							}

							@Override
							public void onPrepare(Object tag) {
								prg_loading.setVisibility(View.VISIBLE);
							}

							@Override
							public void onError(Object tag, VolleyError error) {
								prg_loading.setVisibility(View.GONE);
								new EasyMessageBox(error.getLocalizedMessage(),
										null, getActivity()).show();
							}
						});

			}

			Log.e("Here", "CHECL");
			if (imageAdapter != null) {
				imageAdapter = new PagerAdapter(getActivity()
						.getSupportFragmentManager(), arrayImage);
				viewPager.setAdapter(imageAdapter);
				String show = String.format(
						getResources().getString(R.string.image_count),
						viewPager.getCurrentItem() + 1, arrayImage.size() + "");
				tv_count.setText(show);
			}
			if (imageLoader == null) {
				imageLoader = new ImageLoader(getActivity());
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	String currentUPC;
	private String mCurrentPhotoPath;

	public void getImage(String stylecolor) {
		// Toast.makeText(getActivity(), stylecolor, 2000).show();

		try {

			selectedImage = -1;
			if (!stylecolor.equals(currentUPC)) {
				currentUPC = stylecolor;
				if (arrayImage != null && imageAdapter != null) {
					arrayImage = new ArrayList<ImageItem>();
					imageAdapter.notifyDataSetChanged();
					String index = "1";
					if (arrayImage.size() == 0) {
						index = "0";
					}
					String show = String.format(
							getResources().getString(R.string.image_count),
							index, arrayImage.size() + "");

					tv_count.setText(show);
				}
				getImagesRequest.excute(stylecolor);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int selectedImage = -1;

	void updateAfterDelete() {
		try {
			if (selectedImage != -1 && selectedImage < arrayImage.size() - 1) {
				arrayImage.remove(selectedImage);
				imageAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class SendHttpRequestTask extends AsyncTask<Bitmap, Void, String> {

		Bitmap bitmap;
		String upc;

		public SendHttpRequestTask(Bitmap bitmap, String upc) {
			this.bitmap = bitmap;
			this.upc = upc;
		}

		@Override
		protected String doInBackground(Bitmap... params) {

			Log.e("Start", "Start");
			// Bitmap b = BitmapFactory.decodeResource(getResources(),
			// R.drawable.img_test);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 25, baos);
			String data = null;
			try {
				HttpClient client = new HttpClient(ParamReal.HOST);
				client.connectForMultipart();
				client.addFormPart("func", "upload_image");
				client.addFormPart("stylecolor", upc);
				client.addFilePart("file", "logo.png", baos.toByteArray());
				client.finishMultipart();
				data = client.getResponse();
				// Log.e("DATA", data+"");
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return data;
		}

		@Override
		protected void onPostExecute(String data) {
			if (arrRequestTasks.contains(this)) {
				arrRequestTasks.remove(this);
				Log.e("Size", arrRequestTasks.size() + "");
				if (arrRequestTasks.size() > 0) {
					arrRequestTasks.get(0).execute();

				} else {
					if (upc.equals(currentUPC)) {
						getImagesRequest.excute(upc);
					}
				}
			}
			if (data != null) {
				String result = data.substring(1, data.lastIndexOf("]"));
				Log.e("CHECK", upc + "." + result);
				try {
					JSONObject jsonObject = new JSONObject(result);

					if (jsonObject.getString("code").equals("1")) {
						Log.e("Success", result);
					} else {
						Log.e("Upload Failure", result);
						Toast.makeText(getActivity(), "Upload Failure", 2000)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {

			}
		}
	}

	public void openCamera() {
		// imageFilePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/myfavoritepicture.jpg";
		// File imageFile = new File(imageFilePath);
		// Uri imageFileUri = Uri.fromFile(imageFile);
		// Intent i = new
		// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
		// startActivityForResult(i, 0);
		startActivityForResult(new Intent(getActivity(), CameraActivity.class),
				0);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_upload) {
			openCamera();

		}
		if (v == btn_verify) {

		}
	}

	String imageFilePath;

	// Bitmap getImageFromCamera() throws IOException {
	// // Get a reference to the ImageView
	// Display currentDisplay = getActivity().getWindowManager()
	// .getDefaultDisplay();
	// int dw = currentDisplay.getWidth();
	// int dh = currentDisplay.getHeight();
	// // Load up the image's dimensions not the image itself
	// BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	// bmpFactoryOptions.inJustDecodeBounds = true;
	// Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
	// int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
	// / (float) dh);
	// int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
	// / (float) dw);
	// Log.v("HEIGHTRATIO", "" + heightRatio);
	// Log.v("WIDTHRATIO", "" + widthRatio);
	// // If both of the ratios are greater than 1,
	// // one of the sides of the image is greater than the screen
	// if (heightRatio > 1 && widthRatio > 1) {
	// if (heightRatio > widthRatio) {
	// // Height ratio is larger, scale according to it
	// bmpFactoryOptions.inSampleSize = heightRatio;
	// } else {
	// // Width ratio is larger, scale according to it
	// bmpFactoryOptions.inSampleSize = widthRatio;
	// }
	// }
	// // Decode it for real
	// bmpFactoryOptions.inJustDecodeBounds = false;
	// return bmp = getBitmap(BitmapFactory.decodeFile(imageFilePath,
	// bmpFactoryOptions));
	// }

	// Bitmap getBitmap(Bitmap sourceBitmap) throws IOException {
	//
	// ExifInterface exif = new ExifInterface(imageFilePath);
	// int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	// ExifInterface.ORIENTATION_NORMAL);
	// int rotationInDegrees = exifToDegrees(rotation);
	//
	// Matrix matrix = new Matrix();
	// if (rotation != 0f) {
	// matrix.preRotate(rotationInDegrees);
	// }
	//
	// Bitmap adjustedBitmap = Bitmap
	// .createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
	// sourceBitmap.getHeight(), matrix, true);
	// return adjustedBitmap;
	// }

	// private static int exifToDegrees(int exifOrientation) {
	// if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
	// return 90;
	// } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
	// return 180;
	// } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
	// return 270;
	// }
	// return 0;
	// }

	Bitmap getBitmapFromFile(String path) {
		File imgFile = new File(path);

		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			return myBitmap;
		} else
			return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (arrRequestTasks == null) {
				arrRequestTasks = new ArrayList<ImageSliderFragment.SendHttpRequestTask>();
			}

			Bitmap bmp;

			try {
				String path = data.getExtras().getString("path");
				bmp = getBitmapFromFile(path);

				arrRequestTasks.add(new SendHttpRequestTask(bmp, currentUPC));
				if (arrRequestTasks.size() == 1) {
					arrRequestTasks.get(0).execute();
				}
				openCamera();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	ArrayList<SendHttpRequestTask> arrRequestTasks;

	class PagerAdapter extends FragmentStatePagerAdapter {
		ArrayList<ImageItem> arrayImage;

		public PagerAdapter(FragmentManager fm, ArrayList<ImageItem> arrayList) {
			super(fm);
			arrayImage = arrayList;
		}

		@SuppressWarnings("static-access")
		@Override
		public Fragment getItem(int position) {
			return new ImageFragment().newInstance(arrayImage.get(position)
					.getUrl() + "",arrayImage.get(position).getId());

		}

		@Override
		public int getItemPosition(Object item) {
//			ImageFragment fragment = (ImageFragment)item;
//	        String title = fragment.getTitle();
//	        int position = titles.indexOf(title);
//
//
//	        if (position >= 0) {
//	            return position;
//	        } else {
	            return POSITION_NONE;
//	        };
		}

		@Override
		public int getCount() {
			return arrayImage.size();
		}

	}
}
