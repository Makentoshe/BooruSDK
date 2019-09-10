package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

interface CommentsRequest {
    val type: Type
    val postId: Int?
    val page: Int?
    val limit: Int?
    val creatorName: String?
    val creatorId: Int?
    val postTagMatch: String?
    val isDeleted: Boolean?

    companion object {
        fun build(
            type: Type,
            limit: Int? = null,
            page: Int? = null,
            postId: Int? = null,
            creatorName: String? = null,
            creatorId: Int? = null,
            postTagMatch: String? = null,
            isDeleted: Boolean? = null
        ): CommentsRequest {
            return object : CommentsRequest {
                override val type = type
                override val postId = postId
                override val page = page
                override val limit = limit
                override val creatorName = creatorName
                override val creatorId = creatorId
                override val postTagMatch = postTagMatch
                override val isDeleted = isDeleted
            }
        }
    }
}