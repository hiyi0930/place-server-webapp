package com.lsy.auth

import com.lsy.api.member.domain.Member
import com.lsy.api.member.service.MemberLoginService
import com.lsy.common.constant.JwtTokenType
import com.lsy.common.constant.MbrStatusCode
import com.lsy.common.component.JwtService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtCertificationInterceptor(private val memberLoginService: MemberLoginService, private val jwtUtilService: JwtService) : HandlerInterceptor {
    private val logger = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val header: String = request.getHeader("Authorization") ?: ""
        val requestIp = request
        if (header.isBlank()) return throwUnAuthorizedError(response)

        if (!header.startsWith("Bearer ")) {
            logger.error("[Invalid Header][${requestIp}] $header")
            return throwUnAuthorizedError(response)
        }

        val jwtToken: String = header.substring(7)
        val loginId: String = jwtUtilService.getLoginIdFromToken(jwtToken) ?: return throwUnAuthorizedError(response)

        if (loginId.isBlank()) {
            response.writer.write("EXPIRED")
            return throwUnAuthorizedError(response)
        }

        val jwtTokenType = if (isIssueNewAccessToken(request)) JwtTokenType.REFRESH_TOKEN.type else JwtTokenType.ACCESS_TOKEN.type
        if (jwtUtilService.getTokenSubject(jwtToken) != jwtTokenType) {
            logger.error("[Not Valid Token Type][${requestIp}] $header")
            return throwUnAuthorizedError(response)
        }
        if (!isIssueNewAccessToken(request)) {
            request.setAttribute("loginId", loginId)
            return super.preHandle(request, response, handler)
        }

        val member: Member? = memberLoginService.getMemberInfo(loginId)
        if (member == null || member.statusCd != MbrStatusCode.OK.code) {
            logger.error("[Invalid Member][${requestIp}] $loginId - $header")
            return throwUnAuthorizedError(response)
        }

        val tokenVersion: String = jwtUtilService.getTokenVersion(jwtToken) ?: ""
        if (member.version != tokenVersion) {
            logger.error("[Invalid RefreshToken Version][${requestIp}] $loginId - $header")
            return throwUnAuthorizedError(response)
        }

        request.setAttribute("loginId", loginId)
        return super.preHandle(request, response, handler)
    }
    private fun throwUnAuthorizedError(response: HttpServletResponse): Boolean {
        response.status = HttpStatus.UNAUTHORIZED.value()
        return false
    }
    private fun isIssueNewAccessToken(request: HttpServletRequest) =
            request.requestURI.contains("/member/token/access")
}