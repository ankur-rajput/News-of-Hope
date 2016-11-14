package edu.ggsipu.newsofhope.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.ggsipu.newsofhope.service.IService;
import edu.ggsipu.newsophope.pojo.NewsFeed;

@Controller
public class HomeController {
	@Autowired
	private IService service;

	@RequestMapping(value = "/")
	public ModelAndView index() throws Exception {

		List<NewsFeed> news = service.getNews();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("news", news);
		modelAndView.setViewName("index");
		return modelAndView;
	}

}
