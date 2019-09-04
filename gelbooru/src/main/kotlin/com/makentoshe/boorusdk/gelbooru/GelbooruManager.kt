package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.*
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

@ExperimentalStdlibApi
fun main() {
    val manager = GelbooruManager.build()
    manager.login("Makentoshe", "1243568790").also { loginManager ->
        if (loginManager == null) return@also
        val result = loginManager.commentPost(Id(4901318), "Cute girls makes a cute things", true)
    }
}

open class GelbooruManager(
    protected val gelbooruApi: GelbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    fun votePostUp(id: Id, parser: (ByteArray) -> Int): Int {
        val response = gelbooruApi.votePostUp(id).execute()
        return parser(extractBody(response))
    }

    fun login(user: String, password: String): GelbooruManagerLogin? {
        val response = gelbooruApi.login(user, password).execute().raw().priorResponse ?: return null
        val loginCookies = response.headers("Set-Cookie")
        return if (loginCookies.isEmpty()) null else GelbooruManagerLogin(gelbooruApi, cookieStorage)
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

    fun getPostHttp(id: Id, parser: (ByteArray) -> String): String {
        val response = gelbooruApi.getPostHttp(id).execute()
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
            val cookieJar = SessionCookie()
            val client = OkHttpClient.Builder().cookieJar(cookieJar).build()
            val retrofit = Retrofit.Builder().client(client).baseUrl("https://gelbooru.com")
                .addConverterFactory(ByteArrayConverterFactory()).build()
            return GelbooruManager(retrofit.create(GelbooruApi::class.java), cookieJar)
        }
    }
}

class GelbooruManagerLogin(
    protected val gelbooruApi: GelbooruApi,
    protected val cookieStorage: CookieStorage
) {
    fun commentPost(postId: Id, text: String, postAsAnon: Boolean) {
        if (text.split(" ").count() < 3) throw IllegalArgumentException("Text should be more than 3 words")
        val http = gelbooruApi.getPostHttp(postId).execute().body().let { String(it ?: byteArrayOf()) }
        val csrfToken = Jsoup.parse(http).body().select("#comment_form [name=csrf-token]").attr("value")
        val phpsessid = cookieStorage.getCookie("PHPSESSID").value
        val username = cookieStorage.getCookie("user_id").value
        val passHash = cookieStorage.getCookie("pass_hash").value
        val anon = if (postAsAnon) "on" else null
        val result = gelbooruApi.commentPost(postId, text, csrfToken, phpsessid, username, passHash, anon).execute().raw().priorResponse
        println(String(result!!.body!!.bytes()))
    }
}
