package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.receiver.GetBatchesReceiver;

public class AddBatchRequest extends
		Request<AddStylecolorParam, AddStylecolorReceiver> {

	public AddBatchRequest(AddStylecolorParam param,
			AddStylecolorReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String name) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("func", "add_batch");
		params.put("name",name);
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
