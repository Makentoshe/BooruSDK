package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

interface PostRequest {
    val id: Int
    val type: Type

    companion object {
        fun build(id: Int, type: Type): PostRequest {
            return object : PostRequest {
                override val id = id
                override val type = type
            }
        }
    }
}