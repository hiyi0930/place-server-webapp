package com.lsy.api.member.domain

import com.lsy.common.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Member(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var name: String = "",
        var password: String = "",
        var statusCd: String = "",
        var version: String? = "",
        var loginId: String = ""
): BaseEntity()