package cz.netmail.vitamint.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Country {
	public List<Integer> articles;
	public List<Article> articlesCollection;
    public String id;
    public int has_content;
    public String name;
}
