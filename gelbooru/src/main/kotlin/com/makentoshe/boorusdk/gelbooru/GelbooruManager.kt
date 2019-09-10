package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.ParseResult
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.*
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

open class GelbooruManager(
    protected val gelbooruApi: GelbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    override fun getPostHttp(request: PostRequest): String {
        val response = gelbooruApi.getPostHttp(request.id).execute()
        return String(extractBody(response))
    }

    private val isLoggedIn: Boolean
        get() = cookieStorage.hasCookie("user_id") && cookieStorage.hasCookie("pass_hash")

    override fun login(request: LoginRequest): Boolean {
        val response = gelbooruApi.login(request.username, request.password).execute()
        val headers = response.raw().priorResponse?.headers ?: throw IllegalStateException("Login API invalid")
        return headers.values("Set-Cookie").run {
            any { it.contains("user_id") } && any { it.contains("pass_hash") }
        }
    }

    override fun commentPost(request: CommentPostRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        // check is logged in
        if (!isLoggedIn) throw IllegalStateException("You are not logged in")
        // get http page (type will be ignored)
        val postHttpPage = getPostHttp(PostRequest.build(request.id.value, Type.XML))
        // extract csrf token
        val csrfToken = Jsoup.parse(postHttpPage).body().select("#comment_form [name=csrf-token]").attr("value")
        val postAsAnon = if (request.postAsAnonymous) "on" else null
        // perform request
        gelbooruApi.commentPost(request.id, request.comment, csrfToken, postAsAnon).execute()
        // get all comments for the post
        return getComments(CommentsRequest.build(request.id.value, request.type), parser)
    }

    override fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int {
        val response = gelbooruApi.votePostUp(request.id).execute()
        return parser(extractBody(response))
    }

    override fun getPosts(request: PostsRequest): String {
        val json = request.type.ordinal
        val response = gelbooruApi.getPosts(request.count, request.page, request.tags, json).execute()
        return String(extractBody(response))
    }

    override fun getPost(request: PostRequest): String {
        val response = gelbooruApi.getPost(
            id = request.id,
            type = request.type.ordinal.toString()
        ).execute()
        // return string from not null byte array
        response.body()?.let {
            return String(it)
        }
        throw Exception(response.message())
    }

    override fun getAutocomplete(request: AutocompleteRequest): String {
        val response = gelbooruApi.autocomplete(request.term).execute()
        return String(extractBody(response))
    }

    override fun getComments(request: CommentsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        val response = if (request.id != null) {
            gelbooruApi.comments(request.id!!, request.type.ordinal).execute()
        } else {
            gelbooruApi.comments().execute()
        }
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

fun main() {
    val manager = GelbooruManager.build()
}
