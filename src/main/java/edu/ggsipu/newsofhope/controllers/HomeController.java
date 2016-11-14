package edu.ggsipu.newsofhope.controllers;

import java.net.URL;
import java.net.URLConnection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public ModelAndView index() throws Exception {

		URL feedUrl = new URL("http://timesofindia.indiatimes.com/rssfeedstopstories.cms");
		URLConnection conn = feedUrl.openConnection();

		// Create a document builder
		SAXBuilder saxBuilder = new SAXBuilder();
		// Create a document from a stream
		Document document = saxBuilder.build(conn.getInputStream());

		// Extract the root element
		Element classElement = document.getRootElement();
		System.out.println(classElement.getName());

		ModelAndView modelAndView = new ModelAndView();
		return modelAndView;
	}

}
