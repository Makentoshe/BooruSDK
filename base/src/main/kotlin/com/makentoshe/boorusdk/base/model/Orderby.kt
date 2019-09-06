package com.makentoshe.boorusdk.base.model

enum class Orderby {
    DATE, COUNT, NAME;

    override fun toString() = name.toLowerCase()
}