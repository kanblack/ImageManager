package ctl.dev.api.service.request;

import java.util.HashMap;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.GetImagesParam;
import ctl.dev.api.service.receiver.GetImagesReceiver;

public class GetImagesRequest extends Request<GetImagesParam, GetImagesReceiver> 
{

	

	public GetImagesRequest(GetImagesParam param, GetImagesReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String stylecolor) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("func", "get_images");
		hashMap.put("stylecolor", stylecolor);
		
		super.excute(hashMap);
	}
	
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return 0;
	}

}
