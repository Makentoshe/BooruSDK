package function.comment

import DanbooruApi
import com.google.gson.Gson
import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.comment.DeleteCommentRequest
import cookie.CookieStorage
import function.Function
import org.jsoup.Jsoup

internal class DeleteComment(
    private val danbooruApi: DanbooruApi,
    private val sessionCookie: CookieStorage
) : Function<DeleteCommentRequest, String>() {

    private val isLoggedIn: Boolean
    get() = sessionCookie.hasCookie("user_name") && sessionCookie.hasCookie("password_hash")

    override fun apply(t: DeleteCommentRequest): String {
        if (!isLoggedIn) {
            throw IllegalStateException("Should be logged in")
        }
        val commentJson = getComment(t.commentId)
        val postId = (Gson().fromJson(commentJson, Map::class.java)["post_id"] as Double).toInt()
        val postHttp = getPostHttp(postId)
        val token = Jsoup.parse(postHttp).select("meta[name=csrf-token]").attr("content")
        return danbooruApi.deleteComment(
            commentId = t.commentId,
            type = t.type.name.toLowerCase(),
            token = token
        ).execute().extractBody()
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