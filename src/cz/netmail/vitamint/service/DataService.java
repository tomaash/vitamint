package cz.netmail.vitamint.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cz.netmail.vitamint.R;
import cz.netmail.vitamint.model.Action;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.Chapter;
import cz.netmail.vitamint.model.Country;
import cz.netmail.vitamint.model.ExpandableDataProvider;
import cz.netmail.vitamint.model.User;

public class DataService {
	
	public static String SERVER_URL;// = "https://oauth-demo-netmail.appspot.com";
	public static final String ING_SERVER_URL = "https://vitamin-t.appspot.com";
	public static final String DEMO_SERVER_URL = "https://oauth-demo-netmail.appspot.com";
	
	public static DefaultHttpClient client = new DefaultHttpClient();
	public static Gson gson = new Gson();
	
	public static Type ChapterCollectionType = new TypeToken<Collection<Chapter>>(){}.getType();
	public static Type CountryCollectionType = new TypeToken<Collection<Country>>(){}.getType();
	public static Type ArticleCollectionType = new TypeToken<Collection<Article>>(){}.getType();
	public static Type UserCollectionType = new TypeToken<Collection<User>>(){}.getType();
	public static Type ActionCollectionType = new TypeToken<Collection<Action>>(){}.getType();
	
	public static Collection<Article> articles;
	public static List<ExpandableDataProvider> chapters;
	public static List<ExpandableDataProvider> countries;
	
	public static ArrayList<HashMap<String, String>> listData;
	public static ArrayList<HashMap<String, String>> actionListData;
	
	public static SparseArray<Article> articleData;
	public static SparseArray<User> userData;
	
	public static List<Action> actions;

	public static int getResourceForCountry(String code) {
		if (code.contentEquals("cz")) return R.drawable.czech_republic;
		if (code.contentEquals("bg")) return R.drawable.bulgaria;
		if (code.contentEquals("gr")) return R.drawable.greece;
		if (code.contentEquals("es")) return R.drawable.spain;
		if (code.contentEquals("pl")) return R.drawable.poland;
		if (code.contentEquals("hu")) return R.drawable.hungary;
		if (code.contentEquals("kr")) return R.drawable.south_korea;
		if (code.contentEquals("nl")) return R.drawable.nederland;
		if (code.contentEquals("ro")) return R.drawable.romania;
		if (code.contentEquals("sk")) return R.drawable.slovakia;
		if (code.contentEquals("tr")) return R.drawable.turkey;
		return R.drawable.chapter;
	}
}
