package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.PostsRequest
import retrofit2.Response

class GetPosts(
    private val danbooruApi: DanbooruApi
): java.util.function.Function<PostsRequest, String> {

    override fun apply(t: PostsRequest): String {
        return if (t.id != null) {
            getSinglePost(t)
        } else {
            getListOfPosts(t)
        }
    }

    private fun getListOfPosts(request: PostsRequest): String {
        return danbooruApi.getPosts(
            type = request.type.name.toLowerCase(),
            count = request.count,
            page = request.page,
            tags = request.tags,
            md5 = request.md5,
            random = request.random,
            raw = request.raw
        ).execute().extractBody()
    }

    private fun getSinglePost(request: PostsRequest): String {
        return danbooruApi.getPost(
            type = request.type.name.toLowerCase(),
            id = request.id!!
        ).execute().extractBody()
    }

    private fun Response<ByteArray>.extractBody(): String {
        return if (isSuccessful) {
            String(body() ?: byteArrayOf())
        } else {
            throw Exception(errorBody()?.string() ?: message())
        }
    }
}