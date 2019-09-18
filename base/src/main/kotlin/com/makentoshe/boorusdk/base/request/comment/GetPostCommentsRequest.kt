package com.makentoshe.boorusdk.base.request.comment

import com.makentoshe.boorusdk.base.model.Type

open class GetPostCommentsRequest(
    override val type: Type,
    /* Id of the post which returns the list of comments */
    val postId: Int
): CommentRequest
