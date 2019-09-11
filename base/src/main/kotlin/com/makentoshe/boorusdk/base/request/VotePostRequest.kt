package com.makentoshe.boorusdk.base.request

interface VotePostRequest {
    val id: Int

    companion object {
        fun build(id: Int): VotePostRequest {
            return object : VotePostRequest {
                override val id = id
            }
        }
    }
}