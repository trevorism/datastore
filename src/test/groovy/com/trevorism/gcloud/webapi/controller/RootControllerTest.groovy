package com.trevorism.data.controller

import com.trevorism.gcloud.webapi.controller.RootController
import org.junit.jupiter.api.Test

class RootControllerTest {

    @Test
    void testRootControllerEndpoints() {
        RootController rootController = new RootController()
        assert rootController.index().getBody().get()[0].contains("ping")
        assert rootController.index().getBody().get()[1].contains("help")
    }

    @Test
    void testRootControllerPing() {
        RootController rootController = new RootController()
        assert rootController.ping() == "pong"
    }
}
