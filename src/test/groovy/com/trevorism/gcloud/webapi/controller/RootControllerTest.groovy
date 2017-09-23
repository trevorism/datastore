package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.LocalAppEngineTestBase
import org.junit.Test

/**
 * @author tbrooks
 */
class RootControllerTest extends LocalAppEngineTestBase{

    @Test
    void testRootController(){
        RootController rootController = new RootController()
        assert rootController.displayHelpLink()
    }

    @Test
    void testRootControllerPing(){
        RootController rootController = new RootController()
        assert rootController.ping() == "pong"
    }

    @Test
    void testRootControllerHelp(){
        RootController rootController = new RootController()
        assert rootController.help()
    }
}
