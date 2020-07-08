package com.lsy.api.place.service.ext

import com.lsy.api.place.dto.PlaceDetail
import com.lsy.common.dto.PageableData

interface PlaceApiService {
    fun searchPlaceByKeyword(keyword: String, page: Int?, size: Int?): PageableData<PlaceDetail>
}