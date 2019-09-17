package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.GetPostCommentsRequest

internal class GetPostComments(
    private val danbooruApi: DanbooruApi
): Function<GetPostCommentsRequest, String>() {

    override fun apply(t: GetPostCommentsRequest): String {
        return danbooruApi.getComments(
            type = t.type.name.toLowerCase(),
            postId = t.postId
        ).execute().extractBody()
    }
}