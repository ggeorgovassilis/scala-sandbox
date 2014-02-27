package com.github.ggeorgovassilis.webshop.service;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ApplicationConfiguration extends WebMvcConfigurationSupport {

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(converter());
		addDefaultHttpMessageConverters(converters);
	}

	@Bean
	public MappingJackson2HttpMessageConverter converter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		return converter;
	}
}
