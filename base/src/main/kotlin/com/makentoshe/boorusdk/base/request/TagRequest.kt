package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id

interface TagRequest {
    val id: Id

    companion object {
        fun build(id: Int): TagRequest {
            return object: TagRequest {
                override val id = Id(id)
            }
        }
    }
}