package com.sam.gogoeat.data

data class HeadlineReq (
        val country: String? = null,
        val category: String? = null,
        val q: String? = null,
        val pageSize: Int? = null,
        val page: Int? = null
)