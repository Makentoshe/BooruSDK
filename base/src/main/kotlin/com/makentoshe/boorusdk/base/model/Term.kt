package com.makentoshe.boorusdk.base.model

data class Term(val value: String) {

    override fun toString() = value.split(" ").lastOrNull { it.isNotBlank() }
        ?: throw IllegalArgumentException("Value \"$value\" should not be blank")
}