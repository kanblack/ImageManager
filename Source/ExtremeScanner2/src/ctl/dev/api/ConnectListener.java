package ctl.dev.api;

import org.json.JSONObject;

import com.android.volley.VolleyError;

public interface ConnectListener<R>{
		public void onPrepare(Object tag);
		public void onSuccess(Object tag,JSONObject response,R object);
		public void onError(Object tag,VolleyError error);
	}