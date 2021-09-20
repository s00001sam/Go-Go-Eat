package com.sam.gogoeat.api.repository

import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.data.Article
import retrofit2.Response

class Repository (
        private val remoteDataSource: DataSource,
        private val localDataSource: DataSource,
) {

    suspend fun getTopHeadlines(country: String?, category: String?, q: String?, pageSize: Int?, page: Int?): Response<NewsResp<List<Article>>> {
        return remoteDataSource.getTopHeadlines(country, category, q, pageSize, page)
    }


}