package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id

interface PostHttpRequest {
    val id: Id

    companion object {
        fun build(postId: Int): PostHttpRequest {
            return object : PostHttpRequest {
                override val id = Id(postId)
            }
        }

        fun build(postId: Id): PostHttpRequest {
            return object : PostHttpRequest {
                override val id = postId
            }
        }
    }
}