package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.HeadlineReq
import com.sam.gogoeat.api.usecase.basic.ResourceUseCase
import retrofit2.Response

class GetTopHeadlinesData(private var repository: Repository) : ResourceUseCase<Resource<List<Article>>, HeadlineReq, List<Article>, NewsResp<List<Article>>>() {

    override suspend fun getResponse(params: HeadlineReq): Response<NewsResp<List<Article>>> {
        return repository.getTopHeadlines(params.country, params.category, params.q, params.pageSize, params.page)
    }
}