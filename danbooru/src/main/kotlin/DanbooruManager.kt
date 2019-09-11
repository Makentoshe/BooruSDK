import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.ParseResult
import com.makentoshe.boorusdk.base.model.TagCategory
import com.makentoshe.boorusdk.base.request.*
import okhttp3.Cookie
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit

open class DanbooruManager(
    protected val danbooruApi: DanbooruApi, protected val cookieStorage: CookieStorage
) : BooruManager {

    override fun getPosts(request: PostsRequest): String {
        val type = request.type.name.toLowerCase()
        val postId = request.id
        val response = if (postId != null) {
            danbooruApi.getPost(type = type, id = postId).execute()
        } else {
            danbooruApi.getPosts(
                type = type,
                count = request.count,
                page = request.page,
                tags = request.tags,
                md5 = request.md5,
                random = request.random,
                raw = request.raw
            ).execute()
        }
        return String(extractBody(response))
    }

    override fun commentPost(request: CommentPostRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAutocomplete(request: AutocompleteRequest): String {
        val response = danbooruApi.getAutocomplete(
            type = request.type.name.toLowerCase(),
            term = request.term.toString()
        ).execute()
        return String(extractBody(response))
    }

    override fun getComments(request: CommentsRequest): String {
        val response = danbooruApi.getComments(
            type = request.type.name.toLowerCase(),
            count = request.limit,
            page = request.page,
            postId = request.postId,
            postsTagMatch = request.postTagMatch,
            creatorName = request.creatorName,
            creatorId = request.creatorId,
            isDeleted = request.isDeleted
        ).execute()
        return String(extractBody(response))
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
        val customResponse = String(extractBody(danbooruApi.newSession().execute()))
        val token = Jsoup.parse(customResponse).select("[name=authenticity_token]").attr("value")
        val response = danbooruApi.login(
            token = token,
            username = request.username,
            password = request.password
        ).execute()
        return response.headers().values("Set-Cookie").run {
            any { it.contains("password_hash") } && any { it.contains("user_name") }
        }
    }

    override fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun extractBody(response: Response<ByteArray>): ByteArray {
        return if (response.isSuccessful) {
            response.body() ?: byteArrayOf()
        } else {
            throw Exception(response.message())
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
    val request = LoginRequest.build(
        username = "Makentoshe",
        password = "1243568790"
    )
    val response = manager.login(request)
    println("")
}