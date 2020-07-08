package com.lsy.api.member.controller

import com.lsy.api.member.dto.MemberToken
import com.lsy.api.member.service.MemberLoginService
import com.lsy.common.constant.ResultCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@Controller
class MemberLoginController(private val memberLoginService: MemberLoginService) {

    @PostMapping("/member/login")
    fun login(request: HttpServletRequest, loginId: String?, password: String?): ResponseEntity<MemberToken> {
        if (loginId == null || password == null) {
            val response = MemberToken()
            response.resultMessage = ResultCode.INVALID.message
            return ResponseEntity.badRequest().body(response)
        }

        val response = memberLoginService.getLoginMemberToken(loginId, password)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/member/token/access")
    fun getAccessToken(request: HttpServletRequest): ResponseEntity<MemberToken> {
        val loginId = request.getAttribute("loginId").toString()
        val response = memberLoginService.getAccessToken(loginId)
        return ResponseEntity.ok().body(response)
    }
}