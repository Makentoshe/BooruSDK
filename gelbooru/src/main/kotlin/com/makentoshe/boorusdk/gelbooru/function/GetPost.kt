package com.makentoshe.boorusdk.gelbooru.function

import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.post.GetPostRequest
import com.makentoshe.boorusdk.gelbooru.GelbooruApi

internal class GetPost(
    private val gelbooruApi: GelbooruApi
): Function<GetPostRequest, String>() {

    override fun apply(t: GetPostRequest): String {
        return gelbooruApi.getPosts(
            type = if (t.type == Type.JSON) 1 else null,
            id = t.postId
        ).execute().extractBody()
    }
}
