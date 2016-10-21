package edu.ggsipu.newsofhope.config;

import java.net.URL;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Application {

	public static void main(String[] args) throws Exception {
		URL feedUrl = new URL("http://www.firstpost.com/india/feed");

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedUrl));
		System.out.println(feed);
	}

}
