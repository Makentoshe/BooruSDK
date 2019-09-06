package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id
import com.makentoshe.boorusdk.base.model.Type

interface TagRequest {
    val id: Id
    val type: Type

    companion object {
        fun build(id: Int, type: Type): TagRequest {
            return object: TagRequest {
                override val id = Id(id)
                override val type = type
            }
        }
    }
}