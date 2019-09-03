package com.makentoshe.boorusdk.base

class Tags(val value: Set<String> = setOf()) {
    override fun toString(): String {
        return value.joinToString(separator = " ")
    }
}