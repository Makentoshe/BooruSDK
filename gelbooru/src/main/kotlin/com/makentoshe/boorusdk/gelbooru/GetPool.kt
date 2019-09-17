package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.GetPoolRequest

internal class GetPool(
    private val gelbooruApi: GelbooruApi
) : Function<GetPoolRequest, String>() {
    override fun apply(t: GetPoolRequest): String {
        val response = gelbooruApi.getPool(t.poolId).execute().extractBody()
        return when(t.type) {
            Type.XML -> html2xml(response)
            Type.JSON -> html2json(response)
            else -> response
        }
    }

    private fun html2xml(html: String): String {
        //TODO
        return html
    }

    private fun html2json(html: String): String {
        //TODO
        return html
    }
}
