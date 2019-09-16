package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

/**
 * @param type represents an outcome string format.
 * @param commentId if not null only the one comment, related to current id will be returned
 * @param postId if not null all comments, related to current id will be returned.
 * @param limit the number of results to return per page.
 * @param page returns the given page.
 * @param creatorName the name of the creator (exact match).
 * @param creatorId the user id of the creator.
 * @param postTagMatch the comment's post's tags match the given terms. Meta-tags not supported.
 * @param isDeleted can
 */
open class CommentsRequest(
    val type: Type,
    val commentId: Int? = null,
    val limit: Int? = null,
    val page: Int? = null,
    val postId: Int? = null,
    val creatorName: String? = null,
    val creatorId: Int? = null,
    val postTagMatch: String? = null,
    val isDeleted: Boolean? = null
)
