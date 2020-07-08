package com.lsy.api.place.domain

import org.springframework.data.jpa.repository.JpaRepository


interface KeywordSearchCountRepository : JpaRepository<KeywordSearchCount, Long> {
    fun findByKeyword(keyword: String): KeywordSearchCount?
    fun findTop10ByOrderByCountDesc(): List<KeywordSearchCount>?
}