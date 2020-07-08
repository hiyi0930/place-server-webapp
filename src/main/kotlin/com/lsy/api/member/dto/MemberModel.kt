package com.lsy.api.member.dto

import com.lsy.common.dto.ResultType

data class MemberToken(
        var accessToken: String = "",
        var refreshToken: String = ""
): ResultType()