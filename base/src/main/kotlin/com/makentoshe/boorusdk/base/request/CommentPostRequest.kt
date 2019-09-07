package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Comment
import com.makentoshe.boorusdk.base.model.Id
import com.makentoshe.boorusdk.base.model.Type

interface CommentPostRequest {
    val id: Id
    val comment: Comment
    val postAsAnonymous: Boolean
    val type: Type

    companion object {
        fun build(id: Int, comment: String, type: Type, postAsAnonymous: Boolean = false): CommentPostRequest {
            return object : CommentPostRequest {
                override val id = Id(id)
                override val comment = Comment(comment)
                override val postAsAnonymous = postAsAnonymous
                override val type = type
            }
        }
    }
}
