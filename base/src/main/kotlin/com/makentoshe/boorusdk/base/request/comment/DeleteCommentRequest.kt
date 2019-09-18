package com.makentoshe.boorusdk.base.request.comment

import com.makentoshe.boorusdk.base.model.Type

open class DeleteCommentRequest(
    override val type: Type,
    /* Id of the comment that must be deleted */
    val commentId: Int
): CommentRequest