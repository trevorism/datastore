package com.trevorism.gcloud.bean

import java.text.SimpleDateFormat
import java.util.function.Supplier

@jakarta.inject.Singleton
class DateFormatProvider {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private final ThreadLocal<SimpleDateFormat> dateFormat =
            ThreadLocal.withInitial({ new SimpleDateFormat(PATTERN) } as Supplier)

    SimpleDateFormat getDateFormat() {
        return dateFormat.get()
    }
}
