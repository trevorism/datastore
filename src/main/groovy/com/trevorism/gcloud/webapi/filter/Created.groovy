package com.trevorism.gcloud.webapi.filter

import javax.ws.rs.NameBinding
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author tbrooks
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@interface Created {
}
