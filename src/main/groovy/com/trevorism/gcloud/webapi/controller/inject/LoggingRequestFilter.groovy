package com.trevorism.gcloud.webapi.controller.inject

import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import java.util.logging.Logger

/**
 * @author tbrooks
 */
class LoggingRequestFilter implements ContainerRequestFilter{

    private static final Logger log = Logger.getLogger(LoggingRequestFilter.class.name)

    @Override
    void filter(ContainerRequestContext requestContext) throws IOException {
        String correlationId = requestContext.getHeaderString("X-Correlation-ID")
        String output = ""
        if(correlationId)
            output = "${correlationId}: "

        log.info("${output}Received a ${requestContext.getMethod()} request at ${requestContext.getUriInfo().path}")

    }
}
