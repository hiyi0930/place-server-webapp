package com.lsy.api.member.service

import com.lsy.api.member.domain.Member
import com.lsy.api.member.domain.MemberRepository
import com.lsy.api.member.dto.MemberToken
import com.lsy.common.constant.ResultCode
import com.lsy.common.component.JwtService
import com.lsy.common.constant.MbrStatusCode
import com.lsy.common.util.matches
import org.springframework.stereotype.Service

@Service
class MemberLoginService(private val memberRepository: MemberRepository, private val jwtUtilService: JwtService) {

    fun getLoginMemberToken(loginId: String, password: String): MemberToken {
        if (loginId.isBlank() || password.isBlank()) { return throwInvalidMemberToken() }

        val member = memberRepository.findByLoginId(loginId)?: return throwInvalidMemberToken()
        if (member.password.isBlank() || !matches(password, member.password)) { return throwInvalidMemberToken() }
        if (member.statusCd != MbrStatusCode.OK.code) { return MemberToken() }

        return generateLoginToken(member)
    }

    fun getMemberInfo(loginId: String): Member? {
        if (loginId.isBlank()) { return null }

        return memberRepository.findByLoginId(loginId)
    }

    fun getAccessToken(loginId: String): MemberToken {
        val memberTokenResponse = MemberToken()
        val memberInfo: Member? = getMemberInfo(loginId)
        if (memberInfo != null) {
            memberTokenResponse.accessToken = jwtUtilService.issueAccessToken(loginId)
        }

        return memberTokenResponse
    }

    private fun throwInvalidMemberToken(): MemberToken {
        val response = MemberToken()
        response.resultMessage = ResultCode.INVALID.message
        return response
    }
    private fun generateLoginToken(member: Member): MemberToken {
        return MemberToken(accessToken = jwtUtilService.issueAccessToken(member.loginId), refreshToken = jwtUtilService.issueRefreshToken(member.loginId, member.version?: "0"))
    }
}