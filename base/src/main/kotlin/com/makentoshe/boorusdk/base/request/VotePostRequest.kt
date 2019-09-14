package com.makentoshe.boorusdk.base.request

interface VotePostRequest {
    val id: Int
    val score: Int

    companion object {
        fun build(id: Int, score: Int): VotePostRequest {
            return object : VotePostRequest {
                override val id = id
                override val score = score
            }
        }
    }
}