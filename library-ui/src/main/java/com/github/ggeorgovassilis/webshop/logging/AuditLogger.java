package com.github.ggeorgovassilis.webshop.logging;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;

/**
 * Audit logger
 * @author george georgovassilis
 *
 */
public class AuditLogger extends CustomizableTraceInterceptor{

	protected Logger logger = Logger.getLogger(AuditLogger.class);
	
	@Override
	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
			throws Throwable {
		logger.info("Called "+invocation.getMethod());
		return super.invokeUnderTrace(invocation, logger);
	}
	
	@Override
	protected boolean isLogEnabled(Log logger) {
		return true;
	}
}
