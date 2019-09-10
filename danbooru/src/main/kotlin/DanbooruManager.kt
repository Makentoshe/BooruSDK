import com.makentoshe.boorusdk.base.BooruManager
import com.makentoshe.boorusdk.base.model.ParseResult
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

open class DanbooruManager(
    protected val danbooruApi: DanbooruApi
) : BooruManager {

    override fun getPost(request: PostRequest): String {
        val response = danbooruApi.getPost(
            id = request.id,
            type = request.type.name.toLowerCase()
        ).execute()
        return String(extractBody(response))
    }

    override fun getPosts(request: PostsRequest): String{
        val response = danbooruApi.getPosts(
            type = request.type.name.toLowerCase(),
            count = request.count,
            page = request.page,
            tags = request.tags,
            md5 = request.md5,
            random = request.random,
            raw = request.raw
        ).execute()
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

    override fun getComments(request: CommentsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPostHttp(request: PostRequest): String {
        val response = danbooruApi.getPostHttp(id = request.id).execute()
        return String(extractBody(response))
    }

    override fun getTag(request: TagRequest, parser: (ByteArray) -> ParseResult): ParseResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTags(request: TagsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(request: LoginRequest): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder().client(client).baseUrl("https://danbooru.donmai.us")
                .addConverterFactory(ByteArrayConverterFactory()).build()
            return DanbooruManager(retrofit.create(DanbooruApi::class.java))
        }
    }
}

fun main() {
    val manager = DanbooruManager.build()
    val request = PostRequest.build(3621307, Type.XML)
    val post = manager.getPost(request)
}