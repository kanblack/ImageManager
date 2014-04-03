package ctl.dev.api;

import java.util.HashMap;

public class ParamReal extends Param{

//	public static String HOST = Resources.getSystem().getString(R.string.host);
//	public final static String HOST = "http://www.k5.com.au/bm-test/api/";
	public final static String HOST = "http://www.k5.com.au/image-manager/api/";
	
	@Override
	public String getLink() {
		// TODO Auto-generated method stub
		return HOST;
	}
	
	private HashMap<String, String> param;
		
		public HashMap<String, String> getParam() 
		{
			return param;
		}
	
	
		public void setParam(HashMap<String, String> param) {
			this.param = param;
		}
}
