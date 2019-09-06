package com.makentoshe.boorusdk.base.model

data class Count(val value: Int) {

    init {
        if (value < 0) throw IllegalArgumentException("Value $value is less than 0")
    }

    override fun toString(): String {
        return value.toString()
    }
}