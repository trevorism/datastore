package com.trevorism

import groovy.transform.CompileStatic
import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OpenAPIDefinition(
        info = @Info(
                title = "Datastore API",
                version = "2.1.0",
                description = "Wraps google datastore and conforms to the Trevorism Data interface",
                contact = @Contact(url = "https://trevorism.com", name = "Trevor Brooks", email = "tbrooks@trevorism.com")
        )
)
@CompileStatic
class Application {
    private static final Logger log = LoggerFactory.getLogger( Application )

    static void main(String[] args) {
        log.info("Started Datastore app.")
        Micronaut.run(Application, args)
    }
}
