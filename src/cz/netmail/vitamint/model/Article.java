package cz.netmail.vitamint.model;

import java.util.List;


public class Article {
	public Integer id;
	
	public String chapter;
    
    public String cover_url;
    public String teaser;
    public String description;
    public String title;
    public String order;
    public String country;
    
    // FIXME dates
    public String edited;
    public String posted;
    
    public Boolean is_deleted;
    public Boolean is_tip;
    public Boolean is_complete;
    public Boolean is_displayed;
    
    public List<Integer> authors;
    public List<String> countries;
    
    

    // FIXME proper objects
//    public List<String> attachments;
    public Stat stats;
}