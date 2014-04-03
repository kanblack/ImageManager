package ctl.dev.api;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import ctl.dev.extremescanner2.app.ApplicationController;

public abstract class Request<P,R>
{		
	static HashMap<String, String> headerDefault;
	public Map<String, String> getRequestHeader() {
		
		return null;
	}

	
	
//	RequestQueue queue;
		
	ConnectListener2<R>cListener;
	

	private ParamReal param;
	private R receiver;
	
	public R getReceiver() {
		return receiver;
	}

	public void setReceiver(R receiver) {
		this.receiver = receiver;
	}

	public Request(P param,R receiver) 
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
	
	public Request<P, R> setOnConnectListener(ConnectListener2<R> cListener){
		this.cListener = cListener;
		return this;
	}
	public abstract String getTag();
	
	public abstract int getMethod();
	
	JsonArrayRequest JsonArrayRequest;
	JsonArrayRequest getRequest() 
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
		
		Log.e("Request URL", url+"");
		if(getMethod() == com.android.volley.Request.Method.GET)
		{
			
			
			
//			String a = "";
			
			
			
//			reader.setLenient(true);
			JsonArrayRequest = new JsonArrayRequest(
					getMethod(), url,param.getJsonRequest(), new Listener<JSONArray>() {

						@SuppressWarnings("unchecked")
						@Override
						public void onResponse(JSONArray response) {
//							onSuccess(response);
//							Log.e("CHECK", response.toString());
							try {
								Gson gson = new Gson();
//								JsonReader reader = new JsonReader(new StringReader("{"+response.toString()+"}"));
//								reader.setLenient(true);
								receiver = (R) gson.fromJson("{"+'"'+"Result"+'"'+":"+response.toString()+"}", receiver.getClass());
							} catch (JsonSyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.e("Error",e.getMessage());
							}
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

			JsonArrayRequest.setTag(getTag());
		}
		else
		{
//			Log.e("json"+getMethod(), param.getJsonRequest()+"");
			JsonArrayRequest = new JsonArrayRequest(
					getMethod(),url,param.getJsonRequest(), new Listener<JSONArray>() {

						@SuppressWarnings("unchecked")
						@Override
						public void onResponse(JSONArray response) 
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
								Log.e("HEADER", JsonArrayRequest.getHeaders()+"");
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
			
			JsonArrayRequest.setTag(getTag());
		}
		return JsonArrayRequest;
	}

	
	
}
