package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

open class NewCommentRequest(
    val type: Type,
    val postId: Int,
    val body: String,
    val postAsAnonymous: Boolean? = null,
    val bump: Boolean? = null
)
