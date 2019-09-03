package com.makentoshe.boorusdk.gelbooru.parser

import com.makentoshe.boorusdk.base.ParseResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

open class GelbooruParserXml : GelbooruParser<String>() {

    override fun parse(source: String): List<ParseResult> {
        val root = Jsoup.parse(source).body().child(0)
        return root.childNodes().filter(::isTextNode).map(::nodeToAttributesMap).map(::ParseResult)
    }

    private fun isTextNode(node: Node): Boolean {
        return node !is TextNode
    }

    private fun nodeToAttributesMap(node: Node): Map<String, String> {
        return node.attributes().associate { Pair(it.key, it.value) }
    }
}