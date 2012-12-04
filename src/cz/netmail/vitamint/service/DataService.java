package cz.netmail.vitamint.service;

import org.apache.http.impl.client.DefaultHttpClient;

public class DataService {
	
//	public static DataService inst = new DataService();
	
	public static DefaultHttpClient client = new DefaultHttpClient();

//	public String token;
//	
//	public void run(Intent intent) {
//		new GetCookieTask().execute(intent);
//	}
//
//	private class GetCookieTask extends AsyncTask<Intent,Void,Boolean> {
//		private Intent intent;
//		protected Boolean doInBackground(Intent... intents) {
//			try {
//				// Don't follow redirects
//				intent = intents[0];
//				http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
//				String url = "https://oauth-demo-netmail.appspot.com/_ah/login?continue=http://localhost/&auth=" + token;
////				Log.e("debug", url);
//				HttpGet http_get = new HttpGet(url);
//				HttpResponse response;
//				response = http_client.execute(http_get);
//				if(response.getStatusLine().getStatusCode() != 302)
//					// Response should be a redirect
//					return false;
//
//				for(Cookie cookie : http_client.getCookieStore().getCookies()) {
////					Log.e("debug-cookie", cookie.getName());
////					Log.e("debug-cookie", cookie.getValue());
//					if(cookie.getName().equals("SACSID"))
//						return true;
//				}
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
//			}
//			return false;
//		}
//
//		protected void onPostExecute(Boolean result) {
//			nextTask.execute();
//		}

//	}	

}
