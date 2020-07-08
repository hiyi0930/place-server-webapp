package com.lsy.common

import com.lsy.common.component.JwtService
import com.lsy.common.constant.TEST_PROFILE
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JwtService::class])
class JwtServiceTest(private val jwtService: JwtService) {
    val loginId = "test1"


    @Test
    fun parseIssuedTokenAndMatchSubjectValue() {
        val accessToken = jwtService.issueAccessToken(loginId)
        val refreshToken = jwtService.issueRefreshToken(loginId, "12341234")

        assertThat(jwtService.getLoginIdFromToken(accessToken), equalTo(loginId))
        assertThat(jwtService.getLoginIdFromToken(refreshToken), equalTo(loginId))
    }
    @Test
    fun invalidTokenParseReturnNull() {
        val accessToken = jwtService.issueAccessToken(loginId)
        val refreshToken = jwtService.issueRefreshToken(loginId, "12341234")

        assertThat(null, equalTo(jwtService.getLoginIdFromToken("${accessToken}inValid")))
        assertThat(null, equalTo(jwtService.getLoginIdFromToken("${refreshToken}inValid")))
    }
    @Test
    fun expiredTokenParseReturnEmptyString() {
        val accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsc3kiLCJzdWIiOiJhY2Nlc3MiLCJpYXQiOjE1OTQyMDUxOTUsImV4cCI6MTU5NDIwMzM5NSwibG9naW5JZCI6InRlc3QxIn0.5oJyd7Xo-Uf4VB6cxYeV9nCFEIGgE9yy6X1tUZVtES8"
        val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsc3kiLCJzdWIiOiJyZWZyZXNoIiwiaWF0IjoxNTk0MTM1ODQxLCJleHAiOjE1OTQxNDY2NDEsImxvZ2luSWQiOiJ0ZXN0MSIsInZlcnNpb24iOiIwIn0.Ygss0ki-v00tsY9wG94FzlgvBISmwdVrHE96lDy-PhU"

        assertThat("", equalTo(jwtService.getLoginIdFromToken(accessToken)))
        assertThat("", equalTo(jwtService.getLoginIdFromToken(refreshToken)))
    }
}