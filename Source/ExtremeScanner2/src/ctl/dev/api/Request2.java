package ctl.dev.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import ctl.dev.extremescanner2.app.ApplicationController;

public abstract class Request2<P,R>
{		
	static HashMap<String, String> headerDefault;
	public Map<String, String> getRequestHeader() {
		
		return null;
	}

	
	
//	RequestQueue queue;
		
	ConnectListener<R>cListener;
	

	private ParamReal param;
	private R receiver;
	
	public R getReceiver() {
		return receiver;
	}

	public void setReceiver(R receiver) {
		this.receiver = receiver;
	}

	public Request2(P param,R receiver) 
	{
		this.param = (ParamReal) param;  
		this.receiver = receiver;
//		queue = Volley.newRequestQueue(context);
		
		if(headerDefault==null)
		{
			headerDefault = new HashMap<String, String>();
//			headerDefault.put("Content-Type", "application/json");
//			headerDefault.put("language", "vi");
		}
		 
	}
	
	public void excute() {
		cListener.onPrepare(getTag());

		ApplicationController.getInstance().addToRequestQueue(getRequest());	
	}
	
	public void excute(HashMap<String, String> params)
	{
		cListener.onPrepare(getTag());
		this.param.setParam(params);
		
		ApplicationController.getInstance().addToRequestQueue(getRequest());	
	}
	

	public void excute(JSONObject jsonRequest) 
	{
		cListener.onPrepare(getTag());
		this.param.setJsonRequest(jsonRequest);
		
		ApplicationController.getInstance().addToRequestQueue(getRequest());	
	}
	
	public void cancel()
	{
		ApplicationController.getInstance().cancelPendingRequests(getTag());	

//		queue.cancelAll(getTag());
	}
	
	public void setOnConnectListener(ConnectListener<R> cListener){
		this.cListener = cListener;
	}
	public abstract String getTag();
	
	public abstract int getMethod();
	
	JsonObjectRequest jsonObjectRequest;
	JsonObjectRequest getRequest() 
	{
		
		String url = param.getLink();
		HashMap<String, String> hashMap = param.getParam();
		if(hashMap !=null)
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			for (String key : hashMap.keySet()) 
			{
//				Log.e("key", key);
				params.add(new BasicNameValuePair(key, hashMap.get(key)));
			}
			url = new URLParser().getUrlforGETmethod(url,params);
			
		}
		
		
		if(getMethod() == com.android.volley.Request.Method.GET)
		{
			
			
			
			
		
			
			jsonObjectRequest = new JsonObjectRequest(
					getMethod(), url,param.getJsonRequest(), new Listener<JSONObject>() {

						@SuppressWarnings("unchecked")
						@Override
						public void onResponse(JSONObject response) {
//							onSuccess(response);
							Log.e("CHECK", "response");
							Gson gson = new Gson();
							receiver = (R) gson.fromJson(response.toString(), receiver.getClass());
							cListener.onSuccess(getTag(),response,receiver);
						
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							cListener.onError(getTag(),error);
							
						}
					})
			{
				
				
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(getRequestHeader() !=null)
					{
//						Log.e("HEADER", getRequestHeader()+"");
						return getRequestHeader();
					}
					else
					{
						return headerDefault;
					}
				}
			};

			jsonObjectRequest.setTag(getTag());
		}
		else
		{
//			Log.e("json"+getMethod(), param.getJsonRequest()+"");
			jsonObjectRequest = new JsonObjectRequest(
					getMethod(),url,param.getJsonRequest(), new Listener<JSONObject>() {

						@SuppressWarnings("unchecked")
						@Override
						public void onResponse(JSONObject response) 
						{
							Gson gson = new Gson();
							receiver = (R) gson.fromJson(response.toString(), receiver.getClass());
							
//							receiver = gson.fromJson(response,);
							cListener.onSuccess(getTag(),response,receiver);							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							cListener.onError(getTag(),error);
							try {
								Log.e("HEADER", jsonObjectRequest.getHeaders()+"");
							} catch (AuthFailureError e) {
								e.printStackTrace();
							}
						}
					})
			{
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(getRequestHeader() !=null)
					{
						return getRequestHeader();
						
					}
					else
					{

						return headerDefault;
					}
				}
			};
			
			jsonObjectRequest.setTag(getTag());
		}
		return jsonObjectRequest;
	}

	
	
}
