package com.lsy.api.member.service

import com.lsy.api.member.domain.Member
import com.lsy.api.member.domain.MemberRepository
import com.lsy.common.constant.MbrStatusCode
import com.lsy.common.util.encrypt
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class MemberInitDataService(private val memberRepository: MemberRepository) {

    @PostConstruct
    fun initUser() {
        for (i in 1..10) {
            memberRepository.save(Member(loginId = "test$i", password = encrypt("test$i"), name = "테스트$i", statusCd = MbrStatusCode.OK.code, version = "0"))
        }
    }
}