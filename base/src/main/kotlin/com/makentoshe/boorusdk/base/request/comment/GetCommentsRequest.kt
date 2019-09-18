package com.makentoshe.boorusdk.base.request.comment

import com.makentoshe.boorusdk.base.model.Type

open class GetCommentsRequest(
    override val type: Type,
    /* A number of comments per page */
    val limit: Int? = null,
    /* Comments page starts from 0 */
    val page: Int? = null,
    /* Comments related with specified author name */
    val creatorName: String? = null,
    /* Comments related with specified author id */
    val creatorId: Int? = null,
    /* The comment's post's tags match the given terms */
    val postTagMatch: String? = null,
    /* */
    val isDeleted: Boolean? = null
): CommentRequest