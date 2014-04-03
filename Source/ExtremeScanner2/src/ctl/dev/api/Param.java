package ctl.dev.api;

import java.util.HashMap;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import android.util.Log;

public abstract class Param {
	protected URLParser urlParser;

	public abstract String getLink();
	
	

	public JSONObject getJsonRequest() {
		return jsonRequest;
	}


	public void setJsonRequest(JSONObject jsonRequest) {
		this.jsonRequest = jsonRequest;
	}



	private JSONObject jsonRequest;
	
	

	
//	public abstract JSONObject getJsonRequest();

	// constructor
	public Param(URLParser urlParser) {
		urlParser = new URLParser();
	}

	public Param() {
		urlParser = new URLParser();
	}
}
