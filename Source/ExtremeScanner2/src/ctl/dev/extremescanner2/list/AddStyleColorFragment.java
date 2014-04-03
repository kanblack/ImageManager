package ctl.dev.extremescanner2.list;

import org.json.JSONArray;

import com.android.volley.VolleyError;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ctl.dev.api.ConnectListener2;
import ctl.dev.api.EasyDialogManager;
import ctl.dev.api.EasyMessageBox;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.request.AddStylecorlorRequest;
import ctl.dev.extremescanner2.BrowseActivity;
import ctl.dev.extremescanner2.R;

public class AddStyleColorFragment extends Fragment implements OnClickListener 
{
	TextView tv_batchName, tv_productName;
	Button btn_submit, btn_cancel;
	EditText edt_title;
	EditText edt_color;
	AddStylecorlorRequest addStylecorlorRequest;
	EasyDialogManager easyDialogManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.addstylecolor, container, false);
		tv_batchName = (TextView) v.findViewById(R.id.addstyle_tv_batchName);
		tv_productName = (TextView) v.findViewById(R.id.addstyle_tv_productName);
		btn_submit = (Button) v.findViewById(R.id.addstyle_btn_submit);
		btn_cancel = (Button) v.findViewById(R.id.addstyle_btn_cancel);
		edt_title = (EditText) v.findViewById(R.id.addstyle_edt_title);
		edt_color = (EditText) v.findViewById(R.id.addstyle_edt_color);
		
		btn_cancel.setOnClickListener(this);
		btn_submit.setOnClickListener(this);		
		
		if(addStylecorlorRequest == null)
		{
			addStylecorlorRequest = new AddStylecorlorRequest(new AddStylecolorParam(), new AddStylecolorReceiver());
			addStylecorlorRequest.setOnConnectListener(new ConnectListener2<AddStylecolorReceiver>() {
				
				@Override
				public void onSuccess(Object tag, JSONArray response,
						AddStylecolorReceiver object) {
					easyDialogManager.hideDialog();
					if(object.getResult().size()>0)
					{
						if(object.getResult().get(0).getCode().equals("1"))
						{
							isChange = true;
							edt_color.setText("");
							edt_title.setText("");
						}
						else
						{
							
						}
						new EasyMessageBox(object.getResult().get(0).getMessage()+"", null, getActivity()).show();
					}
					
				}
				
				@Override
				public void onPrepare(Object tag) {
					easyDialogManager = new EasyDialogManager();
					easyDialogManager.setCancelable(false);
					easyDialogManager.setMessage("Creating new stylecolor now, please wait...");
					easyDialogManager.showDialog(getActivity());
				}
				
				@Override
				public void onError(Object tag, VolleyError error) {
					easyDialogManager.hideDialog();
				}
			});
		}
		return v;
	}
	
	String batchId;
	String productTitle;
	public void setCombo(String batchId,String batch,String productTitle)
	{
		this.batchId = batchId;
		this.productTitle = productTitle;
		tv_batchName.setText(batch+"");
		tv_productName.setText(productTitle+"");				
	}

	boolean isChange;
	
	@Override
	public void onClick(View v) 
	{
		if(v == btn_cancel)
		{
			((BrowseActivity)getActivity()).showHolderFragment();
			if(isChange)
			{
				isChange = false;
				((BrowseActivity)getActivity()).batchListFragment.UpdateListAfterChange();
			}
			else
			{
				
			}
		}
		if(v == btn_submit)
		{
			Log.e("batchId+"+"+productTitle", batchId+"+"+productTitle);
			Toast.makeText(getActivity(), batchId+"+"+productTitle, 2000).show();
			if(!(edt_color.getText().toString().trim().length()*edt_title.getText().toString().trim().length() == 0))
			{
				addStylecorlorRequest.excute(batchId, productTitle, edt_title.getText()+"", edt_color.getText()+"");
			}
			else
			{
				new EasyMessageBox("Title and color must not be blank", "Format Error", getActivity()).show();
			}
		}
	}
}
