package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

open class GetPostsRequest(
    override val type: Type,
    val count: Int?,
    val page: Int?,
    val tags: String?,
    val md5: String?,
    val random: Boolean?,
    val raw: String?
): PostRequest
