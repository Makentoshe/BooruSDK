package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.post.GetPostRequest

internal class GetPost(
    private val danbooruApi: DanbooruApi
): Function<GetPostRequest, String>() {
    override fun apply(t: GetPostRequest): String {
        if (t.postId < 0) {
            throw IllegalArgumentException("postId should not be less 0")
        }
        return danbooruApi.getPost(
            type = t.type.name.toLowerCase(),
            id = t.postId
        ).execute().extractBody()
    }
}