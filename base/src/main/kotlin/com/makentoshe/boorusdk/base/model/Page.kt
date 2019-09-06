package com.makentoshe.boorusdk.base.model

data class Page(val value: Int) {

    init {
        if (value < 0) throw IllegalArgumentException("Value $value should not be less than 0")
    }

    override fun toString(): String {
        return value.toString()
    }
}

