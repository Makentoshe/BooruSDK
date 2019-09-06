package com.makentoshe.boorusdk.base.request

import java.lang.reflect.Type

interface CommentsRequest {
    val type: Type

    companion object {
        fun build(type: Type): CommentsRequest {
            return object : CommentsRequest {
                override val type = type
            }
        }
    }
}