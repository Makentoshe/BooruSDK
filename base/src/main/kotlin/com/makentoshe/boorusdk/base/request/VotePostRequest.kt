package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Id

interface VotePostRequest {
    val id: Id

    companion object {
        fun build(id: Int): VotePostRequest {
            return object : VotePostRequest {
                override val id = Id(id)
            }
        }
    }
}