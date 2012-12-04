package cz.netmail.vitamint;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.netmail.vitamint.service.DataService;

public class AccountActivity extends ListActivity {
	protected AccountManager accountManager;
	protected Intent intent;
	protected Activity parentActivity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountManager = AccountManager.get(getApplicationContext());
		Account[] accounts = accountManager.getAccountsByType("com.google");
		this.setListAdapter(new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accounts));        
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Account account = (Account)getListView().getItemAtPosition(position);
		AccountManager accountManager = AccountManager.get(getApplicationContext());
		//    		Account account = (Account)intent.getExtras().get("account");
		accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);
		parentActivity = this;
	}

	private class GetAuthTokenCallback implements AccountManagerCallback {
		public void run(AccountManagerFuture result) {
			Bundle bundle;
			try {
				bundle = (Bundle) result.getResult();
				Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
				if(intent != null) {
					// User input required
					startActivity(intent);
				} else {
					onGetAuthToken(bundle);
				}
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	protected void onGetAuthToken(Bundle bundle) {
		String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
		new GetCookieTask().execute(auth_token);
	}


	private class GetCookieTask extends AsyncTask<String,Void,Boolean> {
		protected Boolean doInBackground(String... tokens) {
			try {
				// Don't follow redirects
				DataService.client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
				String url = "https://oauth-demo-netmail.appspot.com/_ah/login?continue=http://localhost/&auth=" + tokens[0];
				HttpGet http_get = new HttpGet(url);
				HttpResponse response;
				response = DataService.client.execute(http_get);
				if(response.getStatusLine().getStatusCode() != 302)
					// Response should be a redirect
					return false;

				for(Cookie cookie : DataService.client.getCookieStore().getCookies()) {
					if(cookie.getName().equals("SACSID"))
						return true;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				DataService.client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			Intent intent = new Intent(parentActivity, MainActivity.class);
			startActivity(intent);
		}
	}    
}