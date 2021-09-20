package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.apiservice.NewsApi

class RemoteDataSource : DataSource {

    override suspend fun getTopHeadlines(country: String?, category: String?, q: String?, pageSize: Int?, page: Int?)
            =  NewsApi.apiService.getTopHeadlines(country, category, q, pageSize, page)

}