package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.data.Article
import retrofit2.Response

interface DataSource {

    suspend fun getTopHeadlines(country: String? = null,
                                category: String? = null,
                                q: String? = null,
                                pageSize: Int? = null,
                                page: Int? = null): Response<NewsResp<List<Article>>>

}