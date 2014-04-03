package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.receiver.AddProuctReceiver;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.receiver.GetBatchesReceiver;

public class AddProductRequest extends
		Request<AddStylecolorParam, AddProuctReceiver> {

	public AddProductRequest(AddStylecolorParam param,
			AddProuctReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String batch_id,String name) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("func", "add_product");
		params.put("batch_id", batch_id);
		params.put("name", name);
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
