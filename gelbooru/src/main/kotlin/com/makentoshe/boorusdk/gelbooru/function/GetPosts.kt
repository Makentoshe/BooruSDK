package com.makentoshe.boorusdk.gelbooru.function

import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.GetPostsRequest
import com.makentoshe.boorusdk.gelbooru.GelbooruApi

internal class GetPosts(
    private val gelbooruApi: GelbooruApi
): Function<GetPostsRequest, String>() {

    override fun apply(t: GetPostsRequest): String {
        if (t.count != null && t.count!! < 0) {
            throw IllegalArgumentException("count should not be less 0")
        }
        if (t.page != null && t.page!! < 0) {
            throw IllegalArgumentException("page should not be less 0")
        }
        return gelbooruApi.getPosts(
            type = if (t.type == Type.JSON) 1 else null,
            count = t.count,
            page = t.page,
            tags = t.tags
        ).execute().extractBody()
    }
}
