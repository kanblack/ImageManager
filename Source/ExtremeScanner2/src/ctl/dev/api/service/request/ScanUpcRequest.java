package ctl.dev.api.service.request;

import java.util.HashMap;

import com.android.volley.Request.Method;

import ctl.dev.api.Request;
import ctl.dev.api.service.param.ScanUpcParam;
import ctl.dev.api.service.receiver.ScanUpcReceiver;

public class ScanUpcRequest extends Request<ScanUpcParam, ScanUpcReceiver> {


	public ScanUpcRequest(ScanUpcParam param, ScanUpcReceiver receiver) {
		super(param, receiver);
		// TODO Auto-generated constructor stub
	}

	public void excute(String upc) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("func", "scan_upc");
		hashMap.put("upc", upc);
		super.excute(hashMap);
	}
	
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return "ScanUpcRequest";
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

}
