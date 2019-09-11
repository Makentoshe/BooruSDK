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

    override fun getPostHttp(request: PostsRequest): String {
        val postId = request.id ?: throw IllegalArgumentException("Request should contain id value")
        val response = gelbooruApi.getPostHttp(postId).execute()
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
        val postHttpPage = getPostHttp(PostsRequest.build(type = Type.XML, id = request.id.value))
        // extract csrf token
        val csrfToken = Jsoup.parse(postHttpPage).body().select("#comment_form [name=csrf-token]").attr("value")
        val postAsAnon = if (request.postAsAnonymous) "on" else null
        // perform request
        gelbooruApi.commentPost(request.id, request.comment, csrfToken, postAsAnon).execute()
        // get all comments for the post
        return emptyList()//getComments(CommentsRequest.build(request.id.value, request.type), parser)
    }

    override fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int {
        val response = gelbooruApi.votePostUp(request.id).execute()
        return parser(extractBody(response))
    }

    override fun getPosts(request: PostsRequest): String {
        val type = if (request.type == Type.JSON) 1 else null
        val response = gelbooruApi.getPosts(
            type = type,
            id = request.id,
            count = request.count,
            page = request.page,
            tags = request.tags
        ).execute()
        return String(extractBody(response))
    }

    override fun getAutocomplete(request: AutocompleteRequest): String {
        val response = gelbooruApi.autocomplete(request.term).execute()
        return String(extractBody(response))
    }

    override fun getComments(request: CommentsRequest): String {
        val response = gelbooruApi.getComments(
            postId = request.postId,
            type = request.type.ordinal
        ).execute()
        return String(extractBody(response))
    }

    override fun getTags(request: TagsRequest): String {
        val response = gelbooruApi.getTags(
            tagId = request.id,
            pattern = request.pattern,
            count = request.count,
            order = request.order,
            orderby = request.orderby
        ).execute()
        return String(extractBody(response))
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
