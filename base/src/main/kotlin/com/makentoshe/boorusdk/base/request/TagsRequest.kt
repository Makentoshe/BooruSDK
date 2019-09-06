package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Count
import com.makentoshe.boorusdk.base.model.Order
import com.makentoshe.boorusdk.base.model.Orderby
import com.makentoshe.boorusdk.base.model.Term

interface TagsRequest {
    val pattern: Term
    val count: Count
    val order: Order?
    val orderby: Orderby?

    companion object {
        fun build(pattern: String, count: Int = 100, order: Order? = null, orderby: Orderby? = null): TagsRequest {
            return object : TagsRequest {
                override val pattern = Term(pattern)
                override val count = Count(count)
                override val order = order
                override val orderby = orderby
            }
        }
    }
}