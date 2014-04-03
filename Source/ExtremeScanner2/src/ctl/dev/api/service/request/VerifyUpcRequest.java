package ctl.dev.api.service.request;

import java.util.HashMap;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.VerifyUpcParam;
import ctl.dev.api.service.receiver.VerifyUpcReceiver;

public class VerifyUpcRequest extends Request<VerifyUpcParam, VerifyUpcReceiver>{

	public VerifyUpcRequest(VerifyUpcParam param, VerifyUpcReceiver receiver) {
		super(param, receiver);
	}

	public void excute(String upc, String stylecolor) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("func","verify_upc");
		hashMap.put("upc", upc);
		hashMap.put("stylecolor", stylecolor);
		super.excute(hashMap);
	}
	
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "VerifyUpcRequest";
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return 0;
	}

}
