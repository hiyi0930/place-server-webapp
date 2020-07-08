package com.lsy.api.place

import com.lsy.api.ApiDocumentationTest
import com.lsy.api.member.service.MemberLoginService
import com.lsy.api.place.domain.KeywordSearchCount
import com.lsy.api.place.domain.KeywordSearchCountRepository
import com.lsy.api.place.service.PlaceSearchService
import com.lsy.common.component.JwtService
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import com.lsy.getDocumentResponse
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class PlaceSearchControllerTest: ApiDocumentationTest() {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var placeSearchService: PlaceSearchService
    @Autowired
    private lateinit var keywordSearchCountRepository: KeywordSearchCountRepository
    @MockkBean
    private lateinit var memberLoginService: MemberLoginService

    private var sampleAccessToken: String = ""
    @BeforeEach
    fun setAccessTokenForTest() {
        sampleAccessToken = jwtService.issueAccessToken("test1")
    }

    @Test
    fun searchPlaceApiUnauthorizedWithInvalidToken() {
        mockMvc.perform(get("/place/keyword").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${sampleAccessToken}invalid")
                .param("keyword", "서울")
                .param("page", "1")
                .param("size", "15"))
                .andExpect(status().isUnauthorized)
    }
    @Test
    fun searchPlaceApi() {
        mockMvc.perform(get("/place/keyword").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $sampleAccessToken")
                .param("keyword", "서울")
                .param("page", "1")
                .param("size", "1"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("place-search",
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("Access Token")),
                        requestParameters(
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 검색 수"),
                                fieldWithPath("pageIsEnd").type(JsonFieldType.BOOLEAN).description("페이지 종료 여부"),
                                fieldWithPath("documents").type(JsonFieldType.ARRAY).description("장소 목록"),
                                fieldWithPath("documents[0].placeId").type(JsonFieldType.STRING).description("장소 ID"),
                                fieldWithPath("documents[0].placeName").type(JsonFieldType.STRING).description("장소명"),
                                fieldWithPath("documents[0].phone").type(JsonFieldType.STRING).description("전화 번호"),
                                fieldWithPath("documents[0].addressName").type(JsonFieldType.STRING).description("지번 주소"),
                                fieldWithPath("documents[0].roadAddressName").type(JsonFieldType.STRING).description("도로명 주소"),
                                fieldWithPath("documents[0].placeUrl").type(JsonFieldType.STRING).description("장소 상세 URL")
                        )
                ))
    }

    @Test
    fun searchTopKeywordApi() {
        keywordSearchCountRepository.save(KeywordSearchCount(keyword = "서울"))

        mockMvc.perform(get("/place/keyword/top").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $sampleAccessToken"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("top-keyword",
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("Access Token")),
                        responseFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 검색 수"),
                                fieldWithPath("pageIsEnd").type(JsonFieldType.BOOLEAN).description("페이지 종료 여부"),
                                fieldWithPath("documents").type(JsonFieldType.ARRAY).description("검색어 목록"),
                                fieldWithPath("documents[0].keyword").type(JsonFieldType.STRING).description("검색어"),
                                fieldWithPath("documents[0].count").type(JsonFieldType.NUMBER).description("검색 수")
                        )
                ))
    }
}