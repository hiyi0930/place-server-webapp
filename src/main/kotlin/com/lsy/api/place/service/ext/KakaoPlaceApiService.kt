package com.lsy.api.place.service.ext

import com.lsy.api.place.dto.KakaoPlaceApiResultDto
import com.lsy.api.place.dto.PlaceDetail
import com.lsy.common.constant.*
import com.lsy.common.dto.PageableData
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder
import javax.annotation.PostConstruct

@Service
class KakaoPlaceApiService(private val restTemplate: RestTemplate): PlaceApiService {
    private val logger = KotlinLogging.logger {}
    private val kakaoHostUrl = "dapi.kakao.com"
    private val headers = HttpHeaders()

    @Value("\${kakao.api.key}")
    private val kakaoApiKey: String? = null

    @PostConstruct
    fun initKakaoHeaders() {
        headers.connection = listOf("keep-alive")
        headers.set("Authorization", "KakaoAK $kakaoApiKey")
    }

    @Cacheable(value = ["searchByKeywordCache"], unless = "#result.data.size() == 0")
    override fun searchPlaceByKeyword(keyword: String, page: Int?, size: Int?): PageableData<PlaceDetail> {
        val keywordSafe = if (keyword.length > PLACE_API_MAX_KEYWORD_LENGTH) { keyword.substring(0, PLACE_API_MAX_KEYWORD_LENGTH) } else { keyword }
        val pageSafe = if (page == null || page < PLACE_API_MAX_PAGE) { page?: 1 } else { PLACE_API_MAX_PAGE }
        val sizeSafe = if (size == null || size < PLACE_API_MAX_SIZE) { size?: PLACE_API_MAX_SIZE } else { PLACE_API_MAX_SIZE }

        val builder = UriComponentsBuilder.fromHttpUrl("https://${kakaoHostUrl}/v2/local/search/keyword.json")
                .queryParam("query", keywordSafe)
                .queryParam("page", pageSafe)
                .queryParam("size", sizeSafe)
                .build()

        return try{
            val response: ResponseEntity<KakaoPlaceApiResultDto> = restTemplate.exchange(builder.toUri(), HttpMethod.GET, HttpEntity<KakaoPlaceApiResultDto>(headers))
            convertToPageableData(response.body, pageSafe)
        } catch (ex: Exception) {
            logger.error(builder.toUri().toString())
            logger.error(headers.toString())
            logger.error(ex.toString())
            throw ex
        }

    }

    private fun convertToPageableData(resultDto: KakaoPlaceApiResultDto?, page: Int): PageableData<PlaceDetail> {
        if (resultDto == null) { return PageableData() }

        val documents = resultDto.documents?.map {
            PlaceDetail(
                placeId = it.id,
                placeName = it.place_name,
                phone = it.phone,
                addressName = it.address_name,
                roadAddressName = it.road_address_name,
                placeUrl = it.place_url
        )} ?: emptyList()

        return PageableData(data = documents, page = page, totalCount = resultDto.meta?.pageable_count?: 0, pageIsEnd = resultDto.meta?.is_end?: false)
    }
}