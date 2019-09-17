package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.GetCommentRequest
import retrofit2.Response

class GetComment(
    private val danbooruApi: DanbooruApi
): java.util.function.Function<GetCommentRequest, String> {

    override fun apply(t: GetCommentRequest): String {
        return danbooruApi.getComment(
            type = t.type.name.toLowerCase(),
            commentId = t.commentId
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