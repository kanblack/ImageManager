package ctl.dev.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

public class URLParser {
	String mUrl = "";
	HttpPost httpPost;

	public String getUrlforGETmethod(String mUrl, List<NameValuePair> params) {
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		mUrl += "?";
		mUrl += paramString;
		Log.e(mUrl, mUrl);
		return mUrl;
		// return
		// "http://stage.ws.mastercard.com/locationmanagementservice/services/ATMLocationService/?countryCode=USA&city=ALLAMUCHY+TOWNSHIP&clientId=mctravel";
	}

	public String getUrlforGETmethod(String mUrl, HashMap<String, String> map) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
			params.add(new BasicNameValuePair(key, map.get(key)));
		}
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		mUrl += "?";
		mUrl += paramString;
		return mUrl;
	}

	public HttpPost getUrlforPOSTmethod(String mUrl, List<NameValuePair> params) {
		// String paramString = URLEncodedUtils.format(params, "UTF-8");
		// mUrl += "?";
		// mUrl += paramString;
		httpPost = new HttpPost(mUrl);
		// String path;
		try {
			// t
			// TODO: edit: haimt
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
			entity.setContentEncoding(HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			// end

			// httpPost.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpPost;
	}

	public HttpPost getUrlforPOSTmethodWithParams(String mUrl,
			List<NameValuePair> params) {
		// String paramString = URLEncodedUtils.format(params, "UTF-8");
		// mUrl += "?";
		// mUrl += paramString;
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		mUrl += "?";
		mUrl += paramString;
		Log.e(params + " ", " __" + mUrl + " ");

		httpPost = new HttpPost(mUrl);
		// try
		// {
		// TODO: edit: haimt
		// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
		// entity.setContentEncoding(HTTP.UTF_8);
		// entity.setContentType("application/json");
		// httpPost.setEntity(new UrlEncodedFormEntity(params));

		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		// end

		// httpPost.setEntity(new UrlEncodedFormEntity(params));
		// }
		// catch (UnsupportedEncodingException e)
		// {
		// e.printStackTrace();
		// }
		// catch (IOException e) {
		// e.printStackTrace();
		// }
		return httpPost;
	}

	public HttpPost getUrlforPOSTmethod(String mUrl, JSONObject data) {
		httpPost = new HttpPost(mUrl);
		try {
			StringEntity str = new StringEntity(data.toString(), "utf-8");
			str.setContentType("application/json;charset=UTF-8");
			httpPost.setEntity(str);
		} catch (UnsupportedEncodingException e) {
			Log.e("BUG", e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
		return httpPost;
	}

	public HttpPost getUrlforPOSTmethodWithParamsFacebook(String mUrl,
			List<NameValuePair> params) {
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		mUrl += "?";
		mUrl += paramString;
		Log.e(params + " ", " __" + mUrl + " ");

		httpPost = new HttpPost(mUrl);
		try {
			// TODO: edit: haimt
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
			entity.setContentEncoding(HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			// httpPost.setHeader("Content-Type",
			// "multipart/form-data;boundary="+"3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f");
			httpPost.setHeader("Connection", "Keep-Alive");
			// end

			// httpPost.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpPost;
	}

//	public HttpPost getUrlforPOSTmethodWithFile(String mUrl, String filepath,
//			String userId) throws IOException {
//
//		httpPost = new HttpPost(mUrl);
//		File photo = new File(filepath);
//
//		if (!photo.exists()) {
//			throw new FileNotFoundException();
//		}
//
//		MultipartEntity t = new MultipartEntity();
//		t.addPart("avatar", new FileBody(photo, "image/png"));
//		t.addPart("userId", new StringBody(userId));
//		httpPost.setEntity(t);
//
//		return httpPost;
//	}

	public byte[] read(File file) throws IOException {
		InputStream ios = null;
		ByteArrayOutputStream ous = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

}
