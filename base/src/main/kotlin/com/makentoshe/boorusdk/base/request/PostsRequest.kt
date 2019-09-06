package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Count
import com.makentoshe.boorusdk.base.model.Page
import com.makentoshe.boorusdk.base.model.Tags
import com.makentoshe.boorusdk.base.model.Type

interface PostsRequest {
    val count: Count
    val page: Page
    val tags: Tags
    val type: Type

    companion object {
        fun build(count: Int, page: Int, type: Type, vararg tags: String): PostsRequest {
            return object: PostsRequest {
                override val count = Count(count)
                override val page = Page(page)
                override val type = type
                override val tags = Tags(tags.toSet())
            }
        }
    }
}
