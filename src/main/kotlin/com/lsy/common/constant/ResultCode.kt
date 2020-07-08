package com.lsy.common.constant

enum class ResultCode(val code: String, val message: String) {
    OK("OK", ""),
    INVALID("INVALID", "입력한 정보를 다시 확인해 주세요."),
    FAIL("FAIL", "잠시 후 다시 시도해 주세요.");
}