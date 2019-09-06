package com.makentoshe.boorusdk.base.model

data class Tags(val value: Set<String> = setOf()) {
    override fun toString(): String {
        return value.joinToString(separator = " ")
    }
}