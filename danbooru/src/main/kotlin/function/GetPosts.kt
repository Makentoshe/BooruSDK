package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.post.GetPostsRequest

internal class GetPosts(
    private val danbooruApi: DanbooruApi
) : Function<GetPostsRequest, String>() {

    override fun apply(t: GetPostsRequest): String {
        if (t.count != null && t.count!! < 0) {
            throw IllegalArgumentException("count should not be less 0")
        }
        if (t.page != null && t.page!! < 0) {
            throw IllegalArgumentException("page should not be less 0")
        }

        return danbooruApi.getPosts(
            type = t.type.name.toLowerCase(),
            count = t.count,
            page = t.page,
            tags = t.tags,
            md5 = t.md5,
            random = t.random,
            raw = t.raw
        ).execute().extractBody()
    }
}

