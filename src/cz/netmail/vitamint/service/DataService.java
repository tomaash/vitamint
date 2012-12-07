package cz.netmail.vitamint.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.Chapter;
import cz.netmail.vitamint.model.Country;
import cz.netmail.vitamint.model.ExpandableDataProvider;

public class DataService {
	
	public static final String SERVER_URL = "https://oauth-demo-netmail.appspot.com";
	
	public static DefaultHttpClient client = new DefaultHttpClient();
	public static Gson gson = new Gson();
	
	public static Type ChapterCollectionType = new TypeToken<Collection<Chapter>>(){}.getType();
	public static Type CountryCollectionType = new TypeToken<Collection<Country>>(){}.getType();
	public static Type ArticleCollectionType = new TypeToken<Collection<Article>>(){}.getType();
	
	public static Collection<Article> articles;
	public static List<ExpandableDataProvider> chapters;
	public static List<ExpandableDataProvider> countries;

}
