package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.Id
import com.makentoshe.boorusdk.base.model.ParseResult
import com.makentoshe.boorusdk.base.request.*
import com.makentoshe.boorusdk.gelbooru.parser.GelbooruParserXml
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

fun main() {
}

open class GelbooruManager(
    protected val gelbooruApi: GelbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    private val isLoggedIn: Boolean
        get() = cookieStorage.hasCookie("user_id") && cookieStorage.hasCookie("pass_hash")

    fun <R> commentPost(postId: Id, message: String, postAsAnonymous: Boolean, parser: (ByteArray) -> R): R {
        if (!isLoggedIn) throw IllegalStateException("You are not logged in")
        val postHttpPage = getPostHttp(PostHttpRequest.build(postId), ::String)

        val csrfToken = Jsoup.parse(postHttpPage).body().select("#comment_form [name=csrf-token]").attr("value")
        val postAsAnon = if (postAsAnonymous) "on" else null
        val passHash = cookieStorage.getCookie("pass_hash").value
        val userId = cookieStorage.getCookie("user_id").value

        return parser(
            gelbooruApi.commentPost(userId, passHash, postId, message, csrfToken, postAsAnon).execute().body()!!
        )
    }

    fun <R> votePostUp(id: Id, parser: (ByteArray) -> R): R {
        val response = gelbooruApi.votePostUp(id).execute()
        return parser(extractBody(response))
    }

    fun login(user: String, password: String): Boolean {
        val response = gelbooruApi.login(user, password).execute().raw().priorResponse ?: return false
        val loginCookies = response.headers("Set-Cookie")
        return loginCookies.isNotEmpty()
    }

    override fun getPosts(request: PostsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val json = request.type.ordinal
        val response = gelbooruApi.getPosts(request.count, request.page, request.tags, json).execute()
        return parser(extractBody(response))
    }

    override fun getPost(request: PostRequest, parser: (ByteArray) -> ParseResult): ParseResult {
        val response = gelbooruApi.getPost(request.id).execute()
        val xml = String(extractBody(response))
        return GelbooruParserXml().parse(xml)[0]
    }

    override fun getPostHttp(request: PostHttpRequest, parser: (ByteArray) -> String): String {
        val response = gelbooruApi.getPostHttp(request.id).execute()
        return String(extractBody(response))
    }

    override fun getAutocomplete(request: AutocompleteRequest, parser: (ByteArray) -> Array<String>): Array<String> {
        val response = gelbooruApi.autocomplete(request.term).execute()
        return parser(extractBody(response))
    }

    override fun getComment(request: CommentRequest, parser: (ByteArray) -> ParseResult): ParseResult {
        val response = gelbooruApi.comments(request.id).execute()
        return parser(extractBody(response))
    }

    override fun getComments(request: CommentsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = gelbooruApi.comments().execute()
        return parser(extractBody(response))
    }

    override fun getTag(request: TagRequest, parser: (ByteArray) -> ParseResult): ParseResult {
        val response = gelbooruApi.tags(request.id).execute()
        return parser(extractBody(response))
    }

    override fun getTags(request: TagsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = gelbooruApi.tags(request.pattern, request.count, request.order, request.orderby).execute()
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
