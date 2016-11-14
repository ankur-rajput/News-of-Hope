package edu.ggsipu.newsofhope.service.impl;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import edu.ggsipu.newsofhope.service.IService;
import edu.ggsipu.newsophope.pojo.NewsFeed;

public class ServiceImpl implements IService {

	@Override
	public List<NewsFeed> getNews() throws Exception {
		URL feedUrl = new URL("http://timesofindia.indiatimes.com/rssfeedstopstories.cms");
		URLConnection conn = feedUrl.openConnection();

		// Create a document builder
		SAXBuilder saxBuilder = new SAXBuilder();
		// Create a document from a stream
		Document document = saxBuilder.build(conn.getInputStream());

		// Extract the root element
		Element classElement = document.getRootElement();
		System.out.println(classElement.getName());
		return null;
	}

}
