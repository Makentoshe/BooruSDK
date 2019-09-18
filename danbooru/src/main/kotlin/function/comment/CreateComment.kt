package function.comment

import DanbooruApi
import com.makentoshe.boorusdk.base.request.NewCommentRequest
import function.Function
import org.jsoup.Jsoup

internal class CreateComment(
    private val danbooruApi: DanbooruApi
): Function<NewCommentRequest, String>() {

    override fun apply(t: NewCommentRequest): String {
        // create a new comment for selected post
        val newCommentResponse = danbooruApi.createComment(
            type = t.type.name.toLowerCase(),
            postId = t.postId,
            body = t.body,
            doNotBumpPost = t.bump?.not() ?: false,
            token = getCsrfToken(t)
        ).execute()
        // get all comments for post
        return danbooruApi.getComments(
            type = t.type.name.toLowerCase(),
            postId = t.postId
        ).execute().extractBody()
    }

    private fun getCsrfToken(request: NewCommentRequest): String {
        val customResponse = danbooruApi.getPostHttp(request.postId).execute().extractBody()
        return Jsoup.parse(customResponse).select("meta[name=csrf-token]").attr("content")
    }
}