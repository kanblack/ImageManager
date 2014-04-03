package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.AddStylecolorParam;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.receiver.AddStylecolorReceiver;
import ctl.dev.api.service.receiver.GetBatchesReceiver;

public class AddStylecorlorRequest extends
		Request<AddStylecolorParam, AddStylecolorReceiver> {

	public AddStylecorlorRequest(AddStylecolorParam param,
			AddStylecolorReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String batch_id, String product_title, String style, String color) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("func", "add_stylecolor");
		params.put("batch_id",batch_id);
		params.put("product_title", product_title);
		params.put("style", style);
		params.put("color",color);
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
