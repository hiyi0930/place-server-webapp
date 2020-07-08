package com.lsy.common.dto

open class ResultType(
        var resultMessage: String = ""
)

open class PageResultType(
        var page: Int = 1,
        var pageIsEnd: Boolean = true,
        var totalCount: Int = 0
)

class PageableData<T>(var data: List<T>? = emptyList(), var page: Int = 1, var totalCount: Int = 0, var pageIsEnd: Boolean = true)

class ApiListDataResponse<T>(var documents: List<T>? = emptyList(), page: Int = 1, totalCount: Int = 0, pageIsEnd: Boolean = true) : PageResultType() {
    init {
        this.page = page
        this.pageIsEnd = pageIsEnd
        this.totalCount = totalCount
    }
}