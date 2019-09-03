package com.makentoshe.boorusdk.base

enum class Orderby {
    DATE, COUNT, NAME;

    override fun toString() = name.toLowerCase()
}