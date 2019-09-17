package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

open class GetCommentRequest(
    override val type: Type,
    /* Id of the comment that must be returned */
    val commentId: Int
): CommentRequest
