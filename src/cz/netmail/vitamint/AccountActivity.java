package cz.netmail.vitamint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DialerFilter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cz.netmail.vitamint.component.ImageSimpleAdapter;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.Chapter;
import cz.netmail.vitamint.model.Country;
import cz.netmail.vitamint.model.ExpandableDataProvider;
import cz.netmail.vitamint.service.DataService;

public class AccountActivity extends ListActivity {
	protected AccountManager accountManager;
	protected Intent intent;
	protected Activity parentActivity;
	public static ProgressDialog dailog;

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
		dailog = ProgressDialog.show(this, "Please wait",
                "Logging in..", true);
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
//			Intent intent = new Intent(parentActivity, MainActivity.class);
//			startActivity(intent);
			new LoadArticlesTask().execute();
		}
	}
	
	private class LoadArticlesTask extends AsyncTask<Void,Void,Collection<Article>> {
		@Override
		protected Collection<Article> doInBackground(Void... nil) {
			try {
				String url = "https://oauth-demo-netmail.appspot.com/api/articles";
				HttpGet http_get = new HttpGet(url);
				HttpResponse result = DataService.client.execute(http_get);
				String data = EntityUtils.toString(result.getEntity());
				Collection<Article> articles = DataService.gson.fromJson(data, DataService.ArticleCollectionType);
				return articles;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Collection<Article> result) {
			//				Log.e("data", result.toString());
			//				Toast.makeText(getActivity().getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
			if (result == null || result.isEmpty()) {
				Toast.makeText(getApplicationContext(), "No articles for this account", Toast.LENGTH_LONG).show();
				return;
			}

			DataService.articles = result;

//			ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
			HashMap<String, Chapter> chapterData = new HashMap<String, Chapter>();
			HashMap<String, Country> countryData = new HashMap<String, Country>();
			DataService.countries = new ArrayList<ExpandableDataProvider>();
			DataService.chapters = new ArrayList<ExpandableDataProvider>();
			DataService.listData = new ArrayList<HashMap<String,String>>();

			for (Article article : result) {
//				Log.e("chapter", article.chapter);
//				Log.e("title", article.title);
				if (chapterData.containsKey(article.chapter)) {
					chapterData.get(article.chapter).articlesCollection.add(article);
				} else {
					Chapter chapter = new Chapter();
					chapter.id = article.chapter;
					chapter.name = article.chapter;
					chapter.articlesCollection = new ArrayList<Article>();
					chapter.articlesCollection.add(article);
					chapterData.put(article.chapter, chapter);
				}
				
				for (String countryCode : article.countries) {
					if (countryData.containsKey(countryCode)) {
						countryData.get(countryCode).articlesCollection.add(article);
					} else {
						Country country = new Country();
						country.id = countryCode;
						country.name = countryCode;
						country.articlesCollection = new ArrayList<Article>();
						country.articlesCollection.add(article);
						countryData.put(countryCode, country);
					}
				}
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", article.title);
				map.put("id", ""+article.id);
				map.put("teaser", article.teaser);
				map.put("country", article.country);
				
				map.put("views", article.stats.views.toString());
				map.put("likes", article.stats.likes.toString());
				map.put("comments", article.stats.comments.toString());
				
				if (article.cover_url!=null) map.put("image", DataService.SERVER_URL + article.cover_url);
				DataService.listData.add(map);
			}
			
			for (Country country : countryData.values()) {
//				Log.e("country", country.id);
				DataService.countries.add(country);
			}
			
			for (Chapter chapter : chapterData.values()) {
//				Log.e("chapter", chapter.id);
				DataService.chapters.add(chapter);
			}
			
			
			new LoadCountriesTask().execute();
			
		}
	}
	
	
	private class LoadCountriesTask extends AsyncTask<Void,Void,String> {
		@Override
		protected String doInBackground(Void... nil) {
			try {
				String url = "https://oauth-demo-netmail.appspot.com/api/countries";
				HttpGet http_get = new HttpGet(url);
				HttpResponse result = DataService.client.execute(http_get);
				String data = EntityUtils.toString(result.getEntity());
				return data;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			Collection<Country> countries = DataService.gson.fromJson(result, DataService.CountryCollectionType);
			HashMap<String, Country> countryData = new HashMap<String, Country>();
			for (Country country : countries) {
				countryData.put(country.id, country);
			}
			for (ExpandableDataProvider country : DataService.countries) {
				country.name = countryData.get(country.id).name;
			}
			
			new LoadChaptersTask().execute();
		}
	}
	
	private class LoadChaptersTask extends AsyncTask<Void,Void,String> {
		@Override
		protected String doInBackground(Void... nil) {
			try {
				String url = "https://oauth-demo-netmail.appspot.com/api/chapters";
				HttpGet http_get = new HttpGet(url);
				HttpResponse result = DataService.client.execute(http_get);
				String data = EntityUtils.toString(result.getEntity());
				return data;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			Collection<Chapter> chapters = DataService.gson.fromJson(result, DataService.ChapterCollectionType);
			HashMap<String, Chapter> chapterData = new HashMap<String, Chapter>();
			for (Chapter chapter : chapters) {
				chapterData.put(chapter.id, chapter);
			}
			for (ExpandableDataProvider chapter : DataService.chapters) {
				chapter.title = chapterData.get(chapter.id).title;
				chapter.name = chapterData.get(chapter.id).title;
				chapter.introduction = chapterData.get(chapter.id).introduction;
			}
			
			if (AccountActivity.dailog!=null) {
				AccountActivity.dailog.dismiss();
			}			
			
			Intent intent = new Intent(parentActivity, MainActivity.class);
			startActivity(intent);

		}
	}

	
}