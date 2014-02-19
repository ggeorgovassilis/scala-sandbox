package com.github.ggeorgovassilis.webshop;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class TestSupport {

	public static Object getHandler(MockHttpServletRequest request,
			ApplicationContext applicationContext) throws Exception {
		HandlerExecutionChain chain = null;

		Map<String, HandlerMapping> map = applicationContext
				.getBeansOfType(HandlerMapping.class);
		Iterator<HandlerMapping> itt = map.values().iterator();

		while (itt.hasNext()) {
			HandlerMapping mapping = itt.next();
			chain = mapping.getHandler(request);
			if (chain != null) {
				break;
			}
		}

		if (chain == null) {
			throw new InvalidParameterException(
					"Unable to find handler for request URI: "
							+ request.getRequestURI());
		}

		return chain.getHandler();
	}

}
