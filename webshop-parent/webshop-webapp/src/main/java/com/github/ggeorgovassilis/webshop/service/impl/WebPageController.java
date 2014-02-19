package com.github.ggeorgovassilis.webshop.service.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller serving various bits of HTML, mostly the angular.js templates
 * @author george georgovassilis
 *
 */
@Controller
public class WebPageController {

	@RequestMapping("/")
	public ModelAndView showHomePage() {
		return new ModelAndView("index");
	}

	@RequestMapping("/templates/{what}")
	public ModelAndView showTemplate(@PathVariable String what) {
		return new ModelAndView(what);
	}
}
