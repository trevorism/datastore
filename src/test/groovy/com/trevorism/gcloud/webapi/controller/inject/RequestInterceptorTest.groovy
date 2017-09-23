package com.trevorism.gcloud.webapi.controller.inject

import com.trevorism.gcloud.webapi.controller.CrudController
import org.junit.Test

import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.FeatureContext

/**
 * @author tbrooks
 */
class RequestInterceptorTest {

    @Test
    void testConfigure() {
        RequestInterceptor interceptor = new RequestInterceptor()
        boolean registerCalled = false

        ResourceInfo info = [getResourceClass : {return CrudController}] as ResourceInfo
        FeatureContext context = [register : { Class clazz -> registerCalled = true; return null }] as FeatureContext

        interceptor.configure(info, context)
        assert registerCalled

    }
}
