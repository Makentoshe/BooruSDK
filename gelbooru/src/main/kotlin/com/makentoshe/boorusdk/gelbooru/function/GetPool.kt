package com.makentoshe.boorusdk.gelbooru.function

import com.google.gson.Gson
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.pool.GetPoolRequest
import com.makentoshe.boorusdk.gelbooru.GelbooruApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.w3c.dom.Document
import java.io.ByteArrayOutputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

internal class GetPool(
    private val gelbooruApi: GelbooruApi
) : Function<GetPoolRequest, String>() {

    override fun apply(t: GetPoolRequest): String {
        val response = gelbooruApi.getPool(t.poolId).execute().extractBody()
        return when (t.type) {
            Type.XML -> html2xml(response, t)
            Type.JSON -> html2json(response, t)
            else -> response
        }
    }

    private fun html2token(html: String, request: GetPoolRequest): Map<String, Any> {
        val content = Jsoup.parse(html).select("#content")
        val id = request.poolId.toString()
        val title = content.select("h3").text().split(": ").last()
        val description = content.select("div").first { it.id().isEmpty() }.textNodes().first().text()
        val postsIds = content.select(".thumb").map(::element2token)
        return mapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "post_ids" to postsIds
        )
    }

    private fun element2token(element: Element): Int {
        return element.id().substring(1).toInt()
    }

    private fun html2xml(html: String, request: GetPoolRequest): String {
        val token = html2token(html, request)
        return XmlBuilder().build(token)
    }

    private fun html2json(html: String, request: GetPoolRequest): String {
        val token = html2token(html, request)
        return JsonBuilder().build(token)
    }

    private class XmlBuilder {

        fun build(token: Map<String, Any>): String {
            val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val document = buildDocument(builder, token)
            return buildXmlString(document)
        }

        private fun buildDocument(builder: DocumentBuilder, token: Map<String, Any>): Document {
            val document = builder.newDocument()
            val element = document.createElement("pool")
            // add attributes
            token.forEach { (t, u) ->
                if (u is List<*>) {
                    element.setAttribute(t, u.joinToString(" "))
                } else {
                    element.setAttribute(t, u.toString())
                }
                document.appendChild(element)
            }
            return document
        }

        private fun buildXmlString(doc: Document): String {
            val transformer = TransformerFactory.newInstance().newTransformer()
            val source = DOMSource(doc)
            val outputStream = ByteArrayOutputStream()
            val stream = StreamResult(outputStream)
            transformer.transform(source, stream)
            return String(outputStream.toByteArray())
        }
    }

    private class JsonBuilder {
        fun build(token: Map<String, Any>): String {
            return Gson().toJson(token)
        }
    }
}
