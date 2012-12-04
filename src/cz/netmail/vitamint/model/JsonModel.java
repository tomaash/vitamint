package cz.netmail.vitamint.model;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;

public class JsonModel {
//	public static final String SERVER_URL = "http://192.168.0.100:3000/";
//	public static final String SERVER_URL = "http://192.168.33.115:3000/";
	public static final String SERVER_URL = "http://eurosignal.heroku.com/";
	public static String token;
	public static boolean isLoggedIn;
	public static boolean isTracking;
	//	public static List isTracking;
	public static Intent locationUpdateServiceIntent;
	public static String login;
	public static String password;


	public static SaneHttpResponse postJson(String url, JSONObject data) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(JsonModel.SERVER_URL+url);
		HttpResponse response = null;
		SaneHttpResponse resp = new SaneHttpResponse();
		try {
			if (data == null) data = new JSONObject();			
			httpPost.setEntity(new StringEntity(data.toString()));			
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("Accept", "application/json");
			response = httpClient.execute(httpPost);
			resp.code = response.getStatusLine().getStatusCode();
			resp.status = response.getStatusLine().toString();
			resp.body = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			resp.code = 500;
			resp.status = "Internal error";
			e.printStackTrace();
		}
		return resp;		
	}

	public static SaneHttpResponse putJson(String url, JSONObject data, String id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut;
		//		try {
		httpPut = new HttpPut(JsonModel.SERVER_URL+url+"/"+id+".json");
		//		} catch (JSONException e1) {
		//			Log.e("PUT JSON","Cannot PUT without id");
		//			return null;
		//		}
		HttpResponse response = null;
		SaneHttpResponse resp = new SaneHttpResponse();
		try {
			//			if (data == null) data = new JSONObject();
			if (JsonModel.token!=null) data.put("user_credentials", JsonModel.token);			
			httpPut.setEntity(new StringEntity(data.toString()));			
			httpPut.addHeader("Content-Type", "application/json");
			httpPut.addHeader("Accept", "application/json");
			response = httpClient.execute(httpPut);
			resp.code = response.getStatusLine().getStatusCode();
			resp.status = response.getStatusLine().toString();
			if (response.getEntity()!=null){
				resp.body = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			resp.code = 500;
			resp.status = "Internal error";
			e.printStackTrace();
		}
		return resp;		
	}


	public static SaneHttpResponse getJson(String url) {
		if (JsonModel.token!=null) url += "?user_credentials="+JsonModel.token;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(JsonModel.SERVER_URL+url);
		HttpResponse response = null;
		SaneHttpResponse resp = new SaneHttpResponse();
		try {
			response = httpClient.execute(httpGet);
			resp.code = response.getStatusLine().getStatusCode();
			resp.status = response.getStatusLine().toString();
			resp.body = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			resp.code = 500;
			resp.status = "Internal error";
			e.printStackTrace();
		}
		return resp;		
	}

}
