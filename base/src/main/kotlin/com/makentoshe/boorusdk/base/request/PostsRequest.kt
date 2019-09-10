package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.*

interface PostsRequest {
    val type: Type
    val count: Count?
    val page: Page?
    val tags: Tags?
    val md5: Md5?
    val random: Random?
    val raw: String?

    companion object {
        fun build(
            type: Type,
            count: Int? = null,
            page: Int? = null,
            md5: String? = null,
            random: Boolean? = null,
            raw: String? = null,
            vararg tags: String = emptyArray()
        ): PostsRequest {
            return object : PostsRequest {
                override val type = type
                override val count = count?.let(::Count)
                override val page = page?.let(::Page)
                override val tags = if (tags.isEmpty()) null else Tags(tags.toSet())
                override val md5 = md5?.let { Md5(it) }
                override val random = random?.let(::Random)
                override val raw = raw
            }
        }
    }
}
