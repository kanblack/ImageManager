package ctl.dev.extremescanner2.list;

import org.json.JSONArray;

import com.android.volley.VolleyError;

import ctl.dev.api.ConnectListener2;
import ctl.dev.api.EasyDialogManager;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.api.service.object.DeleteImage;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.DeleteImageParam;
import ctl.dev.api.service.receiver.DeleteImageReceiver;
import ctl.dev.api.service.request.DeleteImageRequest;
import ctl.dev.extremescanner2.BrowseActivity;
import ctl.dev.extremescanner2.R;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ImageFragment extends Fragment {
	TextView mTextView, tv_pageIndex;
	ImageView img_view;
	String url, id;
	ImageView btn_delete;

	DeleteImageRequest deleteImageRequest;

	private static final String CONTENT_DATA_EXTRA_URL = "url";
	private static final String CONTENT_DATA_EXTRA_ID = "id";

	public ImageFragment newInstance(String url, String id) {
		final ImageFragment f = new ImageFragment();

		final Bundle args = new Bundle();
		args.putString(CONTENT_DATA_EXTRA_URL, url);
		args.putString(CONTENT_DATA_EXTRA_ID, id);

		f.setArguments(args);

		return f;
	}

	EasyDialogManager easyDialogManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments() != null ? getArguments().getString(
				CONTENT_DATA_EXTRA_URL) : null;

		id = getArguments() != null ? getArguments().getString(
				CONTENT_DATA_EXTRA_ID) : null;
				
				Log.e("imageURL", url+"");

		if (deleteImageRequest == null) {
			deleteImageRequest = new DeleteImageRequest(new DeleteImageParam(),
					new DeleteImageReceiver());
			deleteImageRequest
					.setOnConnectListener(new ConnectListener2<DeleteImageReceiver>() {

						@Override
						public void onSuccess(Object tag, JSONArray response,
								DeleteImageReceiver object) {
							easyDialogManager.hideDialog();
							if (object.getResult().get(0).getCode().equals("1")) {
								new EasyMessageBox("Delete success", null,
										getActivity()).show();
								((BrowseActivity) getActivity()).imageSliderFragment
										.updateAfterDelete();
							} else {
								new EasyMessageBox(object.getResult().get(0)
										.getMessage()
										+ "", null, getActivity()).show();

							}
						}

						@Override
						public void onPrepare(Object tag) {
							easyDialogManager = new EasyDialogManager();
							easyDialogManager.showDialog(getActivity());
							easyDialogManager.setCancelable(false);
						}

						@Override
						public void onError(Object tag, VolleyError error) {
							easyDialogManager.hideDialog();
							new EasyMessageBox(
									error.getLocalizedMessage() + "", null,
									getActivity()).show();
						}
					});
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image, container, false);
		img_view = (ImageView) v.findViewById(R.id.image_image);
		btn_delete = (ImageView) v.findViewById(R.id.image_btn_delete);

		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EasyMessageBox("Confirm delete this image", null, getActivity()).show2(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						deleteImageRequest.excute(id);
					}
				});
			
			}
		});

		ImageSliderFragment.imageLoader.DisplayImage(url + "", img_view, 0);
		return v;
	}
}
