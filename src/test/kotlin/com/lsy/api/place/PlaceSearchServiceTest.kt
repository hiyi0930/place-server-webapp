package com.lsy.api.place

import com.lsy.api.place.domain.KeywordSearchCountRepository
import com.lsy.api.place.domain.MemberKeywordHistoryRepository
import com.lsy.api.place.service.PlaceSearchService
import com.lsy.api.place.service.ext.KakaoPlaceApiService
import com.lsy.common.constant.TEST_PROFILE
import com.lsy.config.HttpConnectionConfig
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PlaceSearchService::class, HttpConnectionConfig::class, KakaoPlaceApiService::class])
class PlaceSearchServiceTest(@Autowired private val placeSearchService: PlaceSearchService) {

    @MockkBean
    private lateinit var memberKeywordHistoryRepository: MemberKeywordHistoryRepository
    @MockkBean
    private lateinit var keywordSearchCountRepository: KeywordSearchCountRepository

    @Test
    fun canSearchPlaceWithOverflowPageAndSize() {
        val size = Integer.MAX_VALUE
        val page = Integer.MAX_VALUE
        val loginId = "TEST_USER"
        val keyword = "서울"

        val response = placeSearchService.getPlaceInfoByKeyword(keyword, page, size, loginId)
        assertTrue(response.totalCount > 0)
    }

    @Test
    fun preventAddKeywordCountByOnePersonForHour() {

    }
}