package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.GetBatchesParam;
import ctl.dev.api.service.receiver.GetBatchesReceiver;

public class GetBatchesRequest extends Request<GetBatchesParam, GetBatchesReceiver> 
{

	public GetBatchesRequest(GetBatchesParam param,
			GetBatchesReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(boolean archive) 
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("func", "get_batches");
		if(archive)
		{
			params.put("type", "archive");
		}
		excute(params);
	}
	
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "GetBatchesRequest";
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}
	
}
