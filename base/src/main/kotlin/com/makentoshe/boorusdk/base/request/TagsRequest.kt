package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.*

interface TagsRequest {
    val pattern: Term
    val count: Count
    val order: Order?
    val orderby: Orderby?
    val type: Type

    companion object {
        fun build(pattern: String, count: Int = 100, order: Order? = null, orderby: Orderby? = null, type: Type): TagsRequest {
            return object : TagsRequest {
                override val pattern = Term(pattern)
                override val count = Count(count)
                override val order = order
                override val orderby = orderby
                override val type = type
            }
        }
    }
}