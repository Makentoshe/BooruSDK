package function.comment

import DanbooruApi
import com.makentoshe.boorusdk.base.request.GetCommentRequest
import function.Function

internal class GetComment(
    private val danbooruApi: DanbooruApi
): Function<GetCommentRequest, String>() {

    override fun apply(t: GetCommentRequest): String {
        return danbooruApi.getComment(
            type = t.type.name.toLowerCase(),
            commentId = t.checkCommentId().commentId
        ).execute().extractBody()
    }

    private fun GetCommentRequest.checkCommentId(): GetCommentRequest {
        if (commentId < 0) throw IllegalArgumentException("Comment id should not be less 0")
        return this
    }
}