package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

open class GetPostRequest(
    override val type: Type,
    val postId: Int
): PostRequest
