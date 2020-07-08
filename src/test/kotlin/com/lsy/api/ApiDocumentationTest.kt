package com.lsy.api

import com.lsy.common.constant.TEST_PROFILE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles(TEST_PROFILE)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
abstract class ApiDocumentationTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc
}