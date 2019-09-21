package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.*
import com.makentoshe.boorusdk.base.request.comment.*
import com.makentoshe.boorusdk.base.request.pool.GetPoolRequest
import com.makentoshe.boorusdk.base.request.pool.GetPoolsRequest
import com.makentoshe.boorusdk.base.request.post.GetPostRequest
import com.makentoshe.boorusdk.base.request.post.GetPostsRequest
import com.makentoshe.boorusdk.gelbooru.function.GetPool
import com.makentoshe.boorusdk.gelbooru.function.GetPools
import com.makentoshe.boorusdk.gelbooru.function.GetPost
import com.makentoshe.boorusdk.gelbooru.function.GetPosts
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

open class GelbooruManager(
    protected val gelbooruApi: GelbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    override fun getPost(request: GetPostRequest): String {
        return GetPost(gelbooruApi).apply(request)
    }

    override fun getPosts(request: GetPostsRequest): String {
        return GetPosts(gelbooruApi).apply(request)
    }

    override fun getPostHttp(request: GetPostRequest): String {
        val response = gelbooruApi.getPostHttp(request.postId).execute()
        return String(extractBody(response))
    }

    private val isLoggedIn: Boolean
        get() = cookieStorage.hasCookie("user_id") && cookieStorage.hasCookie("pass_hash")

    override fun login(request: LoginRequest): Boolean {
        val response = gelbooruApi.login(
            username = request.username,
            password = request.password
        ).execute()
        val headers = response.raw().priorResponse?.headers ?: throw IllegalStateException("Login API invalid")
        return headers.values("Set-Cookie").run {
            any { it.contains("user_id") } && any { it.contains("pass_hash") }
        }
    }

    override fun newComment(request: NewCommentRequest): String {
        // check is logged in
        if (!isLoggedIn) throw IllegalStateException("You are not logged in")
        // get http page (type will be ignored)
        val postHttpPage = getPostHttp(GetPostRequest(Type.XML, request.postId))
        // extract csrf token
        val csrfToken = Jsoup.parse(postHttpPage).body().select("#comment_form [name=csrf-token]").attr("value")
        // perform request
        gelbooruApi.commentPost(
            postId = request.postId,
            body = request.body,
            csrfToken = csrfToken,
            postAsAnonymous = if (request.postAsAnonymous == true) "on" else null
        ).execute()
        // get all comments for the post
        // TODO
        return ""//getComments(CommentsRequest.build(request.id, request.type), parser)
    }

    override fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int {
        val response = gelbooruApi.votePostUp(request.id).execute()
        return parser(extractBody(response))
    }

    override fun getAutocomplete(request: AutocompleteRequest): String {
        val response = gelbooruApi.autocomplete(request.term).execute()
        return String(extractBody(response))
    }

    override fun getComment(request: GetCommentRequest): String {
        TODO("This command does not supports for gelbooru")
    }

    override fun getComments(request: GetCommentsRequest): String {
        val response = gelbooruApi.getComments().execute()
        return String(extractBody(response))
    }

    override fun getPostComments(request: GetPostCommentsRequest): String {
        val response = gelbooruApi.getPostComments(
            postId = request.postId
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

    //https://gelbooru.com/public/remove.php?id=2434772&csrf-token=26a41447fce39b329e16a949ef56550749a25ec570400fb5c1534528923d5d11&removecomment=1&post_id=4915517
    override fun deleteComment(request: DeleteCommentRequest): String {
        TODO("This command does not supports for gelbooru")
    }

    private fun extractBody(response: Response<ByteArray>): ByteArray {
        return if (response.isSuccessful) {
            response.body() ?: byteArrayOf()
        } else {
            throw Exception(response.message())
        }
    }

    override fun getPool(request: GetPoolRequest): String {
        return GetPool(gelbooruApi).apply(request)
    }

    override fun getPools(request: GetPoolsRequest): String {
        return GetPools(gelbooruApi).apply(request)
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

    val request = GetPoolRequest(
        type = Type.XML,
        poolId = 46467
    )

    val response = manager.getPool(request)
    println(response)
}
