package com.github.ggeorgovassilis.webshop.service.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller serving the home page. Since this is a single-page app (so far), that's pretty much all it does
 * @author george georgovassilis
 *
 */
@Controller
public class HomePageController {

	@RequestMapping("/")
	public ModelAndView showHomePage() {
		return new ModelAndView("index");
	}
}
