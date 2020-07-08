package com.lsy.common.component.handler

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestClientException
import java.io.IOException

@Component
class KakaoResponseErrorHandler: ResponseErrorHandler {
    private val logger = KotlinLogging.logger {}

    @Throws(IOException::class)
    override fun hasError(httpResponse: ClientHttpResponse): Boolean {
        return (httpResponse.statusCode.series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.statusCode.series() == HttpStatus.Series.SERVER_ERROR)
    }

    @Throws(IOException::class)
    override fun handleError(httpResponse: ClientHttpResponse) {
        if (httpResponse.statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            logger.error("[ERROR][KAKAO] API SERVER ERROR ${httpResponse.statusCode}")

        }
        if (httpResponse.statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            logger.error("[ERROR][KAKAO] API CLIENT ERROR ${httpResponse.statusCode} ${httpResponse.headers} ${httpResponse.body.readAllBytes()}")
        }
        throw RestClientException(httpResponse.statusCode.name)
    }
}