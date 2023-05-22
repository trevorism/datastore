package com.trevorism.gcloud.bean

import java.text.SimpleDateFormat

@jakarta.inject.Singleton
class DateFormatProvider {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    SimpleDateFormat getDateFormat() {
        return dateFormat
    }
}
