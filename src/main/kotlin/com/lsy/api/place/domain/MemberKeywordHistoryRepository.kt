package com.lsy.api.place.domain

import org.springframework.data.jpa.repository.JpaRepository


interface MemberKeywordHistoryRepository : JpaRepository<MemberKeywordHistory, Long> {
    fun findByLoginIdAndKeywordAndSearchDate(loginId: String, keyword: String, searchDate: String): MemberKeywordHistory?
}