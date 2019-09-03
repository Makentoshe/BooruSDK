package com.makentoshe.boorusdk.gelbooru.parser

import com.google.gson.Gson
import com.makentoshe.boorusdk.base.ParseResult

open class GelbooruParserJson : GelbooruParser<String>() {

    override fun parse(source: String) = Gson().fromJson(source, Array<Any>::class.java)
        .map { it as Map<*, *> }
        .map { vals -> vals.mapValues { it.value.toString() } }
        .map { keys -> keys.mapKeys { it.key.toString() } }
        .map(::ParseResult)
}
