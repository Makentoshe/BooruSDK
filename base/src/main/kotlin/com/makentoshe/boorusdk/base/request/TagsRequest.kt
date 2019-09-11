package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Order
import com.makentoshe.boorusdk.base.model.Orderby
import com.makentoshe.boorusdk.base.model.TagCategory
import com.makentoshe.boorusdk.base.model.Type

interface TagsRequest {
    val type: Type
    val id: Int?
    val pattern: String?
    val count: Int?
    val order: Order?
    val orderby: Orderby?
    val name: String?
    val hasArtist: Boolean?
    val hasWiki: Boolean?
    val hideEmpty: Boolean?
    val category: TagCategory?

    companion object {
        fun build(
            type: Type,
            pattern: String? = null,
            count: Int? = null,
            order: Order? = null,
            orderby: Orderby? = null,
            name: String? = null,
            hasArtist: Boolean? = null,
            hasWiki: Boolean? = null,
            hideEmpty: Boolean? = null,
            category: TagCategory? = null,
            id: Int? = null
        ): TagsRequest {
            return object : TagsRequest {
                override val id = id
                override val pattern = pattern
                override val count = count
                override val order = order
                override val orderby = orderby
                override val type = type
                override val name = name
                override val hasArtist = hasArtist
                override val hideEmpty = hideEmpty
                override val category = category
                override val hasWiki = hasWiki
            }
        }
    }
}
