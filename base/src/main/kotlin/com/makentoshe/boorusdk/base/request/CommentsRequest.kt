package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id
import com.makentoshe.boorusdk.base.model.Type

interface CommentsRequest {
    val type: Type
    val id: Id?

    companion object {
        fun build(id: Int?, type: Type): CommentsRequest {
            return object : CommentsRequest {
                override val type = type
                override val id = if (id == null) null else Id(id)
            }
        }
    }
}