package com.lsy.api.place.controller

import com.lsy.api.place.dto.KeywordSearchCountDto
import com.lsy.api.place.dto.PlaceDetail
import com.lsy.api.place.service.PlaceSearchService
import com.lsy.common.dto.ApiListDataResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import javax.servlet.http.HttpServletRequest

@RestController
class PlaceSearchController(private val placeSearchService: PlaceSearchService) {

    @GetMapping("/place/keyword")
    fun getPlaceInfoByKeyword(request: HttpServletRequest, keyword: String?, page: Int?, size: Int?): ResponseEntity<ApiListDataResponse<PlaceDetail>> {
        if (keyword == null || keyword.isBlank()) { return ResponseEntity.badRequest().body(ApiListDataResponse()) }

        return try {
            val placeDetails = placeSearchService.getPlaceInfoByKeyword(keyword, page, size, request.getAttribute("loginId").toString())
            ResponseEntity.ok().body(ApiListDataResponse(documents = placeDetails.data, page = placeDetails.page, pageIsEnd = placeDetails.pageIsEnd, totalCount = placeDetails.totalCount))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }

    }

    @GetMapping("/place/keyword/top")
    fun getTopSearchKeyword(): ResponseEntity<ApiListDataResponse<KeywordSearchCountDto>> {
        val topSearchKeywords = placeSearchService.getTopSearchKeyword()

        return ResponseEntity.ok().body(ApiListDataResponse(documents = topSearchKeywords, totalCount = topSearchKeywords?.size?: 0))
    }
}