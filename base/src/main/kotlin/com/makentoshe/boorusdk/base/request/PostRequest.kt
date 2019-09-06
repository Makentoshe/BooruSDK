package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id
import com.makentoshe.boorusdk.base.model.Type

interface PostRequest {
    val id: Id
    val type: Type

    companion object {
        fun build(id: Int, type: Type): PostRequest {
            return object: PostRequest {
                override val id = Id(id)
                override val type = type
            }
        }
    }
}