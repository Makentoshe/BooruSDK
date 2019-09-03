package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.*
import com.makentoshe.boorusdk.gelbooru.parser.GelbooruParserXml
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

@ExperimentalStdlibApi
fun main() {
    val manager = GelbooruManager.build()

}

open class GelbooruManager(protected val gelbooruApi: GelbooruApi) : BooruManager {

    fun votePostUp(id: Id, parser: (ByteArray) -> Int): Int{
        val response = gelbooruApi.votePostUp(id).execute()
        return parser(extractBody(response))
    }

    fun login(user: String, password: String): Boolean {
        val response = gelbooruApi.login(user, password).execute().raw().priorResponse ?: return false
        val loginCookies = response.headers("Set-Cookie")
        return loginCookies.isNotEmpty()
    }

    override fun posts(
        count: Int, page: Page, tags: Tags, parser: (ByteArray) -> List<ParseResult>
    ): List<ParseResult> {
        val response = gelbooruApi.posts(count, page, tags).execute()
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
