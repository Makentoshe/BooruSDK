package parser

import com.makentoshe.boorusdk.base.model.ParseResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal interface Parser<S, R> {
    fun parse(source: S): R
}

internal class DanbooruPostsParserXml: Parser<String, List<ParseResult>> {

    override fun parse(source: String): List<ParseResult> {
       return Jsoup.parse(source).body().select("post").map(::parsePost).map(::ParseResult)
    }

    private fun parsePost(element: Element): Map<String, String> {
        return element.children().map { Pair(it.tagName(), it.text()) }.toMap()
    }
}