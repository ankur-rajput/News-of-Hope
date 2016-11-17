package edu.ggsipu.newsofhope.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import edu.ggsipu.newsofhope.service.IService;
import edu.ggsipu.newsophope.pojo.NewsFeed;

@Service
public class ServiceImpl implements IService {

	List<NewsFeed> news = null, positiveNews = null, negativeNews = null, neutralNews = null;

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
	public List<NewsFeed> getNews() throws Exception {

		news = new ArrayList<>();
		synchronized (news) {
			FetchNews t1 = new FetchNews("http://timesofindia.indiatimes.com/rssfeedstopstories.cms");
			FetchNews t2 = new FetchNews("http://pib.nic.in/newsite/rssenglish.aspx");
			t2.start();
			t1.start();
			t2.join();
			t1.join();
		}

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Categorization();
				} catch (Exception e) {
					System.out.println("Exception in categorization");
					e.printStackTrace();
				}
			}
		});
		t.start();

		return news;

	}

	private String sendPostRequest(String newsTitle) throws Exception {
		URL sentimentAPIUrl = new URL("http://sentiment.vivekn.com/api/text/");
		HttpURLConnection con = (HttpURLConnection) sentimentAPIUrl.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameter = "txt=" + newsTitle;

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameter);
		wr.flush();
		wr.close();

		// int responseCode = con.getResponseCode();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();

		JSONObject json = (JSONObject) new JSONParser().parse(response.toString());
		json = (JSONObject) json.get("result");
		String sentiment = json.get("sentiment").toString();

		return sentiment;
	}

	@Override
	public void Categorization() throws Exception {

		positiveNews = new ArrayList<>();
		negativeNews = new ArrayList<>();
		neutralNews = new ArrayList<>();

		for (NewsFeed newsFeed : news) {
			String sentiment = sendPostRequest(newsFeed.getTitle());
			if ("Positive".equalsIgnoreCase(sentiment))
				positiveNews.add(newsFeed);
			else if ("Neutral".equalsIgnoreCase(sentiment))
				neutralNews.add(newsFeed);
			else
				negativeNews.add(newsFeed);
		}

	}

	@Override
	public List<NewsFeed> getPositiveNews() throws Exception {

		return positiveNews;
	}

	@Override
	public List<NewsFeed> getNegativeNews() throws Exception {

		return negativeNews;
	}

	@Override
	public List<NewsFeed> getNeutralNews() throws Exception {

		return neutralNews;
	}

}
