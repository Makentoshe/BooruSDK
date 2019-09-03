package com.makentoshe.boorusdk.gelbooru.parser

interface Parser<S, R> {
    fun parse(source: S): R
}