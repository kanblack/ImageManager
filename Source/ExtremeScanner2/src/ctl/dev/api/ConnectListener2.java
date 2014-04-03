package ctl.dev.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.VolleyError;

public interface ConnectListener2<R>{
		public void onPrepare(Object tag);
		public void onSuccess(Object tag,JSONArray response,R object);
		public void onError(Object tag,VolleyError error);
	}