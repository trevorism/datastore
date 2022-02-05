package com.trevorism.gcloud.webapi.service


import com.trevorism.gcloud.webapi.model.paging.Page

interface PagingService {

    def page(Page pagingRequest, String kind)

}