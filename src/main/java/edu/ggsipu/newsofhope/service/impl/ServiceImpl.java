package edu.ggsipu.newsofhope.service.impl;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;

import edu.ggsipu.newsofhope.service.IService;
import edu.ggsipu.newsophope.pojo.NewsFeed;

@Service
public class ServiceImpl implements IService {

	List<NewsFeed> news = new ArrayList<>();

	class FetchNews extends Thread {
		private String link;

		public FetchNews(String link) {
			this.link = link;
		}

		@Override
		public void run() {

			try {
				URL feedUrl = new URL(link);
				URLConnection conn = feedUrl.openConnection();

				// Create a document builder
				SAXBuilder saxBuilder = new SAXBuilder();
				// Create a document from a stream
				Document document = saxBuilder.build(conn.getInputStream());

				// Extract the root element
				Element classElement = document.getRootElement().getChild("channel");
				List<Element> newsElement = classElement.getChildren("item");

				for (Element element : newsElement) {
					NewsFeed singleNews = new NewsFeed();
					singleNews.setTitle(element.getChildText("title"));
					singleNews.setDescription(element.getChildText("description"));
					singleNews.setLink(element.getChildText("link"));
					singleNews.setPubDate(element.getChildText("pubDate"));

					if (link.contains("pib"))
						singleNews.setWebsiteName("PIB");
					else
						singleNews.setWebsiteName("TOI");

					news.add(singleNews);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<NewsFeed> getNews() throws InterruptedException {

		FetchNews t1 = new FetchNews("http://timesofindia.indiatimes.com/rssfeedstopstories.cms");
		FetchNews t2 = new FetchNews("http://pib.nic.in/newsite/rssenglish.aspx");
		t2.start();
		t1.start();
		t2.join();
		t1.join();

		return news;
	}

}
