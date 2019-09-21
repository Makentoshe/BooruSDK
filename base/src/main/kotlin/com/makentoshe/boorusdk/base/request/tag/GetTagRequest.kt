package com.makentoshe.boorusdk.base.request.tag

import com.makentoshe.boorusdk.base.model.Type

open class GetTagRequest(
    override val type: Type,
    val tagId: Int
): TagRequest
