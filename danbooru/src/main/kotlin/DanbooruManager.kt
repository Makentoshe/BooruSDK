import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.TagCategory
import com.makentoshe.boorusdk.base.request.*
import function.*
import function.DeleteComment
import function.Login
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

open class DanbooruManager(
    protected val danbooruApi: DanbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    override fun getPosts(request: PostsRequest): String {
        return GetPosts(danbooruApi).apply(request)
    }

    override fun newComment(request: NewCommentRequest): String {
        val customResponse = String(extractBody(danbooruApi.getPostHttp(request.postId).execute()))
        val token = Jsoup.parse(customResponse).select("meta[name=csrf-token]").attr("content")
        val newCommentResponse = danbooruApi.createComment(
            type = request.type.name.toLowerCase(),
            postId = request.postId,
            body = request.body,
            doNotBumpPost = request.bump?.not() ?: false,
            token = token
        ).execute()
        val getCommentsResponse = danbooruApi.getComments(
            type = request.type.name.toLowerCase(),
            postId = request.postId
        ).execute()
        return String(extractBody(getCommentsResponse))
    }

    override fun getAutocomplete(request: AutocompleteRequest): String {
        val response = danbooruApi.getAutocomplete(
            type = request.type.name.toLowerCase(),
            term = request.term
        ).execute()
        return String(extractBody(response))
    }

    override fun getComment(request: GetCommentRequest): String {
        return GetComment(danbooruApi).apply(request)
    }

    override fun getComments(request: GetCommentsRequest): String {
        return GetComments(danbooruApi).apply(request)
    }

    override fun getPostComments(request: GetPostCommentsRequest): String {
        return GetPostComments(danbooruApi).apply(request)
    }

    override fun getPostHttp(request: PostsRequest): String {
        val postId = request.id ?: throw IllegalArgumentException("Id value should be defined")
        if (postId < 0) throw IllegalArgumentException("Id value should not be less 0")
        val response = danbooruApi.getPostHttp(id = postId).execute()
        return String(extractBody(response))
    }

    override fun getTags(request: TagsRequest): String {
        val tagId = request.id
        val response = if (tagId != null) {
            danbooruApi.getTag(
                type = request.type.name.toLowerCase(),
                id = tagId
            ).execute()
        } else {
            val hideEmpty = when (request.hideEmpty) {
                true -> "yes"
                false -> "no"
                else -> null
            }
            val hasWiki = when (request.hasWiki) {
                true -> "yes"
                false -> "no"
                else -> null
            }
            val hasArtist = when (request.hasArtist) {
                true -> "yes"
                false -> "no"
                else -> null
            }
            val category = when (request.category) {
                TagCategory.GENERAL -> 0
                TagCategory.ARTIST -> 1
                TagCategory.COPYTIGHT -> 3
                TagCategory.CHARACTER -> 4
                else -> null
            }
            danbooruApi.getTags(
                type = request.type.name.toLowerCase(),
                pattern = request.pattern,
                name = request.name,
                hideEmpty = hideEmpty,
                hasWiki = hasWiki,
                hasArtist = hasArtist,
                order = request.orderby?.name?.toLowerCase(),
                category = category
            ).execute()
        }
        return String(extractBody(response))
    }

    override fun login(request: LoginRequest): Boolean {
        return Login(danbooruApi).apply(request)
    }

    override fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int {
        TODO("Not implemented. For testing this functional need Gold Account permissions")
    }

    override fun deleteComment(request: DeleteCommentRequest): String {
        // todo check is logged in
        val response = DeleteComment(danbooruApi).apply(request)
        return response.isSuccessful.toString()
    }

    private fun extractBody(response: Response<ByteArray>): ByteArray {
        return if (response.isSuccessful) {
            response.body() ?: byteArrayOf()
        } else {
            throw Exception(response.errorBody()?.string() ?: response.message())
        }
    }

    companion object {
        fun build(): DanbooruManager {
            val cookieStorage = SessionCookie()
            val client = OkHttpClient.Builder().cookieJar(cookieStorage).build()
            val retrofit = Retrofit.Builder().client(client).baseUrl("https://danbooru.donmai.us")
                .addConverterFactory(ByteArrayConverterFactory()).build()
            return DanbooruManager(retrofit.create(DanbooruApi::class.java), cookieStorage)
        }
    }
}

fun main() {
    val manager = DanbooruManager.build()

    val result = manager.login(
        LoginRequest.build(
            username = "Makentoshe",
            password = "1243568790"
        )
    )

    println(result)
}
