package com.lsy.common.component

import com.lsy.common.constant.JwtTokenType
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct

@Component
class JwtService {

    private val logger = KotlinLogging.logger {}

    @Value("\${spring.jwt.key}")
    private lateinit var jwtStringKey: String

    private var jwtKey: Key? = null

    @PostConstruct
    fun initJwtKey() {
        jwtKey = Keys.hmacShaKeyFor(jwtStringKey.toByteArray())
    }

    fun issueAccessToken(loginId: String): String {
        val calendar: Calendar = Calendar.getInstance()
        val now = Date()
        calendar.time = now
        calendar.add(Calendar.MINUTE, 20)
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("lsy")
                .setSubject(JwtTokenType.ACCESS_TOKEN.type)
                .setIssuedAt(now)
                .setExpiration(calendar.time)
                .signWith(jwtKey)
                .claim("loginId", loginId)
                .compact()
    }

    fun issueRefreshToken(loginId: String, version: String): String {
        val calendar: Calendar = Calendar.getInstance()
        val now = Date()
        calendar.time = now
        calendar.add(Calendar.HOUR, 3)
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("lsy")
                .setSubject(JwtTokenType.REFRESH_TOKEN.type)
                .setIssuedAt(now)
                .setExpiration(calendar.time)
                .signWith(jwtKey)
                .claim("loginId", loginId)
                .claim("version", version)
                .compact()
    }

    fun getLoginIdFromToken(jwtToken: String): String? = getJwsClaimValue(jwtToken, "loginId")
    fun getTokenSubject(jwtToken: String): String? = getJwsClaimValue(jwtToken, "sub")
    fun getTokenVersion(jwtToken: String): String? = getJwsClaimValue(jwtToken, "version")

    private fun getJwsClaimValue(jwtToken: String, name: String): String? {
        return try {
            val jws: Jws<Claims> = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwtToken)
            jws.body[name].toString()
        } catch (ex: ExpiredJwtException) {
            ""
        } catch (ex: JwtException) {
            logger.error("[JWT PARSE ERROR][$name] $ex -> ($jwtToken)")
            null
        }
    }

}