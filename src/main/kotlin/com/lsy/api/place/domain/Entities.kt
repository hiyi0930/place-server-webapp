package com.lsy.api.place.domain

import com.lsy.common.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class MemberKeywordHistory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var loginId: String,
        var keyword: String,
        var searchDate: String
): BaseEntity()

@Entity
class KeywordSearchCount(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var keyword: String = "",
        var count: Int = 0
): BaseEntity()