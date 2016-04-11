package com.microstrategy.soapuilibrary

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import java.net.URLEncoder;
import net.sf.json.JSONObject;


class FacebookHelper {
	
	public static JSONObject createFBTestUsers(String app_id, String secret, String user_name, String permission, Boolean proxyEnabled){
		return createFBTestUsers(app_id, secret, user_name, permission, proxyEnabled, null);
	}
	
	public static JSONObject createFBTestUsers(String app_id, String secret, String user_name, String permission, Boolean proxyEnabled, String thisProxy){
		// return facebook test user info + app token
		/*
		* Resposne Example:{"id":"100006967872705","email":"carol_lnrnlpv_carol@tfbnw.net","access_token":"CAADCGo7geucBADc0Q3aBxeiilVgW50IqGoPbIXEjkagQCbqzt9vpdz44W2x8QhZBFJElZADpVhZBBy9adhCcPLuEzDHgtc0kamR3IGIksgtAFJwzcuODpySXAxya1D5IYrBjDbutDqskIVZBYOWJrfOWVhQgJSXMwT9tNU6ZA2j4ycWwZB98C0VXai6p4oL82vAXPlZBqLhWwZDZD","login_url":"https://www.facebook.com/platform/test_account_login.php?user_id=100006967872705&n=MnENwO6iGKtVIVi","password":"1820155308","app_token":"213419322014439|6NmmoTcbsmoEDcaVZwL4jAjcjOI"}
		* */
		if(app_id==null | secret==null | user_name==null | permission==null | proxyEnabled==null){
			return null;
		}
		// common setting
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpHost proxy = null;
		if(proxyEnabled){
			if(thisProxy == null) proxy = new HttpHost("10.27.7.110", 8080, "http");
			else{
				def arryThisProxy = thisProxy.split(":");
				proxy = new HttpHost(arryThisProxy[1].replaceAll("//",""),arryThisProxy[2].toInteger(),arryThisProxy[0]);
			}
		}
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			// generate app token
		def url = "https://graph.facebook.com/oauth/access_token?grant_type=client_credentials&client_id="+app_id+"&client_secret="+secret;
		HttpPost httpostAppToken = new HttpPost(url);
		HttpResponse response = httpclient.execute(httpostAppToken);
		HttpEntity entity = response.getEntity();
		String ResponseBody = EntityUtils.toString(entity);
		def app_token = ResponseBody.split("=")[1]
		def encode_app_token = java.net.URLEncoder.encode(app_token,"UTF-8");
									// create test user
		url = "https://graph.facebook.com/"+app_id+"/accounts/test-users?installed=true&name="+user_name+"&locale=en_US&permissions="+permission+"&access_token="+encode_app_token;
		HttpPost httppostTestUser = new HttpPost(url);
		response = httpclient.execute(httppostTestUser);
		entity = response.getEntity();
		ResponseBody = EntityUtils.toString(entity);
		def Response = net.sf.json.JSONSerializer.toJSON(ResponseBody.trim());
		Response.put("app_token",app_token.toString());
		return Response;
	}
	
	public static JSONObject deleteFBTestUsers(String user_id, String app_token, Boolean proxyEnabled){
		return deleteFBTestUsers(user_id, app_token, proxyEnabled, null);
	}
	
	
	public static JSONObject deleteFBTestUsers(String user_id, String app_token, Boolean proxyEnabled, String thisProxy){
		if(user_id==null | app_token==null | proxyEnabled==null){
			return null;
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpHost proxy = null;
		if(proxyEnabled){
			if(thisProxy == null) proxy = new HttpHost("10.27.7.110", 8080, "http");
			else{
				def arryThisProxy = thisProxy.split(":");
				proxy = new HttpHost(arryThisProxy[1].replaceAll("//",""),arryThisProxy[2].toInteger(),arryThisProxy[0]);
			}
		}
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		def url = "https://graph.facebook.com/"+user_id+"?access_token="+java.net.URLEncoder.encode(app_token,"UTF-8");
		HttpDelete httpDeleteUser = new HttpDelete(url);
		HttpResponse response = httpclient.execute(httpDeleteUser);
		HttpEntity entity = response.getEntity();
		String ResponseBody = EntityUtils.toString(entity);
		JSONObject Response = new JSONObject();
		Response.put("message", ResponseBody.trim());
		return Response;
	}
}
