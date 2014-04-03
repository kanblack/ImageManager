package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.DeleteImageParam;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.receiver.DeleteImageReceiver;
import ctl.dev.api.service.receiver.GetBatchesReceiver;

public class DeleteImageRequest extends
		Request<DeleteImageParam, DeleteImageReceiver> {

	

	public DeleteImageRequest(DeleteImageParam param,
			DeleteImageReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String id) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("func", "delete_image");
		params.put("id",id);
		excute(params);
	}

	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "AddStylecorlorRequest";
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

}
