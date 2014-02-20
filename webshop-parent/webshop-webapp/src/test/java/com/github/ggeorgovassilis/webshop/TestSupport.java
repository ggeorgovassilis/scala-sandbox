package com.github.ggeorgovassilis.webshop;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class TestSupport {

	@SuppressWarnings("deprecation")
	public static int getDifferenceInDays(Date a, Date b) {

		long MS_PER_DAY = 1000 * 60 * 60 * 24;

		long utc1 = Date.UTC(a.getYear(), a.getMonth(), a.getDate(), 0,
				0, 0);
		long utc2 = Date.UTC(b.getYear(), b.getMonth(), b.getDate(),
				0, 0, 0);
		return (int)Math.floor((utc2 - utc1) / MS_PER_DAY);
	}

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
