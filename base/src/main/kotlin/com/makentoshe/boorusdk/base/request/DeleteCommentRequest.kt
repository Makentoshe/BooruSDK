package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

open class DeleteCommentRequest(
    val commentId: Int,
    val type: Type
)