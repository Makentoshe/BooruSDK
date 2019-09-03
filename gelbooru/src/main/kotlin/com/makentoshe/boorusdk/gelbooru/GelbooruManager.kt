package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.*
import com.makentoshe.boorusdk.gelbooru.parser.GelbooruParserXml
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

fun main() {
    val manager = GelbooruManager.build()
//    manager.login("Makentoshe", "1243568790")
    val a = manager.posts(10, Page(1), Tags()) {
        GelbooruParserXml().parse(String(it))
    }

    println(a)
}

open class GelbooruManager(protected val gelbooruApi: GelbooruApi) : BooruManager {

    fun login(user: String, password: String): Boolean {
        val response = gelbooruApi.login(user, password).execute().raw().priorResponse ?: return false
        val loginCookies = response.headers("Set-Cookie")
        return loginCookies.isNotEmpty()
    }

    override fun posts(
        count: Int, page: Page, tags: Tags, parser: (ByteArray) -> List<ParseResult>
    ): List<ParseResult> {
        val query = mapOf(
            "page" to "dapi",
            "s" to "post",
            "q" to "index",
            "limit" to "$count",
            "pid" to "$page",
            "tags" to "$tags"
        )
        val response = gelbooruApi.custom(query).execute()
        return parser(extractBody(response))
    }

    override fun posts(id: Id, parser: (ByteArray) -> ParseResult): ParseResult {
        val response = gelbooruApi.posts(id).execute()
        return parser(extractBody(response))
    }

    override fun search(term: String, parser: (ByteArray) -> Array<String>): Array<String> {
        val response = gelbooruApi.autocomplete(term).execute()
        return parser(extractBody(response))
    }

    override fun comments(postId: Id, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = gelbooruApi.comments(postId).execute()
        return parser(extractBody(response))
    }

    override fun comments(parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = gelbooruApi.comments().execute()
        return parser(extractBody(response))
    }

    override fun tags(id: Id, parser: (ByteArray) -> ParseResult): ParseResult {
        val response = gelbooruApi.tags(id).execute()
        return parser(extractBody(response))
    }

    override fun tags(pattern: String, limit: Int, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = gelbooruApi.tags(pattern, limit).execute()
        return parser(extractBody(response))
    }

    override fun tags(
        pattern: String, limit: Int, order: Order, orderby: Orderby, parser: (ByteArray) -> List<ParseResult>
    ): List<ParseResult> {
        val response = gelbooruApi.tags(pattern, limit, order, orderby).execute()
        return parser(extractBody(response))
    }

    private fun extractBody(response: Response<ByteArray>): ByteArray {
        return if (response.isSuccessful) {
            response.body() ?: byteArrayOf()
        } else {
            throw Exception(response.message())
        }
    }

    companion object {
        fun build(): GelbooruManager {
            val client = buildClient()
            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("https://gelbooru.com")
                .addConverterFactory(ByteArrayConverterFactory())
                .build()
            return GelbooruManager(retrofit.create(GelbooruApi::class.java))
        }

        private fun buildClient(): OkHttpClient {
            val cookieJar = SessionCookieJar()
            return OkHttpClient.Builder().cookieJar(cookieJar).build()
        }
    }
}
