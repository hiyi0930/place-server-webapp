package com.lsy.common

import com.lsy.common.constant.TEST_PROFILE
import com.lsy.common.util.encrypt
import com.lsy.common.util.matches
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TEST_PROFILE)
class PasswordUtilsTest {
    @Test
    fun matchPasswordWithEncryptedPassword() {
        val password = "test1"
        val encryptPassword = encrypt(password)
        assertThat(matches(password, encryptPassword), equalTo(true))
        assertThat(matches("${password}1", encryptPassword), equalTo(false))
    }
}