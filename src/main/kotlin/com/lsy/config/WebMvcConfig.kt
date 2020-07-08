package com.lsy.config

import com.lsy.auth.JwtCertificationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    lateinit var jwtCertificationInterceptor: JwtCertificationInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtCertificationInterceptor)
                .excludePathPatterns("/member/login")
                .addPathPatterns("/**")
    }
}