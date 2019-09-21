package com.makentoshe.boorusdk.base.request.tag

import com.makentoshe.boorusdk.base.model.Type

open class GetTagsRequest(
    override val type: Type,
    val nameMatches: String? = null,
    val category: String? = null,
    val hideEmpty: Boolean? = null,
    val hasWiki: Boolean? = null,
    val hasArtist: Boolean? = null,
    val order: String? = null
): TagRequest
