package edu.ggsipu.newsofhope.service;

import java.util.List;

import edu.ggsipu.newsophope.pojo.NewsFeed;

public interface IService {

	public List<NewsFeed> getNews() throws Exception;

}
