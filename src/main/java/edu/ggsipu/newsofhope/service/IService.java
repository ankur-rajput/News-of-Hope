package edu.ggsipu.newsofhope.service;

import java.util.List;

import edu.ggsipu.newsofhope.pojo.NewsFeed;

public interface IService {

	public List<NewsFeed> getNews() throws Exception;

	public void Categorization() throws Exception;

	public List<NewsFeed> getPositiveNews() throws Exception;

	public List<NewsFeed> getNegativeNews() throws Exception;

	public List<NewsFeed> getNeutralNews() throws Exception;

}
