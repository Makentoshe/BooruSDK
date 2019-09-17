package function

import DanbooruApi
import com.google.gson.Gson
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.DeleteCommentRequest
import org.jsoup.Jsoup
import retrofit2.Response

internal class DeleteComment(
    private val danbooruApi: DanbooruApi
) : Function<DeleteCommentRequest, Response<ByteArray>>() {

    override fun apply(t: DeleteCommentRequest): Response<ByteArray> {
        val commentJson = getComment(t.commentId)
        val postId = (Gson().fromJson(commentJson, Map::class.java)["post_id"] as Double).toInt()
        val postHttp = getPostHttp(postId)
        val token = Jsoup.parse(postHttp).select("meta[name=csrf-token]").attr("content")
        return danbooruApi.deleteComment(
            commentId = t.commentId,
            type = t.type.name.toLowerCase(),
            token = token
        ).execute()
    }

    private fun getComment(commentId: Int): String {
        return danbooruApi.getComment(
            type = Type.JSON.name.toLowerCase(),
            commentId = commentId
        ).execute().extractBody()
    }

    private fun getPostHttp(postId: Int): String {
        return danbooruApi.getPostHttp(postId).execute().extractBody()
    }

}