package com.lsy.api.member

import com.lsy.api.ApiDocumentationTest
import com.lsy.api.member.domain.Member
import com.lsy.api.member.domain.MemberRepository
import com.lsy.api.member.service.MemberLoginService
import com.lsy.common.component.JwtService
import com.lsy.common.constant.MbrStatusCode
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import com.lsy.common.util.encrypt
import com.lsy.getDocumentResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.context.event.annotation.BeforeTestClass
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class MemberLoginControllerTest: ApiDocumentationTest() {

    @Autowired
    private lateinit var jwtService: JwtService
    @Autowired
    private lateinit var memberRepository: MemberRepository
    @Autowired
    private lateinit var memberLoginService: MemberLoginService

    private val testMemberLoginId = "test1"

    @BeforeTestClass
    fun saveTestMember() {
        memberRepository.save(Member(loginId = testMemberLoginId, password = encrypt(testMemberLoginId), name = testMemberLoginId, statusCd = MbrStatusCode.OK.code, version = "0"))
    }

    @Test
    fun loginWithMemberInfo() {
        mockMvc.perform(post("/member/login").accept(MediaType.APPLICATION_JSON)
                .param("loginId", testMemberLoginId)
                .param("password", testMemberLoginId))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("member-login",
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("loginId").description("아이디"),
                                parameterWithName("password").description("비밀 번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh 토큰"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ))
    }

    @Test
    fun getAccessTokenWithRefreshToken() {
        val sampleRefreshToken = jwtService.issueRefreshToken(testMemberLoginId, "0")
        mockMvc.perform(get("/member/token/access").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $sampleRefreshToken"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("member-token-access",
                        getDocumentResponse(),
                        requestHeaders(headerWithName("Authorization").description("Refresh Token")),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh 토큰").ignored(),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ))
    }
}