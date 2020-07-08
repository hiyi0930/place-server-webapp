package com.lsy.api.place.service

import com.lsy.api.place.domain.KeywordSearchCount
import com.lsy.api.place.domain.KeywordSearchCountRepository
import com.lsy.api.place.domain.MemberKeywordHistory
import com.lsy.api.place.domain.MemberKeywordHistoryRepository
import com.lsy.api.place.dto.KeywordSearchCountDto
import com.lsy.api.place.dto.PlaceDetail
import com.lsy.api.place.service.ext.KakaoPlaceApiService
import com.lsy.common.dto.PageableData
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PlaceSearchService(
        private val placeApiService: KakaoPlaceApiService,
        private val memberKeywordHistoryRepository: MemberKeywordHistoryRepository,
        private val keywordSearchCountRepository: KeywordSearchCountRepository) {

    fun getPlaceInfoByKeyword(keyword: String, page: Int?, size: Int?, loginId: String): PageableData<PlaceDetail> {
        val response = placeApiService.searchPlaceByKeyword(keyword, page, size)
        if (page == null || page != 1 || response.totalCount == 0) { return response }

        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"))
        val keywordSearchHist = memberKeywordHistoryRepository.findByLoginIdAndKeywordAndSearchDate(loginId, keyword, currentDate)
        if (keywordSearchHist == null) {
            saveKeywordSearchHist(loginId, keyword, currentDate)
        }
        return response
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    fun saveKeywordSearchHist(loginId: String, keyword: String, searchDate: String) {
        memberKeywordHistoryRepository.save(MemberKeywordHistory(loginId = loginId, keyword = keyword, searchDate = searchDate))
        val keywordSearchCount = keywordSearchCountRepository.findByKeyword(keyword)?: KeywordSearchCount(keyword = keyword, count = 0)
        keywordSearchCount.count += 1
        keywordSearchCountRepository.save(keywordSearchCount)
    }

    @Cacheable(value = ["topKeywordCache"], unless = "#result.size() < 10")
    @Transactional(readOnly = true)
    fun getTopSearchKeyword(): List<KeywordSearchCountDto>? {
        return keywordSearchCountRepository.findTop10ByOrderByCountDesc()?.map { KeywordSearchCountDto(keyword = it.keyword, count = it.count) }
    }
}